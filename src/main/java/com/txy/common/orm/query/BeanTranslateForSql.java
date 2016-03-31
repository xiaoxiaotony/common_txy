package com.txy.common.orm.query;

import com.txy.common.orm.ObjectModel;
import com.txy.common.orm.query.impl.QueryParameter;

public interface BeanTranslateForSql
{
	//创建Select语句
	public String createSelectSql(QueryParameter queryParameter,ObjectModel model);
	//创建Insert语句
	public String createInsertSql(ObjectModel model);
}
