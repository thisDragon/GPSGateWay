<%-- 本页面用于进行用户列表 --%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.sys.bean.Organ"%>
<%@ page import="com.cari.sys.biz.*"%>

<%
	OrganManage om = OrganManage.getInstance();
	List orgsList = om.findChildOrgans(Organ.ROOT_ORGAN.getOrganID() , true);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>部门列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<%@include file="/HeadInclude.jsp"%>
		<link rel="stylesheet" type="text/css" href="css/treestyle.css">
		<script src="js/treeLevel.js"></script>
		<script language="JavaScript">
		function linkToRight(id, name){
			if(id!="<%=Organ.ROOT_ORGAN.getOrganID()%>"){
				showList.dept.value = id;
				if (opener){
					opener.document.forms[0].dept.value = id;
					opener.document.forms[0].deptname.value = name;
				}
				close();
			}
		}
	</script>
	</head>

	<body>
		<table id="cc" width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="100%" valign="top">
					<TABLE width="100%" height="100%" border="2" align="center" cellpadding="1" cellspacing="3" bordercolor="#a3d7ef" style="BORDER-COLLAPSE: collapse">
						<tr>
							<td>
								<div id="mov1"
									style="height:100%;width:100%;overflow:auto;white-space:nowrap;"
									class="DivMargin">
									<p id="<%=Organ.ROOT_ORGAN.getOrganID()%>">
										<img src="images/manage/TreeRoot.gif">
										<a href="javascript:void(linkToRight('<%=Organ.ROOT_ORGAN.getOrganID()%>',''))">机构(部门)</a>
					<script>
				<%
				if(orgsList==null){ 
					return;
				}
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
		                   'javascript:linkToRight("<%=organ.getOrganID()%>","<%=organ.getOrganName()%>")');
		        
				<%
					}//   end for
				%>
	 					switchSubNodes("<%=Organ.ROOT_ORGAN.getOrganID()%>","none");
	      			</script>
									</p>
								</div>
							</td>
						</tr>
					</TABLE>
				</td>
			</tr>
		</table>
		<form name="showList" method="post" target="frMain" action="">
			<input name="act" type="hidden" value="QUERY">
			<input name="dept" type="hidden">
			<input name="isNewQuery" type="hidden" value="true">
		</form>
	</body>
</html>
