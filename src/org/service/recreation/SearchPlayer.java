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
import org.dao.declare.UserDao;
import org.dao.impl.UserDaoImpl;
import org.util.ChessUtil;

import net.sf.json.JSONObject;

public class SearchPlayer extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 同步方法
		synchronized (this) {
			res.setCharacterEncoding("utf-8");
			PrintWriter pw = res.getWriter();
			HttpSession session = req.getSession();
			ServletContext context = session.getServletContext();
			User userInfo = (User) session.getAttribute("userInfo");
			String requestingUserName = (String) context.getAttribute("requestingUserName");
			String userName = userInfo.getUserName();
			UserDao userDao = new UserDaoImpl();
			// 正在游戏的用户
			// 游戏编号,游戏玩家
			Map<Integer, String[]> playingUser = (Map<Integer, String[]>) context.getAttribute("playingUser");
			JSONObject jo = new JSONObject();
			if (ChessUtil.findGameId(userName, playingUser) == -1) {
				// 未配对
				if (requestingUserName != null && !requestingUserName.equals(userName)) {
					// 配对成功
					// 置空请求玩家
					context.setAttribute("requestingUserName", null);
					// 两位玩家加入游戏
					ChessUtil.addGame(requestingUserName, userName, playingUser);
					// 添加棋盘
					int id = ChessUtil.findGameId(requestingUserName, playingUser);
					Map<Integer, int[][]> playingChess = (HashMap<Integer, int[][]>) context
							.getAttribute("playingChess");
					if (playingChess == null) {
						context.setAttribute("playingChess", new HashMap<Integer, int[][]>());
						playingChess = (Map<Integer, int[][]>) context.getAttribute("playingChess");
					}
					if (playingChess.get(id) == null) {
						// 添加新棋盘
						playingChess.put(id,
								new int[][] { { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
										{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 } });
					}

					// 放置轮流信息
					Map<Integer, Integer> chessPlacing = (Map<Integer, Integer>) context.getAttribute("chessPlacing");
					if (chessPlacing == null) {
						context.setAttribute("chessPlacing", new HashMap<Integer, Integer>());
						chessPlacing = (Map<Integer, Integer>) context.getAttribute("chessPlacing");
						// System.out.println("new chessPlacing");
					}
					chessPlacing.put(id, 0);
					// System.out.println("puted:" + chessPlacing.get(id) +
					// "...id:" + id);
					// 新棋子信息
					Map<Integer, int[]> chessPlaced = (Map<Integer, int[]>) context.getAttribute("chessPlaced");
					if (chessPlaced == null) {
						context.setAttribute("chessPlaced", new HashMap<Integer, int[]>());
						chessPlaced = (Map<Integer, int[]>) context.getAttribute("chessPlaced");
					}
					chessPlaced.put(id, null);
					// System.out.println("add" + userName + "..." +
					// playingUser);

					// 最近访问信息
					Map<Integer, Long[]> userAsked = (Map<Integer, Long[]>) context.getAttribute("userAsked");
					if (userAsked == null) {
						context.setAttribute("userAsked", new HashMap<Integer, Long[]>());
						userAsked = (Map<Integer, Long[]>) context.getAttribute("userAsked");
					}
					Long timeStamp = System.currentTimeMillis();
					userAsked.put(id, new Long[] { timeStamp, timeStamp });

					// 最近下棋信息
					Map<Integer, Long> userPlaced = (Map<Integer, Long>) context.getAttribute("userPlaced");
					if (userPlaced == null) {
						context.setAttribute("userPlaced", new HashMap<Integer, Long[]>());
						userPlaced = (Map<Integer, Long>) context.getAttribute("userPlaced");
					}
					userPlaced.put(id, timeStamp);
				} else if (requestingUserName == null) {
					// 无人在等待
					requestingUserName = userName;
					context.setAttribute("requestingUserName", userName);
					// System.out.println(userName + "created...");
				} else {
					// 依旧在等待
					// System.out.println(userName + "waiting...");
				}
			} else {
				// 已经配对
				String matchName = ChessUtil.findGameMatch(userName, playingUser);
				jo.put("name", matchName);
				jo.put("nick_name", userDao.queryNickName(matchName));
				// System.out.println(userName + " succeed...");
				// 返回颜色
				jo.put("color", ChessUtil.getUserColor(userName, playingUser));
			}
			pw.write(jo.toString());
		}
	}

}
