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

Date: 2020-03-02 09:41:03
*/


-- ----------------------------
-- Table structure for T_GPSDATASOURCE
-- ----------------------------
DROP TABLE [dbo].[T_GPSDATASOURCE]
GO
CREATE TABLE [dbo].[T_GPSDATASOURCE] (
[id] varchar(50) NOT NULL ,
[deviceId] varchar(50) NULL ,
[sourceType] varchar(30) NULL ,
[lon] float(53) NULL ,
[lat] float(53) NULL ,
[position] varchar(1000) NULL ,
[token] varchar(50) NULL ,
[createTime] datetime NULL ,
[remark] varchar(1000) NULL ,
[speed] varchar(20) NULL ,
[gpsTime] datetime NULL 
)


GO

-- ----------------------------
-- Records of T_GPSDATASOURCE
-- ----------------------------

-- ----------------------------
-- Indexes structure for table T_GPSDATASOURCE
-- ----------------------------
CREATE INDEX [T_GPSDATASOURCE_INDEX] ON [dbo].[T_GPSDATASOURCE]
([sourceType] ASC, [createTime] ASC, [deviceId] ASC) 
GO
