package com.txy.common.exception;
/**
 * 系统异常
 * @author fei
 */
public class SystemException extends Exception
{
	private static final long serialVersionUID = -4075104293760648322L;
	public SystemException(String msg,Exception e)
	{
		super(msg,e);
		e.printStackTrace();
	}
	public SystemException(Exception e)
	{
		super(e);
	}
	public SystemException(String msg)
	{
		super(msg);
	}
}
