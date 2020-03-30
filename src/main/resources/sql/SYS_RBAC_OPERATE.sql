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

Date: 2020-03-02 09:40:44
*/


-- ----------------------------
-- Table structure for SYS_RBAC_OPERATE
-- ----------------------------
DROP TABLE [dbo].[SYS_RBAC_OPERATE]
GO
CREATE TABLE [dbo].[SYS_RBAC_OPERATE] (
[OPERATE_ID] varchar(255) NOT NULL ,
[OPERATE_NAME] varchar(255) NULL ,
[OPERATE_VALUE] numeric(10) NULL 
)


GO

-- ----------------------------
-- Records of SYS_RBAC_OPERATE
-- ----------------------------
INSERT INTO [dbo].[SYS_RBAC_OPERATE] ([OPERATE_ID], [OPERATE_NAME], [OPERATE_VALUE]) VALUES (N'add', N'新增', N'2')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_OPERATE] ([OPERATE_ID], [OPERATE_NAME], [OPERATE_VALUE]) VALUES (N'approve', N'审批', N'16')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_OPERATE] ([OPERATE_ID], [OPERATE_NAME], [OPERATE_VALUE]) VALUES (N'delete', N'删除', N'8')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_OPERATE] ([OPERATE_ID], [OPERATE_NAME], [OPERATE_VALUE]) VALUES (N'modify', N'修改', N'4')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_OPERATE] ([OPERATE_ID], [OPERATE_NAME], [OPERATE_VALUE]) VALUES (N'view', N'浏览', N'1')
GO
GO

-- ----------------------------
-- Indexes structure for table SYS_RBAC_OPERATE
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SYS_RBAC_OPERATE
-- ----------------------------
ALTER TABLE [dbo].[SYS_RBAC_OPERATE] ADD PRIMARY KEY ([OPERATE_ID])
GO
