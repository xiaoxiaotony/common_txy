package com.txy.common.impl;

import com.txy.common.api.DBPage;

public class MySqlDBPageImpl implements DBPage
{
	public String pageForName(String sql)
	{
		StringBuffer sb=new StringBuffer(sql);
		sb.append(" limit :start,:end");
		return sb.toString();
	}
	public String pageForParams(String sql)
	{
		StringBuffer sb=new StringBuffer(sql);
		sb.append(" limit ?,?");
		return sb.toString();
	}
	@Override
	public String pageForCount(String sql)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
