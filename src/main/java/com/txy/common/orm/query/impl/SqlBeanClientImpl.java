package com.txy.common.orm.query.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.txy.common.api.DBPage;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.BeanQuery;
import com.txy.common.orm.query.ExecuteQuery;
import com.txy.common.orm.query.Query;
@Component
public class SqlBeanClientImpl implements SqlBeanClient {

	@Autowired
	private DBPage dbPage = null;
	@Autowired
	private JdbcTemplate simpleJdbcTemplate = null;
	@Autowired
	private ExecuteQuery executeQuery;
	public SqlBeanClientImpl() {
	}

	@Override
	public <T> Query<T> createSqlQuery(Class<T> cla, String sql, Object... args) {
		Query<T> query = new SqlHelperQuery<T>(cla, simpleJdbcTemplate, sql, dbPage, args);
		return query;
	}

	@Override
	public <T> Query<T> createSqlQuery(Class<T> cla, String sql) {
		Query<T> query = new SqlHelperQuery<T>(cla, simpleJdbcTemplate, sql, dbPage, new Object[]{});
		return query;
	}

	@Override
	public <T> BeanQuery<T> createBeanQuery(Class<T> cla, Map<String, Object> args) {
		BeanQuery<T> beanQuery = new BeanHelperQuery<T>(cla, this.simpleJdbcTemplate, dbPage, args);
		return beanQuery;
	}

	@Override
	public <T> BeanQuery<T> createBeanQuery(Class<T> cla) {
		BeanQuery<T> beanQuery = new BeanHelperQuery<T>(cla, this.simpleJdbcTemplate, dbPage,
				new HashMap<String, Object>(10));
		return beanQuery;
	}

	@Override
	public ExecuteQuery createExecuteQuery() {
		return this.executeQuery;
	}

	@Override
	public JdbcTemplate createJdbcTemplate() {
		return this.simpleJdbcTemplate;
	}

	@Override
	public Query<DynamicBean> createSqlQuery(String sql, Object... args) {
		Query<DynamicBean> query = new DynamicBeanHelperQuery(simpleJdbcTemplate, sql, dbPage, args);
		return query;
	}

}
