package com.gennlife.platform.parse;

import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;

import java.util.concurrent.Callable;

/**
 * Created by chensong on 2015/12/12.
 */
public class QRecommendParser implements Callable<String>{
    private String url ;
    public QRecommendParser(String index,String keywords){
        this.url = String.format(ArkService.getConf().getqRecommendURL(), ParamUtils.encodeURI(index),ParamUtils.encodeURI(keywords));
    }

    public String call() throws Exception {
        return HttpRequestUtils.httpGet(url);
    }
}
