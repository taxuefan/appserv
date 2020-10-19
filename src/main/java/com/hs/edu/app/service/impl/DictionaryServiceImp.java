package com.hs.edu.app.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.hs.edu.api.app.dao.DictDao;
import com.hs.edu.api.app.dao.DictTypeDao;
import com.hs.edu.api.entity.Result;
import com.hs.edu.api.entity.ResultCode;
import com.hs.edu.app.cache.CacheManager;
import com.hs.edu.app.entity.Dictionary;
import com.hs.edu.app.enums.Const;
import com.hs.edu.app.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName DictionaryServiceImp
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/12 17:36
 * @Version 1.0
 **/
@Slf4j
public class DictionaryServiceImp implements DictionaryService {
    DictTypeDao dictTypeDao=new DictTypeDao();
    DictDao dictDao=new DictDao();
    CacheManager cacheManager = CacheManager.getInstance();
    @Override
    public Result getDictionaryTypes(boolean isReload ) {
        String key= Const.DICT_TYPE_KEY.name();
        Result result=new Result();
        if(isReload||!cacheManager.containsKey(key)){
            try {
                List list = dictTypeDao.query();
                cacheManager.remove(key);
                cacheManager.put(key,list);
            }catch(Exception e){
                result.setCode(ResultCode.FAILED.getCode());
                result.setMsg(e.getMessage());
                log.error(e.getMessage());
                return result;
            }
        }
        result.setData(cacheManager.get(key));
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        return result;
    }

    @Override
    public Result getRandomDictinary(int type) {
      return dictDao.getRandomDictinary(type);
    }


    @Override
    public Result addDictionary(Dictionary dic) {
        Result result=new Result();
        if(dic==null){
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg("参数对象不能为空");
        }
        if(StrUtil.isEmpty(dic.getWodi())||StrUtil.isEmpty(dic.getPingmin())){
            result.setCode(ResultCode.DIC_PINGNAME_EMPTY_ERROR.getCode());
            result.setMsg(ResultCode.DIC_PINGNAME_EMPTY_ERROR.getMsg());
        }
        try{
         int i=dictDao.add(dic);
         result.setCode(ResultCode.SUCCESS.getCode());
         result.setMsg(ResultCode.SUCCESS.getMsg());
        }
        catch (Exception e){
            result.setCode(ResultCode.DB_OPER_ERROR.getCode());
            result.setMsg(ResultCode.DB_OPER_ERROR.getMsg());
            log.error(e.getMessage());
        }
        return result;
    }
    public  static void main(String[] args){
        DictionaryService service=new DictionaryServiceImp();
        Result result=service.getDictionaryTypes(false);
        System.out.println(JSONObject.toJSONString(result));
        Result result1=service.getRandomDictinary(0);
        System.out.println(JSONObject.toJSONString(result1));
        Result result2=service.getRandomDictinary(1);
        System.out.println(JSONObject.toJSONString(result2));
    }
}
