<%@page import="jodd.util.StringUtil"%>
<%@ page language="java" import="java.util.*,com.cari.sys.bean.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.cari.web.util.*" %>
<%@ page import="com.analog.data.util.DateUtils" %>
<%@ page import="com.cari.rbac.DataSourceForward" %>
<%@ page import="com.cari.rbac.DataSourceConfig" %>
<%@ page import="com.cari.rbac.RBACPermission" %>
<%@ page import="com.cari.web.comm.ListPage" %>
<%@ page import="com.cari.rbac.DataSourceForwardManage" %>
<%@ page import="com.cari.rbac.DataSourceConfigManage" %>		
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="cari" %>
<%
	int operationIndex = 1;
	int pageNo = HttpParamCaster.getIntParameter(request, "pageno", 1);
	if (pageNo < 1) pageNo = 1;
	int pageCount = HttpParamCaster.getIntParameter(request, "pagecount", 20);
	if (pageCount < 1) pageCount = 10;
	String sourceName = HttpParamCaster.getUTF8Parameter(request, "sourceName", "");
	String subscriptionName = HttpParamCaster.getUTF8Parameter(request, "subscriptionName", "");
	ListPage listPage = DataSourceForwardManage.getInstance().getList(pageNo, pageCount, sourceName, subscriptionName);
	List<DataSourceConfig> dataSourceConfigs = DataSourceConfigManage.getInstance().getAllList();
  	request.setAttribute("listPage", listPage);
	long nPageCount = listPage.getTotalPageCount();
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>
	<script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
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
		function toDelete(){
			var frm = document.forms["listFrm"];
			frm.act.value = "toDelete";
			currentAction = "toDelete";
			if (!getSelectNum(frm.configId, false)) {
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
					case "toAdd":
						alert(xmlNode.text);
						gotoPage(<%=listPage.getCurrentPageNo() %>);
						break;
					case "toUpdate" :
						alert(xmlNode.text);
						gotoPage(<%=listPage.getCurrentPageNo() %>);
						break;	
					case "toDelete" :
						alert(xmlNode.text);
						gotoPage(<%=listPage.getCurrentPageNo() %>);
						break;
					case "setUserFalg" :
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
					alert(errorInfo.text);
				}
			}
		}
		var saveOrUpdate = "1";
		//模块添加开始
		function toAdd() {
			initAddModuleObj();
			document.getElementById("addModuleDiv").style.display="";
			document.getElementById("modModuleDiv").style.display="none";
			saveOrUpdate = "1";
			return true;
		}	
		//保存添加的模块	
		function saveModule() {
			var frm = document.forms["moduleFrm"];
			if (saveOrUpdate == 2) {
				frm.act.value = "toUpdate";
				frm.forwardId.value = document.getElementById("forwardId_" + saveOrUpdate).value;
			} else if (saveOrUpdate == 1) {
				frm.act.value = "toAdd";
			}			
			if (!checkForm(saveOrUpdate)) {
				return false;
			}
			var sourceTypes = "";
			var selectCheckBoxes = [];
			var checkBoxes = document.getElementsByName("sourceTypes_" + saveOrUpdate);
			for(var i = 0 ; i < checkBoxes.length ; i++){
				if(checkBoxes[i].checked == true){
					selectCheckBoxes.push(checkBoxes[i].value);
				}
			}
			for (var i = 0; i < selectCheckBoxes.length; i++) {
				if (i == selectCheckBoxes.length -1) {
					sourceTypes += "$" +selectCheckBoxes[i] +"$"
				}else{
					sourceTypes += "$" +selectCheckBoxes[i] +"$" +"," 
				}
			}
			frm.subscriptionName.value = document.getElementById("subscriptionName_" + saveOrUpdate).value;
			frm.forwardUrl.value = document.getElementById("forwardUrl_" + saveOrUpdate).value;
			frm.sourceTypes.value = sourceTypes;
			frm.remark.value = document.getElementById("remark_" + saveOrUpdate).value;
			frm.account.value = document.getElementById("account_" + saveOrUpdate).value;
			frm.password.value = document.getElementById("password_" + saveOrUpdate).value;
			frm.isEnable.value = $("input[name='isEnable_"+ saveOrUpdate +"']:checked").val();
			AJForm.submitForm(frm);
			currentAction = "toAdd";
		}
		//模块修改开始
		function toUpdate(){
			initMoveObj();
			var frm = document.forms["listFrm"];
			if (!getSelectNum(frm.configId, true)) {
				alert("请选择一条记录");
				return ;
			}
			var i = 0;
			if(frm.configId.length){
				for( ;i<frm.configId.length;i++){
					if(frm.configId[i].checked){				
						break;
					}
				}
			}				
			document.getElementById("modModuleDiv").style.display="";
			saveOrUpdate = "2";
			var p = (pageNo > 1) ? pageCount * (pageNo - 1) : 0;
			var index = parseInt(parseInt(i + 1) + p);
			document.getElementById("configId_2").value = document.getElementById("configId" + index).value;
			document.getElementById("sourceName_2").value = document.getElementById("sourceName" + index).value;						
			document.getElementById("sourceType_2").value = document.getElementById("sourceType" + index).value;
			document.getElementById("token_2").value = document.getElementById("token" + index).value;
			document.getElementById("timeSpan_2").value = document.getElementById("timeSpan" + index).value;
			document.getElementById("remark_2").value = document.getElementById("remark" + index).value;
			document.getElementById("account_2").value = document.getElementById("account" + index).value;
			document.getElementById("password_2").value = document.getElementById("password" + index).value;
			document.getElementById("isEnable_2").value = $("input[name='isEnable_"+ saveOrUpdate +"']:checked").val();

			return true;
		}
		//禁用/启用
		function setUserFlag(){
			initMoveObj();
			var frm = document.forms["listFrm"];
			if (!getSelectNum(frm.configId, true)) {
				alert("请选择一条记录");
				return ;
			}
			var i = 0;
			if(frm.configId.length){
				for( ;i<frm.configId.length;i++){
					if(frm.configId[i].checked){				
						break;
					}
				}
			}
			var frm = document.forms["moduleFrm"];
			frm.act.value = "setUserFalg";
			var p = (pageNo > 1) ? pageCount * (pageNo - 1) : 0;
			var index = parseInt(parseInt(i + 1) + p);	
			frm.configId.value = document.getElementById("configId" + index).value;

			AJForm.submitForm(frm);
			currentAction = "setUserFalg";
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
		function initAddModuleOperationObj() {
			domainObj = new moveObj("addModuleOperationDiv","addModuleOperationCon");
		}			
		
		function checkForm(flag) {
			var subscriptionName = document.getElementById("subscriptionName_" + flag);
			var forwardUrl = document.getElementById("forwardUrl_" + flag);
			var remark = document.getElementById("remark_" + flag);
			var account = document.getElementById("account_" + flag);
			var password = document.getElementById("password_" + flag);
			//var isEnable = document.getElementById("isEnable_" + flag);
			var isEnable = $("input[name='isEnable_"+ flag +"']:checked").val();
			if(subscriptionName.value==""){
				alert("订阅名称不能为空！");
				return false;
			}
			if (forwardUrl.value=="") {
				alert("转发地址不能为空！");
				return false;
			}
			if(/.*[\u4e00-\u9fa5]+.*$/.test(forwardUrl.value)) { 
				alert("转发地址不能含有汉字！"); 
				return false; 
			}
			if (isEnable == "1") {
				if (account.value=="" ) {
					alert("账号不能为空！");
					return false;
				}
				if (password.value=="" ) {
					alert("密码不能为空！");
					return false;
				}
			}
			return true;
		}	
	</script>
</head>
<body style="overflow:hidden">
<script type="text/javascript" src="js/public.js"></script>
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
						
						<td>数据源名称：<input type="text" size="20" name="sourceName" value="<%=DataFormater.noNullValue(sourceName) %>" >&nbsp;&nbsp;</td>
						<td>订阅名称：<input type="text" size="20" name="subscriptionName" value="<%=DataFormater.noNullValue(subscriptionName) %>" >&nbsp;&nbsp;</td>
						
						<td style="color:#FFFFFF"><input type="submit"  style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" value=" 搜索 "></td>
						
						<td style="color:#FFFFFF"><input name=""  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="toAdd();" value="新增"></td>
					</tr>
					
				</table>
				</form>
				</td>
			</tr>
		</table>
</td></tr>

<!-- <tr>
	<td valign="top" height="22px">
		<table align="center" width="100%" cellpadding="0" cellspacing="0" border="0" style="border:solid 1px #B4C6E2">
			<tr>
				<td bgcolor="#E7F0FB" style="padding-top:4px;padding-left:8px;">
					<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="toAdd();" value="新增">
				</td>
			</tr>
		</table>
	</td>
</tr> -->

<tr><td valign="top" height="100%">
	<div style="height:100%;width:100%;overflow-x:hidden;overflow-y:auto;">
	<table border="0" cellpadding="0" cellspacing="0" align="center" height="100%">
	<tr><td valign="top">
		<table border="0" cellpadding="0" cellspacing="0" align="center">
			<tr><td align="left">
				<form name="listFrm" method="post" action="manage/forward" onSubmit="ajform:getReturnData">
				<INPUT type="hidden" name="act" value="DELCARDS">
				<table border="1" width="1300" cellpadding="2" cellspacing="2" bordercolordark="#F2F5FB" bordercolorlight="#B4C6E2" style="border-bottom:0px;border-right:solid 1px #B4C6E2">
					<tr style="color:#3D6198; background-color:#CDDFF7;">
						<td width="80" align="center">序号</td>
						<td style="display:none" width="25"><input name="selectAll" type="checkbox" style="cursor:pointer;" title="全选/取消全选" id="checkall" onClick="selAll();"></td>
						<td width="280" align="center">订阅名称</td>
						<td width="420" align="center">转发地址</td>
						<td width="700" align="center">备注</td>
						<td width="220" align="center">创建人</td>
						<td width="330" align="center">创建时间</td>
						<td width="360" align="center">操作</td>											
					</tr>
		<%
			if(listPage != null && !listPage.isEmpty()) {
				List userList = listPage.getDataList();
				int index = (listPage.getCurrentPageNo() - 1) * listPage.getCurrentPageSize() + 1;
				for(Iterator it = userList.iterator(); it.hasNext(); ){
					DataSourceForward dataSourceForward = (DataSourceForward)it.next();
					
		%>			
					<tr>
						<td width="26" align="center"><%=index%></td>
						<td style="display:none"><input name="forwardId" id="forwardId<%=index%>" type="checkbox" value="<%=dataSourceForward.getId()%>"></td>
						<td align="center" id="subscriptionName<%=index%>" value="<%=dataSourceForward.getSubscriptionName()%>"><%=dataSourceForward.getSubscriptionName()%></td>
						<td align="center" id="forwardUrl<%=index%>" value="<%=dataSourceForward.getForwardUrl()%>"><%=dataSourceForward.getForwardUrl()%></td>
						<td style="display:none" align="center" id="sourceTypes<%=index%>" value="<%=dataSourceForward.getSourceType()%>"><%=dataSourceForward.getSourceType()%></td>
						<td style="display:none" align="center" id="account<%=index%>" value="<%=dataSourceForward.getAccount()%>"><%=dataSourceForward.getAccount()%></td>
						<td style="display:none" align="center" id="password<%=index%>" value="<%=dataSourceForward.getPassword()%>"><%=dataSourceForward.getPassword()%></td>
						<td style="display:none" align="center" id="isEnable<%=index%>" value="<%=dataSourceForward.getIsEnable()%>"><%=dataSourceForward.getIsEnable()%></td>
						<td align="center" id="remark<%=index%>" value="<%=dataSourceForward.getRemark()%>"><%=StringUtil.isEmpty(dataSourceForward.getRemark())?"无":dataSourceForward.getRemark()%></td>
						<td align="center" id="createUser<%=index%>" value="<%=dataSourceForward.getCreateUser()%>"><%=dataSourceForward.getCreateUser()%></td>
						<td align="center" id="createTime<%=index%>" value="<%=DateUtils.date2Str(new Date(dataSourceForward.getCreateTime().getTime()), DateUtils.NORMAL_FORMAT)%>"><%=DateUtils.date2Str(new Date(dataSourceForward.getCreateTime().getTime()), DateUtils.NORMAL_FORMAT)%>&nbsp;</td>
						
						<td align="center">
							<input name=""  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="updateOne('<%=index%>')" value="修改">
							<input name=""  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="deleteOne('<%=index%>')" value="删除">
					</td>
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
</td></tr>

<tr><td valign="top" height="22px">
	<table align="center" width="100%" cellpadding="0" cellspacing="0" border="0" style="border:solid 1px #B4C6E2">
		<tr>
			<td bgcolor="#E7F0FB" style="padding-top:4px;padding-left:8px;">
				<cari:permission module_id="SYS-DICT" operator_id="add">
				<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);display: none" class="contentBtn" onClick="toAdd();" value="新增">
				</cari:permission>
				<cari:permission module_id="SYS-DICT" operator_id="modify">
				<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);display: none" class="contentBtn" onClick="toUpdate();" value="修改">
				</cari:permission>
				<cari:permission module_id="SYS-DICT" operator_id="modify">
				<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);display: none" class="contentBtn" onClick="setUserFlag();" value="启用/禁用">
				</cari:permission>
				<cari:permission module_id="SYS-DICT" operator_id="delete">
				<input name="add"  type="button" style="background-image:url(images/manage/contentBtn.gif);display: none" class="contentBtn" onClick="toDelete();" value="删除">
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
</td></tr>

</table>

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
         <td width="100">订阅名称：</td>
         <td width="242">
         		<input name="forwardId_2" id="forwardId_2" type="text" size="20" value="" style="display: none"/>
            <input maxlength="25" name="subscriptionName_2" id="subscriptionName_2" type="text" size="20" value="" /><span style="color:red;">&nbsp;&nbsp;*</span>
         </td>
      </tr>
      <tr>
       <td width="100">转发地址：</td>
       <td>
          <input maxlength="100" name="forwardUrl_2" id="forwardUrl_2" type="text" size="20" /><span style="color:red;">&nbsp;&nbsp;*</span>
       </td>
      </tr>

      <tr>
   	 	 <td width="120">是否开启账号认证：</td>
		   <td>
		   		<input name="isEnable_2" type="radio" value="1" />是
		   		<input name="isEnable_2" type="radio" value="0" />否
 		   </td>
  	  </tr>

  	  <tr>
   	 	 <td width="150">账号:&nbsp;&nbsp;<input style="width:100px" maxlength="30" name="account_2" id="account_2" type="text" size="20" /></td>
		   <td width="150">密码:&nbsp;&nbsp;<input style="width:100px" maxlength="30" name="password_2" id="password_2" type="password" size="20" /></td>
  	  </tr>

        <tr>
         <td width="100">数据源：</td>
        </tr>
        <tr >
            <!-- <td width="300" style="border: 1px solid black" colspan="2"> -->
            <td width="300" colspan="2">
    <%
      if(dataSourceConfigs != null && dataSourceConfigs.size() > 0) {
        for(Iterator it = dataSourceConfigs.iterator(); it.hasNext(); ){
          DataSourceConfig dataSourceConfig = (DataSourceConfig)it.next();
          String name = dataSourceConfig.getSourceName();
    %>
              <input name="sourceTypes_2" type="checkbox" style="cursor:pointer;" value="<%=dataSourceConfig.getSourceType()%>" >
              <a style="width: 120px" title="<%=name%>"><%=name.length()>8?name.substring(0, 8)+"...":name%></a>
    <%    
        }
      }
    %>
            </td>
        </tr>
        <tr>
         <td width="100">备注：</td>
            <td> <textarea style="border:1px solid #a3d7ef;width:150px; height:50px" name="remark_2" id="remark_2"></textarea></td>
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
					<td width="96%">新增模块：</td>
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
     	 	 <td width="100">订阅名称：</td>
     	 	 <td width="242">
     	 	 		<input maxlength="25" name="subscriptionName_1" id="subscriptionName_1" type="text" size="20" value="" /><span style="color:red;">&nbsp;&nbsp;*</span>
      	 </td>
		  </tr>
		  <tr>
   	 	 <td width="100">转发地址：</td>
		   <td>
		   		<input maxlength="100" name="forwardUrl_1" id="forwardUrl_1" type="text" size="20" /><span style="color:red;">&nbsp;&nbsp;*</span>
 		   </td>
  	  </tr>

  	  <tr>
   	 	 <td width="120">是否开启账号认证：</td>
		   <td>
		   		<input name="isEnable_1" type="radio" value="1" />是
		   		<input name="isEnable_1" type="radio" value="0" checked="checked" />否
 		   </td>
  	  </tr>

  	  <tr>
   	 	 <td width="150">账号:&nbsp;&nbsp;<input style="width:100px" maxlength="30" name="account_1" id="account_1" type="text" size="20" /></td>
		   <td width="150">密码:&nbsp;&nbsp;<input style="width:100px" maxlength="30" name="password_1" id="password_1" type="password" size="20" /></td>
  	  </tr>

    	  <tr>
     	 	 <td width="100">数据源：</td>
    	  </tr>
    	  <tr >
      		  <!-- <td width="300" style="border: 1px solid black" colspan="2"> -->
      		  <td width="300" colspan="2">
 		<%
			if(dataSourceConfigs != null && dataSourceConfigs.size() > 0) {
				for(Iterator it = dataSourceConfigs.iterator(); it.hasNext(); ){
					DataSourceConfig dataSourceConfig = (DataSourceConfig)it.next();
					String name = dataSourceConfig.getSourceName();
		%>
      		  	<input name="sourceTypes_1" type="checkbox" style="cursor:pointer;" value="<%=dataSourceConfig.getSourceType()%>" >
      		  	<a style="width: 120px" title="<%=name%>"><%=name.length()>8?name.substring(0, 8)+"...":name%></a>
		<%		
				}
			}
		%>
       		  </td>
    	  </tr>
    	  <tr>
     	 	 <td width="100">备注：</td>
      		  <td> <textarea style="border:1px solid #a3d7ef;width:150px; height:50px" name="remark_1" id="remark_1"></textarea></td>
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
<!-- 添加模块功能div层结束 -->
<div style="display:none;">
<form name="moduleFrm" method="post" action="manage/forward" onSubmit="ajform:getReturnData">
<input type="hidden" name="configId" />
<input type="hidden" name="sourceName" />
<input type="hidden" name="sourceType" />
<input type="hidden" name="token" />
<input type="hidden" name="timeSpan" />

<input type="hidden" name="forwardId" />
<input type="hidden" name="subscriptionName" />
<input type="hidden" name="forwardUrl" />
<input type="hidden" name="sourceTypes" />
<input type="hidden" name="remark" />
<input type="hidden" name="account" />
<input type="hidden" name="password" />
<input type="hidden" name="isEnable" />
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

		function updateOne(index){
			initMoveObj();
			document.getElementById("addModuleDiv").style.display="none";
			var frm = document.forms["listFrm"];
			document.getElementById("modModuleDiv").style.display="";
			saveOrUpdate = "2";

			document.getElementById("subscriptionName_2").value = document.getElementById("subscriptionName" + index).value;
			document.getElementById("forwardUrl_2").value = document.getElementById("forwardUrl" + index).value;
			var sourceTypes = document.getElementById("sourceTypes" + index).value;
			if (sourceTypes!="" && sourceTypes!=undefined && sourceTypes!=null) {
				var arr = sourceTypes.split(",");
				var checkBoxes = document.getElementsByName("sourceTypes_" + saveOrUpdate);
				for(var i = 0 ; i < checkBoxes.length ; i++){
					for (var j = 0; j < arr.length; j++) {
						if (checkBoxes[i].value == arr[j].split("$")[1]) {
							checkBoxes[i].checked = true;
						}
					}
				}
			}
			document.getElementById("forwardId_2").value = document.getElementById("forwardId" + index).value;
			document.getElementById("remark_2").value = document.getElementById("remark" + index).value;
			document.getElementById("account_2").value = document.getElementById("account" + index).value;
			document.getElementById("password_2").value = document.getElementById("password" + index).value;
			var isEnable = document.getElementById("isEnable" + index).value;
			$("input[name='isEnable_2']").each(function(index) {
		    if ($("input[name='isEnable_2']").get(index).value == isEnable) {
		        $("input[name='isEnable_2']").get(index).checked = true;
		    }
			});
		}

		function deleteOne(index){
			var frm = document.forms["moduleFrm"];
			frm.act.value = "toDelete";
			currentAction = "toDelete";
			frm.forwardId.value = document.getElementById("forwardId" + index).value;
			if(!confirm("确定要删除所选择的记录吗")) {
				return;
			}	
			AJForm.submitForm(frm);
		}

</script>
</body>
</html>