package com.analog.data.service;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.analog.data.dao.IBaseDao;


/**
* @ClassName: IBaseService
* @Description: service基类
* @author yangjianlong
* @date 2019年11月6日上午10:28:50
*
* @param <T>
 */
public interface IBaseService<T extends Serializable> {

	public abstract IBaseDao<T> getDao();

	public abstract Serializable insert(T e) throws Exception;

	public abstract void update(T e) throws Exception;

	public abstract void delete(T e);

	public abstract void deleteById(String id);

	public abstract int delEntityByKey(String propertyName, Object propertyValue)
			throws Exception;

	@Transactional(readOnly = true)
	public abstract T getById(String id);

	@Transactional(readOnly = true)
	public abstract T getEntityByKey(String propertyName, Object propertyValue)
			throws Exception;

	@Transactional(readOnly = true)
	public abstract boolean existEntityByKey(String propertyName,
			Object propertyValue) throws Exception;

	@Transactional(readOnly = true)
	public abstract List<T> getAll();

	@Transactional(readOnly = true)
	public abstract Long getAllCount();

	@Transactional(readOnly = true)
	public abstract boolean existById(String id);

	public abstract int delAll();

}