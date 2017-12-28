package com.ts.controller.nlp.data;

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
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.util.PageData;
import com.ts.util.StringUtil;
import com.ts.util.redis.IRedisUtil;
import com.ts.util.redis.RedisKeys;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value = "/redisFind")
public class RedisFindController extends BaseController{
	
	@Resource(name = "redisUtil")
	private IRedisUtil redisDao;
	@Resource(name = "DataClassService")
	private DataClassManger dataClassService;
	@Resource(name = "DataTypeService")
	private DataTypeManger dataTypeService;
	@Resource(name="MTSAreaService")
	private MTSAreaService mTSAreaService;
	
	
	
	/**
	 * 去redis查询页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goRedisFind")
	public ModelAndView goRedisFind(Page page) throws Exception {

		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATACLASS = "02";
		pd.put("DATACLASS", DATACLASS);		
		
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType> dataTypeList = dataTypeService.listClassDataType(DATACLASS);// 列出相应聚类下的标化类型
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域
		mv.setViewName("mts/redisFind/redis_find");
		//mv.addObject("msg", "saveMR");	
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("areaList", areaList);
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
	
	
	
	@RequestMapping(value = "/findOne")
	@ResponseBody
	public String findOne(String areaid,String css,String bhlx,String inkey) {	
		JSONObject jso = new JSONObject();
		String inkeyL=StringUtil.full2Half(inkey).toUpperCase().trim();
		
		String key=areaid+"#"+css+"#"+bhlx+"#"+inkeyL;		
		String value=redisDao.get(RedisKeys.SYSMTS, key);	
		if(value==null||"".equals(value)){
			value="none";
		}
		jso.put("one", value);
		return jso.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/findMore")
	public String findMore(String areaid,String css,String bhlx) {	
		
		
		String key=areaid+"#"+css+"#"+bhlx+"*";			
		Long value=redisDao.countKey(key);	 
	
		return value.toString();
	}
	

}
