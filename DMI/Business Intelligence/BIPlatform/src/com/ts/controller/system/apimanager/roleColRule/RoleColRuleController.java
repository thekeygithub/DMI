package com.ts.controller.system.apimanager.roleColRule;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.stereotype.Controller;
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


/**
 * 类名称：api可访问字段规则管理 创建人： 修改时间：2016年12月1日
 * 
 * @version
 */
@Controller
@RequestMapping(value = "/roleCol")
public class RoleColRuleController extends BaseController {

	@Resource(name = "colRuleService")
	private ColRuleManager colRuleService;
	@Resource(name = "relationService")
	private RelationManager relationService;

	/**
	 * 显示规则列表
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/goRoleCol")
	@Rights(code="roleCol/goRoleCol")
	public ModelAndView listRoleCol(Page page) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			pd.getString("DR_ID");
			pd.getString("ROLE_ID");
			page.setPd(pd);
			List<PageData> colList = colRuleService.listPdPageColRule(page); // 列出列表
			List<String> ids = relationService.listAllName(pd);

			Map<String, List<PageData>> map = new HashMap<String, List<PageData>>();
			for (PageData p : colList) {
				if (ids.contains(p.getString("ID"))) {
					p.put("flag", 1);
				} else {
					p.put("flag", 0);
				}
				if (map.containsKey(p.getString("IN_ID"))) {
					List<PageData> lis = map.get(p.getString("IN_ID"));
					lis.add(p);
				} else {
					List<PageData> lis = new ArrayList<PageData>();
					lis.add(p);
					map.put(p.getString("IN_ID"), lis);
				}
			}
			mv.setViewName("system/apimanager/rolemanager/roleColRule_list");
			mv.addObject("colList", colList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 更新
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updata")
	@Rights(code="roleCol/updata")
	public Object updata() {
		logBefore(logger, Jurisdiction.getUsername() + "修改字段");
		ModelAndView mv = this.getModelAndView();
		Map<String,String> pu = new HashMap<String, String>();
		boolean temp = false;
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			pd.getString("DR_ID");
			pd.getString("ROLE_ID");
			String col = pd.getString("COL_RULE");
			pd = relationService.findById(pd);
			String cols = pd.getString("COL_RULE");
			String str = "";
			if (isNotNull(cols)) {
				if (cols.contains(col)) {
					String[] s = cols.split(";");
					for (String i : s) {
						if (!col.equals(i)) {
							str += ";" + i;
						}
					}
				} else {
					str = cols + ";" + col;
				}
				if (str.startsWith(";")) {
					str = str.substring(1, str.length());
				}
			} else {
				str = col;
			}
			pd.getString("TR_ID");
			pd.put("COL_RULE", str);
			temp = relationService.editA(pd);
			if(temp){
				pu.put("result", "成功！");
			}else{
				pu.put("result", "失败！");
			}
			mv.addAllObjects(pu);
			mv.addObject("msg", "success");
		} catch (Exception e) {
//			pu.put("result", "失败！");
		mv.addObject("msg", "failed");
		}
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 批量更新
	 * 
	 * @return
	 */
	@RequestMapping(value = "/editAllR")
	@Rights(code="roleCol/editAllR")
	@ResponseBody
	public Object editAllR() {
		logBefore(logger, Jurisdiction.getUsername() + "批量更新接口");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String role = pd.getString("ROLE_ID");
			String dr = pd.getString("DR_ID");
			String col = pd.getString("COL_RULE");
			pd.put("ROLE_ID", role);
			pd.put("DR_ID", dr);
			pd = relationService.findById(pd);
			pd.getString("TR_ID"); // ID
			pd.put("COL_RULE", col);
			relationService.editA(pd);
			mv.addObject("msg", "success");
			mv.setViewName("save_result");

		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

}
