package com.gennlife.platform.controller;

import com.gennlife.platform.model.User;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/sync")
public class SyncController {

    private static Logger _LOG = LoggerFactory.getLogger(SyncController.class);

    @RequestMapping(value = "/ImportStaffsFromExcel", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody String ImportStaffsFromExcel(HttpServletRequest request, @RequestParam("name") MultipartFile file) {
        try {
            final User user = (User)request.getAttribute("currentUser");
            return processor.importStaffsFromExcel(file, user);
        } catch (Exception e) {
            _LOG.error(e.getLocalizedMessage(), e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    @RequestMapping(value = "/ImportLabsFromExcel", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody String ImportLabsFromExcel(HttpServletRequest request, @RequestParam("name") MultipartFile file) {
        try {
            final User user = (User)request.getAttribute("currentUser");
            return processor.importLabsFromExcel(file, user);
        } catch (Exception e) {
            _LOG.error(e.getLocalizedMessage(), e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    @Autowired
    private SyncProcessor processor;

}
