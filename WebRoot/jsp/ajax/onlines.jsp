<%@ page contentType="text/html;charset=UTF-8"
	import="java.util.*,org.bean.*" language="java"%>
<%
	User userInfo = (User) session.getAttribute("userInfo");
	ServletContext context = pageContext.getServletContext();
	Set<User> onlineUserInfo = (TreeSet<User>) context.getAttribute("onlineUserInfo");
%>
<small>在线用户: </small><%=onlineUserInfo.size()%>&nbsp;
<span id="addfriend_notice">单击用户名添加好友</span>
<ul id="chatroom_online_user_list" class="chatroom_list">
	<%
		for (User onlineUser : onlineUserInfo) {
			String nickName = onlineUser.getUserNickName();
	%>
	<li class="chatroom_list_item" onclick="send_add_friend(this);"
		user-name="<%=onlineUser.getUserName()%>">
		<%
			if (nickName != null) {
		%> <%=nickName%>(<%=onlineUser.getUserName()%>) <%
 	} else {
 %> <%=onlineUser.getUserName()%> <%
 	}
 %>
	</li>
	<%
		}
	%>
</ul>