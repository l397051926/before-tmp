package com.gennlife.platform.controller;

import com.gennlife.platform.processor.CaseProcessor;
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
public class CaseController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(CaseController.class);
    private CaseProcessor processor = new CaseProcessor();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if ("/case/KnowledgeTermSuggest".equals(uri)) {//1
            try {
                processor.knowledgeTermSuggestion(req, resp);
            } catch (Exception e) {
                logger.error("知识库搜索关键词提示", e);
            }
            logger.info("知识库搜索关键词提示 耗时:" + (System.currentTimeMillis() - start) + "ms");
        }else if("/case/SearchItemSet".equals(uri)){//2
            try {
                processor.searchItemSet(req, resp);
            } catch (Exception e) {
                logger.error("搜索结果列表展示的集合", e);
            }
            logger.info("搜索结果列表展示的集合 耗时:" + (System.currentTimeMillis() - start) + "ms");
        }else if("/case/SearchTermSuggest".equals(uri)){//3
            try {
                processor.searchTermSuggest(req, resp);
            } catch (Exception e) {
                logger.error("搜索关键词提示", e);
            }
            logger.info("搜索关键词提示 耗时:" + (System.currentTimeMillis() - start) + "ms");
        }else if("/case/AdvancedSearchTermSuggest".equals(uri)){//4
            try {
                processor.advancedSearchTermSuggest(req, resp);
            } catch (Exception e) {
                logger.error("高级搜索关键词提示", e);
            }
            logger.info("高级搜索关键词提示 耗时:" + (System.currentTimeMillis() - start) + "ms");
        }else if("/case/SearchKnowledge".equals(uri)){//5
            try {
                processor.searchKnowledge(req, resp);
            } catch (Exception e) {
                logger.error("首页知识库搜索", e);
            }
            logger.info("首页知识库搜索 耗时:" + (System.currentTimeMillis() - start) + "ms");
        }else if("/case/SearchCase".equals(uri)){//6
            try {
                processor.searchCase(req, resp);
            } catch (Exception e) {
                logger.error("首页知识库搜索", e);
            }
            logger.info("首页知识库搜索 耗时:" + (System.currentTimeMillis() - start) + "ms");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
