//package com.znyy.sys.user.action;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletResponse;
//
//import net.sf.json.JSONObject;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.struts2.ServletActionContext;
//
//import com.common.action.CommAction;
//import com.common.redis.IRedisUtil;
//import com.common.redis.RedisKeys;
//import com.common.util.JsonUtils;
//import com.common.util.MD5;
//import com.common.util.Page;
//import com.common.util.Util;
//import com.znyy.bean.XtUnit;
//import com.znyy.bean.XtUser;
//import com.znyy.bean.XtUserFile;
//import com.znyy.sys.function.service.FunctionService;
//import com.znyy.sys.role.service.RoleService;
//import com.znyy.sys.unit.service.UnitService;
//import com.znyy.sys.user.service.PermissionService;
//import com.znyy.sys.user.service.UserPerService;
//import com.znyy.sys.user.service.UserService;
//import com.znyy.sys.user.service.UserdictionaryService;
//
///**
// * @类名称: UserAction
// * @类描述:用户action
// * @作者:慕金剑
// * @创建时间:2015-12-7 下午12:51:35
// */
//
//public class UserAction extends CommAction {
//	private UserService userService;
//	private FunctionService functionService;
//	private RoleService roleService;
//	private UserdictionaryService userDictionaryService;
//	private UnitService unitSer;
//	private PermissionService permissionService;
//	private UserPerService userPerService;
//	private static final long serialVersionUID = 1L;
//	private Integer pageSize=8;
//	private String page;
//	private String rows;
//	private String userid;
//	private String pwd;
//	private String name;
//	private String unitid;
//	private String roleid;
//	private String id;
//	private String czflag;
//	private String zzflag;
//	private String tel;
//	private String address;
//	private String ghnum;
//	private String hzprice;
//	private String teamid;
//	private String sex;
//	private String flag;
//	private String info;
//	private String present;
//	private String avltime;
//	private String sysid;
//	private String username;
//	private String[] fileFileName;//文件名
//	private String[] fileContentType;// 封装文件类型
//	private File[] file;// 对应文件域的file，封装文件内容
//	private File[] img;
//	private String[] imgFileName;//文件名
//	private String[] imgContentType;// 封装文件类型
//	private XtUser user;
//	private List<XtUser> users;
//	private List<XtUnit> units;
//	private List<Map<String, Object>> userDics;
//	private String oldpsw;
//	private String newpsw;
//	private String[] title;// 文件标题
//	private ServletContext context;
//	private String filename;
//	private String mimeType;
//	private List<Map<String, Object>> doczcs;
//	private String systemid;
//    //存放用户附件下载
//	private InputStream getLoadUserFileStream;
//	//存放用户头像下载
//	private InputStream getLoadUserPhotoStream;
//	private JSONObject result;
//	private XtUserFile xtfile;
//	private XtUser leader;
//	//批量转正userids
//	private String userids;
//	//复选框选择部门返回type&id
//	private String ckunits;
//	private List<Map<String, Object>> listmap;
//	
//	private IRedisUtil redisUtil;
//
//	/**
//	 *存放当前系统的所有权限
//	 */
//	private List<Map<String, Object>> allPerList;
//	 /**
//	 * 存放用户所拥有的权限id
//	 */
//	private List<String> userPerList;
//
//	private List<String> updateUserPers;
//	//菜单id，查询模块权限时使用
//	private Integer funid;
//	 /**
//	 * 被继承用户id
//	 */
//	private String extendedid;
//	 /**
//	 * 继承用户id
//	 */
//	private String extendid;
//	//用户选择控件中使用start
//	 /**
//	 * 是否单选
//	 */
//	private String isRadio;
//	 /**
//	 * 排除的用户id
//	 */
//	private String notIn;
//	private List<String> userNames;
//	////用户选择控件中使用end
//	public String index() throws UnsupportedEncodingException {
//		String result = "success";
//		request.setAttribute("info", info);
//		return result;
//	}
//	/**
//	 * @方法名称: dbIndex
//	 * @功能描述: 代办提醒首页
//	 * @作者:秦向红
//	 * @创建时间:2015-12-7 下午2:20:43
//	 * @return String
//	 */
//	public String getDbIndex(){
//		return SUCCESS;
//	}
//	/**
//	 * @方法名称: getDzzUserList
//	 * @功能描述: 需要提醒的待转正人员列表（一周）
//	 * @作者:秦向红
//	 * @创建时间:2015-12-7 下午2:21:26
//	 * @return String
//	 */
//	public String getDzzUserList(){
//		String result="success";
//		if (page == null || "".equals(page)) {
//			page = "1";
//		}
//		int parmpage = Integer.parseInt(page);
//		Page pageUserList = userService.getDzzUserPage(username,parmpage, pageSize);
//		request.setAttribute("totalP", pageUserList.getTotalPageCount());
//		request.setAttribute("totalN", pageUserList.getTotalCount());
//		request.setAttribute("pageSize", pageSize);
//		request.setAttribute("page", parmpage);
//		request.setAttribute("user", user);
//		users =  pageUserList.getData();
//		return result;
//	}
//	/**
//	 * 
//	 * @方法名称: getDtxUserList
//	 * @功能描述: 获取需要提醒的待退休员工列表（一个月）
//	 * @作者:秦向红
//	 * @创建时间:2015-12-8 上午10:23:34
//	 * @return String
//	 */
//	public String getDtxUserList(){
//		String result="success";
//		if (page == null || "".equals(page)) {
//			page = "1";
//		}
//		int parmpage = Integer.parseInt(page);
//		Page pageUserList = userService.getDtxUserPage(username,parmpage, pageSize);
//		request.setAttribute("totalP", pageUserList.getTotalPageCount());
//		request.setAttribute("totalN", pageUserList.getTotalCount());
//		request.setAttribute("pageSize", pageSize);
//		request.setAttribute("page", parmpage);
//		request.setAttribute("user", user);
//		users =  pageUserList.getData();
//		return result;
//	}
//	
//	/**
//	 * 
//	 * @方法名称: getUserInfo
//	 * @功能描述: 获取员工信息
//	 * @作者:秦向红
//	 * @创建时间:2015-12-9 下午1:58:43
//	 * @return String
//	 */
//	public String getUserInfo(){
//		String result="success";
//		String id=request.getParameter("id");
//		Map<String, Object> user=userService.getMapUserById(id);
//		request.setAttribute("user", user);
//		return result;
//	}
//	
//	/**
//	 * 
//	 * @方法名称: showUserInfoback
//	 * @功能描述: 多个功能共用一个员工信息页面，返回按钮返回不同页面
//	 * @作者:秦向红
//	 * @创建时间:2015-12-14 下午1:12:25
//	 * @return String
//	 */
//	public String showUserInfoback(){
//		String result="success";
//		if("1".equals(flag)){//flag=1,人事档案列表页面
//			result="lbUserList";
//		}else if("2".equals(flag)){//flag=2，待转正人员列表页面
//			result="dzzUserList";
//		}else if("3".equals(flag)){//flag=3，待退休人员列表页面
//			result="dtxUserList";
//		}else if("4".equals(flag)){//flag=4，员工权限维护页面
//			result="findUserPerList";
//		}
//		return result;
//	}
//
//	/**
//	 * @方法名称: login
//	 * @功能描述: 用户登录时调用
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-11 上午9:10:46
//	 * @return String   
//	 */
//	public String login() {
//		String result = "success";
//		listmap = userService.getUser(userid, pwd);
//		if (listmap == null) {
//			result = "error";
//			info = "用户名和密码错误";
//		} else {
//			session.put("userid", userid);
//			session.put("name", listmap.get(0).get("NAME"));
//			session.put("sysid","4");
//			List<Map<String, Object>> units =  userService.selectUnitByUserid(userid);
//			if(units.size()>1){
//				request.setAttribute("units",units);
//				result="moreUnit";
//			}else if(units.size()==1){
//				Map map = units.get(0);
//				String unitid = ((Object)map.get("TID")).toString();
//				//System.out.println("科室ID："+unitid);
//				session.put("unitid", unitid);
//			}
//		}
//		return result;
//	}
//	 /**
//		 * @方法名称: loginUnit
//		 * @功能描述: 选择科室跳转到index_main页面
//		 * @作者:詹怀远
//		 * @创建时间:2015-12-22 下午1:52:31
//		 * @return String   
//		 */
//		 
//		public String loginUnit(){
//			//
//			Short unitId = new Short(unitid);  
//			session.put("unitid", unitid);
//			return SUCCESS; 
//		}
//	 /**
//	 * @方法名称: findMenu
//	 * @功能描述: 根据当前登录人，当前系统id获取该用户的菜单
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-11 上午9:19:56
//	 * @return String   
//	 * @throws Exception 
//	 */
//	public String findMenu(){
//		try{
//			sysid=(String)this.session.get("sysid");
//			userid=(String)this.session.get("userid");
//			//根据登录用户获取用户具体哪些模块的权限
//			List<Map<String, Object>> functions=null;
//			//获取该人员的左侧菜单缓存
//			String cache = redisUtil.getLeftMenusData(sysid,userid);
//			//判断是否存在左侧菜单缓存
//			if(cache==null){
//				//左侧菜单缓存不存在时，添加左侧菜单缓存
//				functions = functionService.selectMenu(sysid, userid);
//				redisUtil.setLeftMenusData(sysid,userid, functions);
//				request.setAttribute("isCache","off");
//				request.setAttribute("menus", functions);
//			}else{
//				request.setAttribute("isCache","on");
//				request.setAttribute("cacheMenus",cache);
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "success";
//	}
//	
//	/**
//	 * 
//	 * @方法名称: modPwdTo
//	 * @功能描述: 跳转到修改密码的页面
//	 * @作者:王爽
//	 * @创建时间:2015-12-10 上午9:08:38
//	 * @return String
//	 */
//	public String modPwdTo(){
//		return SUCCESS;
//	}
//	
//	/**
//	 * 
//	 * @方法名称: checkOldpsw
//	 * @功能描述: 修改密码时，验证原密码是否输入的正确
//	 * @作者:秦向红
//	 * @创建时间:2015-12-11 下午2:30:53
//	 * @throws IOException void
//	 */
//	public void checkOldpsw() throws IOException{
//		String result="";
//		String userid=(String) session.get("userid");
//		Map<String, Object> pwd1=userService.getUserPwdById(userid);
//		String oldpswMD5=MD5.convert(oldpsw);
//		if(pwd1.get("PWD").equals(oldpswMD5)){
//			result="0";
//		}else{
//			result="1";
//		}
//		response.setContentType("text/html;charset=UTF-8");
//		PrintWriter out = response.getWriter();
//		out.println(result);
//		out.flush();
//		out.close();
//	}
//	
//	/**
//	 * @方法名称: savePwd
//	 * @功能描述: 保存修改后的密码
//	 * @作者:王爽
//	 * @创建时间:2015-12-10 上午9:09:16
//	 * @return String
//	 */
//	public String savePwd(){
//		String result = "success";
//		try {
//			String id=(String)session.get("userid");
//			String pwdMD5=MD5.convert(newpsw);
//			userService.modUserPwdList(id,pwdMD5);
//			info="修改密码成功！请重新登录";
//			flag="0";
//		} catch (Exception e) {
//			info="修改密码失败！";
//			result="error";
//			flag="1";
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
//	/**
//	 * @方法名称: queryUser
//	 * @功能描述: 查看个人信息
//	 * @作者:王爽
//	 * @创建时间:2015-12-10 上午9:09:38
//	 * @return String
//	 */
//	public String queryUser(){
//		String result="success";
//		String userid=(String) session.get("userid");
//		Map<String, Object> user= userService.getMapUserById(userid);
//		request.setAttribute("user", user);
//		return result;
//	}
//
//	/**
//	 * @方法名称: addEmp
//	 * @功能描述:添加人事档案记录 
//	 * @作者:杜月
//	 * @创建时间:2015-12-8 下午12:00:38
//	 * @return String
//	 */
//	public String addEmp(){
//		try {
//			String[] chkValues = ckunits.split(","); 
//			String	typeandid=chkValues[0];
//			String userid1=	typeandid.substring(typeandid.indexOf(".")+1,typeandid.length());
//			String userid2=	userService.getSeqXtUser();
//			String str = "000000" + userid2;
//			str = str.substring(str.length() - 6);
//			String adduserid=userid1+str;
//			user.setUserid(adduserid);
//			String CurUser = (String)session.get("userid");
//			String userid=	userService.addEmp(user,ckunits,img,imgFileName,imgContentType,CurUser,file,fileFileName,fileContentType);
//			info="添加成功 员工编号为:"+userid;
//			return SUCCESS;
//		} catch (Exception e) {
//			info="添加失败";
//			//添加失败因为序列不能回滚问题，导致照片无法删除，需手动删除
//			if(img!=null){
//				String str2=imgFileName[0].substring(imgFileName[0].indexOf("."), imgFileName[0].length());
//				String imgName=user.getUserid()+str2;
//			    String url = ServletActionContext.getServletContext().getRealPath("/photos/"+imgName);
//			     File  file = new File(url);  
//			        // 判断文件是否存在  
//			        if (file.exists()) {  
//			            if (file.isFile()) {  // 为文件时调用删除文件方法  
//			            	 file.delete(); 
//			            } 
//			        } 
//			}
//			e.printStackTrace();
//			return SUCCESS;
//		}
//	}
//	
//	/**
//	 * @方法名称: getUserMZandKS
//	 * @功能描述: 获取民族,用户信息,还有职称
//	 * @作者:杜月
//	 * @创建时间:2015-12-9 上午11:13:47
//	 * @return String
//	 */
//	public String getUserMZandKS(){
//		doczcs=userService.getDoczcList();
//		userDics=userDictionaryService.getUserDictionary();
//		return SUCCESS;
//	}
//	/**
//	 * @方法名称: selectUserByStatus
//	 * @功能描述: 查询在职并且未注销的用户
//	 * @作者:杨云博
//	 * @创建时间:2015-12-9 上午10:29:31
//	 * @return String   
//	 */
//	 
//	public String selectUserByStatus(){
//		listmap = userService.selectUserByStatus(roleid);
//		return SUCCESS;
//	}
//	/**
//	  * @方法名称: getUserPerTo
//	  * @功能描述: 跳转到用户修改权限页面
//	  * @作者:慕金剑
//	  * @创建时间:2015-12-27下午12:30:52
//	  * @return String
//	  */
//	public String getUserPerTo(){
//		String sysId ="";
//		if(null!=systemid&&!systemid.equals("")){
//			sysId = systemid;
//		}else{
//			sysId = (String)this.session.get("sysid");
//		}
//		List<Map<String, Object>> list = functionService.getFunctionList(sysId);
//		systemid = sysId;
//		request.setAttribute("list", JsonUtils.toJSON(list));
//		return SUCCESS;
//	}
//	 /**
//	 * @方法名称: getUserPer
//	 * @功能描述: 查询用户的权限
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-10 下午6:48:48
//	 * @return String   
//	 */
//	public String getUserPer(){
//		allPerList=functionService.getAllPerBysysid(systemid,userid,funid);
//		return SUCCESS;
//	}
//	/**
//	 * @方法名称: findUserPerList
//	 * @功能描述: 获取员工权限列表
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-9 上午10:18:38
//	 * @return String
//	 */
//	public String findUserPerList() {
//		String result = "success";
//		if (page == null || page.equals("")) {
//			page = "1";
//		}
//		int parmpage = Integer.parseInt(page);
//		if (pageSize == null) {
//			pageSize = 8;
//		}
//		Page pageLbUserList = userService.getLbUserList(username, parmpage, pageSize);
//		request.setAttribute("totalP", pageLbUserList.getTotalPageCount());
//		request.setAttribute("totalN", pageLbUserList.getTotalCount());
//		request.setAttribute("lbuserList", pageLbUserList.getData());
//		request.setAttribute("pageSize", pageSize);
//		request.setAttribute("page", parmpage);
//		return result;
//	}
//	
//	 /**
//	 * @方法名称: updateUserPer
//	 * @功能描述: 更新用户权限关系
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-10 下午6:54:33
//	 * @return String   
//	 */
//	public String updateUserPer(){
//		try {
//			//用户之前的权限
//			userPerList=permissionService.getUserPer(systemid, userid,funid);
//			List<String> addPers=new ArrayList<String>();
//			//遍历修改之后的权限
//			if(null!=updateUserPers&&updateUserPers.size()>0){
//			for(String uper:updateUserPers){
//				//修改前后权限相同
//				if(userPerList.contains(uper)){
//					userPerList.remove(uper);
//				}else{
//				//说明是新增的权限	
//				  addPers.add(uper);
//				}
//			}
//			}
//			userPerService.updateUserPer(addPers, userPerList, userid);
//			//清除该用户的左侧菜单缓存，方式1
//			StringBuffer str = new StringBuffer();
//			str.append(RedisKeys.getSysKey(systemid));
//			str.append(RedisKeys.LEFTMENUSPrefix);
//			str.append(userid);
//			redisUtil.delete(RedisKeys.SYSOA, str.toString());
//			//清除该用户的左侧菜单缓存，方式2
//			//重新添加左侧菜单缓存，覆盖之前的缓存
//			//List<Map<String, Object>> functions = functionService.selectMenu(systemid, userid);
//			//redisUtil.setLeftMenusData(systemid,userid, functions);
//			info="用户权限信息修改成功！";
//		} catch (Exception e) {
//			e.printStackTrace();
//			info="用户权限信息修改失败！";
//		}
//		return SUCCESS;
//	}
//	
//	
//
//	 /**
//	 * @方法名称: uploadExcel
//	 * @功能描述: excel的下载
//	 * @作者:孙鹏
//	 * @创建时间:2015-12-15 下午2:33:04
//	 * @return String   
//	 */
//	 
//	public void uploadExcel(){
//		try {
//			HSSFWorkbook webwork = userService.findWriterExcel(userid);
//			response.setHeader("Content-Disposition", "attachment;filename="
//					+ new String("人事档案录入.xls".getBytes("UTF-8"), "ISO8859-1"));
//			OutputStream outputStream = null;
//			outputStream = new BufferedOutputStream(response.getOutputStream());
//			response.setContentType("application/octet-stream;charset=ISO8859-1");
//			webwork.write(outputStream);
//			outputStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			
//		}
//	}
//	/**
//	 * @方法名称: getLbUserList
//	 * @功能描述: 获取人事档案列表
//	 * @作者:孙鹏
//	 * @创建时间:2015-12-9 上午10:18:38
//	 * @return String
//	 */
//	public String getLbUserList() {
//		String result = "success";
//		if (page == null || page.equals("")) {
//			page = "1";
//		}
//		int parmpage = Integer.parseInt(page);
//
//		if (pageSize == null) {
//			pageSize = 8;
//		}
//		Page pageLbUserList = userService.getLbUserList(username, parmpage, pageSize);
//		request.setAttribute("totalP", pageLbUserList.getTotalPageCount());
//		request.setAttribute("totalN", pageLbUserList.getTotalCount());
//		request.setAttribute("lbuserList", pageLbUserList.getData());
//		request.setAttribute("pageSize", pageSize);
//		request.setAttribute("page", parmpage);
//		return result;
//	}
//	
//	/**
//	 * @方法名称: modUserListTo
//	 * @功能描述: 修改用户列表跳转
//	 * @作者:孙鹏
//	 * @创建时间:2015-12-9 下午2:53:18
//	 * @return String
//	 */
//	public String modUserListTo() {
//		Map<String, Object> user = userService.getMapUserById(userid);
//		request.setAttribute("user", user);
//		userDics=userDictionaryService.getUserDictionary();
//		doczcs=userService.getDoczcList();
//		return "success";
//	}
//	/**
//	 * @方法名称: modUserList
//	 * @功能描述: 修改人事档案列表信息
//	 * @作者:孙鹏
//	 * @创建时间:2015-12-9 下午3:49:22
//	 * @return String
//	 */
//	public String modUserList() {
//		try {
//			userService.modUserListAss(user,img,imgFileName,imgContentType,ckunits);
//			info = "修改成功";
//		} catch (Exception e) {
//			e.printStackTrace();
//			info = "修改失败";
//		}
//		return "success";
//	}
//	
//	/**
//	 * @方法名称: zzUserList
//	 * @功能描述: 员工转正修改
//	 * @作者:孙鹏
//	 * @创建时间:2015-12-9 下午5:27:16
//	 * @return String
//	 */
//	public String zzUserList() {
//		Map<String, Object> xt = new HashMap<String, Object>();
//		xt.put("USERID", userid);
//		xt.put("STATUS", 1);
//		try {
//			userService.modStatus(xt);
//			info = "转正成功";
//		} catch (Exception e) {
//			e.printStackTrace();
//			info = " 转正失败";
//		}
//		return "success";
//	}
//	/**
//	 * 
//	 * @方法名称: batchZZ
//	 * @功能描述: 批量转正
//	 * @作者:杜月
//	 * @创建时间:2015-12-16 下午4:54:36
//	 * @return String
//	 */
//	public String modBatchZZ(){
//		if(null!=request.getParameter("userids")&&""!=request.getParameter("userids")){
//		String[] chkValues = request.getParameter("userids").split(","); 
//		for(String id:chkValues){
//			Map<String, Object> xt = new HashMap<String, Object>();
//			xt.put("USERID", id);
//			xt.put("STATUS", 1);
//			try {
//				userService.modStatus(xt);
//				info = "批量转正成功";
//			} catch (Exception e) {
//				e.printStackTrace();
//				info = " 批量转正失败";
//			}
//		}
//		}else{
//			info="请选择要转正的员工";
//		}
//		return SUCCESS;
//	}
//	/**
//	 * @方法名称: modUserList
//	 * @功能描述: 跳转到注销员工
//	 * @作者:慕金剑
//	 * @创建时间:2016-1-3 下午3:49:22
//	 * @return String
//	 */
//	public String delUserListTo() {
//			String sysid=(String)this.session.get("sysid");
//			List<String> perss=	permissionService.getUserPer(sysid,userid,null);
//			List<String> roles=roleService.selectRoleUser(null,userid);
//			info="将要删除的用户：";
//			if(perss.size()>0||roles.size()>0){
//				if(perss.size()>0){
//					info+="||拥有权限";
//				}
//				if(roles.size()>0){
//					info+="||拥有角色";
//				}
//				return INPUT;
//			}else{
//				return SUCCESS;
//			}
//	}
//	/**
//	 * @方法名称: delUserList
//	 * @功能描述: 员工的注销
//	 * @作者:孙鹏
//	 * @创建时间:2015-12-10 上午9:00:41
//	 * @return String
//	 */
//	public String delUserList() {
//		try {
//			userService.delUser("1", user.getDelbak(), userid);
//			info="注销成功";
//		} catch (Exception e) {
//			e.printStackTrace();
//			info = "注销失败";
//		}
//		return "success";
//	}
//	
//	/**
//	 * @方法名称: getShowFile
//	 * @功能描述: 展示所有附件列表
//	 * @作者:孙鹏
//	 * @创建时间:2015-12-10 下午3:41:23
//	 * @return String
//	 */
//	public String getShowFile() {
//		List<Map<String, Object>> list = userService.getShowFile(userid);
//		request.setAttribute("list", list);
//		return "success";
//	}
//	
//	 /**
//		 * @方法名称: delFile
//		 * @功能描述: 删除附件
//		 * @作者:孙鹏
//		 * @创建时间:2015-12-11 下午5:13:05
//		 * @param id
//		 * @return String   
//		 */
//		public String delFile(){
//			try {
//			List<Map<String, Object>> files=	userService.findByfileId(id);
//			String url=	files.get(0).get("URL").toString();
//			if(null!=url&&!"".equals(url)){
//		     url = ServletActionContext.getServletContext().getRealPath(url);
//		     File  file = new File(url);  
//		        // 判断文件是否存在  
//		        if (file.exists()) {  
//		            if (file.isFile()) {  // 为文件时调用删除文件方法  
//		            	 file.delete(); 
//		            	 info="删除成功";
//		            } 
//		        } else{
//		        	info="没有此文件";
//		        }
//				userService.delFile(id);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return "success";
//		}
//	
//	 /**
//		 * @方法名称: addOrMod
//		 * @功能描述: 添加或者修改附件
//		 * @作者:孙鹏
//		 * @创建时间:2015-12-11 下午6:23:19
//		 * @return String   
//		 * @throws Exception 
//		 */
//		public String addOrMod() {
//			//String CurUser=getCurUserid();
//			String CurUser=(String)this.session.get("userid");
//			try {
//				userService.addUserFile(userid, CurUser, file, fileFileName, fileContentType);
//				info="修改附件成功";
//			} catch (Exception e) {
//				e.printStackTrace();
//				info="修改附件失败";
//			}
//			return "success";
//		}
// 	 /**
// 	 * @方法名称: getUserPhoto
// 	 * @功能描述: 获取用户头像
// 	 * @作者:孙鹏
// 	 * @创建时间:2015-12-14 下午1:44:49
// 	 * @return String   
// 	 */
// 	public String getUserPhoto() {
// 		try {
// 			Map<String, Object> list = userService.getMapUserById(userid);
// 			String userphoto = Util.null2String(list.get("IMG_PATH"));
// 			if(!Util.isBlank(userphoto)){
// 			int n=userphoto.lastIndexOf("/");
// 			filename = new String(userphoto.substring(n+1).getBytes(), "ISO8859-1");
//			mimeType = context.getMimeType(userphoto);
//			getLoadUserPhotoStream = context.getResourceAsStream(userphoto);
//			if (getLoadUserPhotoStream == null) {
//				info="该文件不存在！";
//	 			return ERROR;
//	 		}
// 			}else{
// 				info="该用户没有照片！";
// 				return ERROR;
// 			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
// 		return SUCCESS;
// 	}
// 	/**
// 	 * @方法名称: getUserFile
// 	 * @功能描述: 获取用户附件
// 	 * @作者:孙鹏
// 	 * @创建时间:2015-12-14 下午1:44:49
// 	 * @return String   
// 	 */
// 	public String getUserFile() {
// 		try {
// 			List<Map<String, Object>> list = userService.findByfileId(id);
//			Map<String, Object> xx=list.get(0);
//			String name=xx.get("NAME").toString();
//			filename = new String(name.getBytes(), "ISO8859-1");
//			mimeType=context.getMimeType(name);
//			getLoadUserFileStream = context.getResourceAsStream(xx.get("URL").toString());
//			if (getLoadUserFileStream == null) {
//				info="该文件不存在！";
//	 			return ERROR;
//	 		}
// 		} catch (Exception e) {
//			e.printStackTrace();
//		}
// 		return SUCCESS;
// 	}
//	/**
//	 * @方法名称: addRoleByUserTo
//	 * @功能描述: 用户添加角色跳转
//	 * @作者:杨云博
//	 * @创建时间:2015-12-11 下午1:54:55
//	 * @return String   
//	 */
//	 
//	public String addRoleByUserTo(){
//		userNames = roleService.selectRoleUser(null, userid);
//		return SUCCESS;
//	}
//	
//	
//	 /**
//	 * @方法名称: delUserRole
//	 * @功能描述: 删除用户角色
//	 * @作者:杨云博
//	 * @创建时间:2015-12-11 下午2:18:10
//	 * @return String   
//	 */
//	 
//	public String delUserRole(){
//		try {
//			roleService.delRoleUser(roleid, userid);
//			//清除该用户所有系统的左侧菜单缓存
//			redisUtil.clearLeftMenusCache(userid);
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//		}
//		return SUCCESS;
//	}
//	 /**
//	 * @方法名称: addUserRole
//	 * @功能描述: 添加用户角色
//	 * @作者:杨云博
//	 * @创建时间:2015-12-11 下午2:20:07
//	 * @return String   
//	 */
//	 
//	public String addUserRole(){
//		String data="0";
//		try {
//			roleService.addRoleUser(roleid, userid,"user");
//			//清除该用户所有系统的左侧菜单缓存
//			redisUtil.clearLeftMenusCache(userid);
//		} catch (Exception e) {
//			e.getMessage();
//			data="1";
//		}
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("data", data);
//		result = JSONObject.fromObject(map);
//		return SUCCESS;
//	}
//
//	 /**
//	 * @方法名称: findextendUser
//	 * @功能描述: 跳转到继承用户页面
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-11 下午5:11:58
//	 * @return String   
//	 */
//	public String findextendUser(){
//		return SUCCESS;
//	}
//	
//	 /**
//	 * @方法名称: saveExtendUser
//	 * @功能描述: 保存继承用户权限信息
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-11 下午5:27:17
//	 * @return String   
//	 */
//	public String saveExtendUser(){
//	 try {
//		//获取被继承用户该系统下的所有权限
//		 userPerList=permissionService.getUserPer(systemid,extendedid,null);
//		 userPerService.saveExtendUser(systemid, extendid, userPerList);
//		 info="继承成功！";
//	} catch (Exception e) {
//		e.printStackTrace();
//		info="继承失败！";
//	}
//	 return SUCCESS;
//	}
//	
//	 /**
//	 * @方法名称: selectUser
//	 * @功能描述: 员工选择控件
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-14 下午3:56:02
//	 * @return String   
//	 */
//	public String selectUser(){
//		return SUCCESS;
//	}
//	 /**
//	 * @throws IOException 
//	 * @方法名称: getSelectUser
//	 * @功能描述:为员工选择控件提供查询数据
//	 * @作者:慕金剑
//	 * @创建时间:2015-12-14 下午4:30:30 void   
//	 */
//	public void getSelectUser() throws IOException{
//		Set<String> notinSet=null;
//		if(null!=notIn&&!"".equals(notIn.trim())){
//			String[] notinary=notIn.split(",");
//			notinSet = new HashSet<String>(Arrays.asList(notinary));
//		}
//		List<Map<String, Object>> users=userService.getUsers(unitid, name,notinSet);
//		String ss=JsonUtils.toJSON(users);
//		HttpServletResponse response = this.response;
//		response.setContentType("text/html;charset=utf-8");
//		PrintWriter out = response.getWriter();
//		out.print(ss);
//		out.flush();
//		out.close();
//		
//	}
//
//	public String getUserid() {
//		return userid;
//	}
//	public void setUserid(String userid) {
//		this.userid = userid;
//	}
//	public String getPwd() {
//		return pwd;
//	}
//	public void setPwd(String pwd) {
//		this.pwd = pwd;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getUnitid() {
//		return unitid;
//	}
//	public void setUnitid(String unitid) {
//		this.unitid = unitid;
//	}
//	public String getRoleid() {
//		return roleid;
//	}
//	public void setRoleid(String roleid) {
//		this.roleid = roleid;
//	}
//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
//	public String getPage() {
//		return page;
//	}
//	public void setPage(String page) {
//		this.page = page;
//	}
//	public String getRows() {
//		return rows;
//	}
//	public void setRows(String rows) {
//		this.rows = rows;
//	}
//	public String getCzflag() {
//		return czflag;
//	}
//	public void setCzflag(String czflag) {
//		this.czflag = czflag;
//	}
//	public String getZzflag() {
//		return zzflag;
//	}
//	public void setZzflag(String zzflag) {
//		this.zzflag = zzflag;
//	}
//	public String getTel() {
//		return tel;
//	}
//	public void setTel(String tel) {
//		this.tel = tel;
//	}
//	public String getAddress() {
//		return address;
//	}
//	public void setAddress(String address) {
//		this.address = address;
//	}
//	public String getGhnum() {
//		return ghnum;
//	}
//	public void setGhnum(String ghnum) {
//		this.ghnum = ghnum;
//	}
//	public String getHzprice() {
//		return hzprice;
//	}
//	public void setHzprice(String hzprice) {
//		this.hzprice = hzprice;
//	}
//	public String getTeamid() {
//		return teamid;
//	}
//	public void setTeamid(String teamid) {
//		this.teamid = teamid;
//	}
//	public String getSex() {
//		return sex;
//	}
//	public void setSex(String sex) {
//		this.sex = sex;
//	}
//	public String getFlag() {
//		return flag;
//	}
//	public void setFlag(String flag) {
//		this.flag = flag;
//	}
//	public String getInfo() {
//		return info;
//	}
//	public void setInfo(String info) {
//		this.info = info;
//	}
//	public String getAvltime() {
//		return avltime;
//	}
//	public void setAvltime(String avltime) {
//		this.avltime = avltime;
//	}
//	public String getPresent() {
//		return present;
//	}
//	public void setPresent(String present) {
//		this.present = present;
//	}
//	public Integer getPageSize() {
//		return pageSize;
//	}
//	public void setPageSize(Integer pageSize) {
//		this.pageSize = pageSize;
//	}
//	public XtUser getUser() {
//		return user;
//	}
//	public void setUser(XtUser user) {
//		this.user = user;
//	}
//	public List<XtUser> getUsers() {
//		return users;
//	}
//	public void setUsers(List<XtUser> users) {
//		this.users = users;
//	}
//	public String getUsername() {
//		return username;
//	}
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	public String getSysid() {
//		return sysid;
//	}
//	public void setSysid(String sysid) {
//		this.sysid = sysid;
//	}
//	
//	public List<XtUnit> getUnits() {
//	
//		return units;
//	}
//
//	public void setUnits(List<XtUnit> units) {
//	
//		this.units = units;
//	}
//	
//	public UnitService getUnitSer() {
//	
//		return unitSer;
//	}
//	public void setUnitSer(UnitService unitSer) {
//		this.unitSer = unitSer;
//	}
//	public String[] getFileFileName() {
//		return fileFileName;
//	}
//	public File[] getFile() {
//		return file;
//	}
//	public void setFileFileName(String[] fileFileName) {
//		this.fileFileName = fileFileName;
//	}
//	public void setFile(File[] file) {
//		this.file = file;
//	}
//	public String[] getFileContentType() {
//		return fileContentType;
//	}
//	public void setFileContentType(String[] fileContentType) {
//		this.fileContentType = fileContentType;
//	}
//	public File[] getImg() {
//		return img;
//	}
//	public String[] getImgFileName() {
//		return imgFileName;
//	}
//	public String[] getImgContentType() {
//		return imgContentType;
//	}
//	public void setImg(File[] img) {
//		this.img = img;
//	}
//	public void setImgFileName(String[] imgFileName) {
//		this.imgFileName = imgFileName;
//	}
//	public void setImgContentType(String[] imgContentType) {
//		this.imgContentType = imgContentType;
//	}
//	public List<String> getUserPerList() {
//		return userPerList;
//	}
//	public void setUserPerList(List<String> userPerList) {
//		this.userPerList = userPerList;
//	}
//	public List<String> getUpdateUserPers() {
//		return updateUserPers;
//	}
//	public void setUpdateUserPers(List<String> updateUserPers) {
//		this.updateUserPers = updateUserPers;
//	}
//	public String getOldpsw() {
//		return oldpsw;
//	}
//	public void setOldpsw(String oldpsw) {
//		this.oldpsw = oldpsw;
//	}
//	public String[] getTitle() {
//		return title;
//	}
//	public void setTitle(String[] title) {
//		this.title = title;
//	}
//	public ServletContext getContext() {
//		return context;
//	}
//	public void setContext(ServletContext context) {
//		this.context = context;
//	}
//	public String getMimeType() {
//		return mimeType;
//	}
//	public void setMimeType(String mimeType) {
//		this.mimeType = mimeType;
//	}
//	public JSONObject getResult() {
//		return result;
//	}
//	public void setResult(JSONObject result) {
//		this.result = result;
//	}
//	public String getNewpsw() {
//		return newpsw;
//	}
//	public void setNewpsw(String newpsw) {
//		this.newpsw = newpsw;
//	}
//	public String getExtendedid() {
//		return extendedid;
//	}
//	public String getExtendid() {
//		return extendid;
//	}
//	public void setExtendedid(String extendedid) {
//		this.extendedid = extendedid;
//	}
//	public void setExtendid(String extendid) {
//		this.extendid = extendid;
//	}
//    
//     /**
//     * @方法名称: getGetLoadUserPhotoStream
//     * @功能描述: 获取用户头像
//     * @作者:孙鹏
//     * @创建时间:2015-12-15 上午10:06:39
//     * @return InputStream   
//     */
//     
//    public InputStream getGetLoadUserPhotoStream() {
// 		return getLoadUserPhotoStream;
//	}
//
//	public void setGetLoadUserPhotoStream(InputStream getLoadUserPhotoStream) {
//	
//		this.getLoadUserPhotoStream = getLoadUserPhotoStream;
//	}
//
//	public String getFilename() {
//	
//		return filename;
//	}
//
//	public void setFilename(String filename) {
//	
//		this.filename = filename;
//	}
//
//	public XtUserFile getXtfile() {
//	
//		return xtfile;
//	}
//
//	public void setXtfile(XtUserFile xtfile) {
//	
//		this.xtfile = xtfile;
//	}
//	 /**
//		 * @方法名称: setGetLoadUserFileStream
//		 * @功能描述: 获取用户附件下载输入流
//		 * @作者:孙鹏
//		 * @创建时间:2015-12-15 上午10:33:31
//		 * @param getLoadUserFileStream void   
//		 */
//	public InputStream getGetLoadUserFileStream() {
//		return getLoadUserFileStream;
//	}
//	public void setGetLoadUserFileStream(InputStream getLoadUserFileStream) {
//		this.getLoadUserFileStream = getLoadUserFileStream;
//	}
//	 @Override  
//     public void setServletContext(ServletContext context) {  
//         this.context = context;  
//     }
//
//	public String getIsRadio() {
//	
//		return isRadio;
//	}
//
//	public void setIsRadio(String isRadio) {
//	
//		this.isRadio = isRadio;
//	}
//
//	public XtUser getLeader() {
//		
//		return leader;
//	}
//	public void setLeader(XtUser leader) {
//		this.leader = leader;
//	}
//	public String getNotIn() {
//		
//		return notIn;
//	}
//	public void setNotIn(String notIn) {
//	
//		this.notIn = notIn;
//	}
//	public String getUserids() {
//		
//		return userids;
//	}
//	public void setUserids(String userids) {
//	
//		this.userids = userids;
//	}
//	public UserdictionaryService getUserDictionaryService() {
//	
//		return userDictionaryService;
//	}
//	public void setUserDictionaryService(UserdictionaryService userDictionaryService) {
//	
//		this.userDictionaryService = userDictionaryService;
//	}
//	public List<Map<String, Object>> getUserDics() {
//	
//		return userDics;
//	}
//	public void setUserDics(List<Map<String, Object>> userDics) {
//	
//		this.userDics = userDics;
//	}
//	public FunctionService getFunctionService() {
//	
//		return functionService;
//	}
//	public void setFunctionService(FunctionService functionService) {
//	
//		this.functionService = functionService;
//	}
//	public PermissionService getPermissionService() {
//	
//		return permissionService;
//	}
//	public void setPermissionService(PermissionService permissionService) {
//	
//		this.permissionService = permissionService;
//	}
//	public RoleService getRoleService() {
//	
//		return roleService;
//	}
//	public void setRoleService(RoleService roleService) {
//	
//		this.roleService = roleService;
//	}
//	public UserPerService getUserPerService() {
//	
//		return userPerService;
//	}
//	public void setUserPerService(UserPerService userPerService) {
//	
//		this.userPerService = userPerService;
//	}
//	public List<Map<String, Object>> getAllPerList() {
//	
//		return allPerList;
//	}
//	public void setAllPerList(List<Map<String, Object>> allPerList) {
//	
//		this.allPerList = allPerList;
//	}
//	public Integer getFunid() {
//	
//		return funid;
//	}
//	public void setFunid(Integer funid) {
//	
//		this.funid = funid;
//	}
//	public List<String> getUserNames() {
//	
//		return userNames;
//	}
//	public void setUserNames(List<String> userNames) {
//	
//		this.userNames = userNames;
//	}
//	public UserService getUserService() {
//	
//		return userService;
//	}
//	public void setUserService(UserService userService) {
//	
//		this.userService = userService;
//	}
//	public String getCkunits() {
//		return ckunits;
//	}
//	public void setCkunits(String ckunits) {
//		this.ckunits = ckunits;
//	}
//	public List<Map<String, Object>> getListmap() {
//		return listmap;
//	}
//	public void setListmap(List<Map<String, Object>> listmap) {
//		this.listmap = listmap;
//	}
//	public List<Map<String, Object>> getDoczcs() {
//	
//		return doczcs;
//	}
//	public void setDoczcs(List<Map<String, Object>> doczcs) {
//	
//		this.doczcs = doczcs;
//	}
//	public String getSystemid() {
//	
//		return systemid;
//	}
//	public void setSystemid(String systemid) {
//	
//		this.systemid = systemid;
//	}
//	public IRedisUtil getRedisUtil() {
//	
//		return redisUtil;
//	}
//	public void setRedisUtil(IRedisUtil redisUtil) {
//	
//		this.redisUtil = redisUtil;
//	}
//
//}
