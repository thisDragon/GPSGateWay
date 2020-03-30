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

Date: 2020-03-02 09:41:21
*/


-- ----------------------------
-- Table structure for T_LOGDATA
-- ----------------------------
DROP TABLE [dbo].[T_LOGDATA]
GO
CREATE TABLE [dbo].[T_LOGDATA] (
[id] varchar(32) NOT NULL ,
[logType] bigint NULL ,
[sourceType] varchar(30) NULL ,
[deviceId] varchar(50) NULL ,
[createTime] datetime NULL ,
[content] varchar(1000) NULL ,
[state] bigint NULL 
)


GO

-- ----------------------------
-- Indexes structure for table T_LOGDATA
-- ----------------------------
CREATE INDEX [T_LOGDATA_INDEX] ON [dbo].[T_LOGDATA]
([sourceType] ASC, [logType] ASC, [state] ASC, [createTime] ASC) 
GO

-- ----------------------------
-- Primary Key structure for table T_LOGDATA
-- ----------------------------
ALTER TABLE [dbo].[T_LOGDATA] ADD PRIMARY KEY ([id])
GO
