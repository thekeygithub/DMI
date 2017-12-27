package com.dhcc.ts.current;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.ipdData.behavior.BehaviorDao;
import com.dhcc.ts.ipdData.device.DeviceDao;
import com.dhcc.ts.ipdData.health.HealthArchiveDao;
import com.dhcc.ts.ipdData.hospital.HospitalDataDao;
import com.dhcc.ts.ipdData.model.CurrentModel;
import com.dhcc.ts.ipdData.pharmacy.PharmacyDataDao;
import com.dhcc.ts.ipdData.sheet.SettlementDao;
import com.dhcc.ts.yyxg.Constant;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/** 
* @作者:EbaoWeixun 
* @创建时间：2017年2月21日 下午4:39:53 
* @类说明：查询就诊数据
*/
public class CurrentAction extends ActionSupport implements ModelDriven<CurrentModel>{

	public static final String SENSITIVE_DATA_REPLACEMENT = "****";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CurrentModel model=new CurrentModel();
	
	private String cardno;//身份证
	private String bname;//查询名
	private String bdate;//开始时间
	private String edate;//结束时间
    private String ttype;
    private String flag;
    private InputStream inputStream;
    
    private String isAuth;
    
    public String getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String testFile(){
    	String path = "D:\\2070.flv";  
    	
        File file = new File(path); 
       
        try {
        	
        	inputStream=new FileInputStream(file);
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    	return "success";
    }
	/**  
	* @标题: findData  
	* @描述: 查询所有数据
	* @作者 EbaoWeixun
	*/  
	public void findData(){
		
		List<CurrentModel> list=null;
		HospitalDataDao hd=new HospitalDataDao();
		PharmacyDataDao pd=new PharmacyDataDao();
		BehaviorDao bd=new BehaviorDao();
		DeviceDao dd=new DeviceDao();
		HealthArchiveDao had=new HealthArchiveDao();
		SettlementDao sd=new SettlementDao();
		List<CurrentModel> dataList=new ArrayList<CurrentModel>();
		Map<String,Object> map=new HashMap<String,Object>();
		switch(ttype){
		    case "00"://全部
		    	
			    list=hd.findMzDataList(cardno, bname, bdate, edate);
			    if(list.isEmpty()){
			    	map.put("11","1");
			    }
			    if (!"1".equals(isAuth)) filterICD10(list);
			    dataList=hd.findZyDataList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	if (!"1".equals(isAuth)) filterICD10(dataList);
			    	list.addAll(dataList);
			    }else{
			    	map.put("12","1");
			    }
			    dataList=pd.findPharmacyDataList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }else{
			    	map.put("13","1");
			    }
			    dataList=bd.findBehaviorList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }else{
			    	map.put("05","1");
			    }
			    dataList=dd.findDeviceList(cardno, bname, bdate, edate,null);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }else{
			    	map.put("04","1");
			    }
			    dataList=had.findHerArchiveList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }else{
			    	map.put("17","1");
			    }
			    dataList=sd.findSheetList(cardno, bname, bdate, edate, null);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	if (!"1".equals(isAuth)) filterICD10(dataList);
			    	list.addAll(dataList);
			    }else{
			    	map.put("03","1");
			    }
			    dataList=had.findHerFollowList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }else{
			    	map.put("19","1");
			    }
			    dataList=had.findHerExList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }else{
			    	map.put("18","1");
			    }
			    break;
		    case "01"://就医
		    	
			    list=hd.findMzDataList(cardno, bname, bdate, edate);
			    dataList=hd.findZyDataList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
		    	break;
            case "02"://药店
			    list=pd.findPharmacyDataList(cardno, bname, bdate, edate);
			    break;
            case "03"://结算单
	            list=sd.findSheetList(cardno, bname, bdate, edate,null);
	            if (!"1".equals(isAuth)) filterICD10(list);
	            break;
            case "04"://医疗可穿戴设备
	            list=dd.findDeviceList(cardno, bname, bdate, edate,null);
	            break;
            case "05"://使用行为
	            list=bd.findBehaviorList(cardno, bname, bdate, edate);
	            break;
            case "06"://个人健康档案
        	    list=had.findHerArchiveList(cardno, bname, bdate, edate);
        	    dataList=had.findHerFollowList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
			    dataList=had.findHerExList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
			    dataList=had.findHerFollowList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
	            break;
            case "11"://门诊
            	list=hd.findMzDataList(cardno, bname, bdate, edate);
            	if (!"1".equals(isAuth)) filterICD10(list);
	            break;
            case "12"://住院
        	    list=hd.findZyDataList(cardno, bname, bdate, edate);
        	    if (!"1".equals(isAuth)) filterICD10(list);
	            break;
            case "13"://购药
            	list=pd.findPharmacyDataList(cardno, bname, bdate, edate);
	            break;
            case "15"://测量血压
            	list=dd.findDeviceList(cardno, bname, bdate, edate,"1");
	            break;
            case "16"://测量血糖
            	list=dd.findDeviceList(cardno, bname, bdate, edate,"2");
	            break;
            case "17"://健康档案建档
        	    list=had.findHerArchiveList(cardno, bname, bdate, edate);
	            break;
            case "18"://健康体检
        	    list=had.findHerExList(cardno, bname, bdate, edate);
	            break;
            case "10":
            	list=hd.findMzDataList(cardno, bname, bdate, edate);
            	dataList=hd.findZyDataList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
			    dataList=pd.findPharmacyDataList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
			    dataList=bd.findBehaviorList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
			    dataList=dd.findDeviceList(cardno, bname, bdate, edate,null);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
			    dataList=had.findHerArchiveList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
			    dataList=had.findHerFollowList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
			    dataList=had.findHerExList(cardno, bname, bdate, edate);
			    if(dataList!=null&&!dataList.isEmpty()){
			    	list.addAll(dataList);
			    }
	            break;
	        default://慢病随访
	        	list=had.findHerFollowList(cardno, bname, bdate, edate);
	        	break;
		}
		
		JSONArray jry=null;
		if(!StringUtil.isNullOrEmpty(bname)){
			List<CurrentModel> nlist=new ArrayList<CurrentModel>();
			for(CurrentModel cm:list){
				if(!StringUtil.isNullOrEmpty(cm.getCorp())&&cm.getCorp().contains(bname)||!StringUtil.isNullOrEmpty(cm.getDetail())&&cm.getDetail().contains(bname)||!StringUtil.isNullOrEmpty(cm.getDtype())&&cm.getDtype().contains(bname)||!StringUtil.isNullOrEmpty(cm.getMtype())&&cm.getMtype().contains(bname)||!StringUtil.isNullOrEmpty(cm.getRdate())&&cm.getRdate().contains(bname)||!StringUtil.isNullOrEmpty(cm.getUnit())&&cm.getUnit().contains(bname)){
					nlist.add(cm);
				}
			}
			jry=new JSONArray().fromObject(nlist);
		}else{
			jry=new JSONArray().fromObject(list);
		}
		
		map.put("list", jry);
	
		JSONObject json=new JSONObject().fromObject(map);
		
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
	}
	
	/**
	 * 过滤ICD10的疾病并替换成****
	 * @param list
	 */
	private void filterICD10(List<CurrentModel> list) {
		Map<String, String> icdMap = Constant.getICD10_MAP();
		if (list != null && list.size() > 0) {
			for (CurrentModel model : list) {
				String mts_zd = model.getMts_zd();
				if (StringUtils.isBlank(mts_zd)) continue;
				
				String[] nameAndCodeArr = mts_zd.split(",");
				for (String nameAndCode : nameAndCodeArr) {
					if (StringUtils.isBlank(nameAndCode)) continue;
					
					String[] codeArr = nameAndCode.split("@#&");
					if (codeArr.length >= 2) {
						if (StringUtils.isNotBlank(icdMap.get(codeArr[1]))) {
							model.setUnit(SENSITIVE_DATA_REPLACEMENT);
							model.setDcname(SENSITIVE_DATA_REPLACEMENT);
							model.setCorp(SENSITIVE_DATA_REPLACEMENT);
							model.setDetail(SENSITIVE_DATA_REPLACEMENT); break;
						}
					}
				}
			}
		}
		
	}
	
	/**  
	* @标题: findTestData  
	* @描述: 查询住院检验数据
	* @作者 EbaoWeixun
	*/  
	public void findTestData(){
		
		HospitalDataDao hd=new HospitalDataDao();
		JSONArray jry=new JSONArray().fromObject(hd.findTestData(cardno));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(jry);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/**  
* @标题: findTestDataDetail  
* @描述: 查询住院检验明细
* @作者 EbaoWeixun
*/  
    public void findTestDataDetail(){
		
		HospitalDataDao hd=new HospitalDataDao();
		JSONObject job=new JSONObject().fromObject(hd.findTestDetail(cardno,bname,ttype));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findPersonInformation  
	* @描述: TODO 查询个人信息
	* @作者 EbaoWeixun
	*/  
	public void findPersonInformation(){
		HospitalDataDao hd=new HospitalDataDao();
		JSONObject job=new JSONObject().fromObject(hd.findPersonInformation(cardno));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findBehavior  
	* @描述: TODO主键查询app使用行为
	* @作者 EbaoWeixun
	*/  
	public void findBehavior(){
		BehaviorDao bd=new BehaviorDao();
		JSONObject job=new JSONObject().fromObject(bd.getBehavior(cardno));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findDevice  
	* @描述: TODO主键查询可穿戴设备信息
	* @作者 EbaoWeixun
	*/  
	public void findDevice(){
		DeviceDao dd=new DeviceDao();
		JSONObject job=new JSONObject().fromObject(dd.getDevice(cardno));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findSettlement  
	* @描述: TODO查询结算单详细信息
	* @作者 EbaoWeixun
	*/  
	public void findSettlement(){
		SettlementDao sd=new SettlementDao();
		JSONObject job=new JSONObject().fromObject(sd.getSettlement(cardno,bdate,edate,bname,ttype,flag));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findRyjl  
	* @描述: TODO查询电子病历入院记录
	* @作者 EbaoWeixun
	*/  
	public void findInHospital(){
		HospitalDataDao hd=new HospitalDataDao();
		JSONObject job=new JSONObject().fromObject(hd.findRyjl(cardno,bname));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void findZyjl(){
		HospitalDataDao hd=new HospitalDataDao();
		JSONObject job=new JSONObject().fromObject(hd.findZyjl(cardno));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findSettlementDetail  
	* @描述: TODO查询结算单详细信息
	* @作者 EbaoWeixun
	*/  
	public void findSettlementDetail(){
		SettlementDao sd=new SettlementDao();
		JSONObject job=new JSONObject().fromObject(sd.findSettlementDetail(cardno, ttype, bdate, edate, flag));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findHealthCover  
	* @描述: TODO 查询健康档案封面详细信息
	* @作者 EbaoWeixun
	*/  
	public void findHealthCover(){
		HealthArchiveDao hd=new HealthArchiveDao();
		JSONObject job=new JSONObject().fromObject(hd.getHealthCover(cardno));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**  
	* @标题: findHealthEx  
	* @描述: TODO查询健康体检详细信息
	* @作者 EbaoWeixun
	*/  
	public void findHealthEx(){
		HealthArchiveDao hd=new HealthArchiveDao();
		JSONObject job=new JSONObject().fromObject(hd.getExamination(cardno));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findHealthFollowing  
	* @描述: TODO查询随访记录详细信息
	* @作者 EbaoWeixun
	*/  
	public void findHealthFollowing(){
		HealthArchiveDao hd=new HealthArchiveDao();
		JSONArray job=new JSONArray().fromObject(hd.findHealthFollowing(cardno, flag));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public CurrentModel getModel() {
		return model;
	}

	public void setModel(CurrentModel model) {
		this.model = model;
	}


	public String getCardno() {
		return cardno;
	}


	public void setCardno(String cardno) {
		this.cardno = cardno;
	}


	public String getBname() {
		return bname;
	}


	public void setBname(String bname) {
		this.bname = bname;
	}


	public String getBdate() {
		return bdate;
	}


	public void setBdate(String bdate) {
		this.bdate = bdate;
	}


	public String getEdate() {
		return edate;
	}


	public void setEdate(String edate) {
		this.edate = edate;
	}


	public String getTtype() {
		return ttype;
	}


	public void setTtype(String ttype) {
		this.ttype = ttype;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}


	


	public static void main(String[] args) {
		CurrentAction ca = new CurrentAction();
		List<CurrentModel> list = new ArrayList<CurrentModel>();
		CurrentModel c1 = new CurrentModel();
		c1.setDetail("功能障碍性子宫出血");
		c1.setMts_zd("功能障碍性子宫出血@#&A20@#&");
		list.add(c1);
		ca.filterICD10(list);
		
		for (CurrentModel m : list) {
			System.out.println(m.getDetail());
		}
	}



    
	

}
