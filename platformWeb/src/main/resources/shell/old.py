#coding=utf8
import sys
reload(sys)
sys.setdefaultencoding('utf8')

import xlrd
import json
from collections import OrderedDict

output = open('case.json', 'w')
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
    data = xlrd.open_workbook('/Users/luoxupan/Downloads/病人维度临床数据字段配置_V3.1.6.2.xlsx')
    allItemList = OrderedDict()
    allItemListCopy = OrderedDict()
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
        for i in range(2,len(keylist)):
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
        show_attendant = item['show_attendant'].strip()

        show_attendantArray = show_attendant.split("；")
        displayMainValue = []
        for show_attendantValue in show_attendantArray:
            if show_attendantValue != "" and show_attendantValue != "无定义":
                displayMainValue.append(show_attendantValue)
        if len(displayMainValue) > 0:
            reItem['displayMainValue'] = displayMainValue


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

    mergeResult = OrderedDict()
    # 肝癌
    liver = OrderedDict()
    # 肺癌
    lung = OrderedDict()
    # 肾癌
    kidney = OrderedDict()
    # 心血管
    angiocardiopathy = OrderedDict()
    # 中南的病种
    adrenal_gland_tumor = OrderedDict()

    mergeResult['liver_cancer'] = liver
    mergeResult['lung_cancer'] = lung
    mergeResult['kidney_cancer'] = kidney
    mergeResult['adrenal_gland_tumor'] = adrenal_gland_tumor
    # ///////////////////////////////////////////////////////////////////////////////////////////////////
    # mergeResult["Angiocardiopathy"] = angiocardiopathy
    # ///////////////////////////////////////////////////////////////////////////////////////////////////
    all_liver = OrderedDict()
    default_liver = OrderedDict()
    advancedSearch_liver = OrderedDict()
    import_liver = OrderedDict()
    compare_liver = OrderedDict()

    all_lung = OrderedDict()
    default_lung = OrderedDict()
    advancedSearch_lung = OrderedDict()
    import_lung = OrderedDict()
    compare_lung = OrderedDict()

    all_kidney = OrderedDict()
    default_kidney = OrderedDict()
    advancedSearch_kidney = OrderedDict()
    import_kidney = OrderedDict()
    compare_kidney = OrderedDict()

    # 心血管
    all_angiocardiopathy = OrderedDict()
    default_angiocardiopathy = OrderedDict()
    advancedSearch_angiocardiopathy = OrderedDict()
    import_angiocardiopathy = OrderedDict()
    compare_angiocardiopathy = OrderedDict()
    # 中南的病种
    all_adrenal_gland_tumor = OrderedDict()
    default_adrenal_gland_tumor = OrderedDict()
    advancedSearch_adrenal_gland_tumor = OrderedDict()
    import_adrenal_gland_tumor = OrderedDict()
    compare_adrenal_gland_tumor = OrderedDict()

    for key in allGroup:
        # 心血管
        all_angiocardiopathy[key] = allGroup[key]
        # 中南的病种
        all_adrenal_gland_tumor[key] = allGroup[key]
        if key == '患者基本信息':
            all_liver[key] = allGroup[key]
            all_lung[key] = allGroup[key]
            all_kidney[key] = allGroup[key]


        if key.startswith('肝癌'):
            all_liver[key] = allGroup[key]
        elif key.startswith('肺癌'):
            all_lung[key] = allGroup[key]
        elif not key.startswith('就诊') or not key.startswith('门诊') or not key.startswith('临床相关基因变异'):
            all_kidney[key] = allGroup[key]


    # for key in allGroup:
    #     if key.startswith('就诊') or key.startswith('门诊') or key.startswith('临床相关基因变异'):
    #         all_liver[key] = allGroup[key]
    #         all_lung[key] = allGroup[key]
    #         all_kidney[key] = allGroup[key]
    #         all_angiocardiopathy[key] = allGroup[key]
    liver['all'] = all_liver
    lung['all'] = all_lung
    kidney['all'] = all_kidney
    # 心血管
    angiocardiopathy['all'] = all_angiocardiopathy
    # 中南的病种
    adrenal_gland_tumor['all'] = all_adrenal_gland_tumor






    for key in defaultGroup:
        # 搜索结果默认展示字段
        # 心血管
        default_angiocardiopathy[key] = defaultGroup[key]
        # 中南的病种
        default_adrenal_gland_tumor[key] = defaultGroup[key]
        if key == '患者基本信息':
            default_liver[key] = defaultGroup[key]
            default_lung[key] = defaultGroup[key]
            default_kidney[key] = defaultGroup[key]

        if key.startswith('肝癌'):
            default_liver[key] = defaultGroup[key]
        elif key.startswith('肺癌'):
            default_lung[key] = defaultGroup[key]
        elif not key.startswith('就诊') or not key.startswith('门诊') or not key.startswith('临床相关基因变异'):
            default_kidney[key] = defaultGroup[key]


    for key in defaultGroup:
        if key.startswith('就诊') or key.startswith('门诊') or key.startswith('临床相关基因变异'):
            default_liver[key] = defaultGroup[key]
            default_lung[key] = defaultGroup[key]
            default_kidney[key] = defaultGroup[key]

    liver['default'] = default_liver
    lung['default'] = default_lung
    kidney['default'] = default_kidney
    # 心血管
    angiocardiopathy['default'] = default_angiocardiopathy
    # 中南的病种
    adrenal_gland_tumor['default'] = default_adrenal_gland_tumor


    for key in advancedGroup:
        # 心血管
        advancedSearch_angiocardiopathy[key] = advancedGroup[key]
        # 中南的病种
        advancedSearch_adrenal_gland_tumor[key] = advancedGroup[key]
        if key == '患者基本信息':
            advancedSearch_liver[key] = advancedGroup[key]
            advancedSearch_lung[key] = advancedGroup[key]
            advancedSearch_kidney[key] = advancedGroup[key]

        if key.startswith('肝癌'):
            advancedSearch_liver[key] = advancedGroup[key]
        elif key.startswith('肺癌'):
            advancedSearch_lung[key] = advancedGroup[key]
        elif not key.startswith('就诊') or not key.startswith('门诊') or not key.startswith('临床相关基因变异'):
            advancedSearch_kidney[key] = advancedGroup[key]





    for key in advancedGroup:
        if key.startswith('就诊') or key.startswith('门诊') or key.startswith('临床相关基因变异'):
            advancedSearch_liver[key] = advancedGroup[key]
            advancedSearch_lung[key] = advancedGroup[key]
            advancedSearch_kidney[key] = advancedGroup[key]
    liver['advancedSearch'] = advancedSearch_liver
    lung['advancedSearch'] = advancedSearch_lung
    kidney['advancedSearch'] = advancedSearch_kidney
    # 心血管
    angiocardiopathy['advancedSearch'] = advancedSearch_angiocardiopathy
    # 中南的病种
    adrenal_gland_tumor['advancedSearch'] = advancedSearch_adrenal_gland_tumor

    for key in importedGroup:
        # 心血管
        import_angiocardiopathy[key] = importedGroup[key]
        # 中南的病种
        import_adrenal_gland_tumor[key] = importedGroup[key]
        if key == '患者基本信息':
            import_liver[key] = importedGroup[key]
            import_lung[key] = importedGroup[key]
            import_kidney[key] = importedGroup[key]

        if key.startswith('肝癌'):
            import_liver[key] = importedGroup[key]
        elif key.startswith('肺癌'):
            import_lung[key] = importedGroup[key]
        elif not key.startswith('就诊') or not key.startswith('门诊') or not key.startswith('临床相关基因变异'):
            import_kidney[key] = importedGroup[key]

    for key in importedGroup:
        if key.startswith('就诊') or key.startswith('门诊') or key.startswith('临床相关基因变异'):
            import_liver[key] = importedGroup[key]
            import_lung[key] = importedGroup[key]
            import_kidney[key] = importedGroup[key]
    liver['import'] = import_liver
    lung['import'] = import_lung
    kidney['import'] = import_kidney
    # 心血管
    angiocardiopathy['import'] = import_angiocardiopathy
    # 中南的病种
    adrenal_gland_tumor['import'] = import_adrenal_gland_tumor

    for key in compare:
        # 心血管
        compare_angiocardiopathy[key] = compare[key]
        # 中南的病种
        compare_adrenal_gland_tumor[key] = compare[key]
        if key == '患者基本信息':
            compare_liver[key] = compare[key]
            compare_lung[key] = compare[key]
            compare_kidney[key] = compare[key]

        if key.startswith('肝癌'):
            compare_liver[key] = compare[key]
        elif key.startswith('肺癌'):
            compare_lung[key] = compare[key]
        elif not key.startswith('就诊') or not key.startswith('门诊') or not key.startswith('临床相关基因变异'):
            compare_kidney[key] = compare[key]

    for key in compare:
        if key.startswith('就诊') or key.startswith('门诊') or key.startswith('临床相关基因变异'):
            compare_liver[key] = compare[key]
            compare_lung[key] = compare[key]
            compare_kidney[key] = compare[key]

    liver['compare'] = compare_liver
    lung['compare'] = compare_lung
    kidney['compare'] = compare_kidney
    # 心血管
    angiocardiopathy['compare'] = compare_angiocardiopathy
    adrenal_gland_tumor['compare'] = compare_adrenal_gland_tumor
    output.write(json.dumps(mergeResult,ensure_ascii=False))
    output.close()

    # 患者基本信息
    basic = OrderedDict()
    # EMR数据
    emr = OrderedDict()


if __name__ == '__main__':
    process()
