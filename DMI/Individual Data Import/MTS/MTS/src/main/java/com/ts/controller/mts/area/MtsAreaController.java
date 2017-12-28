package com.ts.controller.mts.area;

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
import com.ts.entity.mts.MtsArea;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.RandomUtil;

/**
 * 
 * @类名称: LoadRuleController
 * @类描述: Redis区域管理维护
 * @作者:夏峰
 * @创建时间:2016年10月8日 下午2:04:26
 */
@Controller
@RequestMapping(value = "/mtsArea")
public class MtsAreaController extends BaseController {

	String menuUrl = "mtsArea/listmtsArea.do"; // 菜单地址(权限用)
	@Resource(name = "MTSAreaService")
	private MTSAreaService mtsAreaService;
	
	/**
	 * 显示区域
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsArea")
	public ModelAndView listMtsArea(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData> listMtsArea = mtsAreaService.mtsAreaListPage(page); // 列出用户列表
		mv.setViewName("mts/area/mts_area_list");
		mv.addObject("listMtsArea", listMtsArea);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 去新增区域页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddArea")
	public ModelAndView goAddDataSource() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/area/mts_area_edit");
		mv.addObject("msg", "saveArea");
		return mv;
	}

	//
	/**
	 * 保存区域
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveArea")
	public ModelAndView saveArea() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "保存MTSDict");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String AREA_NAME = pd.getString("AREA_NAME"); 
		String AREA_CODE = pd.getString("AREA_CODE"); 
		String REMARK = pd.getString("REMARK"); 
		String newId = RandomUtil.getRandomId();
		
		MtsArea mtsArea = new MtsArea();
		mtsArea.setAREA_NAME(AREA_NAME);
		mtsArea.setAREA_CODE(AREA_CODE);
		mtsArea.setREMARK(REMARK);
		mtsArea.setAREA_ID(newId);
		mtsArea.setOPERATE_TIME(new Date());
		
		mtsAreaService.addMtsArea(mtsArea);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 去修改区域页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditArea")
	public ModelAndView goEditArea() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String areaId = pd.getString("AREA_ID");
		MtsArea mtsArea = new MtsArea();
		mtsArea.setAREA_ID(areaId);
		List<MtsArea> areaList = mtsAreaService.findMtsArea(mtsArea);
		if(null != areaList && areaList.size() > 0){
			mtsArea = areaList.get(0);
			pd.put("AREA_ID", mtsArea.getAREA_ID());
			pd.put("AREA_NAME", mtsArea.getAREA_NAME());
			pd.put("AREA_CODE", mtsArea.getAREA_CODE());
			pd.put("REMARK", mtsArea.getREMARK());
		}
		mv.setViewName("mts/area/mts_area_edit");
		mv.addObject("msg", "editArea");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 修改区域
	 */
	@RequestMapping(value = "/editArea")
	public ModelAndView editDataArea() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String AREA_ID = pd.getString("AREA_ID");
		String AREA_NAME = pd.getString("AREA_NAME");
		String AREA_CODE = pd.getString("AREA_CODE");
		String REMARK = pd.getString("REMARK");
		MtsArea mtsArea = new MtsArea();
		
		mtsArea.setAREA_ID(AREA_ID);
		mtsArea.setAREA_CODE(AREA_CODE);
		mtsArea.setAREA_NAME(AREA_NAME);
		mtsArea.setREMARK(REMARK);
		mtsArea.setOPERATE_TIME(new Date());
		mtsAreaService.editMtsArea(mtsArea);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除区域
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delMtsArea")
	public void delMtsArea(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除matchRule");
		PageData pd = new PageData();
		pd = this.getPageData();
		String AREA_ID = pd.getString("AREA_ID");
		MtsArea mtsArea = new MtsArea();
		mtsArea.setAREA_ID(AREA_ID);
		mtsAreaService.deleteMtsArea(mtsArea);
		out.write("success");
		out.close();
	}
	
	/**
	 * 验证区域
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyMtsArea", method = RequestMethod.POST)
	public Map<String,String> verifyMtsArea(String AREA_ID,String AREA_NAME,String AREA_CODE,String OPERATE) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();  
		String result = "false";
		MtsArea mtsArea = null;
		MtsArea mtsAreaNew = null;
		List<MtsArea> areaList = null;
		if("Add".equals(OPERATE)){
			mtsArea = new MtsArea();
			if(!"".equals(AREA_NAME) && null != AREA_NAME){
				mtsArea.setAREA_NAME(AREA_NAME);
			}
			if(!"".equals(AREA_CODE) && null != AREA_CODE){
				mtsArea.setAREA_CODE(AREA_CODE);
			}
			areaList = mtsAreaService.findMtsArea(mtsArea);
			if(areaList != null && areaList.size() > 0){
				result = "false";
			}else{
				result = "success";
			}
		}else if("Edit".equals(OPERATE)){
			mtsArea = new MtsArea();
			if(!"".equals(AREA_NAME) && null != AREA_NAME){
				mtsArea.setAREA_NAME(AREA_NAME);
			}
			if(!"".equals(AREA_CODE) && null != AREA_CODE){
				mtsArea.setAREA_CODE(AREA_CODE);
			}
			areaList = mtsAreaService.findMtsArea(mtsArea);
			if(areaList == null){
				result = "false";
			}else{
				int areaListSize = areaList.size();
				if(areaListSize == 0){
					result = "success";
				}else if(areaListSize == 1){
					mtsAreaNew = areaList.get(0);
					String areaId = mtsAreaNew.getAREA_ID();
					if(areaId.equals(AREA_ID)){
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
