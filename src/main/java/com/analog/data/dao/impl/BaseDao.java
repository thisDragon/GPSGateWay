package com.analog.data.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.analog.data.dao.IBaseDao;
import com.analog.data.util.BeanUtils;
import com.analog.data.util.DateUtils;
import com.analog.data.util.EntityUtils;
import com.analog.data.util.RowMapperImpl;
import com.analog.data.util.SqlCondition;
import com.analog.data.util.Utils;

import jodd.util.StringUtil;


/**
* @ClassName: BaseDao
* @Description: dao的基类
* @author yangjianlong
* @date 2019年11月6日上午10:03:00
*
* @param <T>
 */
@SuppressWarnings("unchecked")
public class BaseDao<T> implements IBaseDao<T> {
	
	protected Class<T> entityClass = null;
	protected String tableName = null;
	
	public static int BATCH_COUNT = 500;

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	protected NamedParameterJdbcOperations namedParameterJdbcTemplate;
 
	public BaseDao() {
		entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		tableName = EntityUtils.getTableName(entityClass);
	}

	/**
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @param t
	 * @throws Exception
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public Serializable insert(T obj) throws Exception {
		T t = StringHtmlEscape(obj);
		Object id = BeanUtils.getProperty(t, "id");
		String id32 = EntityUtils.createId32();
		if(id == null) BeanUtils.setProperty(t, "id", id32);
		String sql = EntityUtils.genInsertClause(t.getClass());
		List<Object> params = EntityUtils.genInsertParams(t, t.getClass());
		executeForUpdate(sql, params.toArray());
		return (Serializable)id32;
	}
	
	 @SuppressWarnings("rawtypes")
	public int[] insert(List<T> entities) throws Exception{
		String sql = EntityUtils.genInsertClause(this.entityClass);

		List<Object[]> objss = new ArrayList<Object[]>();
		for (int i = 0; i < entities.size(); ++i) {
			T entity = StringHtmlEscape(entities.get(i));
			List tempParams = EntityUtils.genInsertParams(entity, this.entityClass);
			objss.add(tempParams.toArray());
		}
		return getJdbcTemplate().batchUpdate(sql, objss);
	 }

	/**
	 * @param t
	 * @throws Exception
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public int update(T t) throws Exception {
		T obj = StringHtmlEscape(t);
		String sql = EntityUtils.genUpdateClause(obj.getClass(), "id");
		List<Object> params = EntityUtils.genUpdateParams(obj, "id",obj.getClass());
		return executeForUpdate(sql, params.toArray());
	}
	
	 @SuppressWarnings("rawtypes")
	public int update(T entity, String keyColumn) throws Exception {
		T obj = StringHtmlEscape(entity);
	    String sql = EntityUtils.genUpdateClause(this.entityClass, keyColumn);
		List params = EntityUtils.genUpdateParams(obj, keyColumn, this.entityClass);
		return executeForUpdate(sql, params.toArray());
	}
	 
	public int[] update(List<T> entities, String keyColumn) throws Exception {
		String sql = EntityUtils.genUpdateClause(this.entityClass, keyColumn);

		List<Object[]> objss = new ArrayList<Object[]>();
		for (int i = 0; i < entities.size(); ++i) {
			T entity = StringHtmlEscape(entities.get(i));
			List<Object> tempParams = EntityUtils.genUpdateParams(entity, keyColumn,
					this.entityClass);
			objss.add(tempParams.toArray());
		}
		return getJdbcTemplate().batchUpdate(sql, objss);
	}

	/**
	 * 
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public int deleteAll() {
		String sql = "delete from %s";
		sql = String.format(sql, tableName);
		return executeForUpdate(sql);
	}

	/**
	 * @param t
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public int delete(T t) {
		Object id = BeanUtils.getProperty(t, "id");
		return deleteById(id);
	}

	/**
	 * @param id
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public int deleteById(Object id) {
		return deleteByKey("id", id);
	}

	/**
	 * @param key
	 * @param value
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public int deleteByKey(String key, Object value) {
		if (StringUtil.isEmpty(key)) throw new IllegalArgumentException("key不能为空");
		if (value == null) throw new IllegalArgumentException("key的value不能为空");
		
		String sql = "delete from %s where %s in(:%s)";
		sql = String.format(sql, tableName, key,key);
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue(key, value);
		return namedParameterJdbcTemplate.update(sql, paramSource);
	}

	/**
	 * @param id
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public T getById(Object id) {
		return getByKey("id", id);
	}
	public List<T> getPageBySQL(String sql, int page, int size,
			Object... objs) {
		sql = Utils.pageSql(sql, (page - 1) * size, page* size);
		return getJdbcTemplate().query(sql,new BeanPropertyRowMapper<T>(entityClass), objs);
	}


	public long getCountBySQL(String sql, Object... objs) {
		return (Long) getJdbcTemplate().queryForObject(sql, Long.class, objs);
	}
	/**
	 * @param key
	 * @param value
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public T getByKey(String key, Object value) {
		//String sql = "select * from %s where %s = ?";
		String sql = "select * from %s where %s collate Chinese_PRC_CS_AS= ?";
		sql = String.format(sql, tableName, key);
		return executeForObject(sql, value);	
	}
	public List<T> getListByJdbcTemplate(String sql, Object... objs) {
		return getJdbcTemplate().query(sql, new RowMapperImpl<T>(entityClass),
				objs);
	}
	/**
	 * @param id
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public boolean existById(Object id) {
		return existByKey("id", id);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public boolean existByKey(String key, Object value) {
		String sql = "select count(*) from %s where %s = ?";
		sql = String.format(sql, tableName, key);
		return executeForLong(sql, value) > 0 ; 
	}

	/**
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public List<T> getAll() {
		String sql = "select * from %s";
		sql = String.format(sql, tableName);
		return executeForList(sql);
	}

	/**
	 * @param page
	 * @param size
	 * @param condition
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public List<T> getPage(int page, int size, SqlCondition condition) {
		String sql = "select * from %s where 1=1 %s ";
		String clause = condition.getCondition();
		if(clause.startsWith(" and ")) clause = clause.replaceFirst(" and ", "");
		sql = String.format(sql, tableName, clause);
		String querySql = Utils.pageSql(sql.toString(), (page - 1) * size, page * size);
		return executeForList(querySql, condition.getParams().toArray());
	}
	
	/**
	 * @param sql
	 * @param page
	 * @param size
	 * @param params
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public List<T> getPage(String sql, int page, int size, Object... params) {
		String querySql = Utils.pageSql(sql.toString(), (page - 1) * size, page * size);
 		return executeForList(querySql, params);
	}

	/**
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public Long getAllCount() {
		String sql = "select count(*) from %s";
		sql = String.format(sql, tableName);
		return executeForLong(sql);
	}
	
	/**
	 * @param entities
	 * @throws Exception
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public void batchInsert(List<T> entities) throws Exception {
		String sql = EntityUtils.genInsertClause(entityClass);
		for (T t : entities) {
			StringHtmlEscape(t);
		}
		executeForBatch(sql, entities);
	}
	
	/**
	 * @param sql
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public long executeForLong(String sql) {
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	/**
	 * @param sql
	 * @param params
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public long executeForLong(String sql, Object... params) {
		return jdbcTemplate.queryForObject(sql, Long.class, params);
	}
	
	/**
	 * @param sql
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public T executeForObject(String sql) {
		List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entityClass));
		if(list.size() > 0) return list.get(0);
		return null;
	}

	/**
	 * @param sql
	 * @param params
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public T executeForObject(String sql, Object... params) {
		List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entityClass), params);
		if(list.size() > 0) return list.get(0);
		return null;
	}	
	
	/**
	 * @param sql
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public List<T> executeForList(String sql) {
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entityClass));
	}
	
	/**
	 * @param sql
	 * @param params
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public List<T> executeForList(String sql, Object... params) {
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entityClass), params);
	}
	
	/**
	 * @param sql
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public int executeForUpdate(String sql) {
		return jdbcTemplate.update(sql);
	}
	
	/**
	 * @param sql
	 * @param params
	 * @return
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public int executeForUpdate(String sql, Object... params) {
		return jdbcTemplate.update(sql, params);
	}
	
	/**
	 * @param sql
	 * @param entities
	 * yanzihui 2013-11-22 下午3:27:57
	 * @throws Exception 
	 */
	@Override
	public void executeForBatch(String sql, List<T> entities) throws Exception {
		List<Object[]> objss = new ArrayList<Object[]>();

		for (int i = 0; i < entities.size(); ++i) {
			T entity = StringHtmlEscape(entities.get(i));
			EntityUtils.GenerateEntityUUID(entity);
			List<Object> tempParams = EntityUtils.genInsertParams(entity, entityClass);
			objss.add(tempParams.toArray());

			if ((i + 1) % BATCH_COUNT == 0) {
				jdbcTemplate.batchUpdate(sql, objss);
				objss.clear();
			}
		}

		if (objss.size() > 0) jdbcTemplate.batchUpdate(sql, objss);
	}
	
	/**
	 * @param sql
	 * @param objss
	 * yanzihui 2013-11-22 下午3:27:57
	 */
	@Override
	public void executeForBatch2(String sql, List<Object[]> objss) {
		List<Object[]> params = new ArrayList<Object[]>();
		
		for (int i = 0; i < objss.size(); ++i) {
			params.add(objss.get(i));

			if ((i + 1) % BATCH_COUNT == 0) {
				jdbcTemplate.batchUpdate(sql, params);
				params.clear();
			}
		}

		if (params.size() > 0) jdbcTemplate.batchUpdate(sql, params);
	}
	
	public int clearOldData(int monthCount, String dateColName) {
		Calendar ca = Calendar.getInstance();
		ca.add(2, -monthCount);
		String strDate = DateUtils.date2Str(new Date(ca.getTimeInMillis()),
				"yyyy-MM-dd HH:mm:ss");

		String sql = "delete from %s where %s < to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
		sql = String.format(sql, new Object[] { this.tableName, dateColName });
		return getJdbcTemplate().update(sql, new Object[] { strDate });
	}
	
	public int clearOldDataByDay (int dayCount, String dateColName) {
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DAY_OF_MONTH, -dayCount);
		String strDate = DateUtils.date2Str(new Date(ca.getTimeInMillis()),
				"yyyy-MM-dd HH:mm:ss");

		String sql = "delete from %s where %s < to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
		sql = String.format(sql, new Object[] { this.tableName, dateColName });
		return getJdbcTemplate().update(sql, new Object[] { strDate });
	}
	
	public static String createId32(){
	    UUID guid = UUID.randomUUID();
	    return guid.toString().replace("-", "");
	  }

	@Override
	public List<String> getByKeyLists(String key, Integer distinct) {
		String sql = "";
		if(distinct == 0){
			sql = "select DISTINCT %s from %s ";
		}else if(distinct == 1){
			sql = "select %s from %s ";
		}else {
			sql = "select %s from %s ";
		}
		sql = String.format(sql, key, this.tableName);
		return getJdbcTemplate().queryForList(sql, String.class);
	}
	
	
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int insertObject(T obj) throws Exception {
		T t = StringHtmlEscape(obj);
		Object id = BeanUtils.getProperty(t, "id");
		String id32 = EntityUtils.createId32();
		if(id == null) BeanUtils.setProperty(t, "id", id32);
		String sql = EntityUtils.genInsertClause(t.getClass());
		List<Object> params = EntityUtils.genInsertParams(t, t.getClass());
		return executeForUpdate(sql, params.toArray());
	}
	/**
	 * string类型的数据进行转义(防止html注入)
	 * @param t
	 * @return
	 */
	private T StringHtmlEscape(T t){
		/*Field[] fields = t.getClass().getDeclaredFields();
		for (int i=0;i<fields.length;i++){
            try {
                Field field = fields[i];
                field.setAccessible(true);
                //String name = field.getName();
                Object value = field.get(t);
                if (value instanceof String) {
					field.set(t, HtmlUtils.htmlEscape(value.toString()));
				}
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }*/
		return t;
	}
}
