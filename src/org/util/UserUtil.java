package org.util;

import java.util.Set;

import org.bean.User;
import org.junit.Test;

public class UserUtil {

	// Whether a list of User contains one specific User or not.
	public static boolean containsUser(Set<User> users, User userInfo) {
		String userInfoName = userInfo.getUserName();
		for (User user : users) {
			if (user.getUserName().equals(userInfoName))
				return true;
		}
		return false;
	}

	public static boolean containsUser(Set<User> users, String userInfoName) {
		for (User user : users) {
			if (user.getUserName().equals(userInfoName))
				return true;
		}
		return false;
	}
}
