package com.cari.sys.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cari.rbac.ModuleManage;
import com.cari.rbac.OperationManage;
import com.cari.sys.biz.SystemConstant;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.MsgXMLBuilder;
import com.cari.web.util.ServletUtil;

/**
 * 用于控制后台管理系统各二级菜单的入口。（包含模块操作）
 * @author Leidg.cn
 * @version 1.0, 2008-05-27
 * @see com.cari.sys.biz.SystemConstant
 */

public class C_ModuleManage extends HttpServlet {
	
	private static final Logger logger = LoggerFactory.getLogger(C_ModuleManage.class);
	private static final long serialVersionUID = 1L;
	
	private ModuleManage mm;
	private OperationManage om;
	/**
	 * Constructor of the object.
	 */
	public C_ModuleManage() {
		super();
		mm = ModuleManage.getInstance();
		om = OperationManage.getInstance();
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
		doPost(request,response);
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
		String action = request.getParameter("act");
		if ("sys-dict".equalsIgnoreCase(action)) {   
			//系统字典管理
        	request.getRequestDispatcher("/manage/sys/sysmodule.jsp").forward(request,response);				
		}else if ("sys-cfg".equalsIgnoreCase(action)){
			//系统参数管理
			request.getRequestDispatcher("/manage/sys/configs.jsp").forward(request, response);
		}else if ("sys-area".equalsIgnoreCase(action)){
			//区域管理
			request.getRequestDispatcher("/manage/sys/regions.jsp").forward(request, response);
		}else if ("sys-organ".equalsIgnoreCase(action)){
			//组织机构管理
			request.getRequestDispatcher("/manage/sys/organs.jsp").forward(request, response);
		}else if ("sys-role".equalsIgnoreCase(action)){
			//角色管理
			request.getRequestDispatcher("/manage/sys/roles.jsp").forward(request, response);
		}else if ("sys-user".equalsIgnoreCase(action)){
			//用户管理
			request.getRequestDispatcher("/manage/sys/users.jsp").forward(request, response);
		}else if ("sys-right".equalsIgnoreCase(action)){
			//权限配置
			request.getRequestDispatcher("/manage/sys/rights.jsp").forward(request, response);
		}else if ("userinfo-info".equalsIgnoreCase(action)){
			//用户个人资料维护
			request.getRequestDispatcher("/manage/user?act=MODIFYMYINFO").forward(request, response);
		}else if ("userinfo-pwd".equalsIgnoreCase(action)){
			//用户修改密码
			request.getRequestDispatcher("/manage/user/password.jsp").forward(request, response);
		} else if ("sys-log".equalsIgnoreCase(action)) {
			//系统日志
			request.getRequestDispatcher("/manage/sys/syslog.jsp").forward(request, response);
		} else if ("datasource-config".equalsIgnoreCase(action)) {
			//数据源配置
			request.getRequestDispatcher("/manage/datasource/config.jsp").forward(request, response);
		} else if ("datasource-forward".equalsIgnoreCase(action)) {
			//转发订阅
			request.getRequestDispatcher("/manage/datasource/forward.jsp").forward(request, response);
		} else if ("datasource-log".equalsIgnoreCase(action)) {
			//数据日志
			request.getRequestDispatcher("/manage/datasource/log.jsp").forward(request, response);
		} else if ("addModule".equalsIgnoreCase(action)) {        	
			// 添加模块
        	String name = request.getParameter( "name");
        	String id = request.getParameter("id");        	
        	String moduleUrl = HttpParamCaster.getParameter(request, "url", "");
        	int moduleSort = HttpParamCaster.getIntParameter(request, "sort", 0);        	
        	String str = "保存成功！";
        	boolean success = false;
        	try {        		
				if (name == null || name.equals("")) {
					throw new BeRefuseException("请输入模块名称！");
				} else if (id == null || id.equals("")) {
					throw new BeRefuseException("请输入模块key值！");
				} else if (moduleSort == 0) {
					throw new BeRefuseException("请输入模块序号！");
				} else if (mm.getLevel(id) > 1 && (moduleUrl == null || moduleUrl.equals(""))) {
					throw new BeRefuseException("请输入模块对应的url地址！");
				}												
				id = id.toUpperCase();				
				mm.saveModule(id, name, moduleSort, moduleUrl);
//				moduleUrl = moduleUrl.replaceAll("=", "/=");
				HashMap map = SystemConstant.URL_MOD_MAPPING;
	        	map.put(id, moduleUrl);
	        	map.put(moduleUrl.replace("..", ""), id);
				success = true;
			} catch (BeRefuseException e) {
				// TODO Auto-generated catch block				
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
//        	System.out.println("id = " + id + "\t" + "name = " + name + "\t" + "parentid = " + parentid );
			System.out.println(ServletUtil.createStandardResponse(success, str));
        	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        } else if ("reloadModule".equalsIgnoreCase(action)) {
        	//重新加载模块com.cari.wind.biz.RBACUtil
//        	request.getSession().removeAttribute("MENU_FOR_MYROLE");
//        	this.getServletContext().removeAttribute("OPERATION_LIST");
        	List list = om.getOperateList();
        	this.getServletContext().removeAttribute("OPERATION_LIST");
        	this.getServletContext().setAttribute("OPERATION_LIST", list);
        	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "重载成功！"));
        } else if ("delModule".equalsIgnoreCase(action)) {
        	String[] id = request.getParameterValues("moduleid");
        	String str = "删除成功！";
        	boolean success = false;
        	try {
				if (id == null || id.length < 1) {
					throw new BeRefuseException("请选择要删除的模块");
				}
				mm.delModule(id);
				success = true;
			} catch (BeRefuseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
			ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        } else if ("modModule".equalsIgnoreCase(action)) {
        	String name = request.getParameter( "name");
        	String id = request.getParameter("id");        	
        	String moduleUrl = HttpParamCaster.getParameter(request, "url", "");
        	int moduleSort = HttpParamCaster.getIntParameter(request, "sort", 0);        	
        	String str = "修改成功！";
        	boolean success = false;
        	try {        		
				if (name == null || name.equals("")) {
					throw new BeRefuseException("请输入模块名称！");
				} else if (id == null || id.equals("")) {
					throw new BeRefuseException("请输入模块key值！");
				} else if (moduleSort == 0) {
					throw new BeRefuseException("请输入模块序号！");
				} else if (mm.getLevel(id) > 1 && (moduleUrl == null || moduleUrl.equals(""))) {
					throw new BeRefuseException("请输入模块对应的url地址！");
				}												
				id = id.toUpperCase();				
				mm.updateModule(id, name, moduleSort, moduleUrl);
				HashMap map = SystemConstant.URL_MOD_MAPPING;
//				moduleUrl = moduleUrl.replaceAll("=", "/=");
				map.remove(id);
	        	map.put(id, moduleUrl);
	        	map.remove(moduleUrl);
	        	map.put(moduleUrl.replace("..", ""), id);
				success = true;
			} catch (BeRefuseException e) {
				// TODO Auto-generated catch block				
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}        	
        	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        } else if ("addModuleOperation".equalsIgnoreCase(action)) {
        	//修改模块的权限
        	String[] operationId = request.getParameterValues("operationid");
        	String moduleId = request.getParameter("module_id");
        	String str = "修改成功！";
        	boolean success = false;
        	try {
				if (moduleId == null || moduleId.equals("")) {
					throw new BeRefuseException("请选择模块！");
				}
				mm.addModuleOperation(moduleId, operationId);
			} catch (BeRefuseException e) {
				// TODO Auto-generated catch block
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
			ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        } else if ("moduleOperaiton".equalsIgnoreCase(action)) {
        	//得到一个模块的权限
        	String moduleId = request.getParameter("id");
        	List list = om.getOperationByModuleId(moduleId);
        	ServletUtil.outputXML(response, MsgXMLBuilder.buildModuleOperation(list));
			
		}else{
			System.out.println("试图进入未知的模块" + action + "，失败...");
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
	
	public static void main(String[] args){
		System.out.println("haha");
	}
}
