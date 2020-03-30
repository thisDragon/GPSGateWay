<%
String path = request.getContextPath();
String sPort = (request.getServerPort() == 80)?"":(":" + request.getServerPort());
String base = request.getScheme()+"://"+request.getServerName()+sPort;
String basePath = base + path + "/";
%>
    <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

	<script type="text/javascript" src="js/ajform.js"></script>
	<script type="text/javascript" src="js/sarissa.js"></script>