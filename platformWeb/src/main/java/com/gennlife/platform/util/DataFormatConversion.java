package com.gennlife.platform.util;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * knowledge 与 ui 之间的数据转换
 * 
 * @author 唐乾斌
 *
 */
public class DataFormatConversion implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(DataFormatConversion.class);
	public static String templateDefault = "{\"disease_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.id\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.term_name\"},\"gene\":{\"data_type\":\"array\",\"path\":\"$.Gene\",\"name\":\"symbol\",\"url\":\"geneSymbolUrl\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]},\"disease_id\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\"},\"disease\":{\"data_type\":\"string\",\"path\":\"$.Disease[0].name\"}},\"drug_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.hpoId\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.hpoTermName\"},\"gene\":{\"data_type\":\"single_array\",\"path_one\":\"$.geneSymbol\",\"path_two\":\"$.geneSymbolUrl\",\"name_one\":\"name\",\"name_two\":\"url\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.reference\",\"name\":\"ref\",\"defaultVal\":[]}},\"gene_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.hpoId\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.hpoTermName\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.reference\",\"name\":\"ref\",\"defaultVal\":[]}},\"variation_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.hpoId\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.hpoTermName\"},\"gene\":{\"data_type\":\"single_array\",\"path_one\":\"$.geneSymbol\",\"path_two\":\"$.geneSymbolUrl\",\"name_one\":\"name\",\"name_two\":\"url\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]}},\"protein_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.id\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.term_name\"},\"Gene\":{\"data_type\":\"array\",\"path\":\"$.Gene\",\"name\":\"geneSymbol\",\"url\":\"geneSymbolUrl\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.reference\",\"name\":\"ref\",\"defaultVal\":[]}},\"phenotype_disease\":{\"disease_id\":{\"data_type\":\"string\",\"path\":\"$.id\"},\"disease\":{\"data_type\":\"string\",\"path\":\"$.name\"},\"phenotype_id\":{\"data_type\":\"single_array\",\"path_one\":\"$.Phenotype[0].id\",\"path_two\":\"$.Phenotype[0].url\",\"name_one\":\"name\",\"name_two\":\"url\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.Phenotype[0].term_name\"},\"Gene\":{\"data_type\":\"array\",\"path\":\"$.Gene\",\"name\":\"symbol\",\"url\":\"url\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]}},\"gene_disease\":{},\"protein_disease\":{\"disease\":{\"data_type\":\"string\",\"path\":\"存疑\"},\"gene\":{\"data_type\":\"single_array\",\"path_one\":\"$.geneSymbol\",\"path_two\":\"$.geneSymbolUrl\",\"name_one\":\"name\",\"name_two\":\"url\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]}},\"drug_disease\":{\"disease\":{\"data_type\":\"string\",\"path\":\"$.name\"}},\"variation_disease\":{},\"drug_variation\":{\"variation_num\":{\"data_type\":\"string\",\"path\":\"$.id\"},\"gene\":{\"data_type\":\"single_array\",\"path_one\":\"$.geneSymbol\",\"path_two\":\"$.geneSymbolUrl\",\"name_one\":\"name\",\"name_two\":\"url\"},\"chromosome\":{\"data_type\":\"string\",\"path\":\"$.chromosome\"},\"start\":{\"data_type\":\"string\",\"path\":\"$.start\"},\"reference_allele\":{\"data_type\":\"string\",\"path\":\"$.ref\"},\"alternate_allele\":{\"data_type\":\"string\",\"path\":\"$.alt\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]}},\"gene_variation\":{\"variation_num\":{\"data_type\":\"string\",\"path\":\"$.variationId\"},\"gene\":{\"data_type\":\"single_array\",\"path_one\":\"$.geneSymbol\",\"path_two\":\"$.geneSymbolUrl\",\"name_one\":\"name\",\"name_two\":\"url\"},\"chromosome\":{\"data_type\":\"string\",\"path\":\"$.chromosome\"},\"start\":{\"data_type\":\"string\",\"path\":\"$.start\"},\"reference_allele\":{\"data_type\":\"string\",\"path\":\"$.ref\"},\"alternate_allele\":{\"data_type\":\"string\",\"path\":\"$.alt\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]}},\"phenotype_variation\":{\"variation_num\":{\"data_type\":\"string\",\"path\":\"$.id\"},\"disease_id\":{\"data_type\":\"single_array\",\"path_one\":\"$.geneSymbol\",\"path_two\":\"$.geneSymbolUrl\",\"name_one\":\"name\",\"name_two\":\"url\"},\"type\":{\"data_type\":\"string\",\"path\":\"$.chromosome\"},\"start\":{\"data_type\":\"string\",\"path\":\"$.start\"},\"reference_allele\":{\"data_type\":\"string\",\"path\":\"$.ref\"},\"alternate_allele\":{\"data_type\":\"string\",\"path\":\"$.alt\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]}}}";
	public static String template=null;
	public static String dis2Ph = "";
	public static JsonParser jsonParser = new JsonParser();
	public static JsonContext tmpJc = new JsonContext();
	public static long start = System.currentTimeMillis();
	public static int DATA_TYPE_STRING = 1;
	public static int DATA_TYPE_SINGLE_ARRAY = 2;
	public static int DATA_TYPE_ARRAY = 3;
	static {
		try {
			template = FilesUtils.readFile("/knowledge_transformation.json");
		} catch (IOException e) {
			logger.error(e.toString());
			template = templateDefault;
		}
		tmpJc.parse(template);
		
		
	}
	/**
	 * 知识库数据格式转换到ui service数据格式
	 * @param fromAToB 例：fromAToB="disease_phenotype";表示从疾病查表型
	 * @param ja
	 * @return
	 */
	public static JsonArray knowledge2UIService(String fromAToB,JsonArray ja){
		JsonArray data = new JsonArray();
		if(null==ja||0==ja.size()){
			return data;
		}
		logger.info("json path=====>@."+fromAToB);
		org.json.JSONArray data2 = new org.json.JSONArray();
		Object formatObj = tmpJc.read("@."+fromAToB);
		Map<String,Object> map = (Map<String,Object>) formatObj;
		JSONObject metaData = new JSONObject(map);
		JSONArray ja2 = JsonPath.read(ja.toString(), "$");
		for(int i=0,j=ja2.size();i<j;i++){
			Object oldObjTemp = ja2.get(i);
			JSONObject sourceData = null;
			if(oldObjTemp instanceof Map){
				sourceData = new JSONObject((Map)ja2.get(i));
			}else{
				sourceData = (JSONObject)ja2.get(i);
			}
			JSONObject newObj = tranlate(metaData, sourceData);
			data2.put(newObj);
		}
		try {
			JsonArray tmpArray = (JsonArray) jsonParser.parse(data2.toString());
			data = tmpArray;
		} catch (JsonSyntaxException e) {
			logger.error(e.toString());
		}
		return data;
	}
	public static org.json.JSONArray knowledge2UIService(String fromAToB,JSONArray ja){
		org.json.JSONArray data = new org.json.JSONArray();
		if(null==ja||0==ja.size()){
			return data;
		}
		Object formatObj = tmpJc.read("@."+fromAToB);
		Map<String,Object> map = (Map<String,Object>) formatObj;
		JSONObject metaData = new JSONObject(map);
		for(int i=0,j=ja.size();i<j;i++){
			Object oldObjTemp = ja.get(i);
			JSONObject sourceData = null;
			if(oldObjTemp instanceof Map){
				sourceData = new JSONObject((Map)ja.get(i));
			}else{
				sourceData = (JSONObject)ja.get(i);
			}
			JSONObject newObj = tranlate(metaData, sourceData);
			data.put(newObj);
		}
		return data;
	}

	public static org.json.JSONArray testTemplate(String fromAToB,String json){
		JsonContext jc = new JsonContext();
		jc.parse(json);
		JSONArray ja = jc.read("$.data",JSONArray.class);
		org.json.JSONArray data = new org.json.JSONArray();
		if(null==ja||0==ja.size()){
			return data;
		}
		Object formatObj = tmpJc.read("@."+fromAToB);
		Map<String,Object> map = (Map<String,Object>) formatObj;
		JSONObject metaData = new JSONObject(map);
		for(int i=0,j=ja.size();i<j;i++){
			Object oldObjTemp = ja.get(i);
			JSONObject sourceData = null;
			if(oldObjTemp instanceof Map){
				sourceData = new JSONObject((Map)ja.get(i));
			}else{
				sourceData = (JSONObject)ja.get(i);
			}
			JSONObject newObj = tranlate(metaData, sourceData);
			data.put(newObj);
		}
		return data;
	}

	public static JsonArray testTemplate2(String fromAToB,String json){
		JsonContext jc = new JsonContext();
		jc.parse(json);
		JSONArray ja = jc.read("$.data",JSONArray.class);
		JsonArray data = new JsonArray();
		org.json.JSONArray data2 = new org.json.JSONArray();
		if(null==ja||0==ja.size()){
			return data;
		}
		Object formatObj = tmpJc.read("@."+fromAToB);
		Map<String,Object> map = (Map<String,Object>) formatObj;
		JSONObject metaData = new JSONObject(map);
		for(int i=0,j=ja.size();i<j;i++){
			Object oldObjTemp = ja.get(i);
			JSONObject sourceData = null;
			if(oldObjTemp instanceof Map){
				sourceData = new JSONObject((Map)ja.get(i));
			}else{
				sourceData = (JSONObject)ja.get(i);
			}
			JSONObject newObj = tranlate(metaData, sourceData);
			if(null!=newObj){
				data2.put(newObj);
			}
		}
		try {
			JsonArray tmpArray = (JsonArray) jsonParser.parse(data2.toString());
			data = tmpArray;
		} catch (JsonSyntaxException e) {
			logger.error(e.toString());
		}
		return data;
	}

	/**
	 * 
	 * @param meta
	 * @param sourceData
	 * @return
	 */
	public static JSONObject tranlate(JSONObject meta,Object sourceData){

		JSONObject newObj = new JSONObject();
		for(String key:meta.keySet()){
			Object tempObj = meta.get(key);
			JSONObject objMeta = null;
  			if(tempObj instanceof Map){
				objMeta = new JSONObject((Map)meta.get(key));
			}else{
				objMeta = (JSONObject) meta.get(key);
			}
			String currentFieldName = key;
			String dataType=(String) objMeta.get("data_type");
			

			Object currentFieldValue = null;
			if("string".equals(dataType)){
				currentFieldValue = fieldValueStr(objMeta,sourceData);
				newObj.put(currentFieldName, currentFieldValue);
			}else if("single_array".equals(dataType)){
				currentFieldValue = fieldValueSingleArray(objMeta,sourceData);
			}else if("array".equals(dataType)){
				currentFieldValue = fieldValueArray(objMeta,sourceData);
			}
			
			
//			switch (dataType) {
//			case "string":
//				currentFieldValue = fieldValueStr(objMeta,sourceData);
//				newObj.put(currentFieldName, currentFieldValue);
//				break;
//			case "single_array":
//				currentFieldValue = fieldValueSingleArray(objMeta,sourceData);
//				break;
//			case "array":
//				currentFieldValue = fieldValueArray(objMeta,sourceData);
//				break;
//			default:
//				break;
//			}
			newObj.put(currentFieldName, currentFieldValue);
		}

		return newObj;
	}




	//处理string
	public static Object fieldValueStr(JSONObject meta,Object sourceData){
		String path = (String)meta.get("path");
		JSONObject sourceObj = (JSONObject)sourceData;
		String strValue = null;
		try {
			Object objStr = JsonPath.read(sourceObj.toString(), path);
			if(null!=objStr){
				strValue = objStr.toString();
				strValue = StringUtils.replace(strValue, "[", "");
				strValue = StringUtils.replace(strValue, "]", "");
				strValue = StringUtils.replace(strValue, "\"", "");
			}else{
				strValue = "";
			}
		} catch (Exception e) {
			strValue = "";
		}

		return  strValue;
	}
	//处理single_array


	public static Object fieldValueSingleArray(JSONObject meta,Object sourceData){
		JSONArray data = new JSONArray();
		if(null==sourceData ){
			return data;
		}
		String jsonStr = sourceData.toString();
		if(StringUtils.isEmpty(sourceData.toString())){
			return data;
		}



		String pathOne = (String)meta.get("path_one");
		String pathTwo = (String)meta.get("path_two");

		String fieldOne = (String)meta.get("name_one");
		String fieldTwo = (String)meta.get("name_two");
		net.minidev.json.JSONObject jo = new net.minidev.json.JSONObject();
		try {
			jo.put(fieldOne, JsonPath.read(jsonStr, pathOne));
		} catch (Exception e) {
			jo.put(fieldOne, "");
			logger.error(e.toString());
		}
		try {
			jo.put(fieldTwo, JsonPath.read(jsonStr, pathTwo));
		} catch (Exception e) {
			jo.put(fieldTwo, "");
			logger.error(e.toString());
		}
		
		data.add(jo);

		return  data;
	}
	//处理array
	public static Object fieldValueArray(JSONObject metaData,Object sourceData){

		JSONArray data = new JSONArray();
		if(null==sourceData ){
			return data;
		}

		Object defaultVal = null;
		try {
			defaultVal=metaData.get("defaultVal");
		} catch (JSONException e) {
		}
		if(null!=defaultVal){
			return data;
		}

		String path = (String)metaData.get("path");
		String name = (String)metaData.get("name");
		String url = (String)metaData.get("url");


		JsonContext jc = new JsonContext();
		jc.parse(sourceData.toString());
		Object arrayObj = jc.read(path);
		try {
			arrayObj = jc.read(path);
		} catch (Exception e) {
			arrayObj = new JSONArray();
			logger.error(e.toString());
		}
		JSONArray ja = (JSONArray) arrayObj;
		if(null==ja||0==ja.size()){
			return data;
		}
		for(int i=0,j=ja.size();i<j;i++){
			net.minidev.json.JSONObject jo = new net.minidev.json.JSONObject();
			Map<String,Object> o = (Map<String, Object>) ja.get(i);
			jo.put("name", o.get(name));
			jo.put("url", o.get(url));
			data.add(jo);
		}
		return  data;
	}
	/**
	 * 定时更新模板
	 */
	@Override
	public void run() {
		while(true){
			if((System.currentTimeMillis()-start)<(10*1000)){
				String newTemplate;
				try {
					newTemplate = FilesUtils.readFile("/knowledge_transformation.json");
					if(!StringUtils.isEmpty(newTemplate)&&!newTemplate.equals(DataFormatConversion.template)){
						DataFormatConversion.template = FilesUtils.readFile("/knowledge_transformation.json");
					}
				} catch (IOException e) {
					logger.error(e.toString());
				}
			}
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
//		String strData = "disease_phenotype	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"disease\",\"to\":\"phenotype\",\"query\":\"Histiocytosis-lymphadenopathy plus syndrome, 602782 (3)\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		//		String strData = "drug_phenotype	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"drug\",\"to\":\"phenotype\",\"query\":\"(R)-3-BROMO-2-HYDROXY-2-METHYL-N-[4-NITRO-3-(TRIFLUOROMETHYL)PHENYL]PROPANAMIDE\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		//		String strData = "protein_phenotype	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"protein\",\"to\":\"phenotype\",\"query\":\"Protoheme IX farnesyltransferase, mitochondrial\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		Cone-shaped epiphysis
//		String strData = "gene_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"disease\",\"to\":\"disease\",\"query\":\"Cone-shaped epiphysis\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//				String strData = "phenotype_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"phenotype\",\"to\":\"disease\",\"query\":\"Decreased nerve conduction velocity\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "phenotype_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"phenotype\",\"to\":\"disease\",\"query\":\"Cone-shaped epiphysis\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//				String strData = "gene_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"gene\",\"to\":\"disease\",\"query\":\"FASLG\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//				String strData = "protein_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"protein\",\"to\":\"disease\",\"query\":\"Myoblast determination protein 1\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		//		String strData = "variation_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"variation\",\"to\":\"disease\",\"query\":\"CN187210\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";

//				String strData = "disease_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"disease\",\"to\":\"gene\",\"query\":\"Burkitt lymphoma, B-NHL\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//				String strData = "drug_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"drug\",\"to\":\"gene\",\"query\":\"Chlorcyclizine\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
				String strData = "phenotype_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"phenotype\",\"to\":\"gene\",\"query\":\"Decreased nerve conduction velocity\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		//		String strData = "protein_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"protein\",\"to\":\"gene\",\"query\":\"Protoheme IX farnesyltransferase, mitochondrial\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		//		String strData = "variation_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"variation\",\"to\":\"gene\",\"query\":\"rs144332606\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";

		//		String strData = "drug_variation	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"drug\",\"to\":\"variation\",\"query\":\"trastuzumab\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		//		String strData = "gene_variation	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"gene\",\"to\":\"variation\",\"query\":\"FASLG\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		//		String strData = "phenotype_variation	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"phenotype\",\"to\":\"variation\",\"query\":\"Schmorl's node\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		//		String strData = "protein_variation	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"protein\",\"to\":\"variation\",\"query\":\"Ankyrin repeat domain-containing protein 55\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";


		//		Object o = testTemplate("disease_phenotype");
				System.out.println(strData);
		String[] pair = strData.split("\t");
		String url = pair[1].split("param=")[0];
		url = url.replace("?", "");
		String param = pair[1].split("param=")[1];
		String json = HttpRequestUtils.httpPost(url, param);
				System.out.println(json);
		//		Object o = testTemplate(pair[0],json);
//				Object o = testTemplate2(pair[0],json);
		JsonObject tmpResult = (JsonObject) jsonParser.parse(json);
		JsonArray dataArray = tmpResult.getAsJsonArray("data");
//		long start = System.currentTimeMillis();
		Object o = knowledge2UIService(pair[0],dataArray);
//		System.out.println("耗时->"+(System.currentTimeMillis()-start));

		System.out.println(o);
	}
}

