package com.ts.entity.mts;

/**
 * 工具实体
 * @ClassName:MtsToolkitT
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年12月4日上午10:11:29
 */
public class MtsToolkitT 
{
    /* 工具包ID */
	private String TOOLKIT_ID;
	
	/*工具包名称*/
	private String TOOLKIT_NAME;
	
	/*备注*/
	private String COMMENTS;
	
	/*程序的名称*/
	private String SOFT_NAME; 
	
	/*工具的绝对路径用逗号分隔 */
	private String TOOLPATHS;

	public String getTOOLKIT_ID() {
		return TOOLKIT_ID;
	}

	public void setTOOLKIT_ID(String tOOLKIT_ID) {
		TOOLKIT_ID = tOOLKIT_ID;
	}

	public String getTOOLKIT_NAME() {
		return TOOLKIT_NAME;
	}

	public void setTOOLKIT_NAME(String tOOLKIT_NAME) {
		TOOLKIT_NAME = tOOLKIT_NAME;
	}

	public String getSOFT_NAME() {
		return SOFT_NAME;
	}

	public void setSOFT_NAME(String sOFT_NAME) {
		SOFT_NAME = sOFT_NAME;
	}

	public String getTOOLPATHS() {
		return TOOLPATHS;
	}

	public void setTOOLPATHS(String tOOLPATHS) {
		TOOLPATHS = tOOLPATHS;
	}

	public String getCOMMENTS() {
		return COMMENTS;
	}

	public void setCOMMENTS(String cOMMENTS) {
		COMMENTS = cOMMENTS;
	}

}
