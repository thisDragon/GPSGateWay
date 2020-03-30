<%@ page contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.cari.rbac.Operation" %>



<%
	Set opSet = (Set)request.getAttribute("RIGHTMANAGE:MODULE");
	Integer rightValue = (Integer)request.getAttribute("RIGHTMANAGE:ROLEMODULERIGHT");
	
	for(Iterator it = opSet.iterator() ; it.hasNext() ; ){
		Operation so = (Operation)it.next();
		if((so.getOperate_value() & rightValue.intValue())> 0){
%>		
			  <input type = "checkbox" name="operation" value="<%=so.getOperate_value()%>" checked onchange="javascript:opStatus=true"> <%=so.getOperate_name()%><br><br>
<%		}else{%>
			  <input type = "checkbox" name="operation" value="<%=so.getOperate_value()%>" onchange="javascript:opStatus=true" > <%=so.getOperate_name()%><br><br>
<%		}
	}
	
%>