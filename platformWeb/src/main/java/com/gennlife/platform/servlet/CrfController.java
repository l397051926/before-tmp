package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.CrfProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chen-song on 16/3/18.
 */
public class CrfController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(CrfController.class);
    private static CrfProcessor processor = new CrfProcessor();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if ("/crf/Model".equals(uri)) {//获取模型
            try{
                processor.model(req, resp);
            }catch (Exception e){
                logger.error("获取模型",e);
            }
            logger.info("获取模型 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/SelectAttr".equals(uri)) {//勾选属性,修改模型
            try{
                processor.selectAttr(req, resp);
            }catch (Exception e){
                logger.error("勾选属性,修改模型",e);
            }
            logger.info("勾选属性,修改模型 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/AddGroup".equals(uri)) {
            try{
                processor.addGroup(req, resp);
            }catch (Exception e){
                logger.error("模型增加组",e);
            }
            logger.info("模型增加组 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/AddAttr".equals(uri)) {
            try{
                processor.addAttr(req, resp);
            }catch (Exception e){
                logger.error("模型增加属性",e);
            }
            logger.info("模型增加属性 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/EditModel".equals(uri)) {//更新模板
            try{
                processor.editModel(req, resp);
            }catch (Exception e){
                logger.error("更新模板",e);
            }
            logger.info("更新模板 耗时:" + (System.currentTimeMillis()-start) +"ms");

        } else if ("/crf/SaveModel".equals(uri)) {//保存模板
            try{
                processor.saveModel(req, resp);
            }catch (Exception e){
                logger.error("保存模板",e);
            }
            logger.info("保存模板 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/GetData".equals(uri)) {//数据录入时,请求某个case数据
            try{
                processor.getData(req, resp);
            }catch (Exception e){
                logger.error("数据录入时,请求某个case数据",e);
            }
            logger.info("数据录入时,请求某个case数据 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/UpLoadData".equals(uri)) {
            try{
                processor.upLoadData(req, resp);
            }catch (Exception e){
                logger.error("上传crf数据",e);
            }
            logger.info("上传crf数据 耗时:" + (System.currentTimeMillis()-start) +"ms");

        }else if("/crf/SaveData".equals(uri)){
            try{
                processor.saveData(req, resp);
            }catch (Exception e){
                logger.error("录入完成接口",e);
            }
            logger.info("录入完成接口 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/crf/ModelTree".equals(uri)){
            try{
                processor.modelTree(req, resp);
            }catch (Exception e){
                logger.error("获取模型树,没有叶子节点",e);
            }
            logger.info("获取模型树,没有叶子节点 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if ("/crf/SampleCaseList".equals(uri)){
            try{
                processor.sampleCaseList(req, resp);
            }catch (Exception e){
                logger.error("病历列表数据",e);
            }
            logger.info("病历列表数据 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/crf/DeleteSample".equals(uri)){
            try{
                processor.deleteSample(req, resp);
            }catch (Exception e){
                logger.error("删除某个case数据",e);
            }
            logger.info("删除某个case数据 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/crf/SearchSampleList".equals(uri)){
            try{
                processor.searchSampleList(req, resp);
            }catch (Exception e){
                logger.error("病历列表",e);
            }
            logger.info("病历列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/crf/UpdateGroupName".equals(uri)){
            try{
                processor.updateGroupName(req, resp);
            }catch (Exception e){
                logger.error("更新组名称异常",e);
            }
            logger.info("更新组名称 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if ("/crf/DeleteGroup".equals(uri)){
            try{
                processor.deleteGroup(req, resp);
            }catch (Exception e){
                logger.error("删除组异常",e);
            }
            logger.info("删除组 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
