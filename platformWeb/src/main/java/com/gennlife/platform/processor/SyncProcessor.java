package com.gennlife.platform.processor;

import com.gennlife.darren.excel.ExcelFileExtension;
import com.gennlife.darren.excel.ExcelSheetHelper;
import com.gennlife.darren.excel.ExcelTitle;
import com.gennlife.darren.excel.ExcelWorkbookHelper;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.dao.OrgMapper;
import com.gennlife.platform.dao.SyRoleMapper;
import com.gennlife.platform.dao.SyUserMapper;
import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.Role;
import com.gennlife.platform.model.Uprofession;
import com.gennlife.platform.model.User;
import com.gennlife.platform.util.DepartDecide;
import com.gennlife.platform.util.FileUploadUtil;
import com.gennlife.platform.util.ParamUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gennlife.darren.collection.string.StringTools.nullStringIfBlank;
import static com.gennlife.darren.controlflow.for_.Foreach.foreach;
import static com.gennlife.platform.util.FileUploadUtil.writeResultFile;
import static com.gennlife.platform.util.GStringUtils.checkEmail;
import static com.gennlife.platform.util.GStringUtils.getDefaultPasswd;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class SyncProcessor {

    private static Logger _LOG = LoggerFactory.getLogger(SyncProcessor.class);

    public static final String ROOT_DEPARTMENT_NAME = "医院";

    public synchronized String importStaffsFromExcel(MultipartFile file, User currentUser) {
        try {
            final Workbook wb = ExcelWorkbookHelper.read(file.getInputStream(), ExcelFileExtension.XLSX);
            final Sheet sheet = wb.getSheetAt(0);
            final List<Staff> lines = ExcelSheetHelper.loadRequestObjects(sheet, Staff.class);
            return importStaffs(lines, currentUser);
        } catch (Exception e) {
            _LOG.error(e.getLocalizedMessage(), e);
            return ParamUtils.errorParam(e.getLocalizedMessage());
        }
    }

    public synchronized String importLabsFromExcel(MultipartFile file, User currentUser) {
        try {
            final Workbook wb = ExcelWorkbookHelper.read(file.getInputStream(), ExcelFileExtension.XLSX);
            final Sheet sheet = wb.getSheetAt(0);
            final String rootId = ExcelSheetHelper.loadRequestObjects(sheet, _RootIdLine.class).get(0).rootId;
            final List<Department> lines = ExcelSheetHelper.loadRequestObjects(sheet, Department.class);
            return importLabs(lines, rootId, currentUser);
        } catch (Exception e) {
            _LOG.error(e.getLocalizedMessage(), e);
            return ParamUtils.errorParam(e.getLocalizedMessage());
        }
    }

    public static class Staff {
        @ExcelTitle("工号")
        public String id;
        @ExcelTitle("姓名")
        public String name;
        @ExcelTitle("邮箱")
        public String mail;
        @ExcelTitle("手机")
        public String phone;
        @ExcelTitle("职称")
        public String title;
        @ExcelTitle("职务")
        public String post;
        @ExcelTitle("状态")
        public String state;
        @ExcelTitle("生效时间")
        public String actTime;
        @ExcelTitle("失效时间")
        public String inactTime;
        @ExcelTitle("所属科室编号")
        public String departmentId;
        @ExcelTitle("所属部门")
        public String departmentName;
        @Override
        public String toString() {
            return String.join(",", id, name, mail, phone, title, post, state, actTime, inactTime, departmentId, departmentName);
        }
    }

    public static synchronized String importStaffs(List<Staff> staffs, User currentUser) throws IOException {
        requireNonNull(staffs);
        requireNonNull(currentUser);
        final SyUserMapper userDao = AllDao.getInstance().getSyUserDao();
        final SyRoleMapper roleDao = AllDao.getInstance().getSyRoleDao();
        final OrgMapper orgDao = AllDao.getInstance().getOrgDao();
        final Set<String> titles = new HashSet<>(new Uprofession().getUprofession());
        final String orgId = currentUser.getOrgID();
        final String orgName = currentUser.getOrg_name();
        final String timeStr = _DATE_FORMAT.format(new Date());
        final List<String> results = new LinkedList<>();
        final Role role = roleDao.getLabMember(orgId);
        final Map<String, Lab> labsById = orgDao.getLabs(orgId)
            .stream()
            .collect(toMap(Lab::getLabID, identity()));
        final Map<String, Staff> srcsById = new HashMap<>();
        final Map<String, User> usersById = new HashMap<>();
        final Set<String> mails = new HashSet<>();
        for (final Staff src : staffs) {
            final String id = nullStringIfBlank(src.id);
            final String name = nullStringIfBlank(src.name);
            final String mail = nullStringIfBlank(src.mail);
            final String phone = nullStringIfBlank(src.phone);
            final String title = nullStringIfBlank(src.title);
            final String post = nullStringIfBlank(src.post);
            final String state = nullStringIfBlank(src.state);
            final String actTime = nullStringIfBlank(src.actTime);
            final String inactTime = nullStringIfBlank(src.inactTime);
            final String departmentId = nullStringIfBlank(src.departmentId);
            if (id.isEmpty()) {
                results.add(src + ",失败,工号为空");
                continue;
            }
            if (usersById.containsKey(id)) {
                results.add(src + ",失败,工号重复");
                continue;
            }
            if (name.isEmpty()) {
                results.add(src + ",失败,姓名为空");
                continue;
            }
            if (!checkEmail(mail)) {
                results.add(src + ",失败,邮箱格式不正确");
                continue;
            }
            if (mails.contains(mail)) {
                results.add(src + ",失败,邮箱重复");
                continue;
            }
            if (!title.isEmpty() && !titles.contains(title)) {
                results.add(src + ",失败,职称错误");
                continue;
            }
            switch (state) {
                case "长期有效":
                case "禁用":
                    // OK
                    break;
                case "定期有效": {
                    final Date actDate;
                    final Date inactDate;
                    try {
                        actDate = _DATE_FORMAT.parse(actTime);
                        inactDate = _DATE_FORMAT.parse(inactTime);
                    } catch (ParseException e) {
                        results.add(src + ",失败,失效时间日期格式不对 格式应该为 " + _DATE_FORMAT_STRING);
                        continue;
                    }
                    if (actDate.after(inactDate)) {
                        results.add(src + ",失败,生效时间不能小于失效时间");
                        continue;
                    }
                    break;
                }
                default:
                    results.add(src + ",失败,状态名称错误 请从：长期有效、定期有效、禁用中选择");
                    continue;
            }
            final Lab lab = labsById.get(departmentId);
            if (lab == null) {
                results.add(src + ",失败,所属部门不存在");
                continue;
            }
            final User user = new User();
            {
                user.setLabID(departmentId);
                user.setLab_name(lab.getLab_name());
                user.setUnumber(id);
                user.setPwd(getDefaultPasswd());
                user.setUptime(timeStr);
                user.setOrgID(orgId);
                user.setOrg_name(orgName);
                user.setCtime(timeStr);
                user.setTelphone(phone);
                user.setUemail(mail);
                user.setUname(name);
                user.setUposition(post);
                user.setUprofession(title);
                user.setEffective_time(actTime);
                user.setFailure_time(inactTime);
                user.setStatus(state);
            }
            usersById.put(id, user);
            srcsById.put(id, src);
            mails.add(mail);
        }
        usersById.forEach((id, user) -> {
            final Staff src = srcsById.get(id);
            final User oldUser = userDao.getUserByUnumber(id, orgId);
            if (oldUser != null) {
                // update
                user.setUid(null);
                if (!user.getUemail().equals(oldUser.getUemail())) {
                    final int mailCount = userDao.existEmail(user.getUemail());
                    if (mailCount > 0) {
                        results.add(src + ",失败,更新后的email是存在的");
                        return;  // continue
                    }
                }
                final int updateCount = userDao.updateUserByUnumber(user);
                if (updateCount > 0) {
                    try {
                        roleDao.insertUserRoleRelation(role.getRoleid(), oldUser.getUid());
                        results.add(src + ",成功,更新成功");
                    } catch (DataIntegrityViolationException e) {
                        results.add(src + ",失败,数据存在问题");
                    }
                } else {
                    results.add(src + ",失败,更新失败");
                }
            } else {
                // insert
                user.setUid(randomUUID().toString());
                try {
                    final int userCount = userDao.insertOneUser(user);
                    if (userCount <= 0) {
                        results.add(src + ",失败,插入失败");
                        return;  // continue
                    }
                    final int relationCount = roleDao.insertUserRoleRelation(role.getRoleid(), user.getUid());
                    if (relationCount <= 0) {
                        results.add(src + ",失败,插入失败");
                        return;  // continue
                    }
                    results.add(src + ",成功,插入成功");
                }catch (DataIntegrityViolationException e){
                    results.add(src + ",失败,数据存在问题");
                }
            }
        });

        final File outputFile = new File(FileUploadUtil.tempPath + currentUser.getOrg_name() + "导入人员历史.csv");
        return writeResultFile(results, outputFile);
    }

    public static class Department {
        @ExcelTitle("科室编号")
        public String id;
        @ExcelTitle("科室名称")
        public String name;
        @ExcelTitle("部门类型")
        public String type;
        @ExcelTitle("DEPTTYPENAME")
        public String originalType;
        @ExcelTitle("上级科室编号")
        public String parentId;
        @ExcelTitle("上级科室名称")
        public String parentName;
        @Override
        public String toString() {
            return String.join(",", id, name, parentId, parentName, type, originalType);
        }
    }

    public static synchronized String importLabs(List<Department> departments, String rootId, User currentUser) throws IOException {
        requireNonNull(departments);
        requireNonNull(currentUser);
        requireNonNull(rootId);
        final List<String> results = new LinkedList<>();
        final String orgId = currentUser.getOrgID();
        final String uid = currentUser.getUid();
        final String timeStr = _DATE_FORMAT.format(new Date());
        final Map<String, Department> srcsById = new HashMap<>();
        final Map<String, Department> srcsByParentId = new HashMap<>();
        for (final Department src : departments) {
            final String id = nullStringIfBlank(src.id);
            final String name = nullStringIfBlank(src.name);
            final String parentId = nullStringIfBlank(src.parentId);
            final String type = nullStringIfBlank(src.type);
            if (id.isEmpty()) {
                results.add(src + ",失败,科室编号为空");
                continue;
            }
            if (id.equals(rootId)) {
                results.add(src + ",失败," + rootId + "为根科室编号");
                continue;
            }
            if (srcsById.containsKey(id)) {
                results.add(src + ",失败,同编号的科室被已定义过");
                continue;
            }
            if (name.isEmpty()) {
                results.add(src + ",失败,科室名称为空");
                continue;
            }
            if (name.equals(ROOT_DEPARTMENT_NAME)) {
                results.add(src + ",失败,科室名称不能为" + ROOT_DEPARTMENT_NAME);
                continue;
            }
            if (name.length() > 30) {
                results.add(src + ",失败,科室名称大于30个字符");
                continue;
            }
            if (parentId.isEmpty()) {
                results.add(src + ",失败,上级科室编号为空");
                continue;
            }
            if (type.isEmpty()) {
                results.add(src + ",失败,部门类型为空");
                continue;
            }
            srcsById.put(id, src);
            if (!srcsByParentId.containsKey(parentId)) {
                srcsByParentId.put(parentId, src);  // reserve first line
            }
        }
        final Map<String, Lab> importedLabsById = AllDao.getInstance().getOrgDao().getLabs(orgId)
            .stream()
            .collect(toMap(Lab::getLabID, identity()));
        final Map<String, Lab> labsById = new HashMap<>(importedLabsById);
        {
            final Lab rootLab = new Lab();
            {
                rootLab.setOrgID(orgId);
                rootLab.setLabID(rootId);
                rootLab.setLab_name(ROOT_DEPARTMENT_NAME);
                rootLab.setLab_parent(orgId);
                rootLab.setLab_level(1);
                rootLab.setAdd_user(uid);
                rootLab.setAdd_time(timeStr);
                rootLab.setDepart_name("行政管理类");
            }
            labsById.put(rootId, rootLab);
        }
        final Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        foreach(srcsById, (id, src) -> {
            final String name = nullStringIfBlank(src.name);
            final String parentId = nullStringIfBlank(src.parentId);
            final String type = nullStringIfBlank(src.type);
            final Lab lab = new Lab();
            {
                lab.setOrgID(orgId);
                lab.setLabID(id);
                lab.setLab_name(name);
                lab.setAdd_user(uid);
                lab.setAdd_time(timeStr);
                lab.setLab_parent(parentId);
                lab.setDepart_name(type);
            }
            labsById.put(id, lab);
            graph.addVertex(id);
            graph.addVertex(parentId);
            graph.addEdge(parentId, id);
        });
        final Iterator<String> it = new TopologicalOrderIterator<>(graph);
        while (it.hasNext()) {
            final String id = it.next();
            if (id.equals(rootId)) {
                continue;
            }
            boolean accepted = false;
            CHECK: {
                final Lab lab = labsById.get(id);
                if (lab == null) {
                    results.add(srcsByParentId.get(id) + ",失败,上级科室编号不存在");
                    break CHECK;
                }
                final Department src = srcsById.get(id);
                final Lab parentLab = labsById.get(lab.getLab_parent());
                if (parentLab == null) {
                    results.add(src + ",失败,上级科室编号不存在");
                    break CHECK;
                }
                if (DepartDecide.decide(lab.getDepart_name(), parentLab.getDepart_name())) {
                    results.add(src + ",失败,上级科室类型为: " + parentLab.getDepart_name() + " 当前科室类型为: " + lab.getDepart_name() + " 不符合科室类型关系约束");
                    break CHECK;
                }
                accepted = true;
                lab.setLab_name(parentLab.getDepart_name());
            }
            if (!accepted) {
                if (!importedLabsById.containsKey(id)) {
                    labsById.remove(id);
                }
            }
        }
        labsById.remove(rootId);
        foreach(labsById, (id, lab) -> {
            if (importedLabsById.containsKey(id)) {
                // update
                final int count = AllDao.getInstance().getOrgDao().updateLabInfoByNameWithLab(lab);
                if ("一线临床类".equals(lab.getDepart_name()) || "行政管理类".equals(lab.getDepart_name())) {
                    AllDao.getInstance().getSyResourceDao().deleteLabsReource(new String[] {lab.getLabID()});
                }
                if ("业务管理类".equals(lab.getDepart_name())) {
                    LaboratoryProcessor.addResource(lab);
                }
                results.add(srcsById.get(id) + (count > 0 ? ",成功,更新成功" : ",失败,无法更新科室"));
            } else {
                // insert
                final int count = AllDao.getInstance().getOrgDao().insertOneLab(lab);
                LaboratoryProcessor.addResource(lab);
                results.add(srcsById.get(id) + (count > 0 ? ",成功,更新成功" : ",失败,无法插入科室"));
            }
        });
        final File outputFile = new File(FileUploadUtil.tempPath + currentUser.getOrg_name() + "导入科室历史.csv");
        return writeResultFile(results, outputFile);
    }

    private static class _RootIdLine {
        @ExcelTitle("根科室编号")
        String rootId;
    }

    private static final String _DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat _DATE_FORMAT = new SimpleDateFormat(_DATE_FORMAT_STRING);

}
