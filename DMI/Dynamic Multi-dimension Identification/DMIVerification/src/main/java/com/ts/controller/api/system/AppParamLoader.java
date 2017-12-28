package com.ts.controller.api.system;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.app.SysParamCheck;
import com.ts.service.system.apimanager.PayRule.PayRuleManager;
import com.ts.util.PageData;
import com.ts.util.app.SessionAppMap;

/**
 * 加载机构和公钥。
 * @author autumn
 *
 */
@Controller
@RequestMapping(value = "/app")
public class AppParamLoader extends BaseController {
	
	
	@Resource(name="payRuleService")
	private PayRuleManager payRuleService;
	/**
	 * 服务启动时加载参数限制规则
	 */
	@PostConstruct
	public void paramLoader(){
		
		try {
			//查询所有规则列表  存到paramRuleMap
			Page page = new Page();
			page.setShowCount(100);
			List<PageData>	payRuleList = payRuleService.listPdPagePay(page);
			for (PageData pageData : payRuleList) {
				String id = pageData.getString("ID");
				SysParamCheck sys = new SysParamCheck(id, pageData.getString("CHECK_TYPE"), pageData.getString("CHECK_VALUE"),  pageData.getString("CHECK_NAME"));
				SessionAppMap.paramRuleMap.put(id, sys);
			}
			//查询所有参数规则关系 存到 dataParamRuleMapMap
			List<PageData>	dataRuleList = payRuleService.getDataRuleAll();
			for (PageData pageData : dataRuleList) {
				String columnName = pageData.getString("COLUMN_NAME");
				String checkId = pageData.getString("CHECK_ID");
				Set<String> set = SessionAppMap.dataParamRuleMapMap.get(columnName);
				if(set == null){
					set = new HashSet<String>();
				}
				set.add(checkId);
				SessionAppMap.dataParamRuleMapMap.put(columnName, set);
			}
			logger.info("缓存参数规则完成");
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
			
	}
			
}