package org.dao.declare;

import java.util.Set;

import org.bean.User;

public interface FriendshipDao {

	// 添加好友
	public int addFriendship(String userName1, String userName2);

	// 删除好友
	public int delFriendship(String username1, String userName2);

	// 查询好友刘表
	public Set<User> queryFriends(String userName1);

	// 查询是否是好友
	public boolean isFriend(String userName1, String userName2);
}
