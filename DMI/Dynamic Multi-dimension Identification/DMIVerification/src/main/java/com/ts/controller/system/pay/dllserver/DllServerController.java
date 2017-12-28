package com.ts.controller.system.pay.dllserver;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_dll_server;
import com.ts.entity.Page;
import com.ts.service.dllserver.PdllServerManager;
import com.ts.util.AppUtil;
import com.ts.util.PageData;


/**
 * Dll服务配置Act
 * @author fus
 *
 */
@Controller
@RequestMapping(value="/dllserver")
public class DllServerController extends BaseController{
	
	@Resource(name="dllServerService")
	private PdllServerManager dllServerManager;
	
	
	/**列表
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/list")
	@Rights(code="dllserver/list")
	public ModelAndView list(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYW = pd.getString("keywords");	//检索条件
		if(null != KEYW && !"".equals(KEYW)){
			pd.put("keywords", KEYW.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = dllServerManager.list(page);	//列出dll列表
		mv.setViewName("pay/dllserver/dll_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 去新增服务配置信息
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	@Rights(code="dllserver/toAdd")
	public  ModelAndView toAdd()  throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("pay/dllserver/dll_edit");
		mv.addObject("msg","add");
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**
	 * 保存dll服务配置
	 * @param dllServer
	 * @return mv
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@Rights(code="dllserver/add")
	public ModelAndView add(P_dll_server dllServer) throws Exception{
		ModelAndView mv = this.getModelAndView();
		DateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dd = format.format(date);
		Date td = format.parse(dd);
		dllServer.setCREATE_DATE(td); //设置时间
		//服务器地址去空格
		String localIp = dllServer.getLOCAL_IP();
		if(!"".equals(localIp)){
			localIp = localIp.trim();
			dllServer.setLOCAL_IP(localIp);
		}
		//医院名称去空格
		String hospName = dllServer.getHOSP_NAME();
		if(!"".equals(hospName)){
			hospName = hospName.trim();
			dllServer.setHOSP_NAME(hospName);
		}
		//医院编码去空格
		String hospId = dllServer.getHOSP_ID();
		if(!"".equals(hospId)){
			hospId =  hospId.trim();
			dllServer.setHOSP_ID(hospId);
		}
		//DLL文件名
		String dllAddr = dllServer.getDLL_ADDRESS();
		if(!"".equals(dllAddr)){
			dllAddr = dllAddr.trim();
			dllServer.setDLL_ADDRESS(dllAddr);
		}
		
		String id = dllServer.getID();//编辑id
		if(!"".equals(id)){ //去编辑
			dllServerManager.edit(dllServer);
		}else{		//去增加
			dllServer.setID(get32UUID()); //设置ID
			dllServerManager.save(dllServer);
		}
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		//mv.setViewName("redirect:/dllserver/list.do");
		return mv;
	} 
	
	/**
	 * 删除一个配置
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteD")
	@Rights(code="dllserver/deleteD")
	@ResponseBody
	public Object deleteD() throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		dllServerManager.delete(pd);
		String errInfo = "success";
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 批量删除
	 * @return
	 */
	@RequestMapping(value="/deleteAllD")
	@Rights(code="dllserver/deleteAllD")
	@ResponseBody
	public Object deleteAllD() {
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = null;
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String IDS = pd.getString("IDS");
			if(null != IDS && !"".equals(IDS)){
				String Array_IDS[] = IDS.split(",");
				dllServerManager.deleteAll(Array_IDS);
				errInfo = "success";
				pd.put("msg", "ok");
			}else{
				errInfo = "failed";
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			errInfo = "failed";
			logger.error(e.toString(), e);
		} finally {
			errInfo = "failed";
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(pd, map);		
	}
	
	/**
	 * 去修改dll配置页
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/toEdit")
	@Rights(code="dllserver/toEdit")
	public ModelAndView goEdit() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = dllServerManager.findById(pd);	//根据ID读取
		mv.setViewName("pay/dllserver/dll_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	
	

}
