package com.dhcc.ts.IPDAdvancedSearch;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.dhcc.modal.system.PageModel;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

public class AdvancedSearchAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String queryParam;
	private Integer page=1;
	private Integer rows=30;
	public String searchIPDData(){
		AdvancedSearchDao dao = new AdvancedSearchDao();
		List<Map<String, String>> list = dao.searchIPDData(queryParam);
		PageModel pageModel=new PageModel();
		pageModel.setPerPage(rows);
		pageModel.setCurrentPage(page);
		int count = list.size();
		int total = count % rows == 0 ? count /rows : count / rows + 1;
		pageModel.setTotalPage(total);
		pageModel.setTotalRecord(count);
		list=list.subList((page-1)*rows, page*rows>count?count:page*rows);
		pageModel.setList(list);
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
	public String getQueryParam() {
		return queryParam;
	}
	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
}
