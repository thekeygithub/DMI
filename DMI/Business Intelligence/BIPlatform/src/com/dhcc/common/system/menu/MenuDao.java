package com.dhcc.common.system.menu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.util.CommDao;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.TreeModal;
import com.dhcc.modal.system.Tsmenu;
import com.opensymphony.xwork2.ActionContext;


public class MenuDao {
	private static final Logger logger = Logger.getLogger(MenuDao.class);
	/**
	 * @param pm
	 * @param sortname
	 * @param sortorder
	 * @return 
	 */
	public PageModel menuQueryList(PageModel pm,String sortname,String sortorder){
		DBManager dbm = new DBManager();
		try {
			String querysql = "select * from tsmenu where 1 = 1 order by "+sortname+" "+sortorder+" ";
			List list = dbm.getObjectList(Tsmenu.class, querysql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询菜单列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		
		return pm;
		
	}
    
    /**
     * 添加一个新的菜单项
     * @param menu
     * @return 执行结果
     */
    public  boolean  menuAdd(Tsmenu menu){
    	DBManager dbm = new DBManager();
    	boolean b=true;
		try {
			b = dbm.insertObject(menu, "tsmenu");
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("菜单添加", "添加ID为："+menu.getId()+"的菜单记录！");
			saveLog = null;
		} catch (Exception e) {
			logger.error(e);
		}finally{
    	  dbm.close();
		}
    	return b;
    }
    
    /**
     * 修改菜单信息
     * @param menu
     */
    public boolean  menuModify(Tsmenu menu){
    	DBManager dbm = new DBManager();
    	boolean b = false;
    	String sql = "";
    	String querysql = "";
		try {
			querysql = "select * from tsmenu where id = '"+menu.getId()+"'";
			Tsmenu obj = (Tsmenu) dbm.getObject(Tsmenu.class, querysql);
			if(!obj.getPid().equals(menu.getPid())){
				querysql = "select * from tsmenu";
				List<Tsmenu> menulist =  dbm.getObjectList(Tsmenu.class, querysql);//查询出所有的菜单
				List<Tsmenu> modelList = this.menuQueryByTopId(menu.getId(), menulist,new ArrayList<String>(),new ArrayList<Tsmenu>());//找出该菜单下的所有菜单
				for(Tsmenu temp:modelList){
					sql="delete from tslrolemenu where menuid = '"+temp.getId()+"'";
					dbm.addBatch(sql);
				}
				dbm.executeBatch();
			}
			b = dbm.updateObject(menu);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("菜单修改", "修改ID为："+menu.getId()+"的菜单记录！");
			saveLog = null;
		} catch (Exception e) {
			e.printStackTrace();
			return b;
		}finally{
			dbm.close();
    	}
		return b;
    }
    
    /**
     * 根据id得到单个的菜单信息
     * @param sql 查询的sql语句
     * @return 菜单
     */
    public Tsmenu menuQueryById(String id){
    	DBManager dbm = new DBManager();
    	String sql = "select * from tsmenu where id='"+id+"'";
    	Tsmenu menu = null;
		try {
			menu = (Tsmenu) dbm.getObject(Tsmenu.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			return menu;
		}finally{
    	   dbm.close();
		}
    	return menu;
    }
    
    /**
     * 
     * @param ids
     */
    protected void deleteMenu(String[] ids) {
    	for(String id:ids) {
    		deleteMenu(id);
		 }
	}
    /**
     * 通过id 删除菜单以及子菜单。
     * @param id
     */
    private  void  deleteMenu(String id){
    	 DBManager dbm = new DBManager();
    	 try {
			 String sql1 = "";
			 sql1 = "delete from tslrolemenu where menuid = '"+id+"'";
			 dbm.addBatch(sql1);
			 sql1 = "delete from tsmenu where id = '"+id+"'";
			 dbm.addBatch(sql1);
			 dbm.executeBatch();
			 String sql2 = "select * from tsmenu where pid = '"+id+"'"; 
			 List<Tsmenu> list = dbm.getObjectList(Tsmenu.class, sql2);
			 if(list!=null&&list.size()!=0){
				 for(Tsmenu temp:list){
					 deleteMenu(temp.getId());
				 }
			 }
		} catch (Exception e) {
			logger.error("删除菜单出错", e);
		}finally{
		    dbm.close();
		}
    }
    
    /**
     *@作者 GYR
     *@日期 Oct 14, 2014 3:07:44 PM
     *@描述 删除菜单
     *@param ids
     *@return
     */
	public String menuDelete(String[] ids){
		 DBManager dbm = new DBManager();
		 SaveLog saveLog = new SaveLog();
		 String sql1 = "";
	   	 try {
	   		 for(String id:ids){
				 sql1 = "delete from tslrolemenu where menuid = '"+id+"'";
				 dbm.addBatch(sql1);
				 sql1 = "delete from tsbtnresource where menuid = '"+id+"'";
				 dbm.addBatch(sql1);
				 sql1 = "delete from tsmenu where id = '"+id+"'";
				 dbm.addBatch(sql1);
				 saveLog.saveLog("菜单删除", "删除ID为："+id+"的菜单记录！");
	   		 }
			 dbm.executeBatch();
		} catch (Exception e) {
			logger.error("删除菜单出错", e);
			return "error";
		}finally{
		    dbm.close();
		}
		return "success";
	}
	
    /**
     * 用户所拥有的一级菜单查询
     */
	public List<Tsmenu> topMenuQueryList(String userid){
		DBManager dbm = new DBManager();
		List<Tsmenu> list = null;
		try {
			String querysql= " select DISTINCT m.id,m.title,m.pid,m.image,m.url,m.actiontype,m.ordernum " +
					" from tsuser u " +
					" left join tsluserrole ur on  u.id=ur.userid " +
					" left join tsrole r on   r.id=ur.roleid  " +
					" left join tslrolemenu rm on rm.roleid=r.id " +
					" left join tsmenu m  on m.id = rm.menuid " +
					" where u.id='"+userid+"' and m.pid='0' and m.title!= 'null' order by m.ordernum";
			list = dbm.getObjectList(Tsmenu.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
		
	}
	
    /**
     * 用户所拥有的二级、三级菜单查询
     */
	public List<Tsmenu> menuQueryList(String userid,String pid){
		DBManager dbm = new DBManager();
		List<Tsmenu> list = null;
		try {
			String querysql= "select m.title,m.id,m.pid,m.image,m.url,m.actiontype " +
					" from tsuser u " +
					" left join tsluserrole ur on  u.id=ur.userid " +
					" left join tsrole r on   r.id=ur.roleid  " +
					" left join tslrolemenu rm on rm.roleid=r.id " +
					" left join tsmenu m  on m.id = rm.menuid " +
					" where u.id='"+userid+"' and m.pid='"+pid+"' and m.title!= 'null' group by m.id order by m.ordernum";
			list = dbm.getObjectList(Tsmenu.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
		
	}
	
	 /**
     * 用户所拥有的二级、三级菜单查询
     */
	public List<Tsmenu> menuQueryList1(String userid,String pid){
		DBManager dbm = new DBManager();
		List<Tsmenu> list = null;
		try {
			String querysql= "select m.title,m.id,m.pid,m.image,m.url,m.actiontype " +
					" from tsuser u " +
					" left join tsluserrole ur on  u.id=ur.userid " +
					" left join tsrole r on   r.id=ur.roleid  " +
					" left join tslrolemenu rm on rm.roleid=r.id " +
					" left join tsmenu m  on m.id = rm.menuid " +
					" where u.id='"+userid+"' and m.id='"+pid+"' and m.title!= 'null' group by m.id order by m.ordernum";
			list = dbm.getObjectList(Tsmenu.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
		
	}
	
	/**
     * 一级菜单查询
     */
	public List<Tsmenu> topMenuQueryListForAll(){
		DBManager dbm = new DBManager();
		List<Tsmenu> list = null;
		try {
			String querysql = "select m.title,m.id,m.pid,m.image,m.ordernum,m.url,m.actiontype from tsmenu m ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			if(userid.equals(superUserId)){//数据权限控制
				querysql += " where m.pid = '0' and m.title!= 'null' order by m.ordernum ";
			}else{
				querysql += " where  m.pid = '0' and m.title!= 'null' and m.whetherpublic='01'  order by m.ordernum ";
			}
			list = dbm.getObjectList(Tsmenu.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
	
	/**
	 * 授权时候查询菜单列表
	 */
	public List<MenuAuModel> menuQueryForAu(String pid){
		DBManager dbm = new DBManager();
		List<MenuAuModel> list = null;
		try {
			String querysql= "select m.title,m.id,m.pid,m.ordernum,group_concat(tsb.id,';',tsb.resourcename) as btnStr " +
					" from tsmenu m " +
					" left join tsbtnresource tsb on tsb.menuid = m.id" +
					" where  m.id = '"+pid+"' and m.title!= 'null' group by m.id order by m.ordernum,tsb.btnsort";
			list = dbm.getObjectList(MenuAuModel.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
	
	/**
	 * 授权时候查询菜单列表
	 */
	public List<MenuAuModel> menuQueryForAu1(String pid){
		DBManager dbm = new DBManager();
		List<MenuAuModel> list = null;
		try {
			String querysql = "select m.title,m.id,m.pid,m.ordernum,group_concat(tsb.id,';',tsb.resourcename) as btnStr " +
						" from tsmenu m " +
						" left join tsbtnresource tsb on tsb.menuid = m.id" +
						" where  m.pid='"+pid+"' and m.title != 'null' group by m.id order by m.ordernum,tsb.btnsort ";
			list = dbm.getObjectList(MenuAuModel.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}


	/**
	 * 查询角色所拥有的权限访问的菜单
	 * 
	 * @return list
	 */
	public List getAllMenuByRole(String roleId){
		List list=new ArrayList();
		String sql="select * from tslrolemenu where roleid='"+roleId+"'";
		DBManager dbm=new DBManager();
		ResultSet rs=dbm.executeQuery(sql);
		try {
			while(rs.next()){
				list.add(rs.getString("menuid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dbm.close();
		}
		return list;
	}
	
	/**
	 * 全部菜单树形查询
	 */
	public List<TreeModal> menuQueryForTree(String condition){
		DBManager dbm = new DBManager();
		List<TreeModal> list = null;
		try {
			String querysql= "select m.title as text,m.id as id,m.pid as pid,m.image as icon,m.url as url " +
					" from tsmenu m  order by m.ordernum";
			list = dbm.getObjectList(TreeModal.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
    
	/**
	 *@作者 GYR
	 *@日期 Oct 14, 2014 3:14:48 PM
	 *@描述 查询菜单下是否含有子菜单
	 *@param id
	 */
	public String menuQueryHasChildren(String ids[]){
		DBManager dbm = new DBManager();
		List list = null;
		try {
			String querysql = "select m.title as text,m.id as id,m.pid as pid,m.image as icon,m.url as url " +
					" from tsmenu m  where m.pid in ("+changeStr(ids)+")";
			list = dbm.getObjectList(TreeModal.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}finally{
			dbm.close();
		}
		if(list != null){
			if(list.size() == 0){
				return "false";
			}else{
				return "true";
			}
		}else{
			return "false";
		}
	}
	/**
	 *@作者 GYR
	 *@日期 Oct 14, 2014 3:28:40 PM
	 *@描述 处理sql条件 把字符串数组 装成 '1','2' 字符串
	 *@param strArr
	 *@return
	 */
	private String changeStr(String[] strArr){
		String temp = "";
		if(strArr != null){
			for(int i=0;i<strArr.length;i++){
				temp = temp +"'"+ strArr[i] + "'";
				if(i<strArr.length-1){
					temp +=  ",";
				}
			}
		}
		return temp;
	}
    
	
	/**
     * 查询用户所拥有的全部菜单列表查询
     */
	public List<Tsmenu> menuQueryListForUser(String userid){
		DBManager dbm = new DBManager();
		List<Tsmenu> list = null;
		try {
			String querysql= " select DISTINCT m.id,m.title,m.pid,m.image,m.ordernum,m.url,m.actiontype,m.ordernum " +
					" from tsuser u " +
					" left join tsluserrole ur on  u.id=ur.userid " +
					" left join tsrole r on   r.id=ur.roleid  " +
					" left join tslrolemenu rm on rm.roleid=r.id " +
					" left join tsmenu m  on m.id = rm.menuid " +
					" where u.id='"+userid+"'  and m.title!= 'null' order by m.ordernum";
			list = dbm.getObjectList(Tsmenu.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
    
	/**
	 * 角色授权时查询所有菜单列表
	 */
	public List<MenuAuModel> menuQueryForAu(){
		DBManager dbm = new DBManager();
		List<MenuAuModel> list = null;
		try {
			String querysql = "select m.title,m.id,m.pid,m.ordernum,listagg(tsb.id||';'||tsb.resourcename, ',') within group( order by  m.ordernum,tsb.btnsort) as btnStr " +
			" from tsmenu m  left join tsbtnresource tsb on tsb.menuid = m.id ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			if(userid.equals(superUserId)){//数据权限控制
				querysql += " where m.title!= 'null' group by m.title,m.id,m.pid,m.ordernum ";
			}else{
				querysql += " where m.title!= 'null' and m.whetherpublic='01' group by m.title,m.id,m.pid,m.ordernum";
			}
			
			List<MenuAuModel> listmodel = dbm.getObjectList(MenuAuModel.class, querysql);
			list = new ArrayList<MenuAuModel>();
			for(MenuAuModel model : listmodel){
				if(model.getBtnStr().startsWith(";")){
					model.setBtnStr("");
				}
				list.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
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
}
