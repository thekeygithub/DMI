package com.dhcc.common.system.role;

import com.dhcc.common.system.user.TreeDao;
import com.dhcc.common.util.CommUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.Tsrole;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author GYR
 */
public class RoleManagerAction extends ActionSupport {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private Tsrole model;// 数据模型
	private String ids;// 前台传过来的id的字符串，用‘，’来分割
	private String ch;// 角色对应的权限字符串
	private String roleId;// 角色id
	private String sort;// 菜单类别
	private String result;// 处理结果
	private String deptid;//公司或部门id

	/**
	 * 根据id 删除角色
	 */
	public String roleDel() {
		RoleDao dao = new RoleDao();
		if (ids != null && !ids.equals("")) {
			result = dao.roleDel(ids.split(","));
		}
		return SUCCESS;
	}

	/**
	 * 添加角色信息方法
	 */
	public String roleAdd() {
		RoleDao dao = new RoleDao();
		model.setId(CommUtil.getID());
		if(StringUtil.isNullOrEmpty(deptid)){
			String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
			model.setTopcorpid(topcorpid);
		}else{
			model.setTopcorpid(new TreeDao().queryTopCorpId(deptid));
		}
		boolean b = dao.roleAdd(model);
		if (b) {
			result = "success";
		}
		dao = null;
		return SUCCESS;
	}

	/**
	 * 根据id获取单个角色信息
	 */
	public String roleInfoQueryById() {
		RoleDao dao = new RoleDao();
		model = dao.roleQueryById(model.getId());
		return SUCCESS;
	}

	/**
	 * 更新角色信息
	 */
	public String roleUpdate() {
		RoleDao dao = new RoleDao();
		boolean b = dao.roleModify(model);
		if (b) {
			result = "success";
		}
		return SUCCESS;
	}

	/**
	 * 角色的权限的修改
	 */
	public String roleMenuModify() {
		String[] arr = ch.split(",");
		RoleDao rd = new RoleDao();
		rd.roleMenuModify(roleId, arr, sort);
		rd = null;
		return SUCCESS;
	}

	/**  
	* @标题: isUserHaveRole  
	* @描述: TODO用户是否有管理员权限
	* @作者 EbaoWeixun
	* @return
	*/  
	public String isUserHaveRole(){
		RoleDao rd=new RoleDao();
		String userid=(String)ActionContext.getContext().getSession().get("userid");
		boolean f=rd.queryUserHaveRole(userid);
		if(f){
			result="success";
		}else{
			result="fail";
		}
		return SUCCESS;
	}
	public Tsrole getModel() {
		return model;
	}

	public void setModel(Tsrole model) {
		this.model = model;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
