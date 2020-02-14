package org.bean;

import java.util.Date;

public class ConversationMessage {

	private Integer id;
	private String sendUserName;
	private String receiveUserName;
	private String sendUserNickName;
	private String content;
	private Date time;

	public ConversationMessage(Integer id, String sendUserName, String receiveUserName, String sendUserNickName,
			String content, Date time) {
		this.id = id;
		this.sendUserName = sendUserName;
		this.receiveUserName = receiveUserName;
		this.sendUserNickName = sendUserNickName;
		this.content = content;
		this.time = time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public String getSendUserNickName() {
		return sendUserNickName;
	}

	public void setSendUserNickName(String sendUserNickName) {
		this.sendUserNickName = sendUserNickName;
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
