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

public class ListenFriendshipReq extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		res.setContentType("application/json; charset=utf-8");
		JSONObject jo = new JSONObject();
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();
		if (session.getAttribute("userInfo") != null) {
			// 用户已登陆
			FriendshipReqDao dao = new FriendshipReqDaoImpl();
			List<FriendshipReq> fsrs = dao
					.queryUnvisitedMessage(((User) session.getAttribute("userInfo")).getUserName());
			for (int i = 0; i < fsrs.size(); ++i) {
				FriendshipReq fsr = fsrs.get(i);
				jo.put("user" + i, fsr.getSendUserName());
			}
			out.write(jo.toString());
			// 置visited
			for (int i = 0; i < fsrs.size(); ++i) {
				FriendshipReq fsr = fsrs.get(i);
				dao.setVisited(fsr.getId());
			}
		}
	}
}
