package com.ts.controller.system.apimanager.colRule;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.ColRule.ColRuleManager;
import com.ts.service.system.apimanager.Relation.RelationManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.app.AppUtil;

/**
 * 类名称：api可访问字段规则管理 创建人： 修改时间：2016年12月1日
 * 
 * @version
 */
@Controller
@RequestMapping(value = "/col")
public class ColRuleController extends BaseController {

	@Resource(name = "colRuleService")
	private ColRuleManager colRuleService;
	@Resource(name = "relationService")
	private RelationManager relationService;

	/**
	 * 显示接口列表
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/goRule")
	@Rights(code="col/goRule")
	public ModelAndView listInterface(Page page) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			page.setPd(pd);
			List<PageData> colList = colRuleService.listPdPageColRule(page); // 列出会员列表
			mv.setViewName("system/apimanager/colRule_list");
			mv.addObject("colList", colList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 去新增用户页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAdd")
	@Rights(code="col/goAdd")
	public ModelAndView goAddA() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/apimanager/colRule_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 保存用户
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save")
	@Rights(code="col/save")
	public ModelAndView save() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "新增api管理字段规则");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ID", this.get32UUID()); // ID
		colRuleService.save(pd);
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 删除用户
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteA")
	@Rights(code="col/deleteA")
	public void deleteA(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除字段");
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.getString("DR_ID");
		String sID = pd.getString("ID");
		List<PageData> list = relationService.listByDRId(pd);
		List<PageData> crule = new ArrayList<PageData>();
		String[] str = null;
		for (PageData pds : list) {
			String rule = "";
			if(isNotNull(pds.getString("COL_RULE"))){
			str = pds.getString("COL_RULE").split(";");
			for (String st : str) {
				if (!sID.equals(st)) {
					rule += ";" + st;
				}
			}
			if (rule.startsWith(";")) {
				rule = rule.substring(1, rule.length());
			}
			}
			pds.put("COL_RULE", rule);
			crule.add(pds);
			
		}
		relationService.editAll(crule);
		colRuleService.deleteA(pd);
		out.write("success");
		out.close();
	}

	/**
	 * 修改用户
	 * 
	 * @param out
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editA")
	@Rights(code="col/editA")
	public ModelAndView editA(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "修改字段");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		mv.addObject("msg", "success");
		try {
			pd = this.getPageData();
			colRuleService.editA(pd);
		} catch (Exception e) {
			mv.addObject("msg", "failed");
		}
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 去修改用户页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/goEdit")
	@Rights(code="col/goedit")
	public ModelAndView goEdit() {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = colRuleService.findById(pd); // 根据ID读取
			mv.setViewName("system/apimanager/colRule_edit");
			mv.addObject("msg", "editA");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 批量删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteAllA")
	@Rights(code="col/daleteAllA")
	@ResponseBody
	public Object deleteAllA() {
		logBefore(logger, Jurisdiction.getUsername() + "批量删除规则");
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String IDS = pd.getString("IDS");
			if (null != IDS && !"".equals(IDS)) {
				String COIDS[] = IDS.split(",");
				colRuleService.deleteAllA(COIDS);
				pd.put("msg", "ok");
			} else {
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
	}

}
