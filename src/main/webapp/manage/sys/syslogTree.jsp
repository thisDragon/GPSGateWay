<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.rbac.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>机构列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>   
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script src="js/treeLevel.js"></script>
</head>
<%
	ModuleManage moduleMng = ModuleManage.getInstance();	
	List moduleList = moduleMng.findChildModules(Module.ROOT.getModuleID());
%>
<script>
	function linkToRight(id){
		showList.action = "manage/log";  
		showList.model.value = id;
		showList.submit();
	}
</script>
<body>
	<table id="cc"  width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="100%" valign="top">
			<TABLE width="100%" height="100%" border="2" align="center" cellpadding="1" cellspacing="3" bordercolor="#a3d7ef" style="BORDER-COLLAPSE: collapse">
			<tr><td>
		    	<div id="mov1" style="height:100%;width:100%;overflow:auto;white-space:nowrap;" class="DivMargin">
					<p id="<%=Module.ROOT.getModuleID()%>" > 
					<img src="images/manage/TreeRoot.gif">
					<a href="javascript:void(linkToRight('<%=Module.ROOT.getModuleID()%>'))">系统模块</a>
					<script>
				<%
				    for(Iterator it = moduleList.iterator(); it.hasNext(); ){
				        Module model = (Module)(it.next());
					    int index=model.getModuleID().lastIndexOf('-');
				        String parent_id = "";
				        if (index == -1){ 
				           parent_id = Module.ROOT.getModuleID();
				        }else{
				           parent_id = model.getModuleID().substring(0,index);
				        }
						    
				%>
		        		appendNode('<%=(model.getModuleID())%>',
		                   '<%=parent_id%>',
		                   '<%=(model.getModuleName())%>',
		                   'javascript:linkToRight("<%=(model.getModuleName())%>")');
		
				<%
					}//   end for
				%>
		 				switchSubNodes("<%=Module.ROOT.getModuleID()%>","none");
		      		</script>
			  		</p>
				</div>
			</td></tr>
			</table>
		</td>
		<td background="images/manage/nav_sp_bg.gif"><img src="images/manage/nav_sp.gif"></td>
	</tr>
	</table>
	<form name="showList" method="post" target="frMain" action="">
		<input name="act" type="hidden" value="QUERY">
		<INPUT type="hidden" name="isNewQuery" value="Y">
		<input name="model" type="hidden">
	</form>
</body>
</html>
