package com.gennlife.platform.dao;


import com.gennlife.platform.bean.SyRole;
import com.gennlife.platform.model.Role;
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



}
