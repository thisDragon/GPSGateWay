package com.cari.wing.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cari.rbac.RBACPermission;
import com.cari.sys.bean.SysUser;
import com.cari.sys.biz.SystemConstant;

/**
 * @author richfans
 * 系统用户身份验证过滤器
 */
public class AuthenticateFilter extends HttpServlet implements Filter {

	private static final long serialVersionUID = -6565547181231850546L;
	private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;
		try {
			SysUser user = (SysUser) httpReq.getSession().getAttribute("LOGINUSER");
			String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":"
				+ request.getServerPort() + httpReq.getContextPath();
			//未验证
			if (user == null) {
				httpResp.sendRedirect(basePath + "/overtime.jsp");
				return;
			}
			
			//未授权
			String reqStr = httpReq.getServletPath()+"?"+httpReq.getQueryString();
			String module_id = (String)SystemConstant.URL_MOD_MAPPING.get(reqStr);
			if(module_id != null && !RBACPermission.checkPermission(user.getRoles() , module_id)){
				httpResp.sendRedirect(basePath + "/overtime.jsp");
				return;
			}
			filterChain.doFilter(httpReq, httpResp);
		} catch (ServletException sx) {
			sx.printStackTrace();
			filterConfig.getServletContext().log(sx.getMessage());
		} catch (IOException iox) {
			iox.printStackTrace();
			filterConfig.getServletContext().log(iox.getMessage());
		}
	}

	/*
	 * 
	 * （非 Javadoc） //进行的sso用户验证 //Step 1 获取用户接口编号, //Step 2 从数据库获取接口对应的返回式验证的URL
	 * String ssoURL = "http://192.168.1.27/richnet/fjii/checkurl/checkurl.asp";
	 * SSOVerifer verifer = SSOVerifer.getInstance(); boolean ssoPass =
	 * verifer.doSSOVerify(httpReq,"RichMap" , ssoURL); if (ssoPass) {
	 * System.out.println("SSOVerifer is ok!"); try { Principal userPrincipal =
	 * verifer.getUserPrincipal(); SysUserInfo ssoUser = new SysUserInfo();
	 * ssoUser.setUserID(userPrincipal.getName()); //设置接口编号
	 * ssoUser.setCompanyID() ssoUser.setUserType("SSOUSER");
	 * httpReq.getSession().setAttribute(SystemConstant.USERSESSION, ssoUser);
	 * //boolean f = SysParamLoader.initialParams(conn); } catch
	 * (IllegalSSOStateException ssoe) {
	 * System.out.println("verifer.getUserPrincipal() Exception : " + ssoe); } }
	 * else { System.out.println("You have not pemission to logon Archive Manage
	 * System!"); //httpResp.sendRedirect("system/adminlogin.jsp"); return; }
	 * 
	 * 
	 */

	// Clean up resources
	public void destroy() {}
}