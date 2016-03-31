package com.txy.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@SuppressWarnings("serial")
@Table(name="WMP_STATION_DIC")
public class AreaInfoBean implements Serializable{
	@Id
	@Column(name="MUNCPL_ID")
	private String id;
	@Column(name="MUNCPL")
	private String text;
	@Column(name="CODE")
	private String code;
	@Column(name="PARENTID")
	private String parentId;
	@Column(name="ENABLE")
	private int enable;
	
	private List<AreaInfoBean> children=new ArrayList<AreaInfoBean>();
	
	public String state;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<AreaInfoBean> getChildren() {
		return children;
	}
	public void setChildren(List<AreaInfoBean> children) {
		this.children = children;
	}
	
	

}
