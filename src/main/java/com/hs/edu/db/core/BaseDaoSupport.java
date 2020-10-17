package com.hs.edu.db.core;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

import com.hs.edu.db.core.parse.SqlHolder;
import com.hs.edu.db.core.parse.QueryConstrain;
import com.hs.edu.db.core.parse.SqlBuilderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

@Slf4j
public class BaseDaoSupport<T> implements DaoSupport<T> {
    private Class<T> clazz;
    BeanListHandler handler = null;

    @SuppressWarnings("unchecked")
    public BaseDaoSupport() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) pt.getActualTypeArguments()[0];
        handler = new BeanListHandler(clazz, new BasicRowProcessor(new GenerousBeanProcessor()));
    }

    @Override
    public int add(T bean) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderInsertSql(clazz, bean);
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return runner.update(ps.getSql(), ps.getSqlParam().getParamValues());

    }

    @Override
    public int delete(Object id) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderDeleteSql(clazz, id);
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return runner.update(ps.getSql(), ps.getSqlParam().getParamValues());

    }

    @Override
    public int update(T bean) throws Exception {
        SqlHolder ps = SqlBuilderUtil.builderUpdateSql(clazz, bean);
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return runner.update(ps.getSql(), ps.getSqlParam().getParamValues());

    }

    @Override
    public List<T> query() throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderQuerySql(clazz, null, getAppDataSource().getDBType());
        System.out.println(ps.getSql());
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return (List<T>) runner.query(ps.getSql(), handler, ps.getSqlParam().getParamValues());


    }

    @Override
    public List<T> query(int rownum) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderQuerySql(clazz, null, rownum, 1, getAppDataSource().getDBType());
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return (List<T>) runner.query(ps.getSql(), handler, ps.getSqlParam().getParamValues());


    }

    @Override
    public List<T> query(QueryConstrain queryConstrain) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderQuerySql(clazz, queryConstrain, getAppDataSource().getDBType());
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return (List<T>) runner.query(ps.getSql(), new BeanListHandler(clazz), ps.getSqlParam().getParamValues());


    }

    @Override
    public List<T> query(QueryConstrain queryConstrain, int rownum) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderQuerySql(clazz, queryConstrain, rownum, 1, getAppDataSource().getDBType());
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return (List<T>) runner.query(ps.getSql(), handler, ps.getSqlParam().getParamValues());


    }

    @Override
    public PageHolder<T> query(int pageSize, int pageNum) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderQuerySql(clazz, null, pageSize, pageNum, getAppDataSource().getDBType());
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        Long totalNum = this.count(null);
        List list = (List<T>) runner.query(ps.getSql(), handler, ps.getSqlParam().getParamValues());
        PageHolder result = new PageHolder();
        result.setTotalCount(totalNum.intValue());
        result.setCurrentPage(pageNum);
        result.setPageSize(pageNum);
        return result;


    }

    @Override
    public PageHolder<T> query(QueryConstrain queryConstrain, int pageSize, int pageNum) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderQuerySql(clazz, queryConstrain, pageSize, pageNum, getAppDataSource().getDBType());
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        Long totalNum = this.count(queryConstrain);
        List list = (List<T>) runner.query(ps.getSql(), handler, ps.getSqlParam().getParamValues());
        PageHolder result = new PageHolder();
        result.setItems(list);
        result.setTotalCount(totalNum.intValue());
        result.setPageSize(pageSize);
        result.setCurrentPage(pageNum);
        return result;


    }


    @Override
    public T get(Object id) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderGetSql(clazz, id);
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        T result = (T) runner.query(ps.getSql(), handler, ps.getSqlParam().getParamValues());
        System.out.println(result.getClass().getName());
        return result;


    }

    @Override
    public T find(T bean) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderFindSql(clazz, bean);
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return (T) runner.query(ps.getSql(), handler, ps.getSqlParam().getParamValues());


    }

    @Override
    public Long count(QueryConstrain queryConstrain) throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderCountSql(clazz, queryConstrain, getAppDataSource().getDBType());
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        return runner.query(ps.getSql(), new ScalarHandler<Long>(), ps.getSqlParam().getParamValues());


    }

    public void executeUpdate(String sql, Object[] aParam) throws Exception {

        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        runner.update(sql, aParam);

    }

    public <T> PageHolder<T> executeQuery(String sql, int pageSize, int pageNum, Object[] aParam, Class<T> clazz1) throws Exception {
        PageHolder holder = new PageHolder();

        String sqlCount = SqlBuilderUtil.builderQueryPageCount(sql);
        sql = SqlBuilderUtil.builderQueryPage(sql, pageSize, pageNum, getAppDataSource().getDBType());
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        for (int i = 0; i < aParam.length; i++) {
            System.out.print("\r\naParam[" + i + "]:" + aParam[i]);
        }
        int total_num = runner.query(sqlCount, new ScalarHandler<Integer>(), aParam);
        List list = null;
        System.out.print("sql：" + sql);
        if ("java.util.Map".endsWith(clazz1.getName())) {
            list = (List) runner.query(sql, new MapListHandler(), aParam);
        } else {
            list = (List) runner.query(sql, handler, aParam);
        }
        holder.setItems(list);
        holder.setPageSize(pageSize);
        holder.setCurrentPage(pageNum);
        holder.setTotalCount(total_num);
        return holder;


    }

    public <T> List<T> executeQuery(String sql, Object[] aParam, Class<T> clazz1) throws Exception {

        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        List list = null;
        log.info("sql:" + sql);
        if ("java.util.Map".endsWith(clazz1.getName())) {
            list = (List) runner.query(sql, new MapListHandler(), aParam);
        } else {
            list = (List) runner.query(sql, handler, aParam);
        }
        return list;


    }

    @Override
    public String getSequence() throws Exception {

        SqlHolder ps = SqlBuilderUtil.builderSequenceSql(clazz);
        QueryRunner runner = new QueryRunner(getAppDataSource().getDataSource());
        Long seq = runner.query(ps.getSql(), new ScalarHandler<Long>(), ps.getSqlParam().getParamValues());
        return String.valueOf(seq);


    }


    public AppDataSource getAppDataSource() {
        return AppDataSourceFactory.getDataSource(null);
    }
}
