package com.gennlife.platform.dao;


import com.gennlife.platform.model.Role;
import com.gennlife.platform.model.User;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by chensong on 2015/12/4.
 */
@Mapper("syRoleDao")
public interface SyRoleMapper {
    /**
     * 获取角色列表
     * @return
     */
    public List<Role> getRoles(Map<String, Object> likeCondition);

    /**
     *获取这个组织的科室成员角色
     * @param orgID
     * @return
     */
    Role getLabMember(@Param("orgID") String orgID);

    /**
     * 插入一个关联
     * @param roleid
     * @param uid
     * @return
     */
    Integer insertUserRoleRelation(@Param("roleid")int roleid, @Param("uid")String uid);

    /**
     * 删除关联关系
     * @param uids
     * @return
     */
    int deleteByUids(@Param("uids")String[] uids);
}
