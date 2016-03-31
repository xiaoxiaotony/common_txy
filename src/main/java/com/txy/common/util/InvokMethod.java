package com.txy.common.util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.txy.common.bean.Model;
import com.txy.common.constant.Constant;
import com.txy.common.exception.ServiceException;
public class InvokMethod
{
	private static final Logger log = Logger.getLogger(InvokMethod.class);
	
	public static Object invok(Object obj, String methodName,Model model)
	{
		Class<? extends Object> cla = obj.getClass();
		Object returnObject = null;
		try
		{
			Method method = cla.getDeclaredMethod(methodName);
			returnObject = method.invoke(obj);			
		}
		catch(IllegalAccessException e)
		{
			log.error(e.getMessage());
			throw new ServiceException("java在反射时调用的方法所导致的异常");
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
			throw new ServiceException("参数："+Constant.METHOD+"="+methodName+"不正确");
		}
		catch(InvocationTargetException e)
		{
			Throwable throwable=e.getCause();
			if(throwable instanceof ServiceException)
			{
				model.getSystemModel().setErrorMsg(throwable.getMessage());
				throw new ServiceException(throwable.getMessage());
			}
			if(throwable instanceof DataAccessException)
			{
				log.error(obj.getClass().getName()+"数据操作异常",e);
				throw new ServiceException("数据操作异常");
			}
			if(throwable instanceof Exception)
			{
				log.error(obj.getClass().getName()+"未知异常",e);
				throw new ServiceException("输入参数错误,导致未知异常");
			}
		}
		return returnObject;
	}
	
	
}
