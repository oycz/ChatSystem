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

import org.apache.catalina.startup.UserConfig;
import org.bean.User;
import org.util.ChessUtil;

import net.sf.json.JSONObject;

public class UserPlace extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession();
		ServletContext context = session.getServletContext();
		User userInfo = (User) session.getAttribute("userInfo");
		PrintWriter pw = res.getWriter();
		String userName = userInfo.getUserName();
		JSONObject jo = new JSONObject();
		// 正在下棋的用户
		Map<Integer, String[]> playingUser = (Map<Integer, String[]>) context.getAttribute("playingUser");
		// 该哪个颜色下棋
		Map<Integer, Integer> chessPlacing = (Map<Integer, Integer>) context.getAttribute("chessPlacing");
		// 正在进行游戏的棋盘
		Map<Integer, int[][]> playingChess = (HashMap<Integer, int[][]>) context.getAttribute("playingChess");
		// 新棋子信息
		Map<Integer, int[]> chessPlaced = (Map<Integer, int[]>) context.getAttribute("chessPlaced");
		// 用户最后访问
		Map<Integer, Long[]> userAsked = (Map<Integer, Long[]>) context.getAttribute("userAsked");
		// 用户最后下棋
		Map<Integer, Long> userPlaced = (Map<Integer, Long>) context.getAttribute("userPlaced");
		// 当前游戏id
		Integer id = ChessUtil.findGameId(userName, playingUser);
		// 当前轮到哪个颜色
		Integer placingColor = chessPlacing.get(id);
		// 请求用户的颜色
		Integer userColor = ChessUtil.getUserColor(userName, playingUser);
		// 对手用户的颜色
		Integer enemyColor = userColor == 1 ? 0 : 1;
		// 获取访问时间
		Long[] askedTimeStamps = userAsked.get(id);
		// 更新访问时间
		if(askedTimeStamps != null) {
			askedTimeStamps[userColor] = System.currentTimeMillis();			
		}
		if (id != -1) {
			// 对方用户的颜色
			// 对方是否退出了游戏
			// System.out.println("***" + userName + "..." +
			// askedTimeStamps[userColor] + "..."
			// + askedTimeStamps[enemyColor] + "..." +
			// (askedTimeStamps[userColor] - askedTimeStamps[enemyColor]));
			if ((askedTimeStamps[userColor] - askedTimeStamps[enemyColor]) > 5000) {
				jo.put("enemy_exited", 1);
				// 已退出游戏,删除游戏
				// 正在下棋的用户
				playingUser.remove(id);
				// 该哪个颜色下棋
				chessPlacing.remove(id);
				// 正在进行游戏的棋盘
				playingChess.remove(id);
				// 新棋子信息
				chessPlaced.remove(id);
				// 最近访问信息
				userAsked.remove(id);
			} else {
				// System.out.println(userName + "..." + placingColor + "..." +
				// userColor);
				if (placingColor == userColor) {
					Long placedTimeStamps = userPlaced.get(id);
					// 自己是否太长时间不下棋
					if (placedTimeStamps != null) {
						// 防止游戏已退出
						if (System.currentTimeMillis() - placedTimeStamps > 20000
								&& System.currentTimeMillis() - placedTimeStamps < 40000) {
							jo.put("no_placed", "20");
						} else if (System.currentTimeMillis() - placedTimeStamps >= 40000) {
							jo.put("no_placed", "40");

						}
					}
					// 轮到请求用户返回
					// System.out.println(userName + "返回");
					int[] newChess = chessPlaced.get(id);
					if (newChess != null) {
						jo.put("y", newChess[0]);
						jo.put("x", newChess[1]);
					}
					jo.put("is_user", "1");
				} else {
					Long placedTimeStamps = userPlaced.get(id);
					// 对手是否太长时间不下棋
					if (placedTimeStamps != null) {
						if (System.currentTimeMillis() - placedTimeStamps >= 40000) {
							jo.put("enemy_no_placed", "40");
							// 删除游戏
							// 对手已退出游戏,删除游戏
							// 正在下棋的用户
							playingUser.remove(id);
							// 该哪个颜色下棋
							chessPlacing.remove(id);
							// 正在进行游戏的棋盘
							playingChess.remove(id);
							// 新棋子信息
							chessPlaced.remove(id);
							// 最近访问信息
							userAsked.remove(id);
						}
					}
					// System.out.println(userName + "等着");
					jo.put("is_user", "0");
				}
			}
		} else {
			// 游戏已删除,输棋
			jo.put("losed", "1");
		}

		pw.write(jo.toString());
	}
}
