package com.ts.controller.app.SearchAPI.PaymentAPI.Reconciliation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.entity.P_total_detail;
import com.ts.entity.Reconciliation.S01InputTotalReconciliationBean;
import com.ts.service.pay.PrecordManager;
import com.ts.util.Transformation;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: TotalRecordDown 
* @Description: TODO(总额对账下载接口) 
* @author Lee 刘峰
* @date 2017年5月4日 上午14:30:00
*
 */

@Controller
@Service
public class TotalRecordDown extends PayRSHandler {

	@Resource(name = "precordService")
	private PrecordManager precordService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exceBusiness(JSONObject value) {
		
		Map param = new LinkedHashMap();//返回值
		Integer S05_OUT_NO02 = 0;//支付总数
		Integer S05_OUT_NO03 = 0;//确认交易成功记录数
		Integer S05_OUT_NO04 = 0;//退费记录条数
		Double S05_OUT_NO05 = 0.0;//费用总额
		Double S05_OUT_NO06 = 0.0;//自费总额(非医保)
		Double S05_OUT_NO07 = 0.0;//药品乙类自负
		Double S05_OUT_NO08 = 0.0;//医保费用
		Double S05_OUT_NO09 = 0.0;//合计报销金额
		Double S05_OUT_NO10 = 0d; //合计现金支付
		
		try{
			String organ_code = value.getString("organicode");//机构
			String hosp_code = value.getString("S05_INP_NO01");//医院编码
			String final_start_date = value.getString("S05_INP_NO02");//交易开始时间
			String final_end_date = value.getString("S05_INP_NO03");//交易结束时间
			String record_type = value.getString("S05_INP_NO04");//对账类型
			
			S01InputTotalReconciliationBean inModel = new S01InputTotalReconciliationBean();
			inModel.setS01_INP_NO13(organ_code);
			inModel.setS01_INP_NO01(hosp_code);
			inModel.setS01_INP_NO02(final_start_date+" 00:00:00");
			inModel.setS01_INP_NO03(final_end_date+" 23:59:59");
			inModel.setS01_INP_NO12(record_type);
			inModel.setDATA_TYPE("1");
			
			List<P_total_detail> totalDownList = precordService.queryTotalRecord(inModel);
			for(int i=0; i<totalDownList.size(); i++){
				//支付总数
				S05_OUT_NO02 = S05_OUT_NO02 + (totalDownList.get(i).getPAY_NUM()==null?0:totalDownList.get(i).getPAY_NUM());
				//确认交易成功记录数
				S05_OUT_NO03 = S05_OUT_NO03 + (totalDownList.get(i).getCONFIRM_NUM()==null?0:totalDownList.get(i).getCONFIRM_NUM());
				//退费记录条数
				S05_OUT_NO04 = S05_OUT_NO04 + (totalDownList.get(i).getRETURN_NUM()==null?0:totalDownList.get(i).getRETURN_NUM());
				//费用总额
				S05_OUT_NO05 = Transformation.doubleAdd(S05_OUT_NO05, totalDownList.get(i).getTOTAL_FEE()==null?0.0:totalDownList.get(i).getTOTAL_FEE());
				//自费总额(非医保)
				S05_OUT_NO06 = Transformation.doubleAdd(S05_OUT_NO06, totalDownList.get(i).getSELF_PAY()==null?0.0:totalDownList.get(i).getSELF_PAY());
				//药品乙类自负
				S05_OUT_NO07 = Transformation.doubleAdd(S05_OUT_NO07, totalDownList.get(i).getSELF_NEG()==null?0.0:totalDownList.get(i).getSELF_NEG());
				//医保费用
				S05_OUT_NO08 = Transformation.doubleAdd(S05_OUT_NO08, totalDownList.get(i).getMED_FEE()==null?0.0:totalDownList.get(i).getMED_FEE());
				//合计报销金额
				S05_OUT_NO09 = Transformation.doubleAdd(S05_OUT_NO09, totalDownList.get(i).getRETURN_FEE()==null?0.0:totalDownList.get(i).getRETURN_FEE());
				// 合计现金支付 
				S05_OUT_NO10 = Transformation.doubleAdd(S05_OUT_NO10, totalDownList.get(i).getCASH_TOTAL()==null?0.0:totalDownList.get(i).getCASH_TOTAL());
			}
			param.put("S05_OUT_NO01", "0");
		}catch(Exception e){
			param.put("S05_OUT_NO01", "-1");
			logger.error(e.getMessage(), e);
		}
		param.put("S05_OUT_NO02", S05_OUT_NO02);
		param.put("S05_OUT_NO03", S05_OUT_NO03);
		param.put("S05_OUT_NO04", S05_OUT_NO04);
		param.put("S05_OUT_NO05", S05_OUT_NO05);
		param.put("S05_OUT_NO06", S05_OUT_NO06);
		param.put("S05_OUT_NO07", S05_OUT_NO07);
		param.put("S05_OUT_NO08", S05_OUT_NO08);
		param.put("S05_OUT_NO09", S05_OUT_NO09);
		param.put("S05_OUT_NO10", S05_OUT_NO10);
		
		return param;
	}
	
}