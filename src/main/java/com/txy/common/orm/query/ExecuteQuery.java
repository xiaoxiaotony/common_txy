package com.txy.common.orm.query;

import java.util.List;
import java.util.Map;
/**
 * 操作数据库接口
 * @author fei
 */
public interface ExecuteQuery {
	/**
	 * 保存一条实体对象 传入对象为实体对象
	 * @param <T>
	 * @param t
	 * @param cascade:true级联保存对象，false：忽略关联对象
	 */
	public <T> int insert(T t,boolean cascade);
	
	/**
	 * 插入单条 默认忽略关联对象 忽略空值
	 * @param t
	 * @return
	 * @author hong
	 */
	public <T> int insert(T t);
	
	/**
	 * 根据ID查询已有的更新 默认忽略关联对象
	 * @param t
	 * @return
	 * @author hong
	 */
    public <T> int insertOrUpdate(T t);
	
	/**
	 * 批量保存对象
	 * @param <T>
	 * @param list
	 * @return
	 */
	public <T> int insert(List<T> list,boolean cascade);
	
	/**
	 * 根据多个条件更新表中的多个字段
	 * @param <T>
	 * @param t
	 * @param args  更新的数据
	 * @param conditionMap  更新的条件
	 */
	public <T> int update(T t, Map<String, Object> conditionMap);
	
	/**
	 * 根据多个条件删除表中的数据
	 * @param <T>
	 * @param cla	映射的javaBean
	 * @param conditionMap 传入条件
	 */
	public <T> int delete(Class<T> cla,Map<String, Object> conditionMap);
	
	/**
	 * 根据主键ID删除数据
	 * @param <T>
	 * @param cla
	 * @param id 主键ID的值
	 */
	public <T> int delete(Class<T> cla, Object value);
	/**
	 * 根据主键ID更新实体字段，如果实体字段为null则忽略更新
	 * @param <T>
	 * @param t
	 * @param value
	 */
	public <T> int update(T t);

	/**
	 * 插入数据
	 * @param string
	 * @param map
	 * @return
	 */
	public int insert(String table, Map<String, Object> map);

	/**
	 * 根据主键删除数据
	 * @param Table
	 * @param primkeyId
	 * @param value
	 * @return
	 */
	public int delete(String Table, String primaryKey, String value);

	/**
	 * 根据主键修改数据
	 * @param string
	 * @param map
	 * @param string2
	 * @param object
	 * @return
	 */
	public int update(String table, Map<String, Object> map, String primaryKey, Object value);

	
	/**
	 * 多条件删除
	 * @param tableName
	 * @param conditMap
	 * @return
	 */
	public int delete(String tableName, Map<String, Object> conditMap);

	/**
	 * 多条件修改
	 * @param tableName
	 * @param paramMap
	 * @param conditMap
	 * @return
	 */
	public int update(String tableName, Map<String, Object> paramMap, Map<String, Object> conditMap);
	

}

