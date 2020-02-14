package org.service.statistics;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bean.User;
import org.bean.UserStatistics;
import org.dao.declare.UserStatisticsDao;
import org.dao.impl.UserStatisticsDaoImpl;

import net.sf.json.JSONObject;

public class GetStatistics extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("application/json; charset=utf-8");
		User userInfo = (User) (req.getSession().getAttribute("userInfo"));
		String userName = userInfo.getUserName();
		UserStatisticsDao userStatisticsDao = new UserStatisticsDaoImpl();
		UserStatistics userStatistics = userStatisticsDao.getUserStatistics(userName);

		JSONObject jo = new JSONObject();
		PrintWriter out = res.getWriter();

		jo.put("access_unit_count", userStatistics.getAccessUnitCount());
		jo.put("chat_message_num", userStatistics.getChatMessageNum());
		jo.put("conversation_max_name", userStatistics.getConversationMaxName());
		jo.put("conversation_max_num", userStatistics.getConversationMaxNum());
		jo.put("conversation_message_num", userStatistics.getConversationMessageNum());
		jo.put("friend_num", userStatistics.getFriendNum());
		jo.put("thumb_up_num", userStatistics.getGetThumbUpNum());
		jo.put("login_days", userStatistics.getLoginDays());
		jo.put("register_days", userStatistics.getRegisterDays());
		jo.put("send_thumb_up_num", userStatistics.getSendThumbUpNum());
		jo.put("social_message_num", userStatistics.getSocialMessageNum());
		out.write(jo.toString());
	}

}
