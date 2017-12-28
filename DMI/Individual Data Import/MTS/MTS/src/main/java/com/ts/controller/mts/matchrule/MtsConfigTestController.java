package com.ts.controller.mts.matchrule;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsConfigRecord;
import com.ts.entity.mts.MtsConfigTest;
import com.ts.entity.mts.MtsDataClass;
import com.ts.service.mts.MTSConfigPrks;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfig;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.MtsConfigTestService;
import com.ts.service.mts.matchrule.impl.DataConfigRecord;
import com.ts.service.mts.matchrule.impl.DataMtsConfigTest;
import com.ts.threadPool.ThreadPoolTaskForMtsConfigStand;
import com.ts.util.ApplicationUtil;
import com.ts.util.BigExcelReader;
import com.ts.util.Const;
import com.ts.util.FileUpload;
import com.ts.util.Jurisdiction;
import com.ts.util.ObjectExcelRead;
import com.ts.util.PageData;
import com.ts.util.PathUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/mtsConfigTest")
public class MtsConfigTestController extends BaseController {
	
	@Resource(name="MtsConfig")
	private MtsConfig mc;
	@Resource(name="DataMtsConfigTest")
	private DataMtsConfigTest dmct;	
	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	@Resource(name="MTSAreaService")
	private MTSAreaService mTSAreaService;
	@Resource(name = "DataClassService")
	private DataClassManger dataClassService;
	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;	
	@Resource(name = "MTSConfigPrks")
	private MTSConfigPrks mtsconfigPrks;	
	@Resource(name = "DataConfigRecord")
	private DataConfigRecord dcr;
	
	
	private  Map<String, String> matchRuleCacheMap ;// 匹配规则缓存容器	
	private static int temp = 20;
	
	/**
	 * 查询批次列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPT")
	public ModelAndView listPT(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		List<PageData> listMtsDBdata =dmct.findPT(); // 列出无用术语列表

		mv.setViewName("mts/configTest/mts_config_list");
		mv.addObject("listMtsDBdata", listMtsDBdata);
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();	
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String batchNum= df.format(new Date());
		pd.put("batchNum", batchNum);
		mv.setViewName("mts/configTest/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcelcol(
			@RequestParam(value="excel",required=false) MultipartFile file
			,String PT_NAME,String BATCH_NUM) throws Exception{
		ModelAndView mv = this.getModelAndView();	
		PageData pd = new PageData();
		pd = this.getPageData();
		MtsConfigTest mctdata=new MtsConfigTest();	
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;								//文件上传路径
			String fileName =  FileUpload.fileUp(file, filePath, "nlpDataexcel");	//执行上传
			@SuppressWarnings("unchecked")
			//List<PageData> listPd = (List)ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 1, 0, 0);		//执行读EXCEL操作,读出的数据导入List 1:从第2行开始；0:从第A列开始；0:第0个sheet
			
			final List<PageData> xlistPd=new ArrayList<PageData>();		
			String xfilepath = filePath+fileName;  		
	        BigExcelReader reader = new BigExcelReader(xfilepath) {  
	        	PageData pd = new PageData();
	            @SuppressWarnings("null")
				@Override  
	            protected void outputRow(String[] datas, int[] rowTypes, int rowIndex) {  
	                // 此处将每一行的数据保存 (除第0标题行)
	            	if(rowIndex!=0){
	            		for(int i=0;i<datas.length;i++){
		                	pd.put("var" + i, datas[i]);
		                }
		                xlistPd.add(pd);
	            	}
	                
	            }  
	        };  
	        // 执行解析  
	        reader.parse();  	     
	       
			final LinkedBlockingQueue<PageData> queueList=new LinkedBlockingQueue<PageData>();			
			for(PageData str:xlistPd){
    			queueList.add(str);
    		}
			final String  ptname=PT_NAME;
			final String batchnum=BATCH_NUM;
			/*多线程存入数据库操作======================================*/
			
			/**	
			 * var0 :序号
			 * var1 :术语			 			
			 */

			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(temp);
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < temp; i++) {
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						MtsConfigTest mctdata=new MtsConfigTest();							
						mctdata.setPT_ID(batchnum);
						mctdata.setPT_NAME(ptname);
						while (!queueList.isEmpty()) {
							String var1 = null;
							String var0 = null;
							try {
									PageData pgd=queueList.take();								
									var1 = pgd.getString("var1");
									var0 = pgd.getString("var0");							
									if(var1!="" || !"".equals(var1)){
										
											mctdata.setSOURCETEXT(var0);
											mctdata.setSOURCEDATA(var1);								
											dmct.addConfigTest(mctdata);
									}
								} catch (Exception e) {									
									e.printStackTrace();
								}								
						}
					}
				});
			}
			
			while (true) {
				if (((ThreadPoolExecutor) fixedThreadPool).getActiveCount() > 0) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}else{
					long endTime = System.currentTimeMillis(); 
					System.out.println("==============导入excel共"+xlistPd.size()+"条数据的总耗时： " + (endTime - startTime) + "ms");
					fixedThreadPool.shutdown();
					mv.addObject("msg", "success");
					mv.setViewName("save_result");
					return mv;
				}
			}			
			
			
		}
		
		mv.setViewName("save_result");
		return mv;
	}
	
	
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteMtsConfig")	
	public void deleteMtsConfig(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"删除MtsConfigTest");
		PageData pd = new PageData();
		pd = this.getPageData();
		List<MtsConfigTest>  list=dmct.findByPT(pd.getString("PT_ID"));
		
		int[] ArrayIDS=new int[list.size()];		
		
		for(int i=0;i<list.size();i++){				
			ArrayIDS[i]=list.get(i).getT_ID();
		}
		
		dmct.deleteConfigTest(ArrayIDS);		
		out.write("success");
		out.close();
	}
	
	

	/**进入标准化页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goMtsStand")
	public ModelAndView goMtsStand(String PT_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PT_ID", PT_ID);
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		
		mv.setViewName("mts/configTest/mts_stand");
		mv.addObject("pd", pd);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("areaList", areaList);
		return mv;
	}
	
	/**
	 * 标准化多线程
	 * @throws Exception 
	 */
	@RequestMapping(value="/standard")	
	public ModelAndView standard(String pt_id,String AREA,String classcode ) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"标准化MtsConfigTest");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();				
		List<MtsConfigTest>  BHList=dmct.findByPT(pd.getString("PT_ID"));
		final String memid=matchRuleCacheMap.get("WMEMIDRK"+AREA+classcode+"1");
		final String area=AREA;
		final String calssCode=classcode;
		
		final LinkedBlockingQueue<MtsConfigTest> queueList=new LinkedBlockingQueue<MtsConfigTest>();			
		for(MtsConfigTest str:BHList){
			queueList.add(str);
		}		

		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(temp);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < temp; i++) {
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					
					while (!queueList.isEmpty()) {
						String var1 = null;					
						try {
							    MtsConfigTest mct=queueList.take();							
							    var1=mct.getSOURCEDATA();							
								if(var1!="" || !"".equals(var1)){
									mtsconfigPrks.prksConfigInto_b(area, calssCode, mct, memid, matchRuleCacheMap);
								}
							} catch (Exception e) {									
								e.printStackTrace();
							}								
					}
				}
			});
		}
		
		while (true) {
			if (((ThreadPoolExecutor) fixedThreadPool).getActiveCount() > 0) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				long endTime = System.currentTimeMillis(); 
				System.out.println("==============标准化"+BHList.size()+"条数据的总耗时： " + (endTime - startTime) + "ms");
				fixedThreadPool.shutdown();
				mv.addObject("msg", "success");
				mv.setViewName("save_result");
				return mv;
			}
		}
		
//		mv.setViewName("save_result");
//		return mv;
	}
	
	/**
	 * 标准化单线程
	 * @throws Exception 
	 */
	@RequestMapping(value="/standard1")	
	public ModelAndView standard1(String pt_id,String AREA,String classcode ) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"标准化MtsConfigTest");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();				
		List<MtsConfigTest>  BHList=dmct.findByPT(pd.getString("PT_ID"));
		String memid=matchRuleCacheMap.get("WMEMIDRK"+AREA+classcode+"1");
		
		for(int i=0;i<BHList.size();i++){
			
			String value=WMatchP(AREA,classcode,"{}",BHList.get(i).getSOURCEDATA(),memid);
			JSONObject temp=JSONObject.fromObject(value);
			String result="";
			String status="";
			String NLP="";
			String NlpValue="";
			String SPEC="";
			String doubt="";
			String TSBH="";
			
			result=temp.getString("result");
			status=temp.getString("status");
			if(temp.has("NLP")){
				NLP=temp.getString("NLP");
			}
			
			if(temp.has("NlpValue")){
				NlpValue=temp.getString("NlpValue");
			}
			
			if(temp.has("SPEC")){
				SPEC=temp.getString("SPEC");
			}			
			
			if(temp.has("doubt")){
				doubt=temp.getString("doubt");
			}
			if(temp.has("TSBH")){
				TSBH=temp.getString("TSBH");
			}
			//特殊字符处理后的剩余字符串如果为空的话，则此术语直接舍弃，不输出标化结果20170711
			if("spec".equals(result)){
				result="";
			}
			MtsConfigTest mcten=new MtsConfigTest();
			mcten.setDOUB(doubt);
			mcten.setNLP(NLP);
			mcten.setNLPVALUE(NlpValue);
			mcten.setRESULT(result);
			mcten.setSPEC(SPEC);
			mcten.setTYPE(status);
			mcten.setT_ID(BHList.get(i).getT_ID());
			
			
			dmct.editConfigTest(mcten);
			
			
		}
		
		mv.setViewName("save_result");
		return mv;
	}
	
	
	/***
	 * DETAIL外部流程
	 * @param areaid  区域
	 * @param classcode  聚类
	 * @param bhlxx   新标化类型
	 * @param jsonarr  新key串
	 * @param jsonarr  拼key标识
	 * @param jsonarr  原始key
	 * @param jsonarr  流程ID
	 * @return
	 */
	public String WMatchP(String areaid,String classcode,String jsonarr,String inkey,String memid){
		
		
		String app=matchRuleCacheMap.get("WAPP"+memid);
		String fallto=matchRuleCacheMap.get("WFAILTO"+memid);
		String succto=matchRuleCacheMap.get("WSUCCTO"+memid);
		String bhlx=matchRuleCacheMap.get("WXBHLX"+memid);
		String mark=matchRuleCacheMap.get("WRESUL"+memid);
		
		JSONObject oValue=new JSONObject();
		oValue.put("status", "0");
		oValue.put("result", "none");
		String oldValue=oValue.toString();
		if(!"{}".equals(jsonarr)){
			
			oldValue=jsonarr;
		}		
		String value="";		
		JSONObject jskey = new JSONObject(); 		 
		jskey.put("data",bhlx+"@#&"+inkey);
		JSONObject jsclass = new JSONObject(); 
		jsclass.put("dataType",classcode);
		
		
		if("matchPRKS".equals(app)){
			
			String outJson=mc.matchPRKS(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				oldValue=	outJson;	
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else{
				value=oldValue;
			}
		}else if("match".equals(app)){
			
			String outJson=mc.match(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				oldValue=	outJson;	
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else{
				value=oldValue;
			}
		}else if("matchNlp".equals(app)){
			
			String outJson=mc.matchNlp(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			oldValue=outJson;		
								
					

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("matchNlpPRKS".equals(app)){
			
			String outJson=mc.matchNlpPRKS(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			oldValue=outJson;		
								
					

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
		}else if("special".equals(app)){
			
			String outJson=mc.special(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("doubt".equals(app)){
			
			String outJson=mc.doubt(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			JSONObject dbjtemp=JSONObject.fromObject(oldValue);
			if(sttpin=="1"||"1".equals(sttpin)){
				
				dbjtemp.put("doubt", jtempin.get("doubt"));
				oldValue=dbjtemp.toString();		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("inerver".equals(app)){
			
			String outJson=mc.inerver(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				JSONObject dbjtemp=JSONObject.fromObject(oldValue);	
				dbjtemp.put("result", "无法标化");
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("matchPRKS23".equals(app)){
			
			String outJson=mc.matchPRKS23(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("matchNlpPRKS23".equals(app)){
			
			String outJson=mc.matchNlpPRKS23(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			oldValue=outJson;			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("specialPRKS23".equals(app)){
			
			String outJson=mc.specialPRKS23(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("tsckZL".equals(app)){
			
			String outJson=mc.tsckZL(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("tsckSHZT".equals(app)){
			
			String outJson=mc.tsckSHZT(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("specialPRKS".equals(app)){
			
			String outJson=mc.specialPRKS(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}
		
		
		
		return value;
	}
	
	
	
	@RequestMapping(value="/recordinto")	
	public ModelAndView recordinto(String pt_id,String AREA,String classcode ) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"外部临时保存数据");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();

		List<MtsConfigTest> conlist=new ArrayList<MtsConfigTest>();
		try {
			conlist=dmct.findByPT("20171011151501");
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		final LinkedBlockingQueue<MtsConfigTest> queueList=new LinkedBlockingQueue<MtsConfigTest>();			
		for(MtsConfigTest str:conlist){
			queueList.add(str);
		}	
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 100; i++) {
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					Pattern paZM = Pattern.compile("[A-Za-z]");
					while (!queueList.isEmpty()) {
											
						try {
							    MtsConfigTest mct=queueList.take();							
							    String Sname=mct.getSOURCEDATA();         //原始数据
							    String nlpNames=mct.getNLP();             //NLP切词数据
							    String nlpVals=mct.getNLPVALUE();         //NLP数据匹配结果
							    String vals=mct.getRESULT();              //直接匹配结果
							    int tid=mct.getT_ID();                    //数据ID
							    String specs=mct.getSPEC();               //特殊处理标识
							    
							  //T_ID,NLP_SNAME,NLP_SVAL,NLP_STYPE,NLP_ORDER,NLP_VNAME,NLP_VCODE1,NLP_VCODE2,NLP_VTYPE,SPEC_TYPE,NLP_STATUS
							    if(nlpNames==null||"".equals(nlpNames)){                //直接匹配
							    	MtsConfigRecord mcr=new MtsConfigRecord();							    
									mcr.setNLP_SNAME(Sname);
									mcr.setNLP_ORDER(0);
									if(specs.contains("1")){
										mcr.setSPEC_TYPE("1");
									}else{
										mcr.setSPEC_TYPE("0");
									}
									
									mcr.setNLP_SVAL(vals);
									mcr.setT_ID(tid);
									String[] valArr= vals.split(",");//NLP值数组
									for(int j=0;j<valArr.length;j++){
										String[] val=valArr[j].split("@#&");
										String valType=val[val.length-1];
										if("中医".equals(valType)){
											//肉瘤病@#&BWL030@#&@#&@#&@#&中医										
											
											mcr.setNLP_STYPE("中医");
											mcr.setNLP_VNAME(val[0]);
											mcr.setNLP_VCODE1(val[1]);
											mcr.setNLP_VTYPE("中医");
											mcr.setNLP_STATUS("1");
											dcr.addConfigRecord(mcr);
											
											String xy=val[2];
											if(!(xy==null||"".equals(xy))){											
												
												mcr.setNLP_VNAME(val[2]);
												mcr.setNLP_VCODE1(val[3]);
												mcr.setNLP_VCODE2(val[4]);
												mcr.setNLP_VTYPE("诊断");
												mcr.setNLP_STATUS("1");
												dcr.addConfigRecord(mcr);
											}
											
										}else if("概念转换".equals(valType)){
											//混合痔@;&I84.201@;&@#&特指手术后状态@;&Z98.800@;&@#&@#&@#&@#&@#&@#&概念转换	
										
										
											mcr.setNLP_STYPE("概念转换");
											for(int d=0;d<val.length-1;d++){
												String[] valg=val[d].split("@;&");					
												if(valg.length>1){
													int op=0;	                      // 0代表手术  1代表诊断
													String beg="";
													if(valg[1]==null||"".equals(valg[1])){
														beg=valg[2].substring(0, 1);
													}else{
														beg=valg[1].substring(0, 1);
													}
													Matcher zmMt=paZM.matcher(beg);
													if(zmMt.find()){
														op=1;
													}
													if(op==1){
															
														mcr.setNLP_VNAME(val[0]);
														mcr.setNLP_VCODE1(val[1]);
														if(valg.length>2){
															mcr.setNLP_VCODE2(val[2]);
														}
														mcr.setNLP_VTYPE("诊断");
														mcr.setNLP_STATUS("1");
														dcr.addConfigRecord(mcr);
														
														
													}else{														
														
														mcr.setNLP_VNAME(val[0]);
														mcr.setNLP_VCODE1(val[1]);													
														mcr.setNLP_VTYPE("手术");
														mcr.setNLP_STATUS("1");
														dcr.addConfigRecord(mcr);
												    }
												}
												
											}
											
											
											
										}else if("".equals(valType)){//不予输出的
											
											mcr.setNLP_STYPE("不输出");
											mcr.setNLP_VNAME("其他");
											mcr.setNLP_VTYPE("无法标化");
											mcr.setNLP_STATUS("1");
											dcr.addConfigRecord(mcr);
											
										}else if("诊断".equals(valType)){											
											
											
											mcr.setNLP_STYPE("诊断");
											mcr.setNLP_VNAME(val[0]);
											mcr.setNLP_VCODE1(val[1]);
											mcr.setNLP_VCODE2(val[2]);
											mcr.setNLP_VTYPE("诊断");
											mcr.setNLP_STATUS("1");
											dcr.addConfigRecord(mcr);
											
										}else if("手术".equals(valType)){											
											
											mcr.setNLP_STYPE("手术");
											mcr.setNLP_VNAME(val[0]);
											mcr.setNLP_VCODE1(val[1]);											
											mcr.setNLP_VTYPE("手术");
											mcr.setNLP_STATUS("1");
											dcr.addConfigRecord(mcr);
											
										}else if("无法标化".equals(valType)){											
											
											mcr.setNLP_STYPE("无法标化");
											mcr.setNLP_VNAME("val[0]");
											mcr.setNLP_VTYPE("无法标化");
											mcr.setNLP_STATUS("1");
											dcr.addConfigRecord(mcr);
											
										}else if("肿瘤部位对照".equals(valType)){											
											
											
											mcr.setNLP_STYPE("肿瘤部位对照");
											int op=0;	                      // 0代表手术  1代表诊断
											String beg=val[1].substring(0, 1);											
											Matcher zmMt=paZM.matcher(beg);
											if(zmMt.find()){
												op=1;
											}
											
											if(op==1){
												mcr.setNLP_VNAME(val[0]);
												mcr.setNLP_VCODE1(val[1]);											
												mcr.setNLP_VTYPE("诊断");
												mcr.setNLP_STATUS("1");
												dcr.addConfigRecord(mcr);
											}else{
												mcr.setNLP_VNAME(val[0]);
												mcr.setNLP_VCODE1(val[1]);											
												mcr.setNLP_VTYPE("手术");
												mcr.setNLP_STATUS("1");
												dcr.addConfigRecord(mcr);
											}
										}else if("none".equals(valType)&&val.length>1){	//专为本次准备，术后转移的标识是none										
											
											mcr.setNLP_STYPE("术后转移");
											mcr.setNLP_VNAME(val[0]);
											mcr.setNLP_VCODE1(val[1]);											
											mcr.setNLP_VTYPE("诊断");
											mcr.setNLP_STATUS("1");
											dcr.addConfigRecord(mcr);
											
										}
										
									}
									
							    }else{                                                  //NLP切词后匹配
							    	String demosub = nlpNames.substring(2,nlpNames.length()-2);									
									String demoArray[] = demosub.split("\",\"");
									List<String> list = Arrays.asList(demoArray);		
									JSONObject js=JSONObject.fromObject(nlpVals);									
									

									for(int i=0;i<list.size();i++){	
										MtsConfigRecord mcr=new MtsConfigRecord();
										String name=list.get(i);//NLP名        --NLP_SNAME   i--NLP_ORDER
										String yname=name.substring(0,name.lastIndexOf("【"));
							
										String yval=js.getString(yname);
										
										mcr.setNLP_SNAME(name);
										mcr.setNLP_ORDER(i);
										//mcr.setSPEC_TYPE(specs);//本次指定，以后循环获取
										if(specs.contains("1")){
											mcr.setSPEC_TYPE("1");
										}else{
											mcr.setSPEC_TYPE("0");
										}
										mcr.setNLP_SVAL(yval);
										mcr.setT_ID(tid);
										
										if(yval==null||"".equals(yval)){
											
											
											mcr.setNLP_STYPE("不输出");
											mcr.setNLP_VNAME("其他");
											mcr.setNLP_VTYPE("无法标化");
											mcr.setNLP_STATUS("1");
											dcr.addConfigRecord(mcr);
											
										}else{
											System.out.println("NLP_SVAL==="+yval);
											String[] valArr= yval.split(",");//NLP值数组
											for(int j=0;j<valArr.length;j++){
												String[] val=valArr[j].split("@#&");
												String valType=val[val.length-1];
												if("中医".equals(valType)){
													//肉瘤病@#&BWL030@#&@#&@#&@#&中医										
													
													mcr.setNLP_STYPE("中医");
													mcr.setNLP_VNAME(val[0]);
													mcr.setNLP_VCODE1(val[1]);
													mcr.setNLP_VTYPE("中医");
													mcr.setNLP_STATUS("1");
													dcr.addConfigRecord(mcr);
													
													String xy=val[2];
													if(!(xy==null||"".equals(xy))){											
														
														mcr.setNLP_VNAME(val[2]);
														mcr.setNLP_VCODE1(val[3]);
														mcr.setNLP_VCODE2(val[4]);
														mcr.setNLP_VTYPE("诊断");
														mcr.setNLP_STATUS("1");
														dcr.addConfigRecord(mcr);
													}
													
												}else if("概念转换".equals(valType)){
													//混合痔@;&I84.201@;&@#&特指手术后状态@;&Z98.800@;&@#&@#&@#&@#&@#&@#&概念转换	
												
												
													mcr.setNLP_STYPE("概念转换");
													for(int d=0;d<val.length-1;d++){
														String[] valg=val[d].split("@;&");					
														if(valg.length>1){
															int op=0;	                      // 0代表手术  1代表诊断
															String beg="";
															if(valg[1]==null||"".equals(valg[1])){
																beg=valg[2].substring(0, 1);
															}else{
																beg=valg[1].substring(0, 1);
															}
															Matcher zmMt=paZM.matcher(beg);
															if(zmMt.find()){
																op=1;
															}
															if(op==1){
																	
																mcr.setNLP_VNAME(valg[0]);
																mcr.setNLP_VCODE1(valg[1]);
																if(valg.length>2){
																	mcr.setNLP_VCODE2(valg[2]);
																}
																mcr.setNLP_VTYPE("诊断");
																mcr.setNLP_STATUS("1");
																dcr.addConfigRecord(mcr);
																
																
															}else{														
																
																mcr.setNLP_VNAME(valg[0]);
																mcr.setNLP_VCODE1(valg[1]);													
																mcr.setNLP_VTYPE("手术");
																mcr.setNLP_STATUS("1");
																dcr.addConfigRecord(mcr);
														    }
														}
														
													}													
													
													
												}else if("none".equals(valType)&&val.length>1){	//专为本次准备，术后转移的标识是none										
													
													mcr.setNLP_STYPE("术后转移");
													mcr.setNLP_VNAME(val[0]);
													mcr.setNLP_VCODE1(val[1]);											
													mcr.setNLP_VTYPE("诊断");
													mcr.setNLP_STATUS("1");
													dcr.addConfigRecord(mcr);
													
												}else if("none".equals(valType)&&val.length==1){//不予输出的
													
													mcr.setNLP_STATUS("0");
													dcr.addConfigRecord(mcr);
													
												}else if("诊断".equals(valType)){											
													
													
													mcr.setNLP_STYPE("诊断");
													mcr.setNLP_VNAME(val[0]);
													mcr.setNLP_VCODE1(val[1]);
													mcr.setNLP_VCODE2(val[2]);
													mcr.setNLP_VTYPE("诊断");
													mcr.setNLP_STATUS("1");
													dcr.addConfigRecord(mcr);
													
												}else if("手术".equals(valType)){											
													
													mcr.setNLP_STYPE("手术");
													mcr.setNLP_VNAME(val[0]);
													mcr.setNLP_VCODE1(val[1]);											
													mcr.setNLP_VTYPE("手术");
													mcr.setNLP_STATUS("1");
													dcr.addConfigRecord(mcr);
													
												}else if("无法标化".equals(valType)){											
													
													mcr.setNLP_STYPE("无法标化");
													mcr.setNLP_VNAME("val[0]");
													mcr.setNLP_VTYPE("无法标化");
													mcr.setNLP_STATUS("1");
													dcr.addConfigRecord(mcr);
													
												}else if("肿瘤部位对照".equals(valType)){											
													
													
													mcr.setNLP_STYPE("肿瘤部位对照");
													int op=0;	                      // 0代表手术  1代表诊断
													String beg=val[1].substring(0, 1);											
													Matcher zmMt=paZM.matcher(beg);
													if(zmMt.find()){
														op=1;
													}
													
													if(op==1){
														mcr.setNLP_VNAME(val[0]);
														mcr.setNLP_VCODE1(val[1]);											
														mcr.setNLP_VTYPE("诊断");
														mcr.setNLP_STATUS("1");
														dcr.addConfigRecord(mcr);
													}else{
														mcr.setNLP_VNAME(val[0]);
														mcr.setNLP_VCODE1(val[1]);											
														mcr.setNLP_VTYPE("手术");
														mcr.setNLP_STATUS("1");
														dcr.addConfigRecord(mcr);
													}
												}
												
											}
											
										}
										
									}
							    }
								
							} catch (Exception e) {									
								e.printStackTrace();
							}								
					}
				}
			});
		}
		
		while (true) {
			if (((ThreadPoolExecutor) fixedThreadPool).getActiveCount() > 0) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				long endTime = System.currentTimeMillis(); 
				//System.out.println("==============标准化"+BHList.size()+"条数据的总耗时： " + (endTime - startTime) + "ms");
				fixedThreadPool.shutdown();
				mv.addObject("msg", "success");
				mv.setViewName("save_result");
				return mv;
			}
		}
		
//		mv.setViewName("save_result");
//		return mv;
	}
	

}
