package com.ts.entity.mts;

/**
 * 工具实体
 * @ClassName:MtsToolT
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年12月8日上午11:14:24
 */
public class MtsToolT 
{
    /* 工具包ID */
	private String TOOLKIT_ID;
	
	/*工具包名称*/
	private String TOOLKIT_NAME;
	
	/* 工具ID */
	private String TOOL_ID;
	
	/*工具名称*/
	private String TOOL_NAME;
	
	/*工具路径*/
	private String TOOL_PATH;
	
	/*工具的顺序*/
	private String TOOL_ORDER;
	
	/*关联ID*/
	private String TOOL_REL_ID;
	
	/*备注*/
	private String COMMENTS;
	

	public String getTOOL_ID() {
		return TOOL_ID;
	}

	public void setTOOL_ID(String tOOL_ID) {
		TOOL_ID = tOOL_ID;
	}

	public String getTOOLKIT_ID() {
		return TOOLKIT_ID;
	}

	public void setTOOLKIT_ID(String tOOLKIT_ID) {
		TOOLKIT_ID = tOOLKIT_ID;
	}

	public String getTOOL_NAME() {
		return TOOL_NAME;
	}

	public void setTOOL_NAME(String tOOL_NAME) {
		TOOL_NAME = tOOL_NAME;
	}

	public String getTOOL_PATH() {
		return TOOL_PATH;
	}

	public void setTOOL_PATH(String tOOL_PATH) {
		TOOL_PATH = tOOL_PATH;
	}

	public String getCOMMENTS() {
		return COMMENTS;
	}

	public void setCOMMENTS(String cOMMENTS) {
		COMMENTS = cOMMENTS;
	}
	public String getTOOL_ORDER() {
		return TOOL_ORDER;
	}

	public void setTOOL_ORDER(String tOOL_ORDER) {
		TOOL_ORDER = tOOL_ORDER;
	}

	public String getTOOL_REL_ID() {
		return TOOL_REL_ID;
	}

	public void setTOOL_REL_ID(String tOOL_REL_ID) {
		TOOL_REL_ID = tOOL_REL_ID;
	}

	public String getTOOLKIT_NAME() {
		return TOOLKIT_NAME;
	}

	public void setTOOLKIT_NAME(String tOOLKIT_NAME) {
		TOOLKIT_NAME = tOOLKIT_NAME;
	}
}
