package com.ts.controller.system.apiLoad;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.app.SysAppUser;
import com.ts.service.system.appuser.AppuserManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.app.SessionAppMap;
import com.ts.controller.api.appuserLoad.UpdataAppSysmap;

@Controller
@RequestMapping(value="/api")
public class ApiLoadController extends BaseController {
	
	@Resource(name="appuserService")
	private AppuserManager AppuserService;
	
	@Resource(name="updataAppSysmapImpl")
	private UpdataAppSysmap UpdataAppSysmapImpl;
	
	/**显示接口列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/load")
	@Rights(code="api/load")
	public ModelAndView listLoad(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String businessName = pd.getString("businessName");							//检索条件 关键词
			if(null != businessName && !"".equals(businessName)){
				pd.put("businessName", businessName.trim());
			}
			String fieldDisc = pd.getString("fieldDisc");
			if(null != fieldDisc && !"".equals(fieldDisc)){
				pd.put("fieldDisc", fieldDisc.trim());
			}
			page.setPd(pd);
			mv.setViewName("system/apimanager/apiload/apiLoad_iframe");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**显示缓存列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/mapload")
	@Rights(code="api/mapload")
	public ModelAndView listMapLoad(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			List<SysAppUser> ls = SessionAppMap.getAppLoad();
			List<SysAppUser> sysAppUserList = new ArrayList<SysAppUser>();
			String keywords = pd.getString("keywords");
			List<PageData>  loadList=  AppuserService.findByVerifyKey();
			for(int u = 0; u < ls.size(); u++){	
				SysAppUser sysAppUser = ls.get(u);
				if(keywords != null && !"".equals(keywords) && !sysAppUser.getUSERNAME().contains(keywords)){//不符合搜索
					continue;
				}
				if(sysAppUser.getIS_VERIFY().equals("0")){
					sysAppUserList.add(sysAppUser);
					continue;
				}
				boolean flag = false;
				for (PageData pageData : loadList) {
					if(pageData.getString("username").equals(sysAppUser.getUSERNAME())){
						loadList.remove(pageData);
						flag = true;
						break;
					}
				}
				if(!flag){//数据库已被删除
					sysAppUser.setIS_VERIFY("0");
					PageData pdu = new PageData();
					pdu.put("USERNAME", sysAppUser.getUSERNAME());
					UpdataAppSysmapImpl.setAppSysmap(pdu);
				}
				sysAppUserList.add(sysAppUser);
			}
			/*List<PageData>  loadList=  AppuserService.findByVerifyKey();
			PageData param1 = null;
			for(int u = 0; u < ls.size(); u++){	
				SysAppUser param2 = ls.get(u);
				if(param2.getIS_VERIFY().equals("0")){
					continue;
				}
				for(int i = 0; i < loadList.size(); i++){
					param1 = loadList.get(i);
					if(param1.getString("username").equals(param2.getUSERNAME())){
						loadList.remove(param1);
						break;
					}
				}
				if(!param1.getString("username").equals(param2.getUSERNAME())){
					ls.get(u).setIS_VERIFY("0");
					PageData pdu = new PageData();
					pdu.put("USERNAME", param2.getUSERNAME());
					UpdataAppSysmapImpl.setAppSysmap(pdu);
//					ls.remove(param2);
//					SessionAppMap.removeCacheItem(param2.getUSERNAME());
				}
			}*/
			page.setPd(pd);
			mv.setViewName("system/apimanager/apiload/mapManager_list");
			mv.addObject("loadList", sysAppUserList);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}


	/**加载数据到-->Map
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/addload")
	@Rights(code="api/addload")
	@ResponseBody
	public void addLoadMap(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"加载数据到缓存");
		PageData pd = new PageData();
		pd = this.getPageData();
		UpdataAppSysmapImpl.setAppSysmap(pd);
		out.write("success");
		out.close();
	}
	/**删除Map数据
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delload")
	@Rights(code="api/delload")
	@ResponseBody
	public void deleteMap(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除缓存数据");
		PageData pd = new PageData();
		pd = this.getPageData();
		SessionAppMap.removeCacheItem(pd.getString("USERNAME"));
		out.write("success");
		out.close();
	}
	
	/**显示用户列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/apiload")
	@Rights(code="api/apiload")
	public ModelAndView listInterface(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			List<SysAppUser> ls = SessionAppMap.getAppLoad();
			List<PageData>  loadList=  AppuserService.findByVerifyKeySearch(pd);
			for(int u = 0; u < ls.size(); u++){	
				SysAppUser param2 = ls.get(u);
				if(param2.getIS_VERIFY().equals("0")){
					continue;
				}
				for(int i = 0; i < loadList.size(); i++){
					PageData param1 = loadList.get(i);
					if(param1.getString("username").equals(param2.getUSERNAME())){
						loadList.remove(param1);
						break;
					}
				}
			}
			
			page.setPd(pd);
			mv.setViewName("system/apimanager/apiload/apiManager_list");
			mv.addObject("loadList",loadList);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
}
