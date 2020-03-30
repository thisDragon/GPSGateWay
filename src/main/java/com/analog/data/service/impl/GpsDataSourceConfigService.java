package com.analog.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.analog.data.dao.IBaseDao;
import com.analog.data.dao.IGpsDataSourceConfigDao;
import com.analog.data.entity.GpsDataSourceConfigEntity;
import com.analog.data.service.IGpsDataSourceConfigService;

/**
* @ClassName: GpsDataSourceConfigService
* @Description: 数据源配置Service
* @author yangjianlong
* @date 2020年1月7日上午11:00:49
 */
@Service
public class GpsDataSourceConfigService extends BaseService<GpsDataSourceConfigEntity> implements IGpsDataSourceConfigService{

	@Autowired
	private IGpsDataSourceConfigDao gpsDataSourceConfigDao;
	
	@Override
	public IBaseDao<GpsDataSourceConfigEntity> getDao() {
		return gpsDataSourceConfigDao;
	}
}
