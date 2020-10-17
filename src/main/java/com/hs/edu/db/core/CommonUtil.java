package com.hs.edu.db.core;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.hs.edu.db.config.DataSourceConfig;

import javax.sql.DataSource;

public class CommonUtil {

	public static int objectToInteger(Object obj) {
		try {
			if (obj != null && !obj.toString().trim().equals(""))
				return Integer.parseInt(obj.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
		return 0;
	}

}
