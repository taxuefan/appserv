package com.hs.edu.api.app.dao;

import com.hs.edu.api.entity.Result;
import com.hs.edu.api.entity.ResultCode;
import com.hs.edu.app.entity.Dictionary;
import com.hs.edu.db.core.BaseDaoSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName DictDao
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/12 17:33
 * @Version 1.0
 **/
@Slf4j
public class DictDao extends BaseDaoSupport<Dictionary> {

    public Result getRandomDictinary(int type) {
        if(type==0){
            return this.queryRandomByAll();
        }else{
            return  this.queryRandomByType(type);
        }
    }
    private Result queryRandomByAll(){
        String  sql="SELECT * FROM app_game_dictionary AS t1 JOIN (SELECT ROUND(RAND() * (SELECT MAX(id) FROM app_game_dictionary )) AS id ) AS t2 WHERE t1.id  >= t2.id" +
                " ORDER BY t1.id ASC LIMIT 1";
        Result result=new Result();
        try {
            List list= this.executeQuery(sql, null, Dictionary.class);
            result.setData(list.get(0));
        }catch (Exception e){
            result.setCode(ResultCode.DB_OPER_ERROR.getCode());
            result.setMsg(ResultCode.DB_OPER_ERROR.getMsg());
            log.error(e.getMessage());
        }
        finally {
            return result;
        }
    }
    private Result queryRandomByType(int type){
        String  sql="SELECT * FROM app_game_dictionary AS t1 JOIN ( SELECT ROUND( RAND( ) * ( SELECT MAX( id ) FROM app_game_dictionary WHERE type_id =? ) ) AS id ) AS t2 " +
                "WHERE t1.id >= t2.id and  t1.type_id =? ORDER BY t1.id ASC LIMIT 1" ;
        Result result=new Result();
        try{
            List list= this.executeQuery(sql,new Object[]{type,type},Dictionary.class);
            result.setData(list.get(0));
        }catch (Exception e){
            result.setCode(ResultCode.DB_OPER_ERROR.getCode());
            result.setMsg(ResultCode.DB_OPER_ERROR.getMsg());
            log.error(e.getMessage());
        }finally {
            return result;
        }
    }
}
