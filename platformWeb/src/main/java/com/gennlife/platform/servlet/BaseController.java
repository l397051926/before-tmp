package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.BaseProcessor;
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
public class BaseController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(BaseController.class);
    private BaseProcessor processor = new BaseProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if("/base/Chain".equals(uri)){//二级关联属性
            processor.chainItem(req,resp);
        }else if("/base/ProjectDisease".equals(uri)){
            processor.projectDisease(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
