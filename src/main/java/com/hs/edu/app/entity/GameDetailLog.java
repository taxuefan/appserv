package com.hs.edu.app.entity;

import com.hs.edu.db.annotation.FieldAttr;
import com.hs.edu.db.annotation.TableAttr;
import lombok.Data;

/**
 * @ClassName GameLog
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/19 16:59
 * @Version 1.0
 **/
@Data
@TableAttr(tableName = "app_game_result_log")
public class GameDetailLog {
    @FieldAttr(fieldName = "user_id")
    private String userId;       //用户ID，wx号
    @FieldAttr(fieldName = "nick_name")
    private String nickName;     //昵称
    @FieldAttr(fieldName = "avatar_url")
    private String avatarUrl;    //头像url地址
    @FieldAttr(fieldName = "word")
    private String word; //持有的字
    @FieldAttr(fieldName = "role")
    private String role;//角色
    @FieldAttr(fieldName = "seq_no")
    private String seqNo;//
    @FieldAttr(id=true,fieldName = "id")
    private Long id;//

}
