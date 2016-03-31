package com.txy.common.util;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;

import com.txy.common.exception.ServiceException;
public class AnnotationUtil {

	public static final String FIELDVALUE = "fieldValue";

	public static final String FOREIGNKEY = "foreignKey";

	public static String getTableName(Class<? extends Object> cla) {
		boolean isFind = cla.isAnnotationPresent(Table.class);
		if (isFind) {
			Table table = cla.getAnnotation(Table.class);
			return table.name();
		}
		return cla.getName();
	}

	public static String getColumnName(Field field) {
		boolean isFind = field.isAnnotationPresent(Column.class);
		if (isFind) {
			Column column = field.getAnnotation(Column.class);
			return column.name();
		}
		return field.getName();
	}

	public static Field getField(Class<? extends Object> cla, String fieldName) {
		try {
			Field field = cla.getDeclaredField(fieldName);
			return field;
		} catch (Exception e) {
			throw new ServiceException("字段:" + fieldName + "不存在");
		}
	}

	public static <T> List<Object> addOutParam(T t, Field[] fields) {
		List<Object> list = new ArrayList<Object>();
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].isAnnotationPresent(Transient.class) || fields[j].isAnnotationPresent(ManyToOne.class)) {
				continue;
			}
			try {
				list.add(BeanUtils.getProperty(t, fields[j].getName()));
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static <T> boolean isFieldObjectNull(T t, String columnName) {
		String getMethodName = "get" + columnName.substring(0, 1).toUpperCase()
				+ columnName.substring(1, columnName.length());
		Class<? extends Object> cla = t.getClass();
		Method method;
		try {
			method = cla.getMethod(getMethodName);
			Object obj = method.invoke(t);
			if (obj == null) {
				return true;
			}
		} catch (NoSuchMethodException e) {
			throw new ServiceException("缺少" + columnName + "的get方法");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static <T> Map<String, Object> getInnerListParamts(T t, String columnName) {
		String getMethodName = "get" + columnName.substring(0, 1).toUpperCase()
				+ columnName.substring(1, columnName.length());
		Class<? extends Object> cla = t.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		Method method;
		try {
			method = cla.getMethod(getMethodName);
			Object obj = method.invoke(t);
			Class<? extends Object> c = obj.getClass();
			Field[] fields = c.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Object fieldValue = BeanUtils.getProperty(obj, fields[i].getName());
				if (fields[i].isAnnotationPresent(Transient.class)) {
					continue;
				}
				if (fieldValue == null) {
					throw new ServiceException("保存对象" + cla.getName() + "的主键不能为空");
				}
				if (fields[i].isAnnotationPresent(Id.class)) {
					map.put(FOREIGNKEY, fieldValue);
				}
				list.add(fieldValue);
			}
		} catch (NoSuchMethodException e) {
			throw new ServiceException("缺少" + columnName + "的get方法");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("反射调用方法异常[" + getMethodName + "]");
		}
		map.put(FIELDVALUE, list);
		return map;
	}
}
