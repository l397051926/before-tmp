package com.gennlife.platform.service;

import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.etl.EtlDatacount;
import com.gennlife.platform.dao.EtlDatacountMapper;
import com.gennlife.platform.enums.EtlStatusTypeEnum;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.TimeUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.sun.tools.javac.jvm.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * @author lmx
 * @create 2019 21 18:18
 * @desc
 **/
@Service
public class EtlDatacountServiceImpl implements EtlDatacountService{

    @Autowired
    private EtlDatacountMapper etlDatacountMapper;
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();

    @Override
    public JsonObject getAllEtlDatacount(){

        List<EtlDatacount> etlDatacountList =  etlDatacountMapper.getAllDataByJoin();

        JsonObject config = (JsonObject) jsonParser.parse(ReadConditionByRedis.getEtlDataCountConfig());
        JsonArray sort = config.getAsJsonArray("sort");
        JsonArray catalogue = config.getAsJsonArray("catalogue");
        JsonObject result = new JsonObject();
        JsonObject patAndVis =  getPatientInfoAndVisitInfo(etlDatacountList);
        //获取右下角
        JsonArray allDataCount = getAllDataCount(etlDatacountList,patAndVis,sort);
        //获取查体报告
        JsonArray inspectionCha = getInspectionCha(etlDatacountList,sort);

        Map<String,List<EtlDatacount>> dataEtlDatacounts = getDataEtlDataCounts();
        JsonObject sevenEtlDataCounts = getSevenEtlDataCounts(dataEtlDatacounts);
        //统计上面信息
        JsonObject dataCount = getElectronicDocumentByValue(etlDatacountList);
        JsonObject statistics = getStatistics(patAndVis,dataCount,sevenEtlDataCounts);

        result.add("statistics",statistics);
        result.add("checkDistribution",inspectionCha);
        result.add("bigDataDistribution",allDataCount);
        result.add("catalogue",catalogue);
        return result;
    }

    @Override
    public String getEtlStatisticsTable(JsonObject paramObj) {
        ResultBean resultBean = new ResultBean();
        JsonObject config = (JsonObject) jsonParser.parse(ReadConditionByRedis.getEtlDataCountConfig());

        Integer statusType = paramObj.get("statusType").getAsInt();
        JsonArray codes = paramObj.getAsJsonArray("codes");

        Integer days = EtlStatusTypeEnum.getEtlStatusType(statusType).getVal();
        String sevenParseDate  = TimeUtils.getPastDate(days);
        Date dates = TimeUtils.strToDateLong(sevenParseDate);

        List<String> codeSqls = new ArrayList<>();
        for (JsonElement element : codes){
            JsonObject  codeIds  = element.getAsJsonObject();
            String code = codeIds.get("id").getAsString();
            if(config.has(code)){
                JsonArray arryas = config.getAsJsonArray(code);
                codeSqls.addAll(gson.fromJson(arryas,new TypeToken<List<String>>(){}.getType()));
            }else {
                codeSqls.add(code);
            }
        }

        if(codeSqls.size() == 0 ){
            resultBean.setCode(0);
            resultBean.setData(new JsonArray());
            return gson.toJson(resultBean);
        }

        List<EtlDatacount> savenParseDates = etlDatacountMapper.getStatisticsTableParseDates(dates,codeSqls);
        Map<String,List<EtlDatacount>> result = new LinkedHashMap<>();
        statisticsTableByTime(savenParseDates, result);
        JsonObject data = transforEtlStatisticsTableResult(result,codes,config);
        
        resultBean.setCode(1);
        resultBean.setData(data);
        return gson.toJson(resultBean);
    }

    private JsonObject transforEtlStatisticsTableResult(Map<String, List<EtlDatacount>> result, JsonArray codes, JsonObject config) {
        JsonObject resData = new JsonObject();
        JsonArray res = new JsonArray();
        List<String> xAxis = new LinkedList<>();
        for (JsonElement element : codes){
            JsonObject data = new JsonObject();
            JsonObject object = element.getAsJsonObject();
            String code = object.get("id").getAsString();
            JsonArray buckets = new JsonArray();
            for (Map.Entry<String,List<EtlDatacount>> entry : result.entrySet()){
                String date = entry.getKey();
                Integer value = 0;
                if(config.has(code)){
                    JsonArray arryas = config.getAsJsonArray(code);
                    List<String> tmpList = gson.fromJson(arryas,new TypeToken<List<String>>(){}.getType());
                    value = getEtlStaisticsVal(tmpList,entry.getValue());
                }else {
                    value = getEtlStaisticsVal(code,entry.getValue());
                }
                if(!xAxis.contains(date)){
                    xAxis.add(date);
                }
                buckets.add(value);
            }
            data.addProperty("id",code);
            data.addProperty("name",object.get("name").getAsString());
            data.add("data",buckets);
            res.add(data);
        }
        resData.add("xAxis",gson.toJsonTree(xAxis));
        resData.add("series",res);
        return resData;
    }

    private Integer getEtlStaisticsVal(List<String> tmpList, List<EtlDatacount> etlDatacounts) {
        Integer  result = 0;
        for (EtlDatacount etlDatacount : etlDatacounts){
            String code =  etlDatacount.getCode();
            if(tmpList.contains(code)){
                result = result + etlDatacount.getValues();
            }
        }
        return result;
    }
    private Integer getEtlStaisticsVal(String target, List<EtlDatacount> etlDatacounts) {
        Integer  result = 0;
        for (EtlDatacount etlDatacount : etlDatacounts){
            String code =  etlDatacount.getCode();
            if(target.equals(code)){
                result = result + etlDatacount.getValues();
                break;
            }
        }
        return result;
    }

    private void statisticsTableByTime(List<EtlDatacount> savenParseDates, Map<String, List<EtlDatacount>> result) {
        for (EtlDatacount etlDatacount : savenParseDates){
            Date date = etlDatacount.getUpdateTime();
            String ymdDate = TimeUtils.getYMDDateStr(date);
            if(!result.containsKey(ymdDate)){
                result.put(ymdDate,new ArrayList<>());
            }
            result.get(ymdDate).add(etlDatacount);
        }
    }

    private JsonObject getSevenEtlDataCounts(Map<String, List<EtlDatacount>> dataEtlDatacounts) {

        JsonArray inspectionData = new JsonArray();
        JsonArray inspectionChaData = new JsonArray();
        JsonArray auditData = new JsonArray();

        for (Map.Entry<String,List<EtlDatacount>> entry : dataEtlDatacounts.entrySet()){
            String date = entry.getKey();
            JsonObject dataCount = getElectronicDocumentByValues(entry.getValue());
            JsonObject insObj = new JsonObject();
            JsonObject insChaObj = new JsonObject();
            JsonObject auditObj = new JsonObject();
            insObj.addProperty("x",date);
            insObj.addProperty("y",dataCount.get("insCount").getAsInt());
            inspectionData.add(insObj);
            insChaObj.addProperty("x",date);
            insChaObj.addProperty("y",dataCount.get("insChaCount").getAsInt());
            inspectionChaData.add(insChaObj);
            auditObj.addProperty("x",date);
            auditObj.addProperty("y",dataCount.get("OperaCount").getAsInt());
            auditData.add(auditObj);
        }
        JsonObject result = new JsonObject();
        result.add("inspectionData",inspectionData);
        result.add("inspectionChaData",inspectionChaData);
        result.add("auditData",auditData);
        return result;
    }

    private JsonObject getStatistics(JsonObject patAndVis, JsonObject dataCount, JsonObject dataEtlDatacounts) {
        JsonObject result = new JsonObject();
        JsonObject patientInfo = new JsonObject();
        patientInfo.addProperty("pnum",patAndVis.get("patCounts").getAsInt());
        patientInfo.add("buckets",patAndVis.get("patientInfo").getAsJsonArray());
        addTitle(patientInfo,"患者总人数");

        JsonObject visitInfo = new JsonObject();
        visitInfo.addProperty("pnum",patAndVis.get("visitCounts").getAsInt());
        visitInfo.add("buckets",patAndVis.get("visitInfo").getAsJsonArray());
        addTitle(visitInfo,"就诊总人数");

        JsonObject inspectionReports = new JsonObject();
        inspectionReports.addProperty("pnum",dataCount.get("insCount").getAsInt());
        addCountBuckets(inspectionReports,dataEtlDatacounts,"inspectionData");
        addTitle(inspectionReports,"检验报告总数量");

        JsonObject inspectionChaReports = new JsonObject();
        inspectionChaReports.addProperty("pnum",dataCount.get("insChaCount").getAsInt());
        addCountBuckets(inspectionChaReports,dataEtlDatacounts,"inspectionChaData");
        addTitle(inspectionChaReports,"检查报告总数量");

        JsonObject auditReports = new JsonObject();
        auditReports.addProperty("pnum",dataCount.get("OperaCount").getAsInt());
        addCountBuckets(auditReports,dataEtlDatacounts,"auditData");
        addTitle(auditReports,"电子文档总数量");

        result.add("patientInfo",patientInfo);
        result.add("visitInfo",visitInfo);
        result.add("inspectionReports",inspectionReports);
        result.add("inspectionChaReports",inspectionChaReports);
        result.add("auditReports",auditReports);
        return result;
    }

    private void addTitle(JsonObject patientInfo, String title) {
        patientInfo.addProperty("title",title);
    }

    private void addCountBuckets(JsonObject inspectionReports, JsonObject dataEtlDatacounts, String inspectionData) {
        inspectionReports.add("buckets",dataEtlDatacounts.get(inspectionData));
    }

    private JsonObject getElectronicDocumentByValue(List<EtlDatacount> etlDatacounts){
        Integer operaCount = 0 ;
        Integer insCount = 0;
        Integer insChaCount = 0;
        for (EtlDatacount etlDatacount : etlDatacounts){
            String type = etlDatacount.getStatisticsType();
            if(StringUtils.isEmpty(type)) continue;
            switch (type){
                case "电子文档" :
                    operaCount = operaCount + etlDatacount.getValue();
                    break;
                case "检验" :
                    insCount = insCount + etlDatacount.getValue();
                    break;
                case "检查" :
                    insChaCount = insChaCount +  etlDatacount.getValue();
                    break;
            }
        }
        JsonObject result = new JsonObject();
        result.addProperty("OperaCount",operaCount);
        result.addProperty("insCount",insCount);
        result.addProperty("insChaCount",insChaCount);
        return result;
    }

    private JsonObject getElectronicDocumentByValues(List<EtlDatacount> etlDatacounts){
        Integer operaCount = 0 ;
        Integer insCount = 0;
        Integer insChaCount = 0;
        for (EtlDatacount etlDatacount : etlDatacounts){
            String type = etlDatacount.getStatisticsType();
            if(StringUtils.isEmpty(type)) continue;
            switch (type){
                case "电子文档" :
                    operaCount = operaCount + etlDatacount.getValues();
                    break;
                case "检验" :
                    insCount = insCount + etlDatacount.getValues();
                    break;
                case "检查" :
                    insChaCount = insChaCount +  etlDatacount.getValues();
                    break;
            }
        }
        JsonObject result = new JsonObject();
        result.addProperty("OperaCount",operaCount);
        result.addProperty("insCount",insCount);
        result.addProperty("insChaCount",insChaCount);
        return result;
    }

    private JsonArray getAllDataCount(List<EtlDatacount> etlDatacounts,JsonObject patAndVis,JsonArray sort){
        JsonArray result = new JsonArray();
        addPatAndVis(result,patAndVis);
        int size = etlDatacounts.size();
        int num = 3;
        Map<String,JsonObject> sortMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            EtlDatacount etlDatacount = etlDatacounts.get(i);
            String code = etlDatacount.getCode();
            if(getPatientInfoList().contains(code) || getVisitInfoList().contains(code)){
                continue;
            }
            JsonObject object = new JsonObject();
            object.addProperty("textType",etlDatacount.getDisplayName());
            object.addProperty("dataVolume",etlDatacount.getValue());
            sortMap.put(code,object);
        }
        for (JsonElement element : sort){
            String key = element.getAsString();
            JsonObject object = sortMap.get(key);
            if(object == null ) continue;
            object.addProperty("key",num);
            result.add(object);
            num++;
        }
        return result;
    }

    private JsonArray getInspectionCha(List<EtlDatacount> etlDatacounts, JsonArray sort){
        JsonArray result = new JsonArray();
        Map<String,EtlDatacount> sortMap = new HashMap<>();
        for (EtlDatacount etlDatacount : etlDatacounts){
            String type = etlDatacount.getStatisticsType();
            if("检查".equals(type)){
                sortMap.put(etlDatacount.getCode(),etlDatacount);
            }
        }
        for (JsonElement element : sort){
            String key = element.getAsString();
            EtlDatacount etlDatacount = sortMap.get(key);
            if(etlDatacount == null) continue;
            JsonObject object = new JsonObject();
            object.addProperty("x",etlDatacount.getDisplayName());
            object.addProperty("y",etlDatacount.getValue());
            result.add(object);
        }
        return result;
    }

    private JsonObject getPatientInfoAndVisitInfo(List<EtlDatacount> etlDatacounts){
        JsonArray patientInfo = new JsonArray();
        JsonArray visitInfo = new JsonArray();
        Integer patCounts = 0;
        Integer visitCounts = 0;
        for (EtlDatacount etlDatacount : etlDatacounts){
            String code = etlDatacount.getCode();
            if(getPatientInfoList().contains(code)){
                patCounts = getPatCounts(patientInfo, patCounts, etlDatacount);
            }
            if(getVisitInfoList().contains(code)){
                visitCounts = getPatCounts(visitInfo, visitCounts, etlDatacount);
            }
        }
        JsonObject result = new JsonObject();
        result.addProperty("patCounts",patCounts);
        result.addProperty("visitCounts",visitCounts);
        result.add("patientInfo",patientInfo);
        result.add("visitInfo",visitInfo);
        return result;
    }

    private void addPatAndVis(JsonArray result, JsonObject patAndVis) {
        JsonObject pat = new JsonObject();
        pat.addProperty("key",1);
        pat.addProperty("textType","患者基本信息");
        pat.addProperty("dataVolume",patAndVis.get("patCounts").getAsInt());
        JsonObject vis = new JsonObject();
        vis.addProperty("key",2);
        vis.addProperty("textType","就诊基本信息");
        vis.addProperty("dataVolume",patAndVis.get("visitCounts").getAsInt());
        result.add(pat);
        result.add(vis);
    }

    private int getPatCounts(JsonArray array, Integer counts, EtlDatacount etlDatacount) {
        JsonObject obj = new JsonObject();
        Integer value = etlDatacount.getValue();
        counts = counts + value;
        obj.addProperty("textType",etlDatacount.getDisplayName());
        obj.addProperty("dataVolume",value);
        array.add(obj);
        return counts;
    }

    private Map<String,List<EtlDatacount>> getDataEtlDataCounts() {
        Map<String,List<EtlDatacount>> result = new LinkedHashMap<>();
        String sevenParseDate  = TimeUtils.getPastDate(8);
        Date dates = TimeUtils.strToDateLong(sevenParseDate);
        List<EtlDatacount> savenParseDates = etlDatacountMapper.getSevenParseDates(dates);
        statisticsTableByTime(savenParseDates, result);
        return result;
    }

    private  List<String> getPatientInfoList( ){
        List<String> patientInfo = new ArrayList<>();
        patientInfo.add("patient_info_man");
        patientInfo.add("patient_info_women");
        patientInfo.add("patient_info_other");
        return patientInfo;
    }

    private List<String> getVisitInfoList(){
        List<String> visitInfo = new ArrayList<>();
        visitInfo.add("visit_info_out");
        visitInfo.add("visit_info_in");
        visitInfo.add("visit_info_other");
        return visitInfo;
    }
}
