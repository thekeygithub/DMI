package com.dhcc.common.system.menu;

import com.dhcc.common.util.CommUtil;
import com.dhcc.modal.system.Tsmenu;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 管理菜单的action类，有增、删、查、改 目前只有二级菜单的生成方法
 * 
 * @author GYR
 */
public class MenuManageAction extends ActionSupport {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String ids;// 前台传过来的id的字符串，用‘，’来分割
	private Tsmenu modal;//菜单模型
	private String id;// 查询单个信息的id
	private String result;//处理结果


	/**
	 * 根据菜单id删除菜单
	 */
	public String menuDel() {
		MenuDao dao = new MenuDao();
		if (ids != null && !ids.equals("")) {
			result = dao.menuDelete(ids.split(","));
		}
		return SUCCESS;
	}

	/**
	 * 添加菜单信息方法
	 */
	public String menuAdd() {
		MenuDao dao = new MenuDao();
		String id = CommUtil.getID();
		modal.setId(id);
		boolean b = dao.menuAdd(modal);
		if(b){
			result = "success";
		}
		return SUCCESS;
	}
	
	/**
	 * 根据id获取单个的菜单信息
	 */
	public String menugetById() {
		MenuDao dao = new MenuDao();
		modal = dao.menuQueryById(id);
		return SUCCESS;
	}

	/**
	 * 更新菜单信息方法
	 */
	public String menuUpdate() {
		MenuDao dao = new MenuDao();
		boolean b = dao.menuModify(modal);
		if(b){
			result = "success";
		}
		return SUCCESS;
	}
    
	/**
	 *@作者 GYR
	 *@日期 Oct 14, 2014 3:12:40 PM
	 *@描述 查询该菜单下是否有子菜单
	 *@return false:没有 true 有
	 */
	/**
	 * 更新菜单信息方法
	 */
	public String menuQueryHasChildren() {
		MenuDao dao = new MenuDao();
		if (ids != null && !ids.equals("")) {
			result = dao.menuQueryHasChildren(ids.split(","));
		}
		return SUCCESS;
	}
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Tsmenu getModal() {
		return modal;
	}

	public void setModal(Tsmenu modal) {
		this.modal = modal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
