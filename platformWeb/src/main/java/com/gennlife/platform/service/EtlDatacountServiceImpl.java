package com.gennlife.platform.service;

import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.bean.etl.EtlDatacount;
import com.gennlife.platform.dao.EtlDatacountMapper;
import com.gennlife.platform.util.TimeUtils;
import com.google.gson.*;
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

    private static JsonParser jsonParser = new JsonParser();

    @Override
    public JsonObject getAllEtlDatacount(){

//        List<EtlDatacount> etlDatacountList =  etlDatacountMapper.getAllEtlDataCount();
        List<EtlDatacount> etlDatacountList =  etlDatacountMapper.getAllDataByJoin();

        JsonObject config = (JsonObject) jsonParser.parse(ReadConditionByRedis.getEtlDataCountConfig());
        JsonArray sort = config.getAsJsonArray("sort");

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
        return result;
    }

    private JsonObject getSevenEtlDataCounts(Map<String, List<EtlDatacount>> dataEtlDatacounts) {
        JsonArray dates = new JsonArray();
        JsonArray inspectionData = new JsonArray();
        JsonArray inspectionChaData = new JsonArray();
        JsonArray auditData = new JsonArray();

        for (Map.Entry<String,List<EtlDatacount>> entry : dataEtlDatacounts.entrySet()){
            String date = entry.getKey();
            dates.add(date);
            JsonObject dataCount = getElectronicDocumentByValues(entry.getValue());
            inspectionData.add(dataCount.get("insCount").getAsInt());
            inspectionChaData.add(dataCount.get("insChaCount").getAsInt());
            auditData.add(dataCount.get("OperaCount").getAsInt());
        }
        JsonObject result = new JsonObject();
        result.add("date",dates);
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
        JsonObject buckets = new JsonObject();
        buckets.add("x",dataEtlDatacounts.get("date"));
        buckets.add("data",dataEtlDatacounts.get(inspectionData));
        inspectionReports.add("buckets",buckets);
    }

    public JsonObject getElectronicDocumentByValue(List<EtlDatacount> etlDatacounts){
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

    public JsonObject getElectronicDocumentByValues(List<EtlDatacount> etlDatacounts){
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

    public JsonArray getAllDataCount(List<EtlDatacount> etlDatacounts,JsonObject patAndVis,JsonArray sort){
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

    public JsonArray getInspectionCha(List<EtlDatacount> etlDatacounts, JsonArray sort){
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

    public JsonObject getPatientInfoAndVisitInfo(List<EtlDatacount> etlDatacounts){
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

    public Map<String,List<EtlDatacount>> getDataEtlDataCounts() {
        Map<String,List<EtlDatacount>> result = new LinkedHashMap<>();
        String sevenParseDate  = TimeUtils.getPastDate(8);
        Date dates = TimeUtils.strToDateLong(sevenParseDate);
        List<EtlDatacount> savenParseDates = etlDatacountMapper.getSevenParseDates(dates);
        for (EtlDatacount etlDatacount : savenParseDates){
            Date date = etlDatacount.getUpdateTime();
            String ymdDate = TimeUtils.getYMDDateStr(date);
            if(!result.containsKey(ymdDate)){
                result.put(ymdDate,new ArrayList<>());
            }
            result.get(ymdDate).add(etlDatacount);
        }
        return result;
    }
}
