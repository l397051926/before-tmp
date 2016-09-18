package com.gennlife.platform.processor;

import com.gennlife.platform.model.User;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by chen-song on 16/9/15.
 */
public class CommonProcessor {
    private static Logger logger = LoggerFactory.getLogger(CommonProcessor.class);
    private static View view = new View();

    public void downLoadFile(String fileName ,HttpServletResponse response){
        try {
            File file = new File(fileName);
            InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"),"utf-8"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            os.write(buffer);// 输出文件
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            logger.error("",e);
            view.viewString(ParamUtils.errorParam("目前无导入记录"),response);
        } catch (IOException e) {
            logger.error("",e);
            view.viewString(ParamUtils.errorParam("发生异常"),response);
        }
    }
}
