package com.dhcc.ts.gh;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年4月10日 上午10:19:54 
* @类说明：智能挂号挨克深
*/
public class GhAction extends ActionSupport{

	private String q_user_id;
	private String q_addr_id;
	private String q_need_prefer;
	private String q_period;
	private String q_disease;
	private String q_date;
	private String q_sort_field;
	private String q_sort_type;
	private String q_hospital_name;
	private String q_doctor_name;
	private String q_hospital_id;
	private String q_service_type;
	private String q_is_yibao;
	private String q_hosp_level;
	private String q_plan_type;
	private String q_hosp_type;
	private String q_doctor_title;
	private String q_prefer_list;
	private String q_doctor_id;
	private String user_id;
	private String user_name;
	private String gender;
	private String mobile;
	private String address;
	private String is_default;
	private String age;
	private String card_no;
	private Integer page=1;
	private String ws_lng;
	private String ws_lat;
	private String ne_lng;
	private String ne_lat;
	private Integer zoom_lvl;
	/**  
	* @标题: queryUserInfoList  
	* @描述: TODO用户地址列表
	* @作者 EbaoWeixun
	*/  
	public void queryUserInfoList(){
		GhDao gd=new GhDao();
		JSONArray jry=new JSONArray().fromObject(gd.queryUserInfoList(q_user_id));
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
	* @标题: queryDoctorPlans  
	* @描述: TODO根据病症查询医生信息接口
	* @作者 EbaoWeixun
	*/  
	public void queryDoctorPlans(){
		GhDao gd=new GhDao();
		
		JSONObject job=new JSONObject().fromObject(gd.queryDoctorPlans(q_date, q_period, q_need_prefer, q_addr_id, q_sort_field, q_sort_type, q_hospital_name, q_doctor_name, q_hospital_id, q_disease, q_service_type, q_is_yibao, q_hosp_level, q_plan_type, q_hosp_type, q_doctor_title, page, ws_lng, ws_lat, ne_lng, ne_lat));
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
	* @标题: setPreference  
	* @描述: TODO修改偏好设置
	* @作者 EbaoWeixun
	*/  
	public void setPreference(){
		GhDao gd=new GhDao();
		JSONObject job=new JSONObject().fromObject(gd.setPreference(q_user_id, q_prefer_list));
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
	* @标题: queryHospitalPlans  
	* @描述: TODO地图医院查询显示医院位置，展示医生个数
	* @作者 EbaoWeixun
	*/  
	public void queryHospitalPlans(){
		GhDao gd=new GhDao();
		JSONArray jry=new JSONArray().fromObject(gd.queryHospitalPlans(q_date, q_period, q_disease, q_service_type, q_is_yibao, q_hosp_level, q_plan_type, q_hosp_type, q_addr_id, q_doctor_title,q_sort_field,q_hospital_name, ws_lng, ws_lat, ne_lng, ne_lat,zoom_lvl));
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
	* @标题: queryDetailPlans  
	* @描述: TODO查询号源接口
	* @作者 EbaoWeixun
	*/  
	public void queryDetailPlans(){
		GhDao gd=new GhDao();
		JSONArray job=new JSONArray().fromObject(gd.queryDetailPlans(q_doctor_id, q_date));
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
	* @标题: queryDoctorInfo  
	* @描述: TODO查询医生信息
	* @作者 EbaoWeixun
	*/  
	public void queryDoctorInfo(){
		GhDao gd=new GhDao();
		JSONObject job=new JSONObject().fromObject(gd.queryDoctorInfo(q_doctor_id));
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
	* @标题: addUserInfo  
	* @描述: TODO添加个人信息
	* @作者 EbaoWeixun
	*/  
	public void addUserInfo(){
		GhDao gd=new GhDao();
		
		Map<String, String> xy = AddressParse.geocode(address.replaceAll("@#", ""));
		String x = xy.get("lat");
		String y = xy.get("lng");
		Map<String,Object> map=null;
		PrintWriter pw = null;
		try {
			if (StringUtils.isBlank(x)||StringUtils.isBlank(y)) {
				map=new HashMap<String,Object>();
				map.put("status", 1);
				map.put("msg", "获取位置坐标失败");
			} else {
				map=gd.addUserInfo(user_id, user_name, gender, mobile, address, is_default, age, card_no, x, y);
			}
			JSONObject job=new JSONObject().fromObject(map);
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
	* @标题: modifyUserInfo  
	* @描述: TODO修改个人信息
	* @作者 EbaoWeixun
	*/  
	public void modifyUserInfo(){
		GhDao gd=new GhDao();
		
		Map<String, String> xy = AddressParse.geocode(address.replaceAll("@#", ""));
		String x = xy.get("lat");
		String y = xy.get("lng");
		Map<String,Object> map=null;
		
		PrintWriter pw = null;
		try {
			if (StringUtils.isBlank(x)||StringUtils.isBlank(y)) {
				map=new HashMap<String,Object>();
				map.put("status", 1);
				map.put("msg", "获取位置坐标失败");
			} else {
				map=gd.modifyUserInfo(q_addr_id, user_name, gender, mobile, address, is_default, age, card_no, x, y);
			}
			JSONObject job=new JSONObject().fromObject(map);
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delAddressInfo(){
		GhDao gd=new GhDao();
		JSONObject job=new JSONObject().fromObject(gd.delAddressInfo(q_addr_id));
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
	* @标题: queryAddressInfo  
	* @描述: TODO查询地址信息
	* @作者 EbaoWeixun
	*/  
	public void queryAddressInfo(){
		GhDao gd=new GhDao();
		JSONObject job=new JSONObject().fromObject(gd.queryAddressInfo(q_addr_id));
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
	* @标题: queryUserInfo  
	* @描述: TODO查询个人信息
	* @作者 EbaoWeixun
	*/  
	public void queryUserInfo(){
		GhDao gd=new GhDao();
		JSONObject job=new JSONObject().fromObject(gd.queryUserInfo(q_user_id));
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
	* @标题: modifyUserDefault  
	* @描述: TODO修改默认地址
	* @作者 EbaoWeixun
	*/  
	public void modifyUserDefault(){
		GhDao gd=new GhDao();
		JSONObject job=new JSONObject().fromObject(gd.modifyUserDefault(q_user_id, q_addr_id));
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
	public String getQ_user_id() {
		return q_user_id;
	}
	public void setQ_user_id(String q_user_id) {
		this.q_user_id = q_user_id;
	}
	public String getQ_addr_id() {
		return q_addr_id;
	}
	public void setQ_addr_id(String q_addr_id) {
		this.q_addr_id = q_addr_id;
	}

	public String getQ_need_prefer() {
		return q_need_prefer;
	}

	public void setQ_need_prefer(String q_need_prefer) {
		this.q_need_prefer = q_need_prefer;
	}

	public String getQ_period() {
		return q_period;
	}

	public void setQ_period(String q_period) {
		this.q_period = q_period;
	}

	public String getQ_disease() {
		return q_disease;
	}

	public void setQ_disease(String q_disease) {
		this.q_disease = q_disease;
	}

	public String getQ_date() {
		return q_date;
	}

	public void setQ_date(String q_date) {
		this.q_date = q_date;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	public String getQ_sort_field() {
		return q_sort_field;
	}
	public void setQ_sort_field(String q_sort_field) {
		this.q_sort_field = q_sort_field;
	}
	public String getQ_sort_type() {
		return q_sort_type;
	}
	public void setQ_sort_type(String q_sort_type) {
		this.q_sort_type = q_sort_type;
	}
	public String getQ_hospital_name() {
		return q_hospital_name;
	}
	public void setQ_hospital_name(String q_hospital_name) {
		this.q_hospital_name = q_hospital_name;
	}
	public String getQ_doctor_name() {
		return q_doctor_name;
	}
	public void setQ_doctor_name(String q_doctor_name) {
		this.q_doctor_name = q_doctor_name;
	}
	public String getQ_hospital_id() {
		return q_hospital_id;
	}
	public void setQ_hospital_id(String q_hospital_id) {
		this.q_hospital_id = q_hospital_id;
	}
	public String getQ_service_type() {
		return q_service_type;
	}
	public void setQ_service_type(String q_service_type) {
		this.q_service_type = q_service_type;
	}
	public String getQ_is_yibao() {
		return q_is_yibao;
	}
	public void setQ_is_yibao(String q_is_yibao) {
		this.q_is_yibao = q_is_yibao;
	}
	public String getQ_hosp_level() {
		return q_hosp_level;
	}
	public void setQ_hosp_level(String q_hosp_level) {
		this.q_hosp_level = q_hosp_level;
	}
	public String getQ_plan_type() {
		return q_plan_type;
	}
	public void setQ_plan_type(String q_plan_type) {
		this.q_plan_type = q_plan_type;
	}
	public String getQ_hosp_type() {
		return q_hosp_type;
	}
	public void setQ_hosp_type(String q_hosp_type) {
		this.q_hosp_type = q_hosp_type;
	}
	public String getQ_doctor_title() {
		return q_doctor_title;
	}
	public void setQ_doctor_title(String q_doctor_title) {
		this.q_doctor_title = q_doctor_title;
	}
	public String getQ_prefer_list() {
		return q_prefer_list;
	}
	public void setQ_prefer_list(String q_prefer_list) {
		this.q_prefer_list = q_prefer_list;
	}
	public String getQ_doctor_id() {
		return q_doctor_id;
	}
	public void setQ_doctor_id(String q_doctor_id) {
		this.q_doctor_id = q_doctor_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIs_default() {
		return is_default;
	}
	public void setIs_default(String is_default) {
		this.is_default = is_default;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getWs_lng() {
		return ws_lng;
	}
	public void setWs_lng(String ws_lng) {
		this.ws_lng = ws_lng;
	}
	public String getWs_lat() {
		return ws_lat;
	}
	public void setWs_lat(String ws_lat) {
		this.ws_lat = ws_lat;
	}
	public String getNe_lng() {
		return ne_lng;
	}
	public void setNe_lng(String ne_lng) {
		this.ne_lng = ne_lng;
	}
	public String getNe_lat() {
		return ne_lat;
	}
	public void setNe_lat(String ne_lat) {
		this.ne_lat = ne_lat;
	}
	public Integer getZoom_lvl() {
		return zoom_lvl;
	}
	public void setZoom_lvl(Integer zoom_lvl) {
		this.zoom_lvl = zoom_lvl;
	}
	
}
