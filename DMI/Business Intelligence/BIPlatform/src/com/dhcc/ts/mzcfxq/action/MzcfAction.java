package com.dhcc.ts.mzcfxq.action;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.dhcc.ts.ipdData.pharmacy.PharmacyDataDao;
import com.dhcc.ts.mzcfxq.dao.MzcfxqDao;
import com.dhcc.ts.mzcfxq.model.DoctorModel;
import com.dhcc.ts.mzcfxq.model.DrugModel;
import com.dhcc.ts.mzcfxq.model.MzcfModel;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MzcfAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private MzcfxqDao dao = new MzcfxqDao();
	private String mzh;//门诊号
	private String cfh;//处方号
	
	/**
	 * @描述：根据处方号查处方
	 * @作者：SZ
	 * @时间：2017年2月22日 下午12:41:10
	 * @return
	 */
	public String queryMzcfModel() {
		MzcfModel model = dao.getMzcfModel(cfh);
		JSONObject json;
		json = JSONObject.fromObject(model);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * @描述：根据门诊号查处方列表
	 * @作者：SZ
	 * @时间：2017年2月22日 下午12:41:10
	 * @return
	 */
	public String queryMzcfListModel() {
		List<MzcfModel> listModel = dao.getMzcfListModel(mzh);
		JSONArray json = JSONArray.fromObject(listModel);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	
	private String doctor_id;
	public String queryDoctor(){
		MzcfxqDao dao = new MzcfxqDao();
		DoctorModel model = dao.queryDoctor(doctor_id);
		JSONObject json = JSONObject.fromObject(model);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	private String cardno;
	public String queryPharmacyDrug(){
		PharmacyDataDao dao = new PharmacyDataDao();
		Map<String,Object> map = dao.getPharmacyDrug(cardno);
		JSONObject json = JSONObject.fromObject(map);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private String ypid;
	public String queryDrugDetails(){
		DrugModel model = dao.getPharmacyDrug(ypid);
		JSONObject json = JSONObject.fromObject(model);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private String diseaseid;
	public String queryDiseaseDetails(){
		DrugModel model = dao.queryDiseaseDetails(diseaseid);
		JSONObject json = JSONObject.fromObject(model);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
		
	}
	
	public String getMzh() {
		return mzh;
	}

	public void setMzh(String mzh) {
		this.mzh = mzh;
	}

	public String getCfh() {
		return cfh;
	}

	public void setCfh(String cfh) {
		this.cfh = cfh;
	}
	public String getDoctor_id() {
		return doctor_id;
	}
	public void setDoctor_id(String doctor_id) {
		this.doctor_id = doctor_id;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getYpid() {
		return ypid;
	}
	public void setYpid(String ypid) {
		this.ypid = ypid;
	}
	public String getDiseaseid() {
		return diseaseid;
	}
	public void setDiseaseid(String diseaseid) {
		this.diseaseid = diseaseid;
	}
}
