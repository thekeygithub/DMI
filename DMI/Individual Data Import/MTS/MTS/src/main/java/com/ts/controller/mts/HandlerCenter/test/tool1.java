package com.ts.controller.mts.HandlerCenter.test;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;
import com.ts.controller.mts.HandlerCenter.TemplateManager.Impl.ToolTempLate;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("tool1")
public class tool1 extends ToolTempLate
{

	@Override
	public JSONArray toolExce(JSONArray value) {
		System.out.println("***********************tool1*************");
		return value;
	}

    

   
}

