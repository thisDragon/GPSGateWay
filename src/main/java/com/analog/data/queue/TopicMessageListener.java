package com.analog.data.queue;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.analog.data.entity.GpsDataSourceEntity;
import com.analog.data.exception.ParamException;
import com.analog.data.pojo.GpsDataSourcePojo;
import com.analog.data.service.IGpsDataSourceService;
import com.analog.data.util.HttpRequest;
import jodd.util.StringUtil;

/**
* @ClassName: RedisMessageListener
* @Description: redis发布订阅模式消息监听
* @author yangjianlong
* @date 2019年11月7日上午11:13:00
*
 */
@Component
public class TopicMessageListener implements MessageListener {
   
    private static final Logger logger = LoggerFactory.getLogger(TopicMessageListener.class);
    public static final String topicName = "topic.channel";	//发布订阅模式的主题名称
    //private static ExecutorService executorService = Executors.newFixedThreadPool(8);//创建大小为8的线程池
    
    private String url = "http://127.0.0.1:9086/GPSGateWay/test3";
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IGpsDataSourceService gpsDataSourceService;

    /**
     * 队列监听
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
    	//redisTemplate.convertAndSend(TopicMessageListener.topicName, gpsdataJson);
    	
        /*RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
        Object channel = serializer.deserialize(message.getChannel());
        Object body = serializer.deserialize(message.getBody());
        System.out.println("队列的监听");
        if (topicName.equals(channel.toString())) {
        	GpsDataSourcePojo gpsDataSourcePojo = JSONArray.parseObject(body.toString(), GpsDataSourcePojo.class);
        	executorService.execute(new ForwardDataThread(gpsDataSourcePojo));
		}*/
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
    * @ClassName: MapDataThread
    * @Description: 处理数据转发线程内部类
    * @author yangjianlong
    * @date 2019年11月13日下午4:32:09
    *
     *//*
    class ForwardDataThread implements Runnable {
    	
    	private GpsDataSourcePojo gpsDataSourcePojo;
    	
    	public ForwardDataThread(GpsDataSourcePojo gpsDataSourcePojo) {
			this.gpsDataSourcePojo = gpsDataSourcePojo;
		}
    	
		@Override
		public void run() {
			try {
				System.out.println("线程的处理");
				//数据转发
				String params = "";
				params += "sourceType=" + gpsDataSourcePojo.getSourceType() + "&";
				params += "deviceID=" + gpsDataSourcePojo.getDeviceID() + "&";
				params += "lon=" + gpsDataSourcePojo.getLon() + "&";
				params += "lat=" + gpsDataSourcePojo.getLat() + "&";
				params += "posintion=" + gpsDataSourcePojo.getPosintion() + "&";
				params += "token=" + gpsDataSourcePojo.getToken() + "&";
				params += "remark=" + gpsDataSourcePojo.getRemark() ;
				
				Map<String, String> reponseMap = HttpRequest.sendGet(url, params);
				if (StringUtil.isNotEmpty(reponseMap.get("exceptionInfo"))) {
					throw new ParamException(reponseMap.get("exceptionInfo"));
				}else{
					GpsDataSourceEntity gpsDataSourceEntity = new GpsDataSourceEntity();
					gpsDataSourceEntity.setId(gpsDataSourcePojo.getId());
					gpsDataSourceEntity.setDeviceID(gpsDataSourcePojo.getDeviceID());
					gpsDataSourceEntity.setLon(gpsDataSourcePojo.getLon());
					gpsDataSourceEntity.setLat(gpsDataSourcePojo.getLat());
					gpsDataSourceEntity.setPosintion(gpsDataSourcePojo.getPosintion());
					gpsDataSourceEntity.setRemark(gpsDataSourcePojo.getRemark());
					gpsDataSourceEntity.setSourceType(gpsDataSourcePojo.getSourceType());
					gpsDataSourceEntity.setToken(gpsDataSourcePojo.getToken());
					gpsDataSourceEntity.setCreateTime(gpsDataSourcePojo.getCreateTime());
					
					gpsDataSourceService.addGpsDate(gpsDataSourceEntity);
				}
			} catch (Exception e) {
				//请求转发超时或其他异常超过3次进行记录,少于3次则重新添加到消息队列中
				gpsDataSourcePojo.setFailureCount(gpsDataSourcePojo.getFailureCount()+1);
				if (gpsDataSourcePojo.getFailureCount() >=3) {
					//TODO 记录数据库或者只记录到日志文件
					logger.error("记录转发异常:"+e.getMessage());
				}else{
					String gpsdataJson = JSONArray.toJSON(gpsDataSourcePojo).toString();
					redisTemplate.convertAndSend(TopicMessageListener.topicName, gpsdataJson);
				}
			}
		}
    }*/
}