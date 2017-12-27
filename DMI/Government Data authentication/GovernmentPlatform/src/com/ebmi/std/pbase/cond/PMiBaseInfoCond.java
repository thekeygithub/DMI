package com.ebmi.std.pbase.cond;

/**
 * 参保人基本信息 条件类
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午4:04:11
 *
 */
public class PMiBaseInfoCond extends BaseCond{
	
	private String p_mi_id;
	private String si_card_no;
	private String sta_type;
	private String year;
	private String med_type_name;//类型名称
	private String dept_num_one;//机构编号
	private String dept_num_two;//定点医疗服务机构编号
	private String item_id;//流水号

	
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getDept_num_one() {
		return dept_num_one;
	}
	public void setDept_num_one(String dept_num_one) {
		this.dept_num_one = dept_num_one;
	}
	public String getDept_num_two() {
		return dept_num_two;
	}
	public void setDept_num_two(String dept_num_two) {
		this.dept_num_two = dept_num_two;
	}
	public String getMed_type_name() {
		return med_type_name;
	}
	public void setMed_type_name(String med_type_name) {
		this.med_type_name = med_type_name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSta_type() {
		return sta_type;
	}
	public void setSta_type(String sta_type) {
		this.sta_type = sta_type;
	}
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public String getSi_card_no() {
		return si_card_no;
	}
	public void setSi_card_no(String si_card_no) {
		this.si_card_no = si_card_no;
	}

}