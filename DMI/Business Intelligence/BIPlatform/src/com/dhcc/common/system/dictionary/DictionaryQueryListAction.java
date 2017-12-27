package com.dhcc.common.system.dictionary;

import java.util.ArrayList;
import java.util.List;

import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tsdict;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 页面要显示的菜单列表查询
 * 
 * @author GYR
 * 
 */
public class DictionaryQueryListAction extends ActionSupport {

	private List<Tsdict> listmodel = new ArrayList<Tsdict>();// 前台的数据集合
	private Integer page; // 当前页码
	private Integer total; // 
	private Integer pagesize; // 当前页显示的数据行数
	private Integer record;// 当前数据条数
	private String sortname = "dtype,dkey";// 排序列名
	private String sortorder = "asc";// 排序方向 asc
	private String 	dtype;//查询条件 类别
	private String 	dvalue;//查询 值

	@Override
	public String execute() throws Exception {
		DictionaryDao dd = new DictionaryDao();
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = dd.dictQueryList(pm, sortname, sortorder, dtype, dvalue);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodel = pm.getList();
		return SUCCESS;
	}

	public List<Tsdict> getListmodel() {
		return listmodel;
	}

	public void setListmodel(List<Tsdict> listmodel) {
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

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public String getDvalue() {
		return dvalue;
	}

	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}

}
