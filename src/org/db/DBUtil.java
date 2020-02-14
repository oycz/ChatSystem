package org.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
	/**
	 * 数据库工具类
	 * 
	 * @author ZP
	 */

	private static String url;
	private static String driverClassName;
	private static String userName;
	private static String password;

	static {
		// 导入db.properties
		InputStream in = DBUtil.class.getResourceAsStream("/org/db/db.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("读取数据库配置文件失败!");
		}
		url = prop.getProperty("url");
		driverClassName = prop.getProperty("driverClassName");
		userName = prop.getProperty("username");
		password = prop.getProperty("password");
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("加载数据库驱动失败!");
		}

	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, userName, password);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("获取连接失败!");
		}
		return conn;
	}

	// 关闭连接
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("关闭连接失败!");
		}
	}

	public static void closeConnection(Connection conn, PreparedStatement ps) {
		try {
			conn.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("关闭连接失败!");
		}
	}
}
