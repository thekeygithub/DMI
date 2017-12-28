package com.ts.controller.app.SearchAPI.PaymentAPI.InpatientComprehensive;

import org.springframework.stereotype.Controller;

import com.ts.CXFClient.I41InputHospitalDetailQueryBean;
import com.ts.CXFClient.I41OutputHospitalDetailQueryBean;
import com.ts.CXFClient.MedicarePayment;
import com.ts.CXFClient.MedicarePaymentService;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.util.Transformation;

import net.sf.json.JSONObject;

@Controller
public class HospitalDetailQueryPayBusiness extends PayRSHandler {

	@Override
	protected Object exceBusiness(JSONObject value) {
		//调用接口
		MedicarePaymentService service = new MedicarePaymentService();
		MedicarePayment function = service.getMedicarePaymentPort();
		//CitizenCardQueryImpl cq = new CitizenCardQueryImpl();
		//创建映射实例
		Transformation tf = new Transformation();
		//得到32位识别码
		//V01InputCitizenCardQueryBean card = new V01InputCitizenCardQueryBean();
		//card.setV01_INP_NO01(value.getString("I41_INP_NO03"));//身份证号
		//card.setV01_INP_NO02(value.getString("I41_INP_NO02"));//市民卡号
		//V01OutCitizenCardQueryBean Vout = cq.getCitizenCardQuery(card);
		//value.put("IC_CARD", Vout.getV01OUTNO03());
		//传入参数
		I41InputHospitalDetailQueryBean inModel = new I41InputHospitalDetailQueryBean();
		inModel = (I41InputHospitalDetailQueryBean) tf.setParam(inModel, value);
		//inModel.setICCARD(Vout.getV01_OUT_NO03());
		//返回实体
		I41OutputHospitalDetailQueryBean outModel = function.getHospitalDetailQuery(inModel);
		return tf.toJson(outModel);
	}
	
}