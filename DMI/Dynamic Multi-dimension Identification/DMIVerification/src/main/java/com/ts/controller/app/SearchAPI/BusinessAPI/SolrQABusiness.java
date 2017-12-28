package com.ts.controller.app.SearchAPI.BusinessAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Controller;

import com.ts.controller.app.SearchAPI.BusinessHandler;
import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.controller.app.SearchAPI.ResultHandler.impl.ResultHandler;
import com.ts.entity.Page;
import com.ts.entity.app.AppToken;
import com.ts.util.Logger;
import com.ts.util.app.SessionAppMap;
import com.ts.util.StringUtil;
import com.ts.util.Enum.EnumStatus;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @描述：solr查询数据集
 * @作者：SZ
 * @时间：2016年10月27日 下午4:23:43
 */
@Controller
public class SolrQABusiness extends ResultHandler {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static SolrClient client;
	private static ConcurrentHashMap<String, SolrClient> clientMap = new ConcurrentHashMap<String, SolrClient>();

	static {
		Logger logger = Logger.getLogger(SolrQABusiness.class);
		boolean on_off = Boolean.parseBoolean(ReadPropertiesFiles.getValue("solr.on_off"));
		if (on_off) {
			for (String key : BusinessHandler.typeMap.keySet()) {
				if (Integer.parseInt(key) > 5) {
					String url = ReadPropertiesFiles.getSolrUrl(BusinessHandler.typeMap.get(key));
					client = new HttpSolrClient(url);
					clientMap.put(key, client);
					try {
						client.ping();
						logger.info("#############################初始化" + url + "成功#############################");
					} catch (Exception e) {
						logger.error("@@@@@@--初始化" + url + "失败--@@@@@@key=" + key);
						logger.error(e.getMessage());
						logger.error("@@@@@@--初始化" + url + "失败--@@@@@@key=" + key);
						e.printStackTrace();
					}
				}
			}
		} else {
			logger.info("-------------------未开启加载solr开关-------------------");
		}
	}

	@Override
	protected Object exceBusiness(JSONObject value) {
		String apitype = value.getString("apitype");
		try {
			client = clientMap.get(apitype);
			client.ping();
		} catch (Exception e) {
			logger.error("@@@@@@--ping  " + BusinessHandler.typeMap.get(apitype) + "  失败--@@@@@@");
			client = new HttpSolrClient(ReadPropertiesFiles.getSolrUrl(BusinessHandler.typeMap.get(apitype)));
			clientMap.put(apitype, client);
			e.printStackTrace();
		}
		return getresult(value, client, apitype);
	}

	/**
	 * @描述：solr统一查询请求方法(注：wheres中必须包含requestHandler和query参数) @作者：SZ
	 * @时间：2016年11月14日 上午10:10:18
	 * @param value
	 *            接收的json串
	 * @param client
	 *            请求的客户端
	 * @param logger
	 *            日志
	 * @param solrProjectName
	 *            solr项目名称 当客户端ping失败时，重新连接时需要此参数
	 * @return
	 */
	private Object getresult(JSONObject value, SolrClient client, String apitype) {
		Map<String, Object> result = null;
		try {
			JSONObject wheres = value.getJSONObject("wheres"); // 查询条件
			if (!wheres.has("requestHandler") || StringUtil.isNullOrEmpty(wheres.get("requestHandler").toString())
					|| !"/select".equals(wheres.get("requestHandler").toString())) {
				return "{\"status\":\"" + EnumStatus.Requst_error.getEnumValue()
						+ "\",\"message\":\"请检查wheres中的requestHandler参数，该参数不能为空!并且该值必须为/select\"}";
			} else if (!wheres.has("query") || StringUtil.isNullOrEmpty(wheres.get("query").toString())
					|| wheres.getJSONArray("query").isEmpty()) {
				return "{\"status\":\"" + EnumStatus.Requst_error.getEnumValue()
						+ "\",\"message\":\"请检查wheres中的query参数，该参数不能为空且必须为JSONArray类型参数!\"}";
			}

			result = new HashMap<String, Object>();
			int pagesize = value.getInt("pagesize"); // 每页面显示数量
			// if (pagesize == 0) {// 为了统一风格，当入参值为0时，设置其为1
			// pagesize = 1;
			// }
			int pageno = value.getInt("pageno"); // 当前第几页
			// int totals = value.getInt("totals"); // 总共多少数量
			// if ((int) Math.ceil((double) totals / (double) pagesize) <
			// pageno) {
			// return "{\"status\":\"" +
			// EnumStatus.Parameter_error.getEnumValue() +
			// "\"}";
			// }
			String ispage = value.get("ispage").toString(); // 是否分页

			List<String> q = new ArrayList<String>();
			List<String> fq = new ArrayList<String>();
			List<String> fl = new ArrayList<String>();

			String requestHandler = wheres.get("requestHandler").toString();

			JSONArray query = wheres.getJSONArray("query");// 查询的问题

			Map<String, String> fieldMap = new HashMap<String, String>();
			for (Object field : query.toArray(new String[0])) {
				String[] tmp = field.toString().split(":",2);
				if(fieldMap.containsKey(tmp[0].toLowerCase())){
					fieldMap.put(tmp[0].toLowerCase(), fieldMap.get(tmp[0])+tmp[1]);
				}else{
					fieldMap.put(tmp[0].toLowerCase(), tmp[1]);
				}
			}

			for (String fieldkey : fieldMap.keySet()) {
				q.add(fieldkey+":"+fieldMap.get(fieldkey));
			}
			if (wheres.has("filterQuery")) {
				JSONArray filterQuery = wheres.getJSONArray("filterQuery"); // 必须包含某个词组
				
				fieldMap = new HashMap<String, String>();
				for (Object field : filterQuery.toArray(new String[0])) {
					String[] tmp = field.toString().split(":",2);
					if(fieldMap.containsKey(tmp[0].toLowerCase())){
						fieldMap.put(tmp[0].toLowerCase(), fieldMap.get(tmp[0])+tmp[1]);
					}else{
						fieldMap.put(tmp[0].toLowerCase(), tmp[1]);
					}
				}

				for (String fieldkey : fieldMap.keySet()) {
					fq.add(fieldkey+":"+fieldMap.get(fieldkey));
				}
			}

			JSONArray showField = value.getJSONArray("showfield");// 返回的字段名称
			if (showField.isEmpty()) {// 查询字段为空时返回问题字段
				fl.add("");//逻辑上不可能为空
			} else {
				//可展示字段数据权限过滤
				String token = value.getString("token");
				AppToken appToken = SessionAppMap.getAppUser(token);
				Map<String, Set<String>> jurisdiction = appToken.getAppUser().getJurisdiction().get(apitype);
				if (null != jurisdiction) {
					for (Map.Entry<String, Set<String>> map : jurisdiction.entrySet()) {
						String rules = "";
						for (String rule : map.getValue()) {
							rules +=rule;
						}
						fq.add(map.getKey().toLowerCase() + ":" + rules);
					}
				}
				for (Object field : showField.toArray(new String[0])) {
					fl.add(field.toString().toLowerCase());
				}
			}

			Map<String, String> sortMap = new HashMap<String, String>();
			if (wheres.has("sortField")) {
				JSONObject sorts = wheres.getJSONObject("sortField"); // 排序字段
				Object[] o = sorts.keySet().toArray();
				for (int i = o.length - 1; i >= 0; i--) {
					JSONObject sort = sorts.getJSONObject((String) o[i]);
					sortMap.put(sort.getString("fieldName").toLowerCase(), sort.getString("order").toLowerCase());
				}
			}

			SolrQuery solrQuery = ruquestSolrQuery(requestHandler, q, fq, fl, sortMap, ispage, pageno, pagesize);
			logger.info("=====solr查询条件：" + solrQuery.toQueryString());
			logger.info("=====solr查询条件：" + solrQuery.toString());
			QueryResponse response = client.query(solrQuery);

			if (response != null) {
				SolrDocumentList list = response.getResults();
				List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
				Map<String, String> map = null;

				for (SolrDocument sd : list) {
					Collection<String> fieldNames = sd.getFieldNames();
					map = new HashMap<String, String>();
					for (String field : fieldNames) {
						map.put(field, sd.get(field).toString());
					}
					datas.add(map);
				}

				int numFound = (int) list.getNumFound();
				if ("1".equals(ispage)) {
					Page page = new Page();
					page.setShowCount(pagesize); // 每页显示记录数
					page.setCurrentPage(pageno); // 当前页
					page.setTotalPage((int) Math.ceil((double) numFound / (double) pagesize));
					page.setTotalResult(numFound);
					result.put("page", page);
				}
				result.put("list", datas);
			}
		} catch (SolrServerException e) {
			logger.error("@@@@@@--solr  " + BusinessHandler.typeMap.get(apitype) + "  服务器连接异常--@@@@@@");
			e.printStackTrace();
			return "{\"status\":\"" + EnumStatus.internal_server_error.getEnumValue() + "\"}";
		} catch (IOException e) {
			logger.error("@@@@@@--solr  " + BusinessHandler.typeMap.get(apitype) + "  请求IO异常--@@@@@@");
			e.printStackTrace();
			return "{\"status\":\"" + EnumStatus.internal_server_error.getEnumValue() + "\"}";
		} catch (RemoteSolrException e) {
			logger.error("@@@@@@--solr" + BusinessHandler.typeMap.get(apitype) + "请求参数错误异常--@@@@@@");
			e.printStackTrace();
			return "{\"status\":\"" + EnumStatus.Parameter_error.getEnumValue() + "\"}";
		} catch (JSONException e) {
			logger.error("@@@@@@--solr" + BusinessHandler.typeMap.get(apitype) + "请求参数不是JSONArray错误异常--@@@@@@");
			e.printStackTrace();
			return "{\"status\":\"" + EnumStatus.Parameter_error.getEnumValue() + "\"}";
		} catch (IllegalArgumentException e) {
			logger.error("@@@@@@--solr" + BusinessHandler.typeMap.get(apitype) + "请求参数中排序应为desc和asc错误异常--@@@@@@");
			e.printStackTrace();
			return "{\"status\":\"" + EnumStatus.Parameter_error.getEnumValue() + "\"}";
		} catch (Exception e) {
			logger.error("@@@@@@--solr" + BusinessHandler.typeMap.get(apitype) + "请求其他异常--@@@@@@");
			e.printStackTrace();
			return "{\"status\":\"" + EnumStatus.internal_server_error.getEnumValue() + "\"}";
		}
		return result;
	}

	/**
	 * @描述：获取solr查询的封装类SolrQuery
	 * @作者：SZ
	 * @时间：2016年11月9日 下午4:43:38
	 * @param requestHandler
	 *            请求处理器
	 * @param q
	 *            问题集合
	 * @param fq
	 *            关键字集合
	 * @param fl
	 *            返回字段的集合
	 * @param sortMap
	 *            排序字段集合
	 * @param ispage
	 *            是否分页
	 * @param pageno
	 *            当前第几页
	 * @param pagesize
	 *            每页面显示数量
	 * @return
	 */
	private static SolrQuery ruquestSolrQuery(String requestHandler, List<String> q, List<String> fq, List<String> fl,
			Map<String, String> sortMap, String ispage, int pageno, int pagesize) {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setRequestHandler(requestHandler);

		if (!q.isEmpty()) {
			String query = "";
			for (int i = 0; i < q.size(); i++) {
				query += q.get(i)+"\n";
			}
			if(query.endsWith("\n"))
				query = query.substring(0, query.length()-1);
			solrQuery.setQuery(query);// 查询的问题
		}

		if (!fq.isEmpty()) {
			for (int i = 0; i < fq.size(); i++) {
				solrQuery.addFilterQuery(fq.get(i));// 必须包含某个词组
			}
		}

		if (!fl.isEmpty()) {
			for (int i = 0; i < fl.size(); i++) {
				solrQuery.addField(fl.get(i));// 返回的字段名称
			}
		}
		List<SortClause> listsolrQuery = new ArrayList<SortClause>();
		if (!sortMap.isEmpty()) {
			for (String key : sortMap.keySet()) {
				SortClause sortClause = new SortClause(key, ORDER.valueOf(sortMap.get(key)));
				listsolrQuery.add(sortClause);// 排序字段
			}
			solrQuery.setSorts(listsolrQuery);
		}
		if ("1".equals(ispage)) {
			solrQuery.setStart(((pageno - 1) * pagesize) < 0 ? 0 : ((pageno - 1) * pagesize));// 起始行数
			solrQuery.setRows(pagesize);// 返回记录数
		} else if ("0".equals(ispage)) {
			solrQuery.setStart(0);
			solrQuery.setRows(Integer.MAX_VALUE);// 最大值
		}

		return solrQuery;
	}
}
