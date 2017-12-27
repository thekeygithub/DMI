package com.ebmi.seng;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class YearStatistics {

	private Double person_paytotal;//个人支付：本次个人帐户支付金额（col6）+本次个人支付金额(col9)
	private Double person_zhanghuzfje;//：本次个人帐户支付金额（col6）
	private Double person_zhujine;//本次个人支付金额(col9)

	//医保支付：本次统筹金支付金额(col5)+本次公务员基金支付金额(col7)+本次大额医疗补助支付金额（col8）
	private Double curyear_paytotal;// 本次统筹金支付金额(col5)
	private Double civil_paytotal;//本次公务员基金支付金额(col7)
	private Double large_paytotal;//本次大额医疗补助支付金额（col8）
	//医保支付金额

	//就诊类型分类
	private Map<String,Double> mapdata;

	//费用明细
	private List<PayDatil> dlist;

	//消费查询
	private List<ConsumpteModel> consumpteList;

	public List<ConsumpteModel> getConsumpteList() {
		return consumpteList;
	}
	public void setConsumpteList(List<ConsumpteModel> consumpteList) {
		this.consumpteList = consumpteList;
	}
	public List<PayDatil> getDlist() {
	   return dlist;
	}
	public void setDlist(List<PayDatil> dlist) {
		this.dlist = dlist;
	}
	
	public static class PayDatil{
		private Double pay_tot;//	就医费用  门诊费用金额  本次发生医疗费总额 
	    private String org_name	;//医疗机构名称  机构  消费地点 
	    private String med_type_name	;// 就诊类型名称
	    private Date clm_date	;//就诊时间
	   
		public Double getPay_tot() {
			return pay_tot;
		}
		public void setPay_tot(Double pay_tot) {
			this.pay_tot = pay_tot;
		}
		public String getOrg_name() {
			return org_name;
		}
		public void setOrg_name(String org_name) {
			this.org_name = org_name;
		}
		public String getMed_type_name() {
			return med_type_name;
		}
		public void setMed_type_name(String med_type_name) {
			this.med_type_name = med_type_name;
		}
		public Date getClm_date() {
			return clm_date;
		}
		public void setClm_date(Date clm_date) {
			this.clm_date = clm_date;
		}
	}

	
	public Map<String, Double> getMapdata() {
		return mapdata;
	}
	public void setMapdata(Map<String, Double> mapdata) {
		this.mapdata = mapdata;
	}
	public Double getPerson_paytotal() {
		return person_paytotal;
	}
	public void setPerson_paytotal(Double person_paytotal) {
		this.person_paytotal = person_paytotal;
	}
	public Double getCuryear_paytotal() {
		return curyear_paytotal;
	}
	public void setCuryear_paytotal(Double curyear_paytotal) {
		this.curyear_paytotal = curyear_paytotal;
	}
	public Double getCivil_paytotal() {
		return civil_paytotal;
	}
	public void setCivil_paytotal(Double civil_paytotal) {
		this.civil_paytotal = civil_paytotal;
	}
	public Double getLarge_paytotal() {
		return large_paytotal;
	}
	public void setLarge_paytotal(Double large_paytotal) {
		this.large_paytotal = large_paytotal;
	}
	public Double getPerson_zhanghuzfje() {
		return person_zhanghuzfje;
	}
	public void setPerson_zhanghuzfje(Double person_zhanghuzfje) {
		this.person_zhanghuzfje = person_zhanghuzfje;
	}
	public Double getPerson_zhujine() {
		return person_zhujine;
	}
	public void setPerson_zhujine(Double person_zhujine) {
		this.person_zhujine = person_zhujine;
	}

}