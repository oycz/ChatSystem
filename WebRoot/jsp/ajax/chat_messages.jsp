<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page language="java"
	import="java.util.*,org.bean.*,org.dao.declare.*,org.dao.impl.*,java.text.SimpleDateFormat"
	pageEncoding="utf-8"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String userName = (String) (((User) session.getAttribute("userInfo")).getUserName());
%>
<%
	ChatMessageDao dao = new ChatMessageDaoImpl();
	List<ChatMessage> chatMessages = dao.getChatMessageInDaysAndAfterId(2, userName);
	for (ChatMessage chatMessage : chatMessages) {
		String sendUserNickName = chatMessage.getSendUserNickName();
		String sendUserName = chatMessage.getUserName();
%>
<li class="chatroom_message_list_item">
	<div class="chatmessage_info"><%=chatMessage.getSendUserNickName() != null ? sendUserNickName : sendUserName%>&nbsp;<%=sdf.format(chatMessage.getTime())%></div>
	<div class="chatmessage_content"><%=chatMessage.getContent()%></div>
</li>
<%
	}
%>