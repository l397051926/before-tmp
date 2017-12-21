package com.gennlife.platform.dao;

import com.gennlife.platform.model.GennDataModel;
import com.gennlife.platform.model.GennImage;
import com.gennlife.platform.model.GennZipLog;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.Mapper;

import java.util.List;

/**
 * Created by Chenjinfeng on 2017/12/14.
 */
@Mapper("gennDao")
public interface GennMapper {
    int addZipLog(GennZipLog log);
    List<GennDataModel> getGennData(@Param("from") int from,
                                    @Param("size") int size,
                                    @Param("patientSn") String patientSn,
                                    @Param("visitSn") String visitSn);

    int upsert(GennDataModel gennDataModel);

    List<GennImage> getAllImage(@Param("uniqueIds") String[] uniqueId);

    int deleteAllImage(@Param("uniqueIds") String[] uniqueId);

    int saveGennImage(GennImage gennImage);

    List<String> getAllPdfPath(@Param("uniqueIds") String[] uniqueId);

    String getOneImagePath(@Param("imgId") String imageId, @Param("patientSn") String patientSn);

    GennDataModel getDataModel(@Param("uniqueId") String uniqueId,@Param("patientSn") String patientSn);
    String getJsonData(@Param("uniqueId") String uniqueId,@Param("patientSn") String patientSn);

    GennDataModel getPdfInfo(@Param("pdfId") String pdfId, @Param("patientSn") String patientSn);
}
