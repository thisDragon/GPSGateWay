package com.analog.data.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.analog.data.cache.JedisQueue;
import com.analog.data.entity.GpsDataSourceForwardEntity;
import com.analog.data.enums.LogStateEnum;
import com.analog.data.enums.LogTypeEnum;
import com.analog.data.exception.ParamException;
import com.analog.data.pojo.GpsDataSourcePojo;
import com.analog.data.service.IGpsDataSourceForwardService;
import com.analog.data.service.ILogDataService;
import com.analog.data.util.HttpRequest;

import jodd.util.StringUtil;

/**
* @ClassName: RedisMessageListener
* @Description: redis生产者消费者模式消息监听
* @author yangjianlong
* @date 2019年11月7日上午11:13:00
*
 */
@Component
public class QueueMessageListener implements InitializingBean{
   
    private static final Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);
    public static final String queueName = "queue.channel";			//生产者消费者模式的队列名称(该队列用于转发)
    public static final String saveDataQueue = "queue.saveData";	//生产者消费者模式的队列名称(该队列用于转发)
    private static ExecutorService executorService = Executors.newFixedThreadPool(12);//创建大小为12的线程池
    private static final int FAILURE_COUNT_MAX = 3;					//默认转发失败的最大次数
    private static final int QUEUE_MAX_COUNT = 1000;				//批处理转发消息队列
    private static final int SLEEP_TIME = 3000;						//休眠时间(单位:毫秒)
    
    @Autowired
    private IGpsDataSourceForwardService gpsDataSourceForwardService;
    @Autowired
    private ILogDataService logDataService;

    @Override
	public void afterPropertiesSet() throws Exception {
    	new Thread(() -> {
    		boolean isOpen = true;
            while (isOpen) {
                try {
                   this.consume();
                   Thread.sleep(SLEEP_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("开启消费者队列处理监听失败:"+e.getMessage());
                }
            }
        }).start();
	}
	
	public void consume(){
		//消息出队列,检查是否为空
		List<String> queueContents = JedisQueue.popMulti(queueName, QUEUE_MAX_COUNT);
		if (queueContents.size() > 0) {
			for (String queueContent : queueContents) {
				GpsDataSourcePojo gpsDataSourcePojo = JSONArray.parseObject(queueContent, GpsDataSourcePojo.class);
	        	executorService.execute(new ForwardDataThread(gpsDataSourcePojo));
			}
		}
	}
    
    /**
    * @ClassName: ForwardDataThread
    * @Description: 处理数据转发线程内部类
    * @author yangjianlong
    * @date 2019年11月13日下午4:32:09
    *
     */
    class ForwardDataThread implements Runnable {
    	
    	private GpsDataSourcePojo gpsDataSourcePojo;
    	
    	public ForwardDataThread(GpsDataSourcePojo gpsDataSourcePojo) {
			this.gpsDataSourcePojo = gpsDataSourcePojo;
		}
    	
		@Override
		public void run() {
			try {
				//数据转发
				String params = "{";
				params += "\"sourceType\"=\"" + gpsDataSourcePojo.getSourceType() + "\",";
				params += "\"deviceId\"=\"" + gpsDataSourcePojo.getDeviceId() + "\",";
				params += "\"lon\"=" + gpsDataSourcePojo.getLon() + ",";
				params += "\"lat\"=" + gpsDataSourcePojo.getLat() + ",";
				params += "\"position\"=\"" + gpsDataSourcePojo.getPosition() + "\",";
				params += "\"token\"=\"" + gpsDataSourcePojo.getToken() + "\",";
				params += "\"gpsTime\"=" + gpsDataSourcePojo.getGpsTime().getTime() + ",";
				params += "\"speed\"=\"" + gpsDataSourcePojo.getSpeed() + "\",";
				params += "\"remark\"=" + gpsDataSourcePojo.getRemark() ;
				params += "}" ;
				
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("sourceType", gpsDataSourcePojo.getSourceType());
				List<GpsDataSourceForwardEntity> forwardList = gpsDataSourceForwardService.getList(map);
				
				if (forwardList.size() > 0) {
					List<String> queues = new ArrayList<String>();
					for (GpsDataSourceForwardEntity forward : forwardList) {
						boolean isAuth = forward.getIsEnable() == 0? false:true;
						String forwardUrl = gpsDataSourcePojo.getForWardUrl();
						
						//根据gpsDataSourcePojo是否保存转发地址来确认是否是转发失败的数据进行再次转发
						String reponseString = HttpRequest.ERROR_VALUE;
						if (StringUtil.isEmpty(forwardUrl)) {
							forwardUrl = forward.getForwardUrl();
							reponseString = HttpRequest.authSendPostOfBody(forwardUrl, params, isAuth, forward.getAccount(), forward.getPassword());
						}else{
							reponseString = HttpRequest.authSendPostOfBody(forwardUrl, params, isAuth, forward.getAccount(), forward.getPassword());
						}
						logger.info("reponseString:"+reponseString);
						if (reponseString.equals(HttpRequest.ERROR_VALUE)) {
							gpsDataSourcePojo.setForWardUrl(forwardUrl);
							throw new ParamException("数据源为:"+gpsDataSourcePojo.getSourceType()+",转发地址:" + forwardUrl + " 进行转发出现异常");
						}else{
							String queue = JSONArray.toJSON(gpsDataSourcePojo).toString();
							queues.add(queue);
							logger.info("订阅转发成功的数据:" + queue);
							logDataService.saveLog(gpsDataSourcePojo, LogTypeEnum.SUBSCIBE_LOG.getState(), LogStateEnum.SUCCESS.getState(),null);
						}
					}
					JedisQueue.push(saveDataQueue, queues);
				}else{
					JedisQueue.push(saveDataQueue, JSONArray.toJSON(gpsDataSourcePojo).toString());
				}
				
			} catch (Exception e) {
				//请求转发超时或其他异常超过3次进行记录,少于3次则重新添加到消息队列中
				gpsDataSourcePojo.setFailureCount(gpsDataSourcePojo.getFailureCount()+1);
				if (gpsDataSourcePojo.getFailureCount() >= FAILURE_COUNT_MAX) {
					//记录数据库或者只记录到日志文件,并且不再添加回消息队列中
					try {
						logger.error("订阅转发异常:" + e.getMessage());
						logger.error("订阅转发异常的数据:" + JSONArray.toJSON(gpsDataSourcePojo).toString());
						JedisQueue.push(saveDataQueue, JSONArray.toJSON(gpsDataSourcePojo).toString());
						logDataService.saveLog(gpsDataSourcePojo, LogTypeEnum.SUBSCIBE_LOG.getState(), LogStateEnum.FAIL.getState(),null);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}else{
					String gpsdataJson = JSONArray.toJSON(gpsDataSourcePojo).toString();
					JedisQueue.push(queueName, gpsdataJson);
				}
			}
		}
    }
}