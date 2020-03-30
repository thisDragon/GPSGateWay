package com.analog.data.service;

import java.util.Map;

import com.analog.data.entity.LogDataEntity;
import com.analog.data.pojo.GpsDataSourcePojo;

public interface ILogDataService  extends IBaseService<LogDataEntity>{

	void saveLog(GpsDataSourcePojo gpsDataSourcePojo,int logType,int state,String content) throws Exception;
	
	void deleteLog(Map<String, Object> params) throws Exception;
}
