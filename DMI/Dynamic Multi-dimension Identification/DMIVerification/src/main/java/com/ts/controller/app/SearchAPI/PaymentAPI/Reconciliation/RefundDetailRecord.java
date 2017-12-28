package com.ts.controller.app.SearchAPI.PaymentAPI.Reconciliation;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.entity.P_return_detail;
import com.ts.entity.Reconciliation.S04InputRefundDetailBean;
import com.ts.entity.Reconciliation.S04OutRefundDetailBean;
import com.ts.service.pay.PrecordManager;
import com.ts.util.Transformation;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: RefundDetailRecord 
* @Description: TODO(退费明细对账接口) 
* @author Lee 刘峰
* @date 2017年5月4日 上午14:30:00 
*
 */

@Controller
@Service
public class RefundDetailRecord extends PayRSHandler {

	@Resource(name = "precordService")
	private PrecordManager precordService;
	
	@Override
	protected Object exceBusiness(JSONObject value) {
		//传入参数
		String organ_code = value.getString("organicode");//机构
		String hosp_code = value.getString("S04_INP_NO01");//医院编码
		String tran_time = value.getString("S04_INP_NO04");//交易时间
		Double re_fee_total = value.getDouble("S04_INP_NO05");//退费费用总额
		Double re_self_fee_total = value.getDouble("S04_INP_NO06");//退费自费总额(非医保)
		Double re_drug_type_money = value.getDouble("S04_INP_NO07");//退费药品乙类自负
		Double re_hosption_fee = value.getDouble("S04_INP_NO08");//退费医保费用
		Double re_reimburse_total = value.getDouble("S04_INP_NO09");//退费合计报销金额
		String check_type = value.getString("S04_INP_NO10");//对账类型
		String doctor_code = value.getString("S04_INP_NO02");//就诊号
		String settle_code = value.getString("S04_INP_NO03");//结算流水号
		
		S04InputRefundDetailBean inModel = new S04InputRefundDetailBean();
		inModel.setS04_INP_NO01(hosp_code);
		inModel.setS04_INP_NO11(organ_code);
		inModel.setS04_INP_NO04(tran_time);
		inModel.setS04_INP_NO10(check_type);
		inModel.setS04_INP_NO02(doctor_code);
		inModel.setS04_INP_NO03(settle_code);
		
		//查询数据库并累加
		Double re_fee_sum = 0.0;//退费费用总额累加
		Double re_self_fee_sum = 0.0;//退费自费总额(非医保)累加
		Double re_drug_type_sum = 0.0;//退费药品乙类自负累加
		Double re_hosption_sum = 0.0;//退费医保费用累加
		Double re_reimburse_sum = 0.0;//退费合计报销金额累加
		List<P_return_detail> reDetailList = precordService.queryReDetailRecord(inModel);
		for(int i=0; i<reDetailList.size(); i++){
			re_fee_sum = Transformation.doubleAdd(re_fee_sum, reDetailList.get(i).getTOTAL_FEE()==null?0.0:reDetailList.get(i).getTOTAL_FEE());
			re_self_fee_sum = Transformation.doubleAdd(re_self_fee_sum, reDetailList.get(i).getSELF_PAY()==null?0.0:reDetailList.get(i).getSELF_PAY());
			re_drug_type_sum = Transformation.doubleAdd(re_drug_type_sum, reDetailList.get(i).getSELF_NEG()==null?0.0:reDetailList.get(i).getSELF_NEG());
			re_hosption_sum = Transformation.doubleAdd(re_hosption_sum, reDetailList.get(i).getMED_FEE()==null?0.0:reDetailList.get(i).getMED_FEE());
			re_reimburse_sum = Transformation.doubleAdd(re_reimburse_sum, reDetailList.get(i).getRETURN_FEE()==null?0.0:reDetailList.get(i).getRETURN_FEE());
		}
		
		//对比金额
		String record_msg = "";
		int k = 0;
		if(!Transformation.doubleCompare(re_fee_total, re_fee_sum)){
			record_msg = record_msg + "费用总额：" + re_fee_total + "_" + re_fee_sum + "、";
			k++;
		}
		
		if(!Transformation.doubleCompare(re_self_fee_total, re_self_fee_sum)){
			record_msg = record_msg + "自费总额(非医保)：" + re_self_fee_total + "_" + re_self_fee_sum + "、";
			k++;
		}
		
		if(!Transformation.doubleCompare(re_drug_type_money, re_drug_type_sum)){
			record_msg = record_msg + "药品乙类自负：" + re_drug_type_money + "_" + re_drug_type_sum + "、";
			k++;
		}
		
		if(!Transformation.doubleCompare(re_hosption_fee, re_hosption_sum)){
			record_msg = record_msg + "医保费用：" + re_hosption_fee + "_" + re_hosption_sum + "、";
			k++;
		}
		
		if(!Transformation.doubleCompare(re_reimburse_total, re_reimburse_sum)){
			record_msg = record_msg + "合计报销金额" + re_reimburse_total + "_" + re_reimburse_sum + "、";
			k++;
		}
		
		//返回实体
		S04OutRefundDetailBean outModel = new S04OutRefundDetailBean();
		if(record_msg != null && !"".equals(record_msg) && k > 0){
			outModel.setS04_OUT_NO01(-1);//交易状态失败
			outModel.setS04_OUT_NO06("-1");//对账失败
			outModel.setS04_OUT_NO02("共"+k+"个项目不一致");//错误信息
			outModel.setS04_OUT_NO07(record_msg);//对帐不一致信息
		}else{
			outModel.setS04_OUT_NO01(0);//交易状态成功
			outModel.setS04_OUT_NO06("0");//对账成功
			outModel.setS04_OUT_NO02("");//错误信息
			outModel.setS04_OUT_NO07("");//对帐不一致信息
		}
		
		return outModel;
	}
	
}