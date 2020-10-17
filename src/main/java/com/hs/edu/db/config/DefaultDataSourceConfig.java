package com.hs.edu.db.config;

import com.hs.edu.db.util.DBUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

/**
 * @Author taxuefan
 * @Description //TODO
 * @Date 15:13 2020/9/29
 * @Param
 * @return
 **/
@Slf4j
public class DefaultDataSourceConfig implements DataSourceConfig{
    private static Properties properties = null;
    private  final String DataBaseURL="url";
    @Override
    public Object getDataSourceConfig() {
        this.properties=this.loadDBConfig();
        return this.properties;
    }
    @Override
    /**
     * @Author taxuefan
     * @Description //获取当前数据源的数据类型
     * @Date 14:39 2020/9/30
     * @Param []
     * @return java.lang.String
     **/
    public String getDBType() {
       if(this.properties==null){
           loadDataSourceConfig();
       }
       return DBUtil.getDBType((String) properties.get(this.DataBaseURL));
    }
    /**
     * 重新加截配置
     * @Description //TODO
     * @Date 14:02 2020/9/30
     * @Param []
     * @return void
     **/
    @Override
    public void reloadDataSourceConfig() {
         if(properties!=null){
             properties.clear();
         }
         loadDBConfig();
    }
    public void loadDataSourceConfig(){
        if(this.properties==null){
            synchronized (DefaultDataSourceConfig.class){
                if(this.properties==null){
                    this.properties=loadDBConfig();
                }
            }
        }
    }
    /**
     * 从properties里获取数据源配置     *
     * @return 配置信息
     */
    private  Properties loadDBConfig() {
        InputStream is = null;
        Properties properties = null;
        try {
            properties = new Properties();
            //resources目录下获取配置文件
            is = DefaultDataSourceConfig.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(is);
        } catch (Exception e) {
           log.error("获取数据库配置信息失败，msg:"+e.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {

            }
        }
        return properties;
    }
}