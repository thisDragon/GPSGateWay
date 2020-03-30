<%-- 本页面用于注册用户/修改用户信息 --%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.sys.bean.*,
				com.cari.sys.biz.OrganManage,
				com.cari.rbac.Role,com.cari.web.util.*"%>

<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="cari" %>

<%
	SysUser user = (SysUser)request.getAttribute("USERMANAGE:USER");
	OrganManage om = OrganManage.getInstance();
	Organ organ = null;
	String organName = "";
	ArrayList myRoles = new ArrayList(0);
	if(user != null){
		myRoles = new ArrayList(user.getRoles());
		organ = om.getOrganByID(user.getDept());
		if (organ != null){
			organName = DataFormater.noNullValue(organ.getOrganName());
		}
	}
	List allRoles = (List)request.getAttribute("ROLEMANAGE:LIST");
	allRoles.removeAll(myRoles);
	
	boolean isModify = "SHOWDETAIL".equals(request.getParameter("preop"));//是否为修改用户操作
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/HeadInclude.jsp"%>   
    <title>用户信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script>
		var ajaxAction = "";
	
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
				if(ajaxAction == "CHECK"){
					ajaxAction = "";
				} else if (ajaxAction == "SAVEORUPDATE"){
					var ajaxForm = document.all['userForm'];
					ajaxForm.act.value = "QUERY";
					ajaxForm.submit();
				}
			}else{
				alert(oDomDoc.selectSingleNode("/root/Error").text);
			}
		}

		function btAction(actionName){
			var ajaxForm = document.all['userForm'];
			switch(actionName){
				case "CHECK" :
					if(ajaxForm.userId.value == ""){
						alert("请输入员工号");
						ajaxForm.userId.focus();
						return;
					}
					ajaxAction = "CHECK";
					ajaxForm.act.value = "CHECK";
					AJForm.submitForm(ajaxForm);
					break;
						
				case "SAVEORUPDATE":
					if(checkForm()){ 
						ajaxAction = "SAVEORUPDATE";
						ajaxForm.act.value = "SAVEORUPDATE";
						var sourceSel = document.getElementById('myRoles');
						var tempRoles = "";
						for(var i = 0 ; i < sourceSel.options.length ; i+=1){
							tempRoles += sourceSel.options[i].value + ",";
						}
						ajaxForm.roles.value = tempRoles;
						AJForm.submitForm(ajaxForm);
					}
					break;

				case "CANCEL":
					ajaxForm.act.value = "QUERY";
					ajaxForm.submit();
					break;
						
			}
		}
		
		function checkForm(){
			var ajaxForm = document.all['userForm'];
			if(ajaxForm.userId.value == ""){
				alert("请输入员工号");
				ajaxForm.userId.focus();
				return false;
			}
			if(ajaxForm.userName.value == ""){
				alert("请输入用户名");
				ajaxForm.userName.focus();
				return false;
			}
			if(ajaxForm.email.value == ""){
				alert("请输入邮箱");
				ajaxForm.email.focus();
				return false;
			}
			if(ajaxForm.mobile.value == ""){
				alert("请输入手机号码");
				ajaxForm.mobile.focus();
				return false;
			}
			return true; 
		}
		
		function roleMove(souceId,targetId,flag){
			var sourceSel = document.all[souceId];
			var targetSel = document.all[targetId];
			var nSel = sourceSel.selectedIndex;
			if (nSel == -1) {
				if (flag == 0)
					alert("请选择要移去的角色！");
				else
					alert("请选择要添加的角色！");
				return false;
			}
			targetSel.options[targetSel.options.length] = new Option(sourceSel.options[nSel].text,sourceSel.options[nSel].value);
			sourceSel.options[nSel] = null;
		}
		//显示选择部门
		function selectDept(){
			window.open("../manage/sys/userDepart.jsp","","width=280,height=320");
		}
	</script>
</head>
<body  class="listMargin" >
<form name="userForm" method="post" action="manage/user" onsubmit="ajform:getReturnData" >
	<input type=hidden name="act" >
	<input type=hidden name="roles" >
	<input type=hidden name="preop" value="<%=DataFormater.noNullValue(request.getParameter("preop"))%>">
	
	<br>
	<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
	<tr>
		<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
				<td style="background-image:url(images/manage/conTitle02.jpg);" class="title02">用户管理</td>
				<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
	
	<TABLE width="90%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
			<cari:permission module_id="SYS-USER" operator_id="add,modify">
			<input name="save"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('SAVEORUPDATE');" value="保存">
			</cari:permission>
			<input name="cancel"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('CANCEL');" value="返回">
		</td>
	</tr>
    <tr>
		<td align="center" valign="top" class="conKa05">
        <table width="90%" align="center" cellpadding="3">
        <tr >
            <td  align="right"> * 工号:</td>
            <td cols="3" nowrap>
                <input id="userid" name="userId" type="text"  size="20" value="<%=DataFormater.noNullValue(user.getUserId())%>" <%=isModify? "readOnly" : ""%> > 
<% if (!isModify) {  	%>             
                <A href="javascript:btAction('CHECK')"><font color="red">检 测</font></A>
<% }  	%>               
            </td>
        </tr>
        <tr>
            <td align="right"> * 姓名:</td>
            <td cols="3"><input name="userName" type="text" size="25"  value="<%=DataFormater.noNullValue(user.getUserName())%>">
            </td>
        </tr>
        <tr>
            <td align="right" valign="top"> 部门:</td>
            <td >
            <%--
            <label name="dept" id="dept"><%=DataFormater.noNullValue(user.getDept())%></label>
            --%>
            <input type="hidden" name="dept"  value="<%=DataFormater.noNullValue(user.getDept())%>">
            <input type="text" name="deptname" readonly="readonly" value="<%=organName%>">
            <A href="javascript:selectDept()"><font color="red">选 择</font></A>
            </td>
           
        </tr>
        <tr>
            <td height="25" align="right">职务:</td>
            <td><input name="duty" type="text" size="25" value="<%=DataFormater.noNullValue(user.getDuty())%>"></td>
            <td align="right">邮编:</td>
            <td><input name="postCode" type="text" id="邮编" size="16" value="<%=DataFormater.noNullValue(user.getPostCode())%>"></td>
        </tr>
        <tr >
            <td height="25" align="right">地址:</td>
            <td><input name="address" type="text" id="地址" size="25"  value="<%=DataFormater.noNullValue(user.getAddress())%>"></td>
            <td align="right">性别:</td>
            <td><select name="sex" size="1" class="texta" style="width:120">
            		<option value="未知" <%="未知".equals(user.getSex()) ? "selected" : ""%>>未知</option>
            		<option value="男"  <%="男".equals(user.getSex()) ? "selected" : ""%>>男</option>
            		<option value="女"  <%="女".equals(user.getSex()) ? "selected" : ""%>>女</option>
            </select></td>
        </tr>
        <tr >
            <td height="25" align="right"> * E-mail:</td>
            <td><input name="email" type="text" id="E-mail" size="25" verify="email" value="<%=DataFormater.noNullValue(user.getEmail())%>"></td>
            <td align="right">电话:</td>
            <td><input name="tel" type="text" id="电话" size="16" value="<%=DataFormater.noNullValue(user.getTel())%>"></td>
        </tr>
        <tr>
            <td align="right"> * 手机:</td>
            <td><input name="mobile" type="text" id="电话" size="25" value="<%=DataFormater.noNullValue(user.getMobile())%>"></td>
        </tr>
        <tr >
            <td width="77" height="90" align="right" >备注:</td>
            <td colspan="3"><textarea name="remark" cols="54" rows="5" class="texta"><%=DataFormater.noNullValue(user.getRemark())%></textarea>
            </td>
        </tr>
        <tr >
            <td align="center" colspan="4">
            	<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
            		<tr>
            			<td align="center"><strong>已有的角色：</strong><br>
							<select name="myRoles" size="8" style="width:170 " id="myRoles" ondblclick="javascript:roleMove('myRoles','otherRoles',0);">
<%
		for(int i = 0 ; i < myRoles.size() ; i+=1){
			Role role = (Role)myRoles.get(i);
%>			
         						<option value="<%=role.getRole_id()%>"><%=role.getRole_name()%></option>
<%		}							
%>         						
                             </select>            			
                        </td>
						<td align="center">
							<p>
                              <input name="Submit" type="button" class="dt" style="background-image:url(images/manage/btn3.gif);" onClick="javascript:roleMove('otherRoles','myRoles',1);" value="&lt;-添加">
                            </p>
                            <p>
                                <input name="Submit2" type="button" class="dt" style="background-image:url(images/manage/btn3.gif);" onClick="javascript:roleMove('myRoles','otherRoles',0);" value="移去->">
                            </p>
                        </td>            			
                        <td align="center"><strong>未分配的角色：</strong><br>
							<select name="otherRoles" size="8" style="width:170 " id="otherRoles" ondblclick="javascript:roleMove('otherRoles','myRoles',1);">
<%
		for(int i = 0 ; i < allRoles.size() ; i+=1){
			Role role = (Role)allRoles.get(i);
%>			
         						<option value="<%=role.getRole_id()%>"><%=role.getRole_name()%></option>
<%		}							
%>         						
                             </select>            			
                        </td>
            		<tr>
            		</table>
            	</td>
        	</tr>
        	</table>
		</td>
	</tr>
	</table>
</form>
</body>
</html>
