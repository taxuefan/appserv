package com.hs.edu.app.exception;

/**
 * @ClassName RoomSeatNoInitException
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/12 14:27
 * @Version 1.0
 **/
public class RoomSeatNoInitException extends Exception{
    private Integer code;
    private String msg;

    public RoomSeatNoInitException(Integer code,String msg) {
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
