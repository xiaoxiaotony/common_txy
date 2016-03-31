package com.txy.common.bean;

@SuppressWarnings("serial")
public class ExceptionInfoBean implements java.io.Serializable
{
	private boolean success;
	private String msg;//异常消息
	private String handler;//处理器名称
	private String method;//处理器方法
	public String getHandler()
	{
		return handler;
	}
	public void setHandler(String handler)
	{
		this.handler = handler;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method = method;
	}
	public boolean isSuccess()
	{
		return success;
	}
	public void setSuccess(boolean success)
	{
		this.success = success;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
