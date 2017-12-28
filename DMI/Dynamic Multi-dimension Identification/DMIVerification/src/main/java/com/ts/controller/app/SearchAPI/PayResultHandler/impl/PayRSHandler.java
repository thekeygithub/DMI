package com.ts.controller.app.SearchAPI.PayResultHandler.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.app.SearchAPI.PayResultHandler.IPayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_inte_info;
import com.ts.entity.app.SysRecycleLog;
import com.ts.util.AppUserQueue;
import com.ts.util.UuidUtil;
import com.ts.util.Enum.EnumStatus;

/**
 * 
 * ClassName: ResultHandler
 * 
 * @Description: 返回结果
 * @author 李世博
 * @date 2017年3月11日
 */
@ResponseBody
public abstract class PayRSHandler extends BaseController implements IPayRSHandler {

	private static List<String> statusList = new ArrayList<String>();
	
	static {
		// 当返回值包含以下状态码时，认定返回错误
		statusList.add(EnumStatus.page_missing.getEnumValue());
		statusList.add(EnumStatus.internal_server_error.getEnumValue());
		statusList.add(EnumStatus.illegal_identity.getEnumValue());
		statusList.add(EnumStatus.illegal_use.getEnumValue());
		statusList.add(EnumStatus.not_API_data_type.getEnumValue());
		statusList.add(EnumStatus.Unauthorized_access.getEnumValue());
		statusList.add(EnumStatus.Parameter_error.getEnumValue());
		statusList.add(EnumStatus.request_timeout.getEnumValue());
		statusList.add(EnumStatus.Requst_error.getEnumValue());
	}

	@Override
	public String getPayRSHandler(JSONObject value) {
		Object jsons = "";
		try {
			/** 业务查询 */
			Object pds = exceBusiness(value);
			JSONObject json = new JSONObject();
			json = JSONObject.fromObject(pds);
			jsons = json.toString();
			logBefore(logger, "-----------返回jsons=" + jsons.toString());
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			jsons = "{\"status\":\"" + EnumStatus.Parameter_error.getEnumValue().toString() + "\"}";
		}
		return (String) jsons;
	}
	
	protected abstract Object exceBusiness(JSONObject value);
}