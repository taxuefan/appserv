package com.hs.edu.db.adapter;

public class OracleSqlAdapter extends BaseDBSqlAdapter {

	public String getPageSql(String sSql, int curPage, int pageNum) {
		if (curPage == 0) {
			curPage = 1;
		}
		int offset = (curPage - 1) * curPage;
		int limit = pageNum;//
		sSql = "select * from (select rownum r, temp.* from (" + sSql
				+ ") temp"
				+ (limit == 0 ? "" : " where rownum<" + (offset + limit))
				+ ") temp1 where temp1.r>=" + offset;
		return sSql;
	}

	public String getPrimaryKeysSql(String sTableName) {
		String sSql = "select b.column_name from user_constraints a,user_ind_columns b  where a.constraint_type='P' and b.index_name = a.index_name and a.table_name='"
				+ sTableName + "' order by column_position";
		return sSql;
	}
}
