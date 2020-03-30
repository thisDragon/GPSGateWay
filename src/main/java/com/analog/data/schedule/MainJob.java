package com.analog.data.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.analog.data.cache.JedisQueue;
import com.analog.data.dao.IGpsDataSourceConfigDao;
import com.analog.data.dao.IGpsDataSourceDao;
import com.analog.data.entity.GpsDataSourceConfigEntity;
import com.analog.data.entity.GpsDataSourceEntity;
import com.analog.data.pojo.GpsDataSourcePojo;
import com.analog.data.queue.QueueMessageListener;
import com.analog.data.service.IGpsDataSourceService;
import com.analog.data.service.ILogDataService;
import com.analog.data.util.DateUtils;

/**
* @ClassName: MainJob
* @Description: 定时器
* @author yangjianlong
* @date 2020年1月7日上午10:05:21
*
 */
@Component
@PropertySource("classpath:schedule.properties")
public class MainJob {

	private static final Logger logger = LoggerFactory.getLogger(MainJob.class);
	private static int count = 0;
	private static final int OVER_TIME = 300;			//时间阈值,超过300秒		满足其中一个条件进行入库
	private static final int OVER_COUNT = 1000;			//数量阈值,超过1000条	满足其中一个条件进行入库
	private static ExecutorService executorService = Executors.newFixedThreadPool(8);//创建大小为8的线程池
	private static int exceptionCount = 0;				//异常数
	private static final int EXCEPTION_WARN = 1000;		//异常警告值,超过该值发出系统警告(微信和电子邮件)
	
	
	@Autowired
	private ILogDataService logDataService;
	@Autowired
    private IGpsDataSourceService gpsDataSourceService;
	@Autowired
	private IGpsDataSourceConfigDao gpsDataSourceConfigDao;
	@Autowired
	private IGpsDataSourceDao gpsDataSourceDao;
	@Autowired
	private Environment environment;
	
	/**
	 * @Title: exceptionWarn   
	 * @Description: 系统异常发出警告
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月30日 上午10:11:28
	 */
	@Scheduled( fixedDelay = 1000 )
	public void exceptionWarn() {
		try {
			JedisQueue.llen(QueueMessageListener.saveDataQueue);
			exceptionCount = 0;
		} catch (Exception e) {
			exceptionCount++;
			e.printStackTrace();
			logger.error("连接redis出现异常的次数:" + exceptionCount);
			if (exceptionCount > EXCEPTION_WARN) {
				//TODO
			}
		}
	}
	
	/**
	 * @Title: batchAddDataSource   
	 * @Description: 当队列数超过1000或者时间超过300s进行一次批量入库  
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月6日 下午3:51:23
	 */
	@Scheduled( fixedDelay = 1000 )
	public void batchAddDataSource() {
		try {
			long queueLen = JedisQueue.llen(QueueMessageListener.saveDataQueue);
			if (count >= OVER_TIME || queueLen >= OVER_COUNT) {
				//批量入库
				List<String> stringList = JedisQueue.popMulti(QueueMessageListener.saveDataQueue, OVER_COUNT);
				if (stringList.size() > 0) {
					logger.info("转发数据批量入库的条数:"+stringList.size());
					executorService.execute(new DataSourceThread(stringList));
				}
				count = 0;
			}else{
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转发数据批量入库出现异常:"+e.getMessage());
		}
	}
	
	/**
	 * 删除日志数据
	 */
	@Scheduled(cron = "0 0 02 * * ?")
	public void clearLogJob(){
		Date nowDate = new Date();
		try {
			Integer beforeDay = environment.getProperty("log.data",Integer.class,7);
			Integer beforeMonth = environment.getProperty("source.data",Integer.class,1);
			
			Date oneMonthDate = DateUtils.getNextMonth(nowDate, -beforeMonth);
			Date oneWeekDate = DateUtils.getNextDay(nowDate, -beforeDay);
			
			logger.info("删除时间为:"+DateUtils.date2Str(oneMonthDate, DateUtils.NORMAL_FORMAT)+" 前的数据源记录,开始...");
			logger.info("删除时间为:"+DateUtils.date2Str(oneWeekDate, DateUtils.NORMAL_FORMAT)+" 前的日志记录,开始...");
			
			Map<String, Object> logParams = new HashMap<String,Object>();
			logParams.put("date", oneWeekDate);
			logDataService.deleteLog(logParams);
			
			List<GpsDataSourceConfigEntity> list = gpsDataSourceConfigDao.getList();
			for (GpsDataSourceConfigEntity entity : list) {
				Map<String, Object> params = new HashMap<>();
				params.put("date", oneMonthDate);
				params.put("sourceType", entity.getSourceType());
				gpsDataSourceDao.deleteDataSource(params);
			}
			
			logger.info("删除数据源记录成功,结束");
			logger.info("删除日志记录成功,结束");
		} catch (Exception e) {
			logger.error("定时删除失败", e);
			e.printStackTrace();
		}
	}
	
	class DataSourceThread implements Runnable {
    	private List<String> stringList;
    	
    	public DataSourceThread(List<String> stringList) {
			this.stringList = stringList;
		}
    	
		@Override
		public void run() {
			try {
				List<GpsDataSourceEntity> list = new ArrayList<>();
				for (String string : stringList) {
					GpsDataSourceEntity entity = pojoToEntity(JSONArray.parseObject(string, GpsDataSourcePojo.class));
					list.add(entity);
				}
				gpsDataSourceService.batchAdd(list);
			} catch (Exception e) {
				logger.error("批量入库出现异常:", e);
				for (String string : stringList) {
					JedisQueue.push(QueueMessageListener.saveDataQueue, string);
				}
			}
		}
		
		private GpsDataSourceEntity pojoToEntity(GpsDataSourcePojo gpsDataSourcePojo){
			GpsDataSourceEntity gpsDataSourceEntity = new GpsDataSourceEntity();
			gpsDataSourceEntity.setId(gpsDataSourcePojo.getId());
			gpsDataSourceEntity.setSourceType(gpsDataSourcePojo.getSourceType());
			gpsDataSourceEntity.setDeviceId(gpsDataSourcePojo.getDeviceId());
			gpsDataSourceEntity.setLon(gpsDataSourcePojo.getLon());
			gpsDataSourceEntity.setLat(gpsDataSourcePojo.getLat());
			gpsDataSourceEntity.setPosition(gpsDataSourcePojo.getPosition());
			gpsDataSourceEntity.setToken(gpsDataSourcePojo.getToken());
			gpsDataSourceEntity.setRemark(gpsDataSourcePojo.getRemark());
			gpsDataSourceEntity.setSpeed(gpsDataSourcePojo.getSpeed());
			gpsDataSourceEntity.setGpsTime(gpsDataSourcePojo.getGpsTime());
			gpsDataSourceEntity.setCreateTime(gpsDataSourcePojo.getCreateTime());
			
			return gpsDataSourceEntity;
		}
    }
}
