package com.gennlife.platform.util;


import com.gennlife.platform.bean.projectBean.*;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.enums.LogActionEnum;
import com.gennlife.platform.enums.MemberEnum;
import com.gennlife.platform.model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.gennlife.platform.util.FileUploadUtil.gson;

/**
 * Created by chensong on 2015/12/7.
 */
public class JsonUtils {
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
    private static JsonParser jsonParser = new JsonParser();


    public static CreateProjectQueryBean parseCreateProject(String param) throws ParseException {
        CreateProjectQueryBean createProjectQueryBean = new CreateProjectQueryBean();
        CreateProject createProject = new CreateProject();
        List<ProUser> proUserList = new LinkedList<ProUser>();
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = UUID.randomUUID().toString();
        String projectName = jsonObject.get("projectName").getAsString();
        String projectEngName = jsonObject.get("projectEngName") == null ? "" : jsonObject.get("projectEngName").getAsString();
        String creator = jsonObject.get("uid").getAsString();
        String stime = jsonObject.get("startTime") == null ? "" : jsonObject.get("startTime").getAsString();
        String etime = jsonObject.get("endTime") == null ? "" : jsonObject.get("endTime").getAsString();
        String center = jsonObject.get("center") == null ? "" : jsonObject.get("center").getAsString();
        String projectDesp = jsonObject.get("projectDesp") != null ? jsonObject.get("projectDesp").getAsString() : "";
        String manager = jsonObject.get("manager") != null ? jsonObject.get("manager").getAsString() : "";
        String unit = jsonObject.get("unit") == null ? "" : jsonObject.get("unit").getAsString();
        String disease = jsonObject.get("disease").getAsString();
        String registerNumber = jsonObject.get("registerNumber") == null ? "" : jsonObject.get("registerNumber").getAsString();
        String type = jsonObject.get("type").getAsString();
        createProject.setCreator(creator);
        ProUser proUser = new ProUser();
        proUser.setProjectID(projectID);
        proUser.setRole(MemberEnum.Creater.getIndex());//创建者
        proUser.setUid(creator);
        proUserList.add(proUser);
        Date startTime = null;
        Date endTime = null;


        if (null != stime && !"".equals(stime)) {
            startTime = time.parse(stime);
        }
        if (null != etime && !"".equals(etime)) {
            endTime = time.parse(etime);
        }
        createProject.setProjectID(projectID);
        createProject.setProjectName(projectName);
        createProject.setProjectEngName(projectEngName);
        createProject.setProjectDesp(projectDesp);
        createProject.setCreator(creator);
        Date createTime = new Date();
        createProject.setCreateTime(createTime);
        createProject.setStartTime(startTime);
        createProject.setEndTime(endTime);
        createProject.setMembers(1);
        createProject.setCenter(center);
        createProject.setSetCount(0);
        createProject.setPlanNum(0);
        createProject.setManager(manager);
        createProject.setUnit(unit);
        createProject.setDisease(disease);
        createProject.setRegisterNumber(registerNumber);
        createProject.setType(type);
        //组建log日志
        User syUser = AllDao.getInstance().getSyUserDao().getUserByUid(creator);
        ProLog proLog = new ProLog();
        proLog.setUid(creator);
        proLog.setProjectID(projectID);
        proLog.setAction(LogActionEnum.CreateProject.getName());
        proLog.setLogTime(createTime);
        proLog.setLogText(syUser.getUname() + LogActionEnum.CreateProject.getName());//文本
        createProjectQueryBean.setProlog(proLog);
        createProjectQueryBean.setCreateProject(createProject);
        createProjectQueryBean.setProUserList(proUserList);
        return createProjectQueryBean;
    }

    public static ProjectPlan parseCreatePlan(String param, boolean iSUpdate) {
        ProjectPlan projectPlan = new ProjectPlan();
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String planName = jsonObject.get("planName").getAsString();
        String desc = jsonObject.get("desc").getAsString();
        String creator = jsonObject.get("creator").getAsString();
        Date createTime = new Date();
        projectPlan.setPlanDesc(desc);
        projectPlan.setPlanName(planName);
        projectPlan.setProjectID(projectID);
        if (iSUpdate) {
            String id = jsonObject.get("id").getAsString();
            projectPlan.setModifier(creator);
            projectPlan.setModifTime(createTime);
            int idInt = Integer.parseInt(id);
            projectPlan.setId(idInt);
        } else {
            projectPlan.setCreator(creator);
            projectPlan.setCreateTime(createTime);
        }
        return projectPlan;
    }

    static public String GetString(JsonObject jobj, String key) {
        String result = "";
        if (!jobj.has(key)) {
            return result;
        }
        Object tmp_data = jobj.get(key);
        if (null == tmp_data) {
            return result;
        }
        if (tmp_data instanceof String) {
            result = jobj.get(key).getAsString();
        } else if (tmp_data instanceof Integer) {
            result = String.valueOf(jobj.get(key).getAsInt());
        } else if (tmp_data instanceof Double) {
            result = String.valueOf(jobj.get(key).getAsDouble());
        } else if (tmp_data instanceof Long) {
            result = String.valueOf(jobj.get(key).getAsLong());
        } else if (tmp_data instanceof Boolean) {
            result = String.valueOf(jobj.get(key).getAsBoolean());
        }
        return result;
    }

    public static JsonObject getJsonObject(String param) {
        try {
            return jsonParser.parse(param).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }


    public static JsonElement toJsonElement(Object obj) {
        if(obj == null) {
            return null;
        } else {
            if(obj instanceof InputStream) {
                toJsonElement((InputStream)obj);
            }

            if(obj instanceof JsonElement) {
                return (JsonElement)obj;
            } else if(obj instanceof String) {
                try {
                    return jsonParser.parse((String)obj);
                } catch (Exception var2) {
                    return null;
                }
            } else {
                return gson.toJsonTree(obj);
            }
        }
    }
    public static <T> T fromJson(JsonElement jsonElement, Type type) {
        return jsonElement == null?null:gson.fromJson(jsonElement, type);
    }

    public static JsonElement toJsonElement(InputStream content) {
        InputStreamReader inputStream = null;
        JsonElement result = null;
        JsonReader reader = null;

        try {
            inputStream = new InputStreamReader(content, "utf-8");
            reader = new JsonReader(inputStream);
            result = jsonParser.parse(reader);
        } catch (Exception var17) {
            logger.error("", var17);
            result = null;
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException var16) {
                    logger.error("", var16);
                }
            }

            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException var15) {
                    logger.error("", var15);
                }
            }

        }

        return result;
    }

}
