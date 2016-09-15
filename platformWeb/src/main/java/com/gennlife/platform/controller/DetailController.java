package com.gennlife.platform.controller;

import com.gennlife.platform.processor.DetailProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chen-song on 16/5/13.
 */
@Controller
@RequestMapping("/detail")
public class DetailController {
    private Logger logger = LoggerFactory.getLogger(DetailController.class);
    private static JsonParser jsonParser = new JsonParser();
    private DetailProcessor processor = new DetailProcessor();
    @RequestMapping(value="/PatientBasicInfo",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postPatientBasicInfo(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);


            logger.info("详情页患者基础信息接口 post方式 参数="+param);
            resultStr =  processor.patientBasicInfo(param);
        }catch (Exception e){
            logger.error("详情页患者基础信息接口",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页患者基础信息接口 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/PatientBasicInfo",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getPatientBasicInfo(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("详情页患者基础信息接口 get方式 参数="+param);
            resultStr =  processor.patientBasicInfo(param);
        }catch (Exception e){
            logger.error("详情页患者基础信息接口",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页患者基础信息接口 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/GeneticDisease",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postGeneticDisease(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("遗传性疾病 post方式 参数="+param);
            resultStr =  processor.geneticDisease(param);
        }catch (Exception e){
            logger.error("遗传性疾病",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("遗传性疾病 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/GeneticDisease",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getGeneticDisease(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("遗传性疾病 get方式 参数="+param);
            resultStr =  processor.geneticDisease(param);
        }catch (Exception e){
            logger.error("遗传性疾病",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("遗传性疾病 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/DrugReaction",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postDrugReaction(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("药物反应 post方式 参数="+param);
            resultStr =  processor.geneticDisease(param);
        }catch (Exception e){
            logger.error("药物反应",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("药物反应 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/DrugReaction",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getDrugReaction(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("药物反应 get方式 参数="+param);
            resultStr =  processor.drugReaction(param);
        }catch (Exception e){
            logger.error("药物反应",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("药物反应 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/PatientBasicFigure",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postPatientBasicFigure(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("基本统计图形&筛选条件  post方式 参数="+param);
            resultStr =  processor.patientBasicFigure(param);
        }catch (Exception e){
            logger.error("基本统计图形&筛选条件",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("基本统计图形&筛选条件 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/PatientBasicFigure",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getPatientBasicFigure(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("基本统计图形&筛选条件 get方式 参数="+param);
            resultStr =  processor.patientBasicFigure(param);
        }catch (Exception e){
            logger.error("基本统计图形&筛选条件",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("基本统计图形&筛选条件 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/PatientBasicTimeAxis",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postPatientBasicTimeAxis(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("详情页时间轴信息接口 post方式 参数="+param);
            resultStr =  processor.patientBasicTimeAxis(param);
        }catch (Exception e){
            logger.error("详情页时间轴信息接口",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页时间轴信息接口 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/PatientBasicTimeAxis",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getPatientBasicTimeAxis(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("详情页时间轴信息接口 get方式 参数="+param);
            resultStr =  processor.patientBasicTimeAxis(param);
        }catch (Exception e){
            logger.error("详情页时间轴信息接口",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页时间轴信息接口 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/ChoiceList",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postChoiceList(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("查看指标变化,可选列表 post方式 参数="+param);
            resultStr =  processor.choicesList(param);
        }catch (Exception e){
            logger.error("查看指标变化,可选列表",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看指标变化,可选列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/ChoiceList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getChoiceList(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("查看指标变化,可选列表 get方式 参数="+param);
            resultStr =  processor.choicesList(param);
        }catch (Exception e){
            logger.error("查看指标变化,可选列表",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看指标变化,可选列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/SpecificChoice",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSpecificChoice(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("查看指标变化,具体指标 post方式 参数="+param);
            resultStr =  processor.specificChoice(param);
        }catch (Exception e){
            logger.error("查看指标变化,具体指标",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看指标变化,具体指标 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/SpecificChoice",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getSpecificChoice(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("查看指标变化,具体指标 get方式 参数="+param);
            resultStr =  processor.specificChoice(param);
        }catch (Exception e){
            logger.error("查看指标变化,具体指标",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看指标变化,具体指标 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/VisitDetail",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postVisitDetail(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("详情页总接口：唐乾斌提供 post方式 参数="+param);
            resultStr =  processor.visitDetail(param);
        }catch (Exception e){
            logger.error("详情页总接口：唐乾斌提供",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页总接口：唐乾斌提供 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/VisitDetail",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getVisitDetail(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("详情页总接口：唐乾斌提供 post方式 参数="+param);
            resultStr =  processor.visitDetail(param);
        }catch (Exception e){
            logger.error("详情页总接口：唐乾斌提供",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页总接口：唐乾斌提供 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/LabResultItem",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postLabResultItem(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("详情页体检接口:唐乾斌提供  post方式 参数="+param);
            resultStr =  processor.labResultItem(param);
        }catch (Exception e){
            logger.error("详情页体检接口:唐乾斌提供",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页体检接口:唐乾斌提供 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/LabResultItem",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getLabResultItem(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("详情页体检接口:唐乾斌提供 post方式 参数="+param);
            resultStr =  processor.labResultItem(param);
        }catch (Exception e){
            logger.error("详情页体检接口:唐乾斌提供",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("详情页体检接口:唐乾斌提供 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/LabResultItemList",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postLabResultItemList(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("检验项列表  post方式 参数="+param);
            resultStr =  processor.labResultItemList(param);
        }catch (Exception e){
            logger.error("检验项列表",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("检验项列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/LabResultItemList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getLabResultItemList(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("检验项列表 get方式 参数="+param);
            resultStr =  processor.labResultItemList(param);
        }catch (Exception e){
            logger.error("检验项列表",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("检验项列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/CategoryCatalog",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postCategoryCatalog(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("分类信息接口及目录 post方式 参数="+param);
            resultStr =  processor.categoryCatalog(param);
        }catch (Exception e){
            logger.error("分类信息接口及目录",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分类信息接口及目录 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/CategoryCatalog",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getCategoryCatalog(@RequestParam("param")  String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("分类信息接口及目录 get方式 参数="+param);
            resultStr =  processor.categoryCatalog(param);
        }catch (Exception e){
            logger.error("分类信息接口及目录",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分类信息接口及目录 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/MolecularDetection",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postMolecularDetection(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("分子检测  post方式 参数="+param);
            resultStr =  processor.molecularDetection(param);
        }catch (Exception e){
            logger.error("分子检测",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分子检测 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/MolecularDetection",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getMolecularDetection(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("分子检测 get方式 参数="+param);
            resultStr =  processor.molecularDetection(param);
        }catch (Exception e){
            logger.error("分子检测",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分子检测 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/BiologicalSpecimen",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postBiologicalSpecimen(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("生物标本 post方式 参数="+param);
            resultStr =  processor.biologicalSpecimen(param);
        }catch (Exception e){
            logger.error("生物标本",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("生物标本 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/BiologicalSpecimen",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getBiologicalSpecimen(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("生物标本 get方式 参数="+param);
            resultStr =  processor.biologicalSpecimen(param);
        }catch (Exception e){
            logger.error("生物标本",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("生物标本 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/ExamResult",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postExamResult(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("检查  post方式 参数="+param);
            resultStr =  processor.examResult(param);
        }catch (Exception e){
            logger.error("检查",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("检查 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/ExamResult",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getExamResult(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("检查 get方式 参数="+param);
            resultStr =  processor.examResult(param);
        }catch (Exception e){
            logger.error("检查",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("检查 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/PathologicalExamination",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postPathologicalExamination(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("病理检测 post方式 参数="+param);
            resultStr =  processor.pathologicalExamination(param);
        }catch (Exception e){
            logger.error("病理检测",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病理检测 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/PathologicalExamination",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getPathologicalExamination(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("病理检测  get方式 参数="+param);
            resultStr =  processor.pathologicalExamination(param);
        }catch (Exception e){
            logger.error("病理检测",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病理检测 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/VisitDiagnose",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getVisitDiagnose(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("单次就诊专用:诊断报告  get方式 参数="+param);
            resultStr =  processor.visitDiagnose(param);
        }catch (Exception e){
            logger.error("诊断报告",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("单次就诊专用:诊断报告 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AdmissionRecords",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getAdmissionRecords(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("病程 诊断报告:主诉  get方式 参数="+param);
            resultStr =  processor.admissionRecords(param);
        }catch (Exception e){
            logger.error("病程 诊断报告:主诉",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病程 诊断报告:主诉 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/MedicalRecord",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getMedicalRecord(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("病案首页  get方式 参数="+param);
            resultStr =  processor.medicalRecord(param);
        }catch (Exception e){
            logger.error("病案首页",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病案首页 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/OperationRecords",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getOperationRecords(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("手术信息  get方式 参数="+param);
            resultStr =  processor.operationRecords(param);
        }catch (Exception e){
            logger.error("手术信息",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("手术信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/Pharmacy",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getPharmacy(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("用药医嘱  get方式 参数="+param);
            resultStr =  processor.pharmacy(param);
        }catch (Exception e){
            logger.error("用药医嘱",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用药医嘱 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/DischargeRecords",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getDischargeRecords(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("出院记录  get方式 参数="+param);
            resultStr =  processor.dischargeRecords(param);
        }catch (Exception e){
            logger.error("出院记录",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("出院记录 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/CourseRecords",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getCourseRecords(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("病程记录  get方式 参数="+param);
            resultStr =  processor.courseRecords(param);
        }catch (Exception e){
            logger.error("病程记录",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病程记录 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/MedicalCourse",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getMedicalCourse(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("病例文书  get方式 参数="+param);
            resultStr =  processor.medicalCourse(param);
        }catch (Exception e){
            logger.error("病例文书",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病例文书 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
