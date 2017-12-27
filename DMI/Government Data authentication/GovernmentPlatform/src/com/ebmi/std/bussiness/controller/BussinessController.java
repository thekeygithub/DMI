/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.bussiness.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.utils.JsonStringUtils;
import com.ebmi.std.bussiness.dto.Complaint;
import com.ebmi.std.bussiness.dto.SiCardInfo;
import com.ebmi.std.bussiness.dto.SiCardStateInfo;
import com.ebmi.std.bussiness.dto.SiCardStateQueryInfo;
import com.ebmi.std.bussiness.service.BussinessService;
import com.ebmi.std.pclaimevlist.controller.PClaimEvListController;

@Controller
@RequestMapping("/bussiness")
public class BussinessController {

	protected final Logger logger = Logger.getLogger(PClaimEvListController.class);
	@Resource(name = "bussinessService")
	private BussinessService bussinessService;
	
	@RequestMapping(value = "/getSiCardInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody SiCardInfo getSiCardInfo(String siCardNo, String pMiId) {
		try {
			return bussinessService.getSiCardInfo(siCardNo,pMiId);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}
	
	@RequestMapping(value = "/getSiCardState", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody SiCardStateInfo getSiCardState(String siCardNo, String pMiId) {
		try {
			return bussinessService.getSiCardStateInfo(siCardNo, pMiId);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}

	@RequestMapping(value = "/updateSiCardInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateInfo(String data) {
		try {
			if(data == null){
				return "false";
			}
			SiCardInfo sicardInfo = JsonStringUtils.jsonStringToObject(data,
					SiCardInfo.class);
			bussinessService.updateInfo(sicardInfo);
			return "true";
		} catch (Exception ex) {
		logger.error(ex);
		return "false";
		}
	}
	
	@RequestMapping(value = "/lostSiCard", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String lostSiCard(String siCardNo, String pMiId) {
		try {

			return String.valueOf(bussinessService.lostSiCard(siCardNo, pMiId));
		} catch (Exception ex) {
			logger.error(ex);
		}
		return "false";
	}

	@RequestMapping(value = "/saveComplaint", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Complaint saveComplaint(String data) {
		try {
			if(data == null){
				return null;
			}
			Complaint complaint = JsonStringUtils.jsonStringToObject(data,
					Complaint.class);
			return bussinessService.saveComplaint(complaint);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}
	
	@RequestMapping(value = "/getcomplaintdetail", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Complaint getComplaintDetail(String recordId) {
		try {
			return bussinessService.getComplaint(recordId);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}
	
	@RequestMapping(value = "/queryMakeCardState", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody SiCardStateQueryInfo querystate(String siCardNo) {
		try {
		
			return bussinessService.queryMakeCardState(siCardNo);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}
	
}
