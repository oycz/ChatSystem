package org.service.chatroom;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.ChatMessageDao;
import org.dao.declare.UserDao;
import org.dao.impl.ChatMessageDaoImpl;
import org.dao.impl.UserDaoImpl;
import org.util.MessageUtil;

public class ChatroomSendMessage extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		HttpSession session = req.getSession();
		String chatMessage = MessageUtil.removeHtmlLabels((String) req.getParameter("chat_message"));
		String userName = (String) (((User) session.getAttribute("userInfo")).getUserName());

		UserDao userDao = new UserDaoImpl();
		ChatMessageDao chatMessageDao = new ChatMessageDaoImpl();
		String userNickName = userDao.queryNickName(userName);
		chatMessageDao.AddChatMessage(userName, userNickName, chatMessage);

	}
}
