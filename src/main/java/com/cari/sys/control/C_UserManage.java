/*
 * 创建日期 May 21, 2006
 *
 */
package com.cari.sys.control;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.cari.web.comm.ListPage;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.ServletUtil;

import com.cari.rbac.Role;
import com.cari.rbac.RoleManage;
import com.cari.sql.hibernate.QueryCondition;
import com.cari.sys.bean.Organ;
import com.cari.sys.bean.SysUser;
import com.cari.sys.biz.OrganManage;
import com.cari.sys.biz.SystemConstant;
import com.cari.sys.biz.UserManage;
import com.cari.sys.biz.UserQueryCondition;

public class C_UserManage extends HttpServlet {

	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = -3762273626217002603L;

	private OrganManage om;
	private UserManage um;
	private RoleManage rm;
	/**
	 * Constructor of the object.
	 */
	public C_UserManage() {
		super();
		om = OrganManage.getInstance();
		um = UserManage.getInstance();
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
		SysUser myInfo = (SysUser)request.getSession().getAttribute("LOGINUSER");
		
		if("LOADORGS".equals(action)){			
			List orgsList = om.findChildOrgans(Organ.ROOT_ORGAN.getOrganID() , true);
			request.setAttribute("ORGANMANAGE:ORGANSLIST" , orgsList);
			request.getRequestDispatcher("/manage/sys/userTree.jsp").forward(request,response);
			return;
		}else if("CHECK".equals(action)){
			String userId = HttpParamCaster.getParameter(request, "userId");
            boolean result = um.checkRegistedUserID(userId);
            if(result){
                ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "工号：" + userId + "未被注册，可以使用！"));
            }else{
            	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "<font color='red'>工号：" + userId + "已经被注册！</font>"));
            }
            
		}else if("GETNOID".equals(action)){
			String userId = um.getNumberUserID();
            if(userId != null){
            	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , userId));
            }else{
            	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "<font color='red'>生成流水工号失败</font>"));
            }
	        return;
		}else if("ADD".equals(action)){
			String dept = HttpParamCaster.getParameter(request, "dept");
			SysUser user = new SysUser();
			user.setDept(dept);
			user.setPassword(SystemConstant.USERMANAGE_DEFAULT_PASSWORD);
			user.getRoles().add(Role.SYS_BASE_ROLE);
						
			List roles = rm.findAllRoles();
			request.setAttribute("USERMANAGE:USER" , user);
			request.setAttribute("ROLEMANAGE:LIST" , roles);
			request.getSession().setAttribute(SystemConstant.SESSION_USER , user);
			request.getRequestDispatcher("/manage/sys/user.jsp?preop=ADD").forward(request,response);
			return;
		}else if("MODIFYMYINFO".equals(action)){
			request.setAttribute("USERMANAGE:USER" , myInfo);
			request.getSession().setAttribute(SystemConstant.SESSION_USER , myInfo);
			request.getRequestDispatcher("/manage/user/userinfo.jsp?preop=SHOWDETAIL").forward(request,response);
			return;
			
		}else if("SHOWDETAIL".equals(action)){
			String userId = HttpParamCaster.getParameter(request, "userId");
			if(userId != null){
				SysUser user = um.getUserById(userId);
				
				List roles = rm.findAllRoles();

				request.setAttribute("USERMANAGE:USER" , user);
				request.setAttribute("ROLEMANAGE:LIST" , roles);
				request.getSession().setAttribute(SystemConstant.SESSION_USER , user);
				request.getRequestDispatcher("/manage/sys/user.jsp?preop=SHOWDETAIL").forward(request,response);
				return;
			}
		}else if("TOQUERY".equals(action)){
			request.getRequestDispatcher("/manage/sys/userQuery.jsp").forward(request,response);
			return;
			
		}else if("QUERY".equals(action)){
			int pageNo = HttpParamCaster.getIntParameter(request , "pageno", 1);
            String isNewQuery = HttpParamCaster.getParameter(request, "isNewQuery");
            if(isNewQuery != null && !isNewQuery.equals("")){
            	//不是新检索,通常是翻页检索或返回列表操作
                request.getSession().removeAttribute(SystemConstant.SESSION_USERQUERYCONDITION);
            }
            QueryCondition qc = (QueryCondition)request.getSession().getAttribute(SystemConstant.SESSION_USERQUERYCONDITION);
            if(qc == null){
	            qc = new UserQueryCondition();
	            Map parameter = request.getParameterMap();
	            qc.setParameters(parameter);
	            request.getSession().setAttribute(SystemConstant.SESSION_USERQUERYCONDITION , qc);
            }
            ListPage userPage = um.queryPage(pageNo , qc);
            request.setAttribute("USERMANAGE:LIST" , userPage);
            request.getRequestDispatcher("/manage/sys/userList.jsp").forward(request,response);
            return;
            
		}else if("SAVEORUPDATE".equals(action)){
			String preop = HttpParamCaster.getParameter(request, "preop");
			
			SysUser user = (SysUser)request.getSession().getAttribute(SystemConstant.SESSION_USER);
			if(user != null){
				String userName = HttpParamCaster.getParameter(request ,"userName");
				user.setUserName(userName);
				String duty = HttpParamCaster.getParameter(request ,"duty");
				user.setDuty(duty);
				String postCode = HttpParamCaster.getParameter(request ,"postCode");
				user.setPostCode(postCode);
				String address = HttpParamCaster.getParameter(request ,"address");
				user.setAddress(address);
				String sex = HttpParamCaster.getParameter(request ,"sex");
				user.setSex(sex);
				String email = HttpParamCaster.getParameter(request ,"email");
				user.setEmail(email);
				String tel = HttpParamCaster.getParameter(request ,"tel");
				user.setTel(tel);
				String mobile = HttpParamCaster.getParameter(request ,"mobile");
				user.setMobile(mobile);
				String remark = HttpParamCaster.getParameter(request ,"remark");
				user.setRemark(remark);
				
				String dept = HttpParamCaster.getParameter(request ,"dept");
				if(dept!=null&&!dept.equals(""))
				user.setDept(dept);
				user.setRegDate(new Timestamp(System.currentTimeMillis()));
				user.setNation("中国");
				
				String sRoleIds = HttpParamCaster.getParameter(request ,"roles");
				if (sRoleIds == null){
					//如果前台没有编辑角色，那么其内容为null，相应的roles不要修改
					//结果：保持原样，什么都不动
				}else if ("".equals(sRoleIds)){
					//前台把角色清空了
					user.getRoles().clear();
				}else{
					String[] asRole = sRoleIds.split(",");
					
					Role role = null;
					user.getRoles().clear();
					for (int i=0;i<asRole.length;i++){
						role = rm.getRoleById(asRole[i]);
						user.getRoles().add(role);
					}
				}
				if("SHOWDETAIL".equals(preop)){
					//保存修改的信息
					if(um.update(user)){
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "保存成功！"));
					}else {
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "保存失败！"));
					}
				}else if("ADD".equals(preop)){
					String userId = HttpParamCaster.getParameter(request ,"userId");
					user.setUserId(userId);
					//目前工号和用户名统一
					user.setCode(userId);
					if(um.save(user)){
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "保存成功！"));
					}else {
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "保存失败！"));
					}
				}
			}
			return;
			
		}else if("RESETPWD".equals(action)){
			String  userID = HttpParamCaster.getParameter(request , "userId");
            try {
                um.modifyPassword(userID, SystemConstant.USERMANAGE_DEFAULT_PASSWORD);
                ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "密码重置成功"));
            } catch(Exception e) {
            	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "密码重置失败"));
            }
            return;
            
		}else if("TOMODIFYPWD".equals(action)){
            request.getRequestDispatcher("/manage/user/password.jsp").forward(request,response);
            return;
		}else if("MODIFYPWD".equals(action)){
			String userId = myInfo.getUserId();
            String password = HttpParamCaster.getParameter(request, "password");
            String newpassword = HttpParamCaster.getParameter(request, "newpassword");
            boolean  isSuccess = um.authenticate(userId, password);
            if(isSuccess) {
                try {
	                um.modifyPassword(userId, newpassword);
	                ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "口令修改成功"));
                } catch(Exception e) {
                	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "操作失败"));
                }
            } else {
            	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "旧口令不正确"));
            }
    		return;
		}else if("DELETE".equals(action)){
			String[] userIds = request.getParameterValues("userId");
			if(userIds != null && userIds.length > 0){
				if(um.delete(userIds)){
					ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "删除成功！"));
				} else {
					ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "删除失败！"));
				}
			}
			return;
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
