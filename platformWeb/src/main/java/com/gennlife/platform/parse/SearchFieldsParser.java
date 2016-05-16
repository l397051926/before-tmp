package com.gennlife.platform.parse;

import com.gennlife.platform.bean.conf.ConfItem;
import com.gennlife.platform.bean.list.ColumnBean;
import com.gennlife.platform.bean.list.ColumnPropetity;
import com.gennlife.platform.bean.list.ColumnValue;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.DataService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chensong on 2015/12/26.
 */
public class SearchFieldsParser {
    private static String spanStart = "<span style=\"color :red\">";
    private static String spanEnd = "</span>";

    public static List<ColumnBean> parse(String indexName,String key){
        List<ColumnBean> result = new LinkedList<ColumnBean>();
        Map<String,ConfItem> map = ArkService.getIndexMap().get(indexName);
        Map<String, Set<String>> groupNameIndexFieldNameMap = DataService.getGroupIndexFieldNameMap().get(indexName);
        for(String groupName:groupNameIndexFieldNameMap.keySet()){
            if(!groupName.contains("#")){
                ColumnBean columnBean = new ColumnBean();
                columnBean.setParentTitle(groupName);
                List<ColumnValue> columnValues = new LinkedList<ColumnValue>();
                columnBean.setColumnValue(columnValues);
                Set<String> indexs = groupNameIndexFieldNameMap.get(groupName);
                for(String index:indexs){
                    ConfItem confItem = map.get(index);
                    if((confItem != null) && ("".equals(key) || confItem.getUIFieldName().contains(key))){
                        ColumnValue columnValue = new ColumnValue();
                        ColumnPropetity columnPropetity = new ColumnPropetity();
                        columnPropetity.setIndexFieldName(confItem.getIndexFieldName());
                        if("".equals(key) ){
                            columnPropetity.setUIFieldName(confItem.getUIFieldName());
                        }else {
                            columnPropetity.setUIFieldName(spanStart+confItem.getUIFieldName()+spanEnd);
                        }

                        columnPropetity.setDataType(confItem.getDataType());
                        columnValue.setColumnPropetity(columnPropetity);
                        columnValues.add(columnValue);
                        if(!result.contains(columnBean)){
                            result.add(columnBean);
                        }
                    }

                }
            }

        }
        return result;

    }

    public static  List<ColumnBean> parse2(String indexName,String key){
        List<ColumnBean> result = new LinkedList<ColumnBean>();
        Map<String,List<ConfItem>> map = ArkService.getGroupMap().get(indexName).get("searchReuslt");
        List<String> list = new LinkedList<String>();
        for(String head:DataService.getListRankIndexMap().get(indexName).keySet()){
            list.add(head);
        }
        list.add("其他");
        for(String groupName:list){
            List<ConfItem> tmp = map.get(groupName);
            ColumnBean columnBean = new ColumnBean();
            columnBean.setParentTitle(groupName);
            List<ColumnValue> columnValues = new LinkedList<ColumnValue>();
            columnBean.setColumnValue(columnValues);
            if(tmp == null){
                continue;
            }
            for(ConfItem confItem:tmp){
                if((confItem != null) && ("".equals(key) || confItem.getUIFieldName().contains(key))){
                    ColumnValue columnValue = new ColumnValue();
                    ColumnPropetity columnPropetity = new ColumnPropetity();
                    columnPropetity.setIndexFieldName(confItem.getIndexFieldName());
                    if("".equals(key) ){
                        columnPropetity.setUIFieldName(confItem.getUIFieldName());
                    }else {
                        columnPropetity.setUIFieldName(spanStart+confItem.getUIFieldName()+spanEnd);
                    }
                    columnPropetity.setDataType(confItem.getDataType());
                    columnValue.setColumnPropetity(columnPropetity);
                    columnValues.add(columnValue);
                }
            }
            if(!columnValues.isEmpty()){
                result.add(columnBean);
            }
        }
        return result;
    }

}
