package com.dhcc.common.system.login;

import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.util.CreateDate;
import com.dhcc.modal.system.Tsuser;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;



/**
 * 用户登陆的校验Action
 * @author GYR
 *
 */
@SuppressWarnings("unchecked")
public class LoginAction  extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	public String userid="";
	public String password="";
	public String msg="";//登陆失败的信息
	private String idforuser;
	
	/**
	 * 查询该用户名、密码是否正确
	 * @return 是否有改用户
	 */
	public String QueryUser(){
		LoginDao dao = new LoginDao();
		Tsuser model = new Tsuser();
		model = dao.QueryUser(this.userid, this.password);
		if(null != model){
			this.setMsg("LOGINSUCCESS");
			idforuser = model.getId();
			ActionContext.getContext().getSession().put("userid",model.getId());
			ActionContext.getContext().getSession().put("username",model.getUsername());
			
//			ActionContext.getContext().getSession().put("roleIds",dao.QueryUserRoleId(model.getId()));
//			ActionContext.getContext().getSession().put("topcorpid",model.getTopcorpid());
//			ActionContext.getContext().getSession().put("dcid",model.getDcid());
//			ActionContext.getContext().getSession().put("corpid",dao.QueryUserCorpid(model.getId()));
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("用户登录", "用户于"+CreateDate.getDateString()+" 登录系统！");
			saveLog = null;
		}else{
			this.setMsg("LOGINERROR");
		}
		return SUCCESS;
	}
	


	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPossword() {
		return password;
	}

	public void setPossword(String possword) {
		this.password = possword;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getIdforuser() {
		return idforuser;
	}

	public void setIdforuser(String idforuser) {
		this.idforuser = idforuser;
	}

}
