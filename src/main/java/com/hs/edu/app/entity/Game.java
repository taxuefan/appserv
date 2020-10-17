package com.hs.edu.app.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @ClassName Game
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/11 18:57
 * @Version 1.0
 **/
@Data
public class Game implements Serializable,Cloneable {
    String seqNo;//流水号
    List<Gamer> gamersList=new java.util.ArrayList<Gamer>();//玩家列表
    Integer dictType;
    String pingmin;
    String wodi;
    Integer status=0;//0初始化状态 1游戏中状态，2游戏结束状态
    String winner;//谁是赢家


    /**
     * @Author chu
     * @Description //TODO 
     * @Date 19:43 2020/10/11
     * @Param [userId]
     * @return com.hs.edu.app.entity.Gamer
     **/
    public Gamer findGamer(String userId){
        Gamer returnGamer =null;
        if(this.gamersList==null||this.gamersList.size()<=0){
            return null;
        }
        for(Gamer gamer:gamersList){
            if(gamer.getUserId().equals(userId)){
                returnGamer=gamer;
            }
        }
        return  returnGamer;
    }
    /**
     * @Author 移除游戏玩家
     * @Description //TODO 
     * @Date 19:49 2020/10/11
     * @Param [userId]
     * @return void
     **/
    public void removeGamer(String userId){
        if(this.gamersList==null){
            return;
        }
        Gamer gamer=this.findGamer(userId);
        if(gamer!=null){
            this.gamersList.remove(gamer);
        }
    }
    public void clearGamer(){
        if(this.gamersList==null){
            return;
        }
        this.gamersList.clear();
        this.gamersList=null;
    }
    /*
     * @Author taxuefan
     * @Description //TODO 
     * @Date 11:24 2020/10/14
     * @Param []
     * @return boolean
     **/
    public boolean isGamerOver(){
       return this.status==2;
    }
}
