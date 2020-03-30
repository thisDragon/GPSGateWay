<%-- 本页面用于检索用户 --%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.web.util.DataFormater,com.cari.sys.biz.OrganManage,com.cari.sys.bean.Organ"%>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="wing" %>
<%
	String dept = request.getParameter("dept");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/HeadInclude.jsp"%>   
    <title>用户检索</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script>
		function btAction(actionName){
			var userForm = document.getElementById('userForm');
			switch(actionName){
				case "QUERY":
					userForm.act.value = "QUERY";
					userForm.isNewQuery.value = 'true';
					userForm.submit();
					break;

				case "CANCEL":
					userForm.act.value = "QUERY";
					userForm.submit();
					break;
			}
		}
		
	</script>
</head>
<body  class="listMargin" >
<form name="userForm" method="post" action="manage/user" >
	<input type=hidden name="act" >
	<input name="isNewQuery" type="hidden">
	<input type=hidden name="dept" value="<%=DataFormater.noNullValue(dept)%>">
	<br>
	<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
	<tr>
		<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
				<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02">用户查找</td>
				<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
	
	<TABLE width="90%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
			<cari:permission module_id="SYS-USER" operator_id="view">
			<input name="query"  type="button" style="background-image:url(images/manage/contentBtn4.gif);" class="contentBtn4" onClick="btAction('QUERY');" value="开始查找">
			</cari:permission>
			<input name="cancel"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('CANCEL');" value="返回">
		</td>
	</tr>
    <tr>
		<td height="260" align="center" valign="top" class="conKa05">
	        <table width="530" align="center" cellpadding="3">
	        <tr >
	            <td width="77" align="right" nowrap>部 门:</td>
	            <td cols="3" nowrap>
	            <%
	            	OrganManage om = OrganManage.getInstance();
					Organ organ = null;
					String deptName = "根目录";
					organ = om.getOrganByID(dept);
					if (organ != null){
						deptName = organ.getOrganName();
					} 
				 %>
	                <input name="deptName" type="text" disabled size="30" value="<%=deptName %>">
	            </td>
	        </tr>
	        <tr >
	            <td width="77" align="right" nowrap>工 号:</td>
	            <td cols="3" nowrap>
	                <input name="userId" type="text" size="30" value="">
	            </td>
	        </tr>
	        <tr>
	            <td align="right" nowrap>姓 名:</td>
	            <td cols="3"><input name="userName" type="text" size="30" value="">
	            </td>
	        </tr>
	        <tr >
	            <td width="77" align="right" nowrap>手 机:</td>
	            <td cols="3" nowrap>
	                <input name="mobile" type="text" size="30" value="">
	            </td>
	        </tr>
	        </table>
         </td>
	</tr>
	</table>
</form>
</body>
</html>
