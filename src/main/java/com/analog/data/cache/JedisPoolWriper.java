package com.analog.data.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 强指定redis的JedisPool接口构造函数，这样才能在centos成功创建jedispool
 * 
 * @author xiangze
 *
 */
public class JedisPoolWriper {
	private JedisPool jedisPool;

	public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host,final int port,
						   final int timeout, final String password, final int database) {
		try {
			jedisPool = new JedisPool(poolConfig, host, port, timeout, password, database);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

}
