package com.ts.controller.app.appuser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.controller.base.BaseController;
import com.ts.entity.app.AppToken;
import com.ts.entity.app.SysAppUser;
import com.ts.service.system.apimanager.Relation.RelationManager;
import com.ts.service.system.appuser.AppTokenManager;
import com.ts.service.system.appuser.AppuserManager;
import com.ts.util.AppUserQueue;
import com.ts.util.MD5;
import com.ts.util.PageData;
import com.ts.util.app.SessionAppMap;
import com.ts.util.Enum.EnumStatus;

/**
 * app 校验管理
 * 
 * @author autumn
 *
 */
@Controller
@RequestMapping(value = "/app")
public class AppVerifyHandler extends BaseController {
	@Resource(name = "appuserService")
	private AppuserManager appuserService;
	@Resource(name = "appTokenService")
	private AppTokenManager appTokenService;
	@Resource(name = "relationService")
	private RelationManager relationService;
	private long t1,t2;

	/**
	 * 校验用户是否有效
	 * 
	 * @param json
	 *            {user:"",pwd:""}
	 *
	 * 
	 * @return json格式 ， "status" : "-2" "access_token":"2YotnFZFEjr1zCsicMWpAA",
	 *         "token_type":"password", "expires_in":3600,
	 *         "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
	 *         "example_parameter":"example_value"
	 * 
	 */
	@RequestMapping(value = "/oauth/verify", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	@Validated
	public String verifyUserByToken(@RequestBody String json) {
		logBefore(logger, "根据用户名密码验证是否有效，返回Token");
		logBefore(logger, "tokensize before:" + SessionAppMap.getTokenSize());
		logBefore(logger, "传入Json" + json);
		t1 = System.currentTimeMillis();
		JSONObject jsons = new JSONObject();
		try {
			JSONObject jo = getJsonObject(json);
			if (jo.has("user") && jo.has("pwd") && jo.size() == 2) {
				PageData pd = new PageData();
				pd = this.getPageData();
				String user = jo.get("user").toString();
				String pwd = jo.get("pwd").toString();
				pd.put("username", user);
				pd = appuserService.findByUsername(pd);
				pwd = MD5.md5(pwd);
				
				if (pd != null) {
					if (!"".equals(pd.get("PASSWORD")) && pwd.equals(pd.get("PASSWORD"))) {
						jsons = getJsonReturn(pd, user, pwd);
					} else {
						jsons.put("status", EnumStatus.illegal_identity.getEnumValue());
						logBefore(logger, jsons.toString());
					}
				} else {
					jsons.put("status", EnumStatus.illegal_identity.getEnumValue());
					logBefore(logger, jsons.toString());
				}
				logBefore(logger, "返回 <<<Token>>>" + jsons);
			} else {
				jsons.put("status", EnumStatus.Parameter_error.getEnumValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsons.put("status", EnumStatus.Parameter_error.getEnumValue());
		}
		t2 = System.currentTimeMillis();
		logBefore(logger,"方法耗时："+(t2-t1)+"ms");
		logBefore(logger, "tokensize after:" + SessionAppMap.getTokenSize());
		return jsons.toString();
	}

	/**
	 * 由于token过期 所刷新出 Refresh_token 值进行 token 的刷新
	 * 
	 * @param {refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA"}
	 * 
	 * @return "access_token":"2YotnFZFEjr1zCsicMWpAA", "token_type":"example",
	 *         "expires_in":3600, "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
	 *         "example_parameter":"example_value"
	 */
	@RequestMapping(value = "/oauth/refToken", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String refreshToken(@RequestBody String json) {
		logBefore(logger, "刷新token");
		logBefore(logger, "tokensize before:" + SessionAppMap.getTokenSize());
		logBefore(logger, "--传入Json--:" + json);
		t1 = System.currentTimeMillis();
		JSONObject jsons = new JSONObject();
		try {
			JSONObject jo = getJsonObject(json);
			if (jo.has("refresh_token") && jo.size() == 1) {
				String reToken = jo.get("refresh_token").toString();
				Long date = System.currentTimeMillis();
				PageData pd = new PageData();
				pd = this.getPageData();
				pd.put("token_info", reToken);
				pd = appTokenService.findByToken(pd);
				Object userId = pd.get("USER_ID");
				long VALIDITY = Long.valueOf(String.valueOf(pd.get("VALIDITY"))).longValue();
				// 刷新token 去到 tokenmanager 查询 如果有效
				if (reToken.equals(pd.get("TOKEN_INFO"))) {
					// //判断时间是否有效
					if (VALIDITY > date) {
						SessionAppMap.reMove(reToken);
						appTokenService.deleteTokenInfo(pd);
						pd = new PageData();
						pd.put("USER_ID", userId);
						pd = appuserService.findByUiId(pd);
						
						jsons = getJsonReturn(pd, pd.get("username").toString(), pd.get("password").toString());
					} else {
						jsons.put("status", EnumStatus.illegal_identity.getEnumValue());
						logBefore(logger, "非法身份" + jsons.toString());
					}
				} else {
					jsons.put("status", EnumStatus.illegal_identity.getEnumValue());
					logBefore(logger, "非法身份" + jsons.toString());
				}
			} else {
				jsons.put("status", EnumStatus.Parameter_error.getEnumValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsons.put("status", EnumStatus.Parameter_error.getEnumValue());
		}
		t2 = System.currentTimeMillis();
		logBefore(logger,"方法耗时："+(t2-t1)+"ms");
		logBefore(logger, "tokensize after:" + SessionAppMap.getTokenSize());
		return jsons.toString();
	}
	
	/**
	 * { access_token refresh_token } 注销当前授权 token 用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/oauth/logoutToken", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String logoutToken(@RequestBody String json) {
		logBefore(logger, "注销token");
		logBefore(logger, "tokensize before:" + SessionAppMap.getTokenSize());
		logBefore(logger, "--传入Json--:" + json);
		t1 = System.currentTimeMillis();
		JSONObject jsons = new JSONObject();
		try {
			JSONObject jo = getJsonObject(json);
			if (jo.has("access_token") && jo.has("refresh_token") && jo.size() == 2) {
				String aToken = jo.get("access_token").toString();
				String rToken = jo.get("refresh_token").toString();
				if (aToken.equals("") || aToken == null) {
					jsons.put("status", EnumStatus.Requst_error.getEnumValue());
					jsons.put("message", "access_token参数不能为空;");
					return jsons.toString();
				}
				if (rToken.equals("") || rToken == null) {
					jsons.put("status", EnumStatus.Requst_error.getEnumValue());
					jsons.put("message", "refresh_token参数不能为空;");
					return jsons.toString();
				}
				if (SessionAppMap.getAppUser(aToken) == null) {
					jsons.put("status", EnumStatus.Parameter_error.getEnumValue());
					return jsons.toString();
				}
				SessionAppMap.reMove(aToken);
				PageData pd = new PageData();
				pd = this.getPageData();
				pd.put("TOKEN_INFO", rToken);
				appTokenService.deleteTokenInfo(pd);

				logBefore(logger, "已删除全局变量");
				jsons.put("status", EnumStatus.success.getEnumValue());
			} else {
				jsons.put("status", EnumStatus.Parameter_error.getEnumValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsons.put("status", EnumStatus.Parameter_error.getEnumValue());
		}
		t2 = System.currentTimeMillis();
		logBefore(logger,"方法耗时："+(t2-t1)+"ms");
		logBefore(logger, "tokensize after:" + SessionAppMap.getTokenSize());
		return jsons.toString();
	}

	/**
	 * @描述：获取业务类型、可显示字段、数据权限的存储数据格式(只显示每个业务的，有数据权限的字段集合) @作者：SZ
	 * @时间：2016年12月2日 上午9:20:52
	 * @param dataList
	 *            用户的可访问业务，可访问字段，可访问数据权限集合
	 * @return
	 */
	private static Map<String, Object> transitionData(List<PageData> dataList) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Map<String, Set<String>>> jurisdictionMap = new HashMap<String, Map<String, Set<String>>>();// 权限
		Map<String, Set<String>> showFieldMap = new HashMap<String, Set<String>>();// 可显示字段

		Set<String> showFieldList = null;
		Set<String> jurisdictionList = null;
		Map<String, Set<String>> jurisdictionMapValue = null;
		for (int i = 0; i < dataList.size(); i++) {
			PageData pageData = dataList.get(i);
			String type = pageData.getString("TYPE");
			String col_rule = pageData.getString("COL_RULE");
			String col_name = pageData.getString("COLUMN_NAME");
			showFieldList = showFieldMap.get(type);
			if (null == showFieldList) {
				showFieldList = new HashSet<String>();
			}
			showFieldList.add(col_name);
			showFieldMap.put(type, showFieldList);
			if (null != col_rule && !"".equals(col_rule)) {
				jurisdictionList = new HashSet<String>();
				for(String rule : col_rule.split(";")){
					jurisdictionList.add(rule);
				}
				jurisdictionMapValue = jurisdictionMap.get(type);
				if (null == jurisdictionMapValue) {
					jurisdictionMapValue = new HashMap<String, Set<String>>();
				}
				if(null != jurisdictionMapValue.get(col_name)){
					for(String rule : jurisdictionMapValue.get(col_name)){
						jurisdictionList.add(rule);
					}
				}
				jurisdictionMapValue.put(col_name, jurisdictionList);
				jurisdictionMap.put(type, jurisdictionMapValue);
			}
		}

		map.put("apitype", showFieldMap.keySet());
		map.put("jurisdiction", jurisdictionMap);
		map.put("showfield", showFieldMap);
		return map;
	}
	
	/**
	 * @描述：用户验证、和刷新用户的返回值拼装
	 * @作者：SZ
	 * @时间：2016年12月5日 下午2:16:10
	 * @param pd
	 * @param jo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getJsonReturn(PageData pd, String user,String pwd) throws Exception {
		JSONObject jsons = new JSONObject();
		long dates = System.currentTimeMillis(); // 获取当前时间戳
		Calendar now = new GregorianCalendar();
		String CalendarKey = ReadPropertiesFiles.getValue("token.CalendarKey");
		if ("HOUR_OF_DAY".equals(CalendarKey)) {
			now.add(Calendar.HOUR_OF_DAY, Integer.parseInt(ReadPropertiesFiles.getValue("token.aToken")));
		} else if ("DAY_OF_MONTH".equals(CalendarKey)) {
			now.add(Calendar.DAY_OF_MONTH, Integer.parseInt(ReadPropertiesFiles.getValue("token.aToken")));
		} else if ("MINUTE".equals(CalendarKey)) {
			now.add(Calendar.MINUTE, Integer.parseInt(ReadPropertiesFiles.getValue("token.aToken")));
		} else if ("SECOND".equals(CalendarKey)) {
			now.add(Calendar.SECOND, Integer.parseInt(ReadPropertiesFiles.getValue("token.aToken")));
		} else {
			logBefore(logger, "时间设置错误！");
			return null;
		}
		long time = Long.valueOf(now.getTimeInMillis());
		long times = time - dates;

		String[] str = appMD5(user, pwd);
		String access_token = MD5.md5(str[0]);
		String token_type = "password";
		String expires_in = Long.toString(times);
		String refresh_token = MD5.md5(str[1]);
		String example_parameter = "example_value";

		jsons.put("status", EnumStatus.success.getEnumValue());
		jsons.put("access_token", access_token);
		jsons.put("token_type", token_type);
		jsons.put("expires_in", expires_in);
		jsons.put("refresh_token", refresh_token);
		jsons.put("example_parameter", example_parameter);

		// 根据用户id获取可访问字段和数据权限 SYS_ROLE_TABLE_RELATION
		List<PageData> dataList = relationService.findDataByUserId(pd);
		Map<String, Object> map = transitionData(dataList);
		Set<String> aiptyep = (Set<String>) map.get("apitype");
		Map<String, Set<String>> showFieldMap = (Map<String, Set<String>>) map.get("showfield");
		Map<String, Map<String, Set<String>>> jurisdictionMap = (Map<String, Map<String, Set<String>>>) map
				.get("jurisdiction");// 权限

		SysAppUser appUser = new SysAppUser();
		String ip = getRequest().getRemoteAddr(); // 获取客户端ip
		appUser.setAREA_ID(pd.getString("area_id"));
		appUser.setNAME(pd.getString("name"));
		appUser.setUSER_ID(pd.getString("user_id"));
		appUser.setReuqestIp(ip);
		appUser.setPASSWORD(pd.getString("password"));
		appUser.setUSERNAME(pd.getString("username"));
		appUser.setApitype(aiptyep);
		appUser.setShowfield(showFieldMap);
		appUser.setJurisdiction(jurisdictionMap);

		AppToken appToken = new AppToken();
		appToken.setStatus(EnumStatus.success.getEnumValue());
		appToken.setAccess_token(access_token);
		appToken.setToken_type(token_type);
		appToken.setExpires_in(time);
		appToken.setRefresh_token(refresh_token);
		appToken.setExample_parameter(example_parameter);
		appToken.setAppUser(appUser);
		SessionAppMap.setAppUser(appToken);
		AppUserQueue.setQueueAppToken(appToken);
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pd.put("IP", ip);
		pd.put("LAST_LOGIN", sf.format(new Date()));
		appuserService.editIpAndTime(pd);
		logBefore(logger, "队列总数>>:" + AppUserQueue.getQueueAppTokenSize());

		return jsons;
	}
	
	private String[] appMD5(String user, String pwd) {
		String[] str = new String[2];
		Long date = System.currentTimeMillis(); // 获取当前时间戳
		StringBuffer aToken = new StringBuffer();
		aToken.append(user).append(pwd).append(date);
		StringBuffer rToken = new StringBuffer();
		rToken.append(user).append(date);
		str[0] = aToken.toString();
		str[1] = rToken.toString();
		return str;
	}
}
