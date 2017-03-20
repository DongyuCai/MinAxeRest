package com.jeeba.common.datasource;

import java.sql.SQLException;
import java.util.Properties;

import org.axe.annotation.persistence.DataSource;
import org.axe.interface_.persistence.BaseDataSource;
import org.axe.util.PropsUtil;
import org.axe.util.StringUtil;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

@DataSource("druid")
public class DruidDataSourceFactory implements BaseDataSource{
	private Properties CONFIG_PROPS = PropsUtil.loadProps("druid.properties");

	
    //#数据库
    private final String DRIVER;
    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;
    private DruidDataSource DATA_SOURCE;
	
	public DruidDataSourceFactory() {
        //#初始化jdbc配置
        DRIVER = setJdbcDriver();
        URL = setJdbcUrl();
        USERNAME = setJdbcUserName();
        PASSWORD = setJdbcPassword();
        
        do{
        	if(StringUtil.isEmpty(DRIVER)) break;
        	if(StringUtil.isEmpty(URL)) break;
        	if(StringUtil.isEmpty(USERNAME)) break;
//        	if(StringUtil.isEmpty(PASSWORD)) break;
        	//么有配置的话，默认不会初始化数据源
        	init();
        }while(false);
	}
	
	private void init() {
        
        try {
            DATA_SOURCE = new DruidDataSource();
        	DATA_SOURCE.setDriverClassName(DRIVER);
        	DATA_SOURCE.setUrl(URL);
        	DATA_SOURCE.setUsername(USERNAME);
        	DATA_SOURCE.setPassword(PASSWORD);
        	
        	DATA_SOURCE.setFilters(PropsUtil.getString(CONFIG_PROPS, "filters"));
        	DATA_SOURCE.setMaxActive(PropsUtil.getInt(CONFIG_PROPS, "maxActive"));
        	DATA_SOURCE.setInitialSize(PropsUtil.getInt(CONFIG_PROPS, "initialSize"));
        	DATA_SOURCE.setMaxWait(PropsUtil.getLong(CONFIG_PROPS, "maxWait"));
        	DATA_SOURCE.setMinIdle(PropsUtil.getInt(CONFIG_PROPS, "minIdle"));
        	DATA_SOURCE.setTimeBetweenEvictionRunsMillis(PropsUtil.getLong(CONFIG_PROPS,"timeBetweenEvictionRunsMillis"));
        	DATA_SOURCE.setMinEvictableIdleTimeMillis(PropsUtil.getLong(CONFIG_PROPS, "minEvictableIdleTimeMillis"));
        	DATA_SOURCE.setValidationQuery(PropsUtil.getString(CONFIG_PROPS, "validationQuery"));
        	DATA_SOURCE.setTestWhileIdle(PropsUtil.getBoolean(CONFIG_PROPS, "testWhileIdle"));
        	DATA_SOURCE.setTestOnBorrow(PropsUtil.getBoolean(CONFIG_PROPS, "testOnBorrow"));
        	DATA_SOURCE.setTestOnReturn(PropsUtil.getBoolean(CONFIG_PROPS, "testOnReturn"));
        	DATA_SOURCE.setMaxOpenPreparedStatements(PropsUtil.getInt(CONFIG_PROPS, "maxOpenPreparedStatements"));
//        	DATA_SOURCE.setRemoveAbandoned(PropsUtil.getBoolean(CONFIG_PROPS, "removeAbandoned"));
//        	DATA_SOURCE.setRemoveAbandonedTimeout(PropsUtil.getInt(CONFIG_PROPS, "removeAbandonedTimeout"));
        	DATA_SOURCE.setPoolPreparedStatements(PropsUtil.getBoolean(CONFIG_PROPS, "maxOpenPreparedStatements"));
        	DATA_SOURCE.setLogAbandoned(PropsUtil.getBoolean(CONFIG_PROPS, "logAbandoned"));
        } catch (Exception e) {
            System.out.println("jdbc driver : " + DRIVER);
            System.out.println("jdbc url : " + URL);
            System.out.println("jdbc username : " + USERNAME);
            System.out.println("jdbc password : " + PASSWORD);
            System.out.println("load jdbc driver failure");
        }
        
	}

	@Override
	public DruidPooledConnection getConnection() throws SQLException {
		return DATA_SOURCE.getConnection();
	}

	@Override
	public String setJdbcDriver() {
		return PropsUtil.getString(CONFIG_PROPS, "driverClassName");
	}

	@Override
	public String setJdbcUrl() {
		return PropsUtil.getString(CONFIG_PROPS, "url");
	}

	@Override
	public String setJdbcUserName() {
		return PropsUtil.getString(CONFIG_PROPS, "username");
	}

	@Override
	public String setJdbcPassword() {
		return PropsUtil.getString(CONFIG_PROPS, "password")==null?"":PropsUtil.getString(CONFIG_PROPS, "password");
	}

	@Override
	public boolean tns() {
		return true;
	}
}
