package org.dao.declare;

import java.util.List;

import org.bean.ChatMessage;

public interface ChatMessageDao {

	// 添加聊天记录
	public int AddChatMessage(String userName, String sendUserNickName, String content);

	// 获取所有聊天记录
	public List<ChatMessage> getAllChatMessage();

	// 获取指定天数内的聊天记录
	public List<ChatMessage> getChatMessageInDays(int days);

	// 获取指定天数内指定用户名visitedid以后的的聊天记录
	public List<ChatMessage> getChatMessageInDaysAndAfterId(int days, String userName);

}
