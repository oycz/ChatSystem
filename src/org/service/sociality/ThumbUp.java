package org.service.sociality;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.ThumbUpDao;
import org.dao.impl.ThumbUpDaoImpl;

public class ThumbUp extends HttpServlet {
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		res.setContentType("text/plain");
		HttpSession session = req.getSession();

		String userName = (String) (((User) session.getAttribute("userInfo")).getUserName());
		Integer textId = Integer.parseInt(req.getParameter("text_id"));
		ThumbUpDao dao = new ThumbUpDaoImpl();
		if (!dao.queryThumbUp(userName, textId))
			// 没点赞过 点赞
			dao.thumbUp(userName, textId);
		else
			// 否则删除点赞
			dao.cancelThumbUp(userName, textId);
	}

}
