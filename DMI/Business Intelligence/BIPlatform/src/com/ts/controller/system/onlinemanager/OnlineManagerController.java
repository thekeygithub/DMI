package com.ts.controller.system.onlinemanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;

/** 
 * 类名称：在线管理列表
 * 创建人： 
 * 创建时间：2015-05-25
 */
@Controller
@RequestMapping(value="/onlinemanager")
public class OnlineManagerController extends BaseController {
	
	/**列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(){
		logBefore(logger, "列表OnlineManager");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/onlinemanager/onlinemanager_list");
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
