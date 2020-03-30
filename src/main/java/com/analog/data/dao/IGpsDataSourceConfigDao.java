package com.analog.data.dao;

import java.util.List;
import java.util.Map;

import com.analog.data.entity.GpsDataSourceConfigEntity;

public interface IGpsDataSourceConfigDao extends IBaseDao<GpsDataSourceConfigEntity>{

	List<GpsDataSourceConfigEntity> getList() throws Exception;
}
