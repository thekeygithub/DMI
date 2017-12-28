package com.ts.entity.mts;

/**
 * 流程控制实体类。
 * @author autumn
 *
 */
public class MatchRuleDetailT 
{
    /* 匹配流程主键ID */
	private String MEM_ID;                      
	/* 标化类型 */
	private String MEM_DATA_CODE;	            
	/* 聚类 */
	private String DATA_CLASS_CODE;             
	/* 流程标识。1外部处理流程 0内部匹配流程 */
	private String FLAG;                        
	/* 匹配入口 */
	private String ORDERBY;                     
	/* 触发标化流程入口的标化类型 */
	private String IFNEXT;                      
	/* 是否插入问题单 1插入，0不插入 */
	private String QUESTION;                    
	/* 返回值处理 0 覆盖 1 追加  */
	private String MARK;                        
	/* 成功跳转--匹配流程主键ID */
	private String SUCCESS_REDIRECT_TO;         
	/* 失败跳转--匹配流程主键ID */
	private String FAILURE_REDIRECT_TO;         
	/*词or码匹配*/
	private String NOC;                         
	/*调用的程序名*/
	private String APPLY_METHOD;
	
	/* 调用的程序名 之前 执行 */
    private String BeforeAPPLY_METHOD;          
    /*调用的程序名 之后 执行 */
    private String AfterAPPLY_METHOD;
    
    /* 调用的程序名 之前执行  子流程     流程自存放  流程 入口 id */
    private String BeforeExceSubFlow;
    /* 调用的程序名 之后执行  子流程  流程自存放  流程 入口 id */
    private String AfterExceSubFlow;
    
	/*是否拼key  0不需要，1需要*/
	private String PINKEY;                      
	/*区域代码*/
	private String AREA_ID;   
	
	
	public String getBeforeExceSubFlow()
    {
        return BeforeExceSubFlow;
    }

    public void setBeforeExceSubFlow(String beforeExceSubFlow)
    {
        BeforeExceSubFlow = beforeExceSubFlow;
    }

    public String getAfterExceSubFlow()
    {
        return AfterExceSubFlow;
    }

    public void setAfterExceSubFlow(String afterExceSubFlow)
    {
        AfterExceSubFlow = afterExceSubFlow;
    }

    public String getBeforeAPPLY_METHOD()
    {
        return BeforeAPPLY_METHOD;
    }
	
    public void setBeforeAPPLY_METHOD(String beforeAPPLY_METHOD)
    {
        BeforeAPPLY_METHOD = beforeAPPLY_METHOD;
    }
    
    public String getAfterAPPLY_METHOD()
    {
        return AfterAPPLY_METHOD;
    }
    
    public void setAfterAPPLY_METHOD(String afterAPPLY_METHOD)
    {
        AfterAPPLY_METHOD = afterAPPLY_METHOD;
    }
    
    public String getAREA_ID() {
		return AREA_ID;
	}
    
	public void setAREA_ID(String aREA_ID) {
		AREA_ID = aREA_ID;
	}
	public String getPINKEY() {
		return PINKEY;
	}
	public void setPINKEY(String pINKEY) {
		PINKEY = pINKEY;
	}
	public String getAPPLY_METHOD() {
		return APPLY_METHOD;
	}
	public void setAPPLY_METHOD(String aPPLY_METHOD) {
		APPLY_METHOD = aPPLY_METHOD;
	}
	public String getNOC() {
		return NOC;
	}
	public void setNOC(String nOC) {
		NOC = nOC;
	}
	public String getSUCCESS_REDIRECT_TO() {
		return SUCCESS_REDIRECT_TO;
	}
	public void setSUCCESS_REDIRECT_TO(String sUCCESS_REDIRECT_TO) {
		SUCCESS_REDIRECT_TO = sUCCESS_REDIRECT_TO;
	}
	public String getFAILURE_REDIRECT_TO() {
		return FAILURE_REDIRECT_TO;
	}
	public void setFAILURE_REDIRECT_TO(String fAILURE_REDIRECT_TO) {
		FAILURE_REDIRECT_TO = fAILURE_REDIRECT_TO;
	}
	public String getORDERBY() {
		return ORDERBY;
	}
	public void setORDERBY(String oRDERBY) {
		ORDERBY = oRDERBY;
	}
	public String getIFNEXT() {
		return IFNEXT;
	}
	public void setIFNEXT(String iFNEXT) {
		IFNEXT = iFNEXT;
	}
	public String getQUESTION() {
		return QUESTION;
	}
	public void setQUESTION(String qUESTION) {
		QUESTION = qUESTION;
	}
	public String getMARK() {
		return MARK;
	}
	public void setMARK(String mARK) {
		MARK = mARK;
	}
	public String getMEM_ID() {
		return MEM_ID;
	}
	public void setMEM_ID(String mEM_ID) {
		MEM_ID = mEM_ID;
	}
	public String getMEM_DATA_CODE() {
		return MEM_DATA_CODE;
	}
	public void setMEM_DATA_CODE(String mEM_DATA_CODE) {
		MEM_DATA_CODE = mEM_DATA_CODE;
	}
	public String getDATA_CLASS_CODE() {
		return DATA_CLASS_CODE;
	}
	public void setDATA_CLASS_CODE(String dATA_CLASS_CODE) {
		DATA_CLASS_CODE = dATA_CLASS_CODE;
	}
	public String getFLAG() {
		return FLAG;
	}
	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}
}
