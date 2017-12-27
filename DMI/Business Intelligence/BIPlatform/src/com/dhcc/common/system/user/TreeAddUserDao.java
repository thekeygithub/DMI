package com.dhcc.common.system.user;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.util.DateUtil;
import com.dhcc.common.util.HashUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.TreeModal;
import com.dhcc.modal.system.Tsconfig;
import com.dhcc.modal.system.Tscorp;
import com.dhcc.modal.system.Tspost;
import com.dhcc.modal.system.Tsrole;
import com.dhcc.modal.system.Tsstation;
import com.dhcc.modal.system.Tsuser;
import com.opensymphony.xwork2.ActionContext;


public class TreeAddUserDao {
	private final static Logger logger=Logger.getLogger(TreeDao.class);
	/**
	 * 添加用户
	 * @作者 SZ
	 * @日期 2014 3:07:56 PM
	 * @param model
	 * model.getDeptid()是公司或部门id
	 */
	public boolean addUser(UserQueryModel model){
	    DBManager dbm=new DBManager();
	    String birthDate = model.getBirth();
		long birthday = DateUtil.toTime(birthDate);
	    boolean result = false;
	    try {
			Tsuser tsusermodel = new Tsuser();
	    	tsusermodel.setId(model.getId());
	    	tsusermodel.setBirth(birthday + "");//用户出生年月
	    	tsusermodel.setArea(model.getArea());//所属区域
	    	tsusermodel.setEmailprivate(model.getEmailprivate());
	    	tsusermodel.setEmailpublic(model.getEmailpublic());
	    	tsusermodel.setLoginname(model.getLoginname());
	    	tsusermodel.setMobileprivate(model.getMobileprivate());
	    	tsusermodel.setMobilepublic(model.getMobilepublic());
	    	tsusermodel.setPhoneprivate(model.getPhoneprivate());
	    	tsusermodel.setPhonepublic(model.getPhonepublic());
	    	tsusermodel.setRemark(model.getRemark());
	    	tsusermodel.setSex(model.getSex());
	    	tsusermodel.setUserpass(HashUtil.hashCode(model.getUserpass()));
	    	tsusermodel.setUsername(model.getUsername());
	    	tsusermodel.setDcid(model.getDcid());
			
	    	String sql = "select * from tscorp where 1=1";
	    	List<Tscorp> listcorp = dbm.getObjectList(Tscorp.class,sql);
	    	
	    	tsusermodel.setTopcorpid(QueryTopCorpid(model.getDeptid(), listcorp));
	    	
	    	if(addUserTSLtable(model.getId(), model.getDeptid(), model.getStationid(), model.getPostid(), model.getRoleid())){
	    		result = dbm.insertObject(tsusermodel, "tsuser");//基本信息入库
	    	}
	    	
	    	SaveLog saveLog = new SaveLog();
			saveLog.saveLog("添加用户信息", "添加ID为"+ model.getId() +"的用户信息记录！");
			saveLog = null;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			logger.info("插入用户出错*********");
		}finally{
			dbm.close();
		}
		return result;
	}
	/**
	 * 添加用户关联表信息
	 * @作者 SZ
	 * @日期 2014 11:14:23 AM
	 */
	public boolean addUserTSLtable(String id,String cropid,String stationid,String postid,String roleid ){
		DBManager dbm=new DBManager();
		boolean result = false;
		String sql="";
		try{
	    	//用户单位关联表
			String strcropid[] = cropid.split(";");
			for(String s : strcropid){
				sql = "insert into tslusercorp(userid,corpid) values('"+id+"','"+s+"')";
				dbm.addBatch(sql);
			}
			//用户岗位关联表
			String strStation[] = stationid.split(";");
			for (String s : strStation) {
				sql = String.format("insert into tsluserstation(userid,stationid) values('%s','%s')",id, s);
				dbm.addBatch(sql);
			}
			//用户职务关联表
			String strPost[] = postid.split(";");
			for (String s : strPost) {
				sql = String.format("insert into tsluserpost(userid,postid) values('%s','%s')",id, s);
				dbm.addBatch(sql);
			}
			//用户权限关联表
			String strRole[] = roleid.split(";");
			for (String s : strRole) {
				sql = String.format("insert into tsluserrole(userid,roleid) values('%s','%s')",id, s);
				dbm.addBatch(sql);
			}
			
			dbm.executeBatch();
			result = true;
		}catch(Exception e){
			result = false;
			e.printStackTrace();
			logger.info("插入用户关联信息表出错*********");
		}finally{
			dbm.close();
		}
		return result;
	}
	/**
	    * 删除用户
	    * 并且删除系统内用户相关的信息
	    */
	public void  userDel(String[] ids){
		DBManager dbm = new DBManager();
		String sql="";
		SaveLog saveLog = new SaveLog();
	    try {
			for (int i = 0; i < ids.length; i++) {
				if (!StringUtil.isNullOrEmpty(ids[i])) {
					sql = "delete from tsuser where id='" + ids[i] + "';";
					dbm.addBatch(sql);
					saveLog.saveLog("删除用户信息", "删除ID为"+ ids[i] +"的用户信息记录！");
				}
			}
			dbm.addBatch("delete from tsluserpost where userid not in (select id from  tsuser)");
			dbm.addBatch("delete from tsluserrole where userid not in (select id from  tsuser)");
			dbm.addBatch("delete from tsluserstation where userid not in (select id from  tsuser)");
			dbm.addBatch("delete from tslusercorp where userid not in (select id from  tsuser)");
			dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
   }
	/**
	 * 根据id查询用户详细信息
	 * @作者 SZ
	 * @日期 2014 5:05:56 PM
	 * @param id
	 * @return
	 */
	public UserQueryModel useQueryById(String id){
		UserQueryModel um = new UserQueryModel();
		DBManager dbm = new DBManager();
		String sql = "select tu.*,tsd.areaname as areaname,rsdc.name as dcidname " +
				" from tsuser tu " +
				" left join tsarea tsd on tsd.id=tu.area " +
				" left join rsdatacenter rsdc on rsdc.id = tu.dcid " +
				" where tu.id='"+id+"'";
		um = (UserQueryModel) dbm.getObject(UserQueryModel.class,sql);
		
		String sql1="select * from tsrole  where id in (select roleid from tsluserrole  where userid='" + id + "')";
		String sql2="select * from tspost  where id in (select postid from tsluserpost  where userid='" + id + "')";
		String sql3="select * from tsstation  where id in (select stationid from tsluserstation  where userid='" + id + "')";
		String sql4="select * from tscorp  where id in (select corpid from tslusercorp  where userid='" + id + "')  order by type ";
		List<Tsrole> listrole=dbm.getObjectList(Tsrole.class, sql1);
		List<Tspost> listpost=dbm.getObjectList(Tspost.class, sql2);
		List<Tsstation> liststat=dbm.getObjectList(Tsstation.class, sql3);
		List<Tscorp> listdept=dbm.getObjectList(Tscorp.class, sql4);
		List<Tscorp> listcorp = dbm.getObjectList(Tscorp.class, "select * from tscorp where 1=1");
		
		String corpname = "";//单位中的公司名称（详情）
		String corpid = "";//单位中的公司名称（详情）
		String deptname = "";//单位中的部门名称
		String deptid = "";//单位中的部门id
		HashSet<String> setlist = new HashSet<String>();
		for(int i=0;i<listdept.size();i++){
			deptid+=listdept.get(i).getId();
			deptname+=listdept.get(i).getCorpname();
			setlist.add(findCorpName(listdept.get(i).getId(), listcorp));//获取部门的所属公司在setlist里面去重
			if(i<listdept.size()-1){
				deptid+=";";
				deptname+=";";
			}
			if(listdept.get(i).getType()==2){
				corpid+=listdept.get(i).getCorpname();
				if(i<listdept.size()-1){
					corpid+=";";
				}
			}
		}
		um.setDeptid(deptid);
		um.setDeptname(deptname);
		
		//为详情使用的公司名称和部门名称
		Iterator<String> it = setlist.iterator();
		int step = 0;
		while (it.hasNext()) {
			corpname += it.next();
			if(step<setlist.size()-1){
				corpname+=";";
			}
			step+=1;
		}
		um.setCorpid(corpid);
		um.setCorpname(corpname);
		
		String rolename="";
		String roleid="";
		for(int i=0;i<listrole.size();i++){
			roleid+=listrole.get(i).getId();
			rolename+=listrole.get(i).getRolename();
			if(i<listrole.size()-1){
				roleid+=";";
				rolename+=";";
			}
		}
		um.setRolename(rolename);
		um.setRoleid(roleid);
		
		String statname="";
		String statid="";
		for(int i=0;i<liststat.size();i++){
			statid+=liststat.get(i).getId();
			statname+=liststat.get(i).getStationname();
			if(i<liststat.size()-1){
				statid+=";";
				statname+=";";
			}
		}
		um.setStationid(statid);
		um.setStationname(statname);
		
		String postname="";
		String postid="";
		for(int i=0;i<listpost.size();i++){
			postid+=listpost.get(i).getId();
			postname+=listpost.get(i).getPostname();
			if(i<listpost.size()-1){
				postid+=";";
				postname+=";";
			}
		}
		um.setPostid(postid);
		um.setPostname(postname);
		
		
		dbm.close();
		return um;
	}
	/**
	 * 修改用户
	 * @作者 SZ
	 * @日期 2014 4:31:40 PM
	 * @param model
	 */
	public boolean updateUser(UserQueryModel model){
		DBManager dbm = new DBManager();
		String birthDate = model.getBirth();
		long birthday = DateUtil.toTime(birthDate);
		boolean result = false;
		try {
			String sql = String.format(
					"update tsuser set username='%s',sex='%s',mobileprivate='%s',phoneprivate='%s',"
					+ "emailprivate='%s',emailpublic='%s',mobilepublic='%s',phonepublic='%s',birth='%s',area='%s',remark='%s',dcid='%s' where id='%s'", 
					model.getUsername(), model.getSex(), model.getMobileprivate(),model.getPhoneprivate(), model.getEmailprivate(),
					model.getEmailpublic(),model.getMobilepublic(),model.getPhonepublic(),birthday, model.getArea(), model.getRemark(),model.getDcid().trim(),
					model.getId())+"";
			dbm.addBatch(sql);
			sql = "delete from tsluserpost where userid='"+ model.getId() + "'";
			dbm.addBatch(sql);
			sql = "delete from tsluserrole where userid='"+ model.getId() + "'";
			dbm.addBatch(sql);
			sql = "delete from tsluserstation where userid='"+ model.getId() + "'";
			dbm.addBatch(sql);
			sql = "delete from tslusercorp where userid='"+ model.getId() + "'";
			dbm.addBatch(sql);
			dbm.executeBatch();
			if(addUserTSLtable(model.getId(), model.getDeptid(), model.getStationid(), model.getPostid(), model.getRoleid())){
				result = true;
			}
			
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("修改用户信息", "修改ID为"+ model.getId() +"的用户信息记录！");
			saveLog = null;
		} catch (Exception e) {
			result = false;
			logger.error("ModifyNewUser--失败", e);
		} finally {
			dbm.close();
		}
		return result;
	}
	
	/**
	 * 查看表中是否已经存在该用户名 
	 * @作者 SZ
	 * @日期 2014 4:29:52 PM
	 * @param sql
	 * @return 查询结果 true 为存在，false 不存在
	 */
	public boolean  userExist(String sql){
		boolean b=false;
		DBManager dbm=new DBManager();
		List list=dbm.getObjectList(Tsuser.class, sql);
		if(list!=null&&list.size()!=0){
			b=true;
		}
		dbm.close();
		return b;
	}
	
	/**
	 * @描述：根据部门id获取公司名称
	 * @作者：SZ
	 * @时间：2014-10-17 下午04:04:54
	 * @param id 单位中部门id
	 * @param list 单位集合list
	 * @return
	 */
	public String findCorpName(String id,List<Tscorp> list){
		for(Tscorp a:list){
			if(id.equals(a.getId())){
				if(a.getType()==2){
//					System.out.println("是部门:"+a.getCorpname());
					return findCorpName(a.getPid(),list);
				}else{
//					System.out.println("是公司:"+a.getCorpname());
					return a.getCorpname();
				}
			}
		}
		return "";
	}
	
	/**
	  * 修改用户密码
	  * @param newpass 新的密码
	  * @param userid
	  */
	public boolean modifyPass(String newpass,String userid){
		DBManager dbm = new DBManager();
		String sql = "update tsuser set userpass='"+HashUtil.hashCode(newpass)+"' where id='"+userid+"';";
		dbm.addBatch(sql);
		int i[];
		try {
			i = dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			 dbm.close();
		}
		if(i.length == 1){
			return true;
		}
		return false;
	}
	/**
	 * @描述：根据单位id获取顶级公司名称
	 * @作者：SZ
	 * @时间：2014-10-17 下午04:04:54
	 * @param id 单位id
	 * @param list 单位集合list
	 * @return
	 */
	public String findTopCorpName(String id,List<Tscorp> list){
		for(Tscorp a:list){
			if(id.equals(a.getId())){
				if("0".equals(a.getPid())){
//					System.out.println("顶级公司:"+a.getCorpname());
					return a.getCorpname();
				}else{
//					System.out.println("是普通公司:"+a.getCorpname());
					return findTopCorpName(a.getPid(),list);
				}
			}
		}
		return "";
	}
	/**
	 * @描述：根据部门id获取顶级公司id(数据权限控制，用户不显示pid为0的数据，但是职务，权限，岗位都在0下面)
	 * @作者：SZ
	 * @时间：2014-12-12 下午02:07:20
	 * @param corpid部门id
	 * @param listcorp部门集合
	 * @return
	 */
	public String QueryTopCorpid(String corpid,List<Tscorp> listcorp){
		String[] id = corpid.split(";");//可能是多个部门，默认都在一个顶级公司下
		String pid = "";
		for(Tscorp model : listcorp){
			if(model.getId().equals(id[0])){
				pid = model.getPid();
				break;
			}
		}
		if("0".equals(pid)){
			return id[0];
		}else{
			return QueryTopCorpid(pid, listcorp);
		}
	}
	
	public  List<TreeModal>  QueryDicdAll(){
		DBManager dbm=new DBManager();
		List<TreeModal> list = null;
		String querysql = "select id as id,'' as pid,name as text from rsdatacenter ";
		try {
			list = dbm.getObjectList(TreeModal.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
}
