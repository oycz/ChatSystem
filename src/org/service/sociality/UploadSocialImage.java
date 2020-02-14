package org.service.sociality;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bean.User;
import org.dao.declare.SocialTextDao;
import org.dao.impl.SocialTextDaoImpl;
import org.util.ImageUtil;

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

public class UploadSocialImage extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		User userInfo = (User) (req.getSession().getAttribute("userInfo"));
		SmartUpload su = new SmartUpload();
		File icon = null;
		HttpSession session = req.getSession();

		// System.out.println(req.getParameter("new_social_text_id"));
		SocialTextDao dao = new SocialTextDaoImpl();
		Integer justPublishedId = (int) session.getAttribute("justPublishedId");
		String iconPath = null;
		su.initialize(this.getServletConfig(), req, res);
		try {
			su.upload();
			icon = su.getFiles().getFile(0);
			iconPath = this.getServletConfig().getServletContext()
					.getRealPath("/style/user/social_image/" + justPublishedId + '.' + icon.getFileExt());
			icon.saveAs(iconPath);
		} catch (SmartUploadException e) {
			e.printStackTrace();
			throw new RuntimeException("上传社交图片失败");
		}

		ImageUtil.resize(300, new java.io.File(iconPath), new java.io.File(iconPath), 1);
		// ImageUtil.resize(new java.io.File(iconPath), new
		// java.io.File(iconPath), 1);
		// dao.setUserIcon(userInfo.getUserName() + '.' + icon.getFileExt(),
		// userInfo.getUserName());
	}

}
