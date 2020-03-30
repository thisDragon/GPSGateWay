package com.analog.data.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: MapCache
 * @Description: 全局缓存
 * @author yangjianlong
 * @date 2020年3月6日上午11:44:40
 *
 */
public class MapCache {

	private static final Logger logger = LoggerFactory.getLogger(MapCache.class);
	private static Map<Object, Object> cacheMap = new ConcurrentHashMap<Object, Object>();

	public static void destoryCacheMap() {
		cacheMap = null;
	}

	public static Map<Object, Object> getCacheMap() {
		return cacheMap;
	}

	public static void set(Object key, Object values) {
		cacheMap.put(key, values);
	}

	public static Object get(Object key) {
		return cacheMap.get(key);
	}

	public static String getString(Object key) {
		return (String) cacheMap.get(key);
	}

	public static Object getToEmpty(Object key) {
		Object o = cacheMap.get(key);
		if (o == null)
			return "";
		else
			return o;
	}

	public static void remove(Object key) {
		cacheMap.remove(key);
	}

	public static void clear() {
		cacheMap.clear();
	}
}
