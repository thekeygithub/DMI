package com.ts.controller.mts.matchrule;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataType;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.matchrule.MatchRuleDetailManger;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value="/detailRule")
public class DetailRuleController extends BaseController{
	
	String menuUrl = "detailRule/listDetailRules.do";
	@Resource(name="DataMatchRuleDetail")
	private MatchRuleDetailManger dataMatchRuleDetail;
	@Resource(name="DataClassService")
	private DataClassManger dataClassService;
	@Resource(name="DataTypeService")
	private DataTypeManger dataTypeService;
	@Resource(name = "mapConfigService")
	private MapConfigService mapConfigService;
	@Resource(name="MTSAreaService")
	private MTSAreaService mTSAreaService;
	@Resource(name = "MapCacheService")
	private MapCacheService mapCacheService;
	
	
	

	/**查询匹配规则详细列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listDetailRules")
	public ModelAndView listDetailRules(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String dataClassCode = pd.getString("DATA_CLASS_CODE");	//聚类
		String area = pd.getString("AREA_ID"); // 区域
		if(dataClassCode != null && !"".equals(dataClassCode)){
			pd.put("DATA_CLASS_CODE", dataClassCode);
		} 
		if(area != null && !"".equals(area)){
			pd.put("AREA_ID", area);
		} 
		page.setPd(pd);
		List<PageData>	detailRuleList = dataMatchRuleDetail.listRuleDetail(page);	//列出匹配规则列表
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域
		
		
		mv.setViewName("mts/matchruledetail/detail_rule_list");
		mv.addObject("detailRuleList", detailRuleList);
		mv.addObject("pd", pd);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("areaList", areaList);
		return mv;
	}
	
	

	/**去新增匹配规则详细页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goAddDR")
	public ModelAndView goAddDR()throws Exception{
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();	
		PageData pd = new PageData();
		pd = this.getPageData();
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		List<MtsDataType>	dataTypeList =dataTypeService.listAllDataType();//列出所有标化类型		
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域
		List<PageData> softList=dataMatchRuleDetail.findSoft();//查询所有程序列表
		
		mv.setViewName("mts/matchruledetail/detail_rule_edit");
		mv.addObject("msg", "saveDR");
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("areaList", areaList);
		mv.addObject("softList", softList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**新增匹配规则详细
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveDR")
	public ModelAndView saveDR(Page page) throws Exception{
//		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"保存MTS_MATCH_RULE_DETAIL");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String mruleid=dataMatchRuleDetail.maxRule();
		int Lid=Integer.parseInt(mruleid);
		String Zruleid=String.valueOf(Lid+1);
		
		
		MatchRuleDetailT mrdt=new MatchRuleDetailT();
		mrdt.setMEM_ID(Zruleid);
		mrdt.setDATA_CLASS_CODE(pd.getString("classcode"));
		mrdt.setMEM_DATA_CODE(pd.getString("typecode"));
		mrdt.setORDERBY(pd.getString("orderby"));
		
		if(null != pd.getString("pinkey") && !"".equals(pd.getString("pinkey"))){
			mrdt.setPINKEY(pd.getString("pinkey"));
		}else{
			mrdt.setPINKEY("");
		}
		
		if(null != pd.getString("ifnext") && !"".equals(pd.getString("ifnext"))){
			mrdt.setIFNEXT(pd.getString("ifnext"));
		}else{
			mrdt.setIFNEXT("");
		}
		
		if(pd.getString("flag")=="1"||"1".equals(pd.getString("flag"))){
			mrdt.setPINKEY("");
			mrdt.setIFNEXT("");
		}
		
		mrdt.setQUESTION(pd.getString("ques"));
		mrdt.setMARK(pd.getString("mark"));
		if(null != pd.getString("sucessto") && !"".equals(pd.getString("sucessto"))){
			mrdt.setSUCCESS_REDIRECT_TO(pd.getString("sucessto"));
		}else{
			mrdt.setSUCCESS_REDIRECT_TO("");
		}
		
		if(null != pd.getString("fallto") && !"".equals(pd.getString("fallto"))){
			mrdt.setFAILURE_REDIRECT_TO(pd.getString("fallto"));
		}else{
			mrdt.setFAILURE_REDIRECT_TO("");
		}		
		
		mrdt.setNOC(pd.getString("noc"));
		mrdt.setFLAG(pd.getString("flag"));
		mrdt.setAPPLY_METHOD(pd.getString("appMethod"));
		mrdt.setAREA_ID(pd.getString("area"));
		
		
		MatchRuleDetailT yzmrdt=null;
		if(pd.getString("orderby")=="1"||"1".equals(pd.getString("orderby"))){
			pd.put("DATA_CLASS_CODE", pd.getString("classcode"));
			pd.put("AREA_ID", pd.getString("area"));
			page.setPd(pd);
			yzmrdt=dataMatchRuleDetail.findRuleDetail(page);
		}
		
		
		if(yzmrdt!=null){
			mv.addObject("msg", "添加失败！此流程入口已存在！");
		}else{
		dataMatchRuleDetail.addRuleDetail(mrdt);
		mapConfigService.ReLoadCache();//重新加载缓存
		mapCacheService.ReLoadCache();
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	
	/**去修改匹配规则详细页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditDR")
	public ModelAndView goEditDR(Page page) throws Exception{
//		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String mem_id=pd.getString("memid");		
		MatchRuleDetailT mred=dataMatchRuleDetail.findRuleDetailById(mem_id);
		String classcode=mred.getDATA_CLASS_CODE();
		String area=mred.getAREA_ID();
		String  flag=mred.getFLAG();
		pd.put("AREA_ID", area);
		pd.put("MEM_ID", mem_id);
		pd.put("DATA_CLASS_CODE", classcode);
		pd.put("FLAG", flag);
		
		page.setPd(pd);
		
		
		
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		List<MtsDataType> dataTypeList = dataTypeService.listClassDataType(classcode);// 列出相应聚类下的标化类型
		
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域
		List<PageData> softList=dataMatchRuleDetail.findSoft();//查询所有程序列表
		List<PageData>	sFList =dataMatchRuleDetail.listSFDetail(page);//列出所有成功失败跳转步骤
		
		mv.setViewName("mts/matchruledetail/detail_rule_edit");
		mv.addObject("msg", "editMR");
		mv.addObject("mred", mred);	
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("areaList", areaList);
		mv.addObject("softList", softList);
		mv.addObject("sFList", sFList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	

	/**修改匹配规则详细
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editMR")
	public ModelAndView editMR(Page page) throws Exception{
//		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"修改MTS_MATCH_RULE_DETAIL");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
				
		MatchRuleDetailT mrdt=new MatchRuleDetailT();
		String meid=pd.getString("memid");
		mrdt.setMEM_ID(meid);
		mrdt.setDATA_CLASS_CODE(pd.getString("classcode"));
		mrdt.setMEM_DATA_CODE(pd.getString("typecode"));
		mrdt.setORDERBY(pd.getString("orderby"));
		if(null != pd.getString("ifnext") && !"".equals(pd.getString("ifnext"))){
			mrdt.setIFNEXT(pd.getString("ifnext"));
		}else{
			mrdt.setIFNEXT("");
		}
		if(null != pd.getString("pinkey") && !"".equals(pd.getString("pinkey"))){
			mrdt.setPINKEY(pd.getString("pinkey"));
		}else{
			mrdt.setPINKEY("");
		}
		
		if(pd.getString("flag")=="1"||"1".equals(pd.getString("flag"))){
			mrdt.setPINKEY("");
			mrdt.setIFNEXT("");
		}
		
		mrdt.setQUESTION(pd.getString("ques"));
		mrdt.setMARK(pd.getString("mark"));
		if(null != pd.getString("sucessto") && !"".equals(pd.getString("sucessto"))){
			mrdt.setSUCCESS_REDIRECT_TO(pd.getString("sucessto"));
		}else{
			mrdt.setSUCCESS_REDIRECT_TO("");
		}
		
		if(null != pd.getString("fallto") && !"".equals(pd.getString("fallto"))){
			mrdt.setFAILURE_REDIRECT_TO(pd.getString("fallto"));
		}else{
			mrdt.setFAILURE_REDIRECT_TO("");
		}		
		
		mrdt.setNOC(pd.getString("noc"));
		mrdt.setFLAG(pd.getString("flag"));
		mrdt.setAPPLY_METHOD(pd.getString("appMethod"));
		mrdt.setAREA_ID(pd.getString("area"));
		
		MatchRuleDetailT yzmrdt=null;
		
		if(pd.getString("orderby")=="1"||"1".equals(pd.getString("orderby"))){
			pd.put("DATA_CLASS_CODE", pd.getString("classcode"));
			pd.put("AREA_ID", pd.getString("area"));
			page.setPd(pd);
			yzmrdt=dataMatchRuleDetail.findRuleDetail(page);
		}
		
		
		if(yzmrdt!=null && !meid.equals(yzmrdt.getMEM_ID())){
			mv.addObject("msg", "修改失败！此流程入口已存在！");
		}else{
		dataMatchRuleDetail.editRuleDetail(mrdt);	
		mapConfigService.ReLoadCache();//重新加载缓存
		mapCacheService.ReLoadCache();
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除匹配详细规则
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteDR")
	public void deleteDR(PrintWriter out) throws Exception{
//		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"删除MTS_MATCH_RULE_DETAIL");
		PageData pd = new PageData();
		pd = this.getPageData();
		String memid=pd.getString("memid");
		dataMatchRuleDetail.deleteRuleDetail(memid);
		mapConfigService.ReLoadCache();//重新加载缓存
		mapCacheService.ReLoadCache();
		out.write("success");
		out.close();
	}
	
	
	/**
	 * ajax查询标化类型
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selType")
	public String selDict(String classcode) throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List<MtsDataType>> map = new HashMap<String, List<MtsDataType>>();

		List<MtsDataType> dataTypeList = dataTypeService.listClassDataType(classcode);// 列出相应聚类下的标化类型

		if (dataTypeList != null && dataTypeList.size() > 0) {			
			map.put("listDataType", dataTypeList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}
	
	
	/**
	 * ajax查询成功失败流转
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selDetail")
	public String selDetail(String AREA_ID,String MEMID,String FLAG,String classcode) throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List<PageData>> map = new HashMap<String, List<PageData>>();
		Page page=new Page();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("AREA_ID", AREA_ID);
		pd.put("MEM_ID", MEMID);
		pd.put("FLAG", FLAG);
		pd.put("DATA_CLASS_CODE", classcode);
		page.setPd(pd);
		List<PageData>	sFList =dataMatchRuleDetail.listSFDetail(page);//列出所有成功失败跳转步骤

		if (sFList != null && sFList.size() > 0) {			
			map.put("listDetail", sFList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}

}
