package com.analog.data.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.analog.data.cache.MapCache;
import com.analog.data.dao.IGpsDataSourceConfigDao;
import com.analog.data.entity.GpsDataSourceConfigEntity;

/**
 * 
* @ClassName: InitListener
* @Description: 初始化
* @author yangjianlong
* @date 2020年3月6日下午2:13:37
*
 */
public class InitListener implements ServletContextListener{

	private static Logger logger = LoggerFactory.getLogger(InitListener.class);
	private static IGpsDataSourceConfigDao gpsDataSourceConfigDao;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("初始化缓存开始...");
		gpsDataSourceConfigDao = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(IGpsDataSourceConfigDao.class);
		List<GpsDataSourceConfigEntity> list = new ArrayList<>();
		try {
			list = gpsDataSourceConfigDao.getList();
		} catch (Exception e) {
			logger.info("初始化异常:"+e);
			e.printStackTrace();
		}
		Map<Object, Object> cacheMap = MapCache.getCacheMap();
		for (GpsDataSourceConfigEntity entity : list) {
			cacheMap.put(entity.getSourceType(), entity);
		}
		logger.info("初始化缓存结束");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
