package com.gennlife.platform.dao;


import com.gennlife.platform.model.DepartmentMap;
import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.Role;
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
     *
     * @return
     */
    public List<Role> getRoles(Map<String, Object> likeCondition);

    /**
     * 获取这个组织的科室成员角色
     *
     * @param orgID
     * @return
     */
    Role getLabMember(@Param("orgID") String orgID);

    /**
     * 插入一个关联
     *
     * @param roleid
     * @param uid
     * @return
     */
    Integer insertUserRoleRelation(@Param("roleid") int roleid, @Param("uid") String uid);

    /**
     * 删除关联关系
     *
     * @param uids
     * @return
     */
    int deleteByUids(@Param("uids") String[] uids);

    /**
     * 搜索角色
     *
     * @param key
     * @param offset
     * @param limit
     * @param orgID
     * @return
     */
    List<Role> searchRoles(@Param("skey") String key, @Param("offset") int offset, @Param("limit") int limit, @Param("orgID") String orgID);

    /**
     * @param key
     * @param orgID
     * @return
     */
    int searchRolesCounter(@Param("skey") String key, @Param("orgID") String orgID);

    /**
     * 删除角色与用户的关联关系,参数为角色id
     *
     * @param roleids
     * @return
     */
    int deleteRelationsByRoleids(@Param("roleids") Integer[] roleids);

    /**
     * 通过roleid获取角色
     *
     * @param roleid
     * @return
     */
    Role getRoleByroleid(@Param("roleid") Integer roleid);

    /**
     * 删除角色与资源的关联关系,参数是roleids
     *
     * @param roleids
     * @return
     */
    int deleteRelationsWithReourcesByRoleids(@Param("roleids") Integer[] roleids);

    /**
     * 删除角色,参数roleids
     *
     * @param roleids
     * @return
     */
    int deleteRolesByRoleids(@Param("roleids") Integer[] roleids);

    /**
     * 通过角色名称获取角色
     *
     * @param orgID
     * @param role
     * @return
     */
    Role getRoleByRoleName(@Param("orgID") String orgID, @Param("role") String role);

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    int insertUserRole(Role role);

    /**
     * 查找用户角色关系
     *
     * @param roleid
     * @param uid
     * @return
     */
    int findUserRoleRelationCounter(@Param("roleid") int roleid, @Param("uid") String uid);

    /**
     * 更新角色信息
     *
     * @param role
     * @return
     */
    int updateUserRole(Role role);

    List<DepartmentMap> getSlabNames();
    List<Lab> getLabMAP();

    List<String> getSlabNameMappingByLabName(@Param("lab_name") String lab_name, @Param("orgID") String orgID);
    List<String> getlabMappingByLabName(@Param("lab_name") String lab_name, @Param("orgID") String orgID);

    List<String> getUserIdByRole(@Param("roleid") int roleid);

    /**
     * 根据 role 获取 角色数据
     * @param
     * @return
     */
    Role getRoleByRole(@Param("role") String role);

    /**
     * 获取全部角色
     * @return
     */
    List<Role> getAllRoles();
}
