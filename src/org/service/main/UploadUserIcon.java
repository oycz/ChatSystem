package org.service.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bean.User;
import org.dao.declare.UserDao;
import org.dao.impl.UserDaoImpl;
import org.util.ImageUtil;

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

public class UploadUserIcon extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// String contentType = req.getContentType();
		// String servername = req.getServerName();
		// UserDao dao = new UserDaoImpl();
		//
		// InputStream in = null;
		// if (contentType.indexOf("multipart/form-data") >= 0) {
		// in = req.getInputStream();
		// int formLength = req.getContentLength();
		// byte[] formContent = new byte[formLength];
		// int read = 0;
		// int pointer = 0;
		// while (read < formLength) {
		// pointer = in.read(formContent, read, formLength);
		// read += pointer;
		// }
		// String strContent = new String(formContent);
		// int typeStart = strContent.indexOf("Content-Type:") + 14;
		// int typeEnd = strContent.indexOf("\n", typeStart) - 1;
		// String formType = strContent.substring(typeStart, typeEnd);
		// if (formType.equals("image/jpeg") || formType.equals("image/gif") ||
		// formType.equals("image/pjepg")
		// || formType.equals("image/png")) {
		// int filenameStart = strContent.indexOf("filename=\"") + 10;
		// int filenameEnd = strContent.indexOf("\n", filenameStart) - 2;
		// String filename = strContent.substring(filenameStart, filenameEnd);
		// filename = filename.substring(filename.lastIndexOf("."));
		// int filestart = strContent.indexOf("\n", typeStart) + 1;
		// int intBoundary = contentType.indexOf("boundary=") + 10;
		// String strBoundary = contentType.substring(intBoundary);
		// int fileEnd = strContent.indexOf(strBoundary, filestart) - 4;
		// int contentStart = strContent.substring(0,
		// filestart).getBytes().length;
		// int contentEnd = strContent.substring(0, fileEnd).getBytes().length;
		// System.out.println(formContent.length +
		// "...........................");
		// System.out.println(strContent.getBytes().length +
		// "...........................");
		// dao.addUserIcon(Arrays.copyOfRange(formContent, contentStart,
		// contentEnd - contentStart), "admin");
		// res.sendRedirect("sho.jsp");
		// } else {
		// res.sendRedirect("error.jsp");
		// }
		// }
		User userInfo = (User) (req.getSession().getAttribute("userInfo"));
		SmartUpload su = new SmartUpload();
		File icon = null;
		UserDao dao = new UserDaoImpl();
		String iconPath = null;
		su.initialize(this.getServletConfig(), req, res);
		try {
			su.upload();
			// Files icons = su.getFiles();
			icon = su.getFiles().getFile(0);
			iconPath = this.getServletConfig().getServletContext()
					.getRealPath("/style/user/user_icon/" + userInfo.getUserName() + '.' + icon.getFileExt());
			icon.saveAs(iconPath);
			// int num =
			// su.save(this.getServletConfig().getServletContext().getRealPath("/style/user/user_icon/"));
			// System.out.println("已上传文件" + num + "个.");
		} catch (SmartUploadException e) {
			e.printStackTrace();
			throw new RuntimeException("上传头像失败");
		}

		ImageUtil.resize(80, new java.io.File(iconPath), new java.io.File(iconPath), 1);
		dao.setUserIcon(userInfo.getUserName() + '.' + icon.getFileExt(), userInfo.getUserName());
	}
}
