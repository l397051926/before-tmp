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
                logger.error("跳转",e);
            }
            logger.info("获取模型 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/SelectAttr".equals(uri)) {//勾选属性,修改模型
            try{
                processor.selectAttr(req, resp);
            }catch (Exception e){
                logger.error("跳转",e);
            }
            logger.info("勾选属性,修改模型 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/AddGroup".equals(uri)) {
            try{
                processor.addGroup(req, resp);
            }catch (Exception e){
                logger.error("跳转",e);
            }
            logger.info("模型增加组 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/AddAttr".equals(uri)) {
            try{
                processor.addAttr(req, resp);
            }catch (Exception e){
                logger.error("跳转",e);
            }
            logger.info("模型增加属性 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/EditModel".equals(uri)) {//更新模板
            try{
                processor.editModel(req, resp);
            }catch (Exception e){
                logger.error("跳转",e);
            }
            logger.info("更新模板 耗时:" + (System.currentTimeMillis()-start) +"ms");

        } else if ("/crf/SaveModel".equals(uri)) {//保存模板
            try{
                processor.saveModel(req, resp);
            }catch (Exception e){
                logger.error("跳转",e);
            }
            logger.info("保存模板 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/GetData".equals(uri)) {//数据录入时,请求某个case数据
            try{
                processor.getData(req, resp);
            }catch (Exception e){
                logger.error("跳转",e);
            }
            logger.info("数据录入时,请求某个case数据 耗时:" + (System.currentTimeMillis()-start) +"ms");
        } else if ("/crf/UpLoadData".equals(uri)) {
            try{
                processor.upLoadData(req, resp);
            }catch (Exception e){
                logger.error("跳转",e);
            }
            logger.info("上传crf数据接口 耗时:" + (System.currentTimeMillis()-start) +"ms");

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
