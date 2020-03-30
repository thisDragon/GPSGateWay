/*
 * 创建日期 May 21, 2006
 * 编码日期 2006.6.20
 * linly @ caripower
 */
package com.cari.sys.control;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cari.rbac.RoleModuleRightManage;
import com.cari.rbac.RoleManage;
import com.cari.rbac.Module;
import com.cari.rbac.ModuleManage;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.sys.bean.SysUser;
import com.cari.sys.biz.SystemConstant;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.ServletUtil;

public class C_RightManage extends HttpServlet {

	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = 5206939778374380816L;
	
	private RoleModuleRightManage mrm;
	private RoleManage rm;
	private ModuleManage smm;
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		mrm = RoleModuleRightManage.getInstance();
		rm = RoleManage.getInstance();
		smm = ModuleManage.getInstance();
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

		requestProcess(request,response);

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
		
		requestProcess(request,response);
		
	}

	
	private void requestProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SysUser user = (SysUser)request.getSession().getAttribute("LOGINUSER");
		String action = request.getParameter("act");
		//获取角色list

		if ("ROLELIST".equals(action)){
			List roles = rm.findAllRoles();
			request.setAttribute("RIGHTMANAGE:ROLELIST" , roles);
			request.getRequestDispatcher("/manage/sys/rightTree.jsp").forward(request, response);
			return;
			
		}else if("SHOWDETAIL".equals(action)){
			List moduleList = smm.findChildModules(Module.ROOT.getModuleID());
			request.setAttribute("RIGHTMANAGE:MODULELIST" , moduleList);
			request.getRequestDispatcher("/manage/sys/right.jsp").forward(request, response);
			return;
			
		}else if("GETMODULEOPS".equals(action)){
			String role_id = HttpParamCaster.getParameter(request, "role_id");
			String module_id = HttpParamCaster.getParameter(request, "module_id");
			if (module_id != null) {
				Session session = HibernateUtil.getInstance().openSession();
				Set operations = null;
				Integer rightValue = null;
				try {
					//获取模块内的操作列表
					Module theModule = (Module)session.load(Module.class , module_id);
					operations = theModule.getOperations();
					operations.size();
					//获取当前角色的特定模块权值
					rightValue = mrm.getModuleRightValue(role_id, module_id, session);
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					session.close();
				}

				request.setAttribute("RIGHTMANAGE:MODULE" , operations);
				request.setAttribute("RIGHTMANAGE:ROLEMODULERIGHT" , rightValue);
				request.getRequestDispatcher("/manage/sys/operations.jsp").forward(request, response);
				return;
			}
		}else if("SETMODULERIGHTS".equals(action)){//修改角色对一个模块的操作权限
			String role_id = HttpParamCaster.getParameter(request, "role_id");
			String rights = HttpParamCaster.getParameter(request, "rights");

			Session session = HibernateUtil.getInstance().openSession();
			Transaction tx = null;
			try {
				if (role_id == null || rights == null) {
					throw new BeRefuseException("未更改设置");
				} else if (rights == null) {
					throw new BeRefuseException("参数不正确");
				}
				String[] arModuleRight = rights.split("#");
				if (arModuleRight.length < 1 ) {
					throw new BeRefuseException("参数不正确");
				}
				
				tx = session.beginTransaction();
				//通常情况下,多样化的权限
				
				int i = 0;
				if (SystemConstant.ALLRIGHTS.equals(arModuleRight[0])) {
					//设置超级权限
					mrm.createSuperRight(role_id, session,user);
					i = 1;
				} else if(SystemConstant.NULLRIGHTS.equals(arModuleRight[0])) {
					mrm.clearModuleRightByRole(role_id, session);
					i = 1;
				}
					
				for( ; i < arModuleRight.length ; i+=1){
					String[] tmp = arModuleRight[i].split("\\.");
					if (tmp.length < 2) {
						throw new BeRefuseException("参数不正确");
					}
					mrm.updateModuleRight(role_id, tmp[0], Integer.parseInt(tmp[1]), session);
				}
				
				tx.commit();
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "保存成功！"));
			} catch (BeRefuseException e) {
				if (tx != null) {
					tx.rollback();
				}
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , e.getMessage()));
			} catch (Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				e.printStackTrace();
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "保存失败"));
				
			}
		}
	}
}
