/*
Navicat MySQL Data Transfer

Source Server         : 10.0.0.180
Source Server Version : 50716
Source Host           : 10.0.0.180:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-04-17 15:39:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `gennlife_group`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_group`;
CREATE TABLE `gennlife_group` (
  `gid` varchar(100) NOT NULL,
  `groupName` varchar(30) DEFAULT NULL,
  `groupDesc` varchar(20) DEFAULT NULL COMMENT '用户组描述',
  `orgID` varchar(100) NOT NULL,
  `groupCreator` varchar(50) DEFAULT NULL COMMENT '用户组创建者',
  `groupCreatTime` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `has_search` varchar(4) DEFAULT NULL COMMENT '搜索EMR权限',
  `has_searchExport` varchar(4) DEFAULT NULL COMMENT '搜索导出权限',
  `has_traceCRF` varchar(4) DEFAULT NULL COMMENT '溯源CRF权限',
  `has_addCRF` varchar(4) DEFAULT NULL COMMENT '录入CRF权限',
  `has_editCRF` varchar(4) DEFAULT NULL COMMENT '编辑CRF权限',
  `has_deleteCRF` varchar(4) DEFAULT NULL COMMENT '删除CRF权限',
  `has_addBatchCRF` varchar(4) DEFAULT NULL COMMENT '导入CRF权限',
  `has_browseDetail` varchar(4) DEFAULT NULL COMMENT '查看病人详情权限',
  PRIMARY KEY (`orgID`,`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_group
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_lab`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_lab`;
CREATE TABLE `gennlife_lab` (
  `orgID` varchar(100) NOT NULL,
  `labID` varchar(200) NOT NULL,
  `lab_name` varchar(30) DEFAULT NULL,
  `lab_leader` varchar(50) DEFAULT NULL,
  `lab_leaderName` varchar(50) DEFAULT NULL COMMENT '科室负责人',
  `lab_parent` varchar(200) DEFAULT NULL,
  `lab_level` tinyint(4) DEFAULT NULL COMMENT '科室类型',
  `add_user` varchar(50) DEFAULT NULL COMMENT '添加的用户',
  `add_time` varchar(30) DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`orgID`,`labID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_lab
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_lab_crf`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_lab_crf`;
CREATE TABLE `gennlife_lab_crf` (
  `labID` varchar(200) NOT NULL,
  `orgID` varchar(100) NOT NULL,
  `crf_id` varchar(50) DEFAULT NULL,
  `crf_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`orgID`,`labID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_lab_crf
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_lab_map`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_lab_map`;
CREATE TABLE `gennlife_lab_map` (
  `lab_id` varchar(100) NOT NULL,
  `depart_name` varchar(30) NOT NULL,
  PRIMARY KEY (`lab_id`,`depart_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_lab_map
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_manage`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_manage`;
CREATE TABLE `gennlife_manage` (
  `uid` varchar(50) NOT NULL COMMENT '用户email',
  `privilegeType` varchar(20) DEFAULT NULL COMMENT '管理员权限等级',
  `privilegeValue` varchar(30) DEFAULT NULL COMMENT '管理员权限描述值',
  `orgID` varchar(100) NOT NULL,
  PRIMARY KEY (`orgID`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_manage
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_org`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_org`;
CREATE TABLE `gennlife_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `orgID` varchar(100) NOT NULL,
  `org_name` varchar(30) DEFAULT NULL,
  `leader` varchar(30) DEFAULT NULL COMMENT '负责人',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`,`orgID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_org
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_org_profession`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_org_profession`;
CREATE TABLE `gennlife_org_profession` (
  `orgID` varchar(100) NOT NULL,
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uprofession` varchar(30) DEFAULT NULL COMMENT '职位名称',
  `ulevel` int(11) DEFAULT NULL COMMENT '职位等级',
  PRIMARY KEY (`id`,`orgID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_org_profession
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_resource`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_resource`;
CREATE TABLE `gennlife_resource` (
  `sid` varchar(100) NOT NULL,
  `sname` varchar(50) DEFAULT NULL COMMENT '资源名称',
  `sdesc` varchar(100) DEFAULT NULL COMMENT '资源描述',
  `stype` varchar(30) DEFAULT NULL COMMENT '资源类型',
  `slab_type` varchar(30) DEFAULT NULL COMMENT '科室类型',
  `slab_name` varchar(50) DEFAULT NULL COMMENT '科室名称',
  `sorgID` varchar(100) NOT NULL,
  `slab_parent` varchar(100) DEFAULT NULL,
  `stype_role` varchar(50) DEFAULT NULL COMMENT '资源角色类型，0代表普通资源，1代表本科室资源',
  PRIMARY KEY (`sorgID`,`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_resource
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_role`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_role`;
CREATE TABLE `gennlife_role` (
  `roleid` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id，角色id',
  `role` varchar(20) DEFAULT NULL COMMENT '角色名称',
  `orgID` varchar(100) NOT NULL,
  `desctext` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `role_type` varchar(50) DEFAULT NULL COMMENT '角色类型，0代表普通角色，1代表科室成员角色',
  `ctime` varchar(30) DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creatorID` varchar(100) DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`roleid`,`orgID`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_role
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_role_resource`;
CREATE TABLE `gennlife_role_resource` (
  `roleid` int(20) NOT NULL COMMENT '角色id',
  `sid` varchar(100) NOT NULL,
  `orgID` varchar(100) NOT NULL,
  `has_search` varchar(4) DEFAULT NULL COMMENT '搜索病例',
  `has_searchExport` varchar(4) DEFAULT NULL COMMENT '导出搜索结果',
  `has_traceCRF` varchar(4) DEFAULT NULL COMMENT '溯源CRF',
  `has_addCRF` varchar(4) DEFAULT NULL COMMENT '新增CRF',
  `has_editCRF` varchar(4) DEFAULT NULL COMMENT '编辑CRF',
  `has_deleteCRF` varchar(4) DEFAULT NULL COMMENT '删除CRF',
  `has_browseDetail` varchar(4) DEFAULT NULL COMMENT '查看病例详情',
  `has_addBatchCRF` varchar(4) DEFAULT NULL COMMENT '批量导入CRF',
  PRIMARY KEY (`orgID`,`sid`,`roleid`),
  KEY `roleid_sid` (`roleid`,`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_role_resource
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_user`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_user`;
CREATE TABLE `gennlife_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(100) NOT NULL COMMENT '用户id',
  `pwd` varchar(40) DEFAULT NULL COMMENT '用户密码加密后的',
  `uname` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `uemail` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `uposition` varchar(20) DEFAULT NULL COMMENT '职位',
  `uprofession` varchar(20) DEFAULT NULL COMMENT '职称',
  `orgID` varchar(100) DEFAULT NULL,
  `org_name` varchar(50) DEFAULT NULL COMMENT '组织名称',
  `labID` varchar(200) DEFAULT NULL,
  `lab_name` varchar(50) DEFAULT NULL COMMENT '科室名称',
  `telphone` varchar(20) DEFAULT NULL COMMENT '电话',
  `age` tinyint(4) DEFAULT NULL COMMENT '年龄',
  `sex` tinyint(6) DEFAULT NULL COMMENT '性别，1为男，0为女',
  `ctime` varchar(30) DEFAULT NULL COMMENT 'yyyy-MM-dd HH:mm:ss格式时间',
  `uptime` varchar(30) DEFAULT NULL COMMENT '最后一次修改数据时间',
  `unumber` varchar(50) DEFAULT NULL COMMENT '工号',
  `md5` varchar(40) DEFAULT NULL COMMENT '修改密码的md5校验',
  PRIMARY KEY (`id`,`uid`),
  KEY `index_name` (`uemail`,`pwd`),
  KEY `uemail_pwd` (`uemail`,`pwd`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_user
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_user_group`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_user_group`;
CREATE TABLE `gennlife_user_group` (
  `gid` varchar(100) NOT NULL COMMENT '用户组id',
  `uid` varchar(100) NOT NULL COMMENT '用户id',
  `orgID` varchar(100) NOT NULL,
  PRIMARY KEY (`orgID`,`uid`,`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_user_group
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_user_role`;
CREATE TABLE `gennlife_user_role` (
  `uid` varchar(50) NOT NULL COMMENT '用户id',
  `roleid` int(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`uid`,`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for `gennlife_user_vitaConfig`
-- ----------------------------
DROP TABLE IF EXISTS `gennlife_user_vitaConfig`;
CREATE TABLE `gennlife_user_vitaConfig` (
  `uid` varchar(100) NOT NULL COMMENT '用户id',
  `dataArray` text COMMENT 'vitabroad配置',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gennlife_user_vitaConfig
-- ----------------------------

-- ----------------------------
-- Table structure for `p_org`
-- ----------------------------
DROP TABLE IF EXISTS `p_org`;
CREATE TABLE `p_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `orgID` varchar(30) NOT NULL COMMENT '机构id',
  `orgName` varchar(100) DEFAULT NULL COMMENT '机构名称',
  `leader` varchar(30) DEFAULT NULL COMMENT '负责人',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`,`orgID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_org
-- ----------------------------

-- ----------------------------
-- Table structure for `p_plan`
-- ----------------------------
DROP TABLE IF EXISTS `p_plan`;
CREATE TABLE `p_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '方案id',
  `projectID` varchar(50) NOT NULL COMMENT '项目id',
  `planDesc` text COMMENT '详细描述',
  `planName` varchar(30) DEFAULT NULL COMMENT '方案名称',
  `creater` varchar(30) DEFAULT NULL COMMENT '创建者',
  `planStatus` int(11) DEFAULT NULL COMMENT '状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(50) DEFAULT NULL COMMENT '修改者',
  `modifTime` datetime DEFAULT NULL COMMENT '修改时间',
  `taskID` varchar(50) DEFAULT NULL COMMENT '子任务id',
  `taskName` varchar(50) DEFAULT NULL COMMENT '字任务名称',
  `sampleName` varchar(50) DEFAULT NULL COMMENT '数据集合名称',
  `creator` varchar(50) DEFAULT NULL,
  `plandelete` tinyint(4) DEFAULT '0' COMMENT '1 delete',
  PRIMARY KEY (`id`,`projectID`)
) ENGINE=InnoDB AUTO_INCREMENT=345 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_plan
-- ----------------------------

-- ----------------------------
-- Table structure for `p_project`
-- ----------------------------
DROP TABLE IF EXISTS `p_project`;
CREATE TABLE `p_project` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `projectID` varchar(50) NOT NULL COMMENT '项目号',
  `projectName` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `startTime` datetime DEFAULT NULL COMMENT '课题开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '课题结束时间',
  `center` varchar(20) DEFAULT NULL COMMENT '中心',
  `members` int(11) DEFAULT NULL COMMENT '成员数',
  `planNum` int(11) DEFAULT NULL COMMENT '方案数',
  `creator` varchar(100) NOT NULL DEFAULT '',
  `projectEngName` char(200) DEFAULT NULL COMMENT '英文名',
  `projectDesp` text,
  `unit` char(50) DEFAULT NULL,
  `manager` char(50) DEFAULT NULL,
  `type` char(50) DEFAULT NULL,
  `setCount` int(11) NOT NULL DEFAULT '0' COMMENT '样本集数目',
  `disease` varchar(50) NOT NULL DEFAULT '' COMMENT '病种',
  `registerNumber` char(100) DEFAULT NULL,
  `creatorName` varchar(100) NOT NULL DEFAULT '',
  `isdelete` tinyint(4) DEFAULT '0' COMMENT '1 del',
  PRIMARY KEY (`id`,`projectID`),
  UNIQUE KEY `projectID` (`projectID`)
) ENGINE=InnoDB AUTO_INCREMENT=418 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_project
-- ----------------------------

-- ----------------------------
-- Table structure for `p_prolog`
-- ----------------------------
DROP TABLE IF EXISTS `p_prolog`;
CREATE TABLE `p_prolog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `projectID` varchar(50) NOT NULL COMMENT '项目ID',
  `planName` varchar(30) DEFAULT NULL COMMENT '方案ID',
  `uid` varchar(50) DEFAULT NULL COMMENT '操作',
  `action` varchar(30) DEFAULT NULL COMMENT '动作',
  `logTime` datetime DEFAULT NULL COMMENT '记录日志时间',
  `logText` text NOT NULL COMMENT '日志内容',
  `sampleName` varchar(100) DEFAULT NULL COMMENT '数据集合名称',
  `sampleURI` text COMMENT 'uri',
  PRIMARY KEY (`id`,`projectID`),
  KEY `p_prolog_projectID` (`projectID`)
) ENGINE=InnoDB AUTO_INCREMENT=2117 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_prolog
-- ----------------------------

-- ----------------------------
-- Table structure for `p_userWords`
-- ----------------------------
DROP TABLE IF EXISTS `p_userWords`;
CREATE TABLE `p_userWords` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(50) NOT NULL COMMENT '用户id',
  `keywords` varchar(200) DEFAULT NULL COMMENT '搜索关键词',
  `counter` int(11) DEFAULT NULL COMMENT '关键词被当前用户搜索次数',
  PRIMARY KEY (`id`,`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=347 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_userWords
-- ----------------------------

-- ----------------------------
-- Table structure for `pro_org`
-- ----------------------------
DROP TABLE IF EXISTS `pro_org`;
CREATE TABLE `pro_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `projectID` varchar(50) NOT NULL COMMENT '项目id',
  `orgID` varchar(30) NOT NULL COMMENT '机构id',
  `correlation` int(11) DEFAULT NULL COMMENT '关系：申办，监督，组长单位',
  PRIMARY KEY (`id`,`orgID`,`projectID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pro_org
-- ----------------------------

-- ----------------------------
-- Table structure for `pro_sample`
-- ----------------------------
DROP TABLE IF EXISTS `pro_sample`;
CREATE TABLE `pro_sample` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `projectID` varchar(50) NOT NULL COMMENT '项目ID',
  `batchID` int(11) DEFAULT NULL COMMENT '批次号',
  `planName` varchar(30) DEFAULT NULL COMMENT '方案名称',
  `bstatus` int(11) DEFAULT NULL COMMENT '批次状态，-1:删除，1,初始化，2,中间数据集',
  `sampleURI` varchar(100) NOT NULL COMMENT '样本集合uri',
  `sampleName` varchar(200) DEFAULT NULL COMMENT '样本集名称',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作者',
  `opTime` datetime DEFAULT NULL COMMENT '操作时间',
  `total` int(11) DEFAULT NULL COMMENT '导入的样本数量',
  `items` text COMMENT '样本属性列表',
  `sampleDesc` text,
  `sampledelete` tinyint(4) DEFAULT '0' COMMENT '1 del',
  PRIMARY KEY (`id`,`projectID`,`sampleURI`)
) ENGINE=InnoDB AUTO_INCREMENT=627 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pro_sample
-- ----------------------------

-- ----------------------------
-- Table structure for `pro_user`
-- ----------------------------
DROP TABLE IF EXISTS `pro_user`;
CREATE TABLE `pro_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(50) NOT NULL COMMENT '用户id',
  `projectID` varchar(50) NOT NULL COMMENT '项目id',
  `role` int(11) DEFAULT NULL COMMENT '用户对于项目的角色，1代表项目的创建者',
  `puisdelete` tinyint(4) DEFAULT '0' COMMENT '1 del',
  PRIMARY KEY (`id`,`uid`,`projectID`)
) ENGINE=InnoDB AUTO_INCREMENT=689 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pro_user
-- ----------------------------

-- ----------------------------
-- Table structure for `searchConditionHistory`
-- ----------------------------
DROP TABLE IF EXISTS `searchConditionHistory`;
CREATE TABLE `searchConditionHistory` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) NOT NULL,
  `conditionText` text,
  `logTime` varchar(30) DEFAULT NULL,
  `conditionName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`,`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of searchConditionHistory
-- ----------------------------

-- ----------------------------
-- Table structure for `session_uid`
-- ----------------------------
DROP TABLE IF EXISTS `session_uid`;
CREATE TABLE `session_uid` (
  `uid` varchar(255) NOT NULL,
  `sessionID` varchar(255) NOT NULL,
  `cTime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`uid`,`sessionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of session_uid
-- ----------------------------
/*
INSERT INTO `gennlife_manage` VALUES ('admin@ytyhdyy.com', 'admin', 'admin', 'yantai_1');
INSERT INTO `gennlife_user` VALUES (1, 'admin@ytyhdyy.com', 'ls123456',"d8b8f198d882909be0a58778875953e8", '管理员', 'admin@ytyhdyy.com', '', '', 'yantai_1', '烟台毓璜顶医院', 'yantai_1', '烟台毓璜顶医院', '', 0, 0, NULL, '2016-09-30 15:16:48', '', '58a5cd0352cdbb26e00114554723e9b0');
INSERT INTO `gennlife_org` VALUES (3, 'yantai_1', '烟台毓璜顶医院', 'XXX', '芝罘区毓东路20号');*/
/*
INSERT INTO `gennlife_role` VALUES ('1', '科室成员', 'yantai_1', '科室成员全部资源', '1', null, null, null);
INSERT INTO `gennlife_role_resource` VALUES ('1', 'yantai_1-benkeshi', 'tianjin_city_1', '有', '有', '有', '有', '有', '有', null, '有');
*/
/*1	科室成员	tianjin_city_1	科室成员全部资源	1			*/