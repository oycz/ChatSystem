package org.bean;

import java.util.Map;
import java.util.TreeMap;

public class UserStatistics {

	Integer registerDays = null;
	Integer loginDays = null;
	Map<Integer, Integer> accessUnitCount = new TreeMap<Integer, Integer>();
	Integer friendNum = null;
	Integer chatMessageNum = null;
	Integer conversationMessageNum = null;
	Integer conversationMaxNum = null;
	String conversationMaxName = null;
	Integer socialMessageNum = null;
	Integer getThumbUpNum = null;
	Integer sendThumbUpNum = null;

	public UserStatistics(Integer registerDays, Integer loginDays, Map<Integer, Integer> accessUnitCount,
			Integer friendNum, Integer chatMessageNum, Integer conversationMessageNum, Integer conversationMaxNum,
			String conversationMaxName, Integer socialMessageNum, Integer getThumbUpNum, Integer sendThumbUpNum) {
		super();
		this.registerDays = registerDays;
		this.loginDays = loginDays;
		this.accessUnitCount = accessUnitCount;
		this.friendNum = friendNum;
		this.chatMessageNum = chatMessageNum;
		this.conversationMessageNum = conversationMessageNum;
		this.conversationMaxNum = conversationMaxNum;
		this.conversationMaxName = conversationMaxName;
		this.socialMessageNum = socialMessageNum;
		this.getThumbUpNum = getThumbUpNum;
		this.sendThumbUpNum = sendThumbUpNum;
	}

	public Integer getRegisterDays() {
		return registerDays;
	}

	public void setRegisterDays(Integer registerDays) {
		this.registerDays = registerDays;
	}

	public Integer getLoginDays() {
		return loginDays;
	}

	public void setLoginDays(Integer loginDays) {
		this.loginDays = loginDays;
	}

	public Map<Integer, Integer> getAccessUnitCount() {
		return accessUnitCount;
	}

	public void setAccessUnitCount(Map<Integer, Integer> accessUnitCount) {
		this.accessUnitCount = accessUnitCount;
	}

	public Integer getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(Integer friendNum) {
		this.friendNum = friendNum;
	}

	public Integer getChatMessageNum() {
		return chatMessageNum;
	}

	public void setChatMessageNum(Integer chatMessageNum) {
		this.chatMessageNum = chatMessageNum;
	}

	public Integer getConversationMessageNum() {
		return conversationMessageNum;
	}

	public void setConversationMessageNum(Integer conversationMessageNum) {
		this.conversationMessageNum = conversationMessageNum;
	}

	public Integer getConversationMaxNum() {
		return conversationMaxNum;
	}

	public void setConversationMaxNum(Integer conversationMaxNum) {
		this.conversationMaxNum = conversationMaxNum;
	}

	public String getConversationMaxName() {
		return conversationMaxName;
	}

	public void setConversationMaxName(String conversationMaxName) {
		this.conversationMaxName = conversationMaxName;
	}

	public Integer getSocialMessageNum() {
		return socialMessageNum;
	}

	public void setSocialMessageNum(Integer socialMessageNum) {
		this.socialMessageNum = socialMessageNum;
	}

	public Integer getGetThumbUpNum() {
		return getThumbUpNum;
	}

	public void setGetThumbUpNum(Integer getThumbUpNum) {
		this.getThumbUpNum = getThumbUpNum;
	}

	public Integer getSendThumbUpNum() {
		return sendThumbUpNum;
	}

	public void setSendThumbUpNum(Integer sendThumbUpNum) {
		this.sendThumbUpNum = sendThumbUpNum;
	}

}
