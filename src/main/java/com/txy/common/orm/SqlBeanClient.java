package com.txy.common.orm;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.txy.common.orm.query.BeanQuery;
import com.txy.common.orm.query.ExecuteQuery;
import com.txy.common.orm.query.Query;
import com.txy.common.orm.query.impl.DynamicBean;

public interface SqlBeanClient {
	/**
	 * 创建基于SQL转对象的Query对象
	 * 
	 * @param <T>
	 * @param cla：实体所对应的Class类型
	 * @param sql:sql语句
	 * @param args:sql语句所要绑定的参数
	 * @return
	 */
	public <T> Query<T> createSqlQuery(Class<T> cla, String sql, Object... args);

	/**
	 * 创建基于SQL转对象的Query对象，SQL语句不带参数
	 * 
	 * @param <T>
	 * @param cla
	 * @param sql
	 * @return
	 */
	public <T> Query<T> createSqlQuery(Class<T> cla, String sql);

	/**
	 * 创建基于实体查询的BeanQuery对象
	 * @param <T>
	 * @param cla:实体所对应的Class类型
	 * @param args：初始查询参数
	 * @return
	 */
	public <T> BeanQuery<T> createBeanQuery(Class<T> cla, Map<String, Object> args);

	/**
	 * 创建基于实体查询的BeanQuery对象
	 * 
	 * @param <T>
	 * @param cla :实体所对应的Class类型
	 * @return
	 */
	public <T> BeanQuery<T> createBeanQuery(Class<T> cla);

	/**
	 * 创建ExecuteQuery对象，ExecuteQuery包含增加，批量增加，编辑，删除等于数据库相关的update方法
	 * 
	 * @return
	 */
	public ExecuteQuery createExecuteQuery();

	/**
	 * 创建JdbcTemplate的对象，该对象提供基于Spring
	 * JdbcTemplate的对象，
	 * 负责查询或存储过程可使用该对象提供的方法进行数据库相关操作
	 * @return
	 */
	public JdbcTemplate createJdbcTemplate();
	
	/**
	 * 创建基于SQL查询的Query对象，该对象提供的方法会将查询结果集转换成DynamicBean的对象，该对象提供了数据转换功能的支持
	 * @param sql
	 * @param args
	 * @return
	 */
	public  Query<DynamicBean> createSqlQuery(String sql,Object...args);
}
