package com.txy.common.format;
/**
 * 数据格式转换接口。该接口主要实现数据格式的转换
 * @author fei
 */
public interface DataFormat
{
	//数据格式化
	public  String format(Object data,String dataType);
	//异常数据格式化
	public String errorMsg(String msg,String dataType);
}
