package com.hs.edu.app.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.hs.edu.app.cache.RoomCacheManager;
import com.hs.edu.app.cache.WebSocketManager;
import com.hs.edu.app.entity.*;
import com.hs.edu.app.entity.Dictionary;
import com.hs.edu.app.enums.Role;
import com.hs.edu.app.websocket.MsgType;
import com.hs.edu.app.websocket.SocketMsg;
import com.hs.edu.app.websocket.WebSocketServer;
import com.hs.edu.app.wraper.GamerMsgWraper;
import com.hs.edu.app.wraper.WinnerWraper;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Wrapper;
import java.util.*;

/**
 * @ClassName Utils
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/13 23:15
 * @Version 1.0
 **/
public class AppUtils {
    private  static Random random = new Random();
    public static String genGameSeqNo() {
        return DateUtil.format(new java.util.Date(), "yyyyMMddHHmmssSSS") + "_" + RandomUtil.randomNumbers(3);
    }
    public static void randomGameWord(Room room, Dictionary dictionary) {
        int baiBanNum = room.getBaibanNum() == null ? 0 : room.getBaibanNum();
        int wodiNum = room.getWodiNum() == null ? 0 : room.getWodiNum();
        int pingminNum = room.getPingminNum() == null ? 0 : room.getPingminNum();
        List<String> list = new java.util.ArrayList<String>();
        String prefix="_";
        for (int i = 0; i < baiBanNum; i++) {
            list.add(Role.PINGMIN.getCode()+prefix+"baibang");
        }
        for (int i = 0; i < wodiNum; i++) {
            list.add(Role.WODI.getCode()+prefix+dictionary.getWodi());
        }
        for (int i = 0; i < pingminNum; i++) {
            list.add(Role.PINGMIN.getCode()+prefix+dictionary.getPingmin());
        }
        Object[] aObject=list.toArray();
        AppUtils.randomSort(aObject);
        List<Gamer> gamerList=room.getGame().getGamersList();
        for(int i=0;i<gamerList.size();i++){
            Gamer gamer=gamerList.get(i);
            String gameDict=(String)aObject[i];
            String[] aDictValue=gameDict.split(prefix);
            gamer.setRole(aDictValue[0]);
            gamer.setWord(aDictValue[1]);
            if(gamer.getWord().indexOf("baibang")>=0){
                gamer.setWord("");
            }
        }

    }
    /**
     * @Author 在
     * @Description //先用
     * @Date 10:07 2020/10/14
     * @Param [srcArray]
     * @return void
     **/
    public static void randomSort(Object[] srcArray){
          if(srcArray==null){
              return ;
          }
          for(int i=0;i<srcArray.length;i++){
              int p = random.nextInt(i+1);
              Object tmp = srcArray[i];
              srcArray[i] = srcArray[p];
              srcArray[p] = tmp;
          }
    }

    /**
     * @Author taxuefan
     * @Description //判断游戏胜利标志：卧底全部淘汰，平民胜利，当只剩两个玩家时，卧底还未被淘汰，卧底胜利。
     * @Date 15:51 2020/10/14
     * @Param [roomId] 检查游戏是否结束，并返回信息
     ** @return boolean
     **/
    public static WinnerWraper computeGameResult(String roomId){
        Room room= RoomCacheManager.getInstance().get(roomId);
        int pingMinNum=room.getPingminNum();
        int wodiNum=room.getWodiNum();
        int baiBanNum=room.getBaibanNum();
        int leftPingMinNum=0;
        int leftWodiNum=0;
        int leftGamerNum=0;
        WinnerWraper wraper=new WinnerWraper();
        List<Gamer> gamers=room.getGame().getGamersList();
        for (int i=0;i<gamers.size();i++){
            Gamer gamer=gamers.get(i);
            if(gamer.getRole().equals(Role.PINGMIN.getCode())&&gamer.getStatus()==0){
                leftPingMinNum++;
                leftGamerNum++;
            }
            if(gamer.getRole().equals(Role.WODI.getCode())&&gamer.getStatus()==0){
                leftWodiNum++;
                leftGamerNum++;
            }
        }
        if(leftWodiNum==0){
            wraper.setRole(Role.PINGMIN.getCode());
            wraper.setPingMin(room.getGame().getPingmin());
            wraper.setWodi(room.getGame().getWodi());
            return wraper;
        }else if(leftPingMinNum==0){
            wraper.setRole(Role.WODI.getCode());
            wraper.setPingMin(room.getGame().getPingmin());
            wraper.setWodi(room.getGame().getWodi());
            return wraper;
        } else if(leftGamerNum==2&&leftWodiNum>0){
            wraper.setRole(Role.WODI.getCode());
            wraper.setPingMin(room.getGame().getPingmin());
            wraper.setWodi(room.getGame().getWodi());
            return wraper;
        }else {
            return null;
        }
    }
    /**
     * @Author taxuefan
     * @Description //TODO:封装数据，这块代码其实可以策略设计模块做一下拆解
     * @Date 11:05 2020/10/15
     * @Param [roomId, msgType, currentUserId]
     * @return com.hs.edu.app.wraper.GamerMsgWraper
     **/
    public static  List<Gamer> wrapMsgGamers(String roomId, MsgType msgType) {
        RoomCacheManager roomCacheManager=RoomCacheManager.getInstance();
        Room room=roomCacheManager.get(roomId);
        if(room==null){
            return null;
        }
        boolean isGaming=room.isGaming();
        List<Gamer> newGamerList =new ArrayList<Gamer>();
        List<Gamer> gamerList=null;
        switch (msgType) {
            case GAMING:
                gamerList = room.getGame().getGamersList();
                break;
            case PLAYER_EXIT_ROOM:
                if (isGaming) {
                    gamerList = room.getGame().getGamersList();
                } else {
                    gamerList = room.getGamersList();
                }
                break;
            default:
               gamerList=room.getGamersList();
        }
        if(gamerList!=null&&gamerList.size()>0){
             for(Gamer gamer:gamerList){
                 Gamer newGamer=ObjectUtil.clone(gamer);
                 newGamerList.add(newGamer);
             }
        }
        return  newGamerList;
     }
     /**
      * @Author taxuefan
      * @Description //TODO  这块代码可以用略策的设计模式进行设计，
      * @Date 14:23 2020/10/15
      * @Param [roomId, userId, msg]
      * @return com.hs.edu.app.websocket.SocketMsg
      **/
    public static  SocketMsg filterMsgPacket(String userId,SocketMsg msg){
        Object data=msg.getData();
        if(!(data instanceof  java.util.List)){
          return msg;
        }
        List<Gamer> gamers=(List)data;
        for(Gamer gamer:gamers){
            if(StrUtil.isEmpty(gamer.getUserId())){
                continue;
            }
            if(!gamer.getUserId().equals(userId)){
                gamer.setWord("");
                gamer.setRole(Role.UNKOWN.getCode());
            }
        }
        return msg;
    }
    public static void main(String[] args){
        Object[] arr = {1,2,3,4,5,6,7,8};
        AppUtils.randomSort(arr);
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println("");
        AppUtils.randomSort(arr);
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
    }
}
