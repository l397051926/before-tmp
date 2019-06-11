package com.gennlife.platform;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v26.message.MFN_M05;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.junit.Test;

public class X {

    @Test
    public void main() throws HL7Exception {
        final String input = "MSH|^~\\&|HRP|010101^0101|HSB|010101^0101|2019-06-04||MFN^M05^MFN_M05|MSG00001|P|2.6|||AL|AL||UNICODE UTF-8|||DS002^0101\n" +
            "MFI|DS002^科室字典^HSB||REP|2019-06-04||AL\n" +
            "MFE|U|1001UD10000000001WS0|2019-06-04|ZB11010101^院前急救||2014-12-09|tzy^^台志有\n" +
            "LOC||临床科室|01|郑州市中心医院^^^^^^^^^010101\n" +
            "LRL|ZB110101";
        final HapiContext context = new DefaultHapiContext();
        context.setValidationContext(ValidationContextFactory.noValidation());
        final Parser parser = new PipeParser(context);
        final MFN_M05 m = (MFN_M05)parser.parse(input);
        System.out.println(m.getMSH().getDateTimeOfMessage());
        System.out.println(m.getMFI().getEnteredDateTime());
        System.out.println(m.getMF_LOCATION().getLOC().getLoc4_OrganizationNameLOC(0).getOrganizationName().getValue());
    }

}
