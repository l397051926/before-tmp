package com.gennlife.platform.controller;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.service.EtlDatacountService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lmx
 * @create 2019 22 14:51
 * @desc
 **/
@Controller
@RequestMapping("/etl")
public class EtlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EtlController.class);
    private static View view = new View();
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();
    @Autowired
    private EtlDatacountService etlDatacountService;

    @RequestMapping(value = "/getEtlDatacount", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void getEtlDatacount(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject data = etlDatacountService.getAllEtlDatacount();
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(data);
            resultStr = gson.toJson(resultBean);

        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        LOGGER.info("获取etl统计数据 耗时" + (System.currentTimeMillis() - start) + "ms");
        view.viewString(resultStr, response);
    }

    @RequestMapping(value = "/getEtlStatisticsTable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getEtlStatisticsTable(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =etlDatacountService.getEtlStatisticsTable(paramObj);
            LOGGER.info("二级关联属性post 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        LOGGER.info("二级关联属性post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
}
