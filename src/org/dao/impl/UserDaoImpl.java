package org.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bean.User;
import org.dao.declare.UserDao;
import org.db.DBUtil;
import org.exception.FailedToAddUserException;
import org.exception.NoSuchUserException;
import org.junit.Test;
import org.util.DateUtil;

public class UserDaoImpl implements UserDao {

	private static final String QUERYUSERBYEMAILSQL = "SELECT * FROM user WHERE email=?";
	private static final String QUERYUSERBYNAMESQL = "SELECT * FROM user WHERE name=?";
	private static final String ADDUSERSQL1 = "INSERT INTO user (name,email,password,time) VALUES (?,?,?,?)";
	private static final String ADDUSERSQL2 = "INSERT INTO user_control (name,viewed_chat_message) VALUES (?,0)";
	private static final String UPDATENICKNAMESQL = "UPDATE user SET nick_name=? WHERE name=?";
	private static final String QUERYNICKNAMESQL = "SELECT nick_name FROM user WHERE name=?";
	private static final String QUERYUSERICONSQL = "SELECT user_icon FROM user WHERE name=?";
	private static final String UPDATEPWDSQL = "UPDATE user SET password=? WHERE name=?";
	private static final String UPDATEUSERICONSQL = "UPDATE user SET user_icon=? WHERE name=?";

	// ͨ根据email查询用户
	public User queryUserByEmail(String uEmail) throws NoSuchUserException {
		Connection conn = DBUtil.getConnection();
		User rst = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(QUERYUSERBYEMAILSQL);
			ps.setString(1, uEmail);
			rs = ps.executeQuery();
			if (!rs.next())
				throw new NoSuchUserException("邮箱不存在");
			rst = new User(rs.getString("name"), rs.getString("email"), rs.getString("password"),
					rs.getString("nick_name"));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询用户失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	// ͨ根据用户名查询用户
	public User queryUserByName(String uName) throws NoSuchUserException {
		Connection conn = DBUtil.getConnection();
		User rst = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(QUERYUSERBYNAMESQL);
			ps.setString(1, uName);
			rs = ps.executeQuery();
			if (!rs.next())
				throw new NoSuchUserException("用户名不存在");
			rst = new User(rs.getString("name"), rs.getString("email"), rs.getString("password"),
					rs.getString("nick_name"));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询用户失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	// 添加用户
	public int addUser(User user) throws FailedToAddUserException {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		int status = 0;
		// 标志位
		boolean haveName = true, haveEmail = true;
		String userName = user.getUserName();
		String userEmail = user.getUserEmail();
		try {
			this.queryUserByName(userName);
		} catch (NoSuchUserException e1) {
			// 用户名可以注册
			haveName = false;
		}
		try {
			this.queryUserByEmail(userEmail);
		} catch (NoSuchUserException e1) {
			// 邮箱可以注册
			haveEmail = false;
		}
		if (!haveName && !haveEmail) {
			try {
				ps = conn.prepareStatement(ADDUSERSQL1);
				ps.setString(1, user.getUserName());
				ps.setString(2, user.getUserEmail());
				ps.setString(3, user.getUserPwd());
				ps.setString(4, DateUtil.DateToString(new Date(System.currentTimeMillis())));
				ps.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				// 注册失败
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new RuntimeException("事务回滚失败");
				}
				throw new FailedToAddUserException("添加用户失败");
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			try {
				ps = conn.prepareStatement(ADDUSERSQL2);
				ps.setString(1, user.getUserName());
				ps.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				// 注册失败
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new RuntimeException("事务回滚失败");
				}
				throw new FailedToAddUserException("添加用户控制失败");
			} finally {
				DBUtil.closeConnection(conn, ps);
			}

		}
		if (haveName) {
			// name重复 第一个二进制位置为1
			status += 1;
		}
		if (haveEmail) {
			// email重复 第二个二进制位置为1
			status += 2;
		}
		return status;
	}

	// 删除用户
	public boolean deleteUser() {
		return false;
	}

	// 添加头像
	public boolean addUserIcon(byte[] userIcon, String userName) {
		return false;
	}

	// 修改昵称
	public void updateNickName(String nickName, String name) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(UPDATENICKNAMESQL);
			ps.setString(1, nickName);
			ps.setString(2, name);
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
			throw new RuntimeException("修改昵称失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	// 修改密码
	public void updatePwd(String name, String pwd) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(UPDATEPWDSQL);
			ps.setString(1, pwd);
			ps.setString(2, name);
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
			throw new RuntimeException("修改密码失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	public String queryNickName(String name) {
		Connection conn = DBUtil.getConnection();
		String rst = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(QUERYNICKNAMESQL);
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				rst = rs.getString("nick_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询用户失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	public String queryUserIcon(String name) {
		Connection conn = DBUtil.getConnection();
		String rst = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(QUERYUSERICONSQL);
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				rst = rs.getString("user_icon");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询用户头像失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rst;
	}

	public void setUserIcon(String userIcon, String name) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(UPDATEUSERICONSQL);
			ps.setString(1, userIcon);
			ps.setString(2, name);
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
			throw new RuntimeException("修改用户头像失败");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
