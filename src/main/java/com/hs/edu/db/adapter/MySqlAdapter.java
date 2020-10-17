package com.hs.edu.db.adapter;

public class MySqlAdapter extends BaseDBSqlAdapter {

	public String getPageSql(String sSql, int curPage, int pageNum) {
		if (curPage == 0) {
			curPage = 1;
		}
		int beginIndex = (curPage - 1) * pageNum;
		StringBuffer sbReturn = new StringBuffer();
		sbReturn.append(sSql).append(" limit ").append(beginIndex).append(",")
				.append(pageNum);
		return sbReturn.toString();
	} 
	public String getPrimaryKeysSql(String sTableName) {
		return "";
	}
}
