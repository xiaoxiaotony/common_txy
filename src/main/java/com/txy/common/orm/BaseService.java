package com.txy.common.orm;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础服务services
 * 
 * @author fei
 */
public abstract class BaseService
{
	
	@Autowired
	private SqlBeanClient queryClient;
	
	/**
	 * 保存单条数据
	 * 
	 * @param tableName
	 * @param map
	 * @return
	 */
	public int insert(String tableName, Map<String, Object> map)
	{
		return this.queryClient.createExecuteQuery().insert(tableName, map);
	}
	
	/**
	 * 多条件修改
	 * 
	 * @param tableName
	 * @param paramMap
	 * @param conditMap
	 * @return
	 */
	public int update(String tableName, Map<String, Object> paramMap, Map<String, Object> conditMap)
	{
		return this.queryClient.createExecuteQuery().update(tableName, paramMap, conditMap);
	}
	
	/**
	 * 单条件修改
	 * 
	 * @param tableName
	 * @param paramMap
	 * @param conditMap
	 * @return
	 */
	public int update(String tableName, Map<String, Object> paramMap, String primaryKey, String value)
	{
		return this.queryClient.createExecuteQuery().update(tableName, paramMap, primaryKey, value);
	}
	
	/**
	 * 单条件删除某条数据
	 * 
	 * @param tableName
	 * @param primaryKey
	 * @param value
	 * @return
	 */
	public int delete(String tableName, String primaryKey, String value)
	{
		return this.queryClient.createExecuteQuery().delete(tableName, primaryKey, value);
	}
	
	/**
	 * 多条件删除数据
	 * 
	 * @param tableName
	 * @param primaryKey
	 * @param value
	 * @return
	 */
	public int delete(String tableName, Map<String, Object> conditMap)
	{
		return this.queryClient.createExecuteQuery().delete(tableName, conditMap);
	}
	
	/**
	 * 查询Id单条数据
	 * 
	 * @param tableName
	 * @param primaryKey
	 * @param value
	 * @return
	 */
	public Map<String, Object> getOneRecordByPrimkeyId(String tableName, String primaryKey, String value)
	{
		StringBuffer sb = new StringBuffer("select * from ");
		sb.append(tableName);
		sb.append(" where 1 = 1 and " + primaryKey);
		sb.append(" = " + value);
		return this.queryClient.createJdbcTemplate().queryForMap(sb.toString().toLowerCase());
	}
	
}
