package com.gennlife.platform.controller;

import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.LaboratoryProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by chen-song on 16/9/12.
 */
@Controller
@RequestMapping("/bsma")
public class BackstageManagementController {
    private Logger logger = LoggerFactory.getLogger(BackstageManagementController.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static LaboratoryProcessor processor = new LaboratoryProcessor();
    @RequestMapping(value="/OrgMapData",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postOrgMapData(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("获取科室组织信息 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.orgMapData(paramObj);
        }catch (Exception e){
            logger.error("获取科室组织信息",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取科室组织信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AddOrg",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postChangePWD(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addOrg(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("添加科室 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/DeleteOrg",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postDeleteOrg(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            HttpSession session = paramRe.getSession();
            if(session == null){
                return ParamUtils.errorParam("当前session已经失效");
            }
            User user = gson.fromJson((String)session.getAttribute("user"),User.class);
            logger.info("删除科室 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.deleteOrg(paramObj,user);
        }catch (Exception e){
            logger.error("删除科室",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除科室 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/UpdateOrg",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postUpdateOrg(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            HttpSession session = paramRe.getSession();
            if(session == null){
                return ParamUtils.errorParam("当前session已经失效");
            }
            User user = gson.fromJson((String)session.getAttribute("user"),User.class);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.updateOrg(paramObj,user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("更新科室 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/GetStaffInfo",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postGetStaffInfo(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            HttpSession session = paramRe.getSession();
            if(session == null){
                return ParamUtils.errorParam("当前session已经失效");
            }
            User user = gson.fromJson((String)session.getAttribute("user"),User.class);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getStaffInfo(paramObj,user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用于获取某组织科室下的人员信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AddStaff",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postAddStaff(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            HttpSession session = paramRe.getSession();
            if(session == null){
                return ParamUtils.errorParam("当前session已经失效");
            }
            User user = gson.fromJson((String)session.getAttribute("user"),User.class);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addStaff(paramObj,user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("添加用户 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/DeleteStaff",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postDeleteStaff(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            HttpSession session = paramRe.getSession();
            if(session == null){
                return ParamUtils.errorParam("当前session已经失效");
            }
            User user = gson.fromJson((String)session.getAttribute("user"),User.class);
            JsonArray paramObj = (JsonArray) jsonParser.parse(param);
            resultStr =  processor.deleteStaff(paramObj,user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除用户 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/GetProfessionList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postGetProfessionList(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            HttpSession session = paramRe.getSession();
            if(session == null){
                return ParamUtils.errorParam("当前session已经失效");
            }
            User user = gson.fromJson((String)session.getAttribute("user"),User.class);
            String orgID = user.getOrgID();
            resultStr =  processor.getProfessionList(orgID);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("职称列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
