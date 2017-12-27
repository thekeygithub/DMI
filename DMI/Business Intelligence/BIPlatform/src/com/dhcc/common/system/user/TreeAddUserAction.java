package com.dhcc.common.system.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.util.CommUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.TreeModal;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**
 * 
 * @作者 SZ
 * @日期 2014 2:58:38 PM
 *
 */
public class TreeAddUserAction extends ActionSupport implements ModelDriven<UserQueryModel>{

	private static final long serialVersionUID = 1L;
	private TreeAddUserDao dao = new TreeAddUserDao();
	private UserQueryModel model = new UserQueryModel();// 前台的数据集合
	
	/**
	 * 
	 * @作者 SZ
	 * @日期 2014 3:01:20 PM
	 * @return
	 */
	public String  userSaveOrUpdate(){
		boolean result = false;
		if(StringUtil.isNullOrEmpty(model.getId())){
			//String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
			//model.setTopcorpid(topcorpid);//顶级部门id为所选部门的等级id，admin账户也可以添加人员了
			model.setId(CommUtil.getID());
			result = dao.addUser(model);
		}else{
			result = dao.updateUser(model);
		}
		JSONObject json;
		if(result){
			json = JSONObject.fromObject(model);
		}else{
			json = JSONObject.fromObject("{\"result\":\"error\"}");
		}
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			PrintWriter pw=ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * 删除用户信息
	 * @作者 SZ
	 * @日期 2014 4:23:23 PM
	 * @return
	 */
	private String ids;//要删除的用户id串
	public  String  userDel(){
		if (ids != null && !ids.equals("")) {
			dao.userDel(ids.split(","));
		}
		return SUCCESS;
	}
	
	/**
	 * 验证用户名是否存在
	 * @作者 SZ
	 * @日期 2014 4:28:10 PM
	 * @return
	 * @throws Exception
	 */
	public String userExist() throws Exception {
		String querysql = "select * from tsuser where loginname = '" + model.getLoginname() + "'";
		boolean b = dao.userExist(querysql);
		String str = "NEXIST";
		if(b){
			str="EXIST";
		}
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		PrintWriter pw=ServletActionContext.getResponse().getWriter();
		pw.write(str);
		pw.flush();
		pw.close();
		return SUCCESS;
	}
	/**
	 * 根据id查询用户详细信息
	 * @作者 SZ
	 * @日期 2014 5:04:16 PM
	 * @return
	 */
	public String UserQueryById(){
		UserQueryModel uqm = dao.useQueryById(model.getId());
		JSONObject json=JSONObject.fromObject(uqm);
		PrintWriter pw=null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.flush();
		pw.close();
		
		return SUCCESS;
	}
	
	/**
	 *@作者 GYR
	 *@日期 Oct 21, 2014 1:19:28 PM
	 *@描述 修改当前用户的密码
	 *@return
	 */
	public String  modifyPass(){
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			boolean b = dao.modifyPass(model.getUserpass(), userid);
			String str = "error";
			if(b){
				str = "success";
			}
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			PrintWriter pw = null;
			try {
				pw = ServletActionContext.getResponse().getWriter();
			} catch (IOException e) {
				e.printStackTrace();
			}
			pw.write(str);
			pw.flush();
			pw.close();
		return SUCCESS;
	}
	
	public String dcidTreeQuery(){
		List<TreeModal> list = dao.QueryDicdAll();
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
	
	public UserQueryModel getModel() {
		return model;
	}

	public void setModel(UserQueryModel model) {
		this.model = model;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	
}
