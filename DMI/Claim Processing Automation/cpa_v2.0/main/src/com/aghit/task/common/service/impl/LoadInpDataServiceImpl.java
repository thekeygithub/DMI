package com.aghit.task.common.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.aghit.base.BaseService;
import com.aghit.task.common.service.LoadMidDataService;
import com.aghit.task.util.Constant;
import com.aghit.task.util.ConvertUtil;

@Service("loadInpDataService")
public class LoadInpDataServiceImpl extends BaseService implements LoadMidDataService{
	
	//执行类型1定时2批量3即时
	private String exeType = "";
	
	@Override
	public Map<String, Collection<Map<String, Object>>> executeFetchMidData(
			Date sdt, Date edt, long sseq, long eseq, long src, String exeType ) throws Exception {

		this.exeType = exeType;
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		Map<String, Collection<Map<String, Object>>>  result = new HashMap<String, Collection<Map<String,Object>>>();
		
		// 1.首先处理结算单数据，结算单ID暂存临时表
		Map<String,Map<String,Object>> clmDt = loadClmDdata(sdt, edt, sseq, eseq, src);
		
		// 2.返回结果中加入结算单CLM数据
		result.put(Constant.MID_DATA_TYPE_CLM, assembleClmData(clmDt));
		
		// 3.返回结果中加入医嘱ORD数据
		result.put(Constant.MID_DATA_TYPE_ORD, assembleOrdData(clmDt));	
		
		// 4.返回结果中加入处方PRE数据
		result.put(Constant.MID_DATA_TYPE_PRE, assemblePreData(clmDt));	
		
		log.debug("全部住院数据处理消耗时间："+ (System.currentTimeMillis()- st)); 
		return result;
	}
	
	/**
	 * 加载住院MID结算单的数据
	 * 
	 * @param sdt 开始日期
	 * @param edt 结束日期
	 * @param sseq 患者开始序号
	 * @param eseq 患者结束序号
	 * @return
	 * @throws Exception
	 */
	private Map<String,Map<String,Object>> loadClmDdata(Date sdt, Date edt, long sseq, long eseq, long src) throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO CPA_TP_CLMID(CLM_ID, DATA_SRC_ID, CLM_DATE, LGCY_P_CODE, P_DOB, ");
		sql.append(" LGCY_ORG_CODE, MI_HOSP_TIER_ID, MI_SCHM_ID, P_GEND_ID, PAY_TOT, SEQID, P_NAME, ");
		sql.append(" SIGN ,ENTR_DATE ,DISCH_DATE, HOSP_ORG_TYPE_ID, CLE_FORM_ID, P_MI_CAT_ID) ");
		sql.append("SELECT fy.CLM_ID, fy.data_src_id, fy.CLM_DATE, fy.LGCY_P_CODE, fy.P_DOB, ");
		sql.append("       fy.LGCY_ORG_CODE, fy.MI_HOSP_TIER_ID, fy.MI_SCHM_ID, fy.P_GEND_ID, fy.PAY_TOT, p.SEQID, fy.P_NAME ");
		sql.append("       ,CASE WHEN fy.clm_id = p.clm_id OR p.clm_id IS NULL THEN 1 ELSE 0 END sign, fy.ENTR_DATE ,fy.DISCH_DATE ");
		sql.append("       ,h.HOSP_ORG_TYPE_ID, fy.CLE_FORM_ID, fy.P_MI_CAT_ID ");
		sql.append("  FROM MID_INP_CLM fy ");
		sql.append("  INNER JOIN CPA_Handled_Patient p ON p.patient_id_number = fy.LGCY_P_CODE");
		sql.append("  LEFT JOIN AG_HOSP h ON fy.org_id = h.hosp_id ");
		sql.append(" WHERE  ");
	    sql.append("   fy.CLM_DATE >= ? ");
		sql.append("   AND fy.CLM_DATE <= ? ");
	    sql.append("   AND p.seqid >= ? ");
	    sql.append("   AND p.seqid <= ? ");
	    if (false == Constant.EXE_TYPE_REALTIME.equals(this.exeType)){ //即时执行不判断mts_flag
			sql.append(" AND fy.MTS_FLAG = '1' ");
			sql.append(" AND NOT EXISTS (SELECT 1 FROM mid_inp_pre WHERE mts_flag <> '1' AND clm_id =fy.clm_id AND data_src_id =fy.data_src_id) ");
		    sql.append(" AND NOT EXISTS (SELECT 1 FROM mid_inp_ord WHERE mts_flag <> '1' AND clm_id =fy.clm_id AND data_src_id =fy.data_src_id) ");
	    }
        sql.append(" AND fy.data_src_id = ? ");
	    
	    // 执行插入事务级临时表
	    getJdbcService().update(sql.toString(), new Object[]{sdt,edt,sseq,eseq,src});
	    
	    // 查询临时表的数据
	    String findSql = "SELECT * FROM CPA_TP_CLMID";
	    List<Map<String,Object>> res = getJdbcService().findForList(findSql, new Object[]{});   
	       
	    log.debug("加载住院Mid结算单消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + res.size());   
	    
	    return ConvertUtil.cvtKeyMap(res, new String[]{"DATA_SRC_ID","CLM_ID"});
	}
	
	/**
	 * 加载住院MTS结算单数据
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> loadMtsClmDdata() throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		StringBuilder sql = new StringBuilder();	    
	    sql.append("SELECT d.CLM_ID, d.DATA_SRC_ID, d.DIAG_ID,  ");
		sql.append("	   d.DIAG_CODE, d.DIAG_NAME, d.ID ");
		sql.append("  FROM MTS_INP_CLM_DIAG d, CPA_TP_CLMID tp ");
		sql.append(" WHERE tp.CLM_ID = d.CLM_ID ");
	    sql.append("   AND d.DATA_SRC_ID = tp.DATA_SRC_ID ");
	    sql.append("   AND d.DIAG_IO_TYPE = 1 ");// 查询出院诊断
	    sql.append(" ORDER BY d.CLM_ID ");
	       
	    List<Map<String,Object>> res = getJdbcService().findForList(sql.toString(), new Object[]{});   
	       
	    log.debug("加载住院MTS结算单消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + res.size());   
	    
	    return res;
	}
	
	/**
	 * 加载住院处方数据
	 * @param clmDt 结算单数据
	 * @return
	 * @throws Exception
	 */
	private Collection<Map<String,Object>> assemblePreData(Map<String,Map<String,Object>> clmDt) throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		Map<String,Map<String,Object>> detail = ConvertUtil.cvtKeyMap(groupPreMtsWithMid().values(),
				new String[]{"DATA_SRC_ID", "CLM_ID", "PRE_ID"});
		Iterator<Entry<String, Map<String,Object>>> it = detail.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Map<String,Object>> row = it.next();
			String key = row.getKey().substring(0,row.getKey().lastIndexOf("_"));
			// 以把clm表的字段补充到mts结果中
			row.getValue().put("CLM_DATE", clmDt.get(key).get("CLM_DATE"));
			row.getValue().put("LGCY_P_CODE", clmDt.get(key).get("LGCY_P_CODE"));
			row.getValue().put("P_DOB", clmDt.get(key).get("P_DOB"));
			row.getValue().put("LGCY_ORG_CODE", clmDt.get(key).get("LGCY_ORG_CODE"));
			row.getValue().put("MI_HOSP_TIER_ID", clmDt.get(key).get("MI_HOSP_TIER_ID"));
			row.getValue().put("MI_SCHM_ID", clmDt.get(key).get("MI_SCHM_ID"));
			row.getValue().put("P_GEND_ID", clmDt.get(key).get("P_GEND_ID"));
			row.getValue().put("SEQID", clmDt.get(key).get("SEQID"));
			row.getValue().put("PAY_TOT", clmDt.get(key).get("PAY_TOT"));
			row.getValue().put("P_NAME", clmDt.get(key).get("P_NAME"));
			row.getValue().put("SIGN", clmDt.get(key).get("SIGN"));
			row.getValue().put("ENTR_DATE", clmDt.get(key).get("ENTR_DATE"));
			row.getValue().put("DISCH_DATE", clmDt.get(key).get("DISCH_DATE"));
			row.getValue().put("HOSP_ORG_TYPE_ID", clmDt.get(key).get("HOSP_ORG_TYPE_ID"));
			row.getValue().put("CLE_FORM_ID", clmDt.get(key).get("CLE_FORM_ID"));
			row.getValue().put("P_MI_CAT_ID", clmDt.get(key).get("P_MI_CAT_ID"));
		}
		
		log.debug("全部住院处方处理消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + detail.size());
		return detail.values();
	}
	
	/**
	 * 加载住院医嘱数据
	 * @param clmDt 结算单数据
	 * @return
	 * @throws Exception
	 */
	private Collection<Map<String,Object>> assembleOrdData(Map<String,Map<String,Object>> clmDt) throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		Map<String,Map<String,Object>> detail = ConvertUtil.cvtKeyMap(groupOrdMtsWithMid().values(),
				new String[]{"DATA_SRC_ID", "CLM_ID", "ORD_ID"});
		Iterator<Entry<String, Map<String,Object>>> it = detail.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Map<String,Object>> row = it.next();
			String key = row.getKey().substring(0,row.getKey().lastIndexOf("_"));
			// 以把clm表的字段补充到mts结果中
			row.getValue().put("CLM_DATE", clmDt.get(key).get("CLM_DATE"));
			row.getValue().put("LGCY_P_CODE", clmDt.get(key).get("LGCY_P_CODE"));
			row.getValue().put("P_DOB", clmDt.get(key).get("P_DOB"));
			row.getValue().put("LGCY_ORG_CODE", clmDt.get(key).get("LGCY_ORG_CODE"));
			row.getValue().put("MI_HOSP_TIER_ID", clmDt.get(key).get("MI_HOSP_TIER_ID"));
			row.getValue().put("MI_SCHM_ID", clmDt.get(key).get("MI_SCHM_ID"));
			row.getValue().put("P_GEND_ID", clmDt.get(key).get("P_GEND_ID"));
			row.getValue().put("SEQID", clmDt.get(key).get("SEQID"));
			row.getValue().put("PAY_TOT", clmDt.get(key).get("PAY_TOT"));
			row.getValue().put("P_NAME", clmDt.get(key).get("P_NAME"));
			row.getValue().put("SIGN", clmDt.get(key).get("SIGN"));
			row.getValue().put("ENTR_DATE", clmDt.get(key).get("ENTR_DATE"));
			row.getValue().put("DISCH_DATE", clmDt.get(key).get("DISCH_DATE"));
			row.getValue().put("HOSP_ORG_TYPE_ID", clmDt.get(key).get("HOSP_ORG_TYPE_ID"));
			row.getValue().put("CLE_FORM_ID", clmDt.get(key).get("CLE_FORM_ID"));
			row.getValue().put("P_MI_CAT_ID", clmDt.get(key).get("P_MI_CAT_ID"));
		}
		
		log.debug("全部住院医嘱处理消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + detail.size());
		return detail.values();
	}
	
	/**
	 * 合并住院MTS和MID处方表中的数据
	 * @return
	 * @throws Exception
	 */
	private Map<String,Map<String,Object>> groupPreMtsWithMid() throws Exception{
		
		Map<String,Map<String,Object>> mts = loadMtsPreData();
		Map<String,Map<String,Object>> mid = loadMidPreData();
		Map<String,Map<String,Map<String,Object>>> total = loadTotalPreData();
		
		Iterator<Entry<String, Map<String,Object>>> it = mts.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Map<String,Object>> row = it.next();
			// 以下把mid表的字段补充到mts结果中
			Map<String,Object> midval = mid.get(row.getKey());
			if(midval != null){
				row.getValue().put("DR_LVL_DETAIL", midval.get("DR_LVL"));
				row.getValue().put("DRUG_ADM_APPRO", midval.get("DRUG_ADM_APPRO"));
				row.getValue().put("DRUG_DAYS", midval.get("DRUG_DAYS"));
				row.getValue().put("DRUG_FEE", midval.get("DRUG_FEE"));
				row.getValue().put("ORD_EXE_DATE", midval.get("ORD_EXE_DATE"));
				row.getValue().put("A_MI_FLAG", midval.get("A_MI_FLAG"));
				row.getValue().put("DRUG_UNIT_PRICE", midval.get("DRUG_UNIT_PRICE"));
				row.getValue().put("LGCY_PRE_CODE", midval.get("LGCY_PRE_CODE"));
				row.getValue().put("PRE_SEQNO", midval.get("PRE_SEQNO"));
				row.getValue().put("ROWNUM", midval.get("ROWNUM"));
				row.getValue().put("CHK_FLAG", midval.get("CHK_FLAG"));
				
			}else{
				it.remove();
				log.error("数据错误：MTS pre_id:"+ row.getValue().get("PRE_ID") + "不存在MID。");
			}
			if (total != null){
				//为明细内容补充合计类值
				Map<String,Map<String,Object>> totalval = total.get(row.getKey());
				if (null != totalval){
					Map<String,Object> mapTotal = new HashMap<String,Object>();
					Iterator<Entry<String, Map<String,Object>>> it1 = totalval.entrySet().iterator();
					while(it1.hasNext()){
						Entry<String, Map<String,Object>> row1 = it1.next();
						// 把total的合计信息添加到mts结果中
						Map<String,Object> val = totalval.get(row1.getKey());
						mapTotal.put(row1.getKey() + "_PRECLMFEE",val.get("PRECLMFEE"));
						mapTotal.put(row1.getKey() + "_PREDAYFEE",val.get("PREDAYFEE"));
						mapTotal.put(row1.getKey() + "_PRECLMCNT",val.get("PRECLMCNT"));
						mapTotal.put(row1.getKey() + "_PREDAYCNT",val.get("PREDAYCNT"));
						mapTotal.put(row1.getKey() + "_PRECNTDAYS",ConvertUtil.getDouble(val.get("PRECLMCNT")) - ConvertUtil.getDouble(val.get("DAYS")));
					}
					row.getValue().put("TOTAL", mapTotal);
				}
				
			}else{
				row.getValue().put("TOTAL", null);
				log.info("MTS pre_id:"+ row.getValue().get("PRE_ID") + "不存在对应的合计值信息。");
			}
			
		}
		
		return mts;
	}
	
	/**
	 * 合并住院MTS和MID医嘱表中的数据
	 * @return
	 * @throws Exception
	 */
	private Map<String,Map<String,Object>> groupOrdMtsWithMid() throws Exception{
		
		Map<String,Map<String,Object>> mts = loadMtsOrdData();
		Map<String,Map<String,Object>> mid = loadMidOrdData();
		Map<String,Map<String,Map<String,Object>>> total = loadTotalOrdData();
		
		Iterator<Entry<String, Map<String,Object>>> it = mts.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Map<String,Object>> row = it.next();
			// 把mid表的DR_LVL字段补充到mts结果中
			Map<String,Object> midval = mid.get(row.getKey());
			if(midval != null){
				row.getValue().put("DR_LVL_DETAIL", midval.get("DR_LVL"));
				row.getValue().put("ORD_FEE", midval.get("ORD_FEE"));
				row.getValue().put("ORD_EXE_DATE", midval.get("ORD_EXE_DATE"));
				row.getValue().put("A_MI_FLAG", midval.get("A_MI_FLAG"));
				row.getValue().put("ORD_UNIT_PRICE", midval.get("ORD_UNIT_PRICE"));
				row.getValue().put("ROWNUM", midval.get("ROWNUM"));
				row.getValue().put("CHK_FLAG", midval.get("CHK_FLAG"));

			}else{
				it.remove();
				log.error("数据错误：MTS ord_id:"+ row.getValue().get("ORD_ID") + "不存在MID。");
			}
			if (total != null){
				//为明细内容补充合计类值
				Map<String,Map<String,Object>> totalval = total.get(row.getKey());
				if (null != totalval){
					Map<String,Object> mapTotal = new HashMap<String,Object>();
					Iterator<Entry<String, Map<String,Object>>> it1 = totalval.entrySet().iterator();
					while(it1.hasNext()){
						Entry<String, Map<String,Object>> row1 = it1.next();
						// 把total的合计信息添加到mts结果中
						Map<String,Object> val = totalval.get(row1.getKey());
						mapTotal.put(row1.getKey() + "_ORDCLMFEE",val.get("ORDCLMFEE"));
						mapTotal.put(row1.getKey() + "_ORDDAYFEE",val.get("ORDDAYFEE"));
						mapTotal.put(row1.getKey() + "_ORDCLMCNT",val.get("ORDCLMCNT"));
						mapTotal.put(row1.getKey() + "_ORDDAYCNT",val.get("ORDDAYCNT"));
						mapTotal.put(row1.getKey() + "_ORDCNTDAYS",ConvertUtil.getDouble(val.get("ORDCLMCNT")) - ConvertUtil.getDouble(val.get("DAYS")));
					}
					row.getValue().put("TOTAL", mapTotal);
				}
			}else{
				row.getValue().put("TOTAL", null);
				log.info("MTS ord_id:"+ row.getValue().get("ORD_ID") + "不存在对应的合计值信息。");
			}
		}
		
		return mts;
	}
	
	/**
	 * 加载住院结算单数据，合并MTS和MID结算单表中的数据
	 * 诊断信息key：DIAG_LIST，value:集合
	 * @return
	 * @throws Exception
	 */
	private Collection<Map<String,Object>> assembleClmData(Map<String,Map<String,Object>> clmMidDt) throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		Map<String,Map<String,Object>> rtn = new HashMap<String, Map<String,Object>>();
		List<Map<String,Object>> mts = loadMtsClmDdata();
		Iterator<Map<String,Object>> it = mts.iterator();
		// 遍历所有的诊断记录，同时构造
		while(it.hasNext()){
			Map<String,Object> row = it.next();
			
			// 组合key: _数据来源_结算单ID
			String key = "_" + row.get("DATA_SRC_ID") + "_" + row.get("CLM_ID");
			Map<String,Object> clm_row = rtn.get(key);
			if(clm_row == null){
				clm_row = new HashMap<String, Object>();
				// 以下把mid表的字段补充到mts结果中
				clm_row.put("DATA_SRC_ID", row.get("DATA_SRC_ID"));
				clm_row.put("CLM_ID", row.get("CLM_ID"));
				clm_row.put("CLM_DATE", clmMidDt.get(key).get("CLM_DATE"));
				clm_row.put("LGCY_P_CODE", clmMidDt.get(key).get("LGCY_P_CODE"));
				clm_row.put("P_DOB", clmMidDt.get(key).get("P_DOB"));
				clm_row.put("LGCY_ORG_CODE", clmMidDt.get(key).get("LGCY_ORG_CODE"));
				clm_row.put("MI_HOSP_TIER_ID", clmMidDt.get(key).get("MI_HOSP_TIER_ID"));
				clm_row.put("MI_SCHM_ID", clmMidDt.get(key).get("MI_SCHM_ID"));
				clm_row.put("P_GEND_ID", clmMidDt.get(key).get("P_GEND_ID"));
				clm_row.put("SEQID", clmMidDt.get(key).get("SEQID"));
				clm_row.put("PAY_TOT", clmMidDt.get(key).get("PAY_TOT"));
				clm_row.put("P_NAME", clmMidDt.get(key).get("P_NAME"));
				clm_row.put("SIGN", clmMidDt.get(key).get("SIGN"));
				clm_row.put("ENTR_DATE", clmMidDt.get(key).get("ENTR_DATE"));
				clm_row.put("DISCH_DATE", clmMidDt.get(key).get("DISCH_DATE"));
				clm_row.put("HOSP_ORG_TYPE_ID", clmMidDt.get(key).get("HOSP_ORG_TYPE_ID"));
				clm_row.put("CLE_FORM_ID", clmMidDt.get(key).get("CLE_FORM_ID"));
				clm_row.put("P_MI_CAT_ID", clmMidDt.get(key).get("P_MI_CAT_ID"));
				
				// 构造一个固定的key：DIAG_LIST，value是个集合，用于保存所有的诊断
				clm_row.put("DIAG_LIST", new ArrayList<Map<String,Object>>());
				
				// 构造的新记录，加入到返回的Map中
				rtn.put(key, clm_row);
			}
			((List<Map<String,Object>>)clm_row.get("DIAG_LIST")).add(row);
			
		}
		log.debug("全部住院诊断处理消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + mts.size());
		
		return rtn.values();
	}
	
	/**
	 * 加载住院MID_ORD表中数据
	 * @return
	 * @throws Exception
	 */
	private Map<String,Map<String,Object>> loadMidOrdData() throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ord.DATA_SRC_ID, ord.ORD_ID, ord.DR_LVL, ord.ORD_FEE, ord.ORD_EXE_DATE, ");
		sql.append("  ord.A_MI_FLAG, ord.ORD_UNIT_PRICE, ROWNUM, ord.CHK_FLAG ");
		sql.append("  FROM MID_INP_ORD ord, CPA_TP_CLMID tp ");
		sql.append(" WHERE tp.CLM_ID = ord.CLM_ID");
	    sql.append("   AND ord.DATA_SRC_ID = tp.DATA_SRC_ID");
	    if (Constant.EXE_TYPE_REALTIME.equals(this.exeType)){
	    	sql.append("   AND ord.DQ_FLAG = '1' ");
	    }else{
	    	sql.append("   AND ord.MTS_FLAG = '1' ");
	    }
	    
	       
	    List<Map<String,Object>> res = getJdbcService().findForList(sql.toString(), new Object[]{});   
	       
	    log.debug("加载住院MID医嘱消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + res.size());   
	    
	    return ConvertUtil.cvtKeyMap(res, new String[]{"DATA_SRC_ID","ORD_ID"});
	}
	
	/**
	 * 加载住院MTS_ORD表中数据
	 * @return
	 * @throws Exception
	 */
	private Map<String,Map<String,Object>> loadMtsOrdData() throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ord.CLM_ID,ord.DATA_SRC_ID,ord.AG_ID,ord.ORD_TYPE_ID,ord.LGCY_CHRG_NAME, ");
		sql.append("       ord.ORD_ID,ord.ORD_CNT DRUG_AMT, ord.CSUM_TREE_NAME_ID ");
		sql.append("  FROM MTS_INP_ORD ord, CPA_TP_CLMID tp ");
		sql.append(" WHERE tp.CLM_ID = ord.CLM_ID");
	    sql.append("   AND ord.DATA_SRC_ID = tp.DATA_SRC_ID");
	    sql.append("   AND ord.AG_ID > 0 ");
	    
	    List<Map<String,Object>> res = getJdbcService().findForList(sql.toString(), new Object[]{});   
	       
	    log.debug("加载住院MTS医嘱消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + res.size());   
	    
	    return ConvertUtil.cvtKeyMap(res, new String[]{"DATA_SRC_ID","ORD_ID"});
	  
	}
	
	//<data_src_id_ord_id,map<k_id_elem_id,map<String,Object>>
	//每个明细对应多种总额（频次）对应值
	private Map<String,Map<String,Map<String,Object>>> loadTotalOrdData() throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		//执行查询结果
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DEF.K_ID,ELEM.ELEM_ID,MID.DATA_SRC_ID,MID.ORD_ID,SUM(MID.ORD_CNT)OVER(PARTITION BY MTS.CLM_ID,DEF.K_ID,ELEM.ELEM_ID ORDER BY MID.ORD_EXE_DATE,ROWNUM) ORDCLMCNT,");
		sql.append(" SUM(MID.ORD_CNT)OVER(PARTITION BY MTS.CLM_ID,DEF.K_ID,ELEM.ELEM_ID,TO_CHAR(ORD_EXE_DATE,'YYYY-MM-DD') ORDER BY MID.ORD_EXE_DATE,ROWNUM) ORDDAYCNT,");		
		sql.append(" SUM(MID.ORD_FEE)OVER(PARTITION BY MTS.CLM_ID,DEF.K_ID,ELEM.ELEM_ID ORDER BY MID.ORD_EXE_DATE,ROWNUM) ORDCLMFEE,");	
		sql.append(" SUM(MID.ORD_FEE)OVER(PARTITION BY MTS.CLM_ID,DEF.K_ID,ELEM.ELEM_ID,TO_CHAR(ORD_EXE_DATE,'YYYY-MM-DD') ORDER BY MID.ORD_EXE_DATE,ROWNUM) ORDDAYFEE,");
		sql.append(" TRUNC(CLM.DISCH_DATE) - TRUNC(CLM.ENTR_DATE) DAYS");
		sql.append(" FROM K_DEF DEF");		
		sql.append(" INNER JOIN K_OBJ OBJ ON DEF.K_ID = OBJ.K_ID");		
		sql.append(" INNER JOIN MTS_INP_ORD MTS ON OBJ.OBJ_ID = MTS.AG_ID");		
		sql.append(" INNER JOIN MID_INP_ORD MID ON MTS.ORD_ID = MID.ORD_ID AND MTS.DATA_SRC_ID = MID.DATA_SRC_ID");	
		sql.append(" INNER JOIN MID_INP_CLM CLM ON CLM.CLM_ID = MTS.CLM_ID AND CLM.DATA_SRC_ID = MTS.DATA_SRC_ID");
		sql.append(" INNER JOIN K_ELEM_DEF ELEM ON DEF.K_ID = ELEM.K_ID AND ELEM_TYPE_ID IN ('301','302','311')");		
		sql.append(" INNER JOIN CPA_TP_CLMID TP ON MTS.CLM_ID = TP.CLM_ID AND MTS.DATA_SRC_ID = TP.DATA_SRC_ID")	;	
		sql.append(" WHERE DEF.OBJ_ELEM_TYPE_ID IN (4,5,6,102,104)  AND MTS.AG_ID > 0 ");
		sql.append(" AND DEF.STOP_SIGN = 0 AND DEF.STD_FLAG = 1");

		
		List<Map<String,Object>> res = getJdbcService().findForList(sql.toString(), new Object[]{}); 
		
		log.debug("加载住院合计类属性医嘱消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + res.size());  
		//获得data_src_id + ord_id 对应map集合
		Map<String,Collection<Map<String,Object>>> tmp = ConvertUtil.cvtKeyCMap(res, new String[]{"DATA_SRC_ID","ORD_ID"});
		//获得data_src_id + ord_id，K_id+Elem_id 对应 map集合
		Map<String,Map<String,Map<String,Object>>> result = new HashMap<String,Map<String,Map<String,Object>>>();
		if (null != tmp){
			Iterator<Entry<String, Collection<Map<String,Object>>>> it = tmp.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, Collection<Map<String,Object>>> row = it.next();
				result.put(row.getKey(), ConvertUtil.cvtKeyMap(row.getValue(), new String[]{"K_ID","ELEM_ID"}));
			}
		}
		//返回结果
		return result;
	}

	
	/**
	 * 加载住院MID_PRE表中数据
	 * @return
	 * @throws Exception
	 */
	private Map<String,Map<String,Object>> loadMidPreData() throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pre.DATA_SRC_ID, pre.PRE_ID, pre.DR_LVL, pre.DRUG_ADM_APPRO, pre.DRUG_DAYS, pre.DRUG_FEE, pre.ORD_EXE_DATE, ");
		sql.append("  pre.A_MI_FLAG, pre.DRUG_UNIT_PRICE, pre.LGCY_PRE_CODE, pre.PRE_SEQNO, ROWNUM, pre.CHK_FLAG ");
		sql.append("  FROM MID_INP_PRE pre, CPA_TP_CLMID tp ");
		sql.append(" WHERE tp.CLM_ID = pre.CLM_ID");
	    sql.append("   AND pre.DATA_SRC_ID = tp.DATA_SRC_ID");
	    if (Constant.EXE_TYPE_REALTIME.equals(this.exeType)){
	    	sql.append("   AND pre.DQ_FLAG = '1' ");
	    }else{
	    	sql.append("   AND pre.MTS_FLAG = '1' ");
	    }
	    
	       
	    List<Map<String,Object>> res = getJdbcService().findForList(sql.toString(), new Object[]{});   
	       
	    log.debug("加载住院MID处方消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + res.size());   
	    
	    return ConvertUtil.cvtKeyMap(res, new String[]{"DATA_SRC_ID", "PRE_ID"});
	}
	
	/**
	 * 加载住院MTS_PRE表中数据
	 * @return
	 * @throws Exception
	 */
	private Map<String,Map<String,Object>> loadMtsPreData() throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pre.CLM_ID, pre.DATA_SRC_ID, pre.DRUG_ID, pre.LGCY_DRUG_DESC, pre.DRUG_USG_UNIT, ");
		sql.append("       pre.DRUG_USG_VAL, pre.DRUG_USG_AMT, pre.PRE_ID, pre.DRUG_AMT, pre.ATC_TERM_TYPE_ID, ");
		sql.append("	   pre.ATC_NAME_ID, pre.DRUG_FORM_ID");
		sql.append("  FROM MTS_INP_PRE pre, CPA_TP_CLMID tp ");
		sql.append(" WHERE tp.CLM_ID = pre.CLM_ID");
	    sql.append("   AND pre.DATA_SRC_ID = tp.DATA_SRC_ID ");
	    sql.append("   AND pre.DRUG_ID >= 0 ");
	       
	    List<Map<String,Object>> res = getJdbcService().findForList(sql.toString(), new Object[]{});   
	       
	    log.debug("加载住院MTS处方消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + res.size());   
	    
	    return ConvertUtil.cvtKeyMap(res, new String[]{"DATA_SRC_ID", "PRE_ID"});
	  
	}
	
	//<data_src_id_ord_id,map<k_id_elem_id,map<String,Object>>
	//每个明细对应多种总额（频次）对应值
	private Map<String,Map<String,Map<String,Object>>> loadTotalPreData() throws Exception{
		
		long st = System.currentTimeMillis();	//记录执行开始时间
		//执行查询结果
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DEF.K_ID,ELEM_ID,MID.DATA_SRC_ID,MID.PRE_ID,SUM(MID.DRUG_AMT)OVER(PARTITION BY MTS.CLM_ID,DEF.K_ID,ELEM.ELEM_ID ORDER BY MID.ORD_EXE_DATE,ROWNUM) PRECLMCNT,");
		sql.append(" SUM(MID.DRUG_AMT)OVER(PARTITION BY MTS.CLM_ID,DEF.K_ID,ELEM.ELEM_ID,TO_CHAR(ORD_EXE_DATE,'YYYY-MM-DD') ORDER BY MID.ORD_EXE_DATE,ROWNUM) PREDAYCNT,");		
		sql.append(" SUM(MID.DRUG_FEE)OVER(PARTITION BY MTS.CLM_ID,DEF.K_ID,ELEM.ELEM_ID ORDER BY MID.ORD_EXE_DATE,ROWNUM) PRECLMFEE,");	
		sql.append(" SUM(MID.DRUG_FEE)OVER(PARTITION BY MTS.CLM_ID,DEF.K_ID,ELEM.ELEM_ID,TO_CHAR(ORD_EXE_DATE,'YYYY-MM-DD') ORDER BY MID.ORD_EXE_DATE,ROWNUM) PREDAYFEE,");		
		sql.append(" TRUNC(CLM.DISCH_DATE) - TRUNC(CLM.ENTR_DATE) DAYS");
		sql.append(" FROM K_DEF DEF");		
		sql.append(" INNER JOIN K_OBJ OBJ ON DEF.K_ID = OBJ.K_ID");		
		sql.append(" INNER JOIN MTS_INP_PRE MTS ON OBJ.OBJ_ID = MTS.DRUG_ID");		
		sql.append(" INNER JOIN MID_INP_PRE MID ON MTS.PRE_ID = MID.PRE_ID AND MTS.DATA_SRC_ID = MID.DATA_SRC_ID");	
		sql.append(" INNER JOIN MID_INP_CLM CLM ON CLM.CLM_ID = MTS.CLM_ID AND CLM.DATA_SRC_ID = MTS.DATA_SRC_ID");
		sql.append(" INNER JOIN K_ELEM_DEF ELEM ON DEF.K_ID = ELEM.K_ID AND ELEM_TYPE_ID IN ('301','302','311')");		
		sql.append(" INNER JOIN CPA_TP_CLMID TP ON MTS.CLM_ID = TP.CLM_ID AND MTS.DATA_SRC_ID = TP.DATA_SRC_ID")	;	
		sql.append(" WHERE DEF.OBJ_ELEM_TYPE_ID IN (1,2,101)  AND MTS.DRUG_ID > 0 ");
		sql.append(" AND DEF.STOP_SIGN = 0 AND DEF.STD_FLAG = 1");

		List<Map<String,Object>> res = getJdbcService().findForList(sql.toString(), new Object[]{}); 
		log.debug("加载住院合计类属性医嘱消耗时间："+ (System.currentTimeMillis()- st) + "，记录数：" + res.size());  
		//获得data_src_id + pre_id 对应map集合
		Map<String,Collection<Map<String,Object>>> tmp = ConvertUtil.cvtKeyCMap(res, new String[]{"DATA_SRC_ID","PRE_ID"});
		//获得data_src_id + pre_id，K_id+Elem_id 对应 map集合
		Map<String,Map<String,Map<String,Object>>> result = new HashMap<String,Map<String,Map<String,Object>>>();
		if (null != tmp){
			Iterator<Entry<String, Collection<Map<String,Object>>>> it = tmp.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, Collection<Map<String,Object>>> row = it.next();
				result.put(row.getKey(), ConvertUtil.cvtKeyMap(row.getValue(), new String[]{"K_ID","ELEM_ID"}));
			}
		}
		//返回结果
		return result;
	}
	

}
