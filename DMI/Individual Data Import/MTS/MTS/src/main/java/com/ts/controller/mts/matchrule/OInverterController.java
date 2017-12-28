package com.ts.controller.mts.matchrule;



import java.net.URLDecoder;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.base.BaseController;
import com.ts.entity.mts.OInterver;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfig;
import com.ts.service.mts.MtsConfigAdd;
import com.ts.service.mts.matchrule.impl.DataOInverter;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value = "/oInverter")
public class OInverterController extends BaseController {
	
	@Resource(name = "DataOInverter")
	private DataOInverter dataOInverter;
	
	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	
	
	@RequestMapping(value = "/updateOInverter", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateOInverter(@RequestBody String toBeOInverter) {	
		try {
			JSONObject inOTerm = JSONObject.fromObject(toBeOInverter);
		
			String  term_name = null;			
			
			if (inOTerm.containsKey("TERM_NAME")) {
				
				term_name=URLDecoder.decode(inOTerm.getString("TERM_NAME"),"UTF-8");
				
			}	
			
			if (term_name != null) {
				
				OInterver oInver=new OInterver();
				oInver.setBARK("");
				oInver.setINDATE(new Date());
				oInver.setINTERV_NAME(term_name);
				dataOInverter.addOInver(oInver);//插入无法干预表
				mcs.regexLoad();    //重新加载无法干预数据
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
