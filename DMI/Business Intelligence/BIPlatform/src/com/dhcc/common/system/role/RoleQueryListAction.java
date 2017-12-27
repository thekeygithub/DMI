package com.dhcc.common.system.role;

import java.util.ArrayList;
import java.util.List;

import com.dhcc.modal.system.PageModel;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 前台系统角色列表查询
 * @author GYR
 * 2014-07-27
 */
public class RoleQueryListAction extends ActionSupport{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	private List listmodel = new ArrayList();// 前台的数据集合
	private Integer page; // 当前页码
	private Integer total; // 
	private Integer pagesize; // 当前页显示的数据行数
	private Integer record;// 当前数据条数
	private String 	rolename;//查询条件 角色名
	private String sortname = "rolename";// 排序列名
	private String sortorder = "asc";// 排序方向 asc

	@Override
	public String execute() throws Exception {
		RoleDao rd = new RoleDao();
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = rd.roleQueryList(pm, rolename, sortname, sortorder);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodel = pm.getList();
		return SUCCESS;
	}

	
	private String id;
	public String roleHaveUserList() throws Exception {
		RoleDao dao = new RoleDao();
		listmodel = dao.queryroleHaveUserList(id);
		return SUCCESS;
	}
	
	private String ids;
	public String roleDelUser(){
		RoleDao dao = new RoleDao();
		dao.roleDelUser(id,ids.split(","));
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public List getListmodel() {
		return listmodel;
	}
	@SuppressWarnings("unchecked")
	public void setListmodel(List listmodel) {
		this.listmodel = listmodel;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public Integer getRecord() {
		return record;
	}

	public void setRecord(Integer record) {
		this.record = record;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
