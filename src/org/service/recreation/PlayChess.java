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

public class PlayChess extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession();
		ServletContext context = session.getServletContext();
		User userInfo = (User) session.getAttribute("userInfo");
		PrintWriter pw = res.getWriter();
		String userName = userInfo.getUserName();
		// 正在下棋的用户
		Map<Integer, String[]> playingUser = (Map<Integer, String[]>) context.getAttribute("playingUser");
		// 该哪个颜色下棋
		Map<Integer, Integer> chessPlacing = (Map<Integer, Integer>) context.getAttribute("chessPlacing");
		// 正在进行游戏的棋盘
		Map<Integer, int[][]> playingChess = (HashMap<Integer, int[][]>) context.getAttribute("playingChess");
		// 新棋子信息
		Map<Integer, int[]> chessPlaced = (Map<Integer, int[]>) context.getAttribute("chessPlaced");
		// 最近访问信息
		Map<Integer, Long[]> userAsked = (Map<Integer, Long[]>) context.getAttribute("userAsked");
		// 当前游戏id
		int id = ChessUtil.findGameId(userName, playingUser);
		// 当前轮到哪个颜色
		int placingColor = chessPlacing.get(id);
		// 请求用户的颜色
		int userColor = ChessUtil.getUserColor(userName, playingUser);

		int[][] chess = playingChess.get(id);
		int y = Integer.parseInt(req.getParameter("chess_y"));
		int x = Integer.parseInt(req.getParameter("chess_x"));
		if (placingColor == userColor && chess[y][x] == -1) {
			// 更新下棋时间戳
			Map<Integer, Long> userPlaced = (Map<Integer, Long>) context.getAttribute("userPlaced");
			Long placedTimeStamps = userPlaced.get(id);
			if (placedTimeStamps != null) {
				// 防止游戏已退出
				userPlaced.put(id, System.currentTimeMillis());
			}
			chess[y][x] = userColor;
			// 更新轮到的颜色
			chessPlacing.put(id, chessPlacing.get(id) == 0 ? 1 : 0);
			// 更新新棋子信息
			chessPlaced.put(id, new int[] { y, x });
			// 更新棋盘
			chess[y][x] = userColor;
			// 打印棋盘
			// for (int i = 0; i < 17; ++i) {
			// for (int j = 0; j < 17; ++j) {
			// System.out.print(chess[i][j] + " ");
			// }
			// System.out.println();
			// }
			// 判赢
			int win = ChessUtil.judgeWin(chess, userColor, y, x);
			if (win == 1) {
				// 取得胜利
				pw.write("2");
				// 删除游戏
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
			} else if (win == 0) {
				// 成功返回1
				pw.write("1");
			}

		} else {
			// 失败返回0
			pw.write("0");
		}
	}
}
