package com.analog.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.analog.data.dao.IGpsDataSourceConfigDao;
import com.analog.data.entity.GpsDataSourceConfigEntity;
import com.analog.data.util.SqlCondition;

/**
* @ClassName: GpsDataSourceConfigDao
* @Description: 数据源配置DAO
* @author yangjianlong
* @date 2020年1月7日上午11:01:50
*
 */
@Repository
public class GpsDataSourceConfigDao  extends BaseDao<GpsDataSourceConfigEntity> implements IGpsDataSourceConfigDao{

	@Override
	public List<GpsDataSourceConfigEntity> getList() throws Exception {
		StringBuilder sql = new StringBuilder(" select * from T_GPSDATASOURCECONFIG where 1=1 ");
		SqlCondition condition = new SqlCondition();
		sql.append(condition.getCondition());
		
		return executeForList(sql.toString(), condition.getParams().toArray());
	}
}
