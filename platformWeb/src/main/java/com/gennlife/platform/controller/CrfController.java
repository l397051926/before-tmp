package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.conf.SystemDefault;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CommonProcessor;
import com.gennlife.platform.processor.CrfProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by chen-song on 16/6/3.
 */
@Controller
@RequestMapping("/crf")
public class CrfController {
    private Logger logger = LoggerFactory.getLogger(CrfController.class);
    private static JsonParser jsonParser = new JsonParser();
    private static CrfProcessor processor = new CrfProcessor();
    private static Gson gson = GsonUtil.getGson();


    @RequestMapping(value = "/ModelByCRFID", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSelectAttr(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("通过CRFID获取crf模板  参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.modelByCRFID(paramObj);
        } catch (Exception e) {
            logger.error("通过CRFID获取crf模板", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("通过CRFID获取crf模板 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/GetData", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getGetData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            logger.info("数据录入时,请求某个case数据 get方式 参数=" + param);
            User user = (User) paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getData(paramObj, user.getOrgID(), user);
        } catch (Exception e) {
            logger.error("数据录入时,请求某个case数据", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("数据录入时,请求某个case数据 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    /**
     * 录入上传
     *
     * @param paramRe
     * @return
     */
    @RequestMapping(value = "/UpLoadData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postUpLoadData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            String indexName = ConfigurationService.getOrgIDIndexNamemap().get(user.getOrgID());
            if (indexName == null) {
                String needToCreateIndex = ((SystemDefault)SpringContextUtil.getBean("systemDefault")).getNeedToCreateIndex();
                if (needToCreateIndex.equals(true)) {
                    return ParamUtils.errorParam("用户所在的组织无法建立索引");
                } else {
                    // 不需要建立索引
                    logger.info("不需要建立索引");
                }
            }
            logger.info("上传crf数据 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            paramObj.addProperty("indexName", indexName);
            resultStr = processor.upLoadData(paramObj);
        } catch (Exception e) {
            logger.error("上传crf数据", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传crf数据  post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/SaveData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSaveData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            String indexName = ConfigurationService.getOrgIDIndexNamemap().get(user.getOrgID());
            String needToCreateIndex = ((SystemDefault)SpringContextUtil.getBean("systemDefault")).getNeedToCreateIndex();
            if (needToCreateIndex.equals(true) && indexName == null) {
                return ParamUtils.errorParam("用户所在的组织无法建立索引");
            }
            String param = ParamUtils.getParam(paramRe);
            logger.info("录入完成接口 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            paramObj.addProperty("indexName", indexName);
            resultStr = processor.saveData(paramObj);
        } catch (Exception e) {
            logger.error("录入完成接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("录入完成接口 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    /**
     * 删除CRF数据
     *
     * @param paramRe
     * @return
     */
    @RequestMapping(value = "/DeleteSample", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postDeleteSample(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            logger.info("删除某个case数据 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteSample(paramObj, user.getOrgID(), user);
        } catch (Exception e) {
            logger.error("删除某个case数据", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除某个case数据 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/SearchSampleList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSearchSampleList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            logger.info("搜索病历列表 get方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchSampleList(paramObj, user);
        } catch (Exception e) {
            logger.error("搜索病历列表", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索病历列表 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/AutoMap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postProjectCrfList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("将搜索到的病例导入crf接口 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.autoMap(paramObj);
        } catch (Exception e) {
            logger.error("将搜索到的病例导入crf接口异常", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("将搜索到的病例导入crf接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    /*
    *获取病人基础信息
    * @param param
    * return
     */
    @RequestMapping(value = "/PatientInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientInfo(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("获取病人基础信息 参数=" + param);
            //JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.PatientInfo(param);
        } catch (Exception e) {
            logger.error("获取病人基础信息异常", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取病人基础信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/PatientVisitDetail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientVisitDetail(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("溯源页病人详细信息 参数=" + param);
            //JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.PatientVisitDetail(param);
        } catch (Exception e) {
            logger.error("获取溯源页病人详细信息", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取溯源页病人详细信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/PatientAllVisitsDetail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientAllVisitDetail(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            logger.info("溯源全部病人详细信息 参数=" + param);
            //JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.PatientAllVisitDetail(param);
        } catch (Exception e) {
            logger.error("获取溯源全部病人详细信息", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取溯源全部病人详细信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/ModelForTraceByCRFID", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getModelForTraceByCRFID(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            logger.info("溯源简化的模型接口 参数=" + param);
            resultStr = processor.modelForTraceByCRFID(param, user.getOrgID(), user);
        } catch (Exception e) {
            logger.error("溯源简化的模型接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("溯源简化的模型接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    //UpLoadDataForCheck
    @RequestMapping(value = "/UpLoadDataForCheck", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postUpLoadDataForCheck(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            String indexName = ConfigurationService.getOrgIDIndexNamemap().get(user.getOrgID());
            if (indexName == null) {
                return ParamUtils.errorParam("用户所在的组织无法建立索引");
            }
            String param = ParamUtils.getParam(paramRe);
            logger.info("自动映射检验上传crf数据 post方式 参数=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            paramObj.addProperty("indexName", indexName);
            resultStr = processor.upLoadDataForCheck(paramObj);
        } catch (Exception e) {
            logger.error("自动映射检验上传crf数据", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("自动映射检验上传crf数据  post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/UploadFileForImportCRF", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String UploadFileForImportCRF(@RequestParam(value = "file") CommonsMultipartFile file, HttpServletRequest paramRe, @RequestParam(value = "crf_id") String crf_id) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            //String crf_id = paramObj.get("crf_id").getAsString();
            if (StringUtils.isEmpty(crf_id)) {
                return ParamUtils.errorParam("crf_id为空");
            }
            if (file.isEmpty()) {
                return ParamUtils.errorParam("文件为空");
            }
            User user = (User) paramRe.getAttribute("currentUser");
            resultStr = processor.uploadFileForImportCRF(file, crf_id, user.getOrgID(), user);
        } catch (Exception e) {
            logger.error("上传CRF数据文件", e);
            if (e instanceof IOException) {
                resultStr = ParamUtils.errorParam("出现异常 " + e.getMessage());
            } else resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传CRF数据文件  post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/ImportCRFMap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String ImportCRFMap(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            String indexName = ConfigurationService.getOrgIDIndexNamemap().get(user.getOrgID());
            if (indexName == null) {
                return ParamUtils.errorParam("用户所在的组织无法建立索引");
            }
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            paramObj.addProperty("indexName", indexName);
            resultStr = processor.importCRFMap(paramObj);
        } catch (Exception e) {
            logger.error("导入CRF导入配置", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("导入CRF导入配置  post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/CsvImportResult", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String CsvImportResult(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = processor.csvImportResult(param);
        } catch (Exception e) {
            logger.error("查询导入状态", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查询导入状态  post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/IsExistPatient", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String IsExistPatient(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = processor.isExistPatient(param);
        } catch (Exception e) {
            logger.error("病人编号是否存在", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病人编号是否存在 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/CsvImportDetail", method = RequestMethod.GET)
    public void CsvImportDetail(HttpServletRequest paramRe, HttpServletResponse response) {
        try {
            String param = ParamUtils.getParam(paramRe);
            if (StringUtils.isEmpty(param)) return;
            String resultstr = HttpRequestUtils.httpPost(ConfigurationService.getUrlBean().getCrfCsvImportDetail(), param);
            if (StringUtils.isEmpty(resultstr)) {
                logger.error("crf返回空");
                return;
            }
            JsonObject result = jsonParser.parse(resultstr).getAsJsonObject();
            CommonProcessor processor = CommonProcessor.getCommonProcessor();
            String path = ConfigurationService.getFileBean().getCRFFileLocation();
            String importId = result.get("importId").getAsString();
            String data = result.get("data").getAsString();
            String filename = "crf_csv_import_" + importId + ".csv";
            String tmpFilePath = path + filename;
            File f = new File(tmpFilePath);
            try {
                if (!f.exists()) f.createNewFile();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFilePath), "GBK"));
                bw.append(data);
                bw.flush();
                bw.close();
            } catch (Exception e) {
                logger.error("CsvImportDetail写文件异常", e);
                return;
            }
            processor.downLoadFile(tmpFilePath, response, filename);

            if (f.exists()) f.delete();
        } catch (Exception e) {
            logger.error("CsvImportDetail 出现异常", e);
        }
    }
    @RequestMapping(value = "/InputSmartPrompt", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String InputSmartPrompt(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = jsonParser.parse(param).getAsJsonObject();
            logger.info("CRF录入通过关键字获取智能提示 请求参数 = " + param);
            resultStr = processor.inputSmartPrompt(paramObj);
            logger.info("CRF录入通过关键字获取智能提示 返回结果: " + resultStr);
        } catch (Exception e) {
            logger.error("CRF录入通过关键字获取智能提示" + e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("CRF录入通过关键字获取智能提示 耗时 " + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/ICD_10_Code", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String ICD_10_Code(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = jsonParser.parse(param).getAsJsonObject();
            logger.info("获取ICD 10 编码 请求参数 = " + param);
            resultStr = processor.ICD_10_Code(paramObj);
            logger.info("获取ICD 10 编码 返回结果: " + resultStr);
        } catch (Exception e) {
            logger.error("获取ICD 10 编码: " + e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取ICD 10 编码 耗时 " + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String UploadImage(@RequestParam("file") MultipartFile[] files) {
        Long start = System.currentTimeMillis();
        String processorStr = null;
        List<String> imgUrl = new LinkedList<String>();
        List<String> failImgName = new LinkedList<String>();
        ResultBean resultBean = new ResultBean();
        logger.info("图片个数: " + Integer.toString(files.length));
        for (int i = 0; i < files.length; ++i) {
            MultipartFile file = files[i];
            if (!file.isEmpty()) {
                try {
                    logger.info("图片 " + file.getOriginalFilename() + " 上传 beginning");
                    processorStr = processor.UploadImage(file);
                    JsonObject processorStrObj = jsonParser.parse(processorStr).getAsJsonObject();
                    logger.info("上传图片FS返回：" + gson.toJson(processorStrObj));
                    if (processorStrObj.has("file_id")) {
                        imgUrl.add(processorStrObj.get("file_id").getAsString());
                    } else {
                        // 这张图片上传失败
                        failImgName.add(file.getOriginalFilename());
                    }
                } catch (Exception e) {
                    logger.error("上传图片错误" + e);
                    return ParamUtils.errorParam("出现异常");
                }
            } else {
                logger.error("file 为空");
            }
        }

        try {
            if (files.length != 0 && imgUrl.size() == 0) {
                logger.error("上传图片失败");
                resultBean.setCode(0);
                resultBean.setMsg("上传图片失败");
            } else if (imgUrl.size() < files.length) {
                resultBean.setCode(1);
                resultBean.setInfo(failImgName);
            } else {
                resultBean.setCode(1);
            }
            resultBean.setData(imgUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e);
            return ParamUtils.errorParam("出现异常");
        }
        logger.info("图片上传 耗时 " + (System.currentTimeMillis() - start) + "ms");
        return gson.toJson(resultBean);
    }
    @RequestMapping(value = "/image/{image_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getImageUrl(@PathVariable(value="image_id") String image_id) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            // 处理获取图片接口地址
            String url = ConfigurationService.getUrlBean().getImageUrl();
            resultStr = url + image_id;
        } catch (Exception e) {
            logger.error("获取图片URL地址失败" + e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取图片URL地址 耗时 " + (System.currentTimeMillis() - start) + "ms");
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(resultStr);
        return gson.toJson(resultBean);
    }
    @RequestMapping(value = "/image/{image_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String deleteImg(@PathVariable(value="image_id") String image_id) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            // 透传到FS
            logger.info("删除图片 id: " + image_id);
            resultStr = processor.deleteImg(image_id);
        } catch (Exception e) {
            logger.error("删除图片失败" + e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除图片 耗时 " + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/ResearchNumber", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String ResearchNumber(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取研究序列号 请求参数" + param);
            JsonObject paramObj = jsonParser.parse(param).getAsJsonObject();
            JsonObject LabIdToNumberObj = ConfigurationService.getLabIdToNumberObj();
            if (LabIdToNumberObj.has(paramObj.get("labID").getAsString())) {
                int number = LabIdToNumberObj.get(paramObj.get("labID").getAsString()).getAsInt();
                paramObj.addProperty("labNumber", number);
                resultStr = processor.ResearchNumber(paramObj);
            } else {
                resultStr = ParamUtils.errorParam("labID 没有对应的编号");
            }
        } catch (Exception e) {
            logger.error("获取研究序列号失败失败" + e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取研究序列号 耗时 " + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
}
