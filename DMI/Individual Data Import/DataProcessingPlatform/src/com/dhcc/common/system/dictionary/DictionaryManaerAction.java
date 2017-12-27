package com.dhcc.common.system.dictionary;

import java.io.PrintWriter;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.util.CommUtil;
import com.dhcc.modal.system.Tsdict;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class DictionaryManaerAction extends ActionSupport implements ModelDriven<Tsdict> {


	/**
	 * @作者 SZ
	 * @日期 2014 12:53:09 PM
	 */
	private static final long serialVersionUID = 1L;
	private Tsdict model = new Tsdict();
	private String ids;// 前台传过来的id的字符串，用‘，’来分割

	/**
	 * 删除根据ids
	 * 
	 * @return
	 */
	public String dictDel() {
		DictionaryDao dao = new DictionaryDao();
		if (ids != null && !ids.equals("")) {
			String a = dao.dictDel(ids.split(","));
			JSONObject json;
			if("success".equals(a)){
				json = JSONObject.fromObject("{\"result\":\"success\"}");
			}else{
				json = JSONObject.fromObject("{\"result\":\"error\"}");
			}
			PrintWriter pw = null;
			try {
				ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
				pw = ServletActionContext.getResponse().getWriter();
				pw.print(json);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				pw.flush();
				pw.close();
			}
		}
		return SUCCESS;
	}

	/**
	 * 添加信息方法
	 * 
	 * @return
	 */
	public String dictAdd() {
		DictionaryDao dao = new DictionaryDao();
		model.setId(CommUtil.getID());
		JSONObject json;
		if(dao.dictAdd(model)){
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
			System.out.println(e.getMessage());
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
	public String dictgetById() {
		DictionaryDao dao = new DictionaryDao();
		Tsdict model1 = dao.dictQueryById(model.getId());
		PrintWriter pw = null;
		try {
			JSONObject json = JSONObject.fromObject(model1);
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
	public String dictUpdate() {
		DictionaryDao dao = new DictionaryDao();
		JSONObject json;
		if(dao.dictModify(model)==1){
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
			System.out.println(e.getMessage());
		} finally {
			pw.flush();
			pw.close();
		}
		return SUCCESS;
	}

	public Tsdict getModel() {
		return model;
	}

	public void setModel(Tsdict model) {
		this.model = model;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
