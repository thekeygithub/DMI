package com.ts.controller.mts.matchrule;

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
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataType;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfig;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.matchrule.MatchRuleDetailManger;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;

import net.sf.json.JSONObject;



@Controller
@RequestMapping(value="/mtsOneStand")
public class MtsOneStand  extends BaseController{
	
	@Resource(name="DataMatchRuleDetail")
	private MatchRuleDetailManger dataMatchRuleDetail;
	@Resource(name="DataClassService")
	private DataClassManger dataClassService;
	@Resource(name="DataTypeService")
	private DataTypeManger dataTypeService;
	@Resource(name="MTSAreaService")
	private MTSAreaService mTSAreaService;
	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	@Resource(name="MtsConfig")
	private MtsConfig mc;
	
	private  Map<String, String> matchRuleCacheMap ;// 匹配规则缓存容器	
	
	/**去单独标化页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goMtsOne")
	public ModelAndView goMtsOne()throws Exception{		
		ModelAndView mv = this.getModelAndView();	
		PageData pd = new PageData();
		pd = this.getPageData();
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		List<MtsDataType>	dataTypeList =dataTypeService.listAllDataType();//列出所有标化类型		
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域
		List<PageData> softList=dataMatchRuleDetail.findSoft();//查询所有程序列表
		
		mv.setViewName("mts/configTest/mts_one_stand");		
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("areaList", areaList);
		mv.addObject("softList", softList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * ajax查询标化类型
	 * liuwei
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
	 * 单独标准化
	 * @throws Exception 
	 */
	@RequestMapping(value="/standOne")	
	public ModelAndView standOne(Page page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"标准化MtsConfigTest");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();		
		pd = this.getPageData();
		
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();	
		
		String area = pd.getString("AREA_ID"); // 区域
		String dataClassCode = pd.getString("classcode");	//聚类
		String bhlx = pd.getString("typecode"); // 标化类型
		String method = pd.getString("appMethod");	//匹配程序
		String key = pd.getString("key");	//匹配内容
		String outJson="{}";
		
		JSONObject jskey = new JSONObject(); 		 
		jskey.put("data",bhlx+"@#&"+key);
		JSONObject jsclass = new JSONObject(); 
		jsclass.put("dataType",dataClassCode);
		
		if("matchPRKS".equals(method)){			
			outJson=mc.matchPRKS(jsclass.toString(), jskey.toString(), area);					

		}else if("matchNlp".equals(method)){
			
			 outJson=mc.matchNlp(jsclass.toString(), jskey.toString(), area);			
			
		}else if("match".equals(method)){
			
			 outJson=mc.match(jsclass.toString(), jskey.toString(), area);			
			
		}else if("matchNlpPRKS".equals(method)){
			
			 outJson=mc.matchNlpPRKS(jsclass.toString(), jskey.toString(), area);			
			
		}else if("special".equals(method)){
			
			 outJson=mc.special(jsclass.toString(), jskey.toString(), area);			
			
		}else if("doubt".equals(method)){
			
			 outJson=mc.doubt(jsclass.toString(), jskey.toString(), area);			
		}else if("specialPRKS23".equals(method)){
			
			 outJson=mc.specialPRKS23(jsclass.toString(), jskey.toString(), area);			
		}else if("matchPRKS23".equals(method)){
			
			 outJson=mc.matchPRKS23(jsclass.toString(), jskey.toString(), area);			
		}else if("matchNlpPRKS23".equals(method)){
			
			 outJson=mc.matchNlpPRKS23(jsclass.toString(), jskey.toString(), area);			
		}else if("tsckZL".equals(method)){
			
			 outJson=mc.tsckZL(jsclass.toString(), jskey.toString(), area);			
		}else if("tsckSHZT".equals(method)){
			
			 outJson=mc.tsckSHZT(jsclass.toString(), jskey.toString(), area);			
		}else if("specialPRKS".equals(method)){
			
			 outJson=mc.specialPRKS(jsclass.toString(), jskey.toString(), area);			
		}
		
		
		JSONObject temp=JSONObject.fromObject(outJson);
		
		if(temp.has("result")){	
			//特殊字符处理后的剩余字符串如果为空的话，则此术语直接舍弃，不输出标化结果20170711
			String result=temp.getString("result");
			if("spec".equals(result)){
				result="";
			}
			pd.put("result", result);
		}
		if(temp.has("status")){			
			pd.put("status", temp.getString("status"));
		}
		if(temp.has("NLP")){			
			pd.put("NLP", temp.getString("NLP"));
		}		
		if(temp.has("NlpValue")){			
			pd.put("NlpValue", temp.getString("NlpValue"));
		}		
		if(temp.has("SPEC")){			
			pd.put("SPEC", temp.getString("SPEC"));
		}
		if(temp.has("TSBH")){			
			pd.put("TSBH", temp.getString("TSBH"));
		}
		if(temp.has("doubt")){		
			pd.put("doubt", temp.getString("doubt"));
		}
		pd.put("souceData", key);
		
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		List<MtsDataType>	dataTypeList =dataTypeService.listAllDataType();//列出所有标化类型		
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域
		List<PageData> softList=dataMatchRuleDetail.findSoft();//查询所有程序列表
		
		
		mv.setViewName("mts/configTest/mts_one_stand");	
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("areaList", areaList);
		mv.addObject("softList", softList);		
		mv.addObject("pd", pd);
		return mv;
	}
	
}
