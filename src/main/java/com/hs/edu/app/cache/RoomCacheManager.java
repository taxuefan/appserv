package com.hs.edu.app.cache;

import cn.hutool.core.util.StrUtil;
import com.hs.edu.app.api.config.AppConfig;
import com.hs.edu.app.entity.Room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName RoomCache
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/13 14:22
 * @Version 1.0
 **/
public class RoomCacheManager {
    private static Map<String,Room> roomCacheMap=new ConcurrentHashMap<String,Room>();
    private   Integer MAX_ROOM_SIZE = null;
    public  static RoomCacheManager getInstance() {
        return  RoomFactory.ROOM_CACHE;
    }

    public  RoomCacheManager(){
        this.MAX_ROOM_SIZE= Integer.valueOf(AppConfig.getInstance().getConfig("MAX_ROOM_SIZE"));
        System.out.println(this.MAX_ROOM_SIZE);
    }
    /**
     * @Author taxuefan
     * @Description //缓存管理工厂
     * @Date 21:22 2020/10/12
     * @Param
     * @return
     **/
    private static class RoomFactory {
        private static final RoomCacheManager ROOM_CACHE = new RoomCacheManager();
    }
    /**
     * @Author taxuefan
     * @Description //判断房间是否满了。
     * @Date 14:48 2020/10/13
     * @Param []
     * @return boolean
     **/
    public     boolean isFull(){
       return roomCacheMap.size()==MAX_ROOM_SIZE;
    }
    /**
     * @Author taxuefan
     * @Description //判断是否包含房间。
     * @Date 14:48 2020/10/13
     * @Param []
     * @return boolean
     **/
    public   boolean existRoom(String roomId){
        return roomCacheMap.containsKey(roomId);
    }
    /**
     * @Author taxuefan
     * @Description //添加房间
     * @Date 14:53 2020/10/13
     * @Param [key, room]
     * @return void
     **/
    public   void put(String key, Room room){
       if (roomCacheMap.size() >MAX_ROOM_SIZE) {
           return;
        }
        if(roomCacheMap.containsKey(key)){
            return;
        }
        //删除使用记录
        roomCacheMap.put(key,room);        //新增使用记录到首位
    }
    /**
     * @Author taxuefan
     * @Description //TODO删除
     * @Date 14:46 2020/10/13
     * @Param [key]
     * @return com.hs.edu.app.entity.Room
     **/
    public   Room get(String key){
       return roomCacheMap.get(key);
    }
    public Map getAllRoomInfo(){
        return this.roomCacheMap;
    }
    /**
     * @Author taxuefan
     * @Description //移除房间
     * @Date 14:46 2020/10/13
     * @Param [key]
     * @return com.hs.edu.app.entity.Room
     **/
    public   Room remove(String key){
         return  roomCacheMap.remove(key);
    }
    /**
     * @Author taxuefan
     * @Description //判断用户是否已创建房间
     * @Date 9:14 2020/10/16
     * @Param [userId]
     * @return com.hs.edu.app.entity.Room
     **/
    public   boolean isUserHasCreateRoom(String userId){
         for(Map.Entry<String,Room> entry :roomCacheMap.entrySet()){
               Room room=entry.getValue();

               if(StrUtil.isEmpty(room.getHostUserId())){
                   continue;
               }
               if(room.getHostUserId().equals(userId)){
                   return true;
               }
         }
         return false;
    }

    public static void main(String args[]){
        RoomCacheManager cacheManager=RoomCacheManager.getInstance();

    }
}
