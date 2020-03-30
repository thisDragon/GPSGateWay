package com.analog.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.analog.data.dao.IGpsDataSourceForwardDao;
import com.analog.data.entity.GpsDataSourceForwardEntity;
import com.analog.data.util.SqlCondition;

import jodd.util.StringUtil;

/**
 * 
* @ClassName: GpsDataSourceForwardDao
* @Description: 数据转发配置Dao
* @author yangjianlong
* @date 2020年1月7日上午11:12:42
*
 */
@Repository
public class GpsDataSourceForwardDao extends BaseDao<GpsDataSourceForwardEntity> implements IGpsDataSourceForwardDao{

	@Override
	public List<GpsDataSourceForwardEntity> getList(Map<String, Object> params) throws Exception {
		String sourceType = params.get("sourceType").toString();
		
		StringBuilder sb = new StringBuilder(" select * from T_GPSDATASOURCEFORWARD where 1=1 ");
		
		SqlCondition condition = new SqlCondition();
		if(StringUtil.isNotEmpty(sourceType)) {
			condition.and().like("sourceType", "$" + sourceType + "$");
		}
		sb.append(condition.getCondition());
		
		return executeForList(sb.toString(), condition.getParams().toArray());
	}

}
