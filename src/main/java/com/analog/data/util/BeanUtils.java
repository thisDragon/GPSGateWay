package com.analog.data.util;

import java.beans.BeanInfo;
import java.beans.Expression;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean操作类
 * 属性必须要有get和set方法
 * @author yexuhui
 *
 */
public class BeanUtils {
	
	/**
	 * 获取对象属性描述对象
	 * @param bean 对象
	 * @param name 属性名
	 * @return 属性描述对象
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String name) {
		try {
			if(clazz == null) throw new Exception("实体类不能为null");
			if(name == null || name.length() == 0) throw new Exception("属性名不能为空");
			
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : descriptors) {
				if(propertyDescriptor.getName().equalsIgnoreCase(name)) return propertyDescriptor;
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
		try {
			if(clazz == null) throw new Exception("实体类不能为null");
			
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			PropertyDescriptor[] tempDescriptors = new PropertyDescriptor[descriptors.length - 1];
			
			int i=0;
			for (PropertyDescriptor propertyDescriptor : descriptors) {
				if(propertyDescriptor.getName().equals("class")) continue;
				tempDescriptors[i++] = propertyDescriptor;
			}
			return tempDescriptors;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 获取对象属性值
	 * @param bean 实体对象
	 * @param name 属性名
	 * @return 属性值
	 */
	public static Object getProperty(Object bean, String name) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(bean.getClass(), name);
			if(descriptor == null) throw new Exception("无该属性：" + name);
			
			Expression expression = new Expression(bean, descriptor.getReadMethod().getName(), new Object[]{});
			return expression.getValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 获取属性类别
	 * @param clazz 实体类
	 * @param name 属性名
	 * @return
	 */
	public static Class<?> getPropertyType(Class<?> clazz, String name) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(clazz, name);
			if(descriptor == null) throw new Exception("无该属性：" + name);
			return descriptor.getPropertyType();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 		
	}
	
	/**
	 * 判断类是否存在该属性
	 * @param clazz 类
	 * @param name 属性名
	 * @return 
	 */
	public static boolean hasProperty(Class<?> clazz, String name) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(clazz, name);
			return descriptor != null;
		} catch (Exception e) {
			return false;
		}  
	}
	
	/**
	 * 设置属性值
	 * @param bean 实体对象
	 * @param name 属性名
	 * @param value 属性值
	 */
	public static void setProperty(Object bean, String name, Object value) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(bean.getClass(), name);
			if(descriptor == null) throw new Exception("无该属性：" + name);
			
			Expression expression = new Expression(bean, descriptor.getWriteMethod().getName(), new Object[]{value});
			expression.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 		
	}
	
	/**
	 * 设置属性值
	 * @param bean 实体对象
	 * @param name 属性名
	 * @param value 属性值
	 */
	public static void setProperty(Object bean, String name, String value) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(bean.getClass(), name);
			if(descriptor == null) throw new Exception("无该属性：" + name);
			
			PropertyEditor pEditor = PropertyEditorManager.findEditor(descriptor.getPropertyType());
			if(pEditor == null) throw new Exception("无法设置该属性：" + name);
			pEditor.setAsText(value);
			
			Expression expression = new Expression(bean, 
					descriptor.getWriteMethod().getName(), 
					new Object[]{pEditor.getValue()});
			expression.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 		
	}
	
	/**
	 * 用MAP对象填充实体
	 * 注意：Map对象中的value类型必须和实体属性的类型要一致
	 * @param bean 实体对象
	 * @param map 属性MAP
	 */
	public static void populate(Object bean, Map<String, Object> map) {
		try {
			if(bean == null) throw new Exception("实体对象不能为null");
			if(map == null) throw new Exception("Map对象不能为null");
			
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				if(map.containsKey(descriptor.getName())) {
					Method method = descriptor.getWriteMethod();
					if(method == null) continue;
					Expression expression = new Expression(bean, method.getName(), 
								new Object[]{map.get(descriptor.getName())});
					expression.execute();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 		
	}
	
	/**
	 * 将属性从源实体对象拷贝到目标实体对象
	 * @param src 源实体对象
	 * @param dest 目标实体对象
	 */
	public static void copyProperties(Object src, Object dest) {
		Map<String, Object> map = describe(src);
		populate(dest, map);
	}
	
	/**
	 * 从实体对象中取出所有的属性，Map的key为属性名，Map的value为属性值
	 * @param bean 实体对象
	 * @return 属性MAP
	 */
	public static Map<String, Object> describe(Object bean) {
		try {
			if(bean == null) throw new Exception("实体对象不能为null");
			Map<String, Object> map = new HashMap<String, Object>();
			
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				if(descriptor.getName().equals("class")) continue;
				Expression expression = new Expression(bean, descriptor.getReadMethod().getName(), new Object[]{});
				map.put(descriptor.getName(), expression.getValue());
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 	
	}
	
	/**
	 * 从实体类中取出属性名和属性类型的MAP
	 * @param clazz 实体类
	 * @return
	 */
	public static Map<String, Class<?>> describeType(Class<?> clazz) {
		try {
			if(clazz == null) throw new Exception("实体类不能为null");
			Map<String, Class<?>> map = new HashMap<String, Class<?>>();
			
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				if(descriptor.getName().equals("class")) continue;
				map.put(descriptor.getName(), descriptor.getPropertyType());
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 			
	}
	
	public static Map<String,Object> getModedPropertyValues(Object src, Object dest){
		 
		Map<String, Object> srcMap = describe(src);
		Map<String, Object> destMap = describe(dest);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> m : srcMap.entrySet()) {	
			 String key = m.getKey();
			 Object srcValue = m.getValue(); 
			 Object destValue = destMap.get(key);
			 
			if(srcValue != null&& !srcValue.equals(destValue)){
				resultMap.put(key, srcValue);
			}else if(srcValue == null&& destValue != null){
				resultMap.put(key, srcValue);
			}
		 }
		 return resultMap;
	}
	
	public static Method getGetMethod(Class<?> clazz, String name) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(clazz, name);
			if(descriptor == null) throw new Exception("无该属性：" + name);
			return descriptor.getReadMethod();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 根据Class和属性名获取GET的方法名
	 * @param clazz 类class
	 * @param name 属性名
	 * @return GET方法名
	 */
	public static String getGetMethodName(Class<?> clazz, String name) {
		return getGetMethod(clazz, name).getName();
	}
	
	public static Method getSetMethod(Class<?> clazz, String name) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(clazz, name);
			if(descriptor == null) throw new Exception("无该属性：" + name);
			return descriptor.getWriteMethod();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 	
	}
	
	/**
	 * 根据Class和属性名获取SET的方法名
	 * @param clazz 类class
	 * @param name 属性名
	 * @return SET方法名
	 */
	public static String getSetMethodName(Class<?> clazz, String name) {
		return getSetMethod(clazz, name).getName();		
	}
}
