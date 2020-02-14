package org.dao.declare;

import java.util.List;

import org.bean.SocialText;
import org.exception.NoSuchUserException;

public interface SocialTextDao {

	/**
	 * SocialTextDao接口
	 * 
	 * @author ZP
	 * @throws NoSuchUserException
	 */
	public int AddSocialText(String userName, String text);

	public List<SocialText> getAllSocialText();

	public List<SocialText> getFriendsSocialText(String userName);

}
