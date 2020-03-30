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
import com.analog.data.entity.GpsDataSourceConfigEntity;
import com.analog.data.exception.ParamException;
import com.analog.data.service.IGpsDataSourceConfigService;
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
@RequestMapping(value = "dataSource/config", method = { RequestMethod.GET,RequestMethod.POST })
public class GpsDataSourceConfigController {

	private static final Logger logger = LoggerFactory.getLogger(GpsDataSourceConfigController.class);
	@Autowired
    private IGpsDataSourceConfigService gpsDataSourceConfigService;
	
	/**
	 * @Title: add   
	 * @Description: 新增数据源配置
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
			GpsDataSourceConfigEntity gpsDataSourceConfig = new GpsDataSourceConfigEntity();
			gpsDataSourceConfig.setId(EntityUtils.createId32());
			gpsDataSourceConfig.setToken("98bc3dd41b17478b");
			gpsDataSourceConfig.setTimeSpan(7);
			gpsDataSourceConfig.setSourceType("sourceType");
			gpsDataSourceConfig.setUserFlag(1);
			gpsDataSourceConfig.setSourceName("sourceName");
			gpsDataSourceConfig.setRemark("remark");
			gpsDataSourceConfig.setCreateUser("createUser");
			gpsDataSourceConfig.setCreateTime(new Date());
			gpsDataSourceConfig.setModifyUser("modifyUser");
			gpsDataSourceConfig.setModifyTime(new Date());
			gpsDataSourceConfigService.insert(gpsDataSourceConfig);
			
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.MESSAGE, "新增数据源配置成功");
		} catch (ParamException e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, e.getMessage());
		}	catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.MESSAGE, "新增数据源配置失败");
		}
		return resultMap;
	}
	
}
