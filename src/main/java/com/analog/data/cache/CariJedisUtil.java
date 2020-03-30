package com.analog.data.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.analog.data.util.Utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jedis的util类
 * @author yexuhui
 * 2012-2-8 下午05:49:44
 */
public class CariJedisUtil {
	
	private static JedisPool pool;
	private static Logger logger = LoggerFactory.getLogger(CariJedisUtil.class);
	
	private static final String REDIS_SERVER = "redis.hostname";
	private static final String REDIS_PORT = "redis.port";
	private static final String REDIS_PASSWORD = "redis.password";
	private static final String REDIS_TIMEOUT = "redis.timeout";
	
	private static final String POOL_MAXIDLE = "redis.pool.maxIdle";
	private static final String POOL_MAXACTIVE = "redis.pool.maxActive";
	private static final String POOL_MAXWAIT = "redis.pool.maxWait";
	private static final String REDIS_DATABASE = "redis.database";
	
	public static final String REDIS_NULL_STRING = "nil";
	/**缓存类使用**/
	public static final String EMPTY_NODE = "$$empty$$";
	
	/**
	 * 初始化pool
	 */
	static{
		try {
			Properties properties = Utils.loadProperties("redis.properties");
			
			String server = properties.getProperty(REDIS_SERVER);
			String port = properties.getProperty(REDIS_PORT);
			String password = properties.getProperty(REDIS_PASSWORD);
			String timeout = properties.getProperty(REDIS_TIMEOUT);
			String maxIdle = properties.getProperty(POOL_MAXIDLE);
			String maxActive = properties.getProperty(POOL_MAXACTIVE);
			String maxWait = properties.getProperty(POOL_MAXWAIT);
			String redisDataBase = properties.getProperty(REDIS_DATABASE);
			if(StringUtil.isEmpty(redisDataBase)) redisDataBase = "0";
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(Integer.parseInt(maxIdle));
			config.setMaxTotal(Integer.parseInt(maxActive));
			config.setMaxWaitMillis(Integer.parseInt(maxWait));
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			
			pool = new JedisPool(config, server, Integer.parseInt(port), 
					Integer.parseInt(timeout), password, Integer.parseInt(redisDataBase));			
		} catch (Exception e) {
			logger.error("Jedis初始化失败", e);
			throw new RuntimeException("Jedis初始化失败，建议退出应用", e);
		}
	}
	
	/**
	 * 从pool获取一个jedis连接
	 * @return
	 */
	public static Jedis getJedis() {
		if(pool != null) return pool.getResource();
		return null;
	}
	
	/**
	 * 返回jedis连接给pool
	 * @param jedis
	 */
	public static void returnJedis(Jedis jedis) {
		if(pool != null) pool.returnResourceObject(jedis);
	}
	
	/**
	 * 关闭pool
	 */
	public static void destroyPool(){
		if(pool != null) pool.destroy();
	}
	
	/**
	 * 获取value
	 * @param key 
	 * @return value
	 */
	public static String get(String key){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.get(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}
	
	/**
	 * set对象，未设置过期时间
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value){
		Jedis jedis = null;
		try {
			jedis = CariJedisUtil.getJedis();
			jedis.set(key, value);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}
	
	/**
	 * set对象，包含过期时间
	 * @param key
	 * @param value
	 * @param expire 过期秒数
	 */
	public static void setex(String key, String value, int expire){
		Jedis jedis = null;
		try {
			jedis = CariJedisUtil.getJedis();
			jedis.setex(key, expire, value);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}	
	
	/**
	 * 删除key对应的缓存
	 * @param key
	 * @return
	 */
	public static long del(String key){
		Jedis jedis = null;
		try {
			jedis = CariJedisUtil.getJedis();
			return jedis.del(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}

	/**
	 * 判断缓存中是否存在某个缓存
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key){
		Jedis jedis = null;
		try {
			jedis = CariJedisUtil.getJedis();
			return jedis.exists(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	/**
	 * 设置过期
	 * @param key
	 * @param expire 过期秒数
	 */
	public static void expire(String key, int expire){
		Jedis jedis = CariJedisUtil.getJedis();
		try {
			jedis.expire(key, expire);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	/**
	 * 获取剩余的过期时间
	 * @param key
	 * @return
	 */
	public static long ttl(String key) {
		Jedis jedis = CariJedisUtil.getJedis();
		try {
			return jedis.ttl(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}
	
	/**
	 * 根据匹配模式获取相关的key
	 * @param pattern
	 * @return
	 */
	public static Set<String> keys(String pattern){
		Jedis jedis = CariJedisUtil.getJedis();
		try {
			return jedis.keys(pattern);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}	
	
	public static long incr(String key){
		Jedis jedis = CariJedisUtil.getJedis();
		try {
			return jedis.incr(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}	
	
	/**
	 * 根据key和field，从MAP结构缓存中获取fieldValue
	 * @param key
	 * @param fieldName
	 * @return
	 */
	public static String hget(String key, String fieldName){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			String value = jedis.hget(key, fieldName);
			if((value == null) || value.equals(CariJedisUtil.REDIS_NULL_STRING)) return null;
			return value;
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}
	
	/**
	 * 从MAP结构缓存中获取多个对象
	 * @param key
	 * @param lstFieldName
	 * @return
	 */
	public static List<String> hmget(String key, List<String> lstFieldName){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			if(lstFieldName.size() == 0) return new ArrayList<String>();
			String[] strArr = new String[lstFieldName.size()];
			for(int i=0; i<lstFieldName.size(); i++) strArr[i] = lstFieldName.get(i);
			
			return jedis.hmget(key, strArr);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}
	
	/**
	 * 从MAP结构缓存中获取多个对象
	 * @param key
	 * @param lstFieldName
	 * @return
	 */
	public static Map<String, String> hgetAll(String key){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			return jedis.hgetAll(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}	
	
	/**
	 * 往MAP结构的缓存中添加一个值
	 * @param key
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void hset(String key, String fieldName, String fieldValue){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			jedis.hset(key, fieldName, fieldValue);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	/**
	 * 往MAP结构的缓存中添加多个值
	 * @param key
	 * @param hash
	 */
	public static void hmset(String key, Map<String, String> hash){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			jedis.hmset(key, hash);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	/**
	 * 从MAP的缓存结构中删除一个键值
	 * @param key
	 * @param fieldName
	 */
	public static long hdel(String key, String fieldName){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			return jedis.hdel(key, fieldName);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	/**
	 * 从MAP的缓存结构中获取所有的KEY
	 *@param key
	 *@return
	 *yanzihui
	 */
	public static Set<String> hkeys(String key){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
		   return jedis.hkeys(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	/**
	 * 获取MAP的缓存结构的size
	 *@param key
	 *@return
	 */
	public static long hlen(String key){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
		   return jedis.hlen(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	/**
	 * 判断MAP结构的缓存中是否存在某个键值
	 * @param key
	 * @param fieldName
	 * @return
	 */
	public static boolean hexists(String key, String fieldName){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			return jedis.hexists(key, fieldName);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	public static Set<String> smembers(String key){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			return jedis.smembers(key);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}
	
	public static void sadd(String key, String member){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			jedis.sadd(key, member);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}
	
	public static void smadd(String key, Set<String> members){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			for (String member : members) {
				if(member != null) jedis.sadd(key, member);
			}
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}	
	
	public static long sdel(String key, String member){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			return jedis.srem(key, member);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}
	
	public static boolean sexists(String key, String member){
		Jedis jedis = CariJedisUtil.getJedis();
		try{
			return jedis.sismember(key, member);
		} finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}		
	}	
	
	public static String flushAll(){
		Jedis jedis = CariJedisUtil.getJedis();
		try {
			return jedis.flushAll();
		}  finally{
			if(jedis != null){
				jedis.close();
				jedis = null;
			} 
		}	
	}
	
	public static void main(String[] args) throws InterruptedException {
		Jedis jedis = null;
		String RESOUSEINFO_KEY=  "resouseinfo_";
		try {
			jedis = CariJedisUtil.getJedis();
//			Map<String, String> terminalMap = resouseInfoCache.getValue(richTalkTerminal.getTerminal());
			//System.out.println(JedisUtil.hgetAll(RESOUSEINFO_KEY + "13348552067").size());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
