package org.bean;

import java.util.Date;

public class ThumbUp {

	private Integer userName;
	private Integer textId;
	private Date time;

	public ThumbUp(Integer userName, Integer textId, Date time) {
		super();
		this.userName = userName;
		this.textId = textId;
		this.time = time;
	}

	public Integer getUserName() {
		return userName;
	}

	public void setUserName(Integer userName) {
		this.userName = userName;
	}

	public Integer getTextId() {
		return textId;
	}

	public void setTextId(Integer textId) {
		this.textId = textId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
