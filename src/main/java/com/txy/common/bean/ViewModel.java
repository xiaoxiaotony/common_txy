package com.txy.common.bean;

import java.util.HashMap;
import java.util.Map;


public class ViewModel
{
	private String page;
	private String model="forward";//跳转模式
	private  boolean isDefault;//是否采用默认登录
	public boolean isDefault()
	{
		return isDefault;
	}
	public void setDefault(boolean isDefault)
	{
		this.isDefault = isDefault;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	private Map<String,String> jsMap=new HashMap<String,String>();
	public ViewModel()
	{
		
	}
	public ViewModel(String page,String js)
	{
		this.page=page;
		this.jsMap.put(js,js);
	}
	public ViewModel(String page)
	{
		this.page=page;
	}
	public String getPage()
	{
		return page;
	}
	public void setPage(String page)
	{
		this.page = page;
	}
	public Map<String, String> getJsMap()
	{
		return jsMap;
	}
	public void setJsMap(Map<String, String> jsMap)
	{
		this.jsMap = jsMap;
	}
	
}
