<%@ page contentType="text/html;charset=UTF-8" import="org.bean.User"
	language="java"%>
<%
	User userInfo = (User) session.getAttribute("userInfo");
	String userName = userInfo.getUserName();
	String userNickName = userInfo.getUserNickName();
%>
<ul class="nav nav-pills" id="homepage_navi">
	<li class="active" onclick="move_scroll(0);" id="chatroom_button"><a>聊天室</a></li>
	<li onclick="move_scroll(1);" id="sociality_button"><a>好友动态</a></li>
	<li onclick="move_scroll(2);" id="recreation_button"><a>休闲一下</a></li>
	<li onclick="move_scroll(3);" id="statistics_button"><a>我与悦聊</a></li>
</ul>
<div id="user_span" onmouseover="show_usermenu();"
	onmouseout="hidden_usermenu();">
	<div id="user_info">
		<div>
			欢迎!
			<h5 id="user_name"><%=userName%></h5>
		</div>
	</div>
	<div id="user_menu" class="display-none">
		<div id="user_settings" onclick="show_settings()">账户设置</div>
		<div id="checkout" onclick="checkout()">退出登录</div>
	</div>
</div>
<div id="user_settings_page" class="display-none"></div>
<div id="homepage_scroll">
	<div class="homepage_page">
		<div class="homepage_container" id="chatroom">
			<jsp:include page="../homepage/chatroom.jsp"></jsp:include>
		</div>
	</div>
	<div class="homepage_page">
		<div class="homepage_container" id="sociality">
			<jsp:include page="../homepage/sociality.jsp"></jsp:include>
		</div>
	</div>
	<div class="homepage_page">
		<div class="homepage_container" id="recreation">
			<jsp:include page="../homepage/recreation.jsp"></jsp:include>
		</div>
	</div>
	<div class="homepage_page">
		<div class="homepage_container" id="statistics"
			onclick="change_chart();">
			<jsp:include page="../homepage/statistics.jsp"></jsp:include>
		</div>
	</div>
</div>