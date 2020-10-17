package com.hs.edu.db.core.parse;
/**
 * @Author taxuefan
 * @Description //保存SqlHolder的相关的信息
 * @Date 16:31 2020/10/10
 * @Param
 * @return
 **/
public class SqlHolder {
	private String sql;
	private SqlParam sqlParam;
	public SqlHolder(String sql, SqlParam sqlParam){
		this.sql=sql;
		this.sqlParam=sqlParam;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public SqlParam getSqlParam() {
		return sqlParam;
	}
	public void setSqlParam(SqlParam sqlParam) {
		this.sqlParam = sqlParam;
	}
	public String toString(){
		String logmsg="sql:"+this.sql+"\r\nparams:";
		for(int i=0;i<this.getSqlParam().getParamValues().length;i++){
			logmsg=logmsg+"\r\nparam["+i+"]"+getSqlParam().getParamValues()[i];
		}
		return logmsg;		
	}	
}
