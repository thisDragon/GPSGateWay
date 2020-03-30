<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="HeadInclude.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'upload.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <script type="text/javascript">
  	function savea() {
  		var frm = document.forms["frma"];
		frm.submit();
		showInfo();
  	}
  	function saveb() {
  		var frm = document.forms["frmb"];
		frm.submit();
		showInfo();
  	}
  	function savec() {
  		var frm = document.forms["frmc"];
		frm.submit();
		showInfo();
  	}
  	
  	function saved() {
  		var frm = document.forms["frmd"];
		frm.submit();
		showInfo();
  	}
  	
  	function showInfo() {
  		document.getElementById("info").innerHTML = "正在上传！";
  	}
  	
  </script>
  <body>
  	<div id="info"></div>
    <form method="post" enctype="multipart/form-data" name="frma" action="<%=basePath %>interface" >
    	<input type="hidden" value="uploada" name="act">
    	用户：<input type="file" name="file1"><br/>    	
    	<input type="button" value="上传" onClick="savea();">
    </form>
    <br/>
    <form method="post" enctype="multipart/form-data" name="frmb" action="<%=basePath %>interface" >
    	<input type="hidden" value="uploadb" name="act">
    	商家：<input type="file" name="file2"><br/>    	
    	<input type="button"  value="上传" onClick="saveb();">
    </form>
    <form method="post" enctype="multipart/form-data" name="frmc" action="<%=basePath %>interface" >
    	<input type="hidden" value="uploadc" name="act">
    	商家1：<input type="file" name="file2"><br/>    	
    	<input type="button"  value="上传" onClick="savec();">
    </form>
    
    <form method="post" enctype="multipart/form-data" name="frmd" action="<%=basePath %>interface" >
    	<input type="hidden" value="uploadd" name="act">
    	地区：<input type="file" name="file2"><br/>    	
    	<input type="button"  value="上传" onClick="saved();">
    </form>
  </body>
</html>
