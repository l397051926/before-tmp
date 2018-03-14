package com.gennlife.platform.dao;

import com.gennlife.platform.model.InspectReportIntellModel;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;

/**
 * Created by Chenjinfeng on 2018/3/14.
 */
@Mapper("intelligentDao")
public interface IntelligentMapper {
    InspectReportIntellModel getOneData(@Param("inspectItemId") int inspectItemId);

    List<InspectReportIntellModel> inspectReportIntell(@Param("inspectName") String inspectName);
}
