package com.dhcc.common.system.resource;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.util.CommUtil;
import com.dhcc.modal.system.Tsbtnresource;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 按钮资源的管理
 */
public class ResManageAction extends ActionSupport {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private Tsbtnresource model;
	private String ids;// 前台传过来的id的字符串，用‘，’来分割
	private String id;
	private String pagename;
	private String roleid;//角色id
	private String menuSort;//菜单类别 顶级菜单id
	private String ch;
	private String result;//处理结果

	/**
	 * 删除根据id
	 */
	public String resDel() {
		ResDao dao = new ResDao();
		if (ids != null && !ids.equals("")) {
			result = dao.resDel(ids.split(","));
		}
		dao = null;
		return SUCCESS;
	}

	/**
	 * 添加信息方法
	 * @return
	 */
	public String resAdd() {
		ResDao dao = new ResDao();
		String proid = CommUtil.getID();
		model.setId(proid);
		boolean b = dao.resAdd(model);
		if(b){
			result = "success";
		}
		dao = null;
		return SUCCESS;
	}

	/**
	 * 根据id获取按钮资源信息
	 */
	public String resGetById() {
		ResDao dao = new ResDao();
		model = dao.resQueryById(id);
		dao = null;
		return SUCCESS;
	}


	/**
	 * 更新按钮资源信息
	 */
	public String resUpdate() {
		ResDao dao = new ResDao();
		boolean b = dao.resModify(model);
		if(b){
			result = "success";
		}
		return SUCCESS;
	}
	
	
	/**
	 * 角色的权限的修改
	 */
	public  String  roleBtnModify(){
		String[] arr = ch.split(",");
		ResDao rd = new ResDao();
		rd.roleBtnModify(roleid, menuSort,arr);
		return SUCCESS;
	}
	
	/**
	 * 获取角色对应页面所拥有的按钮权限
	 * 按钮权限管理页面
	 */
	public String  roleBtnQueryByMenuId(){
		ResDao rd = new ResDao();
		List<String> list = rd.getAllBtnByRole(roleid, menuSort);
		PrintWriter pw = null;
		try {
			JSONArray json = JSONArray.fromObject(list);
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
		} finally {
			pw.flush();
			pw.close();
		}
		return SUCCESS;
	}
	
	/**
	 * 获取角色对应页面所拥有的按钮权限
	 */
	public  String  btnQueryByUser(){
		ResDao rd = new ResDao();
		String userid = (String) ActionContext.getContext().getSession().get("userid");
		List<Tsbtnresource> list = rd.queryListByMenuId(userid,pagename);
		PrintWriter pw = null;
		try {
			JSONArray json = JSONArray.fromObject(list);
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
		} finally {
			pw.flush();
			pw.close();
		}
		return SUCCESS;
	}
	
	/**
	 * 获取页面所拥有的按钮
	 */
	public String  btnQueryAll(){
		ResDao rd = new ResDao();
		List list = rd.getAllBtn(menuSort);
		PrintWriter pw = null;
		try {
			JSONArray json = JSONArray.fromObject(list);
			System.out.println(json.toString());
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
		} finally {
			pw.flush();
			pw.close();
		}
		return SUCCESS;
	}
	
	
	/**
	 * 获取角色对应页面所拥有的按钮权限
	 * 通过页面url
	 */
	public  String  btnQueryByUserPageUrl(){
		ResDao rd = new ResDao();
		String userid = (String) ActionContext.getContext().getSession().get("userid");
		ServletContext sc = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);
		String projectName = sc.getContextPath();
		int pageUrl_Index = pagename.indexOf("/",1);
		String pageUrl = "";
		String urlProjectName = pagename.substring(0,pageUrl_Index);
		if(urlProjectName.equals(projectName)){
			pageUrl = pagename.substring(pageUrl_Index+1);
		}else{
			pageUrl = pagename.substring(1);
		}
		List<Tsbtnresource> list = rd.queryListByPageName(userid,pageUrl);
		PrintWriter pw = null;
		try {
			JSONArray json = JSONArray.fromObject(list);
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
		} finally {
			pw.flush();
			pw.close();
		}
		return SUCCESS;
	}
	
	
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public Tsbtnresource getModel() {
		return model;
	}

	public void setModel(Tsbtnresource model) {
		this.model = model;
	}

	public String getMenuSort() {
		return menuSort;
	}

	public void setMenuSort(String menuSort) {
		this.menuSort = menuSort;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
