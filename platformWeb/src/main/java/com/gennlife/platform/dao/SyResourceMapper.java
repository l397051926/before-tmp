package com.gennlife.platform.dao;


import com.gennlife.platform.bean.SyResource;
import com.gennlife.platform.model.Resource;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;
import java.util.Map;


/**
 * Created by chensong on 2015/12/4.
 */
@Mapper("syResourceDao")
public interface SyResourceMapper {
    /**
     * 获取资源
     * @return
     */
    public List<Resource> getResources(Map<String, Object> likeCondition);



    public List<Resource> getResourcesBySid(Map<String, Object> likeCondition);

}
