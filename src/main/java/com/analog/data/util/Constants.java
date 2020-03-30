package com.analog.data.util;


public class Constants {
	/**集团GPS转发*/
	public final static int GPS_FORWARD = 1;
	/**按键记录、 考勤记录、围栏告警 转发*/
	public final static int FORWARD = 1;	
	/**调度台回调参数*/
	public static final String RUN = "run";
	/**调度台回调参数*/
	public static final String CODE = "code";
	/**调度台回调参数*/
	public static final String MESSAGE = "message";
	/**返回业务数据**/
	public static final String DATA = "data"; 
	
	public static final String CUR_PAGE = "curpage";
	public static final String PAGE_SIZE = "pagesize";
	public static final String RECORD_COUNT = "count";
	
	/**错误的编码*/
	public static final int ERROR_CODE = 0;	
	/**正常的编码*/
	public static final int NORMAL_CODE = 1;	
	
	/**会话过期编码*/
	public static final int SESSION_TIMEOUT_CODE = -1; 
	/**会话踢出编码*/
	public static final int SESSION_KICKOUT_CODE = -2; 
	
	public static final String SESSION_TIMEOUT = "会话已过期";
	public static final String SESSION_KICKOUT = "该账户在其它地方登录，本次会话被踢出";	
	

}
