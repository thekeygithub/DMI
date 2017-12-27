package com.dhcc.common.system.post;

import java.util.ArrayList;
import java.util.List;

import com.dhcc.modal.system.PageModel;
import com.opensymphony.xwork2.ActionSupport;



/**
 * 页面要显示的职务列表查询
 * @author GYR
 */
public class PostQueryListAction extends ActionSupport {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private List listmodal = new ArrayList();// 前台的数据集合
	private Integer page; // 当前页码
	private Integer total; // 
	private Integer pagesize; // 当前页显示的数据行数
	private Integer record;// 当前数据条数
	private String sortname = "postname";// 排序列名
	private String sortorder = "asc";// 排序方向 asc
	private String name;//模糊查询字段

	@Override
	public String execute() throws Exception {
		PostDao md = new PostDao();
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = md.postQueryList(pm,name,sortname,sortorder);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodal = pm.getList();
		return SUCCESS;
	}

	public List getListmodal() {
		return listmodal;
	}

	public void setListmodal(List listmodal) {
		this.listmodal = listmodal;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
