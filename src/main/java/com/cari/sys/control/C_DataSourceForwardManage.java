package com.cari.sys.control;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.analog.data.enums.LogStateEnum;
import com.analog.data.enums.LogTypeEnum;
import com.analog.data.util.DateUtils;
import com.analog.data.util.EntityUtils;
import com.cari.rbac.DataSourceForward;
import com.cari.rbac.DataSourceForwardManage;
import com.cari.rbac.LogData;
import com.cari.rbac.LogDataManage;
import com.cari.sys.bean.SysUser;
import com.cari.sys.biz.EventLogManage;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.ServletUtil;

/**
* @ClassName: C_DataSourceForwardManage
* @Description: 转发订阅模块
* @author yangjianlong
* @date 2020年2月11日下午4:07:59
*
 */

public class C_DataSourceForwardManage extends HttpServlet {
	
	private static final Logger logger = LoggerFactory.getLogger(C_DataSourceForwardManage.class);
	private static final long serialVersionUID = 1L;
	
	private DataSourceForwardManage dataSourceForwardManage;
	private LogDataManage logDataManage;
	
	public C_DataSourceForwardManage() {
		super();
		dataSourceForwardManage = DataSourceForwardManage.getInstance();
		logDataManage = LogDataManage.getInstance();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("act");
		SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGINUSER");
		
		if ("toAdd".equalsIgnoreCase(action)) {
			String subscriptionName = request.getParameter( "subscriptionName");
			String forwardUrl = request.getParameter("forwardUrl");
        	String sourceTypes = request.getParameter("sourceTypes");
        	String remark = HttpParamCaster.getParameter(request, "remark", "");
        	String account = HttpParamCaster.getParameter(request, "account", "");
        	String password = HttpParamCaster.getParameter(request, "password", "");
        	int isEnable = HttpParamCaster.getIntParameter(request, "isEnable", 0);
        	String str = "保存成功！";
        	boolean success = false;
        	try {
        		Date now = new Date();
        		DataSourceForward dataSourceForward = new DataSourceForward();
        		dataSourceForward.setId(EntityUtils.createId32());
        		dataSourceForward.setSubscriptionName(subscriptionName);
        		dataSourceForward.setForwardUrl(forwardUrl);
        		dataSourceForward.setSourceType(sourceTypes);
        		dataSourceForward.setRemark(remark);
        		dataSourceForward.setAccount(account);
        		dataSourceForward.setPassword(password);
        		dataSourceForward.setIsEnable(isEnable);
        		dataSourceForward.setCreateUser(loginUser.getUserName());
        		dataSourceForward.setCreateTime(new Timestamp(now.getTime()));
        		dataSourceForward.setModifyUser(loginUser.getUserName());
        		dataSourceForward.setModifyTime(new Timestamp(now.getTime()));
        		
    			if (dataSourceForwardManage.isExist(null,subscriptionName)) {
					throw new BeRefuseException("订阅名称已存在,请检查配置");
				}else{
					dataSourceForwardManage.save(dataSourceForward);
					success = true;
					EventLogManage.saveLog(loginUser.getUserId(), loginUser.getUserName(),"新增转发订阅", "转发订阅", request.getRemoteAddr(),"","成功");
				}
			} catch (BeRefuseException e) {
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
        	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
		} else if ("toDelete".equalsIgnoreCase(action)) {
        	String[] forwardIds = request.getParameterValues("forwardId");
        	String str = "删除成功！";
        	boolean success = false;
        	try {
				if (forwardIds == null || forwardIds.length < 1) {
					throw new BeRefuseException("请选择要删除的记录");
				}
				dataSourceForwardManage.delete(forwardIds);
				success = true;
				EventLogManage.saveLog(loginUser.getUserId(), loginUser.getUserName(),"删除转发订阅", "转发订阅", request.getRemoteAddr(),"","成功");
			} catch (BeRefuseException e) {
				e.printStackTrace();
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
			ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        
		} else if ("toUpdate".equalsIgnoreCase(action)) {
			String forwardId = request.getParameter( "forwardId");
			String subscriptionName = request.getParameter( "subscriptionName");
			String forwardUrl = request.getParameter("forwardUrl");
        	String sourceTypes = request.getParameter("sourceTypes");
        	String remark = HttpParamCaster.getParameter(request, "remark", "");
        	String account = HttpParamCaster.getParameter(request, "account", "");
        	String password = HttpParamCaster.getParameter(request, "password", "");
        	int isEnable = HttpParamCaster.getIntParameter(request, "isEnable", 0);
        	
        	String str = "修改成功！";
        	boolean success = false;
        	try {
        		DataSourceForward dataSourceForward = new DataSourceForward();
        		dataSourceForward.setId(forwardId);
        		dataSourceForward.setSubscriptionName(subscriptionName);
        		dataSourceForward.setForwardUrl(forwardUrl);
        		dataSourceForward.setSourceType(sourceTypes);
        		dataSourceForward.setAccount(account);
        		dataSourceForward.setPassword(password);
        		dataSourceForward.setIsEnable(isEnable);
        		dataSourceForward.setRemark(remark);
        		dataSourceForward.setModifyUser(loginUser.getUserName());
        		dataSourceForward.setModifyTime(new Timestamp(new Date().getTime()));
    			
    			if (dataSourceForwardManage.isExist(forwardId, subscriptionName)) {
					throw new BeRefuseException("订阅名称已存在,请检查配置");
				}
    			
    			dataSourceForwardManage.update(dataSourceForward);
				success = true;
				EventLogManage.saveLog(loginUser.getUserId(), loginUser.getUserName(),"修改转发订阅", "转发订阅", request.getRemoteAddr(),"","成功");
			} catch (BeRefuseException e) {
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
        	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
		} else{
			return;
		}
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
	}
}
