package com.gennlife.platform.dao;

import com.gennlife.platform.bean.projectBean.*;
import com.gennlife.platform.model.User;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chensong on 2015/12/10.
 */
@Mapper("projectDao")
public interface ProjectMapper {
    public int insertCreateProject(CreateProject createProject);

    public int insertProUserList(List<ProUser> proUserList);

    public int insertProLog(ProLog proLog);

    public ArrayList<ProLog> getProjectLog(Map<String, Object> likeCondition);

    public int getProjectLogCounter(Map<String, Object> likeCondition);

    /**
     * 方案列表
     *
     * @param likeCondition
     * @return
     */
    public ArrayList<ProjectPlan> getProjectPlan(Map<String, Object> likeCondition);

    public int getProjectPlanCounter(Map<String, Object> likeCondition);

    public int insertProPlan(ProjectPlan projectPlan);

    public int deleteProPlan(Map<String, Object> likeCondition);

    public int updateProPlan(ProjectPlan projectPlan);

    public List<SampleMember> getProjectSampleMembers(Map<String, Object> likeCondition);


    /**
     * 向项目里面导入样本集合
     *
     * @param proSample
     * @return
     */
    public int insertProSample(ProSample proSample);

    public Integer getMaxBatchID(Map<String, Object> likeCondition);

    /**
     * 返回数据集合
     *
     * @param likeCondition
     * @return
     */
    public List<ProSample> getSampleDataInitList(Map<String, Object> likeCondition);

    /**
     * 通过项目id和任务id获取样本数据集
     *
     * @param likeCondition
     * @return
     */
    public List<ProSample> getSampleDataListByPlanName(Map<String, Object> likeCondition);


    public int insertProjectMember(Map<String, Object> likeCondition);

    public int deleteProjectMember(Map<String, Object> likeCondition);

    /**
     * 项目数据名称列表
     *
     * @param likeCondition
     * @return
     */
    public List<String> getProjectSetNameList(Map<String, Object> likeCondition);

    /**
     * 删除样本集合
     *
     * @param likeCondition
     * @return
     */
    public int deleteProjectSet(Map<String, Object> likeCondition);

    /**
     * 获取数据集合名称
     *
     * @param likeCondition
     * @return
     */
    public String getProjectSetName(Map<String, Object> likeCondition);

    /**
     * 获取项目中方案号
     *
     * @param likeCondition
     * @return
     */
    public String getPlanName(Map<String, Object> likeCondition);

    /**
     * 更新项目信息
     *
     * @param likeCondition
     * @return
     */
    public int updateProject(Map<String, Object> likeCondition);

    /**
     * 用户主动退出项目
     *
     * @param likeCondition
     * @return
     */
    public int deleteProject(Map<String, Object> likeCondition);

    /**
     * 获取项目对应的疾病类型
     *
     * @param likeCondition
     * @return
     */
    public String getProjectDisease(Map<String, Object> likeCondition);


    public int isExistProject(Map<String, Object> likeCondition);

    public int isExistPlan(Map<String, Object> map);

    public int isExistSet(Map<String, Object> map);
    public int isExistSample(@Param("sampleURI") String  sampleURI);

    /**
     * 搜索样本集
     *
     * @param map
     * @return
     */
    public List<ProSample> searchSampleSetList(Map<String, Object> map);

    /**
     * 搜索样本集计数
     *
     * @param map
     * @return
     */
    public int searchSampleSetListCounter(Map<String, Object> map);

    /**
     * 更新样本集信息
     *
     * @param map
     */
    public int updateSetInfo(Map<String, Object> map);

    /**
     * 项目详情
     *
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
     *
     * @param likeCondition
     * @return
     */
    public List<MyProjectList> getMyProjectList(Map<String,Object> likeCondition);

    /**
     *
     * @param likeCondition
     * @return
     */
    public int getProjectCounter(Map<String,Object> likeCondition);


    /**
     * 项目成员列表
     * @param likeCondition
     * @return
     */
    public List<User> getProjectMemberList(Map<String,Object> likeCondition);

    /**
     *
     * @param likeCondition
     * @return
     */
    public int getProjectMemberCounter(Map<String,Object> likeCondition);

    /**
     *
     * @param likeCondition
     * @return
     */
    public List<User> searchMemberList(Map<String,Object> likeCondition);

    /**
     *
     * @param likeCondition
     * @return
     */
    public int searchMemberCounter(Map<String,Object> likeCondition);

    /**
     * 通过sampleURI获取样本信息
     * @param sampleURI
     * @return
     */
    ProSample getSampleDataBySampleURI(@Param("sampleURI")String sampleURI);

    /**
     * 导入样本集之后，项目的setCount字段自增
     * @param projectID
     */
    public int autoAddSetCountOne(@Param("projectID")String projectID);

    /**
     *
     * @param projectID
     * @return
     */
    public int autoDeleteSetCountOne(@Param("projectID")String projectID);

    int deleteSampleByProjectID(@Param("projectID") String projectID);

    int deletePlanByProjectID(@Param("projectID") String projectID);

    int deleteMemberByProjectID(@Param("projectID") String projectID);
}
