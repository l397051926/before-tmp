package com.gennlife.platform.parse;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chensong on 2015/12/18.
 */
public class SampleImportParser {
    private static Logger logger = LoggerFactory.getLogger(SampleImportParser.class);
    private static Gson gson = GsonUtil.getGson();
    private static String importURL;
    private static String fileURL;
    static{
        importURL = ArkService.getConf().getImportURL();
        fileURL =  ArkService.getConf().getFileURL();
    }
    public static String getURI(String projectid,JsonArray fields,Object query,String indexName){
        logger.info("importURL="+importURL);
        String url = String.format(importURL, indexName);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("projectid",projectid);
        Map<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("url",url);
        searchMap.put("query",query);
        map.put("search",searchMap);
        map.put("fields",fields);
        String param = gson.toJson(map);
        logger.info("param =" + param);
        String result = HttpRequestUtils.httpPost(fileURL,param);
        logger.info("result="+result);
        return result;
    }
}
