package com.gennlife.platform.dao;

import com.gennlife.platform.bean.OrgListBean;
import com.gennlife.platform.bean.OrgMemberBean;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;

/**
 * Created by chensong on 2015/12/9.
 */
@Mapper("orgDao")
public interface OrgMapper {
    public List<OrgListBean> getOrgList();
    public List<OrgMemberBean>  getOneOrgList(String orgID);
}
