package com.gennlife.platform.dao;


import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.projectBean.HistoryWords;
import com.gennlife.platform.bean.projectBean.MyProjectList;
import com.gennlife.platform.bean.searchConditionBean.SearchConditionBean;
import com.gennlife.platform.model.User;
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
     * @param map
     * @return
     */
    public User getUserByUid(Map<String, Object> map);

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
    public SyUser getOneUser(Map<String, Object> likeCondition);
    public int updateByUid(SyUser record);
    public int getProjectCounter(Map<String,Object> likeCondition);
    public List<MyProjectList> getMyProjectList(Map<String,Object> likeCondition);
     */
     /**
     * 项目成员列表
     * @param likeCondition
     * @return

    public List<SyUser> getProjectMemberList(Map<String,Object> likeCondition);
      */
    /**
     * 项目成员列表:请求参数只是projectID
     * @param likeCondition
     * @return

    public List<SyUser> getProjectMemberSList(Map<String,Object> likeCondition);
     */
    //public int getProjectMemberCounter(Map<String,Object> likeCondition);

    /**
     * 用户在项目下搜索用户来增加成员
     * @param likeCondition
     * @return

    public List<SyUser> searchMemberList(Map<String,Object> likeCondition);

    public int searchMemberCounter(Map<String,Object> likeCondition);
     */
    /**
     * 项目详情
     * @param map
     * @return

    public MyProjectList baiscInfo(Map<String, Object> map);
     */
    /**
     * 获取某个uid所有项目,信息
     * @param map
     * @return

    public List<MyProjectList> getProjectList(Map<String, Object> map);
     */
    /**
     * 保存高级检索公式
     * @param searchConditionBean

    void insertSearchCondition(SearchConditionBean searchConditionBean);
     */
    /**
     * 获取保存的高级检索公式
     * @param uid
     * @return

    List<SearchConditionBean> searchConditionList(String uid);
     */

}
