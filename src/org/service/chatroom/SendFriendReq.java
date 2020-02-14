package org.service.chatroom;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.FriendshipDao;
import org.dao.declare.FriendshipReqDao;
import org.dao.impl.FriendshipDaoImpl;
import org.dao.impl.FriendshipReqDaoImpl;

public class SendFriendReq extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		HttpSession session = req.getSession();
		String receiveUserName = req.getParameter("rececive_user_name");
		String userName = (String) (((User) session.getAttribute("userInfo")).getUserName());
		PrintWriter pw = res.getWriter();

		FriendshipReqDao fsrDao = new FriendshipReqDaoImpl();
		FriendshipDao fsDao = new FriendshipDaoImpl();

		if (userName.equals(receiveUserName)) {
			pw.write("0");
		} else if (fsDao.isFriend(userName, receiveUserName)) {
			pw.write("2");
		} else {
			fsrDao.addFriendReq(userName, receiveUserName);
			pw.write("1");
		}
	}
}
