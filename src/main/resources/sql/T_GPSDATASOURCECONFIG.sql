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

Date: 2020-03-02 09:41:08
*/


-- ----------------------------
-- Table structure for T_GPSDATASOURCECONFIG
-- ----------------------------
DROP TABLE [dbo].[T_GPSDATASOURCECONFIG]
GO
CREATE TABLE [dbo].[T_GPSDATASOURCECONFIG] (
[id] varchar(32) NOT NULL ,
[sourceType] varchar(30) NULL ,
[token] varchar(50) NULL ,
[createUser] varchar(50) NULL ,
[remark] varchar(1000) NULL ,
[createTime] datetime NULL ,
[modifyTime] datetime NULL ,
[sourceName] varchar(50) NULL ,
[userFlag] bigint NULL ,
[modifyUser] varchar(50) NULL ,
[timeSpan] int NULL  ,
[count] int NULL
)


GO

-- ----------------------------
-- Records of T_GPSDATASOURCECONFIG
-- ----------------------------
INSERT INTO [dbo].[T_GPSDATASOURCECONFIG] ([id], [sourceType], [token], [createUser], [remark], [createTime], [modifyTime], [sourceName], [userFlag], [modifyUser], [timeSpan]) VALUES (N'2aa7229a0a84449d82dd5a5cfdf83143', N'SOURCETYPE2', N'4b9fe31f46594f91', N'系统管理员', N'remark', N'2020-02-17 10:34:13.103', N'2020-02-17 10:34:13.103', N'sourceName2', N'1', N'系统管理员', N'5')
GO
GO
INSERT INTO [dbo].[T_GPSDATASOURCECONFIG] ([id], [sourceType], [token], [createUser], [remark], [createTime], [modifyTime], [sourceName], [userFlag], [modifyUser], [timeSpan]) VALUES (N'c14972cc74db4456b8d2dbccee3a2880', N'SOURCETYPE1', N'9a6e81758a8942fc', N'系统管理员', N'remark', N'2020-02-14 09:02:38.837', N'2020-02-14 09:02:38.837', N'sourceName1', N'1', N'系统管理员', N'2')
GO
GO
INSERT INTO [dbo].[T_GPSDATASOURCECONFIG] ([id], [sourceType], [token], [createUser], [remark], [createTime], [modifyTime], [sourceName], [userFlag], [modifyUser], [timeSpan]) VALUES (N'c29cc0aa17a242ff97d697e26945f989', N'TYPE', N'9817d87ffc60474d', N'系统管理员', N'remark', N'2020-02-24 11:30:42.717', N'2020-02-24 15:31:23.690', N'name', N'1', N'系统管理员', N'999')
GO
GO

-- ----------------------------
-- Indexes structure for table T_GPSDATASOURCECONFIG
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table T_GPSDATASOURCECONFIG
-- ----------------------------
ALTER TABLE [dbo].[T_GPSDATASOURCECONFIG] ADD PRIMARY KEY ([id])
GO
