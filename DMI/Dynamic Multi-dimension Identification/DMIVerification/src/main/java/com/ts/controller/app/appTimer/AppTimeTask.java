package com.ts.controller.app.appTimer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.controller.base.BaseController;
import com.ts.entity.app.AppToken;
import com.ts.entity.app.SysRecycleLog;
import com.ts.entity.app.SysTokenInfo;
import com.ts.service.system.apimanager.RecycleLog.AppRecycleLogManager;
import com.ts.service.system.appuser.AppTokenManager;
import com.ts.util.AppUserQueue;
import com.ts.util.PageData;
import com.ts.util.app.SessionAppMap;
import com.ts.util.UuidUtil;

@Service
public class AppTimeTask extends BaseController{
	
	@Resource(name = "appTokenService")
	private AppTokenManager appTokenService;
	
	@Resource(name = "appRecycleLogService")
	private AppRecycleLogManager appRecycleLogService;

	/**
	 *   
	 * ClassName: AppDelMapQuartz
	 * @Description: 删除Map缓存过期的token
	 * @author 李世博
	 * @date 2016年9月22日
	 */
	// 需要注意@Scheduled这个注解，它可配置多个属性：cron = "0 0/3 15 * * * ?"\fixedRate\fixedDelay = 5000 表示每隔5秒执行
//	@Scheduled(cron = "0 0/59 * * * ?")
	public void mapTask() {
		logBefore(logger, "进入删除过期MapToken！！！");
		SessionAppMap.reMovetimeOut();		    	
    }  
	
	/**
	  * @Description: 定时删除过期的刷新Token
	  * @author 李世博
	  * @date 2016年9月20日
	  */
//	@Scheduled(cron = "0 0 03 * * ?")
	public void tokenTask() {
		PageData pd = new PageData();
		Long dates = System.currentTimeMillis(); // 获取当前时间戳
		try {
			StringBuffer del = new StringBuffer();
			del.append("delete").append(" from sys_token_info where ").append("VALIDITY <"+ dates).toString();
			pd.put("sql", del);
			appTokenService.deleteValidity(pd);
			logBefore(logger, "删除过期Token！！！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
 		    	
    }  
	
	/**
	 * @Description: 定时保存操作日志
	 * @author 李世博
	 * @date 2016年9月28日
	 */
//	@Scheduled(cron = "0 0/1 * * * ?")
	public void recycleTask() {
		PageData pd = new PageData();
		logBefore(logger, "日志队列数>>>:"+AppUserQueue.getQueueSysRecycleLogSize() );
		try {
			while(AppUserQueue.getQueueSysRecycleLogSize() > 0 )
			{
				SysRecycleLog srl = AppUserQueue.getQueueSysRecycleLog();
				
				pd.put("log_id",srl.getLOG_ID());
				pd.put("iner_type", srl.getINER_TYPE());
				pd.put("input", srl.getINPUT());
				pd.put("output", srl.getOUTPUT());
				pd.put("user_ip", srl.getUSER_IP());
				pd.put("user_id", srl.getUSER_ID());
				pd.put("call_date", srl.getCALL_DATE());
				pd.put("code", srl.getCODE());
				pd.put("req_no", srl.getREQ_NO());
				logBefore(logger, "数据插入：>>"+ pd);
				appRecycleLogService.save(pd);
				
			}
			logBefore(logger, "日志队列为空等待中！！！");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
 		    	
    } 
	
	/**
	  * @Description: 定时保存刷新令牌
	  * @author 李世博
	  * @date 2016年9月28日
	  */
//	@Scheduled(cron = "0 0/1 * * * ?")
	public void timingScanTask(){
		PageData pd = new PageData();
		SysTokenInfo sti = new SysTokenInfo();
		Long dates = System.currentTimeMillis(); // 获取当前时间戳
		Calendar now = new GregorianCalendar();
		String Calendar_Key = ReadPropertiesFiles.getValue("token.Calendar_Key");
		if("HOUR_OF_DAY".equals(Calendar_Key)){
			now.add(Calendar.HOUR_OF_DAY,Integer.parseInt(ReadPropertiesFiles.getValue("token.rToken")) );
		}else if("DAY_OF_MONTH".equals(Calendar_Key)){
			now.add(Calendar.DAY_OF_MONTH,Integer.parseInt(ReadPropertiesFiles.getValue("token.rToken")) );	
		}else if("MINUTE".equals(Calendar_Key)){
			now.add(Calendar.MINUTE,Integer.parseInt(ReadPropertiesFiles.getValue("token.rToken")) );	
		}else if("SECOND".equals(Calendar_Key)){
			now.add(Calendar.SECOND,Integer.parseInt(ReadPropertiesFiles.getValue("token.rToken")) );		
		}else{
			logBefore(logger, "时间设置错误！");
		}
		String sdate = String.valueOf(dates);
		String times = String.valueOf(now.getTimeInMillis());
		logBefore(logger, "令牌队列数>>>:"+AppUserQueue.getQueueAppTokenSize() );
		try {
			while(AppUserQueue.getQueueAppTokenSize() > 0 )
			{
				AppToken appToken = AppUserQueue.getQueueAppToken();
				sti.setTI_ID(UuidUtil.get32UUID());
				sti.setUSER_ID(appToken.getAppUser().getUSER_ID());
				sti.setTOKEN_INFO(appToken.getRefresh_token());
				sti.setVALIDITY(times);
				sti.setCREATE_TIME(sdate);
				pd.put("ti_id", sti.getTI_ID());
				pd.put("user_id", sti.getUSER_ID());
				pd.put("token_info", sti.getTOKEN_INFO());
				pd.put("validity", sti.getVALIDITY());
				pd.put("CREATE_TIME", sti.getCREATE_TIME());
				appTokenService.save(pd);
			}
			logBefore(logger, "令牌队列为空等待中！！！");

		} catch (Exception  e) {
			// TODO Auto-generated catch block
//			logBefore(logger, "异常处理"+e.getMessage());
			logger.error(e.getMessage(), e);
		}
 		    	
    }

}
