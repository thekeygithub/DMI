package com.ts.controller.system.pay.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.pay.PrecordManager;
import com.ts.util.Jurisdiction;
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;
import com.ts.util.Transformation;

@Controller
@RequestMapping(value = "/record")
public class DetailRecordController extends BaseController {

	@Resource(name = "precordService")
	private PrecordManager precordService;
	
	@Autowired
	private ObjectExcelView objectExcelView;
	
	@RequestMapping(value = "/detailList")
	@Rights(code = "record/detailList")
	public ModelAndView detailList(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		
		mv.addObject("keywords", pd.getString("keywords"));
		mv.addObject("final_date_start", pd.getString("final_date_start"));
		mv.addObject("final_date_end", pd.getString("final_date_end"));
		
		pd = this.getPD(pd);
		pd.put("DATA_TYPE", "2");//业务明细对账
		page.setPd(pd);
		List<PageData> varList = precordService.searchTotalList(page);
		
		//========================== start ======================================
		Double fee_sum = 0.0;//费用总额累加
		Double self_fee_sum = 0.0;//自费总额(非医保)累加
		Double drug_type_sum = 0.0;//药品乙类自负累加
		Double hosption_sum = 0.0;//医保费用累加
		Double reimburse_sum = 0.0;//合计报销金额累加
		List<PageData> recordList = precordService.searchTotalListAll(pd);
		for(int i=0; i<recordList.size(); i++){
			fee_sum = Transformation.doubleAdd(fee_sum, recordList.get(i).get("TOTAL_FEE")==null?0.0:Double.parseDouble(Transformation.decimalToStr(recordList.get(i).get("TOTAL_FEE"))));
			self_fee_sum = Transformation.doubleAdd(self_fee_sum, recordList.get(i).get("SELF_PAY")==null?0.0:Double.parseDouble(Transformation.decimalToStr(recordList.get(i).get("SELF_PAY"))));
			drug_type_sum = Transformation.doubleAdd(drug_type_sum, recordList.get(i).get("SELF_NEG")==null?0.0:Double.parseDouble(Transformation.decimalToStr(recordList.get(i).get("SELF_NEG"))));
			hosption_sum = Transformation.doubleAdd(hosption_sum, recordList.get(i).get("MED_FEE")==null?0.0:Double.parseDouble(Transformation.decimalToStr(recordList.get(i).get("MED_FEE"))));
			reimburse_sum = Transformation.doubleAdd(reimburse_sum, recordList.get(i).get("RETURN_FEE")==null?0.0:Double.parseDouble(Transformation.decimalToStr(recordList.get(i).get("RETURN_FEE"))));
		}
		pd.put("fee_sum", String.format("%.2f", fee_sum));
		pd.put("self_fee_sum", String.format("%.2f", self_fee_sum));
		pd.put("drug_type_sum", String.format("%.2f", drug_type_sum));
		pd.put("hosption_sum", String.format("%.2f", hosption_sum));
		pd.put("reimburse_sum", String.format("%.2f", reimburse_sum));
		//============================= end =========================================
		
		mv.setViewName("pay/record/businessDetail/show_detail_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value = "/totalSituation2")
	@Rights(code = "record/totalSituation2")
	public ModelAndView totalSituation2() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("DATA_TYPE", "2");
		PageData varModel = precordService.findTotalById(pd);
		mv.setViewName("pay/record/businessDetail/show_situation_list2");
		mv.addObject("varModel", varModel);
		return mv;
	}
	
	public PageData getPD(PageData pd){
		//关键字
		String keywords = pd.getString("keywords");
		if(null != keywords && !"".equals(keywords.trim())){
			pd.put("KEYWORDS", "%"+keywords.trim()+"%");
		}
		//开始结算时间
		String final_date_start = pd.getString("final_date_start");
		if(final_date_start != null && !"".equals(final_date_start)){
			pd.put("FINAL_DATE_START", final_date_start+" 00:00:00");
		}
		//结束结算时间
		String final_date_end = pd.getString("final_date_end");
		if(final_date_end != null && !"".equals(final_date_end)){
			pd.put("FINAL_DATE_END", final_date_end+" 23:59:59");
		}
		//登录名
		String userName = this.getCurrentUser().getUSERNAME();
		if(userName!=null && !"admin".equals(userName)){
			pd.put("USERNAME", userName);
		}
		return pd;
	}
	
	@RequestMapping(value = "/exporData")
	@Rights(code = "record/exporData")
	public ModelAndView exporData() {
		logBefore(logger, Jurisdiction.getUsername()+"导出业务明细对账数据");
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd = this.getPD(pd);
		pd.put("DATA_TYPE", "2");//业务明细对账
		try {
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("机构名称");
			titles.add("医院名称");
			titles.add("结算日期");
			titles.add("费用总额");
			titles.add("自费总额(非医保)");	
			titles.add("药品乙类自负");
			titles.add("医保费用");
			titles.add("合计报销金额");
			dataMap.put("titles", titles);
			
			Double fee_sum = 0.0;//费用总额累加
			Double self_fee_sum = 0.0;//自费总额(非医保)累加
			Double drug_type_sum = 0.0;//药品乙类自负累加
			Double hosption_sum = 0.0;//医保费用累加
			Double reimburse_sum = 0.0;//合计报销金额累加
			List<PageData> list = precordService.searchTotalListAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0; i<=list.size(); i++){
				PageData vpd = new PageData();
				if(i == list.size()){
					vpd.put("var1", "总计");
					vpd.put("var2", "");
					vpd.put("var3", "");
					vpd.put("var4", String.format("%.2f", fee_sum));
					vpd.put("var5", String.format("%.2f", self_fee_sum));
					vpd.put("var6", String.format("%.2f", drug_type_sum));
					vpd.put("var7", String.format("%.2f", hosption_sum));
					vpd.put("var8", String.format("%.2f", reimburse_sum));
				}else{
					fee_sum = Transformation.doubleAdd(fee_sum, list.get(i).get("TOTAL_FEE")==null?0.0:Double.parseDouble(Transformation.decimalToStr(list.get(i).get("TOTAL_FEE"))));
					self_fee_sum = Transformation.doubleAdd(self_fee_sum, list.get(i).get("SELF_PAY")==null?0.0:Double.parseDouble(Transformation.decimalToStr(list.get(i).get("SELF_PAY"))));
					drug_type_sum = Transformation.doubleAdd(drug_type_sum, list.get(i).get("SELF_NEG")==null?0.0:Double.parseDouble(Transformation.decimalToStr(list.get(i).get("SELF_NEG"))));
					hosption_sum = Transformation.doubleAdd(hosption_sum, list.get(i).get("MED_FEE")==null?0.0:Double.parseDouble(Transformation.decimalToStr(list.get(i).get("MED_FEE"))));
					reimburse_sum = Transformation.doubleAdd(reimburse_sum, list.get(i).get("RETURN_FEE")==null?0.0:Double.parseDouble(Transformation.decimalToStr(list.get(i).get("RETURN_FEE"))));
					
					vpd.put("var1", list.get(i).getString("GROUP_NAME"));
					vpd.put("var2", list.get(i).getString("HOSP_NAME"));
					vpd.put("var3", list.get(i).get("FINAL_DATE"));
					vpd.put("var4", Transformation.decimalToStr(list.get(i).get("TOTAL_FEE")));
					vpd.put("var5", Transformation.decimalToStr(list.get(i).get("SELF_PAY")));
					vpd.put("var6", Transformation.decimalToStr(list.get(i).get("SELF_NEG")));
					vpd.put("var7", Transformation.decimalToStr(list.get(i).get("MED_FEE")));
					vpd.put("var8", Transformation.decimalToStr(list.get(i).get("RETURN_FEE")));
				}
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			mv = new ModelAndView(objectExcelView, dataMap);
		}catch(Exception e){
			logger.error(e.toString(), e);
		}
		
		return mv;
	}
	
}