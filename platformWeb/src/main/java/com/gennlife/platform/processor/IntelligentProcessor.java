package com.gennlife.platform.processor;

import com.gennlife.platform.dao.IntelligentMapper;
import com.gennlife.platform.model.InspectReportIntellModel;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Chenjinfeng on 2018/3/14.
 */
@Component
public class IntelligentProcessor {
    private static final String EMPTY;
    private static final String KEY = "key";
    @Autowired
    private IntelligentMapper intelligentMapper;

    static {
        JsonObject emptyJson = new JsonObject();
        emptyJson.add(ParamUtils.getDataKey(), new JsonObject());
        emptyJson.addProperty(ParamUtils.getStatusKey(), ParamUtils.getSuccessCode());
        EMPTY = GsonUtil.toJsonStr(emptyJson);

    }

    public String inspectReport(JsonObject paramObj) {
        KeyResult keyResult = parse(paramObj);
        if (keyResult.isEmpty) return EMPTY;
        List<InspectReportIntellModel> list = intelligentMapper.inspectReportIntell(keyResult.key);
        JsonObject result = new JsonObject();
        result.add(ParamUtils.getDataKey(), GsonUtil.toJsonTree(list));
        result.addProperty(ParamUtils.getStatusKey(), ParamUtils.getSuccessCode());
        return GsonUtil.toJsonStr(result);
    }

    public String getOneInspectReportData(int id) {
        InspectReportIntellModel data = intelligentMapper.getOneData(id);
        if (data == null) return EMPTY;
        JsonObject result = new JsonObject();
        result.add(ParamUtils.getDataKey(), GsonUtil.toJsonObject(data.getData()));
        result.addProperty(ParamUtils.getStatusKey(), ParamUtils.getSuccessCode());
        return GsonUtil.toJsonStr(result);
    }

    private KeyResult parse(JsonObject paramObj) {
        KeyResult result = new KeyResult();
        if (paramObj == null || !paramObj.has(KEY) || !paramObj.get(KEY).isJsonPrimitive()) {
            result.isEmpty = true;
            return result;
        }
        String key = paramObj.get(KEY).getAsString();
        if (StringUtils.isEmpty(key.trim())) {
            result.isEmpty = true;
            return result;
        }
        result.isEmpty = false;
        result.key = key;
        return result;
    }

    class KeyResult {
        String key;
        boolean isEmpty;
    }
}
