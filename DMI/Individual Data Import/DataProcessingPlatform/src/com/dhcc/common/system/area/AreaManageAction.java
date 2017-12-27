package com.dhcc.common.system.area;

import java.io.PrintWriter;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.system.user.TreeDao;
import com.dhcc.common.util.CommUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.Tsarea;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author GYR
 * 职务信息的管理
 * 添加、修改、删除、获取职务信息
 * 2014-09-16 15:50:30 
 */
public class AreaManageAction extends ActionSupport implements ModelDriven<Tsarea>{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private Tsarea model = new Tsarea();
	private String ids;// 前台传过来的id的字符串，用‘，’来分割
	private String result;//返回執行的結果
	private String deptid;//公司或部门id

	/**
	 * 删除职务信息的执行方法
	 * @return 执行结果
	 */
	public String areaDel() {
		AreaDao dao = new AreaDao();
		if (ids != null && !ids.equals("")) {
			result = dao.areaDel(ids.split(","));
		}
		return SUCCESS;
	}

	/**
	 * 添加职务信息的执行方法
	 * @return 执行结果
	 */
	public String areaAdd() {
		AreaDao dao = new AreaDao();
		model.setId(CommUtil.getID());
		if(StringUtil.isNullOrEmpty(deptid)){
			String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
			model.setTopcorpid(topcorpid);
		}else{
			TreeDao treedao = new TreeDao();
			model.setTopcorpid(treedao.queryTopCorpId(deptid));
		}
		JSONObject json;
		if(dao.areaAdd(model)){
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
	public String areaQueryById() {
		AreaDao dao = new AreaDao();
		PrintWriter pw = null;
		try {
			JSONObject json = JSONObject.fromObject(dao.areaQueryById(model.getId()));
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
	public String areaUpdate() {
		AreaDao dao = new AreaDao();
		JSONObject json;
		if(1==dao.areaModify(model)){
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
	public Tsarea getModel() {
		return model;
	}
	public void setModel(Tsarea model) {
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
	
}
