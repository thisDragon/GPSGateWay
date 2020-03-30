/**
 * @author yanzihui
 */
package com.analog.data.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import jodd.util.StringUtil;

import org.hibernate.annotations.GenericGenerator;


/**
 * 
 * @author yanzihui
 * @date 2013-5-6 下午4:21:45
 */
public class EntityUtils {

	public static String genInsertClause(Class<?> clazz)
			throws Exception {
		String tableName = getTableName(clazz);
		return genInsertClause(clazz, tableName);
	}
	
	public static String genInsertClause(Class<?> clazz, String tablename)
			throws Exception {
		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		List<Field> fields = getFields(clazz);
		for (int i = 0; i < fields.size(); ++i) {
			Field field = (Field) fields.get(i);

			columns.append(getColumnName(clazz, (Field) fields.get(i)));
			if (i != fields.size() - 1)
				columns.append(", ");

			if (Date.class.isAssignableFrom(field.getType()))
				//values.append("to_date(?, 'yyyy-mm-dd HH24:mi:ss')");
				values.append("cast(? as datetime)");
			else
				values.append("?");
			if (i != fields.size() - 1)
				values.append(", ");
		}
		String sql = "insert into %s( %s ) values ( %s )";
		return String.format(sql, new Object[] { tablename, columns, values });
   }

	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		do {
			Field[] tempFields = clazz.getDeclaredFields();
			fields.addAll(Arrays.asList(tempFields));
		} while (!((clazz = clazz.getSuperclass()).equals(Object.class)));
		return fields;
	}

	public static String getColumnName(Class<?> clazz, Field entityField)
			throws Exception {
		Column annoColum = (Column) entityField.getAnnotation(Column.class);
		if (annoColum != null)
			return annoColum.name();
		String methodName = getGetMethodName(entityField);
		Method method = clazz.getMethod(methodName, new Class[0]);
		annoColum = (Column) method.getAnnotation(Column.class);
		if (annoColum != null && StringUtil.isNotEmpty(annoColum.name()))
			return annoColum.name();
		return entityField.getName();
	}

	public static List<Object> genInsertParams(Object entity, Class<?> clazz) {
		List<Field> fields = getFields(clazz);
		List<Object> params = new ArrayList<Object>();
		try {
			for (int i = 0; i < fields.size(); ++i) {
				Field field = (Field) fields.get(i);

				String methodName = getGetMethodName(field);
				Method method = clazz.getMethod(methodName, new Class[0]);
				Object result = method.invoke(entity, new Object[0]);

				if ((result != null)
						&& (Date.class.isAssignableFrom(field.getType())))
					result = DateUtils.date2Str((Date) result,
							"yyyy-MM-dd HH:mm:ss");

				params.add(result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return params;
	}

	public static String getGetMethodName(Field entityField) {
		String methodName = null;
		if (entityField.getType().equals(Boolean.class))
			methodName = "is" + normalize(entityField.getName());
		else
			methodName = "get" + normalize(entityField.getName());
		return methodName;
	}

	private static String normalize(String str) {
		if (str == null)
			return null;
		if (str.trim().length() == 0)
			return str;

		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(str.charAt(0)));
		sb.append(str.substring(1));
		return sb.toString();
	}

	public static String genUpdateClause(Class<?> clazz, String keyColumn)
			throws Exception {
		StringBuilder sb = new StringBuilder();

		List<Field> fields = getFields(clazz);
		for (Iterator<Field> localIterator = fields.iterator(); localIterator
				.hasNext();) {
			Field field = (Field) localIterator.next();
			Id id = field.getAnnotation(Id.class);
			if(id != null) continue;
			
			String methodName = getGetMethodName(field);
			Method method = clazz.getMethod(methodName);
			if(method != null){
				id = method.getAnnotation(Id.class);
				if(id != null) continue;
			}

			String columnName = getColumnName(clazz, field);
			if (columnName.equalsIgnoreCase(keyColumn))
				continue;

			sb.append(columnName);
			if (Date.class.isAssignableFrom(field.getType())) {
				//sb.append(" = ").append("to_date(?, 'yyyy-mm-dd HH24:mi:ss')") .append(", ");
				sb.append(" = ").append("cast(? as datetime)") .append(", ");
				continue;
			}
			sb.append(" = ?, ");
		}

		String columns = sb.toString().trim().replaceAll(",$", "");
		String tableName = getTableName(clazz);

		String sql = "update %s set %s where %s = ?";
		return String.format(sql,
				new Object[] { tableName, columns, keyColumn });
	}

	public static String getTableName(Class<?> entityClazz) {
		String tableName = null;

		Table annoTable = (Table) entityClazz.getAnnotation(Table.class);
		if (annoTable != null) {
			tableName = annoTable.name();
		} else {
			tableName = entityClazz.getName();
			if (tableName.indexOf("Entity") != -1)
				tableName = tableName.substring(0, tableName.indexOf("Entity"));
		}

		return tableName;
	}
	
	public static <T> void GenerateEntityUUID(T entity) throws Exception{
		List<Field> fields = getFields(entity.getClass());
		for (Iterator<Field> localIterator = fields.iterator(); localIterator
				.hasNext();) {
			Field field = (Field) localIterator.next();
			String methodName = getGetMethodName(field);
			Method method = entity.getClass().getMethod(methodName);
			if(method == null) continue;
			GenericGenerator genericGenerator = (GenericGenerator) method.getAnnotation(GenericGenerator.class);	
			if(genericGenerator == null || !genericGenerator.strategy().equalsIgnoreCase("uuid")) continue;
			method = entity.getClass().getMethod("set" + StringUtil.capitalize(field.getName()), String.class);
			if(method == null) continue;
			method.invoke(entity, UUID.randomUUID().toString().replace("-", ""));
		}
	}

	public static List<Object> genUpdateParams(Object bean, String keyColumn,
			Class<?> clazz) throws Exception {
		List<Object> params = new ArrayList<Object>();
		Object keyValue = null;

		List<Field> fields = getFields(clazz);
		for (Iterator<Field> localIterator = fields.iterator(); localIterator
				.hasNext();) {
			Field field = (Field) localIterator.next();
			Object result = BeanUtils.getProperty(bean, field.getName());
			String columnName = getColumnName(clazz, field);
			if (columnName.equalsIgnoreCase(keyColumn)) {
				if (result == null)
					throw new RuntimeException(keyColumn + "字段值为null");
				keyValue = result;
				continue;
			}
			
			Id id = field.getAnnotation(Id.class);
			if(id != null) continue;
			
			String methodName = getGetMethodName(field);
			Method method = clazz.getMethod(methodName);
			if(method != null){
				id = method.getAnnotation(Id.class);
				if(id != null) continue;
			}
			
			if ((result != null)
					&& (Date.class.isAssignableFrom(field.getType()))) {
				result = DateUtils.date2Str((Date) result,
						"yyyy-MM-dd HH:mm:ss");
			}

			params.add(result);
		}

		params.add(keyValue);
		return params;
	}
	
	public static String createId32(){
	    UUID guid = UUID.randomUUID();
	    return guid.toString().replace("-", "");
	}
	
}
