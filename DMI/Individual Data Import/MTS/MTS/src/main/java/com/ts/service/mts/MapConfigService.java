package com.ts.service.mts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ts.entity.Page;
import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.entity.mts.MtsDataType;
import com.ts.entity.mts.NlpTerm;
import com.ts.entity.mts.OInterver;
import com.ts.entity.mts.OTerm;
import com.ts.service.mts.matchrule.impl.DataMatchRule;
import com.ts.service.mts.matchrule.impl.DataMatchRuleDetail;
import com.ts.service.mts.matchrule.impl.DataOInverter;
import com.ts.service.mts.matchrule.impl.DataOTerm;
import com.ts.service.mts.matchrule.impl.DataPrksTs;
import com.ts.service.mts.matchrule.impl.DataTypeService;
import com.ts.service.nlp.data.impl.TermTypeService;
import com.ts.util.CommonUtils;
import com.ts.util.PageData;
import com.ts.util.StringUtil;

import net.sf.json.JSONObject;

@Service("mapConfigService")
public class MapConfigService {
	
	private static Logger logger = Logger.getLogger(MapConfigService.class);
	
	@Resource(name = "DataTypeService")
	private DataTypeService dataTypeService;
	@Resource(name = "DataMatchRule")
	private DataMatchRule dataMatchRule;
	@Resource(name = "DataMatchRuleDetail")
	private DataMatchRuleDetail dataMatchRuleDetail;
	@Resource(name = "TermTypeService")
	private TermTypeService termTypeService;
	@Resource(name = "DataOTerm")
	private DataOTerm dot;
	@Resource(name = "DataOInverter")
	private DataOInverter dataOInverter;
	@Resource(name = "DataPrksTs")
	private DataPrksTs dpt;
	
	private Page page=new Page();
	
	private static Map<String, String> matchRuleCacheMap = new HashMap<String, String>();// 匹配规则详细缓存容器
	
	
	
	/**
	 * 采用单例模式获取缓存对象实例
	 * 
	 * @return
	 */	
	
	public Map<String, String> getMatchRuleCacheMap() {
		//System.out.println(nlpUrl);
		if (null == matchRuleCacheMap || matchRuleCacheMap.isEmpty()) {
			String nlpUrl = CommonUtils.getPropValue("nlp.properties", "nlp.url");
			matchRuleCacheMap.put("nlpUrl", nlpUrl);		
			this.dataTypeCacheLoad();
			this.ruleCacheLoad();
			this.ruleDetailCacheLoad();
			this.termTypeCacheLoad();
			this.wRuleDetailCacheLoad();
			this.regexLoad();
			this.specSoftCacheLoad();
		}
		return matchRuleCacheMap;
	}
	
	public void regexLoad(){
		
		String termsStrs =this.termsLoad();  //非医学术语串
		String doubtsStrs = this.doubtLoad();; //怀疑诊断串
		String specialStrs= this.specialLoad(); //诊断限定词串
		String inverStrs= this.inversLoad(); //无法干预词串
		String jpStrs = this.ZLandSHZTload()[0]; //解剖部位串
		String opStrs= this.ZLandSHZTload()[1]; //术后词串
		String zlStrs= this.ZLandSHZTload()[2]; //肿瘤转移词串
		String	arr=this.arrLoad();//字符集
		matchRuleCacheMap.put("termsStrs", termsStrs);
		matchRuleCacheMap.put("doubtsStrs", doubtsStrs);
		matchRuleCacheMap.put("specialStrs", specialStrs);
		matchRuleCacheMap.put("inverStrs", inverStrs);
		matchRuleCacheMap.put("jpStrs", jpStrs);
		matchRuleCacheMap.put("opStrs", opStrs);
		matchRuleCacheMap.put("zlStrs", zlStrs);
		matchRuleCacheMap.put("arr", arr);
	}

	/**
	 * 加载标化类型
	 */
	public void dataTypeCacheLoad(){		
		
		
		List<MtsDataType> typeList= new ArrayList<MtsDataType>();
		try {
			typeList=dataTypeService.listAllDataType();
			
		} catch (Exception e) {
				
			e.printStackTrace();
		}
		
		for(int i=0;i<typeList.size();i++){
			String dataCode=typeList.get(i).getMEM_DATA_CODE();
			String dataType=typeList.get(i).getDATA_TYPE_ID();
			matchRuleCacheMap.put("DATACODE"+dataCode, dataType);					
		}
		
	
		
		logger.info("====加载标化类型进缓存dataTypeCacheMap，共"+typeList.size()+"条======");
		
	}
	


	/**
	 * 加载匹配规则中，标化类型对应的key和value
	 */
	public void ruleCacheLoad(){
		
		List<PageData> ruleList= new ArrayList<PageData>();
		try {
			ruleList=dataMatchRule.matchRulelp(page);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		for(int i=0;i<ruleList.size();i++){
			String dataType=ruleList.get(i).getString("DATA_TYPE_ID");
			String dataKey=ruleList.get(i).getString("KEY_GEN_RULE");
			String dataValue=ruleList.get(i).getString("VALUE_STR");
			String dataNlp=ruleList.get(i).getString("IFNLP");
			String stander=ruleList.get(i).getString("STANDARDE");
			String areaid=ruleList.get(i).getString("AREA_ID");
			matchRuleCacheMap.put("KEY"+areaid+dataType, dataKey);	
			matchRuleCacheMap.put("VALUE"+areaid+dataType, dataValue);
			matchRuleCacheMap.put("NLP"+areaid+dataType, dataNlp);
			matchRuleCacheMap.put("STAND"+areaid+dataType, stander);
		}
		

		
		logger.info("====加载匹配规则进缓存，共"+ruleList.size()+"条======");
	}
	
	/**
	 * 加载匹配规则详细中的内循环使用的数据，即flag为0的数据
	 */
	public void ruleDetailCacheLoad(){
		
		
		MatchRuleDetailT mrt=new MatchRuleDetailT();
		mrt.setFLAG("0");
		List<MatchRuleDetailT> ruleDetailList= new ArrayList<MatchRuleDetailT>();
		try {
			ruleDetailList=dataMatchRuleDetail.findMatchRuleListByClassCode(mrt);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		for(int i=0;i<ruleDetailList.size();i++){
			String classCode=ruleDetailList.get(i).getDATA_CLASS_CODE();
			String oldType=ruleDetailList.get(i).getIFNEXT();
			String dataType=ruleDetailList.get(i).getMEM_DATA_CODE();
			String pinKey=ruleDetailList.get(i).getPINKEY();
			String areaid=ruleDetailList.get(i).getAREA_ID();
			String memid=ruleDetailList.get(i).getMEM_ID();
			String fallto=ruleDetailList.get(i).getFAILURE_REDIRECT_TO();
			String succto=ruleDetailList.get(i).getSUCCESS_REDIRECT_TO();
			String mark=ruleDetailList.get(i).getMARK();
			matchRuleCacheMap.put("DETAIL"+areaid+classCode+oldType, dataType);
			matchRuleCacheMap.put("DETAILPIN"+areaid+classCode+oldType, pinKey);
			matchRuleCacheMap.put("MEMID"+areaid+classCode+oldType, memid);
			matchRuleCacheMap.put("FAILTO"+memid, fallto);
			matchRuleCacheMap.put("SUCCTO"+memid, succto);
			matchRuleCacheMap.put("XBHLX"+memid, dataType);
			matchRuleCacheMap.put("MEMPIN"+memid, pinKey);
			matchRuleCacheMap.put("RESUL"+memid, mark);
		}
		
		logger.info("====加载 匹配规则内流程进缓存，共"+ruleDetailList.size()+"条======");
	}
	
	
	/**
	 * 加载匹配规则详细中的外环使用的数据，即flag为1的数据
	 */
	public void wRuleDetailCacheLoad(){
		
		
		MatchRuleDetailT mrt=new MatchRuleDetailT();
		mrt.setFLAG("1");
		List<MatchRuleDetailT> ruleDetailList= new ArrayList<MatchRuleDetailT>();
		try {
			ruleDetailList=dataMatchRuleDetail.findMatchRuleListByClassCode(mrt);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		for(int i=0;i<ruleDetailList.size();i++){
			String classCode=ruleDetailList.get(i).getDATA_CLASS_CODE();			
			String dataType=ruleDetailList.get(i).getMEM_DATA_CODE();			
			String areaid=ruleDetailList.get(i).getAREA_ID();
			String memid=ruleDetailList.get(i).getMEM_ID();
			String fallto=ruleDetailList.get(i).getFAILURE_REDIRECT_TO();
			String succto=ruleDetailList.get(i).getSUCCESS_REDIRECT_TO();
			String mark=ruleDetailList.get(i).getMARK();
			String applay=ruleDetailList.get(i).getAPPLY_METHOD();
			String rk=ruleDetailList.get(i).getORDERBY();
			String noc=ruleDetailList.get(i).getNOC();						
			matchRuleCacheMap.put("WMEMIDRK"+areaid+classCode+rk, memid);
			matchRuleCacheMap.put("WFAILTO"+memid, fallto);
			matchRuleCacheMap.put("WSUCCTO"+memid, succto);
			matchRuleCacheMap.put("WXBHLX"+memid, dataType);			;
			matchRuleCacheMap.put("WRESUL"+memid, mark);
			matchRuleCacheMap.put("WNOC"+memid, noc);
			matchRuleCacheMap.put("WAPP"+memid, applay);
		}
		
		logger.info("====加载 匹配规则外流程进缓存，共"+ruleDetailList.size()+"条======");
	}
	
	/**
	 * 加载术语类型转换的有效数据，即flag为0的数据
	 */
	public void termTypeCacheLoad(){
		
		
		List<NlpTerm> nlpTermList= new ArrayList<NlpTerm>();
		try {
			nlpTermList=termTypeService.findAllLoadTerm();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		for(int i=0;i<nlpTermList.size();i++){
			String termName=nlpTermList.get(i).getTERM_CN_NAME();
			String classcode=nlpTermList.get(i).getTERM_CLASSCODE();
			String dataType=nlpTermList.get(i).getTERM_DATATYPE();

			matchRuleCacheMap.put("TERMCLASS"+termName, classcode);
			matchRuleCacheMap.put("TERMTYPE"+termName, dataType);
		}
		
		logger.info("====加载 nlp术语类型 进缓存，共"+nlpTermList.size()+"条======");
	}
	
	
	/**
	 * 重新装载
	 */
	
	@SuppressWarnings("static-access")
	public void ReLoadCache() {
		if (null != matchRuleCacheMap && !matchRuleCacheMap.isEmpty()) {
			this.matchRuleCacheMap.clear();
		}
		String nlpUrl = CommonUtils.getPropValue("nlp.properties", "nlp.url");
		matchRuleCacheMap.put("nlpUrl", nlpUrl);
		this.dataTypeCacheLoad();
		this.ruleCacheLoad();
		this.ruleDetailCacheLoad();
		this.termTypeCacheLoad();
		this.wRuleDetailCacheLoad();
		this.regexLoad();
		this.specSoftCacheLoad();
	}
	
	

	/*
	 * 肿瘤部位、肿瘤转移及术后状态数据加载
	 */
	public String[] ZLandSHZTload(){
		
		 ArrayList<String> jpal = new ArrayList<String>();
		 ArrayList<String> opal = new ArrayList<String>();
		 ArrayList<String> zlal = new ArrayList<String>();
		 List<String> jplist=new ArrayList<String>();
		 List<String> oplist=new ArrayList<String>();
		 List<String> zlzylist=new ArrayList<String>();
		try {
			jplist=dpt.findJP();
			oplist=dpt.findOP();
			zlzylist=dpt.findZLZY();		
		} catch (Exception e) {			
			e.printStackTrace();
		}
		if (jplist != null && jplist.size() > 0) {
			for (int i = 0; i < jplist.size(); i++) {
				String spl= StringUtil.full2Half(jplist.get(i).toUpperCase().trim());				
				jpal.add(Pattern.quote(spl));
			}
		}
		if (oplist != null && oplist.size() > 0) {
			for (int i = 0; i < oplist.size(); i++) {
				String spl= StringUtil.full2Half(oplist.get(i).toUpperCase().trim());				
				opal.add(Pattern.quote(spl));
			}
		}
		if (zlzylist != null && zlzylist.size() > 0) {
			for (int i = 0; i < zlzylist.size(); i++) {
				String spl= StringUtil.full2Half(zlzylist.get(i).toUpperCase().trim());				
				zlal.add(Pattern.quote(spl));
			}
		}
		
		String jpStrs = "(" + StringUtils.join(jpal, "|") + ")";
		String opStrs = "(" + StringUtils.join(opal, "|") + ")";
		String zlStrs = "(" + StringUtils.join(zlal, "|") + ")";
		
		jpal=null;
		opal=null;
		zlal=null;
		zlzylist=null;
		oplist=null;
		jplist=null;
		return new String[]{jpStrs,opStrs,zlStrs};
	}
	
	/**
	 * 加载非医学术语
	 * @param str
	 * @return
	 */
 	public String termsLoad(){		
		
		 ArrayList<String> terms = new ArrayList<String>();
		 List<OTerm> otlist=new ArrayList<OTerm>();
		try {
			otlist=dot.findByFlag("1");
		} catch (Exception e) {			
			e.printStackTrace();
		}
		if (otlist != null && otlist.size() > 0) {
			for (int i = 0; i < otlist.size(); i++) {
				OTerm o= otlist.get(i);
				String term_name=StringUtil.full2Half(o.getTERM_NAME().toUpperCase());
				terms.add(Pattern.quote(term_name));
			}
		}
		
		String termsStrs = "(" + StringUtils.join(terms, "|") + ")";
		
		terms=null;
		otlist=null;
		return termsStrs;
	}
	
	
	/**
	 * 加载无法干预数据
	 * @param str
	 * @return
	 */
	public String inversLoad(){
		
		
		 ArrayList<String> terms = new ArrayList<String>();
		 List<OInterver> otlist=new ArrayList<OInterver>();
		try {
			otlist=dataOInverter.findAllOInver();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		if (otlist != null && otlist.size() > 0) {
			for (int i = 0; i < otlist.size(); i++) {
				OInterver o= otlist.get(i);
				String term_name=StringUtil.full2Half(o.getINTERV_NAME().toUpperCase());
				terms.add(Pattern.quote(term_name));
			}
		}
		
		String inverStrs = "(" + StringUtils.join(terms, "|") + ")";
		
		terms=null;
		otlist=null;
		return inverStrs;
	}
	
	
	/**
	 * 加载怀疑诊断
	 * @param str
	 * @return
	 */
	public String doubtLoad(){
		 ArrayList<String> doubt = new ArrayList<String>();
		 List<OTerm> otdlist=new ArrayList<OTerm>();
		try {
			otdlist=dot.findByFlag("2");
		} catch (Exception e) {			
			e.printStackTrace();
		}
		if (otdlist != null && otdlist.size() > 0) {
			for (int i = 0; i < otdlist.size(); i++) {
				OTerm o= otdlist.get(i);
				doubt.add(Pattern.quote(o.getTERM_NAME()));
			}
		}
		
		String doubtsStrs = "(" + StringUtils.join(doubt, "|") + ")";
		
		doubt=null;
		otdlist=null;
		return doubtsStrs;
	}
	
	/**
	 * 加载特殊字处理
	 * @param str
	 * @return
	 */
	public String specialLoad(){
		 ArrayList<String> special = new ArrayList<String>();
		 List<String> splist=new ArrayList<String>();
		try {
			splist=dot.findBySPType("SP01");
		} catch (Exception e) {			
			e.printStackTrace();
		}
		if (splist != null && splist.size() > 0) {
			for (int i = 0; i < splist.size(); i++) {
				String spl= StringUtil.full2Half(splist.get(i).toUpperCase());				
				special.add(Pattern.quote(spl));
			}
		}
		
		String specialStrs = "(" + StringUtils.join(special, "|") + ")";
		
		special=null;
		splist=null;
		return specialStrs;
	}

	public String arrLoad(){
		
		String[] arrs={"\"",".","。","、","，",",","'","‘","?","？","!","！","“","\\","/","|",";","；",":","："};
		ArrayList<String> doubt = new ArrayList<String>();		 
		for (int i = 0; i < arrs.length; i++) {
			
			doubt.add(Pattern.quote(arrs[i]));
		}		
		
			String arr= "(" + StringUtils.join(doubt, "|") + ")";
			
			return arr;
	}
	
	/**
	 * 加载数据处理流程 进缓存
	 */
	public void specSoftCacheLoad(){
		
		
		try {
			List<PageData> areaList=dataMatchRuleDetail.findSpecCnt();
			for (int i=0;i<areaList.size();i++){
				String AREA_ID=areaList.get(i).getString("AREA_ID");
				String CLASSCODE=areaList.get(i).getString("CLASSCODE");							
				List<String> softList=dataMatchRuleDetail.listSpecSoft(AREA_ID,CLASSCODE);
				String solist=StringUtils.join(softList.toArray(),"@");
				matchRuleCacheMap.put("SPECSOFT"+AREA_ID+CLASSCODE, solist);				
				logger.info("====加载数据处理 进缓存打印======"+softList.toString());
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}	
		
		
		logger.info("====加载数据处理流程 进缓存======");
	}

}
