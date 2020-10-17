package com.hs.edu.app.exception;

/**
 * @ClassName AppConfigNoExsitException
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/15 22:59
 * @Version 1.0
 **/
public class AppConfigNoExsitException extends RuntimeException{
    private Integer code;
    private String msg;

    public AppConfigNoExsitException(Integer code,String msg) {
        super(msg);
        this.code = code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
