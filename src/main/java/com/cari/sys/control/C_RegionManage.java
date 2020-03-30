package com.cari.sys.control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cari.sql.DBKeyCreator;
import com.cari.sys.bean.Region;
import com.cari.sys.biz.RegionManage;
import com.cari.sys.biz.SystemConstant;
import com.cari.web.util.*;
import java.util.List;

/**
 * 类描述：用于支持后台管理系统中的区域配置管理模块，实现与数据库的交互。
 * 具备以下接口：
 * 		1）读取区域配置列表:GETREGIONS
 * 		2）编辑区域配置信息:EDITREGION
 * 		3）保存区域配置信息：SAVEREGION
 * 		4）删除区域配置信息：DELETEREGION
 * 		
 * 用法：
 * 		作为servlet调用，关键字为?act=,参数值如上述。
 * 
 * 返回：
 * 		各函数具备自己的参数，但是要求返回一致的XML格式：
 * 		1、返回正确格式的XML
 * 		<root>
 * 			<Return>1</Return>
 * 			<Content>内容</Content>
 * 		</root>
 * 		2、返回错误格式的XML
 * 		<root>
 * 			<Return>0</Return>
 * 			<Error>错误信息</Error>
 * 		</root>
 * 
 * @author Leidg.cn
 * @version 1.0, 2007-03-30
 * @see
 */

public class C_RegionManage extends HttpServlet {
	
	private static final Logger logger = LoggerFactory.getLogger(C_RegionManage.class);
	private static final long serialVersionUID = 1L;
	private RegionManage rm;
	/**
	 * Constructor of the object.
	 */
	public C_RegionManage() {
		super();
		rm = RegionManage.getInstance();
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
		if("LOADREGIONS".equals(action)){
			List regionList = rm.getAllRegions(); 
			request.setAttribute("REGIONMANAGE:REGIONSLIST" , regionList);
			request.getRequestDispatcher("/manage/sys/regionTree.jsp").forward(request,response);
			
		}else if("SHOWDETAIL".equals(action)){
			String dwKey = HttpParamCaster.getParameter(request, "dwKey");
			String fullName = HttpParamCaster.getUTF8Parameter(request , "fullName");
			Region region = null;
			if(fullName != null && !fullName.equals("")){
				region = rm.getRegionByFullName(fullName);
			}else if(dwKey != null){
				region = rm.getRegionById(dwKey);
			}
			request.setAttribute("REGIONMANAGE:REGIONENTITY" , region);
			request.getSession().setAttribute(SystemConstant.SESSION_REGIONENTITY , region);
			request.getRequestDispatcher("/manage/sys/region.jsp?preop=SHOWDETAIL").forward(request,response);
			
		}else if("ADDCHILD".equals(action)){
			String dwKey = HttpParamCaster.getParameter(request, "dwKey");
			if(dwKey != null){
				Region region = rm.getRegionById(dwKey);
				Region childRegion = new Region();
				childRegion.setActive("N");
				if(!dwKey.equals(Region.REGION_ROOT.getDwKey())){
					childRegion.setProvince(region.getProvince());
					childRegion.setCity(region.getCity());
				}
				childRegion.setDwKey(DBKeyCreator.getRandomKey(12));
				//计算子区域的排序号
				String childOrderNo = region.getOrderNo() + Tools.formatNumber(rm.getChildCount(region)+1, "00");
				childRegion.setOrderNo(childOrderNo);
				request.setAttribute("REGIONMANAGE:REGIONENTITY" , childRegion);
				request.getSession().setAttribute(SystemConstant.SESSION_REGIONENTITY , childRegion);
				request.getRequestDispatcher("/manage/sys/region.jsp?preop=ADDCHILD").forward(request,response);
			}
			
		}else if("SAVEORUPDATE".equals(action)){
			//读取URL参数
			String cityCode = request.getParameter("citycode");
			int defaultLayer = HttpParamCaster.getIntParameter(request, "defaultlayer", 10);
			String province = HttpParamCaster.getParameter(request,"province");
			String city = HttpParamCaster.getParameter(request,"city");
			String county = HttpParamCaster.getParameter(request, "county");
			double centerX = HttpParamCaster.getDoubleParameter(request, "centerx", 0.0);
			double centerY = HttpParamCaster.getDoubleParameter(request, "centery", 0.0);
			double left = HttpParamCaster.getDoubleParameter(request, "left", 0.0);
			double right = HttpParamCaster.getDoubleParameter(request, "right", 0.0);
			double top = HttpParamCaster.getDoubleParameter(request, "top", 0.0);
			double bottom = HttpParamCaster.getDoubleParameter(request, "bottom", 0.0);
			String orderNo = request.getParameter("orderno");
			String active = request.getParameter("active");
			
			if (cityCode == null || orderNo==null ||
					left == 0.0 || right == 0.0 || top == 0.0 || bottom == 0.0){
				ServletUtil.outputXML(response, "<root><Return>0</Return><Error>数据不充分。</Error></root>");
				return;
			}
			
			String preop = request.getParameter("preop");
			
			Region region = (Region)request.getSession().getAttribute(SystemConstant.SESSION_REGIONENTITY);
			//判断是否有儿子，如果有儿子则不能修改
			if (rm.getChildCount(region)>0){
				ServletUtil.outputXML(response, "<root><Return>0</Return><Error>存在下级区域，不能进行修改。</Error></root>");
				return;
			}
			
			if(region != null){
				region.setCityCode(cityCode);
				region.setOrderNo(orderNo);
				region.setProvince(province);
				region.setCity(city);
				region.setCounty(county);
				region.setDefaultLayer(defaultLayer);
				region.setCenterX(centerX);
				region.setCenterY(centerY);
				region.setLeft(left);
				region.setRight(right);
				region.setTop(top);
				region.setBottom(bottom);
				region.setActive(active);
				boolean isSuccess = false;
				if ("ADDCHILD".equals(preop)){
					isSuccess = rm.save(region);
				}else{
					isSuccess = rm.update(region);
				}
				if (isSuccess){
					ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "保存成功！"));
				}else{
					ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "保存失败！"));
				}
			}else{
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "保存失败，原因：操作途径不一致！"));
			}
			
		}else if("DELETE".equals(action)){
			Region region = (Region)request.getSession().getAttribute(SystemConstant.SESSION_REGIONENTITY);
			if(region != null){
				if(region == Region.REGION_ROOT){
					ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "无法删除根结点！"));
				}else{
					try {
						rm.delete(region);
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "删除成功！"));
					} catch (Exception e) {
						e.printStackTrace();
						ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "删除失败！"));
					}
				}
			}
				
		}else if("ISEXIST".equals(action)){
			String cityCode = request.getParameter("citycode");
			Region region = rm.getRegionByCityCode(cityCode);
			if(region != null){
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false,"区域代码" + cityCode + "已被使用，请重新命名。"));
			}else{
				ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true,"未重复，可以使用。"));
			}
		}else{
			ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false, "未知的操作指令！"));
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
