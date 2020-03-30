<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.sys.bean.Organ,com.cari.web.util.*"%>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="wing" %>

<%
	Organ organ = (Organ)request.getAttribute("ORGANMANAGE:ORGANENTITY");
	Organ parentOrgan = (Organ)request.getAttribute("ORGANMANAGE:PARENTORGAN");
	                                                              
	String parentOrganName = "";
	if(parentOrgan != null){
		parentOrganName = parentOrgan.getOrganName();
	}
	
	boolean isRoot = false;
	boolean isNew = false;
	if (organ.getOrganID()==null || "".equals(organ.getOrganID())){
		isNew = true;
	}
	if ("OrganEntity.ROOT".equals(organ.getOrganID())){
		isRoot = true;
	}
	String sTitle = organ.getOrganName();
	if (sTitle == null || "".equals(sTitle)){
		sTitle = "组织机构配置";
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/HeadInclude.jsp"%>   
    <title>组织机构信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script>
		var ajaxAction = "";
		var organ_id = "<%=organ.getOrganID()%>";

		function getReturnData(data , statusCode , statusMessage){
		 	//AJFORM failed. Submit form normally.
		 	if( statusCode != AJForm.STATUS['SUCCESS'] ) {
		 	 alert( statusMessage );
			 return true;
		 	}
		 	//AJFORM succeeded.
			//获取返回信息
			var oDomDoc = Sarissa.getDomDocument();
			oDomDoc = (new DOMParser()).parseFromString(data, "text/xml");
			var status = oDomDoc.selectSingleNode("/root/Return").text;
			
			if (status == "1"){
				alert(oDomDoc.selectSingleNode("/root/Content").text);
				if (ajaxAction == "SAVEORUPDATE"){
					if("SHOWORGANDETAIL" == organForm.preop.value){
						parent.frLeft.updateNode(organ_id,organForm.organName.value);
					}
					if("ADDCHILD" == organForm.preop.value){
						if (organ_id.lastIndexOf("-") == -1){
							if(organ_id == ""){
			            		organ_id = organForm.organCode.value
			            	}
			                parent.frLeft.appendNode(
				            organ_id,
				            '<%=Organ.ROOT_ORGAN.getOrganID()%>',
				            organForm.organName.value,
				           'javascript:linkToRight("' + organ_id + '")');
			            }else{
				            parent.frLeft.appendNode(
				            organ_id,
				            organ_id.substring(0,organ_id.lastIndexOf("-")),
				            organForm.organName.value,
				           'javascript:linkToRight("' + organ_id + '")');
						}
					}
					organForm.preop.value="SHOWORGANDETAIL";
				}else if(ajaxAction == "DELETE"){
					parent.frLeft.removeNode(organ_id);
					organForm.act.value = "SHOWORGANDETAIL";
					if (organ_id.lastIndexOf("-") == -1){
		                organForm.organID.value = "<%=Organ.ROOT_ORGAN.getOrganID()%>";
		            }else{
						organForm.organID.value = organ_id.substring(0,organ_id.lastIndexOf("-"));
					}
					organForm.submit();
				}
			}else{
				alert(oDomDoc.selectSingleNode("/root/Error").text);
			}
		}

		function btAction(actionName){
			switch(actionName){
				case "ADDCHILD":
					organForm.act.value="ADDCHILD";
					organForm.submit();
					break;
					
				case "DELETE":
					if (organ_id == "<%=Organ.ROOT_ORGAN.getOrganID()%>"){
					   alert("该节点是根节点,不能删除!");
					   return false;
					}
					if(organ_id == ''){
						alert('请先在左边选择一个节点.');
						return false;
					}

					if (parent.frLeft.document.getElementById(organ_id).getAttribute("class")=="node"){
							window.alert("该节点不是叶节点，不能删除");
							return false;
					}
				
					if(confirm("删除机构将同时删除机构内的人员！\n确定删除该信息?")){
						ajaxAction = "DELETE";
						organForm.act.value="DELETE";
						AJForm.submitForm(organForm);
					}
					break;
					
				case "SAVEORUPDATE":
					if(checkForm()){ 
						ajaxAction = "SAVEORUPDATE";
						organForm.act.value="SAVEORUPDATE";
						AJForm.submitForm(organForm);
					}
					break;

				case "CANCEL":
					organForm.act.value = "SHOWORGANDETAIL";
					if (organ_id.lastIndexOf("-") == -1){
		                organForm.organID.value = "<%=Organ.ROOT_ORGAN.getOrganID()%>";
		            }else{
						organForm.organID.value = organ_id.substring(0,organ_id.lastIndexOf("-"));
					}
					organForm.submit();
					break;
			}
		}
		
		function checkForm(){
			if(organForm.organName.value == ""){
				alert("请输入机构名称！");
				organForm.organName.focus();
				return false;
			}
			if (organForm.organCode.value == ""){
				alert("请输入机构代码！");
				organForm.organCode.focus();
				return false;
			}
			return true; 
		}
	</script>
</head>
<body onload="document.all('organName').select();" class="listMargin">
<form name="organForm" method="post" action="manage/organ" onSubmit="ajform:getReturnData">
	<input type=hidden name="act" >
	<input type=hidden name="preop" value="<%=DataFormater.noNullValue(request.getParameter("preop"))%>">
	<input type=hidden name="organID" value="<%=DataFormater.noNullValue(organ.getOrganID())%>">
	<br>
	<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
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
	
	<TABLE width="90%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
			<wing:permission module_id="SYS-ORGAN" operator_id="add">
			<input name="add"  type="button" style="background-image:url(images/manage/contentBtn4.gif);" class="contentBtn4" onClick="btAction('ADDCHILD');" value="新增子机构">
			</wing:permission>
			<%if (!isRoot){ %>
			<wing:permission module_id="SYS-ORGAN" operator_id="delete">
			<input name="deletebutton"  style="background-image:url(images/manage/contentBtn.gif);" type="button" class="contentBtn" onClick="btAction('DELETE');" value="删除">
			</wing:permission>
			<wing:permission module_id="SYS-ORGAN" operator_id="add,modify">
			<input name="save"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('SAVEORUPDATE');" value="保存">
			</wing:permission>
			<%} %>
			<input name="cancel"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('CANCEL');" value="返回">
		</td>
	</tr>
    <tr>
         <td height="260" align="center" valign="top" class="conKa05">
			 <table width="600" align="center" cellpadding="4">
	           <tr  >
	             <td width="100" height="30" align="right">上级机构：</td>
	             <td width="500" ><input name="parent_name" type="text" size="43" <%=isRoot?"disabled":"readonly" %> value="<%=parentOrganName%>">
	             </td>
	           </tr>
	           <tr>
	             <td width="100" height="30" align="right" id="nameShow">机构名称：</td>
	             <td width="500"><input name="organName" type="text" size="43" <%=isRoot?"disabled":"" %> value="<%=DataFormater.noNullValue(organ.getOrganName())%>">
	             </td>
	           </tr>
	           <tr>
	             <td width="100" height="30" align="right">机构代码：</td>
	             <td width="500"><input name="organCode" type="text" size="43" <%=isRoot?"disabled":(isNew?"":"readonly") %> value="<%=DataFormater.noNullValue(organ.getOrganCode())%>"></td>
	           </tr>
	           <tr>
	             <td width="100" height="30"  align="right">机构描述：</td>
	             <td colspan="3"><textarea name="organDesc" cols="42" rows="6" class="texta" <%=isRoot?"readonly":"" %>><%=DataFormater.noNullValue(organ.getOrganDesc())%></textarea>
	               &nbsp;</td>
	           </tr>
	         </table>
         </td>
	</tr>
	</table>
</form>
</body>
</html>