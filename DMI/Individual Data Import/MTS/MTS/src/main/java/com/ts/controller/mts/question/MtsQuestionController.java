package com.ts.controller.mts.question;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsQuestion;
import com.ts.service.mts.question.MTSQuestionService;
import com.ts.util.PageData;
import com.ts.util.RandomUtil;

/**
 * 
 * @类名称: LoadRuleController
 * @类描述: Redis数据装载规则维护
 * @作者:李巍
 * @创建时间:2016年10月8日 下午2:04:26
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/mtsQuestion")
public class MtsQuestionController extends BaseController {

	String menuUrl = "mtsQuestion/listMtsQuestion.do"; // 菜单地址(权限用)
	@Resource(name = "MTSQuestionService")
	private MTSQuestionService mtsQuestionService;
	

	/**
	 * 显示问题单列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsQuestion")
	public ModelAndView listMtsQuestion(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		String startDate = pd.getString("startDate");
		String endDate = pd.getString("endDate");
		String BATCH_NUM = pd.getString("BATCH_NUM");
		
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM);
		}
		
		if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
			pd.put("startDate", startDate + " 00:00:00");
			pd.put("endDate", endDate + " 23:59:59");
		}
		pd.put("status", '0');
		page.setPd(pd);
		List<PageData> listMtsQuestion = mtsQuestionService.mtsQuestionlistPage(page); 
		
		mv.setViewName("mts/mtsquestion/mts_question_list");
		mv.addObject("listMtsQuestion", listMtsQuestion);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 显示问题单列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsQuestionForDBData")
	public ModelAndView listMtsQuestionForDBData(Page page,HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		
		if (null != BATCH_NUM && !"".equals(BATCH_NUM)) {
			pd.put("BATCH_NUM", BATCH_NUM);
		}
		pd.put("status", '0');
		page.setPd(pd);
		List<PageData> listMtsQuestion = mtsQuestionService.mtsQuestionlistPage(page); 
		
		mv.setViewName("mts/dbdata/mts_question_list");
		mv.addObject("listMtsQuestion", listMtsQuestion);
		mv.addObject("pd", pd);
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/exportTxt", produces = "application/json;charset=UTF-8")
	public ModelAndView exportTxt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String type = request.getParameter("type");
		String timestamp = request.getParameter("timestamp");
		String batchNum = RandomUtil.getRandomId();
		
		System.out.println("timestamp==================>>>>"+timestamp);
		if ("".equals(startDate) || null == startDate || "".equals(endDate) || null == endDate) {
			return null;
		}

		MtsQuestion mtsQuestion = new MtsQuestion();
		mtsQuestion.setSTART_DATE(startDate);
		mtsQuestion.setEND_DATE(endDate);
		if("0".equals(type)){
			mtsQuestion.setEXPORT_STATUS("0");
		}
		

		List<MtsQuestion> listMtsQuestion = mtsQuestionService.findMTSQuestion(mtsQuestion);
		String fileName = "MtsQuestion-"+batchNum+".txt";
		response.reset();// 不加这一句的话会出现下载错误
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
		response.setContentType("text/x-plain");// 定义输出类型
		try {
			ServletOutputStream out = response.getOutputStream();

			String path = System.getProperty("java.io.tmpdir") + "\\MtsQuestion-"+batchNum+".txt";
			File file = new File(path);
			FileOutputStream fos = new FileOutputStream(file);
			Writer writer = new OutputStreamWriter(fos, "utf-8");

			String text = "诊断";
			text += "\r\n";
			text += batchNum;
			text += "\r\n";

			if (listMtsQuestion != null && listMtsQuestion.size() > 0) {
				for (int i = 0; i < listMtsQuestion.size(); i++) {
					mtsQuestion = listMtsQuestion.get(i);
					text += mtsQuestion.getDATA_SOURCE() + "#q&#" + mtsQuestion.getDESCRIPTION() + "#q&#"+ mtsQuestion.getDATA_TYPE() + "#q&#"
							+ mtsQuestion.getVISIT_ID() + "#q&#" + mtsQuestion.getVISIT_TYPE() + "#q&#"
							+ mtsQuestion.getPARAMETERS() + "#q&#" + mtsQuestion.getNLP_RESULT() ;
					text += "\r\n";
				}
			}

			writer.write(text);
			writer.close();
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
			
			mtsQuestion = new MtsQuestion();
			mtsQuestion.setSTART_DATE(startDate);
			mtsQuestion.setEND_DATE(endDate);
			mtsQuestion.setEXPORT_STATUS("1");
			mtsQuestion.setEXPORT_BATCH_NUM(batchNum);
			mtsQuestionService.editMtsQuestionExportStatus(mtsQuestion);
			
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/exportTxtForDBData", produces = "application/json;charset=UTF-8")
	public ModelAndView exportTxtForDBData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");
		String batchNum = RandomUtil.getRandomId();
		String BATCH_NUM = request.getParameter("BATCH_NUM");
		MtsQuestion mtsQuestion = new MtsQuestion();
		mtsQuestion.setBATCH_NUM(BATCH_NUM);
		mtsQuestion.setSTATUS("0");
		if("0".equals(type)){
			mtsQuestion.setEXPORT_STATUS("0");
		}

		List<MtsQuestion> listMtsQuestion = mtsQuestionService.findMTSQuestion(mtsQuestion);
		String fileName = "MtsQuestion-"+BATCH_NUM+".txt";
		response.reset();// 不加这一句的话会出现下载错误
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
		response.setContentType("text/x-plain");// 定义输出类型

		try {
			ServletOutputStream out = response.getOutputStream();

			String path = System.getProperty("java.io.tmpdir") + "\\MtsQuestion-"+batchNum+".txt";
			File file = new File(path);
			FileOutputStream fos = new FileOutputStream(file);
			Writer writer = new OutputStreamWriter(fos, "utf-8");

			String text = "诊断";
			text += "\r\n";
			text += batchNum;
			text += "\r\n";

			if (listMtsQuestion != null && listMtsQuestion.size() > 0) {
				for (int i = 0; i < listMtsQuestion.size(); i++) {
					mtsQuestion = listMtsQuestion.get(i);
					text += mtsQuestion.getDATA_SOURCE() + "#q&#" + mtsQuestion.getDESCRIPTION() + "#q&#"+ mtsQuestion.getDATA_TYPE() + "#q&#"
							+ mtsQuestion.getVISIT_ID() + "#q&#" + mtsQuestion.getVISIT_TYPE() + "#q&#"
							+ mtsQuestion.getPARAMETERS() + "#q&#" + mtsQuestion.getNLP_RESULT() ;
					text += "\r\n";
				}
			}

			writer.write(text);
			writer.close();
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
			
			mtsQuestion = new MtsQuestion();
			mtsQuestion.setEXPORT_STATUS("1");
			mtsQuestion.setEXPORT_BATCH_NUM(batchNum);
			mtsQuestionService.editMtsQuestionExportStatus(mtsQuestion);
			
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
