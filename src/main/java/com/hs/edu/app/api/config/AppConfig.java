package com.hs.edu.app.api.config;
import com.hs.edu.api.entity.ResultCode;
import com.hs.edu.app.cache.CacheManager;
import com.hs.edu.app.exception.AppConfigNoExsitException;
import com.hs.edu.db.config.DefaultDataSourceConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;
/**
 * @ClassName AppConfig
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/15 17:39
 * @Version 1.0
 **/
@Slf4j
public class AppConfig {
    private Properties properties=null;
    public static AppConfig getInstance() {
        return AppConfig.AppConfigFactory.APP_CONFIG;
    }
    private static class AppConfigFactory {
        private static final AppConfig APP_CONFIG = new AppConfig();
    }
    public String  getConfig(String keyName){
        if(this.properties==null){
            this.properties=loadConfig();
        }
        String value=properties.getProperty(keyName);
        //抛出运行时异常
        if(value==null) {
            throw  new AppConfigNoExsitException(ResultCode.APP_CONFIG_NO_EXSIT.getCode(), ResultCode.APP_CONFIG_NO_EXSIT.getMsg());
        }
       return value;
    }
    private  Properties loadConfig() {
        InputStream is = null;
        Properties properties = null;
        try {
            properties = new Properties();
            //resources目录下获取配置文件
            is = DefaultDataSourceConfig.class.getClassLoader().getResourceAsStream("appConfig.properties");
            properties.load(is);
        } catch (Exception e) {
            log.error("获取app配置文件出错，msg:"+e.getMessage());
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
