package com.txy.common.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {
	
	public static final String METHOD = "method";
	
	public enum DATA_FORMAT {json,xml};
	
	// 数据类型，取值为json|xml
	public static final String RETURN_DATA_TYPE = "dataType";
	
	public static final String JSON_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	
	public  enum STATUS_TYPE
	{
		N,Y,Z
	};
	
	public static final String GRID_PARAM_KEY = "param";
	
	//系统配置项数据
	public static final Map<String, String> CONFIG_MAP = new HashMap<String, String>();
	
	//系统所有按钮和url权限数据
	public static final Map<String, Object> SYSTEM_BUTTON_MAP = new HashMap<String, Object>();
	
	//权限url集合
	public static final List<String> SYSTEM_URL_LIST = new ArrayList<String>();
	
	//系统所有按钮和url权限数据
	public static final Map<String, Object> SYS_MODEL_MAP = new HashMap<String, Object>();
	
	//系统模型属性数据 key: modelCode; value: List<FieldInfo>
	public static final Map<String, Object> MODEL_FIELD_INFO_MAP = new HashMap<String, Object>();
	
	//邮件协议
	public static final String MAIL_SMTP = "mail.smtp.auth";
	
	public static final Map<String, Object> propertiesMap = new HashMap<String, Object>();
	
	
	public static final String COMMON_SEQ = "WMP_COMMON_SEQ.nextval"; 
	
}
