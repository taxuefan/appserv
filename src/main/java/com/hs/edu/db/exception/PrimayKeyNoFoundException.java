package com.hs.edu.db.exception;

/**
 * @ClassName QueryConstrainException
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/5 16:37
 * @Version 1.0
 **/
public class PrimayKeyNoFoundException extends  Exception{
    public PrimayKeyNoFoundException(String message) {
        super(message);
    }
}
