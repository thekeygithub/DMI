package com.ts.controller.mts.HandlerCenter.CacheUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ts.entity.Page;
import com.ts.entity.mts.MatchRule;
import com.ts.entity.mts.MatchRuleDetailT;

import com.ts.entity.mts.MtsDataType;
import com.ts.entity.mts.OInterver;
import com.ts.entity.mts.OTerm;

import com.ts.entity.mts.MtsToolkitT;

import com.ts.entity.mts.RsRuleDetailT;
import com.ts.service.mts.matchrule.MatchRuleDetailManger;
import com.ts.service.mts.matchrule.impl.DataMatchRule;
import com.ts.service.mts.matchrule.impl.DataOInverter;
import com.ts.service.mts.matchrule.impl.DataOTerm;
import com.ts.service.mts.matchrule.impl.DataPrksTs;
import com.ts.service.mts.matchrule.impl.DataTypeService;
import com.ts.service.mts.rsrule.RsRuleDetailManger;
import com.ts.service.mts.tool.ToolManger;
import com.ts.util.CommonUtils;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.StringUtil;

/**
 * 缓存管理 
 * @author autumn
 *
 */
@Component
public class CacheManager
{
    
    private static Logger log = Logger.getLogger("CacheManager");

    /* 主流程缓存   */
    private static Map<String, MatchRuleDetailT> cacheMRD = new HashMap<String , MatchRuleDetailT>();
    /* 嵌套流程缓存   */
    private static Map<String ,MatchRuleDetailT> cacheSubMRD = new HashMap<String , MatchRuleDetailT>();
    
    /*标化结果处理规则缓存*/
    private static Map<String ,RsRuleDetailT> cacheRRD = new HashMap<String , RsRuleDetailT>();
    

    /* 公共缓存（非医学术语串、疾病限定词串等）   */
    private static Map<String, String> cacheCC = new HashMap<String , String>();
    /* 匹配规则缓存   */
    private static Map<String, MatchRule> cacheMR = new HashMap<String , MatchRule>();

    /*程序流程的工具包缓存*/
    private static Map<String ,MtsToolkitT> cacheTOOL=new HashMap<String ,MtsToolkitT>();
    

    /**
     * 主流程缓存设置
     * @param entity
     */
    public static void addMatchRuleDetail(MatchRuleDetailT entity)
    {
        if(entity == null) return ;
        // TODO 在orderby 等于 1 的情况下  ， key拼装的时候， 不需要携带 MEM_ID 字段  
        // 区域代码 + 聚类  + 流程主键ID  
        String key = entity.getAREA_ID() + "_" + 
                     entity.getDATA_CLASS_CODE() + "_" + 
                     //entity.getMEM_DATA_CODE() + "_" + 
                     ("0".equals(entity.getORDERBY())?entity.getMEM_ID():"");
        if(cacheMRD.containsKey(key)) 
        {
            log.info("该流程出现重复:" + key);
            
        }
        cacheMRD.put(key, entity);
    }
    
    /**
     * 获得主流程缓存
     * @param key
     * @return
     */
    public static  MatchRuleDetailT getMatchRuleDetail(String key )
    {
        return cacheMRD.get(key);
    }
    
    
    /**
     * 主流程缓存数量 
     * @return
     */
    public static int getMatchRuleDetailBySize()
    {
        return  cacheMRD.size();
    }
    
    /**
     * 嵌套流程缓存设置
     * @param entity
     */
    public static void addSubMatchRuleDetail(MatchRuleDetailT entity)
    {
        if(entity == null) return ;  
        // 嵌套流程 只用 流程主键ID  
        String key = entity.getMEM_ID();
        if(cacheSubMRD.containsKey(key))
        {
            log.info("该嵌套流程出现重复:" + key);
            
        }
        cacheSubMRD.put(key, entity);
    }
    
    /**
     * 获得嵌套流程
     * @param key
     * @return
     */
    public static  MatchRuleDetailT getSubMatchRuleDetail(String key )
    {
        return cacheSubMRD.get(key);
    }
    
    /**
     * 获得嵌套流程 缓存数量
     * @return
     */
    public static int getSubMatchRuleDetailBySize()
    {
        return  cacheSubMRD.size();
    }
    /****************************标化结果缓存********************************/
    /**
     * 标化结果 缓存配置
     * @param entity
     */
    public static void addRsRuleDetail(RsRuleDetailT entity)
    {
    	if(entity == null) return ;
        // 区域代码 + 聚类 
        String key = entity.getAREA_ID() + "_" + 
                     entity.getDATA_CLASS_CODE() ;
        if(cacheRRD.containsKey(key)) 
        {
            log.info("该配置出现重复:" + key);
            
        }
        cacheRRD.put(key, entity);
    }
    
    /**
     * 获得标化结果配置
     * @param key
     * @return
     */
    public static  RsRuleDetailT getRsRuleDetail(String key )
    {
        return cacheRRD.get(key);
    }
    
    /**
     * 获得标化结果配置缓存数量
     * @return
     */
    public static int getRsRuleDetailSize()
    {
        return  cacheRRD.size();
    }
    
    
    /****************************工具包缓存********************************/
    /**
     * 工具包 缓存配置
     * @param entity
     */
    public static void setTools(MtsToolkitT entity)
    {
    	if(entity == null) return ;
        // 流程的名称 
        String key = entity.getSOFT_NAME() ;//注册的软件的名称
        if(cacheTOOL.containsKey(key)) 
        {
            log.info("该配置出现重复:" + key);
            
        }
        cacheTOOL.put(key, entity);
    }
    
    /**
     * 获得工具列表
     * @param key
     * @return
     */
    public static  MtsToolkitT getTool(String key )
    {
        return cacheTOOL.get(key);
    }
    
    /**
     * 获得工具包数量
     * @return
     */
    public static int getToolSize()
    {
        return  cacheTOOL.size();
    }
    
    
    @Resource(name = "DataMatchRuleDetail")
    private MatchRuleDetailManger dataMatchRuleDetail;
    
    @Resource(name = "RsRuleDetail")
    private RsRuleDetailManger rsRuleDetail;

    
    @Resource(name = "DataTypeService")
   	private DataTypeService dataTypeService;
   	@Resource(name = "DataMatchRule")
   	private DataMatchRule dataMatchRule;
   	@Resource(name = "DataOTerm")
   	private DataOTerm dot;
   	@Resource(name = "DataOInverter")
   	private DataOInverter dataOInverter;
   	@Resource(name = "DataPrksTs")
   	private DataPrksTs dpt;
   	
   	private Page page=new Page();
   	

    
    @Resource(name = "ToolService")
    private ToolManger toolService;

    /**
     * 初始构造缓存数据 
     */
    @PostConstruct
    public void initMTSConfig()
    {
        MatchRuleDetailT mrt=new MatchRuleDetailT();
        RsRuleDetailT rrdt=new RsRuleDetailT();
        MtsToolkitT toolt=new MtsToolkitT();
        // 内循环外环标致 
//        mrt.setFLAG("1");
        List<MatchRuleDetailT> ruleDetailList= new ArrayList<MatchRuleDetailT>();
        List<RsRuleDetailT> rsRuleDatailList=new ArrayList<RsRuleDetailT>();

        
        List<MtsDataType> typeList= new ArrayList<MtsDataType>();
        List<PageData> ruleList= new ArrayList<PageData>();
        

        List<MtsToolkitT> toolList=new ArrayList<MtsToolkitT>();
        

        try {
            log.debug("加载流程缓存 开始 ");
            ruleDetailList = dataMatchRuleDetail.findMatchRuleListByClassCode(mrt);
            for(MatchRuleDetailT entity : ruleDetailList)
            {
                if("1".equals(entity.getFLAG())) 
                    CacheManager.addMatchRuleDetail(entity); // 主流程
                else
                    CacheManager.addSubMatchRuleDetail(entity); //嵌套流程 
            }
            log.debug("加载流程缓存  结束 ");
            
            log.debug("加载标化结果处理规则 缓存开始");
            rsRuleDatailList=rsRuleDetail.findRsRuleList(rrdt);
            for (RsRuleDetailT entity : rsRuleDatailList) {
				CacheManager.addRsRuleDetail(entity);
			}
            log.debug("加载标化结果处理规则 缓存结束");
            

            log.debug("加载公共 缓存开始");
            regexLoad();
            typeList=dataTypeService.listAllDataType();
            for (MtsDataType entity : typeList) {
				CacheManager.adddataType(entity);
			}
            log.debug("加载公共缓存结束");
            
            log.debug("加载匹配规则 缓存开始");
    		ruleList=dataMatchRule.matchRulelp(page);
    		for (PageData entity : ruleList) {
				CacheManager.addMatchRule(entity);
			}
    		log.debug("加载匹配规则 缓存结束");
            

            log.debug("加载流程处理工具包 缓存开始");
            toolList=toolService.findToolList(toolt);
            for (MtsToolkitT entity : toolList) {
				CacheManager.setTools(entity);
			}
            log.debug("加载流程处理工具包 缓存结束");
            
            
            

        } catch (Exception e) {         
            e.printStackTrace();
        }
       
    }
    
   
	
	/**
     * 公共 缓存配置
     * @param entity
     */
	public  void regexLoad(){
		String termsStrs =this.termsLoad();  //非医学术语串
 		String doubtsStrs = this.doubtLoad();; //怀疑诊断串
 		String specialStrs= this.specialLoad(); //诊断限定词串
 		String inverStrs= this.inversLoad(); //无法干预词串
 		String jpStrs = this.ZLandSHZTload()[0]; //解剖部位串
 		String opStrs= this.ZLandSHZTload()[1]; //术后词串
 		String zlStrs= this.ZLandSHZTload()[2]; //肿瘤转移词串
 		String nlpUrl = CommonUtils.getPropValue("nlp.properties", "nlp.url");
 		cacheCC.put("nlpUrl", nlpUrl);	
 		cacheCC.put("termsStrs", termsStrs);
 		cacheCC.put("doubtsStrs", doubtsStrs);
 		cacheCC.put("specialStrs", specialStrs);
 		cacheCC.put("inverStrs", inverStrs);
 		cacheCC.put("jpStrs", jpStrs);
 		cacheCC.put("opStrs", opStrs);
 		cacheCC.put("zlStrs", zlStrs);
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
	
	
	/**
	 * 标化类型结果 缓存配置
	 */
	
	 public static void adddataType(MtsDataType entity)
	    {
	    	if(entity == null) return ;
	        // 标化类型code 
	        String key = entity.getMEM_DATA_CODE();
	        //标化类型id
	        String value = entity.getDATA_TYPE_ID();
	        System.out.println("key:" + key);
	        if(cacheRRD.containsKey(key)) 
	        {
	            log.info("该配置出现重复:" + key);
	            
	        }
	        cacheCC.put(key, value);
	    }


	/**
	 * 加载匹配规则 缓存配置
	 */
	public static void addMatchRule(PageData entity){
		
		if(entity == null) return ;
        // 区域代码 + 标化类型		
        String key = entity.getString("AREA_ID")+ "_" + 
                  entity.getString("DATA_TYPE_ID");
        MatchRule mrentity=new MatchRule();
        mrentity.setDATA_TYPE_ID(entity.getString("DATA_TYPE_ID"));
        mrentity.setIFNLP(entity.getString("IFNLP"));
        mrentity.setKEY_GEN_RULE(entity.getString("KEY_GEN_RULE"));
        mrentity.setAREA_ID(entity.getString("AREA_ID"));
        mrentity.setSTANDARDE(entity.getString("STANDARDE"));
        mrentity.setVALUE_STR(entity.getString("VALUE_STR"));
        System.out.println("key:" + key);
        if(cacheRRD.containsKey(key)) 
        {
            log.info("该配置出现重复:" + key);
            
        }
        cacheMR.put(key, mrentity);
		

		
	}

	 /**
     * 获得公共缓存内容
     * @param key
     * @return
     */
    public static  String getRegex(String key )
    {
        return cacheCC.get(key);
    }
    
    /**
     * 获得公共 缓存数量
     * @return
     */
    public static int getRegexBySize()
    {
        return  cacheCC.size();
    }
    
    /**
     * 获得匹配规则
     * @param key
     * @return
     */
    public static  MatchRule getMatchRule(String key )
    {
        return cacheMR.get(key);
    }
    
    /**
     * 获得公共 缓存数量
     * @return
     */
    public static int getMatchRuleBySize()
    {
        return  cacheMR.size();
    }
	
    
}
