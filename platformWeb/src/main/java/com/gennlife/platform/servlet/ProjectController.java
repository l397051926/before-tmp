package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.ProjectProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chensong on 2015/12/9.
 */
public class ProjectController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private static ProjectProcessor processor = new ProjectProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if("/project/MyProjectList".equals(uri)){
            try{
                processor.myProjectList(req,resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("我的项目 中 项目列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/ProjectLogList".equals(uri)){
            try{
                processor.projectLogList(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("项目日志列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/ProjectPlanList".equals(uri)){
            try{
                processor.projectPlanList(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("项目方案列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/MemberList".equals(uri)){
            try{
                processor.projectMemberList(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("项目成员列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/AddMember".equals(uri)){
            try{
                processor.addMember(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("项目成员列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/CreateNewProject".equals(uri)){
            try{
                processor.createNewProject(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("创建项目 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/CreateNewPlan".equals(uri)) {
            try{
                processor.createNewPlan(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("创建任务 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/Tools".equals(uri)){
            try{
                processor.tools(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("工作区工具列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/SetList".equals(uri)){
            try{
                processor.setList(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("数据集合列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/DeletePlan".equals(uri)){
            try{
                processor.deletePlan(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("删除方案 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/StoreSet".equals(uri)){
            try{
                processor.storeSet(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("保存数据集 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/Atool".equals(uri)){
            try{
                processor.aTool(req,resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("某个工具请求 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/DeleteMemeber".equals(uri)){
            try{
                processor.deleteMemeber(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
        }else if("/project/EditPlan".equals(uri)){
            try{
                processor.editPlan(req,resp);
            }catch (Exception e){
                logger.error("", e);
            }
        }else if("/project/SetNameList".equals(uri)){
            try{
                processor.setNameList(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("数据集合名称列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/DeleteProjectSet".equals(uri)){
            try{
                processor.deleteSet(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("数据集合名称列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/EditProject".equals(uri)){
            try{
                processor.editProject(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("编辑项目信息 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/project/DeleteProject".equals(uri)){
            try{
                processor.deleteProject(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("退出项目 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }

    }
}
