package com.gennlife.platform.dao;


import com.gennlife.platform.model.CRFLab;
import com.gennlife.platform.model.LabResource;
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



    public List<Resource> getResourcesBySid(@Param("orgID")String orgID,@Param("sid") String sid,@Param("roleid")Integer roleid);

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


    /**
     * 添加资源
     * @param labResource
     * @return
     */
    int insertOneResource(LabResource labResource);

    /**
     * 删除资源
     * @param sids
     * @return
     */
    public int deleteLabsReource(@Param("sids") String[] sids);

    /**
     * 获取科室资源
     * @param orgID
     * @return
     */
    List<LabResource> getLabResourcesByOrgID(@Param("orgID")String orgID,@Param("stype")String type);

    /**
     * 获取科室资源计数
     * @param orgID
     * @param roleid
     * @return
     */
    int getResourceByRoleIDCounter(@Param("orgID")String orgID,@Param("roleid")Integer roleid);

    /**
     * 获取科室对应的crf_id
     * @param labID
     * @return
     */
    CRFLab getCrfIDByLab(@Param("labID")String labID, @Param("orgID")String orgID);


    List<CRFLab> getCrfIDListByLab(@Param("labIDs") String[] labIDs, @Param("orgID")String orgID);
    int updateResource(LabResource labResource);
}
