package com.gennlife.platform.parse;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;

import java.util.concurrent.Callable;

/**
 * Created by chen-song on 16/5/13.
 */
public class CaseSuggestParser2 implements Callable<String> {
    private String url;

    public CaseSuggestParser2(String indexName, String dicName, String keywords, String size, String page) {
        this.url = String.format(
                ConfigurationService.getUrlBean().getCaseSuggestURL2(),
                ParamUtils.encodeURI(indexName),
                ParamUtils.encodeURI(dicName),
                ParamUtils.encodeURI(keywords),
                ParamUtils.encodeURI(size),
                ParamUtils.encodeURI(page)
        );

    }

    public String call() throws Exception {
        return HttpRequestUtils.httpGet(url);
    }

    public String parser() throws Exception {
        return HttpRequestUtils.httpGet(url);
    }
}
