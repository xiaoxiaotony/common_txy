package com.txy.common.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * java bean代理对象
 * @author fei
 * 
 */
public class BeanProxy implements MethodInterceptor
{

	private Object target;
	

	/**
	 * 创建代理对象
	 * 
	 * @param target
	 * @return
	 */
	public Object getInstance(Object target)
	{
		this.target = target;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.target.getClass());
		// 回调方法
		enhancer.setCallback(this);
		// 创建代理对象
		return enhancer.create();
	}

	// 回调方法
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable
	{
		return proxy.invokeSuper(obj, args);
	}

	public Field getGetMethodField(String getMethodName, Class<?> cla)
	{
		String tag = getMethodName.substring(0, 3);
		String fieldName = null;
		Field field = null;
		if ("get".equals(tag))
		{
			fieldName = getMethodName.substring(3);
			fieldName = fieldName.substring(0, 1).toLowerCase()
					+ fieldName.substring(1);
			try
			{
				field = cla.getDeclaredField(fieldName);
			} catch (SecurityException e)
			{
			} catch (NoSuchFieldException e)
			{
			}
		}
		return field;
	}
}
