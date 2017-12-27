package com.dhcc.common.util;

import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;


/**
 * 得到前台下拉框JSON 数据
 * @author GYR
 */
public class CommAction extends ActionSupport {
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String dictType;//前台传过来的类型
	private String dictKey;
	
	/**
	 * 得到下拉框
	 * @return
	 */
	public String SelectedGet() {
		CommDao cd = new CommDao();
		List list = cd.getSelectHTML(dictType);
		JSONArray json = JSONArray.fromObject(list);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			PrintWriter pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 得到系统配置表的下拉框
	 * @return
	 */
	public String SelectedGetConfig() {
		CommDao cd = new CommDao();
		List list = cd.getConfigSelectHTML(dictType);
		JSONArray json = JSONArray.fromObject(list);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			PrintWriter pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * @描述：获取已部署的流程名称
	 * @作者：SZ
	 * @时间：2014-12-9 下午01:46:05
	 * @return
	 */
	public String SelectedGetBusiness(){
		CommDao cd = new CommDao();
		List list = cd.getBusinessSelectHTML();
		JSONArray json = JSONArray.fromObject(list);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			PrintWriter pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String queryConfigSuperUserID(){
		CommDao cd = new CommDao();
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			PrintWriter pw=ServletActionContext.getResponse().getWriter();
			pw.write(cd.queryConfigSuperUserID());
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}


	public String getDictKey() {
		return dictKey;
	}

	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}

}
