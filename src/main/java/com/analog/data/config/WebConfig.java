package com.analog.data.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
* @ClassName: WebConfig
* @Description: web的配置(相当于是springmvc.xml)
* @author yangjianlong
* @date 2019年12月27日上午11:03:44
*
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
@ComponentScan(basePackages={"com.analog.data.controller"})
public class WebConfig extends WebMvcConfigurerAdapter{

	//定义视图解析器 
	@Bean(name = "viewResolver")
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/html/");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}
	
	//配置静态资源的处理
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	//文件上传解析器
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver(){
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		multipartResolver.setMaxUploadSize(10485760000L);
		multipartResolver.setMaxInMemorySize(10960);
		return multipartResolver;
	}
	
	//加入这段配置后，spring返回给页面的都是utf-8编码了
	@Bean(name = "annotationMethodHandlerAdapter")
	public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter(){
		AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
		
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		MediaType mediaType = MediaType.valueOf("text/html;UTF-8");
		mediaTypes.add(mediaType);
		stringHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		
		HttpMessageConverter<?>[] messageConverters = new HttpMessageConverter<?>[1];
		messageConverters[0] = stringHttpMessageConverter;
		annotationMethodHandlerAdapter.setMessageConverters(messageConverters);
		
		return annotationMethodHandlerAdapter;
	}
}
