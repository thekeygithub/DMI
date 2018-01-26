package com.ts.controller.system.apimanager.Interface;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.Interface.InterfaceManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.app.AppUtil;

/** 
 * 类名称：接口业务列表管理
 * 创建人：
 * 修改时间：2014年11月17日
 * @version
 */
@Controller
@RequestMapping(value="/sys")
public class InterfaceController extends BaseController {
	
	@Resource(name="interfaceService")
	private InterfaceManager interfaceService;
	
	/**显示接口列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/interface")
	@Rights(code="sys/interface")
	public ModelAndView listInterface(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String keywords = pd.getString("keywords");							//检索条件 关键词
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			List<PageData>	interfaceList = interfaceService.listPdPageInterface(page);		//列出会员列表
			mv.setViewName("system/apimanager/interface_list");
			mv.addObject("interfaceList", interfaceList);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**去新增页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/goAddI")
	@Rights(code="sys/goAddI")
	public ModelAndView goAddI() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/apimanager/interface_edit");
		mv.addObject("msg", "saveI");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**保存
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveI")
	@Rights(code="sys/saveI")
	public ModelAndView saveI() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增接口");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("IN_ID", this.get32UUID());	//ID
		interfaceService.saveI(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}	
	/**删除
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteI")
	@Rights(code="sys/deleteI")
	@ResponseBody
	public Object deleteI() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除接口");
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = null;
		try {
			interfaceService.deleteI(pd);
			errInfo = "success";
		} catch (Exception e) {
			errInfo = "failed";
			// TODO: handle exception
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**修改
	 * @param out
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editI")
	@Rights(code="sys/editI")
	public ModelAndView editI(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		mv.addObject("msg","success");
		try {
			pd = this.getPageData();
			interfaceService.editI(pd);
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**去修改用户页面
	 * @return
	 */
	@RequestMapping(value="/goEditI")
	@Rights(code="sys/goEditI")
	public ModelAndView goEditI(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = interfaceService.findById(pd);						//根据ID读取
			mv.setViewName("system/apimanager/interface_edit");
			mv.addObject("msg", "editI");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	/**删除时检查是否使用
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/checkINID")
	@ResponseBody
	public Object checkAllDRID()throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		String IN_ID = pd.getString("IN_IDS");
		String[] IN_IDS = IN_ID.split(",");
		// 验证是否存在用户使用该角色
		boolean exsit = interfaceService.findByAllInId(IN_IDS);
		String errInfo = null;
		if(exsit){
			errInfo = "存在数据正在被使用，确定是否仍要删除？";
		}else{
			errInfo = "success";
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	/**批量删除
	 * @return
	 */
	@RequestMapping(value="/deleteAllI")
	@Rights(code="sys/deleteAllI")
	@ResponseBody
	public Object deleteAllI() {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除接口");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = null;
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String IN_IDS = pd.getString("IN_IDS");
			if(null != IN_IDS && !"".equals(IN_IDS)){
				String ArrayIN_IDS[] = IN_IDS.split(",");
				interfaceService.deleteAllI(ArrayIN_IDS);
				errInfo = "success";
				pd.put("msg", "ok");
			}else{
				errInfo = "failed";
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			errInfo = "failed";
			logger.error(e.toString(), e);
		} finally {
			errInfo = "failed";
			logAfter(logger);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(pd, map);
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
	
}
