package org.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.bean.FriendshipReq;
import org.dao.declare.FriendshipReqDao;
import org.db.DBUtil;

public class FriendshipReqDaoImpl implements FriendshipReqDao {

	private static final String ADDFRIENDREQSQL = "INSERT INTO friendship_request (send_user_name,receive_user_name) VALUES (?,?)";
	private static final String QUERYUNVISITEDMESSAGESQL = "SELECT * FROM friendship_request WHERE receive_user_name=? AND visited=0";
	private static final String SETVISITEDSQL = "UPDATE friendship_request SET visited=1 WHERE id=?";

	public int addFriendReq(String sendUserName, String receiveUserName) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int rows = 0;
		try {
			ps = conn.prepareStatement(ADDFRIENDREQSQL);
			ps.setString(1, sendUserName);
			ps.setString(2, receiveUserName);
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
			throw new RuntimeException("添加好友req失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

	public List<FriendshipReq> queryUnvisitedMessage(String receiveUserName) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		List<FriendshipReq> rst = new LinkedList<FriendshipReq>();

		try {
			ps = conn.prepareStatement(QUERYUNVISITEDMESSAGESQL);
			ps.setString(1, receiveUserName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new FriendshipReq(rs.getInt("id"), rs.getString("send_user_name"),
						rs.getString("receive_user_name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询好友req失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	// 将>=id1并<=id2置为看过
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
			throw new RuntimeException("visit好友req失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}
