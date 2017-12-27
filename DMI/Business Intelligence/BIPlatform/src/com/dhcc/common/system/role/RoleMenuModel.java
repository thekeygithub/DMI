package com.dhcc.common.system.role;

/**
 * @author wwq 
 * 
 * 用来封装数据
 * 
 */
public class RoleMenuModel {

	private String id;
	private String name;
	private String sort;
	private String superior;
	private String flag;// 是否有该菜单的访问权限 1 是 2 否

	public RoleMenuModel() {
	}

	public RoleMenuModel(String id, String name, String sort, String superior,
			String flag) {
		this.id = id;
		this.name = name;
		this.sort = sort;
		this.superior = superior;
		this.flag = flag;
	}

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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSuperior() {
		return superior;
	}

	public void setSuperior(String superior) {
		this.superior = superior;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "title:"+name+"是 flag"+flag;
	}
   
	
	
	
	
}
