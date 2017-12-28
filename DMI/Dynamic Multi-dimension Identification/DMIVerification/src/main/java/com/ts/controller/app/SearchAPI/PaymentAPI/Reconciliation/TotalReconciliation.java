package com.ts.controller.app.SearchAPI.PaymentAPI.Reconciliation;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.entity.P_total_detail;
import com.ts.entity.Reconciliation.S01InputTotalReconciliationBean;
import com.ts.entity.Reconciliation.S01OutTotalReconciliationBean;
import com.ts.service.pay.PrecordManager;
import com.ts.util.Transformation;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: TotalReconciliation 
* @Description: TODO(总额对账接口) 
* @author Lee 刘峰
* @date 2017年5月4日 上午14:30:00 
*
 */

@Controller
@Service
public class TotalReconciliation extends PayRSHandler {

	@Resource(name = "precordService")
	private PrecordManager precordService;
	
	@Override
	protected Object exceBusiness(JSONObject value) {
		//传入参数
		String organ_code = value.getString("organicode");//机构
		String hosp_code = value.getString("S01_INP_NO01");//医院编码
		String start_tran_time = value.getString("S01_INP_NO02");//交易开始时间
		String end_tran_time = value.getString("S01_INP_NO03");//交易结束时间
		Double fee_total = value.getDouble("S01_INP_NO07");//费用总额
		Double self_fee_total = value.getDouble("S01_INP_NO08");//自费总额(非医保)
		Double drug_type_money = value.getDouble("S01_INP_NO09");//药品乙类自负
		Double hosption_fee = value.getDouble("S01_INP_NO10");//医保费用
		Double reimburse_total = value.getDouble("S01_INP_NO11");//合计报销金额
		String check_type = value.getString("S01_INP_NO12");//对账类型:0门诊和住院，1门诊，2住院
		
		S01InputTotalReconciliationBean inModel = new S01InputTotalReconciliationBean();
		inModel.setS01_INP_NO13(organ_code);
		inModel.setS01_INP_NO01(hosp_code);
		inModel.setS01_INP_NO02(start_tran_time+" 00:00:00");
		inModel.setS01_INP_NO03(end_tran_time+" 23:59:59");
		inModel.setS01_INP_NO12(check_type);
		inModel.setDATA_TYPE("1");
		
		//查询数据库并累加
		Double fee_sum = 0.0;//费用总额累加
		Double self_fee_sum = 0.0;//自费总额(非医保)累加
		Double drug_type_sum = 0.0;//药品乙类自负累加
		Double hosption_sum = 0.0;//医保费用累加
		Double reimburse_sum = 0.0;//合计报销金额累加
		List<P_total_detail> totalList = precordService.queryTotalRecord(inModel);
		for(int i=0; i<totalList.size(); i++){
			fee_sum = Transformation.doubleAdd(fee_sum, totalList.get(i).getTOTAL_FEE()==null?0.0:totalList.get(i).getTOTAL_FEE());
			self_fee_sum = Transformation.doubleAdd(self_fee_sum, totalList.get(i).getSELF_PAY()==null?0.0:totalList.get(i).getSELF_PAY());
			drug_type_sum = Transformation.doubleAdd(drug_type_sum, totalList.get(i).getSELF_NEG()==null?0.0:totalList.get(i).getSELF_NEG());
			hosption_sum = Transformation.doubleAdd(hosption_sum, totalList.get(i).getMED_FEE()==null?0.0:totalList.get(i).getMED_FEE());
			reimburse_sum = Transformation.doubleAdd(reimburse_sum, totalList.get(i).getRETURN_FEE()==null?0.0:totalList.get(i).getRETURN_FEE());
		}
		
		//对比金额
		String record_msg = "";
		int k = 0;
		if(!Transformation.doubleCompare(fee_total, fee_sum)){
			record_msg = record_msg + "费用总额：" + fee_total + "_" + fee_sum + "、";
			k++;
		}
		
		if(!Transformation.doubleCompare(self_fee_total, self_fee_sum)){
			record_msg = record_msg + "自费总额(非医保)：" + self_fee_total + "_" + self_fee_sum + "、";
			k++;
		}
		
		if(!Transformation.doubleCompare(drug_type_money, drug_type_sum)){
			record_msg = record_msg + "药品乙类自负：" + drug_type_money + "_" + drug_type_sum + "、";
			k++;
		}
		
		if(!Transformation.doubleCompare(hosption_fee, hosption_sum)){
			record_msg = record_msg + "医保费用：" + hosption_fee + "_" + hosption_sum + "、";
			k++;
		}
		
		if(!Transformation.doubleCompare(reimburse_total, reimburse_sum)){
			record_msg = record_msg + "合计报销金额" + reimburse_total + "_" + reimburse_sum + "、";
			k++;
		}
		
		//传出参数
		S01OutTotalReconciliationBean outModel = new S01OutTotalReconciliationBean();
		if(record_msg != null && !"".equals(record_msg) && k > 0){
			outModel.setS01_OUT_NO01(-1);//交易状态失败
			outModel.setS01_OUT_NO06("-1");//对账失败
			outModel.setS01_OUT_NO02("共"+k+"个项目不一致");//错误信息
			outModel.setS01_OUT_NO07(record_msg);//对帐不一致信息
		}else{
			outModel.setS01_OUT_NO01(0);//交易状态成功
			outModel.setS01_OUT_NO06("0");//对账成功
			outModel.setS01_OUT_NO02("");//错误信息
			outModel.setS01_OUT_NO07("");//对帐不一致信息
		}
		return outModel;
	}
	
}