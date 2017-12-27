package com.dhcc.common.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dhcc.modal.system.PageModel;
import com.opensymphony.xwork2.ActionSupport;
/**
 * @描述：增删改查详细信息的模板Action
 * @作者：SZ
 * @时间：2014-11-18 下午02:16:47
 * @param <Anytype> 和数据库表对应的model
 * @param <AnytypeMore> 用于查询的扩展model
 */
public class AnyTypeAction<Anytype,AnytypeMore> extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private AnyTypeDao<Anytype,AnytypeMore> dao = new AnyTypeDao<Anytype,AnytypeMore>();
	
	private Integer page; // 当前页码
	private Integer total; // 总页数
	private Integer pagesize; // 当前页显示的数据行数
	private Integer record;// 当前数据条数
	private List<AnytypeMore> listmodel = new ArrayList<AnytypeMore>();
	/**
	 * @描述：列表查询
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:08:31
	 * @param anytypeMore 用于查询的扩展model
	 * @param querySql 列表查询的sql
	 * @return
	 */
	public String List(AnytypeMore anytypeMore,String querySql){
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = dao.QueryList(pm,anytypeMore,querySql);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodel = pm.getList();
		return SUCCESS;
	}
	/**
	 * @描述：添加信息
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:09:06
	 * @param anyType 和数据库表对应的model
	 * @param anyTypeMore 用于查询的扩展model
	 * @param querySql 添加后页面添加一行的查询sql
	 * @param sysLogTitle 日志名称
	 * @param sysLogContent 日志内容
	 * @return
	 */
	public String Add(Anytype anyType,AnytypeMore anyTypeMore, String querySql,String sysLogTitle,String sysLogContent){
		boolean result = false;
		result = dao.Add(anyType,sysLogTitle,sysLogContent);
		JSONObject json;
		if(result){
			json = JSONObject.fromObject(dao.QueryById(anyTypeMore,querySql));
		}else{
			json = JSONObject.fromObject("{\"result\":\"error\"}");
		}
		setJSON(json);
		
		return SUCCESS;
	}
	/**
	 * @描述：修改信息
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:10:17
	 * @param anyType 和数据库表对应的model
	 * @param anyTypeMore 用于查询的扩展model
	 * @param querySql 修改后页面修改一行的查询sql
	 * @param sysLogTitle 日志名称
	 * @param sysLogContent 日志内容
	 * @return
	 */
	public String Updept(Anytype anyType,AnytypeMore anyTypeMore,String querySql,String sysLogTitle,String sysLogContent){
		boolean result = false;
		result = dao.Update(anyType,sysLogTitle,sysLogContent);
		JSONObject json;
		if(result){
			json = JSONObject.fromObject(dao.QueryById(anyTypeMore,querySql));
		}else{
			json = JSONObject.fromObject("{\"result\":\"error\"}");
		}
		setJSON(json);
		
		return SUCCESS;
	}
	/**
	 * @描述：删除信息
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:11:03
	 * @param ids 要删除的信息id串用","隔开
	 * @param tableName 要删除的表名称
	 * @param sysLogTitle 日志名称
	 * @param sysLogContent 日志内容
	 * @return
	 */
	public  String  Del(String ids,String tableName,String sysLogTitle){
		boolean result = false;
		if (ids != null && !ids.equals("")) {
			result = dao.Del(ids.split(","),tableName,sysLogTitle);
		}
		JSONObject json;
		if(result){
			json = JSONObject.fromObject("{\"result\":\"success\"}");
		}else{
			json = JSONObject.fromObject("{\"result\":\"error\"}");
		}
		setJSON(json);

		return SUCCESS;
	}
	/**
	 * @描述：详细信息查询
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:12:11
	 * @param anyTypeMore 用于查询的扩展model
	 * @param querySql 用于详细信息查询的sql
	 * @return
	 */
	public String Mess(AnytypeMore anyTypeMore,String querySql){
		AnytypeMore anytypeModel = dao.QueryById(anyTypeMore,querySql);
		JSONObject json=JSONObject.fromObject(anytypeModel);
		setJSON(json);
		return SUCCESS;
	}
	private void setJSON(JSONObject json){
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			PrintWriter pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	public Integer getRecord() {
		return record;
	}
	public void setRecord(Integer record) {
		this.record = record;
	}
	public List<AnytypeMore> getListmodel() {
		return listmodel;
	}
	public void setListmodel(List<AnytypeMore> listmodel) {
		this.listmodel = listmodel;
	}
}
