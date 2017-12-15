package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AccessUtils;
import com.gennlife.platform.bean.GennUpResultBean;
import com.gennlife.platform.bean.IpBean;
import com.gennlife.platform.dao.GennMapper;
import com.gennlife.platform.enums.GennMappingEnum;
import com.gennlife.platform.model.GennDataModel;
import com.gennlife.platform.model.User;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Chenjinfeng on 2017/12/13.
 */
@Controller("genn")
public class GennController {
    private static final Logger logger = LoggerFactory.getLogger(GennController.class);
    private static String KEY = "code";
    private static int SUCCESS = 1;
    private static String ERR_KEY = "error";
    private static int FAIL = 0;

    Gson gson = GsonUtil.getGson();
    @Autowired
    GennMapper gennMapper;
    @Autowired
    IpBean ipBean;
    @Autowired
    AccessUtils accessUtils;
    View view = new View();

    @RequestMapping("/image")
    public void image(HttpServletResponse response, HttpServletRequest paramRe,
                      @RequestParam("imageId") String imageId, @RequestParam("patientSn") String patientSn) throws IOException {
        if (checkFail(response, paramRe, imageId, patientSn)) return;
        User user = (User) paramRe.getAttribute("currentUser");
        String hospitalId = user.getOrgID();
        String url = "http://" + ipBean.getGennIpAndPort() + "/genn/data/img?imageId="
                + imageId + "&patientSn=" + patientSn + "&hospital=" + hospitalId;
        HttpRequestUtils.httpGetStream(url, response);
    }

    private boolean checkFail(HttpServletResponse response, HttpServletRequest paramRe, String id, String patientSn) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(patientSn)) {
            view.viewString("请求参数存在空", response);
            return true;
        }
        if (!accessUtils.checkAccessPatientSn(patientSn, paramRe)) {
            view.viewString("患者编号无权限访问或不存在", response);
            return true;
        }
        return false;
    }

    @RequestMapping("/pdf")
    public void pdf(HttpServletResponse response, HttpServletRequest paramRe,
                    @RequestParam("pdfId") String pdfId, @RequestParam("patientSn") String patientSn) throws IOException {
        if (checkFail(response, paramRe, pdfId, patientSn)) return;
        User user = (User) paramRe.getAttribute("currentUser");
        String hospitalId = user.getOrgID();
        String url = "http://" + ipBean.getGennIpAndPort() + "/genn/data/pdf?imageId="
                + pdfId + "&patientSn=" + patientSn + "&hospital=" + hospitalId;
        HttpRequestUtils.httpGetStream(url, response);
    }
    @RequestMapping("/json")
    public void json(HttpServletResponse response, HttpServletRequest paramRe,
                    @RequestParam("uniqueId") String uniqueId, @RequestParam("patientSn") String patientSn) throws IOException {
        if (checkFail(response, paramRe, uniqueId, patientSn)) return;
        User user = (User) paramRe.getAttribute("currentUser");
        String hospitalId = user.getOrgID();
        String url = "http://" + ipBean.getGennIpAndPort() + "/genn/data/json?uniqueId="
                + uniqueId + "&patientSn=" + patientSn + "&hospital=" + hospitalId;
        HttpRequestUtils.httpGetStream(url, response);
    }

    @RequestMapping("list")
    public String list(@RequestParam("patientSn") String patientSn,
                       @RequestParam(value = "visitSn", required = false) String visitSn,
                       @RequestParam("page") int page,
                       @RequestParam("size") int size, HttpServletRequest paramRe
    ) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 10;
        JsonObject result = new JsonObject();
        if (!accessUtils.checkAccessPatientSn(patientSn, paramRe)) {
            result.addProperty(ERR_KEY, "患者编号无权限访问或不存在");
            result.addProperty(KEY, FAIL);
            return GsonUtil.toJsonStr(result);
        }
        int from = (page - 1) * size;
        if (StringUtils.isEmpty(visitSn)) visitSn = null;
        List<GennDataModel> list = gennMapper.getGennData(from, size, patientSn, visitSn);
        if (list == null || list.size() == 0) {
            result.addProperty(ERR_KEY, "no data");
            result.addProperty(KEY, FAIL);
            return GsonUtil.toJsonStr(result);
        }
        result.addProperty(KEY, SUCCESS);
        result.add("data", GsonUtil.toJsonTree(list));
        return GsonUtil.toJsonStr(result);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public String receive(@RequestBody String data) {
        JsonObject updateDatas = GsonUtil.toJsonObject(data);
        LinkedList<GennDataModel> list = gson.fromJson(updateDatas, new TypeToken<LinkedList<GennDataModel>>() {
        }.getType());
        Map<String, GennUpResultBean> upResult = new HashMap<>();
        JsonArray source = new JsonArray();
        source.add("visits.visit_info.VISIT_SN");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String synTime = format.format(date);
        for (GennDataModel item : list) {
            String id = item.getUniqueId();
            String patientSn = item.getPatientSn();
            String visitSn = item.getVisitSn();
            JsonObject patData = accessUtils.getPatData(patientSn, null, source);
            if (patData == null) {
                upResult.put(id, new GennUpResultBean(GennMappingEnum.MAPPING_ERR, "patientSn映射失败"));
                continue;
            }
            if (checkVisitSnFail(upResult, id, visitSn, patData)) continue;
            try {
                item.setSynTime(synTime);
                gennMapper.upsert(item);
                upResult.put(id, new GennUpResultBean(GennMappingEnum.SUCCESS));
            } catch (Exception e) {
                logger.error("", e);
                upResult.put(id, new GennUpResultBean(GennMappingEnum.SAVE_ERR, e.getMessage()));
            }
        }
        return GsonUtil.toJsonStr(upResult);

    }

    public boolean checkVisitSnFail(Map<String, GennUpResultBean> upResult, String id, String visitSn, JsonObject patData) {
        LinkedList<JsonElement> visits = GsonUtil.getJsonArrayAllValue("visits.visit_info.VISIT_SN", patData);
        if (visits == null || visits.size() == 0) {
            upResult.put(id, new GennUpResultBean(GennMappingEnum.MAPPING_ERR, "visitSn不匹配"));
            return true;
        }
        boolean find = false;
        for (JsonElement element : visits) {
            if (element.getAsString().equals(visitSn)) {
                find = true;
                break;
            }
        }
        if (!find) {
            upResult.put(id, new GennUpResultBean(GennMappingEnum.MAPPING_ERR, "visitSn不匹配"));
            return true;
        }
        return false;
    }
}
