package com.gennlife.platform.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by chen-song on 2016/11/17.
 */
public class Group {
    private String orgID;
    private String groupName;
    private String groupDesc;
    private String groupCreator;
    private String groupCreatTime;
    private String groupCreatName;

    private String has_search;
    private String has_searchExport;
    private String has_traceCRF;
    private String has_addCRF;
    private String has_editCRF;
    private String has_deleteCRF;
    private String has_browseDetail;
    private String has_addBatchCRF;
    //新增 crf 搜索 导入权限
    private String has_searchCRF;
    private String has_importCRF;
    @SerializedName("groupID")
    private String gid;
    private Object members;


    public String getHas_searchCRF() {
        return has_searchCRF;
    }

    public void setHas_searchCRF(String has_searchCRF) {
        this.has_searchCRF = has_searchCRF;
    }

    public String getHas_importCRF() {
        return has_importCRF;
    }

    public void setHas_importCRF(String has_importCRF) {
        this.has_importCRF = has_importCRF;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGroupCreatName() {
        return groupCreatName;
    }

    public void setGroupCreatName(String groupCreatName) {
        this.groupCreatName = groupCreatName;
    }

    public String getGroupCreator() {
        return groupCreator;
    }

    public void setGroupCreator(String groupCreator) {
        this.groupCreator = groupCreator;
    }

    public String getGroupCreatTime() {
        return groupCreatTime;
    }

    public void setGroupCreatTime(String groupCreatTime) {
        this.groupCreatTime = groupCreatTime;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getHas_search() {
        return has_search;
    }

    public void setHas_search(String has_search) {
        this.has_search = has_search;
    }

    public String getHas_searchExport() {
        return has_searchExport;
    }

    public void setHas_searchExport(String has_searchExport) {
        this.has_searchExport = has_searchExport;
    }

    public String getHas_traceCRF() {
        return has_traceCRF;
    }

    public void setHas_traceCRF(String has_traceCRF) {
        this.has_traceCRF = has_traceCRF;
    }

    public String getHas_addCRF() {
        return has_addCRF;
    }

    public void setHas_addCRF(String has_addCRF) {
        this.has_addCRF = has_addCRF;
    }

    public String getHas_editCRF() {
        return has_editCRF;
    }

    public void setHas_editCRF(String has_editCRF) {
        this.has_editCRF = has_editCRF;
    }

    public String getHas_deleteCRF() {
        return has_deleteCRF;
    }

    public void setHas_deleteCRF(String has_deleteCRF) {
        this.has_deleteCRF = has_deleteCRF;
    }

    public String getHas_browseDetail() {
        return has_browseDetail;
    }

    public void setHas_browseDetail(String has_browseDetail) {
        this.has_browseDetail = has_browseDetail;
    }

    public String getHas_addBatchCRF() {
        return has_addBatchCRF;
    }

    public void setHas_addBatchCRF(String has_addBatchCRF) {
        this.has_addBatchCRF = has_addBatchCRF;
    }

    public Object getMembers() {
        return members;
    }

    public void setMembers(Object members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        Group group = (Group) o;
        return Objects.equals(has_search, group.has_search) &&
            Objects.equals(has_searchExport, group.has_searchExport) &&
            Objects.equals(has_traceCRF, group.has_traceCRF) &&
            Objects.equals(has_addCRF, group.has_addCRF) &&
            Objects.equals(has_editCRF, group.has_editCRF) &&
            Objects.equals(has_deleteCRF, group.has_deleteCRF) &&
            Objects.equals(has_browseDetail, group.has_browseDetail) &&
            Objects.equals(has_addBatchCRF, group.has_addBatchCRF) &&
            Objects.equals(has_searchCRF, group.has_searchCRF) &&
            Objects.equals(has_importCRF, group.has_importCRF);
    }

    @Override
    public int hashCode() {

        return Objects.hash(has_search, has_searchExport, has_traceCRF, has_addCRF, has_editCRF, has_deleteCRF, has_browseDetail, has_addBatchCRF, has_searchCRF, has_importCRF);
    }
}
