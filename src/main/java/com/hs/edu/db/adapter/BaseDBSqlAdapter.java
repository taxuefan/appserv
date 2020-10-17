package com.hs.edu.db.adapter;

public abstract class BaseDBSqlAdapter implements IDBSqlAdapter {
	public abstract String getPageSql(String sSql, int curPage, int pageNum);

	public abstract String getPrimaryKeysSql(String sTableName);
     /**
      * @Author taxuefan
      * @Description //获取Mysql的统计数据
      * @Date 11:34 2020/9/30
      * @Param [sSql]
      * @return java.lang.String
      **/
	public String getTotalCountSql(String sSql) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from ( ").append(sSql).append(" ) temp ");
		return sb.toString();   
	}
   
}

