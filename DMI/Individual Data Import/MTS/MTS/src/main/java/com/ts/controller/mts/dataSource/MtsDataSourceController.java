package com.ts.controller.mts.dataSource;

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
import com.ts.entity.mts.MtsDataSource;
import com.ts.service.mts.dataSource.MtsDataSourceService;
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
@RequestMapping(value = "/mtsDataSource")
public class MtsDataSourceController extends BaseController {

	String menuUrl = "mtsDataSource/listMtsDataSource.do"; // 菜单地址(权限用)
	@Resource(name = "MtsDataSourceService")
	private MtsDataSourceService mtsDataSourceService;

	/**
	 * 显示规则列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsDataSource")
	public ModelAndView listMtsDict(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData> listMtsDataSource = mtsDataSourceService.mtsDataSourcelistPage(page); // 列出用户列表
		mv.setViewName("mts/dataSource/mts_dataSource_list");
		mv.addObject("listMtsDataSource", listMtsDataSource);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 去新增装载规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddDataSource")
	public ModelAndView goAddDataSource() throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
//			return null;
//		} // 校验权限
		ModelAndView mv = this.getModelAndView();
//		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		mv.setViewName("mts/dataSource/mts_dataSource_edit");
		mv.addObject("msg", "saveDataSource");
//		mv.addObject("dataClassList", dataClassList);
		return mv;
	}

	//
	/**
	 * 保存用户
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveDataSource")
	public ModelAndView saveDataSource() throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
//			return null;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "保存MTSDict");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String falg = pd.getString("FLAG"); 
		String description = pd.getString("DESCRIPTION"); 
		String remark = pd.getString("REMARK"); 
		String newId = RandomUtil.getRandomId();
		
		MtsDataSource mtsDataSource = new MtsDataSource();
		mtsDataSource.setDESCRIPTION(description);
		mtsDataSource.setFLAG(falg);
		mtsDataSource.setREMARK(remark);
		mtsDataSource.setDATA_SOURCE_ID(newId);
		mtsDataSource.setOPERATE_TIME(new Date());
		mtsDataSourceService.addMtsDataSource(mtsDataSource);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 去修改加载规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditDataSource")
	public ModelAndView goEditDataSource() throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
//			return null;
//		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_SOURCE_ID = pd.getString("DATA_SOURCE_ID");
		MtsDataSource mtsDataSource = new MtsDataSource();
		mtsDataSource.setDATA_SOURCE_ID(DATA_SOURCE_ID);
		List<MtsDataSource> dsList = mtsDataSourceService.findMtsDataSource(mtsDataSource);
		if(null != dsList && dsList.size() > 0){
			mtsDataSource = dsList.get(0);
			pd.put("DATA_SOURCE_ID", mtsDataSource.getDATA_SOURCE_ID());
			pd.put("DESCRIPTION", mtsDataSource.getDESCRIPTION());
			pd.put("FLAG", mtsDataSource.getFLAG());
			pd.put("REMARK", mtsDataSource.getREMARK());
		}
		
		mv.setViewName("mts/dataSource/mts_dataSource_edit");
		mv.addObject("msg", "editDataSource");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 修改数据字典
	 */
	@RequestMapping(value = "/editDataSource")
	public ModelAndView editDataSource() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "修改DataSource");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String DATA_SOURCE_ID = pd.getString("DATA_SOURCE_ID");
		String DESCRIPTION = pd.getString("DESCRIPTION");
		String REMARK = pd.getString("REMARK");
		String FLAG = pd.getString("FLAG");
		MtsDataSource mtsDataSource = new MtsDataSource();
		mtsDataSource.setDATA_SOURCE_ID(DATA_SOURCE_ID);
		mtsDataSource.setDESCRIPTION(DESCRIPTION);
		mtsDataSource.setFLAG(FLAG);
		mtsDataSource.setREMARK(REMARK);
		mtsDataSource.setOPERATE_TIME(new Date());
		mtsDataSourceService.editMtsDataSource(mtsDataSource);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除匹配规则
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delMtsDataSource")
	public void delMtsDataSource(PrintWriter out) throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
//			return;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "删除matchRule");
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_SOURCE_ID = pd.getString("DATA_SOURCE_ID");
		MtsDataSource mtsDataSource = new MtsDataSource();
		mtsDataSource.setDATA_SOURCE_ID(DATA_SOURCE_ID);
		mtsDataSourceService.deleteMtsDataSource(mtsDataSource);;
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
	@RequestMapping(value = "/verifyMtsDataSource", method = RequestMethod.POST)
	public Map<String,String> verifyMtsDataSource(String DATA_SOURCE_ID,String FLAG,String DESCRIPTION,String OPERATE) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();  
		String result = "false";
		MtsDataSource mtsDataSource  = null;
		MtsDataSource mtsDataSourceNew = null;
		List<MtsDataSource> dataSourceList = null;
		if("Add".equals(OPERATE)){
			mtsDataSource = new MtsDataSource();
			if(!"".equals(DESCRIPTION) && null != DESCRIPTION){
				mtsDataSource.setDESCRIPTION(DESCRIPTION);
			}
			if(!"".equals(FLAG) && null != FLAG){
				mtsDataSource.setFLAG(FLAG);
			}
			dataSourceList = mtsDataSourceService.findMtsDataSource(mtsDataSource);
			if(dataSourceList != null && dataSourceList.size() > 0){
				result = "false";
			}else{
				result = "success";
			}
		}else if("Edit".equals(OPERATE)){
			mtsDataSource = new MtsDataSource();
			if(!"".equals(DESCRIPTION) && null != DESCRIPTION){
				mtsDataSource.setDESCRIPTION(DESCRIPTION);
			}
			if(!"".equals(FLAG) && null != FLAG){
				mtsDataSource.setFLAG(FLAG);
			}
			dataSourceList = mtsDataSourceService.findMtsDataSource(mtsDataSource);
			if(dataSourceList == null){
				result = "false";
			}else{
				int dataSourceListSize = dataSourceList.size();
				if(dataSourceListSize == 0){
					result = "success";
				}else if(dataSourceListSize == 1){
					mtsDataSourceNew = dataSourceList.get(0);
					String dataSourceId = mtsDataSourceNew.getDATA_SOURCE_ID();
					if(dataSourceId.equals(DATA_SOURCE_ID)){
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
