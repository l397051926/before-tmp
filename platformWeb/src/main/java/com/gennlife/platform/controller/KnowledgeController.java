package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.processor.KnowledgeProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by chen-song on 16/5/6.
 */
@Controller
@RequestMapping("/knowledge")
public class KnowledgeController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(KnowledgeController.class);
    private static KnowledgeProcessor processor = new KnowledgeProcessor();
    private static JsonParser jsonParser = new JsonParser();


    @RequestMapping(value = "/Search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSearch(@RequestBody String param) {
        logger.info("知识库搜索 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.search(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("知识库搜索 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/Search", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSearch(@RequestParam("param") String param) {
        logger.info("知识库搜索 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.search(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("知识库搜索 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DetailSearch", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getDetailSearch(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            logger.info("详情页知识库搜索 param=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.detailSearch(paramObj);
        } catch (Exception e) {
            logger.error("详情页知识库搜索", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页知识库搜索 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DetailSearch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postDetailSearch(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            logger.info("详情页知识库搜索 param=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.detailSearch(paramObj);
        } catch (Exception e) {
            logger.error("详情页知识库搜索", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页知识库搜索 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/GeneInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postGeneInfo(@RequestBody String param) {
        logger.info("搜索首页基因信息查询 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.geneInfo(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索首页基因信息查询 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/GeneInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getGeneInfo(@RequestParam("param") String param) {
        logger.info("搜索首页基因信息查询 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.geneInfo(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索首页基因信息查询 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VariationInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postVariationInfo(@RequestBody String param) {
        logger.info("搜索首页变异信息查询 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.variationInfo(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索首页变异信息查询 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/VariationInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getVariationInfo(@RequestParam("param") String param) {
        logger.info("搜索首页变异信息查询 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.variationInfo(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索首页变异信息查询 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DetailVariationSearchDisease", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postDetailVariationSearchDisease(@RequestBody String param) {
        logger.info("详情页,变异信息查询疾病 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.detailVariationSearchDisease(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页,变异信息查询疾病 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DetailVariationSearchDisease", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getDetailVariationSearchDisease(@RequestParam("param") String param) {
        logger.info("详情页,变异信息查询疾病 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.detailVariationSearchDisease(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页,变异信息查询疾病 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DetailVariationSearchDrug", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postDetailVariationSearchDrug(@RequestBody String param) {
        logger.info("详情页,变异信息查询药物 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.detailVariationSearchDrug(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页,变异信息查询药物 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/DetailVariationSearchDrug", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getDetailVariationSearchDrug(@RequestParam("param") String param) {
        logger.info("详情页,变异信息查询药物 param=" + param);
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.detailVariationSearchDrug(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页,变异信息查询药物 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

}
