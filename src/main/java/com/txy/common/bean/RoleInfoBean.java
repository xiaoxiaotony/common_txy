package com.txy.common.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@SuppressWarnings("serial")
@Table(name="T_SYSTEM_GROUP")
public class RoleInfoBean implements Serializable{
	@Id
	@Column(name="GROUPID")
	private String roleId;
	@Column(name="GROUP_NAME")
	private String groupName;
	@Column(name="GROUP_DESCRIPTION")
	private String description;
	@Column(name="ADD_USER")
	private String addUser;
	@Column(name="ADD_DATE")
	private Date addTime;
	@Column(name="UPDATE_USER")
	private String updateUser;
	@Column(name="UPDATE_DATE")
	private Date updateTime;
	@Column(name="STATUS")
	private int status;
	
	public String getRoleId()
	{
		return roleId;
	}
	
	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}

	public String getGroupName()
	{
		return groupName;
	}
	
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getAddUser()
	{
		return addUser;
	}
	
	public void setAddUser(String addUser)
	{
		this.addUser = addUser;
	}
	
	public Date getAddTime()
	{
		return addTime;
	}
	
	public void setAddTime(Date addTime)
	{
		this.addTime = addTime;
	}
	
	public String getUpdateUser()
	{
		return updateUser;
	}
	
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}
	
	public Date getUpdateTime()
	{
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
}
