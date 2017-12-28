package com.ts.controller.api.system;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.base.BaseController;
import com.ts.entity.app.AppOrganiKey;
import com.ts.entity.app.SysAppUser;
import com.ts.service.system.apimanager.Relation.RelationManager;
import com.ts.service.system.appuser.AppuserManager;
import com.ts.util.PageData;
import com.ts.util.app.SessionAppMap;

/**
 * 加载机构和公钥。
 * @author autumn
 *
 */
@Controller
@RequestMapping(value = "/app")
public class AppUserLoader extends BaseController {

	@Resource(name = "appuserService")
	private AppuserManager appuserService;
	
	@Resource(name = "relationService")
	private RelationManager relationService;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void UserLoader(){
		try {
			//获得需要签名验证的用户
			List<PageData> pds =  appuserService.findByVerifyKey();
			for(PageData pd: pds){
				AppOrganiKey aok = new AppOrganiKey();
				// 根据用户id获取可访问字段和数据权限 SYS_ROLE_TABLE_RELATION
				List<PageData> dataList = relationService.findDataByUserId(pd);
				//数据整理
				Map<String, Object> map = transitionData(dataList);
				Set<String> aiptyep = (Set<String>) map.get("apitype");
				Map<String, Set<String>> showFieldMap = (Map<String, Set<String>>) map.get("showfield");
				// 权限
				Map<String, Map<String, Set<String>>> jurisdictionMap = (Map<String, Map<String, Set<String>>>) map	.get("jurisdiction");
				SysAppUser appUser = new SysAppUser();
				appUser.setAREA_ID(pd.getString("area_id"));
				appUser.setNAME(pd.getString("name"));
				appUser.setUSER_ID(pd.getString("user_id"));
				appUser.setReuqestIp(pd.getString("ip"));
				appUser.setPASSWORD(pd.getString("password"));
				appUser.setUSERNAME(pd.getString("username"));
				appUser.setIS_VERIFY(pd.get("is_verify").toString());
				appUser.setPublicKeyUser(pd.getString("PUBLIC_KEY"));
				appUser.setApitype(aiptyep);
				appUser.setShowfield(showFieldMap);
				appUser.setJurisdiction(jurisdictionMap);
				aok.setAppUser(appUser);
				aok.setOrganiCode(pd.getString("username")); 
				SessionAppMap.setOrginInfo(aok);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * @描述：获取业务类型、可显示字段、数据权限的存储数据格式(只显示每个业务的，有数据权限的字段集合) @作者：SZ
	 * @时间：2016年12月2日 上午9:20:52
	 * @param dataList
	 *            用户的可访问业务，可访问字段，可访问数据权限集合
	 * @return
	 */
	private static Map<String, Object> transitionData(List<PageData> dataList) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Map<String, Set<String>>> jurisdictionMap = new HashMap<String, Map<String, Set<String>>>();// 权限
		Map<String, Set<String>> showFieldMap = new HashMap<String, Set<String>>();// 可显示字段

		Set<String> showFieldList = null;
		Set<String> jurisdictionList = null;
		Map<String, Set<String>> jurisdictionMapValue = null;
		for (int i = 0; i < dataList.size(); i++) {
			PageData pageData = dataList.get(i);
			String type = pageData.getString("TYPE");
			String col_rule = pageData.getString("COL_RULE");
			String col_name = pageData.getString("COLUMN_NAME");
			showFieldList = showFieldMap.get(type);
			if (null == showFieldList) {
				showFieldList = new HashSet<String>();
			}
			showFieldList.add(col_name);
			showFieldMap.put(type, showFieldList);
			if (null != col_rule && !"".equals(col_rule)) {
				jurisdictionList = new HashSet<String>();
				for(String rule : col_rule.split(";")){
					jurisdictionList.add(rule);
				}
				jurisdictionMapValue = jurisdictionMap.get(type);
				if (null == jurisdictionMapValue) {
					jurisdictionMapValue = new HashMap<String, Set<String>>();
				}
				if(null != jurisdictionMapValue.get(col_name)){
					for(String rule : jurisdictionMapValue.get(col_name)){
						jurisdictionList.add(rule);
					}
				}
				jurisdictionMapValue.put(col_name, jurisdictionList);
				jurisdictionMap.put(type, jurisdictionMapValue);
			}
		}
		map.put("apitype", showFieldMap.keySet());
		map.put("jurisdiction", jurisdictionMap);
		map.put("showfield", showFieldMap);
		return map;
	}
	@ResponseBody
	public String reLoad(){
		
		
		return  "ok";
	}
}
