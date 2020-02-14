package org.service.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.UserStatisticsDao;
import org.dao.impl.UserStatisticsDaoImpl;

public class logAccessUnit extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		int unitNum = Integer.parseInt(req.getParameter("unit_num"));
		HttpSession session = req.getSession();
		User userInfo = (User) session.getAttribute("userInfo");
		String userName = (String) (userInfo.getUserName());
		UserStatisticsDao dao = new UserStatisticsDaoImpl();
		dao.logAccessUnit(userName, unitNum);
	}

}
