package com.analog.data.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.analog.data.dao.IGpsDataSourceDao;
import com.analog.data.entity.GpsDataSourceEntity;
import com.analog.data.util.DateUtils;
import com.analog.data.util.EntityUtils;
import com.analog.data.util.SqlCondition;
import jodd.util.StringUtil;

/**
* @ClassName: GpsDataSourceDao
* @Description: gps数据dao
* @author yangjianlong
* @date 2019年11月6日上午10:33:35
*/
@Repository
public class GpsDataSourceDao extends BaseDao<GpsDataSourceEntity> implements IGpsDataSourceDao{

	public static final String GPSDATASOURCE_TABLE_PREFIX = "T_GPSDATASOURCE_";
	
	@Override
	public void deleteDataSource(Map<String, Object> params) throws Exception {
		Date date = (Date) params.get("date");
		String sourceType = params.get("sourceType").toString();
		
		String tableName = getTableName(sourceType);
		if(!isTableExist(tableName)) return;
		
		String sql = "delete from " + tableName + " where 1 = 1 ";
		SqlCondition condition = new SqlCondition();
		if(date != null) {
			condition.and().le("createTime", date);
		}
		sql = sql + condition.getCondition();
		executeForUpdate(sql, condition.getParams().toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void batchAdd(List<GpsDataSourceEntity> list) throws Exception {
		//根据不同的资源类型,插入不同的表中
		Map<String, Object> resultMap = new HashMap<>();
		for (GpsDataSourceEntity entity : list) {
			String sourceType = entity.getSourceType();
			ArrayList<GpsDataSourceEntity> l = null;
			if (resultMap.containsKey(sourceType)) {
				l = (ArrayList<GpsDataSourceEntity>) resultMap.get(sourceType);
				l.add(entity);
				resultMap.put(sourceType, l);
			}else{
				l = new ArrayList<>();
				l.add(entity);
				resultMap.put(sourceType, l);
			}
			
		}
		for (String key : resultMap.keySet()) {
			String sql = EntityUtils.genInsertClause(this.entityClass, getTableName(key));
			ArrayList<GpsDataSourceEntity> resultList =(ArrayList<GpsDataSourceEntity>) resultMap.get(key);
			executeForBatch(sql, resultList);
		}
	}
	
	@Override
	public void addGpsDate(GpsDataSourceEntity gpsDataSourceEntity) throws Exception {
		String sourceType = gpsDataSourceEntity.getSourceType();
		String tableName = getTableName(sourceType);
		/*boolean tableExist = isTableExist(tableName);
		if (!tableExist) {
			createTable(sourceType);
		}*/
		String sql = EntityUtils.genInsertClause(GpsDataSourceEntity.class, tableName);
		List<Object> params = EntityUtils.genInsertParams(gpsDataSourceEntity, GpsDataSourceEntity.class);
		getJdbcTemplate().update(sql, params.toArray());
	}
	
	@Override
	public void createTable(String tableName) throws Exception {
		tableName = getTableName(tableName);
		if(isTableExist(tableName)) return;
		
		StringBuilder sbTable = new StringBuilder();
		sbTable.append(" CREATE TABLE %s ");
		sbTable.append(" ( ");
		sbTable.append(" 	 id varchar(50) NOT NULL , ");
		sbTable.append(" 	 deviceId varchar(50) NULL , ");
		sbTable.append(" 	 sourceType varchar(30) NULL , ");
		sbTable.append(" 	 lon float(53) NULL ,");
		sbTable.append(" 	 lat float(53) NULL , ");
		sbTable.append(" 	 position varchar(1000) NULL , ");
		sbTable.append(" 	 token varchar(50) NULL , ");
		sbTable.append(" 	 createTime datetime NULL , ");
		sbTable.append(" 	 remark varchar(1000) NULL , ");
		sbTable.append(" 	 speed varchar(20) NULL , ");
		sbTable.append(" 	 gpsTime datetime NULL  ");
		sbTable.append(" ) ");
		
		StringBuilder sbIndex = new StringBuilder();
		sbIndex.append(" CREATE INDEX %s ON %s ");
		sbIndex.append(" ( ");
		sbIndex.append(" 	sourceType ASC, gpsTime ASC, deviceId ASC ");
		sbIndex.append(" ) ");
		
		String sqlTable = String.format(sbTable.toString(),tableName);
		String sqlIndex = String.format(sbIndex.toString(),tableName+ "_INDEX",tableName);
		executeForUpdate(sqlTable);
		executeForUpdate(sqlIndex);
	}
	
	private boolean isTableExist(String tableName) throws SQLException {
		return (getTableNameNumber(tableName) > 0);
	}
	
	private long getTableNameNumber(String tableName) {
		String sql = "SELECT count(*) FROM sysobjects WHERE name= ?";
		return getJdbcTemplate().queryForObject(sql, Long.class, tableName);
	}

	@Override
	public List<GpsDataSourceEntity> getList(Map<String, String> params) throws Exception {
		String sourceType = params.get("sourceType");
		String deviceId = params.get("deviceId");
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		String count = params.get("count");
		
		StringBuilder sql = new StringBuilder();
		if (StringUtil.isEmpty(count)) {
			sql.append(" select * from " + getTableName(sourceType) + " where 1=1 ");
		}else{
			sql.append(" select top ("+count+")* from " + getTableName(sourceType) + " where 1=1 ");
		}
		
		SqlCondition condition = new SqlCondition();
		
		if(StringUtil.isNotEmpty(sourceType)) {
			condition.and().equal("sourceType", sourceType);
		}
		if(StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime)) {
			Date startDate = DateUtils.str2Date(startTime, DateUtils.NORMAL_FORMAT);
			Date endDate = DateUtils.str2Date(endTime, DateUtils.NORMAL_FORMAT);
			condition.and().between("gpstime", startDate, endDate);
		}
		if(StringUtil.isNotEmpty(deviceId)) {
			condition.and().equal("deviceId", deviceId);
		}
		sql.append(condition.getCondition());
		sql.append(" order by gpsTime desc");
		
		return executeForList(sql.toString(), condition.getParams().toArray());
	}
	
	private String getTableName (String tableNameSuffix){
		return GPSDATASOURCE_TABLE_PREFIX + tableNameSuffix.toUpperCase();
	}

}
