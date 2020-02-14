package org.service.statistics;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bean.User;
import org.dao.declare.AllMessagesDao;
import org.dao.impl.AllMessagesDaoImpl;
import org.util.MessageUtil;

import net.sf.json.JSONObject;

public class WordFrequency extends HttpServlet {

	// 获取词频
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setCharacterEncoding("utf-8");
		User userInfo = (User) req.getSession().getAttribute("userInfo");
		String userName = userInfo.getUserName();
		AllMessagesDao dao = new AllMessagesDaoImpl();
		List<String> messages = dao.getAllMessage(userName);
		List<Entry<String, Integer>> frequencies = MessageUtil.getWordFrequency(messages);
		PrintWriter pw = res.getWriter();
		JSONObject jo = new JSONObject();
		int i = 0;
		for (Entry<String, Integer> frequency : frequencies) {
			if (frequency.getKey().getBytes().length > 3) {
				jo.put(frequency.getKey(), frequency.getValue());
				++i;
			}
			if (i >= 10)
				break;
		}
		pw.write(jo.toString());
	}

	// protected void doGet(HttpServletRequest req, HttpServletResponse res)
	// throws ServletException, IOException {
	// this.doPost(req, res);
	// }

}
