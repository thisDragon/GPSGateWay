package com.analog.data.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.analog.data.cache.JedisPoolWriper;
import com.analog.data.cache.JedisUtil;
import com.analog.data.queue.TopicMessageListener;
import redis.clients.jedis.JedisPoolConfig;

/**
* @ClassName: RedisConfig
* @Description: redis和spring-data-redis的配置
* @author yangjianlong
* @date 2019年12月27日上午9:22:16
*
 */
@Configuration
@PropertySource("classpath:redis.properties")
@ComponentScan(basePackages={"com.analog.data.queue"})
public class RedisConfig {
	
	@Autowired
	private Environment environment;
	//redis基础配置
	private int maxTotal;
	private int maxIdle;
	private int maxWaitMillis;
	private boolean testOnBorrow;
	
	private String host;
	private int port;
	private int timeout;
	private String password;
	private int database;
	//主题的名称
	private String channelTopicName = "topic.channel";
	private String patternTopicName = "topic.*";
	private String allTopicName = "*";
	
	@Bean(name = "jedisPoolConfig")
	public JedisPoolConfig jedisPoolConfig(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		maxTotal = environment.getProperty("redis.pool.maxActive",Integer.class);
		maxIdle = environment.getProperty("redis.pool.maxIdle",Integer.class);
		maxWaitMillis = environment.getProperty("redis.pool.maxWait",Integer.class);
		testOnBorrow = environment.getProperty("redis.pool.testOnBorrow",Boolean.class);
		
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		
		return jedisPoolConfig;
	}
	
	@Bean(name = "jedisWritePool")
	public JedisPoolWriper jedisWritePool(){
		host = environment.getProperty("redis.hostname");
		port = environment.getProperty("redis.port",Integer.class);
		timeout = environment.getProperty("redis.timeout",Integer.class);
		password = environment.getProperty("redis.password");
		database = environment.getProperty("redis.database",Integer.class);
		JedisPoolWriper jedisWritePool = new JedisPoolWriper(jedisPoolConfig(), host, port, timeout, password, database);
		return jedisWritePool;
	}
	
	@Bean(name = "jedisUtil")
	public JedisUtil jedisUtil(){
		JedisUtil jedisUtil = new JedisUtil();
		jedisUtil.setJedisPool(jedisWritePool());
		return jedisUtil;
	}

	@Bean(name = "jedisKeys")
	public JedisUtil.Keys jedisKeys(){
		JedisUtil jedisUtil = jedisUtil();
		JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
		
		return jedisKeys;
	}

	@Bean(name = "jedisStrings")
	public JedisUtil.Strings jedisStrings(){
		JedisUtil jedisUtil = jedisUtil();
		JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
		
		return jedisStrings;
	}

	@Bean(name = "jedisLists")
	public JedisUtil.Lists jedisLists(){
		JedisUtil jedisUtil = jedisUtil();
		JedisUtil.Lists jedisLists = jedisUtil.new Lists();
		
		return jedisLists;
	}

	@Bean(name = "jedisSets")
	public JedisUtil.Sets jedisSets(){
		JedisUtil jedisUtil = jedisUtil();
		JedisUtil.Sets jedisSets = jedisUtil.new Sets();
		
		return jedisSets;
	}
	
	@Bean(name = "jedisHash")
	public JedisUtil.Hash jedisHash(){
		JedisUtil jedisUtil = jedisUtil();
		JedisUtil.Hash jedisHash = jedisUtil.new Hash();
		
		return jedisHash;
	}
	
	@Bean(name = "jedisConnectionFactory")
	public JedisConnectionFactory jedisConnectionFactory(){
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		host = environment.getProperty("redis.hostname");
		port = environment.getProperty("redis.port",Integer.class);
		timeout = environment.getProperty("redis.timeout",Integer.class);
		password = environment.getProperty("redis.password");
		database = environment.getProperty("redis.database",Integer.class);
		
		jedisConnectionFactory.setHostName(host);
		jedisConnectionFactory.setPort(port);
		jedisConnectionFactory.setPassword(password);
		jedisConnectionFactory.setTimeout(timeout);
		jedisConnectionFactory.setUsePool(true);
		jedisConnectionFactory.setPoolConfig(jedisPoolConfig());
		
		return jedisConnectionFactory;
	}
	
	@Bean(name = "redisTemplate")
	public RedisTemplate<String, String> redisTemplate(){
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		return redisTemplate;
	}
	
	//消息监听
	@Bean(name = "topicMessageListener")
	public TopicMessageListener topicMessageListener(){
		TopicMessageListener topicMessageListener = new TopicMessageListener();
		topicMessageListener.setRedisTemplate(redisTemplate());
		return topicMessageListener;
	}
	
	//容器
	@Bean(name = "redisContainer")
	public RedisMessageListenerContainer redisContainer(){
		RedisMessageListenerContainer redisContainer = new RedisMessageListenerContainer();
		redisContainer.setConnectionFactory(jedisConnectionFactory());
		Map<MessageListener, Collection<? extends Topic>> listeners = new HashMap<MessageListener, Collection<? extends Topic>>();
		//普通订阅，订阅具体的频道
		ChannelTopic channelTopic = new ChannelTopic(channelTopicName);
		//模式订阅，支持模式匹配订阅，*为模糊匹配符
		PatternTopic patternTopic = new PatternTopic(patternTopicName);
		//匹配所有频道
		PatternTopic allTopic = new PatternTopic(allTopicName);
		
		Collection<Topic> topics =new ArrayList<Topic>(Arrays.asList(channelTopic,patternTopic,allTopic));
		listeners.put(topicMessageListener(), topics);
		redisContainer.setMessageListeners(listeners);
		return redisContainer;
	}
	
}