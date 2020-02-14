<%@ page contentType="text/html;charset=UTF-8"
	import="org.bean.User,org.dao.declare.UserDao,org.dao.impl.UserDaoImpl"
	language="java"%>
<%
	String path = request.getContextPath();
	String icon_basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/style/user/user_icon/";
	User userInfo = (User) session.getAttribute("userInfo");
	String userName = userInfo.getUserName();
	String userIp = userInfo.getUserIp();
	String userNickName = userInfo.getUserNickName();
	UserDao dao = new UserDaoImpl();
	String UserIcon = dao.queryUserIcon(userName);
%>
<div id="user_settings_container">
	<div id="settings_page_button" onclick="close_settings()"></div>
	<div id="settingspage_user_icon_div" class="user_icon_div">
		<img src="<%=icon_basePath + UserIcon%>"
			onerror="this.src='<%=icon_basePath%>no_user_icon.jpg'"></img>
	</div>
	<form enctype="multipart/form-data" id="upload_icon_form">
		<a href="javascript:;" class="file">上传头像 <input type="file"
			name="file" id="update_usericon" onchange="updateUserIcon();">
		</a>
	</form>
	<div class="user_modify">
		昵称: <input id="nickname_input" type="text"
			placeholder="<%=userNickName == null ? "暂无昵称" : userNickName%>"
			class="text-center" maxlength="7" />
	</div>
	<div class="user_modify">
		密码: <input type="password" id="pwd_input" placeholder="修改密码"
			class="text-center" maxlength="15" />
	</div>
	<div class="user_modify">
		确认: <input type="password" id="pwd_confirm_input" placeholder="确认密码"
			class="text-center" maxlength="15" />
	</div>
	<div class="user_modify">
		<button onclick="change_user_info();">修改信息</button>
	</div>
</div>