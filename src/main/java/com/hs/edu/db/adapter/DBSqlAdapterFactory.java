package com.hs.edu.db.adapter;

import com.hs.edu.db.config.DBType;

import java.util.HashMap;

public class DBSqlAdapterFactory {

	public static HashMap adapterMap = new HashMap();
	//后面如果加其它数据库代码则需要重新写
	static {
		adapterMap.put(DBType.oracle.name(), new OracleSqlAdapter());
		adapterMap.put(DBType.mysql.name(), new MySqlAdapter());
	}
	public static IDBSqlAdapter getDBSqlAdapter(String dbType) {

		return (IDBSqlAdapter) adapterMap.get(dbType);
	}
}
