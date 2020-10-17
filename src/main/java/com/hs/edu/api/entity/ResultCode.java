package com.hs.edu.api.entity;

/**
 * @ClassName ResultCode
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/10 13:47
 * @Version 1.0
 **/

public enum ResultCode {
    /* 成功状态码 */
    SUCCESS(0, "成功"),
    FAILED(-1, "调用服务失败"),
    /* http类型错误 */
    FORBIDDEN(403, "禁止访问"),
    NO_PERMISSION(401, "没有权限"),
    NO_FOUND(404, "文件找不到错误"),
    INTERNAL_SERVER_ERROR(500, "内部服务器出错"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    ROOM_GAMER_SETTING_ERROR(10002, "平民人数或卧底人数不能为零"),
    ROOM_NO_HOST(10003, "房主ID不能为空"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),
    PARAM_GAMMER_BLANK(10005,"游戏对象不能为空"),

    /* 业务错误：20001-29999 */
    APP_ROOM_FULL(20001, "房间已满无法添加！"),
    APP_ROOM_NO_EXIST(20002, "房间不存在"),
    APP_ROOM_USER_FULL(20003, "房间人数已满"),
    APP_USER_NO_EXIT(20004, "用户ID不存在"),
    APP_NO_HOST_USER(20005, "对不起，您不是房主"),
    APP_GAME_NO_EXIST(20006, "表示当前游戏不存在"),
    APP_GAMER_NO_EXIST(20007, "玩家不存在"),
    APP_GAMER_SEAT_NO_INIT(20008, "游戏房间位置未始化"),
    APP_ROOM_NO_ID(20009, "房间号ID不存在"),
    APP_ROOM_USER_EXIST(20010, "APP玩家已经存在"),
    APP_HOST_EMPTY(20011, "对不起，房主不存在"),
    APP_ROOM_NOT_READY(20012, "玩家还没有满"),
    APP_ROOM_GAME_NO_RROR(20013, "游戏编号传入有误"),
    APP_ROOM_GAME_NOT_START(20013, "游戏还没有开始呢？"),
    APP_USER_HAS_CREATE_ROOM(20014, "该用户已创建房间！"),
    APP_USER_HAS_ENTER_ROOM(20015, "您已进入该房间！"),
    /* 数据错误：30001-399999 */
    UNIQUE_ERROR(30001, "数据重复添加"),
    DB_OPER_ERROR(30002, "数据库操作失败"),
    DIC_PINGNAME_EMPTY_ERROR(30003, "卧底名称或平民名称不能为空"),
    APP_NO_DICT(30003, "数据库中没有词汇请添加"),
    APP_CONFIG_NO_EXSIT(30004, "APP配置数据不存在！");


    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}