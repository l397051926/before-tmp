#coding=utf8
import xlrd
import json
from collections import OrderedDict

def sortItem(itemArray,allSortIndexName):
    newArray = []
    for name in allSortIndexName:
        for reItem in itemArray:
            if reItem['IndexFieldName'] == name:
                if 'group_en_name' in reItem:
                    del(reItem['group_en_name'])
                if 'group_cn_name' in reItem:
                    del (reItem['group_cn_name'])
                newArray.append(reItem)
    return newArray

def process():
    data = xlrd.open_workbook('/Users/chen-song/Downloads/肾癌病人维度临床数据字段配置_V2.1.48_5.xls')
    allItemList = OrderedDict()
    allItemListCopy = {}
    sheet = data.sheets()[0]
    rowsNum = sheet.nrows#行数
    keylist = sheet.row_values(1)
    print('keylist = ',len(keylist))
    allSortGroupName = []#组名顺序
    allSortIndexName = []#index名称
    for i in range(2,rowsNum):
        item = {}
        rows = sheet.row_values(i)
        if rows[0] == '':
            continue
        for i in range(0,len(keylist)):
            key = keylist[i].strip()
            value = rows[i]
            item[key] = value
        reItem = OrderedDict()
        reItem['UIFieldName'] = item['ui_field_name'].strip()
        reItem['IndexFieldName'] = item['group_en_name'].strip() + '.' + item['index_field_name'].strip()
        reItem['srchFieldName'] = item['group_cn_name'].strip() + '.' + item['ui_field_name'].strip()
        reItem['group_cn_name'] = item['group_cn_name'].strip()
        reItem['dataType'] = item['field_type'].strip()
        reItem['default_show'] = item['default_show'].strip()
        reItem['advanced_search'] = item['advanced_search'].strip()
        reItem['imported'] = item['imported'].strip()
        reItem['group_en_name'] = item['group_en_name'].strip()
        if reItem['group_cn_name'] not in allSortGroupName:
            allSortGroupName.append(reItem['group_cn_name'])
        if reItem['IndexFieldName'] not in allSortIndexName:
            allSortIndexName.append(reItem['IndexFieldName'])

        if (reItem['dataType'] == 'date'):
            reItem['dateFormat'] = item['data_format'].strip()

        if item['enum'] == 1:
            valueStr = item['enum_values']
            valueList = valueStr.split("；")
            dataMap = []
            for v in valueList:
                dataMap.append(v)
            reItem['dataMap'] = dataMap
        if item['prime_attribute'] == 1:
            reItem['attendant'] = item['attendant']
        allItemList[reItem['IndexFieldName']] = reItem

        reItemStr = json.dumps(reItem)
        reItemCopy = json.loads(reItemStr)

        del (reItemCopy['imported'])
        del (reItemCopy['advanced_search'])
        del (reItemCopy['default_show'])
        del (reItemCopy['group_en_name'])
        del (reItemCopy['group_cn_name'])
        if 'attendant' in reItemCopy:
            del (reItemCopy['attendant'])
        allItemListCopy[reItemCopy['IndexFieldName']] = reItemCopy
    subItem = []#保存子属性IndexFieldName
    subItemLive = []#保存是子属性，又是主属性
    for IndexFieldName in allSortIndexName:
        reItem = allItemList[IndexFieldName]
        if 'attendant' in reItem:
            valueStr = reItem['attendant']
            subItemLive.append(IndexFieldName)
            valueList = valueStr.split("；")
            relatedItems = []
            for v in valueList:
                keyIndex = reItem['group_en_name'] + '.' + v.strip()
                subItem.append(keyIndex)
                if keyIndex in allItemListCopy:
                    relatedItems.append(allItemListCopy[keyIndex])
            reItem['relatedItems'] = relatedItems

    result = OrderedDict()
    allGroup = OrderedDict()
    defaultGroup = OrderedDict()
    advancedGroup = OrderedDict()
    importedGroup = OrderedDict()
    for IndexFieldName in allSortIndexName:
        reItem = allItemList[IndexFieldName]
        default_show = reItem['default_show']
        advanced_search = reItem['advanced_search']
        imported = reItem['imported']
        group_cn_name = reItem['group_cn_name']
        groupArray = []
        if (IndexFieldName in subItem and IndexFieldName in subItemLive) or (IndexFieldName not in subItem): #非单纯子属性进行
            if advanced_search.strip() == "是":
                advanced_searchTmpGroup = []
                if group_cn_name in advancedGroup:
                    advanced_searchTmpGroup = advancedGroup[group_cn_name]
                else:
                    advancedGroup[group_cn_name] = advanced_searchTmpGroup
                advanced_searchTmpGroup.append(reItem)

        if group_cn_name in allGroup:
            groupArray = allGroup[group_cn_name]
        else:
            allGroup[group_cn_name] = groupArray
        groupArray.append(reItem)

        if default_show.strip() == "是":
            defaultTmpGroup = []
            if group_cn_name in defaultGroup:
                defaultTmpGroup = defaultGroup[group_cn_name]
            else:
                defaultGroup[group_cn_name] = defaultTmpGroup
            reItemCopy = allItemListCopy[reItem['IndexFieldName']]
            defaultTmpGroup.append(reItemCopy)

        if imported.strip() == "是":
            importedTmpGroup = []
            if group_cn_name in importedGroup:
                importedTmpGroup = importedGroup[group_cn_name]
            else:
                importedGroup[group_cn_name] = importedTmpGroup
            reItemCopy = allItemListCopy[reItem['IndexFieldName']]
            importedTmpGroup.append(reItemCopy)
        del (reItem['imported'])
        del (reItem['advanced_search'])
        del (reItem['default_show'])
        del (reItem['group_en_name'])
        del (reItem['group_cn_name'])
        if 'attendant' in reItem:
            del (reItem['attendant'])

    result['all'] = allGroup
    result['default'] = defaultGroup
    result['advancedSearch'] = advancedGroup
    result['import'] = importedGroup
    compare=OrderedDict()
    for groupName in allGroup.keys():
        itemList = allGroup[groupName]
        for item in itemList:
            if item['dataType'] == 'date':
                if groupName in compare:
                    newItemList = compare[groupName]
                    newItemList.append(item)
                else:
                    newItemList = []
                    compare[groupName] = newItemList
                    newItemList.append(item)
    result['compare'] = compare
    print(json.dumps(result,ensure_ascii=False))

if __name__ == '__main__':
    process()