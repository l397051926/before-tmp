package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CaseProcessor;
import com.gennlife.platform.processor.IntelligentProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chen-song on 16/5/13.
 */
@ControllerAdvice
@RequestMapping("/case")
public class CaseController {
    private Logger logger = LoggerFactory.getLogger(CaseController.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    @Autowired
    private IntelligentProcessor intelligentProcessor;
    private CaseProcessor processor = new CaseProcessor();

    @RequestMapping(value = "/SearchItemSet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSearchItemSet(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = null;
            logger.info("搜索结果列表展示的集合 post方式 参数=" + param);
            try {
                paramObj = (JsonObject) jsonParser.parse(param);
            } catch (Exception e) {
                return ParamUtils.errorParam("请求参数错误");
            }
            resultStr = processor.searchItemSet(paramObj);
        } catch (Exception e) {
            logger.error("搜索结果列表展示的集合", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索结果列表展示的集合 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SearchItemSetT", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSearchItemSetTest(String arrange,String searchKey,Integer status,String keywords,String filterPath,String crfId) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonArray array =new JsonArray();
            JsonObject paramObj = new JsonObject();
            paramObj.add("arrange",array);
            paramObj.addProperty("searchKey",searchKey);
            paramObj.addProperty("status",status);
            paramObj.addProperty("keywords",keywords);
            paramObj.addProperty("filterPath",filterPath);
            paramObj.addProperty("crfId",crfId);
            logger.info("搜索结果列表展示的集合 get方式 参数=" + paramObj);
//            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchItemSet(paramObj);
            logger.info("搜索结果列表展示的集合: " + resultStr);
        } catch (Exception e) {
            logger.error("搜索结果列表展示的集合", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索结果列表展示的集合 get 耗时" + (System.currentTimeMillis() - start) + "ms");

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

    @RequestMapping(value = "/SearchTermSuggest2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSearchTermSuggest2(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("搜索关键词提示 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchTermSuggest2(paramObj);
        } catch (Exception e) {
            logger.error("搜索关键词提示", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索关键词提示 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SearchTermSuggest2", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSearchTermSuggest2(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("搜索关键词提示 get方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchTermSuggest2(paramObj);
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

    @RequestMapping(value = "/SearchCaseRole", produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String SearchCaseRole(HttpServletRequest paramRe) {
        String resultStr = null;
        try {
            String paramNew = AuthorityUtil.addSearchCaseAuthority(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            resultStr = processor.SearchCaseRole(paramNew, user);
        } catch (Exception e) {
            logger.error("病历搜索", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
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
            JsonObject demo = new JsonObject();
            demo.addProperty("msg", "ngnix 没有转发到 hospital server上");
            return gson.toJson(demo);
        } catch (Exception e) {
            return ParamUtils.errorParam(e.getMessage());
        }
    }

    @RequestMapping(value = "/highlight", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String searchHighlight(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = jsonParser.parse(param).getAsJsonObject();
            resultStr = processor.searchHighlight(paramObj);
            logger.info("搜索详情高亮 post 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索详情高亮 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    //智能提示 检查报告名称
    @RequestMapping(value = "/intelligent/inspect_report", produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String intelligentInspectReport(HttpServletRequest paramRe) {
        String param = ParamUtils.getParam(paramRe);
        JsonObject paramObj = jsonParser.parse(param).getAsJsonObject();
        return intelligentProcessor.inspectReport(paramObj);
    }

    //智能提示 根据id 获取相关信息
    @RequestMapping(value = "/intelligent/inspect_report/info", produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String intelligentInspectReportInfo(@RequestParam("id") int id) {
        return intelligentProcessor.getOneInspectReportData(id);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    String handleException(Exception exception) {
        logger.error("系统异常!", exception);
        return ParamUtils.errorParam("出现异常");
    }

    @RequestMapping(value = "/synonyms", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postOrgMapData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchSynonyms(paramObj, user);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("同义词 高级搜索 POST 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/addSynonym", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postAddSynonym(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.addSynonym(paramObj, user);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("同义词 增加 高级搜索 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/removeSynonym", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postremoveSynonym(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.removeSynonym(paramObj, user);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("同义词 删除 高级搜索 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/saveRelatedPhrasesSelectionBehavior", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String synonymUserbehavior(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveRelatedPhrasesSelectionBehavior(paramObj, user);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("同义词 保存 相关短语 高级搜索 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


}
