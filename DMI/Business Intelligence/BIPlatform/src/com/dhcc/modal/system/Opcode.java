package com.dhcc.modal.system;
public class Opcode {
    private String id;//id
    private String codetype;//编码类型 SQ
    private String type;//固定编码 0 程序编码 1
    private String codevalue;//编码前缀
    private String displayvalue;//是否显示前缀
    private String codeformat;//编码格式
    private String displayformat;//是否显示日期编码
    private Integer seqvalue;//序列号起始值
    private Integer seqlength;//序列号值长度
    private String displayseq;//是否显示序列值
    private String remark;//备注
    private String displaydate;//日期 用于判断是否是新的一天

    /** id */
    public String getId() {
        return id;
    }
    /** id */
    public void setId(String id) {
        this.id = id;
    }
    /** 编码类型 SQ */
    public String getCodetype() {
        return codetype;
    }
    /** 编码类型 SQ */
    public void setCodetype(String codetype) {
        this.codetype = codetype;
    }
    /** 固定编码 0 程序编码 1 */
    public String getType() {
        return type;
    }
    /** 固定编码 0 程序编码 1 */
    public void setType(String type) {
        this.type = type;
    }
    /** 编码前缀 */
    public String getCodevalue() {
        return codevalue;
    }
    /** 编码前缀 */
    public void setCodevalue(String codevalue) {
        this.codevalue = codevalue;
    }
    /** 是否显示前缀 */
    public String getDisplayvalue() {
        return displayvalue;
    }
    /** 是否显示前缀 */
    public void setDisplayvalue(String displayvalue) {
        this.displayvalue = displayvalue;
    }
    /** 编码格式 */
    public String getCodeformat() {
        return codeformat;
    }
    /** 编码格式 */
    public void setCodeformat(String codeformat) {
        this.codeformat = codeformat;
    }
    /** 是否显示日期编码 */
    public String getDisplayformat() {
        return displayformat;
    }
    /** 是否显示日期编码 */
    public void setDisplayformat(String displayformat) {
        this.displayformat = displayformat;
    }
    /** 序列号起始值 */
    public Integer getSeqvalue() {
        return seqvalue;
    }
    /** 序列号起始值 */
    public void setSeqvalue(Integer seqvalue) {
        this.seqvalue = seqvalue;
    }
    /** 序列号值长度 */
    public Integer getSeqlength() {
        return seqlength;
    }
    /** 序列号值长度 */
    public void setSeqlength(Integer seqlength) {
        this.seqlength = seqlength;
    }
    /** 是否显示序列值 */
    public String getDisplayseq() {
        return displayseq;
    }
    /** 是否显示序列值 */
    public void setDisplayseq(String displayseq) {
        this.displayseq = displayseq;
    }
    /** 备注 */
    public String getRemark() {
        return remark;
    }
    /** 备注 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /** 日期 用于判断是否是新的一天 */
	public String getDisplaydate() {
		return displaydate;
	}
	/** 日期 用于判断是否是新的一天*/
	public void setDisplaydate(String displaydate) {
		this.displaydate = displaydate;
	}
}