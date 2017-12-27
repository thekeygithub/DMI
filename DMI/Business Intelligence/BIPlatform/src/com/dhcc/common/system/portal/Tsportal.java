package com.dhcc.common.system.portal;

import java.util.List;

import com.dhcc.common.system.portal.Tspanel;

public class Tsportal {
	
	
	/**
	 * 用户对应的门户布局
	 */
	
	private String rowIndex;

	public String getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}

	private String columnIndex;

	public String getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(String columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	private List<Tspanel> panels;

	public List<Tspanel> getPanels() {
		return panels;
	}

	public void setPanels(List<Tspanel> panels) {
		this.panels = panels;
	}
	
	private String width;
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
