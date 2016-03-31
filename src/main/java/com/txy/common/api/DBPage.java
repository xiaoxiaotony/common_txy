package com.txy.common.api;

public interface DBPage
{
	//问号参数调用
	public String pageForParams(String sql);
	//参数命名方式调用
	public String pageForName(String sql);
	//获取总记录数
	public String pageForCount(String sql);
}
