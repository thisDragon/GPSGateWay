package com.analog.data.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.analog.data.dao.IGpsDataSourceDao;
import com.analog.data.dao.IAopDemo;
import com.analog.data.entity.GpsDataSourceEntity;
import com.analog.data.pojo.GpsDataSourcePojo;
import com.analog.data.queue.QueueMessageListener;
import com.analog.data.service.IGpsDataSourceService;
import com.analog.data.util.Constants;
import com.analog.data.util.EntityUtils;
import com.analog.data.util.HttpServletRequestUtil;

/**
* @ClassName: TestController
* @Description: 测试
* @author yangjianlong
* @date 2019年11月6日下午4:48:31
*
 */
@Controller
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private IGpsDataSourceService gpsDataSourceService;
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	@Autowired
    private IGpsDataSourceDao gpsDataSourceDao;
	@Autowired
	private IAopDemo aopDeme;
	
	@RequestMapping(value = "/aopTest")
	@ResponseBody
	private Map<String, Object> aopTest(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		aopDeme.doAop("param1","param2");
		resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
		return resultMap;
	}
	
	
	@RequestMapping(value = "/test1")
	@ResponseBody
	private Map<String, Object> test1(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		gpsDataSourceDao.createTable("1");
		//redisTemplate.convertAndSend("topic.channel", "hello world!2");
		//producerServiceImpl.sendMessage("我是一条消息");
		resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
		return resultMap;
	}
	
	@RequestMapping(value = "/test2")
	@ResponseBody
	private Map<String, Object> test2(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		GpsDataSourceEntity entity = new GpsDataSourceEntity();
		entity.setId(EntityUtils.createId32());
		entity.setDeviceId("deviceID");
		entity.setLon(123.123);
		entity.setLat(23.2323);
		entity.setPosition("posintion");
		entity.setRemark("remark");
		entity.setSourceType("sourceType");
		entity.setToken("token");
		entity.setCreateTime(new Date());
		try {
			gpsDataSourceService.addGpsDate(entity);
			
			GpsDataSourcePojo gpsdata = new GpsDataSourcePojo();
			gpsdata.setId(EntityUtils.createId32());
			gpsdata.setDeviceId("deviceId");
			gpsdata.setLon(123.123);
			gpsdata.setLat(23.2323);
			gpsdata.setPosition("posintion");
			gpsdata.setRemark("remark");
			gpsdata.setSourceType("sourceType");
			gpsdata.setToken("token");
			gpsdata.setCreateTime(new Date());
			gpsdata.setFailureCount(0);
			String gpsdataJson = JSONArray.toJSON(gpsdata).toString();
			redisTemplate.opsForList().leftPush(QueueMessageListener.queueName, gpsdataJson);
			logger.info("redis和dao都ok................................");
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
		return resultMap;
	}
	
	@RequestMapping(value = "/test3")
	@ResponseBody
	private Map<String, Object> report(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String sourceType = HttpServletRequestUtil.getString(request, "sourceType");
			String deviceID = HttpServletRequestUtil.getString(request, "deviceID");
			Double lon = HttpServletRequestUtil.getDouble(request, "lon");
			Double lat = HttpServletRequestUtil.getDouble(request, "lat");
			String posintion = HttpServletRequestUtil.getString(request, "posintion");
			String token = HttpServletRequestUtil.getString(request, "token");
			String remark = HttpServletRequestUtil.getString(request, "remark");
			
			GpsDataSourcePojo gpsdata = new GpsDataSourcePojo();
			gpsdata.setDeviceId(deviceID);
			gpsdata.setLon(lon);
			gpsdata.setLat(lat);
			gpsdata.setPosition(posintion);
			gpsdata.setRemark(remark);
			gpsdata.setSourceType(sourceType);
			gpsdata.setToken(token);
			String gpsdataJson = JSONArray.toJSON(gpsdata).toString();
			
			System.out.println("接收到的gpsdataJson:"+gpsdataJson);
			
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.MESSAGE, "test3成功");
		} catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, "test3失败");
		}
		return resultMap;
	}
	
}
