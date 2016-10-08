package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.projectBean.MyProjectList;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.LogUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen-song on 16/3/18.
 */
public class CrfProcessor {
    private static Logger logger = LoggerFactory.getLogger(CrfProcessor.class);

    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    /**
     * 保存定义好的数据模型
     *
     * @param paramObj
     */
    public String editModel(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFEditModel();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }

    /**
     * 将模型状态从创建状态,改成保存完成
     *
     * @param paramObj
     */
    public String saveModel(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFSaveModel();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getData(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFGetData();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }

    public String upLoadData(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFUpLoadData();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }


    /**
     * crf数据录入完成后,点击录入完成出发的接口,请求参数crf_id,caseID,返回crf_id,新的caseID,空的数据格式
     *
     * @param paramObj
     */
    public String saveData(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFSaveData();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }


    /**
     * 返回病历列表数据
     *
     * @param paramObj
     */
    public String sampleCaseList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFSampleCaseList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String deleteSample(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFDeleteSample();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String searchSampleList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFSearchSampleList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }


    /**
     * 用户相关项目的crf模版列表
     *
     * @param paramObj
     * @return
     */
    public String projectCrfList(JsonObject paramObj) {
        String uid = null;
        try {
            uid = paramObj.get("uid").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        Map<String, Object> conf = new HashMap<>();
        conf.put("uid", uid);
        try {
            List<MyProjectList> list = AllDao.getInstance().getProjectDao().getProjectList(conf);
            List<JsonObject> paramList = new LinkedList<>();
            for (MyProjectList myProjectList : list) {
                JsonObject newParamObj = new JsonObject();
                String projectID = myProjectList.getProjectID();
                String projectName = myProjectList.getProjectName();
                newParamObj.addProperty("projectID", projectID);
                newParamObj.addProperty("projectName", projectName);
                paramList.add(newParamObj);
            }
            return gson.toJson(paramList);
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    /**
     * 将搜索到的病例导入crf接口,透传
     *
     * @param paramObj
     * @return
     */
    public String autoMap(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFAutoMapURL();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }

    /**
     * 获取一个新的唯一的属性id
     *
     * @param paramObj
     * @return
     */
    public String getAttrID(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFGetAttrID();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }


    /**
     * 获取一个新的唯一的组id
     *
     * @param paramObj
     * @return
     */
    public String getGroupID(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFGetGroupID();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }

    /**
     * 返回模板信息
     *
     * @param paramObj
     * @return
     */
    public String modelByProjectID(JsonObject paramObj) {
        String projectID = null;
        String uid = null;
        try {
            projectID = paramObj.get("projectID").getAsString();
            uid = paramObj.get("uid").getAsString();
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("参数错误");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("projectID", projectID);
        MyProjectList project = AllDao.getInstance().getProjectDao().baiscInfo(map);
        //baiscInfo(map);
        JsonObject newParam = new JsonObject();
        newParam.addProperty("projectID", project.getProjectID());
        newParam.addProperty("disease", project.getDisease());
        newParam.addProperty("projectName", project.getProjectName());
        newParam.addProperty("uid", uid);
        try {
            String url = ConfigurationService.getUrlBean().getCRFModelByProjectID();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(newParam));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    /**
     * 返回模板信息
     *
     * @param paramObj
     * @return
     */
    public String modelByCRFID(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFModelByCRFID();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }
    /*
    * 获取病人信息
     * @param paramObj
     * @return
     */

    public String PatientInfo(String paramObj) {
        try {

            String url = ConfigurationService.getUrlBean().getCRFGetPatientInfo() + "?param=";
            String param = URLEncoder.encode(paramObj, "utf-8");
            url = url + param;
            logger.info("request url:" + url);
            String result = HttpRequestUtils.httpGet(url);
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    /*
   * 溯源页病人详细信息
    * @param paramObj
    * @return
    */
    public String PatientVisitDetail(String paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFPatientVisitDetail() + "?param=";
            String param = URLEncoder.encode(paramObj, "utf-8");
            url = url + param;
            logger.info("request url:" + url);
            String result = HttpRequestUtils.httpGet(url);
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    /*
   * 溯源全部病人详细信息
   * @param paramObj
   * @return
   */
    public String PatientAllVisitDetail(String paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFPatientAllVisitsDetail() + "?param=";
            String param = URLEncoder.encode(paramObj, "utf-8");
            url = url + param;
            logger.info("request url:" + url);
            String result = HttpRequestUtils.httpGet(url);
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String modelForTraceByCRFID(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFModelForTraceByCRFID();
            logger.info("request url:" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String upLoadDataForCheck(JsonObject paramObj) {
        try {
            String param = gson.toJson(paramObj);
            String url = ConfigurationService.getUrlBean().getCRFUpLoadDataForCheck();
            logger.info("request url:" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }


    public String uploadFileForImportCRF(MultipartFile file, String crf_id) {
        logger.info("crf_id:"+crf_id);
        String url = ConfigurationService.getUrlBean().getFileStoreForCRFImport();

        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                String path = ConfigurationService.getFileBean().getCRFFileLocation();
                File f = new File(path + LogUtils.getString_Time()+"-"+fileName);
                if(!f.exists()){
                    f.createNewFile();
                }

                FileWriter fileWritter = new FileWriter(f);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write(new String(bytes,"gbk"));
                bufferWritter.flush();
                bufferWritter.close();
                fileWritter.close();
                String str =  HttpRequestUtils.httpPost(url,f,fileName);
                if(str == null){
                    return ParamUtils.errorParam("导入文件失败");
                }
                JsonObject filebackObj = (JsonObject) jsonParser.parse(str);
                f.delete();
                if(filebackObj.get("success").getAsBoolean()){
                    String file_id = filebackObj.get("file_id").getAsString();
                    String schema = filebackObj.get("schema").getAsString();
                    JsonObject paramObj = new JsonObject();
                    paramObj.addProperty("schema",schema);
                    paramObj.addProperty("fid",file_id);
                    paramObj.addProperty("crf_id",crf_id);
                    String param = gson.toJson(paramObj);
                    url = ConfigurationService.getUrlBean().getCRFImportFile();
                    String reStr =  HttpRequestUtils.httpPost(url,param);
                    if(reStr == null || "".equals(reStr)){
                        return ParamUtils.errorParam("CRF 服务异常");
                    }else {
                        JsonObject object = (JsonObject) jsonParser.parse(reStr);
                        if(object.has("code") && object.get("code").getAsInt() == 1){
                            JsonArray csv_schema = object.getAsJsonArray("csv_schema");
                            JsonObject crf_schema = object.getAsJsonObject("crf_schema");
                            JsonObject result = new JsonObject();
                            result.addProperty("code",1);
                            JsonObject data = new JsonObject();
                            data.add("csv_schema",csv_schema);
                            data.add("crf_schema",crf_schema);
                            data.addProperty("fid",file_id);
                            result.add("data",data);
                            return gson.toJson(result);
                        }else {
                            return ParamUtils.errorParam("crf 返回为空");
                        }
                    }
                }else{
                    return ParamUtils.errorParam("文件存储失败");
                }
            } catch (Exception e) {
                logger.error("",e);
                return ParamUtils.errorParam("出现异常");
            }
        } else {
            return ParamUtils.errorParam("文件为空");
        }

    }

    public String importCRFMap(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCRFImportMap();
            logger.info("CRFImportMap url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("CRFImportMap result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }
}
