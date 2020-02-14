package org.service.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.FriendshipReq;
import org.bean.User;
import org.dao.declare.FriendshipReqDao;
import org.dao.impl.FriendshipReqDaoImpl;

import net.sf.json.JSONObject;

public class CheckOnline extends HttpServlet {
	// 检查用户是否在线
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		res.setContentType("application/json; charset=utf-8");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();
		// 在线返回0，否则返回1
		out.print(session.getAttribute("userInfo") == null ? 0 : 1);
		out.close();
	}
}
