package com.gennlife.platform.dao;

import com.gennlife.platform.bean.projectBean.*;
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

    public int insertProUserList( List<ProUser> proUserList);
    public int insertProLog(ProLog proLog);
    public ArrayList<ProLog> getProjectLog(Map<String,Object> likeCondition);
    public int getProjectLogCounter(Map<String,Object> likeCondition);
    public ArrayList<ProjectPlan> getProjectPlan(Map<String,Object> likeCondition);
    public int insertProPlan(ProjectPlan projectPlan);
    public int deleteProPlan(Map<String,Object> likeCondition);
    public int updateProPlan(ProjectPlan projectPlan);

    public List<SampleMember> getProjectSampleMembers(Map<String,Object> likeCondition);


    /**
     * 向项目里面导入样本集合
     * @param proSample
     * @return
     */
    public int insertProSample(ProSample proSample);
    public Integer getMaxBatchID(Map<String,Object> likeCondition);

    /**
     * 返回数据集合
     * @param likeCondition
     * @return
     */
    public List<ProSample> getSampleDataInitList(Map<String,Object> likeCondition);

    /**
     * 通过项目id和任务id获取样本数据集
     * @param likeCondition
     * @return
     */
    public List<ProSample> getSampleDataListByPlanName(Map<String,Object> likeCondition);


    public int insertProjectMember(Map<String,Object> likeCondition);

    public int deleteProjectMember(Map<String,Object> likeCondition);

    /**
     * 项目数据名称列表
     * @param likeCondition
     * @return
     */
    public List<String> getProjectSetNameList(Map<String,Object> likeCondition);

    /**
     * 删除样本集合
     * @param likeCondition
     * @return
     */
    public int deleteProjectSet(Map<String,Object> likeCondition);

    /**
     * 获取数据集合名称
     * @param likeCondition
     * @return
     */
    public String getProjectSetName(Map<String,Object> likeCondition);

    /**
     * 获取项目中方案号
     * @param likeCondition
     * @return
     */
    public String getPlanName(Map<String,Object> likeCondition);
}
