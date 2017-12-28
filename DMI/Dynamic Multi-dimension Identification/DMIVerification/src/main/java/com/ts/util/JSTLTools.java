package com.ts.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class JSTLTools extends TagSupport {

	private static final long serialVersionUID = 1084130178184084936L;//序列号
	
	private String value;//传值
	
	private String parttern;//类型格式
	
	public int doStartTag() throws JspException {
		try {
			String result = "";//返回结果
			if(parttern!=null && "datetime".equals(parttern)){
				if(value!=null && value.length()>2){
					result = value.substring(0, value.length()-2);
				}
			}
			if(parttern!=null && "number".equals(parttern)){
				if(value!=null && !"".equals(value)){
					result = String.format("%.2f", Double.parseDouble(value));
				}
			}
			pageContext.getOut().write(result);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParttern() {
		return parttern;
	}

	public void setParttern(String parttern) {
		this.parttern = parttern;
	}
	
}