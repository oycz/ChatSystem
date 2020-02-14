package org.dao.declare;

import java.util.List;

import org.bean.ConversationMessage;

public interface ConversationMessageDao {

	// 添加聊天记录
	public int addConversationMessage(String sendUserName, String receiveUserName, String sendUserNickName,
			String content);

	// 根据接收用户获取所有未读聊天记录
	public List<ConversationMessage> getAllUnvisitedConversationMessage(String receiveUserName);

	// 根据接收用户获取指定天数内的聊天记录
	public List<ConversationMessage> getConversationMessageInDays(String receiveUserName, int days);

	// 根据接收用户获取指定天数内指定id以后的的聊天记录
	public List<ConversationMessage> getConversationMessageInDaysAndAfterId(String receiveUserName, int days);

	// 将私聊消息置为看过
	public void setVisited(int id);
}
