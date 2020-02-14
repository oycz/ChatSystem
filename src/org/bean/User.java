package org.bean;

public class User implements Comparable {

	private String userName;
	private String userIp;
	private String userEmail;
	private String userPwd;
	private String userNickName;

	// 后台用构造方法
	public User(String userName, String userEmail, String userPwd, String userNickName) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPwd = userPwd;
		this.userNickName = userNickName;
	}

	// 后台用构造方法
	public User(String userName, String userEmail, String userPwd) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPwd = userPwd;
	}

	// 前台用构造方法
	public User(String userName, String userIp, String userNickName, boolean indentify) {
		this.userName = userName;
		this.userIp = userIp;
		this.userNickName = userNickName;
	}

	// friendslist
	public User(String userName, String userNickName) {
		this.userName = userName;
		this.userNickName = userNickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String toString() {
		return "User [userName=" + userName + "]";
	}

	public int compareTo(Object obj) {
		return this.toString().compareTo(obj.toString());
	}
}
