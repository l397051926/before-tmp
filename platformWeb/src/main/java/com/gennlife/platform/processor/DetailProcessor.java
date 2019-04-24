package com.gennlife.platform.processor;

import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
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
}
