package com.txy.common.bean;

import java.util.Date;

@SuppressWarnings("serial")
public class SystemModel implements java.io.Serializable
{
	private String method;						//请求方法名称
	private String handler;						//处理器名称
	private String authKey;						//授权KEY
	private long timestamp;						//时间戳
	private String url;							//连接到数据库的URL
	private String apiType;						//API协议类型
	private String requestData;					//请求参数
	private String responseData;				//响应数据
	private String requestMethod;				//请求方法
	private String requestIp;					//请求IP
	private String authId;						//授权ID
	private String serviceId;					//SERVICEID
	private String errorMsg;
	private String dataType;					//返回数据类型
	private int size;							//返回数据大小
	private String contentType;					//响应类型
	private Date startDate=new Date();						//请求开始时间
	private String contextPath;
	private String realPath;
	public String getRealPath()
	{
		return realPath;
	}
	public void setRealPath(String realPath)
	{
		this.realPath = realPath;
	}
	public String getContextPath()
	{
		return contextPath;
	}
	public void setContextPath(String contextPath)
	{
		this.contextPath = contextPath;
	}
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
	public String getDataType()
	{
		return dataType;
	}
	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}
	public String getErrorMsg()
	{
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}
	public String getAuthId()
	{
		return authId;
	}
	public void setAuthId(String authId)
	{
		this.authId = authId;
	}
	public String getServiceId()
	{
		return serviceId;
	}
	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}
	public String getRequestIp()
	{
		return requestIp;
	}
	public void setRequestIp(String requestIp)
	{
		this.requestIp = requestIp;
	}
	public String getRequestMethod()
	{
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod)
	{
		this.requestMethod = requestMethod;
	}
	public String getQueryString()
	{
		return queryString;
	}
	public void setQueryString(String queryString)
	{
		this.queryString = queryString;
	}
	private String queryString;					//查询参数字符串
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method = method;
	}
	public String getHandler()
	{
		return handler;
	}
	public void setHandler(String handler)
	{
		this.handler = handler;
	}
	public String getAuthKey()
	{
		return authKey;
	}
	public void setAuthKey(String authKey)
	{
		this.authKey = authKey;
	}
	public long getTimestamp()
	{
		return timestamp;
	}
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getApiType()
	{
		return apiType;
	}
	public void setApiType(String apiType)
	{
		this.apiType = apiType;
	}
	public String getRequestData()
	{
		return requestData;
	}
	public void setRequestData(String requestData)
	{
		this.requestData = requestData;
	}
	public String getResponseData()
	{
		return responseData;
	}
	public void setResponseData(String responseData)
	{
		this.responseData = responseData;
	}
	public String getContentType()
	{
		return contentType;
	}
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}
}
