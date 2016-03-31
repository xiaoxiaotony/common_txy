package com.txy.common.impl;

import java.util.HashMap;
import java.util.Map;

import com.txy.common.api.Box;
import com.txy.common.exception.ServiceException;

public class AbstractBox<T> implements Box<T>
{

    protected Map<String,T> dataMap=new HashMap<String,T>();
    @Override
    public T getValue(String key)
    {
        return this.dataMap.get(key);

    }

    @Override
    public T getValueNotEmpty(String key)
    {
        if(this.dataMap.get(key)==null)
        {
            throw new ServiceException("不能从盒子中获取null数据["+key+"]");
        }
        return this.dataMap.get(key);
    }

    @Override
    public void add(String key, T value)
    {
        this.dataMap.put(key, value);
    }

    @Override
    public void clear()
    {
        this.dataMap.clear();

    }

    @Override
    public <K> K convert(Class<K> cla, String key)
    {
        try
        {
            return cla.cast(this.dataMap.get(key));
        }
        catch(ClassCastException e)
        {
            throw new ServiceException(cla.getName()+"不能转换成"+this.dataMap.get(key).getClass().getName());
        }

    }

}
