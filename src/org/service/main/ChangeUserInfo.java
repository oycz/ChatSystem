package org.service.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.UserDao;
import org.dao.impl.UserDaoImpl;

public class ChangeUserInfo extends HttpServlet {

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		String nickName = req.getParameter("nick_name");
		String pwd = req.getParameter("pwd");
		HttpSession session = req.getSession();
		User userInfo = (User) session.getAttribute("userInfo");
		String userName = (String) (userInfo.getUserName());
		UserDao dao = new UserDaoImpl();
		dao.updateNickName(nickName, userName);
		if (pwd != null) {
			dao.updatePwd(userName, pwd);
		}
		if (nickName != null) {
			userInfo.setUserNickName(nickName);
		}
	}

}
