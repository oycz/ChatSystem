package org.dao.declare;

import java.util.List;

import org.bean.FriendshipReq;

public interface FriendshipReqDao {
	// 添加好友申请
	public int addFriendReq(String sendUserName, String receiveUserName);

	// 查询没看过的好友申请
	public List<FriendshipReq> queryUnvisitedMessage(String receiveUserName);

	// 将好友申请置为看过
	public void setVisited(int id);
}
