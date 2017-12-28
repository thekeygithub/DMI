package com.ts.controller.mts.dbdata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsDBDataDetail;
import com.ts.entity.mts.MtsDBDataUnstructured;
import com.ts.entity.mts.MtsDBDataUnstructuredDetail;
import com.ts.entity.mts.MtsDBDataUnstructuredRelevance;
import com.ts.entity.mts.MtsDBDataUnstructuredResult;
import com.ts.entity.mts.MtsDataSource;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.dataSource.MtsDataSourceService;
import com.ts.service.mts.dbdata.MTSDBDataService;
import com.ts.service.mts.dbdata.MTSDBDataUnstructuredService;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.matchrule.impl.DataClassService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.threadPool.ThreadPoolTaskForStandardizeData;
import com.ts.threadPool.ThreadPoolTaskForStandardizeDataUnstructured;
import com.ts.util.Const;
import com.ts.util.FileUpload;
import com.ts.util.Jurisdiction;
import com.ts.util.ObjectExcelRead;
import com.ts.util.PageData;
import com.ts.util.PathUtil;
import com.ts.util.RandomUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @类名称: MtsDBDataController
 * @类描述: 数据标准化Controller
 * @作者:夏峰
 * @创建时间:2016年10月8日 下午2:04:26
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/mtsDBDataUnstructured")
public class MtsDBDataUnstructuredController extends BaseController {

	String menuUrl = "mtsData/listMtsDBData.do"; // 菜单地址(权限用)
	@Resource(name = "MTSDBDataService")
	private MTSDBDataService mtsDBDataService;
	
	@Resource(name = "MTSAreaService")
	private MTSAreaService mtsAreaService;
	
	@Resource(name = "StandardizeService")
	private StandardizeService standardizeService;
	
	@Resource(name = "DataClassService")
	private DataClassService dataClassService;
	
	@Resource(name = "MtsDataSourceService")
	private MtsDataSourceService mtsDataSourceService;
	
	@Resource(name = "DataTypeService")
	private DataTypeManger dataTypeService;
	
	@Resource(name = "MTSDBDataUnstructuredService")
	private MTSDBDataUnstructuredService mtsDBDataUnstructuredService;
	
	@Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Resource(name = "mapConfigService")
	private MapConfigService mapConfigService;
	
	/**
	 * 显示数据接口列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsDBDataUnstructured")
	public ModelAndView listMtsDBDataUnstructured(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String FLAG = pd.getString("FLAG");
		String startDate = pd.getString("startDate");
		String endDate = pd.getString("endDate");
		
		if (null != FLAG && !"".equals(FLAG)) {
			pd.put("FLAG", FLAG.trim());
		}
		if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
			pd.put("startDate", startDate + " 00:00:00");
			pd.put("endDate", endDate + " 23:59:59");
		}
		page.setPd(pd);
		List<PageData> listMtsDBDataUnstructured =  mtsDBDataUnstructuredService.mtsDBDataUnstructuredlistPage(page);
		List<MtsDataSource> listDataSource =  mtsDataSourceService.findMtsDataSource(new MtsDataSource());
		mv.setViewName("mts/dbdataUnstructured/mts_dbdata_unstructured_list");
		mv.addObject("listMtsDBDataUnstructured", listMtsDBDataUnstructured);
		mv.addObject("listDataSource", listDataSource);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value = "/goUploadExcelUnstructured")
	public ModelAndView goUploadExcelUnstructured() throws Exception {
		ModelAndView mv = this.getModelAndView();
		List<MtsArea> listMtsArea = mtsAreaService.findMtsArea(new MtsArea());
		List<MtsDataSource> listDataSource =  mtsDataSourceService.findMtsDataSource(new MtsDataSource());
		String batchNum = RandomUtil.getRandomId();
		mv.addObject("batchNum", batchNum);
		mv.addObject("listMtsArea", listMtsArea);
		mv.addObject("listDataSource", listDataSource);
		mv.setViewName("mts/dbdataUnstructured/uploadexcelUnstructured");
		return mv;
	}
	
	@RequestMapping(value = "/readExcelUnstructured")
	public ModelAndView readExcelUnstructured(@RequestParam(value = "excel", required = false) MultipartFile file,
			String EXPORT_NAME,String AREA,String BATCH_NUM,String DATA_SOURCE_CODE,String classcode,String typecode) throws Exception {
		ModelAndView mv = this.getModelAndView();
		
		MtsDBDataUnstructured mtsDBDataUnstructured = new MtsDBDataUnstructured();
		String DATA_UNSTRUCTURED_ID = RandomUtil.getRandomId();
		mtsDBDataUnstructured.setAREA_CODE(AREA);
		mtsDBDataUnstructured.setBATCH_NUM(BATCH_NUM);
		mtsDBDataUnstructured.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
		mtsDBDataUnstructured.setIMPORT_NAME(EXPORT_NAME);
		mtsDBDataUnstructured.setOPERATE_TIME(new Date());
		mtsDBDataUnstructured.setSTATUS("0");
		mtsDBDataUnstructured.setDATA_SOURCE_CODE(DATA_SOURCE_CODE);
		mtsDBDataUnstructuredService.addMtsDBDataUnstructured(mtsDBDataUnstructured);
		
		MtsDBDataDetail mtsDBDataDetail;
		
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE; // 文件上传路径
			String fileName = FileUpload.fileUp(file, filePath, "userexcel"); // 执行上传
			List<PageData> listPd = (List) ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 0, 0, 0); // 执行读EXCEL操作,读出的数据导入List
			// 2:从第3行开始；0:从第A列开始；0:第0个sheet
			/* 存入数据库操作====================================== */
			/**
			 * var0 :ID var1 :标化类型 var2 :原始数据ID var3 :原始数据名称 var4 :大串
			 */
			//
			if(listPd != null && listPd.size() > 0){
				@SuppressWarnings("unused")
				int columnsNumber = 0;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_1 = null ;
				String PROPERTY_ID_1 = null ;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_2 = null ;
				String PROPERTY_ID_2 = null ;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_3 = null ;
				String PROPERTY_ID_3 = null ;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_4 = null ;
				String PROPERTY_ID_4 = null ;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_5 = null ;
				String PROPERTY_ID_5 = null ;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_6 = null ;
				String PROPERTY_ID_6 = null ;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_7 = null ;
				String PROPERTY_ID_7 = null ;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_8 = null ;
				String PROPERTY_ID_8 = null ;
				@SuppressWarnings("unused")
				String PROPERTY_NAME_9 = null ;
				String PROPERTY_ID_9 = null ;
				String ORIG_DATA = "";
				for (int i = 0; i < listPd.size(); i++) {
					if(i == 0){
						PROPERTY_NAME_1 = listPd.get(i).getString("var1");
						PROPERTY_ID_1 = RandomUtil.getRandomId();
						PROPERTY_NAME_2 = listPd.get(i).getString("var2");
						PROPERTY_ID_2 = RandomUtil.getRandomId();
						PROPERTY_NAME_3 = listPd.get(i).getString("var3");
						PROPERTY_ID_3 = RandomUtil.getRandomId();
						PROPERTY_NAME_4 = listPd.get(i).getString("var4");
						PROPERTY_ID_4 = RandomUtil.getRandomId();
						PROPERTY_NAME_5 = listPd.get(i).getString("var5");
						PROPERTY_ID_5 = RandomUtil.getRandomId();
						PROPERTY_NAME_6 = listPd.get(i).getString("var6");
						PROPERTY_ID_6 = RandomUtil.getRandomId();
						PROPERTY_NAME_7 = listPd.get(i).getString("var7");
						PROPERTY_ID_7 = RandomUtil.getRandomId();
						PROPERTY_NAME_8 = listPd.get(i).getString("var8");
						PROPERTY_ID_8 = RandomUtil.getRandomId();
						PROPERTY_NAME_9 = listPd.get(i).getString("var9");
						PROPERTY_ID_9 = RandomUtil.getRandomId();

					}else{
						MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance = new MtsDBDataUnstructuredRelevance();
						String DATA_UNSTRUCTURED_RELEVANCE_ID = RandomUtil.getRandomId();
						mtsDBDataUnstructuredRelevance.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredRelevance.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredRelevance.setORDER_NUM(i + "");
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_1 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_1.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_1.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_1.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_1.setORDER_NUM("1");
						mtsDBDataUnstructuredDetail_1.setPROPERTY_CONTENT(listPd.get(i).getString("var1"));
						mtsDBDataUnstructuredDetail_1.setPROPERTY_ID(PROPERTY_ID_1);
						mtsDBDataUnstructuredDetail_1.setPROPERTY_NAME(PROPERTY_NAME_1);
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_2 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_2.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_2.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_2.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_2.setORDER_NUM("2");
						mtsDBDataUnstructuredDetail_2.setPROPERTY_CONTENT(listPd.get(i).getString("var2"));
						mtsDBDataUnstructuredDetail_2.setPROPERTY_ID(PROPERTY_ID_2);
						mtsDBDataUnstructuredDetail_2.setPROPERTY_NAME(PROPERTY_NAME_2);
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_3 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_3.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_3.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_3.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_3.setORDER_NUM("3");
						mtsDBDataUnstructuredDetail_3.setPROPERTY_CONTENT(listPd.get(i).getString("var3"));
						mtsDBDataUnstructuredDetail_3.setPROPERTY_ID(PROPERTY_ID_3);
						mtsDBDataUnstructuredDetail_3.setPROPERTY_NAME(PROPERTY_NAME_3);
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_4 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_4.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_4.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_4.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_4.setORDER_NUM("4");
						mtsDBDataUnstructuredDetail_4.setPROPERTY_CONTENT(listPd.get(i).getString("var4"));
						mtsDBDataUnstructuredDetail_4.setPROPERTY_ID(PROPERTY_ID_4);
						mtsDBDataUnstructuredDetail_4.setPROPERTY_NAME(PROPERTY_NAME_4);
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_5 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_5.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_5.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_5.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_5.setORDER_NUM("5");
						mtsDBDataUnstructuredDetail_5.setPROPERTY_CONTENT(listPd.get(i).getString("var5"));
						mtsDBDataUnstructuredDetail_5.setPROPERTY_ID(PROPERTY_ID_5);
						mtsDBDataUnstructuredDetail_5.setPROPERTY_NAME(PROPERTY_NAME_5);
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_6 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_6.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_6.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_6.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_6.setORDER_NUM("6");
						mtsDBDataUnstructuredDetail_6.setPROPERTY_CONTENT(listPd.get(i).getString("var6"));
						mtsDBDataUnstructuredDetail_6.setPROPERTY_ID(PROPERTY_ID_6);
						mtsDBDataUnstructuredDetail_6.setPROPERTY_NAME(PROPERTY_NAME_6);
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_7 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_7.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_7.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_7.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_7.setORDER_NUM("7");
						mtsDBDataUnstructuredDetail_7.setPROPERTY_CONTENT(listPd.get(i).getString("var7"));
						mtsDBDataUnstructuredDetail_7.setPROPERTY_ID(PROPERTY_ID_7);
						mtsDBDataUnstructuredDetail_7.setPROPERTY_NAME(PROPERTY_NAME_7);
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_8 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_8.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_8.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_8.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_8.setORDER_NUM("8");
						mtsDBDataUnstructuredDetail_8.setPROPERTY_CONTENT(listPd.get(i).getString("var8"));
						mtsDBDataUnstructuredDetail_8.setPROPERTY_ID(PROPERTY_ID_8);
						mtsDBDataUnstructuredDetail_8.setPROPERTY_NAME(PROPERTY_NAME_8);
						
						MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail_9 = new MtsDBDataUnstructuredDetail();
						mtsDBDataUnstructuredDetail_9.setDATA_UNSTRUCTURED_DETAIL_ID(RandomUtil.getRandomId());
						mtsDBDataUnstructuredDetail_9.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredDetail_9.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
						mtsDBDataUnstructuredDetail_9.setORDER_NUM("9");
						mtsDBDataUnstructuredDetail_9.setPROPERTY_CONTENT(listPd.get(i).getString("var9"));
						mtsDBDataUnstructuredDetail_9.setPROPERTY_ID(PROPERTY_ID_9);
						mtsDBDataUnstructuredDetail_9.setPROPERTY_NAME(PROPERTY_NAME_9);
						
						
						ORIG_DATA = PROPERTY_NAME_1 + listPd.get(i).getString("var1") 
								  + PROPERTY_NAME_2 + listPd.get(i).getString("var2") 
								  + PROPERTY_NAME_3 + listPd.get(i).getString("var3") 
								  + PROPERTY_NAME_4 + listPd.get(i).getString("var4") 
								  + PROPERTY_NAME_5 + listPd.get(i).getString("var5") 
								  + PROPERTY_NAME_6 + listPd.get(i).getString("var6") 
								  + PROPERTY_NAME_7 + listPd.get(i).getString("var7") 
								  + PROPERTY_NAME_8 + listPd.get(i).getString("var8") 
								  + PROPERTY_NAME_9 + listPd.get(i).getString("var9") ;
						mtsDBDataUnstructuredRelevance.setORIG_DATA(ORIG_DATA);
						
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredRelevance(mtsDBDataUnstructuredRelevance);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_1);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_2);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_3);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_4);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_5);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_6);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_7);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_8);
						mtsDBDataUnstructuredService.addMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail_9);
					}
				}
			}
			/* 存入数据库操作====================================== */
			mv.addObject("msg", "success");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	
	@RequestMapping(value = "/standardizeDataUnstructured")
	public void standardizeDataUnstructured(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除matchRule");
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_UNSTRUCTURED_ID = pd.getString("DATA_UNSTRUCTURED_ID");
		
		if(null == DATA_UNSTRUCTURED_ID || "".equals(DATA_UNSTRUCTURED_ID)){
			out.write("success");
			out.close();
		}
		mapConfigService.getMatchRuleCacheMap();
		MtsDBDataUnstructuredResult unstrResult = new MtsDBDataUnstructuredResult();
		unstrResult.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
		mtsDBDataUnstructuredService.deleteMtsDBDataUnstructuredResult(unstrResult);
		
		MtsDBDataUnstructured mtsDBDataUnstructured = new MtsDBDataUnstructured();
		mtsDBDataUnstructured.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
		List<MtsDBDataUnstructured> dataUnstructuredList = mtsDBDataUnstructuredService.findMtsDBDataUnstructured(mtsDBDataUnstructured);
		if(dataUnstructuredList != null && dataUnstructuredList.size() == 1){
			MtsDBDataUnstructured dataUnstructured = dataUnstructuredList.get(0);
			if(dataUnstructured != null){
				String AREA_CODE = dataUnstructured.getAREA_CODE();
				if(null != AREA_CODE && !"".equals(AREA_CODE)){
					MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance = new MtsDBDataUnstructuredRelevance();
					mtsDBDataUnstructuredRelevance.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
					List<MtsDBDataUnstructuredRelevance> dataUnstructuredRelevanceList = mtsDBDataUnstructuredService.findMtsDBDataUnstructuredRelevance(mtsDBDataUnstructuredRelevance);
					if(dataUnstructuredRelevanceList != null && dataUnstructuredRelevanceList.size() > 0){
						for(int i = 0;i < dataUnstructuredRelevanceList.size();i ++){
							MtsDBDataUnstructuredRelevance relevance = dataUnstructuredRelevanceList.get(i);
							if(relevance != null){
								MtsDBDataUnstructuredDetail dataUnstructuredDetail = new MtsDBDataUnstructuredDetail();
								dataUnstructuredDetail.setDATA_UNSTRUCTURED_RELEVANCE_ID(relevance.getDATA_UNSTRUCTURED_RELEVANCE_ID());
								List<MtsDBDataUnstructuredDetail> dataUnstructuredDetailList = mtsDBDataUnstructuredService.findMtsDBDataUnstructuredDetail(dataUnstructuredDetail);
								if(dataUnstructuredDetailList != null && dataUnstructuredDetailList.size() > 0){
									
									CountDownLatch latch = new CountDownLatch(dataUnstructuredDetailList.size());
									
									for(int j = 0;j < dataUnstructuredDetailList.size();j ++){
										MtsDBDataUnstructuredDetail unstructuredDetail = dataUnstructuredDetailList.get(j);
										String PROPERTY_CONTENT = unstructuredDetail.getPROPERTY_CONTENT();
										String DATA_UNSTRUCTURED_DETAIL_ID = unstructuredDetail.getDATA_UNSTRUCTURED_DETAIL_ID();
										String PROPERTY_ID = unstructuredDetail.getPROPERTY_ID();
										if(null != PROPERTY_CONTENT && !"".equals(PROPERTY_CONTENT) && null != AREA_CODE && !"".equals(AREA_CODE)){
											
											
											FutureTask<String> futureTask = new FutureTask<String>(new ThreadPoolTaskForStandardizeDataUnstructured(latch,dataUnstructured,DATA_UNSTRUCTURED_DETAIL_ID,DATA_UNSTRUCTURED_ID,PROPERTY_ID,PROPERTY_CONTENT,AREA_CODE));
											threadPoolTaskExecutor.execute(futureTask);
											
											
											/*String resultStr = standardizeService.standardizeDataForZNWD(PROPERTY_CONTENT, AREA_CODE);
											System.out.println("resultStr====>>>"+resultStr);
											if(null != resultStr && !"".equals(resultStr)){
												JSONObject dealWithStrJSON = JSONObject.fromObject(resultStr);
												String result = "";
												if (dealWithStrJSON.containsKey("result")) {
													result = dealWithStrJSON.getString("result");
												}
												if(null != result && !"".equals(result)){
													JSONObject dealWithResultJSON = JSONObject.fromObject(result);
													String NlpResult = "";
													
													if (dealWithResultJSON.containsKey("NlpResult")) {
														NlpResult = dealWithResultJSON.getString("NlpResult");
													}
													if(null != NlpResult && !"".equals(NlpResult)){
														System.out.println("NlpResult===>>>"+NlpResult);
														JSONObject value = JSONObject.fromObject(NlpResult);
														int index = 0;
														for(Object map : value.keySet()){
															String NLP_RESULT = map.toString();
															String MTS_RESULT = (String) value.get(map);
															MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult = new MtsDBDataUnstructuredResult();
															String DATA_UNSTRUCTURED_RESULT_ID = RandomUtil.getRandomId();
															mtsDBDataUnstructuredResult.setDATA_UNSTRUCTURED_RESULT_ID(DATA_UNSTRUCTURED_RESULT_ID);
															mtsDBDataUnstructuredResult.setNLP_RESULT(NLP_RESULT);
															mtsDBDataUnstructuredResult.setDATA_UNSTRUCTURED_DETAIL_ID(DATA_UNSTRUCTURED_DETAIL_ID);
															mtsDBDataUnstructuredResult.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
															mtsDBDataUnstructuredResult.setORDER_NUM(index + "");
															mtsDBDataUnstructuredResult.setPROPERTY_ID(PROPERTY_ID);
															String STANDARD_RESULT = "0";
															if(MTS_RESULT != null && !"".equals(MTS_RESULT)){
																if("none".equals(MTS_RESULT)){
																	STANDARD_RESULT = "0";
																}else{
																	STANDARD_RESULT = "1";
																	String[] mtsResList = MTS_RESULT.split("@#&");
																	if(mtsResList != null && mtsResList.length > 0){
																		if(mtsResList.length == 1){
																			mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
																		}
																		if(mtsResList.length == 2){
																			mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
																			mtsDBDataUnstructuredResult.setRESULT_2(mtsResList[1]);
																		}
																		if(mtsResList.length == 3){
																			mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
																			mtsDBDataUnstructuredResult.setRESULT_2(mtsResList[1]);
																			mtsDBDataUnstructuredResult.setRESULT_3(mtsResList[2]);
																		}
																		if(mtsResList.length == 4){
																			mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
																			mtsDBDataUnstructuredResult.setRESULT_2(mtsResList[1]);
																			mtsDBDataUnstructuredResult.setRESULT_3(mtsResList[2]);
																			mtsDBDataUnstructuredResult.setRESULT_4(mtsResList[3]);
																		}
																		if(mtsResList.length == 5){
																			mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
																			mtsDBDataUnstructuredResult.setRESULT_2(mtsResList[1]);
																			mtsDBDataUnstructuredResult.setRESULT_3(mtsResList[2]);
																			mtsDBDataUnstructuredResult.setRESULT_4(mtsResList[3]);
																			mtsDBDataUnstructuredResult.setRESULT_5(mtsResList[4]);
																		}
																	}
																}
																mtsDBDataUnstructuredResult.setSTANDARD_RESULT(STANDARD_RESULT);
																mtsDBDataUnstructuredService.addMtsDBDataUnstructuredResult(mtsDBDataUnstructuredResult);
																dataUnstructured.setSTATUS("1");
																mtsDBDataUnstructuredService.editMtsDBDataUnstructured(dataUnstructured);
															}
														}
													}
												}
											}*/
										}
										latch.await();
									}
								}
							}
						}
					}
				}
			}
		}
		out.write("success");
		out.close();
	}
	
	/**
	 * 删除匹配规则
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delMtsDBDataUnstructured")
	public void delMtsDBDataUnstructured(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除DBData");
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_UNSTRUCTURED_ID = pd.getString("DATA_UNSTRUCTURED_ID");
		
		if(null != DATA_UNSTRUCTURED_ID && !"".equals(DATA_UNSTRUCTURED_ID)){
			
			MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance = new MtsDBDataUnstructuredRelevance();
			mtsDBDataUnstructuredRelevance.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
			
			MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult = new MtsDBDataUnstructuredResult();
			mtsDBDataUnstructuredResult.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
			
			MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail = new MtsDBDataUnstructuredDetail();
			mtsDBDataUnstructuredDetail.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
			
			MtsDBDataUnstructured mtsDBDataUnstructured = new MtsDBDataUnstructured();
			mtsDBDataUnstructured.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
			
			mtsDBDataUnstructuredService.deleteMtsDBDataUnstructuredResult(mtsDBDataUnstructuredResult);
			mtsDBDataUnstructuredService.deleteMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail);
			mtsDBDataUnstructuredService.deleteMtsDBDataUnstructuredRelevance(mtsDBDataUnstructuredRelevance);
			mtsDBDataUnstructuredService.deleteMtsDBDataUnstructured(mtsDBDataUnstructured);
		}
		
		out.write("success");
		out.close();
	}
	
	@RequestMapping(value = "/listMtsDBDataDetailUnstructured")
	public ModelAndView listMtsDBDataDetailUnstructured(Page page,HttpServletRequest request) throws Exception {
		String DATA_UNSTRUCTURED_ID = request.getParameter("DATA_UNSTRUCTURED_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/dbdataUnstructured/mts_dbdata_unstructured_detail_list");
		mv.addObject("DATA_UNSTRUCTURED_ID", DATA_UNSTRUCTURED_ID);
		mv.addObject("BATCH_NUM", BATCH_NUM);
		mv.addObject("STATUS", STATUS);
		return mv;
	}
	
	@RequestMapping(value = "/listMtsDBDataResultUnstructured")
	public ModelAndView listMtsDBDataResultUnstructured(Page page,HttpServletRequest request) throws Exception {
		String DATA_UNSTRUCTURED_ID = request.getParameter("DATA_UNSTRUCTURED_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		if (null != DATA_UNSTRUCTURED_ID && !"".equals(DATA_UNSTRUCTURED_ID)) {
			pd.put("DATA_UNSTRUCTURED_ID", DATA_UNSTRUCTURED_ID);
		}
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM);
		}
		page.setPd(pd);
		List<PageData> listUnstructuredRelevance = null;
		if(null != DATA_UNSTRUCTURED_ID && !"".equals(DATA_UNSTRUCTURED_ID)){
			listUnstructuredRelevance = mtsDBDataUnstructuredService.mtsDBDataUnstructuredRelevancelistPage(page);
			if(listUnstructuredRelevance != null && listUnstructuredRelevance.size() > 0){
				for(int i = 0;i < listUnstructuredRelevance.size();i ++){
					PageData urpd = listUnstructuredRelevance.get(i);
					String DATA_UNSTRUCTURED_RELEVANCE_ID = urpd.getString("DATA_UNSTRUCTURED_RELEVANCE_ID");
					MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail = new MtsDBDataUnstructuredDetail();
					mtsDBDataUnstructuredDetail.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
					List<MtsDBDataUnstructuredDetail> unstructuredDetailList = mtsDBDataUnstructuredService.findMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail);
					if(unstructuredDetailList != null && unstructuredDetailList.size() > 0){
						String dname = "";
						String nlpStr = "";
						String mtsStr = "";
						for(int k = 0;k < unstructuredDetailList.size();k ++){
							MtsDBDataUnstructuredDetail mdbud = unstructuredDetailList.get(k);
							if(null != mdbud.getPROPERTY_NAME() && !"".equals(mdbud.getPROPERTY_NAME()) && null != mdbud.getPROPERTY_CONTENT() && !"".equals(mdbud.getPROPERTY_CONTENT())){
								dname += "<span style='color:red'>" + mdbud.getPROPERTY_NAME() + ":</span>" + mdbud.getPROPERTY_CONTENT() + "</br>";
							}
							
							
							if(null != mdbud.getPROPERTY_NAME() && !"".equals(mdbud.getPROPERTY_NAME())){
								nlpStr += "<span style='color:red'>" +mdbud.getPROPERTY_NAME() + ":</span>" + "</br>";
								mtsStr += "<span style='color:red'>" +mdbud.getPROPERTY_NAME() + ":</span>" + "</br>";
							}
							
							MtsDBDataUnstructuredResult mdurs = new MtsDBDataUnstructuredResult();
							mdurs.setDATA_UNSTRUCTURED_DETAIL_ID(mdbud.getDATA_UNSTRUCTURED_DETAIL_ID());
							List<MtsDBDataUnstructuredResult> ururLisr = mtsDBDataUnstructuredService.findMtsDBDataUnstructuredResult(mdurs);
							
							if(ururLisr != null && ururLisr.size() > 0){
								String result1Str = "";
								String result2Str = "";
								String result3Str = "";
								for(int p = 0;p < ururLisr.size();p ++){
									MtsDBDataUnstructuredResult utl = ururLisr.get(p);
									nlpStr += utl.getNLP_RESULT() + "</br>";
									
									result1Str = utl.getRESULT_1();
									result2Str = utl.getRESULT_2();
									result3Str = utl.getRESULT_3();
									if( null == result1Str && null == result2Str && null == result3Str){
										mtsStr += "未匹配</br>";
									}else {
										if(null == result1Str){
											result1Str = "无";
										}
										if(null == result2Str){
											result2Str = "无";
										}
										if(null == result3Str){
											result3Str = "无";
										}
										mtsStr += result1Str + "【" + result2Str +"】" + "【" + result3Str +"】" + "</br>";
									}
									
								}
							}
						}
						urpd.put("mtsStr", mtsStr);
						urpd.put("nlpStr", nlpStr);
						urpd.put("dname", dname);
					}
				}
			}
		}
		mv.setViewName("mts/dbdataUnstructured/mts_dbdata_unstructured_result_list");
		mv.addObject("listUnstructuredRelevance", listUnstructuredRelevance);
		int pcount = 0;
		if(page.getCurrentPage() > 0){
			pcount = (page.getCurrentPage()-1)*page.getShowCount();
		}else{
			pcount = 0;
		}
		mv.addObject("pcount", pcount);
		mv.addObject("pd", pd);
		mv.addObject("STATUS", STATUS);
		mv.addObject("DATA_UNSTRUCTURED_ID", DATA_UNSTRUCTURED_ID);
		mv.addObject("BATCH_NUM", BATCH_NUM);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "/exportExcelUnstructured", produces = "application/json;charset=UTF-8")
	public ModelAndView exportExcelUnstructured(Page page,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String DATA_UNSTRUCTURED_ID = request.getParameter("DATA_UNSTRUCTURED_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");
		PageData pd = new PageData();
		pd = this.getPageData();
		if (null != DATA_UNSTRUCTURED_ID && !"".equals(DATA_UNSTRUCTURED_ID)) {
			pd.put("DATA_UNSTRUCTURED_ID", DATA_UNSTRUCTURED_ID.trim());
		}
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM.trim());
		}
		page.setPd(pd);
		page.setShowCount(9999999);
		
		long startTime=System.currentTimeMillis();   //获取开始时间
		System.out.println("==============查询开始时间================"+startTime);
		List<PageData> listUnstructuredRelevance = mtsDBDataUnstructuredService.mtsDBDataUnstructuredRelevancelistPage(page);
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("==============查询结束时间================"+endTime);
		System.out.println("==============查询方法总耗时： "+(endTime-startTime)+"ms");
		if(listUnstructuredRelevance != null && listUnstructuredRelevance.size() > 0){
			System.out.println("listMtsDBdataDetail.size()==============>>>"+listUnstructuredRelevance.size());
			 // 第一步，创建一个webbook，对应一个Excel文件  
	        HSSFWorkbook wb = new HSSFWorkbook();  
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("数据标准化结果");  
	        
	        sheet.setColumnWidth((short) 1, (short) (35.7 * 150));
	        sheet.setColumnWidth((short) 2, (short) (35.7 * 150));
	        sheet.setColumnWidth((short) 3, (short) (35.7 * 150));
	        sheet.setColumnWidth((short) 4, (short) (35.7 * 150));
	        sheet.setColumnWidth((short) 5, (short) (35.7 * 150));
	        sheet.setColumnWidth((short) 6, (short) (35.7 * 150));
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        HSSFRow row = sheet.createRow((int) 0);  
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	  
	        HSSFCell cell = row.createCell((short) 0);  
	        cell.setCellValue("序号");  
	        cell.setCellStyle(style);  
	        cell = row.createCell((short) 1);  
	        cell.setCellValue("原始数据");  
	        cell.setCellStyle(style);  
	        cell = row.createCell((short) 2);  
	        cell.setCellValue("NLP结果");  
	        cell.setCellStyle(style);  
	        cell = row.createCell((short) 3);  
	        cell.setCellValue("标化结果");  
	        cell.setCellStyle(style);  
	        cell = row.createCell((short) 4);  
	        cell.setCellValue("数据来源");  
	        cell.setCellStyle(style);  
	        cell = row.createCell((short) 5);  
	        cell.setCellValue("更新时间");  
	        cell.setCellStyle(style);  

	        
	        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
	        for(int i = 0;i < listUnstructuredRelevance.size();i ++){
				PageData urpd = listUnstructuredRelevance.get(i);
				String DATA_UNSTRUCTURED_RELEVANCE_ID = urpd.getString("DATA_UNSTRUCTURED_RELEVANCE_ID");
				MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail = new MtsDBDataUnstructuredDetail();
				mtsDBDataUnstructuredDetail.setDATA_UNSTRUCTURED_RELEVANCE_ID(DATA_UNSTRUCTURED_RELEVANCE_ID);
				List<MtsDBDataUnstructuredDetail> unstructuredDetailList = mtsDBDataUnstructuredService.findMtsDBDataUnstructuredDetail(mtsDBDataUnstructuredDetail);
				if(unstructuredDetailList != null && unstructuredDetailList.size() > 0){
					String dname = "";
					String nlpStr = "";
					String mtsStr = "";
					for(int k = 0;k < unstructuredDetailList.size();k ++){
						MtsDBDataUnstructuredDetail mdbud = unstructuredDetailList.get(k);
						if(null != mdbud.getPROPERTY_NAME() && !"".equals(mdbud.getPROPERTY_NAME()) && null != mdbud.getPROPERTY_CONTENT() && !"".equals(mdbud.getPROPERTY_CONTENT())){
							dname +=  mdbud.getPROPERTY_NAME() + ":" + mdbud.getPROPERTY_CONTENT() + "\r\n";
						}
						
						
						if(null != mdbud.getPROPERTY_NAME() && !"".equals(mdbud.getPROPERTY_NAME())){
							nlpStr += mdbud.getPROPERTY_NAME() + ":" + "\r\n";
							mtsStr += mdbud.getPROPERTY_NAME() + ":" + "\r\n";
						}
						
						MtsDBDataUnstructuredResult mdurs = new MtsDBDataUnstructuredResult();
						mdurs.setDATA_UNSTRUCTURED_DETAIL_ID(mdbud.getDATA_UNSTRUCTURED_DETAIL_ID());
						List<MtsDBDataUnstructuredResult> ururLisr = mtsDBDataUnstructuredService.findMtsDBDataUnstructuredResult(mdurs);
						
						if(ururLisr != null && ururLisr.size() > 0){
							String result1Str = "";
							String result2Str = "";
							String result3Str = "";
							for(int p = 0;p < ururLisr.size();p ++){
								MtsDBDataUnstructuredResult utl = ururLisr.get(p);
								nlpStr += utl.getNLP_RESULT() + "\r\n";
								
								result1Str = utl.getRESULT_1();
								result2Str = utl.getRESULT_2();
								result3Str = utl.getRESULT_3();
								if( null == result1Str && null == result2Str && null == result3Str){
									mtsStr += "未匹配\r\n";
								}else {
									if(null == result1Str){
										result1Str = "无";
									}
									if(null == result2Str){
										result2Str = "无";
									}
									if(null == result3Str){
										result3Str = "无";
									}
									mtsStr += result1Str + "【" + result2Str +"】" + "【" + result3Str +"】" + "\r\n";
								}
//								\"b\nc\"
							}
						}
					}
					urpd.put("mtsStr", mtsStr);
					urpd.put("nlpStr", nlpStr);
					urpd.put("dname", dname);
					
					// 第四步，创建单元格，并设置值  
					row = sheet.createRow((int) i + 1);  
		            row.createCell((short) 0).setCellValue((int) i + 1);  
		            row.createCell((short) 1).setCellValue(urpd.getString("dname"));  
		            row.createCell((short) 2).setCellValue(urpd.getString("nlpStr"));  
		            row.createCell((short) 3).setCellValue(urpd.getString("mtsStr"));
		            row.createCell((short) 4).setCellValue(urpd.getString("DESCRIPTION"));
		            row.createCell((short) 5).setCellValue("");
		            System.out.println("第"+i+"条==========>>>"+urpd.getString("dname"));
				}
			}
	        
	        String fileName = "StandardizeUnstructuredResult-"+BATCH_NUM+".xls";
			response.reset();// 不加这一句的话会出现下载错误
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
			response.setContentType("text/x-plain");// 定义输出类型
			
			try {
				ServletOutputStream out = response.getOutputStream();

				String path = System.getProperty("java.io.tmpdir") + "\\StandardizeUnstructuredResult-"+BATCH_NUM+".xls";
				File file = new File(path);
				FileOutputStream fos = new FileOutputStream(file);
				wb.write(fos);
				fos.close();

				FileInputStream fis = new java.io.FileInputStream(file);
				ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(4096);

				byte[] cache = new byte[4096];
				for (int offset = fis.read(cache); offset != -1; offset = fis.read(cache)) {
					byteOutputStream.write(cache, 0, offset);
				}

				byte[] bt = null;
				bt = byteOutputStream.toByteArray();

				out.write(bt);
				out.flush();
				out.close();
				fis.close();
				if (file.exists()) {
					file.delete();
				}
			} catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }  
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/downloadTemplate", produces = "application/json;charset=UTF-8")
	public ModelAndView downloadTemplate(Page page,HttpServletRequest request, HttpServletResponse response) throws Exception {
		 // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("非结构化数据标准化模板");  
        
        sheet.setColumnWidth((short) 1, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 2, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 3, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 4, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 5, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 6, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 7, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 8, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 9, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 10, (short) (35.7 * 150));
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
  
        
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("序号");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 1);  
        cell.setCellValue("基本情况");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("主诉");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 3);  
        cell.setCellValue("现病史");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 4);  
        cell.setCellValue("既往史");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 5);  
        cell.setCellValue("个人史");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 6);  
        cell.setCellValue("家族史");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 7);  
        cell.setCellValue("辅助检查");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 8);  
        cell.setCellValue("初步诊断");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 9);  
        cell.setCellValue("诊疗计划");  
        cell.setCellStyle(style);  
  
		String fileName = "Template.xls";
		response.reset();// 不加这一句的话会出现下载错误
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
		response.setContentType("text/x-plain");// 定义输出类型
		
		try {
			ServletOutputStream out = response.getOutputStream();

			String path = System.getProperty("java.io.tmpdir") + "\\Template.xls";
			File file = new File(path);
			FileOutputStream fos = new FileOutputStream(file);
			wb.write(fos);
			fos.close();

			FileInputStream fis = new java.io.FileInputStream(file);
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(4096);

			byte[] cache = new byte[4096];
			for (int offset = fis.read(cache); offset != -1; offset = fis.read(cache)) {
				byteOutputStream.write(cache, 0, offset);
			}

			byte[] bt = null;
			bt = byteOutputStream.toByteArray();

			out.write(bt);
			out.flush();
			out.close();
			fis.close();
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
		return null;
	}
}
