package com.ts.controller.system.pay.inter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.pay.PbillManager;
import com.ts.service.pay.PbillitemManager;
import com.ts.service.pay.PdictManager;
import com.ts.service.pay.PdrugstoreManager;
import com.ts.service.pay.PfeeManager;
import com.ts.service.pay.PfundManager;
import com.ts.service.pay.PinsuinfoManager;
import com.ts.service.pay.PinteinfoManager;
import com.ts.service.pay.PoverdetailManager;
import com.ts.service.pay.PresultManager;
import com.ts.service.pay.PuserManager;
import com.ts.service.system.appuser.AppuserManager;
import com.ts.util.FaceUtils;
import com.ts.util.PageData;
import com.ts.util.Transformation;
import com.ts.util.ZipCompressor;

import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
@RequestMapping(value = "/interInfo")
public class InterInfoController extends BaseController {

	private static final String fileType = "zip";//文件类型 
	@Resource(name = "pdrugstoreService")
	private PdrugstoreManager pdrugstoreService;//药店支付信息
	
	@Resource(name = "pinteinfoService")
	private PinteinfoManager pinteinfoService;//接口数据信息
	
	@Resource(name = "puserService")
	private PuserManager puserService;//用户信息
	
	@Resource(name = "pinsuinfoService")
	private PinsuinfoManager pinsuinfoService;//参保人员信息
	
	@Resource(name = "pfundService")
	private PfundManager pfundService;//基金分段信息
	
	@Resource(name = "pfeeService")
	private PfeeManager pfeeService;//费用汇总信息
	
	@Resource(name = "poverdetailService")
	private PoverdetailManager poverdetailService;//超限明细表
	
	@Resource(name = "presultService")
	private PresultManager presultService;//计算结果信息
	
	@Resource(name = "pbillService")
	private PbillManager pbillService;//结算单据
	
	@Resource(name = "pbillitemService")
	private PbillitemManager pbillitemService;//收费项目
	
	@Resource(name = "pdictService")
	private PdictManager pdictService;//字典表
	
	@Resource(name="appuserService")
	private AppuserManager AppuserService;
	
	public PageData getPD(PageData pd){
		String keywords = pd.getString("keywords");
		if(null != keywords && !"".equals(keywords.trim())){
			pd.put("KEYWORDS", "%"+keywords.trim()+"%");
		}
		String final_date_start = pd.getString("final_date_start");
		if(final_date_start != null && !"".equals(final_date_start)){
			pd.put("FINAL_DATE_START", final_date_start+" 00:00:00");
		}
		String final_date_end = pd.getString("final_date_end");
		if(final_date_end != null && !"".equals(final_date_end)){
			pd.put("FINAL_DATE_END", final_date_end+" 23:59:59");
		}
		return pd;
	}
	
	@RequestMapping(value = "/dataList")
	@Rights(code = "interInfo/dataList")
	public ModelAndView dataList(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		
		mv.addObject("keywords", pd.getString("keywords"));
		mv.addObject("final_date_start", pd.getString("final_date_start"));
		mv.addObject("final_date_end", pd.getString("final_date_end"));
		mv.addObject("type", pd.getString("type"));
		
		pd = this.getPD(pd);
		page.setPd(pd);
		
		List<PageData> varList =new ArrayList<>();
		if("2".equals(pd.getString("type")))
			varList= pdrugstoreService.queryDrugstoreInterList(page);//药店支付的接口信息以“D”开头的接口信息
		else //默认的医院支付
			varList = pinteinfoService.queryInterList(page);//医保的接口列表 以“I”开头的接口信息
		
		List<PageData> sysUserList = AppuserService.findForPay();//ts_plat库sys_app_user表
		varList = getGroupNameList(varList, sysUserList);//添加group_name属性
		
		pd.put("business_type", "business_inter_code");
		pd.put("is_disable", "0");
		List<PageData> dictList = pdictService.queryDictList(pd);
		
		pd.put("business_type", "business_inter_code_ds");//药店业务的接口编号
		pd.put("is_disable", "0");
		List<PageData> dictList_ds = pdictService.queryDictList(pd);
		
		mv.setViewName("pay/inter/p_inte_info_list");
		mv.addObject("varList", varList);
		mv.addObject("dictList", dictList);
		mv.addObject("dictList_ds", dictList_ds);
		
		mv.addObject("sysUserList", sysUserList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	public List<PageData> getGroupNameList(List<PageData> varList, List<PageData> sysUserList){
		List<PageData> resultList = new ArrayList<PageData>();
		for(int i=0; i<varList.size(); i++){
			PageData resPD = getPageData();
			resPD.put("GROUP_NAME", "");
			
			PageData pd1 = (PageData)varList.get(i);
			String group1 = pd1.get("GROUP_ID").toString();
			
			for(int j=0; j<sysUserList.size(); j++){
				PageData pd2 = (PageData)sysUserList.get(j);
				String group2 = pd2.get("GROUP_ID").toString();
				if(group1.equals(group2)){
					resPD.put("GROUP_NAME", pd2.get("GROUP_NAME").toString());
					break;
				}
			}
			
			Iterator<PageData.Entry<String, String>> it = pd1.entrySet().iterator();
			while(it.hasNext()){
				PageData.Entry<String, String> entry = it.next();
				resPD.put(entry.getKey(), entry.getValue());
			}
			
			resultList.add(resPD);
		}
		return resultList;
	}
	
	@RequestMapping(value = "/interDetail")
	@Rights(code = "interInfo/interDetail")
	public ModelAndView interDetail() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		
		String interId = pd.getString("interId");
		if(interId != null && !"".equals(interId)){
			pd.put("ID", interId);
		}
		
		PageData varModel = pinteinfoService.findById(pd);
		varModel.put("GROUP_NAME", "");
		List<PageData> sysUserList = AppuserService.findForPay();//ts_plat库sys_app_user表
		for(int i=0; i<sysUserList.size(); i++){
			PageData pdUser = (PageData)sysUserList.get(i);
			if(pdUser.get("GROUP_ID").equals(varModel.get("GROUP_ID"))){
				varModel.put("GROUP_NAME", pdUser.get("GROUP_NAME"));
				break;
			}
		}
		
		mv.setViewName("pay/inter/p_inte_info_detail");
		mv.addObject("varModel", varModel);
		
		pd.put("interId", varModel.getString("ID"));
		pd.put("userId", varModel.getString("USER_ID"));
		mv.addObject("pd", pd);
		
		return mv;
	}
	
	@RequestMapping(value = "/userDetail")
	@Rights(code = "interInfo/userDetail")
	public ModelAndView userDetail() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		
		String userId = pd.getString("userId");
		if(userId != null && !"".equals(userId)){
			pd.put("ID", userId);
		}
		
		PageData userModel = puserService.searchUserId(pd);
		PageData insuModel = pinsuinfoService.findByUser(pd);
		mv.setViewName("pay/inter/p_user");
		mv.addObject("userModel", userModel);
		mv.addObject("insuModel", insuModel);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value = "/fundDetail")
	@Rights(code = "interInfo/fundDetail")
	public ModelAndView fundDetail(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = pfundService.queryFundList(page);
		mv.setViewName("pay/inter/p_fund_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value = "/feeDetail")
	@Rights(code = "interInfo/feeDetail")
	public ModelAndView feeDetail(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = pfeeService.queryFeeList(page);
		mv.setViewName("pay/inter/p_fee_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value = "/overDetail")
	@Rights(code = "interInfo/overDetail")
	public ModelAndView overDetail(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = poverdetailService.list(page);
		mv.setViewName("pay/inter/p_over_detail_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value = "/resultDetail")
	@Rights(code = "interInfo/resultDetail")
	public ModelAndView resultDetail(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List<PageData> varList = presultService.queryResultList(pd);
		
		PageData resultModel = new PageData();
		if(varList!=null && varList.size()>0){
			resultModel = (PageData)varList.get(0);
		}
		
		mv.setViewName("pay/inter/p_result_detail");
		mv.addObject("resultModel", resultModel);
		mv.addObject("pd", pd);
		
		return mv;
	}
	
	@RequestMapping(value = "/billDetail")
	@Rights(code = "interInfo/billDetail")
	public ModelAndView billDetail(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = pbillService.list(page);
		mv.setViewName("pay/inter/p_bill_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value = "/billItemDetail")
	@Rights(code = "interInfo/billItemDetail")
	public ModelAndView billItemDetail(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = pbillitemService.list(page);
		mv.setViewName("pay/inter/p_bill_item");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value = "/drugstoreInterDetail")
	@Rights(code = "interInfo/drugstoreInterDetail")
	public ModelAndView drugstoreInterDetail() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		
		String interId = pd.getString("interId");
		if(interId != null && !"".equals(interId)){
			pd.put("ID", interId);
		}
		
		PageData varModel = pdrugstoreService.findById_ds(pd);
		
		varModel.put("GROUP_NAME", "");
		List<PageData> sysUserList = AppuserService.findForPay();//ts_plat库sys_app_user表
		for(int i=0; i<sysUserList.size(); i++){
			PageData pdUser = (PageData)sysUserList.get(i);
			if(pdUser.get("GROUP_ID").equals(varModel.get("GROUP_ID"))){
				varModel.put("GROUP_NAME", pdUser.get("GROUP_NAME"));
				break;
			}
		}
		
		mv.setViewName("pay/inter/p_drugstore_inte_info_detail");
		mv.addObject("varModel", varModel);
		
		pd.put("interId", varModel.getString("ID"));
		pd.put("userId", varModel.getString("USER_ID"));
		mv.addObject("pd", pd);
		
		return mv;
	}
	@RequestMapping(value = "/drugstoreDrugDetail")
	@Rights(code = "interInfo/drugstoreDrugDetail")
	public ModelAndView drugstoreDrugDetail(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = pdrugstoreService.queryDrugItemList(page);
		mv.setViewName("pay/inter/p_drugstore_drug_item_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		return mv;
	}
	@RequestMapping(value = "/drugstoreResultDetail")
	@Rights(code = "interInfo/drugstoreResultDetail")
	public ModelAndView drugstoreResultDetail(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List<PageData> varList = pdrugstoreService.queryResultList(pd);
		
		PageData resultModel = new PageData();
		if(varList!=null && varList.size()>0){
			resultModel = (PageData)varList.get(0);
		}
		
		mv.setViewName("pay/inter/p_drugstore_result_detail");
		mv.addObject("resultModel", resultModel);
		mv.addObject("pd", pd);
		
		return mv;
	}
	//========================== 批量导出 excel ===========================================
	@RequestMapping(value = "/exporExcel")
	@Rights(code = "interInfo/exporExcel")
	public ModelAndView exporExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long millis = System.currentTimeMillis();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setCharacterEncoding("UTF-8");
		
		Date date = new Date();
	    long currentTime = date.getTime();
		String file_path = ReadPropertiesFiles.getValue("face_img_path");//excel存放地址
		System.out.println("=================导出开始，创建文件夹::"+file_path+currentTime+"=======================");
		FaceUtils.createFilePath(file_path+currentTime);
		
		PageData pd = this.getPageData();
		pd = this.getPD(pd);
		List<PageData> interList = pinteinfoService.queryInterListAll(pd);//接口数据信息
		List<PageData> sysUserList = AppuserService.findForPay();//ts_plat库sys_app_user表
		interList = getGroupNameList(interList, sysUserList);//添加group_name属性
		
		for(int i=0; i<interList.size(); i++){
			PageData param = (PageData)interList.get(i);
			
			String excel_path = file_path + currentTime + File.separator + param.getString("ID") + ".xls";
			OutputStream fos = new FileOutputStream(excel_path);
			WritableWorkbook workbook = Workbook.createWorkbook(fos);
			
			interExcel(workbook, param, sdformat);//接口数据详情
			userExcel(workbook, param, sdformat);//用户信息
			resultExcel(workbook, param, sdformat);//计算结果信息
			fundExcel(workbook, param, sdformat);//基金分段信息
			feeExcel(workbook, param, sdformat);//费用汇总信息
			overExcel(workbook, param, sdformat);//超限明细信息
			billExcel(workbook, param, sdformat);//结算单据信息
			
			workbook.write();
			workbook.close();
			fos.close();
		}
		//压缩文件夹
		ZipCompressor zca = new ZipCompressor(file_path+currentTime+"."+fileType);  
        zca.compressExe(file_path+currentTime);
		//删除文件夹
		File file = new File(file_path+currentTime);
		if(file.exists()){
			FaceUtils.deleteFile(file);
		}
		System.out.println("==========批量导出excel完毕，用时:" + (System.currentTimeMillis() - millis) + "毫秒==========");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(currentTime+"");
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}
	
	public void billExcel(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		PageData resultModel = new PageData();
		resultModel.put("INTERID", param.get("ID"));
		resultModel.put("USERID", param.get("USER_ID"));
		
		WritableSheet sheet = workbook.createSheet("结算单据信息", 6);
		sheet.setRowView(0, 300);
		int titleWidthIndex = 0;
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);

		int titleIndex = 0;
		Label a1 = new Label(titleIndex++, 0, "单据号码", styleInit()); sheet.addCell(a1);
		Label b1 = new Label(titleIndex++, 0, "科室代码", styleInit()); sheet.addCell(b1);
		Label c1 = new Label(titleIndex++, 0, "科室名称", styleInit()); sheet.addCell(c1);
		Label d1 = new Label(titleIndex++, 0, "医生编码", styleInit()); sheet.addCell(d1);
		Label e1 = new Label(titleIndex++, 0, "收费明细条数", styleInit()); sheet.addCell(e1);
		Label f1 = new Label(titleIndex++, 0, "药品诊疗类型", styleInit()); sheet.addCell(f1);
		Label g1 = new Label(titleIndex++, 0, "医院项目编码", styleInit()); sheet.addCell(g1);
		Label h1 = new Label(titleIndex++, 0, "医院项目名称", styleInit()); sheet.addCell(h1);
		Label i1 = new Label(titleIndex++, 0, "医院项目单位", styleInit()); sheet.addCell(i1);
		Label j1 = new Label(titleIndex++, 0, "医院项目规格", styleInit()); sheet.addCell(j1);
		Label k1 = new Label(titleIndex++, 0, "医院项目剂型", styleInit()); sheet.addCell(k1);
		Label l1 = new Label(titleIndex++, 0, "处方号", styleInit()); sheet.addCell(l1);
		Label m1 = new Label(titleIndex++, 0, "医院项目单价", styleInit()); sheet.addCell(m1);
		Label n1 = new Label(titleIndex++, 0, "贴数", styleInit()); sheet.addCell(n1);
		Label o1 = new Label(titleIndex++, 0, "数量", styleInit()); sheet.addCell(o1);
		Label p1 = new Label(titleIndex++, 0, "用药天数", styleInit()); sheet.addCell(p1);
		Label q1 = new Label(titleIndex++, 0, "总金额", styleInit()); sheet.addCell(q1);
		Label r1 = new Label(titleIndex++, 0, "超限自费标志", styleInit()); sheet.addCell(r1);
		Label s1 = new Label(titleIndex++, 0, "中心编码", styleInit()); sheet.addCell(s1);
		Label t1 = new Label(titleIndex++, 0, "医院项目生产厂家", styleInit()); sheet.addCell(t1);
		Label u1 = new Label(titleIndex++, 0, "医院项目转换比", styleInit()); sheet.addCell(u1);
		int beginRowIndex = 1;
		int endRowIndex = 0;
		List<PageData> billList = pbillService.searchBillListAll(resultModel);
		for(int i=0; i<billList.size(); i++){
			int cellIndex = 0;
			int endCellIndex = 0;
			endRowIndex = beginRowIndex;
			PageData pd = (PageData)billList.get(i);
			
			Label a2 = new Label(cellIndex, beginRowIndex, pd.getString("BILL_NO"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(a2);
			
			Label b2 = new Label(cellIndex, beginRowIndex, pd.getString("CODE1"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(b2);
			
			Label c2 = new Label(cellIndex, beginRowIndex, pd.getString("NAME1"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(c2);
			
			Label d2 = new Label(cellIndex, beginRowIndex, pd.getString("DOC_NO"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(d2);
			
			Label e2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("NUM1")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(e2);
			
			Label f2 = new Label(cellIndex, beginRowIndex, pd.getString("TYPE_NAME"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(f2);
			
			Label g2 = new Label(cellIndex, beginRowIndex, pd.getString("CODE2"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(g2);
			
			Label h2 = new Label(cellIndex, beginRowIndex, pd.getString("NAME2"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(h2);
			
			Label i2 = new Label(cellIndex, beginRowIndex, pd.getString("UNIT"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(i2);
			
			Label j2 = new Label(cellIndex, beginRowIndex, pd.getString("SPEC"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(j2);
			
			Label k2 = new Label(cellIndex, beginRowIndex, pd.getString("FORM"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(k2);
			
			Label l2 = new Label(cellIndex, beginRowIndex, pd.getString("RECIPE_NO"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(l2);
			
			Label m2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("PRICE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(m2);
			
			Label n2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("P_NUM")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(n2);
			
			Label o2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("NUM2")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(o2);
			
			Label p2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("USE_DAY")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(p2);
			
			Label q2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("FEE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(q2);
			
			String over_flag = "";
			if(pd.get("OVER_FLAG") != null){
				if("0".equals(pd.get("OVER_FLAG").toString())){
					over_flag = "正常";
				}
				if("1".equals(pd.get("OVER_FLAG").toString())){
					over_flag = "超限自费";
				}
			}
			Label r2 = new Label(cellIndex, beginRowIndex, over_flag);
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(r2);
			
			Label s2 = new Label(cellIndex, beginRowIndex, pd.getString("CEN_CODE"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(s2);
			
			Label t2 = new Label(cellIndex, beginRowIndex, pd.getString("COM"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(t2);
			
			Label u2 = new Label(cellIndex, beginRowIndex, pd.getString("PACK"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(u2);
			
			beginRowIndex = endRowIndex + 1;
		}
	}
	
	public void overExcel(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		PageData resultModel = new PageData();
		resultModel.put("INTERID", param.get("ID"));
		resultModel.put("USERID", param.get("USER_ID"));
		
		WritableSheet sheet = workbook.createSheet("超限明细信息", 5);
		sheet.setRowView(0, 300);
		int titleWidthIndex = 0;
		sheet.setColumnView(titleWidthIndex++, 20);
		sheet.setColumnView(titleWidthIndex++, 20);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 20);
		sheet.setColumnView(titleWidthIndex++, 20);
		sheet.setColumnView(titleWidthIndex++, 20);
		sheet.setColumnView(titleWidthIndex++, 20);

		int titleIndex = 0;
		Label a1 = new Label(titleIndex++, 0, "药品诊疗类型", styleInit()); sheet.addCell(a1);
		Label b1 = new Label(titleIndex++, 0, "医院项目编码", styleInit()); sheet.addCell(b1);
		Label c1 = new Label(titleIndex++, 0, "超限数量", styleInit()); sheet.addCell(c1);
		Label d1 = new Label(titleIndex++, 0, "超限自费金额", styleInit()); sheet.addCell(d1);
		Label e1 = new Label(titleIndex++, 0, "合计自费金额", styleInit()); sheet.addCell(e1);
		Label f1 = new Label(titleIndex++, 0, "超限原因代码", styleInit()); sheet.addCell(f1);
		Label g1 = new Label(titleIndex++, 0, "超限原因说明", styleInit()); sheet.addCell(g1);
		int beginRowIndex = 1;
		int endRowIndex = 0;
		
		List<PageData> overList = poverdetailService.listAll(resultModel);
		for(int i=0; i<overList.size(); i++){
			int cellIndex = 0;
			int endCellIndex = 0;
			endRowIndex = beginRowIndex;
			PageData pd = (PageData)overList.get(i);
			
			Label a2 = new Label(cellIndex, beginRowIndex, pd.getString("TYPE_NAME"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(a2);
			
			Label b2 = new Label(cellIndex, beginRowIndex, pd.getString("HOSP_NAME"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(b2);
			
			Label c3 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("NUM")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(c3);
			
			Label d4 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("SELF_FEE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(d4);
			
			Label e5 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("TOTAL_FEE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(e5);
			
			Label f6 = new Label(cellIndex, beginRowIndex, pd.getString("REA_CODE_NAME"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(f6);
			
			Label g7 = new Label(cellIndex, beginRowIndex, pd.getString("REA_DESC"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(g7);
			
			beginRowIndex = endRowIndex + 1;
		}
	}
	
	public void feeExcel(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		PageData resultModel = new PageData();
		resultModel.put("INTERID", param.get("ID"));
		resultModel.put("USERID", param.get("USER_ID"));
		
		WritableSheet sheet = workbook.createSheet("费用汇总信息", 4);
		sheet.setRowView(0, 300);
		int titleWidthIndex = 0;
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);

		int titleIndex = 0;
		Label a1 = new Label(titleIndex++, 0, "发票归类", styleInit()); sheet.addCell(a1);
		Label b1 = new Label(titleIndex++, 0, "归类名称", styleInit()); sheet.addCell(b1);
		Label c1 = new Label(titleIndex++, 0, "总费用", styleInit()); sheet.addCell(c1);
		Label d1 = new Label(titleIndex++, 0, "自费费用", styleInit()); sheet.addCell(d1);
		Label e1 = new Label(titleIndex++, 0, "自负费用", styleInit()); sheet.addCell(e1);
		int beginRowIndex = 1;
		int endRowIndex = 0;
		
		List<PageData> feeList = pfeeService.searchFeeListAll(resultModel);
		for(int i=0; i<feeList.size(); i++){
			int cellIndex = 0;
			int endCellIndex = 0;
			endRowIndex = beginRowIndex;
			PageData pd = (PageData)feeList.get(i);
			
			Label a2 = new Label(cellIndex, beginRowIndex, pd.getString("SORT"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(a2);
			
			Label b2 = new Label(cellIndex, beginRowIndex, pd.getString("NAME"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(b2);
			
			Label c3 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("TOTAL_FEE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(c3);
			
			Label d4 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("SELF_FEE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(d4);
			
			Label e5 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("NEG_FEE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(e5);
			
			beginRowIndex = endRowIndex + 1;
		}
	}
	
	public void fundExcel(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		PageData resultModel = new PageData();
		resultModel.put("INTERID", param.get("ID"));
		resultModel.put("USERID", param.get("USER_ID"));
		
		WritableSheet sheet = workbook.createSheet("基金分段信息", 3);
		sheet.setRowView(0, 300);
		int titleWidthIndex = 0;
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 20);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);

		int titleIndex = 0;
		Label a1 = new Label(titleIndex++, 0, "分段编码", styleInit()); sheet.addCell(a1);
		Label b1 = new Label(titleIndex++, 0, "分段名称", styleInit()); sheet.addCell(b1);
		Label c1 = new Label(titleIndex++, 0, "进入额度", styleInit()); sheet.addCell(c1);
		Label d1 = new Label(titleIndex++, 0, "分段基金支付金额", styleInit()); sheet.addCell(d1);
		Label e1 = new Label(titleIndex++, 0, "报销比例", styleInit()); sheet.addCell(e1);
		Label f1 = new Label(titleIndex++, 0, "分段自负金额", styleInit()); sheet.addCell(f1);
		Label g1 = new Label(titleIndex++, 0, "分段自费金额", styleInit()); sheet.addCell(g1);
		int beginRowIndex = 1;
		int endRowIndex = 0;
		
		List<PageData> fundList = pfundService.searchFundListAll(resultModel);
		for(int i=0; i<fundList.size(); i++){
			int cellIndex = 0;
			int endCellIndex = 0;
			endRowIndex = beginRowIndex;
			PageData pd = (PageData)fundList.get(i);
			
			Label a2 = new Label(cellIndex, beginRowIndex, pd.getString("CODE"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(a2);
			
			Label b2 = new Label(cellIndex, beginRowIndex, pd.getString("NAME"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(b2);
			
			Label c3 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("AMOUNT")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(c3);
			
			Label d4 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("PAY_AMOUNT")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(d4);
			
			Label e5 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("RATIO")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(e5);
			
			Label f6 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("SELF_AMOUNT")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(f6);
			
			Label g7 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("NEG_AMOUNT")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(g7);
			
			beginRowIndex = endRowIndex + 1;
		}
		
	}
	
	public void resultExcel(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		PageData resultModel = new PageData();
		resultModel.put("INTERID", param.get("ID"));
		resultModel.put("USERID", param.get("USER_ID"));
		List<PageData> resultList = presultService.queryResultList(resultModel);
		if(resultList!=null && resultList.size()>0){
			resultModel = (PageData)resultList.get(0);
		}
		WritableSheet sheet = workbook.createSheet("计算结果信息", 2);
		
		Label a1 = new Label(0, 0, "费用总额", styleInit());
		Label b1 = new Label(1, 0, Transformation.decimalToStr(resultModel.get("TOTAL_FEE")));
		Label c1 = new Label(2, 0, "自费总额(非医保)", styleInit());
		Label d1 = new Label(3, 0, Transformation.decimalToStr(resultModel.get("SELF_PAY")));
		sheet.addCell(a1);
		sheet.addCell(b1);
		sheet.addCell(c1);
		sheet.addCell(d1);
		
		Label a2 = new Label(0, 1, "药品乙类自负", styleInit());
		Label b2 = new Label(1, 1, Transformation.decimalToStr(resultModel.get("SELF_NEG")));
		Label c2 = new Label(2, 1, "医保费用", styleInit());
		Label d2 = new Label(3, 1, Transformation.decimalToStr(resultModel.get("MED_FEE")));
		sheet.addCell(a2);
		sheet.addCell(b2);
		sheet.addCell(c2);
		sheet.addCell(d2);
		
		Label a3 = new Label(0, 2, "转院自费", styleInit());
		Label b3 = new Label(1, 2, Transformation.decimalToStr(resultModel.get("TRAN_FEE")));
		Label c3 = new Label(2, 2, "起付线", styleInit());
		Label d3 = new Label(3, 2, Transformation.decimalToStr(resultModel.get("LEVEL_PAY")));
		sheet.addCell(a3);
		sheet.addCell(b3);
		sheet.addCell(c3);
		sheet.addCell(d3);
		
		Label a4 = new Label(0, 3, "个人自费现金支付", styleInit());
		Label b4 = new Label(1, 3, Transformation.decimalToStr(resultModel.get("SELF_CASH_PAY")));
		Label c4 = new Label(2, 3, "个人自负现金支付", styleInit());
		Label d4 = new Label(3, 3, Transformation.decimalToStr(resultModel.get("NEG_CASH_PAY")));
		sheet.addCell(a4);
		sheet.addCell(b4);
		sheet.addCell(c4);
		sheet.addCell(d4);
		
		Label a5 = new Label(0, 4, "合计现金支付", styleInit());
		Label b5 = new Label(1, 4, Transformation.decimalToStr(resultModel.get("CASH_TOTAL")));
		Label c5 = new Label(2, 4, "合计报销金额", styleInit());
		Label d5 = new Label(3, 4, Transformation.decimalToStr(resultModel.get("RETURN_FEE")));
		sheet.addCell(a5);
		sheet.addCell(b5);
		sheet.addCell(c5);
		sheet.addCell(d5);
		
		Label a6 = new Label(0, 5, "历年帐户支付", styleInit());
		Label b6 = new Label(1, 5, Transformation.decimalToStr(resultModel.get("I_PAY")));
		Label c6 = new Label(2, 5, "当年帐户支付", styleInit());
		Label d6 = new Label(3, 5, Transformation.decimalToStr(resultModel.get("O_PAY")));
		sheet.addCell(a6);
		sheet.addCell(b6);
		sheet.addCell(c6);
		sheet.addCell(d6);
		
		Label a7 = new Label(0, 6, "职保统筹基金支付", styleInit());
		Label b7 = new Label(1, 6, Transformation.decimalToStr(resultModel.get("WHOLE_PAY")));
		Label c7 = new Label(2, 6, "补充保险支付", styleInit());
		Label d7 = new Label(3, 6, Transformation.decimalToStr(resultModel.get("ADD_PAY")));
		sheet.addCell(a7);
		sheet.addCell(b7);
		sheet.addCell(c7);
		sheet.addCell(d7);
		
		Label a8 = new Label(0, 7, "公务员补助支付", styleInit());
		Label b8 = new Label(1, 7, Transformation.decimalToStr(resultModel.get("SER_PAY")));
		Label c8 = new Label(2, 7, "单位补助支付", styleInit());
		Label d8 = new Label(3, 7, Transformation.decimalToStr(resultModel.get("COM_PAY")));
		sheet.addCell(a8);
		sheet.addCell(b8);
		sheet.addCell(c8);
		sheet.addCell(d8);
		
		Label a9 = new Label(0, 8, "医疗救助支付", styleInit());
		Label b9 = new Label(1, 8, Transformation.decimalToStr(resultModel.get("SALV_PAY")));
		Label c9 = new Label(2, 8, "离休基金支付", styleInit());
		Label d9 = new Label(3, 8, Transformation.decimalToStr(resultModel.get("RETIRE_PAY")));
		sheet.addCell(a9);
		sheet.addCell(b9);
		sheet.addCell(c9);
		sheet.addCell(d9);
		
		Label a10 = new Label(0, 9, "二乙基金支付", styleInit());
		Label b10 = new Label(1, 9, Transformation.decimalToStr(resultModel.get("FUND_PAY")));
		Label c10 = new Label(2, 9, "劳模医疗补助支付", styleInit());
		Label d10 = new Label(3, 9, Transformation.decimalToStr(resultModel.get("MODEL_PAY")));
		sheet.addCell(a10);
		sheet.addCell(b10);
		sheet.addCell(c10);
		sheet.addCell(d10);
		
		Label a11 = new Label(0, 10, "民政救助支付", styleInit());
		Label b11 = new Label(1, 10, Transformation.decimalToStr(resultModel.get("CIVIL_PAY")));
		Label c11 = new Label(2, 10, "优抚救助支付", styleInit());
		Label d11 = new Label(3, 10, Transformation.decimalToStr(resultModel.get("PRIV_PAY")));
		sheet.addCell(a11);
		sheet.addCell(b11);
		sheet.addCell(c11);
		sheet.addCell(d11);
		
		Label a12 = new Label(0, 11, "残联基金支付", styleInit());
		Label b12 = new Label(1, 11, Transformation.decimalToStr(resultModel.get("DPF_PAY")));
		Label c12 = new Label(2, 11, "计生基金支付", styleInit());
		Label d12 = new Label(3, 11, Transformation.decimalToStr(resultModel.get("PLAN_PAY")));
		sheet.addCell(a12);
		sheet.addCell(b12);
		sheet.addCell(c12);
		sheet.addCell(d12);
		
		Label a13 = new Label(0, 12, "工伤基金支付", styleInit());
		Label b13 = new Label(1, 12, Transformation.decimalToStr(resultModel.get("HURT_PAY")));
		Label c13 = new Label(2, 12, "生育基金支付", styleInit());
		Label d13 = new Label(3, 12, Transformation.decimalToStr(resultModel.get("PROC_PAY")));
		sheet.addCell(a13);
		sheet.addCell(b13);
		sheet.addCell(c13);
		sheet.addCell(d13);
		
		Label a14 = new Label(0, 13, "结算后当年个帐余额", styleInit());
		Label b14 = new Label(1, 13, Transformation.decimalToStr(resultModel.get("I_BALANCE")));
		Label c14 = new Label(2, 13, "结算后历年个帐余额", styleInit());
		Label d14 = new Label(3, 13, Transformation.decimalToStr(resultModel.get("O_BALANCE")));
		sheet.addCell(a14);
		sheet.addCell(b14);
		sheet.addCell(c14);
		sheet.addCell(d14);
		
		Label a15 = new Label(0, 14, "合保统筹基金支付", styleInit());
		Label b15 = new Label(1, 14, Transformation.decimalToStr(resultModel.get("INSU_PAY")));
		Label c15 = new Label(2, 14, "合保大病救助支付", styleInit());
		Label d15 = new Label(3, 14, Transformation.decimalToStr(resultModel.get("INSU_SALV_PAY")));
		sheet.addCell(a15);
		sheet.addCell(b15);
		sheet.addCell(c15);
		sheet.addCell(d15);
		
		Label a16 = new Label(0, 15, "慈善基金", styleInit());
		Label b16 = new Label(1, 15, Transformation.decimalToStr(resultModel.get("CHARITY")));
		Label c16 = new Label(2, 15, "共济账户支出", styleInit());
		Label d16 = new Label(3, 15, Transformation.decimalToStr(resultModel.get("MUTUAL")));
		sheet.addCell(a16);
		sheet.addCell(b16);
		sheet.addCell(c16);
		sheet.addCell(d16);
	}
	
	public void userExcel(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		PageData pd = new PageData();
		pd.put("INTERID", param.get("ID"));
		pd.put("ID", param.get("USER_ID"));
		pd.put("USERID", param.get("USER_ID"));
		PageData userModel = puserService.searchUserId(pd);
		PageData insuModel = pinsuinfoService.findByUser(pd);
		
		WritableSheet sheet = workbook.createSheet("用户信息", 1);
		
//		Label a1 = new Label(0, 0, "业务接口编号", styleInit());
//		Label b1 = new Label(1, 0, userModel.getString("API_TYPE"));
//		Label c1 = new Label(2, 0, "请求流水号", styleInit());
//		Label d1 = new Label(3, 0, userModel.getString("REQ_NO"));
//		sheet.addCell(a1);
//		sheet.addCell(b1);
//		sheet.addCell(c1);
//		sheet.addCell(d1);
		
//		Label a2 = new Label(0, 1, "时间戳", styleInit());
//		Label b2 = new Label(1, 1, userModel.getString("TIME_STAMP"));
		
		Label a1 = new Label(0, 0, "卡号", styleInit());
		Label b1 = new Label(1, 0, insuModel.getString("MED_CARD"));
		Label c1 = new Label(2, 0, "身份证", styleInit());
		Label d1 = new Label(3, 0, userModel.getString("ID_CARD"));
		sheet.addCell(a1);
		sheet.addCell(b1);
		sheet.addCell(c1);
		sheet.addCell(d1);
		
		Label a2 = new Label(0, 1, "市民卡号", styleInit());
		Label b2 = new Label(1, 1, userModel.getString("CARD_NO"));
		Label c2 = new Label(2, 1, "识别码", styleInit());
		Label d2 = new Label(3, 1, userModel.getString("IC_CARD"));
		sheet.addCell(a2);
		sheet.addCell(b2);
		sheet.addCell(c2);
		sheet.addCell(d2);
		
		Label a3 = new Label(0, 2, "用户姓名", styleInit());
		Label b3 = new Label(1, 2, userModel.getString("NAME"));
		Label c3 = new Label(2, 2, "卡状态", styleInit());
		Label d3 = new Label(3, 2, userModel.getString("CARD_STAT_NAME"));
		sheet.addCell(a3);
		sheet.addCell(b3);
		sheet.addCell(c3);
		sheet.addCell(d3);
		
		Label a4 = new Label(0, 3, "银行卡信息", styleInit());
		Label b4 = new Label(1, 3, insuModel.getString("BANK_NO"));
		Label c4 = new Label(2, 3, "读卡方式", styleInit());
		Label d4 = new Label(3, 3, insuModel.getString("READ_TYPE"));
		sheet.addCell(a4);
		sheet.addCell(b4);
		sheet.addCell(c4);
		sheet.addCell(d4);
		
//		Label a6 = new Label(0, 5, "医院名称", styleInit());
//		Label b6 = new Label(1, 5, insuModel.getString("HOSP_NAME"));
//		Label c6 = new Label(2, 5, "卡号", styleInit());
//		Label d6 = new Label(3, 5, insuModel.getString("MED_CARD"));
//		sheet.addCell(a6);
//		sheet.addCell(b6);
//		sheet.addCell(c6);
//		sheet.addCell(d6);
		
		Label a5 = new Label(0, 4, "个人社保编号", styleInit());
		Label b5 = new Label(1, 4, insuModel.getString("INSU_NO"));
		Label c5 = new Label(2, 4, "性别", styleInit());
		Label d5 = new Label(3, 4, insuModel.getString("SEX_NAME"));
		sheet.addCell(a5);
		sheet.addCell(b5);
		sheet.addCell(c5);
		sheet.addCell(d5);
		
		Label a6 = new Label(0, 5, "民族", styleInit());
		Label b6 = new Label(1, 5, insuModel.getString("NATION"));
		Label c6 = new Label(2, 5, "出生日期", styleInit());
		Label d6 = new Label(3, 5, insuModel.getString("BIRTH"));
		sheet.addCell(a6);
		sheet.addCell(b6);
		sheet.addCell(c6);
		sheet.addCell(d6);
		
		Label a7 = new Label(0, 6, "单位性质", styleInit());
		Label b7 = new Label(1, 6, insuModel.getString("COM"));
		Label c7 = new Label(2, 6, "单位名称/家庭地址", styleInit());
		Label d7 = new Label(3, 6, insuModel.getString("NAME_ADDR"));
		sheet.addCell(a7);
		sheet.addCell(b7);
		sheet.addCell(c7);
		sheet.addCell(d7);
		
		Label a8 = new Label(0, 7, "地区编码", styleInit());
		Label b8 = new Label(1, 7, insuModel.getString("AREA_CODE"));
		Label c8 = new Label(2, 7, "地区名称", styleInit());
		Label d8 = new Label(3, 7, insuModel.getString("AREA_NAME"));
		sheet.addCell(a8);
		sheet.addCell(b8);
		sheet.addCell(c8);
		sheet.addCell(d8);
		
		Label a9 = new Label(0, 8, "医保待遇（政策）类别，结算依据", styleInit());
		Label b9 = new Label(1, 8, insuModel.getString("MED_NAME"));
		Label c9 = new Label(2, 8, "荣誉类别", styleInit());
		Label d9 = new Label(3, 8, insuModel.getString("HONOR_NAME"));
		sheet.addCell(a9);
		sheet.addCell(b9);
		sheet.addCell(c9);
		sheet.addCell(d9);
		
		Label a10 = new Label(0, 9, "低保类别", styleInit());
		Label b10 = new Label(1, 9, insuModel.getString("LOW_NAME"));
		Label c10 = new Label(2, 9, "优抚级别", styleInit());
		Label d10 = new Label(3, 9, insuModel.getString("PRIV_RANK_NAME"));
		sheet.addCell(a10);
		sheet.addCell(b10);
		sheet.addCell(c10);
		sheet.addCell(d10);
		
		Label a11 = new Label(0, 10, "特殊病标志", styleInit());
		String spec_flag = "";
		if(insuModel.get("SPEC_FLAG") != null){
			if("1".equals(insuModel.getString("SPEC_FLAG"))){
				spec_flag = "中心有特殊病登记";
			}
			if("0".equals(insuModel.getString("SPEC_FLAG"))){
				spec_flag = "没有登记";
			}
		}
		Label b11 = new Label(1, 10, spec_flag);
		Label c11 = new Label(2, 10, "特殊病编码", styleInit());
		Label d11 = new Label(3, 10, insuModel.getString("SPEC_CODE"));
		sheet.addCell(a11);
		sheet.addCell(b11);
		sheet.addCell(c11);
		sheet.addCell(d11);
		
		Label a12 = new Label(0, 11, "当年帐户余额", styleInit());
		Label b12 = new Label(1, 11, Transformation.decimalToStr(insuModel.get("I_BALANCE")));
		Label c12 = new Label(2, 11, "历年帐户余额", styleInit());
		Label d12 = new Label(3, 11, Transformation.decimalToStr(insuModel.get("O_BALANCE")));
		sheet.addCell(a12);
		sheet.addCell(b12);
		sheet.addCell(c12);
		sheet.addCell(d12);
		
		Label a13 = new Label(0, 12, "当年住院医保累计", styleInit());
		Label b13 = new Label(1, 12, Transformation.decimalToStr(insuModel.get("OUT_TOTAL")));
		Label c13 = new Label(2, 12, "当年门诊医保累计", styleInit());
		Label d13 = new Label(3, 12, Transformation.decimalToStr(insuModel.get("IN_TOTAL")));
		sheet.addCell(a13);
		sheet.addCell(b13);
		sheet.addCell(c13);
		sheet.addCell(d13);
		
		Label a14 = new Label(0, 13, "当年规定病医保累计", styleInit());
		Label b14 = new Label(1, 13, Transformation.decimalToStr(insuModel.get("DIS_TOTAL")));
		Label c14 = new Label(2, 13, "当年累计列入统筹基数", styleInit());
		Label d14 = new Label(3, 13, Transformation.decimalToStr(insuModel.get("WHOLE")));
		sheet.addCell(a14);
		sheet.addCell(b14);
		sheet.addCell(c14);
		sheet.addCell(d14);
		
		Label a15 = new Label(0, 14, "当年统筹基金支付累计", styleInit());
		Label b15 = new Label(1, 14, Transformation.decimalToStr(insuModel.get("WHOLE_PAY")));
		Label c15 = new Label(2, 14, "当年补充保险支付累计", styleInit());
		Label d15 = new Label(3, 14, Transformation.decimalToStr(insuModel.get("INSU_PAY")));
		sheet.addCell(a15);
		sheet.addCell(b15);
		sheet.addCell(c15);
		sheet.addCell(d15);
		
		Label a16 = new Label(0, 15, "当年公务员补助支付累计", styleInit());
		Label b16 = new Label(1, 15, Transformation.decimalToStr(insuModel.get("SER_PAY")));
		Label c16 = new Label(2, 15, "当年企事业补助支付累计", styleInit());
		Label d16 = new Label(3, 15, Transformation.decimalToStr(insuModel.get("COM_PAY")));
		sheet.addCell(a16);
		sheet.addCell(b16);
		sheet.addCell(c16);
		sheet.addCell(d16);
		
		Label a17 = new Label(0, 16, "当年专项基金支付累计", styleInit());
		Label b17 = new Label(1, 16, Transformation.decimalToStr(insuModel.get("SPEC_PAY")));
		Label c17 = new Label(2, 16, "当年住院次数", styleInit());
		Label d17 = new Label(3, 16, Transformation.decimalToStr(insuModel.get("IN_COUNT")));
		sheet.addCell(a17);
		sheet.addCell(b17);
		sheet.addCell(c17);
		sheet.addCell(d17);
		
		Label a18 = new Label(0, 17, "工伤认定部位", styleInit());
		Label b18 = new Label(1, 17, insuModel.getString("PART"));
		Label c18 = new Label(2, 17, "医疗小险种", styleInit());
		Label d18 = new Label(3, 17, insuModel.getString("LITTLE_INSU_NAME"));
		sheet.addCell(a18);
		sheet.addCell(b18);
		sheet.addCell(c18);
		sheet.addCell(d18);
		
		Label a19 = new Label(0, 18, "交易状态", styleInit());
		String deal_stat = "失败";
		if(insuModel.get("DEAL_STAT")!=null && "0".equals(insuModel.get("DEAL_STAT").toString())){
			deal_stat = "成功";
		}
		Label b19 = new Label(1, 18, deal_stat);
		Label c19 = new Label(2, 18, "错误信息", styleInit());
		Label d19 = new Label(3, 18, insuModel.getString("ERROR"));
		sheet.addCell(a19);
		sheet.addCell(b19);
		sheet.addCell(c19);
		sheet.addCell(d19);
		
		Label a20 = new Label(0, 19, "写社会保障卡结果", styleInit());
		String card_res = "写卡错误";
		if(insuModel.get("CARD_RES")!=null && "0".equals(insuModel.get("CARD_RES").toString())){
			card_res = "不写或写卡成功";
		}
		Label b20 = new Label(1, 19, card_res);
		Label c20 = new Label(2, 19, "扣银行卡结果", styleInit());
		String bank_res = "失败";
		if(insuModel.get("BANK_RES")!=null && "0".equals(insuModel.get("BANK_RES").toString())){
			bank_res = "不扣或扣成功";
		}
		Label d20 = new Label(3, 19, bank_res);
		sheet.addCell(a20);
		sheet.addCell(b20);
		sheet.addCell(c20);
		sheet.addCell(d20);
		
		Label a21 = new Label(0, 20, "更新后IC卡数据", styleInit());
		Label b21 = new Label(1, 20, insuModel.getString("IC_DATA"));
		Label c21 = new Label(2, 20, "医疗身份验证结果", styleInit());
		Label d21 = new Label(3, 20, insuModel.getString("MED_RES"));
		sheet.addCell(a21);
		sheet.addCell(b21);
		sheet.addCell(c21);
		sheet.addCell(d21);
		
		Label a22 = new Label(0, 21, "工伤身份验证结果", styleInit());
		Label b22 = new Label(1, 21, insuModel.getString("HURT_RES"));
		Label c22 = new Label(2, 21, "生育身份验证结果", styleInit());
		Label d22 = new Label(3, 21, insuModel.getString("PROC_RES"));
		sheet.addCell(a22);
		sheet.addCell(b22);
		sheet.addCell(c22);
		sheet.addCell(d22);
		
		Label a23 = new Label(0, 22, "市民卡社会保障卡号", styleInit());
		Label b23 = new Label(1, 22, insuModel.getString("CARD_NO"));
		sheet.addCell(a23);
		sheet.addCell(b23);
	}
	
	public void interExcel(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		WritableSheet sheet = workbook.createSheet("接口数据详情", 0);
		
		Label a1 = new Label(0, 0, "业务接口编号", styleInit());
		Label b1 = new Label(1, 0, param.getString("API_TYPE"));
		Label c1 = new Label(2, 0, "数据类型", styleInit());
		Label d1 = new Label(3, 0, param.getString("DATA_TYPE_NAME"));
		sheet.addCell(a1);
		sheet.addCell(b1);
		sheet.addCell(c1);
		sheet.addCell(d1);
		
		Label a2 = new Label(0, 1, "机构名称", styleInit());
		Label b2 = new Label(1, 1, param.getString("GROUP_NAME"));
		Label c2 = new Label(2, 1, "附加消息", styleInit());
		Label d2 = new Label(3, 1, param.getString("ADD_INFO"));
		sheet.addCell(a2);
		sheet.addCell(b2);
		sheet.addCell(c2);
		sheet.addCell(d2);
		
		Label a3 = new Label(0, 2, "请求流水号", styleInit());
		Label b3 = new Label(1, 2, param.getString("REQ_NO"));
		Label c3 = new Label(2, 2, "现金支付方式", styleInit());
		Label d3 = new Label(3, 2, param.getString("PAY_TYPE_NAME"));
		sheet.addCell(a3);
		sheet.addCell(b3);
		sheet.addCell(c3);
		sheet.addCell(d3);
		
		Label a4 = new Label(0, 3, "时间戳", styleInit());
		Label b4 = new Label(1, 3, param.getString("TIME_STAMP"));
		Label c4 = new Label(2, 3, "银行卡信息", styleInit());
		Label d4 = new Label(3, 3, param.getString("BANK_NO"));
		sheet.addCell(a4);
		sheet.addCell(b4);
		sheet.addCell(c4);
		sheet.addCell(d4);
		
		Label a5 = new Label(0, 4, "医院名称", styleInit());
		Label b5 = new Label(1, 4, param.getString("HOSP_NAME"));
		Label c5 = new Label(2, 4, "业务类型", styleInit());
		Label d5 = new Label(3, 4, param.getString("BUSI_TYPE_NAME"));
		sheet.addCell(a5);
		sheet.addCell(b5);
		sheet.addCell(c5);
		sheet.addCell(d5);
		
		Label a6 = new Label(0, 5, "医院交易流水号", styleInit());
		Label b6 = new Label(1, 5, param.getString("VISIT_NO"));
		Label c6 = new Label(2, 5, "疾病编号", styleInit());
		Label d6 = new Label(3, 5, param.getString("DIS_CODE"));
		sheet.addCell(a6);
		sheet.addCell(b6);
		sheet.addCell(c6);
		sheet.addCell(d6);
		
		Label a7 = new Label(0, 6, "登记编号", styleInit());
		Label b7 = new Label(1, 6, param.getString("REG_NO"));
		Label c7 = new Label(2, 6, "病种审批号", styleInit());
		Label d7 = new Label(3, 6, param.getString("APPR_NO"));
		sheet.addCell(a7);
		sheet.addCell(b7);
		sheet.addCell(c7);
		sheet.addCell(d7);
		
		Label a8 = new Label(0, 7, "疾病名称", styleInit());
		Label b8 = new Label(1, 7, param.getString("DIS_NAME"));
		Label c8 = new Label(2, 7, "疾病描述", styleInit());
		Label d8 = new Label(3, 7, param.getString("DIS_DESC"));
		sheet.addCell(a8);
		sheet.addCell(b8);
		sheet.addCell(c8);
		sheet.addCell(d8);
		
		Label a9 = new Label(0, 8, "本次结算单据张数", styleInit());
		Label b9 = new Label(1, 8, param.get("BILL_NUM")==null?param.getString("BILL_NUM"):((BigDecimal)param.get("BILL_NUM")).toString());
		Label c9 = new Label(2, 8, "交易状态", styleInit());
		String deal_stat = "失败";
		if(param.get("DEAL_STAT")!=null && "0".equals(param.get("DEAL_STAT").toString())){
			deal_stat = "成功";
		}
		Label d9 = new Label(3, 8, deal_stat);
		sheet.addCell(a9);
		sheet.addCell(b9);
		sheet.addCell(c9);
		sheet.addCell(d9);
		
		Label a10 = new Label(0, 9, "错误信息", styleInit());
		Label b10 = new Label(1, 9, param.getString("error"));
		Label c10 = new Label(2, 9, "写社会保障卡结果", styleInit());
		String card_res = "写卡错误";
		if(param.get("CARD_RES")!=null && "0".equals(param.get("CARD_RES").toString())){
			card_res = "不写或写卡成功";
		}
		Label d10 = new Label(3, 9, card_res);
		sheet.addCell(a10);
		sheet.addCell(b10);
		sheet.addCell(c10);
		sheet.addCell(d10);
		
		Label a11 = new Label(0, 10, "扣银行卡结果", styleInit());
		String bank_res = "失败";
		if(param.get("BANK_RES")!=null && "0".equals(param.get("BANK_RES").toString())){
			bank_res = "不扣或扣成功";
		}
		Label b11 = new Label(1, 10, bank_res);
		Label c11 = new Label(2, 10, "更新后IC卡数据", styleInit());
		Label d11 = new Label(3, 10, param.getString("IC_DATA"));
		sheet.addCell(a11);
		sheet.addCell(b11);
		sheet.addCell(c11);
		sheet.addCell(d11);
		
		Label a12 = new Label(0, 11, "超限提示标记", styleInit());
		String over_flag = "无提示信息";
		if(param.get("OVER_FLAG") != null){
			if("1".equals(param.get("OVER_FLAG").toString())){
				over_flag = "有提示信息";
			}
		}
		Label b12 = new Label(1, 11, over_flag);
		Label c12 = new Label(2, 11, "规定病种标志", styleInit());
		String spec_flag = "";
		if(param.get("SPEC_FLAG") != null){
			if("0".equals(param.get("SPEC_FLAG").toString())){
				spec_flag = "非特种病结算";
			}
			if("1".equals(param.get("SPEC_FLAG").toString())){
				spec_flag = "特种病结算";
			}
		}
		Label d12 = new Label(3, 11, spec_flag);
		sheet.addCell(a12);
		sheet.addCell(b12);
		sheet.addCell(c12);
		sheet.addCell(d12);
		
		Label a13 = new Label(0, 12, "结算时间", styleInit());
		Label b13 = new Label(1, 12, param.get("CREATEDATE")==null?"":sdformat.format(param.get("CREATEDATE")));
		Label c13 = new Label(2, 12, "结算流水号", styleInit());
		Label d13 = new Label(3, 12, param.getString("FINAL_NO"));
		sheet.addCell(a13);
		sheet.addCell(b13);
		sheet.addCell(c13);
		sheet.addCell(d13);
		
		Label a14 = new Label(0, 13, "要作废的结算交易号", styleInit());
		Label b14 = new Label(1, 13, param.getString("ABO_DEAL_NO"));
		Label c14 = new Label(2, 13, "经办人", styleInit());
		Label d14 = new Label(3, 13, param.getString("OPERATOR"));
		sheet.addCell(a14);
		sheet.addCell(b14);
		sheet.addCell(c14);
		sheet.addCell(d14);
		
		Label a15 = new Label(0, 14, "是否重复退费", styleInit());
		String is_repet = "";
		if(param.get("IS_REPET")!=null && "0".equals(param.get("IS_REPET").toString())){
			is_repet = "正常退费";
		}
		if(param.get("IS_REPET")!=null && "1".equals(param.get("IS_REPET").toString())){
			is_repet = "结算单已经被退";
		}
		Label b15 = new Label(1, 14, is_repet);
		Label c15 = new Label(2, 14, "退费交易流水号", styleInit());
		Label d15 = new Label(3, 14, param.getString("RETURN_NO"));
		sheet.addCell(a15);
		sheet.addCell(b15);
		sheet.addCell(c15);
		sheet.addCell(d15);
		
		Label a16 = new Label(0, 15, "退费结算日期", styleInit());
		Label b16 = new Label(1, 15, param.get("RETURN_DATE")==null?"":sdformat.format(param.get("RETURN_DATE")));
		Label c16 = new Label(2, 15, "住院登记交易号", styleInit());
		Label d16 = new Label(3, 15, param.getString("REG_DEAL_NO"));
		sheet.addCell(a16);
		sheet.addCell(b16);
		sheet.addCell(c16);
		sheet.addCell(d16);
		
		Label a17 = new Label(0, 16, "本次结算明细条数", styleInit());
		Label b17 = new Label(1, 16, Transformation.decimalToStr(param.get("FINAL_NUM")));
		Label c17 = new Label(2, 16, "住院结算交易交流水号", styleInit());
		Label d17 = new Label(3, 16, param.getString("IN_DEAL_NO"));
		sheet.addCell(a17);
		sheet.addCell(b17);
		sheet.addCell(c17);
		sheet.addCell(d17);
		
		Label a18 = new Label(0, 17, "住院登记流水号", styleInit());
		Label b18 = new Label(1, 17, param.getString("IN_REG_NO"));
		Label c18 = new Label(2, 17, "明细序号列表", styleInit());
		Label d18 = new Label(3, 17, param.getString("DETAIL_LIST"));
		sheet.addCell(a18);
		sheet.addCell(b18);
		sheet.addCell(c18);
		sheet.addCell(d18);
		
		Label a19 = new Label(0, 18, "未上传成功的明细序号列表", styleInit());
		Label b19 = new Label(1, 18, param.getString("FAIL_DETAIL_LIST"));
		Label c19 = new Label(2, 18, "待查询用户交易类型号", styleInit());
		Label d19 = new Label(3, 18, param.getString("SEARCH_TYPE_NO"));
		sheet.addCell(a19);
		sheet.addCell(b19);
		sheet.addCell(c19);
		sheet.addCell(d19);
		
		Label a20 = new Label(0, 19, "待查询的用户交易流水号", styleInit());
		Label b20 = new Label(1, 19, param.getString("SEARCH_DEAL_NO"));
		Label c20 = new Label(2, 19, "享受状态", styleInit());
		Label d20 = new Label(3, 19, param.getString("HAVE_STAT_NAME"));
		sheet.addCell(a20);
		sheet.addCell(b20);
		sheet.addCell(c20);
		sheet.addCell(d20);
		
		Label a21 = new Label(0, 20, "用户交易是否成功", styleInit());
		String is_ok = "";
		if(param.get("IS_OK") != null){
			if("0".equals(param.get("IS_OK").toString())){
				is_ok = "成功";
			}
			if("-1".equals(param.get("IS_OK").toString())){
				is_ok = "失败";
			}
		}
		Label b21 = new Label(1, 20, is_ok);
		Label c21 = new Label(2, 20, "交易时间", styleInit());
		Label d21 = new Label(3, 20, param.get("DEAL_DATE")==null?"":sdformat.format(param.get("DEAL_DATE")));
		sheet.addCell(a21);
		sheet.addCell(b21);
		sheet.addCell(c21);
		sheet.addCell(d21);
		
		Label a22 = new Label(0, 21, "交易结算流水号", styleInit());
		Label b22 = new Label(1, 21, param.getString("DEAL_FINAL_NO"));
		Label c22 = new Label(2, 21, "交易处于阶段", styleInit());
		String deal_step = "";
		if(param.get("DEAL_STEP") != null){
			if("0".equals(param.getString("DEAL_STEP"))){
				deal_step = "已结算";
			}
			if("1".equals(param.getString("DEAL_STEP"))){
				deal_step = "已确认";
			}
		}
		Label d22 = new Label(3, 21, deal_step);
		sheet.addCell(a22);
		sheet.addCell(b22);
		sheet.addCell(c22);
		sheet.addCell(d22);
		
		Label a23 = new Label(0, 22, "交易类型", styleInit());
		Label b23 = new Label(1, 22, param.getString("DEAL_TYPE"));
		Label c23 = new Label(2, 22, "医保交易流水号", styleInit());
		Label d23 = new Label(3, 22, param.getString("MED_DEAL_NO"));
		sheet.addCell(a23);
		sheet.addCell(b23);
		sheet.addCell(c23);
		sheet.addCell(d23);
		
		Label a24 = new Label(0, 23, "交易流水号", styleInit());
		Label b24 = new Label(1, 23, param.getString("DEAL_NO"));
		Label c24 = new Label(2, 23, "掌医事务结果", styleInit());
		String hos_res = "";
		if(param.get("HOS_RES") != null){
			if("0".equals(param.getString("HOS_RES"))){
				hos_res = "成功";
			}
			if("-1".equals(param.getString("HOS_RES"))){
				hos_res = "失败";
			}
		}
		Label d24 = new Label(3, 23, hos_res);
		sheet.addCell(a24);
		sheet.addCell(b24);
		sheet.addCell(c24);
		sheet.addCell(d24);
	}
	
	@RequestMapping(value = "/fileDown")
	@Rights(code = "interInfo/fileDown")
	public void fileDown(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String file_path = ReadPropertiesFiles.getValue("face_img_path");//文件地址
		String fileName = request.getParameter("fileName");//文件名称
		FaceUtils.fileDown(file_path, fileName, fileType, request, response);

		File myFile = new File(file_path+fileName+"."+fileType);
		if(myFile.exists()){
			myFile.delete();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public CellFormat styleInit(){
		WritableCellFormat cellFormat = null;
		try{
			//设置字体格式为excel支持的格式 
			WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD);
			wf.setColour(Colour.BLUE_GREY);
			cellFormat = new WritableCellFormat(wf);
			cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			cellFormat.setBackground(Colour.PALE_BLUE);
			cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		}catch(Exception e){
			e.printStackTrace();
		}
		return cellFormat;
	}
	//========================== 药店支付批量导出 excel ===========================================
	@RequestMapping(value = "/drugstoreExporExcel")
	@Rights(code = "interInfo/drugstoreExporExcel")
	public ModelAndView drugstoreExporExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long millis = System.currentTimeMillis();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setCharacterEncoding("UTF-8");
		
		Date date = new Date();
	    long currentTime = date.getTime();
		String file_path = ReadPropertiesFiles.getValue("face_img_path");//excel存放地址
		System.out.println("=================导出开始，创建文件夹::"+file_path+currentTime+"=======================");
		FaceUtils.createFilePath(file_path+currentTime);
		
		PageData pd = this.getPageData();
		pd = this.getPD(pd);
		List<PageData> interList = pdrugstoreService.queryDrugstoreInterListAll(pd);//接口数据信息
		List<PageData> sysUserList = AppuserService.findForPay();//ts_plat库sys_app_user表
		interList = getGroupNameList(interList, sysUserList);//添加group_name属性
		
		for(int i=0; i<interList.size(); i++){
			PageData param = (PageData)interList.get(i);
			
			String excel_path = file_path + currentTime + File.separator + param.getString("ID") + ".xls";
			OutputStream fos = new FileOutputStream(excel_path);
			WritableWorkbook workbook = Workbook.createWorkbook(fos);
			
			interExcel_ds(workbook, param, sdformat);//接口数据详情
			drugExcel_ds(workbook, param, sdformat);//药品数据信息
			resultExcel_ds(workbook, param, sdformat);//计算结果信息
			workbook.write();
			workbook.close();
			fos.close();
		}
		//压缩文件夹
		ZipCompressor zca = new ZipCompressor(file_path+currentTime+"."+fileType);  
        zca.compressExe(file_path+currentTime);
		//删除文件夹
		File file = new File(file_path+currentTime);
		if(file.exists()){
			FaceUtils.deleteFile(file);
		}
		System.out.println("==========批量导出excel完毕，用时:" + (System.currentTimeMillis() - millis) + "毫秒==========");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(currentTime+"");
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}
	public void interExcel_ds(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		WritableSheet sheet = workbook.createSheet("接口数据详情", 0);
		
		Label a1 = new Label(0, 0, "业务接口编号", styleInit());
		Label b1 = new Label(1, 0, param.getString("API_TYPE"));
		Label c1 = new Label(2, 0, "数据类型", styleInit());
		Label d1 = new Label(3, 0, param.getString("DATA_TYPE_NAME"));
		sheet.addCell(a1);
		sheet.addCell(b1);
		sheet.addCell(c1);
		sheet.addCell(d1);
		
		Label a2 = new Label(0, 1, "机构名称", styleInit());
		Label b2 = new Label(1, 1, param.getString("GROUP_NAME"));
		Label c2 = new Label(2, 1, "附加消息", styleInit());
		Label d2 = new Label(3, 1, param.getString("ADD_INFO"));
		sheet.addCell(a2);
		sheet.addCell(b2);
		sheet.addCell(c2);
		sheet.addCell(d2);
		
		Label a3 = new Label(0, 2, "请求流水号", styleInit());
		Label b3 = new Label(1, 2, param.getString("REQ_NO"));
		Label c3 = new Label(2, 2, "时间戳", styleInit());
		Label d3 = new Label(3, 2, param.getString("TIME_STAMP"));
		sheet.addCell(a3);
		sheet.addCell(b3);
		sheet.addCell(c3);
		sheet.addCell(d3);
		
		Label a4 = new Label(0, 3, "药店名称", styleInit());
		Label b4 = new Label(1, 3, param.getString("SERVICE_NAME"));
		Label c4 = new Label(2, 3, "业务类型", styleInit());
		Label d4 = new Label(3, 3, param.getString("BUSI_TYPE_NAME"));
		sheet.addCell(a4);
		sheet.addCell(b4);
		sheet.addCell(c4);
		sheet.addCell(d4);
		
		Label a5 = new Label(0, 4, "IC卡号", styleInit());
		Label b5 = new Label(1, 4, param.getString("IC_CARD"));
		Label c5 = new Label(2, 4, "工号", styleInit());
		Label d5 = new Label(3, 4, param.getString("WORK_NO"));
		sheet.addCell(a5);
		sheet.addCell(b5);
		sheet.addCell(c5);
		sheet.addCell(d5);
		
		Label a6 = new Label(0, 5, "个人编号", styleInit());
		Label b6 = new Label(1, 5, param.getString("PERS_NO"));
		Label c6 = new Label(2, 5, "开单医生", styleInit());
		Label d6 = new Label(3, 5, param.getString("BILL_DOC"));
		sheet.addCell(a6);
		sheet.addCell(b6);
		sheet.addCell(c6);
		sheet.addCell(d6);
		
		Label a7 = new Label(0, 6, "收款员编号", styleInit());
		Label b7 = new Label(1, 6, param.getString("CASHIER_NO"));
		Label c7 = new Label(2, 6, "收款员姓名", styleInit());
		Label d7 = new Label(3, 6, param.getString("CASHIER_NAME"));
		sheet.addCell(a7);
		sheet.addCell(b7);
		sheet.addCell(c7);
		sheet.addCell(d7);
		
		Label a8 = new Label(0, 7, "就诊科室", styleInit());
		Label b8 = new Label(1, 7, param.getString("MEDICAL_DEPT"));
		Label c8 = new Label(2, 7, "诊断疾病名称", styleInit());
		Label d8 = new Label(3, 7, param.getString("MEDICAL_NAME"));
		sheet.addCell(a8);
		sheet.addCell(b8);
		sheet.addCell(c8);
		sheet.addCell(d8);
		//param.get("BILL_NUM")==null?param.getString("BILL_NUM"):((BigDecimal)param.get("BILL_NUM")).toString()
		Label a9 = new Label(0, 8, "药店收据类型", styleInit());
		Label b9 = new Label(1, 8, param.getString("RECEIPT_TYPE"));
		Label c9 = new Label(2, 8, "调用状态", styleInit());
		String deal_stat = "失败";
		if(param.get("DEAL_STAT")!=null && "0".equals(param.get("DEAL_STAT").toString())){
			deal_stat = "成功";
		}
		Label d9 = new Label(3, 8, deal_stat);
		sheet.addCell(a9);
		sheet.addCell(b9);
		sheet.addCell(c9);
		sheet.addCell(d9);
		
		Label a10 = new Label(0, 9, "调用错误编码", styleInit());
		Label b10 = new Label(1, 9, param.getString("DEAL_ERROR_CODE"));
		Label c10 = new Label(2, 9, "调用错误信息", styleInit());
		Label d10 = new Label(3, 9, param.getString("DEAL_ERROR_INFO"));
		sheet.addCell(a10);
		sheet.addCell(b10);
		sheet.addCell(c10);
		sheet.addCell(d10);
		
		Label a11 = new Label(0, 10, "服务返回的错误信息", styleInit());
		Label b11 = new Label(1, 10, param.getString("SERVICE_ERROR"));
		Label c11 = new Label(2, 10, "医保流水号", styleInit());
		Label d11 = new Label(3, 10, param.getString("MEDICARE_NO"));
		sheet.addCell(a11);
		sheet.addCell(b11);
		sheet.addCell(c11);
		sheet.addCell(d11);
		
		Label a12 = new Label(0, 11, "药店收据号", styleInit());
		Label b12 = new Label(1, 11, param.getString("RECEIPT_NO"));
		Label c12 = new Label(2, 11, "药店退费收据号", styleInit());
		Label d12 = new Label(3, 11, param.getString("REFUND_NO"));
		sheet.addCell(a12);
		sheet.addCell(b12);
		sheet.addCell(c12);
		sheet.addCell(d12);
		
		Label a13 = new Label(0, 12, "药店新收据号", styleInit());
		Label b13 = new Label(1, 12, param.getString("NEW_NO"));
		sheet.addCell(a13);
		sheet.addCell(b13);
	}
	public void resultExcel_ds(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		PageData resultModel = new PageData();
		resultModel.put("INTERID", param.get("ID"));
		resultModel.put("USERID", param.get("USER_ID"));
		List<PageData> resultList = pdrugstoreService.queryResultList(resultModel);
		if(resultList!=null && resultList.size()>0){
			resultModel = (PageData)resultList.get(0);
		}
		WritableSheet sheet = workbook.createSheet("计算结果信息", 2);
		
		Label a1 = new Label(0, 0, "收据张数", styleInit());
		Label b1 = new Label(1, 0, Transformation.decimalToStr(resultModel.get("RECEIPT_COUNT")));
		Label c1 = new Label(2, 0, "大额补助支付金额", styleInit());
		Label d1 = new Label(3, 0, Transformation.decimalToStr(resultModel.get("LAR_PAY")));
		sheet.addCell(a1);
		sheet.addCell(b1);
		sheet.addCell(c1);
		sheet.addCell(d1);
		
		Label a2 = new Label(0, 1, "统筹金支付金额", styleInit());
		Label b2 = new Label(1, 1, Transformation.decimalToStr(resultModel.get("PLAN_PAY")));
		Label c2 = new Label(2, 1, "公务员补助支付金额", styleInit());
		Label d2 = new Label(3, 1, Transformation.decimalToStr(resultModel.get("SER_PAY")));
		sheet.addCell(a2);
		sheet.addCell(b2);
		sheet.addCell(c2);
		sheet.addCell(d2);
		
		Label a3 = new Label(0, 2, "个人帐户支付金额", styleInit());
		Label b3 = new Label(1, 2, Transformation.decimalToStr(resultModel.get("SELF_ACCOUNT_PAY")));
		Label c3 = new Label(2, 2, "个人现金支付金额", styleInit());
		Label d3 = new Label(3, 2, Transformation.decimalToStr(resultModel.get("SELF_CASH_PAY")));
		sheet.addCell(a3);
		sheet.addCell(b3);
		sheet.addCell(c3);
		sheet.addCell(d3);
		
		Label a4 = new Label(0, 3, "结算前帐户余额", styleInit());
		Label b4 = new Label(1, 3, Transformation.decimalToStr(resultModel.get("B_BALANCE")));
		Label c4 = new Label(2, 3, "结算后帐户余额", styleInit());
		Label d4 = new Label(3, 3, Transformation.decimalToStr(resultModel.get("A_BALANCE")));
		sheet.addCell(a4);
		sheet.addCell(b4);
		sheet.addCell(c4);
		sheet.addCell(d4);
		
		Label a5 = new Label(0, 4, "预结算提示信息", styleInit());
		Label b5 = new Label(1, 4, resultModel.getString("INFO"));
		sheet.addCell(a5);
		sheet.addCell(b5);
	}
	public void drugExcel_ds(WritableWorkbook workbook, PageData param, SimpleDateFormat sdformat) throws Exception {
		PageData resultModel = new PageData();
		resultModel.put("INTERID", param.get("ID"));
		resultModel.put("USERID", param.get("USER_ID"));
		
		WritableSheet sheet = workbook.createSheet("药品信息", 1);
		sheet.setRowView(0, 300);
		int titleWidthIndex = 0;
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 15);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);
		sheet.setColumnView(titleWidthIndex++, 10);

		int titleIndex = 0;
		Label a1 = new Label(titleIndex++, 0, "收费类别编码", styleInit()); sheet.addCell(a1);
		Label b1 = new Label(titleIndex++, 0, "药品编号", styleInit()); sheet.addCell(b1);
		Label c1 = new Label(titleIndex++, 0, "药品名称", styleInit()); sheet.addCell(c1);
		Label d1 = new Label(titleIndex++, 0, "药品中心端编号", styleInit()); sheet.addCell(d1);
		Label e1 = new Label(titleIndex++, 0, "药品中心端名称", styleInit()); sheet.addCell(e1);
		Label f1 = new Label(titleIndex++, 0, "药品单价", styleInit()); sheet.addCell(f1);
		Label g1 = new Label(titleIndex++, 0, "药品数量", styleInit()); sheet.addCell(g1);
		Label h1 = new Label(titleIndex++, 0, "药品金额", styleInit()); sheet.addCell(h1);
		Label i1 = new Label(titleIndex++, 0, "药品剂型编码", styleInit()); sheet.addCell(i1);
		Label j1 = new Label(titleIndex++, 0, "药品每次用量", styleInit()); sheet.addCell(j1);
		Label k1 = new Label(titleIndex++, 0, "药品使用频次", styleInit()); sheet.addCell(k1);
		Label l1 = new Label(titleIndex++, 0, "药品执行天数", styleInit()); sheet.addCell(l1);
		int beginRowIndex = 1;
		int endRowIndex = 0;
		List<PageData> billList = pdrugstoreService.queryDrugItemListAll(resultModel);
		for(int i=0; i<billList.size(); i++){
			int cellIndex = 0;
			int endCellIndex = 0;
			endRowIndex = beginRowIndex;
			PageData pd = (PageData)billList.get(i);
			
			String TYPE = "";
			if(pd.get("TYPE") != null){
				if("1".equals(pd.get("TYPE").toString())){
					TYPE = "西药费";
				}else if("2".equals(pd.get("TYPE").toString())){
					TYPE = "中成药费";
				}else if("3".equals(pd.get("TYPE").toString())){
					TYPE = "中草药费";
				}
			}
			Label a2 = new Label(cellIndex, beginRowIndex, TYPE);
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(a2);
			
			Label b2 = new Label(cellIndex, beginRowIndex, pd.getString("CODE"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(b2);
			
			Label c2 = new Label(cellIndex, beginRowIndex, pd.getString("NAME"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(c2);
			
			Label d2 = new Label(cellIndex, beginRowIndex, pd.getString("CENT_CODE"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(d2);
			
			Label e2 = new Label(cellIndex, beginRowIndex, pd.getString("CENT_NAME"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(e2);
			
			Label f2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("UNIT_PRICE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(f2);
			
			Label g2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("COUNT")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(g2);
			
			Label h2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("PRICE")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(h2);
			
			Label i2 = new Label(cellIndex, beginRowIndex, pd.getString("FORM_NO"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(i2);
			
			Label j2 = new Label(cellIndex, beginRowIndex, pd.getString("DOSE"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(j2);
			
			Label k2 = new Label(cellIndex, beginRowIndex, pd.getString("FREQUENCY"));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(k2);
			
			Label l2 = new Label(cellIndex, beginRowIndex, Transformation.decimalToStr(pd.get("DAYS")));
			sheet.mergeCells(cellIndex++, beginRowIndex, endCellIndex++, endRowIndex);
			sheet.addCell(l2);
			
			beginRowIndex = endRowIndex + 1;
		}
	}
}