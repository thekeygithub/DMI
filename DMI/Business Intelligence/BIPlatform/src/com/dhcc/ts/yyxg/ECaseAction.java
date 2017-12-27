package com.dhcc.ts.yyxg;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.dhcc.ts.yzd.YzdDao;

/**
 * 电子病例
 * 
 * @author xiangbao.guan
 *
 */
public class ECaseAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	/**
	 * demo url: http://localhost:8080/MDEPlatform/yyxg/getECaseRecord.action?h_no=16080895
	 * {
	 * 	  code: "1",
	 *    list: [{
	 *      "info": {},
	 *      "chuyuan": [{}],
	 *      "shoushu": [{}],
	 *      "feiyong": {}
	 *    }],
	 *    message: ""
	 * }
	 */
	public void getECaseRecord() {
		Map<String, String> param = new HashMap<String, String>();
		HttpServletRequest request = getRequest();
		String h_no = request.getParameter("h_no");
		Map<String, Object> resultMap = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		try {
			if (StringUtils.isBlank(h_no)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			
			Map<String, Object> retMap = new HashMap<String, Object>();
			list.add(retMap);
			YzdDao yzd=new YzdDao();
			// 个人信息
			ECaseDao dao = new ECaseDao();
			param.put("h_no", h_no);
			Map<String, Object> infoMap = dao.getHospitalVisitRecord(param);
			infoMap.put("CMZZDMC", yzd.getLibraryUrl(infoMap.get("CMZZDMC").toString()));
			infoMap.put("CRYZDMC", yzd.getLibraryUrl(infoMap.get("CRYZDMC").toString()));
			retMap.put("info", infoMap);
			
			// 出院
			List<Map<String, String>> cyList = new ArrayList<Map<String, String>>();
			setCyList(infoMap, cyList);
			retMap.put("chuyuan", cyList);
			// 手术
			List<Map<String, String>> ssList = new ArrayList<Map<String, String>>();
			retMap.put("shoushu", ssList);
			// 费用
			Map<String, Object> fyMap = new HashMap<String, Object>();
			// 查询处方号
			List<Map<String, String>> cfhList = dao.getCFHList(param);
			if (cfhList != null && cfhList.size() > 0) {
				StringBuilder cfhStr = new StringBuilder();
				for (Map<String, String> map : cfhList) {
					cfhStr.append(", '").append(map.get("CCFH")).append("'");
				}
				String cfhIn = "(" + cfhStr.substring(1) + ")";
				// 总费用/自费
				Map<String, String> sumMap = dao.getHospitalVisitSumFee(cfhIn);
				// 各项费用
				Map<String, String> feeMap = dao.getHospitalVisitFee(cfhIn);
				// 设置其他费用=总费用-各项费用总和
				setOtherFee(sumMap, feeMap);
				
				fyMap.putAll(feeMap);
				fyMap.putAll(sumMap);
			}
			retMap.put("feiyong", fyMap);
			
			resultMap = getSuccessMap();
			resultMap.put("list", list == null ? new ArrayList<Map<String, Object>>():list);
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
		
	}

	private void setOtherFee(Map<String, String> sumMap, Map<String, String> feeMap) {
		BigDecimal sumFee = new BigDecimal(StringUtils.isBlank(sumMap.get("JE")) ? "0.00":sumMap.get("JE"));
		BigDecimal fee = new BigDecimal("0.00");
		int i = 0;
		for (; i < feeMap.size(); i++) {
			fee = fee.add(new BigDecimal(StringUtils.isBlank(feeMap.get("JE_"+(i+1)))?"0.00":feeMap.get("JE_"+(i+1))));
		}
		feeMap.put("JE_"+(i+1), String.valueOf(sumFee.subtract(fee)));
	}

	private void setCyList(Map<String, Object> infoMap, List<Map<String, String>> cyList) {
		Map<String, String> cyMap = new HashMap<String, String>();
		cyList.add(cyMap);
		cyMap.put("JBBM", String.valueOf(infoMap.get("JBBM")));
		cyMap.put("CYZD", String.valueOf(infoMap.get("CRYZDMC")));
		cyMap.put("RYBQ", String.valueOf(infoMap.get("CRYZDMC")));
		infoMap.remove("CRYZDMC");
	}
	
}
