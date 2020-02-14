package org.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.dao.declare.AllMessagesDao;
import org.db.DBUtil;
import org.junit.Test;
import org.util.MessageUtil;

public class AllMessagesDaoImpl implements AllMessagesDao {

	private static final String GETALLCHATMESSAGESQL = "SELECT content FROM chat_message WHERE user_name=? "
			+ "UNION ALL" + " SELECT content FROM conversation_message WHERE send_user_name=? " + "UNION ALL"
			+ " SELECT content FROM social_text WHERE user_name=?";

	public List<String> getAllMessage(String userName) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		List<String> rst = new LinkedList<String>();
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GETALLCHATMESSAGESQL);
			ps.setString(1, userName);
			ps.setString(2, userName);
			ps.setString(3, userName);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (MessageUtil.removeSpaceAndPunct(rs.getString("content")) != "")
					rst.add(MessageUtil.removeSpaceAndPunct(rs.getString("content")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			throw new RuntimeException("getAllMessage失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

}
