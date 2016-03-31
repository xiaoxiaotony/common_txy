package com.txy.common.bean;

import java.lang.reflect.Field;

import com.txy.common.util.StringUtils;

public class FieldInfoBean
{
	private Field field;
	private String columnLabel;
	private boolean isId;//是否为主键
	private boolean isForeignKey;//是否为外键
	private String asColumnLabel;
	private String asTable;
	private boolean nullable;
	public FieldInfoBean()
	{
		this.length=-1;
	}
	public boolean isNullable()
	{
		return nullable;
	}
	public void setNullable(boolean nullable)
	{
		this.nullable = nullable;
	}
	private int length;
	public int getLength()
	{
		return length;
	}
	public void setLength(int length)
	{
		this.length = length;
	}
	public String getAsTable()
	{
		return asTable;
	}
	public void setAsTable(String asTable)
	{
		this.asTable = asTable;
	}
	public synchronized String getAsColumnLabel()
	{
		return asColumnLabel;
	}
	public Field getField()
	{
		return field;
	}
	public void setField(Field field)
	{
		this.field = field;
	}
	public String getColumnLabel()
	{
		return columnLabel;
	}
	public synchronized void setColumnLabel(String columnLabel)
	{
		this.asColumnLabel=columnLabel+"_"+StringUtils.getRandom();
		this.columnLabel = columnLabel;
	}
	public boolean isId()
	{
		return isId;
	}
	public void setId(boolean isId)
	{
		this.isId = isId;
	}
	public boolean isForeignKey()
	{
		return isForeignKey;
	}
	public void setForeignKey(boolean isForeignKey)
	{
		this.isForeignKey = isForeignKey;
	}
	
}
