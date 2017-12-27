package com.aghit.task.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.aghit.task.algorithm.domain.BreakDetail;
import com.aghit.task.algorithm.domain.BreakDetailItem;
import com.aghit.task.algorithm.domain.BreakMaster;
import com.aghit.task.util.CPADateUtil;
import com.aghit.task.util.Constant;

public class BreakDetailMgr extends Manager {

	// 批量操作提交数量
	public static final int COMMIT_SIZE = 200;
	
	// 线程局部变量，保存违规记录的集合
	private List<BreakDetail> curList = new ArrayList<BreakDetail>(COMMIT_SIZE);

	/**
	 * 批量保存违规记录
	 * 2013-10-09 刘晓修改这个方法，添加了MED_TYPE_ID这个字段的保存，原来的方法没有保存这个字段
	 * @param curList
	 *            违规记录集合
	 * @throws Exception
	 */
	public void updateBatch(List<BreakDetail> saveList) {

		SqlParameterSource[] batchParams = SqlParameterSourceUtils.createBatch(saveList.toArray());

		// SQL语句
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO CPA_BREAK_DETAIL ");
		sql.append("(REC_ID, BREAK_TYPE, RULE_ID, SCENARIO_ID, BATCH_CODE, CLM_ID, ");
		sql.append(" DATA_SRC_ID, BREAK_DETAIL, CRT_USER_ID, PRE_ID, ERRDATA_TYPE,");
		sql.append(" CRT_DTTM, MED_TYPE_ID, LOG_ID, ERR_ITEM_NAME, ERR_ELEM_IDS, ERR_CATEGORY_IDS) ");
		sql.append("VALUES (");
		sql.append(":rec_id, :break_type, :rule_id, :scenario_id, :batch_code, :clm_id, ");
		sql.append(":data_src_id, :break_detail, :crt_user_id,:pre_id,:errData_type,");
		sql.append("sysdate, :med_type_id, :log_id, :err_item_name, :err_elem_ids, :err_category_ids");
		sql.append(")");

		getNamedJdbcTemplate().batchUpdate(sql.toString(), batchParams);
	}

	/**
	 * 批量提交主表的违规记录
	 * @param mstList
	 */
	private void saveMasterBatch(List<BreakMaster> mstList){
		
		SqlParameterSource[] batchParams = SqlParameterSourceUtils.createBatch(mstList.toArray());
		// SQL语句
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO CPA_BREAK ");
		sql.append("(REC_ID, SYS_ID, SEND_TYPE, BREAK_TYPE, RULE_ID, SCENARIO_ID, LOG_ID, ERR_MSG, ");
		sql.append(" MO_OBJ_TYPE, MO_OBJ_NAME, MO_OBJ_VAL, LGCY_ORG_CODE, REF_TOT, CYC_START, CYC_END,");
		sql.append(" CRT_USER_ID, CRT_DTTM, APL_STATE, UPD_TIME, UPD_UID, K_ID) ");
		sql.append("VALUES (");
		sql.append(":recId, :sysId, :sendType, :breakType, :ruleId, :scenarioId, :logId, :errMsg, ");
		sql.append(":objType, :objName, :objVal, :lgcyOrgCode, :refTot, :cycStart, :cycEnd, ");
		sql.append(":crtUserId, sysdate, :aplState, sysdate, :crtUserId, :kId");
		sql.append(")");
		
		// 执行批量插入
		getNamedJdbcTemplate().batchUpdate(sql.toString(), batchParams);
		
	}
	
	private void saveItemBatch(List<BreakDetailItem> saveList){
		
		SqlParameterSource[] batchParams = SqlParameterSourceUtils.createBatch(saveList.toArray());
		// SQL语句
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO CPA_BREAK_DETAIL_ITEM ");
		sql.append("(REC_ID, ERR_ELEM_ID, PRE_ID, CRT_DTTM) ");
		sql.append("VALUES (");
		sql.append(":rec_id, :err_item_id, :pre_id, sysdate)");

		getNamedJdbcTemplate().batchUpdate(sql.toString(), batchParams);
		
	}
	
	//整理互斥条件项目
	private List<BreakDetailItem> cvtItem(List<BreakDetail> detailList){
		List<BreakDetailItem> rtn = new ArrayList<BreakDetailItem>();
		if(detailList == null) return rtn;// 如果无数据直接返回
		for(BreakDetail detail : detailList){
			if(null != detail.getErritem()){
				for(String s :detail.getErritem()){
					BreakDetailItem  item = new BreakDetailItem();
					String[] ss =s.split(",");
					if(ss.length != 2){
						continue;
					}
					item.setRec_id(detail.getRec_id());
					item.setErr_item_id(Long.parseLong(ss[0]));
					item.setPre_id(Long.parseLong(ss[1]));
					rtn.add(item);
				}
			}
		}
		return rtn;
	}
	/**
	 * 转化子表数据为头表数据
	 * @param detailList
	 * @return
	 */
	private List<BreakMaster> cvtMaster(List<BreakDetail> detailList){
		
		List<BreakMaster> rtn = new ArrayList<BreakMaster>();
		if(detailList == null) return rtn;// 如果无数据直接返回
		
		for(BreakDetail detail : detailList){
			BreakMaster  master = new BreakMaster();
			master.setRecId(detail.getRec_id());
			master.setSysId(Constant.CPA_SYS_ID);
			master.setSendType("1");//单一下发
			master.setBreakType(detail.getBreak_type());
			master.setRuleId(detail.getRule_id());
			master.setScenarioId(detail.getScenario_id());
			master.setLogId(detail.getLog_id());
			master.setErrMsg(detail.getBreak_detail());
			master.setObjType("1");//监控类型为患者
			master.setObjName(detail.getP_name());
			master.setObjVal(detail.getP_id());
			master.setRefTot(detail.getPay_tot());
			master.setLgcyOrgCode(detail.getLgcy_org_code());
			master.setCycStart(CPADateUtil.date2String(detail.getClm_Dt(),"yyyyMMdd"));
			master.setCycEnd(CPADateUtil.date2String(detail.getClm_Dt(),"yyyyMMdd"));
			master.setAplState(Constant.APPEAL_STATE_FOUL);// 申诉状态为违规
			master.setCrtUserId(String.valueOf(detail.getCrt_user_id()));
			master.setkId(detail.getK_id());
			
			// 新构造的对象加入返回结果
			rtn.add(master);
		}
		
		return rtn;
	}
	
	/**
	 * 保存CPA违规记录
	 * 
	 * @param entity 违规记录实体
	 * @throws Exception
	 */
	public void saveBreakDetail(BreakDetail entity) throws Exception {

		entity.setRec_id(getSeqNext("CPA_BREAK_RECID_SEQ"));
		curList.add(entity);// 添加违规记录
		
		// 当达到批量提交规定数目时，提交保存
		if (curList.size() == COMMIT_SIZE) {
			//保存明细项
			updateBatch(curList);
			//子表数据转化为头表数据，并保存
			saveMasterBatch(cvtMaster(curList));
			//存储互斥条件项目
			saveItemBatch(cvtItem(curList));
			curList.clear();// 清空已保存数据
		}

	}

	/**
	 * 最后提交，保证最后一次数据能够提交保存到数据库
	 * 
	 * @throws Exception
	 */
	public void lastCommit() {

		if(this.curList.size()>0){
			updateBatch(curList);
			// 子表数据转化为头表数据，并保存
			saveMasterBatch(cvtMaster(curList));
			//存储互斥条件项目
			saveItemBatch(cvtItem(curList));
			curList.clear();// 清空已保存数据
		}
	}

	// public static void
	public static void main(String[] args) throws Exception {

		BreakDetailMgr bk = new BreakDetailMgr();
		BreakDetail entity = new BreakDetail();
		entity.setBreak_type("1");
		entity.setRule_id(99);
		entity.setScenario_id(96);
		entity.setBatch_code("BAT2013-08-09-1");
		entity.setClm_id(95);
		entity.setBreak_detail("我是谁");
		entity.setData_src_id(44);
		entity.setCrt_user_id(-1);
		entity.setErrData_type(1);
		entity.setPre_id(77);
		entity.setClm_Dt(new Date());
		entity.setLgcy_org_code("ak47");
		entity.setPay_tot(33.44D);
		entity.setP_id("1234");
		entity.setP_name("我");
		entity.setErr_item_name("item1");
		bk.saveBreakDetail(entity);
//		
//		bk.saveBreakDetail(entity);
//		
		bk.lastCommit();
//
		System.out.println("ok end");
	}
}
