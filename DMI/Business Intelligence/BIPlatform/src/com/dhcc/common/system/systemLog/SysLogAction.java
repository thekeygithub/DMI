package com.dhcc.common.system.systemLog;

import java.util.ArrayList;
import java.util.List;

import com.dhcc.modal.system.PageModel;
import com.opensymphony.xwork2.ActionSupport;

public class SysLogAction extends ActionSupport {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private List listmodel = new ArrayList();
	private int page; // 当前页码
	private int total; // 
	private int pagesize; // 当前页显示的数据行数
	private int record;// 当前数据条数
	private String starttime;//查询条件 开始时间
	private String endtime;  //查询条件 结束时间
	private String username;//查询条件 用户姓名
	private String ids;//删除时用到的字段
	private String result;//执行操作的结果
	
	
	/**
	 * 查询系统日志列表
	 */
	public String sysLogQuery(){
		SysLogDao dao =  new SysLogDao();
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = dao.sysLogQueryList(pm, starttime, endtime,username);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodel = pm.getList();
		return SUCCESS;
	}

	/**
	 * 删除根据id
	 */
	public String sysLogDel() {
		SysLogDao dao =  new SysLogDao();
		if (ids != null && !ids.equals("")) {
			result = dao.sysLogDel(ids.split(","));
		}
		return SUCCESS;
	}
	
	public List getListmodel() {
		return listmodel;
	}


	public void setListmodel(List listmodel) {
		this.listmodel = listmodel;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public int getPagesize() {
		return pagesize;
	}


	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}


	public int getRecord() {
		return record;
	}


	public void setRecord(int record) {
		this.record = record;
	}


	public String getStarttime() {
		return starttime;
	}


	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}


	public String getEndtime() {
		return endtime;
	}


	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}



	
}
