package com.dhcc.ts.hzlb.model;

import java.util.Comparator;

public class HzlbModel implements Comparator<HzlbModel> {
	public int compare(HzlbModel obj1, HzlbModel obj2) {
		if (Integer.parseInt(obj1.getOrders())<Integer.parseInt(obj2.getOrders())) {  
		    return 1;  
		} else {  
		    return -1;  
		}
    }  
	private String id;
	private String nametrue;
	private String name;
	private String card_no;//身份证号
	private String sb_card_no;//社保卡号
	private String shijian;
	private String cblx;
	private String cbzt;
	private String orders;
	
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getSb_card_no() {
		return sb_card_no;
	}
	public void setSb_card_no(String sb_card_no) {
		this.sb_card_no = sb_card_no;
	}
	public String getNametrue() {
		return nametrue;
	}
	public void setNametrue(String nametrue) {
		this.nametrue = nametrue;
	}
	public String getShijian() {
		return shijian;
	}
	public void setShijian(String shijian) {
		this.shijian = shijian;
	}
	public String getCblx() {
		return cblx;
	}
	public void setCblx(String cblx) {
		this.cblx = cblx;
	}
	public String getCbzt() {
		return cbzt;
	}
	public void setCbzt(String cbzt) {
		this.cbzt = cbzt;
	}
}
