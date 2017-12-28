package com.ts.controller.mts.dbdata;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ts.bean.DBDataDetailRes;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsDBData;
import com.ts.entity.mts.MtsDBDataDetail;
import com.ts.entity.mts.MtsDBDataResult;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataSource;
import com.ts.entity.mts.MtsRecordDetail;
import com.ts.entity.mts.MtsRecordInfo;
import com.ts.entity.mts.MtsRecordParameters;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.dataSource.MtsDataSourceService;
import com.ts.service.mts.dbdata.MTSDBDataService;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.matchrule.impl.DataClassService;
import com.ts.service.mts.record.MTSRecordService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.threadPool.ThreadPoolTaskForDataDetail;
import com.ts.threadPool.ThreadPoolTaskForRecord;
import com.ts.threadPool.ThreadPoolTaskForStandardizeData;
import com.ts.threadPool.ThreadPoolTaskForStandardizeDataR;
import com.ts.util.Const;
import com.ts.util.FileUpload;
import com.ts.util.Jurisdiction;
import com.ts.util.ObjectExcelRead;
import com.ts.util.PageData;
import com.ts.util.PathUtil;
import com.ts.util.RandomUtil;
import com.ts.vo.mts.RecordInfoVO;

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
@RequestMapping(value = "/mtsDBData")
public class MtsDBDataController extends BaseController {

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

	@Resource(name = "MTSRecordService")
	private MTSRecordService mtsRecordService;

	@Resource(name = "DataTypeService")
	private DataTypeManger dataTypeService;
	@Resource(name = "mapConfigService")
	private MapConfigService mapConfigService;

	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/**
	 * 显示数据接口列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsDBData")
	public ModelAndView listMtsDBData(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		String FLAG = pd.getString("FLAG");
		String startDate = pd.getString("startDate");
		String endDate = pd.getString("endDate");
		String DB_DATA_TYPE = pd.getString("DB_DATA_TYPE");

		if (null != FLAG && !"".equals(FLAG)) {
			pd.put("FLAG", FLAG.trim());
		}
		if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
			pd.put("startDate", startDate + " 00:00:00");
			pd.put("endDate", endDate + " 23:59:59");
		}
		pd.put("DB_DATA_TYPE", DB_DATA_TYPE);
		page.setPd(pd);
		List<MtsDataSource> listDataSource = mtsDataSourceService.findMtsDataSource(new MtsDataSource());
		List<PageData> listMtsDBdata = mtsDBDataService.mtsDBDatalistPage(page);
		if ("0".equals(DB_DATA_TYPE)) {
			mv.setViewName("mts/dbdata/mts_dbdata_list");
		} else if ("1".equals(DB_DATA_TYPE)) {
			mv.setViewName("mts/dbdata/mts_dbdata_new_list");
		}
		mv.addObject("listMtsDBdata", listMtsDBdata);
		mv.addObject("listDataSource", listDataSource);
		mv.addObject("pd", pd);
		return mv;
	}

	@RequestMapping(value = "/listMtsDBDataDetail")
	public ModelAndView listMtsDBDataDetail(Page page, HttpServletRequest request) throws Exception {
		String DB_DATA_ID = request.getParameter("DB_DATA_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");
		String DB_DATA_TYPE = request.getParameter("DB_DATA_TYPE");
		ModelAndView mv = this.getModelAndView();
		if ("0".equals(DB_DATA_TYPE)) {
			mv.setViewName("mts/dbdata/mts_dbdata_detail_list");
		} else if ("1".equals(DB_DATA_TYPE)) {
			mv.setViewName("mts/dbdata/mts_dbdata_detail_new_list");
		}
		mv.addObject("DB_DATA_ID", DB_DATA_ID);
		mv.addObject("BATCH_NUM", BATCH_NUM);
		mv.addObject("STATUS", STATUS);
		mv.addObject("DB_DATA_TYPE", DB_DATA_TYPE);
		return mv;
	}

	@RequestMapping(value = "/listMtsRecord")
	public ModelAndView listMtsRecord(Page page, HttpServletRequest request) throws Exception {
		String DB_DATA_ID = request.getParameter("DB_DATA_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");

		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/dbdata/mts_dbdata_detail_list");
		mv.addObject("DB_DATA_ID", DB_DATA_ID);
		mv.addObject("BATCH_NUM", BATCH_NUM);
		mv.addObject("STATUS", STATUS);
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/exportExcel", produces = "application/json;charset=UTF-8")
	public ModelAndView exportExcel(Page page, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String DB_DATA_ID = request.getParameter("DB_DATA_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		PageData pd = new PageData();
		pd = this.getPageData();
		if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
			pd.put("DB_DATA_ID", DB_DATA_ID.trim());
		}
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM.trim());
		}
		page.setPd(pd);
		page.setShowCount(9999999);

		long startTime = System.currentTimeMillis(); // 获取开始时间
		System.out.println("==============查询开始时间================" + startTime);
		List<PageData> listMtsDBdataDetail = mtsDBDataService.mtsDBDataDetaillistPage(page);
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("==============查询结束时间================" + endTime);
		System.out.println("==============查询方法总耗时： " + (endTime - startTime) + "ms");
		if (listMtsDBdataDetail != null && listMtsDBdataDetail.size() > 0) {
			System.out.println("listMtsDBdataDetail.size()==============>>>" + listMtsDBdataDetail.size());
			// 第一步，创建一个webbook，对应一个Excel文件
			XSSFWorkbook wb = new XSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = wb.createSheet("数据标准化结果");

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
			sheet.setColumnWidth((short) 11, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 12, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 13, (short) (35.7 * 150));

			sheet.setColumnWidth((short) 14, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 15, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 16, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 17, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 18, (short) (35.7 * 150));
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			Row row = sheet.createRow((int) 0);
			// 第四步，创建单元格，并设置值表头 设置表头居中
			XSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

			Cell cell = row.createCell((short) 0);
			cell.setCellValue("序号");
			cell.setCellStyle(style);
			cell = row.createCell((short) 1);
			cell.setCellValue("原始数据");
			cell.setCellStyle(style);
			cell = row.createCell((short) 2);
			cell.setCellValue("NLP结果");
			cell.setCellStyle(style);
			cell = row.createCell((short) 3);
			cell.setCellValue("切词顺序");
			cell.setCellStyle(style);
			cell = row.createCell((short) 4);
			cell.setCellValue("标准词");
			cell.setCellStyle(style);
			cell = row.createCell((short) 5);
			cell.setCellValue("标注码(主码)");
			cell.setCellStyle(style);
			cell = row.createCell((short) 6);
			cell.setCellValue("标注码(副码)");
			cell.setCellStyle(style);
			cell = row.createCell((short) 7);
			cell.setCellValue("标化类型");
			cell.setCellStyle(style);
			cell = row.createCell((short) 8);
			cell.setCellValue("数据来源");
			cell.setCellStyle(style);
			cell = row.createCell((short) 9);
			cell.setCellValue("更新时间");
			cell.setCellStyle(style);
			cell = row.createCell((short) 10);
			cell.setCellValue("标准结果4");
			cell.setCellStyle(style);
			cell = row.createCell((short) 11);
			cell.setCellValue("标准结果5");
			cell.setCellStyle(style);
			cell = row.createCell((short) 12);
			cell.setCellValue("标准结果6");
			cell.setCellStyle(style);
			cell = row.createCell((short) 13);
			cell.setCellValue("原数据1");
			cell.setCellStyle(style);
			cell = row.createCell((short) 14);
			cell.setCellValue("原数据2");
			cell.setCellStyle(style);
			cell = row.createCell((short) 15);
			cell.setCellValue("原数据3");
			cell.setCellStyle(style);
			cell = row.createCell((short) 16);
			cell.setCellValue("jsonarr");
			cell.setCellStyle(style);
			cell = row.createCell((short) 17);
			cell.setCellValue("标准结果7");
			cell.setCellStyle(style);

			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
			for (int i = 0; i < listMtsDBdataDetail.size(); i++) {
				System.out.println("===========正在执行第" + i + "条===========");
				PageData pdRes = listMtsDBdataDetail.get(i);
				String DB_DATA_DETAIL_ID = pdRes.getString("DB_DATA_DETAIL_ID");
				String DIAG_NAME = pdRes.getString("DIAG_NAME");
				String NLP_RESULT = pdRes.getString("NLP_RESULT");

				String RESULT_1 = pdRes.getString("RESULT_1");
				String RESULT_2 = pdRes.getString("RESULT_2");
				String RESULT_3 = pdRes.getString("RESULT_3");
				String RESULT_4 = pdRes.getString("RESULT_4");
				String RESULT_5 = pdRes.getString("RESULT_5");
				String RESULT_6 = pdRes.getString("RESULT_6");
				String RESULT_7 = pdRes.getString("RESULT_7");
				String STANDARD_RESULT = pdRes.getString("STANDARD_RESULT");
				String DESCRIPTION = pdRes.getString("DESCRIPTION");
				Date OPERATE_TIME = (Date) pdRes.get("OPERATE_TIME");
				String JSONARR = pdRes.getString("JSONARR");
				String OPERATE_TIME_Str = "";
				if (null != OPERATE_TIME) {
					OPERATE_TIME_Str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(OPERATE_TIME);
				}
				String NLP_ORDER = pdRes.getString("NLP_ORDER");
				String USELESS_DATA = pdRes.getString("USELESS_DATA");

				// 第四步，创建单元格，并设置值
				row = sheet.createRow((int) i + 1);
				row.createCell((short) 0).setCellValue((int) i + 1);
				row.createCell((short) 1).setCellValue(DIAG_NAME);
				row.createCell((short) 2).setCellValue(NLP_RESULT);
				row.createCell((short) 3).setCellValue(NLP_ORDER);
				row.createCell((short) 4).setCellValue(RESULT_1);
				row.createCell((short) 5).setCellValue(RESULT_2);
				row.createCell((short) 6).setCellValue(RESULT_3);
				String STANDARD_RESULT_STR = "";
				if ("0".equals(STANDARD_RESULT)) {
					STANDARD_RESULT_STR = "未标化";
				} else if ("1".equals(STANDARD_RESULT)) {
					STANDARD_RESULT_STR = "完全匹配";
				} else if ("2".equals(STANDARD_RESULT)) {
					STANDARD_RESULT_STR = "无法干预";
				} else if ("3".equals(STANDARD_RESULT)) {
					STANDARD_RESULT_STR = "人工标化";
				} else if ("4".equals(STANDARD_RESULT)) {
					STANDARD_RESULT_STR = "AI处理";
				}
				row.createCell((short) 7).setCellValue(STANDARD_RESULT_STR);
				row.createCell((short) 8).setCellValue(DESCRIPTION);
				row.createCell((short) 9).setCellValue(OPERATE_TIME_Str);

				row.createCell((short) 10).setCellValue(RESULT_4);
				row.createCell((short) 11).setCellValue(RESULT_5);
				row.createCell((short) 12).setCellValue(RESULT_6);

				if (null != USELESS_DATA && !"".equals(USELESS_DATA)) {
					String[] uselessList = USELESS_DATA.split("@#&");
					if (uselessList != null && uselessList.length > 0) {
						if (uselessList.length == 1) {
							row.createCell((short) 13).setCellValue(uselessList[0]);
						} else if (uselessList.length == 2) {
							row.createCell((short) 13).setCellValue(uselessList[0]);
							row.createCell((short) 14).setCellValue(uselessList[1]);
						} else if (uselessList.length == 3) {
							row.createCell((short) 13).setCellValue(uselessList[0]);
							row.createCell((short) 14).setCellValue(uselessList[1]);
							row.createCell((short) 15).setCellValue(uselessList[2]);
						}
					}
				}
				if (null != JSONARR && !"".equals(JSONARR)) {
					row.createCell((short) 16).setCellValue(JSONARR);
				}

				row.createCell((short) 17).setCellValue(RESULT_7);
			}

			System.out.println("=======导出即将完毕=============");

			String fileName = "StandardizeResult-" + BATCH_NUM + ".xlsx";
			response.reset();// 不加这一句的话会出现下载错误
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
			response.setContentType("text/x-plain");// 定义输出类型

			try {
				ServletOutputStream out = response.getOutputStream();

				String path = System.getProperty("java.io.tmpdir") + "\\StandardizeResult-" + BATCH_NUM + ".xlsx";
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/exportExcelForPRKS23", produces = "application/json;charset=UTF-8")
	public ModelAndView exportExcelForPRKS23(Page page, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String DB_DATA_ID = request.getParameter("DB_DATA_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");
		String DB_DATA_TYPE = request.getParameter("DB_DATA_TYPE");
		PageData pd = new PageData();
		pd = this.getPageData();

		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM);
		}
		if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
			pd.put("DB_DATA_ID", DB_DATA_ID);
		}
		if (null != DB_DATA_TYPE && !"".equals(DB_DATA_TYPE)) {
			pd.put("DB_DATA_TYPE", DB_DATA_TYPE);
		}
		if (null != STATUS && !"".equals(STATUS)) {
			pd.put("STATUS", "1");
		} else {
			pd.put("STATUS", "");
		}
		page.setPd(pd);
		page.setShowCount(999999999);

		List<PageData> listMtsDBdataDetail = null;
		if ("0".equals(DB_DATA_TYPE)) {

		} else if ("1".equals(DB_DATA_TYPE)) {
			if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
				listMtsDBdataDetail = mtsRecordService.mtsRecordDetaillistPage(page);

				if (listMtsDBdataDetail != null && listMtsDBdataDetail.size() > 0) {
					String parametersStr = "";
					for (int i = 0; i < listMtsDBdataDetail.size(); i++) {
						parametersStr = "";
						PageData urpd = listMtsDBdataDetail.get(i);

						String RECORD_INFO_ID = urpd.getString("RECORD_INFO_ID");
						MtsRecordParameters mtsRecordParameters = new MtsRecordParameters();
						mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
						List<MtsRecordParameters> recordParaList = mtsRecordService
								.findMtsRecordParameters(mtsRecordParameters);
						if (recordParaList != null && recordParaList.size() > 0) {
							for (int j = 0; j < recordParaList.size(); j++) {
								MtsRecordParameters recordParameters = recordParaList.get(j);
								if (recordParameters != null) {
									if (null != parametersStr && !"".equals(parametersStr)) {
										parametersStr = parametersStr + "</br>"
												+ recordParameters.getPARAMETERS_CONTENT();
									} else {
										parametersStr = recordParameters.getPARAMETERS_CONTENT();
									}
								}
							}
						}
						urpd.put("parametersStr", parametersStr);
					}
				}
			}
		}

		if (listMtsDBdataDetail != null && listMtsDBdataDetail.size() > 0) {

			long startTime = System.currentTimeMillis(); // 获取开始时间
			System.out.println("==============查询开始时间================" + startTime);
			long endTime = System.currentTimeMillis(); // 获取结束时间
			System.out.println("==============查询结束时间================" + endTime);
			System.out.println("==============查询方法总耗时： " + (endTime - startTime) + "ms");
			System.out.println("listMtsDBdataDetail.size()==============>>>" + listMtsDBdataDetail.size());
			// 第一步，创建一个webbook，对应一个Excel文件
			XSSFWorkbook wb = new XSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = wb.createSheet("数据标准化结果");

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
			sheet.setColumnWidth((short) 11, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 12, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 13, (short) (35.7 * 150));

			sheet.setColumnWidth((short) 14, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 15, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 16, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 17, (short) (35.7 * 150));
			sheet.setColumnWidth((short) 18, (short) (35.7 * 150));
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			Row row = sheet.createRow((int) 0);
			// 第四步，创建单元格，并设置值表头 设置表头居中
			XSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

			Cell cell = row.createCell((short) 0);
			cell.setCellValue("序号");
			cell.setCellStyle(style);
			cell = row.createCell((short) 1);
			cell.setCellValue("原始数据");
			cell.setCellStyle(style);
			cell = row.createCell((short) 2);
			cell.setCellValue("NLP结果");
			cell.setCellStyle(style);
			cell = row.createCell((short) 3);
			cell.setCellValue("西医名称");
			cell.setCellStyle(style);
			cell = row.createCell((short) 4);
			cell.setCellValue("西医主码");
			cell.setCellStyle(style);
			cell = row.createCell((short) 5);
			cell.setCellValue("西医附码");
			cell.setCellStyle(style);
			cell = row.createCell((short) 6);
			cell.setCellValue("中医名称");
			cell.setCellStyle(style);
			cell = row.createCell((short) 7);
			cell.setCellValue("中医编码");
			cell.setCellStyle(style);
			cell = row.createCell((short) 8);
			cell.setCellValue("手术名称");
			cell.setCellStyle(style);
			cell = row.createCell((short) 9);
			cell.setCellValue("手术编码");
			cell.setCellStyle(style);
			cell = row.createCell((short) 10);
			cell.setCellValue("术语类型");
			cell.setCellStyle(style);
			cell = row.createCell((short) 11);
			cell.setCellValue("怀疑诊断");
			cell.setCellStyle(style);
			cell = row.createCell((short) 12);
			cell.setCellValue("特殊字符处理");
			cell.setCellStyle(style);
			cell = row.createCell((short) 13);
			cell.setCellValue("无法标化-类型");
			cell.setCellStyle(style);
			cell = row.createCell((short) 14);
			cell.setCellValue("标化状态");
			cell.setCellStyle(style);

			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
			for (int i = 0; i < listMtsDBdataDetail.size(); i++) {
				System.out.println("===========正在执行第" + i + "条===========");
				PageData pdRes = listMtsDBdataDetail.get(i);
				String parametersStr = pdRes.getString("parametersStr");
				String NLP_RESULT = pdRes.getString("NLP_RESULT");
				String XY_STANDARD_WORD = pdRes.getString("XY_STANDARD_WORD");
				String XY_STANDARD_MAIN_CODE = pdRes.getString("XY_STANDARD_MAIN_CODE");
				String XY_STANDARD_ATTACH_CODE = pdRes.getString("XY_STANDARD_ATTACH_CODE");
				String ZY_STANDARD_WORD = pdRes.getString("ZY_STANDARD_WORD");
				String ZY_STANDARD_MAIN_CODE = pdRes.getString("ZY_STANDARD_MAIN_CODE");
				String SS_STANDARD_WORD = pdRes.getString("SS_STANDARD_WORD");
				String SS_STANDARD_MAIN_CODE = pdRes.getString("SS_STANDARD_MAIN_CODE");

				String TERMINOLOGY_TYPE = pdRes.getString("TERMINOLOGY_TYPE");
				String TERMINOLOGY_TYPE_STR = "";
				if ("0".equals(TERMINOLOGY_TYPE)) {
					TERMINOLOGY_TYPE_STR = "西医";
				} else if ("1".equals(TERMINOLOGY_TYPE)) {
					TERMINOLOGY_TYPE_STR = "中医";
				} else if ("2".equals(TERMINOLOGY_TYPE)) {
					TERMINOLOGY_TYPE_STR = "手术";
				} else {
					TERMINOLOGY_TYPE_STR = "-";
				}

				String DOUBT_DIAG = pdRes.getString("DOUBT_DIAG");
				String DOUBT_DIAG_STR = "";
				if ("1".equals(DOUBT_DIAG)) {
					DOUBT_DIAG_STR = "Y";
				} else {
					DOUBT_DIAG_STR = "-";
				}

				String SPECIAL_CHARACTERS = pdRes.getString("SPECIAL_CHARACTERS");
				String SPECIAL_CHARACTERS_STR = "";
				if ("1".equals(SPECIAL_CHARACTERS)) {
					SPECIAL_CHARACTERS_STR = "Y";
				} else {
					SPECIAL_CHARACTERS_STR = "-";
				}

				String CAN_NOT_STANDARD_TYPE = pdRes.getString("CAN_NOT_STANDARD_TYPE");
				String CAN_NOT_STANDARD_TYPE_STR = "";
				if ("1".equals(CAN_NOT_STANDARD_TYPE)) {
					CAN_NOT_STANDARD_TYPE_STR = "Y";
				} else {
					CAN_NOT_STANDARD_TYPE_STR = "-";
				}
				String STATUS_PRKS = pdRes.getString("STATUS");
				String STATUS_PRKS_STR = "";
				if ("0".equals(STATUS_PRKS)) {
					STATUS_PRKS_STR = "未匹配";
				} else if ("1".equals(STATUS_PRKS)) {
					STATUS_PRKS_STR = "完全匹配";
				} else if ("2".equals(STATUS_PRKS)) {
					STATUS_PRKS_STR = "无法标化";
				} else if ("3".equals(STATUS_PRKS)) {
					STATUS_PRKS_STR = "人工标化";
				} else if ("4".equals(STATUS_PRKS)) {
					STATUS_PRKS_STR = "AI处理";
				}

				// 第四步，创建单元格，并设置值
				row = sheet.createRow((int) i + 1);
				row.createCell((short) 0).setCellValue((int) i + 1);
				row.createCell((short) 1).setCellValue(parametersStr);
				row.createCell((short) 2).setCellValue(NLP_RESULT);
				row.createCell((short) 3).setCellValue(XY_STANDARD_WORD);
				row.createCell((short) 4).setCellValue(XY_STANDARD_MAIN_CODE);
				row.createCell((short) 5).setCellValue(XY_STANDARD_ATTACH_CODE);
				row.createCell((short) 6).setCellValue(ZY_STANDARD_WORD);
				row.createCell((short) 7).setCellValue(ZY_STANDARD_MAIN_CODE);
				row.createCell((short) 8).setCellValue(SS_STANDARD_WORD);
				row.createCell((short) 9).setCellValue(SS_STANDARD_MAIN_CODE);
				row.createCell((short) 10).setCellValue(TERMINOLOGY_TYPE_STR);
				row.createCell((short) 11).setCellValue(DOUBT_DIAG_STR);
				row.createCell((short) 12).setCellValue(SPECIAL_CHARACTERS_STR);
				row.createCell((short) 13).setCellValue(CAN_NOT_STANDARD_TYPE_STR);
				row.createCell((short) 14).setCellValue(STATUS_PRKS_STR);
			}

			System.out.println("=======导出即将完毕=============");

			String fileName = "StandardizeResult-" + BATCH_NUM + ".xlsx";
			response.reset();// 不加这一句的话会出现下载错误
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
			response.setContentType("text/x-plain");// 定义输出类型

			try {
				ServletOutputStream out = response.getOutputStream();

				String path = System.getProperty("java.io.tmpdir") + "\\StandardizeResult-" + BATCH_NUM + ".xlsx";
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/exportTXTForPRKS23", produces = "application/json;charset=UTF-8")
	public ModelAndView exportTXTForPRKS23(Page page, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String DB_DATA_ID = request.getParameter("DB_DATA_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");
		String DB_DATA_TYPE = request.getParameter("DB_DATA_TYPE");
		PageData pd = new PageData();
		pd = this.getPageData();

		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM);
		}
		if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
			pd.put("DB_DATA_ID", DB_DATA_ID);
		}
		if (null != DB_DATA_TYPE && !"".equals(DB_DATA_TYPE)) {
			pd.put("DB_DATA_TYPE", DB_DATA_TYPE);
		}
		if (null != STATUS && !"".equals(STATUS)) {
			pd.put("STATUS", "1");
		} else {
			pd.put("STATUS", "");
		}
		page.setPd(pd);
		page.setShowCount(999999999);

		List<PageData> listMtsDBdataDetail = null;
		if ("0".equals(DB_DATA_TYPE)) {

		} else if ("1".equals(DB_DATA_TYPE)) {
			if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
				listMtsDBdataDetail = mtsRecordService.mtsRecordDetaillistPage(page);

				if (listMtsDBdataDetail != null && listMtsDBdataDetail.size() > 0) {
					String parametersStr = "";
					for (int i = 0; i < listMtsDBdataDetail.size(); i++) {
						parametersStr = "";
						PageData urpd = listMtsDBdataDetail.get(i);

						String RECORD_INFO_ID = urpd.getString("RECORD_INFO_ID");
						MtsRecordParameters mtsRecordParameters = new MtsRecordParameters();
						mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
						List<MtsRecordParameters> recordParaList = mtsRecordService
								.findMtsRecordParameters(mtsRecordParameters);
						if (recordParaList != null && recordParaList.size() > 0) {
							for (int j = 0; j < recordParaList.size(); j++) {
								MtsRecordParameters recordParameters = recordParaList.get(j);
								if (recordParameters != null) {
									if (null != parametersStr && !"".equals(parametersStr)) {
										parametersStr = parametersStr + "</br>"
												+ recordParameters.getPARAMETERS_CONTENT();
									} else {
										parametersStr = recordParameters.getPARAMETERS_CONTENT();
									}
								}
							}
						}
						urpd.put("parametersStr", parametersStr);
					}
				}
			}
		}

		if (listMtsDBdataDetail != null && listMtsDBdataDetail.size() > 0) {

			System.out.println("=======导出即将完毕=============");

			FileOutputStream outSTr = null;
			BufferedOutputStream Buff = null;
			String path = System.getProperty("java.io.tmpdir") + "\\StandardizeResult-" + BATCH_NUM + ".txt";

			System.out.println("path=============>>>" + path);
			String enter = "\r\n";
			String fgStr = "    ";
			String title = "序号" + fgStr + "原始数据" + fgStr + "NLP结果" + fgStr + "西医名称" + fgStr + "西医主码" + fgStr + "西医附码"
					+ fgStr + "中医名称" + fgStr + "中医编码" + fgStr + "手术名称" + fgStr + "手术编码" + fgStr + "术语类型" + fgStr
					+ "怀疑诊断" + fgStr + "特殊字符处理"+fgStr+"无法标化-类型"+fgStr+"标化状态" +enter;
			

			PageData pdRes;
			StringBuffer write;
			try {
				outSTr = new FileOutputStream(new File(path));
				Buff = new BufferedOutputStream(outSTr);
				
				for (int i = 0; i < listMtsDBdataDetail.size(); i++) {
					write = new StringBuffer();
					if(i == 0){
						write.append(title);
					}
					pdRes = listMtsDBdataDetail.get(i);
					String parametersStr = pdRes.getString("parametersStr");
					String NLP_RESULT = pdRes.getString("NLP_RESULT");
					String XY_STANDARD_WORD = pdRes.getString("XY_STANDARD_WORD");
					String XY_STANDARD_MAIN_CODE = pdRes.getString("XY_STANDARD_MAIN_CODE");
					String XY_STANDARD_ATTACH_CODE = pdRes.getString("XY_STANDARD_ATTACH_CODE");
					String ZY_STANDARD_WORD = pdRes.getString("ZY_STANDARD_WORD");
					String ZY_STANDARD_MAIN_CODE = pdRes.getString("ZY_STANDARD_MAIN_CODE");
					String SS_STANDARD_WORD = pdRes.getString("SS_STANDARD_WORD");
					String SS_STANDARD_MAIN_CODE = pdRes.getString("SS_STANDARD_MAIN_CODE");

					String TERMINOLOGY_TYPE = pdRes.getString("TERMINOLOGY_TYPE");
					String TERMINOLOGY_TYPE_STR = "";
					if ("0".equals(TERMINOLOGY_TYPE)) {
						TERMINOLOGY_TYPE_STR = "西医";
					} else if ("1".equals(TERMINOLOGY_TYPE)) {
						TERMINOLOGY_TYPE_STR = "中医";
					} else if ("2".equals(TERMINOLOGY_TYPE)) {
						TERMINOLOGY_TYPE_STR = "手术";
					} else {
						TERMINOLOGY_TYPE_STR = "-";
					}

					String DOUBT_DIAG = pdRes.getString("DOUBT_DIAG");
					String DOUBT_DIAG_STR = "";
					if ("1".equals(DOUBT_DIAG)) {
						DOUBT_DIAG_STR = "Y";
					} else {
						DOUBT_DIAG_STR = "-";
					}

					String SPECIAL_CHARACTERS = pdRes.getString("SPECIAL_CHARACTERS");
					String SPECIAL_CHARACTERS_STR = "";
					if ("1".equals(SPECIAL_CHARACTERS)) {
						SPECIAL_CHARACTERS_STR = "Y";
					} else {
						SPECIAL_CHARACTERS_STR = "-";
					}

					String CAN_NOT_STANDARD_TYPE = pdRes.getString("CAN_NOT_STANDARD_TYPE");
					String CAN_NOT_STANDARD_TYPE_STR = "";
					if ("1".equals(CAN_NOT_STANDARD_TYPE)) {
						CAN_NOT_STANDARD_TYPE_STR = "Y";
					} else {
						CAN_NOT_STANDARD_TYPE_STR = "-";
					}
					String STATUS_PRKS = pdRes.getString("STATUS");
					String STATUS_PRKS_STR = "";
					if ("0".equals(STATUS_PRKS)) {
						STATUS_PRKS_STR = "未匹配";
					} else if ("1".equals(STATUS_PRKS)) {
						STATUS_PRKS_STR = "完全匹配";
					} else if ("2".equals(STATUS_PRKS)) {
						STATUS_PRKS_STR = "无法标化";
					} else if ("3".equals(STATUS_PRKS)) {
						STATUS_PRKS_STR = "人工标化";
					} else if ("4".equals(STATUS_PRKS)) {
						STATUS_PRKS_STR = "AI处理";
					}
					
					
					pdRes = listMtsDBdataDetail.get(i);
					write.append(((int) i + 1) + fgStr + parametersStr + fgStr + NLP_RESULT + fgStr + XY_STANDARD_WORD + fgStr + XY_STANDARD_MAIN_CODE + fgStr + XY_STANDARD_ATTACH_CODE
							+ fgStr + ZY_STANDARD_WORD + fgStr + ZY_STANDARD_MAIN_CODE + fgStr + SS_STANDARD_WORD + fgStr + SS_STANDARD_MAIN_CODE + fgStr + TERMINOLOGY_TYPE_STR + fgStr
							+ DOUBT_DIAG_STR + fgStr + SPECIAL_CHARACTERS_STR + fgStr + CAN_NOT_STANDARD_TYPE_STR + fgStr + STATUS_PRKS_STR +enter);
					Buff.write(write.toString().getBytes("UTF-8"));
				}
				Buff.flush();
				Buff.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Buff.close();
					outSTr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			/*
			 * String fileName = "StandardizeResult-" + BATCH_NUM + ".txt";
			 * response.reset();// 不加这一句的话会出现下载错误
			 * response.setHeader("Content-disposition", "attachment; filename="
			 * + fileName);// 设定输出文件头 response.setContentType("text/x-plain");//
			 * 定义输出类型
			 * 
			 * try { ServletOutputStream out = response.getOutputStream();
			 * 
			 * File file = new File(path); FileOutputStream fos = new
			 * FileOutputStream(file); wb.write(fos); fos.close();
			 * 
			 * FileInputStream fis = new java.io.FileInputStream(file);
			 * ByteArrayOutputStream byteOutputStream = new
			 * ByteArrayOutputStream(4096);
			 * 
			 * byte[] cache = new byte[4096]; for (int offset = fis.read(cache);
			 * offset != -1; offset = fis.read(cache)) {
			 * byteOutputStream.write(cache, 0, offset); }
			 * 
			 * byte[] bt = null; bt = byteOutputStream.toByteArray();
			 * 
			 * out.write(bt); out.flush(); out.close(); fis.close(); if
			 * (file.exists()) { file.delete(); } } catch (Exception e) {
			 * e.printStackTrace(); }
			 */

			String name = request.getParameter("filename");
			// TODO Auto-generated method stub
			try {
				File file = new File(path);
				String filename = file.getName();

				// 取得文件的扩展名ext
				String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

				InputStream fis = new BufferedInputStream(new FileInputStream(path));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();

				response.reset();
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
				response.addHeader("Content-Length", "" + file.length()); // 设置返回的文件类型
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream()); // 得到向客户端输出二进制数据的对象
				// 根据扩展名声称客户端浏览器mime类型
				if (ext.equals("xls"))
					response.setContentType("application/msexcel");
				else
					response.setContentType("application/octet-stream"); // 设置返回的文件类型
				toClient.write(buffer); // 输出数据
				toClient.flush();
				toClient.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

	@RequestMapping(value = "/listMtsDBDataResult")
	public ModelAndView listMtsDBDataResult(Page page, HttpServletRequest request) throws Exception {
		String DB_DATA_ID = request.getParameter("DB_DATA_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");
		String DB_DATA_TYPE = request.getParameter("DB_DATA_TYPE");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM);
		}
		if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
			pd.put("DB_DATA_ID", DB_DATA_ID);
		}
		if (null != DB_DATA_TYPE && !"".equals(DB_DATA_TYPE)) {
			pd.put("DB_DATA_TYPE", DB_DATA_TYPE);
		}
		page.setPd(pd);

		List<PageData> listMtsDBdataDetail = null;
		if ("0".equals(DB_DATA_TYPE)) {
			if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
				listMtsDBdataDetail = mtsDBDataService.mtsDBDataDetaillistPage(page);
			}
			mv.setViewName("mts/dbdata/mts_dbdataResult_list");

		} else if ("1".equals(DB_DATA_TYPE)) {
			if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
				listMtsDBdataDetail = mtsRecordService.mtsRecordInfolistPage(page);

				if (listMtsDBdataDetail != null && listMtsDBdataDetail.size() > 0) {
					String parametersStr = "";
					String XY_STANDARD_WORD = "";
					String XY_STANDARD_MAIN_CODE = "";
					String XY_STANDARD_ATTACH_CODE = "";
					String NLP_RESULT = "";
					String ZY_STANDARD_WORD = "";
					String ZY_STANDARD_MAIN_CODE = "";
					String TERMINOLOGY_TYPE = "";
					String DOUBT_DIAG = "";
					String SPECIAL_CHARACTERS = "";
					String CAN_NOT_STANDARD_TYPE = "";
					String SS_STANDARD_WORD = "";
					String SS_STANDARD_MAIN_CODE = "";
					String STAND_STATUS = "";
					for (int i = 0; i < listMtsDBdataDetail.size(); i++) {
						parametersStr = "";
						XY_STANDARD_WORD = "";
						XY_STANDARD_MAIN_CODE = "";
						XY_STANDARD_ATTACH_CODE = "";
						NLP_RESULT = "";
						ZY_STANDARD_WORD = "";
						ZY_STANDARD_MAIN_CODE = "";
						TERMINOLOGY_TYPE = "";
						DOUBT_DIAG = "";
						SPECIAL_CHARACTERS = "";
						CAN_NOT_STANDARD_TYPE = "";
						SS_STANDARD_WORD = "";
						SS_STANDARD_MAIN_CODE = "";
						STAND_STATUS = "";
						PageData urpd = listMtsDBdataDetail.get(i);
						String RECORD_INFO_ID = urpd.getString("RECORD_INFO_ID");
						MtsRecordParameters mtsRecordParameters = new MtsRecordParameters();
						mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
						List<MtsRecordParameters> recordParaList = mtsRecordService
								.findMtsRecordParameters(mtsRecordParameters);
						if (recordParaList != null && recordParaList.size() > 0) {
							for (int j = 0; j < recordParaList.size(); j++) {
								MtsRecordParameters recordParameters = recordParaList.get(j);
								if (recordParameters != null) {
									if (null != parametersStr && !"".equals(parametersStr)) {
										parametersStr = parametersStr + "</br>"
												+ recordParameters.getPARAMETERS_CONTENT();
									} else {
										parametersStr = recordParameters.getPARAMETERS_CONTENT();
									}
								}
							}
						}

						MtsRecordDetail mtsRecordDetail = new MtsRecordDetail();
						mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
						List<MtsRecordDetail> recordDetailList = mtsRecordService.findMtsRecordDetail(mtsRecordDetail);
						if (recordDetailList != null && recordDetailList.size() > 0) {
							for (int k = 0; k < recordDetailList.size(); k++) {
								MtsRecordDetail recordDetail = recordDetailList.get(k);
								if (recordDetail != null) {
									NLP_RESULT = this.formatRsult(NLP_RESULT, recordDetail.getNLP_RESULT(), "NORMAL",
											k);
									XY_STANDARD_WORD = this.formatRsult(XY_STANDARD_WORD,
											recordDetail.getXY_STANDARD_WORD(), "NORMAL", k);
									XY_STANDARD_MAIN_CODE = this.formatRsult(XY_STANDARD_MAIN_CODE,
											recordDetail.getXY_STANDARD_MAIN_CODE(), "NORMAL", k);
									XY_STANDARD_ATTACH_CODE = this.formatRsult(XY_STANDARD_ATTACH_CODE,
											recordDetail.getXY_STANDARD_ATTACH_CODE(), "NORMAL", k);
									ZY_STANDARD_WORD = this.formatRsult(ZY_STANDARD_WORD,
											recordDetail.getZY_STANDARD_WORD(), "NORMAL", k);
									ZY_STANDARD_MAIN_CODE = this.formatRsult(ZY_STANDARD_MAIN_CODE,
											recordDetail.getZY_STANDARD_MAIN_CODE(), "NORMAL", k);
									TERMINOLOGY_TYPE = this.formatRsult(TERMINOLOGY_TYPE,
											recordDetail.getTERMINOLOGY_TYPE(), "TERMINOLOGY_TYPE", k);
									DOUBT_DIAG = this.formatRsult(DOUBT_DIAG, recordDetail.getDOUBT_DIAG(),
											"DOUBT_DIAG", k);
									SPECIAL_CHARACTERS = this.formatRsult(SPECIAL_CHARACTERS,
											recordDetail.getSPECIAL_CHARACTERS(), "SPECIAL_CHARACTERS", k);
									CAN_NOT_STANDARD_TYPE = this.formatRsult(CAN_NOT_STANDARD_TYPE,
											recordDetail.getCAN_NOT_STANDARD_TYPE(), "NORMAL", k);
									SS_STANDARD_WORD = this.formatRsult(SS_STANDARD_WORD,
											recordDetail.getSS_STANDARD_WORD(), "NORMAL", k);
									SS_STANDARD_MAIN_CODE = this.formatRsult(SS_STANDARD_MAIN_CODE,
											recordDetail.getSS_STANDARD_MAIN_CODE(), "NORMAL", k);
									STAND_STATUS = this.formatRsult(STAND_STATUS, recordDetail.getSTATUS(),
											"STAND_STATUS", k);
								}
							}
						}
						urpd.put("parametersStr", parametersStr);
						urpd.put("NLP_RESULT", NLP_RESULT);
						urpd.put("XY_STANDARD_ATTACH_CODE", XY_STANDARD_ATTACH_CODE);
						urpd.put("XY_STANDARD_MAIN_CODE", XY_STANDARD_MAIN_CODE);
						urpd.put("XY_STANDARD_WORD", XY_STANDARD_WORD);
						urpd.put("ZY_STANDARD_WORD", ZY_STANDARD_WORD);
						urpd.put("ZY_STANDARD_MAIN_CODE", ZY_STANDARD_MAIN_CODE);
						urpd.put("TERMINOLOGY_TYPE", TERMINOLOGY_TYPE);
						urpd.put("DOUBT_DIAG", DOUBT_DIAG);
						urpd.put("SPECIAL_CHARACTERS", SPECIAL_CHARACTERS);
						urpd.put("CAN_NOT_STANDARD_TYPE", CAN_NOT_STANDARD_TYPE);
						urpd.put("SS_STANDARD_WORD", SS_STANDARD_WORD);
						urpd.put("SS_STANDARD_MAIN_CODE", SS_STANDARD_MAIN_CODE);
						urpd.put("STAND_STATUS", STAND_STATUS);
					}
				}
				mv.setViewName("mts/dbdata/mts_dbdataResult_new_list");
			}
		}

		mv.addObject("listMtsDBdataDetail", listMtsDBdataDetail);

		int pcount = 0;
		if (page.getCurrentPage() > 0) {
			pcount = (page.getCurrentPage() - 1) * page.getShowCount();
		} else {
			pcount = 0;
		}
		mv.addObject("pcount", pcount);
		mv.addObject("pd", pd);
		mv.addObject("STATUS", STATUS);
		mv.addObject("DB_DATA_ID", DB_DATA_ID);
		mv.addObject("BATCH_NUM", BATCH_NUM);
		mv.addObject("DB_DATA_TYPE", DB_DATA_TYPE);
		return mv;
	}

	@RequestMapping(value = "/listMtsDBDataResult2")
	public ModelAndView listMtsDBDataResult2(Page page, HttpServletRequest request) throws Exception {
		String DB_DATA_ID = request.getParameter("DB_DATA_ID");
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String STATUS = request.getParameter("STATUS");
		String DB_DATA_TYPE = request.getParameter("DB_DATA_TYPE");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM);
		}
		if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
			pd.put("DB_DATA_ID", DB_DATA_ID);
		}
		if (null != DB_DATA_TYPE && !"".equals(DB_DATA_TYPE)) {
			pd.put("DB_DATA_TYPE", DB_DATA_TYPE);
		}
		if (null != STATUS && !"".equals(STATUS)) {
			pd.put("STATUS", "1");
		} else {
			pd.put("STATUS", "");
		}
		page.setPd(pd);

		List<PageData> listMtsDBdataDetail = null;
		if ("0".equals(DB_DATA_TYPE)) {
			if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
				listMtsDBdataDetail = mtsDBDataService.mtsDBDataDetaillistPage(page);
			}
			mv.setViewName("mts/dbdata/mts_dbdataResult_list");

		} else if ("1".equals(DB_DATA_TYPE)) {
			if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
				listMtsDBdataDetail = mtsRecordService.mtsRecordDetaillistPage(page);

				if (listMtsDBdataDetail != null && listMtsDBdataDetail.size() > 0) {
					String parametersStr = "";
					/*
					 * String XY_STANDARD_WORD = ""; String
					 * XY_STANDARD_MAIN_CODE = ""; String
					 * XY_STANDARD_ATTACH_CODE = ""; String NLP_RESULT = "";
					 * String ZY_STANDARD_WORD = ""; String
					 * ZY_STANDARD_MAIN_CODE = ""; String TERMINOLOGY_TYPE = "";
					 * String DOUBT_DIAG = ""; String SPECIAL_CHARACTERS = "";
					 * String CAN_NOT_STANDARD_TYPE = ""; String
					 * SS_STANDARD_WORD = ""; String SS_STANDARD_MAIN_CODE = "";
					 * String STAND_STATUS = "";
					 */
					for (int i = 0; i < listMtsDBdataDetail.size(); i++) {
						parametersStr = "";
						/*
						 * XY_STANDARD_WORD = ""; XY_STANDARD_MAIN_CODE = "";
						 * XY_STANDARD_ATTACH_CODE = ""; NLP_RESULT = "";
						 * ZY_STANDARD_WORD = ""; ZY_STANDARD_MAIN_CODE = "";
						 * TERMINOLOGY_TYPE = ""; DOUBT_DIAG = "";
						 * SPECIAL_CHARACTERS = ""; CAN_NOT_STANDARD_TYPE = "";
						 * SS_STANDARD_WORD = ""; SS_STANDARD_MAIN_CODE = "";
						 * STAND_STATUS = "";
						 */
						PageData urpd = listMtsDBdataDetail.get(i);

						String RECORD_INFO_ID = urpd.getString("RECORD_INFO_ID");
						MtsRecordParameters mtsRecordParameters = new MtsRecordParameters();
						mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
						List<MtsRecordParameters> recordParaList = mtsRecordService
								.findMtsRecordParameters(mtsRecordParameters);
						if (recordParaList != null && recordParaList.size() > 0) {
							for (int j = 0; j < recordParaList.size(); j++) {
								MtsRecordParameters recordParameters = recordParaList.get(j);
								if (recordParameters != null) {
									if (null != parametersStr && !"".equals(parametersStr)) {
										parametersStr = parametersStr + "</br>"
												+ recordParameters.getPARAMETERS_CONTENT();
									} else {
										parametersStr = recordParameters.getPARAMETERS_CONTENT();
									}
								}
							}
						}

						/*
						 * MtsRecordDetail mtsRecordDetail = new
						 * MtsRecordDetail();
						 * mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
						 * List<MtsRecordDetail> recordDetailList =
						 * mtsRecordService.findMtsRecordDetail(mtsRecordDetail)
						 * ; if(recordDetailList != null &&
						 * recordDetailList.size() > 0){ for(int k = 0;k <
						 * recordDetailList.size();k ++){ MtsRecordDetail
						 * recordDetail = recordDetailList.get(k);
						 * if(recordDetail != null){ NLP_RESULT =
						 * this.formatRsult(NLP_RESULT,
						 * recordDetail.getNLP_RESULT(),"NORMAL",k);
						 * XY_STANDARD_WORD = this.formatRsult(XY_STANDARD_WORD,
						 * recordDetail.getXY_STANDARD_WORD(),"NORMAL",k);
						 * XY_STANDARD_MAIN_CODE =
						 * this.formatRsult(XY_STANDARD_MAIN_CODE,
						 * recordDetail.getXY_STANDARD_MAIN_CODE(),"NORMAL",k);
						 * XY_STANDARD_ATTACH_CODE =
						 * this.formatRsult(XY_STANDARD_ATTACH_CODE,
						 * recordDetail.getXY_STANDARD_ATTACH_CODE(),"NORMAL",k)
						 * ; ZY_STANDARD_WORD =
						 * this.formatRsult(ZY_STANDARD_WORD,
						 * recordDetail.getZY_STANDARD_WORD(),"NORMAL",k);
						 * ZY_STANDARD_MAIN_CODE =
						 * this.formatRsult(ZY_STANDARD_MAIN_CODE,
						 * recordDetail.getZY_STANDARD_MAIN_CODE(),"NORMAL",k);
						 * TERMINOLOGY_TYPE = this.formatRsult(TERMINOLOGY_TYPE,
						 * recordDetail.getTERMINOLOGY_TYPE(),"TERMINOLOGY_TYPE"
						 * ,k); DOUBT_DIAG = this.formatRsult(DOUBT_DIAG,
						 * recordDetail.getDOUBT_DIAG(),"DOUBT_DIAG",k);
						 * SPECIAL_CHARACTERS =
						 * this.formatRsult(SPECIAL_CHARACTERS,
						 * recordDetail.getSPECIAL_CHARACTERS(),
						 * "SPECIAL_CHARACTERS",k); CAN_NOT_STANDARD_TYPE =
						 * this.formatRsult(CAN_NOT_STANDARD_TYPE,
						 * recordDetail.getCAN_NOT_STANDARD_TYPE(),"NORMAL",k);
						 * SS_STANDARD_WORD = this.formatRsult(SS_STANDARD_WORD,
						 * recordDetail.getSS_STANDARD_WORD(),"NORMAL",k);
						 * SS_STANDARD_MAIN_CODE =
						 * this.formatRsult(SS_STANDARD_MAIN_CODE,
						 * recordDetail.getSS_STANDARD_MAIN_CODE(),"NORMAL",k);
						 * STAND_STATUS = this.formatRsult(STAND_STATUS,
						 * recordDetail.getSTATUS(),"STAND_STATUS",k); } } }
						 */
						urpd.put("parametersStr", parametersStr);
						/*
						 * urpd.put("NLP_RESULT", NLP_RESULT);
						 * urpd.put("XY_STANDARD_ATTACH_CODE",
						 * XY_STANDARD_ATTACH_CODE);
						 * urpd.put("XY_STANDARD_MAIN_CODE",
						 * XY_STANDARD_MAIN_CODE); urpd.put("XY_STANDARD_WORD",
						 * XY_STANDARD_WORD); urpd.put("ZY_STANDARD_WORD",
						 * ZY_STANDARD_WORD); urpd.put("ZY_STANDARD_MAIN_CODE",
						 * ZY_STANDARD_MAIN_CODE); urpd.put("TERMINOLOGY_TYPE",
						 * TERMINOLOGY_TYPE); urpd.put("DOUBT_DIAG",
						 * DOUBT_DIAG); urpd.put("SPECIAL_CHARACTERS",
						 * SPECIAL_CHARACTERS);
						 * urpd.put("CAN_NOT_STANDARD_TYPE",
						 * CAN_NOT_STANDARD_TYPE); urpd.put("SS_STANDARD_WORD",
						 * SS_STANDARD_WORD); urpd.put("SS_STANDARD_MAIN_CODE",
						 * SS_STANDARD_MAIN_CODE); urpd.put("STAND_STATUS",
						 * STAND_STATUS);
						 */
					}
				}
				if (null != STATUS && !"".equals(STATUS)) {
					mv.setViewName("mts/dbdata/mts_dbdataResult_new_list3");
				} else {
					mv.setViewName("mts/dbdata/mts_dbdataResult_new_list2");
				}
			}
		}

		mv.addObject("listMtsDBdataDetail", listMtsDBdataDetail);

		int pcount = 0;
		if (page.getCurrentPage() > 0) {
			pcount = (page.getCurrentPage() - 1) * page.getShowCount();
		} else {
			pcount = 0;
		}
		mv.addObject("pcount", pcount);
		mv.addObject("pd", pd);
		mv.addObject("STATUS", STATUS);
		mv.addObject("DB_DATA_ID", DB_DATA_ID);
		mv.addObject("BATCH_NUM", BATCH_NUM);
		mv.addObject("DB_DATA_TYPE", DB_DATA_TYPE);
		return mv;
	}

	public String formatRsult(String resultStr, String handleStr, String type, int rowNum) {
		String handleResultStr = "";
		String nonStr = "-";
		if ("NORMAL".equals(type)) {
			if (!"".equals(handleStr)) {
				handleResultStr = handleStr;
			} else {
				handleResultStr = nonStr;
			}
		} else if ("DOUBT_DIAG".equals(type)) {
			if ("0".equals(handleStr)) {
				handleResultStr = nonStr;
			} else if ("1".equals(handleStr)) {
				handleResultStr = "Y";
			} else {
				handleResultStr = nonStr;
			}
		} else if ("SPECIAL_CHARACTERS".equals(type)) {
			if ("0".equals(handleStr)) {
				handleResultStr = nonStr;
			} else if ("1".equals(handleStr)) {
				handleResultStr = "Y";
			} else {
				handleResultStr = nonStr;
			}
		} else if ("TERMINOLOGY_TYPE".equals(type)) {
			if ("0".equals(handleStr)) {
				handleResultStr = "西医";
			} else if ("1".equals(handleStr)) {
				handleResultStr = "中医";
			} else if ("2".equals(handleStr)) {
				handleResultStr = "手术";
			} else {
				handleResultStr = nonStr;
			}
		} else if ("STAND_STATUS".equals(type)) {
			if ("0".equals(handleStr)) {
				handleResultStr = "<span style='color:red;'>未匹配</span>";
			} else if ("1".equals(handleStr)) {
				handleResultStr = "<span style='color:green;'>完全匹配</span>";
			} else if ("2".equals(handleStr)) {
				handleResultStr = "<span style='color:red;'>无法标化</span>";
			} else if ("3".equals(handleStr)) {
				handleResultStr = "<span style='color:red;'>人工标化</span>";
			} else if ("4".equals(handleStr)) {
				handleResultStr = "<span style='color:red;'>AI处理</span>";
			} else {
				handleResultStr = nonStr;
			}
		} else if ("CAN_NOT_STANDARD_TYPE".equals(type)) {
			if ("0".equals(handleStr)) {
				handleResultStr = nonStr;
			} else if ("1".equals(handleStr)) {
				handleResultStr = "Y";
			} else {
				handleResultStr = nonStr;
			}
		}

		if (null != resultStr && !"".equals(resultStr)) {
			if (rowNum % 2 == 0) {
				resultStr = resultStr + "</br>" + "<span>" + handleResultStr + "</span>";
			} else {
				resultStr = resultStr + "</br>" + "<span style='font-weight:bold;'>" + handleResultStr + "</span>";
			}
		} else {
			if (rowNum % 2 == 0) {
				resultStr = "<span>" + handleResultStr + "</span>";
			} else {
				resultStr = "<span style='font-weight:bold;'>" + handleResultStr + "</span>";
			}
		}

		return resultStr;
	}

	@RequestMapping(value = "/goUploadExcel")
	public ModelAndView goUploadExcel(HttpServletRequest request) throws Exception {
		String DB_DATA_TYPE = request.getParameter("DB_DATA_TYPE");
		ModelAndView mv = this.getModelAndView();
		List<MtsArea> listMtsArea = mtsAreaService.findMtsArea(new MtsArea());
		List<MtsDataClass> listDataClass = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataSource> listDataSource = mtsDataSourceService.findMtsDataSource(new MtsDataSource());
		String batchNum = RandomUtil.getRandomId();
		mv.addObject("batchNum", batchNum);
		mv.addObject("listMtsArea", listMtsArea);
		mv.addObject("listDataClass", listDataClass);
		mv.addObject("listDataSource", listDataSource);
		mv.addObject("DB_DATA_TYPE", DB_DATA_TYPE);
		mv.setViewName("mts/dbdata/uploadexcel");
		return mv;
	}

	@RequestMapping(value = "/readExcel")
	public ModelAndView readExcelAes(@RequestParam(value = "excel", required = false) MultipartFile file,
			String EXPORT_NAME, String AREA, String BATCH_NUM, String DATA_SOURCE_CODE, String classcode,
			String typecode, String DB_DATA_TYPE) throws Exception {
		ModelAndView mv = this.getModelAndView();
		long startTime = System.currentTimeMillis(); // 获取开始时间
		System.out.println("==============导入excel开始时间================" + startTime);
		MtsDBData mtsDBData = new MtsDBData();
		String DB_DATA_ID = RandomUtil.getRandomId();
		mtsDBData.setAREA_CODE(AREA);
		mtsDBData.setBATCH_NUM(BATCH_NUM);
		mtsDBData.setDATA_CLASS_ID(classcode);
		mtsDBData.setDATA_TYPE_ID(typecode);
		mtsDBData.setDB_DATA_ID(DB_DATA_ID);
		mtsDBData.setIMPORT_NAME(EXPORT_NAME);
		mtsDBData.setOPERATE_TIME(new Date());
		mtsDBData.setSTATUS("0");
		mtsDBData.setDATA_SOURCE_CODE(DATA_SOURCE_CODE);
		mtsDBData.setDB_DATA_TYPE(DB_DATA_TYPE);

		MtsDBDataDetail mtsDBDataDetail = null;

		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE; // 文件上传路径
			String taskId = RandomUtil.getRandomId();
			String fileName = FileUpload.fileUp(file, filePath, taskId + "userexcel"); // 执行上传
			// XLSX2CSV xlsx2csv = new XLSX2CSV("Time.xlsx", "outs.csv",0);
			List<PageData> listPd = (List) ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
			// 2:从第3行开始；0:从第A列开始；0:第0个sheet
			/* 存入数据库操作====================================== */
			/**
			 * var0 :ID var1 :标化类型 var2 :原始数据ID var3 :原始数据名称 var4 :大串
			 */
			//
			CountDownLatch latch = new CountDownLatch(listPd.size());
			System.out.println("========excel总数======" + listPd.size());
			if (listPd != null && listPd.size() > 0) {
				for (int i = 0; i < listPd.size(); i++) {
					mtsDBDataDetail = new MtsDBDataDetail();
					String DATA_DETAIL_ID = RandomUtil.getRandomId();
					mtsDBDataDetail.setDB_DATA_DETAIL_ID(DATA_DETAIL_ID);
					mtsDBDataDetail.setBATCH_NUM(BATCH_NUM);
					mtsDBDataDetail.setDATA_CLASS_ID(classcode);
					mtsDBDataDetail.setDB_DATA_ID(DB_DATA_ID);
					mtsDBDataDetail.setIMPORT_TIME(new Date());
					mtsDBDataDetail.setMARK("0");
					mtsDBDataDetail.setSTATUS("0");
					mtsDBDataDetail.setDEAL_STATUS("0");
					mtsDBDataDetail.setDB_DATA_ID(DB_DATA_ID);

					if ("02".equals(classcode)) {
						mtsDBDataDetail.setDIAG_NAME(listPd.get(i).getString("var1"));
					} else if ("06".equals(classcode)) {
						mtsDBDataDetail.setDIAG_NAME(
								listPd.get(i).getString("var1") + "@#&" + listPd.get(i).getString("var2"));
					} else if ("07".equals(classcode)) {
						// mtsDBDataDetail.setDIAG_NAME(listPd.get(i).getString("var1")
						// + "@#&" + listPd.get(i).getString("var2") + "@#&" +
						// listPd.get(i).getString("var3") + "@#&" +
						// listPd.get(i).getString("var4") + "@#&" +
						// listPd.get(i).getString("var5") + "@#&" +
						// listPd.get(i).getString("var6") + "@#&" +
						// listPd.get(i).getString("var7") + "@#&" +
						// listPd.get(i).getString("var8"));
						String var2 = listPd.get(i).getString("var2");
						String var3 = listPd.get(i).getString("var3");
						String var4 = listPd.get(i).getString("var4");
						String var5 = listPd.get(i).getString("var5");
						String var6 = listPd.get(i).getString("var6");
						String var7 = listPd.get(i).getString("var7");
						// String var8=listPd.get(i).getString("var8");
						if (var2 == "" || "".equals(var2)) {
							var2 = "none";
						}
						if (var3 == "" || "".equals(var3)) {
							var3 = "none";
						}
						if (var4 == "" || "".equals(var4)) {
							var4 = "none";
						}
						if (var5 == "" || "".equals(var5)) {
							var5 = "none";
						}
						if (var6 == "" || "".equals(var6)) {
							var6 = "none";
						}
						if (var7 == "" || "".equals(var7)) {
							var7 = "none";
						}
						// if(var8==""||"".equals(var8)){
						// var8="none";
						// }
						// mtsDBDataDetail.setDIAG_NAME(listPd.get(i).getString("var2")
						// + "@#&" + listPd.get(i).getString("var3") + "@#&" +
						// listPd.get(i).getString("var4") + "@#&" +
						// listPd.get(i).getString("var5") + "@#&" +
						// listPd.get(i).getString("var6")+ "@#&" +
						// listPd.get(i).getString("var7")+ "@#&" +
						// listPd.get(i).getString("var8"));
						mtsDBDataDetail.setDIAG_NAME(
								var2 + "@#&" + var3 + "@#&" + var4 + "@#&" + var5 + "@#&" + var6 + "@#&" + var7);

						mtsDBDataDetail.setUSELESS_DATA(listPd.get(i).getString("var1"));
					} else if ("03".equals(classcode)) {
						mtsDBDataDetail.setDIAG_NAME(listPd.get(i).getString("var2"));
						mtsDBDataDetail.setUSELESS_DATA(listPd.get(i).getString("var0") + "@#&"
								+ listPd.get(i).getString("var1") + "@#&" + listPd.get(i).getString("var3"));
					} else if ("04".equals(classcode)) {
						mtsDBDataDetail.setDIAG_NAME(listPd.get(i).getString("var2"));
						mtsDBDataDetail.setUSELESS_DATA(listPd.get(i).getString("var1"));
					}
					FutureTask<String> futureTask = new FutureTask<String>(
							new ThreadPoolTaskForDataDetail(latch, mtsDBDataDetail));
					threadPoolTaskExecutor.execute(futureTask);
				}
			}
			System.out.println("===================等待所有线程执行完毕123==========================");
			latch.await();
			System.out.println("===================执行完毕123==========================");
			mtsDBDataService.addMTSDBData(mtsDBData);
			long endTime = System.currentTimeMillis(); // 获取结束时间
			System.out.println("==============导入excel时间================" + endTime);
			System.out.println("==============导入excel方法总耗时： " + (endTime - startTime) + "ms");
			/* 存入数据库操作====================================== */
			mv.addObject("msg", "success");
		}
		mv.setViewName("save_result");
		return mv;
	}

	@RequestMapping(value = "/readExcelNew")
	public ModelAndView readExcelNew(@RequestParam(value = "excel", required = false) MultipartFile file,
			String EXPORT_NAME, String AREA, String BATCH_NUM, String DATA_SOURCE_CODE, String classcode,
			String typecode, String DB_DATA_TYPE, String fileType) throws Exception {
		System.out.println("readExcelNew=============>>>执行了");
		ModelAndView mv = this.getModelAndView();
		long startTime = System.currentTimeMillis(); // 获取开始时间
		System.out.println("fileType==========>>>" + fileType);
		System.out.println("==============导入excel开始时间================" + startTime);
		MtsDBData mtsDBData = new MtsDBData();
		String DB_DATA_ID = RandomUtil.getRandomId();
		mtsDBData.setAREA_CODE(AREA);
		mtsDBData.setBATCH_NUM(BATCH_NUM);
		mtsDBData.setDATA_CLASS_ID(classcode);
		mtsDBData.setDATA_TYPE_ID(typecode);
		mtsDBData.setDB_DATA_ID(DB_DATA_ID);
		mtsDBData.setIMPORT_NAME(EXPORT_NAME);
		mtsDBData.setOPERATE_TIME(new Date());
		mtsDBData.setSTATUS("0");
		mtsDBData.setDATA_SOURCE_CODE(DATA_SOURCE_CODE);
		mtsDBData.setDB_DATA_TYPE(DB_DATA_TYPE);

		// MtsDBDataDetail mtsDBDataDetail = null;
		MtsRecordInfo mtsRecordInfo = null;
		MtsRecordParameters mtsRecordParameters = null;

		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE; // 文件上传路径
			String taskId = RandomUtil.getRandomId();
			String fileName = FileUpload.fileUp(file, filePath, taskId + "userexcel"); // 执行上传

			if ("0".equals(fileType)) {
				// XLSX2CSV xlsx2csv = new XLSX2CSV("Time.xlsx", "outs.csv",0);
				List<PageData> listPd = (List) ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
				// 2:从第3行开始；0:从第A列开始；0:第0个sheet
				/* 存入数据库操作====================================== */
				/**
				 * var0 :ID var1 :标化类型 var2 :原始数据ID var3 :原始数据名称 var4 :大串
				 */
				//
				// CountDownLatch latch = new CountDownLatch(listPd.size());
				System.out.println("========excel总数======" + listPd.size());

				if (listPd != null && listPd.size() > 0) {
					int countNum = listPd.size();
					int currentNum = 0;
					int handleLength = 1000;
					if (countNum > 0) {
						mcs.getCacheMap();
						while (currentNum < countNum) {
							int endNum = currentNum + handleLength;
							if (endNum > countNum) {
								endNum = countNum;
							}
							System.out.println("=========正在导入第" + currentNum + "条---第" + endNum + "条数据==============");

							CountDownLatch latch = new CountDownLatch(endNum - currentNum);
							for (int i = currentNum; i < endNum; i++) {
								mtsRecordInfo = new MtsRecordInfo();
								String RECORD_INFO_ID = RandomUtil.getRandomId();
								mtsRecordInfo.setRECORD_INFO_ID(RECORD_INFO_ID);
								mtsRecordInfo.setBATCH_NUM(BATCH_NUM);
								mtsRecordInfo.setDATA_CLASS(classcode);
								mtsRecordInfo.setDATA_SOURCE(DATA_SOURCE_CODE);
								mtsRecordInfo.setINFO_STATUS("0");
								mtsRecordInfo.setSTANDARD_TYPE("");
								mtsRecordInfo.setOPERATE_TIME(new Date());
								mtsRecordInfo.setDB_DATA_ID(DB_DATA_ID);
								if ("02".equals(classcode)) {
									mtsRecordParameters = new MtsRecordParameters();
									mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
									String PARAMETERS_CONTENT = listPd.get(i).getString("var1");
									mtsRecordParameters.setPARAMETERS_CONTENT(PARAMETERS_CONTENT);
									mtsRecordParameters.setPARAMETERS_ORDER("0");
									mtsRecordParameters.setPARAMETERS_TYPE(typecode);
									mtsRecordParameters.setRECORD_PARAMETERS_ID(RandomUtil.getRandomId());
								}
								threadPoolTaskExecutor.execute(
										new ThreadPoolTaskForRecord(latch, mtsRecordInfo, mtsRecordParameters));
							}
							latch.await();
							currentNum = endNum;
						}
					}
				}
				System.out.println("===================等待所有线程执行完毕123==========================");
				System.out.println("===================执行完毕123==========================");
				mtsDBDataService.addMTSDBData(mtsDBData);
				long endTime = System.currentTimeMillis(); // 获取结束时间
				System.out.println("==============导入excel时间================" + endTime);
				System.out.println("==============导入excel方法总耗时： " + (endTime - startTime) + "ms");
				/* 存入数据库操作====================================== */
			} else if ("1".equals(fileType)) {
				BufferedReader br;
				try {
					System.out.println("filePath==========>>>" + filePath);
					System.out.println("fileName==========>>>" + fileName);
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + fileName), "UTF-8"));
					String line = null;
					int recordNum = 0;
					List<String> txtList = null;
					while ((line = br.readLine()) != null) {
						if (recordNum != 0) {
							if (null == txtList) {
								txtList = new ArrayList<String>();
							}
							txtList.add(line);
						}
						recordNum++;
					}
					br.close();

					if (txtList != null && txtList.size() > 0) {
						int countNum = txtList.size();
						int currentNum = 0;
						int handleLength = 1000;
						if (countNum > 0) {
							mcs.getCacheMap();
							while (currentNum < countNum) {
								int endNum = currentNum + handleLength;
								if (endNum > countNum) {
									endNum = countNum;
								}
								System.out.println(
										"=========正在导入第" + currentNum + "条---第" + endNum + "条数据==============");

								CountDownLatch latch = new CountDownLatch(endNum - currentNum);
								for (int i = currentNum; i < endNum; i++) {
									mtsRecordInfo = new MtsRecordInfo();
									String RECORD_INFO_ID = RandomUtil.getRandomId();
									mtsRecordInfo.setRECORD_INFO_ID(RECORD_INFO_ID);
									mtsRecordInfo.setBATCH_NUM(BATCH_NUM);
									mtsRecordInfo.setDATA_CLASS(classcode);
									mtsRecordInfo.setDATA_SOURCE(DATA_SOURCE_CODE);
									mtsRecordInfo.setINFO_STATUS("0");
									mtsRecordInfo.setSTANDARD_TYPE("");
									mtsRecordInfo.setOPERATE_TIME(new Date());
									mtsRecordInfo.setDB_DATA_ID(DB_DATA_ID);
									if ("02".equals(classcode)) {
										mtsRecordParameters = new MtsRecordParameters();
										mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
										String PARAMETERS_CONTENT = txtList.get(i);
										mtsRecordParameters.setPARAMETERS_CONTENT(PARAMETERS_CONTENT);
										mtsRecordParameters.setPARAMETERS_ORDER("0");
										mtsRecordParameters.setPARAMETERS_TYPE(typecode);
										mtsRecordParameters.setRECORD_PARAMETERS_ID(RandomUtil.getRandomId());
									}
									threadPoolTaskExecutor.execute(
											new ThreadPoolTaskForRecord(latch, mtsRecordInfo, mtsRecordParameters));
								}
								latch.await();
								currentNum = endNum;
							}
						}
					}
					System.out.println("===================等待所有线程执行完毕456==========================");
					System.out.println("===================执行完毕456==========================");
					mtsDBDataService.addMTSDBData(mtsDBData);
					long endTime = System.currentTimeMillis(); // 获取结束时间
					System.out.println("==============导入txt时间================" + endTime);
					System.out.println("==============导入txt方法总耗时： " + (endTime - startTime) + "ms");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			mv.addObject("msg", "success");
		}
		mv.setViewName("save_result");
		return mv;
	}

	@RequestMapping(value = "/standardizeData")
	public void standardizeData(PrintWriter out, HttpServletRequest request) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除matchRule");
		PageData pd = new PageData();
		pd = this.getPageData();
		String DB_DATA_ID = pd.getString("DB_DATA_ID");
		String DB_DATA_TYPE = pd.getString("DB_DATA_TYPE");

		mapConfigService.getMatchRuleCacheMap();

		if ("0".equals(DB_DATA_TYPE)) {
			if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
				MtsDBDataResult mtsDBDataResult = new MtsDBDataResult();
				mtsDBDataResult.setDB_DATA_ID(DB_DATA_ID);
				mtsDBDataService.deleteMtsDBDataResult(mtsDBDataResult);
			}

			MtsDBData mtsDBData = new MtsDBData();
			mtsDBData.setDB_DATA_ID(DB_DATA_ID);

			List<MtsDBData> mtsDBDataList = mtsDBDataService.findMTSDBData(mtsDBData);

			if (mtsDBDataList != null && mtsDBDataList.size() == 1) {
				mtsDBData = mtsDBDataList.get(0);
				String visitId = "";
				String visitType = "";
				String dataSource = mtsDBData.getDATA_SOURCE_CODE();
				String dataType = mtsDBData.getDATA_CLASS_ID();
				String parameters = mtsDBData.getDATA_TYPE_ID() + "@#&";
				String batchNum = mtsDBData.getBATCH_NUM();
				String application = "";
				String areaId = mtsDBData.getAREA_CODE();
				String diagName = "";
				MtsDBDataDetail mtsDBDataDetail = new MtsDBDataDetail();
				mtsDBDataDetail.setDB_DATA_ID(DB_DATA_ID);
				List<MtsDBDataDetail> DBDataDetailList = mtsDBDataService.findMTSDBDataDetail(mtsDBDataDetail);
				if (DBDataDetailList != null && DBDataDetailList.size() > 0) {
					mtsDBData.setSTATUS("2");
					mtsDBData.setOPERATE_TIME(new Date());
					mtsDBDataService.editMTSDBData(mtsDBData);
					mtsDBDataDetail.setDEAL_STATUS("0");
					mtsDBDataDetail.setOPERATE_TIME(new Date());
					mtsDBDataService.editMTSDBDataDetail(mtsDBDataDetail);
					CountDownLatch latch = new CountDownLatch(DBDataDetailList.size());
					for (int i = 0; i < DBDataDetailList.size(); i++) {
						mtsDBDataDetail = DBDataDetailList.get(i);
						diagName = mtsDBDataDetail.getDIAG_NAME();
						if (null != diagName && !"".equals(diagName) && !"null".equals(diagName)) {
							try {
								if ("02".equals(dataType)) {
									parameters = "05@#&" + diagName;
								} else if ("06".equals(dataType)) {
									String[] diagNameList = diagName.split("@#&");
									if (diagNameList != null && diagNameList.length == 2) {
										parameters = "01@#&" + diagNameList[0] + "@|&02@#&" + diagNameList[1];
									}
								} else if ("07".equals(dataType)) {
									String[] diagNameList = diagName.split("@#&");
									if (diagNameList != null && diagNameList.length == 6) {
										// parameters =
										// "19@#&"+diagNameList[0]+"@|&21@#&"+diagNameList[1]+"@|&20@#&"+diagNameList[2]+"@|&23@#&"+diagNameList[3]+"@|&03@#&"+diagNameList[4]+"@|&16@#&"+diagNameList[5]+"@|&18@#&"+diagNameList[6]+"@|&17@#&"+diagNameList[7];
										parameters = "19@#&" + diagNameList[0] + "@|&21@#&" + diagNameList[1]
												+ "@|&20@#&" + diagNameList[2] + "@|&23@#&" + diagNameList[3]
												+ "@|&03@#&" + diagNameList[4] + "@|&17@#&" + diagNameList[5];
										System.out.println("parameters=====123=====>>>" + parameters);
									}
								} else if ("03".equals(dataType)) {
									parameters = "30@#&" + diagName;
								} else if ("04".equals(dataType)) {
									parameters = "28@#&" + diagName;
								}

								String taskId = RandomUtil.getRandomId();

								FutureTask<String> futureTask = new FutureTask<String>(
										new ThreadPoolTaskForStandardizeData(latch, mtsDBDataDetail, visitId, visitType,
												dataSource, dataType, parameters, batchNum, application, areaId,
												diagName));
								threadPoolTaskExecutor.execute(futureTask);

							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							mtsDBDataDetail.setSTATUS("0");
							mtsDBDataDetail.setIMPORT_TIME(new Date());
							mtsDBDataDetail.setOPERATE_TIME(new Date());
							mtsDBDataDetail.setBATCH_NUM(batchNum);
							mtsDBDataDetail.setDEAL_STATUS("1");
							mtsDBDataDetail.setMARK("1");
							mtsDBDataService.editMTSDBDataDetail(mtsDBDataDetail);
						}
					}
					latch.await();
					mtsDBData.setSTATUS("1");
					mtsDBData.setOPERATE_TIME(new Date());
					mtsDBDataService.editMTSDBData(mtsDBData);
				}
			}
		} else if ("1".equals(DB_DATA_TYPE)) {
			if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
				MtsDBDataResult mtsDBDataResult = new MtsDBDataResult();
				mtsDBDataResult.setDB_DATA_ID(DB_DATA_ID);
				mtsDBDataService.deleteMtsDBDataResult(mtsDBDataResult);

				MtsRecordDetail recordDetail = new MtsRecordDetail();
				recordDetail.setDB_DATA_ID(DB_DATA_ID);
				mtsRecordService.deleteMtsRecordDetail(recordDetail);
			}

			MtsDBData mtsDBData = new MtsDBData();
			mtsDBData.setDB_DATA_ID(DB_DATA_ID);

			List<MtsDBData> mtsDBDataList = mtsDBDataService.findMTSDBData(mtsDBData);

			if (mtsDBDataList != null && mtsDBDataList.size() == 1) {
				mtsDBData = mtsDBDataList.get(0);
				String visitId = "";
				String visitType = "";
				String dataSource = mtsDBData.getDATA_SOURCE_CODE();
				String dataType = mtsDBData.getDATA_CLASS_ID();
				String parameters = "";
				String batchNum = mtsDBData.getBATCH_NUM();
				String application = "";
				String areaId = mtsDBData.getAREA_CODE();
				String diagName = "";

				MtsRecordInfo mtsRecordInfo = new MtsRecordInfo();
				mtsRecordInfo.setDB_DATA_ID(DB_DATA_ID);
				int countNum = mtsRecordService.findMtsRecordInfoCount(mtsRecordInfo);
				System.out.println("countNum===========>>>" + countNum);

				int currentNum = 0;
				int handleLength = 1000;
				if (countNum > 0) {
					mcs.getCacheMap();
					while (currentNum < countNum) {

						System.out.println("=========正在处理第" + currentNum + "条---第" + (currentNum + handleLength)
								+ "条数据==============");
						RecordInfoVO recordInfoVO = new RecordInfoVO();
						recordInfoVO.setDB_DATA_ID(DB_DATA_ID);
						recordInfoVO.setBeginNum(currentNum);
						recordInfoVO.setEndNum(handleLength);

						List<MtsRecordInfo> recordInfoList = mtsRecordService.findMtsRecordInfoByMark(recordInfoVO);
						if (recordInfoList != null && recordInfoList.size() > 0) {
							System.out.println("recordInfoList=========123=====>>>" + recordInfoList.size());
							CountDownLatch latch = new CountDownLatch(recordInfoList.size());
							for (int i = 0; i < recordInfoList.size(); i++) {
								MtsRecordInfo recordInfo = recordInfoList.get(i);
								MtsRecordParameters mtsRecordParameters = new MtsRecordParameters();
								String RECORD_INFO_ID = recordInfo.getRECORD_INFO_ID();
								mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
								List<MtsRecordParameters> recordParaList = mtsRecordService
										.findMtsRecordParameters(mtsRecordParameters);
								if (recordParaList != null && recordParaList.size() > 0) {
									for (int j = 0; j < recordParaList.size(); j++) {
										MtsRecordParameters recordParameters = recordParaList.get(j);
										if (recordParameters != null) {
											if (null != parameters && !"".equals(parameters)) {
												parameters = parameters + "@|&" + recordParameters.getPARAMETERS_TYPE()
														+ "@#&" + recordParameters.getPARAMETERS_CONTENT();
											} else {
												parameters = recordParameters.getPARAMETERS_TYPE() + "@#&"
														+ recordParameters.getPARAMETERS_CONTENT();
											}
										}
									}
								}
								threadPoolTaskExecutor.execute(new ThreadPoolTaskForStandardizeDataR(latch, visitId,
										visitType, dataSource, dataType, parameters, batchNum, application, areaId,
										diagName, RECORD_INFO_ID, DB_DATA_ID));
							}
							latch.await();
						}
						if ((currentNum + handleLength) < countNum) {
							currentNum = currentNum + handleLength;
						} else {
							currentNum = countNum;
						}
					}
				}
				mtsDBData.setSTATUS("1");
				mtsDBData.setOPERATE_TIME(new Date());
				mtsDBDataService.editMTSDBData(mtsDBData);
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
	@RequestMapping(value = "/delMtsDBData")
	public void delMtsDBData(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除DBData");
		PageData pd = new PageData();
		pd = this.getPageData();
		String DB_DATA_ID = pd.getString("DB_DATA_ID");

		if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
			MtsDBData mtsDBData = new MtsDBData();
			mtsDBData.setDB_DATA_ID(DB_DATA_ID);

			MtsDBDataDetail mtsDBDataDetail = new MtsDBDataDetail();
			mtsDBDataDetail.setDB_DATA_ID(DB_DATA_ID);

			MtsDBDataResult mtsDBDataResult = new MtsDBDataResult();
			mtsDBDataResult.setDB_DATA_ID(DB_DATA_ID);

			mtsDBDataService.deleteMtsDBDataResult(mtsDBDataResult);
			mtsDBDataService.deleteMTSDBDataDetail(mtsDBDataDetail);
			mtsDBDataService.deleteMTSDBData(mtsDBData);
		}

		out.write("success");
		out.close();
	}

	@ResponseBody
	@RequestMapping(value = "/downloadTemplate", produces = "application/json;charset=UTF-8")
	public ModelAndView downloadTemplate(Page page, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("结构化数据标准化模板");

		sheet.setColumnWidth((short) 1, (short) (35.7 * 150));
		sheet.setColumnWidth((short) 2, (short) (35.7 * 150));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新状态 liuwei
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStatus")
	public String updateStatus(String DB_DATA_ID_STRING) throws Exception {
		JSONObject jso = new JSONObject();
		String[] DB_DATA_ID_list;
		// List<DBDataDetailRes> resList = null ;
		Map<String, DBDataDetailRes> map = null;
		if ("".equals(DB_DATA_ID_STRING) || null == DB_DATA_ID_STRING) {
			return jso.toString();
		}
		DB_DATA_ID_list = DB_DATA_ID_STRING.split("@");
		if (DB_DATA_ID_list != null && DB_DATA_ID_list.length > 0) {
			map = new HashMap<String, DBDataDetailRes>();
			// resList = new ArrayList<DBDataDetailRes>();
			DBDataDetailRes DBDataDetailRes = null;
			for (int i = 0; i < DB_DATA_ID_list.length; i++) {
				String DB_DATA_ID = DB_DATA_ID_list[i];
				MtsDBData mtsDBData = new MtsDBData();
				mtsDBData.setDB_DATA_ID(DB_DATA_ID);
				List<MtsDBData> mtsDBDataList = mtsDBDataService.findMTSDBData(mtsDBData);
				if (mtsDBDataList != null && mtsDBDataList.size() == 1) {
					DBDataDetailRes = new DBDataDetailRes();
					mtsDBData = mtsDBDataList.get(0);
					String status = mtsDBData.getSTATUS();
					if ("2".equals(status)) {
						MtsDBDataDetail mtsDBDataDetail = new MtsDBDataDetail();
						mtsDBDataDetail.setDB_DATA_ID(DB_DATA_ID);
						int totalNum = mtsDBDataService.findMtsDBDataDetailCount(mtsDBDataDetail);
						mtsDBDataDetail.setDEAL_STATUS("1");
						int finishNum = mtsDBDataService.findMtsDBDataDetailCount(mtsDBDataDetail);
						// 创建一个数值格式化对象
						NumberFormat numberFormat = NumberFormat.getInstance();
						// 设置精确到小数点后2位
						numberFormat.setMaximumFractionDigits(0);
						String percentage = numberFormat.format((float) finishNum / (float) totalNum * 100);
						DBDataDetailRes.setDb_data_id(DB_DATA_ID);
						DBDataDetailRes.setStatus(status);
						DBDataDetailRes.setPercentage(percentage);
					} else if ("0".equals(status)) {
						DBDataDetailRes.setDb_data_id(DB_DATA_ID);
						DBDataDetailRes.setStatus(status);
						DBDataDetailRes.setPercentage("0");
					} else if ("1".equals(status)) {
						DBDataDetailRes.setDb_data_id(DB_DATA_ID);
						DBDataDetailRes.setStatus(status);
						DBDataDetailRes.setPercentage("100");
					}
					map.put(DB_DATA_ID, DBDataDetailRes);
				}
			}
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}

	/**
	 * 验证就诊类型
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editMtsRecordInfo", method = RequestMethod.POST)
	public Map<String, String> editMtsRecordInfo(String infoId, String batchNum, String XY_STANDARD_WORD,
			String XY_STANDARD_MAIN_CODE, String XY_STANDARD_ATTACH_CODE, String ZY_STANDARD_WORD,
			String ZY_STANDARD_MAIN_CODE, String SS_STANDARD_WORD, String SS_STANDARD_MAIN_CODE) throws Exception {

		System.out.println("infoId=========>>>" + infoId);
		System.out.println("batchNum=========>>>" + batchNum);
		System.out.println("XY_STANDARD_WORD=========>>>" + XY_STANDARD_WORD);
		System.out.println("XY_STANDARD_WORD=========>>>" + XY_STANDARD_MAIN_CODE);
		System.out.println("XY_STANDARD_WORD=========>>>" + XY_STANDARD_ATTACH_CODE);
		System.out.println("XY_STANDARD_WORD=========>>>" + ZY_STANDARD_WORD);
		System.out.println("XY_STANDARD_WORD=========>>>" + ZY_STANDARD_MAIN_CODE);
		System.out.println("XY_STANDARD_WORD=========>>>" + SS_STANDARD_WORD);
		System.out.println("XY_STANDARD_WORD=========>>>" + SS_STANDARD_MAIN_CODE);

		Map<String, String> resultMap = new HashMap<String, String>();
		String result = "false";
		MtsRecordDetail mtsRecordDetail = new MtsRecordDetail();
		mtsRecordDetail.setRECORD_DETAIL_ID(infoId);
		List<MtsRecordDetail> recordDetailList = mtsRecordService.findMtsRecordDetail(mtsRecordDetail);

		if (recordDetailList != null && recordDetailList.size() == 1) {
			mtsRecordDetail = recordDetailList.get(0);

			MtsRecordDetail recordDetail = new MtsRecordDetail();
			recordDetail.setXY_STANDARD_WORD(XY_STANDARD_WORD);
			recordDetail.setXY_STANDARD_MAIN_CODE(XY_STANDARD_MAIN_CODE);
			recordDetail.setXY_STANDARD_ATTACH_CODE(XY_STANDARD_ATTACH_CODE);
			recordDetail.setZY_STANDARD_WORD(ZY_STANDARD_WORD);
			recordDetail.setZY_STANDARD_MAIN_CODE(ZY_STANDARD_MAIN_CODE);
			recordDetail.setSS_STANDARD_WORD(SS_STANDARD_WORD);
			recordDetail.setSS_STANDARD_MAIN_CODE(SS_STANDARD_MAIN_CODE);
			recordDetail.setNLP_RESULT(mtsRecordDetail.getNLP_RESULT());
			recordDetail.setSTATUS("3");
			recordDetail.setTERMINOLOGY_TYPE("0");
			mtsRecordService.editMtsRecordDetail(recordDetail);
		}
		result = "true";
		resultMap.put("result", result);
		return resultMap;
	}
}
