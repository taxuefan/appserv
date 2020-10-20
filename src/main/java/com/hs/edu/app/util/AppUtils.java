package com.hs.edu.app.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.hs.edu.app.cache.RoomCacheManager;
import com.hs.edu.app.entity.*;
import com.hs.edu.app.entity.Dictionary;
import com.hs.edu.app.enums.Role;
import com.hs.edu.app.websocket.MsgType;
import com.hs.edu.app.websocket.SocketMsg;
import com.hs.edu.app.wraper.GamerMsgWraper;
import com.hs.edu.app.wraper.Winner;
import com.hs.edu.app.wraper.WinnerWraper;
import lombok.extern.slf4j.Slf4j;

import javax.rmi.CORBA.Util;
import java.util.*;

/**
 * @ClassName Utils
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/13 23:15
 * @Version 1.0
 **/
@Slf4j
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
        Winner winner=new Winner();
        if(leftWodiNum==0){
            winner.setRole(Role.PINGMIN.getCode());
            winner.setPingMin(room.getGame().getPingmin());
            winner.setWodi(room.getGame().getWodi());
            wraper.setWinner(winner);
            wraper.setGameStatus(2);
            return wraper;
        }else if(leftPingMinNum==0){
            winner.setRole(Role.WODI.getCode());
            winner.setPingMin(room.getGame().getPingmin());
            winner.setWodi(room.getGame().getWodi());
            wraper.setWinner(winner);
            wraper.setGameStatus(2);
            return wraper;
        } else if(leftGamerNum==2&&leftWodiNum>0){
            winner.setRole(Role.WODI.getCode());
            winner.setPingMin(room.getGame().getPingmin());
            winner.setWodi(room.getGame().getWodi());
            wraper.setWinner(winner);
            wraper.setGameStatus(2);
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
      * @Description //TODO 用于过滤消息包的，发送的时候，当前的websocket会显示所有信息，其它玩家的
      * @Date 14:23 2020/10/15
      * @Param [roomId, userId, msg]
      * @return com.hs.edu.app.websocket.SocketMsg
      **/
    public static  SocketMsg filterMsgPacket(String userId,SocketMsg msg){
         Object data=msg.getData();
        if(!(data instanceof GamerMsgWraper)){
          return msg;
        }
        log.info("filter msg packet list....current user:{}",userId);
        SocketMsg newMsg=new SocketMsg();
        newMsg.setMsgType(msg.getMsgType());
        List newUserList=new ArrayList();
        GamerMsgWraper newWraper=new GamerMsgWraper();
        List<Gamer> gamers= ((GamerMsgWraper) data).getUsers();
        newWraper.setGameStatus(((GamerMsgWraper) data).getGameStatus());
        for(Gamer gamer:gamers){
            Gamer newGamer=ObjectUtil.clone(gamer);
            newUserList.add(newGamer);
            if(StrUtil.isEmpty(newGamer.getUserId())){
                continue;
            }
            if(!newGamer.getUserId().equals(userId)){
                newGamer.setWord("");
                if(newMsg.getMsgType()!=MsgType.GAMING||newMsg.getMsgType()!=MsgType.PLAYER_EXIT_ROOM) {
                    newGamer.setRole(Role.UNKOWN.getCode());
                }
            }
        }
        newWraper.setUsers(newUserList);
        newMsg.setData(newWraper);
        return newMsg;
    }
    /*
     * @Author
     * @Description //获取游戏的日志信息
     * @Date 0:02 2020/10/20
     * @Param
     * @return
     **/
    public static   GameLog  wrapperGameLog(String roomId, WinnerWraper winnerWraper) {
        if(winnerWraper==null){
            return null;
        }
        Room room=RoomCacheManager.getInstance().get(roomId);
        GameLog gameLog= new GameLog();
        gameLog.setCreateTime(room.getGame().getCreateTime());
        gameLog.setDictType(room.getDictType());
        gameLog.setEndTime(System.currentTimeMillis());
        gameLog.setHostUserId(room.getHostUserId());
        gameLog.setSeqNo(room.getGame().getSeqNo());
        gameLog.setWinnerRole(winnerWraper.getWinner().getRole());
        gameLog.setRoomId(roomId);
        return  gameLog;
    }
    /**
     * @Author taxuefan
     * @Description //TODO 获取游戏日志详细信息
     * @Date 0:01 2020/10/20
     * @Param [roomId, winnerWraper]
     * @return java.util.List<com.hs.edu.app.entity.GameDetailLog>
     **/
    public static  List<GameDetailLog> wrapGameDetailLog(String roomId, WinnerWraper winnerWraper) {
        if(winnerWraper==null){
            return null;
        }
        Room room=RoomCacheManager.getInstance().get(roomId);
        if(room.getGame()==null){
            return null;
        }
        List<Gamer> gamersList=room.getGame().getGamersList();
        List<GameDetailLog> gameDetailLogList=new ArrayList<GameDetailLog>();
        for(Gamer gamer:gamersList){
            if(StrUtil.isEmpty(gamer.getUserId())){
                continue;
            }
            GameDetailLog gameDetailLog=new GameDetailLog();
            gameDetailLog.setAvatarUrl(gamer.getAvatarUrl());
            gameDetailLog.setRole(gamer.getRole());
            gameDetailLog.setWord(gamer.getWord());
            gameDetailLog.setNickName(gamer.getNickName());
            gameDetailLog.setSeqNo(room.getGame().getSeqNo());
            gameDetailLog.setUserId(gamer.getUserId());
            gameDetailLogList.add(gameDetailLog);
        }
        return  gameDetailLogList;
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
        long systime=System.currentTimeMillis();
        Calendar calendar= DateUtil.calendar(systime);
        //DateUtil.toLocalDateTime()
    }
}
