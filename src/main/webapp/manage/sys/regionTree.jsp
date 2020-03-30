<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.cari.sys.bean.Region"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>区域列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>   
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script src="js/treeLevel.js"></script>
  </head>
<%
	List regions = (List)request.getAttribute("REGIONMANAGE:REGIONSLIST");
%>
  <script>
	function linkToRight(id){
		showDetail.action = "manage/region";  
		showDetail.dwKey.value = id;
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
                <p id="<%=Region.REGION_ROOT.getDwKey()%>" > 
                	<img src="images/manage/TreeRoot.gif">
                  	<a href="javascript:linkToRight('<%=Region.REGION_ROOT.getDwKey()%>')"><%=Region.REGION_ROOT.getFullName()%></a>
		    		<script>
				<%
				    for(Iterator it = regions.iterator(); it.hasNext(); ){
				        Region region = (Region)(it.next());
						
				        String parent_id = "";
						if(region.is_Province()){
							parent_id = Region.REGION_ROOT.getDwKey();
						}else if(region.is_City()){
							parent_id = region.getProvince();
						}else if(region.is_County()){
							parent_id = region.getProvince() + "-" + region.getCity();
						}
				%>
		        		appendNode('<%=(region.getFullName())%>',
		                   '<%=parent_id%>',
		                   '<%=(region.getName())%>',
		                   'javascript:linkToRight("<%=region.getDwKey()%>")');
				<%
					}//   end for
				%>
	 					switchSubNodes("<%=Region.REGION_ROOT.getDwKey()%>","none");
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
		<input name="dwKey" type="hidden">
	</form>
</body>
</html>
