<%-- 本页面用于对用户进行列表 --%>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.cari.web.util.*,
				com.cari.sys.bean.*,
				com.cari.sys.biz.*,
				com.cari.web.comm.ListPage" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="cari" %>
<%
	ListPage listPage = (ListPage)request.getAttribute("USERMANAGE:LIST");
	long nPageCount = listPage.getTotalPageCount();
	String dept = request.getParameter("dept");
	String sTitle = "无部门用户";
	if (dept != null){
		OrganManage om = OrganManage.getInstance();
		Organ organ = om.getOrganByID(dept);
		if (organ != null){
			sTitle = organ.getOrganName();
		}
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
    <script>
   	 	var ajaxAction = "";
	
		function btAction(actionName , dwKey){
			switch(actionName){
			case "ADD":
				userForm.act.value="ADD";
				userForm.submit();
				break;
				
			case "SHOWDETAIL":
				var thisForm = document.forms["userForm"];
				var ids=thisForm.userId;
				if(ids==null) return false;
				if(ids.length == null){
					//列表中只有一条记录显示，默认用户已选中
					ids.checked = true;
				}else{
					//遍历列表
					var count=0;
					for(var i = 0 ; i < ids.length ; i ++){
						if(ids[i].checked){
							count++;
						}
						if(count > 1){
							//用户选择多条记录，提示并终止提交
							alert("请确定只选择一条记录");
							return; 
						}
					}
					
					if(count == 0){
						//用户没有选择记录，提示并终止提交
						alert("请选择一条记录");
						return; 
					}
				}
				thisForm.act.value = "SHOWDETAIL";
				thisForm.submit();
				break;
			case "RESETPWD":
				var thisForm = document.forms["userForm"];
				var ids=thisForm.userId;
				if (ids==null) return false;
				if(ids.length == null){
					//列表中只有一条记录显示
					if(!ids.checked){
						alert("请选择要重置密码的用户");
						return;
					}
				}else{
					//遍历列表
					var count=0;
					for(var i = 0 ; i < ids.length ; i ++){
						if(ids[i].checked){
							count ++;
						}
					}
					
					if(count == 0){
						//用户没有选择记录，提示并终止提交
						alert("请选择一条记录");
						return; 
					}
				}
				if(confirm("确认要重置密码为\"888888\"吗？")){			
					thisForm.act.value = "RESETPWD";
					ajaxAction = "RESETPWD";
					AJForm.submitForm(thisForm);
				}					
				break;
				
			case "DELETE":
				var thisForm = document.forms["userForm"];
				var ids=thisForm.userId;
				if (ids==null) return false;
				if(ids.length == null){
					//列表中只有一条记录显示
					if(!ids.checked){
						alert("请选择要删除的记录");
						return;
					}
				}else{
					//遍历列表
					var count=0;
					for(var i = 0 ; i < ids.length ; i ++){
						if(ids[i].checked){
							count ++;
						}
					}
					
					if(count == 0){
						//用户没有选择记录，提示并终止提交
						alert("请选择一条记录");
						return; 
					}
				}
				if(confirm("确认要删除记录吗？")){			
					thisForm.act.value = "DELETE";
					ajaxAction = "DELETE";
					AJForm.submitForm(thisForm);
				}					
				break;

			case "TOQUERY":
				var thisForm = document.forms["userForm"];
					thisForm.act.value = "TOQUERY";
					thisForm.submit();
				break;

			}
		}						
		function getReturnData( data , statusCode , statusMessage) {
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
				if(ajaxAction == "DELETE" || ajaxAction == "RESETPWD"){
					ajaxAction = "";
					var thisForm = document.forms["userForm"];
					thisForm.act.value = "QUERY";
					thisForm.submit();
				}
			}else{
				alert(oDomDoc.selectSingleNode("/root/Error").text);
			}
	 	}

		function  gotoPage(pageNo){
			if (pageNo == null || pageNo == "") return;
			if (isNaN(pageNo)) return;
			if (pageNo < 1) pageNo = 1;
			if (pageNo > <%=nPageCount%>) pageNo = <%=nPageCount%>;
			var thisForm = document.forms["userForm"];
			thisForm.act.value = "QUERY";
			thisForm.pageno.value = pageNo;
			thisForm.submit();
		}
		
		function selectAll(){
			var checkStatus = document.getElementById("checkall").checked;
			var checkBoxes = document.getElementsByName("userId");
			for(var i = 0 ; i < checkBoxes.length ; i++){
				checkBoxes[i].checked = checkStatus;
			}
		}
    </script>
</head>
<body>
<form NAME="userForm" method="post" action="manage/user"  onsubmit="ajform:getReturnData" >
	<INPUT type="hidden" name="act" value="">
	<input type="hidden" name="pageno" value="<%=listPage.getCurrentPageNo()%>">
	<INPUT type="hidden" name="dept" value="<%=DataFormater.noNullValue(request.getParameter("dept"))%>">
	
	<br>
		<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
	<tr>
		<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
				<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02"><%=sTitle %></td>
				<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
	
	<TABLE width="95%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="9" style="border-top-width:0px">
			<table bordr=0 width="100%">
			<tr>
				<td width="50%">
					<cari:permission module_id="SYS-USER" operator_id="add">
					<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('ADD' , '');" value="新增">
					</cari:permission>
					<cari:permission module_id="SYS-USER" operator_id="add,modify">
					<input name="modify"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('SHOWDETAIL' , '');" value="修改">
					</cari:permission>
					<cari:permission module_id="SYS-USER" operator_id="delete">
					<input name="delete"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('DELETE' , '');" value="删除">
					</cari:permission>
					<cari:permission module_id="SYS-USER" operator_id="view">
					<input name="toquery"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('TOQUERY' , '');" value="查找">
					</cari:permission>
					<cari:permission module_id="SYS-USER" operator_id="modify">
					<input name="pwreset"  type="button" style="background-image:url(images/manage/contentBtn4.gif);" class="contentBtn4" onClick="btAction('RESETPWD' , '');" value="重置密码">
					</cari:permission>
				</td>
				<td width="50%" align=right>
					&nbsp;&nbsp;<A href="javascript:gotoPage(1);">首 页</A>&nbsp;&nbsp;
					<%	if(listPage.hasPreviousPage()){ %>
							<A href="javascript:gotoPage(<%=listPage.getCurrentPageNo()-1%>);">上一页</A>&nbsp;&nbsp;
					<%	}else{%>
							上一页&nbsp;&nbsp;
					<%	}	%>
							共<%=listPage.getTotalPageCount()%>/<%=listPage.getCurrentPageNo()%>页&nbsp;&nbsp;
					<%	if(listPage.hasNextPage()) {	%>
							<A href="javascript:gotoPage(<%=listPage.getCurrentPageNo()+1%>);">下一页</A>&nbsp;&nbsp;
					<%	} else {	%>
							下一页&nbsp;&nbsp;
					<%	}	%>
					<A href="javascript:gotoPage(<%=listPage.getTotalPageCount()%>);">尾 页</A>
					到<input name="no" onkeypress="if(event.keyCode==13){return false;}" style="BORDER-RIGHT: #dddddd 1px solid; BORDER-TOP: #dddddd 1px solid; BORDER-LEFT: #dddddd 1px solid; BORDER-BOTTOM: #dddddd 1px solid; HEIGHT: 16px" type="text" size="2">
					页<A href="javascript:gotoPage(document.userForm.no.value);">GO</A>
				</td>
			</tr>
			</table>
		</td>
	</tr>	
	<TR height="24" align="center">
		<TD width="5%" align="center" style="font-weight:bold"><input type="checkbox" id="checkall" onclick="selectAll();"></TD>
	  	<TD width="5%" align="center" style="font-weight:bold">序号</TD>
	  	<TD width="10%" align="center" style="font-weight:bold">工号</TD>
		<TD width="15%" align="center" style="font-weight:bold">姓名</TD>
		<TD width="20%" align="center" style="font-weight:bold">部门</TD>
		<TD width="15%" align="center" style="font-weight:bold">手机</TD>
		<TD width="30%" align="center" style="font-weight:bold">Email</TD>
	</TR>
<%
	if(listPage != null && !listPage.isEmpty()) {
		OrganManage om = OrganManage.getInstance();
		Organ organ = null;
		SysUser user = null;
		String deptName = "";
		List userList = listPage.getDataList();
		int index = (listPage.getCurrentPageNo() - 1) * listPage.getCurrentPageSize() + 1;
		for(Iterator it = userList.iterator(); it.hasNext(); ){
			user = (SysUser)it.next();
			organ = om.getOrganByID(user.getDept());
			if (organ != null){
				deptName = organ.getOrganName();
			}
%>	
	<TR height="24">
		<TD align="center"><INPUT type="checkbox" name="userId" value="<%=user.getUserId()%>"></TD>
		<TD align="center"><%=index%></TD>
		<TD><%=user.getUserId()%></TD>
		<TD><%=DataFormater.noNullValue(user.getUserName())%></TD>
		<TD><%=deptName%></TD>
		<TD><%=DataFormater.noNullValue(user.getMobile())%></TD>
		<TD><%=DataFormater.noNullValue(user.getEmail())%></TD>
	</TR>
<%		
			index++;
		}
	}
%>
	</TABLE>
</form>
</body>
</html>
