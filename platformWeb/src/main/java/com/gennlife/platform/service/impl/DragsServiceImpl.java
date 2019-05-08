package com.gennlife.platform.service.impl;

import com.gennlife.platform.processor.MessageCenterProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.service.DragsService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author lmx
 * @create 2019 08 14:36
 * @desc
 **/
@Service
public class DragsServiceImpl implements DragsService {

    private static Logger logger = LoggerFactory.getLogger(MessageCenterProcessor.class);

    @Override
    public String drgsIndex(String paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getDrgsIndex();
            String result = HttpRequestUtils.httpPost(url,paramObj);
            return result;
        } catch (Exception e) {
            logger.error("医保控费 登陆进入首页时，返回首页数据 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    @Override
    public String indexSetting(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getIndexSetting();
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        } catch (Exception e) {
            logger.error("医保控费 用户在首页进行高级设置后的请求接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    @Override
    public String indexList(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getIndexList();
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        } catch (Exception e) {
            logger.error("医保控费 用户点击或输入页码，或者点击排序，或者过滤组名后请求该接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    @Override
    public String factorList(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getFactorList();
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        } catch (Exception e) {
            logger.error("医保控费 用户在首页列表点击某一个DRGs组后弹出下钻页面 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    @Override
    public String miningCatalog(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getMiningCatalog();
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        } catch (Exception e) {
            logger.error("医保控费 用户在费用分析列表点击一行时，或者用户再详细分析页面点击按费用展开tab时，调用此接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    @Override
    public String miningDept(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getMiningDept();
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        } catch (Exception e) {
            logger.error("医保控费 用户在点击按科室展开Tab页时访问该接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    @Override
    public String miningParent(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getMiningParent();
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        } catch (Exception e) {
            logger.error("医保控费 用户在点击病人列表Tab页时访问该接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }    }


}
