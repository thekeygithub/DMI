package com.ts.controller.system.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.system.Menu;
import com.ts.entity.system.MenuFun;
import com.ts.entity.system.User;
import com.ts.service.system.appuser.AppuserManager;
import com.ts.service.system.menu.MenuManager;
import com.ts.service.system.menufun.MenuFunManager;
import com.ts.service.system.role.RoleManager;
import com.ts.service.system.user.UserManager;
import com.ts.util.Const;
import com.ts.util.DateUtil;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;
/**
 * 总入口
 * @author fh QQ 3 1 3 5 9 6 7 9 0[青苔]
 * 修改日期：2015/11/2
 */
/**
 * @author Administrator
 *
 */
@Controller
public class LoginController extends BaseController {

	@Resource(name="userService")
	private UserManager userService;
	@Resource(name="menuService")
	private MenuManager menuService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="appuserService")
	private AppuserManager appuserService;
	@Resource(name="menuFunService")
	private MenuFunManager menuFunService;
	
	
	/**访问登录页
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login_toLogin")
	public ModelAndView toLogin()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
		mv.setViewName("system/index/login");
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**请求登录，验证用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login_login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "";
		String KEYDATA[] = pd.getString("KEYDATA").replaceAll("qq313596790fh", "").replaceAll("QQ978336446fh", "").split(",fh,");
		if(null != KEYDATA && KEYDATA.length == 3){
			Session session = Jurisdiction.getSession();
			String sessionCode = (String)session.getAttribute(Const.SESSION_SECURITY_CODE);		//获取session中的验证码
			String code = KEYDATA[2];
			if(null == code || "".equals(code)){//判断效验码
				errInfo = "nullcode"; 			//效验码为空
			}else{
				String USERNAME = KEYDATA[0];	//登录过来的用户名
				String PASSWORD  = KEYDATA[1];	//登录过来的密码
				pd.put("USERNAME", USERNAME);
				if(Tools.notEmpty(sessionCode) && sessionCode.equalsIgnoreCase(code)){// ){		//判断登录验证码
					String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString();	//密码加密
					pd.put("PASSWORD", passwd);
					User user = userService.getUserByNameAndPwd(pd);	//根据用户名和密码去读取用户信息
					if(user != null){
						if(user.getSTATUS()!=null&&user.getSTATUS()==1){
							errInfo = "userFreeze";
						}else{
							user.setLAST_LOGIN(DateUtil.getTime());
							user.setIP(pd.getString("IP"));
							userService.updateLastLogin(user);
							session.setAttribute(Const.SESSION_USER, user);			//把用户信息放session中
							session.removeAttribute(Const.SESSION_SECURITY_CODE);	//清除登录验证码的session
							//shiro加入身份验证
							Subject subject = SecurityUtils.getSubject(); 
						    UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD); 
						    try { 
						        subject.login(token); 
						    } catch (AuthenticationException e) { 
						    	errInfo = "身份验证失败！";
						    }
						}
					}else{
						errInfo = "usererror"; 				//用户名或密码有误
						logBefore(logger, USERNAME+"登录系统密码或用户名错误");
					}
				}else{
					errInfo = "codeerror";				 	//验证码输入有误
				}
				if(Tools.isEmpty(errInfo)){
					errInfo = "success";					//验证成功
					logBefore(logger, USERNAME+"登录系统");
				}
			}
		}else{
			errInfo = "error";	//缺少参数
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**访问系统首页
	 * @param changeMenu：切换菜单参数
	 * @return
	 */
	@RequestMapping(value="/main/index")
	public ModelAndView login_index(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			Session session = Jurisdiction.getSession();
			if(session!=null){
				User user = (User)session.getAttribute(Const.SESSION_USER);	//读取session中的用户信息(单独用户信息)
				if (user != null) {
					String USERNAME = user.getUSERNAME();
					Integer roleMax = (Integer)session.getAttribute(USERNAME+Const.SESSION_USER_MAX_ROLE); //读取session中的用户信息(含角色信息)
					if(null == roleMax){
						roleMax = roleService.getUserMaxRolesByUsId(user.getUSER_ID());	//通过用户ID读取角色信息
						session.setAttribute(USERNAME+Const.SESSION_USER_MAX_ROLE, roleMax); //存入session角色信息
					}
					//查询用户权限
					List<MenuFun> mfList = menuFunService.listAllMenuFunByUserId(user.getUSER_ID());
					List<String> newMfList = Jurisdiction.getMfList(mfList);
					//把用户权限信息放session中
					session.setAttribute(user.getUSERNAME()+Const.SESSION_ROLE_RIGHTS, newMfList);
					session.setAttribute(Const.SESSION_USERNAME, USERNAME);	//放入用户名到session
					List<Menu> allmenuList = new ArrayList<Menu>();
					allmenuList = this.getAttributeMenu(session,user.getUSER_ID(), USERNAME, roleMax,newMfList); //菜单缓存
					List<Menu> menuList = new ArrayList<Menu>();
					//菜单分组，业务与系统
					menuList = this.changeMenuF(allmenuList, session, USERNAME, pd.getString("changeMenu"),roleMax);
					
					this.getRemortIP(USERNAME);	//更新登录IP
					mv.setViewName("system/index/main");
					mv.addObject("user", user);
					mv.addObject("menuList", menuList);
				}
			}else {
				mv.setViewName("system/index/login");//session失效后跳转登录页面
			}
		} catch(Exception e){
			mv.setViewName("system/index/login");
			logger.error(e.getMessage(), e);
		}
		
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
		mv.addObject("pd",pd);
		return mv;
	}
	
	
	/**菜单缓存
	 * @param session
	 * @param USERNAME
	 * @param roleRights
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getAttributeMenu(Session session,String USER_ID, String USERNAME, Integer roleType,List<String> mfList) throws Exception{
		List<Menu> allmenuList = new ArrayList<Menu>();
		Object obj = session.getAttribute(USERNAME + Const.SESSION_ALL_MENU_LIST);
		if(null == obj || ((List<Menu>)obj).size()==0){	
			roleType = roleType==null?2:roleType;
			if(roleType==0){
				allmenuList = menuService.listAllMenuQx(Const.TREE_ROOT_ID);//获取所有菜单
			}else{
				allmenuList = menuService.listValidAllMenuQx(Const.TREE_ROOT_ID,mfList);
			}
			session.setAttribute(USERNAME + Const.SESSION_ALL_MENU_LIST, allmenuList);//菜单权限放入session中
		}else{
			allmenuList = (List<Menu>)session.getAttribute(USERNAME + Const.SESSION_ALL_MENU_LIST);
		}
		return allmenuList;
	}
	
	/**系统菜单 - 业务菜单处理
	 * @param allmenuList
	 * @param session
	 * @param USERNAME
	 * @param changeMenu
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Menu> changeMenuF(List<Menu> allmenuList, Session session, String USERNAME, String changeMenu,Integer roleMax){
		if("yes".equals(changeMenu)){
			// 切换菜单
			return this.changeMenu(allmenuList, session, USERNAME);
		}else{
			List<Menu> menuList = new ArrayList<Menu>();
			if(null == session.getAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST)){
				List<Menu> menuList1 = new ArrayList<Menu>();
				List<Menu> menuList2 = new ArrayList<Menu>();
				for(int i=0;i<allmenuList.size();i++){//拆分菜单
					Menu menu = allmenuList.get(i);
					if(menu.getMENU_TYPE()!=null && menu.getMENU_TYPE()==1){
						menuList1.add(menu);
					}else{
						menuList2.add(menu);
					}
				}
				session.removeAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST);
				if(roleMax!=null && roleMax < 2){
					//超级管理员和授权角色进入系统菜单					
					session.setAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST, menuList1);
					session.removeAttribute("changeMenu");
					session.setAttribute("changeMenu", "1");
					menuList = menuList1;
				}else{
					//业务角色进入业务菜单
					session.setAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST, menuList2);
					session.removeAttribute("changeMenu");
					session.setAttribute("changeMenu", "2");
					menuList = menuList2;
				}
			}else{
				menuList = (List<Menu>)session.getAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST);
			}
			return menuList;
		}
	}
	
	/**
	 * 切换菜单
	 * @param allmenuList
	 * @param session
	 * @param USERNAME
	 * @return
	 */
	private List<Menu> changeMenu(List<Menu> allmenuList, Session session, String USERNAME){
		List<Menu> menuList = new ArrayList<Menu>();
		List<Menu> menuList1 = new ArrayList<Menu>();
		List<Menu> menuList2 = new ArrayList<Menu>();
		for(int i=0;i<allmenuList.size();i++){//拆分菜单
			Menu menu = allmenuList.get(i);
			if(menu.getMENU_TYPE()!=null && menu.getMENU_TYPE()==1){
				menuList1.add(menu);
			}else{
				menuList2.add(menu);
			}
		}
		session.removeAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST);
		if("2".equals(session.getAttribute("changeMenu"))){
			session.setAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST, menuList1);
			session.removeAttribute("changeMenu");
			session.setAttribute("changeMenu", "1");
			menuList = menuList1;
		}else{
			session.setAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST, menuList2);
			session.removeAttribute("changeMenu");
			session.setAttribute("changeMenu", "2");
			menuList = menuList2;
		}
		return menuList;
	}
	
//	@RequestMapping(value="/main/changeMenu")
//	@SuppressWarnings("unchecked")
//	public ModelAndView changeMenu(){
//		ModelAndView mv = this.getModelAndView();
//		Session session = Jurisdiction.getSession();
//		String username = Jurisdiction.getUsername();
//		if(session!=null){
//			List<Menu> allmenuList = (List<Menu>)session.getAttribute( username+ Const.SESSION_ALL_MENU_LIST);
//			changeMenuF(allmenuList,session,username,"yes");
//			//切换菜单后，重新加载主页
//			mv.setViewName("redirect:/main/index");
//		}else{
//			//session失效后跳转登录页面
//			mv.setViewName("system/index/login");
//		}
//		return mv;
//	}
	/**
	 * 进入tab标签
	 * @return
	 */
	@RequestMapping(value="/tab")
	public String tab(){
		return "system/index/tab";
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/login_default")
	public ModelAndView defaultPage() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd.put("userCount", Integer.parseInt(userService.getUserCount("").get("userCount").toString())-1);				//系统用户数
		pd.put("appUserCount", Integer.parseInt(appuserService.getAppUserCount("").get("appUserCount").toString()));	//会员数
		mv.addObject("pd",pd);
		mv.setViewName("system/index/default");
		return mv;
	}
	
	/**
	 * 用户注销
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout")
	public ModelAndView logout(){
		String USERNAME = Jurisdiction.getUsername();	//当前登录的用户名
		logBefore(logger, USERNAME+"退出系统");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		Session session = Jurisdiction.getSession();	//以下清除session缓存
		session.removeAttribute(Const.SESSION_USER);
		session.removeAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS);
		session.removeAttribute(USERNAME + Const.SESSION_USER_MAX_ROLE);
		session.removeAttribute(USERNAME + Const.SESSION_ALL_MENU_LIST);
		session.removeAttribute(USERNAME + Const.SESSION_CURRENT_MENU_LIST);
		session.removeAttribute(Const.SESSION_userpds);
		session.removeAttribute(Const.SESSION_USERNAME);
		session.removeAttribute("changeMenu");
		session.removeAttribute("DEPARTMENT_IDS");
		session.removeAttribute("DEPARTMENT_ID");
		//shiro销毁登录
		Subject subject = SecurityUtils.getSubject(); 
		subject.logout();
		pd = this.getPageData();
		pd.put("msg", pd.getString("msg"));
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
		mv.setViewName("system/index/login");
		mv.addObject("pd",pd);
		return mv;
	}
	
	/** 更新登录用户的IP
	 * @param USERNAME
	 * @throws Exception
	 */
	public void getRemortIP(String USERNAME) throws Exception {  
		PageData pd = new PageData();
		HttpServletRequest request = this.getRequest();
		String ip = "";
		if (request.getHeader("x-forwarded-for") == null) {  
			ip = request.getRemoteAddr();  
	    }else{
	    	ip = request.getHeader("x-forwarded-for");  
	    }
		pd.put("USERNAME", USERNAME);
		pd.put("IP", ip);
		userService.saveIP(pd);
	}  
	
	public static void main(String[] args) {
		String passwd = new SimpleHash("SHA-1", "xx", "xx").toString();	//密码加密
		System.out.println(passwd+ "-" + passwd.length());
	}
}
