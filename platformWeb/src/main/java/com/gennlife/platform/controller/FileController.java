package com.gennlife.platform.controller;

import com.gennlife.platform.processor.FileProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chensong on 2015/12/9.
 */
@Controller
@RequestMapping("/file")
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);
    private static FileProcessor processor = new FileProcessor();
    private static JsonParser jsonParser = new JsonParser();

    @RequestMapping(value = "/Rec", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postRec(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.fsRec(paramObj);
            logger.info("跳转 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("跳转 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Rec", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getRec(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.fsRec(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("跳转 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/SystemInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSystemInfo(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.systemInfo();
            logger.info("系统信息 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("系统信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SystemInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSystemInfo() {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.systemInfo();
            logger.info("系统信息 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("系统信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/KnowledgeReload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postKnowledgeReload(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.reloadConfig();
            logger.info("系统信息 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("系统信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/KnowledgeReload", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getKnowledgeReload() {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.reloadConfig();
            logger.info("更新配置 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("更新配置 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/Create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String createTask(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("创建导出任务 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.createTask(paramObj);
        } catch (Exception e) {
            logger.error("创建导出任务", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("创建导出任务 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/Info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskInfo(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取导出任务基本信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskInfo(paramObj);
        } catch (Exception e) {
            logger.error("获取导出任务基本信息", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取导出任务基本信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/SaveInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskSaveInfo(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("保存导出任务基本信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskSaveInfo(paramObj);
        } catch (Exception e) {
            logger.error("保存导出任务基本信息", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("保存导出任务基本信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/Start", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskStart(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("开始导出任务 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskStart(paramObj);
        } catch (Exception e) {
            logger.error("开始导出任务", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("开始导出任务 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/Cancel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskCancel(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("取消导出任务 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskCancel(paramObj);
        } catch (Exception e) {
            logger.error("取消导出任务", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("取消导出任务 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/Retry", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskRetry(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("重试导出任务 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskRetry(paramObj);
        } catch (Exception e) {
            logger.error("重试导出任务", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("重试导出任务 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/Delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskDelete(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("删除导出任务记录 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskDelete(paramObj);
        } catch (Exception e) {
            logger.error("删除导出任务记录", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除导出任务记录 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/Projects/Export/Task/Download", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskDownload(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取导出任务列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskDownload(paramObj);
        } catch (Exception e) {
            logger.error("获取导出任务列表", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取导出任务列表 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/List", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取导出任务列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskList(paramObj);
        } catch (Exception e) {
            logger.error("获取导出任务列表", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取导出任务列表 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Projects/Export/Task/CancelByProjectId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportTaskCancelByProjectId(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("取消项目相关导出任务 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportTaskCancelByProjectId(paramObj);
        } catch (Exception e) {
            logger.error("取消项目相关导出任务", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("取消项目相关导出任务 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
}
