package com.txy.common.orm.query.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.txy.common.api.DBPage;
import com.txy.common.bean.PageList;
import com.txy.common.exception.ServiceException;
import com.txy.common.orm.query.Query;

public class DynamicBeanHelperQuery implements Query<DynamicBean>
{
	
	private String sql;
	
	private Object[] args;
	
	private DBPage dbPage;
	
	private JdbcTemplate simpleJdbcTemplate;
	
	public DynamicBeanHelperQuery(JdbcTemplate simpleJdbcTemplate, String sql, DBPage dbPage, Object... args)
	{
		this.simpleJdbcTemplate = simpleJdbcTemplate;
		this.sql = sql;
		this.dbPage = dbPage;
		this.args = args;
	}
	
	@Override
	public List<DynamicBean> list()
	{
		
		return this.simpleJdbcTemplate.query(sql, new ResultDynmicBeanCallbackHandler(512), args);
	}
	
	@Override
	public PageList<DynamicBean> page(int start, int pageSize)
	{
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls = new ArrayList<Object>();
		for (Object value : args)
		{
			ls.add(value);
		}
		ls.add((start - 1) * pageSize);
		ls.add(start * pageSize);
		List<DynamicBean> list = this.simpleJdbcTemplate.query(page_sql, new ResultDynmicBeanCallbackHandler(pageSize), ls.toArray());
		String sql_count = this.getCountSql(sql.toUpperCase());
		int count = this.simpleJdbcTemplate.queryForObject(sql_count, Integer.class, args);
		PageList<DynamicBean> pageList = new PageList<DynamicBean>(count, list);
		return pageList;
	}
	
	@Override
	public PageList<DynamicBean> page(int start, int pageSize,String sql_count)
	{
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls = new ArrayList<Object>();
		for (Object value : args)
		{
			ls.add(value);
		}
		ls.add((start - 1) * pageSize);
		ls.add(start * pageSize);
		List<DynamicBean> list = this.simpleJdbcTemplate.query(page_sql, new ResultDynmicBeanCallbackHandler(pageSize), ls.toArray());
		int count = this.simpleJdbcTemplate.queryForObject(sql_count, Integer.class, args);
		PageList<DynamicBean> pageList = new PageList<DynamicBean>(count, list);
		return pageList;
	}
	
	@Override
	public DynamicBean uniqueResult()
	{
		
		List<DynamicBean> list = this.simpleJdbcTemplate.query(this.sql, new ResultDynmicBeanCallbackHandler(10), args);
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
	public List<DynamicBean> list(int start, int pageSize)
	{
		
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls = new ArrayList<Object>();
		for (Object value : args)
		{
			ls.add(value);
		}
		ls.add(start);
		ls.add(pageSize);
		return this.simpleJdbcTemplate.query(page_sql, new ResultDynmicBeanCallbackHandler(pageSize), ls.toArray());
	}
	
	private String getCountSql(String sql)
	{
		int end = sql.indexOf("FROM") == -1 ? sql.indexOf("from") : sql.indexOf("FROM");
		sql = "SELECT COUNT(0) " + sql.substring(end);
		
		// 处理sql中包含group by的问题
		if (sql.toLowerCase().indexOf("group by") > -1)
		{
			if(sql.toLowerCase().indexOf("order by") > -1){
				return "select count(0) from (" + sql.substring(0, sql.toLowerCase().indexOf("order by")) + ")";
			}
			return "select count(0) from (" + sql + ")";
		}
		return sql;
	}
	
	@Override
	public int count()
	{
		return this.simpleJdbcTemplate.queryForObject(this.getCountSql(sql.toUpperCase()), Integer.class, args);
	}
	
}
