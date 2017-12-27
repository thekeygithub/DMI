package com.ebmi.std.share.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ebmi.jms.entity.ViewYyjbxx;
import com.ebmi.std.share.service.ShareService;

/**
 * 共享信息
 * 
 * @author xiangfeng.guan
 */
@Controller
@RequestMapping("/share")
public class ShareController {
	protected final Logger logger = Logger.getLogger(ShareController.class);

	@Resource
	private ShareService shareService;

	@RequestMapping(value = "/yylist", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<ViewYyjbxx> queryHost() {
		try {
			return shareService.queryYyjbxx();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	@RequestMapping(value = "/yyinfo", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ViewYyjbxx getHosp(String yybh) {
		try {
			return shareService.getYyjbxx(yybh);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
}
