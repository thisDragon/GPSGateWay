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
import com.analog.data.entity.GpsDataSourceForwardEntity;
import com.analog.data.exception.ParamException;
import com.analog.data.service.IGpsDataSourceForwardService;
import com.analog.data.util.Constants;
import com.analog.data.util.EntityUtils;
import com.analog.data.util.HttpServletRequestUtil;

/**
* @ClassName: GpsDataSourceForwardController
* @Description: 数据源转发配置
* @author yangjianlong
* @date 2020年1月7日上午10:47:14
*
 */
@Controller
@RequestMapping(value = "dataSource/forward", method = { RequestMethod.GET,RequestMethod.POST })
public class GpsDataSourceForwardController {

	private static final Logger logger = LoggerFactory.getLogger(GpsDataSourceForwardController.class);
	@Autowired
    private IGpsDataSourceForwardService gpsDataSourceForwardService;
	
	/**
	 * @Title: add   
	 * @Description: 新增数据源转发配置
	 * @param request
	 * @return 
	 * Map<String,Object>      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 上午11:31:23
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
			GpsDataSourceForwardEntity gpsDataSourceForward = new GpsDataSourceForwardEntity();
			gpsDataSourceForward.setId(EntityUtils.createId32());
			gpsDataSourceForward.setSubscriptionName("subscriptionName");
			gpsDataSourceForward.setForwardUrl("http://127.0.0.1:9086/GPSGateWay/test3");
			gpsDataSourceForward.setSourceType("sourceType");
			gpsDataSourceForward.setRemark("remark");
			gpsDataSourceForward.setModifyUser("modifyUser");
			gpsDataSourceForward.setModifyTime(new Date());
			gpsDataSourceForward.setCreateUser("createUser");
			gpsDataSourceForward.setCreateTime(new Date());
			
			gpsDataSourceForwardService.insert(gpsDataSourceForward);
			
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.MESSAGE, " 新增数据源转发配置成功");
		} catch (ParamException e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, e.getMessage());
		}	catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, " 新增数据源转发配置失败");
		}
		return resultMap;
	}
}
