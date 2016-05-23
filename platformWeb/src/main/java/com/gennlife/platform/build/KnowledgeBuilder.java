package com.gennlife.platform.build;

import com.gennlife.platform.util.DataFormatConversion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

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
		if("disease".equals(to)){
			result = buildDisease(param,obj);
		}else if("protein".equals(to)){
			result = buildProtein(param,obj);
		}else if("gene".equals(to)){
			result = buildGene(param,obj);
		}else if("variation".equals(to)){
			result = buildVariation(param,obj);
		}else if ("drug".equals(to)){
			result = buildDrug(param,obj);
		}else if("phenotype".equals(to)){
			result = buildPhenotype(param,obj);
		}
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
//		if("phenotype".equals(from) || "disease".equals(from)){
		if("phenotype".equals(from)){
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
		Integer counter = obj.getAsJsonObject("info").get("counter").getAsInt();
		head.addProperty("totalRecords",counter);
		int total_pages = 0;
		if(counter > 0){
			total_pages = counter % pageSize;
		}
		String from = param.get("from").getAsString();
		String to = param.get("to").getAsString();
		head.addProperty("total_pages",total_pages);
		head.addProperty("currentPage",currentPage);
		head.addProperty("from",from);
		head.addProperty("to",to);
		head.addProperty("limit",currentPage+","+pageSize);
		JsonArray schema = null;
		if("disease".equals(to)){
			schema = diseaseSchema(from);
		}else if ("protein".equals(to)){
			schema = proteinSchema(from);
		}else if ("gene".equals(to)){
			schema = geneSchema(from);
		}else if("variation".equals(to)){
			schema = variationSchema(from);
		}else if("drug".equals(to)){
			String tableName = param.get("currentTable").getAsString();
			if("drug".equals(tableName)){
				schema = drugSchema(from);
			}else if("drug_fda".equals(tableName)){
				schema = drugUseSchema(from);
			}else if("drug_nccn".equals(tableName)){
				schema = drugUseSchema(from);
			}else if("drug_gene".equals(tableName)){
				schema = drugGENESchema(from);
			}else if ("drug_target".equals(tableName)){
				schema = drugTargetSchema(from);
			}else if("drug_variation".equals(tableName)){
				schema = drugVariationSchema(from);
			}

		}else if("phenotype".equals(to)){
			schema = phenotypeSchema(from);
		}
		head.add("schema",schema);
		head.add("dimension",obj.getAsJsonArray("dimension"));
		return head;
	}

	private JsonArray phenotypeSchema(String from) {
		JsonArray schema = new JsonArray();

		JsonObject phenotype_idPhenotype = new JsonObject();
		phenotype_idPhenotype.addProperty("name","phenotype_id");
		schema.add(phenotype_idPhenotype);

		JsonObject phenotypePhenotype = new JsonObject();
		phenotypePhenotype.addProperty("name","phenotype");
		schema.add(phenotypePhenotype);

		if("protein".equals(from)||"drug".equals(from)||"variation".equals(from)||"".equals(from)){
			JsonObject genePhenotype = new JsonObject();
			genePhenotype.addProperty("name","gene");
			schema.add(genePhenotype);
		}

		JsonObject refPhenotype = new JsonObject();
		refPhenotype.addProperty("name","ref");
		schema.add(refPhenotype);
		if("disease".equals(from)){
			JsonObject disease_idPhenotype = new JsonObject();
			disease_idPhenotype.addProperty("name","disease_id");
			schema.add(disease_idPhenotype);

			JsonObject diseasePhenotype = new JsonObject();
			diseasePhenotype.addProperty("name","disease");
			schema.add(diseasePhenotype);
		}
		return schema;
	}

	private JsonArray drugVariationSchema(String from) {
		JsonArray schema = new JsonArray();
		if("variation".equals(from)){
			JsonObject drugDrug = new JsonObject();
			drugDrug.addProperty("name","drug");
			schema.add(drugDrug);

			JsonObject variationDrug = new JsonObject();
			variationDrug.addProperty("name","variation");
			schema.add(variationDrug);

			JsonObject refDrug = new JsonObject();
			refDrug.addProperty("name","ref");
			schema.add(refDrug);
		}

		return schema;
	}

	private JsonArray drugTargetSchema(String from) {
		JsonArray schema = new JsonArray();
		if("gene".equals(from)||"protein".equals(from)||"drug".equals(from)||"disease".equals(from)){
			JsonObject drugDrug = new JsonObject();
			drugDrug.addProperty("name","drug");
			schema.add(drugDrug);

			JsonObject brand_nameDrug = new JsonObject();
			brand_nameDrug.addProperty("name","brand_name");
			schema.add(brand_nameDrug);

			JsonObject geneDrug = new JsonObject();
			geneDrug.addProperty("name","gene");
			schema.add(geneDrug);

			JsonObject typeDrug = new JsonObject();
			typeDrug.addProperty("name","type");
			schema.add(typeDrug);

			JsonObject approve_statusDrug = new JsonObject();
			approve_statusDrug.addProperty("name","approve_status");
			schema.add(approve_statusDrug);

			JsonObject refDrug = new JsonObject();
			refDrug.addProperty("name","ref");
			schema.add(refDrug);
		}
		return schema;
	}

	private JsonArray drugUseSchema(String from) {
		JsonArray schema = new JsonArray();
		if("phenotype".equals(from) || "drug".equals(from) ||"disease".equals(from)||"gene".equals(from)){
			JsonObject drugDrug = new JsonObject();
			drugDrug.addProperty("name","drug");
			schema.add(drugDrug);

			JsonObject brand_nameDrug = new JsonObject();
			brand_nameDrug.addProperty("name","brand_name");
			schema.add(brand_nameDrug);

			JsonObject typeDrug = new JsonObject();
			typeDrug.addProperty("name","disease");
			schema.add(typeDrug);

			JsonObject biomarkerDrug = new JsonObject();
			biomarkerDrug.addProperty("name","biomarker");
			schema.add(biomarkerDrug);

			JsonObject referenced_subgroupDrug = new JsonObject();
			referenced_subgroupDrug.addProperty("name","referenced_subgroup");
			schema.add(referenced_subgroupDrug);

			JsonObject indicationDrug = new JsonObject();
			indicationDrug.addProperty("name","description");
			schema.add(indicationDrug);

			JsonObject refDrug = new JsonObject();
			refDrug.addProperty("name","ref");
			schema.add(refDrug);
		}

		return schema;
	}



	private JsonArray drugGENESchema(String from) {
		JsonArray schema = new JsonArray();
		if("gene".equals(from)||"protein".equals(from)){
			JsonObject drugDrug = new JsonObject();
			drugDrug.addProperty("name","drug");
			schema.add(drugDrug);

			JsonObject brand_nameDrug = new JsonObject();
			brand_nameDrug.addProperty("name","brand_name");
			schema.add(brand_nameDrug);

			JsonObject geneDrug = new JsonObject();
			geneDrug.addProperty("name","gene");
			schema.add(geneDrug);

			JsonObject variationDrug = new JsonObject();
			variationDrug.addProperty("name","variation");
			schema.add(variationDrug);


			JsonObject refDrug = new JsonObject();
			refDrug.addProperty("name","ref");
			schema.add(refDrug);
		}

		return schema;
	}

	private JsonArray drugSchema(String from) {
		JsonArray schema = new JsonArray();
		if("phenotype".equals(from)||"drug".equals(from)||"disease".equals(from)){
			JsonObject drugDrug = new JsonObject();
			drugDrug.addProperty("name","drug");
			schema.add(drugDrug);

			JsonObject brand_nameDrug = new JsonObject();
			brand_nameDrug.addProperty("name","brand_name");
			schema.add(brand_nameDrug);
		}

		if("disease_id".equals(from)){
			JsonObject disease_idDrug = new JsonObject();
			disease_idDrug.addProperty("name","disease_id");
			schema.add(disease_idDrug);
		}

		if("phenotype".equals(from)||"drug".equals(from)||"disease".equals(from)){
			JsonObject diseaseDrug = new JsonObject();
			diseaseDrug.addProperty("name","disease");
			schema.add(diseaseDrug);

			JsonObject nccn_refDrug = new JsonObject();
			nccn_refDrug.addProperty("name","nccn_ref");
			schema.add(nccn_refDrug);

			JsonObject nci_refDrug = new JsonObject();
			nci_refDrug.addProperty("name","nci_ref");
			schema.add(nci_refDrug);

			JsonObject fda_refDrug = new JsonObject();
			fda_refDrug.addProperty("name","fda_ref");
			schema.add(fda_refDrug);

			JsonObject refDrug = new JsonObject();
			refDrug.addProperty("name","ref");
			schema.add(refDrug);
		}

		return schema;
	}

	private JsonArray variationSchema(String from) {
		JsonArray schema = new JsonArray();
		JsonObject variation_numVariation = new JsonObject();
		variation_numVariation.addProperty("name","variation_num");
		schema.add(variation_numVariation);
//		if("phenotype".equals(from) || "disease".equals(from)){
		if("phenotype".equals(from)){
			JsonObject diseaseidVariation = new JsonObject();
			diseaseidVariation.addProperty("name","disease_id");
			schema.add(diseaseidVariation);

			JsonObject diseaseVariation = new JsonObject();
			diseaseVariation.addProperty("name","disease");
			schema.add(diseaseVariation);
		}

		JsonObject geneVariation = new JsonObject();
		geneVariation.addProperty("name","gene");
		schema.add(geneVariation);

		if("phenotype".equals(from) || "gene".equals(from)){
			JsonObject typeVariation = new JsonObject();
			typeVariation.addProperty("name","type");
			schema.add(typeVariation);
		}
		JsonObject chromosomeVariation = new JsonObject();
		chromosomeVariation.addProperty("name","chromosome");
		schema.add(chromosomeVariation);

		JsonObject startVariation = new JsonObject();
		startVariation.addProperty("name","start");
		schema.add(startVariation);
		JsonObject reference_alleleVariation = new JsonObject();
		reference_alleleVariation.addProperty("name","reference_allele");
		schema.add(reference_alleleVariation);

		JsonObject alternate_alleleVariation = new JsonObject();
		alternate_alleleVariation.addProperty("name","alternate_allele");
		schema.add(alternate_alleleVariation);

		JsonObject refVariation = new JsonObject();
		refVariation.addProperty("name","ref");
		schema.add(refVariation);
		return schema;
	}

	private JsonArray geneSchema(String from) {
		JsonArray schema = new JsonArray();
		JsonObject entityGene = new JsonObject();
		entityGene.addProperty("name","gene");
		schema.add(entityGene);

		if("phenotype".equals(from)){
			JsonObject entityPhenotypeID = new JsonObject();
			entityPhenotypeID.addProperty("name","phenotype_id");
			schema.add(entityPhenotypeID);

			JsonObject entityPhenotype = new JsonObject();
			entityPhenotype.addProperty("name","phenotype");
			schema.add(entityPhenotypeID);
		}

		JsonObject entityChromosome = new JsonObject();
		entityChromosome.addProperty("name","chromosome");
		schema.add(entityChromosome);

		JsonObject entityChromosomeLoc = new JsonObject();
		entityChromosomeLoc.addProperty("name","map_location");
		schema.add(entityChromosomeLoc);

		JsonObject entityTranscript = new JsonObject();
		entityTranscript.addProperty("name","transcript");
		schema.add(entityTranscript);
		if("phenotype".equals(from)||"gene".equals(from)||"drug".equals(from)||"variation".equals(from)||"disease".equals(from)){
			JsonObject entityprotein = new JsonObject();
			entityprotein.addProperty("name","protein");
			schema.add(entityprotein);
		}
		JsonObject entityRef = new JsonObject();
		entityRef.addProperty("name","ref");
		schema.add(entityRef);
		return schema;
	}

	private JsonArray proteinSchema(String from) {
		JsonArray schema = new JsonArray();

		JsonObject entityprotein = new JsonObject();
		entityprotein.addProperty("name","protein");
		schema.add(entityprotein);

		JsonObject entityGene = new JsonObject();
		entityGene.addProperty("name","gene");
		schema.add(entityGene);

		JsonObject entityRef = new JsonObject();
		entityRef.addProperty("name","ref");
		schema.add(entityRef);

		return schema;
	}
}
