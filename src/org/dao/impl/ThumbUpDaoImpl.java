package org.dao.impl;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.dao.declare.ThumbUpDao;
import org.db.DBUtil;
import org.util.DateUtil;

public class ThumbUpDaoImpl implements ThumbUpDao {

	private static final String THUMBUPSQL = "INSERT INTO thumbup (user_name,text_id,time) VALUES (?,?,?)";
	private static final String CANCELTHUMBUPSQL = "DELETE FROM thumbup WHERE user_name=? AND text_id=?";
	private static final String QUERYTHUMBUPSQL = "SELECT * FROM thumbup WHERE user_name=? AND text_id=?";
	private static final String THUMBUPUSERSSQL = "SELECT user_name FROM thumbup WHERE text_id=?";

	// 点赞
	public int thumbUp(String userName, Integer textId) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int rows = 0;
		try {
			ps = conn.prepareStatement(THUMBUPSQL);
			ps.setString(1, userName);
			ps.setInt(2, textId);
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
			throw new RuntimeException("点赞失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

	// 取消点赞
	public int cancelThumbUp(String userName, Integer textId) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int rows = 0;
		try {
			ps = conn.prepareStatement(CANCELTHUMBUPSQL);
			ps.setString(1, userName);
			ps.setInt(2, textId);
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
			throw new RuntimeException("取消点赞失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

	// 查询是否点赞过
	public boolean queryThumbUp(String userName, Integer textId) {
		Connection conn = DBUtil.getConnection();
		String sql = "SELECT * FROM thumbup WHERE user_name=? AND text_id=?";
		PreparedStatement ps = null;
		boolean exist = false;
		try {
			ps = conn.prepareStatement(QUERYTHUMBUPSQL);
			ps.setString(1, userName);
			ps.setInt(2, textId);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				exist = true;
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("选择点赞失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return exist;
	}

	// 点赞用户列表
	public List<String> thumbUpUsers(Integer textId) {
		Connection conn = DBUtil.getConnection();
		String sql = "SELECT user_name FROM thumbup WHERE text_id=?";
		PreparedStatement ps = null;
		List<String> rst = new LinkedList<String>();

		try {
			ps = conn.prepareStatement(THUMBUPUSERSSQL);
			ps.setInt(1, textId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(rs.getString("user_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("选择点赞失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}
}
