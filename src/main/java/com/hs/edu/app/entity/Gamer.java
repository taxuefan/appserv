package com.hs.edu.app.entity;

import com.hs.edu.app.enums.Role;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GameUser
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/11 17:13
 * @Version 1.0
 **/
@Data
public class Gamer implements Serializable,Cloneable{
    private String userId;       //用户ID，wx号
    private String nickName;     //昵称
    private String avatarUrl;    //头像url地址
    private Integer onlineStatus;//在线状态
    private Integer status; //游戏状态
    private String word; //持有的字
    private String role;//角色




}
