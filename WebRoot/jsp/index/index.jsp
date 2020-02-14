<%@ page language="java" import="java.util.*,org.bean.User"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	User userInfo = (User) session.getAttribute("userInfo");
%>

<!DOCTYPE HTML>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=8">
<base href="<%=basePath%>">
<title>欢迎</title>
<!-- lib css -->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>lib/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>lib/css/iziToast.min.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>lib/css/easyform.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>lib/css/jquery.emoji.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>lib/css/jquery.mCustomScrollbar.min.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>lib/css/railscasts.css">
<!-- 自定义css -->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>style/css/main.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>style/css/index_style.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>style/css/homepage_style.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>style/css/sociality.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>style/css/chatroom.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>style/css/statistics.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>style/css/recreation.css">
</head>
<body>
	<canvas id="index_bg"></canvas>
	<!-- 屏蔽背景图片 -->
	<div
		style="position:absolute; height:100%; width: 100%; z-index: -9998;background-color: rgba(255,255,255,0);"></div>
	<%
		if (userInfo == null) {
	%>
	<div class="index_container">
		<%@include file="../elements/login.jsp"%>
	</div>
	<%
		} else {
	%>
	<!-- 要使用这个include标签，不然无法编译！(重复变量名问题) 
			原理:	jsp:include是先将导入的jsp编译，再引入
					另一个是将导入的jsp直接引入一起编译
	-->
	<div id='home_page'>
		<jsp:include page="../elements/home_page.jsp" flush="true" />
	</div>
	<%
		}
	%>
	<!-- lib js -->
	<!-- <script type="text/javascript"
		src="<%=basePath%>lib/js/jquery-3.1.1.js"></script> -->
	<script type="text/javascript" src="<%=basePath%>lib/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>lib/js/bootstrap.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>lib/js/iziToast.min.js"></script>
	<script src="<%=basePath%>lib/js/easyform.js"></script>
	<script src="<%=basePath%>lib/js/Chart.min.js"></script>
	<script src="<%=basePath%>lib/js/highlight.pack.js"></script>
	<script src="<%=basePath%>lib/js/jquery.mousewheel-3.0.6.min.js"></script>
	<script src="<%=basePath%>lib/js/jquery.mCustomScrollbar.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>lib/js/jquery.emoji.js"></script>
	<!-- 自定义js -->
	s
	<script src="<%=basePath%>style/js/index-bg.js"></script>
	<script src="<%=basePath%>style/js/main.js"></script>
	<script src="<%=basePath%>style/js/sociality.js"></script>
	<script src="<%=basePath%>style/js/chatroom.js"></script>
	<script src="<%=basePath%>style/js/notice.js"></script>
	<script src="<%=basePath%>style/js/statistics.js"></script>
	<script src="<%=basePath%>style/js/recreation.js"></script>
</body>
</html>
