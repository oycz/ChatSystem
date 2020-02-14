package org.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bean.ChatMessage;
import org.dao.declare.ChatMessageDao;
import org.db.DBUtil;
import org.junit.Test;
import org.util.DateUtil;

public class ChatMessageDaoImpl implements ChatMessageDao {

	private static final String ADDCHATMESSAGESQL = "INSERT INTO chat_message (user_name,user_nick_name,content,time) VALUES (?,?,?,?)";
	private static final String GETALLCHATMESSAGESQL = "SELECT * FROM chat_message";
	private static final String GETCHATMESSAGEINDAYSSQL = "SELECT * FROM chat_message WHERE time>?";
	private static final String QUERYIDSQL = "SELECT viewed_chat_message FROM user_control WHERE name=?";
	private static final String QUERYMESSAGESQL = "SELECT * FROM chat_message WHERE time>? AND id>?";
	private static final String SETIDSQL = "UPDATE user_control SET viewed_chat_message=? WHERE name=?";

	public int AddChatMessage(String userName, String sendUserNickName, String content) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int rows = 0;
		try {
			ps = conn.prepareStatement(ADDCHATMESSAGESQL);
			ps.setString(1, userName);
			ps.setString(2, sendUserNickName);
			ps.setString(3, content);
			ps.setString(4, DateUtil.DateToString(new Date(System.currentTimeMillis())));
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
			throw new RuntimeException("添加聊天记录失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

	public List<ChatMessage> getAllChatMessage() {
		Connection conn = DBUtil.getConnection();
		List<ChatMessage> rst = new LinkedList<ChatMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GETALLCHATMESSAGESQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new ChatMessage(rs.getInt("id"), rs.getString("user_name"), rs.getString("send_user_nick_name"),
						rs.getString("content"), rs.getTimestamp("time")));
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
			throw new RuntimeException("getAllChatMessage失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	public List<ChatMessage> getChatMessageInDays(int days) {
		Connection conn = DBUtil.getConnection();

		List<ChatMessage> rst = new LinkedList<ChatMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GETCHATMESSAGEINDAYSSQL);
			ps.setString(1, DateUtil.getDateBeforeDays(days));
			rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new ChatMessage(rs.getInt("id"), rs.getString("user_name"), rs.getString("send_user_nick_name"),
						rs.getString("content"), rs.getTimestamp("time")));
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
			throw new RuntimeException("getChatMessageInDays失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	// 获取指定天数内指定用户名的visited id以后的的聊天记录
	public List<ChatMessage> getChatMessageInDaysAndAfterId(int days, String userName) {
		Connection conn = DBUtil.getConnection();
		List<ChatMessage> rst = new LinkedList<ChatMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		int newId = 0;

		// 获取用户目前ID
		try {
			ps = conn.prepareStatement(QUERYIDSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next())
				id = rs.getInt("viewed_chat_message");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("getChatMessageInDaysAndAfterId失败");
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
				rst.add(new ChatMessage(rs.getInt("id"), rs.getString("user_name"), rs.getString("user_nick_name"),
						rs.getString("content"), rs.getTimestamp("time")));
				// 置新newId
				newId = queryId > newId ? queryId : newId;
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("getChatMessageInDaysAndAfterId失败");
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
				ps.setString(2, userName);
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
				throw new RuntimeException("getChatMessageInDaysAndAfterId失败");
			} finally {
				DBUtil.closeConnection(conn, ps);
			}
		}
		return rst;
	}
}
