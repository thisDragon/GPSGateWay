<%@page import="jodd.util.StringUtil"%>
<%@ page language="java" import="java.util.*,com.cari.sys.bean.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.cari.web.util.*" %>
<%@ page import="com.analog.data.util.DateUtils" %>
<%@ page import="com.cari.rbac.LogData" %>
<%@ page import="com.cari.rbac.RBACPermission" %>
<%@ page import="com.cari.web.comm.ListPage" %>
<%@ page import="com.cari.rbac.LogDataManage" %>		
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="cari" %>
<%
	String contextPath = request.getContextPath();
	String bPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
	int operationIndex = 1;
	int pageNo = HttpParamCaster.getIntParameter(request, "pageno", 1);
	if (pageNo < 1) pageNo = 1;
	int pageCount = HttpParamCaster.getIntParameter(request, "pagecount", 20);
	if (pageCount < 1) pageCount = 10;
	Integer logType = 0;
	String logTypeStr = HttpParamCaster.getUTF8Parameter(request, "logType", "");
	logType = StringUtil.isEmpty(logTypeStr)?null:Integer.parseInt(logTypeStr);
	String sourceType = HttpParamCaster.getUTF8Parameter(request, "sourceType", "");
	Integer state = 1;
	String stateStr = HttpParamCaster.getUTF8Parameter(request, "state", "");
	state= StringUtil.isEmpty(stateStr)?null:Integer.parseInt(stateStr);
	String startTime = HttpParamCaster.getUTF8Parameter(request, "startTime", "");
	String endTime = HttpParamCaster.getUTF8Parameter(request, "endTime", "");
	ListPage listPage = LogDataManage.getInstance().getList(pageNo, pageCount, logType, sourceType, state, startTime, endTime);
    request.setAttribute("listPage", listPage);
	long nPageCount = listPage.getTotalPageCount();
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
	<link rel="stylesheet" type="text/css" href="css/dateTime.css">
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
	</script>
</head>
<body style="overflow:hidden">
<script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="js/public.js"></script>
<script type="text/javascript" src="js/dateTime.min.js"></script>
<table height="100%" border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr><td valign="top" height="28" colspan="2">
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
						<td style="display: none">数据类型：<input type="text" size="20" id="sourceType" name="sourceType" value="<%=DataFormater.noNullValue(sourceType) %>" >&nbsp;&nbsp;</td>
						<td>日志类型：<select id="logType" name="logType" ><option value ="">全部</option><option value ="0" <%if(logType !=null && logType == 0){out.println("selected");} %>>上报日志</option><option value ="1" <%if(logType !=null && logType == 1){out.println("selected");} %>>订阅日志</option><option value ="2" <%if(logType !=null && logType == 2){out.println("selected");} %>>查询日志</option></select>&nbsp;&nbsp;</td>
						<td>日志状态：<select id="state" name="state" "><option value ="">全部</option><option value ="1" <%if(state !=null && state == 1){out.println("selected");} %>>成功</option><option value ="0" <%if(state !=null && state == 0){out.println("selected");} %>>失败</option><option value ="-1" <%if(state !=null && state == -1){out.println("selected");} %>>未参与转发</option></select>&nbsp;&nbsp;</td>
						<td>时间范围：&nbsp;&nbsp;从&nbsp;&nbsp;<input readonly="true" id="startTime" name="startTime" type="text" size="20" value="<%=DataFormater.noNullValue(startTime) %>" >&nbsp;&nbsp;到&nbsp;&nbsp;
															<input readonly="true" id="endTime" name="endTime" type="text" size="20" value="<%=DataFormater.noNullValue(endTime) %>" >&nbsp;&nbsp;</td>
						
						<td style="color:#FFFFFF"><input type="submit"  style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" value=" 搜索 ">&nbsp;&nbsp;</td>
						<td style="color:#FFFFFF"><input id="exportBtn" type="button"  style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" value=" 导出 " onClick="toExport();" ></td>
					</tr>
					
				</table>
				</form>
				</td>
			</tr>
		</table>
</td></tr>
  
  <tr>
  <td height="100%";width="33%">
    <!-- <div style="height:100%;width:400px;border:1px solid black"> -->
    <div style="height:100%;width:400px;">
      <div id ="dateTimeContent"></div>
    </div>
  </td>
  <td valign="top" height="100%">
	<div style="height:100%;width:100%;overflow-x:hidden;overflow-y:auto;">
	<table border="0" cellpadding="0" cellspacing="0" align="left" height="100%">
	<tr><td valign="top">
		<table border="0" cellpadding="0" cellspacing="0" align="center">
			<tr><td align="left">
				<form name="listFrm" >
				<INPUT type="hidden" name="act" value="DELCARDS">
				<table border="1" width="900" cellpadding="2" cellspacing="2" bordercolordark="#F2F5FB" bordercolorlight="#B4C6E2" style="border-bottom:0px;border-right:solid 1px #B4C6E2">
					<tr style="color:#3D6198; background-color:#CDDFF7;">
						<td width="60" align="center">序号</td>
						<td style="display:none" width="25"><input name="selectAll" type="checkbox" style="cursor:pointer;" title="全选/取消全选" id="checkall" onClick="selAll();"></td>
						<td style="display:none" width="200" align="center">数据类型</td>
						<td width="200" align="center">日志类型</td>
						<td width="200" align="center">日志状态</td>
						<td width="400" align="center">创建时间</td>
						<td style="display: none" width="800" align="center">内容</td>
						<td width="200" align="center">操作</td>
						<!-- <td  width="400" align="center">操作</td> -->
					</tr>
		<%
			if(listPage != null && !listPage.isEmpty()) {
				List userList = listPage.getDataList();
				int index = (listPage.getCurrentPageNo() - 1) * listPage.getCurrentPageSize() + 1;
				for(Iterator it = userList.iterator(); it.hasNext(); ){
					LogData logData = (LogData)it.next();
					String logTypeDesc = "上报日志";
					switch(logData.getLogType()){
						case 0:
							logTypeDesc = "上报日志";
						break;
						case 1:
							logTypeDesc = "订阅日志";
						break;
						case 2:
							logTypeDesc = "查询日志";
						break;
						default:
							logTypeDesc = "无效类型";
						break;
					}
					
					String stateDesc = "成功";
					switch(logData.getState()){
						case 0:
							stateDesc = "失败";
						break;
						case 1:
							stateDesc = "成功";
						break;
						case -1:
							stateDesc = "未参与转发";
						break;
						default:
							stateDesc = "无效类型";
						break;
					}
		%>			
					<tr height="30">
						<td width="26" align="center"><%=index%></td>
						<td style="display:none"><input name="configId" id="logId<%=index%>" type="checkbox" value="<%=logData.getId()%>"></td>
						<td style="display:none" align="center" id="sourceType<%=index%>" value="<%=logData.getSourceType()%>"><%=logData.getSourceType()%></td>
						<!-- <td align="center" id="logType<%=index%>" value="<%=logData.getLogType()%>"><%=logTypeDesc%></td> -->
						<!-- <td align="center" id="state<%=index%>" value="<%=logData.getState()%>"><%=stateDesc%></td> -->
						<td align="center" ><%=logTypeDesc%><input style="display: none" id="logType<%=index%>" value="<%=logTypeDesc%>"/></td>
						<td align="center" ><%=stateDesc%><input style="display: none" id="state<%=index%>" value="<%=stateDesc%>"/></td>
						<td align="center" id="createTime<%=index%>" value="<%=DateUtils.date2Str(new Date(logData.getCreateTime().getTime()), DateUtils.NORMAL_FORMAT)%>"><%=DateUtils.date2Str(new Date(logData.getCreateTime().getTime()), DateUtils.NORMAL_FORMAT)%>&nbsp;</td>
						<td id="content<%=index%>" style="display: none" align="center" ><%=StringUtil.isEmpty(logData.getContent())?"无":logData.getContent()%></td>
						
						<td align="center">
							<input name=""  type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="view('<%=index%>')" value="查看">
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
</td>
</tr>

<tr><td valign="top" height="22px" colspan="2">
	<table align="center" width="100%" cellpadding="0" cellspacing="0" border="0" style="border:solid 1px #B4C6E2">
		<tr>
			<td bgcolor="#E7F0FB" style="padding-top:4px;padding-left:8px;">
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

<!-- 添加模块div层开始 -->
<div id="addModuleDiv" style="width:400;height:160;position:absolute;left:550;top:90;text-align:center;display:none; background-color:#FFFFFF">
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
     	 	 <td width="100">日志类型：</td>
     	 	 <td width="242">
     	 	 		<span id="logTypeView"></span>
      	 </td>
		  </tr>
		  <tr>
   	 	 <td width="100">日志状态：</td>
		   <td>
		   		<span id="stateView"></span>
 		   </td>
  	  </tr>

    	  <tr>
     	 	 <td width="100">内容：</td>
      		  <td> <textarea style="border:1px solid #a3d7ef;width:180px; height:120px" id="contentView"></textarea></td>
    	  </tr>
		  <tr><td colspan="2" align="center"></td></tr>
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

<div style="display:none;">
<form name="moduleFrm" method="post" action="manage/datalog" onSubmit="ajform:getReturnData">
<input type="hidden" name="sourceType" />
<input type="hidden" name="logType" />
<input type="hidden" name="state" />
<input type="hidden" name="startTime" />
<input type="hidden" name="endTime" />
<input type="hidden" name="act" />
</form>
</div>

<script type="text/javascript">

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


	$("#startTime").datetime({
		type:"datetime",
		value:[2020,1,1,1,1]
	})
	$("#endTime").datetime({
		type:"datetime",
		value:[2020,12,31,23,59]
	})
	
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
					case "toExport":
					  var infoText = xmlNode.text.split(";")[0];
					  var fileName = xmlNode.text.split(";")[1];
					    window.open("<%=bPath%>"+"/partitionImage/"+fileName);
						document.getElementById("exportBtn").value = "导出";
						break;				
					default:
						alert(xmlNode.text)						
						break;
				}
			}else{
				var errorInfo = oDomDoc.selectSingleNode("/root/Error");
				if (errorInfo != null) {
					alert(errorInfo.text);
				}
			}
		}
	
	function view(index) {
		initAddModuleObj();
		document.getElementById("addModuleDiv").style.display="";
		document.getElementById("logTypeView").innerHTML = document.getElementById("logType" + index).value;
		document.getElementById("stateView").innerHTML = document.getElementById("state" + index).value;
		document.getElementById("contentView").value = document.getElementById("content" + index).innerHTML;
	}	

	function toExport(){
		var frm = document.forms["moduleFrm"];
		frm.sourceType.value = document.getElementById("sourceType").value;
		frm.logType.value = document.getElementById("logType").value;
		frm.state.value = document.getElementById("state").value;
		frm.startTime.value = document.getElementById("startTime").value;
		frm.endTime.value = document.getElementById("endTime").value;
		frm.act.value = "toExport";
		currentAction = "toExport";
		document.getElementById("exportBtn").value = "导出中...";
		AJForm.submitForm(frm);
	}
</script>
</body>
</html>