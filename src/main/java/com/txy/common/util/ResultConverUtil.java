package com.txy.common.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 数据库返回数据取值转换类
 * @author fei
 *
 */
public class ResultConverUtil {
	public static Object getValue(Class<?> cla, String columnLabel, ResultSet rs) throws SQLException {
		String name = cla.getSimpleName();
		Object value = null;
		if ("int".equals(name) || "Integer".equals(name)) {
			value = getInt(columnLabel, rs);

		} else if ("float".equals(name) || "Float".equals(name)) {
			value = getFloat(columnLabel, rs);

		} else if ("long".equals(name) || "Long".equals(name)) {
			value = getLong(columnLabel, rs);
		} else if ("Date".equals(name)) {
			Timestamp timestamp = rs.getTimestamp(columnLabel);
			Date date = new Date();
			if (timestamp != null) {
				date.setTime(timestamp.getTime());
				value = date;
			}
		} else {
			value = rs.getObject(columnLabel);
		}

		return value;
	}

	private static Integer getInt(String column, ResultSet rs) throws SQLException {
		BigDecimal bigDecimal = rs.getBigDecimal(column);
		if (bigDecimal != null) {
			return bigDecimal.intValue();
		}
		return null;

	}

	private static Long getLong(String column, ResultSet rs) throws SQLException {
		BigDecimal bigDecimal = rs.getBigDecimal(column);
		if (bigDecimal != null) {
			return bigDecimal.longValue();
		}
		return null;
	}

	private static Float getFloat(String column, ResultSet rs) throws SQLException {
		BigDecimal bigDecimal = rs.getBigDecimal(column);
		if (bigDecimal != null) {
			return bigDecimal.floatValue();
		}
		return null;
	}
}
