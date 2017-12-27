package com.dhcc.common.system.portal;

import java.util.ArrayList;
import java.util.List;

import com.dhcc.common.util.CommUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tsportalres;
import com.opensymphony.xwork2.ActionSupport;



/**
 * @author GYR
 * 门户资源信息管理ACTION
 */
public class SysPortalManager extends ActionSupport{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String ids;//删除时id的字符串 ，用“，”分割
	private String id;//id
	private Tsportalres model;
	private String result;
	
	/**
	 * 列表属性
	 */
	private List listmodal = new ArrayList();// 前台的数据集合
	private int page; // 当前页码
	private int total; // 
	private int pagesize; // 当前页显示的数据行数
	private int record;// 当前数据条数
	
	/**
	 *@作者 GYR
	 *@日期 Aug 19, 2014 4:50:45 PM
	 *@描述 门户资源列表查询
	 *@throws Exception
	 */
	public String queryPortalList() throws Exception {
		SysPortalDao md = new SysPortalDao();
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = md.sysPortalQueryList(pm);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodal = pm.getList();
		return SUCCESS;
	}
	
	/**
	 * 删除根据id
	 */
	public String sysPortalDelete() {
		SysPortalDao dao = new SysPortalDao();
		if (ids != null && !ids.equals("")) {
			dao.sysPortalDel(ids.split(","));
		}
		dao = null;
		return SUCCESS;
	}
	
	/**
	 *@作者 GYR
	 *@日期 Aug 20, 2014 9:39:34 AM
	 *@描述 添加门户资源信息
	 */
	public String sysPortalAdd(){
		SysPortalDao dao = new SysPortalDao();
		model.setId(CommUtil.getID());
		boolean b = dao.addInfo(model);
		if(b){
			result = "success";
		}else{
			result = "error";
		}
		dao = null;
		return SUCCESS;
	}
	
	/**
	 *@作者 GYR
	 *@日期 Aug 20, 2014 9:39:34 AM
	 *@描述 修改门户资源信息
	 */
	public String sysPortalUpdate(){
		SysPortalDao dao = new SysPortalDao();
		boolean b = dao.modifyInfo(model);
		if(b){
			result = "success";
		}else{
			result = "error";
		}
		dao = null;
		return SUCCESS;
	}
	
	/**
	 *@作者 GYR
	 *@日期 Aug 20, 2014 9:39:34 AM
	 *@描述 根据ID查询门户资源信息
	 */
	public String sysPortalQueryById(){
		SysPortalDao dao = new SysPortalDao();
		model = dao.queryInfoById(id);
		if(model != null ){
			result = "success";
		}else{
			result = "error";
		}
		dao = null;
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
	
	public List getListmodal() {
		return listmodal;
	}
	public void setListmodal(List listmodal) {
		this.listmodal = listmodal;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getRecord() {
		return record;
	}
	public void setRecord(int record) {
		this.record = record;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Tsportalres getModel() {
		return model;
	}

	public void setModel(Tsportalres model) {
		this.model = model;
	}
	
	
}
