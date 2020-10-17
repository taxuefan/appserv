package com.hs.edu.db.core;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.hs.edu.db.config.DBType;
import com.hs.edu.db.config.DataSourceConfig;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 模型实例数据库枚举
 *
 * @author shiyanjun
 * @since 2019-12-15
 */
@Slf4j
public class AppDataSourceFactory {
    private static AppDataSource defaultAppDataSource;
    private static ConcurrentHashMap<String , AppDataSource> dataSourceMap=new ConcurrentHashMap<>();
    /**
     * 创建默认数据源
     * @return
     */
    static {
        AppDataSourceFactory.registerAppDataSource(new DefaultAppDataSource());
    }

    public static AppDataSource getDataSource(String datasourceName) {
        if(datasourceName==null||!dataSourceMap.contains(datasourceName)){
            return getDefaultDataSource();
        }
        return dataSourceMap.get(datasourceName);
    }
    private static AppDataSource getDefaultDataSource(){
        return dataSourceMap.get(DefaultAppDataSource.class.getSimpleName());
    }
    public static void registerAppDataSource( AppDataSource appDataSource){
         if(appDataSource==null){
             throw new RuntimeException("数据源对象为null值");
         }
         //如果存在同名的数据源就不用添加了
         if(dataSourceMap.contains(appDataSource.getName())){
            return ;
         }
        dataSourceMap.put(appDataSource.getName(),appDataSource);
    }
    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        String sql = "select * from user";
        // 使用枚举单例对象DMInstanceDb.INSTANCE获取数据源
        //List<Map<String, Object>> mapList = DataAccess.executeQuery(sql);

        //System.out.println(JSON.toJSONString(mapList));
    }
}