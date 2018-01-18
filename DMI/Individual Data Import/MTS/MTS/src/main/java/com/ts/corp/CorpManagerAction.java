package com.dhcc.common.system.corp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.system.user.TreeDao;
import com.dhcc.common.util.CommUtil;
import com.dhcc.modal.system.TreeModal;
import com.dhcc.modal.system.Tsconfig;
import com.dhcc.modal.system.Tscorp;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @描述：单位管理（公司和部门）
 * @作者：SZ
 * @时间：2014-10-14 上午10:24:27
 */
public class CorpManagerAction extends ActionSupport implements ModelDriven<Tscorp> {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private Tscorp model = new Tscorp();
	private String ids;// 前台传过来的id的字符串，用‘，’来分割
	private CorpDao dao = new CorpDao();

	/**
	 * 删除根据id
	 * @return
	 */
	public String corpDel() {
		if (ids != null && !ids.equals("")) {
			dao.corpDel(ids.split(","));
		}
		return SUCCESS;
	}

	/**
	 * 添加信息方法
	 * @return
	 */
	public String corpAdd() {
		model.setId(CommUtil.getID());
		model.setTopcorpid(new TreeDao().queryTopCorpId(model.getPid()));
//		if("".equals(model.getPid())){
//			model.setPid(topcorpid);
//		}
		JSONObject json;
		if(dao.corpAdd(model)){
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
	 * 获取信息方法
	 * 
	 * @return
	 */
	public String corpgetById() {
		PrintWriter pw = null;
		try {
			JSONObject json = JSONObject.fromObject(dao.corpQueryById(model.getId()));
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
	 * 更新信息方法
	 * 
	 * @return
	 */
	public String corpUpdate() {
		model.setTopcorpid(new TreeDao().queryTopCorpId(model.getPid()));
//		if("".equals(model.getPid())){
//			model.setPid(topcorpid);
//		}
		JSONObject json;
		if(dao.corpModify(model)){
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
	 * 公司添加或者修改时查询公司的下拉框
	 */
	public String corpSelectedQuery(){
		
		List list = dao.getAllCorp();
		JSONArray json = JSONArray.fromObject(list);
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
	
	public String ifDelCorp() throws Exception{
		String str = "error";
		if(dao.isDelcorp(ids.split(","))){
			str="success";
		}
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		PrintWriter pw=ServletActionContext.getResponse().getWriter();
		pw.write(str);
		pw.flush();
		pw.close();
		return SUCCESS;
	}
	/**
	 * @描述：单位树形排序（公司和部门的树形排序）
	 * @作者：SZ
	 * @时间：2014-10-14 下午12:40:31
	 * @return
	 */
	public String QueryCropDeptAll(){
		List<TreeModal> list = dao.QueryCropDeptAll();
		List<Tsconfig> list1 = dao.ConfigQueryByType("TREEICON");
		String CORPICON = "";
		String DEPTICON = "";
		if(list1 != null){
			for(Tsconfig temp:list1){
				if(temp.getDkey().equals("1")){
					CORPICON = temp.getDvalue();
				}else if(temp.getDkey().equals("2")){
					DEPTICON = temp.getDvalue();
				}
			}
		}
		if(list != null){
			for(TreeModal temp:list){
				if(temp.getUrl().equals("1")){
					temp.setIcon(CORPICON);
				}else if(temp.getUrl().equals("2")){
					temp.setIcon(DEPTICON);
				}
			}
		}
		JSONArray  json = JSONArray.fromObject(list);
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

	public Tscorp getModel() {
		return model;
	}

	public void setModel(Tscorp model) {
		this.model = model;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
