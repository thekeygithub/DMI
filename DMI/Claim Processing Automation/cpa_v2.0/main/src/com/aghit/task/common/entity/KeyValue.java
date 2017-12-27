package com.aghit.task.common.entity;

/**
 * 字典表，键值对
 * @author Administrator
 *
 */
public class KeyValue {
	private int key;// 主键
	private String value;// 实际值

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
