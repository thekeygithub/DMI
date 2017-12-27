package com.dhcc.modal.system;

import java.util.List;
/**
 * @author gyr
 * 用于分页的实体类
 */
public class PageModel {
	private int perPage = 10; // 每页默认获取记录数
	private int totalRecord = 0; // 总记录数
	private int currentPage = 1; // 当前页号
	private int minNum = 0; // 当前页的第一条记录的记录号
	private int maxNum = 0; // 当前页的最后一条记录的记录号
	private int totalPage = 0; // 总页数
	private List list; // 查询结果集合

	public PageModel() {
		
	}

	public PageModel(int perPage, int totalRecord, int currentPage, int minNum,
			int maxNum, int totalPage, List list) {
		
		this.perPage = perPage;
		this.totalRecord = totalRecord;
		this.currentPage = currentPage;
		this.minNum = minNum;
		this.maxNum = maxNum;
		this.totalPage = totalPage;
		this.list = list;
	}
    
	
	/**
	 * get&&set方法
	 * @return
	 */
	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getMinNum() {
		return minNum;
	}

	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

}
