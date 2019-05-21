package com.gennlife.platform.processor;

import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chen-song on 16/5/13.
 */
public class DetailProcessor {
    private Logger logger = LoggerFactory.getLogger(DetailProcessor.class);
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();

    /**
     * 详情页患者基础信息接口
     * 透传,无逻辑
     *
     * @param param
     */
    public String patientBasicInfo(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseDetailPatientBasicInfoURL();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }

    }

    public String getGennomicsList(String param) {
        try {
//            String url = "http://10.0.0.152:8111/PatientDetail/getGennomics";
            String url = ConfigurationService.getUrlBean().getGetGennomics();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }

    }

    /**
     * 遗传性疾病
     * 透传,无逻辑
     *
     * @param param
     */
    public String geneticDisease(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseGenetic_disease();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }

    }


    /**
     * 药物反应
     * 透传,无逻辑
     *
     * @param param
     */
    public String drugReaction(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseDrug_reaction();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }

    }

    /**
     * 详情页基本统计图形&筛选条件
     * 透传,无逻辑
     *
     * @param param
     */
    public String patientBasicFigure(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseDetailPatientBasicFigureURL();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 详情页时间轴接口
     * 透传,无逻辑
     *
     * @param param
     */
    public String patientBasicTimeAxis(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePatientBasicTimeAxisURL();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 查看指标变化：可选列表
     *
     * @param param
     */
    public String choicesList(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePhysical_examination_list();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 查看指标变化：具体指标
     *
     * @param param
     */
    public String specificChoice(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePhysical_examination();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 详情页总接口：唐乾斌提供
     *
     * @param param
     */
    public String visitDetail(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseVisit_detail();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 详情页体检接口:唐乾斌提供
     *
     * @param param
     */
    public String labResultItem(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseLab_result_item();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 检验项列表
     *
     * @param param
     */
    public String labResultItemList(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseLab_result_item_list();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }


    public String categoryCatalog(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseCategory_catalog();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String molecularDetection(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseMolecular_detection();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String biologicalSpecimen(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseBiological_specimen();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String examResult(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseExam_result();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String pathologicalExamination(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePathological_examination();
            logger.info("PathologicalExamination url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            //logger.info("PathologicalExamination result="+result);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 单次就诊专用
     *
     * @param param
     * @return
     */
    public String visitDiagnose(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseDiagnose();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病程 诊断报告:主诉
     *
     * @param param
     * @return
     */
    public String admissionRecords(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseAdmission_records();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病案首页
     *
     * @param param
     * @return
     */
    public String medicalRecord(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseMedicalRecord();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 手术信息
     *
     * @param param
     * @return
     */
    public String operationRecords(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseOperationRecords();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 用药医嘱
     *
     * @param param
     * @return
     */
    public String pharmacy(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePharmacy();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 出院记录
     *
     * @param param
     * @return
     */
    public String dischargeRecords(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseDischargeRecords();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病程记录
     *
     * @param param
     * @return
     */
    public String courseRecords(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseCourseRecords();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病例文书
     *
     * @param param
     * @return
     */
    public String medicalCourse(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseMedicalCourse();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病史信息接口
     *
     * @param param
     * @return
     */
    public String visitClassifySection(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseDetailVisitClassifySectionURL();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 图片获取接口
     *
     * @param param
     * @return
     */
    public String visitClassifyImage(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseDetailVisitClassifyImageURL();
            logger.info("visitClassifyImage url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String VisitDcOrder(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseVisitDcOrder();
            logger.info("VisitDcOrder url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String PatientDcOrder(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePatientDcOrder();
            logger.info("PatientDcOrder url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String PatientRadiotherapy(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePatientRadiotherapy();
            logger.info("PatientRadiotherapy url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String VisitRadiotherapy(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseyVisitRadiotherapy();
            logger.info("VisitRadiotherapy url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String PatientChemotherapyInfo(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePatientChemotherapyInfo();
            logger.info("PatientChemotherapyInfo url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * vitaboard画图数据获取接口
     *
     * @param param
     * @return
     */
    public String similarServiceGetSimilars(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceGetSimilars();
            logger.info("SimilarServiceGetSimilars url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * @param param
     * @return
     */
    public String patientDetailVitaboardParam(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceVitaboardparam();
            logger.info("SimilarServiceVitaboardParam url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String similarServiceSimilarParam(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarParam();
            logger.info("similarServiceSimilarParam url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 生成相似病人查询条件接口
     *
     * @param param
     * @return
     */
    public String similarServiceSimilarQuery(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarQuery();
            logger.info("similarServiceSimilarQuery url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 获取相似病人情况的基本信息
     *
     * @param param
     * @return
     */
    public String similarServiceSimilarBasicDetail(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarBasicDetail();
            logger.info("similarServiceSimilarBasicDetail url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 当前病人的信息
     *
     * @param param
     * @return
     */
    public String similarServiceSimilarPatientInfo(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarPatientInfo();
            logger.info("similarServiceSimilarPatientInfo url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String similarServiceSimilarPatientExtraInfo(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarPatientExtraInfo();
            logger.info("similarServiceSimilarPatientExtraInfo url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String PatientBatchData(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCasePatientBatchData();
            logger.info("PatientBatchData url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }

    }

    public String ConsultingRoomData(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getConsultingRoomData();
            logger.info("ConsultingRoomData url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String TripleTestTable(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getTripleTestTableUrl();
            logger.info("TripleTestTable url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public void PatientDetailThumbnail(String urlId, HttpServletResponse response) {
        try {
            String urlStr = ConfigurationService.getUrlBean().getPatientDetailThumbnail() + urlId;
            logger.info("到FS抽取缩略图地址URL " + urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            InputStream inStream = conn.getInputStream(); // 通过输入流获取图片数据

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len = 0;
            while ( (len = inStream.read(buffer)) != -1 ) {
                outStream.write(buffer, 0, len);
            }
            response.setContentType("image/jpg"); // 设置返回的文件类型
            response.getOutputStream().write(outStream.toByteArray());
            inStream.close();
            conn.disconnect();
            outStream.close();
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public String applyOutGoing(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getApplyOutGoing();
            logger.info("applyOutGoing url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String getPatienSn(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getGetPatienSn();
            logger.info("getPatienSn url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("获取基本信息 ");
        }
    }

    public String tripleTestTable(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getTripleTestTable();
            logger.info("getPatienSn url=" + url);
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("获取基本信息 ");
        }
    }

    public String getSchemaData() {
        try {
            String result = ReadConditionByRedis.getDetailSchemaData();
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("获取基本信息 ");
        }
    }

    public String labResultList(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseLab_result_list();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String NewLabResultItemList(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseNew_lab_result_list();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String newMedicalCourse(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseNewMedicalCourse();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String newCourseRecords(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseNewCourseRecords();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String newExamResult(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseNewExamResult();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String newQuotaReports(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getCaseNewQuotaReports();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String newOperationRecords(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getNewOperationRecords();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String newMedicineOrder(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getNewMedicineOrder();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }
    public String newOrdersPharmacy(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getNewOrdersPharmacy();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String newVisitDiagnose(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getNewDiagnose();
            String result = HttpRequestUtils.httpPost(url, param);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String getHomePageConfig() {
        ResultBean resultBean = new ResultBean();
        try {
            String result = ReadConditionByRedis.getCDRHomePageConfig();
            resultBean.setCode(1);
            resultBean.setData(jsonParser.parse(result).getAsJsonArray());
            return gson.toJson(resultBean);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String newMedicalRecords(String param) {
        try {
            String url = ConfigurationService.getUrlBean().getNewMedicalRecords();
//            String result = HttpRequestUtils.httpPost(url, param);
            String result =tmpMedicalRecords;
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }
    private static final String tmpMedicalRecords = "{\"medical_record_home_page\":[{\"QC_NURSE\":\"张一佳\",\"PRACTICE_PHYSICIAN\":\"-\",\"DIS_STATUS_NAME\":\"不详\",\"VISIT_TIMES\":1,\"MARITAL_STATUS\":\"已婚\",\"READMISS_PLAN_31\":\"1\",\"TRANS_REACTION_NAME\":\"不详\",\"READMISS_PLAN_31_PURPOSE\":\"无\",\"NATIVE_PLACE\":\"***\",\"HOSPITAL_DAYS\":2,\"BIRTH_DATE\":\"1971\",\"CONSULT_PHYSICIAN\":\"马金平\",\"LEARN_PHYSICIAN\":\"-\",\"REFER_PHYSICIAN\":\"随雯雯\",\"BA_QUALITY\":\"甲\",\"RH_BLOOD_TYPE\":\"阳性\",\"DIS_DATE\":\"2018-08-25 08:57:37\",\"CHIEF_PHYSICIAN\":\"田晓娜\",\"PAYMENT_TYPE\":\"城镇居民基本医疗保险\",\"HOME_PLACE\":\"***\",\"DEPT_DIRECTOR\":\"侯玉华\",\"QC_DATE\":\"2018-08-25 00:00:00\",\"NATIONALITY\":\"CN\",\"DIAG_CONFORM1\":\"不详\",\"CHARGE_NURSE\":\"闫欣\",\"ADMISS_PATH\":\"门诊\",\"QC_PHYSICIAN\":\"田晓娜\",\"READMISS_PLAN_15\":\"不详\",\"ADMISS_STATUS_NAME\":\"不详\",\"DIS_DEPT_CODE\":\"ZB12040201\",\"BLOOD_TYPE\":\"B型\",\"ADMISS_DATE\":\"2018-08-23 15:33:38\",\"ETHNIC\":\"汉族\",\"ADMISS_DEPT_NAME\":\"妇一科住院\",\"CONTACT_ADDRESS\":\"***\",\"DIS_DEPT_NAME\":\"妇一科住院\",\"EMPLOYER_ADDRESS\":\"***\",\"SURGERY_UNEXPECTED\":\"不详\",\"DISCHARGE_PASS\":\"医嘱离院\",\"GENDER\":\"女性\",\"BIRTH_PLACE\":\"***\",\"AGE\":47,\"ADMISS_DEPT_CODE\":\"ZB12040201\",\"AUTOPSY\":\"3\",\"RELATION\":\"配偶\",\"MEDICAL_INSTITUTION_NAME\":\"郑州市中心医院\",\"LI_TRANS_REACTION_NAME\":\"不详\",\"POSTOPERATIVE_COMPLICATION\":\"不详\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"OCCUPATION\":\"无业人员\",\"DIAG_CONFORM7\":\"不详\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"HUKOU_PLACE\":\"***\",\"DIAG_CONFORM6\":\"不详\",\"DIAG_CONFORM5\":\"不详\",\"DIAG_CONFORM4\":\"不详\",\"DIAG_CONFORM3\":\"不详\",\"DIAG_CONFORM2\":\"不详\"}],\"dis_main_diag\":[{\"DIS_DIAG_NAME\":\"子宫内膜增厚\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"DIS_DIAG_CODE\":\"R93.803\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ADMISS_CONDITION\":\"不详\"},{\"DIS_DIAG_NAME\":\"轻度贫血\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"DIS_DIAG_CODE\":\"D64.901\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ADMISS_CONDITION\":\"不详\"},{\"DIS_DIAG_NAME\":\"瘢痕子宫\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"DIS_DIAG_CODE\":\"N85.801\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ADMISS_CONDITION\":\"不详\"},{\"DIS_DIAG_NAME\":\"肾病综合征\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"DIS_DIAG_CODE\":\"N04.900\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ADMISS_CONDITION\":\"不详\"},{\"DIS_DIAG_NAME\":\"异常子宫出血\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"DIS_DIAG_CODE\":\"N93.901\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ADMISS_CONDITION\":\"不详\"},{\"CLINIC_DIAG_CODE\":\"N93.901\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"CLINIC_DIAG_NAME\":\"异常子宫出血\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\"}],\"operation\":[{\"OPERATION_DATE\":\"2018-08-24 18:19:28\",\"OPERATION_CODE\":\"69.0902\",\"OPERATOR\":\"田晓娜\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"FIRST_ASIS\":\"马金平\",\"SECOND_ASIS\":\"随雯雯\",\"OPERATION_NAME\":\"宫腔镜诊断性刮宫术\",\"ANESTHESIA_TYPE\":\"01.全身麻醉\",\"OPERATION_SEQ\":2,\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"CUT_HEAL_GRADE\":\"II/甲.切口等级Ⅱ/愈合类型甲\",\"ANESTHETIST\":\"党萌\"},{\"OPERATION_DATE\":\"2018-08-24 18:17:58\",\"OPERATION_CODE\":\"68.1200x001\",\"OPERATOR\":\"田晓娜\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"FIRST_ASIS\":\"马金平\",\"OPERATION_GRADE\":\"三级\",\"SECOND_ASIS\":\"随雯雯\",\"OPERATION_NAME\":\"宫腔镜检查\",\"ANESTHESIA_TYPE\":\"01.全身麻醉\",\"OPERATION_SEQ\":1,\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ANESTHETIST\":\"党萌\"},{\"FIRST_ASIS\":\"马金平\",\"OPERATION_GRADE\":\"三级\",\"SECOND_ASIS\":\"随雯雯\",\"ANESTHESIA_TYPE\":\"01.全身麻醉\",\"OPERATION_SEQ\":1,\"ANESTHETIST\":\"党萌\",\"OPERATION_DATE\":\"2018-08-24 18:17:58\",\"OPERATION_CODE\":\"68.1200x001\",\"OPERATOR\":\"田晓娜\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"OPERATION_NAME\":\"宫腔镜检查\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"CUT_HEAL_GRADE\":\"II/甲.切口等级Ⅱ/愈合类型甲\"},{\"OPERATION_DATE\":\"2018-08-24 18:19:28\",\"OPERATION_CODE\":\"69.0902\",\"OPERATOR\":\"田晓娜\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"FIRST_ASIS\":\"马金平\",\"SECOND_ASIS\":\"随雯雯\",\"OPERATION_NAME\":\"宫腔镜诊断性刮宫术\",\"ANESTHESIA_TYPE\":\"01.全身麻醉\",\"OPERATION_SEQ\":2,\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ANESTHETIST\":\"党萌\"},{\"OPERATION_DATE\":\"2018-08-24 18:19:30\",\"OPERATION_CODE\":\"68.2913\",\"OPERATOR\":\"田晓娜\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"FIRST_ASIS\":\"马金平\",\"OPERATION_GRADE\":\"四级\",\"SECOND_ASIS\":\"随雯雯\",\"OPERATION_NAME\":\"宫腔镜子宫病损电切术\",\"ANESTHESIA_TYPE\":\"01.全身麻醉\",\"OPERATION_SEQ\":3,\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ANESTHETIST\":\"党萌\"},{\"FIRST_ASIS\":\"马金平\",\"OPERATION_GRADE\":\"四级\",\"SECOND_ASIS\":\"随雯雯\",\"ANESTHESIA_TYPE\":\"01.全身麻醉\",\"OPERATION_SEQ\":3,\"ANESTHETIST\":\"党萌\",\"OPERATION_DATE\":\"2018-08-24 18:19:30\",\"OPERATION_CODE\":\"68.2913\",\"OPERATOR\":\"田晓娜\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"OPERATION_NAME\":\"宫腔镜子宫病损电切术\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"CUT_HEAL_GRADE\":\"II/甲.切口等级Ⅱ/愈合类型甲\"},{\"OPERATION_DATE\":\"2018-08-24 18:19:32\",\"OPERATION_CODE\":\"67.3203\",\"OPERATOR\":\"田晓娜\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"FIRST_ASIS\":\"马金平\",\"OPERATION_GRADE\":\"三级\",\"SECOND_ASIS\":\"随雯雯\",\"OPERATION_NAME\":\"宫腔镜子宫颈病损电切术\",\"ANESTHESIA_TYPE\":\"01.全身麻醉\",\"OPERATION_SEQ\":4,\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"ANESTHETIST\":\"党萌\"},{\"FIRST_ASIS\":\"马金平\",\"OPERATION_GRADE\":\"三级\",\"SECOND_ASIS\":\"随雯雯\",\"ANESTHESIA_TYPE\":\"01.全身麻醉\",\"OPERATION_SEQ\":4,\"ANESTHETIST\":\"党萌\",\"OPERATION_DATE\":\"2018-08-24 18:19:32\",\"OPERATION_CODE\":\"67.3203\",\"OPERATOR\":\"田晓娜\",\"VISIT_SN\":\"vis_515ecd5f592361664e10939bd098a271\",\"OPERATION_NAME\":\"宫腔镜子宫颈病损电切术\",\"PATIENT_SN\":\"pat_03bb1fc4d3bb691bb141f3b907cad795\",\"CUT_HEAL_GRADE\":\"II/甲.切口等级Ⅱ/愈合类型甲\"}],\"success\":true,\"fstook\":\"85ms\"}";


}
