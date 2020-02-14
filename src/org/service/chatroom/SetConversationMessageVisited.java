package org.service.chatroom;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dao.declare.ConversationMessageDao;
import org.dao.impl.ConversationMessageDaoImpl;

public class SetConversationMessageVisited extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		HttpSession session = req.getSession();
		Integer id = new Integer(req.getParameter("id"));

		ConversationMessageDao dao = new ConversationMessageDaoImpl();
		dao.setVisited(id);
	}

}
