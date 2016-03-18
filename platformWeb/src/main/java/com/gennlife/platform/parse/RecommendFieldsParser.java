package com.gennlife.platform.parse;

import com.gennlife.platform.bean.conf.ConfItem;
import com.gennlife.platform.bean.list.ColumnPropetity;
import com.gennlife.platform.service.ArkService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by chensong on 2015/12/28.
 */
public class RecommendFieldsParser {

    public static List<ColumnPropetity> parse(String indexName,String key){
        List<ColumnPropetity> result = new LinkedList<ColumnPropetity>();
        Map<String,ConfItem> indexMap = ArkService.getIndexMap().get(indexName);
        for(String IndexFieldName:indexMap.keySet()){
            ConfItem confItem = indexMap.get(IndexFieldName);
            if(confItem.getUIFieldName().contains(key)){
                ColumnPropetity columnPropetity = new ColumnPropetity();
                columnPropetity.setDataType(confItem.getDataType());
                columnPropetity.setIndexFieldName(confItem.getIndexFieldName());
                columnPropetity.setUIFieldName(confItem.getUIFieldName());
                result.add(columnPropetity);
            }
        }
        return result;
    }
}
