package com.cari.wing.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cari.web.exception.BeRefuseException;
import com.cari.web.exception.UserNotExistException;
import com.cari.web.util.CodeImage;
import com.cari.web.util.HttpParamCaster;
import com.cari.sys.bean.SysUser;
import com.cari.sys.biz.UserManage;

/**
 * @author richfans
 * 
 */
public class C_LoginManage extends HttpServlet {

	private static final long serialVersionUID = -4587327205268883539L;

	private UserManage um;
	public void init() throws ServletException {
		um = UserManage.getInstance();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("act");
        response.setContentType("text/html;charset=UTF-8");
        if (action == null) {
            request.getRequestDispatcher("/login.jsp").forward(request,response);
        } else if("LOGIN".equalsIgnoreCase(action)) {        	
        	String userId = HttpParamCaster.getParameter(request, "username");
            String password = HttpParamCaster.getParameter(request, "password");
            String subCode = HttpParamCaster.getParameter(request, "code");
            String sesCode = (String)request.getSession().getAttribute("rand");
            
            try {
            	if (subCode == null || !subCode.equals(sesCode)) {
            		throw new BeRefuseException("认证码错误！");
            	}
            	SysUser user = um.getLoninUser(userId, password);
            	request.getSession().setAttribute("LOGINUSER", user);
            	response.sendRedirect("manage/index.jsp");
            	
            } catch (UserNotExistException e) {
            	request.setAttribute("ERRORINFO", "用户不存在或密码错误！");
            	request.getRequestDispatcher("/login.jsp").forward(request,response);
            } catch (Exception e) {
            	request.setAttribute("ERRORINFO", "登录失败！原因：" + e.getMessage());
            	request.getRequestDispatcher("/login.jsp").forward(request,response);
            }
        } else if("LOGOUT".equalsIgnoreCase(action)) {
            request.getSession().invalidate();
            response.sendRedirect("login.jsp");
            
        } else if("CODEIMG".equalsIgnoreCase(action)){
    		response.setContentType(CodeImage.CONTENT_TYPE);
    		//设置页面不缓存
    		response.setHeader("Pragma","No-cache");
    		response.setHeader("Cache-Control","no-cache");
    		response.setDateHeader("Expires", 0);
			OutputStream out = response.getOutputStream();
        	
        	CodeImage cImg = new CodeImage();
        	BufferedImage bufImg = cImg.getImage();
			request.getSession().setAttribute("rand", cImg.getRandValue());
			CodeImage.outPut(out, bufImg);
        }
	}
}