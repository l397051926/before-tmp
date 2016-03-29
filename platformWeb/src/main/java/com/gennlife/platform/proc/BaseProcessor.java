package com.gennlife.platform.proc;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chen-song on 16/3/18.
 */
public class BaseProcessor {
    private Logger logger = LoggerFactory.getLogger(BaseProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer= new View();
    /**
     * 联动的数据接口
     * @param request
     * @param resps
     */
    public void chainItem(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        logger.info("chainItem =" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String attrID = jsonObject.get("attrID").getAsString();
        String value = jsonObject.get("value").getAsString();
        JsonElement object = ArkService.getChainJson().get(attrID);
        ResultBean resultBean = new ResultBean();
        if(object != null
                && object.isJsonObject()
                && object.getAsJsonObject().get(value) != null
                && object.getAsJsonObject().get(value).isJsonObject()){
            resultBean.setCode(1);
            resultBean.setData(object.getAsJsonObject().get(value));
        }else{
            resultBean.setCode(0);
            resultBean.setInfo("请求参数 有点不对哦");
        }
        viewer.viewString(gson.toJson(resultBean),resps,request);

    }
}
