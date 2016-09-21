package com.gennlife.platform.dao;


import com.gennlife.platform.model.Resource;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 通过
     * @param roleid
     * @param offset
     * @param limit
     * @return
     */
    List<Resource> getResourceByRoleID(@Param("orgID")String orgID,@Param("roleid")Integer roleid, @Param("offset")int offset, @Param("limit")int limit);

    /**
     * 插入角色资源关联关系
     * @param resourceObj
     * @return
     */
    int insertRoleResourceRelation(Resource resourceObj);

    int findRoleResourceRelationCounter(@Param("sid") String sid, @Param("roleid")Integer roleid);
}
