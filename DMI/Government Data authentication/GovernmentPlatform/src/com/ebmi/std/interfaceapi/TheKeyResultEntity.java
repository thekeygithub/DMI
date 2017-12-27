package com.ebmi.std.interfaceapi;

import java.util.List;
import java.util.Map;

public class TheKeyResultEntity extends BaseEntity{
	private static final long serialVersionUID = 1331694057630097344L;
	
	
	private String status = "ok";
	  private String error = "";
	  private List<Map<String, String>> data;
	  private List<Map<String, String>> data0;
	  private List<Map<String, String>> data1;
	  private List<Map<String, String>> data2;
	  private List<Map<String, String>> data3;
	  
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<Map<String, String>> getData() {
		return data;
	}
	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}
	public List<Map<String, String>> getData0() {
		return data0;
	}
	public void setData0(List<Map<String, String>> data0) {
		this.data0 = data0;
	}
	public List<Map<String, String>> getData1() {
		return data1;
	}
	public void setData1(List<Map<String, String>> data1) {
		this.data1 = data1;
	}
	public List<Map<String, String>> getData2() {
		return data2;
	}
	public void setData2(List<Map<String, String>> data2) {
		this.data2 = data2;
	}
	public List<Map<String, String>> getData3() {
		return data3;
	}
	public void setData3(List<Map<String, String>> data3) {
		this.data3 = data3;
	}
	  
	  
	  
}
