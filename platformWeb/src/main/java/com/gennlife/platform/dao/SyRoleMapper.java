package com.gennlife.platform.dao;


import com.gennlife.platform.bean.SyRole;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;

/**
 * Created by chensong on 2015/12/4.
 */
@Mapper("syRoleDao")
public interface SyRoleMapper {
    public List<SyRole> getAllSyRoleByUser(Integer integer);

}
