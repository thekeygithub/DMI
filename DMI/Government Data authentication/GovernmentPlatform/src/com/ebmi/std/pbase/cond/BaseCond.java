package com.ebmi.std.pbase.cond;

public class BaseCond {
	/**
	 * 公民身份证号
	 */
 private String idcard;
 /**
  * 姓名
  */
 private String username;
 /**
  * 社会保障卡号
  */
 private String ss_num;
 
 
 /**
  * 状态 
  * 1工作表 2历史表
  */
 private String table_stat;//状态 1工作表 2历史表
 
 /**
  * 险种
  *  1养老保险 2失业保险 3医疗保险 4工伤保险 5生育保险
  */
 private String si_type;//险种 1养老保险 2失业保险 3医疗保险 4工伤保险 5生育保险
 
 /**
  *  开始属期 
  */
 private String start_date;
 /**
  *  终止属期 
  */
 private String end_date;
 /**
  * 查询状态  0全部 1已缴 2欠缴 
  */
 private String query_flag;
 
 
public String getStart_date() {
	return start_date;
}
public void setStart_date(String start_date) {
	this.start_date = start_date;
}
public String getEnd_date() {
	return end_date;
}
public void setEnd_date(String end_date) {
	this.end_date = end_date;
}
public String getQuery_flag() {
	return query_flag;
}
public void setQuery_flag(String query_flag) {
	this.query_flag = query_flag;
}
public String getTable_stat() {
	return table_stat;
}
public void setTable_stat(String table_stat) {
	this.table_stat = table_stat;
}
public String getSi_type() {
	return si_type;
}
public void setSi_type(String si_type) {
	this.si_type = si_type;
}
public String getIdcard() {
	return idcard;
}
public void setIdcard(String idcard) {
	this.idcard = idcard;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getSs_num() {
	return ss_num;
}
public void setSs_num(String ss_num) {
	this.ss_num = ss_num;
}
 
 
}
