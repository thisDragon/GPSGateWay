package com.analog.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* @ClassName: ScheduleConfig
* @Description: 定时器配置
* @author yangjianlong
* @date 2019年12月26日上午11:01:24
*
 */
@Configuration
@EnableScheduling
@ComponentScan(basePackages={"com.analog.data.schedule"})
public class ScheduleConfig {
}
