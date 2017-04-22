package com.gennlife.platform.dao;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

/**
 * Created by Chenjinfeng on 2017/4/4.
 */
@Mapper("sessionDao")
public interface SessionMapper {
    public int clear();
    public int deleteByUids(@Param("uids") String[] uids);
    public int deleteBySessionID(@Param("sessionID") String sessionID);
    public int insertData(@Param("uid")String uid ,@Param("sessionID") String sessionID,@Param("cTime") String cTime);
    public String getUid(@Param("sessionID") String sessionID);

    public int  deleteByUid(@Param("uid")String uid);

    public String getSessionID(@Param("uid") String uid);
}
