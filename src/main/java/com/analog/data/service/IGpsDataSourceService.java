package com.analog.data.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.analog.data.entity.GpsDataSourceEntity;

public interface IGpsDataSourceService extends IBaseService<GpsDataSourceEntity>{
	
	/**
	 * @Title: batchAdd   
	 * @Description: 批量添加
	 * @param list
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月6日 下午5:54:50
	 */
	void batchAdd(List<GpsDataSourceEntity> list) throws Exception;
	
	/**
	 * @Title: upLoadLocation   
	 * @Description: 上报数据
	 * @param jSONObject
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 上午9:00:44
	 */
	void upLoadLocation(JSONObject jSONObject) throws Exception;
	
	/**
	 * @Title: addGpsDate   
	 * @Description: 新增数据源
	 * @param gpsDataSourceEntity
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 上午9:00:58
	 */
	void addGpsDate(GpsDataSourceEntity gpsDataSourceEntity) throws Exception;
	
	/**
	 * @Title: getList   
	 * @Description: 获取上报数据信息
	 * @param jSONObject
	 * @return
	 * @throws Exception 
	 * List<GpsDataSourceEntity>      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 下午3:35:08
	 */
	List<GpsDataSourceEntity> getList(JSONObject jSONObject) throws Exception;
}
