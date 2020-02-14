package org.bean;

import java.util.Date;

public class Friendship {

	private Integer userName1;
	private Integer userName2;
	private Date time;

	public Friendship(Integer userName1, Integer userName2, Date time) {
		super();
		this.userName1 = userName1;
		this.userName2 = userName2;
		this.time = time;
	}

	public Integer getUserName1() {
		return userName1;
	}

	public void setUserName1(Integer userName1) {
		this.userName1 = userName1;
	}

	public Integer getUserName2() {
		return userName2;
	}

	public void setUserName2(Integer userName2) {
		this.userName2 = userName2;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
