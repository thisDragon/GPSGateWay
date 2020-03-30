<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.rbac.Role"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>角色列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>   
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script src="js/treeLevel.js"></script>
</head>
<%
	List roles = (List)request.getAttribute("ROLEMANAGE:LIST");	
%>
<script>
	function linkToRight(role_id){
		showDetail.action = "manage/role";  
		showDetail.role_id.value = role_id;
		showDetail.submit();
	}
</script>

<body>
	<table id="cc"  width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="100%" valign="top">
			<TABLE width="100%" height="100%" border="2" align="center" cellpadding="1" cellspacing="3" bordercolor="#a3d7ef" style="BORDER-COLLAPSE: collapse">
			<tr><td>
				<div id="mov1" style="height:100%;width:100%;overflow:auto;white-space:nowrap;" class="DivMargin">
                <p id="<%=Role.ROOT.getRole_id()%>" > 
                <img src="images/manage/TreeRoot.gif">
                <a href="javascript:void(linkToRight('<%=Role.ROOT.getRole_id()%>'))"><%=Role.ROOT.getRole_name()%></a>
			    <script>
			<%
			    for(Iterator it = roles.iterator(); it.hasNext(); ){
			        Role role = (Role)(it.next());
			%>
		        	appendNode('<%=role.getRole_id()%>',
		                   '<%=Role.ROOT.getRole_id()%>',
		                   '<%=role.getRole_name()%>',
		                   'javascript:linkToRight("<%=role.getRole_id()%>")');
		
			<%
				}//   end for
			%>
		 			switchSubNodes("<%=Role.ROOT.getRole_id()%>","none");
				</script>
				</p>
				</div>
			</td></tr>
			</table>
		</td>
		<td background="images/manage/nav_sp_bg.gif"><img src="images/manage/nav_sp.gif"></td>
	</tr>
	</table>
	<form name="showDetail" method="post" target="frMain" action="">
		<input name="act" type="hidden" value="SHOWDETAIL">
		<input name="role_id" type="hidden">
	</form>
  </body>
</html>
