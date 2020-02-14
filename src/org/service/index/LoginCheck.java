package org.service.index;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.UserDao;
import org.dao.declare.UserLoginDao;
import org.dao.impl.UserDaoImpl;
import org.dao.impl.UserLoginDaoImpl;
import org.exception.NoSuchUserException;
import org.listener.OnlineUsersListener;
import org.util.UserUtil;

public class LoginCheck extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/plain");
		res.setCharacterEncoding("utf-8");
		PrintWriter pw = res.getWriter();

		String loginInfo = req.getParameter("login_info");
		String loginPassword = req.getParameter("password");
		HttpSession session = req.getSession();
		boolean haveUser = true;
		UserDao userDao = new UserDaoImpl();
		UserLoginDao userLoginDao = new UserLoginDaoImpl();
		User user = null;

		boolean isEmail = loginInfo.contains("@") ? true : false;
		try {
			if (isEmail) {
				user = userDao.queryUserByEmail(loginInfo);
			} else {
				user = userDao.queryUserByName(loginInfo);
			}
		} catch (NoSuchUserException e) {
			// 出错返回0
			pw.print("0");
			haveUser = false;
		}
		if (haveUser) {
			ServletContext context = req.getSession().getServletContext();
			Set<User> onlineUserInfo = (TreeSet<User>) context.getAttribute("onlineUserInfo");
			if (onlineUserInfo != null && UserUtil.containsUser(onlineUserInfo, user)) {
				// 用户已在线返回4
				pw.print("4");
			} else if (loginPassword.equals(user.getUserPwd())) {
				String userName = user.getUserName();
				// 获取session
				session.setAttribute("userInfo", new User(userName, req.getRemoteAddr(), user.getUserNickName(), true));
				req.getSession().setAttribute("listener", new OnlineUsersListener());
				// 成功返回1
				pw.print("1");
				userLoginDao.addLogin(userName);
			} else {
				// 密码错误返回2
				pw.print("2");
			}

		}
		pw.close();
	}
}
