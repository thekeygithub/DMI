package com.dhcc.ts.bi;

import java.util.List;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年5月3日 上午9:50:35 
* @类说明：BI 数据类
*/
public class BiPieModel {

	private String id;
	private String name;
	private Double  y;
	private boolean sliced=false;
	private boolean selected=false;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public boolean isSliced() {
		return sliced;
	}
	public void setSliced(boolean sliced) {
		this.sliced = sliced;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
