package com.hs.edu.app.enums;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.hs.edu.app.entity.Gamer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName Role
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/11 19:11
 * @Version 1.0
 **/
public enum Role {
    UNKOWN("unkonw"),//未知
    PINGMIN("pingmin"),//平民
    WODI("wodi"); //卧底;
    private static final Map<String, Role> ROLE_MAP = new HashMap<String, Role>();

    static {
        for (Role role : Role.values()) {
            ROLE_MAP.put(role.getCode(), role);
        }
    }
    private String code;

    Role(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public static Role getRole(String code) {
        return ROLE_MAP.get(code);
    }

    public static void main(String args[]){
        Role role=Role.getRole("unkonw");
        LinkedHashMap hashMap =new LinkedHashMap();
        Gamer gamer1 =new Gamer();
        gamer1.setAvatarUrl("url1");
        Gamer gamer2=new Gamer();
        hashMap.put("name",gamer1);
        hashMap.put("name2",gamer2);

        System.out.println(JSON.toJSONString(hashMap));
        System.out.println(JSON.toJSONString(hashMap.values()));
        System.out.println(JSONObject.toJSONString(hashMap));
        System.out.println(JSONObject.toJSONString(hashMap.values()));
        System.out.println(hashMap.size());
       // System.out.println(role.getCode());
        //System.out.println(role.name());
    }
}
