package com.txy.common.util;


import com.txy.common.bean.FieldInfoBean;
import com.txy.common.exception.ServiceException;
import com.txy.common.orm.ObjectModel;

public class ObjectModelUtil
{
	/**
	 * 根据字段名称获取列名，需要注意对外键的处理
	 * @param model
	 * @param fieldName
	 * @return
	 */
	public static FieldInfoBean getColumnLabel(ObjectModel model,String fieldName)
	{
		String arrays[]=fieldName.split("\\.");
		FieldInfoBean bean=null;
		ObjectModel tempModel=null;
		if(arrays.length==1)
		{
			bean=model.getFieldsMap().get(fieldName);
			tempModel=model;
		}
		else if(arrays.length==2)
		{
			String attribute=arrays[0];
			fieldName=arrays[1];
			bean=model.getFieldsMap().get(attribute);
			if(bean==null)
			{
				throw new ServiceException("对象["+model.getType().getName()+"]未找到属性["+attribute+"]");
			}
			String foreignKey=bean.getColumnLabel();
			ObjectModel attributeModel=model.getObjectModelsMap().get(foreignKey);
			bean=attributeModel.getFieldsMap().get(fieldName);
			tempModel=attributeModel;
		}
		else
		{
			throw new ServiceException("对象["+model.getType().getName()+"]未找到属性["+fieldName+"]");
		}
		if(bean==null)
		{
			throw new ServiceException("对象["+model.getType().getName()+"]未找到属性["+fieldName+"]");
		}
		bean.setAsTable(tempModel.getAsTable());
		return bean;
	}
}
