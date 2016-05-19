package com.gennlife.platform.util;

import java.util.Map;

import org.json.JSONException;
import org.springframework.util.StringUtils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * knolegae 与 ui 之间的数据转换
 * 
 * @author 唐乾斌
 *
 */
public class DataFormatConversion {
	public static String template="{\"disease_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.id\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.term_name\"},\"gene\":{\"data_type\":\"array\",\"path\":\"$.Gene\",\"name\":\"symbol\",\"url\":\"geneSymbolUrl\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]},\"disease_id\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\"},\"disease\":{\"data_type\":\"string\",\"path\":\"$.Disease[0].name\"}},\"drug_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.hpoId\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.hpoTermName\"},\"gene\":{\"data_type\":\"single_array\",\"path_one\":\"$.geneSymbol\",\"path_two\":\"$.geneSymbolUrl\",\"name_one\":\"name\",\"name_two\":\"url\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.reference\",\"name\":\"ref\",\"defaultVal\":[]}},\"gene_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.hpoId\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.hpoTermName\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.reference\",\"name\":\"ref\",\"defaultVal\":[]}},\"variation_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.hpoId\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.hpoTermName\"},\"gene\":{\"data_type\":\"single_array\",\"path_one\":\"$.geneSymbol\",\"path_two\":\"$.geneSymbolUrl\",\"name_one\":\"name\",\"name_two\":\"url\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.Disease\",\"name\":\"name\",\"url\":\"url\",\"defaultVal\":[]}},\"protein_phenotype\":{\"phenotype_id\":{\"data_type\":\"string\",\"path\":\"$.id\"},\"phenotype\":{\"data_type\":\"string\",\"path\":\"$.term_name\"},\"Gene\":{\"data_type\":\"array\",\"path\":\"$.Gene\",\"name\":\"geneSymbol\",\"url\":\"geneSymbolUrl\"},\"ref\":{\"data_type\":\"array\",\"path\":\"$.reference\",\"name\":\"ref\",\"defaultVal\":[]}}}";
	public static String dis2Ph = "";

	public static JsonContext tmpJc = new JsonContext();
	static {
		tmpJc.parse(template);
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
			
			switch (dataType) {
			case "string":
				currentFieldValue = fieldValueStr(objMeta,sourceData);
				newObj.put(currentFieldName, currentFieldValue);
				break;
			case "single_array":
				currentFieldValue = fieldValueSingleArray(objMeta,sourceData);
				break;
			case "array":
				currentFieldValue = fieldValueArray(objMeta,sourceData);
				break;
			default:
				break;
			}
			newObj.put(currentFieldName, currentFieldValue);
		}
		
		return newObj;
	}
	
	
	//处理string
	public static Object fieldValueStr(JSONObject meta,Object sourceData){
		String path = (String)meta.get("path");
		JSONObject sourceObj = (JSONObject)sourceData;
		String strValue = JsonPath.read(sourceObj.toString(), path);
		
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
		jo.put(fieldOne, JsonPath.read(jsonStr, pathOne));
		jo.put(fieldTwo, JsonPath.read(jsonStr, pathTwo));
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
	public static void main(String[] args) {
//		String strData = "disease_phenotype	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"disease\",\"to\":\"phenotype\",\"query\":\"Histiocytosis-lymphadenopathy plus syndrome, 602782 (3)\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "drug_phenotype	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"drug\",\"to\":\"phenotype\",\"query\":\"(R)-3-BROMO-2-HYDROXY-2-METHYL-N-[4-NITRO-3-(TRIFLUOROMETHYL)PHENYL]PROPANAMIDE\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "protein_phenotype	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"protein\",\"to\":\"phenotype\",\"query\":\"Protoheme IX farnesyltransferase, mitochondrial\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
	
//		String strData = "phenotype_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"phenotype\",\"to\":\"disease\",\"query\":\"Decreased nerve conduction velocity\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "gene_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"gene\",\"to\":\"disease\",\"query\":\"FASLG\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "protein_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"protein\",\"to\":\"disease\",\"query\":\"Myoblast determination protein 1\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "variation_disease	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"variation\",\"to\":\"disease\",\"query\":\"CN187210\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		
//		String strData = "disease_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"disease\",\"to\":\"gene\",\"query\":\"Burkitt lymphoma, B-NHL\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "drug_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"drug\",\"to\":\"gene\",\"query\":\"Chlorcyclizine\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "phenotype_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"phenotype\",\"to\":\"gene\",\"query\":\"Decreased nerve conduction velocity\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "protein_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"protein\",\"to\":\"gene\",\"query\":\"Protoheme IX farnesyltransferase, mitochondrial\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "variation_gene	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"variation\",\"to\":\"gene\",\"query\":\"rs144332606\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		
//		String strData = "drug_variation	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"drug\",\"to\":\"variation\",\"query\":\"trastuzumab\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "gene_variation	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"gene\",\"to\":\"variation\",\"query\":\"FASLG\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
		String strData = "phenotype_variation	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"phenotype\",\"to\":\"variation\",\"query\":\"Schmorl's node\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";
//		String strData = "protein_variation	http://192.168.1.111:9881/knowledge/graph?param={\"from\":\"protein\",\"to\":\"variation\",\"query\":\"Ankyrin repeat domain-containing protein 55\",\"currentPage\":1,\"pageSize\":12,\"DEBUG\":true}";

		
		//		Object o = testTemplate("disease_phenotype");
		String[] pair = strData.split("\t");
		String url = pair[1].split("param=")[0];
		url = url.replace("?", "");
		String param = pair[1].split("param=")[1];
		String json = HttpRequestUtils.httpPost(url, param);
		System.out.println(json);
//		Object o = testTemplate(pair[0],json);
//		System.out.println(o);
	}
}
