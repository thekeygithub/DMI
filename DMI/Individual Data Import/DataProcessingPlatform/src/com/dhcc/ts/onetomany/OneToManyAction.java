package com.dhcc.ts.onetomany;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年6月1日 下午1:11:23 
* @类说明：一次录入多种输出
*/
public class OneToManyAction extends ActionSupport{
     
	private String sjType;
	private String id;
	/**  
	* @标题: findOtmData  
	* @描述: TODO查询多种数据列表
	* @作者 EbaoWeixun
	*/  
	public void findOtmData(){
		JSONObject job1=new JSONObject();
		JSONObject job2=new JSONObject();
		JSONObject job3=new JSONObject();
		JSONObject job4=new JSONObject();
		if("0".equals(sjType)){//结算单
			job1.put("a1", "1603017930");
			job1.put("a2", "泌尿外科门诊");
			job1.put("a3","侯俊清");
			job1.put("a4", "2016-03-11");
			
			job2.put("a1", "1603017959");
			job2.put("a2", "乳腺甲状腺门诊");
			job2.put("a3","王顺昌");
			job2.put("a4", "2016-03-11");
			
			job3.put("a1", "1604036132");
			job3.put("a2", "内分泌门诊");
			job3.put("a3","薛磊");
			job3.put("a4", "2016-04-23");
			
			job4.put("a1", "1608025070");
			job4.put("a2", "神经内科门诊");
			job4.put("a3","刘伟");
			job4.put("a4", "2016-08-16");
		}else if("1".equals(sjType)){//电子病历
			job1.put("a1", "53070961");
			job1.put("a2", "卢世臣");
			job1.put("a3","周淑芳");
			job1.put("a4", "2014-03-08");
			
			job2.put("a1", "14021477");
			job2.put("a2", "孔令玉");
			job2.put("a3","孙玉华");
			job2.put("a4", "2014-02-17");
			
			job3.put("a1", "15011645");
			job3.put("a2", "王嫩妮");
			job3.put("a3","杨少琴");
			job3.put("a4", "2015-01-11");
			
			job4.put("a1", "15011355");
			job4.put("a2", "肖景");
			job4.put("a3","郭红雨");
			job4.put("a4", "2015-01-09");
		}else if("2".equals(sjType)){//健康档案
			
		}else{//疾病预防控制中心
			
		}
		JSONArray json=new JSONArray();
		json.add(job1);
		json.add(job2);
		json.add(job3);
		json.add(job4);
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
	* @标题: findJsdBasyData  
	* @描述: TODO查询结算单基本信息
	* @作者 EbaoWeixun
	*/  
	public void findJsdBasyData(){
		Map<String,Object> map=OneToMany.jsdBasicMap.get(id);
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
	
	public String getSjType() {
		return sjType;
	}
	public void setSjType(String sjType) {
		this.sjType = sjType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
}
