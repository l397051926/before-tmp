package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.SampleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chensong on 2015/12/14.
 */
public class SampleController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(SampleController.class);
    private static SampleProcessor processor = new SampleProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if("/sample/Import".equals(uri)){
            try{
                processor.importSample(req,resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("导入样本集合 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/sample/SetRename".equals(uri)){
            try{
                processor.importSample(req,resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("样本集合数据详情查看 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/sample/SetDetail".equals(uri)){
            try{
                processor.sampleDetail(req,resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("样本集合数据详情查看 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/sample/EditSet".equals(uri)){
            try{
                processor.editSet(req,resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("编辑样本集 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }
    }
}
