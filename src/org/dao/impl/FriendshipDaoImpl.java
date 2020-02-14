package org.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.bean.User;
import org.dao.declare.FriendshipDao;
import org.db.DBUtil;
import org.util.DateUtil;
import org.util.UserUtil;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class FriendshipDaoImpl implements FriendshipDao {

	private static final String ADDFRIENDSHIPSQL = "INSERT INTO friendship (user_name1,user_name2,time) VALUES (?,?,?)";
	private static final String QUERYFRIENDSHIPSQL1 = "SELECT name,nick_name FROM user WHERE name IN ("
			+ "SELECT user_name2 FROM friendship WHERE user_name1=?)";
	private static final String QUERYFRIENDSHIPSQL2 = "SELECT name,nick_name FROM user WHERE name IN ("
			+ "SELECT user_name1 FROM friendship WHERE user_name2=?)";

	// 添加好友关系
	public int addFriendship(String userName1, String userName2) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int rows = 0;
		try {
			ps = conn.prepareStatement(ADDFRIENDSHIPSQL);
			ps.setString(1, userName1);
			ps.setString(2, userName2);
			ps.setString(3, DateUtil.DateToString(new Date(System.currentTimeMillis())));
			rows = ps.executeUpdate();
			conn.commit();
		} catch (MySQLIntegrityConstraintViolationException e1) {
			// 好友已存在
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			throw new RuntimeException("添加好友失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

	// 删除好友关系
	public int delFriendship(String username1, String userName2) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 查询好友
	public Set<User> queryFriends(String userName) {
		// 两次查询
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		Set<User> rst = new TreeSet<User>();

		try {
			// 第一次查询
			ps = conn.prepareStatement(QUERYFRIENDSHIPSQL1);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new User(rs.getString("name"), rs.getString("nick_name")));
			}

			// 关闭ps
			ps.close();

			// 第二次查询
			ps = conn.prepareStatement(QUERYFRIENDSHIPSQL2);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			while (rs.next()) {
				rst.add(new User(rs.getString("name"), rs.getString("nick_name")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询好友列表失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	// 是否是好友
	public boolean isFriend(String userName1, String userName2) {
		return UserUtil.containsUser(queryFriends(userName1), userName2);
	}

}
