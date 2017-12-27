package com.dhcc.common.system.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于生成前台的json的model
 * @author GYR
 */
public class PortalModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<Tsportal> columns = new ArrayList<Tsportal>();

	public PortalModel() {
		super();
	}

	public PortalModel(List<Tsportal> columns) {
		super();
		this.columns = columns;
	}

	public List<Tsportal> getColumns() {
		return columns;
	}

	public void setColumns(List<Tsportal> columns) {
		this.columns = columns;
	}



}
