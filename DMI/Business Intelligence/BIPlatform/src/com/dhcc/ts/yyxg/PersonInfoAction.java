package com.dhcc.ts.yyxg;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class PersonInfoAction extends BaseAction {

	private static final long serialVersionUID = -3543331702303971493L;
	
	private SimpleDateFormat df1 = new SimpleDateFormat("yyyymmdd");
	
	private SimpleDateFormat df2 = new SimpleDateFormat("yyyy-mm-dd");
	

	/**
	 * demo url: http://localhost:8080/MDEPlatform/yyxg/getPersonInfo.action?card_no=410204198702066012
	 * 个人社会保险信息
	 */
	public void getPersonInfo() {
		Map<String, String> param = new HashMap<String, String>();
		Map<String, Object> resultMap = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		// 身份证号
		HttpServletRequest request = getRequest();
		String name = request.getParameter("name");
		String card_no = request.getParameter("card_no");
		try {
			if (StringUtils.isBlank(card_no)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			String xbStr = String.valueOf(card_no.charAt(16));
			String aac004 = Integer.parseInt(xbStr)%2 == 0 ? "女":"男";
			
			Map<String, Object> retMap = new HashMap<String, Object>();
			param.put("card_no", card_no);
			PersonInfoDao dao = new PersonInfoDao();
			// 个人信息
			Map<String, Object> info = dao.getPersonInfo(param);
			info.put("AAC003", name);
			info.put("AAC004", aac004);
			setInfoMap(card_no, info);
			retMap.put("geren", info);
			
			Integer age = (Integer) info.get("NIANLING");
			int cbYear = (age-60) >0 ? 60 - 22 : age - 22 ; // 参保年限
			// 参保信息
			String cbdw = (String) info.get("AAB004");
			retMap.put("canbao", getCbMap(cbdw));
			// 养老信息
			String ryzt = (String) info.get("RYZT");
			
			retMap.put("yanglao", getYlMap(ryzt, cbYear));
			// 医保信息
			retMap.put("yibao", getYbMap(cbYear));
			list.add(retMap);
			
			resultMap = getSuccessMap();
			resultMap.put("list", list == null ? new ArrayList<Map<String, Object>>():list);
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
	}

	private void setInfoMap(String card_no, Map<String, Object> info) {
		if (card_no.length() != 18) card_no = "410204198002065052";
		
		info.put("AAC005", "汉");
		info.put("SHBZHM", card_no);
		
		String birthStr = card_no.substring(6, 14);
		
		Date birth = null;
		try {
			birth = df1.parse(birthStr);
		} catch (ParseException e) {
			birth = new Date();
		}
		
		info.put("AAC006", df2.format(birth));
		if (birth == null) birth = new Date();
		int year = TsUtil.yearDateDiff(birth, new Date());
		info.put("NIANLING", year);
		String gender = (String) info.get("AAC004");
		Date ltDate = null;
		String ryzt = "";
		if ("男".equals(gender)) {
			ryzt = year >= 60 ? "退休" : "在职";
			ltDate = TsUtil.calculateDate(birth, Calendar.YEAR, 60);
		} else {
			ryzt = year >= 55 ? "退休" : "在职";
			ltDate = TsUtil.calculateDate(birth, Calendar.YEAR, 55);
		}
		info.put("AIC162", df2.format(ltDate));
		info.put("RYZT", ryzt);
	}
	
	private Map<String, Object> getCbMap(String cbdw) {
		Map<String, Object> cbMap = new HashMap<String, Object>();
		cbMap.put("SSTCQ", "市本级");
		cbMap.put("RYLB", "企业职工");
		cbMap.put("CBDW", cbdw);
		cbMap.put("CBZT", "正常");
		return cbMap;
	}
	
	private Map<String, Object> getYlMap(String ryzt, int year) {
		Map<String, Object> ylMap = new HashMap<String, Object>();
		ylMap.put("LTXBZ", ryzt);
		ylMap.put("LJCBNX", year);
		ylMap.put("LXCBNX", year);
		return ylMap;
	}
 
	private Map<String, Object> getYbMap(int year) {
		Map<String, Object> ybMap = new HashMap<String, Object>();
		ybMap.put("LJCBNX", year);
		ybMap.put("LXCBNX", year);
		ybMap.put("TCYLBC", "否");
		ybMap.put("DYZT", "正常");
		ybMap.put("DYLB", "医疗退休");
		ybMap.put("ZYYLBX", "否");
		return ybMap;
	}
			
	public static void main(String[] args) {
		String card_no = "410204198002065052";
		System.out.println(card_no.substring(6,14));
		String c = String.valueOf("410204198002065062".charAt(16));
		int i = Integer.parseInt(c)%2;
		
		System.out.println(i);
	}
}
