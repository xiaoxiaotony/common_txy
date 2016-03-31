package com.txy.common.orm.query.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParameter
{
	private final List<String> viewList=new ArrayList<String>(5);
	private String sortTag;
	private final List<String> sortList=new ArrayList<String>(3);
	private final Map<String,Object> argsMap=new HashMap<String,Object>(5);
	private boolean isJoin;
	private Map<String,String> beansList=new HashMap<String,String>(3);
	private String joinType="INNER";
	protected String getJoinType()
	{
		return joinType;
	}
	protected void setJoinType(String joinType)
	{
		this.joinType = joinType;
	}
	protected Map<String,String>  getBeansList()
	{
		return beansList;
	}
	protected boolean isJoin()
	{
		return isJoin;
	}
	protected void setJoin(boolean isJoin)
	{
		this.beansList.clear();
		this.isJoin = isJoin;
	}
	//添加视图查询字段
	protected void addSelectField(String field)
	{
		viewList.add(field);
	}
	protected void addQueryParameter(String key,Object value)
	{
		argsMap.put(key, value);
	}
	protected void addQueryParameter(Map<String,Object> args)
	{
		argsMap.putAll(args);
	}
	protected void addSortField(String field)
	{
		sortList.add(field);
	}
	protected void setSortTag(String sortTag)
	{
		this.sortTag=sortTag;
	}
	protected List<String> getViewList()
	{
		return viewList;
	}
	protected String getSortTag()
	{
		return sortTag;
	}
	protected List<String> getSortList()
	{
		return sortList;
	}
	protected Map<String, Object> getArgsMap()
	{
		return argsMap;
	}
	protected void setJoinBean(String bean)
	{
		this.beansList.put(bean,bean);
	}
	protected void clear()
	{
		this.viewList.clear();
		this.sortTag=null;
		this.sortList.clear();
		this.isJoin=false;
		this.argsMap.clear();
		this.beansList.clear();
	}
	
}
