package com.ts.controller.system.apimanager.recycle;


import java.sql.Clob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.RecycleLog.AppRecycleLogManager;
import com.ts.util.FormatUtil;
import com.ts.util.Jurisdiction;
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;

/** 
 * 类名称：API日志管理
 * 创建人：     
 * 修改时间：2016年11月17日
 * @version
 */
@Controller
@RequestMapping(value="/sys")
public class Recycle extends BaseController {
	
	@Resource(name="appRecycleLogService")
	private AppRecycleLogManager appRecycleLogService;
	
	@Autowired
	private ObjectExcelView objectExcelView;
	
	/**显示用户列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/listRecycle")
	@Rights(code="sys/listRecycle")
	public ModelAndView listRecycle(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String keywords = pd.getString("keywords");							//检索条件 关键词
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			List<PageData>	RecyclList = appRecycleLogService.recyclelistPage(page); //列出日志列表
			mv.setViewName("system/recycle/recycle_list");
			mv.addObject("RecyclList", RecyclList);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**日志详情
	 * @return
	 */
	@RequestMapping(value="/goEditU")
	@Rights(code="sys/goEditU")
	public ModelAndView goEditU(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = appRecycleLogService.findById(pd);	//根据ID读取
//			Clob in = (Clob) pd.get("INPUT");
//			Clob out = (Clob) pd.get("OUTPUT");
//			String input = in.getSubString(1, (int) in.length());
//			String output = out.getSubString(1, (int) out.length());
//			pd.put("INPUT", FormatUtil.formatJson(input));
//			pd.put("OUTPUT", FormatUtil.formatJson(output));
			mv.setViewName("system/recycle/recycle_edit");
			mv.addObject("msg", "editU");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	/**导出会员信息到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	@Rights(code="sys/excel")
	public ModelAndView exportExcel(){
		logBefore(logger, Jurisdiction.getUsername()+"导出操作日志");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
				
				String keywords = pd.getString("keywords");
				if(null != keywords && !"".equals(keywords)){
					pd.put("keywords", keywords.trim());
				}
				String lastLoginStart = pd.getString("lastLoginStart");
				if(lastLoginStart != null && !"".equals(lastLoginStart)){
					pd.put("lastLoginStart", lastLoginStart);
				}
				String lastLoginEnd = pd.getString("lastLoginEnd");
				if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
					pd.put("lastLoginEnd", lastLoginEnd);
				} 
				Map<String,Object> dataMap = new HashMap<String,Object>();
				List<String> titles = new ArrayList<String>();
				titles.add("用户名称");		//1
				titles.add("API类型"); 		//2
				titles.add("输入");  		//3
				titles.add("输出");			//4
				titles.add("登录IP");		//5
				titles.add("设备编码");		//6
				titles.add("登录时间");		//7
				dataMap.put("titles", titles);
				List<PageData> recyclList = this.appRecycleLogService.listAllRecycle(pd);
				List<PageData> varList = new ArrayList<PageData>();
				for(int i=0;i<recyclList.size();i++){
					PageData vpd = new PageData();
					Clob in = (Clob) recyclList.get(i).get("INPUT");
					String input = in.getSubString(1, (int) in.length());
					Clob out = (Clob) recyclList.get(i).get("OUTPUT");
					String output = out.getSubString(1, (int) out.length());		
					vpd.put("var1", recyclList.get(i).getString("USERNAME"));		//1
					vpd.put("var2", recyclList.get(i).getString("INER_TYPE"));		//2
					vpd.put("var3", input);			//3
					vpd.put("var4", output);		//4
					vpd.put("var5", recyclList.get(i).getString("USER_IP"));			//5
					vpd.put("var6", recyclList.get(i).getString("CODE"));	//6
					vpd.put("var7", recyclList.get(i).get("CALL_DATE"));		//7
					varList.add(vpd);
				}
				dataMap.put("varList", varList);
				
				//需要统计列
//				Set<String> countCells = new HashSet<String>();
//				countCells.add("var8");
//				dataMap.put("countCells", countCells);
//				//模板引用
//				objectExcelView.setTemplate("excel/template",1);
//				//边框
//				objectExcelView.setBorderFlag(true);
				
				mv = new ModelAndView(objectExcelView,dataMap);
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
	
}
