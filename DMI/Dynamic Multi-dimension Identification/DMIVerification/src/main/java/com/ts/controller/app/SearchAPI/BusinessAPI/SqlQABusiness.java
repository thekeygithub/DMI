package com.ts.controller.app.SearchAPI.BusinessAPI;


import java.sql.Blob;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.codec.Base64;
import org.apache.solr.common.util.JavaBinCodec.StringCache;
import org.springframework.stereotype.Controller;

import com.ts.controller.app.SearchAPI.BusinessHandler;
import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.controller.app.SearchAPI.ResultHandler.impl.ResultHandler;
import com.ts.entity.Page;
import com.ts.entity.app.AppToken;
import com.ts.service.app.AppManager;
import com.ts.util.PageData;
import com.ts.util.app.SessionAppMap;
import com.ts.util.Enum.EnumStatus;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import oracle.sql.DATE;


/**
 * @描述：sql查询数据集
 * @作者：SZ
 * @时间：2016年11月30日 下午3:45:04
 */
@Controller
public class SqlQABusiness extends ResultHandler {
	@Resource(name = "appService")
	private AppManager data;

	@Override
	protected Object exceBusiness(JSONObject value) {
		String ispage = value.get("ispage").toString(); // 是否分页
		JSONObject wheres = value.getJSONObject("wheres"); // 查询条件
		JSONArray sArray = value.getJSONArray("showfield"); // 查询的目标字段
		String apitype = value.getString("apitype");

		String[] result = BusinessHandler.typeMap.get(apitype).split(",");

		StringBuffer sbfield = new StringBuffer();
		// 显示字段
		for (Object field : sArray.toArray(new String[0])) {
			sbfield.append(field).append(",");
		}
		sbfield.deleteCharAt(sbfield.length() - 1);
		boolean flag = false;
		StringBuffer sbWhere = new StringBuffer();
		StringBuffer message = new StringBuffer();
		String wKey = String.valueOf( ReadPropertiesFiles.getValue("wheres.isContainsKey"));
		String[] whe = wKey.split(",");
		if (value.has("wheres")) {
			for(String keys : whe)
			if(wheres.containsKey(keys)){
				message.append(keys+"字段不允许作为where条件查询;");
			}
			
		} else {
			message.append("应当包含wheres参数;");
		}
		if (message.length() > 0) {
			return "{\"status\":\"" + EnumStatus.Requst_error.getEnumValue() + "\",\"message\":\""
					+ message.toString() + "\"}";
		}
		for (Object key : wheres.keySet()) {
			
			JSONObject where = null;
			try {
				where = wheres.getJSONObject(key.toString());	
			} catch (JSONException e) {
				logger.error(e.getMessage(), e);
				message.append("参数错误：不支持相同字段作为KEY值;");
				return "{\"status\":\"" + EnumStatus.Requst_error.getEnumValue() + "\",\"message\":\""
						+ message.toString() + "\"}";
				// TODO: handle exception
			}

			String vle = where.getString("value");
			
			if (result.length > 1) {
				if (result[1].equals(key.toString().toLowerCase())) {
					if (!"".equals(vle)) {
						flag = true;
					}
				}
			} else {
				flag = true;
			}
			String operator = where.getString("operator");
			String con = where.getString("condition");
			if ("in".equals(operator)) {
				sbWhere.append(con).append(" ").append(key).append(" ").append(operator).append(" ").append(vle);
			} else {
				sbWhere.append(con).append(" ").append(key).append(" ").append(operator).append(" ").append("'")
						.append(vle).append("' ");
			}
			String token = value.getString("token");
			AppToken appToken = SessionAppMap.getAppUser(token);
			Map<String, Set<String>> jurisdiction = appToken.getAppUser().getJurisdiction().get(apitype);

			if (null != jurisdiction) {
				StringBuffer sbRules = null;
				for (Map.Entry<String, Set<String>> map : jurisdiction.entrySet()) {
					sbRules = new StringBuffer();
					for (String rule : map.getValue()) {
						// (map.getKey().toLowerCase()+":\""+rule+"\"");
						if (isNotNull(rule)) {
							sbRules.append("'").append(rule).append("',");
						}
					}
					String rules = sbRules.toString();
					if (rules.endsWith(",")) {
						rules = rules.substring(0, rules.length() - 1);
						sbWhere.append(" ").append("and").append(" ").append(map.getKey()).append(" in (").append(rules)
								.append(") ");
					}
				}
			}
		}
		Object object = new Object();
		PageData pageData = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		if (flag) {
			sql.append("select ").append(sbfield).append(" from ").append(result[0]).append(" where 1=1 ")
					.append(sbWhere);

			if (apitype.equals("2")) {
				sql.append(" and is_disable = 0");
			}
			sql.toString();
			try {
				String shows = String.valueOf(ReadPropertiesFiles.getValue("show.isContainKey"));
				String[] isShow = shows.split(",");
				if ("1".equals(ispage)) {
					String pagesize = value.get("pagesize").toString();
					String pageno = value.get("pageno").toString();
					if (!"".equals(pagesize) && !"".equals(pageno)) {
						Page page = new Page();
						page.setShowCount(value.getInt("pagesize")); // 每页显示记录数
						page.setCurrentPage(value.getInt("pageno")); // 当前页
						page.setCurrentResult(0); // 当前记录起始索引
						page.setPd(pageData);
						page.setEntityOrField(false);
						pageData.put("sql", sql);
						List<PageData> pagedatas = this.data.jsonPage(page);
						for(String keys : isShow){							
							for(PageData lis : pagedatas){
								if(lis.containsKey("ROW_ID")){
									lis.remove("ROW_ID");
								}
								if(lis.containsKey(keys)){
									Blob blob = (Blob) lis.get(keys);
									long size = blob.length();
									byte[] bs = blob.getBytes(1, (int) size);
									String str ="data:image/jpg;base64,"+Base64.encodeToString(bs);
									lis.put(keys, str);
								}
							}
						}
						map.put("page", page);
						map.put("list", pagedatas);
						object = map;
					} else {
						object = "{\"status\":\"" + EnumStatus.Parameter_error.getEnumValue().toString() + "\"}";
						return object;
					}

				} else if ("0".equals(ispage)) {
					pageData.put("sql", sql);
					List<PageData> page =  this.data.json(pageData);
					for(String keys : isShow){							
						for(PageData lis : page){
							if(lis.containsKey(keys)){
								Blob blob = (Blob) lis.get(keys);
								long size = blob.length();
								byte[] bs = blob.getBytes(1, (int) size);
								String str = "data:image/jpg;base64,"+Base64.encodeToString(bs);
								lis.put(keys, str);
							}
						}
					}
					map.put("list", page);
					object = map;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			object = "{\"status\":\"" + EnumStatus.Requst_error.getEnumValue().toString() + "\"}";
		}
		return object;
	}  	
}
