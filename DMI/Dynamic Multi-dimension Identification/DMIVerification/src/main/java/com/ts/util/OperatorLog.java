package com.ts.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.ts.entity.app.SysRecycleLog;

import net.sf.json.JSONObject;

public class OperatorLog {
	
	static Logger logger = Logger.getLogger(OperatorLog.class);
	
	/**接口日志
	 * @param apitype
	 * @param userID
	 * @param ip
	 * @param resNo
	 * @param value
	 * @param jsons
	 */
	public static void setInterLog(String apitype, String userID, String ip, String resNo, JSONObject value, String jsons){
		logger.info(String.format("param:%s\t;result:%s\t", value.toString(), jsons));
		String code = "PAY";
		String input = value.toString();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sf.format(new Date());
		
		SysRecycleLog srl = new SysRecycleLog();
		srl.setLOG_ID(UuidUtil.get32UUID());
		srl.setINER_TYPE(apitype);
		srl.setINPUT(input);
		srl.setUSER_IP(ip);
		srl.setUSER_ID(userID);
		srl.setCALL_DATE(time);
		srl.setCODE(code);
		srl.setREQ_NO(resNo);
		srl.setOUTPUT(jsons);
		AppUserQueue.setQueueSysRecycleLog(srl);
	}
}