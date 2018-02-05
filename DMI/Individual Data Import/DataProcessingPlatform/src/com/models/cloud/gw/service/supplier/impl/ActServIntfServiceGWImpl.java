package com.models.cloud.gw.service.supplier.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.gw.service.supplier.ActServIntfServiceGW;
import com.models.cloud.pay.supplier.entity.ActSp;
import com.models.cloud.pay.supplier.service.ActServIntfService;
import com.models.cloud.pay.supplier.service.SupplierService;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.Propertie;

@Service("actServIntfServiceGWImpl")
public class ActServIntfServiceGWImpl implements ActServIntfServiceGW {

	@Resource(name = "actServIntfServiceImpl")
	private ActServIntfService actServIntfService;

	@Resource(name = "supplierServiceImpl")
	private SupplierService supplierService;

	@Override
	public Map<String, Object> checkPrivilege(Map<String, Object> receiveMap) {
		String appId = receiveMap.get("appId").toString();
		Long operId = Long.parseLong(receiveMap.get("operId")==null?"-1":receiveMap.get("operId").toString());
		String interfaceCode = receiveMap.get("interfaceCode").toString();
		ActSp actSp = null;
		try {
			actSp = supplierService.findSpActByActId(operId);
		} catch (Exception e1) {
			return getResultMap(Hint.SP_12002_SEARCH_ACCOUNT_INFO_ERROR);
		}
		if (actSp == null) {
			return getResultMap(Hint.SP_12004_OPER_ACT_NOT_EXIST);
		} else if (!appId.equals(actSp.getChanAppid())) {
			// 应用id与账户id不匹配
			return getResultMap(Hint.SP_12018_APPID_OPERID_UNMATCHED);
			// 审核状态
		} else if (actSp.getSpOpenChkFlag() != BaseDictConstant.SP_OPEN_CHK_FLAG_OK) {
			return getResultMap(Hint.SP_12020_SP_OPEN_CHECK_NOT_OK);
			// 状态冻结
		} else if (actSp.getActStatId() == BaseDictConstant.ACT_STAT_ID_FREEZEN) {
			return getResultMap(Hint.SP_12021_SP_ACT_STATE_NOT_OK);
			// 检查接口访问权限
		} else if (!actServIntfService.hasPrivilege(operId, interfaceCode)) {
			return getResultMap(Hint.SP_12003_INTERFACE_ACCESS_ERROR);
		}

		Object actIdObject = receiveMap.get("actId");
		if (actIdObject != null
				&& !StringUtils.isEmpty(actIdObject.toString())) {
			Long actId = Long.parseLong(actIdObject.toString());
			try {
				actSp = supplierService.findSpActByActId(actId);
			} catch (Exception e) {
				return getResultMap(Hint.SP_12002_SEARCH_ACCOUNT_INFO_ERROR);
			}
			if (actSp == null) {
				return getResultMap(Hint.SP_12001_DEST_SP_NOT_EXIST);
			} else {
				boolean isChanAct = actSp.getChanActFlag() == BaseDictConstant.CHAN_ACT_FLAG_YES;
				Long chanActId = isChanAct ? actId : actSp.getChanActId();
				if (!operId.toString().equals(Propertie.APPLICATION.value("cms.interface.spActId")) && !operId.toString().equals(chanActId.toString())) {
					return getResultMap(Hint.SP_12005_DEST_SP_OPER_ERROR);
				}
				
				if(operId.toString().equals(Propertie.APPLICATION.value("cms.interface.spActId")) && interfaceCode.equals("freezeSupplier") && !isChanAct){
					return getResultMap(Hint.SP_12005_DEST_SP_OPER_ERROR);
				}
			}
		}
		return getResultMap(Hint.SYS_SUCCESS);
	}

	private Map<String, Object> getResultMap(Hint hint) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", hint.getCodeString());
		resultMap.put("resultDesc", hint.getMessage());
		return resultMap;
	}
}
