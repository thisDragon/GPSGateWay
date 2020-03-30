<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <%
	String path = request.getContextPath();
	String sPort = (request.getServerPort() == 80)?"":(":" + request.getServerPort());
	String base = request.getScheme()+"://"+request.getServerName()+sPort;
	String basePath = base + path + "/";
%>
    <title>访问受限</title>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>a,td,body{font-size:9pt;}
	a {color: blue;text-decoration: underline;}
	a:visited {color: blue;text-decoration: underline;}
	a:hover {color: red;text-decoration: underline;}
	a:active {color: #222222;text-decoration: underline;}
	</style>
  </head>
		  
  <body bgcolor="#eeeeee" onload="setTimeout(function(){location.href='login.jsp';},3000);";>
  <table width="100%" height="100%"><tr><td valign="middle" align="center">
		<table align="center" valign="middle" border="2" width="450" height="250" bordercolor="#99ABD9" cellspacing=2 cellpadding=2 bgcolor="#ffffff">
		<tr>
			<td valign="middle" style="padding:4px;">
				<div style="display:inline;width:30%;height:220px;padding-top:20px;padding-left:25px;">
					<img src = "images/manage/error.jpg" style="float:left">
				</div>
				<div style="display:inline;width:70%;height:220px;padding-top:20px;">
					<font color="red" style="font-size:18px;font-weight:bold;">访问受限!</font>
					<br><br>可能是以下几个原因造成：<br>
					<br>1、未登录用户或用户访问已超时，您可以重新登陆；
					<br>2、你不具备对该项的操作权限，您可以和系统管理员联系；
					<br><A href="login.jsp" target="_top">>>点击这里重新登录 </A>
					<br><br>浏览器将在3秒后转向登录页...
				</div>
			</td>
		</tr>
		</table>
</td></tr></table>
  </body>
</html>
