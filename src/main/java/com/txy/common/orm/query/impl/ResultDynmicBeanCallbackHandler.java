package com.txy.common.orm.query.impl;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.txy.common.util.StringUtils;


public class ResultDynmicBeanCallbackHandler implements ResultSetExtractor<List<DynamicBean>>
{
	private int pageSize;
	protected ResultDynmicBeanCallbackHandler(int pageSize)
	{
		this.pageSize=pageSize;
	}
	@Override
	public List<DynamicBean> extractData(ResultSet rs) throws SQLException,
			DataAccessException
	{
		List<DynamicBean> list=new ArrayList<DynamicBean>(pageSize);
		ResultSetMetaData metaData=rs.getMetaData();
		int count=metaData.getColumnCount();
		Map<String,String> columnMaps=new HashMap<String,String>(count);
		String columnLabel=null;
		for(int i=0;i<count;i++)
		{
			columnLabel=metaData.getColumnLabel(i+1);
			columnMaps.put(columnLabel,columnLabel);
		}
		DynamicBean bean=null;
		Object value=null;
		while(rs.next())
		{
			bean=new DynamicBean();
			for(Entry<String,String> entry:columnMaps.entrySet())
			{
				value=rs.getObject(entry.getKey());
				if(value instanceof Date && !StringUtils.isNullOrEmpty(value))
				{
					value=rs.getTimestamp(entry.getKey()).toString().substring(0, rs.getTimestamp(entry.getKey()).toString().length()-2);
				}
				if(value instanceof Clob && !StringUtils.isNullOrEmpty(value))
				{
					value=handleClob(rs.getClob(entry.getKey()));
				}
				bean.add(entry.getKey().toLowerCase(),value);
			}
			list.add(bean);
		}
		columnMaps.clear();
		return list;
	}
	
	/**
	 * 处理CLOB字段
	 * @param clob
	 * @return
	 * @throws SQLException
	 */
	public static String handleClob(Clob clob) throws SQLException {
		if (clob == null){
			return null;
		}
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			char[] buffer = new char[(int)clob.length()];
			reader.read(buffer);
			return new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			try {reader.close();} catch (IOException e) {throw new RuntimeException(e);}
		}
	}

}
