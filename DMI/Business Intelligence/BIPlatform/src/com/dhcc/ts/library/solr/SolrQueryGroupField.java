package com.dhcc.ts.library.solr;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dhcc.common.util.StringUtil;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年4月25日 上午9:06:31 
* @类说明：根据类型分组信息
*/
public class SolrQueryGroupField {

	private static Map<String, Map<String,Object>> map=new HashMap<String, Map<String,Object>>();
	public static Map<String,String> groupMap=new HashMap<String,String>();
	public static List<String> returnField=new LinkedList<String>();
	private static int facetLimit=20;
	static{
		Map<String, Object> zwwx=new HashMap<String,Object>();
		List<String> list=new LinkedList<String>();
		//中文期刊文献
//		list.add("title_chs");//题名
		list.add("author_chs");//作者
		list.add("descriptor_chs");//主题词
//		list.add("abstract_chs");//摘要
		list.add("journal_title");//期刊名
		list.add("publication_year");//出版年
		zwwx.put("facetField", list);
		zwwx.put("facetLimit", facetLimit);
		map.put("3", zwwx);
		list=new LinkedList<String>();
		//外文期刊
		Map<String, Object> wwwx=new HashMap<String,Object>();
//		list.add("title_en");//题名
		list.add("author_en");//作者
		list.add("descriptor_en");//主题词
//		list.add("abstract_en");//摘要
		list.add("journal_title");//期刊名
		list.add("publication_year");//出版年
		list.add("document_type");//文献类型
		list.add("language");//语种
		wwwx.put("facetField", list);
		wwwx.put("facetLimit", facetLimit);
		map.put("4", wwwx);
		//教科书
		list=new LinkedList<String>();
		Map<String, Object> jks=new HashMap<String,Object>();
//		list.add("title_en");//题名
//		list.add("title_chs");//题名
		list.add("descriptor_en");//主题词
		list.add("descriptor_chs");//主题词
		jks.put("facetField", list);
		jks.put("facetLimit", facetLimit);
		map.put("2", jks);
		//百科知识，循证医学
		list=new LinkedList<String>();
		Map<String, Object> bkzs=new HashMap<String,Object>();
//		list.add("title_en");//题名
//		list.add("title_chs");//题名
		list.add("descriptor_en");//主题词
		list.add("descriptor_chs");//主题词
//		list.add("abstract_en");//摘要
//		list.add("abstract_chs");//摘要
		list.add("source_en");//出处
		list.add("source_chs");//出处
		bkzs.put("facetField", list);
		bkzs.put("facetLimit", facetLimit);
		map.put("1", bkzs);
		map.put("5", bkzs);
		//视频资源
		list=new LinkedList<String>();
		Map<String, Object> spzy=new HashMap<String,Object>();
//		list.add("title_en");//题名
//		list.add("title_chs");//题名
		list.add("descriptor_en");//主题词
		list.add("descriptor_chs");//主题词
		list.add("author_en");//作者
		list.add("author_chs");//作者
		list.add("source_en");//出处
		list.add("source_chs");//出处
		spzy.put("facetField", list);
		spzy.put("facetLimit", facetLimit);
		map.put("6", spzy);
		
		//全部
		Map<String, Object> qb=new HashMap<String,Object>();
//		list.add("title_chs");//题名_中文--------==索引字段
//		list.add("title_en");//题名_英文--------==索引字段
		list.add("author_chs");//作者_中文--------==索引字段
		list.add("author_en");//作者_英文--------==索引字段
//		list.add("abstract_chs");//摘要_中文--------==索引字段
//		list.add("abstract_en");//摘要_英文--------==索引字段
		list.add("source_chs");//来源_中文--------==索引字段
		list.add("source_en");//来源_英文--------==索引字段
		list.add("descriptor_chs");//关键词_中文--------==索引字段
		list.add("descriptor_en");//关键词_英文--------==索引字段
		list.add("publication_year");//出版年--------==索引字段
		list.add("resource_type");//资源类型（1：医学百科知识、2：教科书、3：中文期刊、4：外文期刊、5：循证医学、6：影像资料）--------==索引字段
		list.add("journal_title");// 期刊名--------==索引字段
		list.add("document_type");// 文献类型--------==索引字段
		list.add("language");
		qb.put("facetField", list);
		qb.put("facetLimit", facetLimit);
		map.put("0", qb);
		
		
		
//		groupMap.put("title_chs", "题名");
//		groupMap.put("title_en", "题名");
		groupMap.put("author_en", "作者");
		groupMap.put("author_chs", "作者");
//		groupMap.put("abstract_en", "摘要");
//		groupMap.put("abstract_chs", "摘要");
		groupMap.put("source_chs", "出处");
		groupMap.put("source_en", "出处");
		groupMap.put("descriptor_chs", "关键词");
		groupMap.put("descriptor_en", "关键词");
		groupMap.put("publication_year", "出版年");
		groupMap.put("journal_title", "期刊名");
		groupMap.put("document_type", "文献类型");
		groupMap.put("resource_type", "资源类型");
		groupMap.put("language", "语种");
		
		/*返回字段*/
		returnField.add("qlbi_id");//主键id--------==索引字段
//		returnField.add("title_chs");//题名_中文--------==索引字段
//		returnField.add("title_en");//题名_英文--------==索引字段
		returnField.add("author_chs");//作者_中文--------==索引字段
		returnField.add("author_en");//作者_英文--------==索引字段
//		returnField.add("abstract_chs");//摘要_中文--------==索引字段
//		returnField.add("abstract_en");//摘要_英文--------==索引字段
		returnField.add("source_chs");//来源_中文--------==索引字段
		returnField.add("source_en");//来源_英文--------==索引字段
		returnField.add("descriptor_chs");//关键词_中文--------==索引字段
		returnField.add("descriptor_en");//关键词_英文--------==索引字段
		returnField.add("publication_year");//出版年--------==索引字段
		returnField.add("resource_type");//资源类型（1：医学百科知识、2：教科书、3：中文期刊、4：外文期刊、5：循证医学、6：影像资料）--------==索引字段
		returnField.add("view_count");//浏览次数
		returnField.add("bd_update_time");//百度词条更新时间
		returnField.add("bd_update_count");//百度词条更新次数
		returnField.add("creator");//编辑人
		returnField.add("createtime");//创建时间--------==索引字段
		
		returnField.add("journal_title");// 期刊名--------==索引字段
		returnField.add("document_type");// 文献类型--------==索引字段
		returnField.add("language");// 语种-------
	}
	public static Map<String,Object> getGroup(String type){
		Map<String,Object> mm=new HashMap<String,Object>();
		if(!StringUtil.isNullOrEmpty(type)){
			mm= map.get(type);
		}
		return mm;
	}
	public static void main(String[] args){
		System.out.println(groupMap.get("title_chs"));
	}
	
	
	
	
}

