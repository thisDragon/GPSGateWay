/*
 * @(#)WingListener.java 2007-3-27
 *
 * Copyright(2004-2006) of Cari Power Corp.,Inc.. All rights reserved.
 * Cari Power PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.cari.wing.servlet;

import java.util.List;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.cari.rbac.OperationManage;
import com.cari.sys.biz.SystemConstant;
import com.cari.wing.cache.SystemConfig;


/**
 * Wing守护者进程，以监听器模式进行编写，完成对三种Listener的继承，分别为：
 * 应用监听器、应用属性监听器、会话监听器
 * 在应用被装载时初始化地图高速缓存，确保在整个应用中可以正常使用地图缓存
 */
public class WingListener implements ServletContextListener,ServletContextAttributeListener,HttpSessionListener{
	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		try{
			//加载系统配置
			SystemConfig.loadSystemConfig();
			//加载所有操作
			List operationList = OperationManage.getInstance().getOperateList();
			arg0.getServletContext().setAttribute("OPERATION_LIST", operationList);
			//URL-模块ID映射表
			SystemConstant.setUrlModMapping();
		}catch(RuntimeException re){
			//这里需要捕捉异常，否则会造成服务器启动终止			System.out.println("错误内容：" + re.getLocalizedMessage());
			re.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	public void attributeAdded(ServletContextAttributeEvent arg0) {
		// TODO 自动生成方法存根
		
	}

	public void attributeRemoved(ServletContextAttributeEvent arg0) {
		// TODO 自动生成方法存根
		
	}

	public void attributeReplaced(ServletContextAttributeEvent arg0) {
		// TODO 自动生成方法存根
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		//新来一个用户
		//SVGCache.addUser(arg0.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		//用户会话失效
		//SVGCache.removeUser(arg0.getSession());
	}

}

