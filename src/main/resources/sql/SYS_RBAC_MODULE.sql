/*
Navicat SQL Server Data Transfer

Source Server         : sqlServerCon
Source Server Version : 110000
Source Host           : 192.168.2.68:1433
Source Database       : test
Source Schema         : dbo

Target Server Type    : SQL Server
Target Server Version : 110000
File Encoding         : 65001

Date: 2020-03-02 09:40:28
*/


-- ----------------------------
-- Table structure for SYS_RBAC_MODULE
-- ----------------------------
DROP TABLE [dbo].[SYS_RBAC_MODULE]
GO
CREATE TABLE [dbo].[SYS_RBAC_MODULE] (
[MODULE_ID] varchar(255) NOT NULL ,
[MODULE_NAME] varchar(255) NULL ,
[MODULE_LEVEL] numeric(10) NULL ,
[MODULE_SORT] numeric(10) NULL ,
[MODULE_DESC] varchar(255) NULL 
)


GO

-- ----------------------------
-- Records of SYS_RBAC_MODULE
-- ----------------------------
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'DATASOURCE', N'数据源管理', N'1', N'2', N'')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'DATASOURCE-CONFIG', N'数据源配置', N'2', N'1', N'../manage/module?act=datasource-config')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'DATASOURCE-FORWARD', N'转发订阅', N'2', N'2', N'../manage/module?act=datasource-forward')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'DATASOURCE-LOG', N'数据日志', N'2', N'3', N'../manage/module?act=datasource-log')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS', N'系统维护', N'1', N'1', null)
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS-AREA', N'区域配置', N'2', N'3', N'../manage/module?act=sys-area')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS-CFG', N'系统参数', N'2', N'1', N'../manage/module?act=sys-cfg')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS-DICT', N'数据字典', N'2', N'2', N'../manage/module?act=sys-dict')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS-LOG', N'系统日志', N'2', N'8', N'../manage/module?act=sys-log')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS-ORGAN', N'组织机构', N'2', N'4', N'../manage/module?act=sys-organ')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS-RIGHT', N'权限配置', N'2', N'6', N'../manage/module?act=sys-right')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS-ROLE', N'角色管理', N'2', N'5', N'../manage/module?act=sys-role')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'SYS-USER', N'用户管理', N'2', N'7', N'../manage/module?act=sys-user')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'USERINFO', N'个人信息', N'1', N'5', null)
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'USERINFO-INFO', N'资料维护', N'2', N'1', N'../manage/module?act=userinfo-info')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_MODULE] ([MODULE_ID], [MODULE_NAME], [MODULE_LEVEL], [MODULE_SORT], [MODULE_DESC]) VALUES (N'USERINFO-PWD', N'修改密码', N'2', N'2', N'../manage/module?act=userinfo-pwd')
GO
GO

-- ----------------------------
-- Indexes structure for table SYS_RBAC_MODULE
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SYS_RBAC_MODULE
-- ----------------------------
ALTER TABLE [dbo].[SYS_RBAC_MODULE] ADD PRIMARY KEY ([MODULE_ID])
GO
