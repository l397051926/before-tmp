package com.gennlife.platform.processor;

import com.gennlife.platform.model.User;
import com.gennlife.platform.util.ChineseToEnglish;
import com.gennlife.platform.util.FileUploadUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.UnsupportedCharsetException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chen-song on 16/9/15.
 */
public class CommonProcessor {
    private static Logger logger = LoggerFactory.getLogger(CommonProcessor.class);
    private static View view = new View();
    public static CommonProcessor commonProcessor = new CommonProcessor();

    private CommonProcessor() {
    }

    public static CommonProcessor getCommonProcessor() {
        return commonProcessor;
    }

    public void downLoadFile(String pathfile, HttpServletResponse response, String fileName) {
        try {
            // fileName = URLEncoder.encode(fileName, "utf-8"); // 可以解决下载后文件名中文乱码问题
            fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");
            response.reset();
            response.setContentType("application/msexcel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName); // Content-disposition 告诉浏览器以下载的形式打开
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
            logger.error("", e);
            view.viewString(ParamUtils.errorParam("目前无模版"), response);
        } catch (IOException e) {
            logger.error("", e);
            view.viewString(ParamUtils.errorParam("发生异常"), response);
        }
    }
    //通过文件创建用户 切割每行 存到list
    public String uploadFileForImportStaff(MultipartFile file, User user) {
        try {
            byte[] bytes = file.getBytes();
            String string = new String(bytes, "GBK");
            if (ChineseToEnglish.isMessyCode(string)) return ParamUtils.errorParam("文件里含有非GBK编码的字符");
            logger.info("uploadFileForImportStaff=\n" + string);
            String[] strings = string.split("\n");
            List<String> list = new LinkedList();
            for (String line : strings) {
                list.add(line.trim().replace("\r", ""));
            }
            return FileUploadUtil.handleStaff(list, user);
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
    }


    public String uploadFileForImportLab(MultipartFile file, User user) {
        try {
            logger.info("*-------------开始切割文件");

            byte[] bytes = file.getBytes();
            String string = new String(bytes, "GBK");
            //logger.info("GBK "+string);
            //logger.info("default "+new String(bytes));
            if (ChineseToEnglish.isMessyCode(string)) {
                return ParamUtils.errorParam("文件里含有非GBK编码的字符");
            }
            logger.info("uploadFileForImportLab=" + string);
            String[] strings = string.split("\n");
            List<String> list = new LinkedList();
            for (String line : strings) {
                list.add(line);
            }
            return FileUploadUtil.handleLab(list, user);
        } catch (UnsupportedCharsetException e) {
            return ParamUtils.errorParam("文件含有无法识别的字符,需要GBK编码");
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
    }
}
