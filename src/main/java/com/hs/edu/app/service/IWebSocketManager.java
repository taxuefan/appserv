package com.hs.edu.app.service;

import com.alibaba.fastjson.JSONObject;
import com.hs.edu.app.cache.RoomCacheManager;
import com.hs.edu.app.cache.WebSocketManager;
import com.hs.edu.app.entity.Gamer;
import com.hs.edu.app.entity.Room;
import com.hs.edu.app.util.AppUtils;
import com.hs.edu.app.websocket.MsgType;
import com.hs.edu.app.websocket.SocketMsg;
import com.hs.edu.app.websocket.WebSocketServer;
import com.hs.edu.app.wraper.GamerMsgWraper;
import com.hs.edu.app.wraper.WinnerWraper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author taxuefan
 * @Description //webscoket管理接口
 * @Date 16:44 2020/10/19
 * @Param
 * @return
 **/
public interface IWebSocketManager {

        /**
         * @Author taxuefan
         * @Description //TODO 实例化websocket管理器
         * @Date 9:16 2020/10/15
         * @Param []
         * @return com.hs.edu.app.cache.WebSocketManager
         **/
          static  com.hs.edu.app.cache.WebSocketManager getInstance(){
              return null;
          }
        /**
         * @Author taxuefan
         * @Description //获取谋个websocket
         * @Date 14:48 2020/10/13
         * @Param []
         * @return boolean
         **/
        public    WebSocketServer getRoomWebSocketServer(String roomId,String userId);
        /**
         * @Author taxuefan
         * @Description //TODO 加入新的websocket
         * @Date 17:30 2020/10/14
         * @Param [roomId]
         * @return void
         **/
        public  void addWebSocket(String roomId,String userId,WebSocketServer socketServer);
        /***
         * @Author f
         * @Description //是否包含websocket
         * @Date 17:45 2020/10/14
         * @Param [roomId, userId]
         * @return boolean
         **/
        public boolean containsWebSocket(String roomId,String userId);

        /**
         * @Author taxuefan
         * @Description todo:从缓存中移除房间内的某个roomId
         * @Date 23:44 2020/10/14
         * @Param [roomId, userId]
         * @return com.hs.edu.app.websocket.WebSocketServer
         **/
        public WebSocketServer removeWebsocket(String roomId,String userId);
        /**
         * @Author taxuefan
         * @Description todo:从缓存中移除房间内的某个roomId
         * @Date 23:44 2020/10/14
         * @Param [roomId, userId]
         * @return com.hs.edu.app.websocket.WebSocketServer
         **/
        public void removeRoomWebSocket(String roomId);
        /**
         * @Author taxuefan
         * @Description //判断房间是否存在
         * @Date 9:38 2020/10/15
         * @Param [roomId]
         * @return boolean
         **/
        public boolean isRoomExist(String roomId);
        /**
         * @Author taxuefan
         * @Description //发送加入房间数据包
         * @Date 17:08 2020/10/14
         * @Param [roomId]
         * @return void
         **/
        public void sendEnterRoomPacket(String roomId);
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
        public void sendGamingPacket(String roomId) ;

        /**
         * @Author taxuefan
         * @Description //游戏主动结束
         * @Date 17:18 2020/10/14
         * @Param [roomId]
         * @return void
         **/
        public void sendGameVotePacket(String roomId);
        /**
         * @Author taxuefan
         * @Description //游戏强制强束Servlet
         * @Date 17:18 2020/10/14
         * @Param [roomId]
         * @return void
         **/
        public void sendGameOverForcePacket(String roomId);
        /**
         * @Author taxuefan
         * @Description //TODO玩家退出游戏数据包
         * @Date 17:23 2020/10/14
         * @Param [roomId, userId]
         * @return void
         **/
        public  void sendGamerExitPacket(String roomId,String userId);
        /**
         * @Author taxuefan
         * @Description //TOD0发送房间关闭数据包
         * @Date 17:23 2020/10/14
         * @Param [roomId, userId]
         * @return void
         **/
        public  void sendCloseRoomPacket(String roomId,String userId);

        /**
         * @Author taxuefan
         * @Description //获取房间中的websocket
         * @Date 10:44 2020/10/15
         * @Param [roomId]
         * @return java.util.HashMap<java.lang.String,com.hs.edu.app.websocket.WebSocketServer>
         **/
        public Map<String,WebSocketServer> getRoomWebSockets(String roomId);
        /**
         * @Author taxuefan
         * @Description //获取房间中的websocket
         * @Date 10:44 2020/10/15
         * @Param [roomId]
         * @return java.util.HashMap<java.lang.String,com.hs.edu.app.websocket.WebSocketServer>
         **/
        public Map  getAllWebSocket();

}
