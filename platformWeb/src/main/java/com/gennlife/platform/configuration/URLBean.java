package com.gennlife.platform.configuration;

/**
 * Created by chen-song on 16/5/13.
 */
public class URLBean {

    /**
     * 非隐私信息接口
     */
    private String casePatientBatchData;
    private String CrfCsvImportDetail;
    private String consultingRoomData;
    private String fsIpAndPort;
    /**
     * 详情页通用接口
     */
    private String caseDetailCommonUrl;

    public String getCaseAdmission_records() {
        return caseAdmission_records;
    }

    public void setCaseAdmission_records(String caseAdmission_records) {
        this.caseAdmission_records = caseAdmission_records;
    }

    /**
     * 获取当前病人的指定字段的信息
     */
    private String caseSimilarServiceSimilarPatientExtraInfo;


    private String vitaGramServerMedicalHistoryRecommendPath = null;
    /**
     * 获取NCCN推荐路径
     */
    private String vitaGramServerNccnRecommendPath = null;
    /**
     * 当前病人的信息
     */

    private String caseSimilarServiceSimilarPatientInfo;
    /**
     * 获取相似病人情况的基本信息
     */
    private String caseSimilarServiceSimilarBasicDetail;
    /**
     * vitaboard画图数据获取接口
     */
    private String caseSimilarServiceGetSimilars = null;
    /**
     * 获取vitaGram图数据接口
     */
    private String vitaGramServerVitaGramData = null;
    /**
     * 获取vitaboard默认参数接口
     */
    private String caseSimilarServiceVitaboardparam = null;
    /**
     * 相似病人默认参数获取
     */
    private String caseSimilarServiceSimilarParam = null;

    /**
     * 生成相似病人查询条件接口
     */
    private String caseSimilarServiceSimilarQuery = null;


    private String casePatientChemotherapyInfo = null;
    /**
     * 单次就诊放疗
     */
    private String caseyVisitRadiotherapy = null;
    /**
     * 分类详情放疗
     */
    private String casePatientRadiotherapy = null;

    /**
     * 分类详情DC治疗
     */
    private String casePatientDcOrder = null;
    /**
     * 单次就诊DC治疗
     */
    private String caseVisitDcOrder = null;


    /**
     * crf 病人编号是否存在
     */
    private String CRFIsExistPatient = null;
    /**
     * 基线统计
     */
    private String CSBaseline = null;

    /**
     * 图片获取接口
     */
    private String caseDetailVisitClassifyImageURL = null;
    /**
     * 病史信息接口
     */
    private String caseDetailVisitClassifySectionURL = null;
    /**
     * 样本集上传使用不使用标签
     */
    private String sampleUploadAdaptTagURL = null;
    /**
     * crf 文件导入结果查询
     */
    private String CRFImportResult = null;

    /**
     * 数据集搜索
     */
    private String sampleDetailSearchURL = null;
    /**
     * crf 配置映射
     */
    private String CRFImportMap = null;
    /**
     * CRF 导入文件后使用crf_id,schema,fileID请求CRFService
     */
    private String CRFImportFile = null;
    /**
     * FS 存储 CRF导入文件
     */
    private String fileStoreForCRFImport = null;

    /**
     * crf 高级搜索
     */
    private String crfSearchURL = null;


    /**
     * 修改密码的url前缀
     */
    private String EmailSendURL = null;

    /**
     * email url
     */
    private String EmailURL = null;

    /**
     * 工作区工具列表
     */
    private String toolsURL = null;

    private String AtoolURL = null;
    /**
     * 搜索结果导出到项目
     */
    private String sampleImportIURL = null;
    /**
     * 导出数据校验
     */
    private String sampleImportChecKIURL = null;

    public String getSampleImportChecKIURL() {
        return sampleImportChecKIURL;
    }

    public void setSampleImportChecKIURL(String sampleImportChecKIURL) {
        this.sampleImportChecKIURL = sampleImportChecKIURL;
    }


    /**
     * 同义词接口
     */
    private String synonyms = null;
    private String addSynonym = null;
    private String removeSynonym = null;
    private String saveRelatedPhrasesSelectionBehavior = null;

    /**
     * 获取组学信息url
     */
    private String getGennomics = null;

    /**
     * 样本详情接口
     */
    private String sampleDetailURL = null;

    private String sampleDaleteURL = null;

    /**
     * 病程 诊断报告:主诉
     */
    private String caseAdmission_records = null;

    /**
     * 新的搜索后端接口
     */
    private String caseSearchURL = null;
    /**
     * 搜索关键词提示词
     */
    private String caseSuggestURL = null;
    /**
     * 搜索关键词提示2
     */
    private String caseSuggestURL2 = null;
    /**
     * 搜索:V1.0
     * 详情页患者基础信息接口
     */
    private String caseDetailPatientBasicInfoURL = null;
    /**
     * 搜索:V1.0
     * 基本统计图形&筛选条件
     */
    private String caseDetailPatientBasicFigureURL = null;
    /**
     * 搜索:V1.0
     * 详情页时间轴信息接口
     */
    private String casePatientBasicTimeAxisURL = null;

    /**
     * 查看指标变化: 具体指标
     */
    private String casePhysical_examination = null;
    /**
     * 查看指标变化: 可选列表
     */
    private String casePhysical_examination_list = null;

    /**
     * 知识库搜索
     */
    private String knowledgeURL = null;
    /**
     * 详情页总接口：唐乾斌提供
     */
    private String caseVisit_detail = null;
    /**
     * 详情页体检接口:唐乾斌提供
     */
    private String caseLab_result_item = null;
    /**
     * 搜索首页,疾病搜索基因
     */
    private String knowledgeDiseaseSearchGenesURL = null;
    /**
     * 检验项列表
     */
    private String caseLab_result_item_list = null;
    /**
     * 基因信息接口:搜索首页
     */
    private String knowledgeGeneInfoURL = null;
    /**
     * 变异信息接口:搜索首页
     */
    private String knowledgeVariationInfoURL = null;
    /**
     *
     */
    private String knowledgeDetailVariationSearchDiseaseURL = null;
    /**
     *
     */
    private String knowledgeDetailVariationSearchDrugURL = null;

    /**
     * 单次就诊专用:诊断报告
     */
    private String caseDiagnose = null;


    /**
     * 遗传性疾病
     */
    private String caseGenetic_disease = null;
    /**
     * 药物反应
     */
    private String caseDrug_reaction = null;
    /**
     * 分类信息接口、及目录
     */
    private String caseCategory_catalog = null;
    /**
     * 分子检测
     */
    private String caseMolecular_detection = null;
    /**
     * 生物标本
     */
    private String caseBiological_specimen = null;
    /**
     * 检查
     */
    private String caseExam_result = null;
    /**
     * 病理检测
     */
    private String casePathological_examination = null;
    /**
     * 详情页,通过变异号和基因类型查询 药和用药指南
     */
    private String knowledgePharmGKBSearchDrugURL = null;
    /**
     * 基因数组校验接口
     */
    private String caseGeneErrorURL = null;
    /**
     * 构建全部索引接口
     */
    private String buildIndexForAll = null;

    public String getBuildIndexForAll() {
        return buildIndexForAll;
    }

    public void setBuildIndexForAll(String buildIndexForAll) {
        this.buildIndexForAll = buildIndexForAll;
    }

    public String getCaseToDetail() {
        return CaseToDetail;
    }

    public void setCaseToDetail(String caseToDetail) {
        CaseToDetail = caseToDetail;
    }

    /**
     * crf 高级搜索调到详情页
     */
    private String CaseToDetail = null;

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    private String highlight = null;

    /**
     * crf service:用户相关项目的crf模版列表：
     */
    private String CRFProjectCRFListURL = null;

    /**
     * crf service:将搜索到的病例导入crf接口
     */
    private String CRFAutoMapURL = null;

    /**
     * crf service:获取新的属性id
     */
    private String CRFGetAttrID = null;

    /**
     * crf service:获取新的组id
     */
    private String CRFGetGroupID = null;


    /**
     * crf service:通过projectID获取crf模板
     */
    private String CRFModelByProjectID = null;

    /**
     * crf service:通过CRFID获取crf模板
     */
    private String CRFModelByCRFID = null;


    /**
     * crf service:自动映射检验上传crf数据
     */
    private String CRFUpLoadDataForCheck = null;

    /**
     * crf service:编辑模型
     */
    private String CRFEditModel = null;

    /**
     * crf service:保存模型
     */
    private String CRFSaveModel = null;
    /**
     * crf service:数据录入时,请求某个case数据
     */
    private String CRFGetData = null;
    /**
     * crf service:上传crf数据
     */
    private String CRFUpLoadData = null;
    /**
     * crf service:录入完成接口
     */
    private String CRFSaveData = null;

    /**
     * crf service:病历列表数据
     */
    private String CRFSampleCaseList = null;
    /**
     * crf service:删除某个case数据
     */
    private String CRFDeleteSample = null;
    /**
     * crf service:搜索病历列表
     */
    private String CRFSearchSampleList = null;

    /*
     *  病人基本信息查询
     */
    private String CRFGetPatientInfo = null;

    private String RRun = null;

    private String RStop = null;

    private String RSave = null;

    private String RLoad = null;

    private String RList = null;

    /**
     * .病案首页
     */
    private String caseMedicalRecord = null;
    /**
     * 手术信息
     */
    private String caseOperationRecords = null;
    /**
     * 用药医嘱
     */
    private String casePharmacy = null;
    /**
     * 出院记录
     */
    private String caseDischargeRecords = null;
    /**
     * 病程记录
     */
    private String caseCourseRecords = null;
    /**
     * 病例文书
     */
    private String caseMedicalCourse = null;

    /**
     * 计算服务因子图
     */
    private String CSSmg = null;

    /**
     * CRF录入智能提示
     */
    private String InputSmartPrompt = null;

    private String ImageUpload = null;

    private String ImageDel = null;

    private String ImageUrl = null;

    private String ICD_10_CodeUrl = null;

    private String ResearchNumberUrl = null;

    private String TripleTestTableUrl = null;

    private String applyOutGoing = null;

    private String getPatienSn = null;

    private String tripleTestTable = null;

    /**
     * RWS
     */
    private String PreLiminaryUrl = null;
    private String PreAggregationUrl = null;
    private String PreFindForProjectData = null;
    private String FindByProjectId = null;
    private String GetAllActiveOrIndex = null;
    private String GetSavedActivityData = null;
    private String SaveOrSearchActive = null;
    private String ClacResultSearch = null;
    private String ClacIndexResultSearch = null;
    private String GetCalcTotalByActiveId = null;
    private String FindTotalForImport = null;
    private String DeleteByActiveId = null;
    private String CheckActiveIsOnlyOne = null;
    private String CheckActiveDataIsChange = null;
    private String DependenceChange = null;
    private String EditActiveName =null;

    public String getEditActiveName() {
        return EditActiveName;
    }

    public void setEditActiveName(String editActiveName) {
        EditActiveName = editActiveName;
    }
    private String ContResultUrl = null;
    private String ContResultForPatientUrl = null;
    private String PatientGroupCondition = null;
    private String ResearchVariableUrl = null;
    private String SaveGroupCondition = null;
    private String DeletePatientSet = null;
    private String ContrasAnalyList = null;
    private String PatientList = null;
    private String PatientSet = null;
    private String PatientSetList = null;
    private String SearchCondition = null;
    private String SavePatientSet = null;
    private String UpdatePatientSet = null;
    private String ProjectByCrfId = null;
    private String deleteProject = null;
    private String Project = null;
    private String ProjectList = null;
    private String saveProject = null;
    private String UpdateProject = null;
    private String DeletePatientGroup = null;
    private String ExportGroupDataPatient = null;
    private String ActiveIndexList = null;
    private String PatientGroup = null;
    private String PatientGroupList = null;
    private String PatientListForGroup = null;
    private String InsertGroupDataPatient = null;
    private String SaveActiveIndex = null;
    private String SaveGroupAndPatient = null;
    private String SavePatientGroup = null;
    private String UpdatePatientGroup = null;
    private String OperLogsList = null;
    private String DeleteProjectMember = null;
    private String ProjectMember = null;
    private String SaveProjectMember = null;
    private String UpdateProjectMember = null;

    private String Scientific = null;
    private String GroupTypeList = null;

    private String ProjectMemberList = null;
    private String groupAggregation = null;
    private String checkName = null;
    private String groupParentData = null;
    private String allResearchVariable = null;
    private String saveResearchVariable = null;
    private String deleteResearchVariable = null;
    private String projectAggregation = null;
    private String projectPowerExamine = null;


    /**用友单点登陆校验地址*/
    private String yyssoUrl = null;
    private Integer isMock = 1;
    private String ssoSuccessUrl = null;
    private String ssoFailUrl = null;
    private String ssoSysmark = null;

    public String getSsoSysmark() {

        return ssoSysmark;
    }

    public void setSsoSysmark(String ssoSysmark) {

        this.ssoSysmark = ssoSysmark;
    }

    public String getYyssoUrl() {

        return yyssoUrl;
    }

    public void setYyssoUrl(String yyssoUrl) {

        this.yyssoUrl = yyssoUrl;
    }

    public Integer getIsMock() {

        return isMock;
    }

    public void setIsMock(Integer isMock) {

        this.isMock = isMock;
    }

    public String getSsoSuccessUrl() {

        return ssoSuccessUrl;
    }

    public void setSsoSuccessUrl(String ssoSuccessUrl) {

        this.ssoSuccessUrl = ssoSuccessUrl;
    }

    public String getSsoFailUrl() {

        return ssoFailUrl;
    }

    public void setSsoFailUrl(String ssoFailUrl) {

        this.ssoFailUrl = ssoFailUrl;
    }

    public String getProjectPowerExamine() {
        return projectPowerExamine;
    }

    public void setProjectPowerExamine(String projectPowerExamine) {
        this.projectPowerExamine = projectPowerExamine;
    }

    public String getProjectAggregation() {
        return projectAggregation;
    }

    public void setProjectAggregation(String projectAggregation) {
        this.projectAggregation = projectAggregation;
    }

    public String getDeleteResearchVariable() {
        return deleteResearchVariable;
    }

    public void setDeleteResearchVariable(String deleteResearchVariable) {
        this.deleteResearchVariable = deleteResearchVariable;
    }

    public String getSaveResearchVariable() {
        return saveResearchVariable;
    }

    public void setSaveResearchVariable(String saveResearchVariable) {
        this.saveResearchVariable = saveResearchVariable;
    }

    public String getAllResearchVariable() {
        return allResearchVariable;
    }

    public void setAllResearchVariable(String allResearchVariable) {
        this.allResearchVariable = allResearchVariable;
    }

    public String getGroupParentData() {
        return groupParentData;
    }

    public void setGroupParentData(String groupParentData) {
        this.groupParentData = groupParentData;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getGroupAggregation() {
        return groupAggregation;
    }

    public void setGroupAggregation(String groupAggregation) {
        this.groupAggregation = groupAggregation;
    }

    public String getProjectMemberList() {
        return ProjectMemberList;
    }

    public void setProjectMemberList(String projectMemberList) {
        ProjectMemberList = projectMemberList;
    }


    public String getGroupTypeList() {
        return GroupTypeList;
    }

    public void setGroupTypeList(String groupTypeList) {
        GroupTypeList = groupTypeList;
    }

    public String getScientific() {
        return Scientific;
    }

    public void setScientific(String scientific) {
        Scientific = scientific;
    }

    public String getUpdateProjectMember() {
        return UpdateProjectMember;
    }

    public void setUpdateProjectMember(String updateProjectMember) {
        UpdateProjectMember = updateProjectMember;
    }

    public String getSaveProjectMember() {
        return SaveProjectMember;
    }

    public void setSaveProjectMember(String saveProjectMember) {
        SaveProjectMember = saveProjectMember;
    }

    public String getProjectMember() {
        return ProjectMember;
    }

    public void setProjectMember(String projectMember) {
        ProjectMember = projectMember;
    }

    public String getDeleteProjectMember() {
        return DeleteProjectMember;
    }

    public void setDeleteProjectMember(String deleteProjectMember) {
        DeleteProjectMember = deleteProjectMember;
    }

    public String getOperLogsList() {
        return OperLogsList;
    }

    public void setOperLogsList(String operLogsList) {
        OperLogsList = operLogsList;
    }

    public String getUpdatePatientGroup() {
        return UpdatePatientGroup;
    }

    public void setUpdatePatientGroup(String updatePatientGroup) {
        UpdatePatientGroup = updatePatientGroup;
    }

    public String getSavePatientGroup() {
        return SavePatientGroup;
    }

    public void setSavePatientGroup(String savePatientGroup) {
        SavePatientGroup = savePatientGroup;
    }

    public String getSaveGroupAndPatient() {
        return SaveGroupAndPatient;
    }

    public void setSaveGroupAndPatient(String saveGroupAndPatient) {
        SaveGroupAndPatient = saveGroupAndPatient;
    }

    public String getSaveActiveIndex() {
        return SaveActiveIndex;
    }

    public void setSaveActiveIndex(String saveActiveIndex) {
        SaveActiveIndex = saveActiveIndex;
    }

    public String getInsertGroupDataPatient() {
        return InsertGroupDataPatient;
    }

    public void setInsertGroupDataPatient(String insertGroupDataPatient) {
        InsertGroupDataPatient = insertGroupDataPatient;
    }

    public String getPatientListForGroup() {
        return PatientListForGroup;
    }

    public void setPatientListForGroup(String patientListForGroup) {
        PatientListForGroup = patientListForGroup;
    }

    public String getPatientGroupList() {
        return PatientGroupList;
    }

    public void setPatientGroupList(String patientGroupList) {
        PatientGroupList = patientGroupList;
    }

    public String getPatientGroup() {
        return PatientGroup;
    }

    public void setPatientGroup(String patientGroup) {
        PatientGroup = patientGroup;
    }

    public String getActiveIndexList() {
        return ActiveIndexList;
    }

    public void setActiveIndexList(String activeIndexList) {
        ActiveIndexList = activeIndexList;
    }

    public String getExportGroupDataPatient() {
        return ExportGroupDataPatient;
    }

    public void setExportGroupDataPatient(String exportGroupDataPatient) {
        ExportGroupDataPatient = exportGroupDataPatient;
    }

    public String getDeletePatientGroup() {
        return DeletePatientGroup;
    }

    public void setDeletePatientGroup(String deletePatientGroup) {
        DeletePatientGroup = deletePatientGroup;
    }

    public String getUpdateProject() {
        return UpdateProject;
    }

    public void setUpdateProject(String updateProject) {
        UpdateProject = updateProject;
    }

    public String getSaveProject() {
        return saveProject;
    }

    public void setSaveProject(String saveProject) {
        this.saveProject = saveProject;
    }

    public String getProjectList() {
        return ProjectList;
    }

    public void setProjectList(String projectList) {
        ProjectList = projectList;
    }

    public String getProject() {
        return Project;
    }

    public void setProject(String project) {
        Project = project;
    }

    public String getDeleteProject() {
        return deleteProject;
    }

    public void setDeleteProject(String deleteProject) {
        this.deleteProject = deleteProject;
    }

    public String getProjectByCrfId() {
        return ProjectByCrfId;
    }

    public void setProjectByCrfId(String projectByCrfId) {
        ProjectByCrfId = projectByCrfId;
    }

    public String getUpdatePatientSet() {
        return UpdatePatientSet;
    }

    public void setUpdatePatientSet(String updatePatientSet) {
        UpdatePatientSet = updatePatientSet;
    }

    public String getSavePatientSet() {
        return SavePatientSet;
    }

    public void setSavePatientSet(String savePatientSet) {
        SavePatientSet = savePatientSet;
    }

    public String getSearchCondition() {
        return SearchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        SearchCondition = searchCondition;
    }

    public String getPatientSetList() {
        return PatientSetList;
    }

    public void setPatientSetList(String patientSetList) {
        PatientSetList = patientSetList;
    }

    public String getPatientSet() {
        return PatientSet;
    }

    public void setPatientSet(String patientSet) {
        PatientSet = patientSet;
    }

    public String getPatientList() {
        return PatientList;
    }

    public void setPatientList(String patientList) {
        PatientList = patientList;
    }

    public String getContrasAnalyList() {
        return ContrasAnalyList;
    }

    public void setContrasAnalyList(String contrasAnalyList) {
        ContrasAnalyList = contrasAnalyList;
    }

    public String getDeletePatientSet() {
        return DeletePatientSet;
    }

    public void setDeletePatientSet(String deletePatientSet) {
        DeletePatientSet = deletePatientSet;
    }

    public String getSaveGroupCondition() {
        return SaveGroupCondition;
    }

    public void setSaveGroupCondition(String saveGroupCondition) {
        SaveGroupCondition = saveGroupCondition;
    }

    public String getResearchVariableUrl() {
        return ResearchVariableUrl;
    }

    public void setResearchVariableUrl(String researchVariableUrl) {
        ResearchVariableUrl = researchVariableUrl;
    }

    public String getPatientGroupCondition() {
        return PatientGroupCondition;
    }

    public void setPatientGroupCondition(String patientGroupCondition) {
        PatientGroupCondition = patientGroupCondition;
    }

    public String getContResultForPatientUrl() {
        return ContResultForPatientUrl;
    }

    public void setContResultForPatientUrl(String contResultForPatientUrl) {
        ContResultForPatientUrl = contResultForPatientUrl;
    }

    public String getContResultUrl() {
        return ContResultUrl;
    }

    public void setContResultUrl(String contResultUrl) {
        ContResultUrl = contResultUrl;
    }

    public String getDependenceChange() {
        return DependenceChange;
    }

    public void setDependenceChange(String dependenceChange) {
        DependenceChange = dependenceChange;
    }

    public String getCheckActiveDataIsChange() {
        return CheckActiveDataIsChange;
    }

    public void setCheckActiveDataIsChange(String checkActiveDataIsChange) {
        CheckActiveDataIsChange = checkActiveDataIsChange;
    }

    public String getCheckActiveIsOnlyOne() {
        return CheckActiveIsOnlyOne;
    }

    public void setCheckActiveIsOnlyOne(String checkActiveIsOnlyOne) {
        CheckActiveIsOnlyOne = checkActiveIsOnlyOne;
    }

    public String getDeleteByActiveId() {
        return DeleteByActiveId;
    }

    public void setDeleteByActiveId(String deleteByActiveId) {
        DeleteByActiveId = deleteByActiveId;
    }

    public String getFindTotalForImport() {
        return FindTotalForImport;
    }

    public void setFindTotalForImport(String findTotalForImport) {
        FindTotalForImport = findTotalForImport;
    }

    public String getGetCalcTotalByActiveId() {
        return GetCalcTotalByActiveId;
    }

    public void setGetCalcTotalByActiveId(String getCalcTotalByActiveId) {
        GetCalcTotalByActiveId = getCalcTotalByActiveId;
    }

    public String getClacIndexResultSearch() {
        return ClacIndexResultSearch;
    }

    public void setClacIndexResultSearch(String clacIndexResultSearch) {
        ClacIndexResultSearch = clacIndexResultSearch;
    }

    public String getClacResultSearch() {
        return ClacResultSearch;
    }

    public void setClacResultSearch(String clacResultSearch) {
        ClacResultSearch = clacResultSearch;
    }

    public String getSaveOrSearchActive() {
        return SaveOrSearchActive;
    }

    public void setSaveOrSearchActive(String saveOrSearchActive) {
        SaveOrSearchActive = saveOrSearchActive;
    }

    public String getGetSavedActivityData() {
        return GetSavedActivityData;
    }

    public void setGetSavedActivityData(String getSavedActivityData) {
        GetSavedActivityData = getSavedActivityData;
    }

    public String getGetAllActiveOrIndex() {
        return GetAllActiveOrIndex;
    }

    public void setGetAllActiveOrIndex(String getAllActiveOrIndex) {
        GetAllActiveOrIndex = getAllActiveOrIndex;
    }

    public String getFindByProjectId() {
        return FindByProjectId;
    }

    public void setFindByProjectId(String findByProjectId) {
        FindByProjectId = findByProjectId;
    }

    public String getPreFindForProjectData() {
        return PreFindForProjectData;
    }

    public void setPreFindForProjectData(String preFindForProjectData) {
        PreFindForProjectData = preFindForProjectData;
    }

    public String getPreAggregationUrl() {
        return PreAggregationUrl;
    }

    public void setPreAggregationUrl(String preAggregationUrl) {
        PreAggregationUrl = preAggregationUrl;
    }

    public String getPreLiminaryUrl() {
        return PreLiminaryUrl;
    }

    public void setPreLiminaryUrl(String preLiminaryUrl) {
        PreLiminaryUrl = preLiminaryUrl;
    }

    /**
     * 缩略图获取
     */
    private String PatientDetailThumbnail = null;

    public String getPatientDetailThumbnail() {
        return PatientDetailThumbnail;
    }

    public void setPatientDetailThumbnail(String patientDetailThumbnail) {
        PatientDetailThumbnail = patientDetailThumbnail;
    }

    public String getTripleTestTableUrl() {
        return TripleTestTableUrl;
    }

    public void setTripleTestTableUrl(String tripleTestTableUrl) {
        TripleTestTableUrl = tripleTestTableUrl;
    }

    public String getCRFImportMap() {
        return CRFImportMap;
    }

    public void setCRFImportMap(String CRFImportMap) {
        this.CRFImportMap = CRFImportMap;
    }

    private String CRFModelForTraceByCRFID = null;

    public String getCRFModelForTraceByCRFID() {
        return CRFModelForTraceByCRFID;
    }

    public void setCRFModelForTraceByCRFID(String CRFModelForTraceByCRFID) {
        this.CRFModelForTraceByCRFID = CRFModelForTraceByCRFID;
    }

    public String getCRFPatientVisitDetail() {
        return CRFPatientVisitDetail;
    }

    public void setCRFPatientVisitDetail(String CRFPatientVisitDetail) {
        this.CRFPatientVisitDetail = CRFPatientVisitDetail;
    }

    /*
     *溯源页病人详细信息
     */
    private String CRFPatientVisitDetail = null;

    public String getCRFPatientAllVisitsDetail() {
        return CRFPatientAllVisitsDetail;
    }

    public void setCRFPatientAllVisitsDetail(String CRFPatientAllVisitsDetail) {
        this.CRFPatientAllVisitsDetail = CRFPatientAllVisitsDetail;
    }

    /*
     *溯源查看全部病人详细信息
     */
    private String CRFPatientAllVisitsDetail = null;

    public String getSampleImportIURL() {
        return sampleImportIURL;
    }

    public void setSampleImportIURL(String sampleImportIURL) {
        this.sampleImportIURL = sampleImportIURL;
    }

    public String getCRFModelByProjectID() {
        return CRFModelByProjectID;
    }

    public void setCRFModelByProjectID(String CRFModelByProjectID) {
        this.CRFModelByProjectID = CRFModelByProjectID;
    }

    public String getCRFModelByCRFID() {
        return CRFModelByCRFID;
    }

    public void setCRFModelByCRFID(String CRFModelByCRFID) {
        this.CRFModelByCRFID = CRFModelByCRFID;
    }

    public String getCRFEditModel() {
        return CRFEditModel;
    }

    public void setCRFEditModel(String CRFEditModel) {
        this.CRFEditModel = CRFEditModel;
    }

    public String getCRFSaveModel() {
        return CRFSaveModel;
    }

    public void setCRFSaveModel(String CRFSaveModel) {
        this.CRFSaveModel = CRFSaveModel;
    }

    public String getCRFGetData() {
        return CRFGetData;
    }

    public void setCRFGetData(String CRFGetData) {
        this.CRFGetData = CRFGetData;
    }

    public String getCRFUpLoadData() {
        return CRFUpLoadData;
    }

    public void setCRFUpLoadData(String CRFUpLoadData) {
        this.CRFUpLoadData = CRFUpLoadData;
    }

    public String getCRFSaveData() {
        return CRFSaveData;
    }

    public void setCRFSaveData(String CRFSaveData) {
        this.CRFSaveData = CRFSaveData;
    }

    public String getCRFSampleCaseList() {
        return CRFSampleCaseList;
    }

    public void setCRFSampleCaseList(String CRFSampleCaseList) {
        this.CRFSampleCaseList = CRFSampleCaseList;
    }

    public String getCRFDeleteSample() {
        return CRFDeleteSample;
    }

    public void setCRFDeleteSample(String CRFDeleteSample) {
        this.CRFDeleteSample = CRFDeleteSample;
    }

    public String getCRFSearchSampleList() {
        return CRFSearchSampleList;
    }

    public void setCRFSearchSampleList(String CRFSearchSampleList) {
        this.CRFSearchSampleList = CRFSearchSampleList;
    }

    public String getCRFGetAttrID() {
        return CRFGetAttrID;
    }

    public void setCRFGetAttrID(String CRFGetAttrID) {
        this.CRFGetAttrID = CRFGetAttrID;
    }

    public String getCRFGetGroupID() {
        return CRFGetGroupID;
    }

    public void setCRFGetGroupID(String CRFGetGroupID) {
        this.CRFGetGroupID = CRFGetGroupID;
    }

    public String getCRFAutoMapURL() {
        return CRFAutoMapURL;
    }

    public void setCRFAutoMapURL(String CRFAutoMapURL) {
        this.CRFAutoMapURL = CRFAutoMapURL;
    }

    public String getCRFProjectCRFListURL() {
        return CRFProjectCRFListURL;
    }

    public void setCRFProjectCRFListURL(String CRFProjectCRFListURL) {
        this.CRFProjectCRFListURL = CRFProjectCRFListURL;
    }

    public String getCaseGeneErrorURL() {
        return caseGeneErrorURL;
    }

    public void setCaseGeneErrorURL(String caseGeneErrorURL) {
        this.caseGeneErrorURL = caseGeneErrorURL;
    }

    public String getKnowledgePharmGKBSearchDrugURL() {
        return knowledgePharmGKBSearchDrugURL;
    }

    public void setKnowledgePharmGKBSearchDrugURL(String knowledgePharmGKBSearchDrugURL) {
        this.knowledgePharmGKBSearchDrugURL = knowledgePharmGKBSearchDrugURL;
    }

    public String getCaseExam_result() {
        return caseExam_result;
    }

    public void setCaseExam_result(String caseExam_result) {
        this.caseExam_result = caseExam_result;
    }

    public String getCasePathological_examination() {
        return casePathological_examination;
    }

    public void setCasePathological_examination(String casePathological_examination) {
        this.casePathological_examination = casePathological_examination;
    }

    public String getCaseGenetic_disease() {
        return caseGenetic_disease;
    }

    public void setCaseGenetic_disease(String caseGenetic_disease) {
        this.caseGenetic_disease = caseGenetic_disease;
    }

    public String getCaseDrug_reaction() {
        return caseDrug_reaction;
    }

    public void setCaseDrug_reaction(String caseDrug_reaction) {
        this.caseDrug_reaction = caseDrug_reaction;
    }

    public String getCaseCategory_catalog() {
        return caseCategory_catalog;
    }

    public void setCaseCategory_catalog(String caseCategory_catalog) {
        this.caseCategory_catalog = caseCategory_catalog;
    }

    public String getCaseMolecular_detection() {
        return caseMolecular_detection;
    }

    public void setCaseMolecular_detection(String caseMolecular_detection) {
        this.caseMolecular_detection = caseMolecular_detection;
    }

    public String getCaseBiological_specimen() {
        return caseBiological_specimen;
    }

    public void setCaseBiological_specimen(String caseBiological_specimen) {
        this.caseBiological_specimen = caseBiological_specimen;
    }

    public String getKnowledgeDetailVariationSearchDiseaseURL() {
        return knowledgeDetailVariationSearchDiseaseURL;
    }

    public void setKnowledgeDetailVariationSearchDiseaseURL(String knowledgeDetailVariationSearchDiseaseURL) {
        this.knowledgeDetailVariationSearchDiseaseURL = knowledgeDetailVariationSearchDiseaseURL;
    }

    public String getKnowledgeDetailVariationSearchDrugURL() {
        return knowledgeDetailVariationSearchDrugURL;
    }

    public void setKnowledgeDetailVariationSearchDrugURL(String knowledgeDetailVariationSearchDrugURL) {
        this.knowledgeDetailVariationSearchDrugURL = knowledgeDetailVariationSearchDrugURL;
    }

    public String getKnowledgeVariationInfoURL() {
        return knowledgeVariationInfoURL;
    }

    public void setKnowledgeVariationInfoURL(String knowledgeVariationInfoURL) {
        this.knowledgeVariationInfoURL = knowledgeVariationInfoURL;
    }

    public String getKnowledgeGeneInfoURL() {
        return knowledgeGeneInfoURL;
    }

    public void setKnowledgeGeneInfoURL(String knowledgeGeneInfoURL) {
        this.knowledgeGeneInfoURL = knowledgeGeneInfoURL;
    }

    public String getCaseLab_result_item_list() {
        return caseLab_result_item_list;
    }

    public void setCaseLab_result_item_list(String caseLab_result_item_list) {
        this.caseLab_result_item_list = caseLab_result_item_list;
    }

    public String getKnowledgeDiseaseSearchGenesURL() {
        return knowledgeDiseaseSearchGenesURL;
    }

    public void setKnowledgeDiseaseSearchGenesURL(String knowledgeDiseaseSearchGenesURL) {
        this.knowledgeDiseaseSearchGenesURL = knowledgeDiseaseSearchGenesURL;
    }

    public String getCaseLab_result_item() {
        return caseLab_result_item;
    }

    public void setCaseLab_result_item(String caseLab_result_item) {
        this.caseLab_result_item = caseLab_result_item;
    }

    public String getCaseVisit_detail() {
        return caseVisit_detail;
    }

    public void setCaseVisit_detail(String caseVisit_detail) {
        this.caseVisit_detail = caseVisit_detail;
    }

    public String getKnowledgeURL() {
        return knowledgeURL;
    }

    public void setKnowledgeURL(String knowledgeURL) {
        this.knowledgeURL = knowledgeURL;
    }

    public String getCasePhysical_examination_list() {
        return casePhysical_examination_list;
    }

    public void setCasePhysical_examination_list(String casePhysical_examination_list) {
        this.casePhysical_examination_list = casePhysical_examination_list;
    }

    public String getCasePhysical_examination() {
        return casePhysical_examination;
    }

    public void setCasePhysical_examination(String casePhysical_examination) {
        this.casePhysical_examination = casePhysical_examination;
    }

    public String getCaseSuggestURL() {
        return caseSuggestURL;
    }

    public void setCaseSuggestURL(String caseSuggestURL) {
        this.caseSuggestURL = caseSuggestURL;
    }

    public String getCaseSuggestURL2() {
        return caseSuggestURL2;
    }

    public void setCaseSuggestURL2(String caseSuggestURL2) {
        this.caseSuggestURL2 = caseSuggestURL2;
    }

    public String getCasePatientBasicTimeAxisURL() {
        return casePatientBasicTimeAxisURL;
    }

    public void setCasePatientBasicTimeAxisURL(String casePatientBasicTimeAxisURL) {
        this.casePatientBasicTimeAxisURL = casePatientBasicTimeAxisURL;
    }

    public String getCaseSearchURL() {
        return caseSearchURL;
    }

    public void setCaseSearchURL(String caseSearchURL) {
        this.caseSearchURL = caseSearchURL;
    }

    public String getCaseDetailPatientBasicInfoURL() {
        return caseDetailPatientBasicInfoURL;
    }

    public void setCaseDetailPatientBasicInfoURL(String caseDetailPatientBasicInfoURL) {
        this.caseDetailPatientBasicInfoURL = caseDetailPatientBasicInfoURL;
    }

    public String getCaseDetailPatientBasicFigureURL() {
        return caseDetailPatientBasicFigureURL;
    }

    public void setCaseDetailPatientBasicFigureURL(String caseDetailPatientBasicFigureURL) {
        this.caseDetailPatientBasicFigureURL = caseDetailPatientBasicFigureURL;
    }

    public String getCRFGetPatientInfo() {
        return CRFGetPatientInfo;
    }

    public void setCRFGetPatientInfo(String CRFGetPatientInfo) {
        this.CRFGetPatientInfo = CRFGetPatientInfo;
    }

    public String getRRun() {
        return RRun;
    }

    public void setRRun(String RRun) {
        this.RRun = RRun;
    }

    public String getRStop() {
        return RStop;
    }

    public void setRStop(String RStop) {
        this.RStop = RStop;
    }

    public String getRSave() {
        return RSave;
    }

    public void setRSave(String RSave) {
        this.RSave = RSave;
    }

    public String getRLoad() {
        return RLoad;
    }

    public void setRLoad(String RLoad) {
        this.RLoad = RLoad;
    }

    public String getRList() {
        return RList;
    }

    public void setRList(String RList) {
        this.RList = RList;
    }

    public String getCaseDiagnose() {
        return caseDiagnose;
    }

    public void setCaseDiagnose(String caseDiagnose) {
        this.caseDiagnose = caseDiagnose;
    }

    public String getSampleDetailURL() {
        return sampleDetailURL;
    }

    public void setSampleDetailURL(String sampleDetailURL) {
        this.sampleDetailURL = sampleDetailURL;
    }

    public String getSampleDaleteURL() {
        return sampleDaleteURL;
    }

    public void setSampleDaleteURL(String sampleDaleteURL) {
        this.sampleDaleteURL = sampleDaleteURL;
    }

    public String getToolsURL() {
        return toolsURL;
    }

    public void setToolsURL(String toolsURL) {
        this.toolsURL = toolsURL;
    }

    public String getAtoolURL() {
        return AtoolURL;
    }

    public void setAtoolURL(String atoolURL) {
        AtoolURL = atoolURL;
    }

    public String getCaseMedicalRecord() {
        return caseMedicalRecord;
    }

    public void setCaseMedicalRecord(String caseMedicalRecord) {
        this.caseMedicalRecord = caseMedicalRecord;
    }

    public String getCaseOperationRecords() {
        return caseOperationRecords;
    }

    public void setCaseOperationRecords(String caseOperationRecords) {
        this.caseOperationRecords = caseOperationRecords;
    }

    public String getCasePharmacy() {
        return casePharmacy;
    }

    public void setCasePharmacy(String casePharmacy) {
        this.casePharmacy = casePharmacy;
    }

    public String getCaseDischargeRecords() {
        return caseDischargeRecords;
    }

    public void setCaseDischargeRecords(String caseDischargeRecords) {
        this.caseDischargeRecords = caseDischargeRecords;
    }

    public String getCaseCourseRecords() {
        return caseCourseRecords;
    }

    public void setCaseCourseRecords(String caseCourseRecords) {
        this.caseCourseRecords = caseCourseRecords;
    }

    public String getCaseMedicalCourse() {
        return caseMedicalCourse;
    }

    public void setCaseMedicalCourse(String caseMedicalCourse) {
        this.caseMedicalCourse = caseMedicalCourse;
    }

    public String getCSSmg() {
        return CSSmg;
    }

    public void setCSSmg(String CSSmg) {
        this.CSSmg = CSSmg;
    }

    public String getCRFUpLoadDataForCheck() {
        return CRFUpLoadDataForCheck;
    }

    public void setCRFUpLoadDataForCheck(String CRFUpLoadDataForCheck) {
        this.CRFUpLoadDataForCheck = CRFUpLoadDataForCheck;
    }

    public String getEmailSendURL() {
        return EmailSendURL;
    }

    public void setEmailSendURL(String emailSendURL) {
        EmailSendURL = emailSendURL;
    }

    public String getFileStoreForCRFImport() {
        return fileStoreForCRFImport;
    }

    public void setFileStoreForCRFImport(String fileStoreForCRFImport) {
        this.fileStoreForCRFImport = fileStoreForCRFImport;
    }

    public String getCRFImportFile() {
        return CRFImportFile;
    }

    public void setCRFImportFile(String CRFImportFile) {
        this.CRFImportFile = CRFImportFile;
    }

    public String getSampleDetailSearchURL() {
        return sampleDetailSearchURL;
    }

    public void setSampleDetailSearchURL(String sampleDetailSearchURL) {
        this.sampleDetailSearchURL = sampleDetailSearchURL;
    }

    public String getCRFImportResult() {
        return CRFImportResult;
    }

    public void setCRFImportResult(String CRFImportResult) {
        this.CRFImportResult = CRFImportResult;
    }

    public String getSampleUploadAdaptTagURL() {
        return sampleUploadAdaptTagURL;
    }

    public void setSampleUploadAdaptTagURL(String sampleUploadAdaptTagURL) {
        this.sampleUploadAdaptTagURL = sampleUploadAdaptTagURL;
    }

    public String getCaseDetailVisitClassifySectionURL() {
        return caseDetailVisitClassifySectionURL;
    }

    public void setCaseDetailVisitClassifySectionURL(String caseDetailVisitClassifySectionURL) {
        this.caseDetailVisitClassifySectionURL = caseDetailVisitClassifySectionURL;
    }

    public String getCaseDetailVisitClassifyImageURL() {
        return caseDetailVisitClassifyImageURL;
    }

    public void setCaseDetailVisitClassifyImageURL(String caseDetailVisitClassifyImageURL) {
        this.caseDetailVisitClassifyImageURL = caseDetailVisitClassifyImageURL;
    }

    public String getCSBaseline() {
        return CSBaseline;
    }

    public void setCSBaseline(String CSBaseline) {
        this.CSBaseline = CSBaseline;
    }

    public String getCRFIsExistPatient() {
        return CRFIsExistPatient;
    }

    public void setCRFIsExistPatient(String CRFIsExistPatient) {
        this.CRFIsExistPatient = CRFIsExistPatient;
    }

    public String getCaseVisitDcOrder() {
        return caseVisitDcOrder;
    }

    public void setCaseVisitDcOrder(String caseVisitDcOrder) {
        this.caseVisitDcOrder = caseVisitDcOrder;
    }

    public String getCasePatientDcOrder() {
        return casePatientDcOrder;
    }

    public void setCasePatientDcOrder(String casePatientDcOrder) {
        this.casePatientDcOrder = casePatientDcOrder;
    }

    public String getCasePatientRadiotherapy() {
        return casePatientRadiotherapy;
    }

    public void setCasePatientRadiotherapy(String casePatientRadiotherapy) {
        this.casePatientRadiotherapy = casePatientRadiotherapy;
    }

    public String getCaseyVisitRadiotherapy() {
        return caseyVisitRadiotherapy;
    }

    public void setCaseyVisitRadiotherapy(String caseyVisitRadiotherapy) {
        this.caseyVisitRadiotherapy = caseyVisitRadiotherapy;
    }

    public String getCasePatientChemotherapyInfo() {
        return casePatientChemotherapyInfo;
    }

    public void setCasePatientChemotherapyInfo(String casePatientChemotherapyInfo) {
        this.casePatientChemotherapyInfo = casePatientChemotherapyInfo;
    }

    public String getCaseSimilarServiceGetSimilars() {
        return caseSimilarServiceGetSimilars;
    }

    public void setCaseSimilarServiceGetSimilars(String caseSimilarServiceGetSimilars) {
        this.caseSimilarServiceGetSimilars = caseSimilarServiceGetSimilars;
    }

    public String getCaseSimilarServiceVitaboardparam() {
        return caseSimilarServiceVitaboardparam;
    }

    public void setCaseSimilarServiceVitaboardparam(String caseSimilarServiceVitaboardparam) {
        this.caseSimilarServiceVitaboardparam = caseSimilarServiceVitaboardparam;
    }

    public String getCaseSimilarServiceSimilarParam() {
        return caseSimilarServiceSimilarParam;
    }

    public void setCaseSimilarServiceSimilarParam(String caseSimilarServiceSimilarParam) {
        this.caseSimilarServiceSimilarParam = caseSimilarServiceSimilarParam;
    }

    public String getCaseSimilarServiceSimilarQuery() {
        return caseSimilarServiceSimilarQuery;
    }

    public void setCaseSimilarServiceSimilarQuery(String caseSimilarServiceSimilarQuery) {
        this.caseSimilarServiceSimilarQuery = caseSimilarServiceSimilarQuery;
    }

    public String getCaseSimilarServiceSimilarBasicDetail() {
        return caseSimilarServiceSimilarBasicDetail;
    }

    public void setCaseSimilarServiceSimilarBasicDetail(String caseSimilarServiceSimilarBasicDetail) {
        this.caseSimilarServiceSimilarBasicDetail = caseSimilarServiceSimilarBasicDetail;
    }

    public String getCaseSimilarServiceSimilarPatientInfo() {
        return caseSimilarServiceSimilarPatientInfo;
    }

    public void setCaseSimilarServiceSimilarPatientInfo(String caseSimilarServiceSimilarPatientInfo) {
        this.caseSimilarServiceSimilarPatientInfo = caseSimilarServiceSimilarPatientInfo;
    }

    public String getVitaGramServerVitaGramData() {
        return vitaGramServerVitaGramData;
    }

    public void setVitaGramServerVitaGramData(String vitaGramServerVitaGramData) {
        this.vitaGramServerVitaGramData = vitaGramServerVitaGramData;
    }

    public String getVitaGramServerNccnRecommendPath() {
        return vitaGramServerNccnRecommendPath;
    }

    public void setVitaGramServerNccnRecommendPath(String vitaGramServerNccnRecommendPath) {
        this.vitaGramServerNccnRecommendPath = vitaGramServerNccnRecommendPath;
    }

    public String getVitaGramServerMedicalHistoryRecommendPath() {
        return vitaGramServerMedicalHistoryRecommendPath;
    }

    public void setVitaGramServerMedicalHistoryRecommendPath(String vitaGramServerMedicalHistoryRecommendPath) {
        this.vitaGramServerMedicalHistoryRecommendPath = vitaGramServerMedicalHistoryRecommendPath;
    }

    public String getCaseSimilarServiceSimilarPatientExtraInfo() {
        return caseSimilarServiceSimilarPatientExtraInfo;
    }

    public void setCaseSimilarServiceSimilarPatientExtraInfo(String caseSimilarServiceSimilarPatientExtraInfo) {
        this.caseSimilarServiceSimilarPatientExtraInfo = caseSimilarServiceSimilarPatientExtraInfo;
    }

    public void setCasePatientBatchData(String casePatientBatchData) {
        this.casePatientBatchData = casePatientBatchData;
    }

    public String getCasePatientBatchData() {
        return casePatientBatchData;
    }

    public void setCrfCsvImportDetail(String CrfCsvImportDetail) {
        this.CrfCsvImportDetail = CrfCsvImportDetail;
    }

    public String getCrfCsvImportDetail() {
        return this.CrfCsvImportDetail;
    }

    public void setConsultingRoomData(String ConsultingRoomData) {
        consultingRoomData = ConsultingRoomData;
    }

    public String getConsultingRoomData() {
        return consultingRoomData;
    }

    public String getInputSmartPrompt() {
        return InputSmartPrompt;
    }

    public void setInputSmartPrompt(String InputSmartPrompt) {
        this.InputSmartPrompt = InputSmartPrompt;
    }

    public void setImageUpload(String ImageUpload) {
        this.ImageUpload = ImageUpload;
    }

    public String getImageUpload() {
        return this.ImageUpload;
    }

    public void setImageDel(String ImageDel) {
        this.ImageDel = ImageDel;
    }

    public String getImageDel() {
        return this.ImageDel;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getImageUrl() {
        return this.ImageUrl;
    }

    public void setICD_10_CodeUrl(String code) {
        this.ICD_10_CodeUrl = code;
    }

    public String getICD_10_CodeUrl() {
        return ICD_10_CodeUrl;
    }

    public void setResearchNumberUrl(String ResearchNumberUrl) {
        this.ResearchNumberUrl = ResearchNumberUrl;
    }

    public String getResearchNumberUrl() {
        return ResearchNumberUrl;
    }

    public void setFsIpAndPort(String fsIpAndPort) {
        this.fsIpAndPort = fsIpAndPort;
    }

    public String getFsIpAndPort() {
        return fsIpAndPort;
    }

    public void setCaseDetailCommonUrl(String caseDetailCommonUrl) {
        if (!caseDetailCommonUrl.endsWith("/")) caseDetailCommonUrl = caseDetailCommonUrl + "/";
        this.caseDetailCommonUrl = caseDetailCommonUrl;
    }

    public String getCaseDetailCommonUrl() {
        return caseDetailCommonUrl;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getAddSynonym() {
        return addSynonym;
    }

    public void setAddSynonym(String addSynonym) {
        this.addSynonym = addSynonym;
    }

    public String getRemoveSynonym() {
        return removeSynonym;
    }

    public void setRemoveSynonym(String removeSynonym) {
        this.removeSynonym = removeSynonym;
    }

    public String getSaveRelatedPhrasesSelectionBehavior() {
        return saveRelatedPhrasesSelectionBehavior;
    }

    public void setSaveRelatedPhrasesSelectionBehavior(String saveRelatedPhrasesSelectionBehavior) {
        this.saveRelatedPhrasesSelectionBehavior = saveRelatedPhrasesSelectionBehavior;
    }

    public String getEmailURL() {
        return EmailURL;
    }

    public void setEmailURL(String emailURL) {
        EmailURL = emailURL;
    }

    public String getCrfSearchURL() {
        return crfSearchURL;
    }

    public void setCrfSearchURL(String crfSearchURL) {
        this.crfSearchURL = crfSearchURL;
    }

    public String getGetGennomics() {
        return getGennomics;
    }

    public void setGetGennomics(String getGennomics) {
        this.getGennomics = getGennomics;
    }

    public String getApplyOutGoing() {
        return applyOutGoing;
    }

    public void setApplyOutGoing(String applyOutGoing) {
        this.applyOutGoing = applyOutGoing;
    }

    public String getGetPatienSn() {
        return getPatienSn;
    }

    public void setGetPatienSn(String getPatienSn) {
        this.getPatienSn = getPatienSn;
    }

    public String getTripleTestTable() {
        return tripleTestTable;
    }

    public void setTripleTestTable(String tripleTestTable) {
        this.tripleTestTable = tripleTestTable;
    }
}
