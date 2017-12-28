package com.ts.controller.system.payRule;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.PayRule.PayRuleManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.ParamCheckType;
import com.ts.util.app.AppUtil;
import com.ts.util.app.SessionAppMap;

/** 
 * 类名称：接口业务列表管理
 * 创建人：
 * 修改时间：2014年11月17日
 * @version
 */
@Controller
@RequestMapping(value="/pay")
public class PayRuleController extends BaseController {
	
	@Resource(name="payRuleService")
	private PayRuleManager payRuleService;
	
	/**显示接口列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/payrule")
	@Rights(code="pay/payrule")
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
			List<PageData>	payRuleList = payRuleService.listPdPagePay(page);		//列出会员列表
			mv.setViewName("system/apimanager/apiPayRule/payRule_list");
			mv.addObject("payRuleList", payRuleList);
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
	@RequestMapping(value="/goAdd")
	@Rights(code="pay/goAdd")
	public ModelAndView goAdd() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/apimanager/apiPayRule/payRule_edit");
		mv.addObject("checkTypeSet", ParamCheckType.map.keySet());
		mv.addObject("msg", "savePay");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**保存
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/savePay")
	@Rights(code="pay/savePay")
	public ModelAndView savePay() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增规则");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ID", this.get32UUID());	//ID
		payRuleService.savePay(pd);
		//更新下缓存
		SessionAppMap.updateParamRuleMap(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}	
	/**删除
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/deletePay")
	@Rights(code="pay/deletePay")
	@ResponseBody
	public Object deletePay() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除接口");
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = null;
		try {
			payRuleService.deletePay(pd);
			errInfo = "success";
			//更新下缓存
			SessionAppMap.paramRuleMap.remove(pd.getString("ID"));
		} catch (Exception e) {
			errInfo = "failed";
			logger.error(e.getMessage(), e);
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
	@RequestMapping(value="/editPay")
	@Rights(code="pay/editPay")
	public ModelAndView editPay(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		mv.addObject("msg","success");
		try {
			pd = this.getPageData();
			payRuleService.editPay(pd);
			//更新下缓存
			SessionAppMap.updateParamRuleMap(pd);
		} catch (Exception e) {
			mv.addObject("msg","failed");
			logger.error(e.getMessage(), e);
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**去修改用户页面
	 * @return
	 */
	@RequestMapping(value="/goEdit")
	@Rights(code="pay/goEdit")
	public ModelAndView goEdit(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = payRuleService.findById(pd);						//根据ID读取
			mv.setViewName("system/apimanager/apiPayRule/payRule_edit");
			mv.addObject("checkTypeSet", ParamCheckType.map.keySet());
			mv.addObject("msg", "editPay");
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
		boolean exsit = payRuleService.findByAll(IN_IDS);
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
	@RequestMapping(value="/deleteAll")
	@Rights(code="pay/deleteAll")
	@ResponseBody
	public Object deleteAll() {
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
				payRuleService.deleteAll(ArrayIN_IDS);
				//更新下缓存
				for (String string : ArrayIN_IDS) {
					SessionAppMap.paramRuleMap.remove(string);
				}
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
	/**
	 * 显示接口列表
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/checkList")
	@Rights(code="pay/checkList")
	public ModelAndView checkList(Page page) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			page.setPd(pd);
			List<PageData> paramCheckIdList = payRuleService.ParamCheckIdList(pd.getString("DR_ID"));// 查询该参数的规则
			StringBuilder checkids = new StringBuilder(",");
			if(paramCheckIdList != null){
				for (PageData pageData : paramCheckIdList) {
					checkids.append(pageData.getString("ID")).append(",");
				}
			}
			List<PageData> checkList = payRuleService.listPdPagePay(page);
			mv.setViewName("system/apimanager/paramRuleManager");
			mv.addObject("checkList", checkList);
			mv.addObject("checkids", checkids);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**添加校验关系
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveRelation")
	@Rights(code="pay/saveRelation")
	@ResponseBody
	public Map<String, String> saveRelation(){
		logBefore(logger, Jurisdiction.getUsername()+"新增校验关系");
		Map<String,String>	pu = new HashMap<String, String>();
		boolean temp = false;
		PageData pd = this.getPageData();
		try {	
				List<PageData> pds =  payRuleService.findRelationByDrCheckId(pd);
				if(pds == null || pds.size() <= 0){
					pd.put("ID", this.get32UUID());	//ID
					temp = payRuleService.saveRelation(pd);
					if(temp){
						pu.put("result","添加成功！");
						//更新缓存
						Set<String> set = SessionAppMap.dataParamRuleMapMap.get(pd.getString("COL_NAME"));
						if(set == null){
							set = new HashSet<String>();
						}
						set.add(pd.getString("CHECK_ID"));
						SessionAppMap.dataParamRuleMapMap.put(pd.getString("COL_NAME"), set);
					}else{
						pu.put("result","添加失败！");
					}
				}else{
					pu.put("result","添加失败！");
				}
		} catch (Exception e) {
			pu.put("result","添加失败！");
			logger.error(e.getMessage(), e);
		}
		return pu;
	}
	/**删除校验关系
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteRelation")
	@Rights(code="pay/deleteRelation")
	@ResponseBody
	public Map<String, String> deleteRelation() {
		logBefore(logger, Jurisdiction.getUsername()+"删除校验关系");
		Map<String,String>	pu = new HashMap<String, String>();
		boolean temp = false;
		PageData pd = this.getPageData();
		try {
			List<PageData> pds =  payRuleService.findRelationByDrCheckId(pd);
			if(pds != null && pds.size()>0){
				PageData pdResult = pds.get(0);
				if(pdResult != null){
					temp = payRuleService.deleteRelation(pdResult);
				}
			}
			if(temp){
				//更新缓存
				Set<String> set = SessionAppMap.dataParamRuleMapMap.get(pd.getString("COL_NAME"));
				if(set != null){
					set.remove(pd.getString("CHECK_ID"));
				}
				pu.put("result","删除成功！");
			}else{
				pu.put("result","删除失败！");
			}
		} catch (Exception e) {
			pu.put("result","删除失败！");
			logger.error(e.getMessage(), e);
		}
		return pu;
	}
	
}
