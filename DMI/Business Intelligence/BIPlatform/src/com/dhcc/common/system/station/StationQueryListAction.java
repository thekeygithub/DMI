package com.dhcc.common.system.station;

import java.util.ArrayList;
import java.util.List;

import com.dhcc.modal.system.PageModel;
import com.opensymphony.xwork2.ActionSupport;



/**
 * 页面要显示的岗位列表查询
 * @author GYR
 *
 */
public class StationQueryListAction extends ActionSupport {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	private List listmodal = new ArrayList();// 前台的数据集合
	private Integer page; // 当前页码
	private Integer total; // 
	private Integer pagesize; // 当前页显示的数据行数
	private Integer record;// 当前数据条数
	private String sortname = "stationname";// 排序列名
	private String sortorder = "asc";// 排序方向 asc


	@Override
	public String execute() throws Exception {
		StationDao md = new StationDao();
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = md.stationQueryList(pm, sortname, sortorder);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodal = pm.getList();
		return SUCCESS;
	}
	
	private String id;
	public String stationHaveUserList() throws Exception {
		StationDao dao = new StationDao();
		listmodal = dao.queryStationHaveUserList(id);
		return SUCCESS;
	}
	
	private String ids;
	public String stationDelUser(){
		StationDao dao = new StationDao();
		dao.stationDelUser(id,ids.split(","));
		return SUCCESS;
	}
	
	
	@SuppressWarnings("unchecked")
	public List getListmodal() {
		return listmodal;
	}
	@SuppressWarnings("unchecked")
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
