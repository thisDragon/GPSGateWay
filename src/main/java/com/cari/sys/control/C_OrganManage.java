/*
 * 创建日期 May 6, 2006
 *
 */
package com.cari.sys.control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cari.sys.bean.Organ;
import com.cari.sys.biz.OrganManage;
import com.cari.sys.biz.SystemConstant;
import com.cari.sys.biz.UserManage;
import com.cari.web.util.DataFormater;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.ServletUtil;


public class C_OrganManage extends HttpServlet {

	private UserManage um;
	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = 8584759097243229878L;

	
	private OrganManage om;
	/**
	 * Constructor of the object.
	 */
	public C_OrganManage() {
		super();
		om = OrganManage.getInstance();
		um = UserManage.getInstance();
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
		response.setContentType("text/html");	
		String action = request.getParameter("act");
		
		if("LOADORGS".equals(action)){
			List orgsList = om.findChildOrgans(Organ.ROOT_ORGAN.getOrganID() , true);
			request.setAttribute("ORGANMANAGE:ORGANSLIST" , orgsList);
			request.getRequestDispatcher("/manage/sys/organTree.jsp").forward(request,response);
			
		}else if("SHOWORGANDETAIL".equals(action)){
			String organID = HttpParamCaster.getParameter(request, "organID");
			if(organID != null){
				Organ organ = om.getOrganByID(organID);
				Organ parentOrgan = om.getParentOrgan(organ);
				request.setAttribute("ORGANMANAGE:ORGANENTITY" , organ);
				request.getSession().setAttribute(SystemConstant.SESSION_ORGANENTITY , organ);
				request.setAttribute("ORGANMANAGE:PARENTORGAN" , parentOrgan);
				request.getRequestDispatcher("/manage/sys/organ.jsp?preop=SHOWORGANDETAIL").forward(request,response);
			}
			
		}else if("ADDCHILD".equals(action)){
			String organID = HttpParamCaster.getParameter(request, "organID");
			if(organID != null){
				Organ parentOrgan = om.getOrganByID(organID);
				int childCount = om.getChildrenCount(organID);
				Organ organ = new Organ();
				organ.setOrganLevel(parentOrgan.getOrganLevel() + 1);
				organ.setOrganSort(childCount + 1);
				organ.setOrganCode(parentOrgan.getOrganCode());
				if(organID.equals(Organ.ROOT_ORGAN.getOrganID())){
					organ.setOrganID("");
				}else{
					organ.setOrganID(organID + "-" +DataFormater.intToString(childCount + 1,4));
				}
				request.setAttribute("ORGANMANAGE:ORGANENTITY" , organ);
				request.getSession().setAttribute(SystemConstant.SESSION_ORGANENTITY , organ);
				request.setAttribute("ORGANMANAGE:PARENTORGAN" , parentOrgan);
				request.getRequestDispatcher("/manage/sys/organ.jsp?preop=ADDCHILD").forward(request,response);
			}
			
		}else if("SAVEORUPDATE".equals(action)){
			String preop = HttpParamCaster.getParameter(request, "preop");
			
			Organ organ = (Organ)request.getSession().getAttribute(SystemConstant.SESSION_ORGANENTITY);
			if(organ != null){
				if(organ.getOrganID().equals("") && organ.getOrganCode().equals("")){
					String organCode = HttpParamCaster.getParameter(request ,"organCode");
					organ.setOrganCode(organCode);
					organ.setOrganID(organCode);
				}
				String organName = HttpParamCaster.getParameter(request ,"organName");
				organ.setOrganName(organName);
				String organDesc = HttpParamCaster.getParameter(request ,"organDesc");
				organ.setOrganDesc(organDesc);
				
				if("SHOWORGANDETAIL".equals(preop)){
					//保存修改的信息
					try {
						om.update(organ);
			            ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true, "保存成功！"));
					} catch (Exception e) {
						e.printStackTrace();
			            ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "保存失败！"));
					}
				}else if("ADDCHILD".equals(preop)){
					try {
						om.save(organ);
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "保存成功！"));
					} catch (Exception e) {
						e.printStackTrace();
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "保存失败！"));
					}
				}
			}
		}else if("DELETE".equals(action)){
			Organ organ = (Organ)request.getSession().getAttribute(SystemConstant.SESSION_ORGANENTITY);
			if(organ != null){
				if(organ == Organ.ROOT_ORGAN){
					ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "无法删除根结点！"));
				}else{
					try {
						om.deleteOrgan(organ);						
						um.deleteByOrgan(organ);
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "删除成功！"));
					} catch (Exception e) {
						e.printStackTrace();
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "删除失败！"));
					}
				}
			}else{
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "目标不存在！"));
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
