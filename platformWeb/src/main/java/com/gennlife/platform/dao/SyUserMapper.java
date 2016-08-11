package com.gennlife.platform.dao;


import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.projectBean.HistoryWords;
import com.gennlife.platform.bean.projectBean.MyProjectList;
import com.gennlife.platform.bean.searchConditionBean.SearchConditionBean;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by chensong on 2015/12/4.
 */
@Mapper("syUserDao")
public interface SyUserMapper {
    public SyUser login(Map<String, Object> likeCondition);
    public SyUser getOneUser(Map<String, Object> likeCondition);
    public int updateByUid(SyUser record);
    public int getProjectCounter(Map<String,Object> likeCondition);
    public List<MyProjectList> getMyProjectList(Map<String,Object> likeCondition);
    public List<String> getHistoryWords(Map<String,Object> likeCondition);
    public Integer getHistoryWordsCounter(Map<String,Object> likeCondition);
    public Integer insertHistoryWords(HistoryWords historyWords);
    public Integer updateHistoryWordsCounter(Map<String,Object> likeCondition);

    /**
     * 项目成员列表
     * @param likeCondition
     * @return
     */
    public List<SyUser> getProjectMemberList(Map<String,Object> likeCondition);

    /**
     * 项目成员列表:请求参数只是projectID
     * @param likeCondition
     * @return
     */
    public List<SyUser> getProjectMemberSList(Map<String,Object> likeCondition);

    public int getProjectMemberCounter(Map<String,Object> likeCondition);

    /**
     * 用户在项目下搜索用户来增加成员
     * @param likeCondition
     * @return
     */
    public List<SyUser> searchMemberList(Map<String,Object> likeCondition);

    public int searchMemberCounter(Map<String,Object> likeCondition);

    /**
     * 项目详情
     * @param map
     * @return
     */
    public MyProjectList baiscInfo(Map<String, Object> map);

    /**
     * 获取某个uid所有项目,信息
     * @param map
     * @return
     */
    public List<MyProjectList> getProjectList(Map<String, Object> map);

    /**
     * 保存高级检索公式
     * @param searchConditionBean
     */
    void insertSearchCondition(SearchConditionBean searchConditionBean);

    /**
     * 获取保存的高级检索公式
     * @param uid
     * @return
     */
    List<SearchConditionBean> searchConditionList(String uid);
}
