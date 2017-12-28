package com.ts.controller.mts.matchrule;



import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.base.BaseController;
import com.ts.entity.mts.OTerm;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfig;
import com.ts.service.mts.matchrule.impl.DataOTerm;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value = "/oTerm")
public class OTermController extends BaseController {
	
	@Resource(name = "DataOTerm")
	private DataOTerm dataOTerm;
	
	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	
	
	@RequestMapping(value = "/updateOTerm", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateOTerm(@RequestBody String toBeOTerm) {
		try {
			JSONObject inOTerm = JSONObject.fromObject(toBeOTerm);
			String  term_name = null;
			String  flag= null;
			
			if (inOTerm.containsKey("TERM_NAME")) {
				term_name= inOTerm.getString("TERM_NAME");
			}	
			
			if (inOTerm.containsKey("FLAG")) {
				flag=inOTerm.getString("FLAG");
			}	
			
			if (term_name != null&&flag!=null) {
				
				OTerm oTerm=new OTerm();
				oTerm.setTERM_NAME(term_name);
				oTerm.setFLAG(flag);
				dataOTerm.addOTerm(oTerm);//插入无用术语表
				mcs.regexLoad();    //重新加载无用术语
				return 1;

			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	
}
