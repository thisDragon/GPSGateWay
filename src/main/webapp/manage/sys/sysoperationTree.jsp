<%@ page language="java" import="java.util.*,com.cari.sys.bean.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.cari.web.util.*" %>
<%@ page import="com.cari.rbac.Module" %>
<%@ page import="com.cari.rbac.Operation" %>
<%@ page import="com.cari.rbac.RBACPermission" %>
<%@ page import="com.cari.web.comm.ListPage" %>
<%@ page import="com.cari.rbac.OperationManage" %>				
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="cari" %>
<%
	int operationIndex = 1;
	int pageNo = HttpParamCaster.getIntParameter(request, "pageno", 1);
	if (pageNo < 1) pageNo = 1;
	int pageCount = HttpParamCaster.getIntParameter(request, "pagecount", 20);
	if (pageCount < 1) pageCount = 10;
	int moduleLevel = HttpParamCaster.getIntParameter(request, "modulelevel", 0);
	if (moduleLevel < 0) moduleLevel = 0;
	String moduleName = HttpParamCaster.getUTF8Parameter(request, "modulename", "");
	String moduleId = HttpParamCaster.getUTF8Parameter(request, "moduleid", "");
	//System.out.println(pageNo + "\t" + pageCount + "\t" + moduleLevel + "\t" + moduleId + "\t" + moduleName);
	ListPage listPage = OperationManage.getInstance().getOperation(pageNo, pageCount, moduleName);
    request.setAttribute("listPage", listPage);
	long nPageCount = listPage.getTotalPageCount();
	SysUser user = (SysUser)request.getSession().getAttribute("LOGINUSER");
	String cityname = DataFormater.noNullValue(HttpParamCaster.getUTF8Parameter(request,"cityname"));
	String companycnname = DataFormater.noNullValue(HttpParamCaster.getUTF8Parameter(request,"companycnname"));
	String eaccount = DataFormater.noNullValue(request.getParameter("eaccount"));
	String confirmedflag = DataFormater.noNullValue(request.getParameter("confirmedflag"));
	String validflag = DataFormater.noNullValue(request.getParameter("validflag"));
	String score = DataFormater.noNullValue(request.getParameter("score"));
	String creator = DataFormater.noNullValue(request.getParameter("creator"));
	String modifier = DataFormater.noNullValue(request.getParameter("modifier"));
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>
	<script type="text/javascript" src="js/poiManage.js"></script>
	<script type="text/javascript" src="js/validate.js"></script>
	<link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<style>
		.topic{text-align:right; background-color:#CEDFF7;height:30px;width:115px;}
		.menu1{background-color:#CEDFF7;height:25px;color:#29618C;font-weight:bold;}
		.public_btn{background-image:url(namecard/images/btn_bg.gif);width:76px;height:20px;border:0px;}
	</style>
	
	<script language="javascript">
		var currentAction = "";
		var pageNo = "<%=pageNo%>";
		var pageCount = "<%=pageCount%>";
		function  gotoPage(pageNo){
			if (pageNo == null || pageNo == "") return;
			if (isNaN(pageNo)) return;
			if (pageNo < 1) pageNo = 1;
			if (pageNo > <%=nPageCount%>) pageNo = <%=nPageCount%>;
			var thisForm = document.forms["ncFrm"];			
			thisForm.pageno.value = pageNo;
			thisForm.submit();
		}
		
		function selAll(){
			var checkStatus = document.getElementById("checkall").checked;
			var checkBoxes = document.getElementsByName("moduleid");
			for(var i = 0 ; i < checkBoxes.length ; i++){
				checkBoxes[i].checked = checkStatus;
			}
		}		
		function delModule(){
			var frm = document.forms["listFrm"];
			frm.act.value = "delOperation";
			currentAction = "delOperation";
			if (!getSelectNum(frm.moduleid, false)) {
				alert("请选择要删除的记录");
				return ;
			}
			if(!confirm("确定要删除所选择的记录吗")) {
				return;
			}			
			AJForm.submitForm(frm);
		}				
		//判断选中对象的个数
		/*param object是要判断的对象
		 *param num为true时判断是否为单一的，false时判断是否是一个以上的
		 *return true
		 */
		function getSelectNum(object,num) {
			var flag = false;
			var count = 0;
			if (object == null) return false;
			if (num == null) num = false;
			if (object.length == null) {
				if (object.checked) flag = true;
			}else{
				for(i = 0;i < object.length; i++) {
					if (object[i].checked) count++;
				}
				if (num) {
					if (count == 1) flag = true;
				} else {
					if (count > 0) flag = true;
				}
			}
			return flag;
		}
		
		function getReturnData(data , statusCode , statusMessage){	
			var m = document.getElementById("moduleid_2");
			m.readOnly = false;		
			if(statusCode != AJForm.STATUS['SUCCESS'] ) {
			 	alert( statusMessage );
				return true;
			}
			//获取返回信息
			var oDomDoc = Sarissa.getDomDocument();			
			oDomDoc = (new DOMParser()).parseFromString(data, "text/xml");
			var status = oDomDoc.selectSingleNode("/root/Return").text;
			if (status == "1") {
				var xmlNode = oDomDoc.selectSingleNode("/root/Content");
				switch(currentAction){
					case "addOperation":
						alert(xmlNode.text);						
						gotoPage(<%=listPage.getCurrentPageNo() %>);
						break;
					case "modOperation" :
						alert(xmlNode.text);
						gotoPage(<%=listPage.getCurrentPageNo() %>);
						break;	
					case "delOperation" :
						alert(xmlNode.text);
						gotoPage(<%=listPage.getCurrentPageNo() %>);
						break;					
					default:
						alert(xmlNode.text)						
						break;
						;
				}
			}else{
				var errorInfo = oDomDoc.selectSingleNode("/root/Error");
				if (errorInfo != null) {
				//PageState.show(errorInfo);
					alert(errorInfo.text);
				}
			}
		}
		var saveOrUpdate = "1";
		//模块添加开始
		function addModule() {
			initAddModuleObj();
			document.getElementById("addModuleDiv").style.display="";
			saveOrUpdate = "1";
			return true;
		}	
		//保存添加的模块	
		function saveModule() {
			var frm = document.forms["moduleFrm"];
			if (saveOrUpdate == 2) {
				frm.act.value = "modOperation";
			} else if (saveOrUpdate == 1) {
				frm.act.value = "addOperation";
			}			
			if (!checkForm(saveOrUpdate)) {
				return false;
			} 
			frm.id.value = document.getElementById("moduleid_" + saveOrUpdate).value;
			frm.name.value = document.getElementById("modulename_" + saveOrUpdate).value;					
			AJForm.submitForm(frm);
			currentAction = "addOperation";
		}
		//模块修改开始
		function modModule(){
			initMoveObj();
			var frm = document.forms["listFrm"];
			if (!getSelectNum(frm.moduleid, true)) {
				alert("请选择一条记录");
				return ;
			}
			var i = 0;
			if(frm.moduleid.length){
				for( ;i<frm.moduleid.length;i++){
					if(frm.moduleid[i].checked){				
						break;
					}
				}
			}				
			document.getElementById("modModuleDiv").style.display="";
			saveOrUpdate = "2";
			var p = (pageNo > 1) ? pageCount * (pageNo - 1) : 0;
			var index = parseInt(parseInt(i + 1) + p);	
			//alert("i = " + i + "\t" + "p = " + p)					
			document.getElementById("modulename_2").value = document.getElementById("modulename" + index).value;									
			var m = document.getElementById("moduleid_2");
			m.value = document.getElementById("moduleid" + index).value;
			m.readOnly = true;
			return true;
		}
		//重新加载模块到缓存中
		function reloadModule() {
			var frm = document.forms["moduleFrm"];
			frm.act.value = "reloadModule";
			frm.action = "manage/module";
			AJForm.submitForm(frm);
		}
		
		var ie_type = new function (){
			this.type = "";
			if(!window.RegExp) return false;
			var m_v = navigator.userAgent.toLowerCase();	
			var m_a = new Array("opera","msie","safari","firefox","netscape","mozilla");
			for(var  i = 0; i < m_a.length;i++){		
				if(m_v.indexOf(m_a[i])>-1){	
					this.type = m_a[i];			
					return this.type;
				}
			}
			return this.type;
		};
		var domainObj;
		function initMoveObj(){			
			domainObj = new moveObj("modModuleDiv","modModuleCon");
		}		
		function initAddModuleObj() {
			domainObj = new moveObj("addModuleDiv","addModuleCon");	
		}					
		
		function checkForm(flag) {
			var moduleName = document.getElementById("modulename_" + flag);
			var moduleId = document.getElementById("moduleid_" + flag);			
			if(moduleName.value==""){
				alert("模块名称不能为空！");
				moduleName.focus();
				return false;
			}
			if (moduleId.value=="") {
				alert("模块key不能为空！");
				moduleId.focus();
				return false;
			}			
			/*
			if(!validateNumber(moduleSort.value)){
				alert("模块序号必须为数字");
				moduleSort.focus();
				return false;
			}
			
			if (flag == 2) {
				if(moduleUrl.value==""){
					alert("模块Url不能为空！");
					moduleUrl.focus();
					return false;
				}
			}	
			*/		
			return true;
		}							
		//域名修改结束				
	</script>
</head>
<body style="overflow:hidden">
<table height="100%" border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr><td valign="top" height="28">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr id="SearchCon" style="display:">
				<td bgcolor="#5C93D6">
				<form name="ncFrm">
				<table>
					
					<INPUT type="hidden" name="act" value="ENTER">
					<input type="hidden" name="pageno" value="<%=listPage.getCurrentPageNo()%>">
					<input type="hidden" name="pagecount" value="<%=pageCount%>">
					<input type="hidden" name="isnewquery" value="Y">
					<tr style="color:#FFFFFF" height="28">
						<td width="8"></td>
						<td>操作名称：<input type="text" size="20" name="modulename" value="<%=DataFormater.noNullValue(moduleName) %>" >&nbsp;&nbsp;</td>						
						<td style="color:#FFFFFF"><input type="submit"  style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" value=" 搜索 "></td>
					</tr>
					
				</table>
				</form>
				</td>
			</tr>
		</table>
</td></tr><tr><td valign="top" height="22px">
	<table align="center" width="100%" cellpadding="0" cellspacing="0" border="0" style="border:solid 1px #B4C6E2">
		<tr>
			<td bgcolor="#E7F0FB" style="padding-top:4px;padding-left:8px;">
				<cari:permission module_id="SYS-DICT" operator_id="add">
				<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="addModule();" value="新增">
				</cari:permission>
				<cari:permission module_id="SYS-DICT" operator_id="modify">
				<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="modModule();" value="修改">
				</cari:permission>
				<cari:permission module_id="SYS-DICT" operator_id="delete">
				<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="delModule();" value="删除">
				</cari:permission>
				<cari:permission module_id="SYS-DICT" operator_id="delete">
				<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="reloadModule();" value="更新缓存">
				</cari:permission>									
			</td>
			<td align="right" bgcolor="#E7F0FB" style="padding-top:4px;padding-right:20px;">
				<A href="javascript:gotoPage(1);">首 页</A>&nbsp;&nbsp;
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
				到<input name="no" style="BORDER-RIGHT: #dddddd 1px solid; BORDER-TOP: #dddddd 1px solid; BORDER-LEFT: #dddddd 1px solid; BORDER-BOTTOM: #dddddd 1px solid; HEIGHT: 16px" type="text" size="2">
				页<A href="javascript:gotoPage(document.getElementById('no').value);">GO</A>
			</td>		
		</tr>
		
	</table>
</td></tr><tr><td valign="top" height="100%">
	<div style="height:100%;width:100%;overflow-x:hidden;overflow-y:auto;">
	<table border="0" cellpadding="0" cellspacing="0" align="center" height="100%">
	<tr><td valign="top">
		<table border="0" cellpadding="0" cellspacing="0" align="center">
			<tr><td align="left">
				<form name="listFrm" method="post" action="manage/oper" onSubmit="ajform:getReturnData">
				<INPUT type="hidden" name="act" value="">
				<table border="1" width="840" cellpadding="2" cellspacing="2" bordercolordark="#F2F5FB" bordercolorlight="#B4C6E2" style="border-bottom:0px;border-right:solid 1px #B4C6E2">
					<tr style="color:#3D6198; background-color:#CDDFF7;">
						<td width="5%" align="center">序号</td>
						<td width="5%" align="center"><input name="selectAll" type="checkbox" style="cursor:pointer;" title="全选/取消全选" id="checkall" onClick="selAll();"></td>
						<td width="35%" align="center">操作名称</td>
						<td width="35%" align="center">操作主键</td>
						<td width="20%" align="center">操作值</td>															
					</tr>
		<%
			if(listPage != null && !listPage.isEmpty()) {
				List userList = listPage.getDataList();
				int index = (listPage.getCurrentPageNo() - 1) * listPage.getCurrentPageSize() + 1;
				for(Iterator it = userList.iterator(); it.hasNext(); ){
					Operation oper = (Operation)it.next();
		%>			
					<tr>
						<td align="center"><%=index%></td>
						<td align="center"><input name="moduleid"  type="checkbox" value="<%=oper.getOperate_id()%>"></td>
						<td align="center" id="modulename<%=index%>" value="<%=oper.getOperate_name()%>"><%=oper.getOperate_name()%></td>
						<td align="center" id="moduleid<%=index%>" value="<%=oper.getOperate_id()%>"><%=oper.getOperate_id()%></td>
						<td align="center" id="modulevalue<%=index%>" value="<%=oper.getOperate_value()%>"><%=oper.getOperate_value()%></td>						
					</tr>
		<%		
					index++;
				}
			}
		%>
		
				</table>
				</form>
			</td></tr>
			</table>
		</td></tr></table>
	</div>
</td></tr></table>

<!-- 修改域名div层开始 -->
<div id="modModuleDiv" style="width:400;height:160;position:absolute;left:250;top:90;text-align:center;display:none; background-color:#FFFFFF">
<table width="100%" align="center" cellpadding="0" cellspacing="0">
	 <tr id="modModuleCon" onMouseDown="startDrag(domainObj);" onMouseMove="draging(domainObj);" onMouseUp="stopDrag(domainObj);"> 
		<td width="9" background="images/manage/top_bg.jpg"><img src="images/manage/top01.jpg" width="9" height="30"></td>
		<td background="images/manage/top_bg.jpg">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr> 
					<td width="96%">修改模块：</td>
					<td width="4%"><img src="images/manage/close.jpg" width="17" height="30" style="cursor:pointer;" onClick="domainObj.close();"></td>
				</tr>
			</table>
		</td>
		<td width="8" background="images/manage/top_bg.jpg"><img src="images/manage/top03.jpg" width="8" height="30"></td>
	</tr>
	<tr>
		<td background="images/manage/b_bg.jpg"><img src="images/manage/b01.jpg" width="9" height="11"></td>
		<td background="images/manage/b_bg.jpg"></td>
		<td background="images/manage/b_bg.jpg"><img src="images/manage/b02.jpg" width="8" height="11"></td>
	</tr>
    <tr>
		<td background="images/manage/b03.jpg">&nbsp;</td>
		<td id="infoContent" style="padding:5px;" bgcolor="#FFFFFF">
		<table border="0" cellspacing="5">
		  <tr>
     	 	 <td width="100">模块名称：</td>
     	 	 <td width="242"><input name="modulename_2" id="modulename_2" type="text" size="20" value="" /></td>
		  </tr>
		  <tr>
     	 	 <td width="100">模块key：</td>
      		  <td><input name="moduleid_2" id="moduleid_2" type="text" size="20" /></td>
    	  </tr>    	  
		  <tr><td colspan="2" align="center"><input type="button" name="submit" value="保存" class="public_btn" onClick="saveModule();"></td></tr>
		</table>
		</td>
		<td background="images/manage/b04.jpg">&nbsp;</td>
	</tr>
	<tr> 
		<td><img src="images/manage/b05.jpg" width="9" height="10"></td>
		<td background="images/manage/b06.jpg"></td>
		<td><img src="images/manage/b07.jpg" width="8" height="10"></td>
	</tr>
</table>
</div>
<!-- 修改域名div层结束 -->
<!-- 添加模块div层开始 -->
<div id="addModuleDiv" style="width:400;height:160;position:absolute;left:250;top:90;text-align:center;display:none; background-color:#FFFFFF">
<table width="100%" align="center" cellpadding="0" cellspacing="0">
	 <tr id="addModuleCon" onMouseDown="startDrag(domainObj);" onMouseMove="draging(domainObj);" onMouseUp="stopDrag(domainObj);"> 
		<td width="9" background="images/manage/top_bg.jpg"><img src="images/manage/top01.jpg" width="9" height="30"></td>
		<td background="images/manage/top_bg.jpg">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr> 
					<td width="96%">添加模块操作：</td>
					<td width="4%"><img src="images/manage/close.jpg" width="17" height="30" style="cursor:pointer;" onClick="domainObj.close();"></td>
				</tr>
			</table>
		</td>
		<td width="8" background="images/manage/top_bg.jpg"><img src="images/manage/top03.jpg" width="8" height="30"></td>
	</tr>
	<tr>
		<td background="images/manage/b_bg.jpg"><img src="images/manage/b01.jpg" width="9" height="11"></td>
		<td background="images/manage/b_bg.jpg"></td>
		<td background="images/manage/b_bg.jpg"><img src="images/manage/b02.jpg" width="8" height="11"></td>
	</tr>
    <tr>
		<td background="images/manage/b03.jpg">&nbsp;</td>
		<td id="infoContent" style="padding:5px;" bgcolor="#FFFFFF">
		<table border="0" cellspacing="5">
		  <tr>
     	 	 <td width="100">操作名称：</td>
     	 	 <td width="242"><input name="modulename_1" id="modulename_1" type="text" size="20" value="" /></td>
		  </tr>
		  <tr>
     	 	 <td width="100">操作主键：</td>
      		  <td><input name="moduleid_1" id="moduleid_1" type="text" size="20" /></td>
    	  </tr>    	      	 
		  <tr><td colspan="2" align="center"><input type="button" name="submit" value="保存" class="public_btn" onClick="saveModule();"></td></tr>
		</table>
		</td>
		<td background="images/manage/b04.jpg">&nbsp;</td>
	</tr>
	<tr> 
		<td><img src="images/manage/b05.jpg" width="9" height="10"></td>
		<td background="images/manage/b06.jpg"></td>
		<td><img src="images/manage/b07.jpg" width="8" height="10"></td>
	</tr>
</table>
</div>
<!-- 添加模块div层结束 -->
<!-- 添加模块功能div层开始 -->
<div id="addModuleOperationDiv" style="width:500;height:200;position:absolute;left:250;top:90;text-align:center;display:none; background-color:#FFFFFF">
<table width="100%" align="center" cellpadding="0" cellspacing="0" >
	 <tr id="addModuleOperationCon" onMouseDown="startDrag(domainObj);" onMouseMove="draging(domainObj);" onMouseUp="stopDrag(domainObj);"> 
		<td width="9" background="images/manage/top_bg.jpg"><img src="images/manage/top01.jpg" width="9" height="30"></td>
		<td background="images/manage/top_bg.jpg">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr> 
					<td width="96%">添加模块功能：</td>
					<td width="4%"><img src="images/manage/close.jpg" width="17" height="30" style="cursor:pointer;" onClick="domainObj.close();"></td>
				</tr>
			</table>
		</td>
		<td width="8" background="images/manage/top_bg.jpg"><img src="images/manage/top03.jpg" width="8" height="30"></td>
	</tr>
	<tr>
		<td background="images/manage/b_bg.jpg"><img src="images/manage/b01.jpg" width="9" height="11"></td>
		<td background="images/manage/b_bg.jpg"></td>
		<td background="images/manage/b_bg.jpg"><img src="images/manage/b02.jpg" width="8" height="11"></td>
	</tr>
    <tr>
		<td background="images/manage/b03.jpg">&nbsp;</td>
		<td id="infoContent" valign="top" style="padding:5px;" bgcolor="#FFFFFF" height="200">
		<table border="0" cellspacing="5">
			<tr>
				<td width="50%" valign="top">
					<table border="0" cellspacing="5">
					<tr>
			     	 	 <td width="100">模块名称：</td>
			     	 	 <td width="242"><span id="modulename_3"></td>
					</tr>
					<tr>
			     	 	 <td width="100">模块key：</td>
			      		 <td><span id="moduleid_3"></span></td>
			    	</tr>    	  
					<tr><td colspan="2" align="center"><input type="button" name="submit" value="保存" class="public_btn" onClick="saveModuleOperation();"></td></tr>
					</table>
				</td>
				<td><div style="height:300;width:100%;overflow-x:hidden;overflow-y:auto;">
					<table border="0" cellpadding="0" cellspacing="0" align="center" height="100%">
						<tr><td valign="top">
							<table border="0" cellpadding="0" cellspacing="0" align="center">
								<tr><td align="left">
									<form name="operationFrm" method="post" action="manage/oper" onSubmit="ajform:getReturnData">
									<input type="hidden" name="module_id" />		
									<input type="hidden" name="act" value="addModuleOperation"/>																	
									<table border="1" width="200" cellpadding="2" cellspacing="2" bordercolordark="#F2F5FB" bordercolorlight="#B4C6E2" style="border-bottom:0px;border-right:solid 1px #B4C6E2">
										<tr style="color:#3D6198; background-color:#CDDFF7;">
											<td width="30" align="center">序号</td>
											<td width="25"><input name="selectAllOperation" type="checkbox" style="cursor:pointer;" title="全选/取消全选" id="checkalloperation" onClick="selAllOperation();"></td>
											<td width="30" align="center">名称</td>																				
										</tr>
							<%
								
								List operationList = (List)session.getServletContext().getAttribute("OPERATION_LIST");
								if(operationList != null) {																	
									for(Iterator it = operationList.iterator(); it.hasNext(); ){
										Operation module = (Operation)it.next();
							%>			
										<tr>	
											<td width="26" align="center"><%=operationIndex%></td>
											<td><input name="operationid" id="operationid<%=operationIndex++ %>" type="checkbox" value="<%=module.getOperate_id()%>"></td>										
											<td align="center" ><%=module.getOperate_name()%></td>																					
										</tr>
							<%																				
									}
								}
							%>							
									</table>
									</form>
								</td></tr>
								</table>
							</td></tr></table></div>
				</td>
			</tr>
		  
		</table>
		</td>
		<td background="images/manage/b04.jpg">&nbsp;</td>
	</tr>
	<tr> 
		<td><img src="images/manage/b05.jpg" width="9" height="10"></td>
		<td background="images/manage/b06.jpg"></td>
		<td><img src="images/manage/b07.jpg" width="8" height="10"></td>
	</tr>
</table>
</div>
<!-- 添加模块功能div层结束 -->
<div style="display:none;">
<form name="moduleFrm" method="post" action="manage/oper" onSubmit="ajform:getReturnData">
<input type="hidden" name="id" />
<input type="hidden" name="name" />
<input type="hidden" name="value" />
<input type="hidden" name="act" />
</form>
</div>
<script type="text/javascript">
	//------------------------------- 窗口拖动函数组开始 -------------------------------
			var nEvent_x=0,nEvent_y=0;	//初始化鼠标x,y
			var nDrag_x=0,nDrag_y=0;	//初始化拖动窗口x,y
			var nInfo_x=0,nInfo_y=0;	//初始化地图左边窗口x,y
			var nInfoBtn_x=0,nInfoBtn_y=0; //初始化地图左边窗口按钮x,y
			var bMoveable=false;		//当前是否有窗口处于被拖动状态
			
			/**
			*	DragDiv v1.0 窗口拖动公用函数	Author:laoding @ Richmap	CreatDate: 2006-04-14
			*	开始拖动
			*	@param classObj	窗口类，该类必须符合以下条件
						必须有成员.m_oDragObj,该成员指定了窗口拖动的起始鼠标触发事件的元素
						必须有成员函数getPositionX(x)来返回窗口的坐标x
						必须有成员函数getPositionY(y)来返回窗口的坐标y
						必须有成员函数setPosition(x,y)来设置窗口的坐标x,y
			*/
			function startDrag(classObj){
				if(event.button==1)	{
					classObj.m_oDragObj.setCapture();
					nEvent_x = event.clientX;
					nEvent_y = event.clientY;
					nDrag_x = classObj.getPositionX();
					nDrag_y = classObj.getPositionY();
					bMoveable = true;
				}
			};
			//拖动
			function draging(classObj){
				if(bMoveable){
					classObj.setPosition(nDrag_x + event.clientX - nEvent_x,nDrag_y + event.clientY - nEvent_y);
				}
			};
			//结束拖动
			function stopDrag(classObj){
				if(bMoveable){
					classObj.m_oDragObj.releaseCapture();
					bMoveable = false;
				}
				//当窗口被拖动到顶部以上
				if(classObj.getPositionY()<0){classObj.setPosition(classObj.getPositionX(),0);}
				//当窗口被拖动到底部以下
				var nBodyTop=document.body.scrollTop;
				var nBodyHeight=document.body.offsetHeight;
				if(classObj.getPositionY() > nBodyTop + nBodyHeight-50){
					classObj.setPosition(classObj.getPositionX(),nBodyTop + nBodyHeight-50);
				}
			};
		//------------------------------- 窗口拖动函数组结束 -------------------------------
		function getXmlHttp() {
			var xmlHttp=false;
			try {
				xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (E) {
					xmlHttp = false;
				  }
			}
			if (!xmlHttp && typeof XMLHttpRequest!='undefined') {
				xmlHttp = new XMLHttpRequest();
			}
			return xmlHttp;
		}
		
		//在两个输入框组件中不能同时在 onBlur()的方法中事件中使用focus()方法，当两个组件都得焦后会死机
		function checkOld(){
			var frm = document.forms["modfrm"];
			var domain = frm.olddomain.value;
			if(domain==""){
				document.getElementById("msg1").style.color="red";
				document.getElementById("msg1").innerHTML="旧域名不能为空";
				//frm.olddomain.focus();
				return false;
			}
			
			if(!validateNumber(frm.olddomain)){
				document.getElementById("msg1").style.color="red";
				document.getElementById("msg1").innerHTML="旧域名必需为数字";
				//frm.olddomain.focus();
				return false;
			}
			var xmlStr = "";
			var url = "mng/ncctrl?act=CHECKEXIST&domain="+domain;
			var xmlHttp = getXmlHttp();
			xmlHttp.open("GET", url, true); 
			xmlHttp.onreadystatechange = function() {
			　　if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
					xmlStr = xmlHttp.responseText; 
					var oDomDoc = Sarissa.getDomDocument();
					oDomDoc = (new DOMParser()).parseFromString(xmlStr, "text/xml");
					var status = oDomDoc.selectSingleNode("/root/Return").text;
					if (status == "1") {
							var info = oDomDoc.selectSingleNode("/root/Content").text;
							document.getElementById("msg1").style.color="blue";
							document.getElementById("msg1").innerHTML=info;		
					}else{
						var errorInfo = oDomDoc.selectSingleNode("/root/Error").text;	
						document.getElementById("msg1").style.color="red";			
						document.getElementById("msg1").innerHTML=errorInfo;
						//frm.olddomain.focus();
						return false;
					}
				}
			}
			xmlHttp.send(null); 
		}		
		function getAjaxReturn(url,refun,_ieType){	
			var getDataFlag=getRandFlag();	
			if(url.trim() ==''){return false;}		
			ajaxArr[getDataFlag] = Sarissa.getDomDocument();	
			ajaxArr[getDataFlag].async = true;
			ajaxArr[getDataFlag].onreadystatechange=function(){
				if(ajaxArr[getDataFlag].readyState == 4){				
					if (Sarissa.serialize(ajaxArr[getDataFlag]) != ""){					
						if(_ieType!="" && _ieType =="msie"){		
							ajax_Con[getDataFlag] = ajaxArr[getDataFlag].selectNodes("/root");					
						}else if(_ieType!="" && _ieType =="firefox"){					
							ajax_Con[getDataFlag] = ajaxArr[getDataFlag].getElementsByTagName("root");									
						}
						if(typeof(refun) =="string" || refun !=null){						
							try{ eval(refun + "ajax_Con[getDataFlag])")} catch(e) {}
						}
					}else{
						alert('数据读取无效，请刷新重试！');
						return false;
					}
				}
			}
			ajaxArr[getDataFlag].load(url);
		};
		function setModuleOperation(reObj) {
			if(ie_type.type=="")return ;	
			var operationIndex = "<%=operationIndex%>"	
			/*
			try {
				alert("operationIndex = " + operationIndex)
				for (var i = 1; i < operationIndex; i++) {		
					var oper = document.getElementById("operationid" + i);
					if (oper) {
						alert("222" + oper.value)
					} 											
				}
				alert("111" + document.getElementById("operationid2").value)
				
			} catch (e) {
				alert(e)
			}
			*/
			if(ie_type.type =="msie"){		
				reFlag = reObj[0].selectNodes("Return");				
				if(reFlag[0].text =="1"){
					reValue = reObj[0].selectNodes("Content");	
					var moduleId =null,operationId = null,operationSize;
					if(reValue !=null){
		  				moduleId = reValue[0].selectNodes("Operation");
		  				operationSize = reValue[0].selectNodes("Operation").length;	 						  				  			
		  				for(var i = 0 ; i < operationSize ; i++){
		  					selectModule = moduleId[i].selectNodes("ModuleId")[0].text;
		  					moduleOperation = moduleId[i].selectNodes("OperationId")[0].text;			  							  									
							for (var j = 1; j < operationIndex; j++) {		
								var oper = document.getElementById("operationid" + j);
								if (oper) {
									//alert(oper.value + "\t" + moduleOperation)
									if (oper.value == moduleOperation) {
										oper.checked = true;
									}
								} 											
							}									  					 										  							  					  				
		  				}		  					  				
		  			}				
				}else if(reFlag[0].text =="0"){		
					selAllOperation();			
					return;				
				}
			}
		}
</script>
</body>
</html>