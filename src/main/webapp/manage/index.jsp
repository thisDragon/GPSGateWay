
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.cari.rbac.*,
			com.cari.sys.bean.SysUser,
			com.cari.wing.cache.SystemConfig" %>
<%
	SysUser user = (SysUser)request.getSession().getAttribute("LOGINUSER");
	if (user == null) {
		response.sendRedirect("overtime.jsp");
		return;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<!-- <Title>开睿后台管理系统</Title>	 -->
	<Title>GPS Gateway数据管理系统</Title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="../HeadInclude.jsp"%>
  	<link href="css/main.css" rel="stylesheet" type="text/css">
  	<script>
  	
  	var nowMenu = "";
	var MenuStr = '<%=RBACUtil.getMenuScript(user.getRoles(), session)%>';				
	var topMenu = '<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr>'
					+'<td width="16" height="26" class="menu01">&nbsp;</td>'
					+'<td width="150" class="menu03">您好： <%=user.getUserName()%></td>';
	var menuArray = MenuStr.split("&&&&");
	for(i=0;i<menuArray.length;i++){
		menuArray[i] = menuArray[i].split("///");
		topMenu += '<td width="81" class="menu02" onclick="chuangSmallMenu(' + i + ')" style="cursor:hand;" id="topMenu' + i + '">' + menuArray[i][0] + '</td>';
		menuArray[i][1] = menuArray[i][1].split("||");
		for(j=0;j<menuArray[i][1].length;j++){
			menuArray[i][1][j] = menuArray[i][1][j].split("*");
		}
	}
	topMenu += '<td align="right" class="menu03">&nbsp;</td>'
				+'<td width="60" align="center" valign="top" class="toolbtn02">'
				+'<img src="images/manage/top.gif" hidden="false" id="closeBannerBtn" style="cursor:hand;" onclick = "hideBanner(this)">'
				+'<img src="images/manage/close.gif" style="cursor:hand;" onclick = "logout()">'
				+'</td>'
				+'</tr></table>';
				
	//显示二级菜单
	function chuangSmallMenu(ubond){
		document.getElementById("topMenu" + ubond).className = "menu021";
		if (nowMenu != "") {document.getElementById(nowMenu).className = "menu02";}
		nowMenu = "topMenu" + ubond;
		var smallMenu = '<table width="96%" border="0" cellspacing="0" cellpadding="0"><tr>'
						+'<td width="40" height="20">&nbsp;</td>'
		for(i=0;i<menuArray[ubond][1].length;i++){
			smallMenu += '<td width="80" align="center"><A class="menuPadd" href="javascript:menuAction(\'' + menuArray[ubond][1][i][1] + '\');">' + menuArray[ubond][1][i][0] + '</A></td>';
		}
		smallMenu += '<td>&nbsp;</td></tr></table>';
		document.getElementById("smallMenu").innerHTML = smallMenu;
	}
	
  	function menuAction(url){
		window.adminWindow.location =url;
  	}
  	function logout() {
  		document.body.style.filter="Gray";
  		if(!confirm("确定要退出系统吗？")) {
  		  	document.body.style.filter="";
  			return;
  		}
  		location.href="../login?act=LOGOUT"
  	}
  	
  	function hideBanner(obj){
  		if (obj.hidden == "true"){
  			document.getElementById("banner").style.display = "";
  			obj.src = "images/manage/top.gif";
  			obj.hidden = "false";
			setCookie("sys_closeBanner","false");
  		}else{
  			document.getElementById("banner").style.display = "none";
  			obj.src = "images/manage/bottom.gif";
  			obj.hidden = "true";
			setCookie("sys_closeBanner","true");
  		}
  	}
	function myOnload(){
		var isCloseBanner = getCookie("sys_closeBanner");
		if(isCloseBanner == "true"){
			document.getElementById("banner").style.display = "none";
			document.getElementById("closeBannerBtn").src = "images/manage/bottom.gif";
			document.getElementById("closeBannerBtn").hidden = "true";
		}
	}
	//cookie
	function setCookie(name,value){
		var today = new Date();
		var expires = new Date();
		expires.setTime(today.getTime() + 1000*60*60*24*365);
		document.cookie = name + "=" + escape(value) + "; expires=" + expires.toGMTString()+";path=/";
	}
	
	function getCookie(Name){
		var search = Name + "=";
		if(document.cookie.length > 0) {
			offset = document.cookie.indexOf(search);
			if(offset != -1) {
				offset += search.length;
				end = document.cookie.indexOf(";", offset);
				if(end == -1) end = document.cookie.length;
				return unescape(document.cookie.substring(offset, end));
			}else return('');
		}else return('');
	}
  </script>
  </head>
  <body topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0" onLoad="myOnload();">
<table cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
<tr><td>
	<table id="banner" width="100%" border="0" cellspacing="0" cellpadding="0" class="top">
	  <tr>
		<!-- <td height="52" align="left"><img src="images/manage/top_01.gif"></td><td align="right"><img src="images/manage/top_03.gif"></td> -->
	  	<td height="52" align="left"><img src="images/manage/top_01.gif"></td>
	  </tr>
	</table>
</td></tr>
<tr><td>
	<script>document.write(topMenu);</script>
</td></tr>
<tr><td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr> 
		<td width="16" height="23" class="tool01">&nbsp; </td>
		<td valign="bottom" class="tool02" id="smallMenu"></td>
	  </tr>
	</table>
</td></tr>
<tr><td>
	<script>chuangSmallMenu(0);</script>
</td></tr>
<tr><td height="100%">
	<IFRAME width="100%" height="100%" marginheight="0" frameborder="0" marginwidth="0" id="adminWindow" name="adminWindow" scrolling="auto"src="welcome.jsp"></IFRAME>
</td></tr>
</table>
</body>
</html>
  