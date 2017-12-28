package com.ts.entity.mts;

/**
 * 
 * @类名称: MtsDataType
 * @类描述: 数据标化类型
 * @作者:李巍
 * @创建时间:2016年10月13日 上午11:09:07
 */
public class MtsDataType {
	// 数据标化类型ID
	private String DATA_TYPE_ID;
	// 数据标化类型名称
	private String DATA_TYPE_NAME;
	// 数据标化类型编码
	private String MEM_DATA_CODE;
	// 数据标化类型版本
	private String DATA_VER;

	public String getDATA_TYPE_ID() {

		return DATA_TYPE_ID;
	}

	public void setDATA_TYPE_ID(String dATA_TYPE_ID) {

		DATA_TYPE_ID = dATA_TYPE_ID;
	}

	public String getDATA_TYPE_NAME() {

		return DATA_TYPE_NAME;
	}

	public void setDATA_TYPE_NAME(String dATA_TYPE_NAME) {

		DATA_TYPE_NAME = dATA_TYPE_NAME;
	}

	public String getMEM_DATA_CODE() {

		return MEM_DATA_CODE;
	}

	public void setMEM_DATA_CODE(String mEM_DATA_CODE) {

		MEM_DATA_CODE = mEM_DATA_CODE;
	}

	public String getDATA_VER() {

		return DATA_VER;
	}

	public void setDATA_VER(String dATA_VER) {

		DATA_VER = dATA_VER;
	}

}
