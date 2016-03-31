package com.txy.common.orm.query;


/**
 * @author fei
 */
public interface BeanQuery<T> extends Query<T>
{
	/**
	 * 排序接口
	 */
	public BeanQuery<T> sortForDesc(String ...args);
	/**
	 * 视图查询接口
	 * @param fields
	 * @return
	 */
	public BeanQuery<T> selectFields(String ...fields);
	
	/**
     * 排序接口
     */
	public BeanQuery<T> sortForAsc(String ...args);
	/**
	 * 等于
	 * @param field
	 * @param value
	 * @return
	 */
	public BeanQuery<T> eq(String field,Object value);
	/**
	 * 大于
	 * @param field
	 * @param value
	 * @return
	 */
	public BeanQuery<T> gt(String field,Object value);
	/**
	 * 小于
	 * @param field
	 * @param value
	 * @return
	 */
	public BeanQuery<T> lt(String field,Object value);
	/**
	 * 设置Join操作
	 * @param isJoin
	 * @return
	 */
	public BeanQuery<T> setJoin(boolean isJoin);
	/**
	 * 设置关联的对象
	 * @param beans
	 * @return
	 */
	public BeanQuery<T> setJoinBean(String...beans);
	/**
	 * 添加查询参数=
	 * @param key
	 * @param value
	 * @return
	 */
	public BeanQuery<T> addQueryParameter(String key,Object value);
	/**
	 * 设为左连接查询
	 * @return
	 */
	public BeanQuery<T> setJoinLeftJoin();
	/**
	 * 设为右连接查询
	 * @return
	 */
	public BeanQuery<T> setJoinRightJoin();
	/**
	 * 查询总条数
	 * @return
	 */
	public int count();
	
	/**
	 * 模糊查询
	 * @param key
	 * @param value
	 * @return
	 */
	public BeanQuery<T> like(String field,Object value);
	
	/**
	 * 多个条件的模糊查询
	 * @param fields
	 * @param values
	 * @return
	 */
	public BeanQuery<T> orLike(String[] tableColums,String[] values);
	
	
	/**
	 * 通过数据组字段查询
	 * @param colums
	 * @param value
	 * @return
	 */
	public BeanQuery<T> eqByColum(String colums,Object value);
	
}
