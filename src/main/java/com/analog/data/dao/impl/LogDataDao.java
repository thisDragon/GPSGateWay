package com.analog.data.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.alibaba.fastjson.JSONArray;
import com.analog.data.dao.ILogDataDao;
import com.analog.data.entity.LogDataEntity;
import com.analog.data.enums.LogStateEnum;
import com.analog.data.enums.LogTypeEnum;
import com.analog.data.pojo.GpsDataSourcePojo;
import com.analog.data.util.EntityUtils;
import com.analog.data.util.SqlCondition;

/**
* @ClassName: LogDataDao
* @Description: 数据日志Dao
* @author yangjianlong
* @date 2020年1月7日上午11:13:20
*
 */
@Repository
public class LogDataDao extends BaseDao<LogDataEntity> implements ILogDataDao{

	private static final Logger logger = LoggerFactory.getLogger(LogDataDao.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void deleteLog(Map<String, Object> params) throws Exception {
		String sql = "delete from T_LOGDATA where 1 = 1 ";
		SqlCondition condition = new SqlCondition();
		Date date = (Date) params.get("date");
		ArrayList<Integer> types = (ArrayList<Integer>) params.get("types");
		if(date != null) {
			condition.and().le("createTime", date);
		}
		if(types != null && types.size() > 0) {
			condition.and().in("logType", types.toArray());
		}
		sql = sql + condition.getCondition();
		executeForUpdate(sql, condition.getParams().toArray());
	}

	@Override
	public void saveLog(GpsDataSourcePojo gpsDataSourcePojo, int logType, int state,String content) throws Exception {
		LogDataEntity logData = new LogDataEntity();
		logData.setId(EntityUtils.createId32());
		logData.setLogType(logType);
		logData.setState(state);
		
		if (state == LogStateEnum.SUCCESS.getState() && gpsDataSourcePojo != null) {
			logData.setDeviceId(gpsDataSourcePojo.getDeviceId());
			logData.setSourceType(gpsDataSourcePojo.getSourceType());
			logData.setContent(gpsDataSourcePojo.getId());
		}else if (state == LogStateEnum.FAIL.getState() && gpsDataSourcePojo != null) {
			logData.setDeviceId(gpsDataSourcePojo.getDeviceId());
			logData.setSourceType(gpsDataSourcePojo.getSourceType());
			String gpsdataJson = JSONArray.toJSON(gpsDataSourcePojo).toString();
			logData.setContent(gpsdataJson);
		}else if(state == LogStateEnum.NOT_JOIN.getState()){
			if (logType == LogTypeEnum.SELECT_LOG.getState()) {
				logData.setSourceType(gpsDataSourcePojo.getSourceType());
				logData.setContent(content);
			}
		}else if(logType == LogTypeEnum.REPORT_LOG.getState() && gpsDataSourcePojo == null){
			logData.setContent(content);
		}
		logData.setCreateTime(new Date());
		insert(logData);
	}
}
