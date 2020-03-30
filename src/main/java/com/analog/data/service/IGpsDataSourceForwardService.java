package com.analog.data.service;

import java.util.List;
import java.util.Map;
import com.analog.data.entity.GpsDataSourceForwardEntity;

public interface IGpsDataSourceForwardService extends IBaseService<GpsDataSourceForwardEntity>{

	List<GpsDataSourceForwardEntity> getList(Map<String, Object> params) throws Exception;
}
