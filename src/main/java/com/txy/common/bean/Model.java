package com.txy.common.bean;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Model extends TreeMap<String, Object>
{
    private static final long serialVersionUID = 4006793989034895261L;
    private HttpServletRequest request;
    private SystemModel systemModel;
    private HttpServletResponse response;
    private ViewModel viewModel;

    public ViewModel getViewModel()
    {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

    public void setResponse(HttpServletResponse response)
    {
        this.response = response;
    }

    public SystemModel getSystemModel()
    {
        return systemModel;
    }
    
    public Model(){
    	
    }

    public void setSystemModel(SystemModel systemModel)
    {
        this.systemModel = systemModel;
    }

    public Model(Map<String, Object> argsMap)
    {
        this.putAll(argsMap);
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public void setRequest(HttpServletRequest request)
    {
        this.request = request;
    }
    
    public void clear()
    {
        super.clear();
        this.request = null;
        this.response=null;
        this.systemModel = null;
    }
    public void clearNotMap()
    {
        this.request = null;
        this.response=null;
        this.systemModel = null;
    }    
}
