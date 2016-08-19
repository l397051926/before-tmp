package com.gennlife.platform.processor;

import com.gennlife.platform.build.KnowledgeBuilder;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

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
        set.add("geneArray");
        set.add("clinicalTrial");
        set.add("pathway");
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
            	||(("variationArray".equals(from)&&"disease".equals(to)))
            	||("variationArray".equals(from)&&"drug".equals(to))
                    ||("geneArray".equals(from) && "pathway".equals(to))
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
            if("clinicalTrial".equals(to)){//查询临床试验,有表名
                tableName = paramObj.get("currentTable").getAsString();
            }
            if("geneArray".equals(from) && "pathway".equals(to)){
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
        if(("geneDisease".equals(from))
                ||("variationArray".equals(from)&&"disease".equals(to))
            	||("variationArray".equals(from)&&"drug".equals(to))
                ||("geneArray".equals(from) && "pathway".equals(to))
                )
        {
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

    /**
     * 详情页,知识库搜索
     * @param paramObj
     * @return
     */
    public String detailSearch(JsonObject paramObj) {
        try{
            JsonElement fsParam = paramObj.get("filter");
            paramObj.remove("filter");
            String fsParamStr = gson.toJson(fsParam);
            String url = ConfigurationService.getUrlBean().getCaseMolecular_detection();
            logger.info("请求fs参数:"+fsParamStr);
            String reStr = HttpRequestUtils.httpPost(url,fsParamStr);
            logger.info("请求fs结果:"+reStr);
            if(reStr == null){
                return ParamUtils.errorParam("FS返回为空,发生异常");
            }else{
                JsonObject resultObj = (JsonObject) jsonParser.parse(reStr);
                if(resultObj.has("success") && resultObj.get("success").getAsBoolean()){
                    JsonArray genes = new JsonArray();
                    JsonArray vars = new JsonArray();
                    String disease = null;
                    try{
                        JsonArray detection_result = resultObj.getAsJsonArray("detection_result");
                        for (JsonElement item:detection_result){
                            JsonObject itemObj = item.getAsJsonObject();
                            String GENE_SYMBOL = itemObj.get("GENE_SYMBOL").getAsString();
                            genes.add(GENE_SYMBOL);
                            String RS_ID =  itemObj.get("RS_ID").getAsString();
                            if(!".".equals(RS_ID)){
                                vars.add(RS_ID);
                            }

                        }
                        disease = resultObj.get("disease").getAsString();
                    }catch (Exception e){
                        logger.error("",e);
                        return ParamUtils.errorParam("FS解析异常");
                    }
                    String from = paramObj.get("from").getAsString();
                    String to = paramObj.get("to").getAsString();
                    if("diseaseGene".equals(from) && "drug".equals(to)){
                        paramObj.addProperty("query",disease);
                        paramObj.add("genes",genes);
                    }else if("geneDisease".equals(from) && "drug".equals(to)){
                        paramObj.add("query",genes);
                        paramObj.addProperty("disease",disease);
                    }else if("clinicalTrial".equals(from) && "clinicalTrial".equals(to)){
                        paramObj.addProperty("query",disease);
                    }else if("variationArray".equals(from) && "drug".equals(to)){
                        paramObj.add("query",vars);
                    }else {
                        return ParamUtils.errorParam("请求参数异常");
                    }
                    logger.info("通过FS数据 转化后请求参数:"+gson.toJson(paramObj));
                    return search(paramObj);
                }else{
                    return ParamUtils.errorParam("FS返回数据异常");
                }

            }



        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("发生异常");
        }
    }
}
