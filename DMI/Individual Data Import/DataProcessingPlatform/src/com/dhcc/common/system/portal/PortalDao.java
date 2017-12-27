package com.dhcc.common.system.portal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dhcc.common.database.DBManager;
import com.dhcc.modal.system.CommModel;


/**
 * 系统门户对应的dao
 * @author GYR
 *
 */
public class PortalDao {
	private List<String>  listid = new ArrayList<String>();//
	/**
	 *@作者 GYR
	 *@日期 Aug 12, 2014 4:54:42 PM
	 *@描述 新版的
	 *@param userid
	 */
	public List<PortalModel> portalQueryByUserid(String userid){
		List<PortalModel>  list1 = new ArrayList<PortalModel>();
		List<NewPortalModel> list = null;//根据userid查询 集合
		List<Tspanel>  ts1 = null;
		List<Tsportal> ts2 = null;
		DBManager dbm = new DBManager();
		String sql = "select * from tsportal  where userid = '"+userid+"' order by rowIndex,columnIndex,pindex,ctime";
		try{
			list = dbm.getObjectList(NewPortalModel.class, sql);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		
		if(!list.isEmpty()){
			Tsportal temp1 = null;
			listid.clear();
			ts2 = new ArrayList<Tsportal>();
			for(NewPortalModel temp:list){
				if(!listid.contains(temp.getId())){
					Map<String,List> map =  portalQueryByUserid2(temp.getColumnIndex(),userid);
					ts1 = map.get("panel");
					List<Tsportal> li = map.get("portal");
					if(!li.isEmpty()){
						temp1 = li.get(0);
					}
					temp1.setPanels(ts1);
					ts2.add(temp1);
				}
			}
			list1.add(new PortalModel(ts2));
		}
		return list1;
	}
	
	
	/**
	 *@作者 GYR
	 *@日期 Aug 6, 2014 3:01:15 PM
	 *@描述 根据id 查询panel
	 *@param id
	 */
	private Map<String,List> portalQueryByUserid2(String columnIndex,String userid){
		Map<String,List> map = new HashMap<String, List>();
		DBManager dbm = new DBManager();
		List<Tspanel>  ts1 = null;
		List<Tsportal> ts2 = null;
		List<Tsportal> ts3 = null;
		String sql = "select  t.id,t.columnIndex,t.rowIndex,t.title,t.url,t.width,t.pheight as height  from tsportal t where t.userid = '"+userid+"' and t.columnIndex = '"+columnIndex+"'  order by t.rowIndex,t.columnIndex,t.pindex,t.ctime";
		try {
			ts1  =  dbm.getObjectList(Tspanel.class, sql);
			ts2  =  dbm.getObjectList(Tsportal.class, sql);
			if(ts2 != null){
				for(Tsportal temp:ts2){
					String sql1 = "select  t.id,t.columnIndex,t.rowIndex,t.width  from tsportal t where t.id = '"+temp.getId()+"'";
					ts3  =  dbm.getObjectList(Tsportal.class, sql1);
					listid.add(temp.getId());
				}
			}
			map.put("panel", ts1);
			map.put("portal", ts3);
		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}finally{
			dbm.close();
		}
		return map;
	}
		
	/**
	 * 更新门户布局信息
	 * 返回执行结果
	 */
	public String  updatePortal(String userid,List<NewPortalModel> list) {
		String temp = "error";
		int[] flag = null;
		DBManager dbm = new DBManager();
		String sql = "delete from tsportal where userid = '"+userid+"'";
		dbm.addBatch(sql);
		try {
			if(list != null){
				if(!list.isEmpty()){
					for(NewPortalModel model:list){
						String ctime = "" + new Date().getTime();//当前创建时间
						sql = "insert into tsportal(id,userid,width,rowIndex,columnIndex,pindex,title,url,ctime,pheight)" +
								"VALUES('"+model.getId()+"','"+model.getUserid()+"','"+model.getWidth()+"'," +
										"'"+model.getRowIndex()+"','"+model.getColumnIndex()+"','"+model.getIndex()+"','"+model.getTitle()+"','"+model.getUrl()+"','"+ctime+"','"+model.getPheight()+"');";
						dbm.addBatch(sql);
					}
				}
			}
			flag = dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return temp;
		}finally{
			dbm.close();
		}
		if(flag == null){
			return "error";
		}
		return "success";
	}
	
	/**
	 * 添加门户布局信息
	 * 返回执行结果
	 */
	public String  addPortal(String userid,NewPortalModel model) {
		String temp = "error";
		int[] flag = null;
		DBManager dbm = new DBManager();
		String sql = "";
		try {
			String ctime = "" + new Date().getTime();//当前创建时间
			sql = "insert into tsportal(id,userid,width,rowIndex,columnIndex,pindex,title,url,ctime,pheight)" +
					"VALUES('"+model.getId()+"','"+model.getUserid()+"','"+model.getWidth()+"'," +
							"'"+model.getRowIndex()+"','"+model.getColumnIndex()+"','"+model.getIndex()+"','"+model.getTitle()+"','"+model.getUrl()+"','"+ctime+"','"+model.getPheight()+"');";
			dbm.addBatch(sql);
			flag = dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return temp;
		}finally{
			dbm.close();
		}
		if(flag == null){
			return "error";
		}
		return "success";
	}
	
	/**
	 *@作者 GYR
	 *@日期 Aug 19, 2014 4:19:04 PM
	 *@描述 
	 */
	protected List queryPortalSelect() {
		DBManager dbm = new DBManager();
		List list = null;
		String sql = "select t.url as id,t.pname as text from tsportalres t order by t.pname";
		
		try {
			list = dbm.getObjectList(CommModel.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}finally{
			dbm.close();
		}
		return list;
	}

}
