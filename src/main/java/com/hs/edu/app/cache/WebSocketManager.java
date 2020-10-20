package com.hs.edu.app.cache;

import com.alibaba.fastjson.JSONObject;
import com.hs.edu.app.entity.GameDetailLog;
import com.hs.edu.app.entity.GameLog;
import com.hs.edu.app.entity.Gamer;
import com.hs.edu.app.entity.Room;
import com.hs.edu.app.service.GameService;
import com.hs.edu.app.service.IWebSocketManager;
import com.hs.edu.app.service.impl.GameServiceImpl;
import com.hs.edu.app.util.AppUtils;
import com.hs.edu.app.websocket.MsgType;
import com.hs.edu.app.websocket.SocketMsg;
import com.hs.edu.app.websocket.WebSocketServer;
import com.hs.edu.app.wraper.GamerMsgWraper;
import com.hs.edu.app.wraper.Winner;
import com.hs.edu.app.wraper.WinnerWraper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName RoomCache
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/13 14:22
 * @Version 1.0
 **/
@Slf4j
public class WebSocketManager implements IWebSocketManager {
    //把websocket对象缓存起来
    private static Map<String,Map<String,WebSocketServer>> webSocketCache= new ConcurrentHashMap<String, Map<String,WebSocketServer>>();
    private ReentrantLock lock=new ReentrantLock();
    RoomCacheManager roomCacheManager =RoomCacheManager.getInstance();
    GameService gameService=new GameServiceImpl();
    /**
     * @Author taxuefan
     * @Description //TODO 实例化websocket管理器
     * @Date 9:16 2020/10/15
     * @Param []
     * @return com.hs.edu.app.cache.WebSocketManager
     **/
    public   static WebSocketManager getInstance() {
        return  WebSocketManagerFactory.Web_SOCKET_CACHE;
    }
    /**
     * @Author taxuefan
     * @Description todo:单例模式，私有类实现懒加载
     * @Date 21:22 2020/10/12
     * @Param
     * @return
     **/
    private static class WebSocketManagerFactory {
        private static final WebSocketManager Web_SOCKET_CACHE = new WebSocketManager();
    }
    /**
     * @Author taxuefan
     * @Description //获取谋个websocket
     * @Date 14:48 2020/10/13
     * @Param []
     * @return boolean
     **/
    public    WebSocketServer getRoomWebSocketServer(String roomId,String userId){
         if(!containsWebSocket(roomId,userId)){
             return null;
         }
        return  webSocketCache.get(roomId).get(userId);
    }
    /**
     * @Author taxuefan
     * @Description //TODO 加入新的websocket
     * @Date 17:30 2020/10/14
     * @Param [roomId]
     * @return void
     **/
    public  void addWebSocket(String roomId,String userId,WebSocketServer socketServer){

        //判断房间不存在是否在存在
        if(!webSocketCache.containsKey(roomId)){
              Map<String,WebSocketServer> map=new HashMap<String,WebSocketServer>();
              map.put(userId,socketServer);
              webSocketCache.put(roomId,map);
              return;
          }
         Map<String,WebSocketServer> map=webSocketCache.get(roomId);
         if(containsWebSocket(roomId,userId)) {
             map.remove(userId);
         }
         map.put(userId,socketServer);

    }
    /***
     * @Author f
     * @Description //TODO
     * @Date 17:45 2020/10/14
     * @Param [roomId, userId]
     * @return boolean
     **/
    public boolean containsWebSocket(String roomId,String userId){
        if(!webSocketCache.containsKey(roomId)){
            return false;
        }
        Map<String,WebSocketServer> map=webSocketCache.get(roomId);
        if(map==null||map.size()==0){
            return false;
        }
        return map.containsKey(userId);
    }
    /**
     * @Author taxuefan
     * @Description todo:从缓存中移除房间内的某个roomId
     * @Date 23:44 2020/10/14
     * @Param [roomId, userId]
     * @return com.hs.edu.app.websocket.WebSocketServer
     **/
    public WebSocketServer removeWebsocket(String roomId,String userId){

        if(!webSocketCache.containsKey(roomId)){
            return null ;
        }
        return  webSocketCache.get(roomId).remove(userId);

    }
    /**
     * @Author taxuefan
     * @Description todo:从缓存中移除房间内的某个roomId
     * @Date 23:44 2020/10/14
     * @Param [roomId, userId]
     * @return com.hs.edu.app.websocket.WebSocketServer
     **/
    public void removeRoomWebSocket(String roomId){
        if(!webSocketCache.containsKey(roomId)){
            return ;
        }
        webSocketCache.get(roomId).clear();
    }
    /**
     * @Author taxuefan
     * @Description //判断房间是否存在
     * @Date 9:38 2020/10/15
     * @Param [roomId]
     * @return boolean
     **/
    public boolean isRoomExist(String roomId){
          return roomCacheManager.existRoom(roomId)&&webSocketCache.containsKey(roomId);
    }
    /**
     * @Author taxuefan
     * @Description //发送加入房间数据包
     * @Date 17:08 2020/10/14
     * @Param [roomId]
     * @return void
     **/
    public void sendEnterRoomPacket(String roomId){
        log.info("sendEnterRoomPacket......");
        SocketMsg msg=genGamerPacketData(roomId,MsgType.ENTER_ROOM);
        lock.lock();
        this.sendMsgToWebSocket(roomId,MsgType.ENTER_ROOM,msg);
        lock.unlock();
    }
    /**
     * @Author
     * @Description //TODO
     * @Date 10:08 2020/10/15
     * @Param [gamerList]
     * @return java.lang.String
     **/

    /**
     * @Author taxuefan
     * @Description //发送游戏游戏开始的packet包
     * @Date 17:08 2020/10/14
     * @Param [roomId]
     * @return void
     **/
    public void sendGamingPacket(String roomId){
        log.info("sendGamingPacket......");
        SocketMsg msg=genGamerPacketData(roomId,MsgType.GAMING);
        lock.lock();
        this.sendMsgToWebSocket(roomId,MsgType.GAMING,msg);
        lock.unlock();
    }
    private SocketMsg genGamerPacketData(String roomId,MsgType msgType){
        SocketMsg<GamerMsgWraper> data=new SocketMsg();
        data.setMsgType(msgType);
        GamerMsgWraper gameWraper=new GamerMsgWraper();
        List<Gamer> sendGamers= AppUtils.wrapMsgGamers(roomId,msgType);
        Room room=RoomCacheManager.getInstance().get(roomId);
        gameWraper.setGameStatus(room.isGaming()?1:0);
        gameWraper.setUsers(sendGamers);
        data.setData(gameWraper);
        return data;
    }
    private void sendMsgToWebSocket(String roomId,MsgType msgType,SocketMsg data){
        if(!isRoomExist(roomId)){
            log.info("roomid {} is not exist",roomId);
            return;
        }
        Room room=roomCacheManager.get(roomId);
        if(room.getGamersList()==null){
            log.info("roomid  {} contains no game user",roomId);
            return;
        }
        try{
            Map<String,WebSocketServer> serversMap=WebSocketManager.getInstance().getRoomWebSockets(roomId);
            if(serversMap==null||serversMap.size()==0){
                log.info("websocket缓存中，不包含webscoket服务,roomId: {}",roomId);
                return;
            }
            if(log.isInfoEnabled()){
                log.info("send data:{}",JSONObject.toJSONString(data));
            }
            for (Map.Entry<String, WebSocketServer> m : serversMap.entrySet()) {
                WebSocketServer webSocket= m.getValue();
                webSocket.sendPacket(data);
            }
        }catch (Exception e){
            log.info("sendEnterRoomPacket error roomId:{} msg:",roomId,e.toString());
        }
        log.info("Send Enter Room Packet......roomId:{}",roomId);
    }
    /**
     * @Author taxuefan
     * @Description //游戏主动结束
     * @Date 17:18 2020/10/14
     * @Param [roomId]
     * @return void
     **/
    public void sendGameVotePacket(String roomId){
        log.info("sendGameVotePacket......");
        SocketMsg<WinnerWraper> msg=new SocketMsg();
        msg.setMsgType(MsgType.GAME_OVER);
        WinnerWraper winnerWraper=AppUtils.computeGameResult(roomId);
        lock.lock();
        GameLog gameLog=AppUtils.wrapperGameLog(roomId,winnerWraper);
        List<GameDetailLog> gameDetailLogList=AppUtils.wrapGameDetailLog(roomId,winnerWraper);
        if(winnerWraper==null){
            SocketMsg gamingMsg= genGamerPacketData(roomId,MsgType.GAMING);
            this.sendMsgToWebSocket(roomId,MsgType.GAMING,gamingMsg);
        }else{
          msg.setData(winnerWraper);
          this.sendMsgToWebSocket(roomId,MsgType.GAME_OVER,msg);
          Room room=roomCacheManager.get(roomId);
          room.finishGame();
        }
        lock.unlock();
        //主录日志
        this.logGameLog(gameLog,gameDetailLogList);
    }
    /**
     * @Author taxuefan
     * @Description //用多线程序记录日志
     * @Date 0:11 2020/10/20
     * @Param [gameLog, gameDetailLogList]
     * @return void
     **/
     private  void logGameLog(GameLog gameLog,List<GameDetailLog> gameDetailLogList){
             Thread thread=new Thread(()->{
                 if(gameLog!=null){
                     gameService.addGameLog(gameLog);
                 }
                 if(gameDetailLogList!=null&&gameDetailLogList.size()>0){
                     for(GameDetailLog gameDetailLog:gameDetailLogList){
                         gameService.addGameDetailLog(gameDetailLog);
                     }
                 }
             });
             thread.start();
     }
    /**
     * @Author taxuefan
     * @Description //游戏强制强束Servlet
     * @Date 17:18 2020/10/14
     * @Param [roomId]
     * @return void
     **/
    public void sendGameOverForcePacket(String roomId){
        log.info("sendGameOverForcePacket......");
        lock.lock();
        SocketMsg msg=genGamerPacketData(roomId,MsgType.GAME_OVER_FORCE);
        this.sendMsgToWebSocket(roomId,MsgType.GAME_OVER_FORCE,msg);
        lock.unlock();
    }
    /**
     * @Author taxuefan
     * @Description //TODO玩家退出游戏数据包
     * @Date 17:23 2020/10/14
     * @Param [roomId, userId]
     * @return void
     **/
    public  void sendGamerExitPacket(String roomId,String userId){
        log.info("sendGamerExitPacket......");
        Room room=roomCacheManager.get(userId);
        if(room!=null){
            return;
        }
        Gamer gamer=room.findGamer(userId);
        if(gamer==null){
            return;
        }
        lock.lock();
        if(!room.isGaming()){
            int gamerPos=room.findGamerPostion(userId);
            gamer=new Gamer();
            room.getGamersList().set(gamerPos,new Gamer());//初始化数据，等待其它人的加入
        }else{
            gamer.setStatus(0);
            room.getGame().findGamer(userId).setStatus(1);//淘汰状态
            room.getGame().findGamer(userId).setOnlineStatus(0);
        }
        if(!isRoomExist(roomId)){
            return;
        }
        this.removeWebsocket(roomId,userId);//从websocket移除数据
        if(room.isHost(userId)){ //如果是房主退出房间发送关闭房间的消息
            this.sendCloseRoomPacket(roomId,userId);
        }else {
            SocketMsg msg=genGamerPacketData(roomId,MsgType.PLAYER_EXIT_ROOM);
            this.sendMsgToWebSocket(roomId, MsgType.PLAYER_EXIT_ROOM,msg);//给其它玩家发送，数据表明有玩家退现
        }
        lock.unlock();
    }
    /**
     * @Author taxuefan
     * @Description //TOD0发送房间关闭数据包
     * @Date 17:23 2020/10/14
     * @Param [roomId, userId]
     * @return void
     **/
    public  void sendCloseRoomPacket(String roomId,String userId){
        log.info("sendCloseRoomPacket......");
        this.removeWebsocket(roomId,userId);
        Room room=roomCacheManager.get(roomId);
        if(room==null&&this.webSocketCache.get(roomId)==null){
            log.info("room cache and websocket cache is clear......");
            return;
        }
        boolean isForce=room==null?true:false;
        SocketMsg<Map> socketMsg=new SocketMsg();
        socketMsg.setMsgType(MsgType.CLOSE_ROOM);
        Map dataMap=  new HashMap<String,Integer>();
        if(isForce) {//是否强制退出
            dataMap.put("exitFlag",1);
        }else{
            dataMap.put("exitFlag",0);//是否要退出游戏
        }
        socketMsg.setData(dataMap);
        lock.lock();
        this.sendMsgToWebSocket(roomId, MsgType.CLOSE_ROOM,socketMsg);
        if(room!=null){
            room.clearGamer();
            room.removeGame();
            roomCacheManager.remove(roomId);
        }
        if( webSocketCache.get(roomId)!=null){
            webSocketCache.get(roomId).clear();
        }
        webSocketCache.remove(roomId);
        lock.unlock();
    }

    /**
     * @Author taxuefan
     * @Description //获取房间中的websocket
     * @Date 10:44 2020/10/15
     * @Param [roomId]
     * @return java.util.HashMap<java.lang.String,com.hs.edu.app.websocket.WebSocketServer>
     **/
    public Map<String,WebSocketServer> getRoomWebSockets(String roomId){
        return webSocketCache.get(roomId);
    }
    /**
     * @Author taxuefan
     * @Description //获取房间中的websocket
     * @Date 10:44 2020/10/15
     * @Param [roomId]
     * @return java.util.HashMap<java.lang.String,com.hs.edu.app.websocket.WebSocketServer>
     **/
    public Map  getAllWebSocket(){
        return this.webSocketCache;
    }
}
