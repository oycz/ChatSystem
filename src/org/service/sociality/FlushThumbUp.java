package org.service.sociality;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dao.declare.ThumbUpDao;
import org.dao.impl.ThumbUpDaoImpl;

import net.sf.json.JSONObject;

public class FlushThumbUp extends HttpServlet {
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		res.setContentType("application/json; charset=utf-8");
		JSONObject jo = new JSONObject();
		PrintWriter out = res.getWriter();

		Integer textId = Integer.parseInt(req.getParameter("text_id"));

		ThumbUpDao dao = new ThumbUpDaoImpl();
		List<String> thumbUpUsers = dao.thumbUpUsers(textId);
		for (int i = 0; i < thumbUpUsers.size(); ++i) {
			String userName = thumbUpUsers.get(i);
			jo.put("user" + i, userName);
		}
		out.write(jo.toString());
	}
}
