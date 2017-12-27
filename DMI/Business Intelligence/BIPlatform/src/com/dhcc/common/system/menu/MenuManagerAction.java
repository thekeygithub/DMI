package com.dhcc.common.system.menu;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.ServletActionContext;

import com.dhcc.modal.system.Tsmenu;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;

/**
 * 新写的菜单管理action
 * @author GYR
 *
 */
public class MenuManagerAction extends ActionSupport{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String tid;//传入进来的顶级菜单的id
	private String menuCStr = "";//菜单字符串
	private List<MenuAuModel> list = new ArrayList<MenuAuModel>();
	private String roleid;//当前角色的id ，用于查询该角色所拥有的菜单
	
	/**
	 * 用户所拥有的顶级菜单查询
	 */
	public String topMenuQueryList(){
		MenuDao mdn = new MenuDao();
		String userid = (String)ActionContext.getContext().getSession().get("userid");//得到用户id
		List list = mdn.topMenuQueryList(userid);
		PrintWriter pw = null;
		try {
			JSONArray json = JSONArray.fromObject(list);//把list转换成json
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		mdn= null;
		return SUCCESS;
	}
	
	/**
	 * 所有的顶级菜单查询
	 */
	public String topMenuQueryListForAll(){
		MenuDao mdn = new MenuDao();
		List list = mdn.topMenuQueryListForAll();
		PrintWriter pw = null;
		try {
			JSONArray json = JSONArray.fromObject(list);//把list转换成json
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		mdn= null;
		return SUCCESS;
	}
	
	/**
	 * 用户所拥有的二级、三级菜单查询
	 */
	public String menuQueryList(){
		this.menuList(tid);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(menuCStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		return SUCCESS;
	}
	
	/**
	 * 用户所拥有的二级、三级菜单查询
	 */
	private void menuList(String pid){
		MenuDao mdn = new MenuDao();
		String userid = (String)ActionContext.getContext().getSession().get("userid");//得到用户id
		List<Tsmenu> list = mdn.menuQueryList(userid,pid);
		menuStrSC(list,pid);
		mdn= null;
	}
	
	/**
	 * 生成前台所需要的菜单字符串
	 */
	private String menuStrSC(List<Tsmenu> list, String ppID) {
		String menuStr = "";
		for (int i = 0; i < list.size(); i++) {
			String id = ((Tsmenu) list.get(i)).getId();
			String text = ((Tsmenu) list.get(i)).getTitle();
			String url = ((Tsmenu) list.get(i)).getUrl();
			String pID = ((Tsmenu) list.get(i)).getPid();
			String img = ((Tsmenu) list.get(i)).getImage();
			if (ppID.equals(tid)) {
					menuCStr = menuCStr + "<div title='" + text + "' id='" + id
							+ "'><div style=\" height:7px;\"></div>";
					this.menuList(id);
					menuCStr = menuCStr + "</div>";
			} else {
				if (pID.equals(ppID)) {
					menuCStr = menuCStr
								+ "<a class=\"l-link\" href=\"javascript:f_addTab('"
								+ id + "','" + text + "','" + url + "')\"><image style='margin-top:4px;float:left' src='"+img+"'/><span>" + text
								+ "</span></a> ";
				}
			}

		}
		return menuStr;
	}

	/**
	 * 角色权限管理页面的菜单管理
	 */
	public String menuListForAu(){
		MenuDao mdn = new MenuDao();
		List<MenuAuModel> lis = mdn.menuQueryForAu();
		List<MenuAuModel> lisMenuAllForUser = this.menuQueryByTopIdAu(tid, lis, new ArrayList<String>(),new ArrayList<MenuAuModel>());
		List<String> listMenuForUser = mdn.getAllMenuByRole(roleid);
		list = this.listAddData(lisMenuAllForUser,listMenuForUser);
		mdn = null;
		return SUCCESS;
	}
	
	/**
	 * 递归查询菜单下所有的子菜单
	 * @param pid 要查询菜单的父菜单id
	 * @param list 当前所有的菜单集合 
	 * @param listid 保存已有的菜单的id集合，防止重复数据
	 * @param setlist  保存菜单数据集合
	 */
	private List<MenuAuModel> menuQueryByTopIdAu(String pid,List<MenuAuModel> list,List<String> listid,List<MenuAuModel> setlist){
		Set<String> setIdS = new HashSet<String>();
		for(MenuAuModel m:list){
			String id = m.getId();
			String ppid = m.getPid();
			if(id.equals(pid) || ppid.equals(pid)){
				if(!listid.contains(id)){
					setlist.add(m);
					setIdS.add(id);
					listid.add(id);
				}
			}
		}
		if(setIdS.size()>0){
			for(String menuId : setIdS){
				if(!menuId.equals(pid)){
					menuQueryByTopIdAu(menuId,list,listid,setlist);
				}
			}
		}
		return setlist;
	}
	/**
	 * 给list填充数据
	 * 判断该角色是否拥有该权限
	 * lis 当前菜单集合，lists 用户所拥有的菜单id集合
	 * @param lis
	 */
	private List<MenuAuModel> listAddData(List<MenuAuModel> lis,List<String> lists){
		MenuAuModel mm = null;
		List<MenuAuModel> listTemp = new ArrayList<MenuAuModel>();
		if(lis != null){
			for(int i=0;i<lis.size();i++){
				String id = ((MenuAuModel) lis.get(i)).getId();
				String text = ((MenuAuModel) lis.get(i)).getTitle();
				String ordernum = ((MenuAuModel) lis.get(i)).getOrdernum()+"";
				String pID = ((MenuAuModel) lis.get(i)).getPid();
				String btnStr = ((MenuAuModel) lis.get(i)).getBtnStr();
				mm = new MenuAuModel();
				mm.setId(id);
				mm.setTitle(text);
				mm.setOrdernum(ordernum);
				mm.setPid(pID);
				mm.setBtnStr(btnStr);
				if(lists.contains(id)){
					mm.setFlag("1");
				}else{
					mm.setFlag("0");
				}
				listTemp.add(mm);
			}
		}
		return listTemp;
	}
	
	/**
	 * 查询子菜单列表
	 */
	public String treeMenuQuery(){
		MenuDao dao = new MenuDao();
		String userid = (String)ActionContext.getContext().getSession().get("userid");//得到用户id
		List<Tsmenu> listMenu1 = dao.menuQueryListForUser(userid);
		List<Tsmenu> modelList = this.menuQueryByTopId(tid, listMenu1,new ArrayList<String>(),new ArrayList<Tsmenu>());
		JSONArray json=JSONArray.fromObject(modelList);
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
	 * 递归查询菜单下所有的子菜单
	 * @param pid 要查询菜单的父菜单id
	 * @param list 当前所有的菜单集合 
	 * @param listid 保存已有的菜单的id集合，防止重复数据
	 * @param setlist  保存菜单数据集合
	 */
	private List<Tsmenu> menuQueryByTopId(String pid,List<Tsmenu> list,List<String> listid,List<Tsmenu> setlist){
			Set<String> setIdS = new HashSet<String>();
			for(Tsmenu m:list){
				String id = m.getId();
				String ppid = m.getPid();
				if(id.equals(pid) || ppid.equals(pid)){
					if(!listid.contains(id)){
						setlist.add(m);
						setIdS.add(id);
						listid.add(id);
					}
				}
			}
		if(setIdS.size()>0){
			for(String menuId : setIdS){
				if(!menuId.equals(pid)){
					menuQueryByTopId(menuId,list,listid,setlist);
				}
			}
		}
		return setlist;
	}
	
	/**
	 * 用户所拥有的顶级菜单查询
	 */
	public String topMenuQueryTree(){
		MenuDao mdn = new MenuDao();
		List list = mdn.menuQueryForTree("");
		PrintWriter pw = null;
		try {
			JSONArray json = JSONArray.fromObject(list);//把list转换成json
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		mdn= null;
		return SUCCESS;
	}
	
	/**
	 * 查询菜单的图片
	 */
	public String menuIconQuery(){
		List<String> list = this.readListIcon();
		PrintWriter pw = null;
		try {
			JSONArray json = JSONArray.fromObject(list);//把list转换成json
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
	
	/**
	 * 获取特定目录下的图片路径，放入list中
	 */
	private  List<String> readListIcon(){
		String path = ServletActionContext.getServletContext().getRealPath("images/Icon");
		List<String> listIcon = new ArrayList<String>();
		File file = new File(path);
		if(file.exists()){
			if(file.isDirectory()){
				File[] listFile = file.listFiles();
				for(File temp:listFile){
					String tempPath = "images/Icon/" + temp.getName();
					listIcon.add(tempPath);
				}
			}
		}
		return listIcon;
	}
	
	
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMenuCStr() {
		return menuCStr;
	}

	public void setMenuCStr(String menuCStr) {
		this.menuCStr = menuCStr;
	}

	public List getList() {
		return list;
	}
	public void setList(List<MenuAuModel> list) {
		this.list = list;
	}
	
	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	
}
