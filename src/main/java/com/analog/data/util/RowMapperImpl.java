package com.analog.data.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperImpl<T> implements RowMapper<T> {

	private Class<T> entityClass = null;

	public RowMapperImpl(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

	static {
		primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
		primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
		primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
		primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
		primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
		primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
		primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
		primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
	}

	private void callSetter(Object target, PropertyDescriptor prop, Object value)
			throws SQLException, SecurityException, NoSuchMethodException {

		Method setter = prop.getWriteMethod();

		if (setter == null) {
			return;
		}

		Class<?>[] params = setter.getParameterTypes();
		try {
			if (value instanceof java.util.Date) {
				final String targetType = params[0].getName();
				if ("java.sql.Date".equals(targetType)) {
					value = new java.sql.Date(
							((java.util.Date) value).getTime());
				} else if ("java.sql.Time".equals(targetType)) {
					value = new java.sql.Time(
							((java.util.Date) value).getTime());
				} else if ("java.sql.Timestamp".equals(targetType)) {
					value = new java.sql.Timestamp(
							((java.util.Date) value).getTime());
				}
			}

			if (this.isCompatibleType(value, params[0])) {
				setter.invoke(target, new Object[] { value });
			} else {
				throw new SQLException();
			}

		} catch (IllegalArgumentException e) {
			throw new SQLException();

		} catch (IllegalAccessException e) {
			throw new SQLException();

		} catch (InvocationTargetException e) {
			throw new SQLException();
		}
	}

	private boolean isCompatibleType(Object value, Class<?> type) {
		if (value == null || type.isInstance(value)) {
			return true;

		} else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
			return true;

		} else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
			return true;

		} else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
			return true;

		} else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
			return true;

		} else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
			return true;

		} else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
			return true;

		} else if (type.equals(Character.TYPE)
				&& Character.class.isInstance(value)) {
			return true;

		} else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
			return true;

		}
		return false;

	}

	private Object processColumn(ResultSet rs, String columnName,
			Class<?> propType) throws SQLException {

		if (!propType.isPrimitive() && rs.getObject(columnName) == null) {
			return null;
		}

		if (propType.equals(String.class)) {
			return rs.getString(columnName);

		} else if (propType.equals(Integer.TYPE)
				|| propType.equals(Integer.class)) {
			return Integer.valueOf(rs.getInt(columnName));

		} else if (propType.equals(Boolean.TYPE)
				|| propType.equals(Boolean.class)) {
			return Boolean.valueOf(rs.getBoolean(columnName));

		} else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
			return Long.valueOf(rs.getLong(columnName));

		} else if (propType.equals(Double.TYPE)
				|| propType.equals(Double.class)) {
			return Double.valueOf(rs.getDouble(columnName));

		} else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
			return Float.valueOf(rs.getFloat(columnName));

		} else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
			return Short.valueOf(rs.getShort(columnName));

		} else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
			return Byte.valueOf(rs.getByte(columnName));

		}else if (propType.equals(Date.class)) {
			return new Date(rs.getTimestamp(columnName).getTime());

		}else if (propType.equals(Timestamp.class)) {
			return rs.getTimestamp(columnName);

		} else if (propType.equals(SQLXML.class)) {
			return rs.getSQLXML(columnName);

		} else {
			return rs.getObject(columnName);
		}

	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T bean = null;
		try {
			bean = (T) entityClass.newInstance();
			List<Field> fileds = EntityUtils.getFields(entityClass);
			for (Field field : fileds) {
				String columnName = EntityUtils.getColumnName(entityClass,
						field);

				Class<?> propType = field.getType();

				Object value = processColumn(rs, columnName, propType);

				if (propType != null && value == null && propType.isPrimitive()) {
					value = primitiveDefaults.get(propType);
				}
				PropertyDescriptor prop = BeanUtils.getPropertyDescriptor(entityClass, field.getName());
				this.callSetter(bean, prop, value);
			}
		} catch (Exception e) {

		}

		return bean;
	}

}
