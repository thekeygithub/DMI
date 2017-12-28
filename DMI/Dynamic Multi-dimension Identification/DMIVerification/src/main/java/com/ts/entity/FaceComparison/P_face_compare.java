package com.ts.entity.FaceComparison;

import java.io.Serializable;

/**
 * 
* @ClassName: P_face_compare
* @Description: TODO(人脸识别存储表) 
* @author Lee 刘峰
* @date 2017年6月13日 上午10:30:00 
*
 */
public class P_face_compare implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;//唯一标识
	private String id_card;//身份证号码
	private String hosp_code;//医院编码
	private String doctor_code;//就诊号
	private String group_id;//机构编号
	private String verify_img;//传入的参数要比对的图片Base64码
	private String verify_path;//图片存放路径
	private String query_img;//调用接口得到的源图片Base64码
	private String query_path;//图片地址
	private Integer result;//人脸比对结果(0：失败，1：成功)
	private String similarity;//相似度
	private String create_time;//创建时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getHosp_code() {
		return hosp_code;
	}
	public void setHosp_code(String hosp_code) {
		this.hosp_code = hosp_code;
	}
	public String getDoctor_code() {
		return doctor_code;
	}
	public void setDoctor_code(String doctor_code) {
		this.doctor_code = doctor_code;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getVerify_img() {
		return verify_img;
	}
	public void setVerify_img(String verify_img) {
		this.verify_img = verify_img;
	}
	public String getVerify_path() {
		return verify_path;
	}
	public void setVerify_path(String verify_path) {
		this.verify_path = verify_path;
	}
	public String getQuery_img() {
		return query_img;
	}
	public void setQuery_img(String query_img) {
		this.query_img = query_img;
	}
	public String getQuery_path() {
		return query_path;
	}
	public void setQuery_path(String query_path) {
		this.query_path = query_path;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getSimilarity() {
		return similarity;
	}
	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}