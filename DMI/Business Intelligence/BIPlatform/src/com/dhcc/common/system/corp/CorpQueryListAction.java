package com.dhcc.common.system.corp;

import java.util.ArrayList;
import java.util.List;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tscorp;
import com.opensymphony.xwork2.ActionSupport;



/**
 * @描述：页面要显示的单位信息列表查询
 * @作者：SZ
 * @时间：2014-10-14 上午09:55:23
 */
public class CorpQueryListAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Tscorp> listmodal = new ArrayList<Tscorp>();// 前台的数据集合
	private Integer page; // 当前页码
	private Integer total; // 
	private Integer pagesize; // 当前页显示的数据行数
	private Integer record;// 当前数据条数
	private String sortname = "corpname";// 排序列名
	private String sortorder = "asc";// 排序方向 asc
	
	@Override
	public String execute() throws Exception {
		CorpDao md = new CorpDao();
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = md.corpQueryList(pm,sortname,sortorder);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodal = pm.getList();
		return SUCCESS;
	}
	
	public String deptQueryList() throws Exception {
		CorpDao md = new CorpDao();
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = md.deptQueryList(pm,sortname,sortorder);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodal = pm.getList();
		return SUCCESS;
	}
	public List<Tscorp> getListmodal() {
		return listmodal;
	}

	public void setListmodal(List<Tscorp> listmodal) {
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

}
