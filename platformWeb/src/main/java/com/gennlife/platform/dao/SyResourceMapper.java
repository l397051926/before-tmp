package com.gennlife.platform.dao;


import com.gennlife.platform.bean.SyResource;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;


/**
 * Created by chensong on 2015/12/4.
 */
@Mapper("syResourceDao")
public interface SyResourceMapper {
    public List<SyResource> getAllSyResourceByRole(Integer integer);
    public List<SyResource> getAllSyResource();
}
