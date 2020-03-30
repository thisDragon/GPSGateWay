<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="cari" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>
	<link rel="stylesheet" type="text/css" href="css/treestyle.css">
    <script>
		function btAction(actionName){
			switch(actionName){
				case "QUERY":
					poiFrm.submit();
					break;
			}
		}
    </script>
</head>
<body>
<form NAME="poiFrm" method="post" action="manage/log">
	<INPUT type="hidden" name="act" value="QUERY">
	<input type="hidden" name="isNewQuery" value="Y">
	<br>
	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
	<tr>
		<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
				<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02">系统日志检索</td>
				<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
	
	<TABLE width="80%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
			<input type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onclick="btAction('QUERY');" value="开始查找">
		</td>
	</tr>
   <tr>
     	<td height="260" align="center" valign="top" class="conKa05">
	 		<table width="550" align="center" cellpadding="4">
			<TR>
				<TD width="130" height="30" align="center">用户姓名：</TD>
				<TD><input type="text" name="userName" >&nbsp;</TD>
				<TD width="130" align="center">用户 ID：</TD>
				<TD><input name="userID" type="text"/></TD>
			</TR>
			<TR>
				<TD height="30" align="center">模块名称：</TD>
				<TD><input name="model" type="text"/></TD>
				<TD align="center">操作类型：</TD>
				<TD><input type="text" name="event"></TD>
			</TR>
			</table>
		</td>
	</tr>
	</table>
</form>
</body>
</html>
