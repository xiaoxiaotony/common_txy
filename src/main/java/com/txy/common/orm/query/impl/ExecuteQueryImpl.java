package com.txy.common.orm.query.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.txy.common.bean.FieldInfoBean;
import com.txy.common.exception.ServiceException;
import com.txy.common.orm.ObjectModel;
import com.txy.common.orm.query.BeanTranslateForSql;
import com.txy.common.orm.query.ExecuteQuery;
import com.txy.common.util.CrateObjectModelUtil;
import com.txy.common.util.ObjectModelUtil;
import com.txy.common.util.StringUtils;

@Component
public class ExecuteQueryImpl implements ExecuteQuery
{
	@Autowired
	private JdbcTemplate simpleJdbcTemplate;
	
	private final BeanTranslateForSql beanTranslateForSql = new BeanTranslateForSqlImpl();
	
	private static final Logger logger = Logger.getLogger(ExecuteQueryImpl.class);
	
	public JdbcTemplate getSimpleJdbcTemplate()
	{
		return simpleJdbcTemplate;
	}
	
	/**
	 * 单挑对象插入
	 */
	public <T> int insert(T t, boolean cascade)
	{
		ObjectModel model = CrateObjectModelUtil.createObjectModel(t.getClass(), cascade, null);
		FieldInfoBean bean = null;
		String foreignkeyLabel = null;
		Object o = null;
		Object value = null;
		ObjectModel foreignkeyModel = null;
		String sql = beanTranslateForSql.createInsertSql(model);
		List<Object> listArrays = new ArrayList<Object>();
		String idFieldName = null;
		for (Entry<String, FieldInfoBean> entry : model.getFieldsMap().entrySet())
		{
			
			bean = entry.getValue();
			if (bean.isForeignKey() && cascade)
			{
				// 外键Key
				foreignkeyLabel = bean.getColumnLabel();
				// 外键所对应的对象
				o = this.getForeignkeyBean(t, bean.getField().getName());
				// 外键所对应的ObjectModel
				foreignkeyModel = model.getObjectModelsMap().get(foreignkeyLabel);
				idFieldName = this.getIdField(foreignkeyModel.getFieldsMap()).getField().getName();
				// 关联对象主键值
				value = this.getForeignkeyBean(o, idFieldName);
				listArrays.add(value);
				// 处理外键信息
				if (value != null)
				{
					invokForeignkeyInfo(foreignkeyModel, o, value);
				}
			}
			else
			{
				value = this.getForeignkeyBean(t, bean.getField().getName());
				if (bean.getColumnLabel().equalsIgnoreCase("id") && value == null&&bean.getField().getType().toString().equals("class java.lang.String"))
				{
					value = StringUtils.getPrimarykeyId();
					try {
						PropertyUtils.setProperty(t, bean.getField().getName(), value);
					} catch (Exception e) {
						logger.error("setProperty error......");
						e.printStackTrace();
					}
				}
				if (bean.getColumnLabel().equalsIgnoreCase("id") && value == null&&bean.getField().getType().toString().equals("class java.lang.Long")&&sql.indexOf(".nextval")==-1)
				{
					value = "S_"+model.getTableName()+".nextval";
				}
				if(bean.getColumnLabel().equalsIgnoreCase("id") && value == null&&bean.getField().getType().toString().equals("class java.lang.Long")&&sql.indexOf(".nextval")>-1){
					continue;
				}
				if (!bean.isNullable() && value == null)
				{
					throw new ServiceException("字段:[" + bean.getField().getName() + "]不能为Null");
				}
				if (value != null)
				{
					if (bean.getField().getType().getSimpleName().equals("String")
							&& value.toString().length() > bean.getLength())
					{
						throw new ServiceException("字段:[" + bean.getField().getName() + "]长度超过定义最大值["
								+ bean.getLength() + "]");
					}
				}
				listArrays.add(value);
			}
		}
		/**
		 * 处理主表信息
		 */
		return this.simpleJdbcTemplate.update(sql, listArrays.toArray());
	}
	
	
	/**
	 * 单挑对象插入
	 */
	public <T> int insert(T t)
	{
		ObjectModel model = CrateObjectModelUtil.createObjectModel(t.getClass(), false,null);
		FieldInfoBean bean = null;
		Object value = null;
		List<Object> listArrays = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		StringBuilder tempBuilder = new StringBuilder(model.getFieldsMap().size() * 2);
		sql.append("INSERT INTO ");
		sql.append(model.getTableName());
		sql.append("(");
		for (Entry<String, FieldInfoBean> entry : model.getFieldsMap().entrySet())
		{

			bean = entry.getValue();
			value = this.getForeignkeyBean(t, bean.getField().getName());
			if (bean.getColumnLabel().equalsIgnoreCase("id") && value == null&&bean.getField().getType().toString().equals("class java.lang.String"))
			{
				value = StringUtils.getPrimarykeyId();
				try {
					PropertyUtils.setProperty(t, bean.getField().getName(), value);
				} catch (Exception e) {
					logger.error("setProperty error......");
					e.printStackTrace();
				}
			}
			if (bean.getColumnLabel().equalsIgnoreCase("id") && value == null&&bean.getField().getType().toString().equals("class java.lang.Long")&&sql.indexOf(".nextval")==-1)
			{
				value = "S_"+model.getTableName()+".nextval";
			}
			if(bean.getColumnLabel().equalsIgnoreCase("id") && value == null&&bean.getField().getType().toString().equals("class java.lang.Long")&&sql.indexOf(".nextval")>-1){
				continue;
			}
//			if (!bean.isNullable() && value == null)
//			{
//				throw new ServiceException("字段:[" + bean.getField().getName() + "]不能为Null");
//			}
			if (value != null)
			{
				if (bean.getField().getType().getSimpleName().equals("String")
						&& value.toString().length() > bean.getLength())
				{
					throw new ServiceException("字段:[" + bean.getField().getName() + "]长度超过定义最大值["
							+ bean.getLength() + "]");
				}
				
				//修改主键的生成策略
				sql.append(entry.getValue().getColumnLabel());
				sql.append(",");
				if("id".equalsIgnoreCase(entry.getValue().getColumnLabel()) && entry.getValue().getField().getGenericType().toString().equals("class java.lang.Long")){
					tempBuilder.append("S_"+model.getTableName()+".nextval");
				}else{
					tempBuilder.append("?");
				}
				tempBuilder.append(",");
				listArrays.add(value);
			}
		}
		sql = new StringBuilder(sql.substring(0, sql.length() - 1).toString());
		sql.append(")VALUES(");
		sql.append(tempBuilder.substring(0, tempBuilder.length() - 1).toString());
		sql.append(")");
		/**
		 * 处理主表信息
		 */
		return this.simpleJdbcTemplate.update(sql.toString(), listArrays.toArray());
	}
	
	/**
	 * 批量保存
	 */
	public <T> int insert(List<T> list, boolean cascade)
	{
		for (T t : list)
		{
			this.insert(t, cascade);
		}
		return 0;
	}
	
	/**
	 * 删除数据
	 */
	@Override
	public <T> int delete(Class<T> cla, Map<String, Object> conditionMap)
	{
		ObjectModel model = CrateObjectModelUtil.createObjectModel(cla, false, null);
		String sql = this.createDeleteSql(model, conditionMap);
		return this.getSimpleJdbcTemplate().update(sql.toString(), model.getValues().toArray());
	}
	
	/**
	 * 删除数据 传入实例化对象
	 */
	@Override
	public <T> int delete(Class<T> cla, Object value)
	{
		ObjectModel model = CrateObjectModelUtil.createObjectModel(cla, false, null);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(model.getId().getField().getName(), value);
		String sql = this.createDeleteSql(model, args);
		return this.getSimpleJdbcTemplate().update(sql.toString(), value);
	}
	
	// 处理外键信息，如果外键不存在则先保存到外键所对应的表
	private void invokForeignkeyInfo(ObjectModel model, Object o, Object value)
	{
		String sql = this.getSelectCountSql(model);
		if (this.simpleJdbcTemplate.queryForObject(sql, Integer.class, value) == 0)
		{
			insert(o, false);
		}
	}
	
	private FieldInfoBean getIdField(Map<String, FieldInfoBean> fieldMap)
	{
		FieldInfoBean bean = null;
		for (Entry<String, FieldInfoBean> entry : fieldMap.entrySet())
		{
			if (entry.getValue().isId())
			{
				bean = entry.getValue();
				break;
			}
		}
		if (bean == null)
		{
			throw new ServiceException("实体未定义主键");
		}
		return bean;
	}
	
	private String getSelectCountSql(ObjectModel model)
	{
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(0) FROM ");
		sql.append(model.getTableName());
		sql.append(" WHERE ");
		sql.append(this.getIdField(model.getFieldsMap()).getColumnLabel());
		sql.append("=?");
		return sql.toString();
	}
	
	// 得到外键所对应的对象
	private Object getForeignkeyBean(Object bean, String fieldName)
	{
		if (bean == null)
		{
			return null;
		}
		Object o = null;
		
		try
		{
			o = PropertyUtils.getProperty(bean, fieldName);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw new ServiceException("", e);
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
			throw new ServiceException("", e);
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
			throw new ServiceException("", e);
		}
		return o;
	}
	
	/**
	 * 修改
	 */
	@Override
	public <T> int update(T t)
	{
		int result = 0;
		ObjectModel model = CrateObjectModelUtil.createObjectModel(t.getClass(), false, null);
		Object idValue = null;
		try
		{
			idValue = PropertyUtils.getProperty(t, model.getId().getField().getName());
			if (idValue == null)
			{
				throw new ServiceException("实体[" + t.getClass().getName() + "]主键[" + model.getId().getField().getName()
						+ "]值不能为空");
			}
			Map<String, Object> whereMap = new HashMap<String, Object>(0);
			whereMap.put(model.getId().getField().getName(), idValue);
			String sql = this.createUpdateSql(model, whereMap, t);
			if (sql == null)
			{
				throw new ServiceException("转换的SQL不能为空");
			}
			result = this.getSimpleJdbcTemplate().update(sql, model.getValues().toArray());
		}
		catch (IllegalAccessException e)
		{
			throw new ServiceException("更新实体异常", e);
		}
		catch (InvocationTargetException e)
		{
		}
		catch (NoSuchMethodException e)
		{
			throw new ServiceException("更新实体异常", e);
		}
		return result;
	}
	
	/**
	 * 修改数据，传入条件
	 */
	@Override
	public <T> int update(T t, Map<String, Object> conditionMap)
	{
		int result;
		ObjectModel model = CrateObjectModelUtil.createObjectModel(t.getClass(), false, null);
		try
		{
			String sql = this.createUpdateSql(model, conditionMap, t);
			if (sql == null)
			{
				throw new ServiceException("转换的SQL不能为空");
			}
			logger.info(sql);
			result = this.getSimpleJdbcTemplate().update(sql, model.getValues().toArray());
		}
		catch (IllegalAccessException e)
		{
			throw new ServiceException("更新实体异常", e);
		}
		catch (InvocationTargetException e)
		{
			throw new ServiceException("更新实体异常", e);
		}
		catch (NoSuchMethodException e)
		{
			throw new ServiceException("更新实体异常", e);
		}
		return result;
	}
	
	private String createUpdateSql(ObjectModel model, Map<String, Object> fieldMap, Object t)
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if (fieldMap == null || fieldMap.isEmpty() || t == null)
		{
			return null;
		}
		StringBuilder sql = new StringBuilder();
		FieldInfoBean fieldInfoBean = null;
		String columnLabel = null;
		Field field = null;
		Object value = null;
		sql.append("UPDATE ");
		sql.append(model.getTableName());
		sql.append(" SET ");
		String fieldName = null;
		for (Entry<String, FieldInfoBean> entry : model.getFieldsMap().entrySet())
		{
			fieldInfoBean = entry.getValue();
			field = fieldInfoBean.getField();
			fieldName = field.getName();
			
			if (fieldInfoBean.isId())
			{
				continue;
			}
			
			columnLabel = fieldInfoBean.getColumnLabel();
			value = PropertyUtils.getProperty(t, field.getName());
			if (value == null)
			{
				continue;
			}
			sql.append(columnLabel);
			sql.append("=?");
			sql.append(",");
			if (fieldInfoBean.isForeignKey())
			{
				Object foreignKeyValue = null;
				fieldName = model.getObjectModelsMap().get(columnLabel).getId().getField().getName();
				foreignKeyValue = PropertyUtils.getProperty(value, fieldName);
				if (foreignKeyValue == null)
				{
					continue;
				}
				
				model.getValues().add(foreignKeyValue);
			}
			else
			{
				model.getValues().add(value);
			}
			
		}
		if (model.getValues().isEmpty())
		{
			return null;
		}
		sql = new StringBuilder(sql.substring(0, sql.length() - 1).toString());
		sql.append(" WHERE 1=1");
		columnLabel = null;
		for (Entry<String, Object> entry : fieldMap.entrySet())
		{
			sql.append(" AND ");
			columnLabel = ObjectModelUtil.getColumnLabel(model, entry.getKey()).getColumnLabel();
			if (columnLabel == null)
			{
				throw new ServiceException("未找到字段[" + entry.getKey() + "]");
			}
			sql.append(columnLabel);
			sql.append("=?");
			model.getValues().add(entry.getValue());
		}
		return sql.toString();
	}
	
	private String createDeleteSql(ObjectModel model, Map<String, Object> args)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(model.getTableName());
		if (args == null || args.isEmpty())
		{
			return sql.toString();
		}
		sql.append(" WHERE 1=1");
		for (Entry<String, Object> entry : args.entrySet())
		{
			sql.append(" AND ");
			sql.append(ObjectModelUtil.getColumnLabel(model, entry.getKey()).getColumnLabel());
			sql.append("=?");
			model.getValues().add(entry.getValue());
		}
		logger.info(sql.toString());
		return sql.toString();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int insert(String table, Map<String, Object> map)
	{
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlSubffer = new StringBuffer();
		sql.append("insert into ");
		sql.append(table + " ");
		sql.append("(");
		List<Object> args = new ArrayList<Object>();
		Set<String> key = map.keySet();
		StringBuffer valueStr = new StringBuffer();
		for (Iterator it = key.iterator(); it.hasNext();)
		{
			String column = (String) it.next();
			sql.append((String) column + ",");
			if(column.equalsIgnoreCase("id") && map.get(column).toString().contains(".nextval")){
				valueStr.append(map.get(column)+",");
			}else{
				valueStr.append("?,");
				args.add(map.get(column));
			}
		}
		sqlSubffer.append(sql.substring(0, sql.length() - 1));
		sqlSubffer.append(")");
		sqlSubffer.append(" VALUES (");
		sqlSubffer.append(valueStr.substring(0, valueStr.length() - 1));
		sqlSubffer.append(")");
		int num = 0;
		try
		{
			num = this.getSimpleJdbcTemplate().update(sqlSubffer.toString().toLowerCase(), args.toArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return num;
	}
	
	@Override
	public int delete(String table, String colum, String planId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append(table);
		sql.append(" WHERE ");
		sql.append(colum);
		sql.append(" = ? ");
		return this.getSimpleJdbcTemplate().update(sql.toString(), planId);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int update(String table, Map<String, Object> map, String primaryKey, Object value)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET ");
		Set<String> key = map.keySet();
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlSubffer = new StringBuffer();
		for (Iterator it = key.iterator(); it.hasNext();)
		{
			String column = (String) it.next();
			if (!StringUtils.isNullOrEmpty(map.get(column)))
			{
				Object objValue = map.get(column);
				if(StringUtils.isDateType(objValue.toString())){
					sql.append(column+" = to_date (? , 'YYYY-MM-DD HH24:MI:SS'),");
				}else{
					sql.append(column.toUpperCase() + " = ?,");
				}
				args.add(objValue);
			}
		}
		sqlSubffer.append(sql.substring(0, sql.length() - 1));
		sqlSubffer.append(" WHERE ");
		sqlSubffer.append(primaryKey.toUpperCase() + " = ?");
		args.add(value);
		System.err.println("执行sql:"+sqlSubffer.toString());
		System.err.println("传入参数个数："+args.toArray().length);
		return this.getSimpleJdbcTemplate().update(sqlSubffer.toString(), args.toArray());
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public int delete(String tableName, Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlSubffer = new StringBuffer();
		sql.append("delete from ");
		sql.append(tableName + " ");
		sql.append("where 1 = 1 and ");
		List<Object> args = new ArrayList<Object>();
		Set<String> key = map.keySet();
		for (Iterator it = key.iterator(); it.hasNext();)
		{
			String column = (String) it.next();
			sql.append((String) column + " = ? and ");
			args.add(map.get(column));
		}
		sqlSubffer.append(sql.substring(0, sql.length() - 5));
		return this.getSimpleJdbcTemplate().update(sqlSubffer.toString(), args.toArray());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int update(String tableName, Map<String, Object> paramMap, Map<String, Object> conditMap) {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlSubffer = new StringBuffer();
		sql.append("update ");
		sql.append(tableName + " ");
		sql.append("set ");
		List<Object> args = new ArrayList<Object>();
		Set<String> key = paramMap.keySet();
		for (Iterator it = key.iterator(); it.hasNext();)
		{
			String column = (String) it.next();
			sql.append((String) column + " = ?,");
			args.add(paramMap.get(column));
		}
		sqlSubffer.append(sql.substring(0, sql.length() - 1)+" where 1 = 1 and ");
		if(paramMap.isEmpty()){
			logger.error("多条件修改方法传入参数为空......");
			return 0;
		}
		Set<String> set = conditMap.keySet();
		sql.setLength(0);
		for (Iterator it = set.iterator(); it.hasNext();)
		{
			String column = (String) it.next();
			sql.append((String) column + " = ? and ");
			args.add(conditMap.get(column));
		}
		sqlSubffer.append(sql.substring(0, sql.length() - 5));
		return this.getSimpleJdbcTemplate().update(sqlSubffer.toString(), args.toArray());
	}

	@Override
	public <T> int insertOrUpdate(T t) {
		ObjectModel model = CrateObjectModelUtil.createObjectModel(t.getClass(), false, null);
		Object idValue = null;
		try {
			idValue = PropertyUtils.getProperty(t, model.getId().getField().getName());
			if (idValue == null)
			{
				throw new ServiceException("实体[" + t.getClass().getName() + "]主键[" + model.getId().getField().getName()
						+ "]值不能为空");
			}
			String key_name = model.getId().getColumnLabel();
			String table_name = model.getTableName();
			int check_val = this.getSimpleJdbcTemplate().queryForObject("select count(1) from "+table_name +" where "+ key_name +" = ?",new Object[]{idValue}, Integer.class);
			if(check_val == 0) return this.insert(t);
			return this.update(t);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 0;
	}
}
