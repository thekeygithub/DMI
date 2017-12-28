package com.ts.controller.app.appTimer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.controller.base.BaseController;
import com.ts.service.pay.SettlementDrugstoreManager;
import com.ts.service.pay.SettlementManager;
import com.ts.util.PageData;

@Service
public class SettlementTimeTask extends BaseController {

	@Resource(name = "settlementService")
	private SettlementManager settlementService;	
	
	@Resource(name = "settlementDrugstoreService")
	private SettlementDrugstoreManager settlementDrugstoreService;
	
	public void settle() {
		PageData pd = new PageData();
		logBefore(logger, "结算日结定时任务");
		try {
			Date today = new Date();
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(today);
		    cal.add(Calendar.DAY_OF_MONTH, -1);
	        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
	        Date yestoday = datef.parse(datef.format(cal.getTime()));
	        settlementService.settlement(yestoday, today, "", "");
	        
	        //药店的结算对账信息
	        settlementDrugstoreService.settlement(yestoday, today, "", "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logAfter(logger);
    }

}