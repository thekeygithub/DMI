package com.dhcc.ts.yzd;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.mtsService.MTSUtil;
import com.dhcc.ts.mtsService.ResultModel;

public class YzdDao {
	private SimpleDateFormat sf = new SimpleDateFormat("dd-MM月-yyHH.mm.ss");
	private SimpleDateFormat sff = new SimpleDateFormat("2016-MM-dd HH:mm:ss");
	/**
	 * @描述：医嘱单明细
	 * @作者：SZ
	 * @时间：2017年2月28日 下午7:58:57
	 * @param zyh
	 * @param type
	 *            1:临时医嘱单 2:长期医嘱单
	 * @return
	 */
	public List<YzdModel> queryYzd(String zyh, String type, String yzsj) {
		boolean result = "1".equals(type);
		String sql = "SELECT DISTINCT HA.CXM AS hzmc,HA.CZYKS AS ksmc,HA.CZXHS AS zxhs," + "HA.CYPMC AS yzmc,HA.CYPGG AS jx,"
				+ "SUBSTR(HA.DDYSJ,0,19) AS yztime,"
				+ "TO_CHAR(TO_DATE(substr(HA.DDYSJ,0,19), 'yyyy-mm-dd hh24:mi:ss')+3/24 ,'yyyy-mm-dd HH24:MI:SS') AS zxtime,"
				+ "HA.CGCYS AS ysmc,FH.HOS_NAME AS yymv ";
		if (result) {
			sql += " ,'临时医嘱单'  AS yzdmc ";
		} else {
			sql += " ,'长期医嘱单'  AS yzdmc ";
		}
		if(yzsj.length()>25){
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM月-yyHH.mm.ss");
	    	SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		sql += " FROM H_ADVICE HA " + "LEFT JOIN H_VISIT HV ON HV.CZYH = HA.CZYH "
				+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HV.CORP_ID " 
				+ "WHERE HA.CZYH = '" + zyh + "' "
				+ "AND TO_DATE(substr(HA.DDYSJ,0,19), 'yyyy-mm-dd hh24:mi:ss')>= TO_DATE('"+yzsj+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') "
				+ "AND TO_DATE(substr(HA.DDYSJ,0,19), 'yyyy-mm-dd hh24:mi:ss')< TO_DATE('"+yzsj+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')+1";
		if (result) {
			sql += "  AND HA.CYZDLX = '住院临时医嘱单' ";
		} else {
			sql += "  AND HA.CYZDLX = '住院长期医嘱单' ";
		}

		sql += " ORDER BY yztime,yzmc";

		DBManager_HIS dbm = new DBManager_HIS();
		List<YzdModel> listmodel = dbm.getObjectList(YzdModel.class, sql);
		dbm.close();
		return listmodel;
	}

	/**
	 * @描述：医嘱单列表
	 * @作者：SZ
	 * @时间：2017年3月1日 下午12:55:07
	 * @param zyh
	 * @param type
	 *            1:临时医嘱单 2:长期医嘱单
	 * @return
	 * SELECT yztime FROM (SELECT DISTINCT substr(HA.DDYSJ,0,10) AS yztime
	 *  FROM H_ADVICE HA WHERE HA.CZYH = '16091525' AND HA.CYZDLX = '住院临时医嘱单') a ORDER BY yztime DESC

	 */
	public List<YzdModel> queryYzdList(String zyh, String type) {
		String sql = "SELECT yztime FROM (SELECT DISTINCT substr(HA.DDYSJ,0,10) AS yztime "
				+ "FROM H_ADVICE HA "
				+ "WHERE HA.CZYH = '"+zyh+"'";
		if ("1".equals(type)) {
			sql += " AND HA.CYZDLX = '住院临时医嘱单' ";
		} else {
			sql += " AND HA.CYZDLX = '住院长期医嘱单' ";
		}
		sql += ") TAMPA ORDER BY yztime DESC";

		DBManager_HIS dbm = new DBManager_HIS();
		List<YzdModel> listmodel = dbm.getObjectList(YzdModel.class, sql);
		
//		Set<String> setmodel = new HashSet<String>();
//		
//		for(YzdModel model : listmodel){
//			setmodel.add(model.getYztime());
//		}
//		
//		List<YzdModel> list = new ArrayList<YzdModel>();
//		YzdModel model = null;
//		for(String a : setmodel){
//			model = new YzdModel();
//			model.setYztime(a);
//			list.add(model);
//		}
		dbm.close();
		return listmodel;
	}
	
	/**
	 * @描述：查询电子病历页签时间
	 * @作者：SZ
	 * @时间：2017年3月3日 下午3:02:25
	 * @param zyh
	 * @return
	 */
	public Map<String, String> querySj(String zyh){
		Map<String, String> map = new HashMap<String, String>();
		String sql = "SELECT SUBSTR(DRYSJ, 0, 10) AS SJ,SUBSTR(DCYSJ, 0, 10) AS CYSJ FROM H_VISIT WHERE CZYH='"+zyh+"'";
		DBManager_HIS dbm = new DBManager_HIS();
		try {
			map = dbm.executeQueryHashMap(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbm.close();
		}
		return map;
	}
	
	/**
	 * @描述：病程记录查询
	 * @作者：SZ
	 * @时间：2017年3月2日 上午9:34:35
	 * @param zyh 
	 * @param type 1:首次病程   2:日常病程
	 * @return
	 */
	public List<BcjlModel> queryBcjl(String zyh,String type){
		DBManager_HIS dbm = new DBManager_HIS();
		String sql = "SELECT HC.CBCLXMC,HC.CZYH,HC.TJLNR,HC.CJLRMC,HC.DJLSJ,HV.CXM,HV.CRYKSMC,FH.HOS_NAME,HC.CBRXM "
				+ "FROM H_COURSE HC "
				+ "LEFT JOIN H_VISIT HV ON HV.CZYH = HC.CZYH "
				+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HV.CORP_ID "
				+ "WHERE HC.CZYH='"+zyh+"' ";
				if("1".equals(type)){
					sql+= " AND CBCLXMC='首次病程记录'";
				}else{
					sql +=" ORDER BY HC.DJLSJ";
				}
		List<BcjlModel> listBcjlModel = dbm.getObjectList(BcjlModel.class, sql);
		List<BcjlModel> returnList = new ArrayList<BcjlModel>();
		
		for(BcjlModel model : listBcjlModel){
			String TJLNR = replaceBrmc(model.getTJLNR(), model.getCXM(), model.getCBRXM());
			//进行MTS标准化，获取医疗术语，提供链接到图请服务
			model.setTJLNR(getLibraryUrl(TJLNR));
			
			if(model.getDJLSJ().length()>25){
				try {
					model.setDJLSJ(sff.format(sf.parse(model.getDJLSJ().substring(0, 18).replace(" ", ""))));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			returnList.add(model);
		}
		
		BcjlModel model = new BcjlModel();
		Collections.sort(returnList, model);
		
		dbm.close();
		return returnList;
	}
	
	/**
	 * @描述：根据内容，获取MTS结果，把MTS结果链接到图请服务上去
	 *
	 * @作者：SZ
	 * @时间：2017年7月10日 上午9:13:06
	 * @param str
	 * @return
	 */
	public static String getLibraryUrl(String str){
		List<ResultModel> list = MTSUtil.getMTSResult(str, null);
		
		String startA = "<a target=\"view_windo\" href=\"/MDEPlatform/pages_new/library/bookServiceSearchList.html?searchMenu=0&searchText=";
		String middleA = "\">";
		String endA = "</a>";
		for(ResultModel model : list){
			if(!StringUtil.isNullOrEmpty(model.getMtsresult())){
				str = str.replace(model.getNlpresult(), startA+model.getMtsresult()+middleA+model.getNlpresult()+endA);
			}
		}
		return str;
	}
	
//	/**
//	 * @描述：病程记录查询
//	 * @作者：SZ
//	 * @时间：2017年3月2日 上午9:34:35
//	 * @param zyh 
//	 * @param type 1:首次病程   2:日常病程
//	 * @return
//	 */
//	public List<BcjlModel> queryBcjl(String zyh,String type){
//		DBManager_HIS dbm = new DBManager_HIS();
//		String listSql = "SELECT CZYH,CBRXM FROM H_COURSE WHERE CBCLXMC='首次病程记录'";
//		List<BcjlModel> listModel = dbm.getObjectList(BcjlModel.class, listSql);
//		List<String> list = new ArrayList<String>();
//		for(BcjlModel model : listModel){
//			list.add(model.getCZYH());
//		}
//		
//		String brxm = ""; 
//		String truename = "";
//		if(!list.contains(zyh)){
//			int temp = (int) ( 50 * Math.random());
//			String sqltruename = "SELECT CXM FROM H_VISIT WHERE CZYH ='"+zyh+"' ";
//			try {
//				Map<String,String> map = dbm.executeQueryHashMap(sqltruename);
//				truename = map.get("CXM");
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			zyh = listModel.get(temp).getCZYH();
//			brxm = listModel.get(temp).getCBRXM();
//		}
//		
//		String sql = "SELECT HC.CBCLXMC,HC.CZYH,HC.TJLNR,HC.CJLRMC,HC.DJLSJ,HV.CXM,HV.CRYKSMC,FH.HOS_NAME "
//				+ "FROM H_COURSE HC "
//				+ "LEFT JOIN H_VISIT HV ON HV.CZYH = HC.CZYH "
//				+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HV.CORP_ID "
//				+ "WHERE HC.CZYH='"+zyh+"' ";
//				if("1".equals(type)){
//					sql+= " AND HC.DJLSJ = (SELECT MIN(DJLSJ) From H_COURSE WHERE CZYH='"+zyh+"')";
//				}else{
//					sql +=" ORDER BY HC.DJLSJ";
//				}
//				
//		List<BcjlModel> listBcjlModel = dbm.getObjectList(BcjlModel.class, sql);
//		List<BcjlModel> returnList = new ArrayList<BcjlModel>();
//			
//		for(BcjlModel model : listBcjlModel){
//			model.setTJLNR(replaceBrmc(model.getTJLNR(),truename,brxm));
//			model.setCXM(truename);
//			returnList.add(model);
//		}
//		dbm.close();
//		return returnList;
//	}
	
	private static String replaceBrmc(String str,String truename,String brmc){
		return replaceStr(str.replace(brmc, truename));
	}
	
	private static String replaceStr(String str){
		str = str
		.replace("（1）", "</br>(1)")
		.replace("（2）", "</br>(2)")
		.replace("（3）", "</br>(3)")
		.replace("（4）", "</br>(4)")
		.replace("（5）", "</br>(5)")
		.replace("（6）", "</br>(6)")
		.replace("（7）", "</br>(7)")
		.replace("1）", "</br>1)")
		.replace("2）", "</br>2)")
		.replace("3）", "</br>3)")
		.replace("4）", "</br>4)")
		.replace("5）", "</br>5)")
		.replace("6）", "</br>6)")
		.replace("7）", "</br>7)")
		.replace("①.", "</br>1.")
		.replace("②.", "</br>2.")
		.replace("③.", "</br>3.")
		.replace("④.", "</br>4.")
		.replace("⑤.", "</br>5.")
		.replace("⑥.", "</br>6.")
		.replace("⑦.", "</br>7.")
		.replace("⑧.", "</br>8.")
		.replace("⑨.", "</br>9.")
		.replace("⑩.", "</br>10.")
		.replace("二.", "</br>二.")
		.replace("三.", "</br>三.")
		.replace("四.", "</br>四.")
		.replace("五.", "</br>五.")
		.replace("六.", "</br>六.")
		.replace("七.", "</br>七.")
		.replace("八.", "</br>八.")
		.replace("九.", "</br>九.")
		.replace("十.", "</br>十.")
		
		.replace("二、", "</br>二、")
		.replace("三、", "</br>三、")
		.replace("四、", "</br>四、")
		.replace("五、", "</br>五、")
		.replace("六、", "</br>六、")
		.replace("七、", "</br>七、")
		.replace("八、", "</br>八、")
		.replace("九、", "</br>九、")
		.replace("十、", "</br>十、")
		.replace("1、", "</br>1、")
		.replace("2、", "</br>2、")
		.replace("3、", "</br>3、")
		.replace("4、", "</br>4、")
		.replace("5、", "</br>5、")
		.replace("6、", "</br>6、")
		.replace("7、", "</br>7、")
		.replace("8、", "</br>8、")
		.replace("9、", "</br>9、")
		.replace("①", "</br>1.")
		.replace("②", "</br>2.")
		.replace("③", "</br>3.")
		.replace("④", "</br>4.")
		.replace("⑤", "</br>5.")
		.replace("⑥", "</br>6.")
		.replace("⑦", "</br>7.")
		.replace("⑧", "</br>8.")
		.replace("⑨", "</br>9.")
		.replace("⑩", "</br>10.")
		.replace("（一）", "</br>(一)")
		.replace("（二）", "</br>(二)")
		.replace("（三）", "</br>(三)")
		.replace("（四）", "</br>(四)")
		.replace("（五）", "</br>(五)")
		.replace("（六）", "</br>(六)")
		.replace("（七）", "</br>(七)")
//		.replace("男，", "")
//		.replace("女，", "")
		.replace("主治医师：", "</br>主治医师：")
		.replace("副主任医师：", "</br>副主任医师：")
		.replace("住院医师：", "</br>住院医师：")
		.replace("中医辨病辨证依据及鉴别诊断：", "</br>中医辨病辨证依据及鉴别诊断：")
		.replace("西医诊断依据及鉴别诊断：", "</br>西医诊断依据及鉴别诊断：")
		.replace("二确诊分析：", "</br>二确诊分析：")
		.replace("三诊疗计划", "</br>三诊疗计划");
		return replaceStr1(str);
	}
	
	public static String replaceStr1(String str) {
		String b = "";
		String c = "";
		for (int i = 0; i < str.length(); i++) {
			if (i + 1 < str.length()) {
				c = str.charAt(i + 1) + "";
			} else {
				c = ",";
			}
			if (i + 2 < str.length() && Character.isDigit(str.charAt(i)) && !Character.isDigit(str.charAt(i + 2)) && ".".equals(c)) {
				b += "</br>" + str.charAt(i);
			} else {
				b += str.charAt(i);
			}
		}

		return b;
	}
	
	public static void main(String[] args) {
//		SimpleDateFormat sf = new SimpleDateFormat("dd-MM月-yyHH.mm.ss");
//		
//		SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String a = "2014-02-25 11:20:27.0";
//		System.out.println(a.substring(0,19));
//		try {
//			Date d = sf.parse(a.substring(0, 18).replace(" ", ""));
//			System.out.println(sff.format(d));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String str = "病历摘要：患者以“ 阴道淋漓出血1年余”为主诉入院，平素月经规律，5年前无明显诱因出现同房后阴道少量出血，月经期过后偶白带带血丝，无腹痛、下坠、腰酸等任何不适，多次在当地医院药物治疗，效果欠佳。1年来患者无明显诱因出现未同房后阴道淋漓出血，无其他任何不适，在当地多次给予月经期抗感染治疗，稍好转，未在意。今为求进一步治疗来我院行彩超示：宫腔内54*18mm不均质偏低回声。门诊以“宫腔内异常回声”收入我科。入院查体：T：36.8℃、P：84次/分,R：21次/分,BP：120/70mmHg，心肺听诊无异常，腹软，肝脾肋下未触及，移动性浊音阴性，双下肢无水肿，脊柱四肢无畸形，生理反射存在，病理反射未引出。妇科检查：外阴：已婚已产式；阴道：畅，容二指，少量咖啡色分泌物，无异味；宫颈：光滑，稍肥大，无糜烂，摇举痛阴性；宫体：前位，大小正常，质中，活动可，无压痛；附件：双侧附件未触及明显异常。初步诊断：宫腔内异常回声。拟诊讨论：";
		List<ResultModel> list = MTSUtil.getMTSResult(str, null);
		
		String startA = "<a targer=\"_blank\" href=\"/MDEPlatform/pages_new/library/bookServiceSearchList.html?searchMenu=0&searchText=";
		String middleA = "\">";
		String endA = "</a>";
		for(ResultModel model : list){
			if(!StringUtil.isNullOrEmpty(model.getMtsresult())){
				System.out.println(model.getNlpresult()+"----"+model.getMtsresult());
				
				str = str.replaceAll(model.getNlpresult(), startA+model.getMtsresult()+middleA+model.getNlpresult()+endA);
			}
		}
		System.out.println(str);
	}
}
