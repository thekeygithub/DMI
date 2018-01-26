package com.ts.controller.system.user;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.Role;
import com.ts.entity.system.User;
import com.ts.service.system.log.LogManager;
import com.ts.service.system.role.RoleManager;
import com.ts.service.system.user.UserManager;
import com.ts.util.Const;
import com.ts.util.FileDownload;
import com.ts.util.FileUpload;
import com.ts.util.GetPinyin;
import com.ts.util.Jurisdiction;
import com.ts.util.ObjectExcelRead;
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;
import com.ts.util.PathUtil;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;

/** 
 * 类名称：UserController
 * 创建人：
 * 更新时间：2015年11月3日
 * @version
 */
@Controller
@RequestMapping(value="/user")
public class UserController extends BaseController {
	
	@Resource(name="userService")
	private UserManager userService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="logService")
	private LogManager logService;
	
	/**显示用户列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listUsers")
	@Rights(code="user/listUsers")
	public ModelAndView listUsers(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
		String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
		}
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
		} 
		page.setPd(pd);
		User user = getCurrentUser();
		Integer roleMax = (Integer)Jurisdiction.getSession().getAttribute(user.getUSERNAME()+Const.SESSION_USER_MAX_ROLE);
		if(roleMax!=0){
			//查询出当前用户角色的下级角色用户列表
			List<PageData> users = userService.listSubUser(user);
			List<String> USERNAMES = new ArrayList<String>();
			for(PageData p: users){
				USERNAMES.add(p.getString("USERNAME"));
			}
			pd.put("USERNAMES", USERNAMES);
		}
		List<PageData>	userList = userService.listUsers(page);	//列出用户列表
		pd.put("ROLE_ID", "1");
		mv.setViewName("system/user/user_list");
		mv.addObject("userList", userList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**去新增用户页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toAdd")
	@Rights(code="user/toAdd")
	public ModelAndView toAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/user/user_edit");
		mv.addObject("MSG", "add");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**保存用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@Rights(code="user/add")
	public ModelAndView add(User user) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增user");
		ModelAndView mv = this.getModelAndView();
		try {
			user.setUSER_ID(get32UUID());
			user.setSKIN("default");
			user.setPASSWORD(new SimpleHash("SHA-1", user.getUSERNAME(),user.getPASSWORD()).toString());	//密码加密
			mv.addObject("msg","success");
			if(null == userService.findByUsername(user.getUSERNAME())){	//判断用户名是否存在
				userService.saveU(user);
				logService.saveUserLog(user,0,Jurisdiction.getUsername());
			}else{
				mv.addObject("msg","用户用已经存在！");
			}
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**查看用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/view")
	@Rights(code="user/view")
	public ModelAndView view() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//根据ID读取用户
		pd = userService.findById(pd);
		if(pd!=null && "admin".equals(pd.getString("USER_NAME"))){
			String username = (String)Jurisdiction.getSession().getAttribute(Const.SESSION_USERNAME);
			if(!"admin".equals(username)){
				mv.addObject("msg","false");
				mv.setViewName("system/user/user_view");
				return mv;
			}
		}
		mv.setViewName("system/user/user_view");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**删除用户
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	@Rights(code="user/delete")
	public void deleteU(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除user");
		PageData pd = new PageData();
		pd = this.getPageData();
		User u = userService.findUserById( pd.getString("USER_ID"));
		userService.deleteU(pd);
		logService.saveUserLog(u,9,Jurisdiction.getUsername());
		out.write("success");
		out.close();
	}
	
	/**
	 * 批量删除
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteAll")
	@Rights(code="user/deleteAll",type="ajax",dataType="json")
	@ResponseBody
	public Object deleteAllU() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除user");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String USER_IDS = pd.getString("USER_IDS");
		if(null != USER_IDS && !"".equals(USER_IDS)){
			String ArrayUSER_IDS[] = USER_IDS.split(",");
			userService.deleteAllU(ArrayUSER_IDS);
			logService.saveUserLog(USER_IDS, 9, Jurisdiction.getUsername());
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		map.put("result", "success");
		return AppUtil.returnObject(pd, map);
	}
	
	/**去修改用户页面(系统用户列表修改)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toEdit")
	@Rights(code="user/toEdit")
	public ModelAndView goEditU(HttpServletResponse response,HttpServletRequest request) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//根据ID读取
		pd = userService.findById(pd);
		//管理员账号信息只能管理员自己修改
		if(pd!=null && "admin".equals(pd.getString("USER_NAME"))){
			String username = Jurisdiction.getUsername();
			if(!"admin".equals(username)){
				// 跳转到超出权限页面
				response.sendRedirect(request.getContextPath() + Const.NO_RIGHTS);
				return null;
			}
		}
		mv.setViewName("system/user/user_edit");
		mv.addObject("MSG", "edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 修改用户
	 */
	@RequestMapping(value="/edit")
	public ModelAndView editU(HttpServletResponse response,HttpServletRequest request ) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改ser");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if("admin".equals(pd.getString("USER_NAME"))){
				//管理员账号信息只能管理员自己修改
				String username = Jurisdiction.getUsername();
				if(!"admin".equals(username)){
					// 跳转到超出权限页面
					response.sendRedirect(request.getContextPath() + Const.NO_RIGHTS);
					return null;
				}
			}
			if(pd.getString("PASSWORD") != null && !"".equals(pd.getString("PASSWORD"))){
				pd.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());
			}
			userService.editU(pd);	//执行修改
			logService.saveUserLog(pd, 1, Jurisdiction.getUsername());
			mv.addObject("msg","success");
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 显示菜单列表ztree(菜单授权菜单)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/roles")
	@Rights(code="user/roles")
	public ModelAndView roles(Model model,String USER_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			//获取要设置的USER_ID已经拥有的角色
			List<String> hasRoleIds = roleService.getUserRoleIdsById(USER_ID);
			// 当前登录用户
			User user = getCurrentUser();
			//根据已经拥有的角色ID集合，对角色树进行判断是否被选中
			List<Role> roleList = roleService.listUserAllValideRole(Const.TREE_ROOT_ID,user,0);
			//对角色树进行判断是否被选中
			roleService.setCheckedRole( roleList, hasRoleIds);
			//显示登陆用户可以使用的角色树
			JSONArray arr = JSONArray.fromObject(roleList);
			String json = arr.toString();
			json = json.replaceAll("ROLE_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("ROLE_NAME", "name").replaceAll("subRole", "children").replaceAll("hasRole", "checked").replaceAll("chk", "chkDisabled");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("USER_ID",USER_ID);
			mv.setViewName("system/user/roles");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**保存用户的角色
	 * @param USER_ID 用户ID
	 * @param menuIds 角色ID集合
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/saveRoles")
	@Rights(code="user/saveRoles")
	public void saveRoles(@RequestParam String USER_ID,@RequestParam String roleIds,PrintWriter out)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改用户的角色");
		try{
			if(null != roleIds && !"".equals(roleIds.trim())){
				String[] ids = Tools.str2StrArray(roleIds);
				ArrayList<PageData> pds = new ArrayList<PageData>();
				for(String id:ids){
					PageData mypd = new PageData();
					mypd.put("ID", get32UUID());
					mypd.put("USER_ID", USER_ID);
					mypd.put("ROLE_ID", id);
					pds.add(mypd);
				}
				//更新当前角色菜单权限
				userService.updateUserRoles(USER_ID,pds);
				
			}else{
				//删除角色的所有权限(没有任何勾选)
				userService.deleteUserAllRoles(USER_ID);
			}
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
		
	/**判断用户名是否存在
	 * @return
	 */
	@RequestMapping(value="/hasU")
	@ResponseBody
	public Object hasU(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(userService.findByUsername(pd.getString("USER_NAME")) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**判断邮箱是否存在
	 * @return
	 */
	@RequestMapping(value="/hasE")
	@ResponseBody
	public Object hasE(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(userService.findByUE(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**判断编码是否存在
	 * @return
	 */
	@RequestMapping(value="/hasN")
	@ResponseBody
	public Object hasN(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(userService.findByUN(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	
	/**去修改用户页面(个人修改)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditMyU")
	@Rights(code="user/goEditMyU")
	public ModelAndView goEditMyU(HttpServletResponse response,HttpServletRequest request) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.addObject("fx", "head");
		pd.put("USERNAME", Jurisdiction.getUsername());
		pd = userService.findByUsername(pd.getString("USERNAME"));						//根据用户名读取
		mv.setViewName("system/user/user_edit");
		mv.addObject("MSG", "edit");
		mv.addObject("pd", pd);
		return mv;
	}
	

	
	/**去修改用户页面(在线管理页面打开)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditUfromOnline")
	@Rights(code="user/goEditUfromOnline")
	public ModelAndView goEditUfromOnline(HttpServletResponse response,HttpServletRequest request) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if("admin".equals(pd.getString("USER_NAME"))){
			//管理员账号信息只能管理员自己修改
			String username = Jurisdiction.getUsername();
			if(!"admin".equals(username)){
				// 跳转到超出权限页面
				response.sendRedirect(request.getContextPath() + Const.NO_RIGHTS);
				return null;
			}
		}
		pd = userService.findByUsername(pd.getString("USER_NAME"));						//根据ID读取
		mv.setViewName("system/user/user_edit");
		mv.addObject("MSG", "edit");
		mv.addObject("pd", pd);
		return mv;
	}

	
	/**导出用户信息到EXCEL
	 * @return
	 */
	@RequestMapping(value="/excel")
	@Rights(code="user/excel")
	public ModelAndView exportExcel(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
				String keywords = pd.getString("keywords");				//关键词检索条件
				if(null != keywords && !"".equals(keywords)){
					pd.put("keywords", keywords.trim());
				}
				String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
				String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
				if(lastLoginStart != null && !"".equals(lastLoginStart)){
					pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
				}
				if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
					pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
				} 
				Map<String,Object> dataMap = new HashMap<String,Object>();
				List<String> titles = new ArrayList<String>();
				titles.add("用户名"); 		//1
				titles.add("编号");  		//2
				titles.add("姓名");			//3
				titles.add("职位");			//4
				titles.add("手机");			//5
				titles.add("邮箱");			//6
				titles.add("最近登录");		//7
				titles.add("上次登录IP");	//8
				dataMap.put("titles", titles);
				
				//限制导出的数据
				User user = getCurrentUser();
				Integer roleMax = (Integer)Jurisdiction.getSession().getAttribute(user.getUSERNAME()+Const.SESSION_USER_MAX_ROLE);
				if(roleMax!=0){
					//查询出当前用户角色的下级角色用户列表
					List<PageData> users = userService.listSubUser(user);
					List<String> USERNAMES = new ArrayList<String>();
					for(PageData p: users){
						USERNAMES.add(p.getString("USERNAME"));
					}
					pd.put("USERNAMES", USERNAMES);
				}
				List<PageData> userList = userService.listAllUser(pd);
				List<PageData> varList = new ArrayList<PageData>();
				for(int i=0;i<userList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", userList.get(i).getString("USERNAME"));		//1
					vpd.put("var2", userList.get(i).getString("NUMBER"));		//2
					vpd.put("var3", userList.get(i).getString("NAME"));			//3
					vpd.put("var4", userList.get(i).getString("ROLE_NAME"));	//4
					vpd.put("var5", userList.get(i).getString("PHONE"));		//5
					vpd.put("var6", userList.get(i).getString("EMAIL"));		//6
					vpd.put("var7", userList.get(i).getString("LAST_LOGIN"));	//7
					vpd.put("var8", userList.get(i).getString("IP"));			//8
					varList.add(vpd);
				}
				dataMap.put("varList", varList);
				ObjectExcelView erv = new ObjectExcelView();					//执行excel操作
				mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	@Rights(code="user/goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/uploadexcel");
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "Users.xls", "Users.xls");
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	@Rights(code="user/readExcel")
	public ModelAndView readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file
			) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;								//文件上传路径
			String fileName =  FileUpload.fileUp(file, filePath, "userexcel");							//执行上传
			@SuppressWarnings({ "rawtypes", "unchecked" })
			List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 2, 0, 0);		//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
			/*存入数据库操作======================================*/
			pd.put("RIGHTS", "");					//权限
			pd.put("LAST_LOGIN", "");				//最后登录时间
			pd.put("IP", "");						//IP
			pd.put("STATUS", "0");					//状态
			pd.put("SKIN", "default");				//默认皮肤
			pd.put("ROLE_ID", "1");
			/**
			 * var0 :编号
			 * var1 :姓名
			 * var2 :手机
			 * var3 :邮箱
			 * var4 :备注
			 */
			for(int i=0;i<listPd.size();i++){		
				pd.put("USER_ID", this.get32UUID());										//ID
				pd.put("NAME", listPd.get(i).getString("var1"));							//姓名
				
				String USERNAME = GetPinyin.getPingYin(listPd.get(i).getString("var1"));	//根据姓名汉字生成全拼
				pd.put("USERNAME", USERNAME);	
				if(userService.findByUsername(pd.getString("USER_NAME")) != null){									//判断用户名是否重复
					USERNAME = GetPinyin.getPingYin(listPd.get(i).getString("var1"))+Tools.getRandomNum();
					pd.put("USERNAME", USERNAME);
				}
				pd.put("BZ", listPd.get(i).getString("var4"));								//备注
				if(Tools.checkEmail(listPd.get(i).getString("var3"))){						//邮箱格式不对就跳过
					pd.put("EMAIL", listPd.get(i).getString("var3"));						
					if(userService.findByUE(pd) != null){									//邮箱已存在就跳过
						continue;
					}
				}else{
					continue;
				}
				pd.put("NUMBER", listPd.get(i).getString("var0"));							//编号已存在就跳过
				pd.put("PHONE", listPd.get(i).getString("var2"));							//手机号
				
				pd.put("PASSWORD", new SimpleHash("SHA-1", USERNAME, "123").toString());	//默认密码123
				if(userService.findByUN(pd) != null){
					continue;
				}
//				userService.saveU(pd);
			}
			/*存入数据库操作======================================*/
			mv.addObject("msg","success");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**显示用户列表(弹窗选择用)
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listUsersForWindow")
	@Rights(code="user/listUsersForWindow")
	public ModelAndView listUsersForWindow(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
		String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
		}
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
		} 
		page.setPd(pd);
		List<PageData>	userList = userService.listUsers(page);	//列出用户列表
		pd.put("ROLE_ID", "1");
		mv.setViewName("system/user/window_user_list");
		mv.addObject("userList", userList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}

}
