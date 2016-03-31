package com.txy.common.orm.query;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataConver
{
	public  <T> T getValue(Class<T> cla,String columnLabel,ResultSet rs)throws SQLException;
}
