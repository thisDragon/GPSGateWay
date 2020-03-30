1.导出功能需要配置如下
	1.1	server.xml添加如下映射
		<Context docBase="D:/img" path="/partitionImage" reloadable="false"/>
	1.2	添加D:/img文件夹
	
2.删除T_GPSDATASOURCEFORWARD表的frequency字段

3.新增了jar包

4.T_GPSDATASOURCE表的索引改变了

5.需要添加兼容性才能正常使用

6.T_GPSDATASOURCEFORWARD 新增account password isEnable 3个字段
alter table T_GPSDATASOURCEFORWARD add [account] varchar(100) NULL;
alter table T_GPSDATASOURCEFORWARD add [password] varchar(100) NULL;
alter table T_GPSDATASOURCEFORWARD add [isEnable] bigint NULL

7.设置为docBase=""
<Context docBase="" path="/" reloadable="true" source="org.eclipse.jst.jee.server:GPSGateWay"/></Host>

8.web.xml改动