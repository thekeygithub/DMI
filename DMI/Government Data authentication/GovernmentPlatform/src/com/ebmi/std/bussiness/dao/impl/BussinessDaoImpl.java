package com.ebmi.std.bussiness.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.core.utils.DateUtils;
import com.ebmi.std.bussiness.dao.BussinessDao;
import com.ebmi.std.bussiness.dto.Complaint;
import com.ebmi.std.bussiness.dto.SiCardInfo;
import com.ebmi.std.bussiness.dto.SiCardStateInfo;
import com.ebmi.std.bussiness.dto.SiCardStateQueryInfo;
import com.ebmi.std.common.dao.impl.BaseDao;
/**
 * 
 * @Description: 第三方接口实现
 * @ClassName: BussinessDaoImpl 
 * @author: danni.liao
 * @date: 2016年1月8日 下午5:51:43
 */
@Repository
public class BussinessDaoImpl extends BaseDao implements BussinessDao{
	
	// TODO　ＴＥＳＴ
	static Map<String,String> stateMap = new HashMap<String,String>();

	@Override
	public SiCardInfo getSiCardInfo(String siCardNo, String pMiId) throws Exception {
		// TODO 第三方接口实现 
		String sql = "SELECT MOBILE,PHONE,EMAIL,P_ADDR as ADDR,ADDR_POST_CODE as POSTCODE,P_NAME as NAME, p_CERT_NO as IDNO, SI_CARD_NO as SICARDNO FROM P_MI_BASE_INFO "
				+ "where 1=1 ";
		
		StringBuffer strbuff = new StringBuffer();
		strbuff.append(sql);
		List<Object> paramList = new ArrayList<Object>();
//		if(!StringUtils.isBlank(siCardNo)){
//			strbuff.append( " SI_CARD_NO=?" );
//			paramList.add(siCardNo);
//		}
//		
       if(!StringUtils.isBlank(pMiId)){
    	 strbuff.append( " AND P_MI_ID=?" );
	     paramList.add(pMiId);
		}
		return this.getJdbcService().queryForObject(strbuff.toString(), paramList.toArray(), SiCardInfo.class);
	}

	@Override
	public boolean updateInfo(SiCardInfo sicardInfo) throws Exception {
		// TODO 第三方接口实现 
		String sql = "update P_MI_BASE_INFO set MOBILE = ?, PHONE = ?, EMAIL = ?, P_ADDR = ?, ADDR_POST_CODE = ? where P_MI_ID = ?";
		getJdbcService().update(sql, new Object[]{sicardInfo.getMobile(),sicardInfo.getPhone(),
				sicardInfo.getEmail(),sicardInfo.getAddr(),sicardInfo.getPostCode(),sicardInfo.getpMiId()});
		return true;
	}

	@Override
	public boolean lostSiCard(String siCardNo, String pMiId) {
		// TODO 第三方接口实现 
		//大明挂失失败
		if("999888776400".equals(siCardNo)){
			stateMap.put(siCardNo, "1");
			return false;
		}else if("999988887302".equals(siCardNo)){
			stateMap.put(siCardNo, "3");
		}else{
			//办理中
			stateMap.put(siCardNo, "2");
		}
		return true;
	}


	@Override
	public Complaint saveComplaint(Complaint complaint) {
		// TODO 第三方接口实现 
		Complaint c = new Complaint();
		c.setRecordId(String.format("%06d", new Random().nextInt(999999)));
		return c;
	}

	@Override
	public SiCardStateInfo getSiCardStateInfo(String siCardNo, String pMiId)
			throws Exception {
		String state = stateMap.get(siCardNo);
		if(state == null){
			state = "1";
		}
		// TODO 第三方接口实现 
		SiCardStateInfo siCardStateInfo = new SiCardStateInfo();
		siCardStateInfo.setIdNo("1111111111111111111");
		siCardStateInfo.setLockDate("2016-01-04");
		siCardStateInfo.setUnlockDate("2016-01-07");
		siCardStateInfo.setName("測試 ");
		siCardStateInfo.setSiCardNo(siCardNo);
		//1：未挂失，2办理中（未完结）、3已挂失（完结）;
		siCardStateInfo.setState(state);
		return siCardStateInfo;
	}

	@Override
	public void updateLocalInfo(SiCardInfo sicardInfo) throws Exception {
		String sql = "update P_MI_BASE_INFO set MOBILE = ?, PHONE = ?, EMAIL = ?, P_ADDR = ?, ADDR_POST_CODE = ?, UPD_TIME=now() where P_MI_ID = ?";
		getJdbcService().update(sql, new Object[]{sicardInfo.getMobile(),sicardInfo.getPhone(),
				sicardInfo.getEmail(),sicardInfo.getAddr(),sicardInfo.getPostCode(),sicardInfo.getpMiId()});
	}

	@Override
	public SiCardStateQueryInfo queryMakeCardState(String siCardNo)
			throws Exception {
		// TODO 第三方接口实现 
		SiCardStateQueryInfo dto =new SiCardStateQueryInfo();
		if(siCardNo.contains("123456")){
			//社保卡号     查询有值的时候 设置  否则 null
			dto.setSiCardNo(siCardNo);
			Random r = new Random();
			int num = r.nextInt(10);
			int state = num % 8;
			String currentState = String.valueOf(state + 1);
			dto.setCurrentState(currentState);
			dto.setName("王晓华");
			dto.setPlace("天津市崇川区社会保险管理中心服务网点");
			dto.setOperTime("经办日期：" + DateUtils.dts(new Date(), 6));
		}
		return dto;
	}

	@Override
	public Complaint getComplaint(String recordId) throws Exception {
		// TODO
		Complaint complaint = new Complaint();
		complaint.setRecordId(recordId);
		int s = (int)(Math.random()*100);
		s = s%2 == 0?0:1;
		complaint.setState(String.valueOf(s));
		return complaint;
	}

}
