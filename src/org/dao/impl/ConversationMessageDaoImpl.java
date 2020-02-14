package org.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bean.ConversationMessage;
import org.dao.declare.ConversationMessageDao;
import org.db.DBUtil;
import org.junit.Test;
import org.util.DateUtil;

public class ConversationMessageDaoImpl implements ConversationMessageDao {

	private static final String ADDCONVERSATIONMESSAGESQL = "INSERT INTO conversation_message (send_user_name,receive_user_name,send_user_nick_name,content,time) VALUES (?,?,?,?,?)";
	private static final String GETALLCONVERSATIONMESSAGESQL = "SELECT * FROM conversation_message WHERE receive_user_name=? AND visited=0";
	private static final String GETCONVERSATIONMESSAGEINDAYSSQL = "SELECT * FROM conversation_message WHERE time>?";
	private static final String QUERYIDSQL = "SELECT viewed_conversation_message FROM user_control WHERE name=?";
	private static final String QUERYMESSAGESQL = "SELECT * FROM conversation_message WHERE time>? AND id>?";
	private static final String SETIDSQL = "UPDATE user_control SET viewed_conversation_message=? WHERE name=?";
	private static final String SETVISITEDSQL = "UPDATE conversation_message SET visited=1 WHERE id=?";

	public int addConversationMessage(String sendUserName, String receiveUserName, String sendUserNickName,
			String content) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int rows = 0;
		try {
			ps = conn.prepareStatement(ADDCONVERSATIONMESSAGESQL);
			ps.setString(1, sendUserName);
			ps.setString(2, receiveUserName);
			ps.setString(3, sendUserNickName);
			ps.setString(4, content);
			ps.setString(5, DateUtil.DateToString(new Date(System.currentTimeMillis())));
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
			throw new RuntimeException("添加谈话记录失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

	public List<ConversationMessage> getAllUnvisitedConversationMessage(String receiveUserName) {
		Connection conn = DBUtil.getConnection();
		List<ConversationMessage> rst = new LinkedList<ConversationMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GETALLCONVERSATIONMESSAGESQL);
			ps.setString(1, receiveUserName);
			rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new ConversationMessage(rs.getInt("id"), rs.getString("send_user_name"),
						rs.getString("receive_user_name"), rs.getString("send_user_nick_name"), rs.getString("content"),
						rs.getTimestamp("time")));
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			throw new RuntimeException("getAllConversationMessage失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	public List<ConversationMessage> getConversationMessageInDays(String receiveUserName, int days) {
		Connection conn = DBUtil.getConnection();

		List<ConversationMessage> rst = new LinkedList<ConversationMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GETCONVERSATIONMESSAGEINDAYSSQL);
			ps.setString(1, DateUtil.getDateBeforeDays(days));
			rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new ConversationMessage(rs.getInt("id"), rs.getString("send_user_name"),
						rs.getString("receive_user_name"), rs.getString("send_user_nick_name"), rs.getString("content"),
						rs.getTimestamp("time")));
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			throw new RuntimeException("getConversationMessageInDays失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	// 根据接收用户获取指定天数内指定id后的的聊天记录
	public List<ConversationMessage> getConversationMessageInDaysAndAfterId(String receiveUserName, int days) {
		Connection conn = DBUtil.getConnection();
		List<ConversationMessage> rst = new LinkedList<ConversationMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		int newId = 0;

		// 获取用户目前ID
		try {
			ps = conn.prepareStatement(QUERYIDSQL);
			ps.setString(1, receiveUserName);
			rs = ps.executeQuery();
			if (rs.next())
				id = rs.getInt("viewed_Conversation_message");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("getConversationMessageInDaysAndAfterId失败");
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		newId = id;

		// 根据time和id查询
		try {
			ps = conn.prepareStatement(QUERYMESSAGESQL);
			ps.setString(1, DateUtil.getDateBeforeDays(days));
			ps.setInt(2, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				int queryId = rs.getInt("id");
				rst.add(new ConversationMessage(rs.getInt("id"), rs.getString("send_user_name"),
						rs.getString("receive_user_name"), rs.getString("send_user_nick_name"), rs.getString("content"),
						rs.getTimestamp("time")));
				// 置新newId
				newId = queryId > newId ? queryId : newId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("getConversationMessageInDaysAndAfterId失败");
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 将newId写入数据库
		if (newId != 0) {
			try {
				ps = conn.prepareStatement(SETIDSQL);
				ps.setInt(1, newId);
				ps.setString(2, receiveUserName);
				ps.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new RuntimeException("事务回滚失败");
				}
				throw new RuntimeException("getConversationMessageInDaysAndAfterId失败");
			} finally {
				DBUtil.closeConnection(conn, ps);
			}
		}

		return rst;
	}

	public void setVisited(int id) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(SETVISITEDSQL);
			ps.setInt(1, id);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			e.printStackTrace();
			throw new RuntimeException("visit conversation message失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
