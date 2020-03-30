package com.cari.sys.control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cari.rbac.Operation;
import com.cari.rbac.OperationManage;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.util.ServletUtil;


public class C_OperationManage extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7667075691436323708L;
	private OperationManage om;
	/**
	 * Constructor of the object.
	 */
	public C_OperationManage() {
		super();
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

		doPost(request, response);
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
		response.setContentType("text/html");
        String action = request.getParameter("act");
        if ("addOperation".equalsIgnoreCase(action)) {
        	//保存模块操作
        	String id = request.getParameter("id");
        	String name = request.getParameter("name");
        	Operation o = new Operation();
        	o.setOperate_id(id);
        	o.setOperate_name(name);    
        	String str = "保存成功！";
        	boolean success = false;
        	try {
        		if (name == null || name.equals("")) {
					throw new BeRefuseException("请输入操作名称！");
				} else if (id == null || id.equals("")) {
					throw new BeRefuseException("请输入操作主键！");
				} 
				om.saveOperation(o);
				success = true;
			} catch (BeRefuseException e) {
				// TODO Auto-generated catch block				
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
			ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        } else if ("delOperation".equalsIgnoreCase(action)) {
        	String[] id = request.getParameterValues("moduleid");
        	String str = "删除成功！";
        	boolean success = false;
        	try {
				if (id == null || id.length < 1) {
					throw new BeRefuseException("请选择要删除的模块");
				}
				om.delOperation(id);
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
        } else if ("modOperation".equalsIgnoreCase(action)) {        	
        	String name = request.getParameter("name");
        	String id = request.getParameter("id");
        	String str = "更新成功！";
        	boolean success = false;
        	try {
				if (name == null || name.equals("")) {
					throw new BeRefuseException("请输入操作名称！");
				} else if (id == null || id.equals("")) {
					throw new BeRefuseException("请输入操作主键！");
				}
				om.updateOperation(name, id);
				success = true;
        	} catch (BeRefuseException e) {
				// TODO Auto-generated catch block				
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
			ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        }
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
