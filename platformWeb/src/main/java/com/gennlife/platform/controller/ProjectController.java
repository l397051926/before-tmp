package com.gennlife.platform.controller;

import com.gennlife.platform.processor.ProjectProcessor;
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
@RequestMapping("/project")
public class ProjectController {
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private static ProjectProcessor processor = new ProjectProcessor();
    private static JsonParser jsonParser = new JsonParser();
    @RequestMapping(value="/MyProjectList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getMyProjectList(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.myProjectList(paramObj);
            logger.info("我的项目 中 项目列表get 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("我的项目 中 项目列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/ProjectLogList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getProjectLogList(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.projectLogList(paramObj);
            logger.info("项目日志列表 get 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("项目日志列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/ProjectPlanList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getProjectPlanList(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.projectPlanList(paramObj);
            logger.info("项目方案列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("项目方案列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/MemberList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getMemberList(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.projectMemberList(paramObj);
            logger.info("项目成员列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("项目成员列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/BasicInfo",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getBasicInfo(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.baiscInfo(paramObj);
            logger.info("请求项目详情 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("请求项目详情 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/IsExistPlan",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getIsExistPlan(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.isExistPlan(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("判定方案知否存在 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/IsExistSet",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getIsExistSet(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.isExistSet(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("判定样本集知否存在 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/IsExistProject",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getIsExistProject(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.isExistProject(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("判定项目否存在 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/CreateNewProject",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postCreateNewProject(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String param = ParamUtils.getParam(paramRe);
        logger.info("CreateNewProject param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.createNewProject(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("创建项目 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/CreateNewPlan",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postCreateNewPlan(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String param = ParamUtils.getParam(paramRe);
        logger.info("CreateNewPlan param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.createNewPlan(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("创建任务 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AddMember",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postAddMember(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String param = ParamUtils.getParam(paramRe);
        logger.info("AddMember param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.addMember(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("增加项成员 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/Tools",produces = "application/json;charset=UTF-8")
    public @ResponseBody String postTools(){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            resultStr =  processor.tools();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("工作区工具列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/EditProject",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postEditProject(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String param = ParamUtils.getParam(paramRe);
        logger.info("EditProject param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.editProject(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑项目信息 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/EditPlan",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postEditPlan(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();

        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("EditPlan param ="+param);
            resultStr =  processor.editPlan(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑方案 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/DeleteProject",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postDeleteProject(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("DeleteProject param ="+param);
            resultStr =  processor.deleteProject(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除项目 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/DeletePlan",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postDeletePlan(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("DeletePlan param ="+param);
            resultStr =  processor.deletePlan(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除方案 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/DeleteProjectSet",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postDeleteProjectSet(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("DeleteProjectSet param ="+param);
            resultStr =  processor.deleteSet(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除样本集 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/DeleteMember",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postDeleteMember(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("DeleteMember param ="+param);
            resultStr =  processor.deleteMember(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除项目成员 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/Atool",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postAtool(@RequestBody String param){
        Long start = System.currentTimeMillis();
        logger.info("Atool param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.aTool(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("某个工具请求 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/Atool",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getAtool(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        logger.info("Atool param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.aTool(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("某个工具请求 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/SetList",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSetList(@RequestBody String param){
        Long start = System.currentTimeMillis();
        logger.info("SetList param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.setList(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("数据集合列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SetList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSetList(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        logger.info("SetList param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.setList(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("数据集合列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SetNameList",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSetNameList(@RequestBody String param){
        Long start = System.currentTimeMillis();
        logger.info("SetNameList param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.setNameList(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("数据集合名称列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/SetNameList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSetNameList(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        logger.info("SetNameList param ="+param);
        String resultStr = null;
        try{
            resultStr =  processor.setNameList(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("数据集合名称列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
