package com.hs.edu.app.entity;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hs.edu.api.entity.ResultCode;
import com.hs.edu.app.exception.RoomSeatNoInitException;
import com.hs.edu.app.util.AppUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Room
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/11 17:13
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@Slf4j
public class Room implements Serializable {
    private String roomId; //房间号
    private List<Gamer> gamersList = null;//用户列表
    private Game game;//游戏值
    private Integer wodiNum;//卧底人数
    private Integer pingminNum;//平民人数据
    private Integer baibanNum; //白板人数
    private Integer gamerNum;//玩家人员
    private String hostUserId;//主机id类型
    private Integer dictType; //词汇类型

    public Room(Integer wodiNum, Integer pingminNum, Integer baibanNum, String hostUserId) {
        this.wodiNum = wodiNum;
        this.pingminNum = pingminNum;
        this.baibanNum = baibanNum;
        this.hostUserId = hostUserId;
        this.initRoomSeat();
    }

    /**
     * @return void
     * @Author taxuefan
     * @Description //初始化房间位置
     * @Date 19:22 2020/10/13
     * @Param []
     **/
    public void initRoomSeat() {
        if (this.gamersList != null) {
            return;
        }
        this.gamerNum = getGamerNum();
        System.out.println(this.gamerNum);

        if (this.gamerNum == null) {
            this.gamerNum = Integer.valueOf(0);
        }
        this.gamersList = new ArrayList<Gamer>();
        for (int i = 0; i < this.gamerNum.intValue(); i++)
            this.gamersList.add(new Gamer());
    }


    /**
     * @return java.lang.Integer
     * @Author taxuefan
     * @Description //TODO 获取玩家人数
     * @Date 19:32 2020/10/11
     * @Param []
     **/
    public Integer getGamerNum() {
        return (pingminNum == null ? 0 : pingminNum) + (baibanNum == null ? 0 : baibanNum) + (wodiNum == null ? 0 : wodiNum);

    }

    /**
     * @return com.hs.edu.app.entity.Gamer
     * @Author chu
     * @Description //TODO查找游戏玩家
     * @Date 19:43 2020/10/11
     * @Param [userId]
     **/
    public Gamer findGamer(String userId) {
        Gamer returnGamer = null;
        if (this.gamersList == null || this.gamersList.size() <= 0) {
            return null;
        }
        for (Gamer gamer : this.gamersList) {
            if (gamer.getUserId() == null) {
                continue;
            }
            if (gamer.getUserId().equals(userId)) {
                returnGamer = gamer;
                break;
            }
        }
        return returnGamer;
    }

    /**
     * @return com.hs.edu.app.entity.Gamer
     * @Author chu
     * @Description //TODO查找游戏中的玩家状态
     * @Date 19:43 2020/10/11
     * @Param [userId]
     **/
    public Gamer findGamingGamer(String userId) {
        Gamer returnGamer = null;
        if (!this.isGaming()) {
            return null;
        }
        List<Gamer> gamingGamers = this.getGame().getGamersList();
        if (gamingGamers == null) {
            return null;
        }
        for (Gamer gamer : gamingGamers) {
            if (gamer.getUserId() == null) {
                continue;
            }
            if (gamer.getUserId().equals(userId)) {
                returnGamer = gamer;
                break;
            }
        }
        return returnGamer;
    }

    public void fillSeat(Gamer gamer) throws Exception {
        if ((this.gamersList == null) || (this.gamersList.size() < getGamerNum().intValue())) {
            throw new RoomSeatNoInitException(ResultCode.APP_GAMER_SEAT_NO_INIT.getCode(), ResultCode.APP_GAMER_SEAT_NO_INIT.getMsg());
        }
        if (isGamerExist(gamer.getUserId())) {
            return;
        }
        if (this.gamersList.size() == getGamerNum().intValue()) {
            int pos = getEmptySeatIndex();
            this.gamersList.set(pos, gamer);
        }
    }

    public int getEmptySeatIndex() {
        if ((this.gamersList == null) || (this.gamersList.size() <= 0)) {
            return 0;
        }
        for (int i = 0; i < this.gamersList.size(); i++) {
            Gamer gamer = (Gamer) this.gamersList.get(i);
            if (StrUtil.isEmpty(gamer.getUserId())) {
                return i;
            }
        }
        return this.gamersList.size();
    }
    /**
     * @Author taxuefan
     * @Description //查打gamer的位置
     * @Date 15:15 2020/10/15
     * @Param []
     * @return int
     **/
    public int  findGamerPostion(String userId) {
        if ((this.gamersList == null) || (this.gamersList.size() <= 0)) {
            return 0;
        }
        int pos=0;
        for (int i = 0; i < this.gamersList.size(); i++) {
            Gamer gamer =   this.gamersList.get(i);
            if(StrUtil.isEmpty(gamer.getUserId())){
                continue;
            }
            if(gamer.getUserId().equals(userId)){
                pos=i;
            }
        }
        return pos;
    }
    /**
     * @return boolean
     * @Author chu
     * @Description //判断是否在游戏
     * @Date 19:45 2020/10/11
     * @Param []
     **/
    public boolean isGaming() {
        return game != null && getGame().getStatus() == 1;
    }

    /**
     * @return void
     * @Author 移除游戏玩家
     * @Description //TODO
     * @Date 19:49 2020/10/11
     * @Param [userId]
     **/
    public void removeGamer(String userId) {
        if (this.gamersList == null) {
            return;
        }
        Gamer gamer = this.findGamer(userId);
        if (gamer != null) {
            this.gamersList.remove(gamer);
        }
    }

    /**
     * @return boolean
     * @Author taxuefan
     * @Description //是否已有玩家
     * @Date 19:26 2020/10/13
     * @Param [userId]
     **/
    public boolean isGamerExist(String userId) {
        return this.findGamer(userId) != null;
    }

    public boolean isHost(String userId) {
        return this.hostUserId.equals(userId);
    }

    /**
     * @return void
     * @Author taxuefan
     * @Description //TODO
     * @Date 19:56 2020/10/11
     * @Param []
     **/
    public void clearGamer() {
        if (this.gamersList == null) {
            return;
        }
        this.gamersList.clear();
    }

    /**
     * @return void
     * @Author taxuefan
     * @Description //TODO
     * @Date 19:56 2020/10/11
     * @Param []
     **/
    public void removeGame() {
        if (this.game != null) {
            this.game.clearGamer();
            this.game = null;
        }
    }

    /**
     * @return boolean
     * @Author taxuefan
     * @Description //房间是否准备好了
     * @Date 20:31 2020/10/13
     * @Param []
     **/
    public boolean isReady() {
        if(this.gamersList==null){
            return false;
        }
        for (Gamer gamer : this.gamersList) {
            if (gamer.getUserId() == null || gamer.getOnlineStatus() != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return java.util.List<com.hs.edu.app.entity.Gamer>
     * @Author 游戏开始
     * @Description //TODO 房间会添加游戏对象，并更新游戏者的状态
     * @Date 23:48 2020/10/13
     * @Param []
     **/
    public void copyAndUpdateStatusForGameStart(String seqNo, Dictionary dict) {
        List<Gamer> list = new ArrayList<Gamer>();
        if (this.getGame() != null) {
            this.getGame().clearGamer();
            this.game = null;
        }
        this.game = new Game();
        for (Gamer gamer : this.gamersList) {
            gamer.setStatus(0);
            gamer.setOnlineStatus(1);
            Gamer newGamer = ObjectUtil.clone(gamer);
            list.add(newGamer);
        }
        game.setSeqNo(seqNo);
        game.setDictType(this.getDictType());
        game.setGamersList(list);
        game.setStatus(1);
    }

    /**
     * @return void
     * @Author taxuefan
     * @Description //更新网家游戏的状态，包括游戏中的玩家，包括房间中的玩家
     * @Date 10:45 2020/10/14
     * @Param []
     **/
    public void updateGamerGamingStatus(String userId, Integer onlineStatus, Integer status) {

        //游戏房间中的玩家
        Gamer gamingGamer=this.findGamingGamer(userId);
        if(gamingGamer==null){
            return;
        }
        if (onlineStatus != null) {
            gamingGamer.setOnlineStatus(onlineStatus);
        }
        if (status != null) {
            gamingGamer.setStatus(status);
        }
    }
    public void updateGamerStatus(String userId, Integer onlineStatus, Integer status){
        //房间中玩家
        Gamer gamer = this.findGamer(userId);
        if(gamer==null){
            return;
        }
        if (onlineStatus != null) {
            gamer.setOnlineStatus(onlineStatus);
        }
        if (status != null) {
            gamer.setStatus(status);         ;
        }
    }
    /**
     * @Author taxuefan
     * @Description //更新所有房间中玩家状态
     * @Date 11:09 2020/10/14
     * @Param [onlineStatus, status]
     * @return void
     **/
    public void initAllGamerStatus(Integer onlineStatus, Integer status){
        if(this.gamersList==null||this.gamersList.size()==0){
            return;
        }
        for(Gamer gamer:gamersList){
            if(onlineStatus!=null){
                gamer.setOnlineStatus(onlineStatus);
            }
            if(status!=null){
                gamer.setStatus(status);
            }
        }
    }
    /**
     * @Author taxuefan
     * @Description //更新所有房间中玩家状态
     * @Date 11:09 2020/10/14
     * @Param [onlineStatus, status]
     * @return void
     **/
    public void finishGame(){
        this.removeGame();
        initAllGamerStatus(null,0);
    }
}
