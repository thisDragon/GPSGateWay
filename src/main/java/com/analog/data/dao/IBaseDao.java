package com.analog.data.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.analog.data.util.SqlCondition;

public interface IBaseDao<T> {

	public abstract JdbcTemplate getJdbcTemplate();

	public abstract Serializable insert(T t) throws Exception;
	
	public abstract int[] insert(List<T> entities) throws Exception;

	public abstract int update(T t) throws Exception;
	
	public abstract int update(T entity, String keyColumn) throws Exception;
	
	public abstract int[] update(List<T> entities, String keyColumn) throws Exception;

	public abstract int deleteAll();

	public abstract int delete(T t);

	public abstract int deleteById(Object id);

	public abstract int deleteByKey(String key, Object value);

	public abstract T getById(Object id);

	public abstract T getByKey(String key, Object value);

	public abstract boolean existById(Object id);

	public abstract boolean existByKey(String key, Object value);

	public abstract List<T> getAll();

	public abstract List<T> getPage(int page, int size, SqlCondition condition);

	public abstract List<T> getPage(String sql, int page, int size,
			Object... params);

	public abstract Long getAllCount();

	public abstract void batchInsert(List<T> entities) throws Exception;

	public abstract long executeForLong(String sql);

	public abstract long executeForLong(String sql, Object... params);

	public abstract T executeForObject(String sql);

	public abstract T executeForObject(String sql, Object... params);

	public abstract List<T> executeForList(String sql);

	public abstract List<T> executeForList(String sql, Object... params);

	public abstract int executeForUpdate(String sql);

	public abstract int executeForUpdate(String sql, Object... params);

	public abstract void executeForBatch(String sql, List<T> entities) throws Exception;

	public abstract void executeForBatch2(String sql, List<Object[]> objss);
	
	public abstract int clearOldData(int monthCount, String dateColName);
	
	public abstract int clearOldDataByDay (int dayCount, String dateColName);

	public List<String> getByKeyLists(String key, Integer distinct);

}