package com.gennlife.platform.util;

import com.google.gson.*;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * knowledge 与 ui 之间的数据转换
 * 
 * @author 唐乾斌
 *
 */
public class DataFormatConversion{
	private static final Logger logger = LoggerFactory.getLogger(DataFormatConversion.class);
	public static String templateDefault = "";
	public static String template=null;
	public static String dis2Ph = "";
	public static JsonParser jsonParser = new JsonParser();
	public static JsonContext tmpJc = new JsonContext();
	public static long start = System.currentTimeMillis();
	public static int DATA_TYPE_STRING = 1;
	public static int DATA_TYPE_SINGLE_ARRAY = 2;
	public static int DATA_TYPE_ARRAY = 3;
	public static String KNOWLEDGE_TEMPLE_FILE="/knowledge_transformation.json";
	public static String SCHEMA_FILE="/schema.json";
	public static String SCHEMA_STR= null;
	
	public static JsonObject SCHEMAJO = null;
	
	static {
		loadSchema();
		loadTemplate();
	}
	public static void reload(){
		loadSchema();
		loadTemplate();
	}
	
	public static void loadSchema(){
		try {
			SCHEMA_STR = FilesUtils.readFile(SCHEMA_FILE);
		} catch (IOException e) {
			logger.error(e.toString());
		}
		JsonParser jsonParser = new JsonParser();
		try {
			SCHEMAJO = null;
			SCHEMAJO = (JsonObject) jsonParser.parse(SCHEMA_STR);
		} catch (JsonSyntaxException e) {
			logger.error(e.toString());
		}
		
	}
	
	public static void loadTemplate(){
		try {
			template = FilesUtils.readFile(KNOWLEDGE_TEMPLE_FILE);
		} catch (IOException e) {
			logger.error(e.toString());
			template = templateDefault;
		}
		tmpJc.parse(template);
	}
	
	@SuppressWarnings("null")
	public static JsonArray getSchema(String schemaKey){
		JsonElement je = SCHEMAJO.get(schemaKey);
		if(null!=je){
			return je.getAsJsonArray();
		}else{
			return new JsonArray();
		}
		
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
			}else if("array_rename".equals(dataType)){
				currentFieldValue = fieldValueArrayRename(objMeta,sourceData);
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
			if(!StringUtils.isEmpty(fieldOne)&&!StringUtils.isEmpty(pathOne)){
				jo.put(fieldOne, JsonPath.read(jsonStr, pathOne));
			}else if(!StringUtils.isEmpty(fieldOne)&&StringUtils.isEmpty(pathOne)){
				jo.put(fieldOne, "");
			}
		} catch (Exception e) {
			jo.put(fieldOne, "");
			logger.error(e.toString());
		}
		try {
			if(!StringUtils.isEmpty(fieldTwo)&&StringUtils.isEmpty(pathTwo)){
				jo.put(fieldTwo, "");
			}else if(!StringUtils.isEmpty(fieldTwo)&&!StringUtils.isEmpty(pathTwo)){
				jo.put(fieldTwo, JsonPath.read(jsonStr, pathTwo));
			}
			
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

		String path = (String)metaData.get("path");
		String name = (String)metaData.get("name");
		String url = (String)metaData.get("url");


		JsonContext jc = new JsonContext();
		jc.parse(sourceData.toString());
		Object arrayObj = null;
		try {
			arrayObj = jc.read(path);
		} catch (Exception e) {
			logger.error(e.toString());
			return data;
		}
		
		
		if(arrayObj instanceof LinkedHashMap){
			return data;
		}
		
		JSONArray ja = (JSONArray) arrayObj;
		if(null==ja||0==ja.size()){
			return data;
		}
		for(int i=0,j=ja.size();i<j;i++){
			net.minidev.json.JSONObject jo = new net.minidev.json.JSONObject();
			Map<String,Object> o = (Map<String, Object>) ja.get(i);
			if(!StringUtils.isEmpty(name)&&null!=o.get(name)){
				jo.put("name", o.get(name));
			}else{
				jo.put("name", "");
			}
			if(!StringUtils.isEmpty(url)&&null!=o.get(url)){
				jo.put("url", o.get(url));
			}else{
				jo.put("url", "");
			}
			data.add(jo);
		}
		return  data;
	}
	public static Object fieldValueArrayRename(JSONObject metaData,Object sourceData){

		JSONArray data = new JSONArray();
		if(null==sourceData ){
			return data;
		}


		String path = (String)metaData.get("path");
		String name = (String)metaData.get("name");
		String url = (String)metaData.get("url");
		
		String keyName = (String)metaData.get("keyName");
		String valueName = (String)metaData.get("valueName");

		JsonContext jc = new JsonContext();
		jc.parse(sourceData.toString());
		Object arrayObj = null;
		try {
			arrayObj = jc.read(path);
		} catch (Exception e) {
			logger.error(e.toString());
			return data;
		}
		JSONArray ja = (JSONArray) arrayObj;
		if(null==ja||0==ja.size()){
			return data;
		}
		for(int i=0,j=ja.size();i<j;i++){
			net.minidev.json.JSONObject jo = new net.minidev.json.JSONObject();
			Map<String,Object> o = (Map<String, Object>) ja.get(i);
			jo.put(keyName, o.get(name));
			jo.put(valueName, o.get(url));
			
			data.add(jo);
		}
		return  data;
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

