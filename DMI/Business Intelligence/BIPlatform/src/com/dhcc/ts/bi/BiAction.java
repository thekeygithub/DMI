package com.dhcc.ts.bi;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.util.CommUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.database.DBManager_HSD;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年5月3日 上午10:08:34 
* @类说明：
*/
public class BiAction extends ActionSupport{

	private String beginDate;
	private String endDate;
	private String hosType;
	private String disType;
	private String flag;
	private Integer page=1;
	private Integer rows=10;
	private String hospitalId;
	private String nameChn;
	private String disease;
	private String isFirst;
	/**  
	* @标题: findBiInformationListByDisease  
	* @描述: 按疾病分类统计
	* @作者 LWH
	* @return
	*/  
	public void findBiInformationListByDisease(){
		StringBuffer sql = new StringBuffer();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> lineMap=new HashMap<String,Object>();
		List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
		sql.append(" select round(sum(c.AKC255)/100000,2) a1,                   ");
		sql.append(" round(sum(c.AKC260)/100000,2) a2,                        ");
		sql.append(" round(sum(c.AKC261)/100000,2) a3,                        ");
		sql.append(" round(sum(c.AKC285)/100000,2) a4,                        ");
		sql.append(" round(sum(c.akc283)/100000,2) a5, round(sum(c.AKC264)/100000,2) f       ");
		sql.append(" from (select a.aka121,                   ");
		sql.append(" b.AKC255,                                ");
		sql.append(" b.AKC260,                                ");
		sql.append(" b.AKC261,                                ");
		sql.append(" b.AKC264,                                ");
		sql.append(" b.AKC285,                                ");
		sql.append(" b.akc283                                 ");
		sql.append(" from TS_KC21 a                           ");
		sql.append(" inner join kc24 b on a.akc190 = b.akc190 ");
		sql.append(" and a.AKB020 = b.AKB020                  ");
		sql.append(" where b.AAE100 = 1                       ");
		sql.append(" and a.akc231 = '"+disease+"'             ");
		sql.append(" and b.AAE040>=to_date('"+beginDate+"','yyyy-mm')");
		sql.append(" and b.AAE040 < add_months(to_date('"+endDate+"','yyyy-mm'),1)   ) c ");
		DBManager_HSD dbm=new DBManager_HSD();
		try {
			list=dbm.executeQueryListHashMap(sql.toString());
			List<BiPieModel> pieList=new ArrayList<BiPieModel>();
			String title="";
			for(Map<String,Object> map1:list){
				BiPieModel biModel = new BiPieModel();
				biModel.setName("个人帐户");
				biModel.setY(Double.parseDouble(map1.get("A1").toString()));
				BiPieModel biModel1 = new BiPieModel();
				pieList.add(biModel1);
				biModel1.setName("统筹支付");
				biModel1.setY(Double.parseDouble(map1.get("A2").toString()));
				BiPieModel biModel2 = new BiPieModel();
				biModel2.setName("现金支付");
				biModel2.setY(Double.parseDouble(map1.get("A3").toString()));
				pieList.add(biModel2);
				BiPieModel biModel3 = new BiPieModel();
				biModel3.setName("公务员补助支出金额");
				biModel3.setY(Double.parseDouble(map1.get("A4").toString()));
				pieList.add(biModel3);
				BiPieModel biModel4 = new BiPieModel();
				biModel4.setName("救助金支出金额");
				biModel4.setY(Double.parseDouble(map1.get("A5").toString()));
				pieList.add(biModel4);
				pieList.add(biModel);
				title = "总费用："+map1.get("F").toString();
				
				
				StringBuffer sql1 = new StringBuffer();
				sql1.append(" select c.ym,round(sum(c.AKC255)/100000,2) a1,              ");
				sql1.append(" round(sum(c.AKC260)/100000,2) a2,                        ");
				sql1.append(" round(sum(c.AKC261)/100000,2) a3,                        ");
				sql1.append(" round(sum(c.AKC285)/100000,2) a4,                        ");
				sql1.append(" round(sum(c.akc283)/100000,2) a5, round(sum(c.AKC264)/100000,2) f        ");
				sql1.append(" from (select a.aka121,                   ");
				sql1.append(" b.AKC255,                                ");
				sql1.append(" b.AKC260,                                ");
				sql1.append(" b.AKC261,                                ");
				sql1.append(" b.AKC264,                                ");
				sql1.append(" b.AKC285,                                ");
				sql1.append(" b.akc283,to_char(b.AAE040, 'yyyy-mm') ym ");
				sql1.append(" from TS_KC21 a                           ");
				sql1.append(" inner join kc24 b on a.akc190 = b.akc190 ");
				sql1.append(" and a.AKB020 = b.AKB020                  ");
				sql1.append(" where b.AAE100 = 1                       ");
				sql1.append(" and a.akc231 = '"+disease+"'             ");
				sql1.append(" and b.AAE040>=to_date('"+beginDate+"','yyyy-mm')");
				sql1.append(" and b.AAE040 < add_months(to_date('"+endDate+"','yyyy-mm'),1)   ) c ");
				sql1.append(" group by c.ym order by c.ym ");
				Map<String,String> groupMap=new HashMap<String,String>();
				Map<String,String> typeMap=new HashMap<String,String>();
				
				groupMap.put("xAxis","YM");
				groupMap.put("A1", "个人帐户");
				groupMap.put("A2", "统筹支付");
				groupMap.put("A3", "现金支付");
				groupMap.put("A4", "公务员补助支出金额");
				groupMap.put("A5", "救助金支出金额");
				List<Map<String,Object>> list1=findData(sql1.toString());
				if(!list1.isEmpty()){
					lineMap=findLineData(list1,groupMap,typeMap);
				}
				
				
				
				StringBuffer sql2 = new StringBuffer();
				sql2.append("  SELECT tsh.HOS_NAME,   ");
				sql2.append(" round(sum(f)/100000,2) tt, ");
				sql2.append(" round(sum(case   ");
				sql2.append(" when aka063 = '03' or aka063 = '02' or aka063 = '01' then ");
				sql2.append("  f    ");
				sql2.append(" else  ");
				sql2.append("  0     ");
				sql2.append("   end) / 100000,  ");
				sql2.append("   2) yp,   ");
				sql2.append(" round(sum(case  ");
				sql2.append(" when aka063 = '04' or aka063 = '22' or aka063 = '05' or   ");
				sql2.append("  aka063 = '06' or aka063 = '07' or aka063 = '12' or       ");
				sql2.append("  aka063 = '09' then    ");
				sql2.append("  f   ");
				sql2.append(" else  ");
				sql2.append("  0  ");
				sql2.append("   end) / 100000,  ");
				sql2.append("   2) jcjy, ");
				sql2.append(" round(sum(case    ");
				sql2.append(" when aka063 = '08' or aka063 = '10' then  ");
				sql2.append("  f  ");
				sql2.append(" else ");
				sql2.append("  0  ");
				sql2.append("   end) / 100000,  ");
				sql2.append("   2) ss,  ");
				sql2.append(" round(sum(case  ");
				sql2.append(" when aka063 = '91' or aka063 = '11' or aka063 = '32' or   ");
				sql2.append("  aka063 = '16' or aka063 = '15' or aka063 = '13' or       ");
				sql2.append("  aka063 = '34' or aka063 = '92' or aka063 = '17' or       ");
				sql2.append("  aka063 = '18' or aka063 = '14' then ");
				sql2.append("  f  ");
				sql2.append(" else  ");
				sql2.append("  0  ");
				sql2.append("   end) / 100000,  ");
				sql2.append("   2) qt  ");
				sql2.append("    FROM (select c.AKB020, c.AKA063, sum(b.AKC264) f       ");
				sql2.append("    from TS_KC21 a     ");
				sql2.append("   inner join kc24 b on a.akc190 = b.akc190                ");
				sql2.append("   inner join ts_kc22 c on a.akc190 = c.akc190   ");
				sql2.append("   and a.AKB020 = c.AKB020       ");
				sql2.append("   where b.AAE100 = 1       ");
				sql2.append("   and a.akc231 = '"+disease+"'             ");
				sql2.append(" 	and b.AAE040>=to_date('"+beginDate+"','yyyy-mm')");
				sql2.append(" 	and b.AAE040 < add_months(to_date('"+endDate+"','yyyy-mm'),1) ");
				sql2.append("   group by c.AKB020, c.AKA063     ");
				sql2.append("   order by c.AKB020, c.AKA063) t   ");
				sql2.append("   INNER JOIN TS_HOSP tsh ON tsh.HOS_CODE = t.AKB020       ");
				sql2.append("   group by tsh.HOS_NAME        ");
				dataList=dbm.executeQueryListHashMap(sql2.toString());
				
			}
			map.put("pieList",pieList);
			map.put("title", title);
			map.put("lineMap", lineMap);
			map.put("dataList", dataList);
			JSONObject json=new JSONObject().fromObject(map);
			PrintWriter pw = null;
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbm.close();
		}
	}
	/**  
	* @标题: findBiPieInfoMation  
	* @描述: 按疾病分类统计
	* @作者 LWH
	* @return
	*/  
	public void findBiTotalCostByDisease(){
		StringBuffer sql = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		sql.append(" SELECT ");
		sql.append(" dict_code code, ");
		sql.append(" dict_name NAME, ");
		sql.append(" ROUND (SUM(AKC255) / 100000, 2) a1, ");
		sql.append(" ROUND (SUM(AKC260) / 100000, 2) a2, ");
		sql.append(" ROUND (SUM(AKC261) / 100000, 2) a3, ");
		sql.append(" ROUND (SUM(AKC285) / 100000, 2) a4, ");
		sql.append(" ROUND (SUM(AKC264) / 100000, 2) y   ");
		sql.append(" FROM                  ");
		sql.append(" mv_month_sort03       ");
		sql.append(" WHERE ym >= '2016-01' ");
		sql.append(" and ym <= '2016-12'   ");
		sql.append(" GROUP BY              ");
		sql.append(" dict_code,            ");
		sql.append(" dict_name             ");
		sql.append(" ORDER BY              ");
		sql.append(" dict_code,            ");
		sql.append(" dict_name             ");
		Map<String,Object> pieMap = this.findPieDataByDisease(sql.toString());
		StringBuffer sql1 = new StringBuffer();
		sql1.append(" SELECT ym, ");
		sql1.append(" SUM(DECODE(aka123, '0', f, 0)) A, ");
		sql1.append(" SUM(DECODE(aka123, '1', f, 0)) b, ");
		sql1.append(" SUM(DECODE(aka123, '2', f, 0)) c, ");
		sql1.append(" SUM(DECODE(aka123, '3', f, 0)) D, ");
		sql1.append(" SUM(DECODE(aka123, '4', f, 0)) E, ");
		sql1.append(" SUM(DECODE(aka123, '11', f, 0)) f ");
		sql1.append(" FROM mv_month_sort04              ");
		sql1.append(" where ym >= '"+beginDate+"'  ");
		sql1.append(" and ym <= '"+endDate+"'  ");
		sql1.append(" GROUP BY ym                       ");
		sql1.append(" ORDER BY ym                       ");
		Map<String,String> groupMap=new HashMap<String,String>();
		Map<String,String> typeMap=new HashMap<String,String>();
		Map<String,Object> lineMap=new HashMap<String,Object>();
		groupMap.put("xAxis","YM");
		groupMap.put("A", "普通病种");
		groupMap.put("B", "门诊慢性病");
		groupMap.put("C", "工伤病种");
		groupMap.put("D", "生育病种");
		groupMap.put("E", "特殊病种");
		groupMap.put("F", "门诊慢性病大病");
		List<Map<String,Object>> list=findData(sql1.toString());
		if(!list.isEmpty()){
			lineMap=findLineData(list,groupMap,typeMap);
		}
		StringBuffer sql2 = new StringBuffer();
		sql2.append(" SELECT                                ");
		sql2.append("   dict_code code,                     ");
		sql2.append("   dict_name NAME,                     ");
		sql2.append("   ROUND (SUM(AKC255) / 100000, 2) a1, ");
		sql2.append("   ROUND (SUM(AKC260) / 100000, 2) a2, ");
		sql2.append("   ROUND (SUM(AKC261) / 100000, 2) a3, ");
		sql2.append("   ROUND (SUM(AKC285) / 100000, 2) a4, ");
		sql2.append("   ROUND (SUM(AKC264) / 100000, 2) y   ");
		sql2.append(" FROM                                  ");
		sql2.append(" mv_month_sort02                       ");
		sql2.append(" WHERE ym >= '"+beginDate+"'              ");
		sql2.append("    and ym <= '"+endDate+"'            ");
		sql2.append(" GROUP BY                              ");
		sql2.append("   dict_code,                          ");
		sql2.append("   dict_name                           ");
		sql2.append(" ORDER BY                              ");
		sql2.append("   dict_code,                          ");
		sql2.append("   dict_name                           ");
		Map<String,Object> pieMap1 = this.findPieDataByDisease(sql2.toString());
		StringBuffer sql3 = new StringBuffer();
		sql3.append(" SELECT ym,");
		sql3.append(" SUM(DECODE(aka122, '0100', f, 0)) A, ");
		sql3.append(" SUM(DECODE(aka122, '0200', f, 0)) b, ");
		sql3.append(" SUM(DECODE(aka122, '0300', f, 0)) c, ");
		sql3.append(" SUM(DECODE(aka122, '0400', f, 0)) D, ");
		sql3.append(" SUM(DECODE(aka122, '0500', f, 0)) E, ");
		sql3.append(" SUM(DECODE(aka122, '0600', f, 0)) f, ");
		sql3.append(" SUM(DECODE(aka122, '0700', f, 0)) G, ");
		sql3.append(" SUM(DECODE(aka122, '0800', f, 0)) H, ");
		sql3.append(" SUM(DECODE(aka122, '1000', f, 0)) i, ");
		sql3.append(" SUM(DECODE(aka122, '1100', f, 0)) j, ");
		sql3.append(" SUM(DECODE(aka122, '1200', f, 0)) K, ");
		sql3.append(" SUM(DECODE(aka122, '1300', f, 0)) l, ");
		sql3.append(" SUM(DECODE(aka122, '1400', f, 0)) M  ");
		sql3.append(" FROM MV_MONTH_SORT01                 ");
		sql3.append(" where ym >= '"+beginDate+"'                ");
		sql3.append(" and ym <= '"+endDate+"'                  ");
		sql3.append(" GROUP BY ym                          ");
		sql3.append(" ORDER BY ym                          ");
		Map<String,String> groupMap1=new HashMap<String,String>();
		Map<String,String> typeMap1=new HashMap<String,String>();
		Map<String,Object> lineMap1=new HashMap<String,Object>();
		groupMap1.put("xAxis","YM");
		groupMap1.put("A", "心血管系统疾病");
		groupMap1.put("B", "消化系统疾病");
		groupMap1.put("C", "代谢内分泌系统疾病");
		groupMap1.put("D", "造血系统疾病");
		groupMap1.put("E", "肾脏系统疾病");
		groupMap1.put("F", "神经系统疾病");
		groupMap1.put("G", "免疫系统疾病");
		groupMap1.put("H", "传染病");
		groupMap1.put("I", "恶性肿瘤");
		groupMap1.put("J", "精神病");
		groupMap1.put("K", "残疾");
		groupMap1.put("L", "泌尿系统疾病");
		groupMap1.put("M", "其他");
		List<Map<String,Object>> list1=findData(sql3.toString());
		if(!list.isEmpty()){
			lineMap1=findLineData(list1,groupMap1,typeMap1);
		}
		map.put("lineMap", lineMap);
		map.put("lineMap1", lineMap1);
		map.put("pieMap",pieMap);
		map.put("pieMap1",pieMap1);
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
	* @标题: findBiPersonalListByHospital  
	* @描述: 药品下钻机构下的药品(暂时去掉药品分类带逗号的数据)
	* @作者 LWH
	* @return
	*/ 
	public void findBiPersonalListByHospital(){
		StringBuffer sql = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		sql.append(" select b.akc516 tym, ");
		sql.append(" b.akc516 spm, ");
		sql.append(" b.AKA070 jx, ");
		sql.append(" b.AKA097 gg, ");
		sql.append(" b.AKA076 dw, ");
		sql.append(" b.akc225 dj, ");
		sql.append(" sum(b.akc226) num, ");
		sql.append(" sum(b.akc227) zj ");
		sql.append(" from MV_DRUG_DETAIL b ");
		sql.append(" where b.akb020 = '"+hospitalId+"' ");
		//sql.append(" and instr(name_chn, ',') = 0 ");
		if(nameChn!=null&&!nameChn.equals("")){
			sql.append(" and b.name_chn='"+nameChn+"' ");
		}
		sql.append(" and b.AAE040>='"+beginDate+"'  ");
		sql.append(" and b.AAE040 <= '"+endDate+"' ");
		//sql.append(" and b.AAE040>=to_date('"+beginDate+"','yyyy-mm') and b.AAE040 < add_months(to_date('"+endDate+"','yyyy-mm'),1)  ");
		sql.append(" group by b.akc516, b.AKA070, b.AKA097, b.AKA076,b.akc225 ");
		sql.append(" order by b.akc516, b.AKA070, b.AKA097, b.AKA076,b.akc225 ");
		
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append(" select * from ( ");
		sql1.append(" select name_chn, round(sum(b.akc226)/100000,2) sum, round(sum(b.akc227)/100000,2) zj ");
		sql1.append("  from MV_DRUG_SUM b ");
		sql1.append(" where b.akb020 = '"+hospitalId+"' ");
		sql1.append(" and AAE040 >= '"+beginDate+"' ");
		sql1.append(" and b.AAE040 <= '"+endDate+"' ");
		//sql1.append(" and b.AAE040>=to_date('"+beginDate+"','yyyy-mm') and b.AAE040 < add_months(to_date('"+endDate+"','yyyy-mm'),1)  ");
		sql1.append(" group by name_chn ");
		sql1.append(" order by sum desc) ");
		sql1.append(" where rownum <= 10 ");
		Map<String,String> groupMap=new HashMap<String,String>();
		Map<String,String> typeMap=new HashMap<String,String>();
		Map<String,Object> lineMap=new HashMap<String,Object>();
		groupMap.put("xAxis","NAME_CHN");
		groupMap.put("SUM", "使用量");
		groupMap.put("ZJ", "使用价格");
		typeMap.put("SUM", "0");
		typeMap.put("ZJ", "1");
		List<Map<String,Object>> list1=findData(sql1.toString());
		if(!list1.isEmpty()){
			lineMap=findLineData(list1,groupMap,typeMap);
		}
		StringBuffer sql2 = new StringBuffer();
		sql2.append(" select * from ts_dict where dict_type = 'NAME_CHN' ");
		List<Map<String,Object>> list2=findData(sql2.toString());
		try {
			DBManager_HSD dbm=new DBManager_HSD();
			list=dbm.executeQueryListHashMap(sql.toString());
			map.put("dataList", list);
			map.put("lineMap",lineMap);
			map.put("typeList", list2);
			JSONObject json=new JSONObject().fromObject(map);
			PrintWriter pw = null;
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
	* @标题: findBiPersonalListByNameChn
	* @描述: 药品下钻机构
	* @作者 LWH
	* @return
	*/ 
	public void findBiPersonalListByNameChn(){
		StringBuffer sql = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		sql.append(" select sum(AKC264) f, ");
		sql.append(" (sum(AKC226) || '(' || nvl(max(AKA076), '支') || ')') y, ");
		sql.append(" b.hos_name,a.akb020 hos_code  ");
		sql.append(" from mv_fee a,ts_hosp b ");
		sql.append(" where name_chn like '%"+nameChn+"%' ");
		sql.append(" and a.akb020 = b.hos_code ");
		sql.append(" and a.AAE040>=to_date('"+beginDate+"','yyyy-mm') and a.AAE040 < add_months(to_date('"+endDate+"','yyyy-mm'),1)  ");
		sql.append(" group by hos_name,a.akb020 ");
		sql.append(" order by f desc ");
		StringBuffer sql2 = new StringBuffer();
		sql2.append(" select * from ts_dict where dict_type = 'NAME_CHN' ");
		List<Map<String,Object>> list2=findData(sql2.toString());
		DBManager_HSD dbm=new DBManager_HSD();
		try {
			list=dbm.executeQueryListHashMap(sql.toString());
			map.put("dataList", list);
			map.put("typeList", list2);
			JSONObject json=new JSONObject().fromObject(map);
			PrintWriter pw = null;
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbm.close();
		}
	}
	/**  
	* @标题: findBiPersonalLineByNameChn
	* @描述: 药品下钻机构
	* @作者 LWH
	* @return
	*/ 
	public void findBiPersonalLineByNameChn(){
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer sql1 = new StringBuffer();
		sql1.append(" select round(SUM(AKC226)/100000,2) AS sum, round(SUM(AKC227)/100000,2) AS price, datetime AS datetime ");
		sql1.append(" from mv_month ");
		sql1.append(" where datetime >= '"+beginDate+"' ");
		sql1.append(" and datetime <= '"+endDate+"' ");
		sql1.append(" AND NAME_CHN LIKE '%"+nameChn+"%' ");
		sql1.append(" GROUP BY datetime ");
		sql1.append(" ORDER BY datetime ");
		 
		Map<String,String> groupMap=new HashMap<String,String>();
		Map<String,String> typeMap=new HashMap<String,String>();
		Map<String,Object> lineMap=new HashMap<String,Object>();
		groupMap.put("xAxis","DATETIME");
		groupMap.put("SUM", "使用量");
		groupMap.put("PRICE", "使用价格");
		typeMap.put("SUM", "0");
		typeMap.put("PRICE", "1");
		List<Map<String,Object>> list1=findData(sql1.toString());
		if(!list1.isEmpty()){
			lineMap=findLineData(list1,groupMap,typeMap);
		}
		try {
			map.put("lineMap", lineMap);
			JSONObject json=new JSONObject().fromObject(map);
			PrintWriter pw = null;
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
	* @标题: findPieDataByDisease  
	* @描述: 饼图
	* @作者 LWH
	* @param sql
	* @return
	*/  
	private Map<String,Object> findPieDataByDisease(String sql){
		DBManager_HSD dbm=new DBManager_HSD();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<BiPieModel> pieList=new ArrayList<BiPieModel>();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			//查询数据
			list=dbm.executeQueryListHashMap(sql);
			DecimalFormat df = new DecimalFormat("#.00");
			Double sum = 0.0;
			for(Map<String,Object> map1:list){
				BigDecimal b1 = new BigDecimal(Double.valueOf(sum));
				BigDecimal b2 = new BigDecimal(Double.valueOf(Double.parseDouble(map1.get("Y").toString())));
				sum = b1.add(b2).doubleValue();
				BiPieModel biModel = new BiPieModel();
				biModel.setName(map1.get("NAME").toString());
				biModel.setY(Double.parseDouble(map1.get("Y").toString()));
				pieList.add(biModel);
			}
			map.put("title", "总费支出："+df.format(sum));
            map.put("pieList", pieList);
            map.put("dataList", list);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return map;
	}
	
	
	/**  
	* @标题: findPieData  
	* @描述: TODO饼图
	* @作者 EbaoWeixun
	* @param sql
	* @return
	*/  
	private Map<String,Object> findPieData(String sql){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String, Object>();
	
		try{
			//查询数据
			List<BiPieModel> list=dbm.getObjectList(BiPieModel.class, sql);
			map.put("title", "123");
            map.put("data", list);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: findLineData  
	* @描述: TODO折线图柱状图条形图
	* @作者 EbaoWeixun
	* @param sql 查询语句
	*        groupMap  x轴显示的信息: key："xAxis"---value：查询的列         其他：key:查询的列---value:对应页面显示的名称    
	*        typeMap   数据对应的图形:key:查询的列 ---value:图形0折线 1柱状图 2条形图               null默认是折线图或由页面整体配置
	*  
	* @return
	*/  
	private Map<String,Object> findLineData(List<Map<String,Object>> list,Map<String,String> groupMap,Map<String,String> typeMap){
		
		Map<String,Object> map=new HashMap<String,Object>();
	    List<Map<String,Object>> seriesList=new ArrayList<Map<String,Object>>();
	    List<String> xList=new ArrayList<String>();
		
			Map<String,List<Double>> groupListMap=new HashMap<String,List<Double>>();
			Map<String,Map<String,Object>> groupMMap=new HashMap<String,Map<String,Object>>();
			if(groupMap!=null&&!groupMap.isEmpty()){
				for(String key:groupMap.keySet()){
					if(!"xAxis".equals(key)){
						List<Double> groupList=new ArrayList<Double>();
						Map<String,Object> sm=new HashMap<String,Object>();
						groupListMap.put(key, groupList);
						groupMMap.put(key, sm);
					}
					
				}
			}
			
			
			if(!list.isEmpty()){
				for(Map<String,Object> mm:list){
					if(groupMap!=null&&groupMap.get("xAxis")!=null){
					    xList.add(mm.get(groupMap.get("xAxis")).toString());
					}
					for(String key:groupListMap.keySet()){
					    groupListMap.get(key).add(Double.parseDouble(mm.get(key).toString()));
					}
					
				}
				for(String key:groupMMap.keySet()){
					groupMMap.get(key).put("data", groupListMap.get(key));
					groupMMap.get(key).put("name",groupMap.get(key));
					if(typeMap!=null&&typeMap.get(key)!=null){
						if("0".equals(typeMap.get(key))){
							groupMMap.get(key).put("type","spline");
						}else if("1".equals(typeMap.get(key))){
							groupMMap.get(key).put("type","column");
						}else{
							groupMMap.get(key).put("type","bar");
						}
					}
					seriesList.add(groupMMap.get(key));
				}
				
			}
			map.put("series", seriesList);
			map.put("xAxis", xList);
			
		
		return map;
	}
    /**  
    * @标题: findData  
    * @描述: TODO查询数据
    * @作者 EbaoWeixun
    * @param sql
    * @return
    */  
    private List<Map<String,Object>> findData(String sql){
    	List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    	DBManager_HSD dbm=new DBManager_HSD();
    	try{
    		list=dbm.executeQueryListHashMap(sql);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		dbm.close();
    	}
    	return list;
    }
	/**  
	* @标题: findBiPie  
	* @描述: TODO饼图测试
	* @作者 EbaoWeixun
	*/  
	public void findBiPie(){
		Map<String,Object> map=new HashMap<String,Object>();
		/*模拟数据*/
		BiPieModel bm1=new BiPieModel();
		BiPieModel bm2=new BiPieModel();
		BiPieModel bm3=new BiPieModel();
		bm1.setName("个账");
		bm2.setName("自费");
		bm3.setName("统筹");
		bm1.setSelected(true);
		bm1.setSliced(true);
		bm1.setY(100d);
		bm2.setY(200d);
		bm3.setY(300d);
		List<BiPieModel> list=new ArrayList<BiPieModel>();
		list.add(bm1);
		list.add(bm2);
		list.add(bm3);
		/*模拟数据结束*/
		map.put("data", list);
		map.put("title", "600");
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
	* @标题: findBiLine  
	* @描述: TODO折线测试
	* @作者 EbaoWeixun
	*/  
	public void findBiLine(){
		
		Map<String,String> groupMap=new HashMap<String,String>();
		Map<String,String> typeMap=new HashMap<String,String>();
		
		groupMap.put("xAxis","USER_NAME");
		groupMap.put("SG", "身高");
		
		typeMap.put("SG", "0");
		String sql="select * from(select t.user_name,max(t.r2_3) sg from H_PHYSICAL_EXAMINATION t group by t.user_name order by max(t.r2_3) desc) where rownum<=10";
		List<Map<String,Object>> list=findData(sql);
		Map<String,Object> map=findLineData(list,groupMap,typeMap);
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
	* @标题: findBiTotalCost  
	* @描述: TODO查询总费用
	* @作者 EbaoWeixun
	*/  
	public void findBiTotalCost(){
		Map<String,String> groupMap=new HashMap<String,String>();
		Map<String,String> typeMap=new HashMap<String,String>();
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> pieMap=new HashMap<String,Object>();
		groupMap.put("xAxis","YM");
		groupMap.put("A", "个人账户");
		groupMap.put("B", "统筹支付");
		groupMap.put("C", "现金支付");
		groupMap.put("D", "公务员补助支出");
		groupMap.put("E", "救助金支出");
		
		String sql="select round(sum(AKC255)/100000,2) a , round(sum(AKC260)/100000,2) b, round(sum(AKC261)/100000,2) c, round(sum(AKC285)/100000,2) d,round(sum(akc283)/100000,2) e,round(sum(AKC264)/100000,2) f, ym from "
		+"(select AKC255, AKC260, AKC261, AKC264, AKC285, akc283, to_char(AAE040,'yyyy-mm') ym from kc24 where AAE100 = 1 "
        +" and AAE040>=to_date('"+beginDate+"','yyyy-mm') and AAE040<add_months(to_date('"+endDate+"','yyyy-mm'),1 ) )group by ym order by ym";
		List<Map<String,Object>> list=findData(sql);
		if(!list.isEmpty()){
			for(Map<String,Object> m:list){
				Double.parseDouble(m.get("F").toString());
			}
			map=findLineData(list,groupMap,typeMap);
			groupMap.put("title", "F");
			groupMap.remove("xAxis");
			pieMap=findPieDataByLine(list, groupMap);
			map.put("pie", pieMap);
		}
		
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
	* @标题: findPieDataByLine  
	* @描述: TODO
	* @作者 EbaoWeixun
	* @param list
	* @param groupMap
	* @return
	*/  
	private Map<String,Object> findPieDataByLine(List<Map<String,Object>> list,Map<String,String> groupMap){
		Map<String,Object> map=new HashMap<String,Object>();
	
		List<BiPieModel> pieList=new ArrayList<BiPieModel>();
		Double title=0.0;
		for(String key:groupMap.keySet()){
			if(!"title".equals(key)){
				BiPieModel p=new BiPieModel();
				p.setName(groupMap.get(key));
				p.setY(0D);
				p.setId(key);
				pieList.add(p);
			}
		}
		if(!list.isEmpty()){
			for(int i=0;i<pieList.size();i++){
				for(Map<String,Object> m:list){
				    pieList.get(i).setY(CommUtil.add(pieList.get(i).getY(),Double.parseDouble(m.get(pieList.get(i).getId()).toString())));
				    if(groupMap.get("title")!=null&&i==0){
						title=CommUtil.add(title,Double.parseDouble(m.get(groupMap.get("title")).toString()));
					}
	    		} 
				
			}
			
		}
		map.put("title", title);
		map.put("data", pieList);
		return map;
	}
	/**  
	* @标题: findDrugTopThree  
	* @描述: TODO药品按医院前三
	* @作者 EbaoWeixun
	*/  
	public void findDrugTopThree(){
		DBManager_HSD dbm=new DBManager_HSD();
		Map<String,Object> allMap=new HashMap<String,Object>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try{
			String sql="";
			if("1".equals(isFirst)){
			    sql="select * "
					  +"from (select round(sum(m.AKC264)/100000,2) y, m.AKB020 id,p.hos_name "
					          +"from MV_FEE m "
					          +"inner join ts_hosp p on p.hos_code=m.akb020 "
					          +"where m.aae040 >=to_date('"+beginDate+"','yyyy-mm') and  m.aae040<add_months(to_date('"+endDate+"','yyyy-mm'),1) "
					        +" group by m.AKB020,p.hos_name "
					         +"order by sum(m.AKC264) desc) "
					+" where rownum < 4";
			}else{
				sql="select y,id,bi_name hos_name from ts_bisj where fun_status=1 order by sort_status";
			}
			List<Map<String,Object>> hos_list=dbm.executeQueryListHashMap(sql);

			String hos_code="";
			if(!hos_list.isEmpty()){
			
				double bg=0;
				for(Map<String,Object> map:hos_list){
					hos_code=map.get("ID").toString();
					if("1".equals(isFirst)){
						sql="select * "
								  +"from (select round(sum(AKC264) / 100000, 2) y,name_chn name "
								  +"        from MV_FEE "
								    +"      where AKB020 = '"+hos_code+"' "
								      +"    and aae040>= to_date('"+beginDate+"','yyyy-mm') "
								        +"  and aae040< add_months(to_date('"+endDate+"','yyyy-mm'),1) "
								         +"group by name_chn "
								         +"order by y desc) "
								 +"where rownum < 6";
					}else{
						sql="select y,bi_name name from ts_bisj where id='"+hos_code+"' and fun_status=2 order by sort_status ";
					}
					
					List<BiPieModel> pieList=dbm.getObjectList(BiPieModel.class, sql);

					List<BiPieModel> pieList1=new ArrayList<BiPieModel>();
					for(BiPieModel bp:pieList){
						bg=CommUtil.add(bg, bp.getY());
						String[] types=bp.getName().split(",");
						if(types.length>1){
							for(String type:types){
								BiPieModel pp=new BiPieModel();
								pp.setName(type);
								pp.setY(bp.getY());
								pieList1.add(pp);
							}
						}else{
							pieList1.add(bp);
						}
					}
					Map<String,Object> pieMap=new HashMap<String, Object>();
					pieMap.put("title", map.get("Y"));
					BiPieModel pie=new BiPieModel();
					pie.setName("其他");
					pie.setY(CommUtil.sub(Double.parseDouble(map.get("Y").toString()),bg));
					pieList1.add(pie);
					pieMap.put("data", pieList1);
					pieMap.put("name", map.get("HOS_NAME") );
					list.add(pieMap);
				}
			}
			allMap.put("topThree", list);
			//-----------------------------药品使用总价TOP10----------------------------------
			Map<String,String> groupMap=new HashMap<String,String>();
			Map<String,String> typeMap=new HashMap<String,String>();
			
			groupMap.put("xAxis","YP");
			groupMap.put("F", "费用");
			
			typeMap.put("F", "1");
			if("1".equals(isFirst)){
				sql="select * "
			               +"from (select round(sum(AKC264) / 100000, 2) f, akc516 yp "
			               +"from mv_fee "
			               +"where AAE040 >= to_date('"+beginDate+"', 'yyyy-mm') "
			               +"and AAE040 <= add_months(to_date('"+endDate+"', 'yyyy-mm'), 1) "
			               +"group by akc516 "
			               +"order by f desc) "
			               +"where rownum < 11";
			}else{
				sql="select y f,bi_name yp from ts_bisj where fun_status=3 order by sort_status ";
			}
			List<Map<String,Object>> drugList=dbm.executeQueryListHashMap(sql);

			Map<String,Object> drugMap=findLineData(drugList,groupMap,typeMap);
			allMap.put("topTen", drugMap);
			
			//-----------------------------药品分类统计----------------------------------
			
			if("1".equals(isFirst)){
				sql="select round(sum(AKC264) / 100000, 2) f, "
					       +"round(sum(AKC226) / 100000, 2) y, nvl(max(AKA076), '支') d, "
					       +"name_chn "
					  +"from MV_FEE "
					 +"where AAE040 >= to_date('"+beginDate+"', 'yyyy-mm') "
					  +" and AAE040 <= add_months(to_date('"+endDate+"', 'yyyy-mm'), 1) "
					 +"group by name_chn "
					 +"order by f desc";
			}else{
				sql="select a1 f,y,a2 d,bi_name name_chn from ts_bisj where fun_status=4 order by sort_status ";
			}
			List<Map<String,Object>> typeList=dbm.executeQueryListHashMap(sql);
		
			List<Map<String,Object>> typeList1=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> typeList2=new ArrayList<Map<String,Object>>();
			if(!typeList.isEmpty()){
				for(Map<String,Object> typeMap1:typeList){
					String[] types=typeMap1.get("NAME_CHN").toString().split(",");
					if(types.length>1){
						typeList1.add(typeMap1);
					}else{
						typeList2.add(typeMap1);
					}
				}
				
				for(Map<String,Object> m:typeList1){
					String[] types=m.get("NAME_CHN").toString().split(",");
					for(String type:types){
						boolean flag=true;
						for(int i=0;i<typeList2.size();i++){
							if(typeList2.get(i).get("NAME_CHN").toString().equals(type)){
								typeList2.get(i).put("F", CommUtil.add( Double.parseDouble(typeList2.get(i).get("F").toString()),Double.parseDouble(m.get("F").toString())));
								typeList2.get(i).put("Y", CommUtil.add( Double.parseDouble(typeList2.get(i).get("Y").toString()),Double.parseDouble(m.get("Y").toString())));
								flag=false;
							}
						}
						if(flag){
							Map<String,Object> mm=new HashMap<String,Object>();
							mm.put("NAME_CHN", type);
							mm.put("F", m.get("F"));
							mm.put("Y", m.get("Y"));
							mm.put("D", m.get("D"));
							typeList2.add(mm);
						}
					}
				}
			}
			allMap.put("typeList", typeList2);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		JSONObject json=new JSONObject().fromObject(allMap);
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
	* @标题: findDrugByType  
	* @描述: TODO药品分类统计
	* @作者 EbaoWeixun
	*/  
	public void findDrugByType(){
		String sql="select sum(a.AKC264) f, (sum(AKC226)||'('||nvl(max(AKA076), '支')||')') y, b.name_chn from kc24  a "
+"inner join ts_kc22 b on a.akc190=b.akc190 and a.AKB020 = b.AKB020 "
+"where a.AAE100 = 1 and b.AKA125 = '0' and  b.name_chn is not null  and b.AAE040>=to_date('"+beginDate+"','yyyy-mm') and b.AAE040< add_months(to_date('"+endDate+"','yyyy-mm'),1) "
+"group by b.name_chn "
+"order by f desc";
		List<Map<String,Object>> list=findData(sql);
		for(Map<String,Object> map:list){
			map.get("NAME_CHN");
		}
		JSONArray json=new JSONArray().fromObject(list);
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
	* @标题: findByDiseaseType  
	* @描述: TODO疾病类型查询门诊
	* @作者 EbaoWeixun
	*/  
	public void findByDiseaseTypeMz(){
		
		//门诊
		String sql="SELECT "
	              +"K1.aka121 jb, "//疾病名称
	              +"round(SUM(K4.AKC264)/10000,2) AS ZFY, "// 总额,
	              +"round(SUM(K4.AKC255)/10000,2) AS GZ, "// 个人帐户,
	              +"round(SUM(K4.AKC261)/10000,2) AS ZF, "// 自费,
	              +"round(SUM(K4.AKC260)/10000,2) AS TC " //统筹,

	              +"FROM "
	              +"TS_KC21 K1 "
	              +"INNER JOIN KC24 K4 ON K1.AKC190 = K4.AKC190 "
	              +"WHERE ";
		if("0".equals(flag)){
			sql+="K1.AKA123 = '"+disType+"' ";
		}else{
			sql+="K1.AKA122 = '"+disType+"' ";
		}
	    sql+=" AND K4.AAE040>=to_date('"+beginDate+"','yyyy-mm') AND k4.AAE040<add_months(to_date('"+endDate+"','yyyy-mm'),1)";          
	    sql+="AND (K1.AKA130 = '11' OR K1.AKA130 = '15') "
	              +"GROUP BY "
	              +"K1.aka121 "
	              +"ORDER BY "
		          +"ZFY DESC";
	
			
		DBManager_HSD dbm=new DBManager_HSD();
        PageModel pageModel=new PageModel();
        Map<String,String> groupMap=new HashMap<String,String>();
        Map<String,Object> map=new HashMap<String,Object>();
        try{
        	pageModel=dbm.getMapByPage(sql, page, rows);
        	groupMap.put("xAxis", "JB");
        	groupMap.put("GZ", "个账");
        	groupMap.put("TC", "统筹");
        	groupMap.put("ZF", "自费");
        	map=findLineData(pageModel.getList(), groupMap,null );
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	dbm.close();
        }
		JSONObject json=new JSONObject().fromObject(pageModel);
		JSONObject job=new JSONObject().fromObject(map);
		json.put("tb", job);
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
    public void findByDiseaseTypeZy(){
		
		//住院
		String sql="SELECT K1.aka121 JB, "
	              +" CAST(SUM(K4.AKC264)/COUNT(K1.aka121) as decimal(10,2)) AS ZYFY, "
	              +" CAST(SUM(K4.AKC336)/COUNT(K1.aka121) as decimal(10,2)) AS ZYTS "
                  +"FROM "
	              +"TS_KC21 K1 "
                  +"INNER JOIN KC24 K4 ON K1.AKC190 = K4.AKC190 "
                  +"WHERE";
		if("0".equals(flag)){
			sql+=" K1.AKA123 = '"+disType+"' ";
		}else{
			sql+=" K1.AKA122 = '"+disType+"' ";
		}
	    sql+=" AND K4.AAE040>=to_date('"+beginDate+"','yyyy-mm') AND k4.AAE040<add_months(to_date('"+endDate+"','yyyy-mm'),1) ";          
	    sql+="AND K1.AKA130 = '21' "
	              +"GROUP BY "
	              +"K1.aka121 "
	              +"ORDER BY "
		          +"ZYFY DESC";
	
			
		DBManager_HSD dbm=new DBManager_HSD();
        PageModel pageModel=new PageModel();
        Map<String,String> groupMap=new HashMap<String,String>();
        Map<String,String> typeMap=new HashMap<String,String>();
        Map<String,Object> map=new HashMap<String,Object>();
        try{
        	pageModel=dbm.getMapByPage(sql, page, rows);
        	groupMap.put("xAxis", "JB");
        	groupMap.put("ZYFY", "费用");
        	groupMap.put("ZYTS", "住院天数");
        	typeMap.put("ZYFY","1" );
        	typeMap.put("ZYTS","0" );
        	map=findLineData(pageModel.getList(), groupMap,typeMap );
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	dbm.close();
        }
		JSONObject json=new JSONObject().fromObject(pageModel);
		JSONObject job=new JSONObject().fromObject(map);
		json.put("tb", job);
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
	* @标题: findDisOrSysType  
	* @描述: TODO查询疾病类型或系统类型
	* @作者 EbaoWeixun
	*/  
	public void findDisOrSysType(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		DBManager_HSD dbm=new DBManager_HSD();
		String type="0".equals(flag)?"AKA123":"AKA122";
		try{
			String sql="select dict_code code,dict_name name from ts_dict where dict_type='"+type+"'";
			list=dbm.executeQueryListHashMap(sql);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		JSONArray json=new JSONArray().fromObject(list);
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
	public void findCostByType(){
		PageModel pageModel=new PageModel();
		DBManager_HSD dbm=new DBManager_HSD();
		List<BiPieModel> list=new ArrayList<BiPieModel>();
		Map<String,Object> rtMap=new HashMap<String,Object>();
		String total="";
		String type="a";
		try{
			if("1".equals(flag)){
				type="a-b-c";
			}else if("2".equals(flag)){
				type="b+c";
			}
			String sql=" select round(sum("+type+")/100000,2) tt "
            +" ,round(sum(case  when aka063='03' or aka063='02' or aka063='01' then "+type+" else 0 end)/100000,2) yp"
            +" ,round(sum(case  when aka063='04' or aka063='22' or aka063='05' or aka063='06' or aka063='07' or aka063='12' or aka063='09' then "+type+" else 0 end)/100000,2) jcjy"
            +" ,round(sum(case  when aka063='08' or aka063='10'  then "+type+" else 0 end)/100000,2) ss"
            +" ,round(sum(case  when aka063='91' or aka063='11' or aka063='32' or aka063='16' or aka063='15' or aka063='13' or aka063='34' or aka063='92' or aka063='17' or aka063='18' or aka063='14' then "+type+" else 0 end)/100000,2) qt"
            +" from MV_GROUP_AKA"
            +" where ym>='"+beginDate+"' and ym<='"+endDate+"' ";
			Map<String,Object> map=dbm.executeQueryHashMap(sql);
			BiPieModel bp1=new BiPieModel();
			BiPieModel bp2=new BiPieModel();
			BiPieModel bp3=new BiPieModel();
			BiPieModel bp4=new BiPieModel();
			bp1.setY(Double.parseDouble(map.get("YP").toString()));
			bp2.setY(Double.parseDouble(map.get("JCJY").toString()));
			bp3.setY(Double.parseDouble(map.get("SS").toString()));
			bp4.setY(Double.parseDouble(map.get("QT").toString()));
			bp1.setName("药品");
			bp2.setName("检查检验");
			bp3.setName("手术");
			bp4.setName("其他");
			list.add(bp1);
			list.add(bp2);
			list.add(bp3);
			list.add(bp4);
			total=map.get("TT").toString();
			sql=sql.replace("select", "select ym,")+" group by ym order by ym";
			pageModel=dbm.getMapByPage(sql, page, rows);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		rtMap.put("data", list);
		rtMap.put("title", total);
		JSONObject json=new JSONObject().fromObject(pageModel);
		JSONObject t=new JSONObject().fromObject(rtMap);

		json.put("pie", t);
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
	public void findHosTypeCost(){
		Map<String,Object> map=new HashMap<String,Object>();
		DBManager_HSD dbm=new DBManager_HSD();
		String type="ak.a";
		Map<String,Object> fMap=new HashMap<String, Object>();
        Map<String,List> allChildMap=new HashMap<String, List>();
		try{
			if("1".equals(flag)){
				type="ak.a-ak.b-ak.c";
			}else if("2".equals(flag)){
				type="ak.b+ak.c";
			}
			String sql="select h.hos_name name,kk.* from(select ak.AKB020 id, round(sum("+type+")/100000,2) y "
	                  +" from MV_GROUP_AKB ak "
	                  +" where  ak.aae040>= to_date('"+beginDate+"','yyyy-mm') and ak.aae040<add_months(to_date('"+endDate+"','yyyy-mm'),1) " 
	                  +" group by ak.akb020 "
	                  +" order by y desc "
	                  +" ) kk inner join ts_hosp h on h.hos_code=kk.id  where rownum<6 ";
			List<BiPieModel> hosList=dbm.getObjectList(BiPieModel.class, sql);
			if(!hosList.isEmpty()){
				Double total=0D;
				type="a";
				if("1".equals(flag)){
					type="a-b-c";
				}else if("2".equals(flag)){
					type="b+c";
				}
				for(BiPieModel bp:hosList){
					total=CommUtil.add(total, bp.getY());
					List<BiPieModel> childList=new ArrayList<BiPieModel>();
					sql="select round(sum("+type+")/100000,2) tt "
					+" ,round(sum(case  when aka063='03' or aka063='02' or aka063='01' then "+type+" else 0 end)/100000,2) yp"
		            +" ,round(sum(case  when aka063='04' or aka063='22' or aka063='05' or aka063='06' or aka063='07' or aka063='12' or aka063='09' then "+type+" else 0 end)/100000,2) jcjy"
		            +" ,round(sum(case  when aka063='08' or aka063='10'  then "+type+" else 0 end)/100000,2) ss"
		            +" ,round(sum(case  when aka063='91' or aka063='11' or aka063='32' or aka063='16' or aka063='15' or aka063='13' or aka063='34' or aka063='92' or aka063='17' or aka063='18' or aka063='14' then "+type+" else 0 end)/100000,2) qt "
		            +" from MV_GROUP_SORT "
		            +" where akb020='"+bp.getId()+"' and ym <= '"+endDate+"' and ym >= '"+beginDate+"' ";
					Map<String,Object> hosMap=dbm.executeQueryHashMap(sql);
					BiPieModel bp1=new BiPieModel();
					BiPieModel bp2=new BiPieModel();
					BiPieModel bp3=new BiPieModel();
					BiPieModel bp4=new BiPieModel();
					bp1.setY(Double.parseDouble(hosMap.get("YP").toString()));
					bp2.setY(Double.parseDouble(hosMap.get("JCJY").toString()));
					bp3.setY(Double.parseDouble(hosMap.get("SS").toString()));
					bp4.setY(Double.parseDouble(hosMap.get("QT").toString()));
					bp1.setName("药品");
					bp2.setName("检查检验");
					bp3.setName("手术");
					bp4.setName("其他");
					childList.add(bp1);
					childList.add(bp2);
					childList.add(bp3);
					childList.add(bp4);
					allChildMap.put(bp.getName(),childList);
				}
				fMap.put("data", hosList);
				fMap.put("title", total);
			}
			map.put("main", fMap);
			map.put("child",allChildMap);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
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
	public void findHosTypeCostByPage(){
		PageModel pageModel=new PageModel();
		DBManager_HSD dbm=new DBManager_HSD();
		try{
			String type="a";
			if("1".equals(flag)){
				type="a-b-c";
			}else if("2".equals(flag)){
				type="b+c";
			}
			String sql="select akb020 hos_id,hos_name,round(sum("+type+")/100000,2) tt "
					+" ,round(sum(case  when aka063='03' or aka063='02' or aka063='01' then "+type+" else 0 end)/100000,2) yp"
		            +" ,round(sum(case  when aka063='04' or aka063='22' or aka063='05' or aka063='06' or aka063='07' or aka063='12' or aka063='09' then "+type+" else 0 end)/100000,2) jcjy"
		            +" ,round(sum(case  when aka063='08' or aka063='10'  then "+type+" else 0 end)/100000,2) ss"
		            +" ,round(sum(case  when aka063='91' or aka063='11' or aka063='32' or aka063='16' or aka063='15' or aka063='13' or aka063='34' or aka063='92' or aka063='17' or aka063='18' or aka063='14' then "+type+" else 0 end)/100000,2) qt "
		            +" from MV_GROUP_SORT "
		            +" inner join ts_hosp  on hos_code=akb020 "
		            +" where ym <= '"+endDate+"' and ym >= '"+beginDate+"' "
			        +"group by akb020,hos_name "
                    +"order by tt desc";
			pageModel=dbm.getMapByPage(sql, page, rows);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		JSONObject json=new JSONObject().fromObject(pageModel);
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
	public static void main(String[] args) {
	
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getHosType() {
		return hosType;
	}
	public void setHosType(String hosType) {
		this.hosType = hosType;
	}
	public String getDisType() {
		return disType;
	}
	public void setDisType(String disType) {
		this.disType = disType;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getNameChn() {
		return nameChn;
	}
	public void setNameChn(String nameChn) {
		this.nameChn = nameChn;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getIsFirst() {
		return isFirst;
	}
	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}
	
}
