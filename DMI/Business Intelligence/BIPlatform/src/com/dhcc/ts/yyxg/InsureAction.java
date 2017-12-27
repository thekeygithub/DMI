package com.dhcc.ts.yyxg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class InsureAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	/**
	 * http://10.10.40.69:9090/MDEPlatform/yyxg/getInsureList.action?card_no=41020419850220402X
	 */
	public void getInsureList() {
		Map<String, String> param = new HashMap<String, String>();
		HttpServletRequest request = getRequest();
		String card_no = request.getParameter("card_no");
		Map<String, Object> resultMap = null;
		
		try {
			if (StringUtils.isBlank(card_no)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			
			InsureDao dao = new InsureDao();
			param.put("card_no", card_no);
			List<Map<String, Object>> list = dao.getInsureList(param);
			
			resultMap = getSuccessMap();
			resultMap.put("list", list == null ? new ArrayList<Map<String, Object>>():list);
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
		
		
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
