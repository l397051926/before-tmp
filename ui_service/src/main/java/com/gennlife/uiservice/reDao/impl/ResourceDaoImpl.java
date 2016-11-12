package com.gennlife.uiservice.reDao.impl;

import com.gennlife.uiservice.model.*;
import com.gennlife.uiservice.reDao.ResourceDao;
import com.gennlife.uiservice.reDao.UserDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen-song on 2016/11/4.
 */
public class ResourceDaoImpl extends SqlSessionDaoSupport implements ResourceDao {

    @Override
    public List<Resource> getResourcesBySid(String orgID, String labID) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        Map<String,Object> map = new HashMap<>();
        map.put("orgID",orgID);
        map.put("labID",labID);
        List<Resource> list = sqlSession.selectList(Resource.class.getName()+".getResourcesBySid",map);
        return list;
    }

    @Override
    public List<Resource> getResources(String orgID, Integer roleid) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        Map<String,Object> map = new HashMap<>();
        map.put("orgID",orgID);
        map.put("roleid",roleid);
        List<Resource> list = sqlSession.selectList(Resource.class.getName()+".getResources",map);
        return list;
    }

    @Override
    public List<CRFLab> getCrfIDListByLab(String[] labIDs, String orgID) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        Map<String,Object> map = new HashMap<>();
        map.put("orgID",orgID);
        map.put("labIDs",labIDs);
        List<CRFLab> list = sqlSession.selectList(Resource.class.getName()+".getCrfIDListByLab",map);
        return list;
    }

    @Override
    public int deleteLabsReource(String[] labIDs) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        Map<String,Object> map = new HashMap<>();
        map.put("labIDs",labIDs);
        return sqlSession.delete(Resource.class.getName()+".deleteLabsReource",map);
    }

    @Override
    public List<Resource> getResourceByRoleID(String orgID, Integer roleid, int offset, int limit) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        Map<String,Object> map = new HashMap<>();
        map.put("orgID",orgID);
        map.put("roleid",roleid);
        map.put("offset",offset);
        map.put("limit",limit);
        return sqlSession.selectList(Resource.class.getName()+".getResourceByRoleID",map);

    }

    @Override
    public int insertRoleResourceRelation(Resource resourceObj) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        return sqlSession.insert(Resource.class.getName()+".insertRoleResourceRelation",resourceObj);
    }

    @Override
    public int getResourceByRoleIDCounter(Integer roleid) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        Map<String,Object> map = new HashMap<>();
        map.put("roleid",roleid);
        return (int) sqlSession.selectOne(Resource.class.getName()+".getResourceByRoleIDCounter",map);
    }

    @Override
    public List<LabResource> getLabResourcesByOrgID(String orgID, String stype) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        Map<String,Object> map = new HashMap<>();
        map.put("orgID",orgID);
        map.put("stype",stype);
        return sqlSession.selectList(Resource.class.getName()+".getLabResourcesByOrgID",map);
    }

    @Override
    public int insertOneResource(LabResource labResource) {
        SqlSessionTemplate sqlSession = super.getSqlSessionTemplate();
        return sqlSession.insert(Resource.class.getName()+".insertOneResource",labResource);
    }

}
