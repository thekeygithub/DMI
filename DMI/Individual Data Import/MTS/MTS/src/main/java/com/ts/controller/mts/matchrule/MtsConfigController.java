package com.ts.controller.mts.matchrule;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsConfig;
import com.ts.entity.mts.MtsConfigDetail;
import com.ts.entity.mts.MtsConfigRecord;
import com.ts.entity.mts.MtsConfigTest;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataSource;
import com.ts.service.mts.MTSConfigPrks;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.dataSource.MtsDataSourceService;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.impl.DataConfigRecord;
import com.ts.service.mts.matchrule.impl.DataMtsConfigTest;
import com.ts.util.BigExcelReader;
import com.ts.util.Const;
import com.ts.util.FileUpload;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.PathUtil;

@Controller
@RequestMapping(value="/mtsConfigDR")
public class MtsConfigController extends BaseController{
	
	@Resource(name = "MtsDataSourceService")
	private MtsDataSourceService mtsDataSourceService;
	
	@Resource(name = "DataConfigRecord")
	private DataConfigRecord dataConfigRecord;
	
	@Resource(name="DataClassService")
	private DataClassManger dataClassService;
	
	@Resource(name="MTSAreaService")
	private MTSAreaService mTSAreaService;
	
	@Resource(name = "MTSConfigPrks")
	private MTSConfigPrks mtsconfigPrks;
	
	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	
	private static int temp = 20;
	private  Map<String, String> matchRuleCacheMap ;// 匹配规则缓存容器	
	
	/**
	 * 标化页面首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/configFirst")
	public ModelAndView configFirst(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		List<MtsDataSource> listDataSource = mtsDataSourceService.findMtsDataSource(new MtsDataSource());
		List<PageData> listConfigFirst =dataConfigRecord.listConfig(page);
		mv.setViewName("mts/mtsconfig/mts_config_list");
		mv.addObject("listDataSource", listDataSource);
		mv.addObject("listConfigFirst", listConfigFirst);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 标化详细tab页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsCongfigDetail")
	public ModelAndView listMtsCongfigDetail(Page page, HttpServletRequest request) throws Exception {
		String PT_ID = request.getParameter("PT_ID");
		String AREA_CODE = request.getParameter("AREA_CODE");
		String CLASS_CODE = request.getParameter("CLASS_CODE");
		ModelAndView mv = this.getModelAndView();
		
		mv.setViewName("mts/mtsconfig/mts_config_record_list");		
		mv.addObject("PT_ID", PT_ID);
		mv.addObject("AREA_CODE", AREA_CODE);
		mv.addObject("CLASS_CODE", CLASS_CODE);
		return mv;
	}
	
	/**
	 * 自动标化结果维护页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsRecord")
	public ModelAndView listMtsRecord(Page page, HttpServletRequest request) throws Exception {
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String PT_ID = request.getParameter("PT_ID");
		String TYPE = request.getParameter("TYPE");   // 1是自动化页面 0是问题单页面 2是全部
		String AREA_CODE = request.getParameter("AREA_CODE");
		String CLASS_CODE = request.getParameter("CLASS_CODE");
		//查询数据列表用
		pd.put("PT_ID", PT_ID);
		pd.put("TYPE", TYPE);
		
		//查询表头用
		pd.put("AREA_CODE", AREA_CODE);
		pd.put("CLASS_CODE", CLASS_CODE);
		pd.put("RECORD_TYPE", TYPE);
		page.setPd(pd);
		
		List<PageData> listWFBH =dataConfigRecord.listWFBH();
		List<PageData> listZDtitle =dataConfigRecord.listTitle(page);
		List<PageData> listZDrecord =dataConfigRecord.listZDRecord(page);
		
		mv.setViewName("mts/mtsconfig/mts_config_ZDrecord");		
		mv.addObject("listZDrecord", listZDrecord);	
		mv.addObject("listZDtitle", listZDtitle);
		mv.addObject("listWFBH", listWFBH);
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**
	 * NLP人工切词页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manuNLP")
	public ModelAndView manuNLP(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String TID=(String) pd.get("T_ID");		
		int T_ID=Integer.parseInt(TID);
	
		String SNAME=URLDecoder.decode(pd.getString("SNAME"),"utf8"); 
		String WFBH=URLDecoder.decode(pd.getString("WFBH"),"utf8"); 
		pd.put("SNAME", SNAME);
		pd.put("WFBH", WFBH);
	
		List<PageData> listManuNLP =dataConfigRecord.listRecord(T_ID);
		String SOURCEDATA=dataConfigRecord.listDetail(T_ID);
		List<PageData> listTerm=dataConfigRecord.listTermType();
		mv.setViewName("mts/mtsconfig/mts_taskctl_nlp");
		mv.addObject("listManuNLP", listManuNLP);
		mv.addObject("listTerm", listTerm);
		mv.addObject("SOURCEDATA", SOURCEDATA);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 保存人工切词结果
	 * 
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/savaNLP")
	public ModelAndView savaNLP(Page page) throws Exception {
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
	
		String tid=pd.getString("T_ID");
		int T_ID=Integer.parseInt(tid);
        pd.put("T_ID", T_ID);
        String nlpdesc=pd.getString("RIGHT_DESC");
        pd.put("NLP", nlpdesc.replace("#q&q#", "；"));
        page.setPd(pd);
        dataConfigRecord.editDetail(page);           //修改NLP切词结果
        dataConfigRecord.deleteRecord(T_ID);         //删除原有切词匹配结果
        String[] nlpn=nlpdesc.split("#q&q#");
        MtsConfigRecord mcr=new MtsConfigRecord();	
        mcr.setT_ID(T_ID);
        mcr.setNLP_VTYPE("人工切词");
        mcr.setNLP_STATUS("3");
        for(int i=0;i<nlpn.length;i++){
        	 mcr.setNLP_SNAME(nlpn[i]);
        	 mcr.setNLP_ORDER(i);
        	 dataConfigRecord.addConfigRecord(mcr);  //新增切词
        }
		
		mv.setViewName("save_result");
		return mv;
	}
	
	
	/**
	 * 按批次删除数据
	 * 
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/delZDData")
	public ModelAndView delZDData(Page page) throws Exception {
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
	
		String PT_ID=pd.getString("PT_ID");
		dataConfigRecord.deleteRecordByPt(PT_ID);
		dataConfigRecord.deleteDetailByPt(PT_ID);
		dataConfigRecord.deleteConfigByPt(PT_ID);
		mv.setViewName("save_result");
		return mv;
	}
	
	

	/**打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	public ModelAndView goUploadExcel(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();	
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String PT_ID= df.format(new Date());
		pd.put("PT_ID", PT_ID);
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);                //列出所有区域
		List<PageData> sourceList=dataConfigRecord.findDataSource();           //查询数据来源列表
		
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("areaList", areaList);
		mv.addObject("sourceList", sourceList);
		mv.setViewName("mts/mtsconfig/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file
			,String PT_NAME,String PT_ID,String FLAG,String AREACODE, String CLASSCODE) throws Exception{
		ModelAndView mv = this.getModelAndView();	
		PageData pd = new PageData();
		pd = this.getPageData();			
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;								//文件上传路径
			String fileName =  FileUpload.fileUp(file, filePath, "nlpDataexcel");	//执行上传			
			final List<PageData> xlistPd=new ArrayList<PageData>();		
			String xfilepath = filePath+fileName;  		
	        BigExcelReader reader = new BigExcelReader(xfilepath) {  
	        	PageData pd = new PageData();
	           
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
			
			final String batchnum=PT_ID;
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
						MtsConfigDetail mcd=new MtsConfigDetail();							
						mcd.setPT_ID(batchnum);						
						while (!queueList.isEmpty()) {
							String var1 = null;
							String var0 = null;
							try {
									PageData pgd=queueList.take();								
									var1 = pgd.getString("var1");
									var0 = pgd.getString("var0");							
									if(var1!="" || !"".equals(var1)){
										
										mcd.setSOURCETEXT(var0);
										mcd.setSOURCEDATA(var1);								
										dataConfigRecord.addConfigDetail(mcd);
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
					MtsConfig mc=new MtsConfig();
					mc.setAREA_CODE(AREACODE);
					mc.setCLASS_CODE(CLASSCODE);
					mc.setPT_ID(PT_ID);
					mc.setPT_NAME(PT_NAME);
					mc.setSOURCE_FLAG(FLAG);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH24:mm:ss");//设置日期格式
					String date= df.format(new Date());
					mc.setCRE_DATE(date);
					mc.setCON_DATE(date);
					dataConfigRecord.addConfig(mc);
					
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
	 * 标准化多线程
	 * @throws Exception 
	 */
	@RequestMapping(value="/standard")	
	public ModelAndView standard(String pt_id,String AREA,String classcode ) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"标准化MtsConfigDetail");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();		
		pd = this.getPageData();
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();				
		List<MtsConfigDetail>  BHList=dataConfigRecord.findByPT(pd.getString("PT_ID"));
		final String memid=matchRuleCacheMap.get("WMEMIDRK"+AREA+classcode+"1");
		final String area=AREA;
		final String calssCode=classcode;
		
		final LinkedBlockingQueue<MtsConfigDetail> queueList=new LinkedBlockingQueue<MtsConfigDetail>();			
		for(MtsConfigDetail str:BHList){
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
							MtsConfigDetail mcd=queueList.take();							
							    var1=mcd.getSOURCEDATA();							
								if(var1!="" || !"".equals(var1)){
									mtsconfigPrks.prksConfigInto(area, calssCode, mcd, memid, matchRuleCacheMap);
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
		

	}
}
