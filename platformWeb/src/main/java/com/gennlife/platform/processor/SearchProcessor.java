package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.projectBean.ProSample;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.Conf;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.SpringContextUtil;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by chensong on 2015/12/9.
 */
public class SearchProcessor {
    private static Logger logger = LoggerFactory.getLogger(SearchProcessor.class);
    private static String qRecommendURL = null;
    private static String searchURL = null;
    private static String fileURL = null;
    private static  ExecutorService executorService = ArkService.getExecutorService();
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer = new View();
    static{
        ApplicationContext context = SpringContextUtil.getApplicationContext();
        Conf conf = (Conf) context.getBean("com.gennlife.platform.util.Conf");
        qRecommendURL = conf.getqRecommendURL();
        searchURL = conf.getSearchURL();
        fileURL = conf.getFileURL();
    }




    /**
     * 搜索项目中成员
     * @param jsonObject
     */
    public String searchMembers(JsonObject jsonObject ){
        try{
            String searchMemberkey = jsonObject.get("searchMemberkey").getAsString();
            String limit = jsonObject.get("limit").getAsString();
            String projectID = jsonObject.get("projectID").getAsString();
            logger.info("searchProjectMembers searchMemberkey=" + searchMemberkey + ",limit=" + limit);
            int[] ls = ParamUtils.parseLimit(limit);
            Map<String,Object> confMap = new HashMap<String, Object>();
            confMap.put("startIndex",(ls[0]-1) * ls[1]);
            confMap.put("maxNum", ls[1]);
            confMap.put("searchMemberkey",searchMemberkey);
            confMap.put("projectID", projectID);
            List<SyUser> list = AllDao.getInstance().getSyUserDao().searchMemberList(confMap);
            int counter = AllDao.getInstance().getSyUserDao().searchMemberCounter(confMap);
            Map<String,Integer> info = new HashMap<String,Integer>();
            info.put("counter",counter);
            ResultBean userBean = new ResultBean();
            userBean.setCode(1);
            userBean.setData(list);
            userBean.setInfo(info);
            return gson.toJson(userBean);
        }catch (Exception e){
            return ParamUtils.errorParam("搜索失败");
        }

    }

    /**
     *
     * @param jsonObject
     */
    public String searchSetList(JsonObject jsonObject ) {
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            String projectID = jsonObject.get("projectID").getAsString();
            String key = jsonObject.get("key").getAsString();
            String limit = jsonObject.get("limit").getAsString();
            int[] ls = ParamUtils.parseLimit(limit);
            map.put("projectID",projectID);
            map.put("key",key);
            map.put("startIndex",(ls[0]-1) * ls[1]);
            map.put("maxNum",ls[1]);
        }catch (Exception e){
            return ParamUtils.errorParam("请求参数异常");
        }
        List<ProSample> list= AllDao.getInstance().getProjectDao().searchSampleSetList(map);
        int count = AllDao.getInstance().getProjectDao().searchSampleSetListCounter(map);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        Map<String,Integer> info = new HashMap<String, Integer>();
        info.put("count",count);
        resultBean.setData(list);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);
    }
}
