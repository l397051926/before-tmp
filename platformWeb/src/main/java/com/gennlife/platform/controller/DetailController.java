package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.bean.HitsConfigBean;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.processor.DetailProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chen-song on 16/5/13.
 */
@Controller
@RequestMapping("/detail")
public class DetailController {
    private Logger logger = LoggerFactory.getLogger(DetailController.class);

    private DetailProcessor processor = new DetailProcessor();

    @RequestMapping(value = "/PatientBasicInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientBasicInfo(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("详情页患者基础信息接口 get方式 参数=" + param);
            resultStr = processor.patientBasicInfo(param);
        } catch (Exception e) {
            logger.error("详情页患者基础信息接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页患者基础信息接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/PatientDetailBasiInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientBasicInfoDetail(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("详情页患者基础信息接口 get方式 参数=" + param);
            resultStr = processor.patientBasicInfoDetail(param);
        } catch (Exception e) {
            logger.error("详情页患者基础信息接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页患者基础信息接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/PatInfo/{type}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientInfo(HttpServletRequest paramRe, @PathVariable("type") String type) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            String url = ConfigurationService.getUrlBean().getCaseDetailCommonUrl() + type;
            resultStr = HttpRequestUtils.httpPost(url, param);
        } catch (Exception e) {
            logger.error("详情页通用患者信息接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页通用患者信息接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/GeneticDisease", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getGeneticDisease(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("遗传性疾病 get方式 参数=" + param);
            resultStr = processor.geneticDisease(param);
        } catch (Exception e) {
            logger.error("遗传性疾病", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("遗传性疾病 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/DrugReaction", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getDrugReaction(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("药物反应 get方式 参数=" + param);
            resultStr = processor.drugReaction(param);
        } catch (Exception e) {
            logger.error("药物反应", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("药物反应 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/PatientBasicFigure", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientBasicFigure(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("基本统计图形&筛选条件 get方式 参数=" + param);
            resultStr = processor.patientBasicFigure(param);
        } catch (Exception e) {
            logger.error("基本统计图形&筛选条件", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("基本统计图形&筛选条件 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/PatientBasicTimeAxis", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientBasicTimeAxis(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("详情页时间轴信息接口 get方式 参数=" + param);
            resultStr = processor.patientBasicTimeAxis(param);
        } catch (Exception e) {
            logger.error("详情页时间轴信息接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页时间轴信息接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/ChoiceList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getChoiceList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("查看指标变化,可选列表 get方式 参数=" + param);
            resultStr = processor.choicesList(param);
        } catch (Exception e) {
            logger.error("查看指标变化,可选列表", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看指标变化,可选列表 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/SpecificChoice", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSpecificChoice(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("查看指标变化,具体指标 get方式 参数=" + param);
            resultStr = processor.specificChoice(param);
        } catch (Exception e) {
            logger.error("查看指标变化,具体指标", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看指标变化,具体指标 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VisitDetail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getVisitDetail(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("详情页总接口：唐乾斌提供 post方式 参数=" + param);
            resultStr = processor.visitDetail(param);
        } catch (Exception e) {
            logger.error("详情页总接口：唐乾斌提供", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页总接口：唐乾斌提供 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/LabResultItem", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postLabResultItem(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("详情页体检接口:唐乾斌提供  post方式 参数=" + param);
            resultStr = processor.labResultItem(param);
        } catch (Exception e) {
            logger.error("详情页体检接口:唐乾斌提供", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页体检接口:唐乾斌提供 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/LabResultItemList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getLabResultItemList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("检验项列表 get方式 参数=" + param);
            resultStr = processor.labResultItemList(param);
        } catch (Exception e) {
            logger.error("检验项列表", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("检验项列表 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/CategoryCatalog", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getCategoryCatalog(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("分类信息接口及目录 get方式 参数=" + param);
            resultStr = processor.categoryCatalog(param);
        } catch (Exception e) {
            logger.error("分类信息接口及目录", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分类信息接口及目录 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/MolecularDetection", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getMolecularDetection(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("分子检测 get方式 参数=" + param);
            resultStr = processor.molecularDetection(param);
        } catch (Exception e) {
            logger.error("分子检测", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分子检测 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/BiologicalSpecimen", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getBiologicalSpecimen(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("生物标本 get方式 参数=" + param);
            resultStr = processor.biologicalSpecimen(param);
        } catch (Exception e) {
            logger.error("生物标本", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("生物标本 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/ExamResult", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getExamResult(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("检查 get方式 参数=" + param);
            resultStr = processor.examResult(param);
        } catch (Exception e) {
            logger.error("检查", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("检查 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/PathologicalExamination", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPathologicalExamination(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("病理检测  get方式 参数=" + param);
            resultStr = processor.pathologicalExamination(param);
        } catch (Exception e) {
            logger.error("病理检测", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病理检测 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VisitDiagnose", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getVisitDiagnose(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("单次就诊专用:诊断报告  get方式 参数=" + param);
            resultStr = processor.visitDiagnose(param);
        } catch (Exception e) {
            logger.error("诊断报告", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("单次就诊专用:诊断报告 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/AdmissionRecords", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getAdmissionRecords(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("病程 诊断报告:主诉  get方式 参数=" + param);
            resultStr = processor.admissionRecords(param);
        } catch (Exception e) {
            logger.error("病程 诊断报告:主诉", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病程 诊断报告:主诉 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/MedicalRecord", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getMedicalRecord(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("病案首页  get方式 参数=" + param);
            resultStr = processor.medicalRecord(param);
        } catch (Exception e) {
            logger.error("病案首页", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病案首页 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/OperationRecords", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getOperationRecords(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("手术信息  get方式 参数=" + param);
            resultStr = processor.operationRecords(param);
        } catch (Exception e) {
            logger.error("手术信息", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("手术信息 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/Pharmacy", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPharmacy(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("用药医嘱  get方式 参数=" + param);
            resultStr = processor.pharmacy(param);
        } catch (Exception e) {
            logger.error("用药医嘱", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用药医嘱 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DischargeRecords", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getDischargeRecords(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("出院记录  get方式 参数=" + param);
            resultStr = processor.dischargeRecords(param);
        } catch (Exception e) {
            logger.error("出院记录", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("出院记录 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/CourseRecords", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getCourseRecords(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("病程记录  get方式 参数=" + param);
            resultStr = processor.courseRecords(param);
        } catch (Exception e) {
            logger.error("病程记录", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病程记录 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/MedicalCourse", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getMedicalCourse(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("病例文书  get方式 参数=" + param);
            resultStr = processor.medicalCourse(param);
        } catch (Exception e) {
            logger.error("病例文书", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病例文书 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VisitClassifySection", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VisitClassifySection(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("病史信息接口  get方式 参数=" + param);
            resultStr = processor.visitClassifySection(param);
        } catch (Exception e) {
            logger.error("病史信息接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病史信息接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/VisitClassifyImage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VisitClassifyImage(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("图片获取接口  post方式 参数=" + param);
            resultStr = processor.visitClassifyImage(param);
        } catch (Exception e) {
            logger.error("图片获取接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("图片获取接口 post方式 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/VisitDcOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VisitDcOrder(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("单次就诊DC治疗   post方式 参数=" + param);
            resultStr = processor.VisitDcOrder(param);
        } catch (Exception e) {
            logger.error("单次就诊DC治疗", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("单次就诊DC治疗 post方式 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/PatientDcOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String PatientDcOrder(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("分类详情DC治疗  post方式 参数=" + param);
            resultStr = processor.PatientDcOrder(param);
        } catch (Exception e) {
            logger.error("分类详情DC治疗", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分类详情DC治疗 post方式 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/PatientRadiotherapy", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String PatientRadiotherapy(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("分类详情放疗  post方式 参数=" + param);
            resultStr = processor.PatientRadiotherapy(param);
        } catch (Exception e) {
            logger.error("分类详情放疗", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分类详情放疗 post方式 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/VisitRadiotherapy", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VisitRadiotherapy(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("单次就诊放疗  post方式 参数=" + param);
            resultStr = processor.VisitRadiotherapy(param);
        } catch (Exception e) {
            logger.error("单次就诊放疗", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("单次就诊放疗 post方式 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/PatientChemotherapyInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String PatientChemotherapyInfo(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("分类详情化疗  post方式 参数=" + param);
            resultStr = processor.PatientChemotherapyInfo(param);
        } catch (Exception e) {
            logger.error("分类详情化疗", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分类详情化疗 post方式 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    /**
     * 后期修改名称了，原先叫SimilarServiceGetSimilars
     *
     * @param paramRe
     * @return
     */
    @RequestMapping(value = "/SimilarServiceVitaboard", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String SimilarServiceGetSimilars(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.similarServiceGetSimilars(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("vitaboard画图数据获取接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SimilarServiceVitaboardParam", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String PatientDetailVitaboardParam(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.patientDetailVitaboardParam(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取vitaboard默认参数接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SimilarServiceSimilarParam", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String SimilarServiceVitaboardParam(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.similarServiceSimilarParam(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取vitaboard默认参数接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SimilarServiceSimilarQuery", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String SimilarServiceSimilarQuery(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.similarServiceSimilarQuery(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("生成相似病人查询条件接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SimilarServiceSimilarBasicDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String SimilarServiceSimilarBasicDetail(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.similarServiceSimilarBasicDetail(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("生成相似病人查询条件接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SimilarServiceSimilarPatientInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String SimilarServiceSimilarPatientInfo(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.similarServiceSimilarPatientInfo(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("当前病人的信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SimilarServiceSimilarPatientExtraInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String SimilarServiceSimilarPatientExtraInfo(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.similarServiceSimilarPatientExtraInfo(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取当前病人的指定字段的信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    /**
     * 非隐私数据查询
     */
    @RequestMapping(value = "/PatientBatchData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String PatientBatchDataConsultingRoom(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.PatientBatchData(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取非隐私数据 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    /**
     * 我的诊室数据查询
     */
    @RequestMapping(value = "/ConsultingRoomData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String ConsultingRoomData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.ConsultingRoomData(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取非隐私数据 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/TripleTestTable", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String TripleTestTable(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            resultStr = processor.TripleTestTable(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("三测单 TripleTestTable 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    /**
     * 缩略图获取
     * 对应FS接口   FileServcie/PatientDetail/thumbnail?url=
     */
    @RequestMapping(value = "/PatientDetailThumbnail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    void PatientDetailThumbnail(@RequestParam("url") String url, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        try {
            logger.info("缩略图获取URL " + url);
            processor.PatientDetailThumbnail(url, response);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("缩略图获取 耗时" + (System.currentTimeMillis() - start) + "ms");
    }

    @RequestMapping(value = "/getGennomicsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getGennomicsList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("详情页患者基础信息接口 get方式 参数=" + param);
            resultStr = processor.getGennomicsList(param);
        } catch (Exception e) {
            logger.error("详情页患者基础信息接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页患者基础信息接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/applyOutGoing", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String applyOutGoing(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("等处？ get方式 参数=" + param);
            resultStr = processor.applyOutGoing(param);
        } catch (Exception e) {
            logger.error("等处？", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("等处？ get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/get_patien_sn", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatienSn(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            String sessionId = paramRe.getSession(true).getId();
            RedisUtil.setValue(sessionId, HitsConfigBean.HIES_SESSION_ID);
            logger.info("get_patient_sn  sessionId:"+sessionId);
            logger.info("获取患者基本信息 get方式 参数=" + param);
            resultStr = processor.getPatienSn(param);
        } catch (Exception e) {
            logger.error("获取患者基本信息", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取患者基本信息 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newTripleTestTable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String tripleTestTable(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("新三测单渲染 get方式 参数=" + param);
            resultStr = processor.tripleTestTable(param);
        } catch (Exception e) {
            logger.error("新三测单渲染", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新三测单渲染 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/getSchemaData", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSchemaData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.getSchemaData();
        } catch (Exception e) {
            logger.error("获取患者基本信息", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取患者基本信息 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/labResult", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getlabResultList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("检验大项列表 get方式 参数=" + param);
            resultStr = processor.labResultList(param);
        } catch (Exception e) {
            logger.error("检验大项列表", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("检验大项列表 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newLabResultItem", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getNewLabResultItemList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新检验子项列表接口 get方式 参数=" + param);
            resultStr = processor.NewLabResultItemList(param);
        } catch (Exception e) {
            logger.error("新检验子项列表接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新检验子项列表接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newMedicalCourse", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newMedicalCourse(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新详情页病病例文书 接口 get方式 参数=" + param);
            resultStr = processor.newMedicalCourse(param);
        } catch (Exception e) {
            logger.error("新详情页病病例文书 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新详情页病病例文书 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newCourseRecords", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newCourseRecords(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新详情页病程记录 接口 get方式 参数=" + param);
            resultStr = processor.newCourseRecords(param);
        } catch (Exception e) {
            logger.error("新详情页病程记录 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新详情页病程记录 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newExamResult", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newExamResult(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新详情检查页面 接口 get方式 参数=" + param);
            resultStr = processor.newExamResult(param);
        } catch (Exception e) {
            logger.error("新详情检查页面 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新详情检查页面 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/newQuotaReports", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newQuotaReports(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新详情检验子项指标绘制图接口 get方式 参数=" + param);
            resultStr = processor.newQuotaReports(param);
        } catch (Exception e) {
            logger.error("新详情页面检验子项指标绘制图接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新详情页面检验子项指标绘制图接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newOperationRecords", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newOperationRecords(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新详情手术 接口 get方式 参数=" + param);
            resultStr = processor.newOperationRecords(param);
        } catch (Exception e) {
            logger.error("新详情手术 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新详情手术 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newMedicineOrder", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newMedicineOrder(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新详情药品遗嘱接口 get方式 参数=" + param);
            resultStr = processor.newMedicineOrder(param);
        } catch (Exception e) {
            logger.error("新详情药品遗嘱 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新详情药品遗嘱 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newOrdersPharmacy", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newOrdersPharmacy(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新详情非药品遗嘱 接口 get方式 参数=" + param);
            resultStr = processor.newOrdersPharmacy(param);
        } catch (Exception e) {
            logger.error("新详情非药品遗嘱 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新详情非药品遗嘱 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/newVisitDiagnose", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newVisitDiagnose(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新诊断接口调整 接口 get方式 参数=" + param);
            resultStr = processor.newVisitDiagnose(param);
        } catch (Exception e) {
            logger.error("新诊断接口调整 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新诊断接口调整 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/newMedical_records", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String newMedical_records(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("新诊断接口调整 接口 get方式 参数=" + param);
            resultStr = processor.newMedicalRecords(param);
        } catch (Exception e) {
            logger.error("新诊断接口调整 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新诊断接口调整 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/getSwimlane", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSwimlane(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("泳道图 接口 get方式 参数=" + param);
            resultStr = processor.getSwimlane(param);
        } catch (Exception e) {
            logger.error("泳道图 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("泳道图 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/ordersPharmacyDay", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getOrdersPharmacyDay(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            logger.info("泳道图 接口 get方式 参数=" + param);
            resultStr = processor.getOrdersPharmacyDay(param);
        } catch (Exception e) {
            logger.error("用药单天记录 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用药单天记录 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/getHomePageConfig", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getHomePageConfig(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {

            resultStr = processor.getHomePageConfig();

        } catch (Exception e) {
            logger.error("我的诊室首页 功能 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("我的诊室首页 功能 接口 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


}
