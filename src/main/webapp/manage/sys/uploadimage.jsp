<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String message = (String)request.getAttribute("SYSTEMCONFIG:MESSAGE");
%>
<HTML>
<HEAD>
	<base href="<%=basePath%>">
	<TITLE>附件上传</TITLE>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<style>a,td,body{font-size:9pt;}
		a {color: #222222;text-decoration: none;}
		a:visited {color: #222222;text-decoration: none;}
		a:hover {color: red;text-decoration: underline;}
		a:active {color: #222222;text-decoration: underline;}
		.cc {color: blue;text-decoration: none;cursor:pointer}
		.cc:visited {color: blue;text-decoration: none;cursor:pointer}
		.cc:hover {color: red;text-decoration: underline;cursor:pointer}
		.cc:active {color: blue;text-decoration: underline;cursor:pointer}
		</style>
	<script>
	function upload(){
		var frm = document.forms["attach"];
		var fileName = frm.attachfile.value;
		var extName = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length);
		if (extName == "png"){
			frm.submit();
		}else{
			alert("对不起，本系统仅支持png格式的水印图片。");
			return false;
		}
	}
	
	function showMessage(){
		<%if (message != null){%>
			alert("<%=message%>");
		<%}%>
	}
</script>
</HEAD>
<BODY marginleft="0" margintop="0" scroll=no onload="showMessage();">
	<form name="attach" enctype="multipart/form-data" method="post" action="manage/config?act=upload">
	<table width="650" border="1" cellspacing="0" cellpadding="0" bordercolorlight="C0BFD1" bordercolordark="#FFFFFF" bordercolor="#ffffff">
	<tr> 
    	<td height="25"><b>上传水印图片：</b></td>
    	<td>要求<font color="#cc3366">图片大小：512*512，图片格式：png</font></td>
	</tr>
	<tr>
    	<td>1、点右边的“<font color="#CC3366">浏览</font>”按钮<br>&nbsp;&nbsp;找到您所要上传的水印图片：</td>
    	<td width="100%"><input type="file" name="attachfile" size="50"></td> 
	</tr>
	<tr>
    	<td nowrap>2、点“<font color="#CC3366">上传文件</font>”按钮，完成操作：</td>
		<td><input type="button" value="上传文件" onclick="upload();"></td>
	</tr>
	</table>
	</form>
</BODY>
</HTML>