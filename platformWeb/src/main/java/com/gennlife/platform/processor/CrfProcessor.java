package com.gennlife.platform.processor;

import com.gennlife.platform.bean.projectBean.MyProjectList;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.Power;
import com.gennlife.platform.model.User;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.LogUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by chen-song on 16/3/18.
 */
public class CrfProcessor {
    private static Logger logger = LoggerFactory.getLogger(CrfProcessor.class);

    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();


    public String getData(JsonObject paramObj, String orgID, User user) {
        try {
            String crf_id = paramObj.get("crf_id").getAsString();
            String caseID = paramObj.has("caseID") ? paramObj.get("caseID").getAsString() : "";
            Power power = user.getPower();
            boolean flag = true;
            if ("".equals(caseID)) {
                flag = getCRFFlag(power, orgID, crf_id, "has_addCRF");
            } else {
                flag = getCRFFlag(power, orgID, crf_id, "has_editCRF");
            }
            if (flag) {
                String url = ConfigurationService.getUrlBean().getCRFGetData();
                String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
                return result;
            } else {
                return ParamUtils.errorAuthorityParam();
            }

        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }

    /**
     * 如果caseID 为null，以key作为权限控制判断依据
     * 如果caseID 为"",判断录入的
     * 如果caseID 不为null,不为""，判断编辑
     *
     * @param orgID
     * @param crf_id
     * @return
     */
    public static boolean getCRFFlag(Power power, String orgID, String crf_id, String key) {
        boolean flag = false;
        Set<String> validLabID = new HashSet<>();
        JsonObject powerObj = (JsonObject) jsonParser.parse(gson.toJson(power));
        JsonArray array = powerObj.getAsJsonArray(key);
        for (JsonElement item : array) {
            JsonObject itemObj = item.getAsJsonObject();
            String sid = itemObj.get("sid").getAsString();
            validLabID.add(sid);
        }
        for (String labID : validLabID) {
            int count = AllDao.getInstance().getSyResourceDao().isExistsCrfID(labID, orgID, crf_id);
            if (count > 0) {
                flag = true;
                break;
            }
        }
        return flag;
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


    public String deleteSample(JsonObject paramObj, String orgID, User user) {
        try {
            Power power = user.getPower();
            String crf_id = paramObj.get("crf_id").getAsString();
            boolean flag = getCRFFlag(power, orgID, crf_id, "has_deleteCRF");
            if (flag) {
                String url = ConfigurationService.getUrlBean().getCRFDeleteSample();
                String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
                return result;
            } else {
                return ParamUtils.errorAuthorityParam();
            }

        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String searchSampleList(JsonObject paramObj, User user) {
        try {
            String crf_id = paramObj.get("crf_id").getAsString();
            Power power = user.getPower();
            boolean flag1 = getCRFFlag(power, user.getOrgID(), crf_id, "has_deleteCRF");
            boolean flag2 = getCRFFlag(power, user.getOrgID(), crf_id, "has_traceCRF");
            boolean flag3 = getCRFFlag(power, user.getOrgID(), crf_id, "has_addCRF");
            boolean flag4 = getCRFFlag(power, user.getOrgID(), crf_id, "has_editCRF");
            boolean flag5 = getCRFFlag(power, user.getOrgID(), crf_id, "has_addBatchCRF");
            boolean flag = flag1 || flag2 || flag3 || flag4 || flag5;
            if (flag) {
                String url = ConfigurationService.getUrlBean().getCRFSearchSampleList();
                String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
                return result;
            } else {
                return ParamUtils.errorAuthorityParam();
            }

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


    public String modelForTraceByCRFID(String param, String orgID, User user) {
        try {
            Power power = user.getPower();
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            String crf_id = paramObj.get("crf_id").getAsString();
            boolean flag = getCRFFlag(power, orgID, crf_id, "has_traceCRF");
            if (flag) {//有权限请求
                String url = ConfigurationService.getUrlBean().getCRFModelForTraceByCRFID();
                logger.info("request url:" + url);
                String result = HttpRequestUtils.httpPost(url, param);
                return result;
            } else {
                return ParamUtils.errorAuthorityParam();
            }

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


    public String uploadFileForImportCRF(MultipartFile file, String crf_id, String orgID, User user) {
        Power power = user.getPower();
        logger.info("crf_id:" + crf_id);
        boolean flag = getCRFFlag(power, orgID, crf_id, "has_addBatchCRF");
        if (flag) {
            String url = ConfigurationService.getUrlBean().getFileStoreForCRFImport();
            if (!file.isEmpty()) {
                try {
                    String fileName = file.getOriginalFilename();
                    byte[] bytes = file.getBytes();
                    String path = ConfigurationService.getFileBean().getCRFFileLocation();
                    File f = new File(path + LogUtils.getString_Time() + "-" + fileName);
                    if (!f.exists()) {
                        logger.info("文件路径 " + f.getAbsolutePath());
                        f.createNewFile();

                    }
                    FileWriter fileWritter = new FileWriter(f);
                    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                    bufferWritter.write(new String(bytes, "gbk"));
                    bufferWritter.flush();
                    bufferWritter.close();
                    fileWritter.close();
                    String str = HttpRequestUtils.httpPost(url, f, fileName);
                    if (StringUtils.isEmpty(str)) {
                        return ParamUtils.errorParam("导入文件失败");
                    }
                    JsonObject filebackObj = (JsonObject) jsonParser.parse(str);
                    f.delete();
                    if (filebackObj.get("success").getAsBoolean()) {
                        String file_id = filebackObj.get("file_id").getAsString();
                        String schema = filebackObj.get("schema").getAsString();
                        JsonObject paramObj = new JsonObject();
                        paramObj.addProperty("schema", schema);
                        paramObj.addProperty("fid", file_id);
                        paramObj.addProperty("crf_id", crf_id);
                        String param = gson.toJson(paramObj);
                        url = ConfigurationService.getUrlBean().getCRFImportFile();
                        String reStr = HttpRequestUtils.httpPost(url, param);
                        if (reStr == null || "".equals(reStr)) {
                            return ParamUtils.errorParam("CRF 服务异常");
                        } else {
                            JsonObject object = (JsonObject) jsonParser.parse(reStr);
                            if (object.has("code") && object.get("code").getAsInt() == 1) {
                                JsonArray csv_schema = object.getAsJsonArray("csv_schema");
                                JsonObject crf_schema = object.getAsJsonObject("crf_schema");
                                JsonObject result = new JsonObject();
                                result.addProperty("code", 1);
                                JsonObject data = new JsonObject();
                                data.add("csv_schema", csv_schema);
                                data.add("crf_schema", crf_schema);
                                data.addProperty("fid", file_id);
                                result.add("data", data);
                                return gson.toJson(result);
                            } else {
                                return ParamUtils.errorParam("crf 返回为空");
                            }
                        }
                    } else {
                        return ParamUtils.errorParam("文件存储失败");
                    }
                } catch (Exception e) {
                    logger.error("", e);
                    return ParamUtils.errorParam("出现异常");
                }
            } else {
                return ParamUtils.errorParam("文件为空");
            }
        } else {
            return ParamUtils.errorAuthorityParam();
        }


    }

    public String importCRFMap(JsonObject param) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFImportMap();
            logger.info("CRFImportMap url=" + url + " param " + param);
            String result = HttpRequestUtils.httpPost(url, gson.toJson(param));
            logger.info("CRFImportMap result=" + result);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String csvImportResult(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFImportResult();
            logger.info("CRFImportResult url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            logger.info("CRFImportResult result=" + result);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String isExistPatient(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFIsExistPatient();
            logger.info("isExistPatient url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            logger.info("isExistPatient result=" + result);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String inputSmartPrompt(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getInputSmartPrompt();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String UploadImage(MultipartFile file) {
        try {
            String url = ConfigurationService.getUrlBean().getImageUpload(); // fs图片上传后台接口
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            String path = ConfigurationService.getFileBean().getCRFFileLocation();
            File f = new File(path + LogUtils.getString_Time() + "-" + fileName);
            if (!f.exists()) {
                logger.info("文件路径 " + f.getAbsolutePath());
                f.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(f);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(new String(bytes, "gbk"));
            bufferedWriter.flush();
            bufferedWriter.close();
            // 将文件传给fs
            String resultStr = HttpRequestUtils.httpPostImg(url, f);
            f.delete();
            if (StringUtils.isEmpty(resultStr)) {
                return ParamUtils.errorParam("上传图片失败");
            } else {
                return resultStr;
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    public String deleteImg(String image_id) {
        try {
            String url = ConfigurationService.getUrlBean().getImageDel() + image_id;
            String result = HttpRequestUtils.httpGet(url);
            return result;
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
    }
}
