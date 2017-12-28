package com.ts.controller.mts.matchrule;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.ts.entity.mts.MatchRule;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataType;
import com.ts.entity.mts.MtsDict;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.matchrule.MatchRuleManger;
import com.ts.service.mts.matchrule.MtsDictManger;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/matchRule")
public class MatchRuleController extends BaseController {

	String menuUrl = "matchRule/listMatchRules.do";
	@Resource(name = "DataMatchRule")
	private MatchRuleManger dataMatchRule;
	@Resource(name = "DictMtsService")
	private MtsDictManger dictMtsService;
	@Resource(name = "DataClassService")
	private DataClassManger dataClassService;
	@Resource(name = "DataTypeService")
	private DataTypeManger dataTypeService;
	@Resource(name = "mapConfigService")
	private MapConfigService mapConfigService;
	@Resource(name="MTSAreaService")
	private MTSAreaService mTSAreaService;

	/**
	 * 查询匹配规则列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMatchRules")
	public ModelAndView listMatchRules(Page page) throws Exception {
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
		List<PageData> matchRuleList = dataMatchRule.matchRules(page); // 列出匹配规则列表
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域

		mv.setViewName("mts/matchrule/match_rule_list");
		mv.addObject("matchRuleList", matchRuleList);
		mv.addObject("pd", pd);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("areaList", areaList);
		return mv;
	}

	/**
	 * 去新增匹配规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddMR")
	public ModelAndView goAddMR(Page page) throws Exception {

		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATACLASS = "02";
		pd.put("DATACLASS", DATACLASS);
		pd.put("TYPE", "KEYRULE");
		page.setPd(pd);
		List<MtsDict> mtsDictKeyList = dictMtsService.listAllByClass(page);// 列出相关字典表信息key		
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType> dataTypeList = dataTypeService.listClassDataType(DATACLASS);// 列出相应聚类下的标化类型
		pd.put("TYPE", "VALUERULE");
		page.setPd(pd);
		List<MtsDict> mtsDictValueList = dictMtsService.listAllByClass(page);// 列出相关字典表信息value
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域
		mv.setViewName("mts/matchrule/match_rule_edit");
		mv.addObject("msg", "saveMR");
		mv.addObject("mtsDictKeyList", mtsDictKeyList);
		mv.addObject("mtsDictValueList", mtsDictValueList);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("areaList", areaList);
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 保存匹配规则
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveMR")
	public ModelAndView saveMR(Page page) throws Exception {

		logBefore(logger, Jurisdiction.getUsername() + "保存matchRule");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String inkey =  pd.getString("area")+"#"+pd.getString("classcode") + "#" + pd.getString("typecode") + "#" + pd.getString("keys");
		String typeid = dataTypeService.findByCode(pd.getString("typecode"));
		String mruleid = dataMatchRule.maxRule();
		int Lid = Integer.parseInt(mruleid);
		String Zruleid = String.valueOf(Lid + 1);
		
		pd.put("AREA_ID", pd.getString("area"));
		pd.put("DATA_TYPE_ID", typeid);	
		page.setPd(pd);		
		MatchRule yzmr=dataMatchRule.findByAreaType(page);
		
		if(yzmr!=null){
			
			mv.addObject("msg", "添加失败！该区域下的此标化类型规则已存在！");
		}else{
		
			MatchRule mr = new MatchRule();
			mr.setIFNLP(pd.getString("ifnlp"));
			mr.setKEY_GEN_RULE(inkey);
			mr.setSRC_CODE("");
			mr.setVALUE_STR(pd.getString("values"));
			mr.setSTANDARDE(pd.getString("stand"));
			mr.setDATA_TYPE_ID(typeid);
			mr.setMATCH_RULE_ID(Zruleid);
			mr.setAREA_ID(pd.getString("area"));
	
			dataMatchRule.addRule(mr);
			mapConfigService.ReLoadCache();//重新加载缓存
		}
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 去修改匹配规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditMR")
	public ModelAndView goEditMR(Page page) throws Exception {

		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String ruleid = pd.getString("rule_id");
		MatchRule mred = dataMatchRule.findRuleById(ruleid);
		String inkeys = mred.getKEY_GEN_RULE();
		String invalues = mred.getVALUE_STR();
		String[] cnl = inkeys.split("#");	
		String[] val = invalues.split("@#&");

		pd.put("DATA_TYPE_ID", mred.getDATA_TYPE_ID());
		pd.put("MATCH_RULE_ID", mred.getMATCH_RULE_ID());
		pd.put("IF_NLP", mred.getIFNLP());
		pd.put("STANDARDE", mred.getSTANDARDE());
		pd.put("CLASS_CODE", cnl[1]);
		pd.put("TYPE_CODE", cnl[2]);
		pd.put("AREA_ID", mred.getAREA_ID());
		pd.put("ruleid", ruleid);
		List<String> keyList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		for (int i = 3; i < cnl.length; i++) {
			keyList.add(cnl[i]);
		}
		for (int j = 0; j < val.length; j++) {
			valueList.add(val[j]);
		}
		
		pd.put("DATACLASS", cnl[1]);
		pd.put("DATATYPE", cnl[2]);
		pd.put("TYPE", "KEYRULE");
		page.setPd(pd);		
		List<MtsDict> mtsDictKeyList = dictMtsService.listAllByClass(page);// 列出相关字典表信息key
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType> dataTypeList = dataTypeService.listClassDataType(cnl[1]);// 列出相应聚类下的标化类型
		pd.put("TYPE", "VALUERULE");
		page.setPd(pd);		
		List<MtsDict> mtsDictValueList = dictMtsService.listAllByClass(page);// 列出相关字典表信息value
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域

		mv.setViewName("mts/matchrule/match_rule_edit");
		mv.addObject("msg", "editMR");
		mv.addObject("pd", pd);
		mv.addObject("keyList", keyList);
		mv.addObject("valueList", valueList);
		mv.addObject("mtsDictKeyList", mtsDictKeyList);
		mv.addObject("mtsDictValueList", mtsDictValueList);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("areaList", areaList);
		return mv;
	}

	/**
	 * 修改匹配规则
	 */
	@RequestMapping(value = "/editMR")
	public ModelAndView editMR(Page page) throws Exception {

		logBefore(logger, Jurisdiction.getUsername() + "修改matchRule");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String inkey = pd.getString("area")+"#"+pd.getString("classcode") + "#" + pd.getString("typecode") + "#" + pd.getString("keys");
		String typeid = dataTypeService.findByCode(pd.getString("typecode"));
		String ruleid=pd.getString("rid");
		
		pd.put("AREA_ID", pd.getString("area"));
		pd.put("DATA_TYPE_ID", typeid);	
		page.setPd(pd);		
		MatchRule yzmr=dataMatchRule.findByAreaType(page);
		
		if(yzmr!=null && !ruleid.equals(yzmr.getMATCH_RULE_ID())){
			
			mv.addObject("msg", "修改失败！该区域下的此标化类型规则已存在！");
		}
		
		MatchRule mr = new MatchRule();
		mr.setIFNLP(pd.getString("ifnlp"));
		mr.setKEY_GEN_RULE(inkey);
		mr.setSRC_CODE("");
		mr.setVALUE_STR(pd.getString("values"));
		mr.setSTANDARDE(pd.getString("stand"));
		mr.setDATA_TYPE_ID(typeid);
		mr.setMATCH_RULE_ID(ruleid);
		mr.setAREA_ID(pd.getString("area"));

		dataMatchRule.editRule(mr);
		mapConfigService.ReLoadCache();//重新加载缓存
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 删除匹配规则
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteMR")
	public void deleteMR(PrintWriter out) throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
//			return;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "删除matchRule");
		PageData pd = new PageData();
		pd = this.getPageData();
		String RULE_ID = pd.getString("rule_id");
		dataMatchRule.deleteRule(RULE_ID);
		mapConfigService.ReLoadCache();//重新加载缓存
		out.write("success");
		out.close();
	}

	/**
	 * ajax查询字典表
	 * liuwei
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selDict")
	public String selDict(String classcode,String typecode) throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List> map = new HashMap<String, List>();
		Page page=new Page();
		PageData pd = new PageData();
		pd.put("DATACLASS", classcode);
		pd.put("DATATYPE", typecode);
		pd.put("TYPE", "KEYRULE");
		page.setPd(pd);		
		List<MtsDict> mtsDictKeyList = dictMtsService.listAllByClass(page);// 列出相关字典表中key信息
		List<MtsDataType> dataTypeList = dataTypeService.listClassDataType(classcode);// 列出相应聚类下的标化类型
		pd.put("TYPE", "VALUERULE");
		page.setPd(pd);	
		List<MtsDict> mtsDictValueList = dictMtsService.listAllByClass(page);// 列出相关字典表中value信息
		
		if (mtsDictKeyList != null && mtsDictKeyList.size() > 0) {			
			map.put("listK", mtsDictKeyList);
		}
		if (mtsDictValueList != null && mtsDictValueList.size() > 0) {			
			map.put("listV", mtsDictValueList);
		}
		if (dataTypeList != null && dataTypeList.size() > 0) {			
			map.put("listDataType", dataTypeList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}
	
	/**
	 * ajax查询字典表
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selKeyDict")
	public String selKeyDict(String classcode) throws Exception {
		JSONObject jso = new JSONObject();
		//System.out.println(classcode);
		List<MtsDict> mtsDictList = dictMtsService.listKeyRuleByClass(classcode);
		if (mtsDictList != null && mtsDictList.size() > 0) {
			Map<String, List<MtsDict>> map = new HashMap<String, List<MtsDict>>();
			map.put("list", mtsDictList);
			jso = JSONObject.fromObject(map);

		}
		return jso.toString();
	}
	
	/**
	 * ajax查询字典表
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selValueDict")
	public String selValueDict(String classcode) throws Exception {
		JSONObject jso = new JSONObject();
		//System.out.println(classcode);
		List<MtsDict> mtsDictList = dictMtsService.listValueRuleByClass(classcode);
		if (mtsDictList != null && mtsDictList.size() > 0) {
			Map<String, List<MtsDict>> map = new HashMap<String, List<MtsDict>>();
			map.put("list", mtsDictList);
			jso = JSONObject.fromObject(map);
		}
		return jso.toString();
	}

}
