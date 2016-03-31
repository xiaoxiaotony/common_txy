package com.txy.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class TreeInfoBean implements Serializable{
	public String id;
	public String text;
	public String state;
	public String parentId;
	public boolean checked;
	private List<TreeInfoBean> children=new ArrayList<TreeInfoBean>();
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public List<TreeInfoBean> getChildren() {
		return children;
	}
	public void setChildren(List<TreeInfoBean> children) {
		this.children = children;
	}
	public TreeInfoBean() {
	}

	
}
