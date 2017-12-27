package com.framework.pager;

import java.util.List;

public class PaginationSupport<T> {
	
	/**
	 * 每页显示行数,当前默认30
	 */
	private int pageSize = PageConstants.PAGE_DEFAULT_SIZE;

	/**
	 * 总记录数
	 */
	private int totalCount = 0;

	/**
	 * 当前页数，默认第一页
	 */
	private int currentPage = 1;

	/**
	 * 开始记录数
	 */
	@SuppressWarnings("unused")
	private int startIndex = 0;

	/**
	 * 截止记录数
	 */
	@SuppressWarnings("unused")
	private int endIndex = 0;

	/**
	 * 总页数
	 */
	private int pageCount = 0;

	/**
	 * 当前页面记录集合
	 */
	private List<T> items;
	
	/**
	 * 初始化
	 */
	public PaginationSupport() {
	}

	/**
	 * @param items
	 * @param totalCount
	 * @param currentPage
	 */
	public PaginationSupport(List<T> items, int totalCount, int currentPage) {
		this.setPageInfo(items, totalCount, currentPage, this.getPageSize());
	}

	/**
	 * 分页信息
	 * 
	 * @param items 页面记录集合
	 * @param totalCount 总页数
	 * @param currentPage 当前页号
	 * @param pageSize 自定义每页显示的记录数
	 */
	public PaginationSupport(List<T> items, int totalCount, int currentPage, int pageSize) {
		this.setPageInfo(items, totalCount, currentPage, pageSize);
	}

	/**
	 * 设置分页信息
	 * @param items 页面记录集合
	 * @param totalCount 总页数
	 * @param currentPage 当前页号
	 * @param pageSize 自定义每页显示的记录数
	 */
	private void setPageInfo(List<T> items, int totalCount, int currentPage,
			int pageSize) {
		this.setPageSize(pageSize);
		this.setTotalCount(totalCount);
		this.setItems(items);
		this.setCurrentPage(currentPage);
	}

	public void setTotalCount(int totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;
			int count = totalCount / pageSize;
			if (totalCount % pageSize > 0)
				count++;
			setPageCount(count);
		} else {
			this.totalCount = 0;
			setPageCount(0);
		}
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public int getPageCount() {
		return pageCount;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getStartIndex() {
		return (this.currentPage - 1) * this.pageSize;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public int getEndIndex() {
		return this.currentPage * this.pageSize;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
}