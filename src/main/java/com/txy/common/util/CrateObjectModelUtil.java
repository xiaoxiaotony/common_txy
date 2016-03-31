package com.txy.common.util;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.txy.common.bean.FieldInfoBean;
import com.txy.common.orm.ObjectModel;

public class CrateObjectModelUtil
{
	public static ObjectModel createObjectModel(Class<?> cla,boolean isJoin,Map<String,String> beanMaps)
	{
		ObjectModel model=new ObjectModel();
		model.setType(cla);
		
		Field[] fields=cla.getDeclaredFields();
		
		FieldInfoBean bean=null;
		String columnLabel=null;
		Column column=null;
		int length=-1;
		boolean nullable=false;
		for(Field f:fields)
		{
			if(!f.isAnnotationPresent(Transient.class))
			{
				
				columnLabel=f.getName().toLowerCase();
				bean=new FieldInfoBean();
				bean.setField(f);
				bean.setColumnLabel(columnLabel);
				if(f.isAnnotationPresent(Column.class))
				{
					column=f.getAnnotation(Column.class);
					length=column.length();
					nullable=column.nullable();
					columnLabel=column.name().toUpperCase();
					bean.setLength(length);
					bean.setNullable(nullable);
					bean.setColumnLabel(columnLabel);
					if(f.isAnnotationPresent(Id.class))
					{
						bean.setId(true);
						model.setId(bean);
					}
					else if(f.isAnnotationPresent(ManyToOne.class)||f.isAnnotationPresent(OneToOne.class))
					{
						bean.setForeignKey(true);
						model.getObjectModelsMap().put(columnLabel, createFieldMap(f.getType(),isJoin,beanMaps));
						
					}
				}
				model.getFieldsMap().put(f.getName(),bean);	
				model.getColumnMap().put(columnLabel,bean);
			}
		}
		return model;
	}
	private static ObjectModel createFieldMap(Class<?> cla,boolean isJoin,Map<String,String> beansMap)
	{
		ObjectModel model=new ObjectModel();
		if(beansMap!=null&&!beansMap.isEmpty()&&beansMap.get(cla.getSimpleName())==null)
		{
			isJoin=false;
		}
		model.setJoinTable(isJoin);
		model.setType(cla);
		Field[] fields=cla.getDeclaredFields();
		
		FieldInfoBean bean=null;
		String columnLabel=null;
		Column column=null;
		int length=-1;
		boolean nullable=false;
		for(Field f:fields)
		{
			if(!f.isAnnotationPresent(Transient.class))
			{
				
				columnLabel=f.getName();
				bean=new FieldInfoBean();
				bean.setField(f);
				bean.setColumnLabel(columnLabel);
				if(f.isAnnotationPresent(Column.class))
				{
					column=f.getAnnotation(Column.class);
					columnLabel=column.name();
					bean.setColumnLabel(columnLabel);
					bean.setLength(length);
					bean.setNullable(nullable);
					if(f.isAnnotationPresent(Id.class))
					{
						bean.setId(true);
						model.setId(bean);
						
					}
				}
				if(isJoin)
				{
					model.getFieldsMap().put(f.getName(),bean);	
				}
				else
				{
					if(bean.isId())
					{
						model.getFieldsMap().put(f.getName(),bean);	
					}
				}
			}
		}
		return model;
	}
	public static ObjectModel createObjectModel(Class<?> cla)
	{
		Map<String,String> rootClassMap=new HashMap<String,String>();
		Field[] fields=cla.getDeclaredFields();
		for(Field f:fields)
		{
			rootClassMap.put(f.getName(),f.getType().getClass().getSimpleName());
		}
		return null;
	}
}
