package com.ts.controller.mts.visitType;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsVisitType;
import com.ts.service.mts.visitType.MtsVisitTypeService;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.RandomUtil;

/**
 * 
 * @类名称: LoadRuleController
 * @类描述: Redis数据装载规则维护
 * @作者:李巍
 * @创建时间:2016年10月8日 下午2:04:26
 */
@Controller
@RequestMapping(value = "/mtsVisitType")
public class MtsVisitTypeController extends BaseController {

	String menuUrl = "mtsVisitType/listMtsVisitType.do"; // 菜单地址(权限用)
	@Resource(name = "MtsVisitTypeService")
	private MtsVisitTypeService mtsVisitTypeService;

	/**
	 * 显示规则列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsVisitType")
	public ModelAndView listMtsDict(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData> listMtsVisitType = mtsVisitTypeService.mtsVisitTypelistPage(page);// 列出用户列表
		mv.setViewName("mts/visitType/mts_visitType_list");
		mv.addObject("listMtsVisitType", listMtsVisitType);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 去新增装载规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddVisitType")
	public ModelAndView goAddVisitType() throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
//			return null;
//		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/visitType/mts_visitType_edit");
		mv.addObject("msg", "saveVisitType");
		return mv;
	}

	//
	/**
	 * 保存用户
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveVisitType")
	public ModelAndView saveVisitType() throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
//			return null;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "保存VisitType");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String falg = pd.getString("FLAG"); 
		String description = pd.getString("DESCRIPTION"); 
		String remark = pd.getString("REMARK"); 
		String newId = RandomUtil.getRandomId();
		
		MtsVisitType mtsVisitType = new MtsVisitType();
		mtsVisitType.setDESCRIPTION(description);
		mtsVisitType.setFLAG(falg);
		mtsVisitType.setREMARK(remark);
		mtsVisitType.setVISIT_TYPE_ID(newId);
		mtsVisitType.setOPERATE_TIME(new Date());
		mtsVisitTypeService.addMtsVisitType(mtsVisitType);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 去修改加载规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditVisitType")
	public ModelAndView goEditVisitType() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String VISIT_TYPE_ID = pd.getString("VISIT_TYPE_ID");
		MtsVisitType mtsVisitType = new MtsVisitType();
		mtsVisitType.setVISIT_TYPE_ID(VISIT_TYPE_ID);
		List<MtsVisitType> vtList = mtsVisitTypeService.findMtsVisitType(mtsVisitType);
		if(null != vtList && vtList.size() > 0){
			mtsVisitType = vtList.get(0);
			pd.put("VISIT_TYPE_ID", mtsVisitType.getVISIT_TYPE_ID());
			pd.put("DESCRIPTION", mtsVisitType.getDESCRIPTION());
			pd.put("FLAG", mtsVisitType.getFLAG());
			pd.put("REMARK", mtsVisitType.getREMARK());
		}
		
		mv.setViewName("mts/visitType/mts_visitType_edit");
		mv.addObject("msg", "editVisitType");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 修改数据字典
	 */
	@RequestMapping(value = "/editVisitType")
	public ModelAndView editVisitType() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "修改VisitType");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String VISIT_TYPE_ID = pd.getString("VISIT_TYPE_ID");
		String DESCRIPTION = pd.getString("DESCRIPTION");
		String REMARK = pd.getString("REMARK");
		String FLAG = pd.getString("FLAG");
		MtsVisitType mtsVisitType = new MtsVisitType();
		mtsVisitType.setVISIT_TYPE_ID(VISIT_TYPE_ID);
		mtsVisitType.setDESCRIPTION(DESCRIPTION);
		mtsVisitType.setFLAG(FLAG);
		mtsVisitType.setREMARK(REMARK);
		mtsVisitType.setOPERATE_TIME(new Date());
		mtsVisitTypeService.editMtsVisitType(mtsVisitType);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除匹配规则
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delMtsVisitType")
	public void delMtsVisitType(PrintWriter out) throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
//			return;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "删除matchRule");
		PageData pd = new PageData();
		pd = this.getPageData();
		String VISIT_TYPE_ID = pd.getString("VISIT_TYPE_ID");
		MtsVisitType mtsVisitType = new MtsVisitType();
		mtsVisitType.setVISIT_TYPE_ID(VISIT_TYPE_ID);
		mtsVisitTypeService.deleteMtsVisitType(mtsVisitType);
		out.write("success");
		out.close();
	}
	
	/**
	 * 验证就诊类型
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyMtsVisitType", method = RequestMethod.POST)
	public Map<String,String> verifyMtsVisitType(String VISIT_TYPE_ID,String DESCRIPTION,String FLAG,String OPERATE) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();  
		String result = "false";
		MtsVisitType mtsVisitType = null;
		MtsVisitType mtsVisitTypeNew = null;
		List<MtsVisitType> mtsVisitTypeList = null;
		if("Add".equals(OPERATE)){
			mtsVisitType = new MtsVisitType();
			if(!"".equals(DESCRIPTION) && null != DESCRIPTION){
				mtsVisitType.setDESCRIPTION(DESCRIPTION);
			}
			if(!"".equals(FLAG) && null != FLAG){
				mtsVisitType.setFLAG(FLAG);
			}
			mtsVisitTypeList = mtsVisitTypeService.findMtsVisitType(mtsVisitType);
			if(mtsVisitTypeList != null && mtsVisitTypeList.size() > 0){
				result = "false";
			}else{
				result = "success";
			}
		}else if("Edit".equals(OPERATE)){
			mtsVisitType = new MtsVisitType();
			if(!"".equals(DESCRIPTION) && null != DESCRIPTION){
				mtsVisitType.setDESCRIPTION(DESCRIPTION);
			}
			if(!"".equals(FLAG) && null != FLAG){
				mtsVisitType.setFLAG(FLAG);
			}
			mtsVisitTypeList = mtsVisitTypeService.findMtsVisitType(mtsVisitType);
			if(mtsVisitTypeList == null){
				result = "false";
			}else{
				int visitTypeListSize = mtsVisitTypeList.size();
				if(visitTypeListSize == 0){
					result = "success";
				}else if(visitTypeListSize == 1){
					mtsVisitTypeNew = mtsVisitTypeList.get(0);
					String visitTypeId = mtsVisitTypeNew.getVISIT_TYPE_ID();
					if(visitTypeId.equals(VISIT_TYPE_ID)){
						result = "success";
					}else{
						result = "false";
					}
				}else{
					result = "false";
				}
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}
}
