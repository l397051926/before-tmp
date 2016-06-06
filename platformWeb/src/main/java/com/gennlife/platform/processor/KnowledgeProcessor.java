package com.gennlife.platform.processor;

import com.gennlife.platform.build.KnowledgeBuilder;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chen-song on 16/5/6.
 */
public class KnowledgeProcessor {
    private Logger logger = LoggerFactory.getLogger(KnowledgeProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static KnowledgeBuilder builder = new KnowledgeBuilder();
    private static View viewer= new View();
    private static Set<String> set = new HashSet<String>();
    static{
        set.add("phenotype");
        set.add("gene");
        set.add("disease");
        set.add("drug");
        set.add("variation");
        set.add("protein");
        set.add("diseaseGene");
        set.add("geneDisease");
        set.add("variationArray");
    }


    public String search(JsonObject paramObj) {
        String from = null;
        String to = null;
        String limit = null;
        String query = null;
        JsonArray queryArry = null;
        int currentPage = 1;
        int pageSize = 12;
        String tableName = null;
        JsonArray genes = null;
        String diseaseParam = null;
        try{
            from = paramObj.get("from").getAsString();
            to = paramObj.get("to").getAsString();
            limit = paramObj.get("limit").getAsString();
            if("geneDisease".equals(from)
            	||("variationArray".equals(from)&&"disease".equals(to))
            	||("variationArray".equals(from)&&"drug".equals(to))
              ){
            	queryArry = paramObj.getAsJsonArray("query");
            }else{
            	query = paramObj.get("query").getAsString();
            }
            
            
            int[] li = ParamUtils.parseLimit(limit);
            currentPage = li[0];
            pageSize = li[1];
            if("drug".equals(to)&&!"geneDisease".equals(from)
            		&&!"variationArray".equals(from) ){
                tableName = paramObj.get("currentTable").getAsString();
            }
            
            //额外参数 
            if("diseaseGene".equals(from)&&"drug".equals(to)){
            	genes = paramObj.getAsJsonArray("genes");
            }
            if("geneDisease".equals(from)&&"drug".equals(to)){
            	diseaseParam = paramObj.get("disease").getAsString();
            }
            
        }catch (Exception e){
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        if(from.equals("case") || to.equals("case")){
            //涉及到病历搜索
            return "";
        }
        if(!isCheckParam(from)){
            return ParamUtils.errorParam("请求参数from非法");
        }
        if(!isCheckParam(to)){
            return ParamUtils.errorParam("请求参数to非法");
        }
        JsonObject newJson = buildQueryJson(from,to,query,currentPage,pageSize,tableName);
        //额外参数 
        if("diseaseGene".equals(from)&&"drug".equals(to)){
        	newJson.add("genes", genes);
        }
        
        if("geneDisease".equals(from)&&"drug".equals(to)){
        	newJson.addProperty("disease", diseaseParam);
        	newJson.add("query", queryArry);
        }
        if("geneDisease".equals(from)
            	||("variationArray".equals(from)&&"disease".equals(to))
            	||("variationArray".equals(from)&&"drug".equals(to))){
        	newJson.add("query", queryArry);
        }
        
        
        String newParam = gson.toJson(newJson);
        logger.info("knowledge req=" + newParam);
        String url = ConfigurationService.getUrlBean().getKnowledgeURL();
        logger.info("knowledge url=" + url);
        String resultStr = HttpRequestUtils.httpPost(url,newParam);
        logger.info("knowledge result=" + resultStr);
        JsonArray resultArray = null;
        if(StringUtils.isEmpty(resultStr)){
        	resultArray = builder.buildOnlyHead(newJson, new JsonObject());
        	return gson.toJson(resultArray);
        }else if ("variationArray_disease".equals(from+"_"+to)){
        	//陈松定的。按陈松说的修改的。
        	return  resultStr;
        }else{
        	JsonObject tmpResult = (JsonObject) jsonParser.parse(resultStr);
        	resultArray = builder.build(newJson,tmpResult);
        	return gson.toJson(resultArray);
        }
        
        
    }

    private JsonObject buildQueryJson(String from, String to, String query, int currentPage, int pageSize, String tableName) {
        JsonObject queryObj = new JsonObject();
        queryObj.addProperty("from",from);
        queryObj.addProperty("to",to);
        queryObj.addProperty("query",query);
        if(!"geneDisease".equals(from)&&!"variationArray".equals(from)){
        	 queryObj.addProperty("query",query);
        }
        
        queryObj.addProperty("currentPage",currentPage);
        queryObj.addProperty("pageSize",pageSize);
        queryObj.addProperty("currentTable",tableName);
        queryObj.addProperty("DEBUG",false);
        return queryObj;
    }

    private boolean isCheckParam(String from){
        return set.contains(from);
    }

    /**
     * 查询基因相关信息
     * @param paramObj
     */
    public String geneInfo(JsonObject paramObj) {
        JsonArray Gene = null;
        try{
            Gene = paramObj.getAsJsonArray("Gene");
        }catch (Exception e){
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");

        }
        JsonObject newParam = new JsonObject();
        newParam.add("Gene",Gene);
        String paramStr = gson.toJson(newParam);
        String url = ConfigurationService.getUrlBean().getKnowledgeGeneInfoURL();
        String resultStr = HttpRequestUtils.httpPost(url,paramStr);
        return resultStr;
    }

    public String variationInfo(JsonObject paramObj) {
        JsonArray Variation = null;
        try{
            Variation = paramObj.getAsJsonArray("Variation");
        }catch (Exception e){
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        JsonObject newParam = new JsonObject();
        newParam.add("Variation",Variation);
        String paramStr = gson.toJson(newParam);
        String url = ConfigurationService.getUrlBean().getKnowledgeVariationInfoURL();
        String resultStr = HttpRequestUtils.httpPost(url,paramStr);
        return resultStr;
    }

    public String detailVariationSearchDisease(JsonObject paramObj) {
        JsonArray Variation = null;
        try{
            Variation = paramObj.getAsJsonArray("Variation");
        }catch (Exception e){
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        JsonObject newParam = new JsonObject();
        newParam.add("Variation",Variation);
        String paramStr = gson.toJson(newParam);
        String url = ConfigurationService.getUrlBean().getKnowledgeDetailVariationSearchDiseaseURL();
        String resultStr = HttpRequestUtils.httpPost(url,paramStr);
        return  resultStr;
    }

    public String detailVariationSearchDrug(String param) {
        try{
            logger.info("DetailVariationSearchDrug param="+param);
            String url = ConfigurationService.getUrlBean().getKnowledgePharmGKBSearchDrugURL();
            String resultStr = HttpRequestUtils.httpPost(url,param);
            return resultStr;
        }catch (Exception e){
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }

    }
   
}
