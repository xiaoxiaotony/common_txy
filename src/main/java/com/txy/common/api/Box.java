package com.txy.common.api;


public interface Box<T>
{
	/**
	 * 从盒子里面取出数据，该数据可以为null
	 * @param key
	 * @return
	 */
	public T getValue(String key);
	/**
	 * 从盒子里面取出数据，该数据不能为null
	 * @param key
	 * @return
	 */
	public T getValueNotEmpty(String key);
	
	/**
	 * 向盒子添加数据
	 * @param key
	 * @return
	 */
	public void add(String key,T value);
	/**
	 * 清空盒子数据
	 */
	public void clear();
	
	public <K> K convert(Class<K> cla,String key);
}
