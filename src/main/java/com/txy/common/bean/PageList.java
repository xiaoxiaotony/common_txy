package com.txy.common.bean;

import java.util.List;

@SuppressWarnings({"serial" })
public class PageList<T> implements java.io.Serializable
{
	// 总记录数
	private int total;
	
	private List<Object> rows;
	
	private List<T> data;
	
	public PageList()
	{
	}
	
	public int getTotal()
	{
		return total;
	}
	
	public void setTotal(int total)
	{
		this.total = total;
	}

	
	public List<Object> getRows()
	{
		return rows;
	}

	
	public void setRows(List<Object> rows)
	{
		this.rows = rows;
	}

	public PageList(int total, List<T> data)
	{
		super();
		this.total = total;
		this.data = data;
	}
	
	public List<T> getData()
	{
		return data;
	}

	
	public void setData(List<T> data)
	{
		this.data = data;
	}
	
	
	
}
