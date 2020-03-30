package com.analog.data.cache;

import java.util.List;

import jodd.util.StringUtil;
import redis.clients.jedis.Jedis;

/**
 * jedis队列操作类
 * @author yexuhui
 * 2012-2-8 下午05:13:11
 */
public class JedisQueue {
	
	/**
	 * 往队列中插入一个对象
	 * @param queueName 队列名
	 * @param value 插入值
	 * @return 队列长度
	 */
	public static long push(String queueName, String value) {
		if(StringUtil.isEmpty(queueName)) throw new RuntimeException("队列名不能为空");
		
		Jedis jedis = null;
		try{
			jedis = CariJedisUtil.getJedis();
			return jedis.rpush(queueName, value);
		} finally{
			if(jedis != null) CariJedisUtil.returnJedis(jedis);
		}
	}
	
	/**
	 * 往队列中插入多个对象
	 * @param queueName 队列名
	 * @param values 要插入的列表
	 * @return 目前固定返回1
	 * @author yexuhui
	 * @date 2013-5-6 上午9:41:49
	 */
	public static long push(String queueName, List<String> values) {
		if(StringUtil.isEmpty(queueName)) throw new RuntimeException("队列名不能为空");
		Jedis jedis = null;
		try{ 
			jedis = CariJedisUtil.getJedis();
			for(String value : values) {
				jedis.rpush(queueName, value);
			}
			return 1; 
		} finally{
			if(jedis != null) CariJedisUtil.returnJedis(jedis);
		}
	}	
	
	/**
	 * 队列中弹出一个对象
	 * @param queueName 队列名
	 * @return 弹出的对象
	 */
	public static String pop(String queueName) {
		if(StringUtil.isEmpty(queueName)) throw new RuntimeException("队列名不能为空");
		
		Jedis jedis = null;
		try{
			jedis = CariJedisUtil.getJedis();
			String value = jedis.lpop(queueName);
			if((value == null) || value.equals(CariJedisUtil.REDIS_NULL_STRING)) return null;
			return value;
		} finally{
			if(jedis != null) CariJedisUtil.returnJedis(jedis);
		}		
	}
	
	/**
	 * 队列中弹出多个对象
	 * @param queueName 队列名
	 * @param count 弹出的对象个数
	 * @return 弹出的所有对象
	 */
	public static List<String> popMulti(String queueName, int count) {
		if(count <= 0) return null;
		if(StringUtil.isEmpty(queueName)) throw new RuntimeException("队列名不能为空");
		
		Jedis jedis = null;
		try{
			jedis = CariJedisUtil.getJedis();
			List<String> result = jedis.lrange(queueName, 0, count - 1);
			jedis.ltrim(queueName, count, -1);
			return result;
		} finally{
			if(jedis != null) CariJedisUtil.returnJedis(jedis);
		}			
	}	
	
	/**
	 * 获取队列的size
	 *@param queueName
	 *@return
	 *yanzihui
	 */
	public static long llen (String queueName) {
        if(StringUtil.isEmpty(queueName)) throw new RuntimeException("队列名不能为空");
		
		Jedis jedis = null;
		try{
			jedis = CariJedisUtil.getJedis();
			return jedis.llen(queueName);
		} finally{
			if(jedis != null) CariJedisUtil.returnJedis(jedis);
		}	
	}
}
