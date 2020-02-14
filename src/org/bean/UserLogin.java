package org.bean;

import java.util.Date;

public class UserLogin {

	private String userName;
	private Date time;

	public UserLogin(String userName, Date time) {
		this.userName = userName;
		this.time = time;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
