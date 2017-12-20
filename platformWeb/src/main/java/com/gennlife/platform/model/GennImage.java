package com.gennlife.platform.model;

/**
 * Created by Chenjinfeng on 2017/12/15.
 */
public class GennImage {
    private String uniqueId;
    private String imgId;
    private transient String imgPath;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
