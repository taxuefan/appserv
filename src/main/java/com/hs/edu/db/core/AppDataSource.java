package com.hs.edu.db.core;

import javax.sql.DataSource;

/*
 * @Author 数据源
 * @Description //TODO 
 * @Date 12:02 2020/9/30
 * @Param 
 * @return 
 **/ 
public interface AppDataSource {
    /**
     * @Author
     * @Description //获取数据库类型
     * @Date 15:59 2020/10/10
     * @Param []
     * @return java.lang.String
     **/
    public  String getDBType();
    /**
     * @Author 获取数据源
     * @Description //TODO
     * @Date 16:00 2020/10/10
     * @Param []
     * @return javax.sql.DataSource
     **/
    public  DataSource getDataSource();
    /**
     * @Author taxuefan
     * @Description //
     * @Date 16:00 2020/10/10
     * @Param []
     * @return java.lang.String
     **/
    public  String getName();
}