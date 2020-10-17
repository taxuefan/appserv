package com.hs.edu.db.config;



public interface DataSourceConfig<T> {

    /**
     * 从properties里获取数据源配置     *
     * @return 配置信息
     */
    public T getDataSourceConfig() ;
    /**
     * @Author chu
     * @Description //TODO 
     * @Date 15:55 2020/10/10
     * @Param []
     * @return java.lang.String
     **/
    public String getDBType();
    /**
     * @Author taxuefan
     * @Description //重新加载数据源配置
     * @Date 15:57 2020/10/10
     * @Param []
     * @return void
     **/
    public void reloadDataSourceConfig();
    public void loadDataSourceConfig();

}