package com.gennlife.platform.controller;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.LaboratoryProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.UnsupportedCharsetException;

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
    String postOrgMapData(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            resultStr =  processor.orgMapData(user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
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
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("添加科室 param=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.addOrg(paramObj);
        }
        catch (UnsupportedCharsetException e)
        {
            resultStr = ParamUtils.errorParam("无法识别当前编码");

        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
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
            User user = (User)paramRe.getAttribute("currentUser");
            logger.info("删除科室 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.deleteOrg(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
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
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.updateOrg(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
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
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getStaffInfo(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
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
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addStaff(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
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
            User user = (User)paramRe.getAttribute("currentUser");
            JsonArray paramObj = (JsonArray) jsonParser.parse(param);
            resultStr =  processor.deleteStaff(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
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
            User user = (User)paramRe.getAttribute("currentUser");
            String orgID = user.getOrgID();
            resultStr =  processor.getProfessionList(orgID);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("职称列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/GetRoleInfo",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getGetRoleInfo(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getGetRoleInfo(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取角色信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/GetStaffTree",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getGetStaffTree(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getStaffTree(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("返回组织结构及其成员信息（树结构），支持根据用户名和工号搜索 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/DeleteRoles",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String DeleteRoles(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            JsonArray paramObj = (JsonArray) jsonParser.parse(param);
            resultStr =  processor.deleteRoles(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("支持批量删除角色 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AddRole",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String AddRole(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addRole(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("添加角色 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/EditRole",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String EditRole(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.editRole(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑角色信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/GetResourceTypeList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String GetResourceTypeList(){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonArray resourceTypeArray = ConfigurationService.getResourceTypeArray();
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(resourceTypeArray);
            resultStr = gson.toJson(resultBean);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取资源类型列表及其对应可操作类型 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/GetRoleStaff",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String GetRoleStaff(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getRoleStaff(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取角色关联的用户信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/GetRoleResource",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String GetRoleResource(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getRoleResource(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取角色关联的资源信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/GetResourceTree",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String GetResourceTree(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getResourceTree(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("根据资源类型获取资源树 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/UserInfo",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String UserInfo(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(user);
            return gson.toJson(resultBean);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("无参数获取用户信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/GroupList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String GroupList(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.groupList(paramObj,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取用户组列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/AddGroup",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String AddGroup(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            resultStr =  processor.addGroup(param,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }
        catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新建用户组 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/EditGroup",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String EditGroup(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            resultStr =  processor.editGroup(param,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑小组 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/GroupMembers",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String GroupMenbers(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            resultStr =  processor.groupMembers(param,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取组成员列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/DeleteGroup",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String DeleteGroup(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            resultStr =  processor.deleteGroup(param,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除小组 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/IsExistGroupName",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String IsExistGroupName(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            resultStr =  processor.isExistGroupName(param,user);
        } catch (DataIntegrityViolationException e){
            resultStr=DataIntegrityViolationExceptionMsg();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("判定组名称是否存在 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    private String DataIntegrityViolationExceptionMsg()
    {
        return ParamUtils.errorParam("填写内容太长,请尽量保持在20个以内");
    }

}
