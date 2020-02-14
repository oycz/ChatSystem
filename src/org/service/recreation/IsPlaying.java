package org.service.recreation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.util.ChessUtil;

import net.sf.json.JSONObject;

public class IsPlaying extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text");
		PrintWriter pw = res.getWriter();
		HttpSession session = req.getSession();
		User userInfo = (User) session.getAttribute("userInfo");
		String userName = userInfo.getUserName();
		ServletContext context = session.getServletContext();
		JSONObject jo = new JSONObject();
		Map<Integer, String[]> playingUser = (Map<Integer, String[]>) context.getAttribute("playingUser");
		if (playingUser == null) {
			playingUser = new HashMap<Integer, String[]>();
			context.setAttribute("playingUser", playingUser);
		}
		int id = ChessUtil.findGameId(userName, playingUser);
//		System.out.println(id);
		jo.put("game_id", id);
		if (id != -1) {
			// 该哪个颜色下棋
			Map<Integer, Integer> chessPlacing = (Map<Integer, Integer>) context.getAttribute("chessPlacing");
			// 正在进行游戏的棋盘
			Map<Integer, int[][]> playingChess = (HashMap<Integer, int[][]>) context.getAttribute("playingChess");
			jo.put("playing_chess", playingChess.get(id));
			// 新棋子信息
			Map<Integer, int[]> chessPlaced = (Map<Integer, int[]>) context.getAttribute("chessPlaced");
			jo.put("chess_placed", chessPlaced.get(id));
			// 当前游戏id
			// 当前轮到哪个颜色
			int placingColor = chessPlacing.get(id);
			jo.put("placing_color", placingColor);
			// 请求用户的颜色
			int userColor = ChessUtil.getUserColor(userName, playingUser);
			jo.put("user_color", userColor);
		}
		pw.write(jo.toString());
	}

}
