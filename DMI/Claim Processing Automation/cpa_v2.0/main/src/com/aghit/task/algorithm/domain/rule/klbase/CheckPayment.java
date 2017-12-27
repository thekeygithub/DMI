package com.aghit.task.algorithm.domain.rule.klbase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.aghit.task.algorithm.domain.BreakDetail;
import com.aghit.task.algorithm.domain.ComparedEntity;
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.algorithm.domain.Payment;
import com.aghit.task.algorithm.domain.exp.klbase.KLElemExpression;
import com.aghit.task.common.entity.klbase.KlDef;
import com.aghit.task.common.entity.klbase.KlStat;
import com.aghit.task.util.Constant;
import com.aghit.task.util.ExpressionUtil;

/**
 * 审核项目审核抽象类
 */
public abstract class CheckPayment {
	
	private static Logger log = Logger.getLogger(CheckPayment.class);
	
	private int checkType; // 审核项目类型
	public long ruleId; // 规则ID
	public int cycleStart; // 周期开始日
	public int cycleEnd; // 周期结束日
	public int cycleFlg; // 是否周期
	//规则对应知识集
	private KlDef klDef;
	private List<KLElemExpression> exps;
	
	public CheckPayment(int checkType, long ruleId,
			int cycleStart, int cycleEnd, int cycleFlg,KlDef klDef,List<KLElemExpression> exps) {
		this.checkType = checkType;
		this.ruleId = ruleId;
		this.cycleStart = cycleStart;
		this.cycleEnd = cycleEnd;
		this.cycleFlg = cycleFlg;
		this.klDef= klDef;
		this.exps= exps;
	}
	
	public abstract List<BreakDetail> checkPayment(Collection<Payment> list, Patient p, Payment pay);
	 
	 /**
	 * 获取审核项目的ID
	 * @param record 审核项目
	 * @return
	 */
	protected long[] decideIdFrom(ComparedEntity entity){
		return new long[]{Long.valueOf(entity.getCode())};
	}
	
	protected ErrMessage getErrMessage(){
		//返回错误信息
		ErrMessage errMessage= new ErrMessage();
		//是否错误
		boolean bl = true;// 默认返回结果正确
		//错误元素ID
		Set<String> errElemSet = new TreeSet<String>();
		//错误分类
		Set<String> errCateSet = new TreeSet<String>();
	    //错误描述
	    StringBuffer err_des = new StringBuffer();
	    //明细
	    Set<String> errItem = new TreeSet<String>();
	    
	    //表达式算式
	    StringBuilder sbStat = new StringBuilder();
	    //元素表达式循环序号
	    int idxStat = 0;
		for (KlStat stat:klDef.getKlStats()){
			//表达式之间为 and 关系
			if(idxStat != 0) {
			    sbStat.append(" && ");
		    }
			idxStat++;
			//当存在情景时
			if (klDef.getScene_enable() == 1){
				//情景表达式
				String scene_state=stat.getScene_stat();
		        if (null !=scene_state && scene_state.length()>0){
		        	for(KLElemExpression ex:exps){
		        		scene_state=scene_state.replace("[" + ex.getElemDef().getElem_id() + "]", " " + String.valueOf(ex.isElem_state() + " "));
	        		}
		        	//当情景不满足时，不做判断
		        	if(false == (StringUtils.isBlank(scene_state) ? true : ExpressionUtil.calcLogicExp(scene_state))){
		        	    sbStat.append(" true ");
		        		continue;
		        	}
		        }
			}
			//如果条件表达式为其它
			if (Constant.COND_STAT_REL_OTHER == stat.getCond_stat_rel()){
				String con_state=stat.getCond_stat();
				Set<String> tmpErrElemset = new TreeSet<String>();
				Set<String> tmpErrCateset = new TreeSet<String>();
				Set<String> tmpErrItem = new TreeSet<String>();
		        if (null !=con_state && con_state.length()>0){
		        	for(int i=0;i<exps.size();i++){
		    			KLElemExpression ex = exps.get(i);
		        		con_state=con_state.replace("[" + ex.getElemDef().getElem_id() + "]", " " + String.valueOf(ex.isElem_state() + " "));
	        		    if(false==ex.isElem_state() && 1==ex.getElemDef().getCond_flag()){
	        		    	tmpErrElemset.add(String.valueOf(ex.getElemDef().getElem_id()));
	        		    	tmpErrCateset.add(String.valueOf(ex.getElemDef().getCategory_id()));
	        		    	//当存在违规明细时
	        		    	if (null != ex.getPreId() && ex.getPreId().size()>0){
	        		    		for(Long l:ex.getPreId()){
	        		    			if(l != -1l){
	        		    				tmpErrItem.add(ex.getElemDef().getElem_id() + "," + l);
	        		    			}
	        		    		}
	        		    	}
	        		    }
		        	}
		        	//当情景不满足时，不做判断
		        	if(false == (StringUtils.isBlank(con_state) ? true : ExpressionUtil.calcLogicExp(con_state))){
		        		sbStat.append(" false ");
		        		err_des.append(stat.getWarn_desc() + ",");
		        		errElemSet.addAll(tmpErrElemset);
		        		errCateSet.addAll(tmpErrCateset);
		        		errItem.addAll(tmpErrItem);
		        	}else{
		        		sbStat.append(" true ");
		        	}
		        }else{
		        	sbStat.append(" true ");
		        }
		        continue;
			}
			
			//当条件表达式为逗号分隔，与或关系时
		    String[] con = stat.getCond_stat().split(",");
		    //表达式条件项
		    StringBuilder sbCon = new StringBuilder();
		    //单一表达式循环序号
		    int idxCon = 0;
		    Set<String> tmpErrElemset = new TreeSet<String>();
		    Set<String> tmpErrCateset = new TreeSet<String>();
		    Set<String> tmpErrItem = new TreeSet<String>();
        	for (String c:con){
        		if(idxCon != 0) {
        			//与或
        			if (Constant.COND_STAT_REL_ADD ==stat.getCond_stat_rel()){
        				sbCon.append(" && ");
        			}else{
        				sbCon.append(" || ");
        			}
    		    }
        		Set<String> tmpErrElemSet1 = new TreeSet<String>();
        		Set<String> tmpErrCateset1 = new TreeSet<String>();
        		Set<String> tmpErrItem1 = new TreeSet<String>();
        		for(int i=0;i<exps.size();i++){
        			KLElemExpression ex = exps.get(i);
        			c=c.replace("[" + ex.getElemDef().getElem_id() + "]", " " + String.valueOf(ex.isElem_state() + " "));
	        	    if(false == ex.isElem_state() && 1 == ex.getElemDef().getCond_flag()){
	        	        tmpErrElemSet1.add(String.valueOf(ex.getElemDef().getElem_id()));
	        	        tmpErrCateset1.add(String.valueOf(ex.getElemDef().getCategory_id()));
	        	       //当存在违规明细时
        		    	if (null != ex.getPreId() && ex.getPreId().size()>0){
        		    		for(Long l:ex.getPreId()){
        		    			if(l != -1l){
        		    				tmpErrItem1.add(ex.getElemDef().getElem_id() + "," + l);
        		    			}
        		    		}
        		    	}
	        	    }
        		}
        		//表达式条件项是否成立
        		boolean b = StringUtils.isBlank(c) ? true : ExpressionUtil.calcLogicExp(c);
        		if (false == b){
        			tmpErrElemset.addAll(tmpErrElemSet1);
        			tmpErrCateset.addAll(tmpErrCateset1);
        			tmpErrItem.addAll(tmpErrItem1);
        		}
        		sbCon.append(String.valueOf(b));
        		idxCon++;
        	}		
        	
        	if(false == (StringUtils.isBlank(sbCon.toString()) ? true : ExpressionUtil.calcLogicExp(sbCon.toString()))){
        		sbStat.append(" false ");
        		err_des.append(stat.getWarn_desc() + ",");
        		errElemSet.addAll(tmpErrElemset);
        		errCateSet.addAll(tmpErrCateset);
        		errItem.addAll(tmpErrItem);
        	}else{
        		sbStat.append(" true ");
        	}
		
		}
		
		
		bl= StringUtils.isBlank(sbStat.toString()) ? true : ExpressionUtil.calcLogicExp(sbStat.toString());
        errMessage.setLegal(bl);
        if(err_des.length()>0){
        	errMessage.setErrDes(err_des.deleteCharAt(err_des.length()-1) + "。");
        }
        if(errElemSet.size()>0){
        	StringBuffer tmp = new StringBuffer();
        	for(String s:errElemSet){
        		tmp.append(s);
                tmp.append(",");
        	}
        	tmp.deleteCharAt(tmp.length()-1);
        	errMessage.setErrElemID(tmp.toString());
        }
        if(errCateSet.size()>0){
        	StringBuffer tmp = new StringBuffer();
        	for(String s:errCateSet){
        		tmp.append(s);
                tmp.append(",");
        	}
        	tmp.deleteCharAt(tmp.length()-1);
        	errMessage.setErrCateid(tmp.toString());
        }
        if(errItem.size()>0){
        	errMessage.setErr_item(errItem);
        }

		return errMessage;
	}
	
	/**
	 * 判断一个数组中是否有一个值，在另个参考数据组中
	 * @param factIds 比较IDs
	 * @param compArr 参考数组
	 * @return
	 */
	protected boolean isExeRule (long[] factIds, long[] compArr){
		
		// 防止代码异常错误，为空时直接跳出，只是记录
		if(factIds == null || compArr == null || factIds.length < 1 || compArr.length <1 ){
			log.warn("比较对象为空，或者参考对象为空");
			return false;
		}
		
		// 循环判断是否在参考数组中
		for(long id : factIds){
			// 如果找到，直接跳出
			if(Arrays.binarySearch(compArr, id) >= 0) return true;
		}
		
		return false;
	}
	
	public int getCheckType() {
		return checkType;
	}
	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}

	public List<KLElemExpression> getExps() {
		return exps;
	}
	public void setExps(List<KLElemExpression> exps) {
		this.exps = exps;
	}

	public KlDef getKlDef() {
		return klDef;
	}
	public void setKlDef(KlDef klDef) {
		this.klDef = klDef;
	}
	
	public long getRuleId() {
		return ruleId;
	}
	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}

	public int getCycleStart() {
		return cycleStart;
	}
	public void setCycleStart(int cycleStart) {
		this.cycleStart = cycleStart;
	}

	public int getCycleEnd() {
		return cycleEnd;
	}

	public void setCycleEnd(int cycleEnd) {
		this.cycleEnd = cycleEnd;
	}

	public int getCycleFlg() {
		return cycleFlg;
	}

	public void setCycleFlg(int cycleFlg) {
		this.cycleFlg = cycleFlg;
	}
	
	//计算日期区间，返回值<"start",startDate>,<"end",endDate>
	protected Map<String,Calendar> calDateArea(Date dat,String area,String unit){
		
		Map<String,Calendar> map = new HashMap<String,Calendar>();
		//开始时间
		Calendar startDate = Calendar.getInstance();
		//截止时间
		Calendar endDate = Calendar.getInstance();
		//无时间单位返回null，后续不做处理
		if (null == area || "".equals(area) || null == dat){
			return null;
			//如果时间区域为空，则开始时间默认最小日期，如1900年，结束时间为当前审核项目时间
//				startDate.set(1900, 0, 1, 0, 0, 0);
//				endDate.setTime(dat);
		}else{
			int start,end;
			boolean startNegative=false;
			boolean endPositive =false;
			startDate.setTime(dat);
			endDate.setTime(dat);
		    //计算日期区间
			String[] areas = area.split("[|]");
			//参数不对，返回null
			if (2 > areas.length){
				return null;
			}
			
			start = Integer.parseInt(areas[0]);
			// -0:向后查找 -0 当前时间，除此之外0:0:0
			if (areas[0].contains("-") && 0 == start){
				startNegative = true;
//				start = -0;
			}
			end = Integer.parseInt(areas[1]);
			// 0 当前时间 除此之外23:59:59
			if (false == areas[1].contains("-") && 0 == end){
				endPositive = true;
//				end = -0;
			}
			if ("HH".equals(unit)){
				startDate.add(Calendar.HOUR_OF_DAY, -start);		
				endDate.add(Calendar.HOUR_OF_DAY, -end);
			}else if ("MI".equals(unit)){
				startDate.add(Calendar.MINUTE, -start);
				endDate.add(Calendar.MINUTE, -end);
			}else{
				startDate.add(Calendar.DATE, -start);
//				if (!"-0".equals(String.valueOf(start))){
				if (startNegative == false){
					startDate.set(Calendar.HOUR_OF_DAY, 0);
					startDate.set(Calendar.MINUTE, 0);
					startDate.set(Calendar.SECOND, 0);
				}
				endDate.add(Calendar.DATE, -end);
//				if (!"0".equals(String.valueOf(end))){
				if (endPositive == false){
					endDate.set(Calendar.HOUR_OF_DAY, 23);
					endDate.set(Calendar.MINUTE, 59);
					endDate.set(Calendar.SECOND, 59);
				}			
			}
		}
			
		map.put("start", startDate);
		map.put("end", endDate);
		
		return map;
	}
		
		/**
		 * 构造错误信息类
		 * @param key 联合主键
		 * @param dataType 错误数据类型
		 * @param medType 就医类型
		 * @param preID 医嘱或者处方ID
		 * @param msg 违规提示
		 * @param errItem 违规项目
		 * @param refTot 涉及金额
		 * @param pid 患者ID
		 * @param pname 患者姓名
		 * @param clmDt 结算日期
		 * @param lgcyOrgCode 医院编号
		 * @return
		 */
		protected BreakDetail writeError(String key, int dataType, int medType, long preID, String msg,
				String errItem, double refTot, String pid, String pname,Date clmDt,String lgcyOrgCode,long kID,String errElemIDs,String errElemcateIDs,Set<String> errItems){
			
			BreakDetail err = new BreakDetail();
			err.setRule_id(this.ruleId);// 规则ID
			String[] clmAndDs = key.split("_");
			err.setClm_id(Long.valueOf(clmAndDs[1]));// 结算单ID
			err.setData_src_id(Long.valueOf(clmAndDs[0]));// 数据来源ID
			err.setErrData_type(dataType);// 错误数据类型(1诊断、2处方、3医嘱、4分解住院)
			err.setMed_type_id(String.valueOf(medType));// 就医类型（1门诊、2门特、3住院）
			err.setPre_id(preID);// 医嘱或者处方ID
			err.setBreak_detail(msg);// 错误详细信息
			err.setErr_item_name(errItem);//审核项目名称
			err.setPay_tot(refTot);
			err.setP_id(pid);
			err.setP_name(pname);
			err.setClm_Dt(clmDt);
			err.setLgcy_org_code(lgcyOrgCode);
			err.setK_id(kID);
			err.setErr_elem_ids(errElemIDs);
			err.setErr_category_ids(errElemcateIDs);
			err.setErritem(errItems);
			
			return err;
		}

}

/**
 * 返回违规信息信息类
 */
class ErrMessage{
	
	private boolean legal;
	private String errElemID; 
	private String errCateid;
	private String errDes;
	private Set<String> err_item; //违规记录明细 elem_id,pre_id
	
	public boolean isLegal() {
		return legal;
	}
	public void setLegal(boolean legal) {
		this.legal = legal;
	}
	public String getErrElemID() {
		return errElemID;
	}
	public void setErrElemID(String errElemID) {
		this.errElemID = errElemID;
	}
	public String getErrCateid() {
		return errCateid;
	}
	public void setErrCateid(String errCateid) {
		this.errCateid = errCateid;
	}
	public String getErrDes() {
		return errDes;
	}
	public void setErrDes(String errDes) {
		this.errDes = errDes;
	}
	public Set<String> getErr_item() {
		return err_item;
	}
	public void setErr_item(Set<String> err_item) {
		this.err_item = err_item;
	}
	
}
