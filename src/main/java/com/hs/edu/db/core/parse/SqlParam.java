package com.hs.edu.db.core.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlParam {

    private Map<Object,Object> paramMap;//参数信息
    private List<Object> paramValuesList;//参数信息
    private Object paramObj;
    private int index = 0;
    /**
     * 参数时间类型
     */
    public static final String DATE_TYPE = "Date";
    /**
     * 参数时间TimeStamp类型
     */
    public static final String TIMESTAMP_TYPE = "Timestamp";
    /**
     * 整数类型
     */
    public static final String INT_TYPE = "int";

    /**
     * 长整型
     */
    public static final String LONG_TYPE = "long";
    /**
     * 浮点数类型
     */
    public static final String DOUBLE_TYPE = "double";
    /**
     * 字符串类型
     */
    public static final String STRING_TYPE = "String";
    /**
     * StringBuffer类型
     */
    public static final String STRING_BUFFER_TYPE = "java.lang.StringBuffer";
    /**
     * ORACLE中的blob类型
     */
    public static final String BLOB = "blob";
    /**
     * 游标类型,只能作为参数值，同时参数类型必须是下面的OutParameter（输出参数类型）
     */
    public static final String CURSOR_TYPE = "Cursor";
    /**
     * 输出参数类型
     */
    public static final String OUT_TYPE = "OutParameter";

    public SqlParam() {
        paramMap = new HashMap<>();
        paramValuesList=new java.util.ArrayList<>();
    }
    public void clearParam() {
        paramMap.clear();
    }
    /**
     * 获取参数及值的组合数组
     *
     * @return Object[]  每一个[x]都是一个一维数组,[n][0]:表示具体的值对象，[n][1]表示类型
     */
    public Object[] getParamValues() {
       return  paramValuesList.toArray();

    }
    public void addParamValue(Object value) {
        paramValuesList.add(value);
    }
    /**
     * @return boolean
     * @Author taxuefan
     * @Description //TODO
     * @Date 17:20 2020/9/30
     * @Param [name, value]
     **/
    public boolean addParam(Object name, Object value) {
        try {
            if (name == null) {
                return false;
            }
            paramMap.put(name, value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 分析自定义的sql语法，参数  使用“:参数名”的方式，这样添加参数的时候可以按名添加,
     * 它一定和方法addParam配合使用
     * @param sql String
     * @return String
     */
    public String prepareSql(String sql) {
        List paramIndexList = new java.util.ArrayList();
        try {
            if (sql == null) {
                return null;
            }
            //clearParam();
            int dead_num = 0;
            int index = 0;
            int end_index = 0;
            int start_index = sql.indexOf(":");
            int dead_index = sql.indexOf("'"); //看看是否存在该死的单引号
            while (start_index >= 0 && start_index < sql.length()) {
                start_index++;
                end_index = getNextPostion(sql, start_index);
                //--------------------处理单引号--------------------------
                if (dead_index >= 0) { //太不幸了，居然有单引号
                    String temp = sql.substring(0, start_index);
                    int temp_index = temp.indexOf("'");
                    dead_num = 0;
                    while (temp_index >= 0 && temp_index < start_index) {
                        dead_num++;
                        temp_index = temp.indexOf("'", temp_index + 1);
                    }
                }
                //----------------------------------------------------------------------
                if (dead_num % 2 == 0) { //只有是配对的'才说明当前参数是有效的
                    String sParam = sql.substring(start_index, end_index);
                    paramIndexList.add(index, sParam);
                    index++;
                }             
                start_index = sql.indexOf(":", end_index);
            }
            //把参数值放在参数值中，并把生成sql语名生成问号
            start_index = 0;
            StringBuffer sbSql = new StringBuffer(sql);
            for (int i = 0; i < paramIndexList.size(); i++) {
                String sParam = (String) paramIndexList.get(i);//获取参数名
                String sParaName = ":" +sParam;//:+参数名
                start_index = sbSql.indexOf(sParaName);
                end_index = sParaName.length() + start_index;
                sbSql.replace(start_index, end_index, "?");//把参数名替成？
                paramValuesList.add(paramMap.get(sParam));
            }
            return new String(sbSql);
        } catch (Exception ex) {
            return sql;
        }
    }

    private int getNextPostion(String s, int start_index) {
        try {
            if (s == null) {
                start_index++;
            }
            for (int i = start_index; i < s.length(); i++) {
                if (s.charAt(i) == '\r') {
                    return i;
                }
                if (s.charAt(i) == '\n') {
                    return i;
                }
                if (s.charAt(i) == ' ') {
                    return i;
                }
                if (s.charAt(i) == '|') {
                    return i;
                }
                if (s.charAt(i) == '"') {
                    return i;
                }
                if (s.charAt(i) == ',') {
                    return i;
                }
                if (s.charAt(i) == ')') {
                    return i;
                }
                if (s.charAt(i) == ';') {
                    return i;
                }
                if (s.charAt(i) == '+') {
                    return i;
                }
                if (s.charAt(i) == '-') {
                    return i;
                }
                if (s.charAt(i) == '/') {
                    return i;
                }
                if (s.charAt(i) == '*') {
                    return i;
                }
                if (s.charAt(i) == 39) {
                    return i;
                }
            }
            return s.length();
        } catch (Exception ex) {
            return start_index++;
        }
    }

    public Map<Object, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<Object, Object> paramMap) {
        this.paramMap = paramMap;
    }
    public List<Object> getParamValuesList() {
        return paramValuesList;
    }
    public void setParamValuesList(List<Object> paramValuesList) {
        this.paramValuesList = paramValuesList;
    }
    public static void main(String[] args){
        SqlParam sqlParam = new SqlParam();
        String sql = "update about set title=:title where id=:id and title=:title";
        sqlParam.addParam("title", "eoruwoeiruqw");
        sqlParam.addParam("num", "2");
        sqlParam.addParam("id", "1");
        String sSql = sqlParam.prepareSql(sql);
        System.out.println(sSql);
        for (Object x : sqlParam.getParamValues()) {
            System.out.println(x);
        }
    }
}
