package com.gennlife.platform.processor;

import com.gennlife.platform.model.User;
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

    public void downLoadFile(String pathfile ,HttpServletResponse response,String fileName){
        try {
            fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");
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

    public String uploadFileForImportStaff(MultipartFile file,User user) {
        try{
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            String string = new String(bytes,"GBK");
            if(!string.equals(new String(bytes))) return ParamUtils.errorParam("文件里含有非GBK编码的字符");
            logger.info("uploadFileForImportStaff="+string);
            String[] strings = string.split("\n");
            List<String> list = new LinkedList();
            for(String line:strings){
                list.add(line.trim().replace("\r",""));
            }
            return FileUploadUtil.handleStaff(list,user);
        }catch (Exception e){
            return ParamUtils.errorParam("出现异常");
        }
    }


    public String uploadFileForImportLab(MultipartFile file,User user) {
        try {
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            String string = new String(bytes, "GBK");
            if(!string.equals(new String(bytes))) return ParamUtils.errorParam("文件里含有非GBK编码的字符");
            logger.info("uploadFileForImportLab=" + string);
            String[] strings = string.split("\n");
            List<String> list = new LinkedList();
            for (String line : strings) {
                list.add(line);
            }
            return FileUploadUtil.handleLab(list, user);
        }
        catch (UnsupportedCharsetException e)
        {
            return ParamUtils.errorParam("文件含有无法识别的字符,需要GBK编码");
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("出现异常");
        }
    }
}
