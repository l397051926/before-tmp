package com.gennlife.platform.util;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.LaboratoryProcessor;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chen-song on 16/9/6.
 */
public class FileUploadUtil {
    public static Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
    public static Gson gson = GsonUtil.getGson();
    private static SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    //文件位置，注意是绝对路径
    private String tempPath = "/home/tomcat_demo2_web/update/";
    //文件后缀
    private String suffix = ".csv";
    //获取的上传请求
    private HttpServletRequest fileuploadReq = null;
    //设置最多只允许在内存中存储的数据,单位:字节，这个参数不要设置太大
    private int sizeThreshold = 4096;
    //设置允许用户上传文件大小,单位:字节
    //共10M
    private long sizeMax = 10485760;
    //图片文件序号
    private int picSeqNo = 1;

    public FileUploadUtil() {
    }

    public FileUploadUtil(String tempPath) {
        this.tempPath = tempPath;
    }

    public FileUploadUtil(String tempPath, String suffix ,HttpServletRequest fileuploadRequest) {
        this.tempPath = tempPath;
        this.suffix = suffix;
        this.fileuploadReq = fileuploadRequest;

    }

    /**
     * 文件上载
     *
     * @return true —— success; false —— fail.
     */
    public String Upload(String from,String labImportsuffix) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        try {
            HttpSession session = fileuploadReq.getSession();
            if(session == null){
                return ParamUtils.errorParam("当前session已经失效");
            }
            User user = gson.fromJson((String)session.getAttribute("user"),User.class);
            if(user == null){
                return ParamUtils.errorParam("当前session已经失效");
            }
            String orgID = user.getOrgID();
            boolean isAdmin = LaboratoryProcessor.isAdmin(user);
            if(!isAdmin){
                return ParamUtils.errorParam("当前用户没有权限");
            }
            //如果没有临时目录，则创建它
            FilesUtils.makeDirectory(tempPath);
            //上传项目只要足够小，就应该保留在内存里。
            //较大的项目应该被写在硬盘的临时文件上。
            //非常大的上传请求应该避免。
            //限制项目在内存中所占的空间，限制最大的上传请求，并且设定临时文件的位置。
            //设置最多只允许在内存中存储的数据,单位:字节
            factory.setSizeThreshold(sizeThreshold);
            // the location for saving data that is larger than getSizeThreshold()
            factory.setRepository(new File(tempPath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            //设置允许用户上传文件大小,单位:字节
            upload.setSizeMax(sizeMax);
            List fileItems = upload.parseRequest(fileuploadReq);
            Iterator iter = fileItems.iterator();
            List<File> fileList = new LinkedList<>();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                //忽略其他不是文件域的所有表单信息
                if (!item.isFormField()) {
                    String name = item.getName();
                    long size = item.getSize();
                    //有多个文件域时，只上传有文件的
                    if ((name == null || name.equals("")) && size == 0)
                        continue;
                    String ext = "." + FilesUtils.getTypePart(name);
                    if(!suffix.equals(ext)){
                        return ParamUtils.errorParam("文件格式不是.csv");
                    }
                    UUID uuid = UUID.randomUUID();
                    try {
                        //保存上传的文件到指定的目录
                        //在下文中上传文件至数据库时，将对这里改写
                        //没有指定新文件名时以原文件名来命名
                        String uploadfilename = "";
                        uploadfilename = tempPath +
                                name.replaceAll(ext,"") +
                                "_" +
                                uuid.toString() +
                                "_" +
                                time.format(new Date())
                                + suffix;
                        FilesUtils.makeDirectory(uploadfilename);
                        File file = new File(uploadfilename);
                        fileList.add(file);
                        item.write(file);
                        picSeqNo++;
                    } catch (Exception e) {
                        logger.error("",e);
                        throw new IOException(e.getMessage());
                    }
                }
            }
            if("导入科室".equals(from)){
                return handleLab(fileList,orgID,user,labImportsuffix);
            }else if("导入人员".equals(from)){
                return handleStaff(fileList,orgID,user,labImportsuffix);
            }else{
                return from;
            }
        } catch (IOException e) {
            logger.error("",e);
            return ParamUtils.errorParam("导入失败");
        } catch (FileUploadException e) {
            logger.error("",e);
            return ParamUtils.errorParam("导入失败");
        } catch (Exception e) {
            logger.error("",e);
            return ParamUtils.errorParam("导入失败");
        }
    }

    private String handleStaff(List<File> fileList, String orgID,User user,String labImportsuffix) throws Exception {
        List<String> list = importsStaffs(fileList,orgID,user);
        File orgIDImportResultFile =new File(tempPath + user.getOrg_name() +labImportsuffix);
        return writeResultFile(list,orgIDImportResultFile);
    }



    private String writeResultFile(List<String> list,File orgIDImportResultFile) throws IOException {
        if(!orgIDImportResultFile.exists()){
            orgIDImportResultFile.createNewFile();
        }
        FileWriter fw = new FileWriter(orgIDImportResultFile);
        int insert = 0;
        int fail = 0;
        int update = 0;
        for(String line:list){
            String[] data = line.split(",");
            if(data.length > 2){
                if("成功".equals(data[data.length-2])){
                    if("更新成功".equals(data[data.length-1])){
                        update ++;
                    }else{
                        insert ++;
                    }
                }else if("失败".equals(data[data.length-2])){
                    fail ++;
                }
            }
            fw.write(line+"\n");
        }
        fw.flush();
        fw.close();
        Map<String,Integer> map = new HashMap<>();
        map.put("update",update);
        map.put("insert",insert);
        map.put("fail",fail);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(map);
        return gson.toJson(resultBean);
    }

    private List<String> importsStaffs(List<File> fileList, String orgID, User user) throws Exception {
        List<String> strList = readFiles(fileList);
        List<String> srcList = new LinkedList<>();
        Map<String,Integer> map = new HashMap<>();
        String termLine = strList.get(0);
        String[] data = termLine.split(",");
        for(int index = 0; index < data.length; index++){
            map.put(data[index],index);
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
        List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        if(unumberIndex == null){
            for(String line:strList){
                srcList.add(line+",失败,缺少工号列");
            }
            return srcList;
        }
        if(nameIndex == null){
            for(String line:strList){
                srcList.add(line+",失败,缺少姓名列");
            }
            return srcList;
        }
        if(emailIndex == null){
            for(String line:strList){
                srcList.add(line+",失败,缺少邮箱列");
            }
            return srcList;
        }
        if(labIndex == null){
            for(String line:strList){
                srcList.add(line+",失败,缺少所属部门列");
            }
            return srcList;
        }
        List<User> userList = new LinkedList<>();
        List<String> lineList = new LinkedList<>();
        for(String line:strList){
            if(line.equals(termLine)){
                continue;
            }
            String[] terms = line.split(",");
            if(terms.length < data.length ){
                srcList.add(line+",失败,缺少数据");
                continue;
            }else{
                String number = terms[unumberIndex];
                String name = terms[nameIndex];
                String lab_name = terms[labIndex];
                String email = terms[emailIndex];
                String tel = "";
                if(telIndex != null){
                    tel = terms[telIndex];
                }
                String uposition = "";
                if(upositionIndex != null){
                    uposition = terms[upositionIndex];
                }
                String uprofession = "";
                if(uprofessionIndex != null){
                    uprofession = terms[uprofessionIndex];
                }
                User addUser = new User();
                Lab lab = getLabByName(lab_name,labs);
                if(lab == null && !"医院".equals(lab_name)){
                    srcList.add(line+",失败,所属部门不存在");
                    continue;
                }else if("医院".equals(lab_name)){
                    addUser.setLabID(user.getOrgID());
                    addUser.setLab_name(user.getOrg_name());
                }else if(lab != null){
                    addUser.setLabID(lab.getLabID());
                    addUser.setLab_name(lab_name);
                }
                addUser.setUnumber(number);
                addUser.setPwd("ls123456");
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
                userList.add(addUser);
                lineList.add(line);
            }
        }
        for(int index = 0;index < userList.size();index++){
            User addUser = userList.get(index);
            String line = lineList.get(index);
            User exUser = AllDao.getInstance().getSyUserDao().getUserByUnumber(addUser.getUnumber(),addUser.getOrgID());
            if(exUser != null){//更新
                addUser.setUid(null);//uid 不更新
                if(!addUser.getUemail().equals(exUser.getUemail())){//邮箱不一样,需要检查
                    int emailCounter = AllDao.getInstance().getSyUserDao().existEmail(addUser.getUemail());
                    if(emailCounter >= 1){
                        srcList.add(line+",失败,更新后的email是存在的");
                    }else{
                        int counter = AllDao.getInstance().getSyUserDao().updateUserByUnumber(addUser);
                        if(counter >= 1){
                            srcList.add(line+",成功,更新成功");
                        }else{
                            srcList.add(line+",失败,更新失败");
                        }
                    }
                }else{
                    int counter = AllDao.getInstance().getSyUserDao().updateUserByUnumber(addUser);
                    if(counter >= 1){
                        srcList.add(line+",成功,更新成功");
                    }else{
                        srcList.add(line+",失败,更新失败");
                    }
                }
            }else{//插入
                int counter = AllDao.getInstance().getSyUserDao().insertOneUser(addUser);
                if(counter >= 1){
                    srcList.add(line+",成功,插入成功");
                }else{
                    srcList.add(line+",失败,插入失败");
                }
            }
        }
        return srcList;

    }

    private String handleLab(List<File> fileList, String orgID,User user,String labImportsuffix) throws Exception {
        List<String> list = importLabs(fileList,orgID,user.getUid());
        File orgIDImportResultFile =new File(tempPath + user.getOrg_name() +labImportsuffix);
        return writeResultFile(list,orgIDImportResultFile);
    }

    /**
     * 设置临时存贮目录
     */
    public void setTmpPath(String tmppath) {
        this.tempPath = tmppath;
    }

    /**
     * 设置最大上传文件字节数，不设置时默认10M
     */
    public void setFileMaxSize(long maxsize) {
        this.sizeMax = maxsize;
    }

    /**
     * 设置Http 请求参数，通过这个能数来获取文件信息
     */
    public void setHttpReq(HttpServletRequest httpreq) {
        this.fileuploadReq = httpreq;
    }

    /**
     * 设置Http 请求参数，通过这个能数来获取文件信息
     */
    public void setPicSeqNo(int seqNo) {
        this.picSeqNo = seqNo;
    }

    /**
     * 通过文件列表,将导入科室信息导入到orgID当中
     * @param fileList
     * @param orgID
     */
    private List<String> importLabs(List<File> fileList,String orgID,String uid) throws Exception {
        List<String> strList = readFiles(fileList);
        List<String> srcList = new LinkedList<>();
        List<Lab> newList = new LinkedList<>();
        for(String str:strList){
            String[] data = str.split(",");
            if(data.length >= 4){
                if("科室名称".equals(data[0].trim())){
                    srcList.add(str);
                }else{
                    String name = data[0].trim();
                    String leader = data[1].trim();
                    String parentName = data[3].trim();
                    String leaderName = data[2].trim();
                    if("".equals(name)){
                        srcList.add(str+",失败,科室名称为空");
                        continue;
                    }else if("".equals(parentName)){
                        srcList.add(str+",失败,上级科室名称");
                        continue;
                    }else{
                        Lab lab = new Lab();
                        lab.setOrgID(orgID);
                        lab.setLab_level(1);
                        lab.setLab_leaderName(leaderName);
                        lab.setLab_leader(leader);
                        lab.setLab_parent(orgID);
                        lab.setLab_name(name);
                        lab.setLab_parentName(parentName);
                        lab.setAdd_user(uid);
                        lab.setAdd_time(time.format(new Date()));
                        lab.status = "未处理";
                        newList.add(lab);
                    }
                }
            }else{
                srcList.add(str+",失败,缺少数据");
            }
        }
        Set<String> names = new HashSet<>();
        List<Lab> invertedList = new LinkedList<>();
        for(int i=newList.size() -1 ;i>=0;i--){//反向遍历去重
            Lab lab = newList.get(i);
            if(!names.contains(lab.getLab_name())){
                names.add(lab.getLab_name());
                invertedList.add(lab);
            }else{
                srcList.add(lab.getSrcLog()+",失败,被后面最新的覆盖");
            }
        }
        newList.clear();
        List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        for(Lab lab:invertedList){
            Lab ex1Lab = getLabByName(lab.getLab_parentName(),invertedList);
            Lab ex2Lab = getLabByName(lab.getLab_parentName(),labs);
            if(ex1Lab == null && ex2Lab == null){//父节点不存在
                lab.setLab_parentName("医院");
            }
            newList.add(lab);
        }
        List<Lab> sortList = sortnewLabs(newList,orgID);
        //插入的
        List<Lab> insertList = new LinkedList<>();
        //更新的
        List<Lab> updateList = new LinkedList<>();
        logger.info("关联无问题"+gson.toJson(sortList));
        for(Lab lab:sortList){
            if("未处理".equals(lab.status)){
                Lab exLab = getLabByNameFilter(lab.getLab_name(),sortList);
                if(exLab == null){
                    insertList.add(lab);
                }else{
                    updateList.add(lab);
                }
            }
        }
        Set<String> failNames = new HashSet<>();
        for(Lab lab:insertList){
            if(failNames.contains(lab.getLab_parentName())){
                failNames.add(lab.getLab_name());
                srcList.add(lab.getSrcLog()+",失败,上级部门插入失败");
            }else{
                int counter = AllDao.getInstance().getOrgDao().insertOneLab(lab);
                //同步增加资源
                LaboratoryProcessor.addResource(lab);
                if(counter == 1){
                    srcList.add(lab.getSrcLog()+",成功,插入成功");
                }else{
                    srcList.add(lab.getSrcLog()+",失败,插入失败");
                }
            }
        }
        for(Lab lab:updateList){
            if(failNames.contains(lab.getLab_parentName())){
                failNames.add(lab.getLab_name());
                srcList.add(lab.getSrcLog()+",失败,上级部门插入失败");
            }else{
                int counter = AllDao.getInstance().getOrgDao().updateLabInfoByNameWithLab(lab);
                if(counter == 1){
                    srcList.add(lab.getSrcLog()+",成功,更新成功");
                }else{
                    srcList.add(lab.getSrcLog()+",失败,更新失败");
                }
            }
        }

        return srcList;
    }

    /**
     * 过滤存在判断
     * @param lab_name
     * @param sortList
     * @return
     */
    private Lab getLabByNameFilter(String lab_name, List<Lab> sortList) {
        for(Lab lab:sortList){
            if(lab.status == null && lab_name.equals(lab.getLab_name())){
                return lab;
            }
        }
        return null;
    }


    private Lab getLabByName(String name,List<Lab> labs){
        for(Lab lab:labs){
            if(lab.getLab_name().equals(name)){
                return lab;
            }
        }
        return null;
    }

    private Lab getLabByID(String id,List<Lab> labs){
        for(Lab lab:labs){
            if(lab.getLabID().equals(id)){
                return lab;
            }
        }
        return null;
    }
    private List<Lab> sortnewLabs(List<Lab> newlabs,String orgID){
        List<Lab> sort = new LinkedList<>();
        List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        Map<String,String> namesMap = new HashMap<>();
        for(int i=1;i<5;i++){//支持五层
            if(i==1){//一层
                for(Lab lab:newlabs){
                    if(lab.getLab_parentName().equals("医院")){
                        labs.add(lab);
                    }
                }
                for(Lab lab:labs){
                    if(lab.getLab_level() == 1){
                        if("未处理".equals(lab.status)){
                            String name = lab.getLab_name();
                            String labID = getLabID(name,orgID,sort);
                            lab.setLabID(labID);
                        }
                        sort.add(lab);
                        namesMap.put(lab.getLab_name(),lab.getLabID());
                    }
                }
            }else{
                for(Lab lab:newlabs){//从新增的里面找到下层Lab,放入
                    if(namesMap.keySet().contains(lab.getLab_parentName()) && !labs.contains(lab)){
                        lab.setLab_level(i);
                        String parentID = namesMap.get(lab.getLab_parentName());
                        lab.setLab_parent(parentID);
                        labs.add(lab);
                    }
                }
                for(Lab lab:labs){
                    if(lab.getLab_level() == i ){
                        if("未处理".equals(lab.status)){
                            String name = lab.getLab_name();
                            String labID = getLabID(name,orgID,sort);
                            lab.setLabID(labID);
                        }
                        namesMap.put(lab.getLab_name(),lab.getLabID());
                        sort.add(lab);
                    }
                }

            }
        }
        for(Lab lab:sort){
            newlabs.remove(lab);
        }
        return sort;

    }

    /**
     *
     * @param name
     * @param sort
     * @return
     */
    private String getLabID(String name, String orgID,List<Lab> sort) {
        String labID = orgID + "-" + ChineseToEnglish.getPingYin(name);
        int counter = 0;
        while(true){
            boolean flag = false;
            for(Lab lab:sort){
                if(labID.equals(lab.getLabID())){
                    flag = true;
                }
            }
            if(flag){
                labID = orgID + "-" + ChineseToEnglish.getPingYin(name)+"_"+counter;
                counter ++;
            }else {
                break;
            }
        }
        return labID;
    }
    private List<String> readFiles(List<File> fileList) throws Exception {
        BufferedReader reader = null;
        List<String> strList = new LinkedList<>();
        for(File file:fileList){
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

}
