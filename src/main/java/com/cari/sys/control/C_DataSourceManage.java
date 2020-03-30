package com.cari.sys.control;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.analog.data.cache.MapCache;
import com.analog.data.entity.GpsDataSourceConfigEntity;
import com.analog.data.util.EntityUtils;
import com.cari.rbac.DataSourceConfig;
import com.cari.rbac.DataSourceConfigManage;
import com.cari.rbac.DataSourceForwardManage;
import com.cari.sys.bean.SysUser;
import com.cari.sys.biz.EventLogManage;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.ServletUtil;

/**
* @ClassName: C_DataSourceManage
* @Description: 数据源配置模块
* @author yangjianlong
* @date 2020年2月10日下午2:16:19
*
 */

public class C_DataSourceManage extends HttpServlet {
	
	private static final Logger logger = LoggerFactory.getLogger(C_DataSourceManage.class);
	private static final long serialVersionUID = 1L;
	private static final int ADD_STATE = 0;
	private static final int UPDATE_STATE = 1;
	private static final int DELETE_STATE = 2;
	
	private DataSourceConfigManage dataSourceConfigManage;
	private DataSourceForwardManage dataSourceForwardManage;
	
	public C_DataSourceManage() {
		super();
		dataSourceConfigManage = DataSourceConfigManage.getInstance();
		dataSourceForwardManage = DataSourceForwardManage.getInstance();
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
			String sourceName = request.getParameter( "sourceName");
        	String sourceType = request.getParameter("sourceType");
        	String token = request.getParameter("token");
        	String remark = HttpParamCaster.getParameter(request, "remark", "");
        	int timeSpan = HttpParamCaster.getIntParameter(request, "timeSpan", 0);
        	String str = "保存成功！";
        	boolean success = false;
        	try {
        		DataSourceConfig dataSourceConfig = new DataSourceConfig();
        		dataSourceConfig.setId(EntityUtils.createId32());
        		dataSourceConfig.setToken(token);
    			dataSourceConfig.setTimeSpan(timeSpan);
    			dataSourceConfig.setSourceType(sourceType.toUpperCase());
    			dataSourceConfig.setUserFlag(1);
    			dataSourceConfig.setSourceName(sourceName);
    			dataSourceConfig.setRemark(remark);
    			dataSourceConfig.setCreateUser(loginUser.getUserName());
    			dataSourceConfig.setCreateTime(new Timestamp(new Date().getTime()));
    			dataSourceConfig.setModifyUser(loginUser.getUserName());
    			dataSourceConfig.setModifyTime(new Timestamp(new Date().getTime()));
        		
    			if (dataSourceConfigManage.isExist(null,sourceName, sourceType)) {
					throw new BeRefuseException("数据源名称或数据类型已存在,请检查配置");
				}else{
					dataSourceConfigManage.save(dataSourceConfig);
					dataSourceConfigManage.createTable(dataSourceConfig.getSourceType());
					refreshMapCache(dataSourceConfig,ADD_STATE);
					success = true;
					EventLogManage.saveLog(loginUser.getUserId(), loginUser.getUserName(),"新增数据源", "数据源配置", request.getRemoteAddr(),"","成功");
				}
			} catch (BeRefuseException e) {
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
        	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        } else if ("toDelete".equalsIgnoreCase(action)) {
        	String[] configIds = request.getParameterValues("configId");
        	String str = "删除成功！";
        	boolean success = false;
        	try {
				if (configIds == null || configIds.length < 1) {
					throw new BeRefuseException("请选择要删除的配置");
				}
				DataSourceConfig dataSourceConfig = dataSourceConfigManage.getDataSourceConfigByKey(configIds[0]);
				ListPage list = dataSourceForwardManage.getList(1, 1, dataSourceConfig.getSourceName(), "");
				
				if (list.getTotalSize() > 0) {
					throw new BeRefuseException("该数据源下配置了转发订阅,请先取消关联的转发订阅");
				}
				dataSourceConfigManage.delete(configIds);
				dataSourceConfigManage.renameTable(dataSourceConfig.getSourceType());
				refreshMapCache(dataSourceConfig,DELETE_STATE);
				success = true;
				EventLogManage.saveLog(loginUser.getUserId(), loginUser.getUserName(),"删除数据源", "数据源配置", request.getRemoteAddr(),"","成功");
			} catch (BeRefuseException e) {
				e.printStackTrace();
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
			ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        } else if ("toUpdate".equalsIgnoreCase(action)) {
        	String configId = request.getParameter( "configId");
        	String sourceName = request.getParameter( "sourceName");
        	String sourceType = request.getParameter("sourceType");
        	String token = request.getParameter("token");
        	String remark = HttpParamCaster.getParameter(request, "remark", "");
        	int timeSpan = HttpParamCaster.getIntParameter(request, "timeSpan", 0);
        	
        	String str = "修改成功！";
        	boolean success = false;
        	try {
    			if (dataSourceConfigManage.isExist(configId,sourceName, sourceType)) {
					throw new BeRefuseException("数据源名称或数据类型已存在,请检查配置");
				}
    			
    			DataSourceConfig dataSourceConfig = dataSourceConfigManage.getDataSourceConfigByKey(configId);
        		dataSourceConfig.setToken(token);
    			dataSourceConfig.setTimeSpan(timeSpan);
    			dataSourceConfig.setSourceType(sourceType.toUpperCase());
    			dataSourceConfig.setSourceName(sourceName);
    			dataSourceConfig.setRemark(remark);
    			dataSourceConfig.setModifyUser(loginUser.getUserName());
    			dataSourceConfig.setModifyTime(new Timestamp(new Date().getTime()));
    			
				dataSourceConfigManage.update(dataSourceConfig);
				refreshMapCache(dataSourceConfig,UPDATE_STATE);
				success = true;
				EventLogManage.saveLog(loginUser.getUserId(), loginUser.getUserName(),"修改数据源", "数据源配置", request.getRemoteAddr(),"","成功");
			} catch (BeRefuseException e) {
				str = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				str = "系统异常！";
			}
        	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str));
        } else if ("setUserFalg".equalsIgnoreCase(action)) {
        	String configId = request.getParameter( "configId");
        	String str = "修改成功！";
        	boolean success = false;
        	try {
        		dataSourceConfigManage.updateUserFlag(configId);
        		refreshMapCache(dataSourceConfigManage.getDataSourceConfigByKey(configId),UPDATE_STATE);
				success = true;
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
	
	/**
	 * @Title: refreshMapCache   
	 * @Description: 刷新map缓存   
	 * @param dataSourceConfig
	 * @param operateState 操作状态 0:新增;1:修改,2:删除
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月6日 下午2:30:50
	 */
	private void refreshMapCache(DataSourceConfig dataSourceConfig,int operateState){
		GpsDataSourceConfigEntity gpsDataSourceConfigEntity = new GpsDataSourceConfigEntity();
		BeanUtils.copyProperties(dataSourceConfig,gpsDataSourceConfigEntity);
		
		Map<Object, Object> cacheMap = MapCache.getCacheMap();
		
		switch (operateState) {
		case ADD_STATE:
			cacheMap.put(gpsDataSourceConfigEntity.getSourceType(), gpsDataSourceConfigEntity);
			break;
		case UPDATE_STATE:
			cacheMap.put(gpsDataSourceConfigEntity.getSourceType(), gpsDataSourceConfigEntity);
			break;
		case DELETE_STATE:
			cacheMap.remove(gpsDataSourceConfigEntity.getSourceType());
			break;

		default:
			break;
		}
		logger.info("数据源配置缓存最新状态------:"+JSON.toJSONString(cacheMap));
	}
}
