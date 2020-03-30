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

Date: 2020-03-02 09:40:58
*/


-- ----------------------------
-- Table structure for SYS_RBAC_USERTOROLE
-- ----------------------------
DROP TABLE [dbo].[SYS_RBAC_USERTOROLE]
GO
CREATE TABLE [dbo].[SYS_RBAC_USERTOROLE] (
[USER_ID] varchar(255) NOT NULL ,
[ROLE_ID] varchar(255) NOT NULL 
)


GO

-- ----------------------------
-- Records of SYS_RBAC_USERTOROLE
-- ----------------------------
INSERT INTO [dbo].[SYS_RBAC_USERTOROLE] ([USER_ID], [ROLE_ID]) VALUES (N'admin', N'SUPER_ROLE')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_USERTOROLE] ([USER_ID], [ROLE_ID]) VALUES (N'test', N'Md98wqQjyLRk')
GO
GO
INSERT INTO [dbo].[SYS_RBAC_USERTOROLE] ([USER_ID], [ROLE_ID]) VALUES (N'1', N'BASE_ROLE')
GO
GO
