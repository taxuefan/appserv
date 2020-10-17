package com.hs.edu.app.model;

import com.hs.edu.db.annotation.FieldAttr;
import com.hs.edu.db.annotation.TableAttr;
import com.sun.tracing.dtrace.ArgsAttributes;
import lombok.*;

/**
 * @author www.java1234.com
 *
 */
@TableAttr(tableName = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@FieldAttr(id = true, fieldName = "id")
	private String id;
	@FieldAttr(fieldName = "user_name")
	private String userName;
	@FieldAttr(fieldName = "password")
	private String password;
}
