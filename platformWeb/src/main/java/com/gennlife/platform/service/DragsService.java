package com.gennlife.platform.service;

/**
 * @author lmx
 * @create 2019 08 14:36
 * @desc
 **/
public interface DragsService {
    String drgsIndex(String paramObj);

    String indexSetting(String param);

    String indexList(String param);

    String factorList(String param);

    String miningCatalog(String param);

    String miningDept(String param);

    String miningParent(String param);

    String drgsHint(String param);

    String indexRedraw(String param);
}
