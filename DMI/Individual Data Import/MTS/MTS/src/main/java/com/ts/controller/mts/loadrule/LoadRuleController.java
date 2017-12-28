package com.ts.controller.mts.loadrule;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.LoadRule;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataType;
import com.ts.entity.mts.MtsDict;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.loadrule.LoadRuleManager;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.matchrule.MtsDictManger;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.service.system.role.RoleManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.RandomUtil;
import com.ts.util.SeparatorConstant;
import com.ts.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @类名称: LoadRuleController
 * @类描述: Redis数据装载规则维护
 * @作者:李巍
 * @创建时间:2016年10月8日 下午2:04:26
 */
@Controller
@RequestMapping(value="/loadRule")
public class LoadRuleController extends BaseController {
	
	String menuUrl = "loadrule/listLoadRules.do"; //菜单地址(权限用)
	@Resource(name="loadRuleService")
	private LoadRuleManager loadRuleService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="DictMtsService")
	private MtsDictManger dictMtsService;
	@Resource(name="DataClassService")
	private DataClassManger dataClassService;
	@Resource(name="DataTypeService")
	private DataTypeManger dataTypeService;
	@Resource(name = "MTSAreaService")
	private MTSAreaService mtsAreaService;
	@Resource(name = "StandardizeService")
	private StandardizeService standardizeService;
	
	
	//====================Excel本体数据导入====================
	/**打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/loadrule/uploadexcel");
		return mv;
	}
	
	/**显示规则列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listLoadRules")
	public ModelAndView listLoadRules(Page page)throws Exception{
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
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		List<MtsArea> areaList=mtsAreaService.findMtsArea(new MtsArea());//列出所有区域
		List<PageData>	listLoadRules = loadRuleService.listLoadRules(page);	//列出用户列表
		mv.setViewName("mts/loadrule/load_rule_list");
		mv.addObject("listLoadRules", listLoadRules);
		mv.addObject("areaList", areaList);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("pd", pd);
		return mv;
	}
	/**去新增装载规则页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goAddRule")
	public ModelAndView goAddRule()throws Exception{
		ModelAndView mv = this.getModelAndView();
		String DATACLASS="02";		 
	/*	List<MtsDict> mtsDictValueList = dictMtsService.listValueRuleByClass(DATACLASS);//列出相关字典表信息
		List<MtsDict> mtsDictKeyList = dictMtsService.listKeyRuleByClass(DATACLASS);//列出相关字典表信息
*/		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
//		List<MtsDataType>	dataTypeList =dataTypeService.listAllDataType();//列出所有标化类型
		List<MtsArea> areaList =this.mtsAreaService.findMtsArea(new MtsArea());//区域
		mv.setViewName("mts/loadrule/load_rule_edit");
		mv.addObject("msg", "saveRule");	
//		mv.addObject("mtsDictValueList", mtsDictValueList);
		mv.addObject("dataClassList", dataClassList);
//		mv.addObject("dataTypeList", dataTypeList);
//		mv.addObject("mtsDictKeyList", mtsDictKeyList);
		mv.addObject("areaList", areaList);
		return mv;
	}
//	
	/**保存用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveRule")
	public ModelAndView saveRule() throws Exception{
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
//			return null;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "保存matchRule");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String inkey = "%" + pd.getString("classcode") + "$" + pd.getString("typecode") + pd.getString("keys");
		String invalue = pd.getString("values");
		String lname = pd.getString("loadrulename");
		String keyids = pd.getString("keyids");
		String valueids = pd.getString("valueids");
		String areaid = pd.getString("area");
		String typeid = dataTypeService.findByCode(pd.getString("typecode"));
		String mruleid = loadRuleService.maxRule();
		int Lid = Integer.parseInt(mruleid);
		String Zruleid = String.valueOf(Lid + 1);
		//添加操作
		LoadRule lr = new LoadRule();
		lr.setLOAD_RULE_ID(Zruleid);
		lr.setKEY_GEN_RULE(inkey);
		lr.setVALUE_GEN_RULE(invalue);
		lr.setDATA_TYPE_ID(typeid);
		lr.setLOAD_RULE_NAME(lname);
		lr.setAREA_ID(areaid);
		lr.setOP_DATE(new Date());
		loadRuleService.saveRule(lr);
		//建立新关系
		String maxruledictid = dictMtsService.maxRuleDict();
		int maxruledictidInt= Integer.parseInt(maxruledictid);
		if(StringUtil.isNotBlank(keyids)){
			String[] keydicts  = keyids.split(",");
			for (int i = 0; i < keydicts.length; i++) {
				maxruledictidInt++;
				PageData addpd = new PageData();
				addpd.put("ID", maxruledictidInt);
				addpd.put("RULE_ID", lr.getLOAD_RULE_ID());
				addpd.put("DICT_ID", keydicts[i]);
				dictMtsService.saveRuleDict(addpd);
			}
		}
		if(StringUtil.isNotBlank(valueids)){
			String[] valuedicts  = valueids.split(",");
			for (int i = 0; i < valuedicts.length; i++) {
				maxruledictidInt++;
				PageData addpd = new PageData();
				addpd.put("ID", maxruledictidInt);
				addpd.put("RULE_ID", lr.getLOAD_RULE_ID());
				addpd.put("DICT_ID", valuedicts[i]);
				dictMtsService.saveRuleDict(addpd);
			}
		}
		mv.setViewName("save_result");
        return mv;
	}

	/**
	 * 去修改加载规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditLR")
	public ModelAndView goEditLR() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Page page = new Page();
		page.setPd(pd);
		String ruleid = pd.getString("rule_id");
		//查询规则实体
		LoadRule lred = loadRuleService.findRuleById(ruleid);
		//KEY生成规则
		String inkeys = lred.getKEY_GEN_RULE();
		//VALUE生成规则
		String invalues = lred.getVALUE_GEN_RULE();
		String[] cnl = inkeys.split("@");
		
		String tmptnl = cnl[0].replace("%", "");
		String[] tnl = tmptnl.split("\\$");
		pd.put("LOAD_RULE_NAME", lred.getLOAD_RULE_NAME());
		//标化类型
		pd.put("DATA_TYPE_ID", lred.getDATA_TYPE_ID());
		//主键ID
		pd.put("LOAD_RULE_ID", lred.getLOAD_RULE_ID());
		//聚类
		pd.put("CLASS_CODE", tnl[0]);
		//标化类型
		pd.put("TYPE_CODE", tnl[1]);
		//规则ID
		pd.put("ruleid", ruleid);
		//区域
		pd.put("AREA_ID", lred.getAREA_ID());
		
		
		//后面跟的原始数据名称或码,key加载规则回传值
		//根据区域、聚类、标化类型、以及本身的key值，取到字典表里的值
		//key列表
		List<PageData> returnKeyList = dictMtsService.findMtsKeyDictByRuleID(ruleid);
		//value列表
		List<PageData> returnValueList = dictMtsService.findMtsValueDictByRuleID(ruleid);
		
		pd.put("DATACLASS",tnl[0]);
		pd.put("DATATYPE", tnl[1]);
		pd.put("TYPE", "KEYRULE");
		page.setPd(pd);		
		List<MtsDict> mtsDictKeyList = dictMtsService.listAllByClass(page);
		List<MtsDict> newMtsDictKeyList = new ArrayList<MtsDict>();
		if(mtsDictKeyList != null && mtsDictKeyList.size() >0){
			for (int i = 0; i < mtsDictKeyList.size(); i++) {
				MtsDict mtsDict = mtsDictKeyList.get(i);
				if(returnKeyList != null && returnKeyList.size() >0){
					for (int j = 0; j < returnKeyList.size(); j++) {
						PageData pd4 = returnKeyList.get(j);
						if(pd4.getString("DID").equals(mtsDict.getDID())){
							newMtsDictKeyList.add(mtsDict);
						}
					}
				}
			}
		}
		if(newMtsDictKeyList != null && newMtsDictKeyList.size() >0){
			for (int i = 0; i < newMtsDictKeyList.size(); i++) {
				MtsDict mtsDict = newMtsDictKeyList.get(i);
				mtsDictKeyList.remove(mtsDict);
			}
		}
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType> dataTypeList = dataTypeService.listClassDataType(tnl[0]);// 列出相应聚类下的标化类型
		pd.put("TYPE", "VALUERULE");
		page.setPd(pd);		
		List<MtsDict> mtsDictValueList = dictMtsService.listAllByClass(page);// 列出相关字典表信息value
		List<MtsDict> newMtsDictValueList = new ArrayList<MtsDict>();
		if(mtsDictValueList != null && mtsDictValueList.size() >0){
			for (int i = 0; i < mtsDictValueList.size(); i++) {
				MtsDict mtsDict = mtsDictValueList.get(i);
				if(returnValueList != null && returnValueList.size() >0){
					for (int j = 0; j < returnValueList.size(); j++) {
						PageData pd4 = returnValueList.get(j);
						if(pd4.getString("DID").equals(mtsDict.getDID())){
							newMtsDictValueList.add(mtsDict);
						}
					}
				}
			}
		}
		if(newMtsDictValueList != null && newMtsDictValueList.size() >0){
			for (int i = 0; i < newMtsDictValueList.size(); i++) {
				MtsDict mtsDict = newMtsDictValueList.get(i);
				mtsDictValueList.remove(mtsDict);
			}
		}
		List<MtsArea> areaList =this.mtsAreaService.findMtsArea(new MtsArea());//区域
		mv.setViewName("mts/loadrule/load_rule_edit");
		mv.addObject("msg", "editLR");
		mv.addObject("pd", pd);
		mv.addObject("returnKeyList", returnKeyList);
		mv.addObject("returnValueList", returnValueList);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("mtsDictValueList", mtsDictValueList);
		mv.addObject("mtsDictKeyList", mtsDictKeyList);
		mv.addObject("areaList", areaList);
		return mv;
	}
	
	private String[] spellParamStr(String inkeys){
		List<String> list = new ArrayList<String>();
		inkeys = inkeys.substring(inkeys.indexOf("@")+1);
		String[] keyrules = inkeys.split("@");
		String paramKeyStr = "";
		String[] ids = null;
		if(keyrules.length>0){
			ids = new String[keyrules.length];
			for (int i = 0; i < keyrules.length; i++) {
//				String key = keyrules[i];
				ids[i] = keyrules[i];
//				paramKeyStr+=key+",";
				/*if("word".equals(validateRE(key))){
					paramKeyStr+="'"+key+"',";
				}else{
					paramKeyStr+=key+",";
				}*/
			}
//			paramKeyStr = paramKeyStr.substring(0, paramKeyStr.length()-1);
		}
		return ids;
	}

	/**
	 * 修改匹配规则
	 */
	@RequestMapping(value = "/editLR")
	public ModelAndView editLR() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "修改loadRule");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keyids = pd.getString("keyids");
		String valueids = pd.getString("valueids");
		String inkey = "%" + pd.getString("classcode") + "$" + pd.getString("typecode")  + pd.getString("keys");
		String typeid = dataTypeService.findByCode(pd.getString("typecode"));
		String invalue = pd.getString("values");
		String areaid = pd.getString("area");
		LoadRule lr = new LoadRule();
		lr.setKEY_GEN_RULE(inkey);
		lr.setVALUE_GEN_RULE(invalue);
		lr.setDATA_TYPE_ID(typeid);
		lr.setLOAD_RULE_NAME(pd.getString("loadrulename"));
		lr.setLOAD_RULE_ID(pd.getString("rid"));
		lr.setAREA_ID(areaid);
		lr.setOP_DATE(new Date());
		pd.put("keyids", keyids);
		pd.put("valueids", valueids);
		loadRuleService.editRule(lr);
		//删除关系
		dictMtsService.deleteMtsRuleDict(lr.getLOAD_RULE_ID());
		//建立新关系
		String maxruledictid = dictMtsService.maxRuleDict();
		int maxruledictidInt= Integer.parseInt(maxruledictid);
		if(StringUtil.isNotBlank(keyids)){
			String[] keydicts  = keyids.split(",");
			for (int i = 0; i < keydicts.length; i++) {
				maxruledictidInt++;
				PageData addpd = new PageData();
				addpd.put("ID", maxruledictidInt);
				addpd.put("RULE_ID", lr.getLOAD_RULE_ID());
				addpd.put("DICT_ID", keydicts[i]);
				dictMtsService.saveRuleDict(addpd);
			}
		}
		if(StringUtil.isNotBlank(valueids)){
			String[] valuedicts  = valueids.split(",");
			for (int i = 0; i < valuedicts.length; i++) {
				maxruledictidInt++;
				PageData addpd = new PageData();
				addpd.put("ID", maxruledictidInt);
				addpd.put("RULE_ID", lr.getLOAD_RULE_ID());
				addpd.put("DICT_ID", valuedicts[i]);
				dictMtsService.saveRuleDict(addpd);
			}
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除匹配规则
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteLR")
	public void deleteLR(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除loadRule");
		PageData pd = new PageData();
		pd = this.getPageData();
		String RULE_ID = pd.getString("rule_id");
		loadRuleService.deleteRule(RULE_ID);
		dictMtsService.deleteMtsRuleDict(RULE_ID);
		out.write("success");
		out.close();
	}
	
	public static void main(String[] args) {
		String str = "@0@1@2";
		String orig_data_str = "101246|10001|101";
		//String[] strs = str.split("@");
		str = str.substring(str.indexOf("@")+1);
		String[] keyrules = str.split("@");
		String temp_str = null;
		for (int i = 0; i < keyrules.length; i++) {
			String key = keyrules[i];
			//根据区域、聚类、标化类型、以及本身的值，取到字典表里的值
			if("number".equals(validateRE(key))){
				temp_str = orig_data_str.split(SeparatorConstant.dataValueSeparator)[Integer.parseInt(key)];
			}else if("word".equals(validateRE(key))){
				
			}
			
		}
		//System.out.println(validateRE(str));
	}
	

	/**
	 * 
	 * @方法名称: validateRE
	 * @功能描述: 正则匹配数字下标 或是数据名称
	 * @作者:李巍
	 * @创建时间:2016年9月28日 下午2:55:16
	 * @param line
	 * @return String
	 */
	public static String validateRE(String line) {
		String pattern = "(@\\d)+";
		String pattern2 = "(@\\D)+";
		String pattern3 = "(@\\d)+(@\\D)";
		String pattern4 = "(@\\D)+(@\\d)";
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(line);
		// 创建 Pattern 对象
		Pattern r2 = Pattern.compile(pattern2);
		// 现在创建 matcher 对象s
		Matcher m2 = r2.matcher(line);
		Pattern r3 = Pattern.compile(pattern3);
		// 现在创建 matcher 对象
		Matcher m3 = r3.matcher(line);
		Pattern r4 = Pattern.compile(pattern4);
		// 现在创建 matcher 对象
		Matcher m4 = r4.matcher(line);
		if (m.find()) {
			return "number";
		} else if (m2.find()) {
			return "word";
		}  else if (m3.find() || m4.find()) {
			return "wordAndNumber";
		} else {
			//logger.info(line + "===未知匹配类型===!!");
			return "";
		}
	}

	
	
	
	@RequestMapping(value="/goGetMtsResult")
	public ModelAndView goGetMtsResult()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/loadrule/mts_result");
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMtsResult", produces = "application/json;charset=UTF-8" )
	public String getMtsResult(String toBeStandardStr) throws Exception {
		String visitId = "";
		String visitType = "";
		String dataSource = "";
		String dataType = "";
		String parameters = "";
//		String batchNum = "";
		String resultStr = "";
		String application = "";
		String areaId = "";
		
		if (toBeStandardStr == null || "".equals(toBeStandardStr)) {
			return null;
		}
		JSONObject dealWithStrJSON = JSONObject.fromObject(toBeStandardStr);
		if (dealWithStrJSON.containsKey("visitId")) {
			visitId = dealWithStrJSON.getString("visitId");
		}
		if (dealWithStrJSON.containsKey("visitType")) {
			visitType = dealWithStrJSON.getString("visitType");
		}
		if (dealWithStrJSON.containsKey("dataSource")) {
			dataSource = dealWithStrJSON.getString("dataSource");
		}
		if (dealWithStrJSON.containsKey("dataType")) {
			dataType = dealWithStrJSON.getString("dataType");
		}
		if (dealWithStrJSON.containsKey("parameters")) {
			parameters = dealWithStrJSON.getString("parameters");
		}
		if (dealWithStrJSON.containsKey("application")) {
			application = dealWithStrJSON.getString("application");
		}
		if (dealWithStrJSON.containsKey("areaId")) {
			areaId = dealWithStrJSON.getString("areaId");
		}
		
		String batchNum = RandomUtil.getRandomId();
		
		try {
			resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, parameters, batchNum,application,areaId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultStr;
	}
	
}
