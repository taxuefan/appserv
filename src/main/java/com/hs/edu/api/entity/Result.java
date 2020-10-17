package com.hs.edu.api.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author taxuefan
 * @Description //所有返回的数据的封装类
 * @Date 23:37 2020/9/29
 * @Param
 * @return
 **/
@Data
public class Result<T> implements Serializable {
    /**
     * 服务结果代码
     */
    private int code = 0;
    /**
     * 服务结果信息
     */
    private String msg="success";
    /**
     * @Author taxuefan
     * @Description //返回数扰
     * @Date 16:07 2020/10/10
     * @Param
     * @return
     **/
    private T data=null;


}
