<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page language="java"
	import="java.util.*,org.bean.*,org.dao.declare.*,org.dao.impl.*,org.util.DateUtil,java.io.File"
	pageEncoding="utf-8"%>

<ul>
	<%
		String path = request.getContextPath();
		String icon_basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ path + "/style/user/user_icon/";
		String socialTextImgPath = request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + path + "/style/user/social_image/";
		String userName = ((User) session.getAttribute("userInfo")).getUserName();
		SocialTextDao socialTextDao = new SocialTextDaoImpl();
		ThumbUpDao thumbUpDao = new ThumbUpDaoImpl();
		UserDao userDao = new UserDaoImpl();
		List<SocialText> texts = socialTextDao.getFriendsSocialText(userName);
		for (int i = texts.size() - 1; i >= 0; i--) {
			SocialText text = texts.get(i);
			List<String> thumbUpUsers = thumbUpDao.thumbUpUsers(text.getId());
			String time = DateUtil.DateToString(text.getTime());
			String textUserName = text.getUserName();
			String textUserNickName = userDao.queryNickName(textUserName);
			String userIcon = userDao.queryUserIcon(textUserName);
			Integer textId = text.getId();
	%>
	<li class="social_content_li"><span class="social_content"><div
				class="social_user_info">
				<div class="user_icon text-center">
					<img <%if (userIcon != null) {%>
						src="<%=icon_basePath + userIcon%>" <%} else {%>
						src="<%=icon_basePath + "no_user_icon.jpg"%>">
					<%
						}
					%></img>
				</div>
				<%=textUserNickName != null ? textUserNickName : textUserName%></div><%=text.getContent()%>&nbsp;&nbsp;</span>
		<%
			String extName = "";
				boolean imgExist = false;
				File jpgImg = new File(this.getServletConfig().getServletContext()
						.getRealPath("/style/user/social_image/" + textId + '.' + "jpg"));
				//System.out.println(jpgImg);
				File jpegImg = new File(this.getServletConfig().getServletContext()
						.getRealPath("/style/user/social_image/" + textId + '.' + "jpeg"));
				File gifImg = new File(this.getServletConfig().getServletContext()
						.getRealPath("/style/user/social_image/" + textId + '.' + "gif"));
				File pngImg = new File(this.getServletConfig().getServletContext()
						.getRealPath("/style/user/social_image/" + textId + '.' + "png"));
				File bmpImg = new File(this.getServletConfig().getServletContext()
						.getRealPath("/style/user/social_image/" + textId + '.' + "bmp"));
				if (jpgImg.exists()) {
					extName = "jpg";
					imgExist = true;
				} else if (jpegImg.exists()) {
					extName = "jpeg";
					imgExist = true;
				} else if (gifImg.exists()) {
					extName = "gif";
					imgExist = true;
				} else if (pngImg.exists()) {
					extName = "png";
					imgExist = true;
				} else if (bmpImg.exists()) {
					extName = "bmp";
					imgExist = true;
				}
		%> <%
 	if (imgExist) {
 %>
	<li class="social_content_img"><img class="social_text_img"
		src='<%=socialTextImgPath + textId + "." + extName%>' /></li>
	<%
		}
	%>
	<div class="clear-div"></div>
	<li class="social_content_info_li">
		<div id="send_date">
			发表日期:<%=time%>
			<span id="text_id" class="display-none"><%=text.getId()%></span>
		</div>
		<div id="thumb_up" onclick="do_thumb_up(this);"
			onmouseover="show_thumb_up_list(this);"
			onmouseout="hideen_thumb_up_list(this);">
			<div id="thumb_up_icon"
				class="<%=thumbUpDao.queryThumbUp(((User) session.getAttribute("userInfo")).getUserName(),
						text.getId()) == true ? "already_thumb_up" : "no_thumb_up"%>"></div>
			<div id="thumb_up_num"><%=thumbUpUsers.size()%></div>
			<div id="thumb_up_list" class="display-none">
				<%
					for (String thumbUpUser : thumbUpUsers) {
				%>
				<%=thumbUpUser%>
				<%
					}
				%>
				<br />赞过!
			</div>
		</div>
	</li>
	<%
		}
	%>
</ul>