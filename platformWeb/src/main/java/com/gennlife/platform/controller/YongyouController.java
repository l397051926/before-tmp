package com.gennlife.platform.controller;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.MFN_M02;
import ca.uhn.hl7v2.model.v26.message.MFN_M05;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import com.gennlife.platform.processor.YongyouProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static ca.uhn.hl7v2.validation.impl.ValidationContextFactory.noValidation;
import static com.gennlife.darren.controlflow.exception.Force.force;

@Controller
@RequestMapping("/Yongyou")
public class YongyouController {

    private static Logger _LOG = LoggerFactory.getLogger(YongyouController.class);
    private static String _RET = "{}";

    @RequestMapping(value = "/DS001", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String DS001(@RequestBody String msgStr) {
        try {
            final Message msg = _parseRequest(msgStr);
            _processor.ds001((MFN_M02)msg);
        } catch (Exception e) {
            _LOG.error(e.getLocalizedMessage(), e);
        }
        return _RET;
    }

    @RequestMapping(value = "/DS002", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String DS002(@RequestBody String msgStr) {
        try {
            final Message msg = _parseRequest(msgStr);
            _processor.ds002((MFN_M05)msg);
        } catch (Exception e) {
            _LOG.error(e.getLocalizedMessage(), e);
        }
        return _RET;
    }

    private static Message _parseRequest(String msgStr) throws HL7Exception {
        final Parser parser = new PipeParser(_HAPI_CONTEXT);
        return parser.parse(msgStr.replaceAll("[\r\n]+", "\r"));
    }

    private static final HapiContext _HAPI_CONTEXT = force(() -> {
        final HapiContext ret = new DefaultHapiContext();
        ret.setValidationContext(noValidation());
        return ret;
    });

    @Autowired
    private YongyouProcessor _processor;

}
