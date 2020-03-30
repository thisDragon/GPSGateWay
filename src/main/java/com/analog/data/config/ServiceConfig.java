package com.analog.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
* @ClassName: ServiceConfig
* @Description: service注解配置
* @author yangjianlong
* @date 2019年12月26日上午9:19:49
*
 */
@Configuration
@ComponentScan(basePackages={"com.analog.data.service"})
public class ServiceConfig {
}
