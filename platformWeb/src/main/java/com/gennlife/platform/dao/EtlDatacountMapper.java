package com.gennlife.platform.dao;

import com.gennlife.platform.bean.etl.EtlDatacount;
import com.gennlife.platform.model.InspectReportIntellModel;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author  lmx
 * @create   2019/3/21 17:58
 * @desc
 */
@Mapper("EtlDatacountDao")
public interface EtlDatacountMapper {
    List<EtlDatacount> getAllEtlDataCount();

    List<EtlDatacount> getAllDataByJoin();

    List<EtlDatacount> getSevenParseDates(@Param("date") Date date);

    List<EtlDatacount> getStatisticsTableParseDates(@Param("date") Date date, @Param("codes") List<String> codes);
}
