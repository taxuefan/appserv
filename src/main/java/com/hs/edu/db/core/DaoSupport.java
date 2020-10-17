package com.hs.edu.db.core;

import com.hs.edu.db.core.parse.QueryConstrain;

import java.util.List;

public interface DaoSupport<T> {
	/**
	 * 增加一个实体
	 * @param bean
	 */
	int add(T bean) throws Exception;

	/**删除一个实体
	 * @param id
	 */
	public int delete(Object id) throws Exception;

	/**
	 * 更新一个实体
	 * @param bean
	 */
	public int update(T bean) throws Exception;
	
	/**得到系序号
	 * @return
	 */
	public String getSequence() throws Exception;
	/**
	 * 查询所有实体
	 * @return
	 */
	public List<T> query() throws Exception;
	

	/**
	 * 查询实体
	 * @param rownum 记录数
	 * @return
	 */
	public List<T> query(int rownum)throws Exception ;
	
	/**
	 * 查询实体
	 * @param queryConstrain 查询条件
	 * @return
	 */
	public List<T> query(QueryConstrain queryConstrain) throws Exception;
	
	/**
	 * 统计实体条数
	 * @param queryConstrain
	 * @return
	 */
	public Long count(QueryConstrain queryConstrain) throws Exception ;

	
	/**
	 * 分页查询
	 * @param pageSize 每页显示记录数
	 * @param pageNum 页码
	 * @return
	 */
	public PageHolder<T> query(int pageSize, int pageNum)throws Exception ;
	
	/**
	 * 查询指定条件的记录
	 * @param queryConstrain
	 * @param rownum
	 * @return
	 */
	public List<T> query(QueryConstrain queryConstrain,int rownum) throws Exception;

	/**
	 * 分页查询
	 * @param queryConstrain 查询条件
	 * @param pageSize 每页显示记录数
	 * @param pageNum 页码
	 * @return
	 */
	public PageHolder<T> query(QueryConstrain queryConstrain,int pageSize,int pageNum) throws Exception;
	
	/**
	 * 按id取出实体
	 * @param id
	 * @return
	 */
	public T get(Object id) throws Exception;
	
	/**
	 * 查找实体
	 * @param bean
	 * @return
	 */
	public T find(T bean) throws Exception ;
	
	/**
	 * 执行sql语句
	 * @param sql sql
	 * @param aParam 参数
	 */
	public void executeUpdate(String sql,Object[] aParam) throws Exception;
	/**
	 * 执行sql查询
	 * @param <T>
	 * @param sql sql
	 * @param aParam 参数
	 * @param clazz1 对像类型
	 * @return
	 */
	public <T> List<T>  executeQuery(String sql,Object[] aParam,Class<T>  clazz1) throws Exception;
	/**
	 * 执行分页的sql查询
	 * @param <T>
	 * @param sql sql
	 * @param pageSize 每页显示条数
	 * @param pageNum 页数
	 * @param aParam 参数
	 * @param clazz1 对像类型
	 * @return
	 */
	public <T> PageHolder<T> executeQuery(String sql,int pageSize,int pageNum,Object[] aParam,Class<T>  clazz1) throws Exception;

	public AppDataSource getAppDataSource() throws Exception;
	
}
