package com.txy.common.orm.query.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.txy.common.api.DBPage;
import com.txy.common.bean.PageList;
import com.txy.common.exception.ServiceException;
import com.txy.common.orm.ObjectModel;
import com.txy.common.orm.query.BeanQuery;
import com.txy.common.orm.query.BeanTranslateForSql;
import com.txy.common.util.CrateObjectModelUtil;
import com.txy.common.util.StringUtils;
public class BeanHelperQuery<T> implements BeanQuery<T>
{
	private static final Logger logger = Logger.getLogger(BeanHelperQuery.class);
	private Class<T> cla;
	private JdbcTemplate simpleJdbcTemplate;
	private DBPage dbPage;
	private QueryParameter queryParameter;
	private BeanTranslateForSql beanTranslateForSql=new BeanTranslateForSqlImpl();
	public BeanHelperQuery(Class<T> cla, JdbcTemplate simpleJdbcTemplate,
			DBPage dbPage, Map<String, Object> mapArgs)
	{
		this.cla = cla;
		this.simpleJdbcTemplate = simpleJdbcTemplate;
		this.dbPage = dbPage;
		queryParameter = new QueryParameter();
		for(Entry<String,Object> entry:mapArgs.entrySet())
		{
			queryParameter.addQueryParameter("="+entry.getKey(),entry.getValue());
		}

	}

	@Override
	public BeanQuery<T> sortForDesc(String... args)
	{
		queryParameter.setSortTag("DESC");
		for (int i = 0; i < args.length; i++)
		{
			queryParameter.addSortField(args[i]);
		}
		return this;
	}

	@Override
	public BeanQuery<T> sortForAsc(String... args)
	{
		queryParameter.setSortTag("ASC");
		for(String field:args)
		{
			queryParameter.addSortField(field);
		}
		return this;
	}

	@Override
	public  BeanQuery<T> selectFields(String... fields)
	{
		for (String field : fields)
		{
			queryParameter.addSelectField(field);
		}
		return this;
	}

	@Override
	public List<T> list()
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,this.queryParameter.isJoin(),this.queryParameter.getBeansList());
		String sql=beanTranslateForSql.createSelectSql(queryParameter, model);
		logger.info(sql);
		return this.simpleJdbcTemplate.query(sql,
				ResultListCallbackHandler.create(cla, 100,model),
				model.getValues().toArray());
	}

	@Override
	public PageList<T> page(int start, int pageSize)
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,this.queryParameter.isJoin(),this.queryParameter.getBeansList());
		String sql=beanTranslateForSql.createSelectSql(queryParameter, model);
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls = new ArrayList<Object>();
		for (Object o :model.getValues())
		{
			ls.add(o);
		}
		ls.add((start-1)*pageSize);
		ls.add(start*pageSize);
		List<T> list = this.simpleJdbcTemplate.query(page_sql,
				ResultListCallbackHandler.create(cla, pageSize,model), ls.toArray());
		String sql_count = this.getCountSql(sql);
		int count = this.simpleJdbcTemplate.queryForObject(sql_count,
				Integer.class,model.getValues().toArray());
		PageList<T> pageList = new PageList<T>(count,list);
		return pageList;
	}

	@Override
	public T uniqueResult()
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,this.queryParameter.isJoin(),this.queryParameter.getBeansList());
		String sql=beanTranslateForSql.createSelectSql(queryParameter, model);
		List<T> list = this.simpleJdbcTemplate.query(sql,
				ResultListCallbackHandler.create(cla,model),model.getValues().toArray());
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

	private String getCountSql(String sql)
	{
		int end = sql.indexOf(" FROM");
		sql = sql.replaceFirst("\\*", " COUNT(0)");
		end = sql.indexOf(" FROM");
		sql = sql.replaceFirst(sql.substring(6, end), " COUNT(0)");
		end = sql.indexOf("ORDER BY ");
		if (end == -1)
		{
			return sql;
		}
		return sql.substring(0, end);

	}

	@Override
	public BeanQuery<T> eq(String field, Object value)
	{
		
		this.queryParameter.addQueryParameter("="+field, value);
		return this;
	}

	@Override
	public BeanQuery<T> gt(String field, Object value)
	{
		this.queryParameter.addQueryParameter(">"+field, value);
		return this;
	}

	@Override
	public BeanQuery<T> lt(String field, Object value)
	{
		this.queryParameter.addQueryParameter("<"+field, value);
		return this;
	}

	@Override
	public List<T> list(int start, int pageSize)
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,this.queryParameter.isJoin(),this.queryParameter.getBeansList());
		String sql=beanTranslateForSql.createSelectSql(queryParameter, model);
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls =model.getValues();
		ls.add(start);
		ls.add(pageSize);
		return this.simpleJdbcTemplate.query(page_sql,
				ResultListCallbackHandler.create(cla,pageSize,model), ls.toArray());
	}

	@Override
	public  BeanQuery<T> setJoin(boolean isJoin)
	{
		this.queryParameter.setJoin(isJoin);
		return this;
	}

	

	@Override
	public  BeanQuery<T> addQueryParameter(String key, Object value)
	{
		this.queryParameter.addQueryParameter("="+key, value);
		return this;
	}

	@Override
	public BeanQuery<T> setJoinBean(String... beans)
	{
		this.queryParameter.setJoin(true);
		for(String bean:beans)
		{
			if(!StringUtils.isEmpty(bean))
			{
				this.queryParameter.setJoinBean(bean);	
			}
		}
		return this;
	}

	@Override
	public BeanQuery<T> setJoinLeftJoin()
	{
		this.queryParameter.setJoinType("LEFT");
		return this;
	}

	@Override
	public BeanQuery<T> setJoinRightJoin()
	{
		this.queryParameter.setJoinType("RIGHT");
		return this;
	}

	@Override
	public int count()
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,this.queryParameter.isJoin(),this.queryParameter.getBeansList());
		String sql=beanTranslateForSql.createSelectSql(queryParameter, model);
		String sql_count = this.getCountSql(sql);
		int count = this.simpleJdbcTemplate.queryForObject(sql_count,
				Integer.class,model.getValues().toArray());
		return count;
	}

	@Override
	public BeanQuery<T> like(String field, Object value)
	{
		this.queryParameter.addQueryParameter("like"+field, value);
		return this;
	}

	@Override
	public BeanQuery<T> orLike(String[] fields, String[] values)
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableColums", fields);
		paramMap.put("values", values);
		this.queryParameter.addQueryParameter("orlike", paramMap);
		return this;
	}

	@Override
	public BeanQuery<T> eqByColum(String columName, Object value) {
		this.queryParameter.addQueryParameter("=Colum"+columName, value);
		return this;
	}

	@Override
	public PageList<T> page(int start, int pageSize, String sql_count) {
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,this.queryParameter.isJoin(),this.queryParameter.getBeansList());
		String sql=beanTranslateForSql.createSelectSql(queryParameter, model);
		String page_sql = this.dbPage.pageForParams(sql);
		List<Object> ls = new ArrayList<Object>();
		for (Object o :model.getValues())
		{
			ls.add(o);
		}
		ls.add((start-1)*pageSize);
		ls.add(start*pageSize);
		List<T> list = this.simpleJdbcTemplate.query(page_sql,
				ResultListCallbackHandler.create(cla, pageSize,model), ls.toArray());
		int count = this.simpleJdbcTemplate.queryForObject(sql_count,
				Integer.class,model.getValues().toArray());
		PageList<T> pageList = new PageList<T>(count,list);
		return pageList;
	}
	
}
