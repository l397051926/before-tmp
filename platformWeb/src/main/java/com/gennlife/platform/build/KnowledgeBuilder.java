package com.gennlife.platform.build;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen-song on 16/5/6.
 */
public class KnowledgeBuilder {
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
        }
        return result;
    }
    //分表
    private JsonArray buildDrug(JsonObject param, JsonObject obj) {
        JsonArray result = new JsonArray();
        JsonObject head = buildHead(param,obj);
        return result;
    }

    /**
     * 变异号：mutation_num |数组 url（all）
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
        for(JsonElement data:dataArray){
            JsonObject bodyEntity = new JsonObject();
            body.add(bodyEntity);
            JsonObject dataObj = data.getAsJsonObject();
            JsonArray idArray = new JsonArray();
            JsonObject idObj = new JsonObject();
            idObj.addProperty("name",dataObj.get("id").getAsString());
            idObj.addProperty("url",dataObj.get("idUrl").getAsString());
            idArray.add(idObj);
            bodyEntity.add("mutation_num",idArray);
            if("phenotype".equals(from) || "disease".equals(from)){
                //补
                JsonArray disease_idArray = new JsonArray();
                JsonObject disease_idObj = new JsonObject();
                disease_idObj.addProperty("name","补");
                disease_idObj.addProperty("url","");
                disease_idArray.add(disease_idObj);
                bodyEntity.add("disease_id",disease_idArray);

                bodyEntity.addProperty("disease","补");
            }
            JsonArray geneArray = new JsonArray();
            JsonObject geneObj = new JsonObject();
            geneObj.addProperty("name",dataObj.get("geneSymbol").getAsString());
            geneObj.addProperty("url",dataObj.get("geneUrl").getAsString());
            geneArray.add(geneObj);
            bodyEntity.add("gene",geneArray);

            if("phenotype".equals(from) || "gene".equals(from)){
                bodyEntity.addProperty("type","补");
            }

            bodyEntity.addProperty("chromosome",dataObj.get("chromosome").getAsString());

            bodyEntity.addProperty("start",dataObj.get("start").getAsString());

            bodyEntity.addProperty("reference_allele",dataObj.get("ref").getAsString());

            bodyEntity.addProperty("alternate_allele",dataObj.get("alt").getAsString());

            JsonArray refArray = new JsonArray();
            JsonObject refObj = new JsonObject();
            refObj.addProperty("name",dataObj.get("geneSymbol").getAsString());
            refObj.addProperty("url",dataObj.get("geneUrl").getAsString());
            refArray.add(refObj);
            bodyEntity.add("ref",refArray);

        }
        result.add(body);
        return result;

    }

    private JsonArray buildGene(JsonObject param, JsonObject obj) {
        JsonArray result = new JsonArray();
        JsonObject head = buildHead(param,obj);
        result.add(head);
        JsonArray body = new JsonArray();
        JsonArray dataArray = obj.getAsJsonArray("data");
        for(JsonElement data:dataArray){
            JsonObject bodyEntity = new JsonObject();
            body.add(bodyEntity);
            JsonObject dataObj = data.getAsJsonObject();
            JsonArray geneArray = new JsonArray();
            JsonObject geneObj = new JsonObject();
            geneArray.add(geneObj);
            bodyEntity.add("gene",geneArray);
            geneObj.addProperty("name",dataObj.get("geneSymbol").getAsString());
            geneObj.addProperty("url",dataObj.get("geneSymbolUrl").getAsString());
            bodyEntity.addProperty("chromosome",dataObj.get("chromosome").getAsString());
            bodyEntity.addProperty("map_location",dataObj.get("mapLocation").getAsString());
            JsonArray RNAArray = new JsonArray();
            JsonArray rnas = dataObj.get("RNA").getAsJsonArray();
            JsonArray rnaArray = dataObj.get("RNAUrlArray").getAsJsonArray();
            Map<String,String> map = new HashMap<String, String>();
            for(JsonElement rna:rnas){
                String rnaStr = rna.getAsString();
                for(JsonElement r:rnaArray){
                    String rs = r.getAsString();
                    if(rs.endsWith(rnaStr)){
                        map.put(rnaStr,rs);
                    }
                }
            }
            for(String rna:map.keySet()){
                JsonObject rnaObj = new JsonObject();
                rnaObj.addProperty("name",rna);
                rnaObj.addProperty("url",map.get(rna));
                RNAArray.add(rnaObj);
            }
            bodyEntity.add("transcript",RNAArray);
            JsonArray refArray = new JsonArray();
            JsonObject ref = new JsonObject();
            ref.addProperty("name",dataObj.get("source").getAsString());
            ref.addProperty("url","");
            refArray.add(ref);
            bodyEntity.add("ref",refArray);
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
        for(JsonElement data:dataArray){
            JsonObject bodyEntity = new JsonObject();
            body.add(bodyEntity);
            JsonObject dataObj = data.getAsJsonObject();
            JsonArray proteinArray = new JsonArray();
            JsonObject proteinObj = new JsonObject();
            proteinArray.add(proteinObj);
            bodyEntity.add("protein",proteinArray);
            proteinObj.addProperty("name",dataObj.get("proteinName").getAsString());
            proteinObj.addProperty("url",dataObj.get("url").getAsString());
            JsonArray geneArray = new JsonArray();
            JsonArray gene = dataObj.getAsJsonArray("Gene");
            for(JsonElement json:gene){
                JsonObject g = json.getAsJsonObject();
                String symbol = g.get("symbol").getAsString();
                String url = g.get("url").getAsString();
                JsonObject gen = new JsonObject();
                gen.addProperty("name",symbol);
                gen.addProperty("url",url);
                geneArray.add(gen);
            }
            bodyEntity.add("gene",geneArray);
            JsonArray refArray = new JsonArray();
            JsonObject ref = new JsonObject();
            ref.addProperty("name",dataObj.get("source").getAsString());
            ref.addProperty("url","");
            refArray.add(ref);
            bodyEntity.add("ref",refArray);
        }
        result.add(body);
        return result;
    }

    private JsonArray buildDisease(JsonObject param, JsonObject obj) {
        JsonArray result = new JsonArray();
        JsonObject head = buildHead(param,obj);
        result.add(head);
        JsonArray body = new JsonArray();
        JsonArray dataArray = obj.getAsJsonArray("data");
        for(JsonElement data:dataArray){
            JsonObject bodyEntity = new JsonObject();
            body.add(bodyEntity);
            JsonObject dataObj = data.getAsJsonObject();
            JsonObject dObj = new JsonObject();
            JsonArray disease_id = new JsonArray();
            dObj.addProperty("name",dataObj.get("id").getAsString());
            dObj.addProperty("url",dataObj.get("url").getAsString());
            disease_id.add(dObj);
            bodyEntity.add("disease_id",disease_id);
            JsonObject diseaseObj = new JsonObject();
            JsonArray disease = new JsonArray();
            diseaseObj.addProperty("name",dataObj.get("name").getAsString());
            diseaseObj.addProperty("url","");
            disease.add(diseaseObj);
            bodyEntity.add("disease",disease);
            String icd_10 = "";
            if(dataObj.get("icd_10") != null){
                icd_10 = dataObj.get("icd_10").getAsString();
            }
            bodyEntity.addProperty("icd_10",icd_10);
            JsonArray refArray = new JsonArray();
            JsonObject ref = new JsonObject();
            ref.addProperty("name",dataObj.get("source").getAsString());
            ref.addProperty("url","");
            refArray.add(ref);
            bodyEntity.add("ref",refArray);
        }
        result.add(body);
        return result;
    }

    private JsonArray diseaseSchema(String from){
        JsonArray schema = new JsonArray();
        JsonObject entityid = new JsonObject();
        entityid.addProperty("name","disease_id");
        schema.add(entityid);
        JsonObject entityDisease = new JsonObject();
        entityDisease.addProperty("name","disease");
        schema.add(entityDisease);
        JsonObject entityICD10 = new JsonObject();
        entityICD10.addProperty("name","icd_10");
        schema.add(entityICD10);
        JsonObject entityRef = new JsonObject();
        entityRef.addProperty("name","ref");
        schema.add(entityRef);
        return schema;
    }

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
        head.addProperty("currentTable","");
        JsonArray schema = null;
        if("disease".equals(to)){
            schema = diseaseSchema(from);
        }else if ("protein".equals(to)){
            schema = proteinSchema(from);
        }else if ("gene".equals(to)){
            schema = geneSchema(from);
        }else if("variation".equals(to)){
            schema = variationSchema(from);
        }
        head.add("schema",schema);
        head.add("dimension",obj.getAsJsonArray("dimension"));
        return head;
    }

    private JsonArray variationSchema(String from) {
        JsonArray schema = new JsonArray();
        JsonObject mutation_numVariation = new JsonObject();
        mutation_numVariation.addProperty("name","mutation_num");
        schema.add(mutation_numVariation);
        if("phenotype".equals(from) || "disease".equals(from)){
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
        if("drug".equals(from)){
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
