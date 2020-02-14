package org.dao.declare;

import org.bean.User;
import org.exception.FailedToAddUserException;
import org.exception.NoSuchUserException;

public interface UserDao {

	/**
	 * userDao接口
	 * 
	 * @author ZP
	 * @throws NoSuchUserException
	 */
	public User queryUserByEmail(String uEmail) throws NoSuchUserException;

	public User queryUserByName(String uName) throws NoSuchUserException;

	public int addUser(User user) throws FailedToAddUserException;

	public boolean addUserIcon(byte[] userIcon, String userName);

	public boolean deleteUser();

	public void updateNickName(String nickName, String name);

	public void updatePwd(String name, String pwd);

	public String queryNickName(String name);

	public String queryUserIcon(String name);

	public void setUserIcon(String userIcon, String name);
}
