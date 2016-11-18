package com.gennlife.platform.dao;

import com.gennlife.platform.util.SpringContextUtil;
import org.springframework.context.ApplicationContext;

/**
 * Created by chensong on 2015/12/4.
 */
public class AllDao {
    private SyUserMapper syUserDao;
    private SyRoleMapper syRoleDao;
    private SyResourceMapper syResourceDao;
    private OrgMapper orgDao;
    private ProjectMapper projectDao;
    private GroupMapper groupDao;

    public ProjectMapper getProjectDao() {
        return projectDao;
    }

    public void setProjectDao(ProjectMapper projectDao) {
        this.projectDao = projectDao;
    }

    public OrgMapper getOrgDao() {
        return orgDao;
    }

    public void setOrgDao(OrgMapper orgDao) {
        this.orgDao = orgDao;
    }

    private static AllDao instance = null;
    public static AllDao getInstance(){
        if(instance == null){
            ApplicationContext context = SpringContextUtil.getApplicationContext();
            instance = (AllDao) context.getBean("allDao");
        }
        return instance;
    }
    public SyUserMapper getSyUserDao() {
        return syUserDao;
    }

    public void setSyUserDao(SyUserMapper syUserDao) {
        this.syUserDao = syUserDao;
    }

    public SyRoleMapper getSyRoleDao() {
        return syRoleDao;
    }

    public void setSyRoleDao(SyRoleMapper syRoleDao) {
        this.syRoleDao = syRoleDao;
    }

    public SyResourceMapper getSyResourceDao() {
        return syResourceDao;
    }

    public void setSyResourceDao(SyResourceMapper syResourceDao) {
        this.syResourceDao = syResourceDao;
    }

    public GroupMapper getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupMapper groupDao) {
        this.groupDao = groupDao;
    }
}
