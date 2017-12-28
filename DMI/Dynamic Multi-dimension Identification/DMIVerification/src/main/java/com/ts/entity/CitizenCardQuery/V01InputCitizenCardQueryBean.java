package com.ts.entity.CitizenCardQuery;

import java.io.Serializable;
/**
 * 
* @ClassName: InputPreSectionAccountsBean 
* @Description: TODO(市民卡查询输入实体类) 
* @author Lee 李世博
* @date 2017年3月20日 下午2:42:37 
*
 */
public class V01InputCitizenCardQueryBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3563371855409669564L;
	private String V01_INP_NO01;
	private String V01_INP_NO02;
	public String getV01_INP_NO01() {
		return V01_INP_NO01;
	}
	public void setV01_INP_NO01(String v01_INP_NO01) {
		V01_INP_NO01 = v01_INP_NO01;
	}
	public String getV01_INP_NO02() {
		return V01_INP_NO02;
	}
	public void setV01_INP_NO02(String v01_INP_NO02) {
		V01_INP_NO02 = v01_INP_NO02;
	}

}
