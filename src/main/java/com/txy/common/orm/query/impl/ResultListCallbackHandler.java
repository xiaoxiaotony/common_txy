package com.txy.common.orm.query.impl;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.txy.common.bean.FieldInfoBean;
import com.txy.common.exception.ServiceException;
import com.txy.common.orm.ObjectModel;
import com.txy.common.util.ResultConverUtil;

public class ResultListCallbackHandler<T> implements ResultSetExtractor<List<T>>
{

	private static final Logger logger = Logger.getLogger(ResultListCallbackHandler.class);
	
	private Class<T>	cla;
	private int			pageSize;
	private ObjectModel	model;

	public ResultListCallbackHandler(Class<T> cla, int pageSize, ObjectModel model)
	{
		this.cla = cla;
		this.pageSize = pageSize;
		this.model = model;
	}

	public static <T> ResultListCallbackHandler<T> create(Class<T> cla, ObjectModel model)
	{
		ResultListCallbackHandler<T> resultListCallbackHandler = new ResultListCallbackHandler<T>(cla, 1, model);
		return resultListCallbackHandler;
	}

	public static <T> ResultListCallbackHandler<T> create(Class<T> cla, int pageSize, ObjectModel model)
	{
		ResultListCallbackHandler<T> resultListCallbackHandler = new ResultListCallbackHandler<T>(cla, pageSize, model);
		return resultListCallbackHandler;
	}

	@Override
	public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException
	{
		List<T> resultList = new ArrayList<T>(pageSize);

		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount();
		Map<String, String> columnMaps = new HashMap<String, String>(count);
		String columnLabel = null;
		for (int i = 0; i < count; i++)
		{
			columnLabel = metaData.getColumnLabel(i + 1);
			columnMaps.put(columnLabel, columnLabel);
		}
		Object value = null;
		Object reBean = null;
		T t = null;
		while (rs.next())
		{
			try
			{
				t = cla.newInstance();
				for (Entry<String, FieldInfoBean> entry : model.getFieldsMap().entrySet())
				{
					columnLabel = entry.getValue().getAsColumnLabel();
					if (columnMaps.get(columnLabel) != null)
					{
						value = ResultConverUtil.getValue(entry.getValue().getField().getType(), columnLabel, rs);
						if (value == null)
						{
							continue;
						}
						if (entry.getValue().isForeignKey())
						{
							if (value != null)
							{
								reBean = entry.getValue().getField().getType().newInstance();
								getObject(rs, columnMaps, reBean, model.getObjectModelsMap().get(entry.getValue().getColumnLabel()).getFieldsMap(), value);
								PropertyUtils.setProperty(t, entry.getValue().getField().getName(), reBean);
							}
						}
						else
						{
							// 判断是否为CLOB字段,若是则进行特殊处理
							if (value != null && value != "")
							{
								if (value.toString().contains("oracle.sql.CLOB"))
								{
									value = clobToString(value);
								}
							}
							//发布的时候记得取消try catch
							try {
								PropertyUtils.setProperty(t, entry.getValue().getField().getName(), value);
							} catch (Exception e) {
								logger.info(t.toString() +"里："+entry.getValue().getField().getName()+"和数据库返回的值的类型不一样");
								e.printStackTrace();
							}
						}

					}
				}
			}
			catch (InstantiationException e)
			{
				throw new ServiceException("数据查询异常[" + model.getType().getName() + "]", e);

			}
			catch (IllegalAccessException e)
			{
				throw new ServiceException("数据查询异常[" + model.getType().getName() + "]", e);
			}
			catch (InvocationTargetException e)
			{
				throw new ServiceException("数据查询异常[" + model.getType().getName() + "]", e);
			}
			catch (NoSuchMethodException e)
			{
				throw new ServiceException("数据查询异常[" + model.getType().getName() + "]", e);
			}

			resultList.add(t);
		}
		return resultList;
	}

	private void getObject(ResultSet rs, Map<String, String> columnMap, Object o, Map<String, FieldInfoBean> fieldsMap, Object idValue) throws InvocationTargetException, IllegalAccessException, SQLException, NoSuchMethodException
	{
		String columnLabel = null;
		Object value = null;
		for (Entry<String, FieldInfoBean> entry : fieldsMap.entrySet())
		{
			if (entry.getValue().isId())
			{
				PropertyUtils.setProperty(o, entry.getValue().getField().getName(), idValue);
				continue;
			}
			columnLabel = entry.getValue().getAsColumnLabel();
			if (columnMap.get(columnLabel) != null)
			{
				value = ResultConverUtil.getValue(entry.getValue().getField().getType(), columnLabel, rs);
				PropertyUtils.setProperty(o, entry.getValue().getField().getName(), value);
			}
		}

	}

	/**
	 * 数据库Clob对象转换为String
	 */
	public static String clobToString(Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		try
		{
			Clob clob = (Clob) obj;
			Reader inStreamDoc = clob.getCharacterStream();

			char[] tempDoc = new char[(int) clob.length()];
			inStreamDoc.read(tempDoc);
			inStreamDoc.close();
			return new String(tempDoc);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SQLException es)
		{
			es.printStackTrace();
		}

		return null;
	}

}
