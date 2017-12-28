package com.ts.service.pay.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_bill;
import com.ts.entity.P_bill_item;
import com.ts.entity.P_drugstore_drug_item;
import com.ts.entity.P_drugstore_inte_info;
import com.ts.entity.P_drugstore_result;
import com.ts.entity.P_fee;
import com.ts.entity.P_fund;
import com.ts.entity.P_insu_info;
import com.ts.entity.P_inte_info;
import com.ts.entity.P_over_detail;
import com.ts.entity.P_result;
import com.ts.entity.P_user;
import com.ts.service.pay.PayManager;
import com.ts.util.PageData;
import com.ts.util.Transformation;
import com.ts.util.UuidUtil;

@Service("payService")
public class PayService implements PayManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	@SuppressWarnings ("unchecked")
	public void insertPay(P_inte_info pif) throws Exception {
		//用户信息
		PageData pd = new PageData();
		String userID = "";
		P_user pdUser = pif.getPUser();
		pd.put("card_no", pdUser.getCARD_NO());
		pd.put("id_card", pdUser.getID_CARD());
		pd.put("ic_card", pdUser.getIC_CARD());
		
		// 查找是否存在用户 
		pdUser = (P_user) dao.findForObject("PuserMapper.findById", pd);
		if(pdUser == null){
		    userID = UuidUtil.get32UUID();
		    pdUser = pif.getPUser();
		    pdUser.setID(userID);
			dao.save("PuserMapper.save", pdUser);
		}
		userID = pdUser.getID();
		
		//参保人员信息
        P_insu_info piis = pif.getPUser().getPInsuInfo();
        if(piis.getAPI_TYPE()!=null && "I22".equals(piis.getAPI_TYPE()) && piis.getREQ_NO()!=null && !"".equals(piis.getREQ_NO()))
        {
            piis.setID(UuidUtil.get32UUID());
        	piis.setUSER_ID(userID);
            dao.save("PinsuinfoMapper.save", piis);             
        }
		
		//接口数据信息
		String iID = "";

		if(pif.getAPI_TYPE() !=null && !"".equals(pif.getAPI_TYPE()) && pif.getREQ_NO() != null && !"".equals(pif.getREQ_NO()))
		{
			pif.setUSER_ID(userID);
		    // 需要关联就诊号功能 ， 参与接口为：门诊退费: 3 ， 交易确认： 9。
		    if(pif.getDATA_TYPE() == 3 || pif.getDATA_TYPE() == 9 )
		    {
		        //本期完事 字段需要合并   结算流水号， 要作废的结算流水号， 医保交易流水号 ，交易结算流水号 ，  目前分状态
		        if(pif.getDATA_TYPE() == 3) pif.setFINAL_NO(pif.getABO_DEAL_NO());
		        if(pif.getDATA_TYPE() == 9) pif.setFINAL_NO(pif.getMED_DEAL_NO());
                
		        List<PageData> pds = (List<PageData>)dao.findForList("PinteinfoMapper.findByVisit_no", pif);
		        if(pds != null  && pds.size() > 0 ) 
	            {
		            //添加 医院交易流水号做关联   为了体现整个交易业务过程 。  
		            pif.setVISIT_NO( pds.get(0).getString("visit_no"));
	            }
		        
		    }
		    iID = UuidUtil.get32UUID();
		    pif.setID(iID);
			dao.save("PinteinfoMapper.save", pif);	
		}
		
		//费用汇总信息
		List<P_fee> fees = pif.getPFee();
		for(P_fee fee: fees){
			fee.setID(UuidUtil.get32UUID());
			fee.setIN_ID(iID);
			dao.save("PfeeMapper.save", fee);
		}
		
		//基金分段信息
		List<P_fund> funds = pif.getPFund();
		for(P_fund fund : funds){
			fund.setID(UuidUtil.get32UUID());
			fund.setIN_ID(iID);
			dao.save("PfundMapper.save", fund);
		}
		
		//计算结果信息
		List<P_result> prs = pif.getPResult();
		for(P_result pr : prs){
			pr.setID(UuidUtil.get32UUID());
			pr.setIN_ID(iID);
			dao.save("PresultMapper.save", pr);
		}
		
		//超限明细表
		List<P_over_detail> pods = pif.getPOverDetail();
		for(P_over_detail pod : pods){
			pod.setID(UuidUtil.get32UUID());
			pod.setIN_ID(iID);
			dao.save("PoverdetailMapper.save", pod);
		}
		
		//结算单据
		List<P_bill> bills = pif.getPBill();
		for(P_bill bill : bills){
			String bID = UuidUtil.get32UUID();
			bill.setID(bID);
			bill.setIN_ID(iID);
			dao.save("PbillMapper.save", bill);
			
			//收费项目
			List<P_bill_item> items = bill.getPBillItem();
			for(P_bill_item item : items){
				item.setID(UuidUtil.get32UUID());
				item.setB_ID(bID);
				dao.save("PbillitemMapper.save", item);
			}
		
		}
		//未匹配上的收费项目
		List<P_bill_item> billItems = pif.getPBillItem();
		for (P_bill_item p_bill_item : billItems) {
			p_bill_item.setID(UuidUtil.get32UUID());
			dao.save("PbillitemMapper.save", p_bill_item);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updatePrList(P_inte_info pif) throws Exception {
		PageData pd = new PageData();
		pd.put("VISIT_NO", pif.getSEARCH_DEAL_NO());
		pd.put("API_TYPE","%" +pif.getSEARCH_TYPE_NO()+"%");
		pd.put("HOS_CODE", pif.getHOS_CODE());
		pd.put("GROUP_ID", pif.getGROUP_ID());
		pd.put("DATA_TYPE", "9");
		PageData  data =  (PageData) dao.findForObject("PinteinfoMapper.findByDataId", pd);
		PageData  inte =  (PageData) dao.findForObject("PinteinfoMapper.findByDealId", pd);
		List<PageData> funds = (List<PageData>) dao.findForList("PfundMapper.findByfundId", pd);
		List<PageData> result = (List<PageData>) dao.findForList("PresultMapper.findByResId", pd);

		if(pif.getDEAL_STEP().equals("1")){
			//判断28交易状态
			if((inte.get("DEAL_STAT").toString()).equals("0")){
				this.forupdate(pif, funds, result);
//				dao.update("PinteinfoMapper.edit", inte);
			}
			//判断49交易确认状态
			if(!(data.get("DEAL_STAT").toString()).equals("0")){
				data.put("DEAL_STAT", "0");
				data.put("ERROR", "");
				dao.update("PinteinfoMapper.edit", data);
			}
		}else if(pif.getDEAL_STEP().equals("0")){
			//判断28交易状态
			if((inte.get("DEAL_STAT").toString()).equals("0")){
				this.forupdate(pif, funds, result);
			}
		}else if(pif.getDEAL_STEP().equals("")){
			this.forupdate(pif, funds, result);
		}
	}
	
	@Override
	public String findUserID() throws Exception {
		return (String)dao.findForObject("PuserMapper.findUserID", null);
	}

	@Override
	public String findInteID() throws Exception {
		return (String)dao.findForObject("PinteinfoMapper.findInteID", null);
	}

	@Override
	public String findBillID() throws Exception {
		return (String)dao.findForObject("PbillMapper.findBillID", null);
	}

	@SuppressWarnings("rawtypes")
	public void forupdate(P_inte_info pif, List<PageData> funds, List<PageData> result) throws Exception {
		//基金分段信息
		List<P_fund> Pfund = pif.getPFund();
		for(int p=0; p<Pfund.size(); p++){
			P_fund pfd = Pfund.get(p);
			for(int i=0; i<funds.size(); i++){
				PageData pid = funds.get(i);
				String id = pid.getString("id");
				String in_id = pid.getString("in_id");
				pfd.setID(id);
				pfd.setIN_ID(in_id);
				Map pds = Transformation.beanToMap(pfd);
				if(!pid.equals(pds)){
					dao.update("PfundMapper.editINID", pds);
					funds.remove(pid);
					break;
				}
			}	
		}
		
		for(PageData prid:result){
			String id = prid.getString("id");
			String in_id = prid.getString("in_id");
			//计算结果信息
			List<P_result> prs = pif.getPResult();
			for(P_result pr : prs){
				pr.setID(id);
				pr.setIN_ID(in_id);
				dao.update("PresultMapper.editINID", pr);
			}
		}
	}
	//----------------------------药店支付的业务---------------------------------------
	@Override
	@SuppressWarnings ("unchecked")
	public void insertPay_Drugstore(P_drugstore_inte_info pif) throws Exception {
		//接口数据信息
		String iID = "";

		if(pif.getAPI_TYPE() !=null && !"".equals(pif.getAPI_TYPE()) && pif.getREQ_NO() != null && !"".equals(pif.getREQ_NO()))
		{
//		    // 需要关联就诊号功能 ， 参与接口为：门诊退费: 3 ， 交易确认： 9。
//		    if(pif.getDATA_TYPE() == 3 || pif.getDATA_TYPE() == 9 )
//		    {
//		        //本期完事 字段需要合并   结算流水号， 要作废的结算流水号， 医保交易流水号 ，交易结算流水号 ，  目前分状态
//		        if(pif.getDATA_TYPE() == 3) pif.setFINAL_NO(pif.getABO_DEAL_NO());
//		        if(pif.getDATA_TYPE() == 9) pif.setFINAL_NO(pif.getMED_DEAL_NO());
//                
//		        List<PageData> pds = (List<PageData>)dao.findForList("PinteinfoMapper.findByVisit_no", pif);
//		        if(pds != null  && pds.size() > 0 ) 
//	            {
//		            //添加 医院交易流水号做关联   为了体现整个交易业务过程 。  
//		            pif.setVISIT_NO( pds.get(0).getString("visit_no"));
//	            }
//		        
//		    }
		    iID = UuidUtil.get32UUID();
		    pif.setID(iID);
			dao.save("PdrugstoreinteinfoMapper.save", pif);	
		}
		
		//药品信息
		List<P_drugstore_drug_item> drugs = pif.getpDrug();
		for (P_drugstore_drug_item temp : drugs) {
			temp.setID(UuidUtil.get32UUID());
			temp.setIN_ID(iID);
			dao.save("PdrugstoredrugitemMapper.save",temp);
		}
		//计算信息
		List<P_drugstore_result> rs = pif.getpResult();
		for (P_drugstore_result temp : rs) {
			temp.setID(UuidUtil.get32UUID());
			temp.setIN_ID(iID);
			dao.save("PdrugstoreresultMapper.save",temp);
		}
	}
}