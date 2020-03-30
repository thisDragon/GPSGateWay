package com.cari.wing.cache;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cari.util.Utils;

/**
 * 
 * @author leidg.cn
 * @version 2007-03-27
 * 用于读取系统参数的配置文件config.xml
 * 1.数据库帐号
 * driverClass					数据库驱动名称
 * jdbcUrl						数据库连接串
 * username						数据库用户名
 * password						数据库密码
 * 2.C3P0配置参数
 * maxIdleTime					最长空闲时间
 * maxPoolSize					连接池最大数
 * minPoolSize					连接池最小数
 * maxSatements					预处理的最大缓存
 * acquireIncrement				可扩展连接数
 * idleConnectionTestPeriod		定时测试空闲连接
 * 
 */
public class SystemConfig{
	private static final Logger logger = LoggerFactory.getLogger(SystemConfig.class);
	private static String m_ClassRootPath = null;
	public static final String CONFIG_FILE_NAME = "jdbc.properties";
	private static SystemConfig systemParam = null;
	private static HashMap<String, String> params = new HashMap<String, String>(9);
	
	private static boolean creatingMap = false;
	private static int creatingType = 0;

	private SystemConfig() {
		super();
	}


	public static void setCreatingMap(boolean flag){
		SystemConfig.creatingMap = flag;
	}
	
	public static boolean isCreatingMap(){
		return SystemConfig.creatingMap;
	}
	
	public static void setCreatingType(int type){
		SystemConfig.creatingType = type;
	}
	
	public static int getCreatingType(){
		return SystemConfig.creatingType;
	}
	
	/**
	 * System runtime configs loader
	 * @return the configs set , by SysRuntimeParams's singleton instance
	 */
	public static SystemConfig loadSystemConfig(){
		if(systemParam == null){
			systemParam = new SystemConfig();
			try {
				Properties properties = Utils.loadProperties(CONFIG_FILE_NAME);
				parse(properties);
			} catch (IOException e) {
				logger.error("加载数据库配置异常:"+e);
				e.printStackTrace();
			}
		}
		return systemParam;
	}
	
	/**
	 * Reload the system runtime configs
	 * @return the configs set , by SysRuntimeParams's singleton instance
	 */
	public static void reloadSystemConfig(){
		if(systemParam == null){
			systemParam = new SystemConfig();
		}
		try {
			Properties properties = Utils.loadProperties(CONFIG_FILE_NAME);
			parse(properties);
		} catch (IOException e) {
			logger.error("加载数据库配置异常:"+e);
			e.printStackTrace();
		}
	}
	
	public static void parse(Properties properties) {
		logger.info("开始加载系统配置参数...");
		params.put("jdbc/driverClassName" , properties.getProperty("jdbc.driver"));
		params.put("jdbc/url" , properties.getProperty("jdbc.url"));
		params.put("jdbc/username" , properties.getProperty("jdbc.username"));
		params.put("jdbc/password" , properties.getProperty("jdbc.password"));
		params.put("jdbc/connectionString", properties.getProperty("jdbc.connectionString"));
		params.put("c3p0/maxIdleTime" , properties.getProperty("jdbc.maxIdleTime"));
		params.put("c3p0/maxPoolSize" , properties.getProperty("jdbc.maxPoolSize"));
		params.put("c3p0/minPoolSize" , properties.getProperty("jdbc.minPoolSize"));
		params.put("c3p0/maxSatements" , properties.getProperty("jdbc.maxStatements"));
		params.put("c3p0/acquireIncrement" , properties.getProperty("jdbc.acquireIncrement"));
		params.put("c3p0/idleConnectionTestPeriod" , properties.getProperty("jdbc.idleConnectionTestPeriod"));
		logger.info("结束加载系统配置参数");
	}
	
	//====以下是数据库配置=======
	/**
	 * 数据库连接驱动名称
	 */
	public String getJDBCDriverClass() {
		return (String)params.get("jdbc/driverClassName");
	}
	public void setJDBCDriverclass(String driver){
		params.put("jdbc/driverClassName", driver);
	}
	/**
	 * 数据库连接串
	 * @return
	 */
	public String getJDBCURL() {
		return (String)params.get("jdbc/url");
	}
	public void setJDBCURL(String url){
		params.put("jdbc/url", url);
	}
	/**
	 * 数据库用户名
	 * @return
	 */
	public String getJDBCUserName() {
		return (String)params.get("jdbc/username");
	}
	public void setJDBCUserName(String userName){
		params.put("jdbc/username", userName);
	}
	/**
	 * 数据库密码
	 * @return
	 */
	public String getJDBCPassword() {
		return (String)params.get("jdbc/password");
	}
	public void setJDBCPassword(String pwd){
		params.put("jdbc/password", pwd);
	}
	/**
	 * 数据库连接字符串标识
	 * @return
	 */
	public String getConnectionString() {
		return (String)params.get("jdbc/connString");
	}
	public void setConnectionString(String str){
		params.put("jdbc/connString", str);
	}
	
	//====以下是连接池配置=======
	/**
	 * 连接的最长空闲时间（超时时间），单位：秒
	 */
	public String getC3P0MaxIdleTime() {
		if (params.get("c3p0/maxIdleTime") == null) return "0";
		return (String)params.get("c3p0/maxIdleTime");
	}
	public void setC3P0MaxIdleTime(String time){
		params.put("c3p0/maxIdleTime", time);
	}
	/**
	 * 连接池的最大连接数（上限）
	 * @return
	 */
	public String getC3P0MaxPoolSize() {
		if (params.get("c3p0/maxPoolSize") == null) return "15";
		return (String)params.get("c3p0/maxPoolSize");
	}
	public void setC3P0MaxPoolSize(String size){
		params.put("c3p0/maxPoolSize", size);
	}
	/**
	 * 连接池的最小连接数（初始连接数）
	 * @return
	 */
	public String getC3P0MinPoolSize() {
		if (params.get("c3p0/minPoolSize") == null) return "3";
		return (String)params.get("c3p0/minPoolSize");
	}
	public void setC3P0MinPoolSize(String size){
		params.put("c3p0/minPoolSize", size);
	}
	/**
	 * 最大可缓存数据库语句对象，设为0则不缓存
	 * @return
	 */
	public String getC3P0MaxStatements() {
		if (params.get("c3p0/maxStatements") == null) return "0";
		return (String)params.get("c3p0/maxStatements");
	}
	public void setC3P0MaxStatements(String value){
		params.put("c3p0/maxStatements", value);
	}
	/**
	 * 当连接池耗尽并接到获得连接的请求，则新增加连接的数量
	 * @return
	 */
	public String getC3P0AcquireIncrement() {
		if (params.get("c3p0/acquireIncrement") == null) return "3";
		return (String)params.get("c3p0/acquireIncrement");
	}
	public void setC3P0AcquireIncrement(String value){
		params.put("c3p0/acquireIncrement", value);
	}
	/**
	 * 在连接空闲多长时间后，检查连接。单位：毫秒
	 * @return
	 */
	public String getC3P0IdleConnectionTestPeriod() {
		if (params.get("c3p0/idleConnectionTestPeriod") == null) return "0";
		return (String)params.get("c3p0/idleConnectionTestPeriod");
	}
	public void setC3P0IdleConnectionTestPeriod(String value){
		params.put("c3p0/idleConnectionTestPeriod", value);
	}
    
}
