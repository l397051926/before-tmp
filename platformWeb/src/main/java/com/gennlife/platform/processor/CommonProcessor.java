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

    public void downLoadFile(String pathfile ,HttpServletResponse response,String fileName){
        try {
            fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");
            /*
            DataInputStream in = new DataInputStream(new FileInputStream(new File(pathfile)));
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String temp = null;
            StringBuffer stringBuffer = new StringBuffer();
            while((temp=br.readLine()) != null){
                stringBuffer.append(temp).append("\n");
            }
            */
            response.reset();
            response.setContentType("application/msexcel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pathfile));
            BufferedOutputStream os = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                os.write(buff, 0, bytesRead);

            }
            os.flush();
            os.close();
            bis.close();
        } catch (FileNotFoundException e) {
            logger.error("",e);
            view.viewString(ParamUtils.errorParam("目前无模版"),response);
        } catch (IOException e) {
            logger.error("",e);
            view.viewString(ParamUtils.errorParam("发生异常"),response);
        }
    }
}
