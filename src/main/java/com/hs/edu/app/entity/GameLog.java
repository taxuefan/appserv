package com.hs.edu.app.entity;

import com.hs.edu.db.annotation.FieldAttr;
import com.hs.edu.db.annotation.TableAttr;
import lombok.Data;

import java.util.List;

/**
 * @ClassName GameLog
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/19 16:59
 * @Version 1.0
 **/
@Data
@TableAttr(tableName = "app_game_result_log")
public class GameLog {
    @FieldAttr(fieldName = "seq_no")
    String seqNo;//流水号
    @FieldAttr(fieldName = "room_id")
    String roomId;//流水号
    @FieldAttr(fieldName = "dict_type")
    Integer dictType;
    @FieldAttr(fieldName = "winner_role")
    String winnerRole;
    @FieldAttr(fieldName = "host_user_id")
    String hostUserId;//谁是赢家
    @FieldAttr(fieldName = "create_time")
    Long createTime;//创建时间
    @FieldAttr(fieldName = "end_time")
    Long endTime;
    @FieldAttr(id=true,fieldName = "id")
    Long id;

}
