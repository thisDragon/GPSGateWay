package com.analog.data.dao;

import java.util.List;
import java.util.Map;

import com.analog.data.entity.GpsDataSourceEntity;

public interface IGpsDataSourceDao extends IBaseDao<GpsDataSourceEntity>{
	
	/**
	 * @Title: deleteDataSource   
	 * @Description: 删除
	 * @param params
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月18日 下午2:45:37
	 */
	void deleteDataSource(Map<String, Object> params) throws Exception;
	
	/**
	 * @Title: batchAdd   
	 * @Description: 批量添加
	 * @param list
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月9日 下午2:27:29
	 */
	void batchAdd(List<GpsDataSourceEntity> list) throws Exception;
	
	/**
	 * @Title: addGpsDate   
	 * @Description: 新增上报数据
	 * @param gpsDataSourceEntity
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2019年11月14日 下午4:23:10
	 */
	void addGpsDate(GpsDataSourceEntity gpsDataSourceEntity) throws Exception;
	
	/**
	 * @Title: createTable   
	 * @Description: 创建表
	 * @param tableName
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2019年11月14日 下午4:23:34
	 */
	void createTable(String tableName) throws Exception;
	
	/**
	 * @Title: getList   
	 * @Description: 获取上报数据
	 * @param params
	 * @return
	 * @throws Exception 
	 * List<GpsDataSourceEntity>      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月7日 下午3:37:51
	 */
	List<GpsDataSourceEntity> getList(Map<String, String> params) throws Exception;
}
