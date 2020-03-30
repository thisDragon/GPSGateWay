<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.cari.sys.bean.SysEventLog,
				com.cari.web.util.*,
				com.cari.web.comm.ListPage" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="cari" %>

<%
	ListPage logPage = (ListPage)request.getAttribute("LOGMANAGE:LOGLIST");
	long nPageCount = logPage.getTotalPageCount();
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
    		function getReturnData( data , statusCode , statusMessage) {
			 	//AJFORM failed. Submit form normally.
			 	if( statusCode != AJForm.STATUS['SUCCESS'] ) {
			 	 alert( statusMessage );
				 return true;
			 	}
			 	//AJFORM succeeded.
			 	else {
					//获取返回信息
					var oDomDoc = Sarissa.getDomDocument();
					oDomDoc = (new DOMParser()).parseFromString(data, "text/xml");
					var status = oDomDoc.selectSingleNode("/HTTPResponse/INFO").getAttribute("ATTIBUTE_STATUS");
					var info = oDomDoc.selectSingleNode("/HTTPResponse/INFO").text;

					if(ajaxAction == "DELETE"){
						if(status == "STATUS_FAILURE"){
							//删除失败，出现提示
							alert(info);
						}else if(status == "STATUS_SUCCESS"){
							//删除成功
							alert(info);
							cateListFrm.act.value = "LIST";
							cateListFrm.submit();
						}
					}else if(ajaxAction == "ACTIVE"){
						if(status == "STATUS_FAILURE"){
							//激活失败，出现提示
							alert(info);
						}else if(status == "STATUS_SUCCESS"){
							//激活成功
							alert(info);
							parent.frLeft.location.reload();
						}
					}
					ajaxAction = "";
			 	}
			}
			
			function toSub(actionName) {
				logFrm.act.value = actionName;
				logFrm.submit();
			}			

		function  gotoPage(pageNo){
			if (pageNo == null || pageNo == "") return;
			if (isNaN(pageNo)) return;
			if (pageNo < 1) pageNo = 1;
			if (pageNo > <%=nPageCount%>) pageNo = <%=nPageCount%>;
			var thisForm = document.forms["logFrm"];
			thisForm.act.value = "QUERY";
			thisForm.pageno.value = pageNo;
			thisForm.submit();
		}
		/** laoding新增函数，用来检测从到第几页得来的分页 2006-8-15*/
		function gotoPageFromInput(){
			var oTempObj=document.logFrm.no;
			var nValue=oTempObj.value;
			if(nValue==""){
				alert("请输入页码");
				oTempObj.focus();
				return false;
			}
			if(isNaN(nValue) || nValue < 1 || nValue > <%=logPage.getTotalPageCount()%>){
				alert("您输入的页码不规范,请重新输入");
				oTempObj.select();
				return false;
			}
			gotoPage(nValue);
		}
	</script> 
  </head>
  <body marginheight="0" topmargin="0" leftmargin="0" rightmargin="0">
  	<Form name="logFrm" method="post" action="manage/log" onsubmit="ajform:getReturnData">
  		<input type="hidden" name="act" value="">
		<input type="hidden" name="pageno" value="<%=logPage.getCurrentPageNo()%>">
  		<br>
  		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
				<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="4" height="23" style="background-image:url(images/blue/conTitle01.jpg);" class="title01"></td>
					<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02">系统日志</td>
					<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
				</tr>
				</table>
			</td>
		</tr>
		</table>
		<TABLE width="98%" border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="#a3d7ef">
			<tr><td colspan="9" style="border-top-width:0px">
				<table bordr=0 width="100%" style="border:solid 1px a3d7ef;border-top:0px;"><tr>
					<td width="40%">
						<input type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="toSub('TOQUERY');" value="查找">
						<input type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="toSub('QUERY');" value="刷新">
					</td>
					<td width="60%" align=right>
							&nbsp;&nbsp;<A href="javascript:gotoPage(1);">首 页</A>&nbsp;&nbsp;
							<%	if(logPage.hasPreviousPage()){ %>
									<A href="javascript:gotoPage(<%=logPage.getCurrentPageNo()-1%>);">上一页</A>&nbsp;&nbsp;
							<%	}else{%>
									上一页&nbsp;&nbsp;
							<%	}	%>
									共<%=logPage.getTotalPageCount()%>/<%=logPage.getCurrentPageNo()%>页&nbsp;&nbsp;
							<%	if(logPage.hasNextPage()) {	%>
									<A href="javascript:gotoPage(<%=logPage.getCurrentPageNo()+1%>);">下一页</A>&nbsp;&nbsp;
							<%	} else {	%>
									下一页&nbsp;&nbsp;
							<%	}	%>
							<A href="javascript:gotoPage(<%=logPage.getTotalPageCount()%>);">尾 页</A>
							到<input name="no" onkeypress="if(event.keyCode==13){gotoPage(this.value);event.keyCode=0;}" style="BORDER-RIGHT: #dddddd 1px solid; BORDER-TOP: #dddddd 1px solid; BORDER-LEFT: #dddddd 1px solid; BORDER-BOTTOM: #dddddd 1px solid; HEIGHT: 16px" type="text" size="2">
				页<A href="javascript:gotoPage(document.logFrm.no.value);">GO</A>
						</td>
					</tr>
					</table>
				</td>
			</tr>
		<%
			if(logPage != null && !logPage.isEmpty()){
				List logList = logPage.getDataList();
				int index = (logPage.getCurrentPageNo() - 1) * logPage.getCurrentPageSize() + 1;
				for(int i = 0 ; i < logList.size() ; i++){
					SysEventLog log = (SysEventLog)logList.get(i);
		%>			
    		<TR height="24" align="center"><td>
				<table cellpadding="3" cellspacing="1" border="0" bgcolor="#a3d7ef" width="100%">
					<tr bgcolor="#FFFFFF">						
						<td align="right" width="10%" style="background-color:#eeeeee;color:#000000">用户名</td><td width="30%"><%=DataFormater.noNullValue(log.getUserName())%></td>
						<td align="right" width="10%" style="background-color:#eeeeee;color:#000000">用户ID</td><td width="20%"><%=DataFormater.noNullValue(log.getUserID())%></td>
						<td align="right" width="10%" style="background-color:#eeeeee;color:#000000">操作类型</td><td width="20%"><%=DataFormater.noNullValue(log.getEvent())%></td>
					</tr><tr bgcolor="#FFFFFF">
						<td align="right" width="10%" style="background-color:#eeeeee;color:#000000">操作时间</td><td><%=DataFormater.noNullValue(log.getEventTime())%></td>
						<td align="right" width="10%" style="background-color:#eeeeee;color:#000000">模块名称</td><td><%=DataFormater.noNullValue(log.getModel())%></td>
						<td align="right" width="10%" style="background-color:#eeeeee;color:#000000">IP地址</td><td><%=DataFormater.noNullValue(log.getIp())%></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td style="display:none" align="right" width="10%" style="background-color:#eeeeee;color:#000000">详情</td><td colspan="5" width="90%"><%=DataFormater.noNullValue(log.getEventObject())%></td>
					</tr>
					<TD></TD>
				</table></td>
    		</TR>
			<tr><td height="2"></td></tr>
		<%		
				}
			}
		%>    		
    		<tr>
				<td class="toolH" colspan="9" style="border-top-width:0px">
					<table bordr=0 width="100%">
					<tr>
						<td width="40%">
							<input style="display:none" type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="toSub('TOQUERY');" value="查找">
							<input style="display:none" type="button" style="background-image:url(images/manage/contentBtn.gif);" class="contentBtn" onClick="toSub('QUERY');" value="刷新">
						</td>
						<td width="60%" align=right>
							&nbsp;&nbsp;<A href="javascript:gotoPage(1);">首 页</A>&nbsp;&nbsp;
							<%	if(logPage.hasPreviousPage()){ %>
									<A href="javascript:gotoPage(<%=logPage.getCurrentPageNo()-1%>);">上一页</A>&nbsp;&nbsp;
							<%	}else{%>
									上一页&nbsp;&nbsp;
							<%	}	%>
									共<%=logPage.getTotalPageCount()%>/<%=logPage.getCurrentPageNo()%>页&nbsp;&nbsp;
							<%	if(logPage.hasNextPage()) {	%>
									<A href="javascript:gotoPage(<%=logPage.getCurrentPageNo()+1%>);">下一页</A>&nbsp;&nbsp;
							<%	} else {	%>
									下一页&nbsp;&nbsp;
							<%	}	%>
							<A href="javascript:gotoPage(<%=logPage.getTotalPageCount()%>);">尾 页</A>
							到<input name="no1" onkeypress="if(event.keyCode==13){gotoPage(this.value);event.keyCode=0;}" style="BORDER-RIGHT: #dddddd 1px solid; BORDER-TOP: #dddddd 1px solid; BORDER-LEFT: #dddddd 1px solid; BORDER-BOTTOM: #dddddd 1px solid; HEIGHT: 16px" type="text" size="2">
				页<A href="javascript:gotoPage(document.logFrm.no1.value);">GO</A>
						</td>
					</tr>
					</table>
				</td>
			</tr>
    	</TABLE>
	</Form>
  </body>
</html>
