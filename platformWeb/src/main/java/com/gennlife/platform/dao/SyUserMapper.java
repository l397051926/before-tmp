package com.gennlife.platform.dao;


import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.projectBean.*;
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
    public List<FinishedProject> getFinishedProjects(Map<String,Object> likeCondition);
    public List<UnfinishedProject> getUnfinishedProjects(Map<String,Object> likeCondition);
    public int getFinishedProjectCounter(Map<String,Object> likeCondition);
    public int getUnfinishedProjectCounter(Map<String,Object> likeCondition);
    public int getProjectCounter(Map<String,Object> likeCondition);
    public List<MyProjectList> getMyProjectListNoPstatus(Map<String,Object> likeCondition);
    public List<MyProjectList> getMyProjectList(Map<String,Object> likeCondition);
    public List<ProjectPstatus> getProjectPstatus(Map<String,Object> likeCondition);
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

}
