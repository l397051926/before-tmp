package com.gennlife.platform.util;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
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
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }
    public static void errorParam(HttpServletRequest request, HttpServletResponse resps){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setInfo("请求参数出错");
        viewer.viewString(gson.toJson(resultBean),resps,request);
        return;
    }
}
