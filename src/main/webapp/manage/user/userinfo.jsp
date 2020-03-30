<%-- 本页面用于用户修改自身资料 --%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.sys.bean.SysUser,com.cari.web.util.*"%>			
<%@ page import="com.cari.sys.bean.Organ,com.cari.sys.biz.OrganManage"%>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="wing" %>
<%
	SysUser user = (SysUser)request.getAttribute("USERMANAGE:USER");
	if (user == null){
		response.sendRedirect("../login.jsp");
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<%@include file="/HeadInclude.jsp"%>   
    <title>用户信息</title>
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
  </head>
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
			}else{
				alert(oDomDoc.selectSingleNode("/root/Error").text);
			}
		}

		function btAction(actionName){
			var ajaxForm = document.all['userForm'];
			switch(actionName){
				case "SAVEORUPDATE":
					if(checkForm()){ 
						ajaxAction = "SAVEORUPDATE";
						ajaxForm.act.value = "SAVEORUPDATE";
						AJForm.submitForm(ajaxForm);
					}
					break;
			}
		}
		
		function checkForm(){
			var ajaxForm = document.all['userForm'];
			if(ajaxForm.userName.value == ""){
				alert("请输入用户名");
				return false;
			}
			return true; 
		}
  </script>
<body  class="listMargin" >
<form name="userForm" method="post" action="manage/user" onSubmit="ajform:getReturnData" >
	<input type=hidden name="act" >
	<input type=hidden name="preop" value="SHOWDETAIL">
		
	<br>
	<table width="60%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
	<tr>
		<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
				<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02">个人资料维护</td>
				<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
	
	<TABLE width="60%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
		<wing:permission module_id="USERINFO-INFO" operator_id="modify">
			<input type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('SAVEORUPDATE');" id="submitbtn" value="保存">
		</wing:permission>
			<input type="reset" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" value="重置">
		</td>
	</tr>
    <tr>
      <td height="260" align="center" valign="top" class="conKa05">
        <table width="600" align="center" cellpadding="3" border="0">
          <tr >
            <td width="100" align="right">工 号：</td>
            <td cols="3"><%=DataFormater.noNullValue(user.getUserId())%></td>
          </tr>
          <tr>
            <td width="100" align="right"> * 姓 名：</td>
            <td width="200"><input name="userName" type="text" size="24"  value="<%=DataFormater.noNullValue(user.getUserName())%>"></td>
            <td width="100" align="right">性 别：</td>
            <td with="200"><input type="radio" name="sex" value="男" <%="男".equals(user.getSex())?"checked":""%>>男&nbsp;&nbsp;
            	<input type="radio" name="sex" value="女" <%="女".equals(user.getSex())?"checked":""%>>女
            </td>
          </tr>
          <tr>
          	<td width="100" align="right">部 门：</td>
          	<%
				OrganManage om = OrganManage.getInstance();
				Organ organ = null;
				String deptName = "";
				organ = om.getOrganByID(user.getDept());
				if (organ != null){
					deptName = organ.getOrganName();
				}
			%>
            <td width="200"><%=deptName%></td>
            <td width="100" align="right">职 务：</td>
            <td width="200"><input name="duty" type="text" size="16" value="<%=DataFormater.noNullValue(user.getDuty())%>"></td>
          </tr>
          <tr>
            <td width="100" align="right">地 址：</td>
            <td width="200"><input name="address" type="text" size="24"  value="<%=DataFormater.noNullValue(user.getAddress())%>"></td>
            <td width="100" align="right">邮 编：</td>
            <td width="200"><input name="postCode" type="text" size="16" value="<%=DataFormater.noNullValue(user.getPostCode())%>"></td>
          </tr>
          <tr >
            <td width="100" align="right">E-mail：</td>
            <td width="200"><input name="email" type="text" size="24" verify="email" value="<%=DataFormater.noNullValue(user.getEmail())%>"></td>
            <td width="100" align="right">电 话：</td>
            <td width="200"><input name="tel" type="text" size="16" value="<%=DataFormater.noNullValue(user.getTel())%>"></td>
          </tr>
          <tr>
            <td width="100" align="right">手 机：</td>
            <td colspan=3><input name="mobile" type="text" size="24" value="<%=DataFormater.noNullValue(user.getMobile())%>"></td>
          </tr>
          <tr >
            <td width="100" height="80" align="right">备 注：</td>
            <td colspan="3"><textarea name="remark" class="texta" style="width:450px;height:100%"><%=DataFormater.noNullValue(user.getRemark())%></textarea>
            </td>
          </tr>
         </table>
       </td>
     </tr>
     </table>
	</form>
  </body>
</html>
