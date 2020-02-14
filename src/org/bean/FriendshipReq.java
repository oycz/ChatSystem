package org.bean;

public class FriendshipReq {

	private Integer id;
	private String sendUserName;
	private String receiveUserName;

	public FriendshipReq(Integer id, String sendUserName, String receiveUserName) {
		this.id = id;
		this.sendUserName = sendUserName;
		this.receiveUserName = receiveUserName;
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

}
