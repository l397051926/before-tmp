package com.gennlife.platform.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by chen-song on 16/10/1.
 */
public class FileBean {
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

}
