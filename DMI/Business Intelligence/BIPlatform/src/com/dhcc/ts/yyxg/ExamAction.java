package com.dhcc.ts.yyxg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 检查操作相关行为
 * 
 * @author xiangbao.guan
 *
 */
public class ExamAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	

	/**
	 * demo url: http://localhost:8080/MDEPlatform/yyxg/getExamList.action?h_no=16013699
	 * 根据住院号取得检查列表
	 * 
	 * 返回结果数据结构: code=1 成功,可取list; code=0 失败,取message失败原因
	 * {
	 * 	  code: "1",
	 *    list: [{
	 *      "H_NO": "住院号",
	 *      "drysj": "入院时间",
	 *      "title": "检查标题"
	 *    }],
	 *    message: ""
	 * }
	 */
	public void getExamList() {
		Map<String, String> param = new HashMap<String, String>();
		Map<String, Object> resultMap = null;
		List<Map<String, Object>> list = null;
		
		// 住院号
		HttpServletRequest request = getRequest();
		String h_no = request.getParameter("h_no");
		String type = request.getParameter("type"); // 1:门诊; 2:住院
		try {
			if (StringUtils.isBlank(h_no) || StringUtils.isBlank(type)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			
			param.put("type", type);
			param.put("h_no", h_no);
			ExamDao dao = new ExamDao();
			list = dao.getExamList(param);
			resultMap = getSuccessMap();
			resultMap.put("list", resetImgUrl(list));
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
	}
	
	private List<Map<String, Object>> resetImgUrl(List<Map<String, Object>> list) {
		List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
		if (list == null) return lst;
		for (Map<String, Object> map : list) {
			String img = (String)map.get("IMG_URL");
			String img_name = StringUtils.isNotBlank(img) ? img.substring(img.lastIndexOf('\\')+1) : "";
			if (StringUtils.isNotBlank(img_name) && Constant.EXAM_REPORT_LIST.contains(img_name))
				lst.add(map);
		}
		return lst;
	}

	/**
	 * demo url: http://localhost:8080/MDEPlatform/yyxg/getExamReport.action?exam_id=1001
	 * 
	 * 取得检查的图片报告
	 */
	public void getExamReport() {
		Map<String, String> param = new HashMap<String, String>();
		HttpServletRequest request = getRequest();
		String exam_id = request.getParameter("exam_id");
		Map<String, Object> resultMap = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		try {
			if (StringUtils.isBlank(exam_id)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			
			param.put("exam_id", exam_id); // 045e26ccb1924eb9b0bf8c3f4e46c3e1
			ExamDao dao = new ExamDao();
			list = dao.getExamRecordList(param);
			setReportImage(list);
			resultMap = getSuccessMap();
			resultMap.put("list", list == null ? new ArrayList<Map<String, Object>>():list);
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
	}
	
	private void setReportImage(List<Map<String, Object>> list) {
		if (list == null) return ;
		for (Map<String, Object> map : list) {
//			String report_image = getRequest().getContextPath() + "/pages/examReport/" + Contants.EXAM_REPORT_MAP.get(map.get("TITLE"));
			String img = (String)map.get("IMG_URL");
			String report_image = getRequest().getContextPath() + "/pages/examReport/" + img.substring(img.lastIndexOf('\\')+1);
			map.remove("IMG_URL");
			map.put("report_image", report_image);
		}
	}

}
