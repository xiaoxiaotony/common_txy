package com.txy.common.orm.query.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.txy.common.orm.query.DataConver;

public class OracleDataConver implements DataConver
{

	@Override
	public <T> T getValue(Class<T> cla, String columnLabel, ResultSet rs)
			throws SQLException
	{
		String name=cla.getSimpleName();
		Object value=null;
		if("int".equals(name)||"Integer".equals(name))
		{
			value=getInt(columnLabel,rs);
			
		}
		else if("float".equals(name)||"Float".equals(name))
		{
			value=getFloat(columnLabel,rs);
			
		}
		else if("long".equals(name)||"Long".equals(name))
		{
			value=getLong(columnLabel,rs);
		}
		else if("Date".equals(name))
		{
			value=new Date();
		}
		else
		{
			value=rs.getObject(columnLabel);
		}
		return cla.cast(value);
	}
	private  int getInt(String column,ResultSet rs)throws SQLException
	{
		BigDecimal bigDecimal=rs.getBigDecimal(column);
		if(bigDecimal!=null)
		{
			return bigDecimal.intValue();
		}
		return 0;
		
	}
	private  long getLong(String column,ResultSet rs)throws SQLException
	{
		BigDecimal bigDecimal=rs.getBigDecimal(column);
		if(bigDecimal!=null)
		{
			return bigDecimal.longValue();
		}
		return 0;
	}
	private  float getFloat(String column,ResultSet rs)throws SQLException
	{
		BigDecimal bigDecimal=rs.getBigDecimal(column);
		if(bigDecimal!=null)
		{
			return bigDecimal.floatValue();
		}
		return 0;
	}
}
