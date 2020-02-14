package org.service.chatroom;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.FriendshipDao;
import org.dao.impl.FriendshipDaoImpl;

public class AddFriend extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		HttpSession session = req.getSession();
		String friendName = req.getParameter("friend_name");
		String userName = (String) (((User) session.getAttribute("userInfo")).getUserName());
		FriendshipDao dao = new FriendshipDaoImpl();
		dao.addFriendship(userName, friendName);
	}

}
