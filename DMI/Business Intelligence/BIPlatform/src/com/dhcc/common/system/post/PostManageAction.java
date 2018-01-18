package com.dhcc.common.system.post;

import java.io.PrintWriter;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.system.user.TreeDao;
import com.dhcc.common.util.CommUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.Tspost;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author GYR
 * 职务信息的管理
 * 添加、修改、删除、获取职务信息
 * 2014-09-16 15:50:30 
 */
public class PostManageAction extends ActionSupport implements ModelDriven<Tspost>{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private Tspost model = new Tspost();
	private String ids;// 前台传过来的id的字符串，用‘，’来分割
	private String result;//返回執行的結果
	private String deptid;//公司或部门id

	/**
	 * 删除职务信息的执行方法
	 * @return 执行结果
	 */
	public String postDel() {
		PostDao dao = new PostDao();
		if (ids != null && !ids.equals("")) {
			result = dao.postDel(ids.split(","));
		}
		return SUCCESS;
	}

	/**
	 * 添加职务信息的执行方法
	 * @return 执行结果
	 */
	public String postAdd() {
		PostDao dao = new PostDao();
		model.setId(CommUtil.getID());
		if(StringUtil.isNullOrEmpty(deptid)){
			String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
			model.setTopcorpid(topcorpid);
		}else{
			TreeDao treedao = new TreeDao();
			model.setTopcorpid(treedao.queryTopCorpId(deptid));
		}
		JSONObject json;
		if(dao.postAdd(model)){
			json = JSONObject.fromObject(model);
		}else{
			json = JSONObject.fromObject("{\"result\":\"error\"}");
		}
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 获取职务信息的执行方法
	 * 根据职务id
	 * @return 执行结果
	 */
	public String postQueryById() {
		PostDao dao = new PostDao();
		PrintWriter pw = null;
		try {
			JSONObject json = JSONObject.fromObject(dao.postQueryById(model.getId()));
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 更新职务信息的执行方法
	 * @return 执行结果
	 */
	public String postUpdate() {
		PostDao dao = new PostDao();
		JSONObject json;
		if(1==dao.postModify(model)){
			json = JSONObject.fromObject(model);
		}else{
			json = JSONObject.fromObject("{\"result\":\"error\"}");
		}
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Tspost getModel() {
		return model;
	}
	public void setModel(Tspost model) {
		this.model = model;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String ids;// 前台传过来的id的字符串，用‘，’来分割
	private Tsmenu modal;//菜单模型
	private String id;// 查询单个信息的id
	private String result;//处理结果


	/**
	 * 根据菜单id删除菜单
	 */
	public String menuDel() {
		MenuDao dao = new MenuDao();
		if (ids != null && !ids.equals("")) {
			result = dao.menuDelete(ids.split(","));
		}
		return SUCCESS;
	}

	/**
	 * 添加菜单信息方法
	 */
	public String menuAdd() {
		MenuDao dao = new MenuDao();
		String id = CommUtil.getID();
		modal.setId(id);
		boolean b = dao.menuAdd(modal);
		if(b){
			result = "success";
		}
		return SUCCESS;
	}
	
	/**
	 * 根据id获取单个的菜单信息
	 */
	public String menugetById() {
		MenuDao dao = new MenuDao();
		modal = dao.menuQueryById(id);
		return SUCCESS;
	}

	/**
	 * 更新菜单信息方法
	 */
	public String menuUpdate() {
		MenuDao dao = new MenuDao();
		boolean b = dao.menuModify(modal);
		if(b){
			result = "success";
		}
		return SUCCESS;
	}
    
	/**
	 *@作者 GYR
	 *@日期 Oct 14, 2014 3:12:40 PM
	 *@描述 查询该菜单下是否有子菜单
	 *@return false:没有 true 有
	 */
	/**
	 * 更新菜单信息方法
	 */
	public String menuQueryHasChildren() {
		MenuDao dao = new MenuDao();
		if (ids != null && !ids.equals("")) {
			result = dao.menuQueryHasChildren(ids.split(","));
		}
		return SUCCESS;
	}
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Tsmenu getModal() {
		return modal;
	}

	public void setModal(Tsmenu modal) {
		this.modal = modal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
