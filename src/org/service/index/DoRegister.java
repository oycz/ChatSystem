package org.service.index;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.UserDao;
import org.dao.impl.UserDaoImpl;
import org.exception.FailedToAddUserException;
import org.exception.NoSuchUserException;

public class DoRegister extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/plain");
		res.setCharacterEncoding("utf-8");
		boolean addSucceed = true;
		PrintWriter pw = res.getWriter();

		String registerEmail = req.getParameter("register_email");
		String registerName = req.getParameter("register_name");
		String registerPassword = req.getParameter("register_password");
		UserDao userDao = new UserDaoImpl();
		User user = new User(registerName, registerEmail, registerPassword);

		String status = null;
		try {
			status = userDao.addUser(user) + "";
		} catch (FailedToAddUserException e) {
			addSucceed = false;
			// 不成功返回4
			pw.write("4");
		}
		if (addSucceed) {
			pw.write(status);
		}
	}

}
