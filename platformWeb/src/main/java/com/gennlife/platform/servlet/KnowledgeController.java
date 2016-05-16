package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.FileProcessor;
import com.gennlife.platform.proc.KnowledgeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chen-song on 16/5/6.
 */
public class KnowledgeController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(KnowledgeController.class);
    private static KnowledgeProcessor processor = new KnowledgeProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if("/knowledge/Search".equals(uri)){
            try{
                processor.search(req,resp);
            }catch (Exception e){
                logger.error("知识库搜索",e);
            }
            logger.info("知识库搜索 耗时:" + (System.currentTimeMillis()-start) +"ms");

        }
    }
}
