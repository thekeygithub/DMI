package com.aghit.task.algorithm.domain;

import java.util.Date;

/**
 * 比较对象封装，含有编码名称、ID、原始数据ID信息
 * 便于输入日志和错误信息
 * @author Administrator
 *
 */
public class ComparedEntity {

	private String code;			// 比较对象编码
	private String name;			// 比较对象名称
	private double pay_tot;			// 涉及金额（诊断时，结算单金额）
	private long id; 				// 比较对象的数据库ID（医嘱ID或者处方ID或者结算单ID（诊断时））
	private Date exe_date;          // 执行日期（诊断时，结算单日期）
	private String pre_code = "";        // 处方单ID
	private String seq_no = "";          // 组ID
	private long rownum;            //项目序号 
	
	private long[] cateId;			// 所属分类ID
	
	public ComparedEntity() {
		this.cateId = new long[0];
	}

	public ComparedEntity(String code, String name, long id, double pay_tot,Date exe_date,long rownum) {
		this();
		this.code = code;
		this.name = name;
		this.id = id;
		this.pay_tot=pay_tot;
		this.exe_date=exe_date;
		this.rownum = rownum;
	}
	
	public ComparedEntity(String code, String name, long id, double pay_tot,Date exe_date,long rownum,String pre_code,String seq_no) {
		this(code,name,id,pay_tot,exe_date,rownum);
		this.pre_code = pre_code;
		this.seq_no = seq_no;
	}

	public double getPay_tot() {
		return pay_tot;
	}

	public void setPay_tot(double payTot) {
		pay_tot = payTot;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long[] getCateId() {
		return cateId;
	}

	public void setCateId(long[] cateId) {
		this.cateId = cateId;
	}

	public Date getExe_date() {
		return exe_date;
	}

	public void setExe_date(Date exe_date) {
		this.exe_date = exe_date;
	}

	public String getPre_code() {
		return pre_code;
	}

	public void setPre_code(String pre_code) {
		this.pre_code = pre_code;
	}

	public String getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}

	public long getRownum() {
		return rownum;
	}

	public void setRownum(long rownum) {
		this.rownum = rownum;
	}

}
