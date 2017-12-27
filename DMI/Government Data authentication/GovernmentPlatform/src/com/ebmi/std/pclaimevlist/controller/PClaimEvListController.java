/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.pclaimevlist.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.utils.JsonStringUtils;
import com.ebmi.std.pclaimevlist.dto.PClaimEvList;
import com.ebmi.std.pclaimevlist.dto.PClaimModel;
import com.ebmi.std.pclaimevlist.service.PClaimEvListService;

/**
 * @author xiangfeng.guan
 *
 */
@Controller
@RequestMapping("/pclaimevlist")
public class PClaimEvListController {

	protected final Logger logger = Logger.getLogger(PClaimEvListController.class);
	
	@Resource(name = "pClaimEvListService")
	private PClaimEvListService pClaimEvListService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PClaimEvList> list(String p_mi_id,
			String ent_id, Integer stat_id, Integer days, Integer start, Integer count) {

		try {
			return pClaimEvListService.queryPMiClaimEvList(p_mi_id, ent_id, stat_id, days, start, count);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}
	
	@RequestMapping(value = "/listNew", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PClaimModel> listNew(String p_mi_id, String userName, String startDate, String endDate, String clmId){
		try{
			return pClaimEvListService.queryPMiClaimEvListNew(p_mi_id, userName, startDate, endDate, clmId);
		}catch(Exception ex){
			logger.error(ex);
		}
		return null;
	}
	
	@RequestMapping(value = "/count", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String count(String p_mi_id, Integer stat_id) {

		try {
			return pClaimEvListService.queryPMiClaimEvListCount(p_mi_id, stat_id);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}
	
	@RequestMapping(value = "/updatestate", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateState(String data) {
		boolean flg = true;
		try {
			List<PClaimEvList> list = JsonStringUtils.jsonStringToList(data,
					PClaimEvList.class);
		    pClaimEvListService.updatePMiClaimEvListState(list);
		} catch (Exception ex) {
			logger.error(ex);
			flg = false;
					
		}
		return String.valueOf(flg);
	}
	
	@RequestMapping(value = "/updatesinglestate", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updatesingleState(String data) {

		boolean flg = true;
		try {
			PClaimEvList pClaimEvList = JsonStringUtils.jsonStringToObject(data,
					PClaimEvList.class);
		    pClaimEvListService.updatePMiClaimEvListState(pClaimEvList);
		} catch (Exception ex) {
			logger.error(ex);
			 flg = false;
		}
		return String.valueOf(flg);
	}
	
}
