package com.hs.edu.db.util;

import com.hs.edu.db.config.DBType;

/**
 * @ClassName DBUtil
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/9/30 14:08
 * @Version 1.0
 **/
public class DBUtil {
    public static String getDBType(String url) {
        if (url.toLowerCase().indexOf(":oracle:") > 0) {
            return DBType.oracle.name();
        } else if (url.toLowerCase().indexOf(":mysql:") > 0) {
            return DBType.mysql.name();
        } else if (url.toLowerCase().indexOf(":db2:") > 0) {
            return DBType.db2.name();
        } else if (url.toLowerCase().indexOf(":odbc:") > 0) {
            return DBType.odbc.name();
        } else if (url.toLowerCase().indexOf(":postgresql:") > 0) {
            return DBType.postgresql.name();
        } else {
            return DBType.mysql.name();
        }
    }

}
