package com.gennlife.platform.configuration;

/**
 * Created by chen-song on 16/10/1.
 */
public class FileBean {
    /**
     * 科室,人员管理文件导入缓存位置
     */
    private String manageFileLocation = null;
    /**
     * CRF 文件导入的缓存位置
     */
    private String CRFFileLocation = null;


    public String getManageFileLocation() {
        return manageFileLocation;
    }

    public void setManageFileLocation(String manageFileLocation) {
        this.manageFileLocation = manageFileLocation;
    }

    public String getCRFFileLocation() {
        return CRFFileLocation;
    }

    public void setCRFFileLocation(String CRFFileLocation) {
        this.CRFFileLocation = CRFFileLocation;
    }
}
