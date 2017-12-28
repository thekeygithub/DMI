package com.ts.entity.mts;

/**
 * 标化结果规则实体类
 * @ClassName:RsRuleDetailT
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年11月30日下午2:38:51
 */
public class RsRuleDetailT 
{
    /* 主键ID */
	private String RRD_ID; 
	/*区域代码*/
	private String AREA_ID;  
	/* 聚类 */
	private String DATA_CLASS_CODE;             
	/*调用的程序名*/
	private String APPLY_METHOD;
	
	
	public String getRRD_ID() {
		return RRD_ID;
	}
	public void setRRD_ID(String rRD_ID) {
		RRD_ID = rRD_ID;
	}
	public String getAREA_ID() {
		return AREA_ID;
	}
	public void setAREA_ID(String aREA_ID) {
		AREA_ID = aREA_ID;
	}
	public String getDATA_CLASS_CODE() {
		return DATA_CLASS_CODE;
	}
	public void setDATA_CLASS_CODE(String dATA_CLASS_CODE) {
		DATA_CLASS_CODE = dATA_CLASS_CODE;
	}
	public String getAPPLY_METHOD() {
		return APPLY_METHOD;
	}
	public void setAPPLY_METHOD(String aPPLY_METHOD) {
		APPLY_METHOD = aPPLY_METHOD;
	}
    
}
