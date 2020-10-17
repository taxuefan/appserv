package com.hs.edu.app.service;

import com.hs.edu.api.entity.Result;
import com.hs.edu.app.entity.Gamer;
import com.hs.edu.app.entity.Room;

/**
 * @ClassName GameService
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/12 16:59
 * @Version 1.0
 **/
public interface GameService {
    /**
     * @Author taxuefan
     * @Description //获取房间号
     * @Date 17:17 2020/10/12
     * @Param [roomId]
     * @return com.hs.edu.app.entity.Room
     **/
    public Room getRoom(String roomId);
    /**
     * @Author taxuefan
     * @Description //获取RoomId
     * @Date 17:17 2020/10/12
     * @Param []
     * @return java.lang.String
     **/
    public String genRoomID();
    /**
     * @Author taxuefan
     * @Description //创建房间
     * @Date 17:17 2020/10/12
     * @Param [Room]
     * @return com.hs.edu.api.entity.Result
     **/
    public Result createRoom(Room Room);
    /**
     * @Author taxuefan
     * @Description //添加游戏
     * @Date 17:17 2020/10/12
     * @Param [roomId, gamer]
     * @return com.hs.edu.api.entity.Result
     **/
    public Result addGamer(String roomId,Gamer gamer);
    /**
     * @Author taxuefan
     * @Description //开始游戏
     * @Date 17:17 2020/10/12
     * @Param [roomId, seqNo, userId, playUserId]
     * @return com.hs.edu.api.entity.Result
     **/
    public Result startGame(String roomId,String userId );
    /**
     * @Author taxuefan
     * @Description //结束游戏
     * @Date 17:17 2020/10/12
     * @Param [roomId, seqNo, userId,isForce是否强制结束游戏]
     * @return com.hs.edu.api.entity.Result
     **/
    public Result finishGame(String roomId,String  seqNo,String userId,boolean isForce);
    /**
     * @Author taxuefan
     * @Description //
     * @Date 11:36 2020/10/14
     * @Param [roomId, userId, 退出你房间]
     * @return com.hs.edu.api.entity.Result
     **/
    public Result exitRoom(String roomId, String userId);
    /**
     * @Author taxuefan
     * @Description //退出游戏
     * @Date 17:17 2020/10/12
     * @Param [roomId, seqNo, userId]
     * @return com.hs.edu.api.entity.Result
     **/
    public Result weedUpGamer(String roomId,String  seqNo,String userId,String playUserId);
}
