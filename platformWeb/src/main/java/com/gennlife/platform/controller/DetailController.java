package com.gennlife.platform.controller;

import com.gennlife.platform.processor.DetailProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chen-song on 16/5/13.
 */
public class DetailController  extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(DetailController.class);
    private DetailProcessor processor = new DetailProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if("/detail/PatientBasicInfo".equals(uri)){//
            try{
                processor.patientBasicInfo(req,resp);
            }catch (Exception e){
                logger.error("详情页患者基础信息接口",e);
            }
            logger.info("详情页患者基础信息接口 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/detail/PatientBasicFigure".equals(uri)){
            try{
                processor.patientBasicFigure(req,resp);
            }catch (Exception e){
                logger.error("基本统计图形&筛选条件",e);
            }
            logger.info("基本统计图形&筛选条件 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/detail/PatientBasicTimeAxis".equals(uri)){
            try{
                processor.patientBasicTimeAxis(req,resp);
            }catch (Exception e){
                logger.error("详情页时间轴信息接口",e);
            }
            logger.info("详情页时间轴信息接口 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/detail/ChoiceList".equals(uri)){
            try{
                processor.choicesList(req,resp);
            }catch (Exception e){
                logger.error("查看指标变化,可选列表",e);
            }
            logger.info("查看指标变化,可选列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/detail/SpecificChoice".equals(uri)){
            try{
                processor.specificChoice(req,resp);
            }catch (Exception e){
                logger.error("查看指标变化,具体指标",e);
            }
            logger.info("查看指标变化,具体指标 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
