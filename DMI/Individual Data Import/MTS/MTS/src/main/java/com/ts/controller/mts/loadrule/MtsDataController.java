package com.ts.controller.mts.loadrule;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.ts.controller.base.BaseController;
import com.ts.controller.mts.PrimaryGenerater;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsData;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataType;
import com.ts.entity.mts.MtsDict;
import com.ts.service.mts.MtsConfig;
import com.ts.service.mts.MtsConfigAdd;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.mtsdata.MtsDataManager;
import com.ts.service.system.role.RoleManager;
import com.ts.threadPool.ThreadPoolTaskForDataDetail;
import com.ts.threadPool.ThreadPoolTaskForMtsData;
import com.ts.threadPool.ThreadPoolTaskForReloadMtsData;
import com.ts.util.AESSecurityUtil;
import com.ts.util.CommonUtils;
import com.ts.util.ConnectionPoolManager;
import com.ts.util.Const;
import com.ts.util.DBinit;
import com.ts.util.FileUpload;
import com.ts.util.IConnectionPool;
import com.ts.util.ObjectExcelRead;
import com.ts.util.PageData;
import com.ts.util.PathUtil;
import com.ts.util.QStringUtil;
import com.ts.util.RandomUtil;
import com.ts.util.SeparatorConstant;
import com.ts.util.SpringContextUtils;
import com.ts.util.StringUtil;
import com.ts.util.redis.IRedisUtil;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 
 * @类名称: LoadRuleController
 * @类描述: Redis数据装载规则维护
 * @作者:李巍
 * @创建时间:2016年10月8日 下午2:04:26
 */
@Controller
@RequestMapping(value = "/mtsData")
public class MtsDataController extends BaseController {

	String menuUrl = "mtsData/listMtsData.do"; // 菜单地址(权限用)
	@Resource(name = "mtsDataService")
	private MtsDataManager mtsDataService;
	@Resource(name = "roleService")
	private RoleManager roleService;
	@Resource(name = "redisUtil")
	private IRedisUtil redisUtil;
	@Resource(name = "DataClassService")
	private DataClassManger dataClassService;
	@Resource(name = "DataTypeService")
	private DataTypeManger dataTypeService;
	@Resource(name = "MTSAreaService")
	private MTSAreaService mtsAreaService;
	@Resource(name = "MtsConfigAdd")
	private MtsConfigAdd mc;

	private static Logger logger = Logger.getLogger(MtsDataController.class);
	Long starttime = System.currentTimeMillis();
	ResultSet data_load_rule_rs = null;
	ResultSet data_rs = null;
	ResultSet data_rs2 = null;
	ResultSet data_class_rs = null;
	ResultSet data_type_rs = null;
	Statement stmt = null;
	Statement stmt2 = null;
	Statement stmt3 = null;
	PreparedStatement pstmt = null;
	Connection conn = null;
	private static final String tableMTSDATA = "MTS_DATA";

	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@ResponseBody
	@RequestMapping(value = "/selAllLoadBatch")
	public String selAllLoadBatch() throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List> map = new HashMap<String, List>();
		PageData pd = this.getPageData();
		List<PageData> batchList = mtsDataService.listAllBatch(pd);
		if (batchList != null && batchList.size() > 0) {
			map.put("batchList", batchList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}

	/**
	 * 查询未加载且未被删除的批次号 liuwei
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selLoadBatch")
	public String selLoadBatch() throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List> map = new HashMap<String, List>();
		PageData pd = this.getPageData();
		List<PageData> batchList = mtsDataService.listBatchWithNoLoad(pd);
		if (batchList != null && batchList.size() > 0) {
			map.put("batchList", batchList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}

	/**
	 * ajax查询字典表 liuwei
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selReloadBatch")
	public String selReloadBatch(String areaid) throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List> map = new HashMap<String, List>();
		PageData pd = this.getPageData();
		List<PageData> batchList = mtsDataService.listBatchWithNoDel(pd);
		if (batchList != null && batchList.size() > 0) {
			map.put("batchList", batchList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}

	/**
	 * ajax查询字典表 liuwei
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selClassLoadDict")
	public String selClassLoadDict() throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List> map = new HashMap<String, List>();
		PageData pd = this.getPageData();
		List<PageData> batchList = mtsDataService.listBatchWithNoLoad(pd);
		List<MtsDataType> dataTypeList = dataTypeService.listClassDataTypeById(pd.getString("DATA_CLASS_ID"));// 列出相应聚类下的标化类型
		if (dataTypeList != null && dataTypeList.size() > 0) {
			map.put("listDataType", dataTypeList);
		}
		if (batchList != null && batchList.size() > 0) {
			map.put("batchList", batchList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}

	/**
	 * ajax查询字典表 liuwei
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selClassReloadDict")
	public String selClassReloadDict() throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List> map = new HashMap<String, List>();
		PageData pd = this.getPageData();
		List<PageData> batchList = mtsDataService.listBatchWithNoDel(pd);
		List<MtsDataType> dataTypeList = dataTypeService.listClassDataTypeById(pd.getString("DATA_CLASS_ID"));// 列出相应聚类下的标化类型
		if (dataTypeList != null && dataTypeList.size() > 0) {
			map.put("listDataType", dataTypeList);
		}
		if (batchList != null && batchList.size() > 0) {
			map.put("batchList", batchList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/getDBSize")
	public String getDBSize(String regular) throws Exception {
		ModelAndView mv = this.getModelAndView();
		String host = CommonUtils.getPropValue("redis.properties", "redis.host");
		int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
		String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
		Jedis redis = new Jedis(host, port, 400000);
		redis.auth(pass);
		Set<String> set = redis.keys(regular + "*");
		int size = set.size();
		return String.valueOf(size);
	}

	/**
	 * 显示MTS本体列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsData")
	public ModelAndView listMtsData(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if (!StringUtil.isNotBlank(pd.getString("BATCH_NO"))) {
			String batchNo = mtsDataService.maxBatchNo();
			pd.put("BATCH_NO", batchNo);
		}
		page.setPd(pd);
		// 查最近批次号的数据
		List<PageData> listMtsData = mtsDataService.listMtsData(page); // 列出本体数据列表
		// 区域
		List<MtsArea> areaList = this.mtsAreaService.findMtsArea(new MtsArea());
		List<PageData> batchList = this.mtsDataService.listAllBatch(pd);
		mv.setViewName("mts/mtsdata/mts_data_list");
		mv.addObject("areaList", areaList);
		mv.addObject("batchList", batchList);
		mv.addObject("listMtsData", listMtsData);
		mv.addObject("pd", pd);
		return mv;
	}

	// ============START========Excel本体数据导入====================
	/**
	 * 打开上传EXCEL页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goUploadExcel")
	public ModelAndView goUploadExcel() throws Exception {
		ModelAndView mv = this.getModelAndView();
		// 区域
		List<MtsArea> areaList = this.mtsAreaService.findMtsArea(new MtsArea());
		mv.addObject("areaList", areaList);
		mv.addObject("batchNo",
				PrimaryGenerater.getInstance().generaterNextNumber(this.mtsDataService.maxNowBatchNo()));
		mv.setViewName("mts/mtsdata/uploadexcel");
		return mv;
	}

	/**
	 * 
	 * @方法名称: readExcelAes
	 * @功能描述: Excel导入数据库
	 * @作者:李巍
	 * @创建时间:2017年2月7日 下午1:40:21
	 * @param file
	 *            上传excel文件
	 * @param cityCode
	 *            区域代码
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
//	@RequestMapping(value = "/readExcel")
//	public ModelAndView readExcelAes(@RequestParam(value = "excel", required = false) MultipartFile file, String area,
//			String batchNo) throws Exception {
//		ModelAndView mv = this.getModelAndView();
//		long startTime = System.currentTimeMillis(); // 获取开始时间
//		System.out.println("==============导入excel开始时间================" + startTime);
//		String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
//		MtsData md = new MtsData();
//		if (null != file && !file.isEmpty()) {
//			String taskId = RandomUtil.getRandomId();
//			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE; // 文件上传路径
//			String fileName = FileUpload.fileUp(file, filePath, taskId + "userexcel"); // 执行上传
//			List<PageData> listPd = (List) ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
//			CountDownLatch latch = new CountDownLatch(listPd.size());
//			System.out.println("========excel总数======" + listPd.size());
//			// 2:从第3行开始；0:从第A列开始；0:第0个sheet
//			/* 存入数据库操作====================================== */
//			/**
//			 * var0 :ID var1 :标化类型 var2 :原始数据ID var3 :原始数据名称 var4 :大串
//			 */
//			String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
//			// 批次号
//			for (int i = 0; i < listPd.size(); i++) {
//				md = new MtsData();
//				String ORIG_DATA_STR = listPd.get(i).getString("var3");
//				if ("||".equals(ORIG_DATA_STR)) {// 过滤所有大串里没值的数据
//					continue;
//				}
//				md.setDATA_TYPE_ID(String.valueOf(Double.valueOf(listPd.get(i).getString("var0")).intValue()));
//				// pd.put("DATA_TYPE_ID",
//				// Double.valueOf(listPd.get(i).getString("var0")).intValue());
//				String orig_data_id = listPd.get(i).getString("var1");
//				if (!QStringUtil.isEmpty(orig_data_id)) {
//					String bytes1 = AESSecurityUtil.encrypt(orig_data_id, aresKey);
//					// pd.put("ORIG_DATA_ID", bytes1);
//					md.setORIG_DATA_ID(bytes1);
//				} else {
//					// pd.put("ORIG_DATA_ID", null);
//					md.setORIG_DATA_ID(null);
//				}
//				String orig_data_name = listPd.get(i).getString("var2");
//				if (!QStringUtil.isEmpty(orig_data_name)) {
//					String bytes2 = AESSecurityUtil.encrypt(orig_data_name, aresKey);
//					// pd.put("ORIG_DATA_NAME", bytes2);
//					md.setORIG_DATA_NAME(bytes2);
//				} else {
//					md.setORIG_DATA_NAME(null);
//					// pd.put("ORIG_DATA_NAME", null);
//				}
//				// 数据AES加密
//				if (ORIG_DATA_STR.lastIndexOf("|") == (ORIG_DATA_STR.length() - 1)) {
//					ORIG_DATA_STR += emptyAes;
//				}
//				String[] strs = ORIG_DATA_STR.split("\\|");
//				String mtspass = "";
//				for (int j = 0; j < strs.length; j++) {
//					String bytes = "";
//					if (!emptyAes.equals(strs[j])) {
//						bytes = AESSecurityUtil.encrypt(strs[j], aresKey);
//					} else {
//						bytes = strs[j];
//					}
//					mtspass += bytes + "|";
//				}
//				mtspass = mtspass.substring(0, mtspass.length() - 1);
//				md.setORIG_DATA_STR(mtspass);
//				// pd.put("ORIG_DATA_STR", mtspass);
//				// 诊断标识 1、科室 2、诊断本体 3、诊疗
//				String zl_flag = listPd.get(i).getString("var4");
//				if (!"".equals(zl_flag) && zl_flag != null) {
//					double d = Double.valueOf(zl_flag);
//					md.setZL_FLAG(String.valueOf((int) d));
//					// pd.put("ZL_FLAG", (int) d);
//				}
//				// 区域代码
//				md.setAREA_ID(area);
//				// pd.put("AREA_ID", area);
//				// 批次号
//				md.setBATCH_NO(batchNo);
//				// pd.put("BATCH_NO", batchNo);
//				// 删除标识 0 未删除 、 1 已删除
//				// pd.put("DEL_FLAG", 0);
//				md.setDEL_FLAG("0");
//				// 加载标识 0、未加载 1 已加载
//				// pd.put("LOAD_FLAG", 0);
//				md.setLOAD_FLAG("0");
//				// 导入时间
//				md.setIMP_DATE(new Date());
//				// pd.put("IMP_DATE", new Date());
//				/* 存入数据库操作====================================== */
//				this.mtsDataService.saveMtsData(md);
//			}
//			System.out.println("===================等待所有线程执行完毕123==========================");
////			latch.await();
//			System.out.println("===================执行完毕123==========================");
//			long endTime = System.currentTimeMillis(); // 获取结束时间
//			System.out.println("==============导入excel时间================" + endTime);
//			System.out.println("==============导入excel方法总耗时： " + (endTime - startTime) + "ms");
//			mv.addObject("msg", "success");
//		}
//
//		mv.setViewName("save_result");
//		return mv;
//	}
	
	
	/**
	 * 
	 * @方法名称: readExcelAes
	 * @功能描述: Excel导入数据库
	 * @作者:李巍
	 * @创建时间:2017年2月7日 下午1:40:21
	 * @param file
	 *            上传excel文件
	 * @param cityCode
	 *            区域代码
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/readExcel")
	public ModelAndView readExcelAes(@RequestParam(value = "excel", required = false) MultipartFile file, String area,
			String batchNo) throws Exception {
		ModelAndView mv = this.getModelAndView();
		long startTime=System.currentTimeMillis();   //获取开始时间
		System.out.println("==============导入excel开始时间================"+startTime);
		String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
		MtsData md = new MtsData();
		if (null != file && !file.isEmpty()) {
			String taskId = RandomUtil.getRandomId();
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE; // 文件上传路径
			String fileName = FileUpload.fileUp(file, filePath, taskId+"userexcel"); // 执行上传
			List<PageData> listPd = (List) ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
			CountDownLatch latch = new CountDownLatch(listPd.size());
			System.out.println("========excel总数======"+listPd.size());
			// 2:从第3行开始；0:从第A列开始；0:第0个sheet
			/* 存入数据库操作====================================== */
			/**
			 * var0 :ID var1 :标化类型 var2 :原始数据ID var3 :原始数据名称 var4 :大串
			 */
			String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
			// 批次号
			for (int i = 0; i < listPd.size(); i++) {
				String ORIG_DATA_STR = listPd.get(i).getString("var3");
				if ("||".equals(ORIG_DATA_STR)) {// 过滤所有大串里没值的数据
					continue;
				}
				md.setDATA_TYPE_ID(String.valueOf(Double.valueOf(listPd.get(i).getString("var0")).intValue()));
				//pd.put("DATA_TYPE_ID", Double.valueOf(listPd.get(i).getString("var0")).intValue());
				String orig_data_id = listPd.get(i).getString("var1");
				if (!QStringUtil.isEmpty(orig_data_id)) {
					String bytes1 = AESSecurityUtil.encrypt(orig_data_id, aresKey);
					//pd.put("ORIG_DATA_ID", bytes1);
					md.setORIG_DATA_ID(bytes1);
				} else {
					//pd.put("ORIG_DATA_ID", null);
					md.setORIG_DATA_ID(null);
				}
				String orig_data_name = listPd.get(i).getString("var2");
				if (!QStringUtil.isEmpty(orig_data_name)) {
					String bytes2 = AESSecurityUtil.encrypt(orig_data_name, aresKey);
					//pd.put("ORIG_DATA_NAME", bytes2);
					md.setORIG_DATA_NAME(bytes2);
				} else {
					md.setORIG_DATA_NAME(null);
//					pd.put("ORIG_DATA_NAME", null);
				}
				// 数据AES加密
				if (ORIG_DATA_STR.lastIndexOf("|") == (ORIG_DATA_STR.length() - 1)) {
					ORIG_DATA_STR += emptyAes;
				}
				String[] strs = ORIG_DATA_STR.split("\\|");
				String mtspass = "";
				for (int j = 0; j < strs.length; j++) {
					String bytes = "";
					if (!emptyAes.equals(strs[j])) {
						bytes = AESSecurityUtil.encrypt(strs[j], aresKey);
					} else {
						bytes = strs[j];
					}
					mtspass += bytes + "|";
				}
				mtspass = mtspass.substring(0, mtspass.length() - 1);
				md.setORIG_DATA_STR(mtspass);
//				pd.put("ORIG_DATA_STR", mtspass);
				// 诊断标识 1、科室 2、诊断本体 3、诊疗
				String zl_flag = listPd.get(i).getString("var4");
				if (!"".equals(zl_flag) && zl_flag != null) {
					double d = Double.valueOf(zl_flag);
					md.setZL_FLAG(String.valueOf((int) d));
					//pd.put("ZL_FLAG", (int) d);
				}
				// 区域代码
				md.setAREA_ID(area);
//				pd.put("AREA_ID", area);
				// 批次号
				md.setBATCH_NO(batchNo);
//				pd.put("BATCH_NO", batchNo);
				// 删除标识 0 未删除 、 1 已删除
//				pd.put("DEL_FLAG", 0);
				md.setDEL_FLAG("0");
				// 加载标识 0、未加载 1 已加载
//				pd.put("LOAD_FLAG", 0);
				md.setLOAD_FLAG("0");
				// 导入时间
				md.setIMP_DATE(new Date());
				this.mtsDataService.saveMtsData(md);
//				pd.put("IMP_DATE", new Date());
				/* 存入数据库操作====================================== */
//				FutureTask<String> futureTask = new FutureTask<String>(new ThreadPoolTaskForMtsData(latch,md));
//				threadPoolTaskExecutor.execute(futureTask);
			}
			System.out.println("===================等待所有线程执行完毕123==========================");
//			latch.await();
			System.out.println("===================执行完毕123==========================");
			long endTime=System.currentTimeMillis(); //获取结束时间
			System.out.println("==============导入excel时间================"+endTime);
			System.out.println("==============导入excel方法总耗时： "+(endTime-startTime)+"ms");
			mv.addObject("msg", "success");
		}

		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 
	 * @方法名称: readExcelAes
	 * @功能描述: Excel导入数据库
	 * @作者:李巍
	 * @创建时间:2017年2月7日 下午1:40:21
	 * @param file
	 *            上传excel文件
	 * @param cityCode
	 *            区域代码
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
//	@RequestMapping(value = "/readAes")
//	public ModelAndView readAes(String area, String batchNo) throws Exception {
//		ModelAndView mv = this.getModelAndView();
//		batchNo = PrimaryGenerater.getInstance().generaterNextNumber(this.mtsDataService.maxNowBatchNo());
//		long startTime = System.currentTimeMillis(); // 获取开始时间
//		System.out.println("==============导入excel开始时间================" + startTime);
//		String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
//		MtsData md = new MtsData();
//		String taskId = RandomUtil.getRandomId();
//		String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE; // 文件上传路径
//		Page page = new Page();
//		page.setShowCount(Integer.MAX_VALUE);
//		List<PageData> listPd  = this.mtsDataService.listTempMtsData();
//		// List<PageData> listPd = (List)
//		// ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 1, 0, 0);
//		// // 执行读EXCEL操作,读出的数据导入List
//		CountDownLatch latch = new CountDownLatch(listPd.size());
//		System.out.println("========excel总数======" + listPd.size());
//		// 2:从第3行开始；0:从第A列开始；0:第0个sheet
//		/* 存入数据库操作====================================== */
//		/**
//		 * var0 :ID var1 :标化类型 var2 :原始数据ID var3 :原始数据名称 var4 :大串
//		 */
//		String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
//		// 批次号
//		for (int i = 0; i < listPd.size(); i++) {
//			PageData pd   = listPd.get(i);
//			// pd.put("IMP_DATE", new Date());
//			/* 存入数据库操作====================================== */
//			//mtsDataService.saveMtsData(md);
//			FutureTask<String> futureTask = new FutureTask<String>(new ThreadPoolTaskForMtsData(latch, pd,batchNo));
//			threadPoolTaskExecutor.execute(futureTask);
//		}
//		logger.info("===================等待所有线程执行完毕123==========================");
//		latch.await();
//		logger.info("===================执行完毕123==========================");
//		long endTime = System.currentTimeMillis(); // 获取结束时间
//		logger.info("==============导入excel时间================" + endTime);
//		logger.info("==============导入excel方法总耗时： " + (endTime - startTime) + "ms");
//		mv.addObject("msg", "success");
//		mv.setViewName("save_result");
//		return mv;
//	}

	public static void main(String[] args) throws Exception {
		String str1 = "12";
		DecimalFormat df = new DecimalFormat("00");
		String str2 = df.format(Integer.parseInt(str1));
		System.out.println(str2);
		// Map<String, Integer> countMap = new HashMap<String, Integer>();
		// String resultKey = "aa";
		// countMap.put(resultKey, null);
		// if (countMap.get(resultKey) != null) {
		// Integer keycount = (Integer) countMap.get(resultKey);
		// countMap.put(resultKey, keycount == null ? 0 : ++keycount);
		// } else {
		// countMap.put(resultKey, 1);
		// }
		// String orig_data_str = "111.1111.0";
		// int pointindex = orig_data_str.lastIndexOf(".");
		// if(pointindex !=-1 &&
		// "0".equals(orig_data_str.substring(pointindex+1))){
		// orig_data_str = orig_data_str.substring(0, pointindex);
		// }
		// System.out.println(orig_data_str);
		/*
		 * System.out.println(AESSecurityUtil.encrypt(StringUtil.full2Half("冠心病"
		 * ), "VxDksHQiTvQt9MMPtMVXdA=="));
		 * System.out.println(AESSecurityUtil.decrypt(
		 * "5EFkdAAoXCOlIvvnvU1g/ACI9w56H0CyJ9Ai6QQzIJU=",
		 * "VxDksHQiTvQt9MMPtMVXdA=="));
		 */

		/*
		 * String orig_data_str = "113282|107502|101305|30001|1237|779|110|";
		 * if(orig_data_str.lastIndexOf("|") == (orig_data_str.length()-1)){
		 * orig_data_str+="RWn/2++3w3bFqNl1lQxzwA=="; } String[] strs =
		 * orig_data_str.split("\\|"); System.out.println(strs.length);
		 */

		// System.out.println(AESSecurityUtil.encrypt(StringUtil.full2Half("二磷酸果糖"),
		// "VxDksHQiTvQt9MMPtMVXdA=="));

	}

	// ====================END======Excel本体数据导入====================

	// ====================START======Redis数据重载====================
	@RequestMapping(value = "/goDelBatchData")
	public ModelAndView goDelBatchData() throws Exception {
		ModelAndView mv = this.getModelAndView();
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType> dataTypeList = dataTypeService.listAllDataType();// 列出所有标化类型
		// 显示所有未加载的批次
		PageData pd = this.getPageData();
		List<PageData> batchList = this.mtsDataService.listBatchWithNoLoad(pd);
		List<MtsArea> areaList = this.mtsAreaService.findMtsArea(new MtsArea());// 区域
		mv.addObject("areaList", areaList);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("batchList", batchList);
		mv.addObject("areaList", areaList);
		mv.setViewName("mts/mtsdata/del_batch_data");
		return mv;
	}

	/**
	 * 
	 * @方法名称: delBatchData
	 * @功能描述: 按批次删除数据
	 * @作者:李巍
	 * @创建时间:2017年2月13日 下午1:55:55
	 * @return
	 * @throws Exception
	 *             String
	 */
	@RequestMapping(value = "/delBatchData")
	public void delBatchData(PrintWriter out) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		mtsDataService.delBatchData(pd.getString("batchNo"));
		out.write("success");
		out.close();
	}

	/**
	 * 
	 * @方法名称: goReloadRedisData
	 * @功能描述: 进入Redis数据重载页面
	 * @作者:李巍
	 * @创建时间:2017年2月9日 下午1:48:10
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/goReloadRedisData")
	public ModelAndView goReloadRedisData() throws Exception {
		ModelAndView mv = this.getModelAndView();
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType> dataTypeList = dataTypeService.listAllDataType();// 列出所有标化类型
		// 显示所有未加载的批次
		PageData pd = this.getPageData();
		List<PageData> batchNoList = this.mtsDataService.listBatchWithNoDel(pd);
		List<MtsArea> areaList = this.mtsAreaService.findMtsArea(new MtsArea());// 区域
		mv.addObject("areaList", areaList);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("batchNoList", batchNoList);
		mv.addObject("areaList", areaList);
		mv.setViewName("mts/mtsdata/reload_redis_data");
		return mv;
	}

	/**
	 * 
	 * @方法名称: goLoadRedisData
	 * @功能描述: 进入Redis内存数据加载页面
	 * @作者:李巍
	 * @创建时间:2017年3月9日 上午8:58:17
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/goLoadRedisData")
	public ModelAndView goLoadRedisData() throws Exception {
		ModelAndView mv = this.getModelAndView();
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType> dataTypeList = dataTypeService.listAllDataType();// 列出所有标化类型
		// 显示所有未加载的批次
		PageData pd = this.getPageData();
		List<PageData> batchNoList = this.mtsDataService.listBatchWithNoLoad(pd);
		List<MtsArea> areaList = this.mtsAreaService.findMtsArea(new MtsArea());// 区域
		mv.addObject("areaList", areaList);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("batchNoList", batchNoList);
		mv.addObject("areaList", areaList);
		mv.setViewName("mts/mtsdata/load_redis_data");
		return mv;
	}

	/**
	 * 
	 * @方法名称: loadRedisData
	 * @功能描述: 根据筛选条件加载数据
	 * @作者:李巍
	 * @创建时间:2017年5月15日 下午3:11:49
	 * @param out
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/loadRedisData")
	public ModelAndView loadRedisData(PrintWriter out) throws Exception {
		ModelAndView mv = this.getModelAndView();
		try {
			// 重载Redis数据
			PageData pd = this.getPageData();
			// 区域编码
			String cityCode = pd.getString("area");
			// 聚类
			String classCode = pd.getString("classcode");
			// 标化类型
			String typeCode = pd.getString("typecode");
			// 批次号
			String batchNo = pd.getString("batchNo");
			this.excuteLoad(cityCode, classCode, typeCode, batchNo, false, false, true, 0);
			/* 存入数据库操作====================================== */
			mv.addObject("msg", "success");
			// out.write("success");
		} catch (Exception e) {
			mv.addObject("msg", "fail");
			e.printStackTrace();
		}
		mv.setViewName("save_result2");
		return mv;
	}

	/**
	 * 
	 * @方法名称: reloadRedisData
	 * @功能描述: 根据筛选条件 重载数据
	 * @作者:李巍
	 * @创建时间:2017年5月15日 下午3:11:26
	 * @param out
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/reloadRedisData")
	public ModelAndView reloadRedisData(PrintWriter out) throws Exception {
		ModelAndView mv = this.getModelAndView();
		try {
			// 重载Redis数据
			PageData pd = this.getPageData();
			// 区域编码
			String cityCode = pd.getString("area");
			// 聚类
			String classCode = pd.getString("classcode");
			// 标化类型
			String typeCode = pd.getString("typecode");
			// 批次号
			String batchNo = pd.getString("batchNo");
			this.excuteLoad(cityCode, classCode, typeCode, batchNo, false, true, false, 0);
			/* 存入数据库操作====================================== */
			mv.addObject("msg", "success");
			// out.write("success");
		} catch (Exception e) {
			mv.addObject("msg", "fail");
			e.printStackTrace();
		}
		mv.setViewName("save_result2");
		return mv;
	}

	/**
	 * 
	 * @方法名称: reloadAllRedisData
	 * @功能描述: 重载全部数据
	 * @作者:李巍
	 * @创建时间:2017年5月15日 下午3:11:10
	 * @param out
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/reloadAllRedisData")
	public ModelAndView reloadAllRedisData(PrintWriter out) throws Exception {
		ModelAndView mv = this.getModelAndView();
		try {
			// 重载Redis数据
			PageData pd = this.getPageData();
			// 区域编码
			String cityCode = pd.getString("area");
			// 聚类
			String classCode = pd.getString("classcode");
			// 标化类型
			String typeCode = pd.getString("typecode");
			// 批次号
			String batchNo = pd.getString("batchNo");
			this.excuteLoad(null, null, null, null, true, false, false, 0);
			/* 存入数据库操作====================================== */
			mv.addObject("msg", "success");
			// out.write("success");
		} catch (Exception e) {
			mv.addObject("msg", "fail");
			e.printStackTrace();
		}
		mv.setViewName("save_result4");
		return mv;
	}

	private static String formatNum(String num) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(Integer.parseInt(num));
	}
	
	@ResponseBody
	@RequestMapping(value = "/getLoadSize")
	public Integer getLoadSize() throws Exception {
		PageData pd = this.getPageData();
		String delStr = pd.getString("delStr");
		String host = CommonUtils.getPropValue("redis.properties", "redis.host");
		int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
		String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
		Jedis redis = new Jedis(host, port, 400000);
		String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
		String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
		redis.auth(pass);
		Set<String> set = redis.keys(delStr.toString() + "*");
		logger.info("key的数量===" + set.size());
		return  set.size();
	}

	/**
	 * 
	 * @方法名称: excuteLoad
	 * @功能描述:
	 * @作者:李巍
	 * @创建时间:2017年4月27日 上午11:08:50
	 * @param cityCode
	 *            区域代码
	 * @param classCode
	 *            聚类代码
	 * @param typeCode
	 *            标化类型代码
	 * @param batchNo
	 *            批次号
	 * @param isFlush
	 *            是否清空redis库
	 * @param isReload
	 *            是否需要删掉之前的key，重载数据库，
	 * @param isLoadData
	 *            是否需要加载 之前未被加载的数据
	 * @param dbNum
	 *            数据库编号
	 * @throws Exception
	 *             void
	 */
	private void excuteLoad(String cityCode, String classCode, String typeCode, String batchNo, boolean isFlush,
			boolean isReload, boolean isLoadData, int dbNum) throws Exception {
		// 清空redis
		try {
			String host = CommonUtils.getPropValue("redis.properties", "redis.host");
			int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
			String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
			Jedis redis = new Jedis(host, port, 400000);
			String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
			String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
			redis.auth(pass);
			// 是否刷新库
			if (isFlush) {
				redis.flushDB();
			}
			// 切换到哪个库
			if (dbNum > 0) {
				redis.select(dbNum);
			}
			logger.info("========DBSIZE====" + redis.dbSize() + "==========");
			IConnectionPool pool = initDB();
			conn = pool.getConnection();
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();

			Long count = 0L;
			// 装载内存数据规则，从前台配置的装载规则中，查询标准化类型ID

			/**
			 * 加载区分区域 2017.2.8 liwei
			 */
			StringBuffer rulesql = new StringBuffer("SELECT * FROM MTS_LOAD_RULE_DETAIL WHERE 1=1 ");
			StringBuffer delStr = new StringBuffer("");
			// 区域code
			if (StringUtil.isNotBlank(cityCode)) {
				rulesql.append(" AND AREA_ID = " + cityCode);
				// 删掉该区域下的所有数据
				delStr.append(cityCode);
			}
			// 聚类code
			if (StringUtil.isNotBlank(classCode)) {
				classCode = formatNum(classCode);
				rulesql.append(" AND SUBSTRING(KEY_GEN_RULE,2,2) = " + classCode);
				delStr.append("#" + classCode);
			}
			// 标化类型code
			if (StringUtil.isNotBlank(typeCode)) {
				typeCode = formatNum(typeCode);
				rulesql.append(" AND SUBSTRING(KEY_GEN_RULE,5,2) = " + typeCode);
				delStr.append("#" + typeCode);
			}

			// 删除区域下的key
			// 如果是重载，需要先删除之前的key数据
			if (isReload) {
				Set<String> set = redis.keys(delStr.toString() + "*");
				logger.info("需要删除key的数量===" + set.size());
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String keyStr = it.next();
					logger.info("删除的key====" + keyStr);
					redis.del(keyStr);
				}
			}
			data_load_rule_rs = stmt.executeQuery(rulesql.toString());
			logger.info(rulesql);
			while (data_load_rule_rs.next()) {
				String keyRule = data_load_rule_rs.getString("KEY_GEN_RULE");
				String valueRule = data_load_rule_rs.getString("VALUE_GEN_RULE");
				String zlFlag = data_load_rule_rs.getString("ZL_FLAG");
				String loadruleareaid = data_load_rule_rs.getString("AREA_ID");
				if (keyRule.indexOf(SeparatorConstant.dataClassSeparator) != -1) {// 判断是否有聚类
					// 区分聚类code
					/**
					 * 加载区分聚类code 2017.2.8 liwei
					 */
					String data_class_code = null;
					data_class_code = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataClassSeparator) + 1,
							keyRule.indexOf(SeparatorConstant.dataTypeSeparator));
					/**
					 * 加载时区分标化类型code 2017.2.8 liwei
					 */
					String data_type_code = null;
					// 标准化类型ID
					int matchDataTypeId = data_load_rule_rs.getInt("DATA_TYPE_ID");
					data_type_code = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataTypeSeparator) + 1,
							keyRule.indexOf(SeparatorConstant.dataSeparator));
					// 拼装key
					// 校验key规则<<<<<<<<<<<<<<<<<<<<<<<<<多加
					if (keyRule.indexOf(SeparatorConstant.dataSeparator) != -1) {
						// 未被删除的数据
						StringBuffer sql = new StringBuffer(
								"select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR,AREA_ID from " + tableMTSDATA
										+ " where DATA_TYPE_ID = " + matchDataTypeId + " AND DEL_FLAG = 0 ");
						// 如果是第一次加载数据，需要选择未加载过的数据
						if (isLoadData) {
							sql.append(" AND LOAD_FLAG = 0");
						}
						if (StringUtil.isNotBlank(zlFlag)) {
							sql.append(" AND ZL_FLAG = " + zlFlag);
						}
						if (StringUtil.isNotBlank(cityCode)) {
							sql.append(" AND AREA_ID = " + cityCode);
						}
						/**
						 * 2017.2.9 批次号
						 */
						if (StringUtil.isNotBlank(batchNo)) {
							sql.append(" AND BATCH_NO = " + batchNo);
						}
						data_rs = stmt2.executeQuery(sql.toString());
						data_rs.last(); // 移到最后一行
						int rowCount = data_rs.getRow(); // 得到当前行号，也就是记录数
						if (rowCount == 0) {
							continue;
						} else {
							data_rs.beforeFirst();// 如果还要用结果集，就把指针再移到初始化的位置
						}
						// 截取key规则
						String keyline = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator));
						/**
						 * valueline==================================
						 */
						String valueline = valueRule.substring(valueRule.indexOf(SeparatorConstant.dataSeparator));
						StringBuffer dataKey = new StringBuffer();
						StringBuffer dataValue = new StringBuffer();
						List resultlist = resultSetToList(data_rs);
						if ("number".equals(validateRE(keyline))) {// 是数字
							keyline = keyline.substring(1);
							valueline = valueline.substring(1);
							String[] keylines = keyline.split(SeparatorConstant.dataSeparator);
							/**
							 * valuelines==================================
							 */
							String[] valuelines = valueline.split(SeparatorConstant.dataSeparator);
							/**
							 * 循环 MTSDATA 结果集
							 */
							Map<String, String> countMap = new HashMap<String, String>();// 计数器
							Map<String, String> resultMap = new HashMap<String, String>();// 计数器
							for (int i = 0; i < resultlist.size(); i++) {
								Map map = (Map) resultlist.get(i);
								String areaId = (String) map.get("AREA_ID");
								if (StringUtil.isNotBlank(areaId) && StringUtil.isNotBlank(areaId)
										&& !areaId.equals(loadruleareaid)) {
									continue;
								}
								dataKey = new StringBuffer(areaId + SeparatorConstant.datajoinSeparator
										+ data_class_code + SeparatorConstant.datajoinSeparator + data_type_code
										+ SeparatorConstant.datajoinSeparator);
								dataValue = new StringBuffer("");
								String orig_data_str = (String) map.get("ORIG_DATA_STR");
								int data_id = (Integer) map.get("DATA_ID");
								if (orig_data_str.lastIndexOf("|") == (orig_data_str.length() - 1)) {
									orig_data_str += emptyAes;
								}
								logger.info("orig_data_str最后一个竖线位置==" + orig_data_str.lastIndexOf("|") + "=====大串长度-1"
										+ (orig_data_str.length() - 1));
								logger.info("key=====orig_data_str====" + orig_data_str);
								logger.info("key=====orig_data_str拆分长度===="
										+ orig_data_str.split(SeparatorConstant.dataValueSeparator).length);
								logger.info("key=====keylines长度====" + keylines.length);
								logger.info("dataid====" + data_id);
								for (int j = 0; j < keylines.length; j++) {
									String temp_str = orig_data_str.split(SeparatorConstant.dataValueSeparator)[Integer
											.parseInt(keylines[j])];
									if (temp_str != null && !"".equals(temp_str) && !emptyAes.equals(temp_str)) {
										temp_str = StringUtil.full2Half(AESSecurityUtil.decrypt(temp_str, aresKey))
												.toUpperCase().trim();
										temp_str=mc.trimUnicode(temp_str);
										dataKey.append(temp_str + SeparatorConstant.datajoinSeparator);
									}
								}
								// 拼装KEY
								String resultKey = dataKey.toString().substring(0,
										dataKey.length() - SeparatorConstant.datajoinSeparator.length());
								// 拼装VALUE//valueRule
								// @ORIG_DATA_ID@ORIG_DATA_NAME@3
								// VALUE 重症肌无力|脑血管意外||1
								for (int j = 0; j < valuelines.length; j++) {
									dataValue.append(spellValue(SeparatorConstant.dataSeparator + valuelines[j],
											data_id, map, orig_data_str));
								}
								String resultValue = dataValue.substring(0,
										dataValue.length() - SeparatorConstant.datareturnSeparator.length());
								// 所有的key、 解密 半角 大写

								// 拼装redis
								// 药品类需要校验key是否重复,如果重复，本身连带重复的一律删除
								// 聚类为 药品制剂型 ,并且 能够获取到值，那么不加入redis内存，并保存
								if (StringUtil.isNotBlank(resultKey) && StringUtil.isNotBlank(resultValue)
										&& redis.exists(resultKey) && !resultValue.equals(redis.get(resultKey))) {
									countMap.put(resultKey, resultValue);
								} else {
									// key相同，value也相同：如果数据库有此key，先删除key，然后在添加，否则key拼接会出现错误数据
									if (redis.exists(resultKey)) {
										redis.del(resultKey);
									}
									redis.append(resultKey, resultValue);
									count++;
								}
							}
							for (String key : countMap.keySet()) {
								redis.del(key);
							}
						} else if ("word".equals(validateRE(keyline))) {// 是字母
							String queryWord = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator) + 1);
							Map<String, String> countMap = new HashMap<String, String>();
							for (int i = 0; i < resultlist.size(); i++) {
								Map map = (Map) resultlist.get(i);
								String areaId = (String) map.get("AREA_ID");
								// 如果本体数据里的区域 与 加载规则中的区域 不一致 不进行加载
								if (StringUtil.isNotBlank(areaId) && StringUtil.isNotBlank(areaId)
										&& !areaId.equals(loadruleareaid)) {
									continue;
								}
								dataKey = new StringBuffer(areaId + SeparatorConstant.datajoinSeparator
										+ data_class_code + SeparatorConstant.datajoinSeparator + data_type_code
										+ SeparatorConstant.datajoinSeparator);
								dataValue = new StringBuffer("");
								// 暂时定为string型
								String keyword = (String) map.get("" + queryWord + "");
								String orig_data_str = (String) map.get("ORIG_DATA_STR");
								int data_id = (Integer) map.get("DATA_ID");
								if (StringUtil.isNotBlank(keyword)) {
									keyword = StringUtil.full2Half(AESSecurityUtil.decrypt(keyword, aresKey))
											.toUpperCase().trim();
									keyword=mc.trimUnicode(keyword);
								} else {
									continue;
								}
								dataKey.append(keyword + SeparatorConstant.datajoinSeparator);
								dataValue.append(spellValue(valueRule, data_id, map, orig_data_str));
								logger.info("dataValue======" + dataValue);
								String dataValueStr = dataValue.toString();
								String resultKey = dataKey.toString().substring(0,
										dataKey.length() - SeparatorConstant.datajoinSeparator.length());
								String resultValue = dataValueStr.substring(0,
										dataValueStr.length() - SeparatorConstant.datareturnSeparator.length());
								// 拼装redis
								// 药品类需要校验key是否重复,如果重复，本身连带重复的一律删除
								// 聚类为 药品制剂型 ,并且 能够获取到值，那么不加入redis内存，并保存
								//// key相同，value不同：如果查到redis内存中有此key，并且 value值与
								// redis 获取的value值 不同 ，删掉key
								if (StringUtil.isNotBlank(resultKey) && StringUtil.isNotBlank(resultValue)
										&& redis.exists(resultKey) && !resultValue.equals(redis.get(resultKey))) {
									countMap.put(resultKey, resultValue);
								} else {
									// key相同，value也相同：如果数据库有此key，先删除key，然后在添加，否则key拼接会出现错误数据
									if (redis.exists(resultKey)) {
										redis.del(resultKey);
									}
									logger.info("redis==key ===" + resultKey + "====resultValue" + resultValue);
									redis.append(resultKey, resultValue);
									count++;
								}
							}
							logger.error("countMap长度===="+countMap.size());
							for (String key : countMap.keySet()) {
								// map.keySet()返回的是所有key的值
								redis.del(key);
							}
						}
					} else {
						logger.info("此规则没有配置匹配原始数据的规则!!~~");
					}
				} else {
					logger.info("此规则没有配置匹配数据聚类的规则!!~~");
				}
			}
			logger.info("count数量===============" + count);
			logger.info("恭喜，数据装载正常结束!!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (data_class_rs != null) {
					data_class_rs.close();
					// data_class_rs = null;
				}
				if (data_type_rs != null) {
					data_type_rs.close();
					// data_type_rs = null;
				}
				if (data_rs != null) {
					data_rs.close();
					// data_rs = null;
				}
				if (data_rs2 != null) {
					data_rs2.close();
					// data_rs2 = null;
				}
				if (data_load_rule_rs != null) {
					data_load_rule_rs.close();
					// data_load_rule_rs = null;
				}
				if (stmt != null) {
					stmt.close();
					// stmt = null;
				}
				if (conn != null) {
					conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.error("Redis数据加载用时======" + formatDuring(System.currentTimeMillis() - starttime));

	}

	/**
	 * 
	 * @方法名称: excuteLoad
	 * @功能描述:
	 * @作者:李巍
	 * @创建时间:2017年4月27日 上午11:08:50
	 * @param cityCode
	 *            区域代码
	 * @param classCode
	 *            聚类代码
	 * @param typeCode
	 *            标化类型代码
	 * @param batchNo
	 *            批次号
	 * @param isFlush
	 *            是否清空redis库
	 * @param isReload
	 *            是否需要删掉之前的key，重载数据库，
	 * @param isLoadData
	 *            是否需要加载 之前未被加载的数据
	 * @param dbNum
	 *            数据库编号
	 * @throws Exception
	 *             void
	 */
	private void excuteThreadLoad(String cityCode, String classCode, String typeCode, String batchNo, boolean isFlush,
			boolean isReload, boolean isLoadData, int dbNum) throws Exception {
		// 清空redis
		try {
			String host = CommonUtils.getPropValue("redis.properties", "redis.host");
			int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
			String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
			Jedis redis = new Jedis(host, port, 400000);
			String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
			String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
			redis.auth(pass);
			// 是否刷新库
			if (isFlush) {
				redis.flushDB();
			}
			// 切换到哪个库
			if (dbNum > 0) {
				redis.select(dbNum);
			}
			logger.info("========DBSIZE====" + redis.dbSize() + "==========");
			IConnectionPool pool = initDB();
			conn = pool.getConnection();
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();

			Long count = 0L;
			// 装载内存数据规则，从前台配置的装载规则中，查询标准化类型ID

			/**
			 * 加载区分区域 2017.2.8 liwei
			 */
			StringBuffer rulesql = new StringBuffer("SELECT * FROM MTS_LOAD_RULE_DETAIL WHERE 1=1 ");
			StringBuffer delStr = new StringBuffer("");
			// 区域code
			if (StringUtil.isNotBlank(cityCode)) {
				rulesql.append(" AND AREA_ID = " + cityCode);
				// 删掉该区域下的所有数据
				delStr.append(cityCode);
			}
			// 聚类code
			if (StringUtil.isNotBlank(classCode)) {
				classCode = formatNum(classCode);
				rulesql.append(" AND SUBSTRING(KEY_GEN_RULE,2,2) = " + classCode);
				delStr.append("#" + classCode);
			}
			// 标化类型code
			if (StringUtil.isNotBlank(typeCode)) {
				typeCode = formatNum(typeCode);
				rulesql.append(" AND SUBSTRING(KEY_GEN_RULE,5,2) = " + typeCode);
				delStr.append("#" + typeCode);
			}

			// 删除区域下的key
			// 如果是重载，需要先删除之前的key数据
			if (isReload) {
				Set<String> set = redis.keys(delStr.toString() + "*");
				logger.info("需要删除key的数量===" + set.size());
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String keyStr = it.next();
					logger.info("删除的key====" + keyStr);
					redis.del(keyStr);
				}
			}
			data_load_rule_rs = stmt.executeQuery(rulesql.toString());
			logger.info(rulesql);
			// 计算 data_load_rule_rs 总记录数
			data_load_rule_rs.last(); // 移到最后一行
			int rowCount = data_load_rule_rs.getRow(); // 得到当前行号，也就是记录数
			// 线程计数器(发令枪)
			CountDownLatch latch = new CountDownLatch(rowCount);
			data_load_rule_rs.beforeFirst();// 如果还要用结果集，就把指针再移到初始化的位置
			Long startTime = System.currentTimeMillis();
			// 参数map
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// paramMap.put("data_load_rule_rs", data_load_rule_rs);
			paramMap.put("redis", redis);
			paramMap.put("cityCode", cityCode);
			paramMap.put("classCode", classCode);
			paramMap.put("typeCode", typeCode);
			paramMap.put("batchNo", batchNo);
			paramMap.put("data_rs", data_rs);
			paramMap.put("emptyAes", emptyAes);
			paramMap.put("aresKey", aresKey);
			paramMap.put("isLoadData", isLoadData);
			paramMap.put("stmt2", stmt2);
			paramMap.put("tableMTSDATA", tableMTSDATA);
			while (data_load_rule_rs.next()) {
				paramMap.put("keyRule", data_load_rule_rs.getString("KEY_GEN_RULE"));
				paramMap.put("valueRule", data_load_rule_rs.getString("VALUE_GEN_RULE"));
				paramMap.put("zlFlag", data_load_rule_rs.getString("ZL_FLAG"));
				paramMap.put("loadruleareaid", data_load_rule_rs.getString("AREA_ID"));
				paramMap.put("matchDataTypeId", data_load_rule_rs.getInt("DATA_TYPE_ID"));
				FutureTask<String> futureTask = new FutureTask<String>(
						new ThreadPoolTaskForReloadMtsData(latch, paramMap));
				threadPoolTaskExecutor.execute(futureTask);
			}
			latch.await();
			logger.info("count数量===============" + count);
			logger.info("恭喜，数据装载正常结束!!");
			logger.error("Redis数据加载用时======" + formatDuring(System.currentTimeMillis() - starttime));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (data_class_rs != null) {
					data_class_rs.close();
					// data_class_rs = null;
				}
				if (data_type_rs != null) {
					data_type_rs.close();
					// data_type_rs = null;
				}
				if (data_rs != null) {
					data_rs.close();
					// data_rs = null;
				}
				if (data_rs2 != null) {
					data_rs2.close();
					// data_rs2 = null;
				}
				if (data_load_rule_rs != null) {
					data_load_rule_rs.close();
					// data_load_rule_rs = null;
				}
				if (stmt != null) {
					stmt.close();
					// stmt = null;
				}
				if (conn != null) {
					conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static IConnectionPool initDB() {
		Thread dbSKThread = DBinit.dbSKInit();
		dbSKThread.start();
		try {
			dbSKThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		IConnectionPool pool;
		pool = ConnectionPoolManager.getInstance().getPool("skPool");
		return pool;
	}

	public static List resultSetToList(ResultSet rs) throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List list = new ArrayList();
		Map rowData = new HashMap();
		while (rs.next()) {
			rowData = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
			// System.out.println("list:" + list.toString());
		}
		return list;
	}

	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds ";
	}

	/**
	 * 
	 * @方法名称: validateRE
	 * @功能描述: 正则匹配数字下标 或是数据名称
	 * @作者:李巍
	 * @创建时间:2016年9月28日 下午2:55:16
	 * @param line
	 * @return String
	 */
	public static String validateRE(String line) {
		String pattern = "(@\\d)+";
		String pattern2 = "(@\\D)+";
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(line);
		// 创建 Pattern 对象
		Pattern r2 = Pattern.compile(pattern2);
		// 现在创建 matcher 对象s
		Matcher m2 = r2.matcher(line);
		if (m.find()) {
			return "number";
		} else if (m2.find()) {
			return "word";
		} else {
			logger.info(line + "===未知匹配类型===!!");
			return "";
		}
	}

	/**
	 * 
	 * @方法名称: getNumberValueOrOldData
	 * @功能描述: 匹配value
	 * @作者:李巍
	 * @创建时间:2016年9月28日 下午2:55:05
	 * @param valueRule
	 * @param map
	 * @param data_id
	 * @param orig_data_str
	 * @return StringBuffer
	 */
	public static StringBuffer getNumberValueOrOldData(String valueRule, Map map, int data_id, String orig_data_str) {
		StringBuffer dataValue = new StringBuffer();
		String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
		try {
			String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
			if (orig_data_str.lastIndexOf("|") == (orig_data_str.length() - 1)) {
				orig_data_str += emptyAes;
			}
			if ("number".equals(validateRE(valueRule))) {
				valueRule = valueRule.substring(1);
				String[] lines = valueRule.split(SeparatorConstant.dataSeparator);
				if (orig_data_str != null) {
					for (int j = 0; j < lines.length; j++) {
						/*
						 * logger.info("value规则的长度=====" + lines.length +
						 * "==========当前索引位置===========" + j + "===========" +
						 * lines[j]); logger.info("orig_data_str===========" +
						 * orig_data_str);
						 * logger.info("orig_data_name===========" +
						 * map.get("ORIG_DATA_NAME"));
						 */
						String[] orig_data_strs = orig_data_str.split(SeparatorConstant.dataValueSeparator);
						// logger.info("orig_data_str数组长度===========" +
						// orig_data_strs.length);
						String tempVal = orig_data_strs[Integer.parseInt(lines[j])];
						tempVal = AESSecurityUtil.decrypt(tempVal, aresKey);
						dataValue.append(tempVal + SeparatorConstant.datareturnSeparator);
						// 拼装value
					}
				}
			}
			if ("word".equals(validateRE(valueRule))) {
				String queryWord = valueRule.substring(1);
				String[] queryWords = queryWord.split(SeparatorConstant.dataSeparator);
				String queryTmpWord = "";
				for (int i = 0; i < queryWords.length; i++) {
					queryTmpWord += queryWords[i] + ",";
				}
				queryTmpWord = queryTmpWord.substring(0, queryTmpWord.length() - 1);
				if (map.size() > 0) {
					String[] queryTmpWords = queryTmpWord.split(",");
					String queryTmpWords2 = "";
					for (int i = 0; i < queryTmpWords.length; i++) {
						queryTmpWords2 = queryTmpWords[i];
						queryWord = (String) map.get("" + queryTmpWords2 + "");
						queryWord = AESSecurityUtil.decrypt(queryWord, aresKey);
						// 拼装value
						dataValue.append(queryWord + SeparatorConstant.datareturnSeparator);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataValue;
	}

	/**
	 * 装载数据聚类key
	 * 
	 * @param matchDataTypeId
	 * @param keyRule
	 * @param stmt
	 * @return
	 * @throws Exception
	 */
	public static String matchClassType(String keyRule, int matchDataTypeId, Connection conn) throws Exception {
		// 去中间表找出标化类型对应的聚类
		Statement stmt = conn.createStatement();
		String queryClassColumn = keyRule.substring(keyRule.indexOf("%") + 1, keyRule.indexOf("$"));
		ResultSet class_relation_rs = stmt.executeQuery("select " + queryClassColumn
				+ " from MTS_CLASS_RELATION r,MTS_DATA_CLASS c where r.DATA_CLASS_ID = c.DATA_CLASS_ID  and  DATA_TYPE_ID="
				+ matchDataTypeId);
		String data_class_code = "";
		try {
			if (class_relation_rs.next()) {
				data_class_code = class_relation_rs.getString("DATA_CLASS_CODE");
			} else {
				logger.info("标准化类型ID为" + matchDataTypeId + "无数据聚类项，请及时配置归属的数据聚类!!~~");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			class_relation_rs.close();
		}
		return data_class_code;
	}

	/*
	 * 装载标化类型key**
	 * 
	 * @param matchDataTypeId
	 * 
	 * @param keyRule
	 * 
	 * @param stmt
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	private static String matchDataType(int matchDataTypeId, String keyRule, Connection conn) throws Exception {
		String queryTypeColumn = keyRule.substring(keyRule.indexOf("$") + 1, keyRule.indexOf("@"));
		Statement stmt = conn.createStatement();
		ResultSet data_type_rs = stmt.executeQuery(
				"select " + queryTypeColumn + " from MTS_DATA_TYPE where DATA_TYPE_ID=" + matchDataTypeId);
		String data_type_code = "";
		try {
			if (data_type_rs.next()) {
				data_type_code = data_type_rs.getString(queryTypeColumn);
				return data_type_code;
			} else {
				logger.info("此匹配规则无需匹配标准化类型配置项!!~~");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data_type_rs.close();
		}
		return data_type_code;
	}

	/**
	 * 
	 * @方法名称: spellValue
	 * @功能描述: 根据value规则 组装VALUE值..包括下标、数据名称、MD5加密。
	 * @作者:李巍
	 * @创建时间:2016年9月26日 下午2:50:19
	 * @param valueRule
	 * @param data_id
	 * @param conn
	 * @return
	 * @throws SQLException
	 *             String
	 */
	public static String spellValue(String valueRule, int data_id, Map map, String orig_data_str) throws SQLException {
		StringBuffer dataValue = new StringBuffer();
		ResultSet data_rs = null;
		// 判断数字下标 还是 原始数据名称
		dataValue = getNumberValueOrOldData(valueRule, map, data_id, orig_data_str);
		// 判断MD5加密,这块也要加一个判断MD5的正则表达式 ???????
		if (valueRule.indexOf(SeparatorConstant.md5ValueSeparator) != -1) {
			valueRule = valueRule.substring(valueRule.indexOf(SeparatorConstant.md5ValueSeparator) + 3,
					valueRule.indexOf(")") + 1);
			if (valueRule.indexOf(SeparatorConstant.md5joinSeparator) != -1) {
				String[] valueRules = valueRule.split(SeparatorConstant.md5joinSeparator);
				for (int i = 0; i < valueRules.length; i++) {
					dataValue.append(getNumberValueOrOldData(valueRules[i], map, data_id, orig_data_str));
				}
			} else {
				dataValue = getNumberValueOrOldData(valueRule, map, data_id, orig_data_str);
			}
		}
		return dataValue.toString();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
	}

}
