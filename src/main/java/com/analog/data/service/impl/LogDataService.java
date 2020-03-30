package com.analog.data.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.analog.data.dao.IBaseDao;
import com.analog.data.dao.ILogDataDao;
import com.analog.data.entity.LogDataEntity;
import com.analog.data.pojo.GpsDataSourcePojo;
import com.analog.data.service.ILogDataService;

/**
* @ClassName: LogDataService
* @Description: 日志数据Service
* @author yangjianlong
* @date 2020年1月7日上午11:16:08
*
 */
@Service
public class LogDataService extends BaseService<LogDataEntity> implements ILogDataService{

	@Autowired
	private ILogDataDao logDataDao;
	
	@Override
	public IBaseDao<LogDataEntity> getDao() {
		return logDataDao;
	}

	@Override
	public void deleteLog(Map<String, Object> params) throws Exception {
		logDataDao.deleteLog(params);
	}

	@Override
	public void saveLog(GpsDataSourcePojo gpsDataSourcePojo, int logType, int state,String content) throws Exception {
		logDataDao.saveLog(gpsDataSourcePojo, logType, state, content);
	}
}
