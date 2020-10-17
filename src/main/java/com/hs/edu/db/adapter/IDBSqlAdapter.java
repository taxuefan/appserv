package com.hs.edu.db.adapter;
 /**
  * @Author taxuefan
  * @Description //TODO
  * @Date 11:34 2020/9/30
  * @Param
  * @return
  **/
public interface IDBSqlAdapter {

	public String getPageSql(String sSql,int curPage,int pageNum);

	public String getPrimaryKeysSql(String sTableName);
	
	public  String getTotalCountSql(String sSql);
	
}
