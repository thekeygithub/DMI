package com.dhcc.ts.yyxg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;

import com.dhcc.ts.yzd.YzdDao;

/**
 * 医院相关行为操作
 * 
 * @author xiangbao.guan
 *
 */
public class HospitalAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	
	/**
	 * demo url: http://localhost:8080/MDEPlatform/yyxg/outHospital.action?h_no=53069921
	 * 出院记录
	 * 
	 * 返回结果数据结构: code=1 成功,可取list; code=0 失败,取message失败原因
	 * {
	 * 	  code: "1",
	 *    list: [{
	 *      "CBRH": "住院号",
门诊收治诊断 CMZZDMC
临床初步诊断 CRYZDMC
出院确认诊断 CCYQX
门诊号 CBRH
	 *      "CZYYS": "住院医生"
	 *    }],
	 *    message: ""
	 * }
	 */
	
	@SuppressWarnings("unchecked")
	public void outHospital() {
		
		Map<String, String> param = new HashMap<String, String>();
		Map<String, Object> resultMap = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HttpServletRequest request = getRequest();
		// 住院号
		String h_no = request.getParameter("h_no");
		try {
			if (StringUtils.isBlank(h_no)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			param.put("h_no", h_no);
			HospitalDao dao = new HospitalDao();
			Map<String, Object> infMap = dao.getOutHospitalInfo(param);
			Map<String, Object> recMap = dao.getOutHospitalRecord(param);
			String xml = recMap.get("XMLNR") == null ? null : recMap.get("XMLNR").toString();
			
			Map<String, Object> xmlMap = MyXmlUtil.xml2mapWithAttr(xml, false);
			Map<String, Object> result = getResultMap(infMap, xmlMap);
			YzdDao yzd=new YzdDao();
			for(String key:result.keySet()){
				result.put(key, yzd.getLibraryUrl(result.get(key).toString()));
			}
			list.add(result);
			resultMap = getSuccessMap();
			resultMap.put("list", list);
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		} catch (DocumentException e) {
			logger.error("解析xml数据出错\n", e);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getResultMap(Map<String, Object> infMap, Map<String, Object> xmlMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(infMap);
		
		if (xmlMap == null || xmlMap.size() == 0) return result;
//		Map<String, Object> zyxMap = (Map<String, Object>) xmlMap.get("ZYX");
//		if (zyxMap == null) return result;
//		result.put("CXM", zyxMap.get("ZYX.7")); // 姓名
//		result.put("CXB", zyxMap.get("ZYX.8")); // 性别
		
		Object obj = xmlMap.get("NUL");
		if (obj == null) return result;
		
		if (obj instanceof Map) {
			Map<String, Object> nulMap = (Map<String, Object>) obj;
			List<Map<String, Object>> nul0List = (List<Map<String, Object>>) nulMap.get("NUL.0");
			if (nul0List == null) return result;
			
			setTextMap(nul0List, result);
		} else {
			List<Map<String, Object>> nulList = (List<Map<String, Object>>) obj;
			for (Map<String, Object> nul : nulList) {
				Object nobj = nul.get("NUL.0");
				if (nobj == null) {
				} else {
					List<Map<String, Object>> lst = (List<Map<String, Object>>)nobj;
					if (lst != null) {
						setTextMap(lst, result);
					}
				}
			}
		}
		return result;
	}
	
	private void setTextMap(List<Map<String, Object>> lst, Map<String, Object> map) {
		for (Map<String, Object> m : lst) {
			String key = (String) m.get("@Serch");
			if ("住院后的治疗及检查情况".equals(key)) {
				map.put("ZLJG", m.get("#text"));  // 治疗经过
				map.put("ZLJGG", "治愈"); // 治疗结果
			} else if ("出院时情况及医嘱".equals(key)) {
				map.put("CYSQK", m.get("#text")); // 出院时情况
				map.put("CYYZ", m.get("#text")); // 出院医嘱
			} else if ("入院时情况".equals(key)) {
				map.put("RYSQK", m.get("#text")); // 入院时情况
			} else {}
		}
	}
	
	/**
	 * 取得标准化的处方诊断和药品名称
	 */
	public void getDiagsSTD() {
		Map<String, String> param = new HashMap<String, String>();
		Map<String, Object> resultMap = null;
		HttpServletRequest request = getRequest();
		// 处方号
		String cfh = request.getParameter("cfh");
		try {
			if (StringUtils.isBlank(cfh)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			param.put("cfh", cfh);
			HospitalDao dao = new HospitalDao();
			List<Map<String, Object>> list = dao.getCfzdSTD(param);
			
			resultMap = getSuccessMap();
			resultMap.put("list", list == null ? new ArrayList<Map<String, Object>>():list);
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
	}
}
