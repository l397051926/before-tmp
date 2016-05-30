package com.gennlife.platform.util;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.parse.QueryServerParser;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;
import java.util.*;

/**
 * Created by chensong on 2015/12/9.
 */
public class ParamUtils {
    private static Logger logger = LoggerFactory.getLogger(ParamUtils.class);
    private static Gson gson = GsonUtil.getGson();
    private static View viewer = new View();
    public static String getParam(HttpServletRequest request){
        String param = null;
        if("GET".equals(request.getMethod())){
            param = request.getParameter("param");
        }else{
            param = getPostParm(request);
        }
        return cleanXSS(param);
    }
    private static String getPostParm(HttpServletRequest request){
        StringBuffer jb = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            logger.error("读取请求参数出错",e);
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (Exception e){
                    logger.error("",e);
                }
            }
        }
        return jb.toString();
    }



    public static String encodeURI(String uri) {
        try {
            return URLEncoder.encode(uri, "UTF-8");
        } catch (Exception e) {
            return uri;
        }
    }

    public static int[] parseLimit(String limit){
        String[] strings = limit.split(",");
        int[] result = new int[2];
        result[0] = Integer.parseInt(strings[0]);
        result[1] = Integer.parseInt(strings[1]);
        return result;
    }

    private static String cleanXSS(String value){
        if(value == null){
            return null;
        }
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("<script>", "");
        value = value.replaceAll("<javascript>", "");
        return value;
    }
    public static void errorParam(HttpServletRequest request, HttpServletResponse resps){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setInfo("请求参数出错");
        viewer.viewString(gson.toJson(resultBean),resps,request);
        return;
    }

    public static void errorParam(String info, HttpServletRequest req, HttpServletResponse resp) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setInfo(info);
        String data = gson.toJson(resultBean);
        viewer.viewString(data, resp, req);
    }

    /**
     *
     * @param filters 前端发过来的病历搜索 过滤条件
     * @return
     */
    public static String queryExpression(JsonArray filters){
        StringBuffer queryBuf = new StringBuffer();
        for (JsonElement filterElement : filters) {
            JsonObject filter = filterElement.getAsJsonObject();
            String dataType = filter.get("dataType").getAsString();
            String IndexFieldName = filter.get("IndexFieldName").getAsString();
            int type = filter.get("type").getAsInt();
            JsonArray values = filter.get("values").getAsJsonArray();
            if (queryBuf.length() != 0) {
                queryBuf.append(" ")
                        .append("AND")
                        .append(" ");
            }
            queryBuf.append("(");
            if (type == 0 || type == 1) {//type 取 0 或 1
                int count = 0;
                for (JsonElement valueElement : values) {
                    count++;
                    if (count > 1) {
                        queryBuf.append(" ")
                                .append("OR")
                                .append(" ");
                    }
                    if ("string".equals(dataType)) {
                        String value = valueElement.getAsString();
                        queryBuf.append("[")
                                .append(IndexFieldName)
                                .append("]")
                                .append(" ")
                                .append("包含")
                                .append(" ")
                                .append(value);
                    } else if ("long".equals(dataType)) {
                        Long value = valueElement.getAsLong();
                        queryBuf.append("[")
                                .append(IndexFieldName)
                                .append("]")
                                .append(" ")
                                .append("=")
                                .append(" ")
                                .append(value);
                    }
                }

            } else if (type == 2) {
                int count = 0;
                for (JsonElement valueElement : values) {
                    count++;
                    if (count > 1) {
                        queryBuf.append(" ")
                                .append("OR")
                                .append(" ");
                    }
                    JsonArray subValues = valueElement.getAsJsonArray();
                    int subCount = 0;
                    for (JsonElement subElement : subValues) {
                        subCount++;
                        if ("string".equals(dataType)) {
                            String value = subElement.getAsString();
                            queryBuf.append("[")
                                    .append(IndexFieldName)
                                    .append("]")
                                    .append(" ")
                                    .append("包含")
                                    .append(" ")
                                    .append(value);
                        } else if ("long".equals(dataType)) {
                            Long value = subElement.getAsLong();
                            if (subCount == 1) {
                                queryBuf.append("(")
                                        .append("[")
                                        .append(IndexFieldName)
                                        .append("]")
                                        .append(" ")
                                        .append(">=")
                                        .append(" ")
                                        .append(value)
                                        .append(" ");
                            } else if (subCount == 2) {
                                queryBuf.append(" ")
                                        .append("AND")
                                        .append(" ")
                                        .append("[")
                                        .append(IndexFieldName)
                                        .append("]")
                                        .append(" ")
                                        .append("<")
                                        .append(" ")
                                        .append(value)
                                        .append(")");
                            }

                        } else if ("date".equals(dataType)) {
                            String value = subElement.getAsString();
                            if (subCount == 1) {
                                queryBuf.append("(")
                                        .append("[")
                                        .append(IndexFieldName)
                                        .append("]")
                                        .append(" ")
                                        .append("早于")
                                        .append(" ")
                                        .append(value);
                            } else if (subCount == 2) {
                                queryBuf.append(" ")
                                        .append("AND")
                                        .append(" ")
                                        .append("[")
                                        .append(IndexFieldName)
                                        .append("]")
                                        .append(" ")
                                        .append("晚于")
                                        .append(" ")
                                        .append(value)
                                        .append(")");
                            }
                        }
                    }
                }

            }
            queryBuf.append(")");
        }
        String queryBufStr = queryBuf.toString();
        return queryBufStr;
    }


    public static String buildQuery(JsonObject paramObj) throws Exception {
        logger.info("处理前请求参数=" + gson.toJson(paramObj));
        String from = paramObj.get("from").getAsString();
        String to = paramObj.get("to").getAsString();
        int currentPage = paramObj.get("page").getAsInt();
        int pageSize = paramObj.get("size").getAsInt();
        String query = paramObj.get("query").getAsString();
        StringBuffer sb = new StringBuffer();
        Map<String,String> map = new HashMap<>();
        boolean flag = false;
        for (int i = 0; i < query.length(); i++) {
            char  item =  query.charAt(i);

            if(item == '['){
                flag = true;
            }else if(item == ']'){
                flag = false;
                String uiName = sb.toString();
                String index = ConfigurationService.getIndexFieldName(uiName);
                if(index != null){
                    map.put(uiName,index);
                }
                sb = new StringBuffer();
            }else if(flag && item != '[' && item != ']'){
                sb.append(item);
            }
        }
        for(String uiName:map.keySet()){
            String indexName = map.get(uiName);
            query = query.replaceAll(uiName,indexName);
        }
        logger.info("query替换[] = "+query);
        if ("case".equals(from) && "case".equals(to)) {
            boolean isAdv = paramObj.get("isAdv").getAsBoolean();
            if (!isAdv && !"".equals(query)) {
                QueryServerParser queryServerParser = new QueryServerParser(query);
                Set<String> set = queryServerParser.parser();
                for (String k : set) {
                    query = query + "," + k;
                }
            }
        } else if ("gene".equals(from) && "case".equals(to)) {//基因到病历

        } else if ("variation".equals(from) && "case".equals(to)) {//变异到病历

        } else if ("disease".equals(from) && "case".equals(to)) {//疾病到病历

        }
        JsonArray filters = paramObj.getAsJsonArray("filters");
        String queryBufStr = ParamUtils.queryExpression(filters);
        if(!"".equals(queryBufStr)){
            query = query + " AND " + queryBufStr;
        }

        return query;
    }
}
