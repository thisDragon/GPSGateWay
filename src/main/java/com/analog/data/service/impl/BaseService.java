/**
 * 
 */
package com.analog.data.service.impl;

import java.io.Serializable;
import java.util.List;
import jodd.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;

import com.analog.data.dao.IBaseDao;
import com.analog.data.service.IBaseService;

/**
* @ClassName: BaseService
* @Description: 公共的service基类
* @author yangjianlong
* @date 2019年11月6日上午10:29:31
*
* @param <T>
 */
@Transactional(rollbackFor = Exception.class)
public abstract class BaseService<T extends Serializable> implements IBaseService<T> {

	public abstract IBaseDao<T> getDao();

	public Serializable insert(T e) throws Exception {
		return getDao().insert(e);
	}

	public void update(T e) throws Exception {
		getDao().update(e);
	}

	public void delete(T e) {
		getDao().delete(e);
	}
	public int deleteObject(T e) {
		return getDao().delete(e);
	}
	public void deleteById(String id) {
		getDao().deleteById(id);
	}
	
	public int delEntityByKey(String propertyName, Object propertyValue) throws Exception{
		if (StringUtil.isEmpty(propertyName))
			throw new Exception("属性名不能为空");
		if (propertyValue == null)
			throw new Exception("属性值不能为空");
		return getDao().deleteByKey(propertyName, propertyValue);
	}

	@Transactional(readOnly = true)
	public T getById(String id) {
		return getDao().getById(id);
	}

	@Transactional(readOnly = true)
	public T getEntityByKey(String propertyName, Object propertyValue)
			throws Exception {
		if (StringUtil.isEmpty(propertyName))
			throw new Exception("属性名不能为空");
		if (propertyValue == null)
			throw new Exception("属性值不能为空");
		return getDao().getByKey(propertyName, propertyValue);
	}
	
	@Transactional(readOnly = true)
	public boolean existEntityByKey(String propertyName, Object propertyValue) throws Exception{
		if (StringUtil.isEmpty(propertyName))
			throw new Exception("属性名不能为空");
		if (propertyValue == null)
			throw new Exception("属性名不能为空");
		return getDao().existByKey(propertyName, propertyValue);
	}

	@Transactional(readOnly = true)
	public List<T> getAll() {
		return getDao().getAll();
	}

	@Transactional(readOnly = true)
	public Long getAllCount() {
		return getDao().getAllCount();
	}

	@Transactional(readOnly = true)
	public boolean existById(String id) {
		return getDao().existById(id);
	}

	@Override
	public int delAll() {
		return getDao().deleteAll();
	}

}