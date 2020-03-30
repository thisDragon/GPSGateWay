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

Date: 2020-03-02 09:40:09
*/


-- ----------------------------
-- Table structure for SYS_ORGAN
-- ----------------------------
DROP TABLE [dbo].[SYS_ORGAN]
GO
CREATE TABLE [dbo].[SYS_ORGAN] (
[ORGAN_ID] varchar(255) NOT NULL ,
[ORGAN_NAME] varchar(255) NULL ,
[ORGAN_CODE] varchar(255) NULL ,
[ORGAN_LEVEL] numeric(10) NOT NULL ,
[ORGAN_SORT] numeric(10) NULL ,
[ORGAN_DESC] varchar(255) NULL 
)


GO

-- ----------------------------
-- Records of SYS_ORGAN
-- ----------------------------
INSERT INTO [dbo].[SYS_ORGAN] ([ORGAN_ID], [ORGAN_NAME], [ORGAN_CODE], [ORGAN_LEVEL], [ORGAN_SORT], [ORGAN_DESC]) VALUES (N'yanfa', N'研发部', N'yanfa', N'1', N'20', null)
GO
GO

-- ----------------------------
-- Indexes structure for table SYS_ORGAN
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SYS_ORGAN
-- ----------------------------
ALTER TABLE [dbo].[SYS_ORGAN] ADD PRIMARY KEY ([ORGAN_ID])
GO
