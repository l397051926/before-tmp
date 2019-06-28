package com.gennlife.platform.controller;

import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CommonProcessor;
import com.gennlife.platform.processor.SyncProcessor;
import com.gennlife.platform.util.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/sync")
public class SyncController {

    @RequestMapping(value = "/DownloadLabsExcelTemplate", method = GET, produces = APPLICATION_PDF_VALUE)
    public void DownloadLabsExcelTemplate(HttpServletRequest paramRe, HttpServletResponse response) {
        final String file = DIRECTORY + "组织导入模版.xlsx";
        commonProcessor.downLoadFile(file, response, "组织导入模版.xlsx",true);
    }

    @RequestMapping(value = "/DownloadStaffsExcelTemplate", method = GET, produces = APPLICATION_PDF_VALUE)
    public void DownloadStaffsExcelTemplate(HttpServletRequest paramRe, HttpServletResponse response) {
        final String file = DIRECTORY + "人员导入模版.xlsx";
        commonProcessor.downLoadFile(file, response, "人员导入模版.xlsx",true);
    }

    private static Logger _LOG = LoggerFactory.getLogger(SyncController.class);
    @RequestMapping(value = "/ImportStaffsFromExcel", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody String ImportStaffsFromExcel(HttpServletRequest request, @RequestParam("name") MultipartFile file) {
        try {
            final User user = (User)request.getAttribute("currentUser");
            return syncProcessor.importStaffsFromExcel(file, user);
        } catch (Exception e) {
            _LOG.error(e.getLocalizedMessage(), e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    @RequestMapping(value = "/ImportLabsFromExcel", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody String ImportLabsFromExcel(HttpServletRequest request, @RequestParam("name") MultipartFile file) {
        try {
            final User user = (User)request.getAttribute("currentUser");
            return syncProcessor.importLabsFromExcel(file, user);
        } catch (Exception e) {
            _LOG.error(e.getLocalizedMessage(), e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    private static String DIRECTORY = "/home/tomcat_demo2_web/update/";//默认位置

    @Autowired
    private SyncProcessor syncProcessor;
    @Autowired
    private CommonProcessor commonProcessor;

}
