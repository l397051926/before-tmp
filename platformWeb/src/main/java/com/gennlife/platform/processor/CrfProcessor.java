package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.conf.SystemDefault;
import com.gennlife.platform.bean.projectBean.MyProjectList;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.CRFLab;
import com.gennlife.platform.model.Power;
import com.gennlife.platform.model.Resource;
import com.gennlife.platform.model.User;
import com.gennlife.platform.parse.CaseSearchParser;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.google.gson.*;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
            boolean flag = false;
            boolean traceflag = getCRFFlag(power, orgID, crf_id, "has_traceCRF");
            if ("".equals(caseID)) {
                flag = getCRFFlag(power, orgID, crf_id, "has_addCRF");
            } else {
                flag = getCRFFlag(power, orgID, crf_id, "has_editCRF");
            }
            if (flag || traceflag) {
                String url = ConfigurationService.getUrlBean().getCRFGetData();
                String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
                try {
                    JsonObject json = jsonParser.parse(result).getAsJsonObject();
                    if (json != null && json.has("status") && json.get("status").getAsString().contains("验证")) {
                        if (!traceflag) return ParamUtils.errorAuthorityParam();// 没有溯源权限
                        //logger.info("溯源权限");
                    } else {
                        if (!flag) return ParamUtils.errorAuthorityParam();
                        //logger.info("添加和编辑");
                    }
                } catch (Exception e) {
                }

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
//            String url = "http://127.0.0.1:8090/crf/UpLoadData";
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
            boolean flag6 = getCRFFlag(power, user.getOrgID(), crf_id, "has_searchCRF");
            boolean flag7 = getCRFFlag(power, user.getOrgID(), crf_id, "has_importCRF");
            boolean flag = flag1 || flag2 || flag3 || flag4 || flag5 || flag6 || flag7;
            if (flag) {
                String url = ConfigurationService.getUrlBean().getCRFSearchSampleList();
                //10.0.0.152:9885/crf/SearchSampleList
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
        String keyWords = null;
        try {
            JsonObject paramObject = (JsonObject) jsonParser.parse(paramObj);
            if(paramObject.has("keyWords")){
                keyWords = paramObject.get("key").getAsString();
            }
            String url = ConfigurationService.getUrlBean().getCRFGetPatientInfo() + "?param=";
            String param = URLEncoder.encode(paramObj, "utf-8");
            url = url + param;
            logger.info("request url:" + url);
            String result = HttpRequestUtils.httpGet(url);
            result = result.replaceAll(keyWords,"<span style='color:red'>" + keyWords + "</span>");
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
        String keyWords = null;
        try {
            JsonObject paramObject = (JsonObject) jsonParser.parse(paramObj);
            if(paramObject.has("keyWords")){
                keyWords = paramObject.get("key").getAsString();
            }
            String url = ConfigurationService.getUrlBean().getCRFPatientVisitDetail() + "?param=";
            String param = URLEncoder.encode(paramObj, "utf-8");
            url = url + param;
            logger.info("request url:" + url);
            String result = HttpRequestUtils.httpGet(url);
            result = result.replaceAll(keyWords,"<span style='color:red'>" + keyWords + "</span>");

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
                    File f = new File(path + UUID.randomUUID().toString().replace("-", "") + "-" + fileName);
                    if (!f.exists()) {
                        // logger.info("文件路径 " + f.getAbsolutePath());
                        f.createNewFile();
                    } else {
                        logger.warn("文件路径 " + f.getAbsolutePath() + " 不应该存在");
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
            String result = HttpRequestUtils.httpPost(url, gson.toJson(param));
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String csvImportResult(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFImportResult();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String isExistPatient(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCRFIsExistPatient();
            String result = HttpRequestUtils.httpPost(url, param);
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

    public String ICD_10_Code(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getICD_10_CodeUrl();
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
            // String path = "/Users/luoxupan/demoTest/";
            File f = new File(path + LogUtils.getString_Time() + UUID.randomUUID() + "-" + fileName);
            if (!f.exists()) {
                logger.info("文件路径 " + f.getAbsolutePath());
                f.createNewFile();
            }
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
            out.write(bytes);
            out.flush();
            out.close();
            // 将文件传给fs
            String resultStr = HttpRequestUtils.httpPostImg(url, f, file.getContentType());
            f.delete();
            logger.info("上传图片HTTP返回：" + resultStr);
            return resultStr;
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    public String deleteImg(String image_id) {
        try {
            String url = ConfigurationService.getUrlBean().getImageDel() + image_id;
            String result = HttpRequestUtils.httpDelte(url);
            return result;
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    public String ResearchNumber(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getResearchNumberUrl();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    public String searchCase(JsonObject newParam, User user) {
        String crf_id = null;
        ResultBean resultBean = new ResultBean();
        try{
            crf_id = newParam.get("crf_id").getAsString();
            Power power = user.getPower();
            boolean flag = getCRFFlag(power, user.getOrgID(), crf_id, "has_searchCRF");
            if(flag){
                String url = ConfigurationService.getUrlBean().getCRFSearchSampleList();
                logger.info("请求参数： "+newParam);
                String result = HttpRequestUtils.httpPost(url, gson.toJson(newParam));
                resultBean.setCode(1);
                resultBean.setData(result);
            }else {
                return ParamUtils.errorParam("没有搜索权限");
            }

            return gson.toJson(resultBean);

        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("出现异常");
        }

    }

       public String searchItemSet(JsonObject paramObj) {
        String param = null;
        String searchKey = null;
        String keywords = null;
        String status = null;
        String crf_id = ((SystemDefault) SpringContextUtil.getBean("systemDefault")).getSearchItemSetDefault();
        Set<String> set = new HashSet<String>();
        ResultBean resultBean = new ResultBean();
        try {

            //searchKey = paramObj.get("searchKey").getAsString();//病历搜索的关键词
            keywords = paramObj.get("keywords").getAsString();//属性搜索的关键词
//            status = paramObj.get("status").getAsString();
            JsonArray arrange = paramObj.get("arrange").getAsJsonArray();
            for (JsonElement json : arrange) {
                set.add(json.getAsString());
            }
            if (paramObj.has("crf_id")) {
                crf_id = paramObj.get("crf_id").getAsString();
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        JsonObject all = ConfigurationService.getCrfSearch(crf_id);
        JsonObject allNew = new JsonObject();
        for (Map.Entry<String, JsonElement> obj : all.entrySet()) {
            String groupName = obj.getKey();
            JsonArray items = obj.getValue().getAsJsonArray();
            JsonArray newGroup = new JsonArray();
            for (JsonElement json : items) {
                JsonObject item = json.getAsJsonObject();
                String UIFieldName = item.get("UIFieldName").getAsString();
                if ("".equals(keywords) || UIFieldName.contains(keywords)) {
                    JsonObject itemNew = (JsonObject) jsonParser.parse(gson.toJson(item));
                    if (!"".equals(keywords)) {
                        UIFieldName = UIFieldName.replaceAll(keywords, "<span style='color:red'>" + keywords + "</span>");
                        itemNew.addProperty("UIFieldName", UIFieldName);
                    }
                    newGroup.add(itemNew);
                }
            }
            if (newGroup.size() > 0) {
                if (paramObj.has("filterPath")) {
                    String filterPath = paramObj.get("filterPath").getAsString();
                    if (!StringUtils.isEmpty(filterPath)) {
                        if (groupName.equals(filterPath)) {
                            allNew.add(groupName, newGroup);
                        }
                    } else {
                        allNew.add(groupName, newGroup);
                    }
                } else {
                    allNew.add(groupName, newGroup);
                }
            }
        }
        resultBean.setCode(1);
        resultBean.setData(allNew);
        return gson.toJson(resultBean);
    }

    public String getCrfProjectDiseaseItem(JsonObject paramObj, User user) {
        String orgId = null;
        String labId = null;
        ResultBean resultBean = new ResultBean();
        try {
            orgId = user.getOrgID();
            labId = paramObj.get("labID").getAsString();
            List<CRFLab> crfLabs = AllDao.getInstance().getSyResourceDao().getCrfIDByLab(labId,orgId);
            JSONArray jsonArray = new JSONArray();
            for (CRFLab crfLab:crfLabs) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("sid",crfLab.getCrf_id());
                jsonObject.addProperty("slab_name",ArkService.getDiseaseName(crfLab.getCrf_id()));
                jsonArray.add(jsonObject);
            }
            resultBean.setCode(1);
            resultBean.setData(jsonArray);

        }catch (Exception e){
            logger.error("error",e);
            return ParamUtils.errorParam("获取病种列表失败");
        }

        return gson.toJson(resultBean);
    }


    public String searchHighlight(JsonObject paramObj) {
        String crfID = null;
        try {
            if(paramObj.has("crfID")){
                crfID = paramObj.get("crfID").getAsString();
            }
            /*
            获取 indexName；
            * */

            String url = ConfigurationService.getUrlBean().getHighlight();
            logger.info("CRF搜索详情高亮 url=" + url);
            paramObj.addProperty("indexName", ConfigUtils.getSearchIndexName());
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            logger.info("CRF搜索详情高亮 SS返回结果： " + result);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }


    }
}
