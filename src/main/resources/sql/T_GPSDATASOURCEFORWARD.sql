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

Date: 2020-03-03 15:02:25
*/


-- ----------------------------
-- Table structure for T_GPSDATASOURCEFORWARD
-- ----------------------------
DROP TABLE [dbo].[T_GPSDATASOURCEFORWARD]
GO
CREATE TABLE [dbo].[T_GPSDATASOURCEFORWARD] (
[id] varchar(32) NOT NULL ,
[forwardUrl] varchar(100) NULL ,
[sourceType] varchar(2000) NULL ,
[subscriptionName] varchar(32) NULL ,
[createTime] datetime NULL ,
[modifyTime] datetime NULL ,
[createUser] varchar(50) NULL ,
[modifyUser] varchar(50) NULL ,
[remark] varchar(1000) NULL ,
[account] varchar(100) NULL ,
[password] varchar(100) NULL ,
[isEnable] bigint NULL 
)


GO

-- ----------------------------
-- Indexes structure for table T_GPSDATASOURCEFORWARD
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table T_GPSDATASOURCEFORWARD
-- ----------------------------
ALTER TABLE [dbo].[T_GPSDATASOURCEFORWARD] ADD PRIMARY KEY ([id])
GO
