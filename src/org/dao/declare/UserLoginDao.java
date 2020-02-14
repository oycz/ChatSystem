package org.dao.declare;

public interface UserLoginDao {

	public int getUserLoginCount(String userName);

	public int addLogin(String userName);
}
