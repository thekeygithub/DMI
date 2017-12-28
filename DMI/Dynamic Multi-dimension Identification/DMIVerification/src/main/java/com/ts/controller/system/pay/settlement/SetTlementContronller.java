package com.ts.controller.system.pay.settlement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.pay.SettlementDrugstoreManager;
import com.ts.service.pay.impl.SettlementService;
import com.ts.util.PageData;
import com.ts.util.StringUtil;

/**手动结算升迁功能
 * @author ping
 *
 */
@Controller
@RequestMapping(value = "/settlement")
public class SetTlementContronller extends BaseController {

	@Resource(name = "settlementService")
	private SettlementService settlementService;
	
	@Resource(name = "settlementDrugstoreService")
	private SettlementDrugstoreManager settlementDrugstoreService;
	
	@RequestMapping(value = "/exec")
	@Rights(code = "settlement/exec")
	public ModelAndView totalDetail(Page page) throws Exception {
		String msg = "执行失败 ！";
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String flag = pd.getString("flag"); //用来标识是否时页面提交
		String startDateStr = pd.getString("final_date_start");
		String endDateStr = pd.getString("final_date_end");
		String settlementType = pd.getString("settlementType");
		String groupId = pd.getString("groupId");//机构ID
		String hospCode = pd.getString("hospCode");//医院ID
		
		//查询医院列表
		List<PageData> hospList = settlementService.getHospList();
		//查询机构列表
		List<PageData> groupList = settlementService.getGroupList();
		
		mv.setViewName("pay/record/settlement/settlement");
		mv.addObject("final_date_start", startDateStr);
		mv.addObject("final_date_end", endDateStr);
		mv.addObject("settlementType", settlementType);
		mv.addObject("groupId", groupId);
		mv.addObject("hospCode", hospCode);
		mv.addObject("hospList", hospList);
		mv.addObject("groupList", groupList);
		if(!"1".equals(flag)){
			return mv;
		}
		if(StringUtil.isNullOrEmpty(startDateStr) || StringUtil.isNullOrEmpty(endDateStr)){
			msg = "请选择结算起止时间！";
			mv.addObject("msg", msg);
			return mv;
		}
		if(StringUtil.isNullOrEmpty(settlementType)){
			msg = "请选择对账类型！";
			mv.addObject("msg", msg);
			return mv;
		}
		if(!"".equals(settlementType)){//结算类型   mz:门诊；all:全部；zy:住院；yd:药店
			SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = datef.parse(startDateStr);
			Date endDate = datef.parse(endDateStr);
			if(startDate.after(endDate)){
				msg = "执行失败！（开始时间应小于结束时间）";
				mv.addObject("msg", msg);
				return mv;
			}
			Date thisDate = startDate;
			while (!thisDate.after(endDate)) {
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(thisDate);
			    cal.add(Calendar.DAY_OF_MONTH, 1);
			    Date tmpDate = datef.parse(datef.format(cal.getTime()));
			    if("mz".equals(settlementType) || "all".equals(settlementType))//门诊
			    	settlementService.settlement(thisDate, tmpDate, groupId, hospCode);
			    if("yd".equals(settlementType) || "all".equals(settlementType))//药店
			    	settlementDrugstoreService.settlement(thisDate, tmpDate, groupId, hospCode);
				thisDate = tmpDate;
			}
			msg = "执行成功！";
			mv.addObject("msg", msg);
			return mv;
		}else{
			msg = "结算类型为空！";
			mv.addObject("msg", msg);
			return mv;
		}
	}
}