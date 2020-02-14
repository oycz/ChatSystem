package org.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class ChessUtil {

	// 在线五子棋工具类

	// 通过用户名返回正在游戏的id
	public static int findGameId(String userName, Map<Integer, String[]> playingUser) {
		int rst = -1;
		Set<Integer> gameIds = playingUser.keySet();
		for (Integer gameId : gameIds) {
			String[] players = playingUser.get(gameId);
			if (userName.equals(players[0]) || userName.equals(players[1]))
				rst = gameId;
		}
		return rst;
	}

	// 获取用户棋子颜色(数组下标0为黑,下标1为红)
	public static int getUserColor(String userName, Map<Integer, String[]> playingUser) {
		Set<Integer> gameIds = playingUser.keySet();
		for (Integer gameId : gameIds) {
			String[] players = playingUser.get(gameId);
			if (userName.equals(players[0])) {
				return 0;
			} else if (userName.equals(players[1])) {
				return 1;
			}
		}
		return -1;
	}

	// 通过用户名获取对战用户名
	public static String findGameMatch(String userName, Map<Integer, String[]> playingUser) {
		String rst = null;
		Set<Integer> gameIds = playingUser.keySet();
		for (Integer gameId : gameIds) {
			String[] players = playingUser.get(gameId);
			if (userName.equals(players[0])) {
				rst = players[1];
			} else if (userName.equals(players[1])) {
				rst = players[0];
			}
		}
		return rst;
	}

	// 添加配对并返回id
	public static int addGame(String redUserName, String blackUserName, Map<Integer, String[]> playingUser) {
		Properties chessProperties = new Properties();
		try {
			chessProperties.load(ChessUtil.class.getResourceAsStream("/org/util/chess.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int newId = Integer.parseInt(chessProperties.getProperty("chess_id")) + 1;
		// System.out.println(newId);
		playingUser.put(newId, new String[] { redUserName, blackUserName });
		chessProperties.setProperty("chess_id", newId + "");

		OutputStream out = null;
		try {
			out = new FileOutputStream(ChessUtil.class.getResource("chess.properties").getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			chessProperties.store(out, "");
			// System.out.println("stored..." + newId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newId;
	}

	// 删除一场游戏
	public static void delGame(int id, Map<Integer, String[]> playingUser) {
		playingUser.remove(id);
	}

	// @Test
	// public void TestCase() {
	// System.out
	// .println(
	// judgeWin(
	// new int[][] {
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, 1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 } },
	// 1, 2, 2));
	// }

	public static int judgeWin(int[][] chess, int color, int y, int x) {
		// 横
		int i = y, j = x + 1, count = 1;
		while (j < 17 && color == chess[i][j]) {
			if (++count == 5) {
				// 1为赢
				return 1;
			}
			j++;
		}
		j = x - 1;
		while (j >= 0 && color == chess[i][j]) {
			if (++count == 5) {
				return 1;
			}
			j--;
		}
		// 竖
		i = y + 1;
		j = x;
		count = 1;
		while (i < 17 && color == chess[i][j]) {
			// System.out.println("..." + count);
			if (++count == 5) {
				// 1为赢
				return 1;
			}
			i++;
		}
		i = y - 1;
		while (i >= 0 && color == chess[i][j]) {
			// System.out.println("***" + count);
			if (++count == 5) {
				return 1;
			}
			i--;
		}
		// 左上右下
		i = y - 1;
		j = x - 1;
		count = 1;
		while (i >= 0 && j >= 0 && color == chess[i][j]) {
			System.out.println("..." + count);
			if (++count == 5) {
				// 1为赢
				return 1;
			}
			i--;
			j--;
		}
		i = y + 1;
		j = x + 1;
		while (i < 17 && j < 17 && color == chess[i][j]) {
			System.out.println("***" + count);
			if (++count == 5) {
				return 1;
			}
			i++;
			j++;
		}
		// 左下右上
		i = y + 1;
		j = x - 1;
		count = 1;
		while (i < 17 && j >= 0 && color == chess[i][j]) {
			if (++count == 5) {
				// 1为赢
				return 1;
			}
			i++;
			j--;
		}
		i = y - 1;
		j = x + 1;
		while (i >= 0 && j < 17 && color == chess[i][j]) {
			if (++count == 5) {
				return 1;
			}
			i--;
			j++;
		}
		return 0;
	}
}
