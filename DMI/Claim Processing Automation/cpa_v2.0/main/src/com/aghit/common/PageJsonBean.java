package com.aghit.common;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyUI DataGrid分页Bean
 * 
 * @param <T>
 * 
 */
public final class PageJsonBean<T> {

	private long total;// 总数
	private List<T> rows;// 数据

	public PageJsonBean() {
		super();
	}

	public PageJsonBean(long total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		if(this.rows == null) {
			rows = new ArrayList<T>(0);
		}
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}