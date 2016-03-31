package com.txy.common.exception;
@SuppressWarnings("serial")
public class ServiceException extends RuntimeException
{
	public ServiceException(int code)
	{
		super(code+"");
	}
	public ServiceException(String msg,Exception e)
	{
		super(msg,e);
		e.printStackTrace();
	}
	
	public ServiceException(Exception e)
	{
		super(e);
	}
	public ServiceException(String msg)
	{
		super(msg);
	}
}
