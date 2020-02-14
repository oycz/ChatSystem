package org.service.chatroom;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.ConversationMessage;
import org.bean.User;
import org.dao.declare.ConversationMessageDao;
import org.dao.declare.UserDao;
import org.dao.impl.ConversationMessageDaoImpl;
import org.dao.impl.UserDaoImpl;
import org.util.DateUtil;
import org.util.MessageUtil;

public class SendConversationMessage extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		PrintWriter pw = res.getWriter();
		HttpSession session = req.getSession();
		String chatMessage = MessageUtil.removeHtmlLabels((String) req.getParameter("chat_message"));

		String sendUserName = (String) (((User) session.getAttribute("userInfo")).getUserName());
		String receiveUserName = (String) (req.getParameter("receive_user_name"));

		UserDao userDao = new UserDaoImpl();
		ConversationMessageDao conversationMessageDao = new ConversationMessageDaoImpl();
		String sendUserNickName = userDao.queryNickName(sendUserName);
		conversationMessageDao.addConversationMessage(sendUserName, receiveUserName, sendUserNickName, chatMessage);
		if (sendUserNickName != null) {
			pw.write("<li class='chatroom_message_list_item'>" + "<div class='chatmessage_info'>" + sendUserNickName
					+ "&nbsp;" + DateUtil.dateFormatter(new Date(System.currentTimeMillis())) + "</div>"
					+ "<div class='chatmessage_content'>" + chatMessage + "</div>" + "</li>");
		} else {
			pw.write("<li class='chatroom_message_list_item'>" + "<div class='chatmessage_info'>" + sendUserName
					+ "&nbsp;" + DateUtil.dateFormatter(new Date(System.currentTimeMillis())) + "</div>"
					+ "<div class='chatmessage_content'>" + chatMessage + "</div>" + "</li>");
		}
	}
}
