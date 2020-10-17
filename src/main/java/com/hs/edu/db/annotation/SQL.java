package com.hs.edu.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * @Author taxuefan
 * @Description //后续再扩展
 * @Date 16:30 2020/10/10
 * @Param
 * @return
 **/
public @interface SQL {
    String value() default "";

}
