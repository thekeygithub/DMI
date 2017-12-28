package com.ts.service.pay.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_return_detail;
import com.ts.entity.P_total_detail;
import com.ts.service.pay.SettlementDrugstoreManager;
import com.ts.service.pay.SettlementManager;
import com.ts.service.system.appuser.AppuserManager;
import com.ts.util.PageData;
import com.ts.util.StringUtil;
import com.ts.util.UuidUtil;

/**
 * 药店对账数据升迁
 * @ClassName:SettlementDrugstoreService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月27日上午9:37:39
 */
@Service("settlementDrugstoreService")
public class SettlementDrugstoreService implements SettlementDrugstoreManager {

	@Resource(name="appuserService")
	private AppuserManager AppuserService;
	
	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;

	public static String settlementSaveFilePath = ReadPropertiesFiles.getValue("settlement_saveFilePath");
	public static String settlementIsSaveFile = ReadPropertiesFiles.getValue("settlement_isSaveFile");
	
	@Override
	public void saveSettleTotalDetail(Date date) throws Exception {
		Date today = date;
		dao.delete("PtotaldetailMapper.deleteTodayTotal", today);
		dao.update("PtotaldetailMapper.insertTodayTotal", today);
		dao.update("PtotaldetailMapper.insertTodayTotalSum", today);
	}

	@Override
	public void saveSettleDetail(Date date) throws Exception {
		Date today = date;
		dao.delete("PreturndetailMapper.deleteTodayReturn", today);
		dao.update("PreturndetailMapper.insertTodayReturn", today);
		dao.update("PreturndetailMapper.insertTodayReturnSum", today);
	}

	/* (non-Javadoc)
	 * @see com.ts.service.pay.SettlementManager#getI31DataByDate(com.ts.util.PageData)
	 */
	@Override
	public List<PageData> getD02DataByDate(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PtotaldetailMapper.d02DataByDate", pd);
	}

	/* (non-Javadoc)
	 * @see com.ts.service.pay.SettlementManager#getI49DataByDate(com.ts.util.PageData)
	 */
	@Override
	public List<PageData> getD04DataByDate(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PtotaldetailMapper.d04DataByDate", pd);
	}
	/* (non-Javadoc)
	 * @see com.ts.service.pay.SettlementManager#settlement(java.util.Date)
	 */
	public void settlement(Date startDate, Date endDate, String groupId, String hospCode) throws Exception{
		if(StringUtil.isNullOrEmpty(hospCode) || "all".equals(hospCode)){
			hospCode = "";
		}
		if(StringUtil.isNullOrEmpty(groupId) || "all".equals(groupId)){
			groupId = ""; 
		}
		PageData pd = new PageData();
		pd.put("groupId", groupId);
		pd.put("hospCode", hospCode);
		pd.put("startDate", startDate);
		pd.put("endDate", endDate);
		//先删除当天数据
		dao.delete("PreturndetailMapper.deleteTodayReturn", pd);
		dao.delete("PtotaldetailMapper.deleteTodayTotal", pd);
		List<PageData> sysUserList = AppuserService.findForPay();//ts_plat库sys_app_user表
		Map<String, String> sysUserMap = getSysUserMap(sysUserList);
		List<PageData> d04List = getD04DataByDate(pd);
		List<PageData> d02List = getD02DataByDate(pd);
		
		Map<String, P_total_detail> totalMap = new HashMap<String, P_total_detail>();
		Map<String, P_return_detail> returnMap = new HashMap<String, P_return_detail>();
		
		Map<String, List<P_total_detail> > totalFileMap = null;
		Map<String, List<P_return_detail> > returnFileMap = null;
		if("true".equals(settlementIsSaveFile)){
			totalFileMap = new HashMap<String, List<P_total_detail>>();
			returnFileMap = new HashMap<String, List<P_return_detail>>();
		}
		//遍历结算列表
		for (PageData pageData : d02List) {
			P_total_detail thisTotalDetail = getTotalDetailByPageDate(pageData, startDate, sysUserMap);
			String key = new StringBuilder().append(thisTotalDetail.getGROUP_ID()).append("_").append(thisTotalDetail.getHOS_CODE()).toString();
			P_total_detail totalDetail = totalMap.get(key);
			String uuid = "";
			if(totalDetail == null){
				uuid = UuidUtil.get32UUID();
				totalDetail = getPTotalDetail(thisTotalDetail, uuid);
				totalDetail.setPAY_NUM(1);
				totalDetail.setCONFIRM_NUM(1);
			}else{
				uuid = totalDetail.getID();
				totalDetail = getTotalSumObj(thisTotalDetail, totalDetail, 0, 1);
			}
			totalMap.put(key, totalDetail);
			thisTotalDetail.setSUPER_ID(uuid);
			dao.save("PtotaldetailMapper.save", thisTotalDetail);
			addTotalDetailListData(totalFileMap, thisTotalDetail, key+"_detail");
			
		}	
		//遍历退费列表
		for (PageData pageData : d04List) {
			//P_total_detail thisTotalDetail = getTotalDetailByPageDate(pageData, startDate, sysUserMap);
			P_return_detail thisReturnDetail = getReturnDetailByPageDate(pageData, startDate, sysUserMap);
			String key = new StringBuilder().append(thisReturnDetail.getGROUP_ID()).append("_").append(thisReturnDetail.getHOS_CODE()).toString();
			/*P_total_detail totalDetail = totalMap.get(key);
			String uuid = "";
			if(totalDetail == null){
				uuid = UuidUtil.get32UUID();
				totalDetail = getPTotalDetail(thisTotalDetail, uuid);
				totalDetail.setPAY_NUM(1);
				totalDetail.setRETURN_NUM(1);
			}else{
				uuid = totalDetail.getID();
				totalDetail = getTotalSumObj(thisTotalDetail, totalDetail, 1, 0);
			}
			thisTotalDetail.setSUPER_ID(uuid);
			totalMap.put(key, totalDetail);*/
			
			P_return_detail returnDetail = returnMap.get(key);
			String uuid1 = "";
			if(returnDetail == null){
				uuid1 = UuidUtil.get32UUID();
				returnDetail = getPReturnDetail(thisReturnDetail, uuid1);
				returnDetail.setRETURN_NUM(1);
			}else{
				uuid1 = returnDetail.getID();
				returnDetail = getSumReturnObj(thisReturnDetail, returnDetail);
			}
			thisReturnDetail.setSUPER(uuid1);
			returnMap.put(key, returnDetail);
			//dao.save("PtotaldetailMapper.save", thisTotalDetail);
			dao.save("PreturndetailMapper.save", thisReturnDetail);
			//将数据添加到集合 最后输出到文件
			//addTotalDetailListData(totalFileMap, thisTotalDetail, key+"_detail");
			addReturnDetailListData(returnFileMap, thisReturnDetail, key+"_returndetail");
			
		}
		for (String key : totalMap.keySet()) {
			dao.save("PtotaldetailMapper.save", totalMap.get(key));
			addTotalDetailListData(totalFileMap, totalMap.get(key), key+"_total");
		}
		for (String key : returnMap.keySet()) {
			dao.save("PreturndetailMapper.save", returnMap.get(key));
			addReturnDetailListData(returnFileMap, returnMap.get(key), key+"_returntotal");
		}
		//写文件
		writeTotalDetailFile(totalFileMap, startDate);
		writeReturnDetailFile(returnFileMap, startDate);
	}
	private Map<String, String> getSysUserMap(List<PageData> sysUserList) {
		 Map<String, String> sysUserMap = new HashMap<String, String>();
		for(int j=0; j<sysUserList.size(); j++){
			PageData pd = (PageData)sysUserList.get(j);
			String group = pd.get("GROUP_ID").toString();
			String groupName = pd.get("GROUP_NAME").toString();
			sysUserMap.put(group, groupName);
		}
		return sysUserMap;
	}

	/**将TotalDetail写入文件
	 * @param totalFileMap
	 * @param dataDate
	 */
	private void writeReturnDetailFile(Map<String, List<P_return_detail> > returnFileMap, Date dataDate){
		try{
			if(returnFileMap == null){
				return;
			}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dataDateStr  = sdf.format(dataDate);
		for (String key : returnFileMap.keySet()) {
			List<P_return_detail> list = returnFileMap.get(key);
			if(list != null && list.size()>0){
				//写文件
				String[] keyStrArr = key.split("_");
				String contentType = keyStrArr[keyStrArr.length-1];
				StringBuilder pathBuilder = new StringBuilder(settlementSaveFilePath);
				if(pathBuilder != null && !pathBuilder.toString().endsWith("/")){
					pathBuilder.append("/");
				}
				pathBuilder.append(list.get(0).getGROUP_NAME()).append("-").append(list.get(0).getGROUP_ID()).append("/");
				pathBuilder.append(dataDateStr).append("/");
				pathBuilder.append(list.get(0).getHOS_NAME()).append("-").append(list.get(0).getHOS_CODE()).append("/");
				File directory = new File(pathBuilder.toString());
				if  (!directory .exists() || !directory .isDirectory()){
					directory.mkdirs();
				}
				pathBuilder.append(contentType).append(".txt");
				File file=new File(pathBuilder.toString());
		        if(!file.exists())
		            file.createNewFile();
		        FileOutputStream out=new FileOutputStream(file,false); //如果追加方式用true   
		        StringBuffer sb=new StringBuffer();
		        for (P_return_detail obj : list) {
					sb.append(getReturnFileContentByObj(obj)).append("\n");
				}
		        out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
		        out.close();
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**将TotalDetail写入文件
	 * @param totalFileMap
	 * @param dataDate
	 */
	private void writeTotalDetailFile(Map<String, List<P_total_detail> > totalFileMap, Date dataDate){
		try{
			if(totalFileMap == null){
				return;
			}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dataDateStr  = sdf.format(dataDate);
		for (String key : totalFileMap.keySet()) {
			List<P_total_detail> list = totalFileMap.get(key);
			if(list != null && list.size()>0){
				//写文件
				String[] keyStrArr = key.split("_");
				String contentType = keyStrArr[keyStrArr.length-1];
				StringBuilder pathBuilder = new StringBuilder(settlementSaveFilePath);
				if(pathBuilder != null && !pathBuilder.toString().endsWith("/")){
					pathBuilder.append("/");
				}
				pathBuilder.append(list.get(0).getGROUP_NAME()).append("-").append(list.get(0).getGROUP_ID()).append("/");
				pathBuilder.append(dataDateStr).append("/");
				pathBuilder.append(list.get(0).getHOS_NAME()).append("-").append(list.get(0).getHOS_CODE()).append("/");
				File directory = new File(pathBuilder.toString());
				if  (!directory .exists() || !directory .isDirectory()){
					directory.mkdirs();
				}
				pathBuilder.append(contentType).append(".txt");
				File file=new File(pathBuilder.toString());
		        if(!file.exists())
		            file.createNewFile();
		        FileOutputStream out=new FileOutputStream(file,false); //如果追加方式用true   
		        StringBuffer sb=new StringBuffer();
		        for (P_total_detail obj : list) {
					sb.append(getTotalFileContentByObj(obj)).append("\n");
				}
		        out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
		        out.close();
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**将对象P_return_detail 转成文件行
	 * @param thisReturnDetail
	 * @return
	 */
	private String getReturnFileContentByObj(P_return_detail thisReturnDetail) {
		return String.format("%s\t%s\t%d\t%tF\t%d\t%tF %tT\t%s\t%s\t%s\t%s\t%s\t%g\t%s\t%g\t%d\t%g\t%g\t%s\t%s\t%g\t%s\t%g\t", 
				thisReturnDetail.getID(),
				thisReturnDetail.getAPI_TYPE(),
				thisReturnDetail.getCHECK_TYPE(),
				thisReturnDetail.getDATA_DATE(),
				thisReturnDetail.getDATA_TYPE(),
				thisReturnDetail.getFINAL_DATE(),
				thisReturnDetail.getFINAL_DATE(),
				thisReturnDetail.getFINAL_NO(),
				thisReturnDetail.getGROUP_ID(),
				thisReturnDetail.getGROUP_NAME(),
				thisReturnDetail.getHOS_CODE(),
				thisReturnDetail.getHOS_NAME(),
				thisReturnDetail.getMED_FEE(),
				thisReturnDetail.getREQ_NO(),
				thisReturnDetail.getRETURN_FEE(),
				thisReturnDetail.getRETURN_NUM(),
				thisReturnDetail.getSELF_NEG(),
				thisReturnDetail.getSELF_PAY(),
				thisReturnDetail.getSUPER(),
				thisReturnDetail.getTIME_STAMP(),
				thisReturnDetail.getTOTAL_FEE(),
				thisReturnDetail.getVISIT_NO(),
				thisReturnDetail.getCASH_TOTAL());
	}
	/**将对象P_total_detail 转成文件行
	 * @param thisTotalDetail
	 * @return
	 */
	private String getTotalFileContentByObj(P_total_detail thisTotalDetail) {
		return String.format("%s\t%s\t%d\t%tF\t%d\t%tF %tT\t%s\t%s\t%s\t%s\t%s\t%g\t%s\t%g\t%d\t%g\t%g\t%s\t%s\t%g\t%s\t%d\t%d\t%g\t", 
				thisTotalDetail.getID(),
				thisTotalDetail.getAPI_TYPE(),
				thisTotalDetail.getCHECK_TYPE(),
				thisTotalDetail.getDATA_DATE(),
				thisTotalDetail.getDATA_TYPE(),
				thisTotalDetail.getFINAL_DATE(),
				thisTotalDetail.getFINAL_DATE(),
				thisTotalDetail.getFINAL_NO(),
				thisTotalDetail.getGROUP_ID(),
				thisTotalDetail.getGROUP_NAME(),
				thisTotalDetail.getHOS_CODE(),
				thisTotalDetail.getHOS_NAME(),
				thisTotalDetail.getMED_FEE(),
				thisTotalDetail.getREQ_NO(),
				thisTotalDetail.getRETURN_FEE(),
				thisTotalDetail.getRETURN_NUM(),
				thisTotalDetail.getSELF_NEG(),
				thisTotalDetail.getSELF_PAY(),
				thisTotalDetail.getSUPER_ID(),
				thisTotalDetail.getTIME_STAMP(),
				thisTotalDetail.getTOTAL_FEE(),
				thisTotalDetail.getVISIT_NO(),
				thisTotalDetail.getPAY_NUM(),
				thisTotalDetail.getCONFIRM_NUM(),
				thisTotalDetail.getCASH_TOTAL());
	}
	/**汇总对象P_return_detail累加参数值
	 * @param thisReturnDetail
	 * @param returnDetail
	 * @return
	 */
	private P_return_detail getSumReturnObj(P_return_detail thisReturnDetail, P_return_detail returnDetail) {
		returnDetail.setRETURN_NUM(returnDetail.getRETURN_NUM() + 1);
		returnDetail.setRETURN_FEE(returnDetail.getRETURN_FEE()+thisReturnDetail.getRETURN_FEE());
		returnDetail.setTOTAL_FEE(returnDetail.getTOTAL_FEE()+thisReturnDetail.getTOTAL_FEE());
		returnDetail.setSELF_NEG(returnDetail.getSELF_NEG()+thisReturnDetail.getSELF_NEG());
		returnDetail.setSELF_PAY(returnDetail.getSELF_PAY()+thisReturnDetail.getSELF_PAY());
		returnDetail.setMED_FEE(returnDetail.getMED_FEE()+thisReturnDetail.getMED_FEE());
		returnDetail.setCASH_TOTAL(returnDetail.getCASH_TOTAL()+thisReturnDetail.getCASH_TOTAL());
		return returnDetail;
	}

	/**汇总对象P_total_detail累加参数值
	 * @param thisTotalDetail
	 * @param totalDetail
	 * @param returnNum
	 * @param confirmNum
	 * @return
	 */
	private P_total_detail getTotalSumObj(P_total_detail thisTotalDetail, P_total_detail totalDetail, int returnNum, int confirmNum) {
		totalDetail.setRETURN_NUM(totalDetail.getRETURN_NUM() + returnNum);
		totalDetail.setCONFIRM_NUM(totalDetail.getCONFIRM_NUM() + confirmNum);
		totalDetail.setPAY_NUM(totalDetail.getPAY_NUM() + 1);
		totalDetail.setRETURN_FEE(totalDetail.getRETURN_FEE()+thisTotalDetail.getRETURN_FEE());
		totalDetail.setTOTAL_FEE(totalDetail.getTOTAL_FEE()+thisTotalDetail.getTOTAL_FEE());
		totalDetail.setSELF_NEG(totalDetail.getSELF_NEG()+thisTotalDetail.getSELF_NEG());
		totalDetail.setSELF_PAY(totalDetail.getSELF_PAY()+thisTotalDetail.getSELF_PAY());
		totalDetail.setMED_FEE(totalDetail.getMED_FEE()+thisTotalDetail.getMED_FEE());
		totalDetail.setCASH_TOTAL(totalDetail.getCASH_TOTAL()+thisTotalDetail.getCASH_TOTAL());
		return totalDetail;
	}

	/**添加P_total_detail数据到集合
	 * @param totalFileMap
	 * @param thisTotalDetail
	 * @param key
	 */
	private void addTotalDetailListData(Map<String, List<P_total_detail>> totalFileMap, P_total_detail thisTotalDetail,
			String key) {
		if(totalFileMap == null){
			return;
		}
		List<P_total_detail> fileList = totalFileMap.get(key);
		if(fileList == null){
			fileList = new ArrayList<P_total_detail>();
		}
		fileList.add(thisTotalDetail);
		totalFileMap.put(key, fileList);
	}
	/**添加P_return_detail数据到集合
	 * @param returnFileMap
	 * @param thisReturnDetail
	 * @param key
	 */
	private void addReturnDetailListData(Map<String, List<P_return_detail>> returnFileMap, P_return_detail thisReturnDetail,
			String key) {
		if(returnFileMap == null){
			return;
		}
		List<P_return_detail> fileList = returnFileMap.get(key);
		if(fileList == null){
			fileList = new ArrayList<P_return_detail>();
		}
		fileList.add(thisReturnDetail);
		returnFileMap.put(key, fileList);
	}

	/**初始化P_return_detail汇总对象
	 * @param totalDetailParam
	 * @param uuid
	 * @return
	 */
	private P_return_detail getPReturnDetail(P_return_detail totalDetailParam, String uuid) {
		P_return_detail returnDetail = new P_return_detail();
		returnDetail.setID(uuid);
		returnDetail.setDATA_TYPE(1);//1总额对账，2总额明细
		returnDetail.setGROUP_ID(totalDetailParam.getGROUP_ID());
		returnDetail.setGROUP_NAME(totalDetailParam.getGROUP_NAME());
		returnDetail.setHOS_CODE(totalDetailParam.getHOS_CODE());
		returnDetail.setHOS_NAME(totalDetailParam.getHOS_NAME());
		returnDetail.setFINAL_DATE(totalDetailParam.getDATA_DATE());
		returnDetail.setRETURN_NUM(0);
		returnDetail.setTOTAL_FEE(totalDetailParam.getTOTAL_FEE());
		returnDetail.setSELF_PAY(totalDetailParam.getSELF_PAY());
		returnDetail.setSELF_NEG(totalDetailParam.getSELF_NEG());
		returnDetail.setMED_FEE(totalDetailParam.getMED_FEE());
		returnDetail.setRETURN_FEE(totalDetailParam.getRETURN_FEE());
		returnDetail.setCHECK_TYPE(totalDetailParam.getCHECK_TYPE());
		returnDetail.setDATA_DATE(totalDetailParam.getDATA_DATE());
		returnDetail.setCASH_TOTAL(totalDetailParam.getCASH_TOTAL());
		return returnDetail;
	}

	/**初始化P_total_detail汇总对象
	 * @param totalDetailParam
	 * @param id
	 * @return
	 */
	private P_total_detail getPTotalDetail(P_total_detail totalDetailParam, String id) {
		P_total_detail totalDetail = new P_total_detail();
		totalDetail.setID(id);
		totalDetail.setDATA_TYPE(1);//1总额对账，2总额明细
		totalDetail.setGROUP_ID(totalDetailParam.getGROUP_ID());
		totalDetail.setGROUP_NAME(totalDetailParam.getGROUP_NAME());
		totalDetail.setHOS_CODE(totalDetailParam.getHOS_CODE());
		totalDetail.setHOS_NAME(totalDetailParam.getHOS_NAME());
		totalDetail.setFINAL_DATE(totalDetailParam.getDATA_DATE());
		totalDetail.setPAY_NUM(0);
		totalDetail.setCONFIRM_NUM(0);
		totalDetail.setRETURN_NUM(0);
		totalDetail.setTOTAL_FEE(totalDetailParam.getTOTAL_FEE());
		totalDetail.setSELF_PAY(totalDetailParam.getSELF_PAY());
		totalDetail.setSELF_NEG(totalDetailParam.getSELF_NEG());
		totalDetail.setMED_FEE(totalDetailParam.getMED_FEE());
		totalDetail.setRETURN_FEE(totalDetailParam.getRETURN_FEE());
		totalDetail.setCHECK_TYPE(totalDetailParam.getCHECK_TYPE());
		totalDetail.setDATA_DATE(totalDetailParam.getDATA_DATE());
		totalDetail.setCASH_TOTAL(totalDetailParam.getCASH_TOTAL());
		return totalDetail;
	}

	/**将数据库查询结果转成P_total_detail对象
	 * @param pageData
	 * @param dataDate
	 * @param sysUserMap 
	 * @return
	 */
	private P_total_detail getTotalDetailByPageDate(PageData pageData, Date dataDate, Map<String, String> sysUserMap) {
		String uuid = UuidUtil.get32UUID();
		P_total_detail totalDetail = new P_total_detail();
		totalDetail.setAPI_TYPE(pageData.getString(""));
		totalDetail.setCHECK_TYPE(3);//0门诊和住院，1门诊，2住院 ，3药店
		totalDetail.setDATA_TYPE(2);//1总额对账，2总额明细
		totalDetail.setFINAL_DATE((Date) pageData.get("CREATE_DATE"));//这里存数据的生成时间
		totalDetail.setFINAL_NO(pageData.getString("MEDICARE_NO"));//结算流水号，此处为医保流水号
		totalDetail.setGROUP_ID(pageData.getString("GROUP_ID"));
		totalDetail.setGROUP_NAME(sysUserMap.get(pageData.getString("GROUP_ID")));
		totalDetail.setHOS_CODE(pageData.getString("SERVICE_NO"));//服务机构编码
//		totalDetail.setHOS_NAME(pageData.getString("HOS_NAME"));//服务机构名称
		totalDetail.setID(uuid);
		totalDetail.setMED_FEE(0.00);//设置为0
		totalDetail.setREQ_NO(pageData.getString("REQ_NO"));//请求流水号
		double all_ret=((BigDecimal) pageData.get("LAR_PAY")).doubleValue()
				+((BigDecimal) pageData.get("PLAN_PAY")).doubleValue()
				+((BigDecimal) pageData.get("SER_PAY")).doubleValue()
				;
		totalDetail.setRETURN_FEE(all_ret);//合计报销金额
		totalDetail.setSELF_NEG(0.00);//设置为0
		totalDetail.setSELF_PAY(((BigDecimal) pageData.get("SELF_ACCOUNT_PAY")).doubleValue()+((BigDecimal) pageData.get("SELF_CASH_PAY")).doubleValue());//自费总额，个人账户+个人现金
		totalDetail.setTIME_STAMP(pageData.getString("TIME_STAMP"));//
		totalDetail.setTOTAL_FEE(((BigDecimal) pageData.get("SELF_ACCOUNT_PAY")).doubleValue());//费用总额，个人账户支付金额
		totalDetail.setVISIT_NO(pageData.getString("MEDICARE_NO"));//此处赋值为医保流水号
		totalDetail.setDATA_DATE(dataDate);
		totalDetail.setCASH_TOTAL(((BigDecimal) pageData.get("SELF_CASH_PAY")).doubleValue());//合计现金支付，个人现金支付金额
		return totalDetail;
	}
	/**将数据库查询结果转成P_return_detail对象
	 * @param pageData
	 * @param dataDate
	 * @param sysUserMap 
	 * @return
	 */
	private P_return_detail getReturnDetailByPageDate(PageData pageData, Date dataDate, Map<String, String> sysUserMap) {
		String uuid = UuidUtil.get32UUID();
		P_return_detail returnDetail = new P_return_detail();
		returnDetail.setAPI_TYPE(pageData.getString(""));
		returnDetail.setCHECK_TYPE(3);//0门诊和住院，1门诊，2住院  ,3药店
		returnDetail.setDATA_TYPE(2);
		returnDetail.setFINAL_DATE((Date) pageData.get("CREATE_DATE"));//这里存数据的生成时间
		returnDetail.setFINAL_NO(pageData.getString("MEDICARE_NO"));
		returnDetail.setGROUP_ID(pageData.getString("GROUP_ID"));
		returnDetail.setGROUP_NAME(sysUserMap.get(pageData.getString("GROUP_ID")));
		returnDetail.setHOS_CODE(pageData.getString("SERVICE_NO"));
//		returnDetail.setHOS_NAME(pageData.getString("HOS_NAME"));
		returnDetail.setID(uuid);
		returnDetail.setMED_FEE(0.00);//设置为0
		returnDetail.setREQ_NO(pageData.getString("REQ_NO"));
		double all_ret=((BigDecimal) pageData.get("LAR_PAY")).doubleValue()
				+((BigDecimal) pageData.get("PLAN_PAY")).doubleValue()
				+((BigDecimal) pageData.get("SER_PAY")).doubleValue()
				;
		returnDetail.setRETURN_FEE(all_ret);//
		returnDetail.setSELF_NEG(0.00);//设置为0
		returnDetail.setSELF_PAY(((BigDecimal) pageData.get("SELF_ACCOUNT_PAY")).doubleValue()+((BigDecimal) pageData.get("SELF_CASH_PAY")).doubleValue());
		returnDetail.setTIME_STAMP(pageData.getString("TIME_STAMP"));//
		returnDetail.setTOTAL_FEE(((BigDecimal) pageData.get("SELF_ACCOUNT_PAY")).doubleValue());
		returnDetail.setVISIT_NO(pageData.getString("MEDICARE_NO"));
		returnDetail.setDATA_DATE(dataDate);
		returnDetail.setCASH_TOTAL(((BigDecimal) pageData.get("SELF_CASH_PAY")).doubleValue());
		return returnDetail;
	}

	/* (non-Javadoc)
	 * @see com.ts.service.pay.SettlementManager#getHospList()
	 */
	@Override
	public List<PageData> getHospList() throws Exception {
		return (List<PageData>) dao.findForList("PdllserverMapper.listDistinctHospId", null);
	}

	/* (non-Javadoc)
	 * @see com.ts.service.pay.SettlementManager#getGroupList()
	 */
	@Override
	public List<PageData> getGroupList() throws Exception {
		return (List<PageData>) dao.findForList("PtotaldetailMapper.listDistinctGroupId", null);
	}
}