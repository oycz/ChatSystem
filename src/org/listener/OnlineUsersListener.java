package org.listener;

import java.util.TreeSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.bean.User;
import org.util.UserUtil;

public class OnlineUsersListener implements HttpSessionBindingListener, HttpSessionListener {

	public void valueBound(HttpSessionBindingEvent event) {
		// 获得Session对象
		HttpSession session = event.getSession();
		ServletContext context = session.getServletContext();
		User userInfo = (User) session.getAttribute("userInfo");
		Set<User> onlineUserInfo = (TreeSet<User>) context.getAttribute("onlineUserInfo");
		if (onlineUserInfo == null) {
			onlineUserInfo = new TreeSet<User>();
			onlineUserInfo.add(userInfo);
			context.setAttribute("onlineUserInfo", onlineUserInfo);
		} else {
			onlineUserInfo.add(userInfo);
		}
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		// 获得Session对象
		HttpSession session = event.getSession();
		ServletContext context = session.getServletContext();
		// 获取userInfo
		User userInfo = (User) session.getAttribute("userInfo");
		// 从列表中移出
		Set<User> onlineUserInfo = (TreeSet<User>) context.getAttribute("onlineUserInfo");
		if (onlineUserInfo != null && userInfo != null && UserUtil.containsUser(onlineUserInfo, userInfo)) {
			onlineUserInfo.remove(userInfo);
		}
		// 移除从session中移除用户信息
		session.removeAttribute("userInfo");
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		// 获得Session对象
		HttpSession session = event.getSession();
		ServletContext context = session.getServletContext();
		// 获取userInfo
		User userInfo = (User) session.getAttribute("userInfo");
		// 从列表中移出
		Set<String> onlineUserInfo = (TreeSet<String>) context.getAttribute("onlineUserInfo");
		if (onlineUserInfo != null && userInfo != null) {
			onlineUserInfo.remove(userInfo);
			// 移除从session中移除用户信息
			session.removeAttribute("userInfo");
		}

	}

	public void sessionCreated(HttpSessionEvent event) {
		// System.out.println("session created...");
	}

}