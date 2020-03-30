<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="wing" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>
	<link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script language="javascript">
		function checkForm(ajaxFrm){
			if(ajaxFrm.password.value==""){
				alert("请输入口令");
				ajaxFrm.password.focus();
				return false;
			}
			if(ajaxFrm.newpassword.value==""){
				alert("请输入新口令");
				ajaxFrm.newpassword.focus();
				return false;
			}
			if(ajaxFrm.newpassword.value != ajaxFrm.newpassword1.value){
				alert("两次输入的口令不一致");
				ajaxFrm.newpassword1.select();
				return false;
			}
			return true;
		}
		
		function savePsw(){
			var ajaxFrm = document.forms["savePassword"];
			ajaxFrm.act.value = "MODIFYPWD";
			if(checkForm(ajaxFrm)){
				AJForm.submitForm(ajaxFrm);
			}
			
		}
		
		function getReturnData(data , statusCode , statusMessage){
		 	if( statusCode != AJForm.STATUS['SUCCESS'] ) {
		 		alert( statusMessage );
				return true;
		 	}
		 	var oDomDoc = Sarissa.getDomDocument();
			oDomDoc = (new DOMParser()).parseFromString(data, "text/xml");
		 	
			var status = oDomDoc.selectSingleNode("/root/Return").text;
			if (status == "1"){
				alert(oDomDoc.selectSingleNode("/root/Content").text);
			}else{
				alert(oDomDoc.selectSingleNode("/root/Error").text);
			}
		}		
		
	</script>   
  </head>
		  
<body>
<form NAME="savePassword" method="post" action="manage/user" onSubmit="ajform:getReturnData">
	<INPUT type="hidden" name="act" value="MODIFYPWD">

	<br>
	<table width="60%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
	<tr>
		<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
				<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02">修改密码</td>
				<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
	
	<TABLE width="60%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
		<wing:permission module_id="USERINFO-PWD" operator_id="modify">
			<input type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="savePsw();" value="提交">
		</wing:permission>
		</td>
	</tr>
	<tr>
      <td height="200" align="center" valign="top" class="conKa05">
		 <table width="430" align="center" cellpadding="4">
           <tr  >
             <td width="89" height="30"  align="right">您的旧口令：</td>
             <td width="212" ><input name="password" type="password" size="40" >
             </td>
           </tr>
           <tr  >
             <td width="89" height="30"  align="right" id="nameShow">输入新口令：</td>
             <td ><input name="newpassword" type="password" size="40">
             </td>
           </tr>
           <tr   id="stz">
             <td width="89" height="27" align="right">新口令确认：</td>
             <td><input name="newpassword1" type="password" size="40"></td>
           </tr>
         </table>
       </td>
     </tr>
     </table>
    </form>
  </body>
</html>
