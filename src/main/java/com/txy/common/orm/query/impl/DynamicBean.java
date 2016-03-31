package com.txy.common.orm.query.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;

import com.txy.common.bean.FieldInfoBean;
import com.txy.common.exception.ServiceException;
import com.txy.common.impl.AbstractBox;
import com.txy.common.orm.ObjectModel;
import com.txy.common.util.CrateObjectModelUtil;


public class DynamicBean extends AbstractBox<Object>
{
	public <T> T getValue(Class<T> cla,String column)
	{
		return this.convert(cla,column);
	}
	public String getValue(String column)
	{
		return this.getValue(String.class, column);
	}
	public String getValueNotEmpty(String column)
	{
		String value= getValue(column);
		if(value==null)
		{
			throw new ServiceException(column+"不能为Null");
		}
		return value;
	}
	public <T> T getValueNotEmpty(Class<T> cla,String column)
	{
		T t=this.getValue(cla, column);
		if(t==null)
		{
			throw new ServiceException(column+"不能为Null");
		}
		return t;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <K> K convert(Class<K> cla, String key)
	{
		String typeName=cla.getSimpleName();
		if(typeName.equals("Integer"))
		{
			BigDecimal bigDecimal=(BigDecimal)this.dataMap.get(key);
			if(bigDecimal==null)
			{
				return cla.cast(0);
			}
			return cla.cast(bigDecimal.intValue());
		}
		else if(typeName.equals("Long"))
		{
			BigDecimal bigDecimal=(BigDecimal)this.dataMap.get(key);
			if(bigDecimal==null)
			{
				return cla.cast(0);
			}
			return cla.cast(bigDecimal.intValue());
		}
		else if(typeName.equals("Float"))
		{
			BigDecimal bigDecimal=(BigDecimal)this.dataMap.get(key);
			if(bigDecimal==null)
			{
				return cla.cast(0);
			}
			return cla.cast(bigDecimal.intValue());
		}
		else if(typeName.equals("String"))
		{
			Object obj = this.dataMap.get(key);
			if(obj==null)
			{
				return null;
			}
			return (K) String.valueOf(obj);
		}
		else
		{
			return cla.cast(this.dataMap.get(key));
		}
	}
	public <T> T toBean(Class<T> cla)throws IllegalAccessException, InstantiationException,NoSuchMethodException,InvocationTargetException
	{
		ObjectModel model=CrateObjectModelUtil.createObjectModel(cla,true,null);
		T t=cla.newInstance();
		toBean(model,t);
		return t;
	}
	private Object toBean(ObjectModel model,Object o)throws IllegalAccessException, InstantiationException,NoSuchMethodException,InvocationTargetException
	{
		String columnLabel=null;
		FieldInfoBean bean=null;
		Object value=null;
		
		for(Entry<String,Object> entry:this.dataMap.entrySet())
		{
			columnLabel=entry.getKey();
			bean=model.getColumnMap().get(columnLabel);
			value=entry.getValue();
			if(bean!=null&&value!=null)
			{
				if(bean.isForeignKey())
				{
					ObjectModel refModel=model.getObjectModelsMap().get(columnLabel);
					Object refBean=toBean(refModel,bean.getField().getType().newInstance());
					PropertyUtils.setProperty(refBean,refModel.getId().getField().getName(), value);
					PropertyUtils.setProperty(o,bean.getField().getName(),refBean);
					continue;
				}
				PropertyUtils.setProperty(o,bean.getField().getName(),value);
			}
		}
		return o;
	}
	
    public Map<String, Object> getMap() {
        return this.dataMap;
    }
}
