package com.gennlife.platform.util;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.configuration.FileBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.Role;
import com.gennlife.platform.model.Uprofession;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.LaboratoryProcessor;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chen-song on 16/9/6.
 */
@Component
public class FileUploadUtil implements InitializingBean {
    public static final Object Lock = new Object();
    public static Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
    public static Gson gson = GsonUtil.getGson();
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    //文件位置，注意是绝对路径
    public static String tempPath = "/home/tomcat_demo2_web/update/";
    @Autowired
    private FileBean fileBean;


    public static String handleStaff(List<String> fileList, User user) throws Exception {
        if (fileList.size() == 0) {
            return ParamUtils.errorParam("文件为空");
        } else {
            List<String> list = importsStaffs(fileList, user.getOrgID(), user);
            //创建最近导入模板
            File orgIDImportResultFile = new File(tempPath + user.getOrg_name() + "导入人员历史.csv");
            return writeResultFile(list, orgIDImportResultFile);
        }

    }


    public static String writeResultFile(List<String> list, File orgIDImportResultFile) throws IOException {
        if (!orgIDImportResultFile.exists()) {
            orgIDImportResultFile.createNewFile();
        }
        PrintWriter fw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(orgIDImportResultFile), "GB2312")));
        int insert = 0;
        int fail = 0;
        int update = 0;
        for (String line : list) {
            String[] data = line.split(",");
            if (data.length >= 4) {
                if ("成功".equals(data[data.length - 2])) {
                    if ("更新成功".equals(data[data.length - 1])) {
                        update++;
                    } else {
                        insert++;
                    }
                } else if ("失败".equals(data[data.length - 2])) {
                    fail++;
                }
            }
            fw.write(line + "\n");
        }
        fw.flush();
        fw.close();
        Map<String, Integer> map = new HashMap<>();
        map.put("update", update);
        map.put("insert", insert);
        map.put("fail", fail);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(map);
        return gson.toJson(resultBean);
    }
    //处理文件
    public static List<String> importsStaffs(List<String> fileList, String orgID, User user) throws Exception {
        synchronized (Lock) {
            List<String> strList = fileList;
            List<String> srcList = new LinkedList<>();
            Map<String, Integer> map = new HashMap<>();
            String termLine = strList.get(0);
            termLine = termLine.trim();
            String[] data = termLine.split(",");
            for (int index = 0; index < data.length; index++) {
                map.put(data[index], index);
            }
            String term1Name = "工号";
            String term2Name = "姓名";
            String term3Name = "邮箱";
            String term4Name = "所属部门";
            Integer unumberIndex = map.get(term1Name);
            Integer nameIndex = map.get(term2Name);
            Integer emailIndex = map.get(term3Name);
            Integer labIndex = map.get(term4Name);
            Integer telIndex = map.get("手机");
            Integer uprofessionIndex = map.get("职称");
            Integer upositionIndex = map.get("职务");
            Integer statusIndex=map.get("状态");
            Integer effectiveIndex = map.get("生效时间");
            Integer failureIndex=map.get("失效时间");
            List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
            //获取labs
            if (unumberIndex == null) {
                for (int index = 1; index < strList.size(); index++) {
                    String line = strList.get(index);
                    srcList.add(line + ",失败,缺少工号列");
                }
                return srcList;
            }
            if (nameIndex == null) {
                for (int index = 1; index < strList.size(); index++) {
                    String line = strList.get(index);
                    srcList.add(line + ",失败,缺少姓名列");
                }
                return srcList;
            }
            if (emailIndex == null) {
                for (int index = 1; index < strList.size(); index++) {
                    String line = strList.get(index);
                    srcList.add(line + ",失败,缺少邮箱列");
                }
                return srcList;
            }
            if (labIndex == null) {
                for (int index = 1; index < strList.size(); index++) {
                    String line = strList.get(index);
                    srcList.add(line + ",失败,缺少所属部门列");
                }
                return srcList;
            }
            if (statusIndex == null) {
                for (int index = 1; index < strList.size(); index++) {
                    String line = strList.get(index);
                    srcList.add(line + ",失败,缺少状态序列");
                }
                return srcList;
            }
            List<User> userList = new LinkedList<>();
            List<String> lineList = new LinkedList<>();
            Role role = AllDao.getInstance().getSyRoleDao().getLabMember(orgID);
            //角色
            for (int index = 1; index < strList.size(); index++) {
                String line = strList.get(index);
                String[] terms = line.split(",");
                if (terms.length < data.length) {
                    srcList.add(line + ",失败,缺少数据");
                    continue;
                } else {
                    //工号,姓名,邮箱,手机,职称,职务,所属部门 状态 生效时间 失效时间
                    String number = terms[unumberIndex].replaceAll("\n", "");//工号
                    String name = terms[nameIndex].replaceAll("\n", "");//姓名
                    String lab_name = terms[labIndex].replaceAll("\n", "");
                    String email = terms[emailIndex].replaceAll("\n", "");//邮箱
                    //---
                    if (name == null || name.equals("")) {
                        srcList.add(line + ",失败,姓名为空");
                        continue;
                    }
                    if (!StringUtils.isEmpty(email)) {
                        if (!GStringUtils.checkEmail(email)) {
                            srcList.add(line + ",失败,邮箱格式不正确");
                            continue;
                        }
                    }

                    if (number == null || number.equals("")) {
                        srcList.add(line + ",失败,工号为空");
                        continue;
                    }
                    String tel = "";
                    if (telIndex != null) {
                        tel = terms[telIndex];
                        if (!StringUtils.isEmpty(tel)) {
                            try {
                                BigDecimal db = new BigDecimal(tel.replaceAll("[\r\n\t ]", ""));
                                tel = db.toPlainString();
                            } catch (Exception e) {
                                srcList.add(line + ",失败,号码错误");
                                continue;
                            }
                        }
                    }
                    String uposition = "";
                    if (upositionIndex != null) {
                        uposition = terms[upositionIndex];
                    }
                    String uprofession = "";
                    if (uprofessionIndex != null) {
                        uprofession = terms[uprofessionIndex];
                        Uprofession up=new Uprofession();
                        if(!StringUtils.isEmpty(uposition) && !up.getUprofession().contains(uprofession)){
                            srcList.add(line + ",失败,职称错误");
                            continue;
                        }
                    }
                    String effective_time="";

                    String failure_time="";

                    String status="";
                    if(statusIndex!=null){
                        status=terms[statusIndex];
                        if("长期有效".equals(status)){
                            status="长期有效";
                        }else if("定期有效".equals(status)){
                            status="定期有效";
                            if(failureIndex!=null){
                                failure_time=terms[failureIndex];
                                logger.info("---------------"+failure_time);
                                if(StringUtils.isEmpty(failure_time)){
                                    srcList.add(line + ",失败,失效时间不能为空");
                                    failure_time="";
                                    continue;
                                }
                                try{
                                    Date failDate=new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(failure_time);
                                    failure_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(failDate);
                                }catch (Exception e){
                                    srcList.add(line + ",失败,失效时间日期格式不对 格式应该为 2020/1/1 00:00");
                                    failure_time="";
                                    continue;
                                }
                            }
                            if(effectiveIndex!=null){
                                effective_time=terms[effectiveIndex];
                                if(StringUtils.isEmpty(effective_time)){
                                    srcList.add(line + ",失败,生效时间不能为空");
                                    failure_time="";
                                    continue;
                                }
                                try{
                                    logger.info("---------------"+effective_time);
                                    //这里需要处理这种形式
                                    Date effDate=new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(effective_time);
                                    Date failDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(failure_time);
                                    if(effDate.after(failDate)){
                                        srcList.add(line + ",失败,生效时间 不能小于失效时间");
                                        failure_time="";
                                        effective_time="";
                                        continue;
                                    }
                                    effective_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(effDate);
                                }catch (Exception e){
                                    srcList.add(line + ",失败,生效时间日期格式不对 格式应该为 2020/1/1 00:00");
                                    effective_time="";
                                    continue;
                                }
                            }
                        }else if("禁用".equals(status)){
                            status="禁用";
                        }else{
                            srcList.add(line + ",失败,状态名称错误 请从：长期有效、定期有效、禁用中选择");
                            continue;
                        }
                    }
                    User addUser = new User();
                    Lab lab = getLabByName(lab_name, labs);
                    if (lab == null && !"医院".equals(lab_name)) {
                        srcList.add(line + ",失败,所属部门不存在");
                        continue;
                    } else if ("医院".equals(lab_name)) {
                        addUser.setLabID(user.getOrgID());
                        addUser.setLab_name(user.getOrg_name());
                    } else if (lab != null) {
                        addUser.setLabID(lab.getLabID());
                        addUser.setLab_name(lab_name);
                    }
                    addUser.setUnumber(number);
                    addUser.setPwd(GStringUtils.getDefaultPasswd());
                    UUID uuid = UUID.randomUUID();
                    addUser.setUid(uuid.toString());
                    addUser.setUptime(LogUtils.getStringTime());
                    addUser.setOrgID(user.getOrgID());
                    addUser.setOrg_name(user.getOrg_name());
                    addUser.setCtime(LogUtils.getStringTime());
                    addUser.setTelphone(tel);
                    addUser.setUemail(email);
                    addUser.setUname(name);
                    addUser.setUposition(uposition);
                    addUser.setUprofession(uprofession);
                    addUser.setEffective_time(effective_time);
                    addUser.setFailure_time(failure_time);
                    addUser.setStatus(status);
                    userList.add(addUser);
                    lineList.add(line);
                }
            }
            for (int index = 0; index < userList.size(); index++) {
                User addUser = userList.get(index);
                String line = lineList.get(index);
                User exUser = AllDao.getInstance().getSyUserDao().getUserByUnumber(addUser.getUnumber(), addUser.getOrgID());
                if (exUser != null) {//更新
                    addUser.setUid(null);//uid 不更新
                    if (!StringUtils.isEmpty(addUser.getUemail()) && !addUser.getUemail().equals(exUser.getUemail())) {//邮箱不一样,需要检查
                        int emailCounter = AllDao.getInstance().getSyUserDao().existEmail(addUser.getUemail());
                        if (emailCounter >= 1) {
                            srcList.add(line + ",失败,更新后的email是存在的");
                        } else {
                            int counter = AllDao.getInstance().getSyUserDao().updateUserByUnumber(addUser);
                            if (counter >= 1) {
                                try {
                                    AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(role.getRoleid(), exUser.getUid());
                                }catch (DataIntegrityViolationException e){
                                    srcList.add(line + ",失败,数据存在问题");
                                    continue;
                                }
                                srcList.add(line + ",成功,更新成功");
                            } else {
                                srcList.add(line + ",失败,更新失败");
                            }
                        }
                    } else {
                        int counter=0;
                        try {
                            counter = AllDao.getInstance().getSyUserDao().updateUserByUnumber(addUser);
                        }catch (DataIntegrityViolationException e){
                            srcList.add(line + ",失败,数据存在问题");
                            continue;
                        }

                        if (counter >= 1) {
                            srcList.add(line + ",成功,更新成功");
                        } else {
                            srcList.add(line + ",失败,更新失败");
                        }
                    }
                } else {//插入
                    //判定当前文件中是否存在相同工号
                    boolean exUserFile = getUserByUnumber(addUser, userList);
                    if (exUserFile) {
                        srcList.add(line + ",失败,文件存在相同的email");
                    } else {
                        int counter=0;
                        try {
                            counter = AllDao.getInstance().getSyUserDao().insertOneUser(addUser);
                            Role role1 = AllDao.getInstance().getSyRoleDao().getLabMember(addUser.getOrgID());//科室成员
                            AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(role1.getRoleid(), addUser.getUid());
                        }catch (DataIntegrityViolationException e){
                            srcList.add(line + ",失败,数据存在问题");
                            continue;
                        }

                        if (counter >= 1) {
                            srcList.add(line + ",成功,插入成功");
                        } else {
                            srcList.add(line + ",失败,插入失败");
                        }
                    }

                }
            }
            return srcList;
        }

    }

    /**
     * @param user
     * @param userList
     * @return
     */
    private static boolean getUserByUnumber(User user, List<User> userList) {
        for (User exUnumber : userList) {
            if (!exUnumber.equals(user)) {
                if (!StringUtils.isEmpty(user.getUemail()) && user.getUemail().equals(exUnumber.getUemail())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String handleLab(List<String> fileList, User user) throws Exception {
        List<String> list = importLabs(fileList, user.getOrgID(), user.getUid());
        File orgIDImportResultFile = new File(tempPath + user.getOrg_name() + "导入科室历史.csv");
        return writeResultFile(list, orgIDImportResultFile);
    }

    /**
     * 设置临时存贮目录
     */
    public void setTmpPath(String tmppath) {
        this.tempPath = tmppath;
    }


    /**
     * 通过文件列表,将导入科室信息导入到orgID当中
     *
     * @param fileList
     * @param orgID
     */
    public static List<String> importLabs(List<String> fileList, String orgID, String uid) throws Exception {
        synchronized (Lock) {

            List<String> strList = fileList;
            List<String> srcList = new LinkedList<>();
            List<Lab> newList = new LinkedList<>();
            Map<String,String> partNameMap=new HashMap<>();// key:departName value :parentName
            for (String str : strList) {
                str = str.replace("\r", "");
                String[] data = str.split(",");
                if (data.length >= 3) {
                    if ("科室名称".equals(data[0].trim())) {
                        srcList.add(str);
                    } else {
                        String name = data[0].trim();
                        String parentName = data[1].trim();
                        String departName = data[2].trim();
                        String partName=null;

                        if(partNameMap.containsKey(parentName)){
                            partName=partNameMap.get(parentName);
                        }else{
                            partName = AllDao.getInstance().getOrgDao().getDepartNameByParentName(parentName);
                        }

                        if("医院".equals(parentName)){
                            partName="行政管理类";
                        }

                        if(StringUtils.isEmpty(partName)){
                            srcList.add(str + ",失败,科室不存在");
                            continue;
                        }
                        if (StringUtils.isEmpty(name)) {
                            srcList.add(str + ",失败,科室名称为空");
                            continue;
                        } else if (name.length() > 30) {
                            srcList.add(str + ",失败,科室名称大于30个字符");
                            continue;
                        } else if (StringUtils.isEmpty(parentName)) {
                            srcList.add(str + ",失败,上级科室名称为空");
                            continue;
                        } else if (parentName.length() > 30) {
                            srcList.add(str + ",失败,上级科室名称大于30个字符");
                            continue;
                        } else if (parentName.equals(name)) {
                            srcList.add(str + ",失败,上级科室与当前科室名称一致");
                            continue;
                        } else if(StringUtils.isEmpty(departName)){
                            srcList.add(str + ",失败,部门类别为空");
                            continue;
                        } else if(DepartDecide.decide(departName,partName)){
                            srcList.add(str + ",失败,上级科室类型为: "+partName+" 当前科室类型为: "+departName+ " 不符合科室类型关系约束");
                            continue;
                        } else {
                            partNameMap.put(name,departName);
                            Lab lab = new Lab();
                            lab.setOrgID(orgID);
                            lab.setLab_level(1);
                            lab.setLab_parent(orgID);
                            lab.setLab_name(name);
                            lab.setLab_parentName(parentName);
                            lab.setAdd_user(uid);
                            lab.setDepart_name(departName);//
                            lab.setAdd_time(time.format(new Date()));
                            lab.status = "未处理";
                            newList.add(lab);
                        }
                    }
                } else {
                    srcList.add(str + ",失败,缺少数据");
                }
            }
            Set<String> names = new HashSet<>();
            List<Lab> invertedList = new LinkedList<>();
            for (int i = newList.size() - 1; i >= 0; i--) {//反向遍历去重
                Lab lab = newList.get(i);
                if (!names.contains(lab.getLab_name())) {
                    names.add(lab.getLab_name());
                    invertedList.add(lab);
                } else {
                    srcList.add(lab.getSrcLog() + ",失败,被后面最新的覆盖");
                }
            }
            newList.clear();
            List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
            for (Lab lab : invertedList) {
                Lab ex1Lab = getLabByName(lab.getLab_parentName(), invertedList);//在文件中查找
                Lab ex2Lab = getLabByName(lab.getLab_parentName(), labs);//在现在数据库中查找
                if (ex1Lab == null && ex2Lab == null) {//父节点不存在
                    lab.setLab_parentName("医院");
                }
                newList.add(lab);
            }

            List<Lab> sortList = sortnewLabs(newList, orgID);
            //插入的
            List<Lab> insertList = new LinkedList<>();
            //更新的
            List<Lab> updateList = new LinkedList<>();

            for (Lab lab : sortList) {
                if ("未处理".equals(lab.status)) {
                    Lab exLab = getLabByNameFilter(lab.getLab_name(), sortList);
                    if (exLab == null) {
                        insertList.add(lab);
                    } else {
                        updateList.add(lab);
                    }
                }
            }
            Set<String> failNames = new HashSet<>();
            for (Lab lab : insertList) {
                if (failNames.contains(lab.getLab_parentName())) {
                    failNames.add(lab.getLab_name());
                    srcList.add(lab.getSrcLog() + ",失败,上级部门插入失败");
                } else {
                    int counter = AllDao.getInstance().getOrgDao().insertOneLab(lab);
                    //同步增加资源
                    LaboratoryProcessor.addResource(lab);
                    if (counter == 1) {
                        srcList.add(lab.getSrcLog() + ",成功,插入成功");
                    } else {
                        srcList.add(lab.getSrcLog() + ",失败,插入失败");
                    }
                }
            }
            for (Lab lab : updateList) {
                if (failNames.contains(lab.getLab_parentName())) {
                    failNames.add(lab.getLab_name());
                    srcList.add(lab.getSrcLog() + ",失败,上级部门插入失败");
                } else {
                    int counter = AllDao.getInstance().getOrgDao().updateLabInfoByNameWithLab(lab);
                    if("一线临床类".equals(lab.getDepart_name()) || "行政管理类".equals(lab.getDepart_name())){
                        AllDao.getInstance().getSyResourceDao().deleteLabsReource(new String[]{lab.getLabID()});
                    }
                    if("业务管理类".equals(lab.getDepart_name())){
                        LaboratoryProcessor.addResource(lab);
                    }
                    if (counter == 1) {
                        srcList.add(lab.getSrcLog() + ",成功,更新成功");
                    } else {
                        srcList.add(lab.getSrcLog() + ",失败,更新失败");
                    }
                }
            }

            return srcList;
        }
    }

    /**
     * 过滤存在判断
     *
     * @param lab_name
     * @param sortList
     * @return
     */
    public static Lab getLabByNameFilter(String lab_name, List<Lab> sortList) {
        for (Lab lab : sortList) {
            if (lab.status == null && lab_name.equals(lab.getLab_name())) {
                return lab;
            }
        }
        return null;
    }


    public static Lab getLabByName(String name, List<Lab> labs) {
        for (Lab lab : labs) {
            if (lab.getLab_name().equals(name)) {
                return lab;
            }
        }
        return null;
    }

    private Lab getLabByID(String id, List<Lab> labs) {
        for (Lab lab : labs) {
            if (lab.getLabID().equals(id)) {
                return lab;
            }
        }
        return null;
    }

    //构建
    public static List<Lab> sortnewLabs(List<Lab> newlabs, String orgID) {
        List<Lab> sort = new LinkedList<>();
        List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        Map<String, String> namesMap = new HashMap<>();
        for (int i = 1; i < 5; i++) {//支持五层
            if (i == 1) {//一层
                for (Lab lab : newlabs) {
                    if (lab.getLab_parentName().equals("医院")) {
                        labs.add(lab);
                    }
                }
                for (Lab lab : labs) {
                    if (lab.getLab_level() == 1) {
                        if ("未处理".equals(lab.status)) {
                            String name = lab.getLab_name();
                            String labID = getLabID(name, orgID, labs);
                            lab.setLabID(labID);
                        }
                        sort.add(lab);
                        namesMap.put(lab.getLab_name(), lab.getLabID());
                    }
                }
            } else {
                for (Lab lab : newlabs) {//从新增的里面找到下层Lab,放入
                    if (namesMap.keySet().contains(lab.getLab_parentName()) && !labs.contains(lab)) {
                        lab.setLab_level(i);
                        String parentID = namesMap.get(lab.getLab_parentName());
                        lab.setLab_parent(parentID);
                        labs.add(lab);
                    }
                }
                for (Lab lab : labs) {
                    if (lab.getLab_level() == i) {
                        if ("未处理".equals(lab.status)) {
                            String name = lab.getLab_name();
                            String labID = getLabID(name, orgID, sort);
                            lab.setLabID(labID);
                        }
                        namesMap.put(lab.getLab_name(), lab.getLabID());
                        sort.add(lab);
                    }
                }

            }
        }
        for (Lab lab : sort) {
            newlabs.remove(lab);
        }
        return sort;

    }

    /**
     * @param name
     * @param sort
     * @return
     */
    public static String getLabID(String name, String orgID, List<Lab> sort) {
        for (Lab lab : sort) {
            if (lab.getLab_name().equals(name)) {
                if (!"未处理".equals(lab.status)) {
                    return lab.getLabID();
                }
            }
        }
        String labID = orgID + "-" + ChineseToEnglish.getPingYin(name);
        int counter = 0;
        while (true) {
            boolean flag = false;
            for (Lab lab : sort) {
                if (labID.equals(lab.getLabID())) {
                    flag = true;
                }
            }
            if (flag) {
                labID = orgID + "-" + ChineseToEnglish.getPingYin(name) + counter;
                counter++;
            } else {
                break;
            }
        }
        return labID;
    }

    public static List<String> readFiles(List<File> fileList) throws Exception {
        BufferedReader reader = null;
        List<String> strList = new LinkedList<>();
        for (File file : fileList) {
            String path = file.getPath();
            String code = FilesUtils.codeString(path);
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), code));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                strList.add(tempString);
            }
            reader.close();
        }
        return strList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tempPath = fileBean.getManageFileLocation();
    }

}
