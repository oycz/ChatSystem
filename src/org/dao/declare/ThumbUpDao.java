package org.dao.declare;

import java.util.List;

import org.bean.User;

public interface ThumbUpDao {

	// 点赞
	public int thumbUp(String userName, Integer textId);

	// 取消点赞
	public int cancelThumbUp(String userName, Integer textId);

	// 查询是否赞过
	public boolean queryThumbUp(String userName, Integer textId);

	// 点赞用户列表
	public List<String> thumbUpUsers(Integer textId);

}
