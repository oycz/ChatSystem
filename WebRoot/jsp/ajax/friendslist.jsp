<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page language="java"
	import="java.util.*,org.bean.*,org.dao.declare.*,org.dao.impl.*"
	pageEncoding="utf-8"%>
<%
	User userInfo = (User) session.getAttribute("userInfo");
	String userName = userInfo.getUserName();
%>
<small>我的好友: </small>
<span id="addfriend_notice">单击好友开始私聊</span>
&nbsp;
<ul id="chatroom_friendslist" class="chatroom_list">
	<%
		String path = request.getContextPath();
		String icon_basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ path + "/style/user/user_icon/";
		UserDao userDao = new UserDaoImpl();
		FriendshipDao friendshipDao = new FriendshipDaoImpl();
		ThumbUpDao thumbUpDao = new ThumbUpDaoImpl();
		Set<User> friends = friendshipDao.queryFriends(userName);
		for (User friend : friends) {
			String friendNickName = friend.getUserNickName();
			String friendName = friend.getUserName();
			String userIcon = userDao.queryUserIcon(friendName);
	%>
	<li class="chatroom_list_item" id="friend_list_item"
		onclick="chat('<%=friendNickName != null ? friendNickName : friend.getUserName()%>',this);"
		user-name="<%=friend.getUserName()%>"><div
			class="friendlist_icon">
			<img <%if (userIcon != null) {%> src="<%=icon_basePath + userIcon%>"
				<%} else {%> src="<%=icon_basePath + "no_user_icon.jpg"%>">
			<%
				}
			%></img>
		</div>
		<div id="friendlist_name">
			<span> <%
 	if (friendNickName != null) {
 %> <%=friendNickName%>(<%=friend.getUserName()%>) <%
 	} else {
 %> <%=friendName%> <%
 	}
 %>
			</span>
		</div></li>
	<%
		}
	%>

</ul>