package com.gennlife.platform.service;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.dao.ProjectMapper;
import com.gennlife.platform.processor.RwsProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class RwsService implements RwsServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(RwsProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public String PreLiminary(JsonObject paramObj) {
        int counter = 0;
        ResultBean resultBean = new ResultBean();
        try {
            String url = ConfigurationService.getUrlBean().getPreLiminaryUrl();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));

            JsonObject resultObj = (JsonObject) jsonParser.parse(result);
            if (resultObj.get("status").getAsString().equals("200")){
                String projectId = paramObj.get("projectId").getAsString();
                if (paramObj.has("crfId")){
                    String crfId = paramObj.get("crfId").getAsString();
                    //获取单病种对应的名称
                    String crfName = AllDao.getInstance().getProjectDao().getCrfName(crfId);
                    //String crfName = projectMapper.getCrfName(crfId);
                    counter = insertProCrfId(projectId,"单病种-"+crfName,crfId);
                } else {
                    counter = insertProCrfId(projectId,"EMR","");
                }
            }

            Map<String, Object> info = new HashMap<String, Object>();
            info.put("counter", counter);
            info.put("result",result);
            resultBean.setCode(1);
            resultBean.setInfo(info);
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
        return gson.toJson(resultBean);
    }

    //如果是crf项目，需要写入p_project的crfId字段中，然后存入对应名字到datasource
    public int insertProCrfId(String projectID,String dataSource,String crfId){
        Map<String,String> map = new HashMap<>();
        map.put("projectID",projectID);
        map.put("dataSource",dataSource);
        map.put("crfId",crfId);
        long start = System.currentTimeMillis();
        int count = AllDao.getInstance().getProjectDao().insertProCrfId(map);
        //int count = projectMapper.insertProCrfId(map);
        long end = System.currentTimeMillis();
        logger.debug("insertProCrfId(map) mysql耗时"+(end-start)+"ms");
        return count;
    }
}
