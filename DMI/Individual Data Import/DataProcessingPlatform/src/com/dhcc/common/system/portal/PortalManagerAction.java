package com.dhcc.common.system.portal;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.util.CommUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**
 * @author GYR
 * 系统门户管理
 */
public class PortalManagerAction extends ActionSupport implements ModelDriven<NewPortalModel>{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String cont;//系统门户布局信息 前台传入 
	private NewPortalModel model = new NewPortalModel();
	
	/**
	 * 更新门户信息
	 */
	public String updatePortal(){
		PortalDao dao = new PortalDao();
		String userid = (String) ActionContext.getContext().getSession().get("userid");
		List<NewPortalModel> list = jsonToObject1(cont,userid);
		String result = dao.updatePortal(userid,list);
		//String result = "success";
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		dao = null;
		return SUCCESS;
	}
	/**
	 * 添加门户信息
	 */
	public String addPortal(){
		PortalDao dao = new PortalDao();
		String userid = (String) ActionContext.getContext().getSession().get("userid");
		model.setId(CommUtil.getID());
		model.setUserid(userid);
		String result = dao.addPortal(userid,model);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		dao = null;
		return SUCCESS;
	}
	/**
	 * 获取当前用户的门户信息
	 */
	public String  queryPortal(){
		PortalDao dao = new PortalDao();
		String userid = (String) ActionContext.getContext().getSession().get("userid");
		List<PortalModel>  list = dao.portalQueryByUserid(userid);
		JSONArray json = JSONArray.fromObject(list);
		System.out.println(json.toString());
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
		dao = null;
		return SUCCESS;
	}
		
	/*
	 * 新的解析json的方法
	 * */
	private List<NewPortalModel> jsonToObject1(String jsonString,String userid){
		/**
		 * 定义list 添加解析后的 需要的数据
		 */
	    List<NewPortalModel>  listadd = new ArrayList<NewPortalModel>();
		/**
		 * 解析传入的json数据start
		 */
	    JSONArray array = JSONArray.fromObject(jsonString);    
	    NewPortalModel[] obj = (NewPortalModel[]) JSONArray.toArray(array,NewPortalModel.class);
	    for(int i=0;i<obj.length;i++){
	    	JSONObject json = JSONObject.fromObject(obj[i]);
	    	NewPortalModel t1 = (NewPortalModel) JSONObject.toBean(json, NewPortalModel.class);
	    	t1.setId(CommUtil.getID());
	    	t1.setUserid(userid);
	    	listadd.add(t1);
	    }

        return listadd;   
	}
	
	/**
	 *@作者 GYR
	 *@日期 Aug 19, 2014 4:23:22 PM
	 *@描述 门户url 下来狂加载
	 */
	public String queryPortalSelect(){
		PortalDao dao = new PortalDao();
		List list1 = dao.queryPortalSelect();
		JSONArray json = JSONArray.fromObject(list1);
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
	
	
	
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	public NewPortalModel getModel() {
		return model;
	}
	public void setModel(NewPortalModel model) {
		this.model = model;
	}
}
