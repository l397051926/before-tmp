package com.gennlife.platform.util;

import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.projectBean.*;
import com.gennlife.platform.enums.LogActionEnum;
import com.gennlife.platform.enums.MemberEnum;
import com.gennlife.platform.proc.UserProcessor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chensong on 2015/12/7.
 */
public class JsonUtils {
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd");
    private static JsonParser jsonParser = new JsonParser();

    public static CreateProjectQueryBean parseCreateProject(String param) throws ParseException {
        CreateProjectQueryBean createProjectQueryBean = new CreateProjectQueryBean();
        CreateProject createProject = new CreateProject();
        List<ProUser> proUserList = new LinkedList<ProUser>();
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = UUID.randomUUID().toString();
        createProject.setProjectID(projectID);
        createProject.setProjectName(jsonObject.get("projectName").getAsString());
        String creater = jsonObject.get("creater").getAsString();
        createProject.setCreater(creater);
        ProUser proUser = new ProUser();
        proUser.setProjectID(projectID);
        proUser.setRole(MemberEnum.Creater.getIndex());//创建者
        proUser.setUid(creater);
        proUserList.add(proUser);
        createProject.setIssueType(jsonObject.get("issueType").getAsString());
        createProject.setIssueName(jsonObject.get("issueName").getAsString());
        createProject.setIssueEngName(jsonObject.get("issueEngName").getAsString());
        String stime = jsonObject.get("starttime").getAsString();
        String etime = jsonObject.get("endtime").getAsString();
        Date startTime = null;
        Date endTime = null;
        Integer caseNum = null;
        if(null != stime && !"".equals(stime)){
            startTime = time.parse(stime);
        }
        if(null !=etime &&  !"".equals(etime)){
            endTime = time.parse(etime);
        }
        caseNum = jsonObject.get("caseNum").getAsInt();
        createProject.setStartTime(startTime);
        createProject.setEndTime(endTime);
        createProject.setCaseNum(caseNum);
        createProject.setCRO(jsonObject.get("CRO").getAsString());
        createProject.setPlanVersion(jsonObject.get("planVersion") != null ? jsonObject.get("planVersion").getAsString() : null);
        createProject.setAbstractText(jsonObject.get("abstractText") != null ? jsonObject.get("abstractText").getAsString() : null);
        createProject.setResearchArea(jsonObject.get("researchArea") != null ? jsonObject.get("researchArea").getAsString() : null);
        createProject.setResearchDesc(jsonObject.get("researchDesc") != null ? jsonObject.get("researchDesc").getAsString() : null);
        createProject.setCenter(jsonObject.get("center") != null ? jsonObject.get("center").getAsString() : null);
        createProject.setTypein(jsonObject.get("typein") != null ? jsonObject.get("typein").getAsString() : null);
        createProject.setTestType(jsonObject.get("testType")!=null?jsonObject.get("testType").getAsString():null);
        String testGroup = jsonObject.get("testGroup").getAsString();
        createProject.setTestGroup(jsonObject.get("testGroup") != null? jsonObject.get("testGroup").getAsString():null);

        String manager = jsonObject.get("manager")!=null?jsonObject.get("manager").getAsString():null;
        if(creater.equals(manager)){
            createProject.setMembers(1);
        }else{
            ProUser managerUser = new ProUser();
            managerUser.setProjectID(projectID);
            managerUser.setUid(manager);
            managerUser.setRole(MemberEnum.Manager.getIndex());
            proUserList.add(managerUser);
            createProject.setMembers(2);
        }
        createProject.setPlanNum(0);
        createProject.setManager(jsonObject.get("manager").getAsString());
        createProject.setLeaderUnit(jsonObject.get("leaderUnit").getAsString());
        createProject.setUnit(jsonObject.get("unit").getAsString());
        createProject.setMonitorUnit(jsonObject.get("monitorUnit").getAsString());
        createProject.setPstatus(0);//项目状态：0，创建，1代表样本未录入，2，部分样本就绪（部分数据测序中）3.样本已经就绪，4样本分析中，5已完结
        Date createTime = new Date();
        createProject.setCreateTime(createTime);
        //组建log日志
        SyUser syUser = UserProcessor.getUser(creater);
        ProLog proLog = new ProLog();
        proLog.setUid(creater);
        proLog.setProjectID(projectID);
        proLog.setAction(LogActionEnum.CreateProject.getName());
        proLog.setLogTime(createTime);
        proLog.setLogText(syUser.getUname() + LogActionEnum.CreateProject.getName());//文本
        createProjectQueryBean.setProlog(proLog);
        createProjectQueryBean.setCreateProject(createProject);
        createProjectQueryBean.setProUserList(proUserList);
        return createProjectQueryBean;
    }

    public static ProjectPlan parseCreatePlan(String param ,boolean iSUpdate){
        ProjectPlan projectPlan = new ProjectPlan();
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String planName = jsonObject.get("planName").getAsString();
        String desc = jsonObject.get("desc").getAsString();
        String creater = jsonObject.get("creater").getAsString();
        Date createTime = new Date();
        projectPlan.setPlanDesc(desc);
        projectPlan.setPlanName(planName);
        projectPlan.setProjectID(projectID);
        if(iSUpdate){
            String id = jsonObject.get("id").getAsString();
            projectPlan.setModifier(creater);
            projectPlan.setModifTime(createTime);
            int idInt = Integer.parseInt(id);
            projectPlan.setId(idInt);
        }else{
            projectPlan.setCreater(creater);
            projectPlan.setCreateTime(createTime);
        }
        return projectPlan;
    }

    static public String GetString( JsonObject jobj, String key) {
        String result = "";
        if (!jobj.has(key)) {
            return result;
        }
        Object tmp_data = jobj.get(key);
        if (null == tmp_data) {
            return result;
        }
        if(tmp_data instanceof String){
            result = jobj.get(key).getAsString();
        }else if(tmp_data instanceof Integer){
            result = String.valueOf(jobj.get(key).getAsInt());
        }else if(tmp_data instanceof Double){
            result = String.valueOf(jobj.get(key).getAsDouble());
        }else if(tmp_data instanceof Long){
            result = String.valueOf(jobj.get(key).getAsLong());
        }else if(tmp_data instanceof Boolean){
            result = String.valueOf(jobj.get(key).getAsBoolean());
        }
        return result;
    }



}
