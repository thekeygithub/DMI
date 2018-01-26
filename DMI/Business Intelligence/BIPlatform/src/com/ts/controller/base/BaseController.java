package com.ts.controller.base;


import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.UuidUtil;

/**
 * @author 
 * 修改时间：2015、12、11
 */
public class BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	
	/** new PageData对象
	 * @return
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**得到ModelAndView
	 * @return
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**得到分页列表的信息
	 * @return
	 */
	public Page getPage(){
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
    public JSONObject getJsonObject(String json){
    	 return JSONObject.fromObject(json);
    }
    
    
	/**
	 * 获取当前登录的用户
	 * @return
	 */
	public static User getCurrentUser(){
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User)session.getAttribute(Const.SESSION_USER);	
		return user;
	}
	
	/**
	 * 校验字符是否为空
	 * @Title: isNull 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param str
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public boolean isNull(String str){
		return (str==null||str=="");
	}
    
	/**
	 * 校验字符非空
	 * @Title: isNotNull 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param str
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public boolean isNotNull(String str){
		return (str!=null&&str!="");
	}
}
