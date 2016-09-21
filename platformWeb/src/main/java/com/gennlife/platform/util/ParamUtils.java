package com.gennlife.platform.util;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.net.URLEncoder;

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
        value = value.replaceAll("eval", "");
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

    public static String errorParam(String info){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);
    }

    public static JsonArray indexNameTranformUIName(JsonArray array){
        JsonArray jsonArray = new JsonArray();
        for(JsonElement indexNameElement:array){
            String indexName = indexNameElement.getAsString();
            String uiName = ConfigurationService.getUIFieldName(indexName);
            jsonArray.add(uiName);
        }
        return jsonArray;
    }

    public static ResultBean errorParamResultBean(String info){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setInfo(info);
        return resultBean;
    }

    public static String errorSessionLosParam() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(-1);
        resultBean.setInfo("session失效");
        return gson.toJson(resultBean);
    }
}
