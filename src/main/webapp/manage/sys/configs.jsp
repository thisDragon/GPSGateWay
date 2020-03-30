<%-- 用于维护系统缓存 --%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.cari.wing.cache.SystemConfig"%>
<%@ page import="com.cari.web.util.DataFormater"%>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="wing" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	SystemConfig syscfg = SystemConfig.loadSystemConfig();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>">
	<title>系统参数管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<script language="javascript" src="js/ajform.js"></script>
	<script language="javascript" src="js/manage.js"></script>
	<link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script language="JavaScript">
		//保存配置
		function save(){
			if (confirmForm()){
				if (confirm("您所做的修改将立即生效，是否继续？")){
			    	var frm = document.forms["frmConfig"];
			    	AJForm.submitForm(frm);
				}
			}
		}
		function callback(data , statusCode , statusMessage){
			if(statusCode != AJForm.STATUS['SUCCESS'] ) {
			 	alert( statusMessage );
				return true;
			}
			//获取返回信息
			var xmldom = new XMLParser(data);
			if (xmldom.isError){
				alert(xmldom.error);
			}else{
				alert(xmldom.content.text);
			}
		}
		function confirmForm(){
			var frm = document.forms["frmConfig"];
			if (frm.JDBC_Driver.value==""){
				alert("请填写JDBC驱动名称");
				frm.JDBC_Driver.focus();
				return false;
			}
			if (frm.JDBC_URL.value==""){
				alert("请填写JDBC连接字符串");
				frm.JDBC_URL.focus();
				return false;
			}
			if (frm.JDBC_UserName.value==""){
				alert("请填写数据库用户名");
				frm.JDBC_UserName.focus();
				return false;
			}
			if (frm.JDBC_Password.value==""){
				alert("请填写数据库密码");
				frm.JDBC_Password.focus();
				return false;
			}
			if (frm.JDBC_ConnectionString.value==""){
				alert("请填写应用服务器与数据库的连接标识。");
				frm.JDBC_ConnectionString.focus();
				return false;
			}
			if (frm.C3P0_MaxPoolSize.value==""){
				alert("请填写连接池最大连接数。");
				frm.C3P0_MaxPoolSize.focus();
				return false;
			}
			if (frm.C3P0_MinPoolSize.value==""){
				alert("请填写连接池初始连接数。");
				frm.C3P0_MinPoolSize.focus();
				return false;
			}
			if (frm.C3P0_AcquireIncrement.value==""){
				alert("请填写连接池可追加连接数。");
				frm.C3P0_AcquireIncrement.focus();
				return false;
			}
			if (frm.C3P0_MaxStatements.value==""){
				alert("请填写数据库语句的最大缓存数。");
				frm.C3P0_MaxStatements.focus();
				return false;
			}
			if (frm.C3P0_MaxIdleTime.value==""){
				alert("请填写连接最长空闲时间。");
				frm.C3P0_MaxIdleTime.focus();
				return false;
			}
			if (frm.C3P0_IdleConnectionTestPeriod.value==""){
				alert("请填写检查空闲连接的时间。");
				frm.C3P0_IdleConnectionTestPeriod.focus();
				return false;
			}
			if (frm.Map_WorkingPath.value==""){
				alert("请填写地图工作目录");
				frm.Map_WorkingPath.focus();
				return false;
			}
			if (frm.Map_CreatingPath.value==""){
				alert("请填写地图生产目录");
				frm.Map_CreatingPath.focus();
				return false;
			}
			if (frm.Map_ShowBusStopLayer.value == ""){
				alert("请填写开始显示公交站点的层级");
				frm.Map_ShowBusStopLayer.focus();
				return false;
			}
			if (frm.Map_Left.value==""){
				alert("请填写地图的左边界");
				frm.Map_Left.focus();
				return false;
			}
			if (frm.Map_Right.value==""){
				alert("请填写地图的右边界");
				frm.Map_Right.focus();
				return false;
			}
			if (frm.Map_Top.value==""){
				alert("请填写地图的上边界");
				frm.Map_Top.focus();
				return false;
			}
			if (frm.Map_Bottom.value==""){
				alert("请填写地图的下边界");
				frm.Map_Bottom.focus();
				return false;
			}
			return true;
		}
		
		function showIFrame(){
			document.all("ifrmUpload").style.display = "";
			document.all("btnPreview").style.display = "";
		}
		function hideIFrame(){
			document.all("ifrmUpload").style.display = "none";
			document.all("btnPreview").style.display = "none";
		}
		function previewWaterMark(){
			var win = window.open("<%=basePath%>icons/watermark.png","","width=512,height=512");
		}
	</script>
	<style>
		body,td,a,table{font-size:9pt;}
		.title{ background-color:#E9F0FB;color:#2566A7;text-align:right;}
		.myA{text-decoration:underline;color:blue;}
		a.myA:hover{color:red;text-decoration:none;}
		a.myA:visited{text-decoration:underline;color:blue;}
	</style>
</head>

<body topmargin="0" rightmargin="0" leftmargin="0" bottommargin="0">
<form method="post" name="frmConfig" action="<%=basePath %>manage/config" onSubmit="ajform:callback">
	<input type="hidden" name="act" value="SAVE">
	<br>
	<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
	<tr>
		<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
				<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02">系统参数配置</td>
				<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
	<TABLE width="90%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
		<wing:permission module_id="SYS-CFG" operator_id="modify">
			<input type="button" style="background-image:url(images/manage/contentBtn4.gif);" class="contentBtn4" onClick="save();" id="submitbtn" value="保存并生效">
		</wing:permission>
			<input type="reset" style="background-image:url(images/manage/contentBtn4.gif);" class="contentBtn4" value="放弃修改">
		</td>
	</tr>
    <tr>
      <td height="260" align="center" valign="top" class="conKa05">
		<table cellpadding="0" cellspacing="0" border="0" width="80%" height="80%" align="center" valign="middle">
		<tr>
			<td colspan="3"><b>数据库配置</b></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">JDBC驱动名称：</td>
			<td><input type="text" name="JDBC_Driver" size="50" value="<%=DataFormater.noNullValue(syscfg.getJDBCDriverClass()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">JDBC连接字符串：</td>
			<td><input type="text" name="JDBC_URL" size="50" value="<%=DataFormater.noNullValue(syscfg.getJDBCURL()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">数据库用户：</td>
			<td><input type="text" name="JDBC_UserName" size="50" value="<%=DataFormater.noNullValue(syscfg.getJDBCUserName()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">数据库密码：</td>
			<td><input type="text" name="JDBC_Password" size="50" value="<%=DataFormater.noNullValue(syscfg.getJDBCPassword()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">应用服务器与数据库的连接标识：</td>
			<td><input type="text" name="JDBC_ConnectionString" size="50" value="<%=DataFormater.noNullValue(syscfg.getConnectionString()) %>"></td>
		</tr>
		<tr>
			<td colspan="2"><b>C3P0连接池配置</b></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">连接池初始连接数：</td>
			<td><input type="text" name="C3P0_MinPoolSize" size="50" value="<%=DataFormater.noNullValue(syscfg.getC3P0MinPoolSize()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">连接池最大连接数：</td>
			<td><input type="text" name="C3P0_MaxPoolSize" size="50" value="<%=DataFormater.noNullValue(syscfg.getC3P0MaxPoolSize()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">连接池可追加连接数：</td>
			<td><input type="text" name="C3P0_AcquireIncrement" size="50" value="<%=DataFormater.noNullValue(syscfg.getC3P0AcquireIncrement()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">数据库语句对象最大缓存数：</td>
			<td><input type="text" name="C3P0_MaxStatements" size="50" value="<%=DataFormater.noNullValue(syscfg.getC3P0MaxStatements()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">连接最长空闲时间（秒）：</td>
			<td><input type="text" name="C3P0_MaxIdleTime" size="50" value="<%=DataFormater.noNullValue(syscfg.getC3P0MaxIdleTime()) %>"></td>
		</tr>
		<tr>
			<td width="30">&nbsp;</td>
			<td width="200">检查空闲连接的时间间隔（毫秒）：</td>
			<td><input type="text" name="C3P0_IdleConnectionTestPeriod" size="50" value="<%=DataFormater.noNullValue(syscfg.getC3P0IdleConnectionTestPeriod()) %>"></td>
		</tr>		
		</table>
		<br>
		</td>
	</tr>
	</table>
	<br>
</form>
</body>
</html>
