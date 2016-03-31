package com.txy.common.orm.query;

import java.util.List;

import com.txy.common.bean.PageList;

public interface Query<T> {
	/**
	 * 将查询结果以List返回
	 * 
	 * @return
	 */
	public List<T> list();
	/**
	 * 分页查询
	 * 
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<T> page(int start, int pageSize);
	
	/**
	 * 分页查询
	 * 用于子查询。自己传入统计总条数的sql
	 * @param start
	 * @param pageSize
	 * @param sql_count 
	 * @return
	 */
	public PageList<T> page(int start, int pageSize,String sql_count);
	/***
	 * 查询单条记录，如果查询结果大于1则抛出异常
	 * 
	 * @return
	 */
	public T uniqueResult();
	/**
	 * 查询指定数量的List
	 * 
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public List<T> list(int start, int pageSize);
	
	/**
	 * 查询总条数
	 * @return
	 */
	public int count();

}
