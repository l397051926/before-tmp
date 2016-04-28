package com.gennlife.platform.proc;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.projectBean.*;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.enums.LogActionEnum;
import com.gennlife.platform.enums.MemberEnum;
import com.gennlife.platform.enums.ProjectStageEnum;
import com.gennlife.platform.filter.ScatterDrawer;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.JsonUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chensong on 2015/12/9.
 */
public class ProjectProcessor {
    private static Logger logger = LoggerFactory.getLogger(ProjectProcessor.class);
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer = new View();
    private Map<String, Object> confMap;

    /**
     * {}
     * 我的项目中 项目列表
     * @param request
     * @param resps
     */
    public void myProjectList(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        logger.info("myProjectList param ="+param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String uid = jsonObject.get("uid").getAsString();
        String limit = jsonObject.get("limit").getAsString();
        String key = jsonObject.get("key").getAsString();
        int[] result = ParamUtils.parseLimit(limit);
        int startIndex = result[0];
        int maxNum = result[1];
        Map<String,Object> conf = new HashMap<String, Object>();
        conf.put("loginname",uid);
        conf.put("startIndex",(startIndex + 1) * maxNum);
        conf.put("maxNum",maxNum);
        conf.put("key",key);
        List<MyProjectList> list = null;
        int counter = 0;
        list = AllDao.getInstance().getSyUserDao().getMyProjectList(conf);
        counter  = AllDao.getInstance().getSyUserDao().getProjectCounter(conf);
        Map<String,Object> confMap = new HashMap<String, Object>();
        for(MyProjectList myProjectList:list){
            String projectID = myProjectList.getProjectID();
            confMap.put("projectID",projectID);
            confMap.put("startIndex",0);
            confMap.put("maxNum",5);
            List<ProLog> logList = AllDao.getInstance().getProjectDao().getProjectLog(confMap);
            for(ProLog proLog:logList){
                proLog.setSampleName(null);
                proLog.setProjectID(null);
            }
            myProjectList.setLogs(logList);
        }
        Map<String,Integer> info = new HashMap<String,Integer>();
        info.put("counter", counter);
        viewer.viewList(list, info, true, resps, request);
    }

    /**
     * 创建新项目
     * @param request
     * @param resps
     */
    public void createNewProject(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        logger.info("createNewProject param="+param);
        boolean flag = true;
        try {
            CreateProjectQueryBean createProjectQueryBean = JsonUtils.parseCreateProject(param);
            CreateProject createProject = createProjectQueryBean.getCreateProject();
            List<ProUser> proUserList = createProjectQueryBean.getProUserList();
            ProLog proLog = createProjectQueryBean.getProlog();
            int counter = AllDao.getInstance().getProjectDao().insertCreateProject(createProject);
            counter = AllDao.getInstance().getProjectDao().insertProUserList(proUserList);
            counter = AllDao.getInstance().getProjectDao().insertProLog(proLog);
        } catch (ParseException e) {
            logger.error("解析请求参数出错",e);
            flag = false;
        }
        viewer.viewList(null, null, flag, resps, request);
    }

    /**
     * 项目详情
     * @param request
     * @param resps
     */
    public void projectDetail(HttpServletRequest request, HttpServletResponse resps){

    }

    /**
     * 创建一个新方案（工作流）
     * @param request
     * @param resps
     */
    public void createNewPlan(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        ProjectPlan projectPlan = JsonUtils.parseCreatePlan(param,false);
        projectPlan.setPlanStatus(1);//初建状态
        int counter = AllDao.getInstance().getProjectDao().insertProPlan(projectPlan);
        ProLog proLog = new ProLog();
        proLog.setProjectID(projectPlan.getProjectID());
        proLog.setPlanName(projectPlan.getPlanName());
        proLog.setUid(projectPlan.getCreater());
        proLog.setAction(LogActionEnum.CreatePlan.getName());
        SyUser syUser = UserProcessor.getUser(projectPlan.getCreater());
        proLog.setLogText(syUser.getUname() + LogActionEnum.CreatePlan.getName() + projectPlan.getPlanName());
        proLog.setLogTime(new Date());
        AllDao.getInstance().getProjectDao().insertProLog(proLog);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("projectID",projectPlan.getProjectID());
        List<ProjectPlan> list = AllDao.getInstance().getProjectDao().getProjectPlan(map);
        viewer.viewList(list, null, true, resps, request);
    }

    /**
     * 删除一个新方案（工作流）
     * @param request
     * @param resps
     */
    public void deletePlan(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String uid = jsonObject.get("uid").getAsString();
        String id = jsonObject.get("id").getAsString();
        Map<String,Object> mapConf = new HashMap<String, Object>();
        mapConf.put("projectID", projectID);
        mapConf.put("id", id);
        String planName = AllDao.getInstance().getProjectDao().getPlanName(mapConf);
        int counter = AllDao.getInstance().getProjectDao().deleteProPlan(mapConf);
        ProLog proLog = new ProLog();
        proLog.setProjectID(projectID);
        proLog.setUid(uid);
        proLog.setAction(LogActionEnum.DeletePlan.getName());
        SyUser syUser = UserProcessor.getUser(uid);
        proLog.setLogText(syUser.getUname() + LogActionEnum.DeletePlan.getName() + planName);
        proLog.setLogTime(new Date());
        AllDao.getInstance().getProjectDao().insertProLog(proLog);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("projectID",projectID);
        List<ProjectPlan> list = AllDao.getInstance().getProjectDao().getProjectPlan(map);
        viewer.viewList(list, null, true, resps, request);
    }

    /**
     * 删除项目
     * @param request
     * @param resps
     */
    public void deleteProject(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        logger.info("deleteProject param=" + param);
        JsonArray ids = null;
        String uid = null;

        try{
            JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
            uid = jsonObject.get("uid").getAsString();
            ids = jsonObject.get("projectIDSet").getAsJsonArray();
        }catch(Exception e){
            ParamUtils.errorParam(request,resps);
            return;
        }
        Map<String,Object> confMap =  new HashMap<String, Object>();
        SyUser syUser = UserProcessor.getUser(uid);
        int c = 0;
        confMap.put("uid",uid);
        for(JsonElement jsonElement:ids){
            String projectID = jsonElement.getAsString();
            confMap.put("projectID",projectID);
            int count = AllDao.getInstance().getProjectDao().deleteProjectMember(confMap);
            c = c + count;
            if(count == 1){
                ProLog proLog = new ProLog();
                proLog.setUid(uid);
                proLog.setProjectID(projectID);
                proLog.setAction(LogActionEnum.ExitProject.getName());
                proLog.setLogTime(new Date());
                proLog.setLogText(syUser.getUname()+ LogActionEnum.ExitProject.getName());
                AllDao.getInstance().getProjectDao().insertProLog(proLog);
            }
        }

        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo("成功删除"+ c + "个项目");
        viewer.viewString(gson.toJson(resultBean),resps,request);

    }

    /**
     * 删除数据集
     * @param request
     * @param resps
     */
    public void deleteSet(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        logger.info("deleteSet param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String uid = jsonObject.get("uid").getAsString();
        JsonArray users = jsonObject.getAsJsonArray("uris");
        Date createTime = new Date();
        int counter = 0;
        for(JsonElement entry:users){
            String uri = "";
            if(entry.getAsJsonObject().get("sampleID") != null){
                uri = entry.getAsJsonObject().get("sampleID").getAsString();
            }
            Map<String,Object> confMap = new HashMap<String, Object>();
            confMap.put("projectID",projectID);
            confMap.put("uri",uri);
            String setName =  AllDao.getInstance().getProjectDao().getProjectSetName(confMap);
            ProLog proLog = new ProLog();
            proLog.setUid(uid);
            SyUser syUser = UserProcessor.getUser(uid);
            proLog.setProjectID(projectID);
            proLog.setAction(LogActionEnum.DeleteSamples.getName());
            proLog.setLogTime(createTime);
            proLog.setLogText(syUser.getUname()+ LogActionEnum.DeleteSamples.getName() + setName);
            AllDao.getInstance().getProjectDao().insertProLog(proLog);
            counter = counter + AllDao.getInstance().getProjectDao().deleteProjectSet(confMap);
        }
        Map<String,Integer> info = new HashMap<String,Integer>();
        info.put("counter",counter);
        viewer.viewList(null, info, true, resps, request);
    }




    /**
     * 工作区的工具列表
     * @param request
     * @param resp
     */
    public void tools(HttpServletRequest request, HttpServletResponse resp){
        String content= HttpRequestUtils.httpGet(ArkService.getConf().getToolsURL());
        viewer.viewString(content,resp,request);
    }


    /**
     *某次动作的接口
     * @param request
     * @param resp
     */
    public void aTool(HttpServletRequest request, HttpServletResponse resp){
        String param = ParamUtils.getParam(request);
        logger.info("aTool param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        StringBuffer newParam = new StringBuffer();
        String uri = null;
        String tool_name = null;
        String row = null;
        for(Map.Entry<String, JsonElement> entry:jsonObject.entrySet()){
            String key = entry.getKey();
            String value = null;
            if(entry.getValue().isJsonObject()){
                value = entry.getValue().getAsJsonObject().toString();
            }else if(entry.getValue().isJsonArray()){
                value = entry.getValue().getAsJsonArray().toString();
            }else if(entry.getValue().getAsBoolean()){
                value = entry.getValue().getAsBoolean()+"";
            }else {
                value = entry.getValue().getAsString();
            }
            if("uri".equals(key)){
                uri = value;
            }else if("tool_name".equals(key)) {
                tool_name = value;
            }else if("row".equals(key)){
                row = value;
            }
            newParam.append("&").append(key).append("=").append(ParamUtils.encodeURI(value));
        }
        logger.info("aTool uri="+uri);
        logger.info("aTool tool_name="+tool_name);
        logger.info("aTool newParam="+newParam);
        String url = ArkService.getConf().getAtoolURL() + "?"+newParam.toString();
        logger.info("aTool url="+url);
        String content= HttpRequestUtils.httpGet(url);
        logger.info("aTool 结果:" + content);
        if("scatter_drawer".equals(tool_name)){
            ScatterDrawer scatterDrawer = new ScatterDrawer();
            content = scatterDrawer.filter(jsonObject,content,row);
        }
        logger.info("aTool 过滤后结果:" + content);
        viewer.viewString(content,resp,request);
    }

    /**
     * 数据集合存储
     * @param request
     * @param resp
     */
    public void storeSet(HttpServletRequest request, HttpServletResponse resp){

    }

    /**
     * 数据区数据集列表
     * @param request
     * @param resp
     */
    public void setList(HttpServletRequest request, HttpServletResponse resp){
        String param = ParamUtils.getParam(request);
        logger.info("setList param="+param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        Map<String,Object> conf = new HashMap<String, Object>();
        conf.put("projectID",projectID);
        List<ProSample> list = AllDao.getInstance().getProjectDao().getSampleDataInitList(conf);
        Map<String,Object> result = new HashMap<String, Object>();
        for(ProSample proSample:list){
            if(null != proSample.getOpTime()){
                proSample.setOperatTime((time.format(proSample.getOpTime())));
            }
            if(proSample.getSampleDesc() == null){
                proSample.setSampleDesc("");
            }
            proSample.setOpTime(null);
            proSample.setProjectID(null);
            proSample.setPlanName(null);
            List<ProSample> setBeanList = (List<ProSample>) result.get("CollectionOfInit");
            if(null == setBeanList){
                setBeanList = new LinkedList<ProSample>();
            }
            setBeanList.add(proSample);
            result.put("CollectionOfInit", setBeanList);
        }
        List<ProjectPlan> plansList = AllDao.getInstance().getProjectDao().getProjectPlan(conf);
        for(ProjectPlan projectPlan:plansList){
            String planName = projectPlan.getPlanName();
            if(planName == null){
                continue;
            }
            conf.put("planName",planName);
            list = AllDao.getInstance().getProjectDao().getSampleDataListByPlanName(conf);
            if(list == null || list.isEmpty()){
                result.put(planName, new LinkedList<String>());
            }
            for(ProSample proSample:list){
                if(null != proSample.getOpTime()){
                    proSample.setOperatTime((time.format(proSample.getOpTime())));
                }
                if(proSample.getSampleDesc() == null){
                    proSample.setSampleDesc("");
                }
                proSample.setOpTime(null);
                proSample.setProjectID(null);
                List<ProSample> setBeanList = (List<ProSample>) result.get(proSample.getPlanName());
                if(null == setBeanList){
                    setBeanList = new LinkedList<ProSample>();
                }
                setBeanList.add(proSample);
                result.put(proSample.getPlanName(), setBeanList);
            }
        }
        viewer.viewString(gson.toJson(result),resp,request);
    }

    /**
     * 导出
     * @param request
     * @param resp
     */
    public void exportResult(HttpServletRequest request, HttpServletResponse resp){

    }

    /**
     * 项目日志列表
     * @param request
     * @param resp
     */
    public void projectLogList(HttpServletRequest request, HttpServletResponse resp){
        String param = ParamUtils.getParam(request);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String limit = "0,5";
        if(jsonObject.get("limit") != null){
            limit = jsonObject.get("limit").getAsString();
        }
        int[] ls = ParamUtils.parseLimit(limit);
        Map<String,Object> confMap = new HashMap<String, Object>();
        confMap.put("projectID",projectID);
        confMap.put("startIndex",ls[0] * ls[1]);
        confMap.put("maxNum",ls[1]);
        List<ProLog> logList = AllDao.getInstance().getProjectDao().getProjectLog(confMap);
        for(ProLog proLog:logList){
            if(null != time.format(proLog.getLogTime())){
                proLog.setlTime(time.format(proLog.getLogTime()));
            }
            proLog.setLogTime(null);
        }
        int counter =  AllDao.getInstance().getProjectDao().getProjectLogCounter(confMap);
        Map<String,Integer> info = new HashMap<String,Integer>();
        info.put("counter",counter);
        viewer.viewList(logList, info, true, resp, request);
    }

    /**
     * 项目方案列表
     * @param request
     * @param resp
     */
    public void projectPlanList(HttpServletRequest request, HttpServletResponse resp){
        String param = ParamUtils.getParam(request);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        Map<String,Object> confMap = new HashMap<String, Object>();
        confMap.put("projectID", projectID);
        List<ProjectPlan> plansList = AllDao.getInstance().getProjectDao().getProjectPlan(confMap);
        viewer.viewList(plansList, null, true, resp, request);
    }

    /**
     * 项目成员列表
     * @param request
     * @param resp
     */
    public void projectMemberList(HttpServletRequest request, HttpServletResponse resp){
        String param = ParamUtils.getParam(request);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String searchMemberkey = jsonObject.get("searchMemberkey").getAsString();
        String limit = jsonObject.get("limit").getAsString();
        logger.info("projectID=" + projectID + ",searchMemberkey=" + searchMemberkey + ",limit=" + limit);
        int[] ls = ParamUtils.parseLimit(limit);
        Map<String,Object> confMap = new HashMap<String, Object>();
        confMap.put("projectID",projectID);
        confMap.put("startIndex",ls[0] * ls[1]);
        confMap.put("maxNum",ls[1]);
        confMap.put("searchMemberkey",searchMemberkey);
        List<SyUser> list = AllDao.getInstance().getSyUserDao().getProjectMemberList(confMap);
        int counter = AllDao.getInstance().getSyUserDao().getProjectMemberCounter(confMap);
        Map<String,Integer> info = new HashMap<String,Integer>();
        info.put("counter",counter);
        viewer.viewList(list, info, true, resp, request);
    }

    /**
     * 项目添加用户
     * @param req
     * @param resp
     */
    public void addMember(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("addMember param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        JsonArray users = jsonObject.getAsJsonArray("users");
        int counter = 0;
        for(JsonElement entry:users){
            String uid = entry.getAsJsonObject().get("uid").getAsString();
            Map<String,Object> confMap = new HashMap<String, Object>();
            confMap.put("projectID",projectID);
            confMap.put("uid",uid);
            counter = counter + AllDao.getInstance().getProjectDao().insertProjectMember(confMap);
        }
        Map<String,Integer> info = new HashMap<String,Integer>();
        info.put("counter",counter);
        viewer.viewList(null, info, true, resp, req);
    }

    /**
     * 删除项目成员
     * @param req
     * @param resp
     */
    public void deleteMemeber(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("deleteMemeber param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        JsonArray users = jsonObject.getAsJsonArray("users");
        int counter = 0;
        for(JsonElement entry:users){
            String uid = entry.getAsJsonObject().get("uid").getAsString();
            Map<String,Object> confMap = new HashMap<String, Object>();
            confMap.put("projectID",projectID);
            confMap.put("uid",uid);
            counter = counter + AllDao.getInstance().getProjectDao().deleteProjectMember(confMap);

        }
        Map<String,Integer> info = new HashMap<String,Integer>();
        info.put("counter",counter);
        viewer.viewList(null, info, true, resp, req);

    }

    /**
     * 编辑方案
     * @param req
     * @param resp
     */
    public void editPlan(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("editPlan param=" + param);
        ProjectPlan projectPlan = JsonUtils.parseCreatePlan(param, true);
        projectPlan.setPlanStatus(2);//初建状态
        int counter = AllDao.getInstance().getProjectDao().updateProPlan(projectPlan);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("projectID",projectPlan.getProjectID());
        List<ProjectPlan> list = AllDao.getInstance().getProjectDao().getProjectPlan(map);
        viewer.viewList(list, null, true, resp, req);
    }


    public void setNameList(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("setNameList param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("projectID",projectID);
        List<String> list = AllDao.getInstance().getProjectDao().getProjectSetNameList(map);
        viewer.viewList(list, null, true, resp, req);
    }

    public void editProject(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("editProject param=" + param);
        String uid = null;
        String projectID = null;
        String projectName = null;
        String projectDesp = null;
        String stime = null;
        String etime = null;
        String center = null;
        String projectEngName = null;
        String unit = null;
        String manager = null;
        String type = null;
        String registerNumber = null;
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
            uid = jsonObject.get("uid").getAsString();
            projectID = jsonObject.get("projectID").getAsString();
            projectName = jsonObject.get("projectName").getAsString();
            projectDesp = jsonObject.get("projectDesp").getAsString();
            if(jsonObject.get("starttime") != null){
                stime = jsonObject.get("starttime").getAsString();
                Date startTime = null;
                if(null != stime && !"".equals(stime)){
                    startTime = time.parse(stime);
                }

                map.put("startTime",startTime);
            }
            if(jsonObject.get("endtime") != null){
                etime = jsonObject.get("endtime").getAsString();
                Date endTime = null;
                if(null !=etime &&  !"".equals(etime)){
                    endTime = time.parse(etime);
                }
                map.put("endtime",endTime);
            }
            if(jsonObject.get("projectEngName") != null){
                projectEngName = jsonObject.get("endtime").getAsString();
                map.put("projectEngName",projectEngName);
            }
            if(jsonObject.get("manager") != null){
                manager = jsonObject.get("manager").getAsString();
                map.put("manager",manager);
            }
            if(jsonObject.get("type") != null){
                type = jsonObject.get("type").getAsString();
                map.put("type",type);
            }
            if(jsonObject.get("registerNumber") != null){
                registerNumber = jsonObject.get("registerNumber").getAsString();
                map.put("registerNumber",registerNumber);
            }
            if(jsonObject.get("center") != null){
                registerNumber = jsonObject.get("center").getAsString();
                map.put("center",center);
            }
            if(jsonObject.get("unit") != null){
                registerNumber = jsonObject.get("unit").getAsString();
                map.put("unit",unit);
            }
            map.put("uid",uid);
            map.put("projectID",projectID);
            map.put("projectName",projectName);
            map.put("projectDesp",projectDesp);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        int count = AllDao.getInstance().getProjectDao().updateProject(map);
        ProLog proLog = new ProLog();
        proLog.setProjectID(projectID);
        proLog.setUid(uid);
        proLog.setAction(LogActionEnum.UpdateProject.getName());
        SyUser syUser = UserProcessor.getUser(uid);
        proLog.setLogText(syUser.getUname() + LogActionEnum.UpdateProject.getName());
        proLog.setLogTime(new Date());
        AllDao.getInstance().getProjectDao().insertProLog(proLog);
        if(count == 1){
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setInfo("保存成功");
            viewer.viewString(gson.toJson(resultBean),resp,req);
        }else{
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(0);
            resultBean.setInfo("没有找到项目信息");
            viewer.viewString(gson.toJson(resultBean),resp,req);
        }

    }
}
