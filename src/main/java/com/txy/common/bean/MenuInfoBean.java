package com.txy.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Table(name="T_SYSTEM_MENU")
public class MenuInfoBean implements Serializable{
	@Id
	@Column(name="MENU_ID")
	public String id;
	@Column(name="NAME")
	public String name;
	@Column(name="MENU_DESCRIPTION")
	public String description;
	@Column(name="TYPE")
	public String type;
	@Column(name="PARENT_ID")
	public String parentId;
	@Column(name="SORT")
	public int sort;
	@Column(name="ICON")
	public String icon;
	@Column(name="OPEN_ADDRESS")
	public String url;
	@Column(name="ENABLE")
	public int enable;
	@Column(name="LEAF")
	public int leaf;
	@Column(name="MENUCODE")
	public String btnCode;
	@Transient
	private List<MenuInfoBean> children=new ArrayList<MenuInfoBean>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public int getLeaf() {
		return leaf;
	}
	public void setLeaf(int leaf) {
		this.leaf = leaf;
	}
	public List<MenuInfoBean> getChildren() {
		return children;
	}
	public void setChildren(List<MenuInfoBean> children) {
		this.children = children;
	}
	public MenuInfoBean() {
	}
	public String getBtnCode() {
		return btnCode;
	}
	public void setBtnCode(String btnCode) {
		this.btnCode = btnCode;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}

}
