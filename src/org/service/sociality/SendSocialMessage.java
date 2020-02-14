package org.service.sociality;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.bean.User;
import org.dao.declare.SocialTextDao;
import org.dao.impl.SocialTextDaoImpl;

public class SendSocialMessage extends HttpServlet {

	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/plain");
		res.setCharacterEncoding("utf-8");
		PrintWriter pw = res.getWriter();
		HttpSession session = req.getSession();
		SocialTextDao dao = new SocialTextDaoImpl();

		String socialMessage = req.getParameter("social_message");
		String userName = (String) (((User) session.getAttribute("userInfo")).getUserName());
		int key = dao.AddSocialText(userName, socialMessage);
		session.setAttribute("justPublishedId", key);
	}
}
