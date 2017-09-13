package com.gennlife.platform.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by chen-song on 16/10/1.
 */
@Component("FileLocation")
@ConfigurationProperties(prefix = "ui.FileLocation")
public class FileBean implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(FileBean.class);
    /**
     * 科室,人员管理文件导入缓存位置
     */
    private String manageFileLocation = null;
    /**
     * CRF 文件导入的缓存位置
     */
    private String CRFFileLocation = null;
    private String sessionLocation;


    public String getManageFileLocation() {
        return manageFileLocation;
    }

    public void setManageFileLocation(String manageFileLocation) {
        this.manageFileLocation = manageFileLocation.trim();
        mkdir(this.manageFileLocation);
    }

    public String getCRFFileLocation() {
        return CRFFileLocation;
    }

    public void setCRFFileLocation(String CRFFileLocation) {
        this.CRFFileLocation = CRFFileLocation.trim();
        mkdir(this.CRFFileLocation);
    }

    private static void mkdir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                logger.info("创建目录" + path + "成功");
            } else logger.error("创建目录" + path + "失败");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String updateFiles = this.getClass().getClassLoader().getResource("update").getFile();
        logger.info("copy from " + updateFiles + " to " + manageFileLocation);
        copy(updateFiles, manageFileLocation);
    }

    private static void copy(String src, String des) {
        File file1 = new File(src);
        File[] fs = file1.listFiles();
        File file2 = new File(des);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        for (File f : fs) {
            if (f.isFile()) {
                fileCopy(f.getPath(), des + "\\" + f.getName()); //调用文件拷贝的方法
            } else if (f.isDirectory()) {
                copy(f.getPath(), des + "\\" + f.getName());
            }
        }

    }

    /**
     * 文件拷贝的方法
     */
    private static void fileCopy(String src, String des) {

        BufferedReader br = null;
        PrintStream ps = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
            ps = new PrintStream(new FileOutputStream(des));
            String s = null;
            while ((s = br.readLine()) != null) {
                ps.println(s);
                ps.flush();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block  
            logger.error("", e);
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            logger.error("", e);
        } finally {

            try {
                if (br != null) br.close();
                if (ps != null) ps.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block  
                logger.error("", e);
            }

        }


    }

}
