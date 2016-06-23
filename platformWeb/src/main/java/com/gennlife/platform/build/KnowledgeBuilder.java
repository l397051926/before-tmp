package com.gennlife.platform.build;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.gennlife.platform.util.DataFormatConversion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * Created by chen-song on 16/5/6.
 */
public class KnowledgeBuilder {
	/**
	 * 按照格式,构建最终结果
	 * @param param
	 * @param obj
	 * @return
	 */
	public JsonArray build(JsonObject param,JsonObject obj){
		JsonArray result = null;
		String to = param.get("to").getAsString();
		String from = param.get("from").getAsString();
		if("variationArray".equals(from)&&"disease".equals(to)){
			result = buildVariationArry2Disease(param,obj);
		}else if("variationArray".equals(from)&&"drug".equals(to)){
			result = buildVariationArry2Drug(param,obj);
		}else if("disease".equals(to)){
			result = buildDisease(param,obj);
		}else if("protein".equals(to)){
			result = buildProtein(param,obj);
		}else if("gene".equals(to)){
			result = buildGene(param,obj);
		}else if("variation".equals(to)){
			result = buildVariation(param,obj);
		}else if ("drug".equals(to)&&!"diseaseGene".equals(from)&&!"geneDisease".equals(from)){
			result = buildDrug(param,obj);	
		}else if("phenotype".equals(to)){
			result = buildPhenotype(param,obj);
		}else if("diseaseGene".equals(from)&&"drug".equals(to)){
			result = buildBiseaseGene(param,obj);
		}else if("geneDisease".equals(from)&&"drug".equals(to)){
			result = buildGeneBisease(param,obj);
		}else if("clinicalTrial".equals(to)){
			result = buildClinicalTrial(param,obj);
		}else if("pathway".equals(to) && "geneArray".equals(from)){
			result = buildPathway(param,obj);
		} else{
			result = buildGeneral(param,obj);
		}
		return result;
	}

	/**
	 * 通路分表,转化,陈松
	 * @param param
	 * @param obj
     * @return
     */
	private JsonArray buildPathway(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		String currentTable = param.get("currentTable").getAsString();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		String to = param.get("to").getAsString();
		String fromA2BOnC = from+"_"+currentTable;
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray PathwayObjs = obj.getAsJsonArray("data");
		if(null!=PathwayObjs && PathwayObjs.size()>0){
			JsonObject fristObj = (JsonObject) PathwayObjs.get(0);
			if(null != fristObj){
				JsonArray dataArray = fristObj.getAsJsonArray(currentTable);
				if(null == dataArray){
					dataArray = PathwayObjs;
				}

				if(null != dataArray){
					JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromA2BOnC, dataArray);
					if(null!=bodyRtn&&0<bodyRtn.size()){
						body = bodyRtn;
					}
				}
			}
		}
		result.add(body);
		return result;
	}

	/**
	 * 一般通用转化,即不分表转化,陈松
	 * @param param
	 * @param obj
     * @return
     */
	private JsonArray buildGeneral(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");
		String to = param.get("to").getAsString();
		String fromAToB = from+"_"+to;
		JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromAToB, dataArray);
		if(null!=bodyRtn&&0<bodyRtn.size()){
			body = bodyRtn;
		}
		result.add(body);
		return result;
	}

	private JsonArray buildClinicalTrial(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");

		//add by 唐乾斌 05.20
		String to = param.get("to").getAsString();
		String fromAToB = from+"_"+to;

		//跨域查询
		JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromAToB, dataArray);
		if(null!=bodyRtn&&0<bodyRtn.size()){
			body = bodyRtn;
		}
		result.add(body);
		return result;
	}

	public JsonArray buildOnlyHead(JsonObject param,JsonObject obj){
    	JsonArray result = new JsonArray();
    	JsonObject emptyObj = new JsonObject();
		JsonObject head = buildHead(param,emptyObj);
		result.add(head);
		return result;
	}
	
	
	private JsonArray buildVariationArry2Disease(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		//构建返回的头部
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		String to = param.get("to").getAsString();
		String fromA2B = from+"_"+to;;
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");
		
		
		JsonArray dataArray2 = new JsonArray();
		if(null!=dataArray&&dataArray.size()>0){
			for(int i=0,j=dataArray.size();i<j;i++){
				JsonElement je = dataArray.get(i);
				JsonObject joTemp = je.getAsJsonObject();
				Set<Entry<String, JsonElement>> set = joTemp.entrySet();
				for(Entry<String, JsonElement> em:set){
					String key = em.getKey();
					if(null!=em.getValue()){
						
						JsonArray joV = em.getValue().getAsJsonArray();
						if(null!=joV){
							for(int m=0,n=joV.size();m<n;m++){
								dataArray2.add(joV.get(m));
							}
							
						}
					}
				}
			}
			
			
			JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromA2B, dataArray2);
			if(null!=bodyRtn&&0<bodyRtn.size()){
				body = bodyRtn;
			}
		}
		result.add(body);
		return result;
	}
	private JsonArray buildVariationArry2Drug(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		String to = param.get("to").getAsString();
		String fromA2B = from+"_"+to;;
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");
		
		if(null!=dataArray&&dataArray.size()>0){
			JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromA2B, dataArray);
			if(null!=bodyRtn&&0<bodyRtn.size()){
				body = bodyRtn;
			}
		}
		result.add(body);
		return result;
	}
	private JsonArray buildBiseaseGene(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		String currentTable = param.get("currentTable").getAsString();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		String fromA2BOnC = from+"_"+currentTable;;
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray drugObjs = obj.getAsJsonArray("data");
		
		if(null!=drugObjs&&drugObjs.size()>0){
			JsonObject fdaObj = (JsonObject)drugObjs.get(0);
			if(null!=fdaObj){
				String currentTableName = null;
				if("drug_fda".equals(currentTable)){
					currentTableName = "fda";
				}else if("drug_nccn".equals(currentTable)){
					currentTableName = "nccn";
				}else if("drug_target".equals(currentTable)){
					currentTableName = "targetDrug";
				}
				JsonArray dataArray = fdaObj.getAsJsonArray(currentTableName);
				if(null!=dataArray){
					JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromA2BOnC, dataArray);
					if(null!=bodyRtn&&0<bodyRtn.size()){
						body = bodyRtn;
					}
				}
			}
		}
		result.add(body);
		return result;
	}
	private JsonArray buildGeneBisease(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		String to = param.get("to").getAsString();
		String fromA2B = from+"_"+to;;
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");
		
		if(null!=dataArray&&dataArray.size()>0){
			if(null!=dataArray){
				JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromA2B, dataArray);
				if(null!=bodyRtn&&0<bodyRtn.size()){
					body = bodyRtn;
				}
			}
		}
		result.add(body);
		return result;
	}
	private JsonArray buildPhenotype(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");

		//add by 唐乾斌 05.20
		String to = param.get("to").getAsString();
		String fromAToB = from+"_"+to;

		//跨域查询
		JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromAToB, dataArray);
		if(null!=bodyRtn&&0<bodyRtn.size()){
			body = bodyRtn;
		}
		result.add(body);
		return result;
	}

	//分表
	private JsonArray buildDrug(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		String currentTable = param.get("currentTable").getAsString();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		String to = param.get("to").getAsString();
		String fromA2BOnC = null;
		if("drug".equals(to)){
			fromA2BOnC = from+"_"+currentTable;
		}else{
			fromA2BOnC = from+"_"+to+"_"+currentTable;
		}
		
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray drugObjs = obj.getAsJsonArray("data");
		
		if(null!=drugObjs&&drugObjs.size()>0){
			JsonObject fdaObj = (JsonObject)drugObjs.get(0);
			if(null!=fdaObj){
				String currentTableName = null;
				if("drug_fda".equals(currentTable)){
					currentTableName = "fda";
				}else if("drug_nccn".equals(currentTable)){
					currentTableName = "nccn";
				}else if("drug_target".equals(currentTable)){
					currentTableName = "targetDrug";
				}else if("drug_variation".equals(currentTable)){
					currentTableName = "pharmDrug";
				}
				JsonArray dataArray = fdaObj.getAsJsonArray(currentTableName);
				if(null == dataArray){
					dataArray = drugObjs;
				}
				
				if(null!=dataArray){
					JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromA2BOnC, dataArray);
					if(null!=bodyRtn&&0<bodyRtn.size()){
						body = bodyRtn;
					}
				}
			}
		}
		result.add(body);
		return result;
	}


	/**
	 * 变异号：variation_num |数组 url（all）
     疾病ID: disease_id |数组 url（phenotype，disease）
     疾病：disease |string（phenotype，disease）
     基因：gene |数组 url（all）
     类型：type |string（phenotype,gene）
     染色体：chromosome |string (all)
     起点：start |string (all)
     参考等位基因：reference_allele|string(all)
     变异等位基因：alternate_allele|string(all)
     参考信息：ref |数组 url(all)
	 * @param param
	 * @param obj
	 * @return
	 */
	private JsonArray buildVariation(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		JsonObject head = buildHead(param,obj);
		String from = param.get("from").getAsString();
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");
		//add by 唐乾斌 05.20
		String to = param.get("to").getAsString();

		//跨域查询
		String fromAToB = from+"_"+to;
		JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromAToB, dataArray);
		if(null!=bodyRtn&&0<bodyRtn.size()){
			body = bodyRtn;
		}

		result.add(body);
		return result;

	}

	private JsonArray buildGene(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		JsonObject head = buildHead(param,obj);
		result.add(head);
		JsonArray body = new JsonArray();
		String from = param.get("from").getAsString();
		JsonArray dataArray = obj.getAsJsonArray("data");
		//add by 唐乾斌 05.20
		String to = param.get("to").getAsString();

		String fromAToB = from+"_"+to;
		JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromAToB, dataArray);
		if(null!=bodyRtn&&0<bodyRtn.size()){
			body = bodyRtn;
		}

		result.add(body);
		return result;
	}

	private JsonArray buildProtein(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		JsonObject head = buildHead(param,obj);
		result.add(head);
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");
		String from = param.get("from").getAsString();
		//add by 唐乾斌 05.20
		String to = param.get("to").getAsString();

		String fromAToB = from+"_"+to;
		JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromAToB, dataArray);
		if(null!=bodyRtn&&0<bodyRtn.size()){
			body = bodyRtn;
		}


		result.add(body);
		return result;
	}

	private JsonArray buildDisease(JsonObject param, JsonObject obj) {
		JsonArray result = new JsonArray();
		//head
		JsonObject head = buildHead(param,obj);
		result.add(head);
		String from = param.get("from").getAsString();

		String query = param.get("query").getAsString();
		//body
		JsonArray body = new JsonArray();
		JsonArray dataArray = obj.getAsJsonArray("data");

		//add by 唐乾斌 05.20
		String to = param.get("to").getAsString();
		String fromAToB = from+"_"+to;
		JsonArray bodyRtn = DataFormatConversion.knowledge2UIService(fromAToB, dataArray);
		if(null!=bodyRtn&&0<bodyRtn.size()){
			body = bodyRtn;
		}

		result.add(body);
		return result;
	}

	/**
	 * 构建疾病的Schema
	 * @param from
	 * @return
	 */
	private JsonArray diseaseSchema(String from){
		JsonArray schema = new JsonArray();
		if("phenotype".equals(from) || "disease".equals(from)){
			JsonObject entityid = new JsonObject();
			entityid.addProperty("name","disease_id");
			schema.add(entityid);
		}

		JsonObject entityDisease = new JsonObject();
		entityDisease.addProperty("name","disease");
		schema.add(entityDisease);

		if("phenotype".equals(from)){
			JsonObject phenotype_idDisease = new JsonObject();
			phenotype_idDisease.addProperty("name","phenotype_id");
			schema.add(phenotype_idDisease);

			JsonObject phenotypeDisease = new JsonObject();
			phenotypeDisease.addProperty("name","phenotype");
			schema.add(phenotypeDisease);
		}
		if("phenotype".equals(from)||"gene".equals(from)||"protein".equals(from)){
			JsonObject geneDisease = new JsonObject();
			geneDisease.addProperty("name","gene");
			schema.add(geneDisease);
		}

		if("drug".equals(from)){
			JsonObject drugDisease = new JsonObject();
			drugDisease.addProperty("name","drug");
			schema.add(drugDisease);

			JsonObject ncnn_refDisease = new JsonObject();
			ncnn_refDisease.addProperty("name","ncnn_ref");
			schema.add(ncnn_refDisease);

			JsonObject nci_refDisease = new JsonObject();
			nci_refDisease.addProperty("name","nci_ref");
			schema.add(nci_refDisease);

			JsonObject fda_refDisease = new JsonObject();
			fda_refDisease.addProperty("name","fda_ref");
			schema.add(fda_refDisease);
		}

		if("variation".equals(from)){
			JsonObject variation_idDisease = new JsonObject();
			variation_idDisease.addProperty("name","variation_id");
			schema.add(variation_idDisease);
		}

		if("disease".equals(from)){
			JsonObject entityICD10 = new JsonObject();
			entityICD10.addProperty("name","icd_10");
			schema.add(entityICD10);
		}

		JsonObject entityRef = new JsonObject();
		entityRef.addProperty("name","ref");
		schema.add(entityRef);

		return schema;
	}

	/**
	 * 构建返回数据的头部信息
	 * @param param
	 * @param obj
	 * @return
	 */
	private JsonObject buildHead(JsonObject param,JsonObject obj){
		JsonObject head = new JsonObject();
		int pageSize = param.get("pageSize").getAsInt();
		int currentPage = param.get("currentPage").getAsInt();

		head.addProperty("pageSize",pageSize);
		Integer counter = 0;
		if(null==obj.getAsJsonObject("info")){
			counter = 0;
		}else{
			String to = param.get("to").getAsString();
			String from = param.get("from").getAsString();
			if(to.equals("drug")&&
					!"geneDisease".equals(from) &&
					!"variationArray".equals(from)){
				String currentTable = param.get("currentTable").getAsString();
				String currentTableName = "";
				if("drug_fda".equals(currentTable)){
					currentTableName = "fda";
				}else if("drug_nccn".equals(currentTable)){
					currentTableName = "nccn";
				}else if("drug_target".equals(currentTable)){
					currentTableName = "targetDrug";
				}else if("drug_variation".equals(currentTable)){
					currentTableName = "pharmDrug";
				}
				counter = obj.getAsJsonObject("info").getAsJsonObject("counter").get(currentTableName).getAsInt();
			}else if ("".equals(from) && "".equals(to)){
				String currentTable = param.get("currentTable").getAsString();
				counter = obj.getAsJsonObject("info").getAsJsonObject("counter").get(currentTable).getAsInt();
			}else {
				counter = obj.getAsJsonObject("info").get("counter").getAsInt();
			}

		}
		head.addProperty("totalRecords",counter);
		int total_pages = 0;
		if(counter > 0){
			total_pages = counter % pageSize;
		}
		String from = param.get("from").getAsString();
		String to = param.get("to").getAsString();
		String currentTable = null;
		if(param.has("currentTable")){
			if(param.get("currentTable") instanceof JsonNull){
				currentTable = null;
			}else{
				currentTable = param.get("currentTable").getAsString();
			}
		}
				
		head.addProperty("total_pages",total_pages);
		head.addProperty("currentPage",currentPage);
		head.addProperty("from",from);
		head.addProperty("to",to);
		head.addProperty("limit",currentPage+","+pageSize);
		JsonArray schema = null;
		String schemaKey = null;
		
		if(StringUtils.isEmpty(currentTable)){
			schemaKey = from+"_"+to;
		}else{
			schemaKey = from+"_"+currentTable;
		}
		schema = DataFormatConversion.getSchema(schemaKey);
		head.add("schema",schema);
		if(null!=obj.getAsJsonArray("dimension")){
			head.add("dimension",obj.getAsJsonArray("dimension"));
		}
		return head;
	}

}
