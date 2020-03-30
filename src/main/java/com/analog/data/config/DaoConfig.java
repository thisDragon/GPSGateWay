package com.analog.data.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
* @ClassName: DaoConfig
* @Description: dao注解配置
* @author yangjianlong
* @date 2019年12月26日上午9:26:08
*
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages={"com.analog.data.dao"})
@PropertySource("classpath:jdbc.properties")
public class DaoConfig {
	@Autowired
	private Environment environment;
	private String driverClass;
	private String jdbcUrl;
	private String user;
	private String password;
	private int maxPoolSize = 5000;
	private int minPoolSize = 10;
	private boolean autoCommitOnClose = false;
	private int checkoutTimeout = 300000;
	private int acquireRetryAttempts = 10;
	
	private String hibernateDialect = "org.hibernate.dialect.SQLServerDialect";
	private String hibernateShowSql = "false";
	private String hibernateFormatSql = "true";
	
	private String packagesToScan = "com.analog.data.entity";

	@Bean(name = "dataSource")
	public ComboPooledDataSource dataSource() throws PropertyVetoException{
		ComboPooledDataSource source = new ComboPooledDataSource();
		//配置连接池属性
		driverClass = environment.getProperty("jdbc.driver");
		jdbcUrl = environment.getProperty("jdbc.url");
		user = environment.getProperty("jdbc.username");
		password = environment.getProperty("jdbc.password");
		source.setDriverClass(driverClass);
		source.setJdbcUrl(jdbcUrl);
		source.setUser(user);
		source.setPassword(password);
		//c3p0连接池的私有属性
		source.setMinPoolSize(minPoolSize);
		source.setMaxPoolSize(maxPoolSize);
		//关闭连接后不自动commit
		source.setAutoCommitOnClose(autoCommitOnClose);
		//获取连接超时时间
		source.setCheckoutTimeout(checkoutTimeout);
		//当获取连接失败重试次数
		source.setAcquireRetryAttempts(acquireRetryAttempts);
		
		return source;
	}
	
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate() throws PropertyVetoException{
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource());
		return jdbcTemplate;
	}
	
	@Bean(name = "namedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws PropertyVetoException{
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource());
		return namedParameterJdbcTemplate;
	}
	
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() throws PropertyVetoException{
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource());
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", hibernateDialect);
		properties.setProperty("hibernate.show_sql", hibernateShowSql);
		properties.setProperty("hibernate.format_sql", hibernateFormatSql);
		localSessionFactoryBean.setHibernateProperties(properties);
		localSessionFactoryBean.setPackagesToScan(packagesToScan);
		
		return localSessionFactoryBean;
	}
	@Bean(name = "transactionManager")
	public HibernateTransactionManager transactionManager() throws PropertyVetoException{
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		
		return transactionManager;
	}
}
