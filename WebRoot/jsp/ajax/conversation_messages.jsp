<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page language="java"
	import="java.util.*,org.bean.*,org.dao.declare.*,org.dao.impl.*,java.text.SimpleDateFormat"
	pageEncoding="utf-8"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String userName = (String) (((User) session.getAttribute("userInfo")).getUserName());
%>
<%
	ConversationMessageDao dao = new ConversationMessageDaoImpl();
	List<ConversationMessage> conversationMessages = dao.getAllUnvisitedConversationMessage(userName);
	for (ConversationMessage conversationMessage : conversationMessages) {
		String sendUserNickName = conversationMessage.getSendUserNickName();
		String sendUserName = conversationMessage.getSendUserName();
%>
<name="<%=sendUserName%>"%></name>
<id="<%=conversationMessage.getId()%>"%></id>
<li class="chatroom_message_list_item">

	<div class="chatmessage_info"><%=sendUserNickName != null ? sendUserNickName : sendUserName%>&nbsp;<%=sdf.format(conversationMessage.getTime())%></div>
	<div class="chatmessage_content"><%=conversationMessage.getContent()%></div>
</li>
</message-end>
<%
	}
%>