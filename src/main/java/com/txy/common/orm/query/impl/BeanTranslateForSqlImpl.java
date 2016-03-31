package com.txy.common.orm.query.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.txy.common.bean.FieldInfoBean;
import com.txy.common.orm.ObjectModel;
import com.txy.common.orm.query.BeanTranslateForSql;
import com.txy.common.util.ObjectModelUtil;

public class BeanTranslateForSqlImpl implements BeanTranslateForSql
{
	
	/**
	 * 组装查询的sql
	 */
	@Override
	public String createSelectSql(QueryParameter queryParameter, ObjectModel model)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(createViewSql(model, queryParameter.getViewList(), queryParameter.isJoin(), queryParameter.getBeansList()));
		sql.append(createJoinSql(model, queryParameter.isJoin(), queryParameter.getJoinType()));
		sql.append(createWhereSql(model, queryParameter.getArgsMap()));
		sql.append(createSortSql(model, queryParameter.getSortTag(), queryParameter.getSortList()));
		queryParameter.clear();
		return sql.toString();
	}
	
	/**
	 * 组装连接查询条件
	 * 
	 * @param model
	 * @param isJoin
	 * @param joinType
	 * @return
	 */
	private String createJoinSql(ObjectModel model, boolean isJoin, String joinType)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM ");
		sql.append(model.getTableName());
		sql.append(" ");
		sql.append(model.getAsTable());
		
		for (Entry<String, ObjectModel> entry : model.getObjectModelsMap().entrySet())
		{
			if (entry.getValue().isJoinTable())
			{
				sql.append(" ");
				sql.append(joinType);
				sql.append(" ");
				sql.append(" JOIN ");
				sql.append(entry.getValue().getTableName());
				sql.append(" ");
				sql.append(entry.getValue().getAsTable());
				sql.append(" ON ");
				sql.append(model.getAsTable());
				sql.append(".");
				sql.append(entry.getKey());
				sql.append("=");
				sql.append(entry.getValue().getAsTable());
				sql.append(".");
				sql.append(entry.getValue().getId().getColumnLabel());
			}
			
		}
		return sql.toString();
	}
	
	/**
	 * 组装查询视图
	 * 
	 * @param model
	 * @param viewFields
	 * @param isJoin
	 * @param beanMaps
	 * @return
	 */
	private String createViewSql(ObjectModel model, List<String> viewFields, boolean isJoin, Map<String, String> beanMaps)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		FieldInfoBean bean = null;
		if (viewFields == null || viewFields.isEmpty())
		{
			for (Entry<String, FieldInfoBean> entry : model.getFieldsMap().entrySet())
			{
				bean = ObjectModelUtil.getColumnLabel(model, entry.getValue().getField().getName());
				
				sql.append(bean.getAsTable() + "." + bean.getColumnLabel());
				sql.append(" AS ");
				sql.append(bean.getAsColumnLabel());
				sql.append(",");
			}
			if (isJoin)
			{
				for (Entry<String, ObjectModel> entry : model.getObjectModelsMap().entrySet())
				{
					if (beanMaps == null || beanMaps.isEmpty() || beanMaps.get(entry.getValue().getClassName()) != null)
					{
						for (Entry<String, FieldInfoBean> ent : entry.getValue().getFieldsMap().entrySet())
						{
							
							bean = ObjectModelUtil.getColumnLabel(entry.getValue(), ent.getValue().getField().getName());
							sql.append(bean.getAsTable() + "." + bean.getColumnLabel());
							sql.append(" AS ");
							sql.append(bean.getAsColumnLabel());
							sql.append(",");
						}
					}
					
				}
			}
		}
		else
		{
			for (String field : viewFields)
			{
				bean = ObjectModelUtil.getColumnLabel(model, field);
				sql.append(bean.getAsTable() + "." + bean.getColumnLabel());
				sql.append(" AS ");
				sql.append(bean.getAsColumnLabel());
				sql.append(",");
			}
			for (Entry<String, FieldInfoBean> entry : model.getFieldsMap().entrySet())
			{
				if (entry.getValue().isForeignKey())
				{
					sql.append(model.getAsTable() + "." + entry.getValue().getColumnLabel());
					sql.append(" AS ");
					sql.append(entry.getValue().getAsColumnLabel());
					sql.append(",");
				}
			}
		}
		return sql.substring(0, sql.length() - 1).toString() + " ";
	}
	
	/**
	 * 组装排序查询条件
	 * 
	 * @param model
	 * @param sortTag
	 * @param sortList
	 * @return
	 */
	private String createSortSql(ObjectModel model, String sortTag, List<String> sortList)
	{
		if (sortTag == null || sortList == null || sortList.isEmpty())
		{
			return "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" ORDER BY ");
		FieldInfoBean bean = null;
		for (String field : sortList)
		{
			bean = ObjectModelUtil.getColumnLabel(model, field);
			sql.append(bean.getAsTable());
			sql.append(".");
			sql.append(bean.getColumnLabel());
			sql.append(",");
		}
		return sql.substring(0, sql.length() - 1).toString() + " " + sortTag;
	}
	
	/**
	 * 组装where条件
	 * 
	 * @param model
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String createWhereSql(ObjectModel model, Map<String, Object> args)
	{
		
		if (args == null || args.isEmpty())
		{
			return "";
		}
		String condition = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" WHERE 1=1");
		String columnLabel = null;
		FieldInfoBean bean = null;
		for (Entry<String, Object> entry : args.entrySet())
		{
			if (entry.getKey().contains("orlike") && null != entry.getValue())
			{
				condition = entry.getKey().substring(2, 6);
				Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
				sql.append(" AND ( ");
				String[] fields = (String[]) valueMap.get("tableColums");
				String[] values = (String[]) valueMap.get("values");
				for (int i = 0; i < fields.length; i++)
				{
					columnLabel = fields[i];
					sql.append(columnLabel);
					if (i == fields.length - 1)
					{
						sql.append(" like ? ");
					}
					else
					{
						sql.append(" like ? or ");
					}
					model.addQueryParameter("%" + values[i] + "%");
				}
				sql.append(" ) ");
			}
			else if (entry.getKey().contains("like"))
			{
				if (null != entry.getValue() && "" != entry.getValue())
				{
					condition = entry.getKey().substring(0, 4);
					columnLabel = entry.getKey().substring(4, entry.getKey().length());
					sql.append(" AND ");
					bean = ObjectModelUtil.getColumnLabel(model, columnLabel);
					sql.append(bean.getAsTable() + "." + bean.getColumnLabel());
					sql.append(" " + condition);
					sql.append(" ? ");
					model.addQueryParameter("%" + entry.getValue() + "%");
				}
			}
			else if (entry.getKey().contains("=Colum"))
			{
				condition = entry.getKey().substring(0, 1);
				columnLabel = entry.getKey().substring(6, entry.getKey().length());
				sql.append(" AND ");
				sql.append(model.getAsTable() + "." + columnLabel);
				sql.append(condition);
				sql.append("?");
				model.addQueryParameter(entry.getValue());
			}
			else
			{
				condition = entry.getKey().substring(0, 1);
				columnLabel = entry.getKey().substring(1);
				sql.append(" AND ");
				bean = ObjectModelUtil.getColumnLabel(model, columnLabel);
				sql.append(bean.getAsTable() + "." + bean.getColumnLabel());
				sql.append(condition);
				sql.append("?");
				model.addQueryParameter(entry.getValue());
			}
		}
		return sql.toString();
	}
	
	@Override
	public String createInsertSql(ObjectModel model)
	{
		StringBuilder sql = new StringBuilder();
		StringBuilder tempBuilder = new StringBuilder(model.getFieldsMap().size() * 2);
		sql.append("INSERT INTO ");
		sql.append(model.getTableName());
		sql.append("(");
		for (Entry<String, FieldInfoBean> entry : model.getFieldsMap().entrySet())
		{
			//修改主键的生成策略
			sql.append(entry.getValue().getColumnLabel());
			sql.append(",");
			if("id".equalsIgnoreCase(entry.getValue().getColumnLabel()) && entry.getValue().getField().getGenericType().toString().equals("class java.lang.Long")){
				tempBuilder.append("S_"+model.getTableName()+".nextval");
			}else{
				tempBuilder.append("?");
			}
			tempBuilder.append(",");
		}
		sql = new StringBuilder(sql.substring(0, sql.length() - 1).toString());
		sql.append(")VALUES(");
		sql.append(tempBuilder.substring(0, tempBuilder.length() - 1).toString());
		sql.append(")");
		return sql.toString();
	}
}
