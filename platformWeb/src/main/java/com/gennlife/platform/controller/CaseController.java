package com.gennlife.platform.controller;

import com.gennlife.platform.processor.CaseProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chen-song on 16/5/13.
 */
@Controller
//@RequestMapping("/case")
public class CaseController {
    private Logger logger = LoggerFactory.getLogger(CaseController.class);
    private static JsonParser jsonParser = new JsonParser();
    private CaseProcessor processor = new CaseProcessor();
    @RequestMapping(value="/SearchItemSet",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSearchItemSet(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("搜索结果列表展示的集合 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchItemSet(paramObj);
        }catch (Exception e){
            logger.error("搜索结果列表展示的集合",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索结果列表展示的集合 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/SearchItemSet",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getSearchItemSet(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("搜索结果列表展示的集合 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchItemSet(paramObj);
        }catch (Exception e){
            logger.error("搜索结果列表展示的集合",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索结果列表展示的集合 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SearchTermSuggest",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSearchTermSuggest(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("搜索关键词提示 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchTermSuggest(paramObj);
        }catch (Exception e){
            logger.error("搜索关键词提示",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索关键词提示 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SearchTermSuggest",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getSearchTermSuggest(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("搜索关键词提示 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchTermSuggest(paramObj);
        }catch (Exception e){
            logger.error("搜索关键词提示",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索关键词提示 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AdvancedSearchTermSuggest",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postAdvancedSearchTermSuggest(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("高级搜索关键词提示 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.advancedSearchTermSuggest(paramObj);
        }catch (Exception e){
            logger.error("高级搜索关键词提示",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("高级搜索关键词提示 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AdvancedSearchTermSuggest",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getAdvancedSearchTermSuggest(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("高级搜索关键词提示 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.advancedSearchTermSuggest(paramObj);
        }catch (Exception e){
            logger.error("高级搜索关键词提示",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("高级搜索关键词提示 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/SearchKnowledgeFirst",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSearchKnowledgeFirst(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("首页知识库搜索 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchKnowledgeFirst(paramObj);
        }catch (Exception e){
            logger.error("首页知识库搜索",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("首页知识库搜索 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/SearchKnowledgeFirst",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getSearchKnowledgeFirst(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("首页知识库搜索 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchKnowledgeFirst(paramObj);
        }catch (Exception e){
            logger.error("首页知识库搜索",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("首页知识库搜索 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/case/SearchCase",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSearchCase(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("病历搜索 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchCase(paramObj);
        }catch (Exception e){
            logger.error("病历搜索",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病历搜索 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/DiseaseSearchGenes",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postDiseaseSearchGenes(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("返回该疾病相关基因 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.diseaseSearchGenes(paramObj);
        }catch (Exception e){
            logger.error("返回该疾病相关基因",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("返回该疾病相关基因 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/DiseaseSearchGenes",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getDiseaseSearchGenes(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("返回该疾病相关基因 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.diseaseSearchGenes(paramObj);
        }catch (Exception e){
            logger.error("返回该疾病相关基因",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("返回该疾病相关基因 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/SampleImport",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSampleImport(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("样本集导出到项目空间 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.sampleImport(paramObj);
        }catch (Exception e){
            logger.error("样本集导出到项目空间",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本集导出到项目空间 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SampleImport",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getSampleImport(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("样本集导出到项目空间 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.sampleImport(paramObj);
        }catch (Exception e){
            logger.error("样本集导出到项目空间",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本集导出到项目空间 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

}
