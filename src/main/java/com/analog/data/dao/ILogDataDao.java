package com.analog.data.dao;

import java.util.Map;

import com.analog.data.entity.LogDataEntity;
import com.analog.data.pojo.GpsDataSourcePojo;

public interface ILogDataDao extends IBaseDao<LogDataEntity>{

	/**
	 * @Title: saveLog   
	 * @Description: 保存日志
	 * @param gpsDataSourcePojo
	 * @param logType
	 * @param state
	 * @param content
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月8日 上午11:34:20
	 */
	void saveLog(GpsDataSourcePojo gpsDataSourcePojo,int logType,int state,String content) throws Exception;
	
	/**
	 * @Title: deleteLog   
	 * @Description: 删除日志
	 * @param params
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月8日 上午9:52:43
	 */
	void deleteLog(Map<String, Object> params) throws Exception;
}
