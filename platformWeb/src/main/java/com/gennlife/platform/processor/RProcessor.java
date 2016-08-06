package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.crf.DataBean;
import com.gennlife.platform.bean.crf.SampleListBean;
import com.gennlife.platform.bean.crf.SummaryBean;
import com.gennlife.platform.bean.projectBean.MyProjectList;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import com.mongodb.BasicDBObject;
import org.bson.BSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chen-shuai on 16/7/29.
 */
public class RProcessor {
    private static Logger logger = LoggerFactory.getLogger(RProcessor.class);
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    /**
     * 执行R脚本
     *
     * @param paramObj
     */
    public String r_run(JsonObject paramObj) {
        try{
            String url = ConfigurationService.getUrlBean().getRRun();
            String result = HttpRequestUtils.httpPostForRRun(url,gson.toJson(paramObj));
            return result;
        }catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }

    /**
     * 停止R脚本之行
     * @param paramObj
     */
    public String r_stop(JsonObject paramObj) {
        try{
            String url = ConfigurationService.getUrlBean().getRStop();
            String result = HttpRequestUtils.httpPost(url,gson.toJson(paramObj));
            return result;
        }catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    /**
    * 保存R脚本到项目空间
    *  @param paramObj
    */
    public String r_save(JsonObject paramObj) {
        try{
            String url = ConfigurationService.getUrlBean().getRSave();
            String result = HttpRequestUtils.httpPost(url,gson.toJson(paramObj));
            return result;
        }catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }

    /**
    *  从项目空间load脚本
    *  @param paramObj
    * */
    public String r_load(JsonObject paramObj) {
        try{
            String url = ConfigurationService.getUrlBean().getRLoad();
            String result = HttpRequestUtils.httpPost(url,gson.toJson(paramObj));
            return result;
        }catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }


    /**
     * 列举用户在某项目及方案下的所有R代码 
     * @param paramObj
     */
    public String r_list(JsonObject paramObj) {
        try{
            String url = ConfigurationService.getUrlBean().getRList();
            String result = HttpRequestUtils.httpPost(url,gson.toJson(paramObj));
            return result;
        }catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

}
