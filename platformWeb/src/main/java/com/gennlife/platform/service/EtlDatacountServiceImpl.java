package com.gennlife.platform.service;

import com.gennlife.platform.bean.etl.EtlDatacount;
import com.gennlife.platform.dao.EtlDatacountMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lmx
 * @create 2019 21 18:18
 * @desc
 **/
@Service
public class EtlDatacountServiceImpl implements EtlDatacountService{

    @Autowired
    private EtlDatacountMapper etlDatacountMapper;

    @Override
    public JsonObject getAllEtlDatacount(){
        List<EtlDatacount> etlDatacountList =  etlDatacountMapper.getAllEtlDataCount();
        JsonObject result = new JsonObject();
        JsonObject patAndVis =  getPatientInfoAndVisitInfo(etlDatacountList);
        //获取右下角
        JsonArray allDataCount = getAllDataCount(etlDatacountList,patAndVis);
        //获取查体报告
        JsonObject inspectionCha = getInspectionCha(etlDatacountList);
        //统计上面信息
        JsonObject dataCount = getElectronicDocument(etlDatacountList);

        JsonObject statistics = getStatistics(patAndVis,dataCount);

        result.add("statistics",statistics);
        result.add("checkDistribution",inspectionCha);
        result.add("bigDataDistribution",allDataCount);
        return result;
    }

    private JsonObject getStatistics(JsonObject patAndVis, JsonObject dataCount) {
        JsonObject result = new JsonObject();
        JsonObject patientInfo = new JsonObject();
        patientInfo.addProperty("pnum",patAndVis.get("patCounts").getAsInt());
        patientInfo.add("buckets",patAndVis.get("patientInfo").getAsJsonArray());
        JsonObject visitInfo = new JsonObject();
        visitInfo.addProperty("pnum",patAndVis.get("visitCounts").getAsInt());
        visitInfo.add("buckets",patAndVis.get("visitInfo").getAsJsonArray());
        JsonObject inspectionReports = new JsonObject();
        inspectionReports.addProperty("pnum",dataCount.get("insCount").getAsInt());
        JsonObject inspectionChaReports = new JsonObject();
        inspectionChaReports.addProperty("pnum",dataCount.get("insChaCount").getAsInt());
        JsonObject auditReports = new JsonObject();
        auditReports.addProperty("pnum",dataCount.get("OperaCount").getAsInt());
        result.add("patientInfo",patientInfo);
        result.add("visitInfo",visitInfo);
        result.add("inspectionReports",inspectionReports);
        result.add("inspectionChaReports",inspectionChaReports);
        result.add("auditReports",auditReports);
        return result;
    }

    public JsonObject getElectronicDocument(List<EtlDatacount> etlDatacounts){
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

    public JsonArray getAllDataCount(List<EtlDatacount> etlDatacounts,JsonObject patAndVis){
        JsonArray result = new JsonArray();
        addPatAndVis(result,patAndVis);
        int size = etlDatacounts.size();
        int num = 3;
        for (int i = 0; i < size; i++) {
            EtlDatacount etlDatacount = etlDatacounts.get(i);
            String code = etlDatacount.getCode();
            if(getPatientInfoList().contains(code) || getVisitInfoList().contains(code)){
                continue;
            }
            JsonObject object = new JsonObject();
            object.addProperty("key",num);
            object.addProperty("textType",etlDatacount.getDisplayName());
            object.addProperty("dataVolume",etlDatacount.getValues());
            result.add(object);
            ++num;
        }
        return result;
    }

    public JsonObject getInspectionCha(List<EtlDatacount> etlDatacounts){
        JsonObject result = new JsonObject();
        JsonArray yAxis = new JsonArray();
        JsonArray series = new JsonArray();
        for (EtlDatacount etlDatacount : etlDatacounts){
            String type = etlDatacount.getStatisticsType();
            if("检查".equals(type)){
                yAxis.add(etlDatacount.getDisplayName());
                series.add(etlDatacount.getValues());
            }
        }
        result.add("yAxis",yAxis);
        result.add("series",series);
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
        Integer value = etlDatacount.getValues();
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
}
