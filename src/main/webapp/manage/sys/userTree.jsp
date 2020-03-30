<%-- 本页面用于进行用户列表 --%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.sys.bean.Organ"%>
<%
	List orgsList = (List)request.getAttribute("ORGANMANAGE:ORGANSLIST");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>用户列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>   
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script src="js/treeLevel.js"></script>
	<script language="JavaScript">
		function linkToRight(id){
			if(id=="<%=Organ.ROOT_ORGAN.getOrganID()%>"){
				showList.action = "manage/user";  
				showList.dept.value = "ROOT";
				showList.submit();
			}else{
				showList.action = "manage/user";  
				showList.dept.value = id;
				showList.submit();
			}
		}
	</script>
</head>

<body>
	<table id="cc"  width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="100%" valign="top">
			<TABLE width="100%" height="100%" border="2" align="center" cellpadding="1" cellspacing="3" bordercolor="#a3d7ef" style="BORDER-COLLAPSE: collapse">
			<tr><td>
				<div id="mov1" style="height:100%;width:100%;overflow:auto;white-space:nowrap;" class="DivMargin">
					<p id="<%=Organ.ROOT_ORGAN.getOrganID()%>" > 
               		<img src="images/manage/TreeRoot.gif">
                  	<a href="javascript:void(linkToRight('<%=Organ.ROOT_ORGAN.getOrganID()%>'))">系统用户管理</a>
		    		<script>
				<%
				    for(Iterator it = orgsList.iterator(); it.hasNext(); ){
				        Organ organ = (Organ)(it.next());
					    int index=organ.getOrganID().lastIndexOf('-');
				        String parent_id = "";
				        if (index == -1){ 
				           parent_id = Organ.ROOT_ORGAN.getOrganID();
				        }else{
				           parent_id = organ.getOrganID().substring(0,index);
				        }
						    
				%>
		        		appendNode('<%=(organ.getOrganID())%>',
		                   '<%=parent_id%>',
		                   '<%=(organ.getOrganName())%>',
		                   'javascript:linkToRight("<%=(organ.getOrganID())%>")');
				<%
					}//   end for
				%>
	 					switchSubNodes("<%=Organ.ROOT_ORGAN.getOrganID()%>","none");
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
		<input name="dept" type="hidden" value="">
		<input name="isNewQuery" type="hidden" value="true">
	</form>
</body>
</html>
