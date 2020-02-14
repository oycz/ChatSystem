package org.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.dao.declare.UserLoginDao;
import org.db.DBUtil;
import org.util.DateUtil;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class UserLoginDaoImpl implements UserLoginDao {

	private static final String GETUSERLOGINCOUNTSQL = "SELECT COUNT(*) count FROM user_login WHERE name=?";
	private static final String ADDLOGINSQL = "INSERT INTO user_login (name,date) VALUES (?,?)";

	public int getUserLoginCount(String userName) {

		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rst = -1;
		try {
			ps = conn.prepareStatement(GETUSERLOGINCOUNTSQL);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next())
				rst = rs.getInt("count");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("getUserLoginCount失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	public int addLogin(String userName) {

		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int rows = 0;
		try {
			ps = conn.prepareStatement(ADDLOGINSQL);
			ps.setString(1, userName);
			ps.setString(2, DateUtil.DateToStringNoHours(new Date(System.currentTimeMillis())));
			rows = ps.executeUpdate();
			conn.commit();
		} catch(MySQLIntegrityConstraintViolationException micve) {
			//一天内重复登陆
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("事务回滚失败");
			}
			throw new RuntimeException("ADDLOGINSQL失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

}
