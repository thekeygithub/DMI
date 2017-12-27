package com.aghit.task.algorithm.domain;

import java.util.List;

public abstract class AbstractKL {

	private long klId;// 知识ID
	private String autoName;// 自动标识名称
//	private String klType;// 知识类型
	private long checkType;// 审核项目类型
	private String[] checkItemCode;// 审核项目编码
	private String checkItemName;// 审核项目名称
	private int conditionType;// 条件项目类型
	private long relationType;// 关系类型
	private long relationAttr;// 关系属性
	
//	private List klContent;  //知识内容
//	
//	public List getKlContent() {
//		return klContent;
//	}
//	public void setKlContent(List klContent) {
//		this.klContent = klContent;
//	}
	
	public long getKlId() {
		return klId;
	}
	public void setKlId(long klId) {
		this.klId = klId;
	}
	public String getAutoName() {
		return autoName;
	}
	public void setAutoName(String autoName) {
		this.autoName = autoName;
	}
//	public String getKlType() {
//		return klType;
//	}
//	public void setKlType(String klType) {
//		this.klType = klType;
//	}
	public long getCheckType() {
		return checkType;
	}
	public void setCheckType(long checkType) {
		this.checkType = checkType;
	}
	public String[] getCheckItemCode() {
		return checkItemCode;
	}
	public void setCheckItemCode(String[] checkItemCode) {
		this.checkItemCode = checkItemCode;
	}
	public String getCheckItemName() {
		return checkItemName;
	}
	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}
	public int getConditionType() {
		return conditionType;
	}
	public void setConditionType(int conditionType) {
		this.conditionType = conditionType;
	}
	public long getRelationType() {
		return relationType;
	}
	public void setRelationType(long relationType) {
		this.relationType = relationType;
	}
	public long getRelationAttr() {
		return relationAttr;
	}
	public void setRelationAttr(long relationAttr) {
		this.relationAttr = relationAttr;
	}
}
