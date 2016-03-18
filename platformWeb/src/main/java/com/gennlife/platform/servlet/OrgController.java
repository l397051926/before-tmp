package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.OrganizationProcessor;
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
public class OrgController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(OrgController.class);
    private static OrganizationProcessor processor = new OrganizationProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if("/org/List".equals(uri)){
            try{
                processor.orgList(req,resp);
            }catch (Exception e){
                logger.error("组织机构列表异常",e);
            }
            logger.info("组织机构列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/org/Member".equals(uri)){
            try{
                processor.orgMembers(req, resp);
            }catch (Exception e){
                logger.error("组织机构成员列表异常",e);
            }
            logger.info("组织机构列表成员 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }
    }
}
