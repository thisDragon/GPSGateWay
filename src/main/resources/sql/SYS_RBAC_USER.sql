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

Date: 2020-03-02 09:40:54
*/


-- ----------------------------
-- Table structure for SYS_RBAC_USER
-- ----------------------------
DROP TABLE [dbo].[SYS_RBAC_USER]
GO
CREATE TABLE [dbo].[SYS_RBAC_USER] (
[USER_ID] varchar(255) NOT NULL ,
[USER_NAME] varchar(255) NULL ,
[USER_PASS] varchar(255) NULL ,
[USER_CODE] varchar(255) NULL ,
[USER_DEPT_ID] varchar(255) NULL ,
[USER_DUTY] varchar(255) NULL ,
[USER_CERT_TYPE] varchar(255) NULL ,
[USER_CERT_CODE] varchar(255) NULL ,
[USER_NATION] varchar(255) NULL ,
[USER_SEX] varchar(255) NULL ,
[USER_PHONE] varchar(255) NULL ,
[USER_MOBILE] varchar(255) NULL ,
[USER_MAIL] varchar(255) NULL ,
[USER_ADDRESS] varchar(255) NULL ,
[USER_POST_CODE] varchar(255) NULL ,
[USER_REMARK] varchar(255) NULL ,
[USER_DATE] datetime2(0) NULL 
)


GO

-- ----------------------------
-- Records of SYS_RBAC_USER
-- ----------------------------
INSERT INTO [dbo].[SYS_RBAC_USER] ([USER_ID], [USER_NAME], [USER_PASS], [USER_CODE], [USER_DEPT_ID], [USER_DUTY], [USER_CERT_TYPE], [USER_CERT_CODE], [USER_NATION], [USER_SEX], [USER_PHONE], [USER_MOBILE], [USER_MAIL], [USER_ADDRESS], [USER_POST_CODE], [USER_REMARK], [USER_DATE]) VALUES (N'1', N'1', N'888888', N'1', N'ROOT', null, null, null, N'中国', N'未知', null, N'1', N'1', null, null, null, N'2020-02-18 16:06:59')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_USER] ([USER_ID], [USER_NAME], [USER_PASS], [USER_CODE], [USER_DEPT_ID], [USER_DUTY], [USER_CERT_TYPE], [USER_CERT_CODE], [USER_NATION], [USER_SEX], [USER_PHONE], [USER_MOBILE], [USER_MAIL], [USER_ADDRESS], [USER_POST_CODE], [USER_REMARK], [USER_DATE]) VALUES (N'admin', N'系统管理员', N'admin', N'admin', null, null, null, null, null, null, null, null, null, null, null, null, null)
GO
GO
INSERT INTO [dbo].[SYS_RBAC_USER] ([USER_ID], [USER_NAME], [USER_PASS], [USER_CODE], [USER_DEPT_ID], [USER_DUTY], [USER_CERT_TYPE], [USER_CERT_CODE], [USER_NATION], [USER_SEX], [USER_PHONE], [USER_MOBILE], [USER_MAIL], [USER_ADDRESS], [USER_POST_CODE], [USER_REMARK], [USER_DATE]) VALUES (N'test', N'test', N'888888', N'test', N'yanfa', null, null, null, N'中国', null, null, N'test', N'test', null, N'sdfsdfs', null, N'2020-02-18 11:21:30')
GO
GO

-- ----------------------------
-- Indexes structure for table SYS_RBAC_USER
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SYS_RBAC_USER
-- ----------------------------
ALTER TABLE [dbo].[SYS_RBAC_USER] ADD PRIMARY KEY ([USER_ID])
GO
