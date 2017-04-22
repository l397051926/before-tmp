package com.gennlife.platform.dao;


import com.gennlife.platform.bean.searchConditionBean.SearchConditionBean;
import com.gennlife.platform.model.Admin;
import com.gennlife.platform.model.User;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by chensong on 2015/12/4.
 */
@Mapper("syUserDao")
public interface SyUserMapper {
    /**
     * 获取一个用户的信息
     * @param likeCondition
     * @return
     */
    public User getUser(Map<String, Object> likeCondition);

    /**
     * 通过email获取uid
     *
     */
    public String getUidByEmail(Map<String, Object> likeCondition);

    /**
     * 更新用户基本信息
     * @param user
     */
    public int updateByUid(User user);

    /**
     * 通过uid获取当前用户的数据
     * @param uid
     * @return
     */
    public User getUserByUid(@Param("uid") String uid);

    /**
     * 更新密码
     * @param map
     * @return
     */
    public int updatePWD(Map<String, Object> map);

    /**
     * 发送邮件前,更新md5值
     * @param map
     * @return
     */
    int updateMd5(Map<String, Object> map);

    /**
     * 查看email是否存在
     * @param email
     * @return
     */
    int existEmail(@Param("email") String email);

    /**
     * 获取用户的管理权限
     * @param confMap
     * @return
     */
    List<Admin> getAdmins(Map<String, Object> confMap);

    /**
     * 按照科室搜索
     * @param key
     * @param offset
     * @param limit
     * @param labIDs
     * @return
     */
    List<User> searchUsersByLabIDs(@Param("skey") String key, @Param("offset")int offset,@Param("limit")int limit,@Param("labIDs") String[] labIDs);


    /**
     * 按照医院id搜索
     * @param key
     * @param offset
     * @param limit
     * @param orgID
     * @return
     */
    List<User> searchUsersByOrgID(@Param("skey") String key, @Param("offset")int offset,@Param("limit")int limit,@Param("orgID") String orgID);

    /**
     * 按照科室搜索计数
     * @param key
     * @param labIDs
     * @return
     */
    Long searchUsersByLabIDsCounter(@Param("skey")String key, @Param("labIDs")String[] labIDs);
    /**
     * 按照医院id搜索计数
     * @param key
     * @param orgID
     * @return
     */
    Long searchUsersByOrgIDCounter(@Param("skey")String key, @Param("orgID")String orgID);

    /**
     * 获取保存的高级检索公式
     * @param uid
     * @return
     */
    List<SearchConditionBean> searchConditionList(String uid);

    /**
     * 保存高级检索公式
     * @param searchConditionBean
     */
    int insertSearchCondition(SearchConditionBean searchConditionBean);

    /**
     * 更新高级检索公式
     * @param searchConditionBean
     */
    int updateSearchCondition(SearchConditionBean searchConditionBean);

    /**
     *
     * @return
     */
    List<SearchConditionBean> findSearchConditionByName(@Param("uid")String uid,@Param("conditionName")String conditionName);

    /**
     * 增加一个用户
     * @param adduser
     * @return
     */
    int insertOneUser(User adduser);

    /**
     * 批量删除用户
     * @param uids
     * @return
     */
    int deleteUserByUids(@Param("uids") String[] uids);

    /**
     * 获取用户,通过工号
     * @param unumber
     * @param orgID
     * @return
     */
    User getUserByUnumber(@Param("unumber")String unumber,@Param("orgID") String orgID);

    /**
     * 以工号为为条件更新用户信息
     * @param addUser
     * @return
     */
    int updateUserByUnumber(User addUser);

    /**
     * 通过邮箱获取用户
     * @param email
     * @return
     */
    User getUserByEmail(@Param("email") String email);

    /**
     * 通过角色id 获取相关用户的列表
     * @param roleid
     * @param offset
     * @param limit
     * @return
     */
    List<User> getUserByRoleID(@Param("roleid")Integer roleid, @Param("offset")int offset, @Param("limit")int limit);

    List<String> getAllUserIDByRoleID(@Param("roleids") Integer[] roleids);

    List<User> searchUsersByOrgIDNoLimit(@Param("skey") String key,@Param("orgID") String orgID);;

    List<User> searchUsersByLabIDsNoLimit(@Param("skey") String key,@Param("labIDs") String[] labIDs);

    /**
     * 通过角色id 获取相关用户 计数
     * @param roleid
     * @return
     */
    int getUserByRoleIDCounter(Integer roleid);

    int deleteSearchCondition(@Param("conditionIDs") Integer[] conditionIDs);

    /**
     * 更新科室的名称后，将科室下成员的个人信息，都更新了
     * @param lab_name
     * @param orgID
     * @return
     */
    int updateUserLabNameByLabName(@Param("lab_name") String lab_name,@Param("old_name") String old_name, @Param("orgID") String orgID);

    /**
     * 通过
     * @param labID
     * @param orgID
     * @return
     */
    List<User> getUserByLabID(@Param("labID") String labID, @Param("orgID") String orgID);

    List<String> getUserIDByLabID(@Param("labID") String labID, @Param("orgID") String orgID);
    List<String> getUserIDsByLabID(@Param("labIDs") String[] labIDs, @Param("orgID") String orgID);
    /**
     * 获取组织内管理员
     * @param orgID
     * @return
     */
    List<String> getAdminsByOrgID(@Param("orgID") String orgID);


    /**
     * 删除用户在vitaboard的配置
     */
    int deleteVitaCong(@Param("uid") String uid);

    /**
     * 插入vitaboard的配置
     */
    int insertVitaCong(@Param("uid") String uid,@Param("data") String data);

    /**
     * 获取用户vitaboard的配置
     * @param uid
     * @return
     */
    String getVitaCong(@Param("uid") String uid);

    List<String> getAllUserIDByGroupID(@Param("gid") String groupID);

    int  checkUnumber(@Param("unumber") String unumber,@Param("uid") String uid);


    /**
    public SyUser getOneUser(Map<String, Object> likeCondition);
    public int updateByUid(SyUser record);
    public int getProjectCounter(Map<String,Object> likeCondition);

     */

    /**
     * 项目成员列表:请求参数只是projectID
     * @param likeCondition
     * @return

    public List<SyUser> getProjectMemberSList(Map<String,Object> likeCondition);
     */
    //

    /**
     * 用户在项目下搜索用户来增加成员
     * @param likeCondition
     * @return

    public List<SyUser> searchMemberList(Map<String,Object> likeCondition);


     */
    /**
     * 项目详情
     * @param map
     * @return

    public MyProjectList baiscInfo(Map<String, Object> map);
     */

    public List<String> getUserIdByLabID( @Param("orgID")String orgID, @Param("labID")String labID);

    int updateUseInfoWhenDelLab(@Param("labPID") String labPID,@Param("lab_pname") String lab_pname,
                                @Param("labID") String labID,@Param("orgID") String orgID);
    int updateUsersWhenDelLab(@Param("labPID") String labPID,@Param("lab_pname") String lab_pname,
                              @Param("labIDs") String[] labIDs,@Param("orgID") String orgID);


}
