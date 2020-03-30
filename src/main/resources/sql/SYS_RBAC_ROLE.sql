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

Date: 2020-03-02 09:40:49
*/


-- ----------------------------
-- Table structure for SYS_RBAC_ROLE
-- ----------------------------
DROP TABLE [dbo].[SYS_RBAC_ROLE]
GO
CREATE TABLE [dbo].[SYS_RBAC_ROLE] (
[ROLE_ID] varchar(255) NOT NULL ,
[ROLE_NAME] varchar(255) NULL ,
[ROLE_ORDERNO] numeric(10) NULL ,
[ROLE_DESC] varchar(255) NULL 
)


GO

-- ----------------------------
-- Records of SYS_RBAC_ROLE
-- ----------------------------
INSERT INTO [dbo].[SYS_RBAC_ROLE] ([ROLE_ID], [ROLE_NAME], [ROLE_ORDERNO], [ROLE_DESC]) VALUES (N'BASE_ROLE', N'基础用户', N'2', N'系统新增用户是，默认的用户角色')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_ROLE] ([ROLE_ID], [ROLE_NAME], [ROLE_ORDERNO], [ROLE_DESC]) VALUES (N'Md98wqQjyLRk', N'测试角色', N'3', null)
GO
GO
INSERT INTO [dbo].[SYS_RBAC_ROLE] ([ROLE_ID], [ROLE_NAME], [ROLE_ORDERNO], [ROLE_DESC]) VALUES (N'N4Se07vayMLf', N'x', N'4', N'x')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_ROLE] ([ROLE_ID], [ROLE_NAME], [ROLE_ORDERNO], [ROLE_DESC]) VALUES (N'SUPER_ROLE', N'超级管理员', N'0', N'拥有系统全部权限，可以进行任何操作')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_ROLE] ([ROLE_ID], [ROLE_NAME], [ROLE_ORDERNO], [ROLE_DESC]) VALUES (N'SYS_ROLE', N'系统管理员', N'1', N'拥有绝大部分的系统维护权限。')
GO
GO

-- ----------------------------
-- Indexes structure for table SYS_RBAC_ROLE
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SYS_RBAC_ROLE
-- ----------------------------
ALTER TABLE [dbo].[SYS_RBAC_ROLE] ADD PRIMARY KEY ([ROLE_ID])
GO
