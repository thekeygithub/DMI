package com.dhcc.common.system.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.system.user.UserQueryModel;
import com.dhcc.modal.system.PageModel;
import com.opensymphony.xwork2.ActionSupport;



public class TreeManageAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private TreeDao dao = new TreeDao();

	private List<UserQueryModel> listmodal = new ArrayList<UserQueryModel>();// 前台的数据集合
	private Integer page; // 当前页码
	private Integer total; // 总页数
	private Integer pagesize; // 当前页显示的数据行数
	private Integer record;// 当前数据条数
	private String corpid;//树形结构查询人的部门或公司id
	private String username;//用户姓名
	private String ids;//查询过滤 的人员的id字符串 用“，”分割
	private String  sortname = "username";// 排序列名
	private String  sortorder = "asc";// 排序方向 asc
	private String showsubdivision;//是否显示子部门人员
	/**
	 * 树形结构根据单位id查询所属人员
	 */
	public String execute() throws Exception {
		PageModel pm = new PageModel();
		pm.setCurrentPage(page);
		pm.setPerPage(pagesize);
		pm = dao.userQueryList(pm, corpid, username,ids,sortname,sortorder,showsubdivision);
		record = pm.getTotalRecord();
		total = pm.getTotalPage();
		listmodal = pm.getList();
		return SUCCESS;
	}
	/**
	 * 通过单位id查询对应职务信息
	 * @作者 SZ
	 * @日期 2014 2:03:43 PM
	 * @return
	 */
	
	public String QueryPostByDept(){
		JSONArray  json=JSONArray.fromObject(dao.queryPostByCDid(corpid));
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
	
	/**
	 * 通过单位id查询对应岗位信息
	 * @作者 SZ
	 * @日期 2014 2:03:43 PM
	 * @return
	 */
	public String QueryStationByDept(){
		JSONArray  json=JSONArray.fromObject(dao.queryStationByCDid(corpid));
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
	
	/**
	 * 通过单位id查询对应权限信息
	 * @作者 SZ
	 * @日期 2014 2:03:43 PM
	 * @return
	 */
	public String QueryRoleByDept(){
		JSONArray  json=JSONArray.fromObject(dao.queryRoleByCDid(corpid));
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
	/**
	 * @描述：通过单位id查询对应所属区域信息
	 * @作者：SZ
	 * @时间：2014-12-15 下午02:48:27
	 * @return
	 */
	
	public String QueryAreaByDept(){
		JSONArray  json=JSONArray.fromObject(dao.queryAreaByCDid(corpid));
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
	/**
	 * @描述：通过单位id查询topcorpid
	 * @作者：SZ
	 * @时间：2014-12-16 上午11:19:42
	 * @return
	 */
	public String QueryTopCorpId(){
		String topcorpid = dao.queryTopCorpId(corpid);
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		try {
			pw = ServletActionContext.getResponse().getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.write(topcorpid);
		pw.flush();
		pw.close();
		return SUCCESS;
	}
	public List<UserQueryModel> getListmodal() {
		return listmodal;
	}

	public void setListmodal(List<UserQueryModel> listmodal) {
		this.listmodal = listmodal;
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
	public String getCorpid() {
		return corpid;
	}
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getSortname() {
		return sortname;
	}
	public void setSortname(String sortname) {
		this.sortname = sortname;
	}
	public String getSortorder() {
		return sortorder;
	}
	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}
	public String getShowsubdivision() {
		return showsubdivision;
	}
	public void setShowsubdivision(String showsubdivision) {
		this.showsubdivision = showsubdivision;
	}
}
