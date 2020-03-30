package com.analog.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
* @ClassName: AopConfig
* @Description: aop切面注解配置
* @author yangjianlong
* @date 2019年12月26日上午9:18:56
*
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages={"com.analog.data.aspect"})
public class AopConfig {
}
