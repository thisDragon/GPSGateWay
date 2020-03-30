package com.analog.data.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.analog.data.dao.IBaseDao;
import com.analog.data.dao.IGpsDataSourceForwardDao;
import com.analog.data.entity.GpsDataSourceForwardEntity;
import com.analog.data.service.IGpsDataSourceForwardService;

/**
* @ClassName: GpsDataSourceForwardService
* @Description: TODO(这里用一句话描述这个类的作用)
* @author yangjianlong
* @date 2020年1月7日上午11:13:48
*
 */
@Service
public class GpsDataSourceForwardService extends BaseService<GpsDataSourceForwardEntity> implements IGpsDataSourceForwardService{

	@Autowired
	private IGpsDataSourceForwardDao gpsDataSourceForwardDao;
	
	@Override
	public IBaseDao<GpsDataSourceForwardEntity> getDao() {
		return gpsDataSourceForwardDao;
	}

	@Override
	public List<GpsDataSourceForwardEntity> getList(Map<String, Object> params) throws Exception {
		return gpsDataSourceForwardDao.getList(params);
	}
}
