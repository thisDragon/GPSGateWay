package com.analog.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.analog.data.cache.JedisQueue;
import com.analog.data.cache.JedisUtil;
import com.analog.data.cache.MapCache;
import com.analog.data.dao.IBaseDao;
import com.analog.data.dao.IGpsDataSourceConfigDao;
import com.analog.data.dao.IGpsDataSourceDao;
import com.analog.data.entity.GpsDataSourceConfigEntity;
import com.analog.data.entity.GpsDataSourceEntity;
import com.analog.data.enums.DataSourceConfigUserFlagEnum;
import com.analog.data.enums.LogStateEnum;
import com.analog.data.enums.LogTypeEnum;
import com.analog.data.exception.ParamException;
import com.analog.data.pojo.GpsDataSourcePojo;
import com.analog.data.queue.QueueMessageListener;
import com.analog.data.service.IGpsDataSourceService;
import com.analog.data.service.ILogDataService;
import com.analog.data.util.DateUtils;
import com.analog.data.util.EntityUtils;

import jodd.util.StringUtil;

/**
* @ClassName: GpsDataSourceService
* @Description: gps数据service
* @author yangjianlong
* @date 2019年11月6日上午10:32:59
*
 */
@Service
@Transactional
public class GpsDataSourceService extends BaseService<GpsDataSourceEntity> implements IGpsDataSourceService{

	private static final Logger logger = LoggerFactory.getLogger(GpsDataSourceService.class);
	
	@Autowired
	private IGpsDataSourceDao gpsDataSourceDao;
	@Autowired
	private IGpsDataSourceConfigDao gpsDataSourceConfigDao;
	@Autowired
	private ILogDataService logDataService;
	//@Autowired
    //private RedisTemplate<String, String> redisTemplate;
	//@Autowired
	//private JedisUtil.Lists jedisList;
	public static int minites = 300;	//与服务器时间之差,用于安全性验证(单位:,秒)
	
	@Override
	public IBaseDao<GpsDataSourceEntity> getDao() {
		return gpsDataSourceDao;
	}

	@Override
	public void addGpsDate(GpsDataSourceEntity gpsDataSourceEntity) throws Exception {
		gpsDataSourceDao.addGpsDate(gpsDataSourceEntity);
	}

	@Override
	public void upLoadLocation(JSONObject jSONObject) throws Exception {
		String sourceType = jSONObject.getString("sourceType");
		String deviceId = jSONObject.getString("deviceId");
		Double lon = Double.valueOf(jSONObject.getString("lon"));
		Double lat = Double.valueOf(jSONObject.getString("lat"));
		String position = jSONObject.getString("position");
		String token = jSONObject.getString("token");
		Long gpsTime = Long.parseLong(jSONObject.getString("gpsTime"));
		String remark = jSONObject.getString("remark");
		String speed = jSONObject.getString("speed");
		
		GpsDataSourcePojo gpsdata = new GpsDataSourcePojo();
		gpsdata.setId(EntityUtils.createId32());
		gpsdata.setDeviceId(deviceId);
		gpsdata.setLon(lon);
		gpsdata.setLat(lat);
		gpsdata.setPosition(position);
		gpsdata.setRemark(remark);
		gpsdata.setSourceType(sourceType);
		gpsdata.setToken(token);
		gpsdata.setSpeed(speed);
		gpsdata.setGpsTime(new Date(gpsTime));
		gpsdata.setCreateTime(new Date());
		gpsdata.setFailureCount(0);
		String gpsdataJson = JSONArray.toJSON(gpsdata).toString();
		//jedisList.lpush(QueueMessageListener.queueName, gpsdataJson);
		JedisQueue.push(QueueMessageListener.queueName, gpsdataJson);
		logger.info("上报成功的数据:" + JSONArray.toJSON(gpsdata).toString());
		logDataService.saveLog(gpsdata, LogTypeEnum.REPORT_LOG.getState(), LogStateEnum.SUCCESS.getState(),null);
		//redisTemplate.opsForList().leftPush(QueueMessageListener.queueName, gpsdataJson);
	}

	@Override
	public List<GpsDataSourceEntity> getList(JSONObject jSONObject) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		String sourceType = jSONObject.getString("sourceType");
		String deviceId = jSONObject.getString("deviceId");
		String startTime = jSONObject.getString("startTime");
		String endTime = jSONObject.getString("endTime");
		String count = jSONObject.getString("count");
		
		//GpsDataSourceConfigEntity dataSourceConfig = gpsDataSourceConfigDao.getByKey("sourceType", sourceType);
		Map<Object, Object> cacheMap = MapCache.getCacheMap();
		Object cacheObject = cacheMap.get(sourceType);
		if (cacheObject == null ) throw new ParamException("该数据源类型无效,请重新填写数据源类型");
		//if (dataSourceConfig == null) throw new ParamException("不存在该数据源类型,请检查数据源类型");
		GpsDataSourceConfigEntity dataSourceConfig = (GpsDataSourceConfigEntity)cacheObject;
		if (dataSourceConfig.getUserFlag() == DataSourceConfigUserFlagEnum.DISABLED.getState()) throw new ParamException("该数据源类型已被禁用,请启用");
		
		Integer timeSpan = dataSourceConfig.getTimeSpan();
		if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			Date date = new Date();
			params.put("startTime", DateUtils.date2Str(DateUtils.getNextDay(date, -timeSpan), DateUtils.NORMAL_FORMAT));
			params.put("endTime", DateUtils.date2Str(date, DateUtils.NORMAL_FORMAT));
		}else{
			Date startDate = new Date(Long.parseLong(startTime));
			params.put("startTime", DateUtils.date2Str(startDate, DateUtils.NORMAL_FORMAT));
			Date endDate1 = new Date(Long.parseLong(endTime));
			Date endDate2 = DateUtils.getNextDay(startDate, timeSpan);
			
			int betweenSeconds1 = DateUtils.getBetweenSeconds(startDate, endDate1);
			int betweenSeconds2 = DateUtils.getBetweenSeconds(startDate, endDate2);
			if (betweenSeconds1 > betweenSeconds2) {
				params.put("endTime", DateUtils.date2Str(endDate2, DateUtils.NORMAL_FORMAT));
			}else{
				params.put("endTime", DateUtils.date2Str(endDate1, DateUtils.NORMAL_FORMAT));
			}
		}
		params.put("sourceType", sourceType);
		params.put("deviceId", deviceId);
		params.put("count", count);
		
		return gpsDataSourceDao.getList(params);
	}

	@Override
	public void batchAdd(List<GpsDataSourceEntity> list) throws Exception {
		gpsDataSourceDao.batchAdd(list);
	}

}
