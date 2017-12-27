package com.aghit.task.algorithm.domain.rule;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.aghit.task.algorithm.AbstractRule;
import com.aghit.task.algorithm.domain.BreakDetail;
import com.aghit.task.algorithm.domain.Hospital;
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.algorithm.domain.Payment;
import com.aghit.task.util.Constant;

public class HospitalPatientRule extends AbstractRule {
	
	private int day = 0;
	
	private Date auditDataTime = new Date();
	
	public HospitalPatientRule(int day, long ruleId, Date auditDataTime){
		this.day = day;
		this.ruleId = ruleId;
		this.auditDataTime = auditDataTime;
	}
	
	/**
	 * 根据患者的住院结算单数据，进行两步校验工作
	 * 1.	校验一个结算中的结束时间在开始时间之后，如果不是则该数据是违规数据
	 * 2.	校验相邻的结算单数据A、B，如果B的入院时间  >  A的出院时间 + 周期时间 则数据正常
	 * 		反之为违规数据
	 */
	public List<BreakDetail> check(Patient p) throws Exception{
		List<BreakDetail> bds = new ArrayList<BreakDetail>();
		List<Payment> impClmList = p.getInpClmList();//患者的住院结算单数据,已经排序
		if(impClmList == null || impClmList.isEmpty()){
			return new ArrayList<BreakDetail>();
		}
		
		for(int i = 0; i < impClmList.size(); i++){
			Hospital hos = (Hospital)impClmList.get(i);
			
			//验证当前结算的结算时间与任务的数据验证时间
			//如果结算单的结算时间 >= 任务的数据验证时间（auditDataTime）的时候，才验证单次住院的入院时间，和 出院时间
			if(!hos.getClm_date().before(auditDataTime)){
				
				//验证单次的入院时间和出院时间的时间前后关系（注释1）
				if(this.compareDates(hos.getEnterDate(), hos.getLeaveDate()) > 0){
					this.createBreakdetail(bds, hos, "一次住院，入院时间晚于出院时间");
				}
			}
			
			
			if(i + 1 > impClmList.size() - 1){
				break;
			}
			
			Hospital paymentCurrent = hos;
			Hospital paymentNext = (Hospital)impClmList.get(i + 1);
			
			//验证相邻的结算单数据的时间前后关系（注释2）
			if(this.calIntervalDays(paymentCurrent.getLeaveDate(), paymentNext.getEnterDate()) < this.day){
				this.createBreakdetail(bds, paymentNext, "两次相邻的住院，规定间隔不少于" + this.day + "天，患者间隔" 
						+ new DecimalFormat("0.0").format(this.calIntervalDays(paymentCurrent.getLeaveDate(), 
								paymentNext.getEnterDate())) + "天");
			}
			
		}
		
		return bds;
	}
	
	public void createBreakdetail( List<BreakDetail> bds, Hospital hos, String beakDetail){
		BreakDetail bk = new BreakDetail();
		
		bk.setRule_id(this.ruleId);
		//bk.setErrData_type(4);
		bk.setErrData_type(hos.getType());//把错误类型的源头依赖加到加载数据过程中去，修改的时候，只需要修改加载数据即可
		bk.setMed_type_id(String.valueOf(Constant.MEDICAL_TYPE_HOSPITAL));
		if(StringUtils.isNotBlank(hos.getSi_clearing_cost_no())){
			String[] temp = hos.getSi_clearing_cost_no().split("_");
			bk.setData_src_id(Long.parseLong(temp[0]));
			bk.setClm_id(Long.parseLong(temp[1]));
		}
		
		bk.setPay_tot(hos.getPay_tot());
		bk.setErr_item_name(beakDetail);
		bk.setP_name(hos.getP_name());
		bk.setP_id(hos.getP_id());
		bk.setLgcy_org_code(hos.getH_code());
		bk.setClm_Dt(hos.getClm_date());
		
		bk.setBreak_detail(beakDetail);
		
		bds.add(bk);
	}
	
	
	/**
	 * 比较date1 和 date2 的大小
	 * 如果 < 0 则 date1 < date2
	 * 如果 = 0 则 date1 = date2
	 * @param date1
	 * @param date2
	 * @return
	 */
	public int compareDates(Date date1, Date date2){
		return date1.compareTo(date2);
	}
	
	/**
	 * 计算date1 和 date2 时间间隔是否在day天数之外
	 * 在day天数之外返回true，在day天数之内返回false
	 * @param date1
	 * @param date2
	 * @return
	 */
	public boolean calInTime(Date date1, Date date2){
		long temp = this.day * 1000 * 60 * 60 * 24;
		return (date2.getTime() - date1.getTime()) - temp > 0;
	}
	
	/**
	 * 计算date2 与 date1 相差几天 ，返回相差天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public double calIntervalDays(Date date1, Date date2){
		return ((double)(date2.getTime() - date1.getTime())/(1000 * 60 * 60 * 24));
	}
	
	@Override
	public List<BreakDetail> checkPayment(Collection<Payment> list, Patient p)
			throws Exception {
		return null;
	}

}
