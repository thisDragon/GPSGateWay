/*
 * 创建日期 2006-1-12
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.cari.sys.bean;

import java.sql.Timestamp;;

/**
 * @author zsy
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class SysEventLog {
    private String dwKey;
    private String userID;
    private String userName;
    private String model;
    private String ip;
    private String companyID;
    private Timestamp eventTime;
    private String event;
    private String eventObject;
    private String eventResult;
    /**
     * 
     * @return
     */
    public String getCompanyID() {
        return companyID;
    }
    /**
     * @param companyID 公司ID
     */
    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }
    /**
     * @return
     */
    public String getDwKey() {
        return dwKey;
    }
    /**
     * @param dwKey 
     */
    public void setDwKey(String dwKey) {
        this.dwKey = dwKey;
    }
    /**
     * @return
     */
    public String getEvent() {
        return event;
    }
    /**
     * @param event 事件
     */
    public void setEvent(String event) {
        this.event = event;
    }
    /**
     * @return
     */
    public String getEventObject() {
        return eventObject;
    }
    /**
     * @param eventObject 事件对象
     */
    public void setEventObject(String eventObject) {
        this.eventObject = eventObject;
    }
    /**
     * @return
     */
    public String getEventResult() {
        return eventResult;
    }
    /**
     * @param eventResult 事件结果
     */
    public void setEventResult(String eventResult) {
        this.eventResult = eventResult;
    }
    /**
     * @return
     */
    public Timestamp getEventTime() {
        return eventTime;
    }
    /**
     * @param eventTime 事件时间
     */
    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }
    /**
     * @return
     */
    public String getUserID() {
        return userID;
    }
    /**
     * @param userID 用户ID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
	/**
	 * @return Returns the ip.
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip The ip to set.
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return Returns the model.
	 */
	public String getModel() {
		return model;
	}
	/**
	 * @param model The model to set.
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
