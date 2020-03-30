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
import com.alibaba.fastjson.JSONObject;
import com.analog.data.entity.LogDataEntity;
import com.analog.data.exception.ParamException;
import com.analog.data.service.ILogDataService;
import com.analog.data.util.Constants;
import com.analog.data.util.EntityUtils;
import com.analog.data.util.HttpServletRequestUtil;

/**
* @ClassName: GpsDataSourceConfigController
* @Description: 数据源配置
* @author yangjianlong
* @date 2020年1月7日上午10:47:06
*
 */
@Controller
@RequestMapping(value = "logdata", method = { RequestMethod.GET,RequestMethod.POST })
public class LogDataController {

	private static final Logger logger = LoggerFactory.getLogger(LogDataController.class);
	@Autowired
    private ILogDataService logDataService;
	
	/**
	 * @Title: add   
	 * @Description: 新增日志数据
	 * @param request
	 * @return 
	 * Map<String,Object>      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 上午11:29:19
	 */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> add(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject jSONObject = HttpServletRequestUtil.getRequestBody(request, logger);
			if (jSONObject == null) {
				throw new ParamException("json实体不能为空");
			}
			LogDataEntity logData = new LogDataEntity();
			logData.setId(EntityUtils.createId32());
			logData.setDeviceId("deviceId");
			logData.setLogType(0);
			logData.setState(1);
			logData.setSourceType("sourceType");
			logData.setContent("content");
			logData.setCreateTime(new Date());
			logDataService.insert(logData);
			
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.MESSAGE, "新增日志数据成功");
		} catch (ParamException e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, e.getMessage());
		}	catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, "新增日志数据失败");
		}
		return resultMap;
	}
	
}
