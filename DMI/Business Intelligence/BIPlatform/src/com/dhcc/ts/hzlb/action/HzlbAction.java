package com.dhcc.ts.hzlb.action;

import java.io.PrintWriter;

import org.apache.struts2.ServletActionContext;

import com.dhcc.modal.system.PageModel;
import com.dhcc.ts.hzlb.dao.HzlbDao;
import com.dhcc.ts.hzlb.model.HzlbModel;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

public class HzlbAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private HzlbDao dao = new HzlbDao();
	private String type;//类别
	private String query;//查询条件
	private int page=1;
	private int rows=30;
	
	public String queryHzlbListModel() {
		/*String userid = (String)ActionContext.getContext().getSession().get("userid");
		if("a0babdd2c52c403483b44d8ae66238fc".equals(userid)){
			userid = "56474";
		}*/
		PageModel pageModel = dao.getHzlbModel(query,page,rows);
		JSONObject json = JSONObject.fromObject(pageModel);
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
	
	
	public String queryLoginName(){
		
		String username = (String)ActionContext.getContext().getSession().get("username");
		HzlbModel model = new HzlbModel();
		model.setName(username);
		JSONObject json = JSONObject.fromObject(model);
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
	
	private String hos_id;
	public String queryHospital(){
		HzlbModel model = dao.queryHospital(hos_id);
		String [] a = model.getId().split("/t");
		String str = "";
		for(int i=0;i< a.length;i++){
			str+="<p>"+a[i]+"</p>";
		}
		model.setId(str);
		
		JSONObject json = JSONObject.fromObject(model);
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

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getHos_id() {
		return hos_id;
	}
	public void setHos_id(String hos_id) {
		this.hos_id = hos_id;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getRows() {
		return rows;
	}


	public void setRows(int rows) {
		this.rows = rows;
	}


	


	
}
