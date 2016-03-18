package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.SearchProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chensong on 2015/12/12.
 */
public class SearchController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(SearchController.class);
    private static SearchProcessor processor = new SearchProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if("/search/QRecommend".equals(uri)){
            try{
                processor.qRecommend(req,resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("搜索关键词提示 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/search/SearchKey".equals(uri)){
            try{
                processor.search(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("搜索关键词 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/search/ListFields".equals(uri)){
            try{
                processor.listFields(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info(" 属性列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/search/DetailSample".equals(uri)){
            try{
                processor.detailSample(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("样本详情页 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/search/RecommendFields".equals(uri)){
            try{
                processor.recommendFields(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("属性列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/search/SearchFields".equals(uri)){
            try{
                processor.searchFields(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("搜索属性列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/search/SearchMembers".equals(uri)){
            try{
                processor.searchMembers(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("搜索用户列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/search/StatV2".equals(uri)){
            try{
                processor.searchStatV2(req, resp);
            }catch (Exception e){
                logger.error("", e);
            }
            logger.info("二维属性统计 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }


    }
}
