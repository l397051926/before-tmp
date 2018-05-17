package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.projectBean.*;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.enums.LogActionEnum;
import com.gennlife.platform.model.User;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

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
    private static SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();


    /**
     * {}
     * 我的项目中 项目列表
     *
     * @param jsonObject
     */
    public String myProjectList(JsonObject jsonObject) {
        try {
            String uid = jsonObject.get("uid").getAsString();
            String limit = jsonObject.get("limit").getAsString();
            String key = jsonObject.get("key").getAsString();
            int[] result = ParamUtils.parseLimit(limit);
            int startIndex = result[0];
            int maxNum = result[1];
            Map<String, Object> conf = new HashMap<String, Object>();
            conf.put("loginname", uid);
            conf.put("startIndex", (startIndex - 1) * maxNum);
            conf.put("maxNum", maxNum);
            conf.put("key", key);
            List<MyProjectList> list = null;
            int counter = 0;
            Long start = System.currentTimeMillis();
            list = AllDao.getInstance().getProjectDao().getMyProjectList(conf);
            Long start1 = System.currentTimeMillis();
            logger.debug("getMyProjectList mysql耗时:" + (start1 - start) + "ms");
            //getSyUserDao().getMyProjectList(conf);
            counter = AllDao.getInstance().getProjectDao().getProjectCounter(conf);
            Long start2 = System.currentTimeMillis();
            logger.debug("getProjectCounter mysql耗时:" + (start2 - start1) + "ms");
            Map<String, Object> confMap = new HashMap<String, Object>();
            for (MyProjectList myProjectList : list) {
                String projectID = myProjectList.getProjectID();
                String creator = myProjectList.getCreator();

                if (creator != null) {
                    User user = AllDao.getInstance().getSyUserDao().getUserByUid(creator);
                    if (user != null) {
                        myProjectList.setCreatorName(user.getUname() + "");
                    }

                }
                confMap.put("projectID", projectID);
                confMap.put("startIndex", 0);
                confMap.put("maxNum", 5);
                List<ProLog> logList = AllDao.getInstance().getProjectDao().getProjectLog(confMap);
                for (ProLog proLog : logList) {
                    proLog.setSampleName(null);
                    proLog.setProjectID(null);
                }
                myProjectList.setLogs(logList);
            }
            Long start3 = System.currentTimeMillis();
            logger.debug("填充日志 mysql耗时:" + (start3 - start2) + "ms");
            Map<String, Integer> info = new HashMap<String, Integer>();
            info.put("counter", counter);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(list);
            resultBean.setInfo(info);
            return gson.toJson(resultBean);

        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }

    }

    /**
     * 创建新项目
     *
     * @param param
     */
    public String createNewProject(String param) {
        logger.info("createNewProject param=" + param);
        CreateProjectQueryBean createProjectQueryBean = null;
        try {
            createProjectQueryBean = JsonUtils.parseCreateProject(param);
            CreateProject createProject = createProjectQueryBean.getCreateProject();
            List<ProUser> proUserList = createProjectQueryBean.getProUserList();
            ProLog proLog = createProjectQueryBean.getProlog();
            int counter = AllDao.getInstance().getProjectDao().insertCreateProject(createProject);
            counter = AllDao.getInstance().getProjectDao().insertProUserList(proUserList);
            counter = AllDao.getInstance().getProjectDao().insertProLog(proLog);
        } catch (ParseException e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(createProjectQueryBean);
        return gson.toJson(resultBean);
    }

    /**
     * 项目详情
     *
     * @param request
     * @param resps
     */
    public void projectDetail(HttpServletRequest request, HttpServletResponse resps) {

    }

    /**
     * 创建一个新方案（工作流）
     *
     * @param param
     */
    public String createNewPlan(String param, User user) {
        ProjectPlan projectPlan = JsonUtils.parseCreatePlan(param, false);
        projectPlan.setPlanStatus(1);//初建状态
        int counter = AllDao.getInstance().getProjectDao().insertProPlan(projectPlan);
        user = AllDao.getInstance().getSyUserDao().getUserByUid(projectPlan.getCreator());
        ProLog proLog = new ProLog();
        proLog.setProjectID(projectPlan.getProjectID());
        proLog.setPlanName(projectPlan.getPlanName());
        proLog.setUid(projectPlan.getCreator());
        proLog.setAction(LogActionEnum.CreatePlan.getName());
        proLog.setLogText(user.getUname() + LogActionEnum.CreatePlan.getName() + projectPlan.getPlanName());
        proLog.setLogTime(new Date());
        AllDao.getInstance().getProjectDao().insertProLog(proLog);
        ResultBean resultBean = new ResultBean();
        if (counter == 1) {
            resultBean.setCode(1);
        } else {
            resultBean.setCode(2);
        }
        return gson.toJson(resultBean);
    }

    /**
     * 删除一个新方案（工作流）
     *
     * @param param
     */
    public String deletePlan(String param, User user) {
        try {
            logger.info("deletePlan param=" + param);
            JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
            String projectID = jsonObject.get("projectID").getAsString();
            String uid = jsonObject.get("uid").getAsString();
            JsonArray idSet = jsonObject.get("idSet").getAsJsonArray();
            Map<String, Object> mapConf = new HashMap<String, Object>();
            int counter = 0;
            mapConf.put("projectID", projectID);
            for (JsonElement id : idSet) {
                mapConf.put("id", id.getAsString());
                String planName = AllDao.getInstance().getProjectDao().getPlanName(mapConf);
                counter = counter + AllDao.getInstance().getProjectDao().deleteProPlan(mapConf);
                ProLog proLog = new ProLog();
                proLog.setProjectID(projectID);
                proLog.setUid(uid);
                proLog.setAction(LogActionEnum.DeletePlan.getName());
                proLog.setLogText(user.getUname() + LogActionEnum.DeletePlan.getName() + planName);
                proLog.setLogTime(new Date());
                AllDao.getInstance().getProjectDao().insertProLog(proLog);
            }
            if (counter >= 1) {
                ResultBean resultBean = new ResultBean();
                resultBean.setCode(1);
                resultBean.setInfo("成功删除" + counter + "个项目");
                return gson.toJson(resultBean);
            } else {
                ResultBean resultBean = new ResultBean();
                resultBean.setCode(0);
                resultBean.setInfo("删除项目失败");
                return gson.toJson(resultBean);
            }

        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }

    }

    /**
     * 删除项目
     *
     * @param param
     */
    public String deleteProject(String param, User user) {
        logger.info("deleteProject param=" + param);
        JsonArray ids = null;
        String uid = null;
        try {
            JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
            uid = jsonObject.get("uid").getAsString();
            ids = jsonObject.get("projectIDSet").getAsJsonArray();
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数异常");
        }
        Map<String, Object> confMap = new HashMap<String, Object>();
        int c = 0;
        confMap.put("uid", uid);
        for (JsonElement jsonElement : ids) {
            String projectID = jsonElement.getAsString();
            confMap.put("projectID", projectID);
            int count = AllDao.getInstance().getProjectDao().deleteProject(confMap);
            AllDao.getInstance().getProjectDao().deleteSampleByProjectID(projectID);
            AllDao.getInstance().getProjectDao().deletePlanByProjectID(projectID);
            AllDao.getInstance().getProjectDao().deleteMemberByProjectID(projectID);
            c = c + count;
            if (count == 1) {
                ProLog proLog = new ProLog();
                proLog.setUid(uid);
                proLog.setProjectID(projectID);
                proLog.setAction(LogActionEnum.ExitProject.getName());
                proLog.setLogTime(new Date());
                proLog.setLogText(user.getUname() + LogActionEnum.ExitProject.getName());
                AllDao.getInstance().getProjectDao().insertProLog(proLog);
            }
        }

        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo("成功删除" + c + "个项目");
        return gson.toJson(resultBean);

    }

    /**
     * 删除数据集
     *
     * @param param
     */
    public String deleteSet(String param, User user) {
        logger.info("deleteSet param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String uid = jsonObject.get("uid").getAsString();
        JsonArray users = jsonObject.getAsJsonArray("uris");
        Date createTime = new Date();
        int counter = 0;
        for (JsonElement entry : users) {
            String uri = entry.getAsString();
            Map<String, Object> confMap = new HashMap<String, Object>();
            confMap.put("projectID", projectID);
            confMap.put("uri", uri);
            String setName = AllDao.getInstance().getProjectDao().getProjectSampleName(projectID, uri);
            if (StringUtils.isEmpty(setName)) {
                return ParamUtils.errorParam("sample不存在");
            }
            ProLog proLog = new ProLog();
            proLog.setUid(uid);
            proLog.setProjectID(projectID);
            proLog.setAction(LogActionEnum.DeleteSamples.getName());
            proLog.setLogTime(createTime);
            proLog.setLogText(user.getUname() + LogActionEnum.DeleteSamples.getName() + setName);
            AllDao.getInstance().getProjectDao().insertProLog(proLog);
            counter = counter + AllDao.getInstance().getProjectDao().deleteProjectSet(confMap);
            AllDao.getInstance().getProjectDao().autoDeleteSetCountOne(projectID);
        }
        Map<String, Integer> info = new HashMap<String, Integer>();
        info.put("counter", counter);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);
    }


    /**
     * 工作区的工具列表
     */
    public String tools() {
        String content = HttpRequestUtils.httpGet(ConfigurationService.getUrlBean().getToolsURL());
        return content;
    }


    /**
     * 某次动作的接口
     *
     * @param param
     */
    public String aTool(String param) {
        logger.info("aTool param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        StringBuffer newParam = new StringBuffer();
        String uri = null;
        String tool_name = null;
        String row = null;
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = null;
            if (entry.getValue().isJsonObject()) {
                value = entry.getValue().getAsJsonObject().toString();
            } else if (entry.getValue().isJsonArray()) {
                value = entry.getValue().getAsJsonArray().toString();
            } else if (entry.getValue().getAsBoolean()) {
                value = entry.getValue().getAsBoolean() + "";
            } else {
                value = entry.getValue().getAsString();
            }
            if ("uri".equals(key)) {
                uri = value;
            } else if ("tool_name".equals(key)) {
                tool_name = value;
            } else if ("row".equals(key)) {
                row = value;
            }
            newParam.append("&").append(key).append("=").append(ParamUtils.encodeURI(value));
        }
        logger.info("aTool uri=" + uri);
        logger.info("aTool tool_name=" + tool_name);
        logger.info("aTool newParam=" + newParam);
        String url = ConfigurationService.getUrlBean().getAtoolURL() + "?" + newParam.toString();
        logger.info("aTool url=" + url);
        String content = HttpRequestUtils.httpGetForCS(url);
        logger.info("aTool 结果:" + content);
        return content;
    }

    /**
     * 数据集合存储
     *
     * @param request
     * @param resp
     */
    public void storeSet(HttpServletRequest request, HttpServletResponse resp) {

    }

    /**
     * 用户相关项目的列表不分页
     * @param paramObj
     * @return
     */
    public String projectListNoPage(JsonObject paramObj, HttpServletRequest paramRe) {
        String uid = null,crf_id = null;
        boolean flag = false;
        Map<String, Object> conf = new HashMap<>();
        ResultBean resultBean = new ResultBean();

        try {
            uid = paramObj.get("uid").getAsString();
            flag = paramObj.has("crf_id");//是否含有crf_id
            if (flag){
                crf_id = paramObj.get("crf_id").getAsString();
                conf.put("crf_id", crf_id);
            }
            conf.put("uid",uid);
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        try {
            List<MyProjectList> list = AllDao.getInstance().getProjectDao().getProjectList(conf);
            List<JsonObject> paramList = new LinkedList<>();
            if (flag) {
                for (MyProjectList myProjectList : list) {
                    String pro_crfId = myProjectList.getCrfId();
                    //含有crf_id字段，同时项目病种相同，或者项目为空 可导入
                    if (pro_crfId.equals(crf_id) || StringUtils.isEmpty(pro_crfId)) {
                        JsonObject newParamObj = new JsonObject();
                        String projectID = myProjectList.getProjectID();
                        String projectName = myProjectList.getProjectName();

                        newParamObj.addProperty("crfId",crf_id);
                        newParamObj.addProperty("projectID", projectID);
                        newParamObj.addProperty("projectName", projectName);
                        paramList.add(newParamObj);
                    }
                }
            } else {
                for (MyProjectList myProjectList : list) {
                    //如果是emr数据则导入crf_id为空的项目
                    if (StringUtils.isEmpty(myProjectList.getCrfId())){
                        JsonObject newParamObj = new JsonObject();
                        String projectID = myProjectList.getProjectID();
                        String projectName = myProjectList.getProjectName();

                        newParamObj.addProperty("projectID", projectID);
                        newParamObj.addProperty("projectName", projectName);
                        paramList.add(newParamObj);
                    }
                }
            }
            resultBean.setCode(1);
            resultBean.setData(paramList);
            return gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    /**
     * 数据区数据集列表
     *
     * @param param
     */
    public String setList(String param) {
        logger.info("setList param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        Map<String, Object> conf = new HashMap<String, Object>();
        conf.put("projectID", projectID);
        List<ProSample> list = AllDao.getInstance().getProjectDao().getSampleDataInitList(conf);
        Map<String, Object> result = new HashMap<String, Object>();
        for (ProSample proSample : list) {
            if (null != proSample.getOpTime()) {
                proSample.setOperatTime((time.format(proSample.getOpTime())));
            }
            if (proSample.getSampleDesc() == null) {
                proSample.setSampleDesc("");
            }
            proSample.setOpTime(null);
            proSample.setProjectID(null);
            proSample.setPlanName(null);
            List<ProSample> setBeanList = (List<ProSample>) result.get("CollectionOfInit");
            if (null == setBeanList) {
                setBeanList = new LinkedList<ProSample>();
            }
            setBeanList.add(proSample);
            result.put("CollectionOfInit", setBeanList);
        }
        List<ProjectPlan> plansList = AllDao.getInstance().getProjectDao().getProjectPlan(conf);
        for (ProjectPlan projectPlan : plansList) {
            String planName = projectPlan.getPlanName();
            if (planName == null) {
                continue;
            }
            conf.put("planName", planName);
            list = AllDao.getInstance().getProjectDao().getSampleDataListByPlanName(conf);
            if (list == null || list.isEmpty()) {
                result.put(planName, new LinkedList<String>());
            }
            for (ProSample proSample : list) {
                if (null != proSample.getOpTime()) {
                    proSample.setOperatTime((time.format(proSample.getOpTime())));
                }
                if (proSample.getSampleDesc() == null) {
                    proSample.setSampleDesc("");
                }
                proSample.setOpTime(null);
                proSample.setProjectID(null);
                List<ProSample> setBeanList = (List<ProSample>) result.get(proSample.getPlanName());
                if (null == setBeanList) {
                    setBeanList = new LinkedList<ProSample>();
                }
                setBeanList.add(proSample);
                result.put(proSample.getPlanName(), setBeanList);
            }
        }
        return gson.toJson(result);
    }

    /**
     * 导出
     *
     * @param request
     * @param resp
     */
    public void exportResult(HttpServletRequest request, HttpServletResponse resp) {

    }

    /**
     * 项目日志列表
     *
     * @param jsonObject
     */
    public String projectLogList(JsonObject jsonObject) {
        try {
            String projectID = jsonObject.get("projectID").getAsString();
            String limit = "0,5";
            if (jsonObject.get("limit") != null) {
                limit = jsonObject.get("limit").getAsString();
            }
            int[] ls = ParamUtils.parseLimit(limit);
            Map<String, Object> confMap = new HashMap<String, Object>();
            confMap.put("projectID", projectID);
            confMap.put("startIndex", (ls[0] - 1) * ls[1]);
            confMap.put("maxNum", ls[1]);
            List<ProLog> logList = AllDao.getInstance().getProjectDao().getProjectLog(confMap);
            int counter = AllDao.getInstance().getProjectDao().getProjectLogCounter(confMap);
            Map<String, Integer> info = new HashMap<String, Integer>();
            info.put("counter", counter);
            ResultBean userBean = new ResultBean();
            userBean.setCode(1);
            userBean.setData(logList);
            userBean.setInfo(info);
            return gson.toJson(userBean);
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }

    }

    /**
     * 项目方案列表
     *
     * @param jsonObject
     */
    public String projectPlanList(JsonObject jsonObject) {
        ResultBean resultBean = new ResultBean();
        Map<String, Object> confMap = new HashMap<String, Object>();
        try {
            String projectID = jsonObject.get("projectID").getAsString();
            String limit = "0,5";
            if (jsonObject.get("limit") != null) {
                limit = jsonObject.get("limit").getAsString();
            }
            int[] ls = ParamUtils.parseLimit(limit);
            confMap.put("startIndex", (ls[0] - 1) * ls[1]);
            confMap.put("maxNum", ls[1]);
            confMap.put("projectID", projectID);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数异常");
        }
        List<ProjectPlan> plansList = AllDao.getInstance().getProjectDao().getProjectPlan(confMap);
        for (ProjectPlan projectPlan : plansList) {
            String creator = projectPlan.getCreator();
            User cUser = AllDao.getInstance().getSyUserDao().getUserByUid(creator);
            if (cUser != null) {
                projectPlan.setCreatorName(cUser.getUname());
            }
        }
        int count = AllDao.getInstance().getProjectDao().getProjectPlanCounter(confMap);
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("count", count);
        resultBean.setCode(1);
        resultBean.setData(plansList);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);
    }

    /**
     * 项目成员列表
     *
     * @param jsonObject
     */
    public String projectMemberList(JsonObject jsonObject) {
        String projectID = jsonObject.get("projectID").getAsString();
        logger.info("projectID=" + projectID);
        Map<String, Object> confMap = new HashMap<String, Object>();
        confMap.put("projectID", projectID);
        List<User> list = AllDao.getInstance().getProjectDao().getProjectMemberList(confMap);
        int counter = AllDao.getInstance().getProjectDao().getProjectMemberCounter(confMap);
        //.getSyUserDao().getProjectMemberCounter(confMap);
        Map<String, Integer> info = new HashMap<String, Integer>();
        info.put("counter", counter);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(list);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);
    }

    /**
     * 项目添加用户
     *
     * @param param
     */
    public String addMember(String param, User user) {
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        JsonArray users = jsonObject.getAsJsonArray("uidSet");
        String uid = jsonObject.get("uid").getAsString();
        int counter = 0;
        for (JsonElement entry : users) {
            String uidAdd = entry.getAsString();
            Map<String, Object> confMap = new HashMap<String, Object>();
            confMap.put("projectID", projectID);
            confMap.put("uid", uidAdd);
            counter = counter + AllDao.getInstance().getProjectDao().insertProjectMember(confMap);
            ProLog proLog = new ProLog();
            proLog.setProjectID(projectID);
            proLog.setUid(uid);
            proLog.setAction(LogActionEnum.AddProjectMember.getName());
            User syAdd = AllDao.getInstance().getSyUserDao().getUserByUid(uidAdd);
            proLog.setLogText(user.getUname() + LogActionEnum.AddProjectMember.getName() + syAdd.getUname());
            proLog.setLogTime(new Date());
            AllDao.getInstance().getProjectDao().insertProLog(proLog);
        }
        Map<String, Integer> info = new HashMap<String, Integer>();
        info.put("counter", counter);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);
    }

    /**
     * 删除项目成员
     *
     * @param param
     */
    public String deleteMember(String param, User user) {
        logger.info("deleteMemeber param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String uid = jsonObject.get("uid").getAsString();
        JsonArray users = jsonObject.getAsJsonArray("uidSet");
        int counter = 0;
        for (JsonElement entry : users) {
            String uidDelete = entry.getAsString();
            Map<String, Object> confMap = new HashMap<String, Object>();
            confMap.put("projectID", projectID);
            confMap.put("uid", uidDelete);
            counter = counter + AllDao.getInstance().getProjectDao().deleteProjectMember(confMap);
            ProLog proLog = new ProLog();
            proLog.setProjectID(projectID);
            proLog.setUid(uid);
            proLog.setAction(LogActionEnum.DeleteProjectMember.getName());
            User syDelete = AllDao.getInstance().getSyUserDao().getUserByUid(uidDelete);
            proLog.setLogText(user.getUname() + LogActionEnum.DeleteProjectMember.getName() + syDelete.getUname());
            proLog.setLogTime(new Date());
            AllDao.getInstance().getProjectDao().insertProLog(proLog);
        }
        Map<String, Integer> info = new HashMap<String, Integer>();
        info.put("counter", counter);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);

    }

    /**
     * 编辑方案
     *
     * @param param
     */
    public String editPlan(String param) {
        logger.info("editPlan param=" + param);
        ProjectPlan projectPlan = null;
        ResultBean resultBean = new ResultBean();
        try {
            projectPlan = JsonUtils.parseCreatePlan(param, true);
            projectPlan.setPlanStatus(2);//初建状态
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
        int counter = AllDao.getInstance().getProjectDao().updateProPlan(projectPlan);
        if (counter == 1) {
            resultBean.setCode(1);
        } else {
            resultBean.setCode(0);
        }
        return gson.toJson(resultBean);
    }


    public String setNameList(String param) {
        logger.info("setNameList param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("projectID", projectID);
        List<String> list = AllDao.getInstance().getProjectDao().getProjectSetNameList(map);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(list);
        return gson.toJson(resultBean);
    }

    public String editProject(String param, User user) {
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
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
            uid = jsonObject.get("uid").getAsString();
            projectID = jsonObject.get("projectID").getAsString();
            if (jsonObject.get("projectName") != null) {
                projectName = jsonObject.get("projectName").getAsString();
                map.put("projectName", projectName);
            }
            if (jsonObject.get("projectDesp") != null) {
                projectDesp = jsonObject.get("projectDesp").getAsString();
                map.put("projectDesp", projectDesp);
            }

            if (jsonObject.get("startTime") != null) {
                stime = jsonObject.get("startTime").getAsString();
                Date startTime = null;
                if (null != stime && !"".equals(stime)) {
                    startTime = day.parse(stime);
                }
                map.put("startTime", startTime);
            }
            if (jsonObject.get("endTime") != null) {
                etime = jsonObject.get("endTime").getAsString();
                Date endTime = null;
                if (null != etime && !"".equals(etime)) {
                    endTime = day.parse(etime);
                }
                map.put("endTime", endTime);
            }
            if (jsonObject.get("projectEngName") != null) {
                projectEngName = jsonObject.get("projectEngName").getAsString();
                map.put("projectEngName", projectEngName);
            }
            if (jsonObject.get("manager") != null) {
                manager = jsonObject.get("manager").getAsString();
                map.put("manager", manager);
            }
            if (jsonObject.get("type") != null) {
                type = jsonObject.get("type").getAsString();
                map.put("type", type);
            }
            if (jsonObject.get("registerNumber") != null) {
                registerNumber = jsonObject.get("registerNumber").getAsString();
                map.put("registerNumber", registerNumber);
            }
            if (jsonObject.get("center") != null) {
                center = jsonObject.get("center").getAsString();
                map.put("center", center);
            }
            if (jsonObject.get("unit") != null) {
                unit = jsonObject.get("unit").getAsString();
                map.put("unit", unit);
            }
            map.put("uid", uid);
            map.put("projectID", projectID);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数异常");
        }
        int count = AllDao.getInstance().getProjectDao().updateProject(map);
        ProLog proLog = new ProLog();
        proLog.setProjectID(projectID);
        proLog.setUid(uid);
        proLog.setAction(LogActionEnum.UpdateProject.getName());
        proLog.setLogText(user.getUname() + LogActionEnum.UpdateProject.getName());
        proLog.setLogTime(new Date());
        AllDao.getInstance().getProjectDao().insertProLog(proLog);
        if (count == 1) {
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setInfo("保存成功");
            return gson.toJson(resultBean);
        } else {
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(0);
            resultBean.setInfo("没有找到项目信息");
            return gson.toJson(resultBean);
        }

    }

    public String isExistProject(JsonObject jsonObject) {
        String uid = null;
        String projectName = null;
        Map<String, Object> map = new HashMap<String, Object>();
        ResultBean resultBean = new ResultBean();
        try {
            uid = jsonObject.get("uid").getAsString();
            projectName = jsonObject.get("projectName").getAsString();
            map.put("uid", uid);
            map.put("projectName", projectName);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数异常");
        }
        int count = AllDao.getInstance().getProjectDao().isExistProject(map);
        resultBean.setCode(1);
        Map<String, Integer> result = new HashMap<String, Integer>();
        if (count >= 1) {
            result.put("count", 1);
        } else {
            result.put("count", 0);
        }
        resultBean.setData(result);
        return gson.toJson(resultBean);
    }

    public String isExistPlan(JsonObject jsonObject) {
        String projectID = null;
        String planName = null;
        Map<String, Object> map = new HashMap<String, Object>();
        ResultBean resultBean = new ResultBean();
        try {
            projectID = jsonObject.get("projectID").getAsString();
            planName = jsonObject.get("planName").getAsString();
            map.put("projectID", projectID);
            map.put("planName", planName);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数异常");
        }
        int count = AllDao.getInstance().getProjectDao().isExistPlan(map);
        resultBean.setCode(1);
        Map<String, Integer> result = new HashMap<String, Integer>();
        if (count >= 1) {
            result.put("count", 1);
        } else {
            result.put("count", 0);
        }
        resultBean.setData(result);
        return gson.toJson(resultBean);

    }

    public String isExistSet(JsonObject jsonObject) {
        String projectID = null;
        String sampleName = null;
        Map<String, Object> map = new HashMap<String, Object>();
        ResultBean resultBean = new ResultBean();
        try {
            projectID = jsonObject.get("projectID").getAsString();
            sampleName = jsonObject.get("sampleName").getAsString();
            map.put("projectID", projectID);
            map.put("sampleName", sampleName);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数异常");
        }
        int count = AllDao.getInstance().getProjectDao().isExistSet(map);
        resultBean.setCode(1);
        Map<String, Integer> result = new HashMap<String, Integer>();
        if (count >= 1) {
            result.put("count", 1);
        } else {
            result.put("count", 0);
        }
        resultBean.setData(result);
        return gson.toJson(resultBean);
    }

    /**
     * 项目详情
     *
     * @param jsonObject
     */
    public String baiscInfo(JsonObject jsonObject) {
        String projectID = null;
        Map<String, Object> map = new HashMap<String, Object>();
        ResultBean resultBean = new ResultBean();
        try {
            projectID = jsonObject.get("projectID").getAsString();
            map.put("projectID", projectID);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数异常");
        }
        MyProjectList project = AllDao.getInstance().getProjectDao().baiscInfo(map);
        if (project != null) {
            User creator = AllDao.getInstance().getSyUserDao().getUserByUid(project.getCreator());
            if (creator != null) {
                project.setCreatorName(creator.getUname());
            }
            String disease = project.getDisease();
            String diseaseName = ArkService.getDiseaseName(disease);
            project.setDiseaseName(diseaseName);
        }
        resultBean.setCode(1);
        resultBean.setData(project);
        return gson.toJson(resultBean);
    }

    public String aSurvivalTool(String param) {
        logger.info("aSurvivalTool param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        StringBuffer newParam = new StringBuffer();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = null;
            if (entry.getValue().isJsonObject()) {
                value = entry.getValue().getAsJsonObject().toString();
            } else if (entry.getValue().isJsonArray()) {
                value = entry.getValue().getAsJsonArray().toString();
            } else if (entry.getValue().getAsBoolean()) {
                value = entry.getValue().getAsBoolean() + "";
            } else {
                value = entry.getValue().getAsString();
            }
            newParam.append("&").append(key).append("=").append(ParamUtils.encodeURI(value));
        }
        logger.info("aTool newParam=" + newParam);
        String url = ConfigurationService.getUrlBean().getAtoolURL() + "?" + newParam.toString();
        logger.info("aTool url=" + url);
        String content = HttpRequestUtils.httpGetForCS(url);
        logger.info("aTool 结果:" + content);
        return content;

    }
}
