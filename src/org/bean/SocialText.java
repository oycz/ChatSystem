package org.bean;

import java.util.Date;

public class SocialText {

	private Integer id;
	private String userName;
	private String content;
	private Date time;

	public SocialText(Integer id, String userName, String content, Date time) {
		this.id = id;
		this.userName = userName;
		this.content = content;
		this.time = time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
