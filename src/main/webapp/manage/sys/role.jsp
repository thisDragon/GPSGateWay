<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.rbac.Role,com.cari.web.util.*"%>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="wing" %>
		
<%
	Role role = (Role)request.getAttribute("ROLEMANAGE:ROLE");
	boolean isRoot = false;
	if ("RBACRole".equals(role.getRole_id())){
		isRoot = true;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/HeadInclude.jsp"%>   
    <title>系统用户角色</title>
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
</head>
<script>
		var ajaxAction = "";
		var id = "<%=role.getRole_id()%>";

		function getReturnData(data , statusCode , statusMessage){
		 	if( statusCode != AJForm.STATUS['SUCCESS'] ) {
		 	 alert( statusMessage );
			 return true;
		 	}
			//获取返回信息
			var oDomDoc = Sarissa.getDomDocument();
			oDomDoc = (new DOMParser()).parseFromString(data, "text/xml");
			var status = oDomDoc.selectSingleNode("/root/Return").text;
			if (status == "1"){
				alert(oDomDoc.selectSingleNode("/root/Content").text);
				if (ajaxAction == "SAVEORUPDATE"){
					if("SHOWORGANDETAIL" == roleForm.preop.value){
						parent.frLeft.updateNode(id,roleForm.role_name.value);
					}
					if("ADD" == roleForm.preop.value){
				    	parent.frLeft.appendNode(id,'<%=Role.ROOT.getRole_id()%>',roleForm.role_name.value,'javascript:linkToRight("' + id + '")');
					}
					roleForm.preop.value="SHOWDETAIL";
				}else if(ajaxAction == "DELETE"){
					parent.frLeft.removeNode(id);
					roleForm.act.value = "SHOWDETAIL";
		            roleForm.role_id.value = "<%=Role.ROOT.getRole_id()%>";
					roleForm.submit();
				}
			}else{
				alert(oDomDoc.selectSingleNode("/root/Error").text);
				document.all("role_name").select();
			}
		}

		function btAction(actionName){
			switch(actionName){
				case "ADD":
					roleForm.act.value="ADD";
					roleForm.submit();
					break;
					
				case "DELETE":
					if (id == "<%=Role.ROOT.getRole_id()%>"){
					   alert("该节点是根节点,不能删除!");
					   return false;
					}
					if(id == ''){
						alert('请先在左边选择一个节点.');
						return false;
					}
					if (parent.frLeft.document.getElementById(id).getAttribute("class")=="node"){
							window.alert("该节点不是叶节点，不能删除");
							return false;
					}
					if (confirm("即将删除此角色！而且，正在使用本角色的用户，将失去本角色。\n该操作无法恢复，是否继续？")) {
						ajaxAction = "DELETE";
						roleForm.act.value="DELETE";
						AJForm.submitForm(roleForm);
					}
					break;
					
				case "SAVEORUPDATE":
					if(checkForm()){ 
						ajaxAction = "SAVEORUPDATE";
						roleForm.act.value="SAVEORUPDATE";
						AJForm.submitForm(roleForm);
					}
					break;

				case "CANCEL":
					roleForm.act.value = "SHOWDETAIL";
		            roleForm.role_id.value = "<%=Role.ROOT.getRole_id()%>";
					roleForm.submit();
					break;
					
			}
		}

		function checkForm(){
			if(roleForm.role_name.value == ""){
				alert("请填写角色名称。");
				roleForm.role_name.focus();
				return false;
			}
			return true; 
		}
</script>
<body onload="document.all('role_name').select();" class="listMargin">
<form name="roleForm" method="post" action="manage/role" onsubmit="ajform:getReturnData">
	<input type=hidden name="act" >
	<input type=hidden name="preop" value="<%=DataFormater.noNullValue(request.getParameter("preop"))%>">
	<input type=hidden name="role_id" value="<%=DataFormater.noNullValue(role.getRole_id())%>">
	<br>
	<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
	<tr>
		<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
				<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02">角色管理</td>
				<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
	
	<TABLE width="90%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
			<wing:permission module_id="SYS-ROLE" operator_id="add">
			<input name="add"  type="button" style="background-image:url(images/manage/contentBtn4.gif);" class="contentBtn4" onClick="btAction('ADD');" value="新增">
			</wing:permission>
			<%if (!isRoot){ %>
			<wing:permission module_id="SYS-ROLE" operator_id="delete">
			<input name="deletebutton"  style="background-image:url(images/manage/contentBtn.gif);" type="button" class="contentBtn" onClick="btAction('DELETE');" value="删除">
			</wing:permission>
			<wing:permission module_id="SYS-ROLE" operator_id="add,modify">
			<input name="save"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('SAVEORUPDATE');" value="保存">
			</wing:permission>
			<%} %>
			<input name="cancel"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('CANCEL');" value="返回">
		</td>
	</tr>
    <tr>
    	<td height="260" align="center" valign="top" class="conKa05">
			<table width="90%" align="center" cellpadding="4">
	        <tr><td height="30"  align="right">角色名称:</td>
	            <td nowrap>
	            	<input name="role_name" type="text" size="45" <%=isRoot?"disabled":"" %> value="<%=DataFormater.noNullValue(role.getRole_name())%>">
	            </td>
	        </tr>
	        <tr>
	            <td height="30"  align="right" id="nameShow" valign="top">角色描述:</td>
	            <td ><textarea name="role_desc" cols="46" rows="5" class="texta" <%=isRoot?"disabled":"" %>><%=DataFormater.noNullValue(role.getRole_desc())%></textarea></td>
	        </tr>
	        </table>
		</td>
	</tr>
	</table>
</form>
</body>
</html>