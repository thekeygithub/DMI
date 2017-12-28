package com.ts.controller.app.SearchAPI.PaymentAPI.Reconciliation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: ReDetailRecordDown 
* @Description: TODO(退费明细对账下载接口) 
* @author Lee 刘峰
* @date 2017年5月4日 上午14:30:00
*
 */

@Controller
@Service
public class ReDetailRecordDown extends PayRSHandler {

	@Resource(name = "precordService")
	private PrecordManager precordService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exceBusiness(JSONObject value) {
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map param = new LinkedHashMap();//返回值
		List dataList = new ArrayList();//子集明细
		
		try{
			String organ_code = value.getString("organicode");//机构
			String hosp_code = value.getString("S08_INP_NO01");//医院编码
			String final_start_date = value.getString("S08_INP_NO02");//交易开始时间
			String final_end_date = value.getString("S08_INP_NO03");//交易结束时间
			String record_type = value.getString("S08_INP_NO04");//对账类型
			
			S03InputRefundTotalBean inModel = new S03InputRefundTotalBean();
			inModel.setS03_INP_NO11(organ_code);
			inModel.setS03_INP_NO01(hosp_code);
			inModel.setS03_INP_NO02(final_start_date+" 00:00:00");
			inModel.setS03_INP_NO03(final_end_date+" 23:59:59");
			inModel.setS03_INP_NO10(record_type);
			inModel.setDATA_TYPE("2");
			
			List<P_return_detail> reDetailList = precordService.queryReTotalRecord(inModel);
			for(int i=0; i<reDetailList.size(); i++){
				Map map = new LinkedHashMap();
				map.put("requestno", reDetailList.get(i).getREQ_NO()==null?"":reDetailList.get(i).getREQ_NO());//请求流水号
				map.put("S08_INP_NO01", reDetailList.get(i).getHOS_CODE());//医院编码
				map.put("S08_INP_NO02", reDetailList.get(i).getVISIT_NO()==null?"":reDetailList.get(i).getVISIT_NO());//就诊序号
				map.put("S08_INP_NO03", reDetailList.get(i).getFINAL_NO()==null?"":reDetailList.get(i).getFINAL_NO());//退款流水号、结算流水号
				map.put("S08_INP_NO04", reDetailList.get(i).getFINAL_DATE()==null?null:sf.format(reDetailList.get(i).getFINAL_DATE()));//交易时间
				map.put("S08_INP_NO05", reDetailList.get(i).getTOTAL_FEE());//费用总额
				map.put("S08_INP_NO06", reDetailList.get(i).getSELF_PAY());//自费总额(非医保)
				map.put("S08_INP_NO07", reDetailList.get(i).getSELF_NEG());//药品乙类自负
				map.put("S08_INP_NO08", reDetailList.get(i).getMED_FEE());//医保费用
				map.put("S08_INP_NO09", reDetailList.get(i).getRETURN_FEE());//合计报销金额
				map.put("S08_INP_NO10", reDetailList.get(i).getCASH_TOTAL());//合计现金支付 
				dataList.add(map);
			}
			
			param.put("S08_OUT_NO01", "0");
		}catch(Exception e){
			param.put("S08_OUT_NO01", "-1");
			logger.error(e.getMessage(), e);
		}
		
		param.put("S08_OUT_LIST01", dataList);
		return param;
	}
	
}