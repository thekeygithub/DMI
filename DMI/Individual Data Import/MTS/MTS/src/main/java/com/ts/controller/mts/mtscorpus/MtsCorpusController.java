package com.ts.controller.mts.mtscorpus;

import com.ts.controller.base.BaseController;
import com.ts.controller.mts.loadrule.MtsDataController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsCorpus;
import com.ts.entity.mts.MtsCorpusDetail;
import com.ts.entity.mts.MtsCorpusEntity;
import com.ts.entity.mts.MtsData;
import com.ts.entity.mts.NlpTerm;
import com.ts.listener.RedisDataLoadListener;
import com.ts.plugin.TsHttpsClient;
import com.ts.service.mts.mtscorpus.MtsCorpusDetailManager;
import com.ts.service.mts.mtscorpus.MtsCorpusManager;
import com.ts.service.mts.mtscorpus.impl.MtsCorpusEntityService;
import com.ts.service.nlp.data.TermTypeManger;
import com.ts.util.AESSecurityUtil;
import com.ts.util.CommonUtils;
import com.ts.util.Const;
import com.ts.util.FileUpload;
import com.ts.util.FileUtil;
import com.ts.util.ObjectExcelRead;
import com.ts.util.PageData;
import com.ts.util.PathUtil;
import com.ts.util.QStringUtil;
import com.ts.util.StringUtil;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/mtsCorpus" })
public class MtsCorpusController extends BaseController {
	private static Logger logger = Logger.getLogger(MtsCorpusController.class);
	String menuUrl = "mtsCorpus/listMtsCorpus.do";

	@Resource(name = "mtsCorpusService")
	private MtsCorpusManager mtsCorpusManager;

	@Resource(name = "mtsCorpusDetailService")
	private MtsCorpusDetailManager mtsCorpusDetailManager;

	@Resource(name = "mtsCorpusEntityService")
	private MtsCorpusEntityService mtsCorpusEntityService;

	@Resource(name = "TermTypeService")
	private TermTypeManger termTypeManger;

	private static String uploadNLPCorpus;
	private static String traningNLPCorpus;
	private static String nlpTraningStatus;

	static {
		uploadNLPCorpus = CommonUtils.getPropValue("nlp.properties", "uploadNLPCorpus");
		traningNLPCorpus = CommonUtils.getPropValue("nlp.properties", "traningNLPCorpus");
		nlpTraningStatus = CommonUtils.getPropValue("nlp.properties", "nlpTraningStatus");
	}

	@RequestMapping({ "/goUploadXmlOrExcel" })
	public ModelAndView goUploadXmlOrExcel() throws Exception {
		ModelAndView mv = getModelAndView();
		mv.setViewName("mts/mtscorpus/export_xml");
		return mv;
	}

	@RequestMapping({ "/goUploadExcel" })
	public ModelAndView goUploadExcel() throws Exception {
		ModelAndView mv = getModelAndView();
		mv.setViewName("mts/mtscorpus/export_assert_excel");
		return mv;
	}

	/**
	 * 
	 * @方法名称: goEditMtsCorpus
	 * @功能描述: 进入语料编辑页面
	 * @作者:李巍
	 * @创建时间:2017年4月20日 上午9:10:23
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping({ "/goEditMtsCorpus" })
	public ModelAndView goEditMtsCorpus() throws Exception {
		ModelAndView mv = getModelAndView();
		String mtsCorpustId = this.getRequest().getParameter("id");
		MtsCorpus mtsCorpus = this.mtsCorpusManager.findMtsCorpusById(mtsCorpustId);
		List<PageData> listEntity = this.mtsCorpusEntityService.listMtsCorpusEntityByMtsCorpus(mtsCorpustId);
		List<NlpTerm> termList = termTypeManger.findAllLoadTerm();
		mv.addObject("listEntity", listEntity);
		mv.addObject("mtsCorpus", mtsCorpus);
		mv.addObject("termList", termList);
		mv.setViewName("mts/mtscorpus/mtscorpus_edit");
		return mv;
	}

	/**
	 * 去新增实体页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goUpdateEntity")
	public ModelAndView goUpdateEntity() throws Exception {
		PageData pd = this.getPageData();
		ModelAndView mv = this.getModelAndView();
		String detailId = pd.getString("detailId");
		List<PageData> entityList = this.mtsCorpusEntityService.listMtsCorpusEntityByMtsCorpusDetail(detailId);
		List<NlpTerm> termList = termTypeManger.findAllLoadTerm();
		mv.addObject("listEntity", entityList);
		mv.addObject("termList", termList);
		MtsCorpus mtsCorpus = new MtsCorpus();
		MtsCorpusDetail mtsCorpusDetail = this.mtsCorpusDetailManager.findMtsCorpusDetailById(detailId);
		String origCorpus = mtsCorpusDetail.getORIG_CORPUS();
		String pid = mtsCorpusDetail.getP_ID();
		String phtml = mtsCorpusDetail.getP_HTML();
		if (entityList.size() > 0) {
			mtsCorpus.setNEW_CONTENT(phtml);
		}
		mv.addObject("mtsCorpus", mtsCorpus);
		mv.addObject("corpusDetailId", pd.getString("detailId"));
		mv.setViewName("mts/mtscorpus/mtscorpus_assert_add");
		return mv;
	}

	@RequestMapping({ "/editMtsCorpus" })
	public ModelAndView editMtsCorpus() throws Exception {
		ModelAndView mv = getModelAndView();
		String corpusId = getRequest().getParameter("corpusId");
		String jsonContent = getRequest().getParameter("jsonContent");
		JSONObject myObj = JSONObject.fromObject(jsonContent);
		// 通过主ID 查找 语料明细
		String msg = this.mtsCorpusManager.editMtsCorpus(myObj, corpusId);
		mv.setViewName("save_result");
		mv.addObject("msg", msg);
		return mv;
	}

	/**
	 * 
	 * @方法名称: editAssertMtsCorpus
	 * @功能描述: 维护语料实体
	 * @作者:李巍
	 * @创建时间:2017年5月4日 上午8:55:30
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping({ "/editAssertMtsCorpus" })
	public ModelAndView editAssertMtsCorpus() throws Exception {
		ModelAndView mv = getModelAndView();
		String corpusDetailId = getRequest().getParameter("corpusDetailId");
		String jsonContent = getRequest().getParameter("jsonContent");
		JSONObject myobj = JSONObject.fromObject(jsonContent);
		// 通过主ID 查找 语料明细
		String msg = this.mtsCorpusDetailManager.editAssertMtsCorpus(myobj, corpusDetailId);
		mv.setViewName("save_result");
		mv.addObject("msg", msg);
		return mv;
	}

	/**
	 * 
	 * @方法名称: readXml
	 * @功能描述: 添加语料标注的实体
	 * @作者:李巍
	 * @创建时间:2017年5月4日 上午8:56:40
	 * @param file
	 * @return ModelAndView
	 */
	@RequestMapping({ "/readXml" })
	public ModelAndView readXml(@RequestParam(value = "xml", required = false) MultipartFile file) {
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
			String fileName = FileUpload.fileUp(file, filePath, "userxml");

			BufferedReader bf = new BufferedReader(new FileReader(filePath + fileName));
			StringBuffer sb = new StringBuffer();
			String valueString = null;
			int count = 0;
			while ((valueString = bf.readLine()) != null) {
				count++;
				sb.append("<p id=\"p" + count + "\">" + valueString + "</p>");
			}
			mv.addObject("fileName", file.getOriginalFilename());
			mv.addObject("fileFullName", filePath + fileName);
			mv.addObject("html", sb.toString());
			List<NlpTerm> termList = termTypeManger.findAllLoadTerm();
			mv.addObject("termList", termList);
			mv.setViewName("mts/mtscorpus/mtscorpus_add");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping({ "/listMtsCorpus" })
	public ModelAndView listMtsCorpus(Page page) throws Exception {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		List listMtsCorpus = this.mtsCorpusManager.listMtsCorpus(pd);
		mv.addObject("listMtsCorpus", listMtsCorpus);
		mv.setViewName("mts/mtscorpus/mtscorpus_list");
		return mv;
	}

	/**
	 * 
	 * @方法名称: listMtsCorpusDetailViewPage
	 * @功能描述: 根据原始语料模糊查询
	 * @作者:李巍
	 * @创建时间:2017年5月5日 上午10:20:59
	 * @param page
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping({ "/listMtsCorpusViewPage" })
	public ModelAndView listMtsCorpusViewPage(Page page) throws Exception {
		ModelAndView mv = getModelAndView();
		PageData pd = this.getPageData();
		List<PageData> listMtsCorpus = getListMtsCorpus(pd, page);
		// 实体类型
		mv.addObject("typeList", getTypeList());
		mv.addObject("listMtsCorpus", listMtsCorpus);
		mv.addObject("pd", pd);
		mv.addObject("listSize", listMtsCorpus.size());
		mv.setViewName("mts/mtscorpus/corpus_assert_list");
		return mv;
	}

	private List<PageData> getListMtsCorpus(PageData pd, Page page) throws Exception {
		List<PageData> listMtsCorpus = new ArrayList<PageData>();
		String accessType = pd.getString("ACCSESSTYPE");
		if (StringUtil.isNotBlank(pd.getString("CURRENTPAGE")) && Integer.parseInt(pd.getString("CURRENTPAGE")) > 1) {
			if (StringUtil.isNotBlank(pd.getString("ORIG_CORPUS"))
					&& StringUtil.isNotBlank(pd.getString("ENTITY_NAME"))) {
				accessType = pd.getString("ACCSESSTYPE");
			} else if (StringUtil.isNotBlank(pd.getString("ENTITY_NAME"))) {
				accessType = "2";
			} else if (StringUtil.isNotBlank(pd.getString("ORIG_CORPUS"))) {
				accessType = "1";
			} else if (StringUtil.isBlank(pd.getString("ORIG_CORPUS"))
					&& StringUtil.isBlank(pd.getString("ENTITY_NAME"))) {
				accessType = "3";
			}
		}
		if (StringUtil.isBlank(pd.getString("ORIG_CORPUS")) && StringUtil.isBlank(pd.getString("ENTITY_NAME"))
				&& (StringUtil.isNotBlank(pd.getString("START_DATE")) || StringUtil.isNotBlank(pd.getString("END_DATE"))
						|| StringUtil.isNotBlank(pd.getString("TYPE")))) {
			accessType = "3";
		}
		if ("1".equals(accessType) || StringUtil.isNotBlank(pd.getString("ORIG_CORPUS"))) {
			pd.put("ENTITY_NAME", "");
			page.setPd(pd);
			listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusDetailView(page);
		} else if ("2".equals(accessType) || StringUtil.isNotBlank(pd.getString("ENTITY_NAME"))) {
			pd.put("ORIG_CORPUS", "");
			page.setPd(pd);
			listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusEntityViewPage(page);
		} else if ("3".equals(accessType)) {
			pd.put("ENTITY_NAME", "");
			pd.put("ORIG_CORPUS", "");
			page.setPd(pd);
			listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusView(page);
		} else {
			listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusView(page);
		}
		return listMtsCorpus;
	}

	@RequestMapping({ "/exportCorpusExcel" })
	@ResponseBody
	public void exportCorpusExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 通过主表ID 查询
		PageData pd = this.getPageData();
		Page page = new Page();
		page.setShowCount(Integer.MAX_VALUE);
		page.setPd(pd);
		List<PageData> mtsCorpusDetailList = getListMtsCorpusForExcel(pd, page);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
		BufferedOutputStream buff = null;
		ServletOutputStream outSTr = null;
		try {
			outSTr = response.getOutputStream();
			buff = new BufferedOutputStream(outSTr);
			Map<String, List<List<String>>> data = new HashMap<String, List<List<String>>>();
			List<List<String>> rowList = new ArrayList<List<String>>();
			if (mtsCorpusDetailList.size() > 0) {
				for (int i = 0; i < mtsCorpusDetailList.size(); i++) {
					PageData detail = (PageData) mtsCorpusDetailList.get(i);
					List<String> cellList = new ArrayList<String>();
					String origCorpus = detail.getString("P_HTML");
					String entityArray = detail.getString("ENTITY_NAME");
					entityArray = entityArray.replaceAll(";", "\n");
					cellList.add(origCorpus);
					cellList.add(entityArray);
					rowList.add(cellList);
				}
			}
			data.put("Sheet1", rowList);
			ObjectExcelRead.writeExcel(outSTr, "xlsx", data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buff.close();
				outSTr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @方法名称: importExcel
	 * @功能描述: 语料导入
	 * @作者:李巍
	 * @创建时间:2017年5月16日 上午9:46:19
	 * @param file
	 * @param area
	 * @param batchNo
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/importCorpusExcel")
	public ModelAndView importCorpusExcel(@RequestParam(value = "excel", required = false) MultipartFile file,
			String area, String batchNo) throws Exception {
		ModelAndView mv = this.getModelAndView();
		String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
		MtsData md = new MtsData();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE; // 文件上传路径
			String fileName = FileUpload.fileUp(file, filePath, "userexcel"); // 执行上传
			List<PageData> listPd = (List) ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 0, 0, 0); // 执行读EXCEL操作,读出的数据导入List
			// 2:从第3行开始；0:从第A列开始；0:第0个sheet
			/* 存入数据库操作====================================== */
			/**
			 * var0 :ID var1 :标化类型 var2 :原始数据ID var3 :原始数据名称 var4 :大串
			 */
			fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";

			this.mtsCorpusDetailManager.importCorpusExcel(listPd, filePath, fileName);
			// 批次号

		}

		mv.setViewName("save_result");
		return mv;
	}

	private List<PageData> getListMtsCorpusForExcel(PageData pd, Page page) throws Exception {
		List<PageData> listMtsCorpus = new ArrayList<PageData>();
		String accessType = pd.getString("ACCSESSTYPE");
		if (StringUtil.isNotBlank(pd.getString("CURRENTPAGE")) && Integer.parseInt(pd.getString("CURRENTPAGE")) > 1) {
			if (StringUtil.isNotBlank(pd.getString("ORIG_CORPUS"))
					&& StringUtil.isNotBlank(pd.getString("ENTITY_NAME"))) {
				accessType = pd.getString("ACCSESSTYPE");
			} else if (StringUtil.isNotBlank(pd.getString("ENTITY_NAME"))) {
				accessType = "2";
			} else if (StringUtil.isNotBlank(pd.getString("ORIG_CORPUS"))) {
				accessType = "1";
			} else if (StringUtil.isBlank(pd.getString("ORIG_CORPUS"))
					&& StringUtil.isBlank(pd.getString("ENTITY_NAME"))) {
				accessType = "3";
			}
		}
		if (StringUtil.isBlank(pd.getString("ORIG_CORPUS")) && StringUtil.isBlank(pd.getString("ENTITY_NAME"))
				&& (StringUtil.isNotBlank(pd.getString("START_DATE")) || StringUtil.isNotBlank(pd.getString("END_DATE"))
						|| StringUtil.isNotBlank(pd.getString("TYPE")))) {
			accessType = "3";
		}
		if ("1".equals(accessType) || StringUtil.isNotBlank(pd.getString("ORIG_CORPUS"))) {
			pd.put("ENTITY_NAME", "");
			page.setPd(pd);
			listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusDetailViewForExcel(page);
		} else if ("2".equals(accessType) || StringUtil.isNotBlank(pd.getString("ENTITY_NAME"))) {
			pd.put("ORIG_CORPUS", "");
			page.setPd(pd);
			listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusEntityViewForExcel(page);
		} else if ("3".equals(accessType)) {
			pd.put("ENTITY_NAME", "");
			pd.put("ORIG_CORPUS", "");
			page.setPd(pd);
			listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusView(page);
		} else {
			listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusView(page);
		}
		return listMtsCorpus;
	}

	private List<PageData> getTypeList() {
		List<PageData> typeList = new ArrayList<PageData>();
		PageData pd2 = new PageData();
		pd2.put("CODE", "0");
		pd2.put("NAME", "AI更新");
		PageData pd3 = new PageData();
		pd3.put("CODE", "1");
		pd3.put("NAME", "人工标注");
		typeList.add(pd2);
		typeList.add(pd3);
		return typeList;
	}

	/**
	 * 
	 * @方法名称: listMtsCorpusEntityViewPage
	 * @功能描述: 根据实体名称精确查询
	 * @作者:李巍
	 * @创建时间:2017年5月5日 上午10:21:11
	 * @param page
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping({ "/listMtsCorpusEntityViewPage" })
	public ModelAndView listMtsCorpusEntityViewPage(Page page) throws Exception {
		ModelAndView mv = getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> listMtsCorpus = this.mtsCorpusDetailManager.listMtsCorpusEntityViewPage(page);
		mv.addObject("listMtsCorpus", listMtsCorpus);
		mv.addObject("pd", pd);
		mv.setViewName("mts/mtscorpus/corpus_assert_list");
		return mv;
	}

	@RequestMapping({ "/saveMtsCorpus" })
	public ModelAndView saveMtsCorpus() throws Exception {
		ModelAndView mv = getModelAndView();
		String jsonContent = getRequest().getParameter("jsonContent");
		JSONObject myObj = JSONObject.fromObject(jsonContent);
		this.mtsCorpusManager.saveMtsCorpus(myObj);
		mv.setViewName("save_result");
		mv.addObject("msg", "success");
		return mv;
	}

	@RequestMapping({ "/importAllMtsCorpus" })
	public ModelAndView importAllMtsCorpus() throws Exception {
		ModelAndView mv = getModelAndView();
		File dir = new File("C:/Users/heroliyaoa/Desktop/实体识别工具/全部原始语料/yl");
		try {
			this.mtsCorpusManager.importAllMtsCorpus(dir);
			mv.addObject("msg", "success");
		} catch (Exception e) {
			mv.addObject("msg", "fail");
			e.printStackTrace();
		}
		mv.setViewName("save_result");
		return mv;
	}

	public static List<File> getFileList(String strPath) throws Exception {
		File dir = new File(strPath);
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		List<File> filelist = new ArrayList<>();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName();
				if (files[i].isDirectory()) { // 判断是文件还是文件夹
					getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
				} else if (fileName.endsWith("txt")) { // 判断文件名是否以.avi结尾
					String strFileName = files[i].getAbsolutePath();
					BufferedReader bf = new BufferedReader(new FileReader(strFileName));
					byte[] bs = FileUtil.getContent(strFileName);
					String str = new String(bs);
					String[] strs = str.split("\\n");
					for (int j = 0; j < 1; j++) {
						String readLine = strs[j];
						String[] lines = readLine.split("\\t\\t\\t");
						// for (int k = 0; k < lines.length; k++) {
						//
						// }
						String[] line1s = lines[1].split("\\s\\t");
						for (int k = 0; k < line1s.length; k++) {
							// System.out.println(line1s[k]+"============");
							System.out.println(line1s[k].split("\\t")[3]);
						}
					}
				} else {
					continue;
				}
			}

		}
		return filelist;
	}

	@RequestMapping(value = { "/saveMtsCorpusForAI" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveMtsCorpusForAI(@RequestBody String toBeStandardStr) {
		try {
			toBeStandardStr = URLDecoder.decode(toBeStandardStr, "UTF-8");
			JSONObject myobj = JSONObject.fromObject(toBeStandardStr);
			String origCorpus = myobj.getString("origCorpus");
			JSONArray jsonArray = myobj.getJSONArray("entityList");
			MtsCorpusDetail corpusDetail = new MtsCorpusDetail();
			corpusDetail.setORIG_CORPUS(origCorpus);
			corpusDetail.setTYPE("2");
			corpusDetail.setEDIT_DATE(new Date());
			int count = this.mtsCorpusDetailManager.saveMtsCorpusDetail(corpusDetail).intValue();
			if (count > 0) {
				// int id = this.mtsCorpusDetailManager.maxId();
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					System.out.println(obj.getString("entityName"));
					MtsCorpusEntity mtsCorpusEntity = new MtsCorpusEntity();
					mtsCorpusEntity.setENTITY_NAME(obj.getString("entityName"));
					mtsCorpusEntity.setSTART_TEXT_OFF(obj.getString("startTextOff"));
					mtsCorpusEntity.setEND_TEXT_OFF(obj.getString("endTextOff"));
					mtsCorpusEntity.setENTITY_TYPE_NAME(obj.getString("entityTypeName"));
					mtsCorpusEntity.setCORPUS_DETAIL_ID(corpusDetail.getID());
					mtsCorpusEntity.setEDIT_DATE(new Date());
					this.mtsCorpusEntityService.saveMtsCorpusEntity(mtsCorpusEntity);
				}
				return Integer.valueOf(count);
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@RequestMapping({ "/goUploadXml" })
	public ModelAndView goUploadXml() throws Exception {
		ModelAndView mv = getModelAndView();
		mv.setViewName("mts/mtscorpus/uploadxml");
		return mv;
	}

	/**
	 * 
	 * @方法名称: exportTxt
	 * @功能描述: 人工语料标注 导出功能
	 * @作者:李巍
	 * @创建时间:2017年4月21日 上午11:39:52
	 * @param request
	 * @param response
	 * @param id
	 * @throws Exception
	 *             void
	 */
	@RequestMapping({ "/exportTxt" })
	@ResponseBody
	public void exportTxt(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
		// 通过主表ID 查询
		List<MtsCorpusDetail> mtsCorpusDetailList = this.mtsCorpusDetailManager
				.listMtsCorpusDetailByCorpus(Integer.parseInt(id));
		MtsCorpus corpusTag = this.mtsCorpusManager.findMtsCorpusById(id);
		response.setContentType("text/plain");
		String fileName = corpusTag.getFILE_NAME();
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".txt.ent");
		BufferedOutputStream buff = null;
		StringBuffer write = new StringBuffer();
		String enter = "\r\n";
		ServletOutputStream outSTr = null;
		try {
			outSTr = response.getOutputStream();
			buff = new BufferedOutputStream(outSTr);

			if (mtsCorpusDetailList.size() > 0) {
				for (int i = 0; i < mtsCorpusDetailList.size(); i++) {
					MtsCorpusDetail detail = (MtsCorpusDetail) mtsCorpusDetailList.get(i);
					write.append(detail.getORIG_CORPUS() + "\t\t\t");
					List entityList = this.mtsCorpusEntityService.listMtsCorpusEntity(Integer.valueOf(detail.getID()));
					for (int j = 0; j < entityList.size(); j++) {
						MtsCorpusEntity entity = (MtsCorpusEntity) entityList.get(j);
						write.append(entity.getENTITY_NAME() + "\t");
						write.append(entity.getSTART_TEXT_OFF() + "\t");
						write.append(entity.getEND_TEXT_OFF() + "\t");
						NlpTerm nlpTerm = this.termTypeManger.findNlpTermByName(entity.getENTITY_TYPE_NAME());
						if (j != entityList.size() - 1)
							write.append(nlpTerm.getTERM_EN_NAME() + "\t\t");
						else {
							write.append(nlpTerm.getTERM_EN_NAME());
						}
					}
					if (i != mtsCorpusDetailList.size() - 1) {
						write.append(enter);
					}
				}
			}
			buff.write(write.toString().getBytes("UTF-8"));
			buff.flush();
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buff.close();
				outSTr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping({ "/exportAITxt" })
	@ResponseBody
	public void exportAITxt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List mtsCorpusDetailList = this.mtsCorpusDetailManager.listMtsCorpusDetail();
		ModelAndView modelAndView = new ModelAndView();

		response.setContentType("text/plain");
		String fileName = "AI";
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".txt.ent");
		BufferedOutputStream buff = null;
		StringBuffer write = new StringBuffer();
		String enter = "\r\n";
		String tab = "\t";
		ServletOutputStream outSTr = null;
		try {
			outSTr = response.getOutputStream();
			buff = new BufferedOutputStream(outSTr);

			if (mtsCorpusDetailList.size() > 0) {
				for (int i = 0; i < mtsCorpusDetailList.size(); i++) {
					MtsCorpusDetail detail = (MtsCorpusDetail) mtsCorpusDetailList.get(i);
					write.append(detail.getORIG_CORPUS() + "\t\t\t");
					List entityList = this.mtsCorpusEntityService.listMtsCorpusEntity(Integer.valueOf(detail.getID()));
					for (int j = 0; j < entityList.size(); j++) {
						MtsCorpusEntity entity = (MtsCorpusEntity) entityList.get(j);
						write.append(entity.getENTITY_NAME() + "\t");
						write.append(entity.getSTART_TEXT_OFF() + "\t");
						write.append(entity.getEND_TEXT_OFF() + "\t");
						NlpTerm nlpTerm = this.termTypeManger.findNlpTermByName(entity.getENTITY_TYPE_NAME());
						if (j != entityList.size() - 1)
							write.append(nlpTerm.getTERM_EN_NAME() + "\t\t");
						else {
							write.append(nlpTerm.getTERM_EN_NAME());
						}
					}
					if (i != mtsCorpusDetailList.size() - 1) {
						write.append(enter);
					}
				}
			}
			buff.write(write.toString().getBytes("UTF-8"));
			buff.flush();
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buff.close();
				outSTr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/selEntityByDetail")
	public String selEntityByDetail() throws Exception {
		JSONObject jso = new JSONObject();
		Map<String, List> map = new HashMap<String, List>();
		PageData pd = this.getPageData();
		List<PageData> entityList = this.mtsCorpusEntityService
				.listMtsCorpusEntityByMtsCorpusDetail(pd.getString("CORPUS_DETAIL_ID"));
		if (entityList != null && entityList.size() > 0) {
			map.put("entityList", entityList);
		}
		jso = JSONObject.fromObject(map);
		return jso.toString();
	}

	/**
	 * 
	 * @方法名称: traningNLPCorpus
	 * @功能描述: 语料训练
	 * @作者:李巍
	 * @创建时间:2017年5月19日 上午8:53:56
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             String
	 */
	@RequestMapping({ "/traningNLPCorpus" })
	@ResponseBody
	public String traningNLPCorpus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 通过主表ID 查询
		String msg = "";
		PageData pd = this.getPageData();
		Page page = new Page();
		page.setShowCount(Integer.MAX_VALUE);
		page.setPd(pd);
		List<PageData> mtsCorpusDetailList = getListMtsCorpusForExcel(pd, page);
		JSONArray myArray = new JSONArray();
		if (mtsCorpusDetailList.size() > 0) {
			for (int i = 0; i < mtsCorpusDetailList.size(); i++) {
				PageData pddata = mtsCorpusDetailList.get(i);
				JSONObject detailObj = new JSONObject();
				// 原始语料
				detailObj.put("origCorpus", pddata.getString("P_HTML"));
				String entities = pddata.getString("ENTITY_NAME2");
				String entityOffs = pddata.getString("TEXT_OFF");
				String entitieTypeNames = pddata.getString("ENTITY_TYPE_NAME");
				String[] entitieNames = entities.split("&");
				String[] offs = entityOffs.split(";");
				String[] typeNames = entitieTypeNames.split(";");
				JSONArray entitiyArray = new JSONArray();
				for (int j = 0; j < entitieNames.length; j++) {
					JSONObject jobj = new JSONObject();
					jobj.put("entityName", entitieNames[j]);
					jobj.put("startOff", offs[j].split("_")[0]);
					jobj.put("endOff", offs[j].split("_")[1]);
					String Tp = RedisDataLoadListener.cnNameMap.get(typeNames[j]);
					jobj.put("entityType", Tp);
					if(null != Tp && !"".equals(Tp)){
						entitiyArray.add(jobj);
					}
				}
				detailObj.put("entityList", entitiyArray);
				myArray.add(detailObj);
			}
		}
		String message = TsHttpsClient.postMTSJsonSSL(uploadNLPCorpus, myArray.toString());// 操作成功返回1失败返回0
		if ("OK".equals(message)) {
			String status = TsHttpsClient.getRequest(nlpTraningStatus);
			if("Busy".equals(status)){
				msg = "busy";
			}else{
				// 开始训练
				message = TsHttpsClient.getRequest(traningNLPCorpus);
				if ("OK".equals(message)) {
					msg = "training success";
				} else {
					msg = "training fail";
				}
			}
		} else {
			msg = "upload fail";
		}
		return msg;
	}
}