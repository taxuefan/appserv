package com.hs.edu.db.core.parse;
import com.hs.edu.app.model.User;
import com.hs.edu.db.adapter.DBSqlAdapterFactory;
import com.hs.edu.db.adapter.IDBSqlAdapter;
import com.hs.edu.db.annotation.FieldAttr;
import com.hs.edu.db.annotation.TableAttr;
import com.hs.edu.db.exception.PrimayKeyNoFoundException;
import com.hs.edu.db.exception.QueryConstrainException;
import com.hs.edu.db.util.CommonUtil;
import com.hs.edu.db.util.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Slf4j
public class SqlBuilderUtil {
	/**
	 * @Author taxuefan
	 * @Description //生成插入的sql
	 * @Date 16:48 2020/10/5
	 * @Param [clazz, bean]
	 * @return com.hs.edu.db.core.parse.SqlHolder
	 **/
	public static <T> SqlHolder builderInsertSql(Class<T> clazz, T bean) {
		StringBuilder sb = new StringBuilder();
		String tableName =getTableName(clazz);
		String column = "";
		String values = "";
		SqlParam sqlParam=new SqlParam();
		sb.append("insert into " + tableName + "(");
		try {
			java.lang.reflect.Field[] fields = bean.getClass().getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				String field_name = field.getName();
		        //获取字段中包含fieldMeta的注解  
		    	FieldAttr meta = field.getAnnotation(FieldAttr.class);
		    	if(meta!=null){
		    		if(meta.fieldName()!=null&&!meta.fieldName().equals("")){
		    			field_name = meta.fieldName();
		    		}
					field.setAccessible(true);
					Object v = field.get(bean);
					if (v == null) {
						continue;
					}
					column += field_name + ",";
					values += "?,";
					sqlParam.addParamValue(v);
		    	}
			}
			column = column.substring(0, column.length() - 1);
			values = values.substring(0, values.length() - 1);
			sb.append(column + ")");
			sb.append(" values(" + values + ")");			
			SqlHolder sqlHolder =new SqlHolder(sb.toString(),sqlParam);
			log.debug(sqlHolder.toString());
			return sqlHolder;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @Author taxuefan
	 * @Description //生成删除Sql的类
	 * @Date 16:49 2020/10/5
	 * @Param [clazz, id]
	 * @return com.hs.edu.db.core.parse.SqlHolder
	 **/
	public static <T> SqlHolder builderDeleteSql(Class<T> clazz, Object id) {
		StringBuilder sb = new StringBuilder();
		String tableName =getTableName(clazz);
		sb.append("delete from " + tableName + " where ");
		SqlParam sqlParam=new SqlParam();
		boolean hasKey=false;
		try {
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				String field_name = field.getName();
		        //获取字段中包含fieldMeta的注解  
		    	FieldAttr meta = field.getAnnotation(FieldAttr.class);
		    	if(meta!=null &&meta.id()){//主键
		    		hasKey=true;
		    		if(meta.fieldName()!=null&&!meta.fieldName().equals("")){
		    			field_name = meta.fieldName();
		    		}
					field.setAccessible(true);				
					sb.append(field_name+"=?" );
					sqlParam.addParamValue(id);
		    	}
		    }
			//未定义主键
			if (!hasKey) {
				throw new PrimayKeyNoFoundException("删除对象主健不存在");
			}
			SqlHolder sqlHolder =new SqlHolder(sb.toString(),sqlParam);
			log.debug(sqlHolder.toString());
			return sqlHolder;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @Author taxuefan
	 * @Description //通过实体类，以及主健id自动生成查询的SqlHolder类
	 * @Date 16:54 2020/10/5
	 * @Param clazz 实体类
	 * @Param bean 主健ID
	 * @return com.hs.edu.db.core.parse.SqlHolder
	 **/
	public static <T> SqlHolder builderGetSql(Class<T> clazz, Object id) {
		StringBuilder sb = new StringBuilder();
		String tableName =getTableName(clazz);
		sb.append("select * from " + tableName + " where ");
		SqlParam sqlParam=new SqlParam();
		boolean hasKey=false;
		try {
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				String field_name = field.getName();
		        //获取字段中包含fieldMeta的注解  
		    	FieldAttr meta = field.getAnnotation(FieldAttr.class);
		    	if(meta!=null &&meta.id()){//主键
		    		hasKey=true;
		    		if(meta.fieldName()!=null&&!meta.fieldName().equals("")){
		    			field_name = meta.fieldName();
		    		}
					field.setAccessible(true);				
					sb.append(field_name+"=?" );
					sqlParam.addParamValue(id);
		    	}
			}
			//未定义主键
			if (!hasKey) {
				throw new PrimayKeyNoFoundException("主健不存在错误");
			}
			SqlHolder sqlHolder =new SqlHolder(sb.toString(),sqlParam);
			log.debug(sqlHolder.toString());
			return sqlHolder;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    /**
     * @Author taxuefan
     * @Description //通过实体类，以及主健id对象生成用于查询的SqlHolder类
     * @Date 16:54 2020/10/5
     * @Param clazz 实体类
	 * @Param bean 主健ID
	 * @return com.hs.edu.db.core.parse.SqlHolder
     **/
	public static <T> SqlHolder builderFindSql(Class<T> clazz, T bean) {
		StringBuilder sb = new StringBuilder();
		String tableName =getTableName(clazz);
		sb.append("select * from " + tableName + " where 1=1 ");
		SqlParam sqlParam=new SqlParam();
		boolean hasKey=false;
		try {
			java.lang.reflect.Field[] fields = bean.getClass().getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				String field_name = field.getName();
		        //获取字段中包含fieldMeta的注解  
		    	FieldAttr meta = field.getAnnotation(FieldAttr.class);
		    	if(meta!=null){//主键
		    		hasKey=true;
		    		if(meta.fieldName()!=null&&!meta.fieldName().equals("")){
		    			field_name = meta.fieldName();
		    		}
					field.setAccessible(true);
					Object v = field.get(bean);
					if (v == null) {
						continue;
					}
					sb.append(" and "+field_name+"=?" );
					sqlParam.addParamValue(v);
		    	}
			}
			//未定义主键
			if (!hasKey) {
				throw new PrimayKeyNoFoundException("查找对象不存在自动增长列");
			}
			SqlHolder sqlHolder =new SqlHolder(sb.toString(),sqlParam);
			log.debug(sqlHolder.toString());
			return sqlHolder;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
   /**
    * @Author taxuefan
	* @Description //通过实体类，以及主健id对象生成用于更新的SqlHolder类
    * @Date 17:05 2020/10/5
	* @Param clazz 实体类
	* @Param bean 主健ID
    * @return com.hs.edu.db.core.parse.SqlHolder
    **/
	public static <T> SqlHolder builderUpdateSql(Class<T> clazz, T bean) {
		StringBuilder sb = new StringBuilder();
		String tableName =getTableName(clazz);
		String setValue = "";
		String where = "";
		SqlParam sqlParam=new SqlParam();
		sb.append("update " + tableName);
		try {
			java.lang.reflect.Field[] fields = bean.getClass().getDeclaredFields();
			String key="";
			Object keyValue="";
			for (java.lang.reflect.Field field : fields) {
				String field_name = field.getName();
		        //获取字段中包含fieldMeta的注解  
		    	FieldAttr meta = field.getAnnotation(FieldAttr.class);
		    	if(meta!=null){
		    		if(meta.fieldName()!=null&&!meta.fieldName().equals("")){
		    			field_name = meta.fieldName();
		    		}
					field.setAccessible(true);
		    		if(meta.id()){
						Object v = field.get(bean);
		    			key=field_name;
		    			keyValue=v;
		    		}else{
						Object v = field.get(bean);
						if (v == null) {
							continue;
						}
						setValue += field_name + " = ? ,";
						sqlParam.addParamValue(v);
		    		}
		    	}
			}
			if (!"".equals(setValue)) {
				setValue = setValue.substring(0, setValue.length() - 1);
				setValue =setValue+" where "+key+"=? ";
				sqlParam.addParam(key, keyValue);
			}
			if (setValue.length() > 0) {
				setValue = " set " + setValue;
			} else {
				throw new Exception("修改字段不能全为空");
			}
			sb.append(setValue);
			sb.append(where);
			SqlHolder sqlHolder =new SqlHolder(sb.toString(),sqlParam);
			log.debug(sqlHolder.toString());
			return sqlHolder;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    /**
     * @Author taxuefan
     * @Description //通过查询语句，生成
     * @Date 17:31 2020/10/5
     * @Param [clazz, queryConstrain]
     * @return com.hs.edu.db.core.parse.SqlHolder
     **/
	public static <T> SqlHolder builderQuerySql(Class<T> clazz, QueryConstrain queryConstrain,String dbType) {
		String tableName =getTableName(clazz);
		String sql="select * from " + tableName;
		try {
			int index = 0;
			if (queryConstrain == null) {
				SqlParam sqlParam=new SqlParam();
               return new SqlHolder(sql,sqlParam);
			}
			if(queryConstrain.getQueryType()!=Constants.QUERY_BY_MAP){
				throw new QueryConstrainException("查询类型有误");
			}
		  return builderQuerySqlByMap(sql,clazz,queryConstrain,-1,-1,dbType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @Author taxuefan
	 * @Description //TODO 通过注册map的方式来获取SqlHolder参数
	 * @Date 19:27 2020/10/5
	 * @Param [clazz, queryConstrain]
	 * @return com.hs.edu.db.core.parse.SqlHolder
	 **/
	private static <T>SqlHolder builderQuerySqlByMap(String sql,Class<T> clazz, QueryConstrain queryConstrain,int pageSize, int pageNum,String dbType ){
		String where = "";
		String orderby = "";
		SqlParam sqlParam=new SqlParam();
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		Map params=queryConstrain.getQueryParam();
		Map paramsOderby=queryConstrain.getOrderbyParam();
		where= where+" where 1=1 ";
		Set set = params.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			Map.Entry entry=(Map.Entry)it.next();
			String key =(String)entry.getKey();
			Object[] oObject=(Object[])entry.getValue();
			Object value=(Object)oObject[0];
			String exp=(String)oObject[1];
			if(exp.equals("=")){
				where= where+" and "+ key+" =? ";
			}
			if(exp.equals("like")){
				where= where+" and "+ key+" like ? ";
			}
			if(exp.equals("in")){
				where= where+ " and "+key+" in (?)";
			}
			sqlParam.addParam(key, value);
		}
		set = paramsOderby.entrySet();
		it = set.iterator();
		while(it.hasNext()){
			Map.Entry entry=(Map.Entry)it.next();
			String key =(String)entry.getKey();
			String exp=(String)entry.getValue();
			if(exp.equals("asc")){
				orderby= orderby+" "+key+" asc,";
			}
			if(exp.equals("desc")){
				orderby= orderby+" "+key+" desc,";
			}
		}
		sb.append(where);
		if (!"".equals(orderby)) {
			orderby = "order by "+orderby.substring(0, orderby.length() - 1);
		}
		sb.append(orderby);
		if(pageSize>-1){
			sql =builderQueryPage(sb.toString(),pageSize,pageNum,dbType);
		}
		return new SqlHolder(sql,sqlParam);
	}
	private static <T>SqlHolder builderQuerySql(String sql,  QueryConstrain queryConstrain,int pageSize, int pageNum,String dbType){
		SqlParam sqlParam=new SqlParam();
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		try {
			if (queryConstrain == null||queryConstrain.getParamObj()==null) {
				return new SqlHolder(sql,sqlParam);
			}
			if(queryConstrain.getQueryType()!=Constants.QUERY_BY_OBJECT){
				throw new QueryConstrainException("查询类型有误");
			}
			Object queryObj=queryConstrain.getParamObj();
			Map map= CommonUtil.getObjectToMap(queryObj);
			sqlParam.setParamMap(map);
			String finalSql=sqlParam.prepareSql(sql);
			log.info("finalSql:"+finalSql);
			return  new SqlHolder(finalSql,sqlParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static <T> SqlHolder builderQuerySql(Class<T> clazz, QueryConstrain queryConstrain, int pageSize, int pageNum,String dbType) {
		String tableName =getTableName(clazz);
		String sql= "select * from " + tableName;
		try {
			if (queryConstrain == null) {
				SqlParam sqlParam=new SqlParam();
				return new SqlHolder(sql,sqlParam);
			}
			if(queryConstrain.getQueryType()!=Constants.QUERY_BY_MAP){
				throw new QueryConstrainException("查询类型有误");
			}
			return builderQuerySqlByMap(sql,clazz,queryConstrain,-1,-1,dbType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分页查询
	 * @param sql 查询的sql
	 * @param pageSize 每页显示条数
	 * @param pageNum 显示第几页
	 * @return
	 */
	public static String builderQueryPage(String sql, int pageSize, int pageNum,String sDBType){
		  String dbType="mysql";
		  if(sDBType!=null){
			  dbType=sDBType;
		  }
		  IDBSqlAdapter dbSqlAdapter = DBSqlAdapterFactory.getDBSqlAdapter(dbType);
		  return dbSqlAdapter.getPageSql(  sql,  pageNum,pageSize);

     }
	public static String builderQueryPageCount(String sql){
		  String selectfield = sql.substring(sql.indexOf("select"), sql.indexOf("from"));
		  String sqlCount = sql.replace(selectfield, " select count(1)  ");
		  if(sqlCount.indexOf("order by")>0){
		      sqlCount = sqlCount.substring(0,sqlCount.indexOf("order by"));
		  }
		  return sqlCount;
	}
	public static <T> SqlHolder builderCountSql(Class<T> clazz, QueryConstrain queryConstrain,String dbType) {
		StringBuilder sb = new StringBuilder();
		String tableName =getTableName(clazz);
		String where = "";
		SqlParam sqlParam=new SqlParam();
		sb.append("select count(1) count_ from " + tableName);
		try {
			int index = 0;
			if (queryConstrain != null) {
				Map params=queryConstrain.getQueryParam();
				where= where+" where 1=1 ";
				Set set = params.entrySet();         
				Iterator i = set.iterator();         
				while(i.hasNext()){      
				     Map.Entry entry=(Map.Entry)i.next();    
				     String key =(String)entry.getKey();
				     Object[] oObject=(Object[])entry.getValue();
				     Object value=(Object)oObject[0];
				     String exp=(String)oObject[1];
				     if(exp.equals("=")){
				    	 where= where+" and "+ key+" =? ";
				     }
				     if(exp.equals("like")){
				    	 where= where+" and "+ key+" like ? ";
				     }
				     if(exp.equals("in")){
				    	 where= where+ " and "+key+" in (?)";
				     }
				     sqlParam.addParam(key, value);
				}
				sb.append(where);
			}
			SqlHolder sqlHolder =new SqlHolder(sb.toString(),sqlParam);
			log.debug(sqlHolder.toString());
			return sqlHolder;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static <T> SqlHolder builderSequenceSql(Class<T> clazz) {
		StringBuilder sb = new StringBuilder();
		String tableName = getTableName(clazz);
		tableName=tableName.toUpperCase();
		SqlParam sqlParam=new SqlParam();		
		sb.append("select nextval('"+tableName+"') ");
	    SqlHolder sqlHolder =new SqlHolder(sb.toString(),sqlParam);
		log.debug(sqlHolder.toString());
		return sqlHolder;
	}
	private  static <T> String getTableName(Class<T> clazz){
		String tableName = clazz.getSimpleName().toLowerCase();
		TableAttr meta1 = clazz.getAnnotation(TableAttr.class);
		if(meta1!=null&&!meta1.tableName().equals("")){
			tableName=meta1.tableName();
		}
		return tableName;
	}
    public  static void main(String args[]){
		QueryConstrain queryConstrain=new QueryConstrain();
		User user =new User();
		user.setPassword("1231230");
		user.setUserName("huge");
		queryConstrain.setParamObj(user);
		String sql="insert into user (user_name,password) values(:userName,:password)";
		SqlHolder holder=SqlBuilderUtil.builderQuerySql(sql,queryConstrain,-1,-1,null);
        System.out.println(holder.getSql());
        List list=holder.getSqlParam().getParamValuesList();
        for(Object obj:list){
        	System.out.println(obj.toString());
		}

	}
}
