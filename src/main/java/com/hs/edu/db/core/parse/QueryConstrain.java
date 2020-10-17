package com.hs.edu.db.core.parse;

import com.hs.edu.db.util.Constants;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class QueryConstrain {
	/**
	 * and查询条件
	 */
	private Map queryParam;
	/**
	 * 排序条件
	 */
	private Map orderbyParam;
	/**
	 * or查询条件
	 */
	private Map orParam;

	private Object paramObj;

	public int getQueryType() {
		return queryType;
	}

	private int queryType=1;

	private QueryConstrain(Object obj){
		this.paramObj=obj;
		this.queryType=Constants.QUERY_BY_OBJECT;
	}

	public QueryConstrain(){
		queryParam=new HashMap();
		orderbyParam=new HashMap();
		orParam=new HashMap();
		this.queryType= Constants.QUERY_BY_MAP;
	}
	public void addQueryParam(String key,Object value){
		if(value==null||value.equals(""))return;
		Object [] o=new Object[3];
		o[0]=value;
		o[1]="=";
		queryParam.put(key, o);
	}
	public void addQueryLikeParam(String key,Object value){
		if(value==null||value.equals(""))return;
		if(!value.toString().startsWith("%")){
			value="%"+value+"%";
		}
		Object [] o=new Object[3];
		o[0]=value;
		o[1]="like";
		queryParam.put(key, o);
		
	}
	
	public void addQueryInParam(String key,Object value){
		if(value==null||value.equals(""))return;
		Object [] o=new Object[3];
		o[0]=value;
		o[1]="in";
		queryParam.put(key, o);
		
	}
	public Object getParamObj() {
		return paramObj;
	}
	public void setParamObj(Object paramObj) {
		this.paramObj = paramObj;
		this.queryType=Constants.QUERY_BY_OBJECT;
	}
	public void addQueryOrParam(String key, String[] aObject){
		Object [] o=new Object[3];
		o[0]=aObject;
		o[1]="or";
		orParam.put(key, o);
	}
	public void addOrderby(Object orderby){
		if(orderby==null||orderby.equals(""))return;
		String s=(String)orderby;
		String[] a=s.split(":");
		String key=s;
		String type="desc";
		if(a.length>1){
			key=a[0];
			type=a[1];
		}
		orderbyParam.put(key, type);
	}
	public void addOrderbyAsc(Object key){
		if(key==null||key.equals(""))return;
		orderbyParam.put(key, "asc");
	}
	public void addOrderbyDesc(Object key){
		if(key==null||key.equals(""))return;
		orderbyParam.put(key, "desc");
	}
	public String debug(){
        return "";
	}
	public Map getOrderbyParam() {
		return orderbyParam;
	}

	public void setOrderbyParam(Map orderbyParam) {
		this.orderbyParam = orderbyParam;
	}

	public Map getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(Map queryParam) {
		this.queryParam = queryParam;
	}

}
