package com.gennlife.platform.parse;

import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by chensong on 2015/12/18.
 */
public class SearchKeyParser implements Callable<String> {
    private static Logger logger = LoggerFactory.getLogger(SearchKeyParser.class);
    private static String searchKeyURL;
    private String url;
    static{
        searchKeyURL = ArkService.getConf().getSearchURL();
    }
    public SearchKeyParser(String indexName,String query){
        logger.info("原始url="+  String.format(searchKeyURL, indexName,query));
        this.url = String.format(searchKeyURL, ParamUtils.encodeURI(indexName),ParamUtils.encodeURI(query));
    }


    public String call() throws Exception {
        logger.info("url="+url);
        return HttpRequestUtils.httpGet(url);
    }
}
