package com.hs.edu.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.hs.edu.api.app.dao.GameLogDao;
import com.hs.edu.api.app.dao.GameLogDetailDao;
import com.hs.edu.api.entity.Result;
import com.hs.edu.api.entity.ResultCode;
import com.hs.edu.app.cache.RoomCacheManager;
import com.hs.edu.app.entity.*;
import com.hs.edu.app.service.DictionaryService;
import com.hs.edu.app.service.GameService;
import com.hs.edu.app.util.AppUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName GameServiceImp
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/12 17:20
 * @Version 1.0
 **/
@Slf4j
public class GameServiceImpl implements GameService {
    private static ConcurrentHashMap<String,Room> roomManagerMap=new ConcurrentHashMap<String,Room>();
    private RoomCacheManager roomManager= RoomCacheManager.getInstance();//创建房间缓存
    private static ReentrantLock reentrantLock = new ReentrantLock();
    private DictionaryService dictService=new DictionaryServiceImp();
    private GameLogDao gameLogDao=new GameLogDao();
    private GameLogDetailDao gameLogDetalDao=new GameLogDetailDao();
    @Override
    public Room getRoom(String roomId) {
        return null;
    }

    @Override
    public String genRoomID() {
        return null;
    }
    @Override
    public Result createRoom(Room room) {
        log.debug("参数信息："+ JSONObject.toJSONString(room));
        Result result =new Result();
        if(room==null){
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg("参数对象不能为空");
            return result;
        }
        if(room.getDictType()==null){
            room.setDictType(0);
        }
        if(room.getWodiNum()==null||room.getWodiNum()==0||room.getPingminNum()==null||room.getPingminNum()==0){
            result.setCode(ResultCode.ROOM_GAMER_SETTING_ERROR.getCode());
            result.setMsg(ResultCode.ROOM_GAMER_SETTING_ERROR.getMsg());
            return result;
        }
        if(StrUtil.isEmpty(room.getHostUserId())){
            result.setCode(ResultCode.ROOM_NO_HOST.getCode());
            result.setMsg(ResultCode.ROOM_NO_HOST.getMsg());
            return result;
        }
        if(roomManager.isUserHasCreateRoom(room.getHostUserId())){
            result.setCode(ResultCode.APP_USER_HAS_CREATE_ROOM.getCode());
            result.setMsg(ResultCode.APP_USER_HAS_CREATE_ROOM.getMsg());
            return result;
        }
        if(roomManager.isFull()){
            result.setCode(ResultCode.APP_ROOM_FULL.getCode());
            result.setMsg(ResultCode.APP_ROOM_FULL.getMsg());
            return result;
        }
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        try{
            reentrantLock.lock();
            Room newRoom= ObjectUtil.clone(room);
            newRoom.initRoomSeat();//这个一定要初始， 者room用带4个参数构造方法创建
           //加缓存的时候需要加一个锁
            String roomId=genRandomRoomId();
            newRoom.setRoomId(roomId);
            roomManager.put(roomId,newRoom);//加入缓存中
            Map resultData=new HashMap<String,String>();
            resultData.put("roomId",roomId);
            result.setData(resultData);
        }catch (Exception e){
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg(e.getMessage());
        }finally {
            reentrantLock.unlock();//解锁
        }
        return result;
    }
    /**
     * @Author taxuefan
     * @Description //生成随机房间数
     * @Date 14:56 2020/10/13
     * @Param []
     * @return java.lang.String
     **/
    private  String genRandomRoomId(){
      String roomId= RandomUtil.randomNumbers(6);
      if(!roomManager.existRoom(roomId)){
          return roomId;
      }
      while (roomManager.existRoom(roomId)){
          log.info("loop get room num");
          roomId= RandomUtil.randomNumbers(6);
      }
      log.info("loop get room num:"+roomId);
      return roomId;
    }
    @Override
    public Result addGamer(String roomId, Gamer gamer) {
        Result result =new Result();
        if(StrUtil.isEmpty(roomId)){
            result.setCode(ResultCode.APP_ROOM_NO_ID.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_ID.getMsg());
            return result;
        }
        if(ObjectUtil.isEmpty(gamer)){
            result.setCode(ResultCode.PARAM_GAMMER_BLANK.getCode());
            result.setMsg(ResultCode.PARAM_GAMMER_BLANK.getMsg());
            return result;
        }
        if(ObjectUtil.isEmpty(roomManager.get(roomId))){
            result.setCode(ResultCode.APP_ROOM_NO_EXIST.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_EXIST.getMsg());
            return result;
        }
        if(StrUtil.isEmpty(gamer.getUserId())){
            result.setCode(ResultCode.APP_USER_NO_EXIT.getCode());
            result.setMsg(ResultCode.APP_USER_NO_EXIT.getMsg());
            return result;
        }
        Gamer newGamer= ObjectUtil.clone(gamer);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        newGamer.setStatus(0);
        try{
            reentrantLock.lock();//修订缓存的时候需要加一个锁
            Room room= roomManager.get(roomId);
            //房间人数已准备好，不能再添加游戏玩家了
            if(room.isReady()){
                result.setCode(ResultCode.APP_ROOM_USER_FULL.getCode());
                result.setMsg(ResultCode.APP_ROOM_USER_FULL.getMsg());
                return result;
            }
            //判断是否已加入房间
            if( room.isGamerExist(gamer.getUserId())){
                result.setCode(ResultCode.APP_USER_HAS_ENTER_ROOM.getCode());
                result.setMsg(ResultCode.APP_USER_HAS_ENTER_ROOM.getMsg());
                return result;
            }
            newGamer.setOnlineStatus(1);
                room.fillSeat(newGamer);
            Map resultData=new HashMap<String,String>();
            resultData.put("wodiNum",room.getWodiNum());
            resultData.put("pingminNum",room.getPingminNum());
            resultData.put("baibanNum",room.getBaibanNum());
            resultData.put("hostUserId",room.getHostUserId());
            result.setData(resultData);
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg(e.getMessage());
        }finally {
            reentrantLock.unlock();//解锁
        }
        return result;

    }
    @Override
    public Result startGame(String roomId, String userId) {
        Result result =new Result();
        if(StrUtil.isEmpty(roomId)){
            result.setCode(ResultCode.APP_ROOM_NO_ID.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_ID.getMsg());
            return result;
        }

        Room room= roomManager.get(roomId);
        if(ObjectUtil.isEmpty(room)){
            result.setCode(ResultCode.APP_ROOM_NO_EXIST.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_EXIST.getMsg());
            return result;
        }
        if(!room.getHostUserId().equals(userId)){
            result.setCode(ResultCode.APP_NO_HOST_USER.getCode());
            result.setMsg(ResultCode.APP_NO_HOST_USER.getMsg());
            return result;
        }
        //玩家未加满
        if(!room.isReady()){
            result.setCode(ResultCode.APP_ROOM_NOT_READY.getCode());
            result.setMsg(ResultCode.APP_ROOM_NOT_READY.getMsg());
            return result;
        }
        String seqNo= AppUtils.genGameSeqNo();
        Result dictResult  =dictService.getRandomDictinary(room.getDictType());
        if(dictResult.getCode()!=0){
            return dictResult;
        }
        Dictionary dictionary=(Dictionary)dictResult.getData();
        if(dictionary==null||StrUtil.isEmpty(dictionary.getPingmin())||StrUtil.isEmpty(dictionary.getWodi())){
            result.setCode(ResultCode.APP_NO_DICT.getCode());
            result.setMsg(ResultCode.APP_NO_DICT.getMsg());
            return result;
        }
        log.info("dictionary:",dictionary);
        try{
            reentrantLock.lock();//修订缓存的时候需要加一个锁
            room.copyAndUpdateStatusForGameStart(seqNo,dictionary);//游戏开始更新游戏的状态
            AppUtils.randomGameWord(room,dictionary);
            //当前游戏中的卧底，平民词
            room.getGame().setPingmin(dictionary.getPingmin());
            room.getGame().setWodi(dictionary.getWodi());
            room.getGame().setCreateTime(System.currentTimeMillis());
            Map resultData=new HashMap<String,String>();
            resultData.put("seqNo",seqNo);
            result.setData(resultData);
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg(e.getMessage());
        }finally {
            reentrantLock.unlock();//解锁
        }
        return result;

    }
    /**
     * @Author taxuefan
     * @Description //淘汰玩家服务
     * @Date 10:59 2020/10/14
     * @Param [roomId, seqNo, userId, playUserId]
     * @return com.hs.edu.api.entity.Result
     **/
    public Result weedUpGamer(String roomId,String  seqNo,String userId,String playUserId){
        Result result =new Result();
        if(StrUtil.isEmpty(roomId)){
            result.setCode(ResultCode.APP_ROOM_NO_ID.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_ID.getMsg());
            return result;
        }
        if(StrUtil.isEmpty(userId)){
            result.setCode(ResultCode.APP_HOST_EMPTY.getCode());
            result.setMsg(ResultCode.APP_HOST_EMPTY.getMsg());
            return result;
        }
        Room room= roomManager.get(roomId);
        if(ObjectUtil.isEmpty(room)){
            result.setCode(ResultCode.APP_ROOM_NO_EXIST.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_EXIST.getMsg());
            return result;
        }
        if(!room.getHostUserId().equals(userId)){
            result.setCode(ResultCode.APP_NO_HOST_USER.getCode());
            result.setMsg(ResultCode.APP_NO_HOST_USER.getMsg());
            return result;
        }
        //游戏不存在
        if(!room.isGaming()){
            result.setCode(ResultCode.APP_ROOM_GAME_NOT_START.getCode());
            result.setMsg(ResultCode.APP_ROOM_GAME_NOT_START.getMsg());
            return result;
        }
        //游戏序列号有误
        if(!room.getGame().getSeqNo().equals(seqNo)){
            result.setCode(ResultCode.APP_ROOM_GAME_NO_RROR.getCode());
            result.setMsg(ResultCode.APP_ROOM_GAME_NO_RROR.getMsg());
            return result;
        }
        try{
            reentrantLock.lock();//修订缓存的时候需要加一个锁
            room.updateGamerGamingStatus(playUserId,null,1);
            room.updateGamerStatus(playUserId,null,1);
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMsg(ResultCode.SUCCESS.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg(e.getMessage());
        }finally {
            reentrantLock.unlock();//解锁
        }
        return result;
    }
    @Override
    public Result finishGame(String roomId,String  seqNo,String userId,boolean isForce) {
        Result result =new Result();
        if(StrUtil.isEmpty(roomId)){
            result.setCode(ResultCode.APP_ROOM_NO_ID.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_ID.getMsg());
            return result;
        }
        if(StrUtil.isEmpty(userId)){
            result.setCode(ResultCode.APP_HOST_EMPTY.getCode());
            result.setMsg(ResultCode.APP_HOST_EMPTY.getMsg());
            return result;
        }
        Room room= roomManager.get(roomId);
        if(ObjectUtil.isEmpty(room)){
            result.setCode(ResultCode.APP_ROOM_NO_EXIST.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_EXIST.getMsg());
            return result;
        }
        if(!room.getHostUserId().equals(userId)){
            result.setCode(ResultCode.APP_NO_HOST_USER.getCode());
            result.setMsg(ResultCode.APP_NO_HOST_USER.getMsg());
            return result;
        }
        //游戏
        if(!room.isGaming()){
            result.setCode(ResultCode.APP_ROOM_GAME_NOT_START.getCode());
            result.setMsg(ResultCode.APP_ROOM_GAME_NOT_START.getMsg());
            return result;
        }
        //游戏序列号有误
        if(!room.getGame().getSeqNo().equals(seqNo)){
            result.setCode(ResultCode.APP_ROOM_GAME_NO_RROR.getCode());
            result.setMsg(ResultCode.APP_ROOM_GAME_NO_RROR.getMsg());
            return result;
        }
        try{
            reentrantLock.lock();//修订缓存的时候需要加一个锁
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMsg(ResultCode.SUCCESS.getMsg());
            room.finishGame();//结束游戏
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg(e.getMessage());
        }finally {
            reentrantLock.unlock();//解锁
        }
        return result;
    }


    /**
     * @Author taxuefan
     * @Description //玩家退出游戏
     * @Date 11:33 2020/10/14
     * @Param [roomId, seqNo, userId]
     * @return com.hs.edu.api.entity.Result
     **/
    @Override
    public Result exitRoom(String roomId, String userId) {
        Result result =new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        if(StrUtil.isEmpty(roomId)){
            result.setCode(ResultCode.APP_ROOM_NO_ID.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_ID.getMsg());
            return result;
        }
        if(StrUtil.isEmpty(userId)){
            result.setCode(ResultCode.APP_HOST_EMPTY.getCode());
            result.setMsg(ResultCode.APP_HOST_EMPTY.getMsg());
            return result;
        }
        Room room= roomManager.get(roomId);
        if(ObjectUtil.isEmpty(room)){
            result.setCode(ResultCode.APP_ROOM_NO_EXIST.getCode());
            result.setMsg(ResultCode.APP_ROOM_NO_EXIST.getMsg());
            return result;
        }
        try{
            reentrantLock.lock();//修订缓存的时候需要加一个锁
            room.finishGame();
            room.clearGamer();
            roomManager.remove(roomId);
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResultCode.FAILED.getCode());
            result.setMsg(e.getMessage());
        }finally {
            reentrantLock.unlock();//解锁
        }
        return result;
    }

    @Override
    public int addGameLog(GameLog gameLog) {
        try {
           return gameLogDao.add(gameLog);
        }catch (Exception e){
          log.error(e.getMessage());
          return -1;
        }

    }

    @Override
    public int addGameDetailLog(GameDetailLog gameDetailLog) {
        try {
            return gameLogDetalDao.add(gameDetailLog);
        }catch (Exception e){
            log.error(e.getMessage());
            return -1;
        }
    }
}
