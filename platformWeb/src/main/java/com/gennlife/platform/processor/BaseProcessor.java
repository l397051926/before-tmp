package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chen-song on 16/3/18.
 */
public class BaseProcessor {
    private Logger logger = LoggerFactory.getLogger(BaseProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    /**
     * 联动的数据接口
     *
     * @param jsonObject
     */
    public String chainItem(JsonObject jsonObject) {
        String attrID = jsonObject.get("attrID").getAsString();
        String value = jsonObject.get("value").getAsString();
        JsonElement object = ArkService.getChainJson().get(attrID);
        ResultBean resultBean = new ResultBean();
        if (object != null
                && object.isJsonObject()
                && object.getAsJsonObject().get(value) != null
                && object.getAsJsonObject().get(value).isJsonObject()) {
            resultBean.setCode(1);
            resultBean.setData(object.getAsJsonObject().get(value));
        } else {
            resultBean.setCode(0);
            resultBean.setInfo("请求参数 有点不对哦");
        }
        return gson.toJson(resultBean);

    }

    /**
     * 项目病种
     *
     * @return
     */
    public String projectDisease() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(ArkService.getProjectDisease());
        return gson.toJson(resultBean);
    }

    public String test() throws Exception {
        //List<Lab> list = orgMapper.getLabs("tianjin_city_1");
        return gson.toJson("");
    }

    public String vitaGramServerVitaGramData(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getVitaGramServerVitaGramData();
            logger.info("vitaGramServerVitaGramData url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String vitaGramServerNccnRecommendPath(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getVitaGramServerNccnRecommendPath();
            logger.info("vitaGramServerNccnRecommendPath url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String VitaGramServerMedicalHistoryRecommendPath(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getVitaGramServerMedicalHistoryRecommendPath();
            logger.info("VitaGramServerMedicalHistoryRecommendPath url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }
}
