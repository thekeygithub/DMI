package com.ts.controller.app.SearchAPI.PaymentAPI.Reconciliation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.entity.P_return_detail;
import com.ts.entity.Reconciliation.S03InputRefundTotalBean;
import com.ts.service.pay.PrecordManager;
import com.ts.util.Transformation;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: ReTotalRecordDown 
* @Description: TODO(退费对账下载接口) 
* @author Lee 刘峰
* @date 2017年5月4日 上午14:30:00
*
 */

@Controller
@Service
public class ReTotalRecordDown extends PayRSHandler {

	@Resource(name = "precordService")
	private PrecordManager precordService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exceBusiness(JSONObject value) {
		
		Map param = new LinkedHashMap();//返回值
		Integer S07_OUT_NO02 = 0;//退费记录条数
		Double S07_OUT_NO03 = 0.0;//退费费用总额
		Double S07_OUT_NO04 = 0.0;//退费自费总额(非医保)
		Double S07_OUT_NO05 = 0.0;//退费药品乙类自负
		Double S07_OUT_NO06 = 0.0;//退费医保费用
		Double S07_OUT_NO07 = 0.0;//退费合计报销金额
		Double S07_OUT_NO08 = 0d; //退费合计现金支付
		
		try{
			String organ_code = value.getString("organicode");//机构
			String hosp_code = value.getString("S07_INP_NO01");//医院编码
			String final_start_date = value.getString("S07_INP_NO02");//交易开始时间
			String final_end_date = value.getString("S07_INP_NO03");//交易结束时间
			String record_type = value.getString("S07_INP_NO04");//对账类型
			
			S03InputRefundTotalBean inModel = new S03InputRefundTotalBean();
			inModel.setS03_INP_NO11(organ_code);
			inModel.setS03_INP_NO01(hosp_code);
			inModel.setS03_INP_NO02(final_start_date+" 00:00:00");
			inModel.setS03_INP_NO03(final_end_date+" 23:59:59");
			inModel.setS03_INP_NO10(record_type);
			inModel.setDATA_TYPE("1");
			
			List<P_return_detail> reTotalList = precordService.queryReTotalRecord(inModel);
			for(int i=0; i<reTotalList.size(); i++){
				//退费记录数
				S07_OUT_NO02 = S07_OUT_NO02 + (reTotalList.get(i).getRETURN_NUM()==null?0:reTotalList.get(i).getRETURN_NUM());
				//退费费用总额
				S07_OUT_NO03 = Transformation.doubleAdd(S07_OUT_NO03, reTotalList.get(i).getTOTAL_FEE()==null?0.0:reTotalList.get(i).getTOTAL_FEE());
				//退费自费总额(非医保)
				S07_OUT_NO04 = Transformation.doubleAdd(S07_OUT_NO04, reTotalList.get(i).getSELF_PAY()==null?0.0:reTotalList.get(i).getSELF_PAY());
				//退费药品乙类自负
				S07_OUT_NO05 = Transformation.doubleAdd(S07_OUT_NO05, reTotalList.get(i).getSELF_NEG()==null?0.0:reTotalList.get(i).getSELF_NEG());
				//退费医保费用
				S07_OUT_NO06 = Transformation.doubleAdd(S07_OUT_NO06, reTotalList.get(i).getMED_FEE()==null?0.0:reTotalList.get(i).getMED_FEE());
				//退费合计报销金额
				S07_OUT_NO07 = Transformation.doubleAdd(S07_OUT_NO07, reTotalList.get(i).getRETURN_FEE()==null?0.0:reTotalList.get(i).getRETURN_FEE());
				//合计现金支付  
				S07_OUT_NO08 = Transformation.doubleAdd(S07_OUT_NO08, reTotalList.get(i).getCASH_TOTAL()==null?0.0:reTotalList.get(i).getCASH_TOTAL());
			}
			param.put("S07_OUT_NO01", "0");
		}catch(Exception e){
			param.put("S07_OUT_NO01", "-1");
			logger.error(e.getMessage(), e);
		}
		
		param.put("S07_OUT_NO02", S07_OUT_NO02);
		param.put("S07_OUT_NO03", S07_OUT_NO03);
		param.put("S07_OUT_NO04", S07_OUT_NO04);
		param.put("S07_OUT_NO05", S07_OUT_NO05);
		param.put("S07_OUT_NO06", S07_OUT_NO06);
		param.put("S07_OUT_NO07", S07_OUT_NO07);
		param.put("S07_OUT_NO08", S07_OUT_NO08);
		return param;
	}
	
}