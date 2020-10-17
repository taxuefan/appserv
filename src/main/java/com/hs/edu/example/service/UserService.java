package com.hs.edu.example.service;

import com.alibaba.fastjson.JSON;
import com.hs.edu.example.dao.UserDao;
import com.hs.edu.app.model.User;
import com.hs.edu.api.entity.Result;


/**
 * @ClassName UserService
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/7 21:38
 * @Version 1.0
 **/
public class UserService {
    private UserDao userDao=new UserDao ();
    public Result addUser(User user){
        Result result =new Result();
        try{
        int result_code=userDao.add(user);
        if(result_code!=0){
            result.setCode(-1);;
            result.setMsg("添加数据失败！"); ;
        }}
        catch (Exception e){

        }
        return result;
    }
    public Result getUser(long id){
        Result result =new Result();
        try{
            User user=userDao.get(id);
            result.setData(user);
        }
        catch (Exception e){

        }

        return result;
    }
    public  static void main(String[] args){
        UserService userService=new UserService();
        /**User user=new User();
        user.setPassword("123456");
        user.setUserName("Lucy");
        Result result=userService.addUser(user);
        System.out.println(JSON.toJSONString(result));**/
        Result result= userService.getUser(1);
       System.out.println(JSON.toJSONString(result));
    }
}

