package com.gennlife.platform.view;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.conf.ConfItem;
import com.gennlife.platform.util.GsonUtil;
import com.google.gson.*;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by chensong on 2015/12/19.
 */
public class View {
    private Logger logger = LoggerFactory.getLogger(View.class);
    private static Gson gson = GsonUtil.getGson();
    private static final JsonFactory jsonFactory = new JsonFactory();

    public void setHttpServletResponse(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://angular.js");
        response.setHeader("Access-Control-Allow-Methods","GET,PUT,POST,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setContentType("application/json");
    }
    public void writeResult(String reuslt,HttpServletResponse response,HttpServletRequest request){
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(reuslt);
            writer.flush();
        } catch (IOException e) {
            logger.error("",e);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(0);
            resultBean.setMsg("请求出错");
            writer.write(gson.toJson(resultBean));
        }finally {
            writer.close();
        }
    }
    public void viewString(String str,HttpServletResponse response,HttpServletRequest request){
        logger.info("结果:" + str);
        setHttpServletResponse(response);
        writeResult(str, response, request);
    }

    public void viewList(List<?> list, Object info, Boolean flag, HttpServletResponse response, HttpServletRequest request){
        setHttpServletResponse(response);
        ResultBean userBean = new ResultBean();
        if(flag){
            userBean.setCode(1);
        }else{
            userBean.setCode(0);
        }
        userBean.setData(list);
        userBean.setInfo(info);
        String jsonString = gson.toJson(userBean);
        logger.info("返回数据:"+jsonString);
        writeResult(jsonString, response, request);
    }

    public String ViewBio(Map<String, Map<String, Map<String, String>>> map, List<String> groupNames,Map<String,String> groupData,String groupName){
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
            jg.writeStartObject();//mark-0
            jg.writeArrayFieldStart("list");//mark-1
            for(String group:groupNames){
                jg.writeString(group);
            }
            jg.writeEndArray();//mark-1

            jg.writeObjectFieldStart("data");//mark-4
            jg.writeStringField("name", groupName);
            jg.writeArrayFieldStart("group");//mark-2
            for(String name:groupData.keySet()){
                jg.writeStartObject();//mark-3
                jg.writeStringField("name", name);
                jg.writeStringField("value", groupData.get(name) == null? "":groupData.get(name));
                jg.writeEndObject();//mark-3
            }
            jg.writeEndArray();//mark-2
            jg.writeArrayFieldStart("items");//mark-5
            for(String group1:map.keySet()){
                jg.writeStartObject();//mark-6
                jg.writeStringField("name", group1);//原发肿瘤等
                jg.writeArrayFieldStart("items");//mark-7
                Map<String, Map<String, String>> tmp = map.get(group1);
                for(String group2:tmp.keySet()){
                    jg.writeStartObject();//mark-8
                    jg.writeStringField("name", group2);//肿瘤组织等
                    Map<String,String> tmpLowMap = tmp.get(group2);
                    jg.writeArrayFieldStart("items");//mark-9
                    for(String name:tmpLowMap.keySet()){
                        jg.writeStartObject();//mark-10
                        jg.writeStringField("name", name);//具体值
                        jg.writeStringField("value", tmpLowMap.get(name));//具体值
                        jg.writeEndObject();//mark-10
                    }
                    jg.writeEndArray();//mark-9
                    jg.writeEndObject();//mark-8
                }
                jg.writeEndArray();//mark-7
                jg.writeEndObject();//mark-6
            }
            jg.writeEndArray();//mark-5
            jg.writeEndObject();//mark-4

            jg.writeEndObject();//mark-0
            jg.close();
            return sw.toString();
        } catch (IOException e) {
           logger.error("",e);
        }
        return null;

    }

    public String viewSummary(Map<String,Map<String,String>> resultMap,List<String> groupNames,String groupName){
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
            jg.writeStartObject();//mark-0
            jg.writeArrayFieldStart("list");//mark-1
            for(String group:groupNames){
                jg.writeString(group);
            }
            jg.writeEndArray();//mark-1
            jg.writeObjectFieldStart("data");//mark-3
            jg.writeStringField("name", groupName);
            jg.writeArrayFieldStart("items");//mark-4
            for(String subGroupName:resultMap.keySet()){
                jg.writeStartObject();//mark-6
                jg.writeStringField("name", subGroupName);
                jg.writeArrayFieldStart("items");//mark-5
                for(String name:resultMap.get(subGroupName).keySet()){
                    String value = resultMap.get(subGroupName).get(name);
                    jg.writeStartObject();//mark-7
                    jg.writeStringField("name", name);
                    jg.writeStringField("value", value == null ? "" : value);
                    jg.writeEndObject();//mark-7
                }

                jg.writeEndArray();//mark-5
                jg.writeEndObject();//mark-6
            }

            jg.writeEndArray();//mark-4
            jg.writeEndObject();//mark-3
            jg.writeEndObject();//mark-0
            jg.close();
            return sw.toString();
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    public String viewClinicalOrFollowup(Map<String,String> resultMap,List<String> groupNames,String groupName){
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
            jg.writeStartObject();//mark-0
            jg.writeArrayFieldStart("list");//mark-1
            for(String group:groupNames){
                jg.writeString(group);
            }
            jg.writeEndArray();//mark-1
            jg.writeObjectFieldStart("data");//mark-3
            jg.writeStringField("name", groupName);
            jg.writeArrayFieldStart("items");//mark-4
            for(String name:resultMap.keySet()){
                String value = resultMap.get(name);
                jg.writeStartObject();//mark-6
                jg.writeStringField("name", name);
                jg.writeStringField("value", value == null ? "" : value);
                jg.writeEndObject();//mark-6
            }

            jg.writeEndArray();//mark-4
            jg.writeEndObject();//mark-3
            jg.writeEndObject();//mark-0
            jg.close();
            return sw.toString();
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }


    public String viewGene(List<String> groupNames,String groupName,List<Map<String,String>> indexResultList,List<String> dataHeadList){
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
            jg.writeStartObject();//mark-0
            jg.writeArrayFieldStart("list");//mark-1
            for(String group:groupNames){
                jg.writeString(group);
            }
            jg.writeEndArray();//mark-1
            jg.writeObjectFieldStart("data");//mark-3
            jg.writeStringField("name", groupName);
            jg.writeArrayFieldStart("head");//mark-4
            for(String head:dataHeadList){
                String[] data = head.split(",");
                jg.writeString(data[0]);
            }
            jg.writeEndArray();//mark-4
            jg.writeArrayFieldStart("data");//mark-5
            for(Map<String,String> map:indexResultList){
                jg.writeStartArray();//mark-6
                for(String head:dataHeadList){
                    String[] data = head.split(",");
                    jg.writeString(map.get(data[1]) == null ? "" : map.get(data[1]));
                }
                jg.writeEndArray();//mark-6
            }
            jg.writeEndArray();//mark-5
            jg.writeEndObject();//mark-3
            jg.writeEndObject();//mark-0
            jg.close();
            return sw.toString();
        } catch (IOException e) {
            logger.error("", e);
        }
        return  null;
    }

    public String ViewDetailSet( Map<String,JsonObject> headMap, JsonArray dataArray ) {
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
            jg.writeStartObject();//mark-0
            jg.writeArrayFieldStart("head");//mark-1
            for(String id:headMap.keySet()){
                jg.writeStartObject();//mark-2
                JsonObject headObj = headMap.get(id);
                jg.writeStringField("key", headObj.get("key").getAsString());
                jg.writeStringField("UIFieldName", headObj.get("UIFieldName").getAsString());
                jg.writeStringField("type", headObj.get("type").getAsString());
                jg.writeEndObject();//mark-2
            }
            jg.writeEndArray();//mark-1
            jg.writeArrayFieldStart("data");//mark-2
            for(JsonElement item:dataArray){
                jg.writeStartArray();//mark-3
                JsonObject itemObj = item.getAsJsonObject();
                for(String id:headMap.keySet()){
                    String value = "";
                    if(itemObj.get(id)!= null){
                        value = itemObj.get(id).getAsString();
                    }
                    jg.writeString(value);
                }
                jg.writeEndArray();//mark-3
            }
            jg.writeEndArray();//mark-2
            jg.writeEndObject();//mark-0
            jg.close();
            return sw.toString();
        } catch (IOException e) {
            logger.error("", e);
        }

        return null;
    }
}
