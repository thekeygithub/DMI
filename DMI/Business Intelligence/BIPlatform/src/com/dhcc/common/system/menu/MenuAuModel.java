package com.dhcc.common.system.menu;


/**
 * 角色对应菜单权限的model
 */
public class MenuAuModel {
	private String id;
	private String title;
	private String pid;
	private String ordernum;
	private String flag;//0 没有 1 有
	private String btnStr;//按钮字符串 以逗号分隔
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getBtnStr() {
		return btnStr;
	}
	public void setBtnStr(String btnStr) {
		this.btnStr = btnStr;
	}

}
