package com.gennlife.platform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liumingxin
 * @create 2018 09 17:48
 * @desc
 * 判定该部门类别等级 和其上级 部门类别等级是否冲突
 **/
public class DepartDecide {
    private Logger logger = LoggerFactory.getLogger(DepartDecide.class);

    /**
     *
     * @param departName 该 部门等级
     * @param partName  其上级部门等级
     * @return
     */
    public static Boolean decide(String departName,String partName){
        if("行政管理类".equals(departName) && "行政管理类".equals(partName)){
            return false;
        }
        if("业务管理类".equals(departName)){
            if("行政管理类".equals(partName) || "业务管理类".equals(partName)){
                return false;
            }
        }
        if("一线临床类".equals(departName) && "业务管理类".equals(partName)){
            return false;
        }
        return true;
    }

}
