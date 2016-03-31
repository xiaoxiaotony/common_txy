package com.txy.common.orm.query.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.txy.common.api.DBPage;
import com.txy.common.bean.PageList;
import com.txy.common.exception.ServiceException;
import com.txy.common.orm.ObjectModel;
import com.txy.common.orm.query.Query;
import com.txy.common.util.CrateObjectModelUtil;

public class SqlHelperQuery<T> implements Query<T>
{

	private String sql;
	private Object[] args;
	private DBPage dbPage;
	private JdbcTemplate simpleJdbcTemplate;
	private Class<T> cla;

	public SqlHelperQuery(Class<T> cla, JdbcTemplate simpleJdbcTemplate,
			String sql, DBPage dbPage, Object... args)
	{
		this.cla = cla;
		this.simpleJdbcTemplate = simpleJdbcTemplate;
		this.sql = sql;
		this.dbPage = dbPage;
		this.args = args;
	}

	public SqlHelperQuery(Class<T> cla, String sql, Object[] args)
	{
		this.cla = cla;
		this.sql = sql;
		this.args = args;
	}

	@Override
	public List<T> list()
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,true,null);
		return this.simpleJdbcTemplate.query(sql,
				ResultSqlListCallbackHandler.create(cla,100,model), args);
	}

	@Override
	public PageList<T> page(int start, int pageSize)
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,true,null);
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls = new ArrayList<Object>();
		for (Object value : args)
		{
			ls.add(value);
		}
		ls.add((start-1)*pageSize);
		ls.add(start*pageSize);
		List<T> list = this.simpleJdbcTemplate.query(page_sql,
				ResultSqlListCallbackHandler.create(cla,pageSize,model), ls.toArray());
		String sql_count = this.getCountSql(sql);
		int count = this.simpleJdbcTemplate.queryForObject(sql_count,
				Integer.class,args);
		PageList<T> pageList = new PageList<T>(count,list);
		return pageList;
	}

	private String getCountSql(String sql)
	{
		StringBuilder sql_= new StringBuilder("SELECT COUNT(0) from (");
		sql_.append(sql).append(")");
		return sql_.toString();
	}

	@Override
	public T uniqueResult()
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,true,null);
		List<T> list = this.simpleJdbcTemplate.query(this.sql,
				ResultSqlListCallbackHandler.create(cla,model), args);
		if (list.isEmpty())
		{
			return null;
		}
		if (list.size() != 1)
		{
			throw new ServiceException("返回数据结果集大于1");
		}

		return list.get(0);
	}

	@Override
	public List<T> list(int start, int pageSize)
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,true,null);
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls = new ArrayList<Object>();
		for (Object value : args)
		{
			ls.add(value);
		}
		ls.add(start);
		ls.add(pageSize);
		return this.simpleJdbcTemplate.query(page_sql,
				ResultSqlListCallbackHandler.create(cla,pageSize,model), ls.toArray());
	}

	@Override
	public int count() {
		List<Object> ls = new ArrayList<Object>();
		for (Object value : args)
		{
			ls.add(value);
		}
		return this.simpleJdbcTemplate.queryForObject(getCountSql(sql), ls.toArray(), Integer.class);
	}

	@Override
	public PageList<T> page(int start, int pageSize, String sql_count) {
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,true,null);
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls = new ArrayList<Object>();
		for (Object value : args)
		{
			ls.add(value);
		}
		ls.add((start-1)*pageSize);
		ls.add(start*pageSize);
		List<T> list = this.simpleJdbcTemplate.query(page_sql,
				ResultSqlListCallbackHandler.create(cla,pageSize,model), ls.toArray());
		int count = this.simpleJdbcTemplate.queryForObject(sql_count,
				Integer.class,args);
		PageList<T> pageList = new PageList<T>(count,list);
		return pageList;
	}

}
