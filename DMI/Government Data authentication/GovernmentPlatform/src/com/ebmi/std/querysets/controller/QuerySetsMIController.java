package com.ebmi.std.querysets.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.utils.JsonStringUtils;
import com.ebmi.std.common.controller.BaseController;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.querysets.dto.MaternReimbItem;
import com.ebmi.std.querysets.dto.TurnoutItem;
import com.ebmi.std.querysets.service.QuerySetsMIService;

/**
 * 转外就医
 * 
 * @author xiangbao.guan
 *
 */
@Controller
public class QuerySetsMIController extends BaseController {

	@Resource(name="querySetsMIServiceImpl")
	private QuerySetsMIService querySetsMIService;
	
	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;
	
	
	/**
	 * xuhai edit 2016-5-17 15:05:37 
	 * @param p_mi_id
	 * @param recordStart
	 * @param recordCount
	 * @param idcard
	 * @param username
	 * @param ss_num
	 * @return
	 */
	@RequestMapping(value = "/turnoutmi/list.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<TurnoutItem> getTurnoutListMI(String p_mi_id, Integer recordStart
			, Integer recordCount,String idcard,String username,String ss_num) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("p_mi_id", p_mi_id);
			condition.put("recordStart", recordStart);
			condition.put("recordCount", recordCount);
			condition.put("idcard", idcard);
			condition.put("username", username);
			condition.put("ss_num", ss_num);
			List<TurnoutItem> list = querySetsMIService.queryTurnoutListMI(condition);
			return list;
		} catch (Exception e) {
			logger.error("查询转外就医列表报错！", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/allopatrymi/treat.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> getAllopatryTreatMI(String p_mi_id, String name) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("p_mi_id", p_mi_id);
			condition.put("name", name);
			Map<String, Object> map = querySetsMIService.queryAllopatryTreatMI(condition);
			return map;
		} catch (Exception e) {
			logger.error("查询异地就医报错！", e);
		}
		return null;
	}

	@RequestMapping(value = "/maternmi/list.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<MaternReimbItem> getMaternReimbMI(String p_mi_id, Integer recordStart, Integer recordCount) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("p_mi_id", p_mi_id);
			condition.put("recordStart", recordStart);
			condition.put("recordCount", recordCount);
			List<MaternReimbItem> list = querySetsMIService.queryMaternReimbMI(condition);
			return list;
		} catch (Exception e) {
			logger.error("查询生育报销报错！", e);
		}
		return null;
	}

	@RequestMapping(value = "chronicmi/benefit.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> getChronicBenefitMI(String p_mi_id,String idcard,String username,String ss_num) {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("p_mi_id", p_mi_id);
			//xuhai add 
			condition.put("idcard", idcard);//公民身份证号
			condition.put("username", username);//姓名
			condition.put("ss_num", ss_num);//社会保障卡号
			//xuhai add end 
			Map<String, Object> map = querySetsMIService.queryChronicBenefitMI(condition);
			return map;
		} catch (Exception e) {
			logger.error("查询慢性病待遇报错！", e);
		}
		return null;
	}
	
	//测试森工原样返回的接口 
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/data/infos.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody TheKeyResultEntity getData(String methodName, String parmeMap) {
		try {
			System.out.println("test methodName>>>"+methodName);
			System.out.println("test parmeMap>>>"+parmeMap);
			Map<String,String> map  = JsonStringUtils.jsonStringToObject(parmeMap, Map.class);
			TheKeyResultEntity res = baseApi.getDataInfo(map, methodName);
			return res;
		} catch (Exception e) {
			logger.error("测试森工原样返回的接口报错！", e);
		}
		return null;
	}
	
}
