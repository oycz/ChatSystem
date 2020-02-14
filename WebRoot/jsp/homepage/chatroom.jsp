<%@ page contentType="text/html;charset=UTF-8"
	import="java.util.*,org.bean.User" language="java"%>
<%
	User userInfo = (User) session.getAttribute("userInfo");
	ServletContext context = pageContext.getServletContext();
	Set<String> onlineUserName = (HashSet<String>) context.getAttribute("onlineUserName");
%>
<div id="chatroom_container">
	<div id="chatroom_left">
		<div id="friendslist">
			<!-- 好友列表 -->
			<jsp:include page="../ajax/friendslist.jsp"></jsp:include>
		</div>
		<div id="online_users" class="display-none">
			<!-- 在线用户 -->
			<jsp:include page="../ajax/onlines.jsp"></jsp:include>
		</div>
		<!-- 左下滑动按钮 -->
		<div id="friendslist_button" class="chatroom_button display-selected"
			onclick="toggle_left(0)">好友列表</div>
		<div id="online_users_button" class="chatroom_button"
			onclick="toggle_left(1)">在线用户</div>
	</div>
	<div id="chatroom_right">
		<!-- 消息列表 -->
		<ul class="nav nav-tabs" id="chat_select">
			<li class="active" onclick="active_chat_window(0);" user-name="聊天室"><a>聊天室</a></li>
		</ul>
		<!-- 聊天窗体 -->
		<div id="chat_windows">
			<!-- 聊天室窗体 -->
			<div class="chat_window" id="chat_window_chatroom">
				<ul id="message_list_chatroom">
				</ul>
			</div>
		</div>
		<div id="talk">
			<textarea id="chatroom_send_context"
				oninput="chatroom_count_words();"
				onkeydown="return chatroom_check_enter(event);"></textarea>
			<!-- 发送消息按钮 -->
			<div id="enter_to_send">
				<div class="enter_to_send_button">
					<input type="checkbox" id="check-box" /> <label for="check-box"></label>
				</div>
				<label for="check-box">按回车发送信息</label>
			</div>
			<!-- 选择表情按钮 -->
			<input type="image" id="emoji_btn"
				src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAZBAMAAAA2x5hQAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAkUExURUxpcfTGAPTGAPTGAPTGAPTGAPTGAPTGAPTGAPTGAPTGAPTGAOfx6yUAAAALdFJOUwAzbVQOoYrzwdwkAoU+0gAAAM1JREFUGNN9kK0PQWEUxl8fM24iCYopwi0muuVuzGyKwATFZpJIU01RUG/RBMnHxfz+Oef9uNM84d1+23nO+zxHKVG2WWupRJkdcAwtpCK0lpbqWE01pB0QayonREMoIp7AawQrWSgGGb4pn6dSeSh68FAVXqHqy3wKrkJiDGDTg3dnp//w+WnwlwIOJauF+C7sXRVfdha4O4oIJfTbtdSxs2uqhs585A0ko8iLTMEcDE1n65A+29pYAlr72nz9dKu7GuNTcsL2fDQzB/wCPVJ69nZGb3gAAAAASUVORK5CYII=">
			<div id="chatroom_wordscount_area">
				<span id="chatroom_words_count">0</span>/400
			</div>
			<div id="chatroom_send_button" onclick="chatroom_send_message()">发送消息</div>
		</div>
	</div>
</div>