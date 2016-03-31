package com.txy.common.bean;
@SuppressWarnings("serial")
public class BaseBean implements java.io.Serializable
{
	
	private Object data;
	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}
	
	private boolean success;

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}
}
