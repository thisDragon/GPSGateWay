package com.analog.data.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.analog.data.cache.MapCache;
import com.analog.data.entity.GpsDataSourceConfigEntity;
import com.analog.data.enums.DataSourceConfigUserFlagEnum;
import com.analog.data.enums.LogStateEnum;
import com.analog.data.enums.LogTypeEnum;
import com.analog.data.exception.ParamException;
import com.analog.data.pojo.GpsDataSourcePojo;
import com.analog.data.service.IGpsDataSourceService;
import com.analog.data.service.ILogDataService;
import com.analog.data.service.impl.GpsDataSourceService;
import com.analog.data.util.Constants;
import com.analog.data.util.DateUtils;
import com.analog.data.util.HttpServletRequestUtil;
import com.analog.data.util.Utils;

import jodd.util.StringUtil;

/**
* @ClassName: GpsDateSourceController
* @Description: gps数据源上报
* @author yangjianlong
* @date 2019年11月6日上午10:14:32
*
 */
@Controller
public class GpsDataSourceController {

	private static final Logger logger = LoggerFactory.getLogger(GpsDataSourceController.class);
	@Autowired
    private IGpsDataSourceService gpsDataSourceService;
	@Autowired
    private ILogDataService logDataService;
	
	/**
	 * @Title: locations   
	 * @Description: 查询定位数据
	 * @param request
	 * @return 
	 * Map<String,Object>      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 上午10:13:15
	 */
	@RequestMapping(value = "/locations",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> locations(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject jSONObject = HttpServletRequestUtil.getRequestBody(request, logger);
			if (jSONObject == null) {
				throw new ParamException("json实体不能为空");
			}
			checkLocationsParams(jSONObject);
			
			resultMap.put(Constants.DATA, gpsDataSourceService.getList(jSONObject));
			GpsDataSourcePojo gpsDataSourcePojo = new GpsDataSourcePojo();
			gpsDataSourcePojo.setSourceType(jSONObject.getString("sourceType"));
			logDataService.saveLog(gpsDataSourcePojo, LogTypeEnum.SELECT_LOG.getState(), LogStateEnum.NOT_JOIN.getState(),jSONObject.toJSONString());
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.MESSAGE, "查询定位数据成功");
			
		} catch (ParamException e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, e.getMessage());
		}	catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, "查询定位数据失败");
		}
		return resultMap;
	}
	
	/**
	 * @Title: upLoadLocation   
	 * @Description: 上报数据
	 * @param request
	 * @return 
	 * Map<String,Object>      
	 * @throws Exception 
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月6日 下午3:07:09
	 */
	@RequestMapping(value = "/upLoadLocation",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> upLoadLocation(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONObject jSONObject = null;
		try {
			jSONObject = HttpServletRequestUtil.getRequestBody(request, logger);
			if (jSONObject == null) {
				throw new ParamException("json实体不能为空");
			}
			checkUpLoadLocationParams(jSONObject);
			
			gpsDataSourceService.upLoadLocation(jSONObject);
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.MESSAGE, "上报成功");
		} catch (ParamException e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, e.getMessage());
			logDataService.saveLog(null, LogTypeEnum.REPORT_LOG.getState(), LogStateEnum.FAIL.getState(), jSONObject.toString());
		}	catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, "上报失败,请检查上报的数据是否符合规范");
			logDataService.saveLog(null, LogTypeEnum.REPORT_LOG.getState(), LogStateEnum.FAIL.getState(), jSONObject.toString());
		}
		return resultMap;
	}
	
	/**
	 * @Title: upLoadLocations   
	 * @Description: 批量上报
	 * @param request
	 * @return 
	 * Map<String,Object>      
	 * @throws Exception 
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月21日 上午10:40:58
	 */
	@RequestMapping(value = "/upLoadLocations",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> upLoadLocations(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONArray locations = null;
		try {
			locations = HttpServletRequestUtil.getRequestBodyOfJSONArray(request, logger);
			if (locations == null) {
				throw new ParamException("json实体不能为空");
			}
			if (locations.size() > 10) {
				throw new ParamException("locations内容不能超过10组");
			}
			for (int i = 0; i < locations.size(); i++) {
				checkUpLoadLocationParams(locations.getJSONObject(i));
			}
			for (int i = 0; i < locations.size(); i++) {
				gpsDataSourceService.upLoadLocation(locations.getJSONObject(i));
			}
			
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.MESSAGE, "上报成功");
		} catch (ParamException e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, e.getMessage());
			logDataService.saveLog(null, LogTypeEnum.REPORT_LOG.getState(), LogStateEnum.FAIL.getState(), locations.toString());
		}	catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, "上报失败,请检查上报的数据是否符合规范");
			logDataService.saveLog(null, LogTypeEnum.REPORT_LOG.getState(), LogStateEnum.FAIL.getState(), locations.toString());
		}
		return resultMap;
	}
	
	/**
	 * @Title: checkLocationsParams   
	 * @Description: 参数校验
	 * @param jSONObject
	 * @throws ParamException 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 上午10:28:25
	 */
	private void checkLocationsParams(JSONObject jSONObject) throws ParamException{
		String sourceType = jSONObject.getString("sourceType");
		String deviceId = jSONObject.getString("deviceId");
		String startTime = jSONObject.getString("startTime");
		String endTime = jSONObject.getString("endTime");
		String count = jSONObject.getString("count");
		
		if (StringUtil.isEmpty(sourceType)) throw new ParamException("sourceType不能为空");
		if (sourceType.length() > 30) throw new ParamException("sourceType的长度不能大于30个字符");
		if (StringUtil.isNotEmpty(deviceId) && deviceId.length() > 50) throw new ParamException("deviceId的长度不能大于50个字符");
		if (StringUtil.isNotEmpty(startTime) && StringUtil.isEmpty(endTime)) throw new ParamException("startTime和endTime必须同时存在");
		if (StringUtil.isEmpty(startTime) && StringUtil.isNotEmpty(endTime)) throw new ParamException("startTime和endTime必须同时存在");
		if (StringUtil.isNotEmpty(startTime) && Utils.isLong(startTime) == false) throw new ParamException("startTime的类型不符合要求");
		if (StringUtil.isNotEmpty(endTime) && Utils.isLong(endTime) == false) throw new ParamException("endTime的类型不符合要求");
		if (StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime) && Long.parseLong(startTime) > Long.parseLong(endTime)) throw new ParamException("startTime不能大于endTime");
		if (StringUtil.isNotEmpty(count) && Utils.isNumber(count) == false) throw new ParamException("count的类型不符合要求");
	}
	
	/**
	 * @Title: checkUpLoadLocationParams   
	 * @Description: 参数校验
	 * @param jSONObject 
	 * void      
	 * @throws Exception 
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 上午8:42:38
	 */
	private void checkUpLoadLocationParams(JSONObject jSONObject) throws Exception{
		String sourceType = jSONObject.getString("sourceType");
		String deviceId = jSONObject.getString("deviceId");
		String lon = jSONObject.getString("lon");
		String lat = jSONObject.getString("lat");
		String position = jSONObject.getString("position");
		String token = jSONObject.getString("token");
		String gpsTime = jSONObject.getString("gpsTime");
		String remark = jSONObject.getString("remark");
		String speed = jSONObject.getString("speed");
		
		if (StringUtil.isEmpty(sourceType)) throw new ParamException("sourceType不能为空");
		if (StringUtil.isEmpty(deviceId)) throw new ParamException("deviceId不能为空");
		if (StringUtil.isEmpty(lon)) throw new ParamException("lon不能为空");
		if (StringUtil.isEmpty(lat)) throw new ParamException("lat不能为空");
		if (StringUtil.isEmpty(position)) throw new ParamException("position不能为空");
		if (StringUtil.isEmpty(token)) throw new ParamException("token不能为空");
		if (StringUtil.isEmpty(gpsTime)) throw new ParamException("gpsTime不能为空");
		if (StringUtil.isEmpty(remark)) throw new ParamException("remark不能为空");
		if (StringUtil.isEmpty(speed)) throw new ParamException("speed不能为空");
		
		if (sourceType.length() > 30) throw new ParamException("sourceType的长度不能大于30个字符");
		if (deviceId.length() > 50) throw new ParamException("deviceId的长度不能大于50个字符");
		if (Utils.isNumber(lon) == false) throw new ParamException("lon的类型不符合要求");
		if (Utils.isNumber(lat) == false) throw new ParamException("lat的类型不符合要求");
		if (position.length() > 1000) throw new ParamException("position的长度不能大于1000个字符");
		if (token.length() < 16 || token.length() > 50 || Utils.isLong(token.substring(16, token.length())) == false) throw new ParamException("token的长度不符合要求");
		if (Utils.isLong(gpsTime) == false) throw new ParamException("gpsTime的类型不符合要求");
		if (remark.length() > 1000) throw new ParamException("remark的长度不能大于1000个字符");
		if (speed.length() > 20) throw new ParamException("speed的长度不能大于20个字符");
		
		//安全性校验(token = 16位token+时间戳,与服务器时间之差小于300秒)
		String checkTaken = token.substring(0, 16);
		//GpsDataSourceConfigEntity dataSourceConfig = gpsDataSourceConfigService.getEntityByKey("sourceType", sourceType);
		Map<Object, Object> cacheMap = MapCache.getCacheMap();
		Object cacheObject = cacheMap.get(sourceType);
		
		if (cacheObject == null ) throw new ParamException("该数据源类型无效,请重新填写数据源类型");
		GpsDataSourceConfigEntity dataSourceConfig = (GpsDataSourceConfigEntity)cacheObject;
		if (false == dataSourceConfig.getToken().equals(checkTaken)) throw new ParamException("token无效,请重新填写token");
		
		Long timestamp = Long.parseLong(token.substring(16, token.length()));
		long mistiming = DateUtils.getBetweenSeconds(new Date(), new Date(timestamp));
		if (mistiming > GpsDataSourceService.minites) throw new ParamException("token过期,请重新生成token");
		if (dataSourceConfig.getUserFlag() == DataSourceConfigUserFlagEnum.DISABLED.getState()) throw new ParamException("该数据源类型已被禁用,请启用");
	}
}
