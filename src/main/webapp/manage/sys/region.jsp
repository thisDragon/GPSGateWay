<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.sys.bean.Region,com.cari.sys.biz.RegionManage,com.cari.web.util.DataFormater"%>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="wing" %>

<%
	Region region = (Region)request.getAttribute("REGIONMANAGE:REGIONENTITY");
	if (region == null){
		//说明是通过非法途径进入本页面的，强制退出
		response.sendRedirect("../overtime.jsp");
		return;
	}
	boolean isRoot = false;
	String sTitle = "行政区域配置";
	if (region.getCounty()!=null && !"".equals(region.getCounty())){
		sTitle = region.getCounty();
	}else if (region.getCity()!= null && !"".equals(region.getCity())){
		sTitle = region.getCity();
	}else if (region.getProvince()!= null && !"".equals(region.getProvince())){
		sTitle = region.getProvince();
	}
	
	if ("REGION_ROOT".equals(region.getDwKey())){
		isRoot = true;
	}
	
	RegionManage rm = RegionManage.getInstance();
	long childCountyCount = rm.getChildCount(region);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/HeadInclude.jsp"%>   
    <title>区域信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
    <script language="javascript" src="js/manage.js"></script>
	<script>
		var ajaxAction = "";
		var dwKey = "<%=region.getDwKey()%>";
		var regionName = "<%=region.getName()%>";
		var m_sOldRegionCode = "<%=region.getCityCode()%>";
		
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
					parent.frLeft.location.reload();
				}else if(ajaxAction == "DELETE"){
					if("ADDCHILD" == regionForm.preop.value){
						parent.frLeft.removeNode('<%=region.getFullName()%>' + regionName);
						regionForm.act.value = "SHOWDETAIL";
		                regionForm.fullName.value = '<%=region.getFullName()%>';
						regionForm.submit();
					}else{
						parent.frLeft.removeNode('<%=region.getFullName()%>');
						regionForm.act.value = "SHOWDETAIL";
		                regionForm.fullName.value = '<%=region.getParentFullName()%>';
						regionForm.submit();
					}
				}
			}else{
				alert(oDomDoc.selectSingleNode("/root/Error").text);
			}
		}

		function btAction(actionName){
			switch(actionName){
				case "ADDCHILD":
					regionForm.act.value="ADDCHILD";
					regionForm.submit();
					break;
					
				case "DELETE":
					if (dwKey == "<%=Region.REGION_ROOT.getDwKey()%>"){
					   alert("该节点是根节点,不能删除!");
					   return false;
					}
					if(dwKey == ''){
						alert('请先在左边选择一个节点.');
						return false;
					}

					if (parent.frLeft.document.getElementById("<%=region.getFullName()%>").getAttribute("class")=="node"){
							window.alert("该节点不是叶节点，不能删除");
							return false;
					}
				
					if(confirm("确定删除该信息?")){
						ajaxAction = "DELETE";
						regionForm.act.value="DELETE";
						AJForm.submitForm(regionForm);
					}
					break;
					
				case "SAVEORUPDATE":
					if(checkForm()){ 
						ajaxAction = "SAVEORUPDATE";
						regionForm.act.value="SAVEORUPDATE";
						AJForm.submitForm(regionForm);
					}
					break;

				case "CANCEL":
					regionForm.act.value = "SHOWDETAIL";
					if("ADDCHILD" == regionForm.preop.value){
		                regionForm.fullName.value = '<%=region.getFullName().equals("") ? Region.REGION_ROOT.getFullName() : region.getFullName()%>';
						regionForm.submit();
					}else{
		                regionForm.fullName.value = '<%=region.getParentFullName()%>';
						regionForm.submit();
					}
					break;
			}
		}
		
		function checkForm(){
			var doc = document.forms["regionForm"];
			if("ADDCHILD" == doc.preop.value){
			<%
				if(region.getProvince().equals("")){
			%>			
					if(doc.province.value == ""){
						alert("请输入省份名称");
						return false;
					}else{
						regionName = doc.province.value;
					}
			<%				
				}
				if(region.is_City()){
			%>			
					if(doc.county.value == ""){
						alert("请输入区、县名称");
						return false;
					}else{
						regionName = doc.county.value;
					}
			<%
				}
				if(region.is_Province()){
			%>
					if(doc.city.value == ""){
						alert("请输入城市名称");
						return false;
					}else{
						regionName = doc.city.value;
					}
			<%
				}
			%>			
			}else{
			<%
				if(region.is_County()){
			%>			
					if(doc.county.value == ""){
						alert("请输入区、县名称");
						return false;
					}else{
						regionName = doc.county.value;
					}
			<%				
				}
				if(region.is_City()){
			%>			
					if(doc.city.value == ""){
						alert("请输入城市名称");
						return false;
					}else{
						regionName = doc.city.value;
					}
			<%
				}
				if(region.is_Province()){
			%>
					if(doc.province.value == ""){
						alert("请输入省份名称");
						return false;
					}else{
						regionName = doc.province.value;
					}
			<%
				}
			%>			
			}
			if (doc.orderno.value == ""){
				alert("请填写排序号");
				doc.orderno.focus();
				return false;
			}
			if (doc.citycode.value == ""){
				alert("请填写区域代码");
				doc.citycode.focus();
				return false;
			}
			if (doc.defaultlayer.value==""){
				alert("请填写默认显示层级");
				doc.defaultlayer.focus();
				return false;
			}
			if (doc.centerx.value==""){
				alert("请填写中心点经度");
				doc.centerx.focus();
				return false;
			}
			if (doc.centery.value==""){
				alert("请填写中心点纬度");
				doc.centery.focus();
				return false;
			}
			if (doc.left.value==""){
				alert("请填写左边界经度");
				doc.left.focus();
				return false;
			}
			if (doc.right.value==""){
				alert("请填写右边界经度");
				doc.right.focus();
				return false;
			}
			if (doc.top.value==""){
				alert("请填写上边界经度");
				doc.top.focus();
				return false;
			}
			if (doc.bottom.value==""){
				alert("请填写下边界经度");
				doc.bottom.focus();
				return false;
			}
			if (isNaN(doc.defaultlayer.value) || doc.defaultlayer.value.indexOf(".")>=0){
				alert("层级必须是整数");
				doc.defaultlayer.select();
				return false;
			}
			if (isNaN(doc.centerx.value) || parseInt(doc.centerx.value)<=0 || parseInt(doc.centerx.value)>=180){
				alert("中心点经度必须是介于0到180之间的数字。");
				doc.centerx.select();
				return false;
			}
			if (isNaN(doc.centery.value) || parseInt(doc.centery.value)<=0 || parseInt(doc.centery.value)>=90){
				alert("中心点纬度必须是介于0到90之间的数字。");
				doc.centery.select();
				return false;
			}
			if (isNaN(doc.left.value) || parseInt(doc.left.value)<=0 || parseInt(doc.left.value)>=180){
				alert("左边界必须是介于0到180之间的数字。");
				doc.left.select();
				return false;
			}
			if (isNaN(doc.right.value) || parseInt(doc.right.value)<=0 || parseInt(doc.right.value)>=180){
				alert("右边界必须是介于0到180之间的数字。");
				doc.right.select();
				return false;
			}
			if (isNaN(doc.top.value) || parseInt(doc.top.value)<=0 || parseInt(doc.top.value)>=90){
				alert("上边界必须是介于0到180之间的数字。");
				doc.top.select();
				return false;
			}
			if (isNaN(doc.bottom.value) || parseInt(doc.bottom.value)<=0 || parseInt(doc.bottom.value)>=90){
				alert("下边界必须是介于0到90之间的数字。");
				doc.bottom.select();
				return false;
			}
			//定义同步调用器
			var syncCaller = new SyncCaller();
			//判断重复
			if (doc.citycode.value!=m_sOldRegionCode){
				syncCaller.call("<%=basePath%>manage/region?act=ISEXIST&citycode=" + doc.citycode.value);
				if (syncCaller.isError){
					alert(syncCaller.error);
					doc.citycode.select();
					return false;
				}
			}
			return true; 
		}
		
		function isHTML(obj){
			if (obj.value.indexOf("<")>=0
				 || obj.value.indexOf(">")>=0
				 || obj.value.indexOf("\'")>=0
				 || obj.value.indexOf("\"")>=0 
				 || obj.value.indexOf("|")>=0
				 || obj.value.indexOf("\\")>=0
				 || obj.value.indexOf("?")>=0
				 || obj.value.indexOf("*")>=0
				 || obj.value.indexOf(":")>=0
				 || obj.value.indexOf("&")>=0
				 || obj.value.indexOf("/")>=0){
				alert("信息中不允许包含以下字符：< > \' \" & / \\ | ? * :");
				obj.select();
			}
		}
		function checkForm(){
			var doc = document.forms[0];
			if (doc.orderno.value == ""){
				alert("请填写排序号");
				doc.orderno.focus();
				return false;
			}
			if (doc.citycode.value == ""){
				alert("请填写区域编码");
				doc.citycode.focus();
				return false;
			}
			if (doc.province.value == ""){
				alert("请填写省份");
				doc.province.focus();
				return false;
			}
			if (doc.defaultlayer.value==""){
				alert("请填写默认显示层级");
				doc.defaultlayer.focus();
				return false;
			}
			if (doc.centerx.value==""){
				alert("请填写中心点经度");
				doc.centerx.focus();
				return false;
			}
			if (doc.centery.value==""){
				alert("请填写中心点纬度");
				doc.centery.focus();
				return false;
			}
			if (doc.left.value==""){
				alert("请填写左边界经度");
				doc.left.focus();
				return false;
			}
			if (doc.right.value==""){
				alert("请填写右边界经度");
				doc.right.focus();
				return false;
			}
			if (doc.top.value==""){
				alert("请填写上边界经度");
				doc.top.focus();
				return false;
			}
			if (doc.bottom.value==""){
				alert("请填写下边界经度");
				doc.bottom.focus();
				return false;
			}
			if (isNaN(doc.defaultlayer.value) || doc.defaultlayer.value.indexOf(".")>=0){
				alert("层级必须是整数");
				doc.defaultlayer.select();
				return false;
			}
			if (isNaN(doc.centerx.value) || parseInt(doc.centerx.value)<=0 || parseInt(doc.centerx.value)>=180){
				alert("中心点经度必须是介于0到180之间的数字。");
				doc.centerx.select();
				return false;
			}
			if (isNaN(doc.centery.value) || parseInt(doc.centery.value)<=0 || parseInt(doc.centery.value)>=90){
				alert("中心点纬度必须是介于0到90之间的数字。");
				doc.centery.select();
				return false;
			}
			if (isNaN(doc.left.value) || parseInt(doc.left.value)<=0 || parseInt(doc.left.value)>=180){
				alert("左边界必须是介于0到180之间的数字。");
				doc.left.select();
				return false;
			}
			if (isNaN(doc.right.value) || parseInt(doc.right.value)<=0 || parseInt(doc.right.value)>=180){
				alert("右边界必须是介于0到180之间的数字。");
				doc.right.select();
				return false;
			}
			if (isNaN(doc.top.value) || parseInt(doc.top.value)<=0 || parseInt(doc.top.value)>=90){
				alert("上边界必须是介于0到180之间的数字。");
				doc.top.select();
				return false;
			}
			if (isNaN(doc.bottom.value) || parseInt(doc.bottom.value)<=0 || parseInt(doc.bottom.value)>=90){
				alert("下边界必须是介于0到90之间的数字。");
				doc.bottom.select();
				return false;
			}
			//定义同步调用器
			var syncCaller = new SyncCaller();
			//判断重复
			if (doc.citycode.value!=m_sOldRegionCode){
				syncCaller.call("<%=basePath%>manage/region?act=ISEXIST&citycode=" + doc.citycode.value);
				if (syncCaller.isError){
					alert(syncCaller.error);
					doc.citycode.select();
					return false;
				}
			}
			return true;
		}
	</script>
</head>
<body  class="listMargin" >
<form name="regionForm" method="post" action="manage/region" onsubmit="ajform:getReturnData" >
	<input type=hidden name="act" value="SAVEORUPDATE">
	<input type=hidden name="preop" value="<%=DataFormater.noNullValue(request.getParameter("preop"))%>">
	<input type=hidden name="dwKey" value="<%=DataFormater.noNullValue(region.getDwKey())%>">
	<input type=hidden name="fullName" >
	<br>
	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
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
	
	<TABLE width="80%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
	<tr>
		<td class="toolH" colspan="4" style="border-top-width:0px">
			<wing:permission module_id="SYS-AREA" operator_id="add">
			<input name="add"  type="button" style="background-image:url(images/manage/contentBtn4.gif);" class="contentBtn4" onClick="btAction('ADDCHILD');" value="新增子区域">
			</wing:permission>	
			<%if (!isRoot){ %>
			<wing:permission module_id="SYS-AREA" operator_id="delete">
			<input name="deletebutton"  style="background-image:url(images/manage/contentBtn.gif);" type="button" class="contentBtn" onClick="btAction('DELETE');" value="删除">
			</wing:permission>	
			<wing:permission module_id="SYS-AREA" operator_id="add,modify">
			<input name="save"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('SAVEORUPDATE');" value="保存">
			</wing:permission>	
			<%} %>
			<input name="cancel"  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="btAction('CANCEL');" value="返回">
		</td>
	</tr>
    <tr>
		<td height="400"><br><br>
			<table align="center" cellpadding="0" cellspacing="0" border="0" bordercolordark="#e1e1e1" bordercolorlight="#FFFFFF" width="80%" height="80%">
			<tr>
				<td width="20%" align="right">排 序 号：</td>
				<td width="30%"><input type="text" name="orderno" maxlength="10" onblur="isHTML(this);" <%=isRoot?"disabled":"" %> value="<%=DataFormater.noNullValue(region.getOrderNo())%>"></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="20%" align="right">默认层级：</td>
				<td width="30%"><input type="text" name="defaultlayer" maxlength="2"  <%=isRoot?"disabled":"" %> value="<%=DataFormater.noNullValue(region.getDefaultLayer())%>"></td>
				<td width="20%" align="right">启用状态：</td>
				<td width="30%">
					<select name="active" <%=isRoot?"disabled":"" %> >
						<option <%=DataFormater.noNullValue(region.getActive()).equals("Y")?"selected":""%> value="Y">是</option>
						<option <%=!DataFormater.noNullValue(region.getActive()).equals("Y")?"selected":""%> value="N">否</option>
					</select>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right">省 份：</td>
				<td width="30%"><input type="text" name="province" maxlength="20" onblur="isHTML(this);" <%=isRoot?"disabled":"" %> <%=(region.is_City()||region.is_County())?"readonly":"" %> <%=childCountyCount>0?"readonly":"" %> value="<%=DataFormater.noNullValue(region.getProvince())%>"></td>
				<td align="right">城 市：</td>
				<td><input type="text" name="city" maxlength="20" onblur="isHTML(this);" <%=isRoot?"disabled":"" %> <%=region.is_County()?"readonly":"" %> <%=childCountyCount>0?"readonly":"" %> value="<%=DataFormater.noNullValue(region.getCity())%>"></td>
			</tr>
			<tr>
				<td align="right">县 区：</td>
				<td><input type="text" name="county" maxlength="20" onblur="isHTML(this);" <%=isRoot?"disabled":"" %> <%=childCountyCount>0?"readonly":"" %> value="<%=DataFormater.noNullValue(region.getCounty())%>"></td>
				<td width="20%" align="right">区域代码：</td>
				<td width="30%"><input type="text" name="citycode" maxlength="20" onblur="isHTML(this);" <%=isRoot?"disabled":"" %>  value="<%=DataFormater.noNullValue(region.getCityCode())%>"></td>
			</tr>
			<tr>
				<td align="right">中心点经度：</td>
				<td><input type="text" name="centerx" maxlength="20" <%=isRoot?"disabled":"" %>  value="<%=DataFormater.noNullValue(region.getCenterX())%>"></td>
				<td align="right">中心点纬度：</td>
				<td><input type="text" name="centery" maxlength="20" <%=isRoot?"disabled":"" %>  value="<%=DataFormater.noNullValue(region.getCenterY())%>"></td>
			</tr>
			<tr>
				<td align="right">左边界经度：</td>
				<td><input type="text" name="left" maxlength="20" <%=isRoot?"disabled":"" %>  value="<%=DataFormater.noNullValue(region.getLeft())%>"></td>
				<td align="right">右边界经度：</td>
				<td><input type="text" name="right" maxlength="20" <%=isRoot?"disabled":"" %>  value="<%=DataFormater.noNullValue(region.getRight())%>"></td>
			</tr>
			<tr>
				<td align="right">上边界纬度：</td>
				<td><input type="text" name="top" maxlength="20" <%=isRoot?"disabled":"" %>  value="<%=DataFormater.noNullValue(region.getTop())%>"></td>
				<td align="right">下边界纬度：</td>
				<td><input type="text" name="bottom" maxlength="20" <%=isRoot?"disabled":"" %>  value="<%=DataFormater.noNullValue(region.getBottom())%>"></td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
</form>
</body>
</html>
