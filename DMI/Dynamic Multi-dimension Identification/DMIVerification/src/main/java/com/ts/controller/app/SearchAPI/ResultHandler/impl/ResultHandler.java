package com.ts.controller.app.SearchAPI.ResultHandler.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.app.SearchAPI.ResultHandler.IResultHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.app.AppToken;
import com.ts.entity.app.SysRecycleLog;
import com.ts.util.AppUserQueue;
import com.ts.util.app.SessionAppMap;
import com.ts.util.UuidUtil;
import com.ts.util.Enum.EnumStatus;

/**
 * 
 * ClassName: ResultHandler
 * 
 * @Description: 返回结果
 * @author 李世博
 * @date 2016年9月28日
 */
@ResponseBody
public abstract class ResultHandler extends BaseController implements IResultHandler {

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
	public String getRSHandler(JSONObject value) {
		Object jsons = "";
		try {
			String token = "";
			String code = "";
			String ispage = "";
			String format = "";
			String apitype = "";
			int value_pageno = 0;
			int value_pagesize = 0;
			int value_totalpage = 0;
			int value_totals = 0;
			String input = value.toString();

			JSONArray showField = new JSONArray();
			StringBuffer message = new StringBuffer();
			if (value.has("token")) {
				token = value.getString("token");
			} else {
				message.append("应当包含token参数;");
			}

			if (value.has("code")) {
				code = value.getString("code");
			} else {
				message.append("应当包含code参数;");
			}

			apitype = value.getInt("apitype") + "";// API访问类型
			
			if (value.has("ispage")) {
				ispage = value.getString("ispage");
				if (!ispage.equals("1") && !ispage.equals("0")) {
					message.append("ispage参数只能为0或1;");
				}
			} else {
				message.append("应当包含ispage参数;");
			}

			format = value.getInt("format") + ""; // 输出格式
			if (value.has("format")) {
				format = value.getString("format");
				if (!format.equals("1") && !format.equals("2")) {
					message.append("format参数只能为1或2;");
				}
			} else {
				message.append("应当包含format参数;");
			}

			if (value.has("showfield")) {
				showField = value.getJSONArray("showfield");
				if (showField.isEmpty()) {
					message.append("showField参数不能为空;");
				}
			} else {
				message.append("应当包含showfield参数;");
			}

			if (value.has("pageno")) {
				if (isPositiveInteger(value.getString("pageno"))) {
					value_pageno = value.getInt("pageno");
				} else {
					message.append("pageno参数应为正整数或零;");
				}
			} else {
				message.append("应当包含pageno参数;");
			}

			if (value.has("pagesize")) {
				if (isPositiveInteger(value.getString("pagesize"))) {
					value_pagesize = value.getInt("pagesize");
				} else {
					message.append("pagesize参数应为正整数或零;");
				}
			} else {
				message.append("应当包含pagesize参数;");
			}

			if (value.has("totalpage")) {
				if (isPositiveInteger(value.getString("totalpage"))) {
					value_totalpage = value.getInt("totalpage");
				} else {
					message.append("totalpage参数应为正整数或零;");
				}
			} else {
				message.append("应当包含totalpage参数;");
			}

			if (value.has("totals")) {
				if (isPositiveInteger(value.getString("totals"))) {
					value_totals = value.getInt("totals");
				} else {
					message.append("totals参数应为正整数或零;");
				}
			} else {
				message.append("应当包含totals参数;");
			}

			if (value_pageno != 0 && value_pagesize != 0
					&& (int) Math.ceil((double) value_totals / (double) value_pagesize) < value_pageno) {
//				message.append("总条数除以每页显示记录数应大于等于总页数!");//当前页
				message.append("请检查总记录数、当前页数、总页数和每页记录数!");
			}

			if (message.length() > 0) {
				return "{\"status\":\"" + EnumStatus.Requst_error.getEnumValue() + "\",\"message\":\""
						+ message.toString() + "\"}";
			}

			AppToken appToken = SessionAppMap.getAppUser(token);
			String userID = appToken.getAppUser().getUSER_ID();
			String ip = appToken.getAppUser().getReuqestIp();
			Set<String> showfieldList = appToken.getAppUser().getShowfield().get(apitype);

			JSONArray showFieldNew = new JSONArray();// 经过可授权处理后的可展示字段
			JSONArray cannotShowFieldNew = new JSONArray();// 经过可授权处理后的不可展示字段
			for (Object field : showField.toArray(new String[0])) {
				boolean type = false;
				for (String fieldName : showfieldList) {
					if (field.toString().equalsIgnoreCase(fieldName)) {
						type = true;
						break;
					}
				}
				if (type) {
					showFieldNew.add(field.toString());
				} else {
					cannotShowFieldNew.add(field);
				}
			}
			if (showFieldNew.isEmpty() || value_pagesize == 0) {
				JSONObject result = new JSONObject();
				result.put("status", EnumStatus.success.getEnumValue().toString());
				result.put("pageno", value_pageno + "");
				result.put("pagesize", value_pagesize + "");
				result.put("totalpage", value_totalpage + "");
				result.put("totals", "0");
				result.put("datas", new JSONArray());
				result.put("showfield", showFieldNew);
				result.put("cannotshowfield", cannotShowFieldNew);
				return result.toString();
			} else {
				value.put("showfield", showFieldNew);
			}

			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sf.format(new Date());

			/** 业务查询 */
			Object pds = exceBusiness(value);

			JSONObject json = new JSONObject();

			String status = "";
			if (JSONObject.fromObject(pds).has("status")) {
				status = JSONObject.fromObject(pds).getString("status");
			}

			if (statusList.contains(status)) {
				json = JSONObject.fromObject(pds);
				// json.put("status", status);
			} else {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				Map<String, Object> map = (Map) pds;
				Page page = (Page) map.get("page");
				@SuppressWarnings("rawtypes")
				List list = (List) map.get("list");
				int pageno = 0, pagesize = 0, totalpage = 0, totals = 0;
				if ("1".equals(ispage)) {
					totalpage = page.getTotalPage();
					totals = page.getTotalResult();
					pageno = page.getCurrentPage();
					pagesize = page.getShowCount();
				} else {
					totals = list.size();
				}
				json.put("status", EnumStatus.success.getEnumValue().toString());
				json.put("pageno", pageno);
				json.put("pagesize", pagesize);
				json.put("totalpage", totalpage);
				json.put("totals", totals);
				json.put("datas", list);
				json.put("showfield", showFieldNew);
				json.put("cannotshowfield", cannotShowFieldNew);
			}
			if ("1".equals(format)) {
				XMLSerializer xmlserializer = new XMLSerializer();
				String xml = xmlserializer.write(json);
				xml = xml.replaceAll("<o>", "<xml>").replaceAll("</o>", "</xml>");
				logBefore(logger, "-----------返回xml=" + xml);
				return xml;
			} else if ("2".equals(format)) {
				jsons = json.toString();
				logBefore(logger, "-----------返回jsons=" + jsons.toString());
			} else {
				return "{\"status\":\"" + EnumStatus.Requst_error.getEnumValue().toString()
						+ "\",\"message\":\"检查传入format参数;1为返回xml数据;2为返回json数据!\"}";
			}

			SysRecycleLog srl = new SysRecycleLog();
			srl.setLOG_ID(UuidUtil.get32UUID());
			srl.setINER_TYPE(apitype);
			srl.setINPUT(input);
			srl.setUSER_IP(ip);
			srl.setUSER_ID(userID);
			srl.setCALL_DATE(time);
			srl.setCODE(code);
			srl.setOUTPUT(jsons.toString());
			AppUserQueue.setQueueSysRecycleLog(srl);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			jsons = "{\"status\":\"" + EnumStatus.Parameter_error.getEnumValue().toString() + "\"}";
		}
		return (String) jsons;
	}

	/**
	 * @描述：判断字符串是否为正整数
	 * @作者：SZ
	 * @时间：2016年11月15日 下午1:29:19
	 * @param str
	 * @return
	 */
	private static boolean isPositiveInteger(String str) {
		if (str != null && !"".equals(str.trim())) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str);
			Long number = 0l;
			if (isNum.matches()) {
				number = Long.parseLong(str);
			} else {
				return false;
			}
			if (number > 2147483647) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	protected abstract Object exceBusiness(JSONObject value);
}