package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.FileProcessor;
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
public class FileServelt extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(FileServelt.class);
    private static FileProcessor processor = new FileProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if("/file/Rec".equals(uri)){
            try{
                processor.fsRec(req,resp);
            }catch (Exception e){
                logger.error("跳转",e);
            }
            logger.info("跳转 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/file/SystemInfo".equals(uri)){
            try{
                processor.systemInfo(req,resp);
            }catch (Exception e){
                logger.error("系统",e);
            }
            logger.info("系统信息 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }
    }
}
