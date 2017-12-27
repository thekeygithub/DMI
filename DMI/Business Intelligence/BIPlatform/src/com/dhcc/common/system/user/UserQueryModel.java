package com.dhcc.common.system.user;

public class UserQueryModel {
	
	private String id;//用户ID
	private String username;//用户姓名
	private String loginname;//用户登录ID
	private String userpass;//用户密码
	private String sex;//用户性别
	private String birth;//用户出生年月
	private String  emailpublic;//公司的email
	private String  emailprivate;//私人的邮箱
	private String  mobilepublic;//公司的座机
	private String  mobileprivate;//私人的座机
	private String  phonepublic;//公司的电话
	private String  phoneprivate;//私人的电话
	private String  remark;//备注
	private String  deptid;//部门id
	private String  roleid;//角色id
	private String  stationid;//岗位id
	private String  postid;//职务id
	private String  rolename;//职务名称
	private String  stationname;//岗位名称
	private String  postname;//职务名称
	private String  deptname;//部门名称
	private String  corpid;
	private String  corpname;
	private String  area;//所属区域
	private String  areaname;//所属区域
	private String topcorpid;//顶级公司（页面不展示）
	private String dcid;//数据中心id
	private String dcidname;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEmailpublic() {
		return emailpublic;
	}
	public void setEmailpublic(String emailpublic) {
		this.emailpublic = emailpublic;
	}
	public String getEmailprivate() {
		return emailprivate;
	}
	public void setEmailprivate(String emailprivate) {
		this.emailprivate = emailprivate;
	}
	public String getMobilepublic() {
		return mobilepublic;
	}
	public void setMobilepublic(String mobilepublic) {
		this.mobilepublic = mobilepublic;
	}
	public String getMobileprivate() {
		return mobileprivate;
	}
	public void setMobileprivate(String mobileprivate) {
		this.mobileprivate = mobileprivate;
	}
	public String getPhonepublic() {
		return phonepublic;
	}
	public void setPhonepublic(String phonepublic) {
		this.phonepublic = phonepublic;
	}
	public String getPhoneprivate() {
		return phoneprivate;
	}
	public void setPhoneprivate(String phoneprivate) {
		this.phoneprivate = phoneprivate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getStationid() {
		return stationid;
	}
	public void setStationid(String stationid) {
		this.stationid = stationid;
	}
	public String getPostid() {
		return postid;
	}
	public void setPostid(String postid) {
		this.postid = postid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getStationname() {
		return stationname;
	}
	public void setStationname(String stationname) {
		this.stationname = stationname;
	}
	public String getPostname() {
		return postname;
	}
	public void setPostname(String postname) {
		this.postname = postname;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getCorpid() {
		return corpid;
	}
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public String getCorpname() {
		return corpname;
	}
	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getTopcorpid() {
		return topcorpid;
	}
	public void setTopcorpid(String topcorpid) {
		this.topcorpid = topcorpid;
	}
	/** 数据中心id */
    public String getDcid() {
        return dcid;
    }
    /** 数据中心id */
    public void setDcid(String dcid) {
        this.dcid = dcid;
    }
	public String getDcidname() {
		return dcidname;
	}
	public void setDcidname(String dcidname) {
		this.dcidname = dcidname;
	}
}
