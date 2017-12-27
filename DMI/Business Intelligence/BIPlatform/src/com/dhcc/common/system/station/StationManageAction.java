package com.dhcc.common.system.station;

import java.io.IOException;
import java.io.PrintWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.system.user.TreeDao;
import com.dhcc.common.util.CommUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.Tsstation;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 岗位的添加、修改和删除
 */
public class StationManageAction extends ActionSupport implements ModelDriven<Tsstation> {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private Tsstation model = new Tsstation();
	private String ids;// 前台传过来的id的字符串，用‘，’来分割
	private String deptid;//公司或部门id
	private StationDao dao = new StationDao();

	/**
	 * 根据id删除岗位信息
	 */
	public String stationDel() {
		if (ids != null && !ids.equals("")) {
			dao.stationDel(ids.split(","));
		}
		return SUCCESS;
	}

	/**
	 * 添加新的岗位信息
	 */
	public String stationAdd() {
		model.setId(CommUtil.getID());
		if(StringUtil.isNullOrEmpty(deptid)){
			String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
			model.setTopcorpid(topcorpid);
		}else{
			TreeDao treedao = new TreeDao();
			model.setTopcorpid(treedao.queryTopCorpId(deptid));
		}
		JSONObject json;
		if(dao.stationAdd(model)){
			json = JSONObject.fromObject(model);
		}else{
			json = JSONObject.fromObject("{\"result\":\"error\"}");
		}
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		return SUCCESS;
	}

	/**
	 * 获取单个的岗位信息
	 * id 岗位的id
	 */
	public String stationQueryById() {
		Tsstation modal1 = dao.stationQueryById(model.getId());
		PrintWriter pw = null;
		try {
			JSONObject json = JSONObject.fromObject(modal1);
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}

		return SUCCESS;
	}

	/**
	 * 更新岗位信息方法
	 */
	public String stationUpdate() {
		JSONObject json;
		if(1==dao.stationModify(model)){
			json = JSONObject.fromObject(model);
		}else{
			json = JSONObject.fromObject("{\"result\":\"error\"}");
		}
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		return SUCCESS;
	}
	/**
	 * @描述：删除职务、岗位、角色时查看是否有人员分配该信息
	 * @作者：SZ
	 * @时间：2014-10-14 下午01:20:16
	 * @return
	 */
	private String type;

	public String QueryIsDelete(){
		boolean aa = false;
		if(dao.QueryIsDelete(type, ids.split(","))){
			aa = true;
		}
		JSONArray  json=JSONArray.fromObject(aa);
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		try {
			pw = ServletActionContext.getResponse().getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.print(json);
		pw.flush();
		pw.close();
		return SUCCESS;
	}

	public Tsstation getModel() {
		return model;
	}

	public void setModel(Tsstation model) {
		this.model = model;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
