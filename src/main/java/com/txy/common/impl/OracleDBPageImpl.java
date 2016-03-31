package com.txy.common.impl;

import com.txy.common.api.DBPage;

public class OracleDBPageImpl implements DBPage
{
	public String pageForName(String sql)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (");
		sb.append(sql);
		sb.append(")");
		sb.append("A)");
		sb.append(" WHERE RN BETWEEN :start AND :end");
		return sb.toString();
	}
	public String pageForParams(String sql)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (");
		sb.append(sql);
		sb.append(")");
		sb.append("A)");
		sb.append(" WHERE RN > ? AND RN <= ?");
		return sb.toString();
	}
	
	public String pageForCount(String sql)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT COUNT(*) datacount FROM (");
		sb.append(sql);
		sb.append(")");
		return sb.toString();
	}
}
