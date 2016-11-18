package com.gennlife.platform.dao;

import com.gennlife.platform.model.Group;
import com.gennlife.platform.model.User;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by chen-song on 2016/11/17.
 */
@Mapper("groupDao")
public interface GroupMapper {
    /**
     * 搜索小组，带分页
     * @param conf
     * @return
     */
    List<Group> getGroupsBySearchName(Map<String, Object> conf);
    int getGroupsBySearchNameCounter(Map<String, Object> conf);
    /**
     * 通过组id 获取小组成员
     * @param map
     * @return
     */
    List<User> getUsersByGroupID(Map<String, Object> map);


    int insertOneGroup(Group group);

    int insertOneGroupRelationUid(Map<String, Object> map);

    int updateOneGroup(Group group);

    int deleteGroupRelationUid(@Param("groupID") String groupID);

    List<User> getUsersBySearchNameGroupID(Map<String, Object> conf);

    int getUsersBySearchNameGroupIDCounter(Map<String, Object> conf);

    List<Group> getGroupsByName(Map<String, Object> map);

    List<Group> getGroupsByUid(Map<String, Object> confMap);
}
