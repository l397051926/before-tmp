package com.gennlife.platform.dao;

import com.gennlife.platform.bean.OrgListBean;
import com.gennlife.platform.bean.OrgMemberBean;
import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.Organization;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by chensong on 2015/12/9.
 */
@Mapper("orgDao")
public interface OrgMapper {
    /**
     * 所有组织的信息
     * @return
     */
    public List<OrgListBean> getOrgList();

    /**
     * 返回某个组织的所有成员
     * @param orgID
     * @return
     */
    public List<OrgMemberBean>  getOneOrgList(String orgID);

    /**
     * 返回某个医院的所有科室信息
     * @param orgID
     * @return
     */
    public List<Lab> getLabs(String orgID);

    /**
     * 获取组织信息
     * @param orgID
     * @return
     */
    public Organization getOrganization(String orgID);

    /**
     * 获取某个组织的最大深度
     * @param orgID
     * @return
     */
    public Integer getMaxlabLevel(String orgID);

    /**
     * 获取某个科室的level
     * @param map
     * @return
     */
    public Integer getLabLevel(Map<String,Object> map);

    /**
     * 插入一个科室信息
     * @param lab
     * @return
     */
    public int insertOneLab(Lab lab);

    /**
     * 获取这个组织所有的名称
     * @param orgID
     * @return
     */
    public List<String> getLabsName(String orgID);

    /**
     * 删除科室信息
     * @param labIDs
     * @return
     */
    public int deleteLabs(@Param("labIDs") String[] labIDs);

    /**
     * 通过labID获取科室信息
     * @param labID
     * @return
     */
    public Lab getLabBylabID(@Param("labID") String labID);

    /**
     * 更新科室信息
     * @param map
     * @return
     */
    public Integer updateLabInfo(Map<String, Object> map);


    /**
     * 通过labID获取科室信息
     * @param labName
     * @param orgID
     * @return
     */
    public Lab getLabBylabName(@Param("lab_name") String labName,@Param("orgID") String orgID);

    /**
     * 通过parentID获取科室信息
     * @param orgID
     * @param lab_parent
     * @return
     */
    public List<Lab> getLabsByparentID(@Param("orgID") String orgID,@Param("lab_parent") String lab_parent);

    /**
     * 以名称更新更新
     * @param lab
     * @return
     */
    public int updateLabInfoByNameWithLab(Lab lab);

    /**
     * 职称列表
     * @param orgID
     * @return
     */
    List<String> getProfessionList(@Param("orgID")String orgID);

    /**
    * 获取科室的上级信息
    * */
    Lab getLabPInfo(@Param("labID") String labID,@Param("orgID") String orgID);

    public List<String> getSubLabs(@Param("labID") String labID,@Param("orgID") String orgID);
    int updateSubLabPid(@Param("labIDs") String[] labIDs,@Param("orgID") String orgID,@Param("pid") String pid);
    }
