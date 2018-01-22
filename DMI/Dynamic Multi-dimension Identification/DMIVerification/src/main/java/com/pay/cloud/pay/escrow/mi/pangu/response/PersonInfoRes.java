package com.pay.cloud.pay.escrow.mi.pangu.response;

/**
 * 个人信息结果
 * @author yanjie.ji
 * @date 2016年11月20日 
 * @time 上午11:26:50
 */
public class PersonInfoRes extends BaseResponse {

	private String cardID;
	private String personID;		 
	private String unitCode;
	private String unitName;
	private String perCardID;
	private String sex;
	private String nation;
	private String birthday;
	private String yldyType;
	private String sbjgCode;
	private String balance;
	private String perName;
	private String cycid;
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getPerCardID() {
		return perCardID;
	}
	public void setPerCardID(String perCardID) {
		this.perCardID = perCardID;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getYldyType() {
		return yldyType;
	}
	public void setYldyType(String yldyType) {
		this.yldyType = yldyType;
	}
	public String getSbjgCode() {
		return sbjgCode;
	}
	public void setSbjgCode(String sbjgCode) {
		this.sbjgCode = sbjgCode;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPerName() {
		return perName;
	}
	public void setPerName(String perName) {
		this.perName = perName;
	}
	public String getCycid() {
		return cycid;
	}
	public void setCycid(String cycid) {
		this.cycid = cycid;
	}
}
