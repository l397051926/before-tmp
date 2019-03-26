package com.gennlife.platform.service;

import com.google.gson.JsonObject;

/**
 * @author lmx
 * @create 2019 21 18:18
 * @desc
 **/
public interface EtlDatacountService {

    public JsonObject getAllEtlDatacount();

    String getEtlStatisticsTable(JsonObject paramObj);
}
