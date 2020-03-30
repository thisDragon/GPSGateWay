<%-- 进入后台管理系统，必须先登录 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.cari.wing.cache.SystemConfig" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String sShowInfo = "";
	String sErrorInfo = (String)request.getAttribute("ERRORINFO");
	if (sErrorInfo != null && !sErrorInfo.trim().equals("")){
		sShowInfo = "alert('" + sErrorInfo + "');";
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<!-- <title>Wing地图引擎管理系统</title> -->
<title>GPS Gateway数据管理系统</title>
<script language="JavaScript" type="text/JavaScript">
	function toSub() {
		if (document.form1.username.value == "") {
			alert("请输入用户名！");
			document.form1.username.focus();
			return false;
		}
		if (document.form1.password.value == "") {
			alert("请输入密码！");
			document.form1.password.focus();
			return false;
		}
		if (document.form1.code.value == "") {
			alert("请输入验证码！");
			document.form1.code.focus();
			return false;
		}
	}
 if(top.location.href!=location.href){top.location.href=location.href;}
</script>
<style>
	a,td,form,table,p{font-size:12px;color:white;}
	.myinput{  
		border:solid 1px #000000;
		background-color:#eeeeee;
		width:180px;
	}
	.bg2{background:url(images/manage/login_face.png) no-repeat;}
	.bg1{background:url(images/manage/login_title.png) no-repeat;}
	body{background:url(images/manage/login_bg.jpg) no-repeat;background-color:#627198;}
	.btn{background-color:#627198;width:93px;height:50px;color:white;}
</style>
<script language="javascript">
	function pageInit(){
		<%=sShowInfo%>
		document.form1.username.focus();
		if(!document.all){
			var divs = document.getElementsByTagName("div");
			divs[0].className = "bg1";
			divs[2].className = "bg2";
		}
	}
</script>
</head>
<body onLoad="pageInit()">
<table width="100%" height="100%">
	<tr><td valign="middle" align="center">
		<table cellpadding="0" cellspacing="0" border="0" width="525">
		<tr><td><div style="with:334px;height:43px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=crop,src='images/manage/login_title.png')">&nbsp;</div></td></tr>
		<tr><td>
			<div style="position:relative;height:200px;width:525px;">
				<div style="position:absolute;with:525px;height:200px;left:0px;padding:0px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=crop,src='images/manage/login_face.png');">
					<table cellpadding="0" cellspacing="0" border="0" width="525" height="200px"><tr><td>&nbsp;</td></tr></table>
				</div>
				<div style="position:absolute;with:525px;height:200px;top:0px;padding-left:80px;padding-top:50px;">
				<form name="form1" method="post" action="login" onsubmit="return toSub();">
				<input type="hidden" name="act" value="LOGIN">
				<table>
					<tr> 
						<td width="52" height="25">用户名：</td>
						<td colspan="2"><input name="username" type="text" tabindex="1" class="myinput"></td>
						<!-- <td rowspan="3"><input type="submit" class="btn" value=" 登录|Login "></td> -->
						<td rowspan="3"><input type="submit" class="btn" value=" 登录 "></td>
					</tr>
					<tr> 
						<td height="25">密　码：</td>
						<td colspan="2"><input type="password" name="password" tabindex="2" class="myinput"></td>
					</tr>
					<tr> 
						<td height="25">认证码：</td>
						<td width="49" height="20"><input name="code" type="text" tabindex="3" class="myinput" style="width:120px;"></td>
						<td width="71"><img src="login?act=CODEIMG" width="50" height="20" tabindex="4"></td>
					</tr>
				</table>
				</form>
				</div>
				<!-- <div style="position:absolute;top:0px;left:370px;top:200px;width:170px;color:#B3BEE1">
					© 2006-2008 开睿动力科技
				</div> -->
				<div style="position:absolute;top:0px;left:370px;top:200px;width:170px;color:#B3BEE1">
				</div>
			</div>
			</td></tr>
		</table>
	</td></tr>
</table>
</body>
</html>