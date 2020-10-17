package com.hs.edu.app.entity;

import com.hs.edu.db.annotation.FieldAttr;
import com.hs.edu.db.annotation.TableAttr;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Dictionary
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/10 17:27
 * @Version 1.0
 **/
@Data
@TableAttr(tableName = "app_game_dictionary")
public class Dictionary implements Serializable {
   @FieldAttr(id=true,fieldName = "id")
   Integer id;
   @FieldAttr(fieldName ="type_id")
   String typeId;
   @FieldAttr(fieldName ="wodi")
   String wodi;
   @FieldAttr(fieldName ="pingmin")
   String pingmin;
   @FieldAttr(fieldName ="create_time")
   Long createTime;
}
