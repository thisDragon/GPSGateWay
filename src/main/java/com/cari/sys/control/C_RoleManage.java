package com.cari.sys.control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.cari.rbac.Role;
import com.cari.rbac.RoleManage;
import com.cari.sql.DBKeyCreator;
import com.cari.sql.hibernate.NHibernateUtil;
import com.cari.sys.biz.SystemConstant;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.ServletUtil;

public class C_RoleManage extends HttpServlet {

	/**
	 * modify by zhuoshiyao@2007.8.22
	 */
	private static final long serialVersionUID = 3071388973166197259L;
	
	private RoleManage rm;

	/**
	 * Constructor of the object.
	 */
	public C_RoleManage() {
		super();
		rm = RoleManage.getInstance();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requestProcess(request , response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requestProcess(request , response);
	}

	
	private void requestProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("act");
		
		if("LIST".equals(action)){
			List roles = rm.findAllRoles();
			request.setAttribute("ROLEMANAGE:LIST" , roles);
			request.getRequestDispatcher("/manage/sys/roleTree.jsp").forward(request, response);
			return;
			
		}else if("SHOWDETAIL".equals(action)){
			String role_id = HttpParamCaster.getParameter(request, "role_id");
			try {
				if (role_id == null){
					throw new Exception("Need role id parameter.");
				}
				Role role = rm.getRoleById(role_id);
				request.setAttribute("ROLEMANAGE:ROLE" , role);
				request.getSession().setAttribute(SystemConstant.SESSION_ROLE , role);
				request.getRequestDispatcher("/manage/sys/role.jsp?preop=SHOWDETAIL").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				//重定向到出错页面
				request.setAttribute("ERROR_MESSAGE" , "无法获得系统用户角色信息");
				request.getRequestDispatcher("/manage/error.jsp").forward(request, response);
			}
			return;

		}else if("ADD".equals(action)){
			Role role = new Role();
			role.setRole_id(DBKeyCreator.getRandomKey(12));
			long orderNo = rm.getNextRoleOrder();
			role.setRole_orderNo((int)orderNo);
			
			request.setAttribute("ROLEMANAGE:ROLE" , role);
			request.getSession().setAttribute(SystemConstant.SESSION_ROLE , role);
			request.getRequestDispatcher("/manage/sys/role.jsp?preop=ADD").forward(request,response);
			return;
			
		}else if("SAVEORUPDATE".equals(action)){
			String preop = HttpParamCaster.getParameter(request, "preop");
			Role role = (Role)request.getSession().getAttribute(SystemConstant.SESSION_ROLE);
			if(role == null || preop == null) {
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "所操作的角色不存在！"));
				return;
			}
			String role_name = HttpParamCaster.getParameter(request , "role_name");
			String role_desc = HttpParamCaster.getParameter(request , "role_desc");
			try {
				Session session = NHibernateUtil.openSession();
				NHibernateUtil.beginTransaction();
				// 判断角色名称是否重复:名称有变动，并且存在新名称的角色
				rm.checkRoleName(role_name, session,role.getRole_name());
				
				role.setRole_name(role_name);
				role.setRole_desc(role_desc);
				if ("SHOWDETAIL".equals(preop)) {
					//保存修改的信息
					session.update(role);
		            ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "保存成功！"));
				} else if("ADD".equals(preop)) {
					session.save(role);
					ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "保存成功！"));
				} else{
					ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false,"未知的操作指令！"));
				}
				
				NHibernateUtil.commitTransaction();
			} catch (BeRefuseException e) {
				NHibernateUtil.rollbackTransaction();
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, e.getMessage()));
			} catch (Exception e) {
				NHibernateUtil.rollbackTransaction();
				e.printStackTrace();
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "保存失败！"));
			} finally {
				NHibernateUtil.closeSession();
			}
		} else if("DELETE".equals(action)) {
			String id = HttpParamCaster.getParameter(request, "role_id");
			Role role = null;
			try {
				if (id == null) {
					throw new BeRefuseException("Need role id parameter.");
				}
				//删除角色之前，先将所有正在使用本角色的用户的此角色删除
				Session session = NHibernateUtil.openSession();
				NHibernateUtil.beginTransaction();
				
				rm.deleteUserRoleById(id, session);
				role = (Role)session.get(Role.class ,id);
				session.delete(role);
				
				NHibernateUtil.commitTransaction();
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "删除成功！"));
			} catch (BeRefuseException e) {
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, e.getMessage()));
			} catch (Exception e) {
				NHibernateUtil.rollbackTransaction();
				e.printStackTrace();
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "删除失败！"));
			} finally {
				NHibernateUtil.closeSession();
			}
		}
	}
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
