package com.ts.controller.app.SearchAPI.PaymentAPI.Reconciliation;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.entity.P_total_detail;
import com.ts.entity.Reconciliation.S02InputDetailReconciliationBean;
import com.ts.entity.Reconciliation.S02OutDetailReconciliationBean;
import com.ts.service.pay.PrecordManager;
import com.ts.util.Transformation;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: DetailReconciliation 
* @Description: TODO(明细对账接口) 
* @author Lee 刘峰
* @date 2017年5月4日 上午14:30:00 
*
 */

@Controller
@Service
public class DetailReconciliation extends PayRSHandler {

	@Resource(name = "precordService")
	private PrecordManager precordService;
	
	@Override
	protected Object exceBusiness(JSONObject value) {
		//传入参数
		String organ_code = value.getString("organicode");//机构
		String hosp_code = value.getString("S02_INP_NO01");//医院编码
		String tran_time = value.getString("S02_INP_NO04");//交易时间
		Double fee_total = value.getDouble("S02_INP_NO05");//费用总额
		Double self_fee_total = value.getDouble("S02_INP_NO06");//自费总额(非医保)
		Double drug_type_money = value.getDouble("S02_INP_NO07");//药品乙类自负
		Double hosption_fee = value.getDouble("S02_INP_NO08");//医保费用
		Double reimburse_total = value.getDouble("S02_INP_NO09");//合计报销金额
		String record_type = value.getString("S02_INP_NO10");//对账类型
		String doctor_code = value.getString("S02_INP_NO02");//就诊号
		String settle_code = value.getString("S02_INP_NO03");//结算流水号
		
		S02InputDetailReconciliationBean inModel = new S02InputDetailReconciliationBean();
		inModel.setS02_INP_NO01(hosp_code);
		inModel.setS02_INP_NO11(organ_code);
		inModel.setS02_INP_NO04(tran_time);
		inModel.setS02_INP_NO10(record_type);
		inModel.setS02_INP_NO02(doctor_code);
		inModel.setS02_INP_NO03(settle_code);
		
		//查询数据库并累加
		Double fee_sum = 0.0;//费用总额累加
		Double self_fee_sum = 0.0;//自费总额(非医保)累加
		Double drug_type_sum = 0.0;//药品乙类自负累加
		Double hosption_sum = 0.0;//医保费用累加
		Double reimburse_sum = 0.0;//合计报销金额累加
		List<P_total_detail> detailList = precordService.queryDetailRecord(inModel);
		for(int i=0; i<detailList.size(); i++){
			fee_sum = Transformation.doubleAdd(fee_sum, detailList.get(i).getTOTAL_FEE()==null?0.0:detailList.get(i).getTOTAL_FEE());
			self_fee_sum = Transformation.doubleAdd(self_fee_sum, detailList.get(i).getSELF_PAY()==null?0.0:detailList.get(i).getSELF_PAY());
			drug_type_sum = Transformation.doubleAdd(drug_type_sum, detailList.get(i).getSELF_NEG()==null?0.0:detailList.get(i).getSELF_NEG());
			hosption_sum = Transformation.doubleAdd(hosption_sum, detailList.get(i).getMED_FEE()==null?0.0:detailList.get(i).getMED_FEE());
			reimburse_sum = Transformation.doubleAdd(reimburse_sum, detailList.get(i).getRETURN_FEE()==null?0.0:detailList.get(i).getRETURN_FEE());
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
		
		//返回实体
		S02OutDetailReconciliationBean outModel = new S02OutDetailReconciliationBean();
		if(record_msg != null && !"".equals(record_msg) && k > 0){
			outModel.setS02_OUT_NO01(-1);//交易状态失败
			outModel.setS02_OUT_NO06("-1");//对账失败
			outModel.setS02_OUT_NO02("共"+k+"个项目不一致");//错误信息
			outModel.setS02_OUT_NO07(record_msg);//对帐不一致信息
		}else{
			outModel.setS02_OUT_NO01(0);//交易状态成功
			outModel.setS02_OUT_NO06("0");//对账成功
			outModel.setS02_OUT_NO02("");//错误信息
			outModel.setS02_OUT_NO07("");//对帐不一致信息
		}
		return outModel;
	}
	
}