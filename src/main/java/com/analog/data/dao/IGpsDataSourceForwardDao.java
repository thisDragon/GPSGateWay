package com.analog.data.dao;

import java.util.List;
import java.util.Map;

import com.analog.data.entity.GpsDataSourceForwardEntity;

public interface IGpsDataSourceForwardDao extends IBaseDao<GpsDataSourceForwardEntity>{

	List<GpsDataSourceForwardEntity> getList(Map<String, Object> params) throws Exception;
}
