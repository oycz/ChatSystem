package org.dao.impl;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.bean.SocialText;
import org.dao.declare.SocialTextDao;
import org.db.DBUtil;
import org.util.DateUtil;

public class SocialTextDaoImpl implements SocialTextDao {

	private static final String ADDSOCIALTEXTSQL = "INSERT INTO social_text (user_name,content,time) VALUES (?,?,?)";
	private static final String GETALLSOCIALTEXTSQl = "SELECT * FROM social_text";
	private static final String GETFRIENDSSOCIALTEXTSQL = "SELECT * FROM social_text WHERE user_name IN ("
			+ "SELECT user_name1 FROM friendship WHERE user_name2=? " + "UNION ALL "
			+ "SELECT user_name2 FROM friendship WHERE user_name1=?) OR user_name=?";

	public int AddSocialText(String userName, String text) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rst = -1;
		try {
			ps = conn.prepareStatement(ADDSOCIALTEXTSQL, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, userName);
			ps.setString(2, text);
			ps.setString(3, DateUtil.DateToString(new Date(System.currentTimeMillis())));
			ps.executeUpdate();
			conn.commit();
			rs = ps.getGeneratedKeys();
			// 返回自动生成的主键
			if (rs.next()) {
				rst = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			throw new RuntimeException("添加社交状态失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	public List<SocialText> getAllSocialText() {
		Connection conn = DBUtil.getConnection();
		List<SocialText> rst = new LinkedList<SocialText>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GETALLSOCIALTEXTSQl);
			rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new SocialText(rs.getInt("id"), rs.getString("user_name"), rs.getString("content"),
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
			throw new RuntimeException("获取社交状态失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	public List<SocialText> getFriendsSocialText(String userName) {
		Connection conn = DBUtil.getConnection();
		List<SocialText> rst = new LinkedList<SocialText>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GETFRIENDSSOCIALTEXTSQL);
			ps.setString(1, userName);
			ps.setString(2, userName);
			ps.setString(3, userName);
			rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new SocialText(rs.getInt("id"), rs.getString("user_name"), rs.getString("content"),
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
			throw new RuntimeException("获取好友社交状态失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

}
