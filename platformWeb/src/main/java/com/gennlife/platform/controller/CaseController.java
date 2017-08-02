package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CaseProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chen-song on 16/5/13.
 */
@Controller
@RequestMapping("/case")
public class CaseController {
    private Logger logger = LoggerFactory.getLogger(CaseController.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private CaseProcessor processor = new CaseProcessor();

    @RequestMapping(value = "/SearchItemSet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSearchItemSet(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("搜索结果列表展示的集合 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchItemSet(paramObj);
        } catch (Exception e) {
            logger.error("搜索结果列表展示的集合", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索结果列表展示的集合 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SearchItemSet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSearchItemSet(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("搜索结果列表展示的集合 get方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchItemSet(paramObj);
            logger.info("搜索结果列表展示的集合: " + resultStr);
        } catch (Exception e) {
            logger.error("搜索结果列表展示的集合", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索结果列表展示的集合 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SearchTermSuggest", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSearchTermSuggest(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("搜索关键词提示 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchTermSuggest(paramObj);
        } catch (Exception e) {
            logger.error("搜索关键词提示", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索关键词提示 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SearchTermSuggest", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSearchTermSuggest(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("搜索关键词提示 get方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchTermSuggest(paramObj);
        } catch (Exception e) {
            logger.error("搜索关键词提示", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索关键词提示 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/AdvancedSearchTermSuggest", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postAdvancedSearchTermSuggest(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("高级搜索关键词提示 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.advancedSearchTermSuggest(paramObj);
        } catch (Exception e) {
            logger.error("高级搜索关键词提示", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("高级搜索关键词提示 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/AdvancedSearchTermSuggest", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getAdvancedSearchTermSuggest(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("高级搜索关键词提示 get方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.advancedSearchTermSuggest(paramObj);
        } catch (Exception e) {
            logger.error("高级搜索关键词提示", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("高级搜索关键词提示 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SearchKnowledgeFirst", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSearchKnowledgeFirst(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("首页知识库搜索 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchKnowledgeFirst(paramObj);
        } catch (Exception e) {
            logger.error("首页知识库搜索", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("首页知识库搜索 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/SearchKnowledgeFirst", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSearchKnowledgeFirst(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("首页知识库搜索 get方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchKnowledgeFirst(paramObj);
        } catch (Exception e) {
            logger.error("首页知识库搜索", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("首页知识库搜索 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SearchCase", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSearchCase(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String paramNew = AuthorityUtil.addSearchCaseAuthority(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            logger.info("病历搜索 post方式 增加科室后参数=" + paramNew);
            resultStr = processor.searchCase(paramNew, user);
        } catch (Exception e) {
            logger.error("病历搜索", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病历搜索 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        //logger.info("病历搜索 结果=" + resultStr);
        return resultStr;
    }

    @RequestMapping(value = "/GeneVerify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postGeneVerify(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("基因数组校验 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.geneVerify(paramObj);
        } catch (Exception e) {
            logger.error("基因数组校验", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("基因数组校验 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        logger.info("基因数组校验 结果=" + resultStr);
        return resultStr;
    }

    @RequestMapping(value = "/DiseaseSearchGenes", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postDiseaseSearchGenes(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("返回该疾病相关基因 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.diseaseSearchGenes(paramObj);
        } catch (Exception e) {
            logger.error("返回该疾病相关基因", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("返回该疾病相关基因 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DiseaseSearchGenes", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getDiseaseSearchGenes(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("返回该疾病相关基因 get方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.diseaseSearchGenes(paramObj);
        } catch (Exception e) {
            logger.error("返回该疾病相关基因", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("返回该疾病相关基因 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SampleImport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSampleImport(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("样本集导出到项目空间 get方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.sampleImport(paramObj);
        } catch (Exception e) {
            logger.error("样本集导出到项目空间", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本集导出到项目空间 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SampleImport", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSampleImport(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("样本集导出到项目空间 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.sampleImport(paramObj);
        } catch (Exception e) {
            logger.error("样本集导出到项目空间", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本集导出到项目空间 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    /**
     * 我的zens
     */
    @RequestMapping(value = "/myclinicSearchCase", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String myclinicSearchCase(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String paramNew = AuthorityUtil.addSearchCaseAuthority(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            logger.info("我的诊室 病历搜索 post方式 增加科室后参数=" + paramNew);
            resultStr = processor.searchCaseByCurrentDept(paramNew, user);
        } catch (Exception e) {
            logger.error("我的诊室 病历搜索", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("我的诊室 病历搜索 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/myclinicSearchCase", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String myclinicSearchCaseTest(HttpServletRequest paramRe) {

        try {
            String param=ParamUtils.getParam(paramRe);
            int page=1;
            int size=1;
            JsonObject json=null;
            try {
               json = jsonParser.parse(param).getAsJsonObject();
            }
            catch (Exception e)
            {

            }
            if(json!=null) {
                page = json.get("page").getAsInt();
                size = json.get("size").getAsInt();
            }
            int max = 1391;
            if (Math.ceil(max * 1.0 / size) < page) {
                return "{\n" +
                        "  \"code\": 0,\n" +
                        "  \"ERRORMSG\": \"no vitark data\",\n" +
                        "  \"success\": false\n" +
                        "}";
            }
            String result = "{\n" +
                    "  \"code\": 1,\n" +
                    "  \"size\": 1,\n" +
                    "  \"data\": [\n" +
                    "    {\n" +
                    "      \"HSNS\": [\n" +
                    "        \"000899445800\",\n" +
                    "        \"000899447500\",\n" +
                    "        \"001310919400\"\n" +
                    "      ],\n" +
                    "      \"patient_info\": {\n" +
                    "        \"IDENTITY\": \"测试身份证\",\n" +
                    "        \"IDENTITY_TYPE\": \"身份证\",\n" +
                    "        \"GENDER\": \"女\",\n" +
                    "        \"PATIENT_NAME\": \"测试姓名\",\n" +
                    "        \"PATIENT_SN\": \"pat_2d86ca4bc6c14381a765f0393813c21c\"\n" +
                    "      },\n" +
                    "      \"visit_info\": {\n" +
                    "        \"LAST_VISIT_TIME\": \"2017-07-04 00:00:00\",\n" +
                    "        \"LAST_VISIT_DEPT\": \"神经内科门诊\",\n" +
                    "        \"VISIT_TIMES\": 15,\n" +
                    "        \"LAST_VISIT_TYPE\": \"0\",\n" +
                    "\t\t\"LAST_VISIT_BED\":\"测试床号\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  \n" +
                    "  ],\n" +
                    "  \"max\": 1391,\n" +
                    "  \"success\": true,\n" +
                    "  \"page\": 1\n" +
                    "}";
            JsonObject test = jsonParser.parse(result).getAsJsonObject();
            test.addProperty("page", page);
            test.addProperty("size", size);
            test.addProperty("max", max);
            return gson.toJson(test);
        } catch (Exception e) {
            return ParamUtils.errorParam(e.getMessage());
        }
    }


}
