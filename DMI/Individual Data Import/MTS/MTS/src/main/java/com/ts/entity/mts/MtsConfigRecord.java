package com.ts.entity.mts;

public class MtsConfigRecord {
	
	private int RECORD_ID;                     //主键自增ID
	private int T_ID;                          //MTS_CONFIG_TEST表的主键ID
	private String NLP_SNAME;                  //NLP原始术语名
	private String NLP_SVAL;                   //原始匹配
	private String NLP_STYPE;                  //原始类型
	private int NLP_ORDER;                     //NLP切词顺序
	private int RES_ORDER;                     //概念转换 或单词返回多结果序号
	private String NLP_VNAME;                  //匹配标准词
	private String NLP_VCODE1;                 //匹配标准码1
	private String NLP_VCODE2;                 //匹配标准码2
	private String NLP_VTYPE;                  //匹配类型
	private String SPEC_TYPE;                  //特殊处理标识
	private String NLP_STATUS;                 //匹配状态  1匹配成功 0未匹配
	
	
	
	public int getRECORD_ID() {
		return RECORD_ID;
	}
	public void setRECORD_ID(int rECORD_ID) {
		RECORD_ID = rECORD_ID;
	}
	public int getT_ID() {
		return T_ID;
	}
	public void setT_ID(int t_ID) {
		T_ID = t_ID;
	}
	public String getNLP_SNAME() {
		return NLP_SNAME;
	}
	public void setNLP_SNAME(String nLP_SNAME) {
		NLP_SNAME = nLP_SNAME;
	}
	public String getNLP_SVAL() {
		return NLP_SVAL;
	}
	public void setNLP_SVAL(String nLP_SVAL) {
		NLP_SVAL = nLP_SVAL;
	}
	public String getNLP_STYPE() {
		return NLP_STYPE;
	}
	public void setNLP_STYPE(String nLP_STYPE) {
		NLP_STYPE = nLP_STYPE;
	}
	public int getNLP_ORDER() {
		return NLP_ORDER;
	}
	public void setNLP_ORDER(int nLP_ORDER) {
		NLP_ORDER = nLP_ORDER;
	}
	
	public int getRES_ORDER() {
		return RES_ORDER;
	}
	public void setRES_ORDER(int rES_ORDER) {
		RES_ORDER = rES_ORDER;
	}
	public String getNLP_VNAME() {
		return NLP_VNAME;
	}
	public void setNLP_VNAME(String nLP_VNAME) {
		NLP_VNAME = nLP_VNAME;
	}
	public String getNLP_VCODE1() {
		return NLP_VCODE1;
	}
	public void setNLP_VCODE1(String nLP_VCODE1) {
		NLP_VCODE1 = nLP_VCODE1;
	}
	public String getNLP_VCODE2() {
		return NLP_VCODE2;
	}
	public void setNLP_VCODE2(String nLP_VCODE2) {
		NLP_VCODE2 = nLP_VCODE2;
	}
	public String getNLP_VTYPE() {
		return NLP_VTYPE;
	}
	public void setNLP_VTYPE(String nLP_VTYPE) {
		NLP_VTYPE = nLP_VTYPE;
	}
	public String getSPEC_TYPE() {
		return SPEC_TYPE;
	}
	public void setSPEC_TYPE(String sPEC_TYPE) {
		SPEC_TYPE = sPEC_TYPE;
	}
	public String getNLP_STATUS() {
		return NLP_STATUS;
	}
	public void setNLP_STATUS(String nLP_STATUS) {
		NLP_STATUS = nLP_STATUS;
	}
	
	
	
}
