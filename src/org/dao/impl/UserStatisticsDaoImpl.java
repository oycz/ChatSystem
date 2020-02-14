package org.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.bean.UserStatistics;
import org.dao.declare.UserStatisticsDao;
import org.db.DBUtil;
import org.junit.Test;
import org.util.DateUtil;

public class UserStatisticsDaoImpl implements UserStatisticsDao {
	private static final String GETREGISTERDAYSSQL = "SELECT time FROM user WHERE name=?";
	private static final String GETLOGINDAYSSQL = "SELECT COUNT(*) count FROM user_login WHERE name=?";
	private static final String GETACCESSUNITCOUNTSQL = "SELECT homepage_item,COUNT(*) count FROM user_click_homepage_item WHERE name=? GROUP BY homepage_item;";
	private static final String GETFRIENDNUMSQL = "SELECT COUNT(*) count FROM("
			+ "SELECT user_name1 FROM friendship WHERE user_name2=? " + "UNION ALL "
			+ "SELECT user_name2 FROM friendship WHERE user_name1=?) friends";
	private static final String GETCHATMESSAGENUMSQL = "SELECT COUNT(*) count FROM chat_message WHERE user_name=?";
	private static final String GETCONVERSATIONMESSAGENUMSQL = "SELECT COUNT(*) count FROM"
			+ "(SELECT * FROM conversation_message WHERE send_user_name=? " + "UNION ALL "
			+ "SELECT * FROM conversation_message WHERE receive_user_name=?) messages";
	private static final String GETCONVERSATIONMESSAGEMAXNUMSQL = "SELECT name.receive_user_name name,COUNT(name.receive_user_name) count "
			+ "FROM(SELECT receive_user_name FROM conversation_message WHERE (send_user_name=? AND receive_user_name IN(SELECT user_name2 FROM friendship WHERE user_name1=? UNION  SELECT user_name1 FROM friendship WHERE user_name2=?)) "
			+ "UNION ALL " + "SELECT send_user_name FROM conversation_message "
			+ "WHERE (receive_user_name=? AND send_user_name IN "
			+ "(SELECT user_name2 FROM friendship WHERE user_name1=? UNION  SELECT user_name1 FROM friendship WHERE user_name2=?))) name "
			+ "GROUP BY name.receive_user_name ORDER BY count DESC LIMIT 1;";
	private static final String GETSOCIALMESSAGENUMSQL = "SELECT COUNT(*) count FROM social_text WHERE user_name=?";
	private static final String GETTHUMBUONUMSQL = "SELECT COUNT(*) count FROM thumbup WHERE text_id IN (SELECT id FROM social_text WHERE user_name=?);";
	private static final String GETSENDTHUMPUPNUMSQL = "SELECT COUNT(*) count FROM thumbup WHERE user_name=?";
	private static final String LOGACCESSUNIT = "INSERT INTO user_click_homepage_item (name,homepage_item,time) VALUES (?,?,?)";

	public UserStatistics getUserStatistics(String userName) {

		Integer registerDays = null;
		Integer loginDays = null;
		Map<Integer, Integer> accessUnitCount = new TreeMap<Integer, Integer>();
		Integer friendNum = null;
		Integer chatMessageNum = null;
		Integer conversationMessageNum = null;
		Integer conversationMaxNum = null;
		String conversationMaxName = null;
		Integer socialMessageNum = null;
		Integer getThumbUpNum = null;
		Integer sendThumbupNum = null;

		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// registerDays
			ps = conn.prepareStatement(GETREGISTERDAYSSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				Date registerDate = rs.getDate("time");
				registerDays = (int) DateUtil.getDaySub(DateUtil.DateToString(registerDate),
						DateUtil.DateToString(new Date(System.currentTimeMillis())));
			}
			ps.close();

			// loginDays
			ps = conn.prepareStatement(GETLOGINDAYSSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				loginDays = rs.getInt("count");
			}
			ps.close();

			// accessUnitCount
			ps = conn.prepareStatement(GETACCESSUNITCOUNTSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			while (rs.next()) {
				accessUnitCount.put(rs.getInt("homepage_item"), rs.getInt("count"));
			}
			ps.close();

			// friendNum
			ps = conn.prepareStatement(GETFRIENDNUMSQL);
			ps.setString(1, userName);
			ps.setString(2, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				friendNum = rs.getInt("count");
			}
			ps.close();

			// chatMessageNum
			ps = conn.prepareStatement(GETCHATMESSAGENUMSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				chatMessageNum = rs.getInt("count");
			}
			ps.close();

			// conversationMessageNum
			ps = conn.prepareStatement(GETCONVERSATIONMESSAGENUMSQL);
			ps.setString(1, userName);
			ps.setString(2, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				conversationMessageNum = rs.getInt("count");
			}
			ps.close();

			// conversationMaxMessageNum conversationMaxName
			ps = conn.prepareStatement(GETCONVERSATIONMESSAGEMAXNUMSQL);
			ps.setString(1, userName);
			ps.setString(2, userName);
			ps.setString(3, userName);
			ps.setString(4, userName);
			ps.setString(5, userName);
			ps.setString(6, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				conversationMaxNum = rs.getInt("count");
				conversationMaxName = rs.getString("name");
			}
			ps.close();

			// socialMessageNum
			ps = conn.prepareStatement(GETSOCIALMESSAGENUMSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				socialMessageNum = rs.getInt("count");
			}
			ps.close();

			// getThumbUpNum
			ps = conn.prepareStatement(GETTHUMBUONUMSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				getThumbUpNum = rs.getInt("count");
			}
			ps.close();

			// sendThumbupNUm
			ps = conn.prepareStatement(GETSENDTHUMPUPNUMSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				sendThumbupNum = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			throw new RuntimeException("获取社交状态失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}

		return new UserStatistics(registerDays, loginDays, accessUnitCount, friendNum, chatMessageNum,
				conversationMessageNum, conversationMaxNum, conversationMaxName, socialMessageNum, getThumbUpNum,
				sendThumbupNum);

	}

	public int logAccessUnit(String name, int unitNum) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int rows = 0;
		try {
			ps = conn.prepareStatement(LOGACCESSUNIT);
			ps.setString(1, name);
			ps.setInt(2, unitNum);
			ps.setString(3, DateUtil.DateToString(new Date(System.currentTimeMillis())));
			rows = ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			throw new RuntimeException("记录模块访问失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

}
