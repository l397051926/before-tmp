package com.gennlife.platform.dao;

import com.gennlife.platform.model.GennDataModel;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;

/**
 * Created by Chenjinfeng on 2017/12/14.
 */
@Mapper("GennDao")
public interface GennMapper {
    List<GennDataModel> getGennData(@Param("from") int from,
                                    @Param("size") int size,
                                    @Param("patientSn") String patientSn,
                                    @Param("visitSn") String visitSn);
    int upsert(GennDataModel gennDataModel);

}
