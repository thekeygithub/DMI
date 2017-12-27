package com.dhcc.common.system.portal;


public class Tspanel {
	
	/**
	 * panel的标题
	 */
	private  String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * panel的url
	 */
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * panel的width
	 */
	private String width;
	
	public String getWidth() {
		this.width = "100%";
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	/**
	 * panel的高度
	 */
	private String height;

	public String getHeight() {
		
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
	
	
}
