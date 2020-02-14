<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<base href="<%=basePath%>">
	<title>My JSP 'index.jsp' starting page</title>
	<script type="text/javascript" src="<%=basePath%>lib/js/jquery-3.1.1.js"></script>
	<script type="text/javascript" src="<%=basePath%>lib/js/bootstrap.js"></script>
	<link rel="stylesheet" type="text/css"
		href="<%=basePath%>lib/css/bootstrap.min.css">
</head>
<body>
	
</body>
</html>
