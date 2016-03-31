package com.txy.common.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Table;

import com.txy.common.bean.FieldInfoBean;
import com.txy.common.util.StringUtils;

public class ObjectModel {

	private Map<String, FieldInfoBean> fieldsMap = new TreeMap<String, FieldInfoBean>();
	private Map<String, FieldInfoBean> columnMap = new HashMap<String, FieldInfoBean>(10);

	public Map<String, FieldInfoBean> getColumnMap() {
		return columnMap;
	}

	public void setColumnMap(Map<String, FieldInfoBean> columnMap) {
		this.columnMap = columnMap;
	}

	private Map<String, ObjectModel> objectModelsMap = new HashMap<String, ObjectModel>(5);
	private Class<?> type;
	private String tableName;
	private FieldInfoBean id;
	private List<Object> argsList = new ArrayList<Object>(5);
	private String className;
	private String asTable;
	// 是否进行表连接操作
	private boolean isJoinTable;

	public boolean isJoinTable() {
		return isJoinTable;
	}

	public void setJoinTable(boolean isJoinTable) {
		this.isJoinTable = isJoinTable;
	}

	public String getAsTable() {
		return asTable;
	}

	public String getClassName() {
		return className;
	}

	public synchronized ObjectModel addQueryParameter(Object value) {
		argsList.add(value);
		return this;
	}

	public List<Object> getValues() {
		return this.argsList;
	}

	public FieldInfoBean getId() {
		return id;
	}

	public void setId(FieldInfoBean id) {
		this.id = id;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, FieldInfoBean> getFieldsMap() {
		return fieldsMap;
	}

	public void setFieldsMap(Map<String, FieldInfoBean> fieldsMap) {
		this.fieldsMap = fieldsMap;
	}

	public Map<String, ObjectModel> getObjectModelsMap() {
		return objectModelsMap;
	}

	public void setObjectModelsMap(Map<String, ObjectModel> objectModelsMap) {
		this.objectModelsMap = objectModelsMap;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.tableName = type.getSimpleName();
		if (type.isAnnotationPresent(Table.class)) {
			this.tableName = type.getAnnotation(Table.class).name();
		}
		this.className = type.getSimpleName();
		this.asTable = this.className + "_" + StringUtils.getRandomAsTable();
		this.type = type;
	}

	public String getTableName() {
		return tableName;
	}
}
