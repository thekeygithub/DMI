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
import com.ts.entity.P_total_detail;
import com.ts.entity.Reconciliation.S01InputTotalReconciliationBean;
import com.ts.service.pay.PrecordManager;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: DetailRecordDown 
* @Description: TODO(明细对账下载接口) 
* @author Lee 刘峰
* @date 2017年5月4日 上午14:30:00
*
 */

@Controller
@Service
public class DetailRecordDown extends PayRSHandler {

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
			String hosp_code = value.getString("S06_INP_NO01");//医院编码
			String final_start_date = value.getString("S06_INP_NO02");//交易开始时间
			String final_end_date = value.getString("S06_INP_NO03");//交易结束时间
			String record_type = value.getString("S06_INP_NO04");//对账类型
			
			S01InputTotalReconciliationBean inModel = new S01InputTotalReconciliationBean();
			inModel.setS01_INP_NO13(organ_code);
			inModel.setS01_INP_NO01(hosp_code);
			inModel.setS01_INP_NO02(final_start_date+" 00:00:00");
			inModel.setS01_INP_NO03(final_end_date+" 23:59:59");
			inModel.setS01_INP_NO12(record_type);
			inModel.setDATA_TYPE("2");
			
			List<P_total_detail> detailDownList = precordService.queryTotalRecord(inModel);
			for(int i=0; i<detailDownList.size(); i++){
				Map map = new LinkedHashMap();
				map.put("S06_OUT_LIST01_NO01", detailDownList.get(i).getHOS_CODE());//医院编码
				map.put("S06_OUT_LIST01_NO02", detailDownList.get(i).getVISIT_NO());//就诊号
				map.put("S06_OUT_LIST01_NO03", detailDownList.get(i).getFINAL_NO());//结算流水号
				map.put("S06_OUT_LIST01_NO04", detailDownList.get(i).getFINAL_DATE()==null?null:sf.format(detailDownList.get(i).getFINAL_DATE()));//交易时间
				map.put("S06_OUT_LIST01_NO05", detailDownList.get(i).getTOTAL_FEE());//费用总额
				map.put("S06_OUT_LIST01_NO06", detailDownList.get(i).getSELF_PAY());//自费总额(非医保)
				map.put("S06_OUT_LIST01_NO07", detailDownList.get(i).getSELF_NEG());//药品乙类自负
				map.put("S06_OUT_LIST01_NO08", detailDownList.get(i).getMED_FEE());//医保费用
				map.put("S06_OUT_LIST01_NO09", detailDownList.get(i).getRETURN_FEE());//合计报销支付 
				map.put("S06_OUT_LIST01_NO11", detailDownList.get(i).getCASH_TOTAL());//合计现金支付 
				map.put("S06_OUT_LIST01_NO10", detailDownList.get(i).getREQ_NO()==null?"":detailDownList.get(i).getREQ_NO());//请求流水号
				dataList.add(map);
			}
			
			param.put("S06_OUT_NO01", "0");
		}catch(Exception e){
			param.put("S06_OUT_NO01", "-1");
			logger.error(e.getMessage(), e);
		}
		
		param.put("S06_OUT_LIST01", dataList);
		return param;
	}
	
}