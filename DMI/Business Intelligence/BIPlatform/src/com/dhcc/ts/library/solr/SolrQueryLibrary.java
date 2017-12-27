package com.dhcc.ts.library.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;

import com.dhcc.common.util.SystemConfig;

import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * @描述：图请服务查询
 * @作者：SZ
 * @时间：2017年4月20日 上午9:40:05
 */
public class SolrQueryLibrary {
	private static SolrClient client;
	
	
	public static final String IP = SystemConfig.getConfigInfomation("SOLR_IP");
	public static final String POST = SystemConfig.getConfigInfomation("SOLR_POST");
	public static final String PACKAGE = SystemConfig.getConfigInfomation("SOLR_PACKAGE");
	public static final String PROJECT = SystemConfig.getConfigInfomation("SOLR_PROJECT");
	public static final String DEFAULT_URL = "http://"+IP+":"+POST+"/"+PACKAGE+"/"+PROJECT;//"http://10.10.50.33:8090/solrQuery/ts_library";

	public static void init() {
		client = new HttpSolrClient(DEFAULT_URL);
	}
	/**
	 * @描述：图请服务查询接口
	 * @作者：SZ
	 * @时间：2017年4月20日 下午2:22:45
	 * @param queryValue 
	 * 			查询条件key为字段名称，value为对应的查询条件
	 * 
	 * @param mustHaveValue 
	 * 			查询时必须包含的条件  key为字段名称，value为对应的查询条件
	 * 
	 * @param sortMap 
	 * 			排序map 排序字段名称为key
	 * 
	 * @param mapGroupField 
	 *		包含的 参数
	 * 			facetField：需要返回分组的字段
	 * 			facetLimit：返回分组的结果集个数
	 * 			prefix：限制匹配分组结果集的前缀
	 * 
	 * @param ispage 是否分页   1：分页、0：不分页
	 * @param pageno 页数
	 * @param pagesize 每页显示条数
	 * @return
	 * Map<String, Object>
	 * status--0：失败，1：成功
	 * data--查询的结果集
	 * numFound--总记录数
	 * start--起始行数
	 * groupData--mapGroupField的返回参数
	 */
	public static Map<String, Object> queryLibrary(Map<String,String> queryValue, Map<String,String> mustHaveValue, 
			List<String> returnFiled,Map<String, SolrQuerySort> sortMap,Map<String, Object> mapGroupField,
			String ispage,int pageno,int pagesize){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String requestHandler = "/select";
			List<String> q = new ArrayList<String>();
			List<String> fq = new ArrayList<String>();
			for (String fieldkey : queryValue.keySet()) {
				if("*".equals(fieldkey)&&"*".equals(queryValue.get(fieldkey))){
					q.add("*:*");
				}else{
					if("detailes".equals(fieldkey)){
						String[] queryValueDetailes = queryValue.get(fieldkey).split("@#&");
						for(String queryValueDetaile : queryValueDetailes){
							q.add(fieldkey+":"+queryValueDetaile);
						}
					}
					q.add(fieldkey+":"+queryValue.get(fieldkey));
				}
			}
			
			for (String fieldkey : mustHaveValue.keySet()) {
				fq.add(fieldkey+":"+mustHaveValue.get(fieldkey));
			}
			
			QueryResponse response = null;
		
			SolrQuery query = requestSolrQuery(requestHandler, q, fq, returnFiled, sortMap, mapGroupField, ispage, pageno, pagesize);
			System.out.println(query.toQueryString());
			response = client.query(query);
		
			if (response != null) {
				List<SolrQueryModel> list = response.getBeans(SolrQueryModel.class);
				map.put("status", "1");
				map.put("data", list);
				map.put("numFound", response.getResults().getNumFound());
				map.put("start", response.getResults().getStart());
				List<FacetField> listFacetField = response.getFacetFields();
				if(null!=listFacetField){
					Map<String, Map<String,List<String>>> mapGroup = new HashMap<String, Map<String,List<String>>>();
					for(FacetField model : listFacetField){
						List<String> listGroupField = new ArrayList<String>();
						Map<String,List<String>> gMap=new HashMap<String,List<String>>();
						if(!model.getValues().isEmpty()){
							for(Count model1 : model.getValues()){
								listGroupField.add(model1.getName());
							}
							gMap.put(model.getName(), listGroupField);
							if(mapGroup.get(SolrQueryGroupField.groupMap.get(model.getName()))!=null){
								gMap.putAll(mapGroup.get(SolrQueryGroupField.groupMap.get(model.getName())));	
							}
							mapGroup.put(SolrQueryGroupField.groupMap.get(model.getName()),gMap);
						}
						
					}
					map.put("groupData", mapGroup);
				}
			}else{
				map.put("status", "0");
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
			map.put("status", "0");
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			map.put("status", "0");
			return map;
		}
		return map;
	}

	/**
	 * @描述：获取solr查询的封装类SolrQuery
	 * @作者：SZ
	 * @时间：2017年4月20日 下午2:25:19
	 * @param requestHandler 请求处理器
	 * @param q 问题集合
	 * @param fq 关键字集合
	 * @param fl 返回字段的集合
	 * @param sortMap 排序字段集合
	 * @param groupField 提取分组字段
	 * @param ispage 是否分页1：分页、0：不分页
	 * @param pageno 当前第几页
	 * @param pagesize 每页面显示数量
	 * @return
	 */
	private static SolrQuery requestSolrQuery(String requestHandler, List<String> q, List<String> fq, List<String> fl,
			Map<String, SolrQuerySort> sortMap, Map<String, Object> mapGroupField, String ispage, int pageno, int pagesize) {
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
				SortClause sortClause = new SortClause(key, ORDER.valueOf(sortMap.get(key).toString()));
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
		
		if(!mapGroupField.isEmpty()){
			for(String field : (List<String>)mapGroupField.get("facetField")){
				solrQuery.addFacetField(field);
			}
			solrQuery.setFacetLimit((int)mapGroupField.get("facetLimit"));
			solrQuery.setFacetPrefix((String)mapGroupField.get("prefix"));
			solrQuery.setFacetMinCount(1);
		}
		
		return solrQuery;
	}
	
	/**
	 * @描述：判断是否能正常连接solr
	 * @作者：SZ
	 * @时间：2017年5月9日 下午3:41:30
	 */
	public static boolean solrPing(){
		try {
			if(null==client){
				init();
			}
			client.ping();
//			SolrPingResponse solrPingResponse = client.ping();
//			System.out.println("=======ping时长："+solrPingResponse.getQTime()+"ms");
//			System.out.println("=======ping状态："+solrPingResponse.getStatus());
			return true;
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			System.out.println("====SolrServerException=服务器拒绝连接====");
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("====IOException=IO异常===="+e.getMessage());
//			e.printStackTrace();
		} catch (RemoteSolrException e){
			System.out.println("=HttpSolrClient$RemoteSolrException:=url异常="+e.getMessage());
//			e.printStackTrace();
		}
		return false;
	}
	public static void main(String[] args) {
		init();
		Map<String,String> queryValue = new HashMap<String ,String>();
		queryValue.put("*", "*");
		Map<String,String> mustHaveValue = new HashMap<String ,String>();
		queryValue.put("resource_type", "5");
		Map<String, SolrQuerySort> sortMap = new HashMap<String ,SolrQuerySort>();
//		sortMap.put("detailes", SolrQuerySort.asc);
		List<String> listGroupField = new ArrayList<String>();
		listGroupField.add("descriptor_chs");
//		listGroupField.add("author_en");
		Map<String, Object> mapGroupField = new HashMap<String, Object>();
		mapGroupField.put("facetField", listGroupField);//需要返回分组的字段
		mapGroupField.put("facetLimit", 20);//返回分组的结果集个数
//		mapGroupField.put("prefix", "a");//限制匹配分组结果集的前缀
//		mapGroupField.put("facetQuery", "");//在这个查询结果中分组
		List<String> listreturn = new ArrayList<String>();
		//listreturn.add("author_en");
		long start = System.currentTimeMillis();
		Map<String, Object>  map = queryLibrary(queryValue, mustHaveValue,listreturn, sortMap,mapGroupField, "1", 0, 1);
		System.out.println(map);
		
		for(SolrQueryModel list :(List<SolrQueryModel>)map.get("data")){
			System.out.println(list.getDetailes());
			System.out.println(list.getAuthor_en());
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println(end-start +"ms");
		
	}
}
