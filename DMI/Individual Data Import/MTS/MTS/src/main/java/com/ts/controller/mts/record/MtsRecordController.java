package com.ts.controller.mts.record;

import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsRecordDetail;
import com.ts.entity.mts.MtsRecordInfo;
import com.ts.service.mts.record.MTSRecordService;
import com.ts.util.PageData;
import com.ts.util.RandomUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @类名称: LoadRuleController
 * @类描述: Redis数据装载规则维护
 * @作者:李巍
 * @创建时间:2016年10月8日 下午2:04:26
 */
@Controller
@RequestMapping(value = "/mtsRecord")
public class MtsRecordController extends BaseController {

	String menuUrl = "mtsRecord/listMtsRecord.do"; // 菜单地址(权限用)
	@Resource(name = "MTSRecordService")
	private MTSRecordService mtsRecordService;

	/**
	 * 显示规则列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsRecord")
	public ModelAndView listMtsDict(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData> listMtsRecord = mtsRecordService.mtsRecordlistPage(page); // 列出用户列表
		mv.setViewName("mts/mtsrecord/mts_record_list");
		mv.addObject("listMtsRecord", listMtsRecord);
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 保存MTS传递的问题单原始数据
	 * 
	 * @param toBeStandardStr
	 * @return
	 */
	@RequestMapping(value = { "/syncResultFromAI" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object syncResultFromAI(@RequestBody String oStr) {
		try {
			/*oStr = "{\"O_TYPE\":\"1\",\"BATCH_NUMBER\":\"62730020170804123627694\",\"O_ID\":\"11042120170804123628305\",\"CLM_ID \":\"11042120170804123628305\",\"DEAL_STATUS\":\"3\","
					+ "\"oList\":[{\"NLP_RESULT\":\"咳嗽【症状】\",\"NLP_ORDER\":\"0\",\"XY_STANDARD_WORD\":\"咳嗽\",\"XY_STANDARD_MAIN_CODE\":\"R05.x00\",\"XY_STANDARD_ATTACH_CODE\":\"\","
					+ "\"ZY_STANDARD_WORD\":\"咳嗽病\",\"ZY_STANDARD_MAIN_CODE\":\"BNF010\",\"SS_STANDARD_WORD\":\"咳嗽病\",\"SS_STANDARD_MAIN_CODE\":\"BNF010\",\"TERMINOLOGY_TYPE\":\"1\","
					+ "\"NLP_RESULT\":\"咳嗽1【症状】\",\"NLP_ORDER\":\"0\",\"XY_STANDARD_WORD\":\"咳嗽1\",\"XY_STANDARD_MAIN_CODE\":\"R05.x00\",\"XY_STANDARD_ATTACH_CODE\":\"\","
					+ "\"ZY_STANDARD_WORD\":\"咳嗽病1\",\"ZY_STANDARD_MAIN_CODE\":\"BNF010\",\"SS_STANDARD_WORD\":\"咳嗽病1\",\"SS_STANDARD_MAIN_CODE\":\"BNF010\",\"TERMINOLOGY_TYPE\":\"1\""
					+ "}]}";*/
			oStr = URLDecoder.decode(oStr, "UTF-8");
			JSONObject myobj = JSONObject.fromObject(oStr);// 原始数据为json格式
			String O_TYPE = "";
			String BATCH_NUMBER = "";
			String O_ID = "";
			String CLM_ID = "";
			System.out.println("oStr=====>>>"+oStr);
			if(myobj.containsKey("O_TYPE")){
				O_TYPE = myobj.getString("O_TYPE");
			}
			if(myobj.containsKey("BATCH_NUMBER")){
				BATCH_NUMBER = myobj.getString("BATCH_NUMBER");
			}
			if(myobj.containsKey("O_ID")){
				O_ID = myobj.getString("O_ID");
			}
			if(myobj.containsKey("CLM_ID")){
				CLM_ID = myobj.getString("CLM_ID");
			}
			
			if(!"".equals(O_ID) && null != O_ID){
				MtsRecordInfo recordInfo = new MtsRecordInfo();
				recordInfo.setRECORD_INFO_ID(O_ID);
				
				List<MtsRecordInfo> recordInfoList = mtsRecordService.findMtsRecordInfo(recordInfo);
				if(recordInfoList != null && recordInfoList.size() == 1){
					recordInfo = recordInfoList.get(0);
					MtsRecordDetail mtsRecordDetail = new MtsRecordDetail();
					mtsRecordDetail.setRECORD_INFO_ID(recordInfo.getRECORD_INFO_ID());
					mtsRecordService.deleteMtsRecordDetail(mtsRecordDetail);
					
					JSONArray jsonArray = myobj.getJSONArray("oList");
					if(jsonArray != null && jsonArray.size() > 0){
						for(int i=0;i<jsonArray.size();i++){
							JSONObject job = jsonArray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
							
							String NLP_RESULT = "";
							String NLP_ORDER = "";
							String XY_STANDARD_WORD = "";
							String XY_STANDARD_MAIN_CODE = "";
							String XY_STANDARD_ATTACH_CODE = "";
							String ZY_STANDARD_WORD = "";
							String ZY_STANDARD_MAIN_CODE = "";
							String SS_STANDARD_WORD = "";
							String SS_STANDARD_MAIN_CODE = "";
							String TERMINOLOGY_TYPE = "";
							String DEAL_STATUS = "";
							
							if(job.containsKey("NLP_RESULT")){
								NLP_RESULT = job.getString("NLP_RESULT");
							}
							if(job.containsKey("NLP_ORDER")){
								NLP_ORDER = job.getString("NLP_ORDER");
							}
							if(job.containsKey("XY_STANDARD_WORD")){
								XY_STANDARD_WORD = job.getString("XY_STANDARD_WORD");
							}
							if(job.containsKey("XY_STANDARD_MAIN_CODE")){
								XY_STANDARD_MAIN_CODE = job.getString("XY_STANDARD_MAIN_CODE");
							}
							if(job.containsKey("XY_STANDARD_ATTACH_CODE")){
								XY_STANDARD_ATTACH_CODE = job.getString("XY_STANDARD_ATTACH_CODE");
							}
							if(job.containsKey("ZY_STANDARD_WORD")){
								ZY_STANDARD_WORD = job.getString("ZY_STANDARD_WORD");
							}
							if(job.containsKey("ZY_STANDARD_MAIN_CODE")){
								ZY_STANDARD_MAIN_CODE = job.getString("ZY_STANDARD_MAIN_CODE");
							}
							if(job.containsKey("SS_STANDARD_WORD")){
								SS_STANDARD_WORD = job.getString("SS_STANDARD_WORD");
							}
							if(job.containsKey("SS_STANDARD_MAIN_CODE")){
								SS_STANDARD_MAIN_CODE = job.getString("SS_STANDARD_MAIN_CODE");
							}
							if(job.containsKey("TERMINOLOGY_TYPE")){
								TERMINOLOGY_TYPE = job.getString("TERMINOLOGY_TYPE");
							}
							if(job.containsKey("DEAL_STATUS")){
								DEAL_STATUS = job.getString("DEAL_STATUS");
							}
							
							mtsRecordDetail = new MtsRecordDetail();
							mtsRecordDetail.setNLP_RESULT(NLP_RESULT);
							mtsRecordDetail.setNLP_ORDER(NLP_ORDER);
							mtsRecordDetail.setXY_STANDARD_WORD(XY_STANDARD_WORD);
							mtsRecordDetail.setXY_STANDARD_MAIN_CODE(XY_STANDARD_MAIN_CODE);
							mtsRecordDetail.setXY_STANDARD_ATTACH_CODE(XY_STANDARD_ATTACH_CODE);
							mtsRecordDetail.setZY_STANDARD_WORD(ZY_STANDARD_WORD);
							mtsRecordDetail.setZY_STANDARD_MAIN_CODE(ZY_STANDARD_MAIN_CODE);
							mtsRecordDetail.setSS_STANDARD_WORD(SS_STANDARD_WORD);
							mtsRecordDetail.setSS_STANDARD_MAIN_CODE(SS_STANDARD_MAIN_CODE);
							mtsRecordDetail.setTERMINOLOGY_TYPE(TERMINOLOGY_TYPE);
							mtsRecordDetail.setRECORD_INFO_ID(recordInfo.getRECORD_INFO_ID());
							mtsRecordDetail.setDB_DATA_ID(recordInfo.getDB_DATA_ID());
							mtsRecordDetail.setSTATUS("4");
							mtsRecordDetail.setRECORD_DETAIL_ID(RandomUtil.getRandomId());
							mtsRecordDetail.setCAN_NOT_STANDARD_TYPE("");
							mtsRecordService.addMtsRecordDetail(mtsRecordDetail);
							recordInfo.setINFO_STATUS("3");
							mtsRecordService.editMtsRecordInfo(recordInfo);
						}
					}
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
