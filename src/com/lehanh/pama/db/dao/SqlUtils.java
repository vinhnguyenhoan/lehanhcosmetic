package com.lehanh.pama.db.dao;

import java.sql.Date;

public class SqlUtils {

	public static Date newSqlDate(java.util.Date date) {
		if (date == null) {
			return null;
		}
		return new Date(date.getTime());
	}
	
	
}
