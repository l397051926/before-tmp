package com.gennlife.platform.dao;

import com.gennlife.platform.bean.OrgListBean;
import com.gennlife.platform.bean.OrgMemberBean;
import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.Organization;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
}
