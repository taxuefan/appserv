package com.hs.edu.app.service;

import com.hs.edu.api.app.dao.DictDao;
import com.hs.edu.api.app.dao.DictTypeDao;
import com.hs.edu.api.entity.Result;
import com.hs.edu.app.entity.Dictionary;
import com.hs.edu.app.entity.GameDetailLog;
import com.hs.edu.app.entity.GameLog;


import java.util.List;

/**
 * @ClassName DictionaryService
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/10 17:22
 * @Version 1.0
 **/
public interface DictionaryService {
     /**
      * @Author taxuefan
      * @Description //获取字典类型
      * @Date 19:54 2020/10/12
      * @Param []
      * @return com.hs.edu.api.entity.Result
      **/
    public Result getDictionaryTypes(boolean isReload );

     /**
      * @Author taxuefan
      * @Description //TOD0 通过随类型获取随机字典
      * @Date 19:54 2020/10/12
      * @Param [type]
      * @return com.hs.edu.app.entity.Dictionary
      **/
     Result getRandomDictinary(int type);
 
     /**
      * @Author taxuefan
      * @Description //ig
      * @Date 10:54 2020/10/13
      * @Param []
      * @return com.hs.edu.api.entity.Result
      **/
     Result addDictionary(Dictionary dic);



}
