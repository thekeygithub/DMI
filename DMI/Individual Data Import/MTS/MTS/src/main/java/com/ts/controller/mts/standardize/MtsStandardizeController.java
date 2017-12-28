package com.ts.controller.mts.standardize;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataSource;
import com.ts.entity.mts.MtsVisitType;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.dataSource.MtsDataSourceService;
import com.ts.service.mts.record.MTSRecordService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.service.mts.visitType.MtsVisitTypeService;
import com.ts.util.PageData;
import com.ts.util.RandomUtil;
import com.ts.vo.mts.StandardizeData;

import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
@RequestMapping(value = "/mtsStandardize")
public class MtsStandardizeController extends BaseController {

	String menuUrl = "mtsStandardize/standardizeDataPage.do"; // 菜单地址(权限用)
/*	@Resource(name = "MTSQuestionService")
	private MTSQuestionService mtsQuestionService;*/
	@Resource(name = "MTSRecordService")
	private MTSRecordService mtsRecordService;
	
	@Resource(name = "MtsVisitTypeService")
	private MtsVisitTypeService mtsVisitTypeService;
	
	@Resource(name = "MtsDataSourceService")
	private MtsDataSourceService mtsDataSourceService;
	
	
	@Resource(name = "StandardizeService")
	private StandardizeService standardizeService;
	
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	 
	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		return threadPoolTaskExecutor;
	}


	public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
	}

	
	@RequestMapping(value = "/standardizeData" , produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public Object standardizeData(@RequestBody String toBeStandardStr) {
		String visitId = "";
		String visitType = "";
		String dataSource = "";
		String dataType = "";
		String parameters = "";
//		String batchNum = "";
		String resultStr = "";
		String application = "";
		String areaId = "";
		
		if (toBeStandardStr == null || "".equals(toBeStandardStr)) {
			return null;
		}
		
		JSONObject dealWithStrJSON = JSONObject.fromObject(toBeStandardStr);
		if (dealWithStrJSON.containsKey("visitId")) {
			visitId = dealWithStrJSON.getString("visitId");
		}
		if (dealWithStrJSON.containsKey("visitType")) {
			visitType = dealWithStrJSON.getString("visitType");
		}
		if (dealWithStrJSON.containsKey("dataSource")) {
			dataSource = dealWithStrJSON.getString("dataSource");
		}
		if (dealWithStrJSON.containsKey("dataType")) {
			dataType = dealWithStrJSON.getString("dataType");
		}
		if (dealWithStrJSON.containsKey("parameters")) {
			parameters = dealWithStrJSON.getString("parameters");
		}
		if (dealWithStrJSON.containsKey("application")) {
			application = dealWithStrJSON.getString("application");
		}
		if (dealWithStrJSON.containsKey("areaId")) {
			areaId = dealWithStrJSON.getString("areaId");
		}
		
		String batchNum = RandomUtil.getRandomId();
		if(dealWithStrJSON != null){
			dealWithStrJSON = null;
		}
		
		StandardizeData data = new StandardizeData();
		data.setAreaId(areaId);
		data.setBatchNum(batchNum);
		data.setDataSource(dataSource);
		data.setDataType(dataType);
		data.setParameters(parameters);
		data.setVisitId(visitId);
		data.setVisitType(visitType);
		data.setApplication(application);
		try {
			resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, parameters, batchNum,application,areaId);
			/*new Thread(new StartTaskThread(threadPoolTaskExecutor, batchNum ,data)).start();
			int waitTime = 0;
			boolean bool = true;
			while(bool){
	            try {
	            	Thread.sleep(1000);
	                //这里可以写你自己要运行的逻辑代码
	                System.out.println("一分钟运行一次");
	                
	                Map<String, String> standardizeResMap = mcs.getStandardizeResMap();
	                if(standardizeResMap.containsKey(batchNum)){
	                	resultStr = standardizeResMap.get(batchNum);
	                	standardizeResMap.remove(batchNum);
	                	bool = false;
	                }else{
	                	if(100000 < waitTime){
	                		if("".equals(resultStr)){
	                			JSONObject resultJSON = new JSONObject();
	                			resultJSON.put("status", "3");
	                			resultJSON.put("description", "链接超时");
	                			resultStr = resultJSON.toString();
	                		}
	 	                	bool = false;
	 	                }
	                }
	                waitTime += 1000;
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultStr;
	}
	
	@RequestMapping(value = "/standardizeDataPage")
	public ModelAndView standardizeDataPage(Page page) throws Exception {
		String resultStr = "";
		String para = "";
		String[] paraStr = null;
		String stanWord = "";
		String stanCode = "";
		String paraToStan = "";
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String visitId = pd.getString("visitId"); 
		String visitType = pd.getString("visitType"); 
		String dataSource = pd.getString("dataSource"); 
		String dataType = pd.getString("dataType"); 
		String parameters = pd.getString("parameters");
		String application = "";
		String areaId = pd.getString("areaId");
		String batchNum = RandomUtil.getRandomId();
		
		if(null != parameters && !"".equals(parameters)){
			if(parameters.contains("@|&")){
				paraStr = parameters.split("@\\|&");
				if(paraStr != null && paraStr.length > 0){
					for(int i = 0;i < paraStr.length; i++){
						para = paraStr[i];
						if(para.contains("@#&")){
							stanWord = para.substring(0, para.indexOf("@#&"));
							stanCode = para.substring(para.indexOf("@#&") + "@#&".length());
							paraToStan = "05@#&" + stanWord + "@|&09@#&" + stanCode;
							resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, paraToStan, batchNum,application,areaId);
						}
					}
				}
			}else{
				if(parameters.contains("@#&")){
					stanWord = parameters.substring(0, parameters.indexOf("@#&"));
					stanCode = parameters.substring(parameters.indexOf("@#&") + "@#&".length());
					paraToStan = "05@#&" + stanWord + "@|&09@#&" + stanCode;
					resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, paraToStan, batchNum,application,areaId);
				}
			}
		}
		
		if(null != batchNum && !"".equals(batchNum)){
			pd.put("batchNum", batchNum);
		}
		page.setPd(pd);
		List<PageData> listStanData = mtsRecordService.mtsRecordlistPage(page); // 列出用户列表
		List<MtsVisitType> listMtsVisit = mtsVisitTypeService.findMtsVisitType(new MtsVisitType());
		List<MtsDataSource> listMtsDataSource = mtsDataSourceService.findMtsDataSource(new MtsDataSource());
		mv.setViewName("mts/standardize/mts_standardize_list");
		mv.addObject("listStanData", listStanData);
		mv.addObject("listMtsVisit", listMtsVisit);
		mv.addObject("listMtsDataSource", listMtsDataSource);
		mv.addObject("pd", pd);
		return mv;
	}
	                          
	@RequestMapping(value = "/standardizeDataForSinglePage")
	public ModelAndView standardizeDataForSinglePage(Page page) throws Exception {
		
		String resultStr = "";
		String paraToStan = "";
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String visitId = pd.getString("visitId"); 
		String visitType = pd.getString("visitType"); 
		String dataSource = pd.getString("dataSource"); 
		String dataType = pd.getString("dataType"); 
		/*String parameters = pd.getString("parameters");*/
		String toStandWord = pd.getString("toStandWord");
		String toStandCode = pd.getString("toStandCode");
		String application = "";
		String areaId = pd.getString("areaId");
		String batchNum = RandomUtil.getRandomId();
		
		if(null != toStandWord && !"".equals(toStandWord)){
			if(null != toStandCode && !"".equals(toStandCode)){
				paraToStan = "05@#&" + toStandWord + "@|&09@#&" + toStandCode;
			}else{
				paraToStan = "05@#&" + toStandWord ;
			}
//			resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, paraToStan, batchNum);
		}else{
			if(null != toStandCode && !"".equals(toStandCode)){
				paraToStan = "09@#&" + toStandCode;
//				resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, paraToStan, batchNum);
			}else{
				paraToStan = "";
			}
		}
		
		if(null != paraToStan && !"".equals(paraToStan)){
			resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, paraToStan, batchNum,application,areaId);
		}
		
		if(null != batchNum && !"".equals(batchNum)){
			pd.put("batchNum", batchNum);
		}
		page.setPd(pd);
		List<MtsVisitType> listMtsVisit = mtsVisitTypeService.findMtsVisitType(new MtsVisitType());
		List<MtsDataSource> listMtsDataSource = mtsDataSourceService.findMtsDataSource(new MtsDataSource());
		List<PageData> listStanData = mtsRecordService.mtsRecordlistPage(page); 
		
		mv.setViewName("mts/standardize/mts_standardize_single_list");
		mv.addObject("listStanData", listStanData);
		mv.addObject("listMtsVisit", listMtsVisit);
		mv.addObject("listMtsDataSource", listMtsDataSource);
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	@RequestMapping(value = "/standardizeDataForZNWD" , produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public Object standardizeDataForZNWD(@RequestBody String toBeStandardStr) {
		String parameters = "";
		String resultStr = "";
		String areaId = "";
		
		if (toBeStandardStr == null || "".equals(toBeStandardStr)) {
			return null;
		}
		JSONObject dealWithStrJSON = JSONObject.fromObject(toBeStandardStr);
		if (dealWithStrJSON.containsKey("parameters")) {
			parameters = dealWithStrJSON.getString("parameters");
		}
		if (dealWithStrJSON.containsKey("areaId")) {
			areaId = dealWithStrJSON.getString("areaId");
		}
		
		try {
			resultStr = standardizeService.standardizeDataForZNWD(parameters, areaId);
		} catch (Exception e) {
			JSONObject statusJSON = null;
			JSONObject resultJSON = null;
			resultJSON = new JSONObject();
			statusJSON = new JSONObject();
			statusJSON.put("status", "2");
			statusJSON.put("description", "标准化过程失败");
			resultJSON.put("result", statusJSON);
			resultStr = resultJSON.toString();
			e.printStackTrace();
		}
		/*JSONObject resultJSON = null;
		resultJSON = new JSONObject();
		resultJSON.put("result", matchRes);*/
		return resultStr;
	}
	
	
	@RequestMapping(value = "/ckEditor")
	public ModelAndView ckEditor(Page page) throws Exception {
		System.out.println("ckEditor==============>>>执行了!");
		ModelAndView mv = this.getModelAndView();
		/*mv.setViewName("system/index/ckEditor");*/
		mv.setViewName("mts/standardize/ckEditor");
		return mv;
	}
	
	public static void main(String[] args){
		String str = "中华人民共和国";

	}
}
