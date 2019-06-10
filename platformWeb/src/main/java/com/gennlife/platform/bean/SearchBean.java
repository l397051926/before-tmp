package com.gennlife.platform.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gennlife.platform.util.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author lmx
 * @create 2019 18 11:06
 * @desc
 **/
public class SearchBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchBean.class);

    private Integer page ;
    private Integer size ;
    private String indexName;
    private String query;
    private JSONArray source;
    private JSONObject power;

    public SearchBean() {
    }

    public SearchBean(Integer page, Integer size, String indexName, JSONObject power) {
        this.page = page;
        this.size = size;
        this.indexName = indexName;
        this.power = power;
    }

    public SearchBean(Integer page, Integer size, String indexName, String query, JSONArray source, JSONObject power) {
        this.page = page;
        this.size = size;
        this.indexName = indexName;
        this.query = query;
        this.source = source;
        this.power = power;
    }

    public void addQuery(){

    }

    public String querySearch(){
        return null;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public JSONArray getSource() {
        return source;
    }

    public void setSource(JSONArray source) {
        this.source = source;
    }

    public JSONObject getPower() {
        return power;
    }

    public void setPower(JSONObject power) {
        this.power = power;
    }


    public void setMyclinicSource() {
        if(this.source == null){
            source = new JSONArray();
        }
        source.add(PATIENT_INFO);
        source.add(VISIT_INFO);
    }

    public boolean setMyclinicQuery(JSONObject paramObj) {
        String ADMISSION_DATE = getMyclinnicQuery(paramObj,"ADMISSION_DATE");
        String PATIENT_NAME =getMyclinnicQuery(paramObj,"PATIENT_NAME");
        String IDCARD =getMyclinnicQuery(paramObj,"IDCARD");
        String MEDICARECARD =getMyclinnicQuery(paramObj,"MEDICARECARD");
        String OUTPATIENT_SN =getMyclinnicQuery(paramObj,"OUTPATIENT_SN");
        String INPATIENT_SN =getMyclinnicQuery(paramObj,"INPATIENT_SN");

        if(StringUtils.isEmpty(PATIENT_NAME) && StringUtils.isEmpty(IDCARD) && StringUtils.isEmpty(MEDICARECARD) && StringUtils.isEmpty(OUTPATIENT_SN) && StringUtils.isEmpty(INPATIENT_SN) ){
            return true;
        }
        addLeftlit();
        addMyclinicQuery(PATIENT_NAME,IDCARD,MEDICARECARD);
        addRightlit();
        addAnd();
        addLeftBg();
        addMyclinicQuery(ADMISSION_DATE,OUTPATIENT_SN,INPATIENT_SN);
        addRightBg();
        return false;
    }

    public void addAnd(){
        if(StringUtils.isEmpty(this.query)){
            LOGGER.warn("query 为空 不能直接 增加 AND ");
            return;
        }
        this.query = this.query + " AND ";
    }
    public void addOr(){
        if(StringUtils.isEmpty(this.query)){
            LOGGER.warn("query 为空 不能直接 增加 OR ");
            return;
        }
        this.query = this.query + " OR ";
    }
    public void addLeftlit(){
        if(StringUtils.isEmpty(this.query)){
            this.query = "(";
        }else {
            this.query = this.query + "(";
        }
    }
    public void addRightlit(){
        if(StringUtils.isEmpty(this.query)){
            LOGGER.warn("query 为空 不能直接 增加 ) ");
            return;
        }
        this.query = this.query + ")";
    }
    public void addRightBg(){
        if(StringUtils.isEmpty(this.query)){
            LOGGER.warn("query 为空 不能直接 增加 } ");
            return;
        }
        this.query = this.query + "}";
    }
    public void addLeftBg(){
        if(StringUtils.isEmpty(this.query)){
            this.query = "{";
        }else {
            this.query = this.query + "{";
        }
    }

    private void addMyclinicQuery(String...querys) {
        for(String str : querys){
            addAndQuery(str);
        }
    }

    private void addAndQuery(String str) {
        if(StringUtils.isEmpty(str)){
            return;
        }
        if(StringUtils.isEmpty(this.query)){
            this.query = str;
        } else  if("(".equals(this.query.substring(this.query.length()-1)) || "{".equals(this.query.substring(this.query.length()-1))){
            setQuery(this.query.concat(str));
        }else {
            setQuery(this.query.concat(" AND ").concat(str));
        }
    }

    private String getMyclinnicQuery(JSONObject paramObj, String key) {
        Object tmpVal = paramObj.get(key);
        if(tmpVal == null || StringUtils.isEmpty(tmpVal.toString())) {
            return null;
        }
        if(tmpVal instanceof  String) {
            return MYCLINIC_QUERY_CONFIG.getString(key) + " 包含 " + paramObj.getString(key);
        }else if(tmpVal instanceof  JSONArray){
            JSONArray val = paramObj.getJSONArray(key);
            if(val.size() == 2){
                return MYCLINIC_QUERY_CONFIG.getString(key) + " 从 " + val.getString(0) + " 到 " + val.getString(1);
            }else {
                LOGGER.error("就诊日期格式不对 ：" + val.toJSONString() );
            }
        }
        return null;
    }

    private static final String PATIENT_INFO = "patient_info";
    private static final String VISIT_INFO = "visits.visit_info";

    private static final JSONObject MYCLINIC_QUERY_CONFIG = new JSONObject()
        .fluentPut("ADMISSION_DATE","[就诊.就诊基本信息.入院（就诊）时间]")
        .fluentPut("OUTPATIENT_SN","[就诊.就诊基本信息.住院号]")
        .fluentPut("INPATIENT_SN","[就诊.就诊基本信息.门诊号]")
        .fluentPut("IDCARD","[患者基本信息.证件号码]")
        .fluentPut("PATIENT_NAME","[患者基本信息.患者姓名]")
        .fluentPut("MEDICARECARD","[患者基本信息.医保卡号]")
        ;
}
