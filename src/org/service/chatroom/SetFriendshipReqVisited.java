package org.service.chatroom;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dao.declare.FriendshipReqDao;
import org.dao.impl.FriendshipReqDaoImpl;

public class SetFriendshipReqVisited extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		HttpSession session = req.getSession();
		Integer id = new Integer(req.getParameter("id"));

		FriendshipReqDao dao = new FriendshipReqDaoImpl();
		dao.setVisited(id);
	}
}
