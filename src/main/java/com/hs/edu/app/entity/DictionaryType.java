package com.hs.edu.app.entity;

import com.hs.edu.db.annotation.FieldAttr;
import com.hs.edu.db.annotation.TableAttr;
import lombok.Data;
import java.io.Serializable;

/**
 * @ClassName DictionaryType
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/10 17:27
 * @Version 1.0
 **/
@Data
@TableAttr(tableName = "app_dictionary_type")
public class DictionaryType implements Serializable {
    @FieldAttr(id=true,fieldName ="type_id")
    int typeId;
    @FieldAttr(fieldName ="type_name")
    String typeName;

}
