package com.hs.edu.db.core;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.hs.edu.db.config.DataSourceConfig;
import com.hs.edu.db.config.DefaultDataSourceConfig;

import javax.sql.DataSource;
import java.util.Properties;
/*
 * @Author 数据源
 * @Description //TODO 
 * @Date 12:02 2020/9/30
 * @Param 
 * @return 
 **/ 
public class DefaultAppDataSource implements AppDataSource{
    DataSourceConfig dbConfig=new DefaultDataSourceConfig();
    private   static DataSource dataSource=null;
    public DefaultAppDataSource( ) {

    }

    @Override
    public String getDBType() {
        return  dbConfig.getDBType();
    }
    /**
     * @Author taxuefan
     * @Description //单例模式创建数据源
     * @Date 14:53 2020/9/30
     * @Param []
     * @return javax.sql.DataSource
     **/
    @Override
    public DataSource getDataSource() {
        DataSourceConfig dbConfig=new DefaultDataSourceConfig();
        if(this.dataSource==null){
            synchronized (DefaultAppDataSource.class){
                if( this.dataSource==null){
                    try{
                        this.dataSource=DruidDataSourceFactory.createDataSource((Properties)dbConfig.getDataSourceConfig());

                    }catch (Exception e){
                        throw new RuntimeException("load default datasource error!");
                    }
                }
            }
        }

        return this.dataSource;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

}