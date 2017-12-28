package com.ts.controller.mts.matchrule;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataType;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.matchrule.impl.DataClassService;
import com.ts.service.mts.matchrule.impl.DataRelationService;
import com.ts.service.mts.matchrule.impl.DataTypeService;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/typeClss")
public class DataClassTypeController extends BaseController  {
	
	String menuUrl = "typeClss/listDataClss.do";
	
	@Resource(name = "DataClassService")
	private DataClassService dataClassService;
	@Resource(name = "DataTypeService")
	private DataTypeService dataTypeService;
	@Resource(name = "DataRelationService")
	private DataRelationService dataRelationService;
	@Resource(name = "mapConfigService")
	private MapConfigService mapConfigService;
	
	
	/**
	 * 查询聚类列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listDataClss")
	public ModelAndView listDataClss(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
	
		page.setPd(pd);
		List<PageData> dataClassList = dataClassService.DataClasslistPage(page); // 列出匹配规则列表

		mv.setViewName("mts/mtstycl/data_class_list");
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 查询相应标化类型列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listDataType")
	public ModelAndView listDataType(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
	
		page.setPd(pd);
		List<PageData> dataTypeList = dataTypeService.DataTypelistPage(page); // 列出匹配规则列表

		mv.setViewName("mts/mtstycl/data_type_list");
		mv.addObject("dataTypeList", dataTypeList);
		mv.addObject("pd", pd);		
		return mv;
	}
	
	/**
	 * 去新增聚类页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddDC")
	public ModelAndView goAddDC() throws Exception {
		/*if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
			return null;
		} */// 校验权限
		ModelAndView mv = this.getModelAndView();		
		mv.setViewName("mts/mtstycl/data_class_edit");
		mv.addObject("msg", "saveDC");		
		return mv;
	}
	
	/**
	 * 去新增标化类型页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddDT")
	public ModelAndView goAddDT() throws Exception {
		/*if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
			return null;
		} */// 校验权限
		
		PageData pd = new PageData();
		pd = this.getPageData();
		ModelAndView mv = this.getModelAndView();	
		
		mv.setViewName("mts/mtstycl/data_type_edit");
		mv.addObject("pd", pd);	
		mv.addObject("msg", "saveDT");		
		return mv;
	}
	
	/**
	 * 保存聚类
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveDC")
	public ModelAndView saveDC() throws Exception {

		logBefore(logger, Jurisdiction.getUsername() + "保存dataClass");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String classCode =  pd.getString("classcode");
		String className =  pd.getString("classname");
		String clid = dataClassService.maxDataClass();
		int Lid = Integer.parseInt(clid);
		String Zclid = String.valueOf(Lid + 1);
		
		MtsDataClass mtcode=dataClassService.codeByCode(classCode);
		MtsDataClass mtname=dataClassService.nameByName(className);
		
		if(mtcode!=null ){
			
			String maxCode=dataClassService.maxDataCode();
			if(maxCode.length()==1){
				maxCode='0'+maxCode;
			}
			mv.addObject("msg", "新增失败！该聚类代码已存在，请填写大于"+maxCode+"的聚类代码！");

		}else if(mtname!=null ){
			
			mv.addObject("msg", "新增失败！该聚类名称已存在！");
		}else{
			MtsDataClass mtsDataClass=new MtsDataClass();
			mtsDataClass.setDATA_CLASS_ID(Zclid);
			mtsDataClass.setDATA_CLASS_CODE(classCode);
			mtsDataClass.setDATA_CLASS_NAME(className);
	
			dataClassService.addDataClass(mtsDataClass);
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 保存标化类型
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveDT")
	public ModelAndView saveDT(Page page) throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
//			return null;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "保存dataType");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String typecode =  pd.getString("typecode");
		String typename =  pd.getString("typename");
		String clssid =  pd.getString("DATA_CLASS_ID");
		String dtid = dataTypeService.maxDataType();
		String rlid = dataRelationService.maxDataRelation();
		int Ldtid = Integer.parseInt(dtid);
		int Lrlid = Integer.parseInt(rlid);
		String Zdtid = String.valueOf(Ldtid + 1);
		String Zrlid = String.valueOf(Lrlid + 1);
		
		MtsDataType mtcode=dataTypeService.codeByCode(typecode);
		MtsDataType mtname=dataTypeService.nameByName(typename);
		
		if(mtcode!=null ){
			String maxCode=dataTypeService.maxDataCode();
			if(maxCode.length()==1){
				maxCode='0'+maxCode;
			}
			mv.addObject("msg", "新增失败！该标化代码已存在，请填写大于"+maxCode+"的标化代码！");

		}else if(mtname!=null){
			
			mv.addObject("msg", "新增失败！该标化名称已存在！");
		}else{
		
			MtsDataType mdt=new MtsDataType();
			mdt.setDATA_TYPE_ID(Zdtid);
			mdt.setDATA_TYPE_NAME(typename);
			mdt.setMEM_DATA_CODE(typecode);
			mdt.setDATA_VER("");
			
			pd.put("RELATION_ID", Zrlid);
			pd.put("DATA_CLASS_ID", clssid);
			pd.put("DATA_TYPE_ID", Zdtid);		
			page.setPd(pd);
			
			
			dataTypeService.addDataType(mdt);
			dataRelationService.addDataRelation(page);
			mapConfigService.ReLoadCache();//重新加载缓存
		}
		
		mv.setViewName("save_result");
		return mv;
	}

	
	/**
	 * 去修改聚类页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditDC")
	public ModelAndView goEditDC() throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
//			return null;
//		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String clid = pd.getString("clssid");
		MtsDataClass mdc=dataClassService.dataClassById(clid);
		
		mv.setViewName("mts/mtstycl/data_class_edit");
		mv.addObject("msg", "editDC");
		mv.addObject("mdc", mdc);
	

		return mv;
	}
	
	
	/**
	 * 去修改标化类型页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditDT")
	public ModelAndView goEditDT() throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
//			return null;
//		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String typeid = pd.getString("typeid");
		MtsDataType mdt=dataTypeService.findById(typeid);
		//String clssid=dataRelationService.findRelationid(typeid);
		//pd.put("DATA_CLASS_ID", clssid);
		mv.setViewName("mts/mtstycl/data_type_edit");
		mv.addObject("msg", "editDT");
		//mv.addObject("pd", "pd");
		mv.addObject("mdt", mdt);
	

		return mv;
	}
	
	/**
	 * 修改聚类
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editDC")
	public ModelAndView editDC() throws Exception {

		logBefore(logger, Jurisdiction.getUsername() + "修改dataClass");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String classCode =  pd.getString("classcode");
		String className =  pd.getString("classname");
		String classid =  pd.getString("clssid");
		
		MtsDataClass mtcode=dataClassService.codeByCode(classCode);
		MtsDataClass mtname=dataClassService.nameByName(className);
		
		if(mtcode!=null && !classid.equals(mtcode.getDATA_CLASS_ID())){
			
			String maxCode=dataClassService.maxDataCode();
			if(maxCode.length()==1){
				maxCode='0'+maxCode;
			}
			mv.addObject("msg", "修改失败！该聚类代码已存在，请填写大于"+maxCode+"的聚类代码！");

		}else if(mtname!=null && !classid.equals(mtname.getDATA_CLASS_ID())){
			
			mv.addObject("msg", "修改失败！该聚类名称已存在！");
		}else{

			MtsDataClass mtsDataClass=new MtsDataClass();
			mtsDataClass.setDATA_CLASS_ID(classid);
			mtsDataClass.setDATA_CLASS_CODE(classCode);
			mtsDataClass.setDATA_CLASS_NAME(className);
	
			dataClassService.editDataClass(mtsDataClass);
		}
		mv.setViewName("save_result");
		return mv;
	}

	
	/**
	 * 修改标化类型
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editDT")
	public ModelAndView editDT() throws Exception {

		logBefore(logger, Jurisdiction.getUsername() + "修改dataType");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String typecode =  pd.getString("typecode");
		String typename =  pd.getString("typename");
		String typeid =  pd.getString("typeid");
		
		MtsDataType mtcode=dataTypeService.codeByCode(typecode);
		MtsDataType mtname=dataTypeService.nameByName(typename);
		
		if(mtcode!=null && !typeid.equals(mtcode.getDATA_TYPE_ID())){
			
			String maxCode=dataTypeService.maxDataCode();
			if(maxCode.length()==1){
				maxCode='0'+maxCode;
			}
			mv.addObject("msg", "修改失败！该标化代码已存在，请填写大于"+maxCode+"的标化代码！");

		}else if(mtname!=null && !typeid.equals(mtname.getDATA_TYPE_ID())){
			
			mv.addObject("msg", "修改失败！该标化名称已存在！");
		}else{
		
			MtsDataType mdt=new MtsDataType();
			mdt.setDATA_TYPE_ID(typeid);
			mdt.setDATA_TYPE_NAME(typename);
			mdt.setMEM_DATA_CODE(typecode);
			mdt.setDATA_VER("");

			dataTypeService.editDataType(mdt);		
			mapConfigService.ReLoadCache();//重新加载缓存
		}
		mv.setViewName("save_result");
		return mv;
	}

	
	/**
	 * 删除聚类
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteDC")
	public void deleteDC(PrintWriter out) throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
//			return;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "删除dataClass");
		PageData pd = new PageData();
		pd = this.getPageData();
		String clssid = pd.getString("clssid");
		dataClassService.deleteDataClass(clssid);
		out.write("success");
		out.close();
	}
	
	/**
	 * 删除标化类型
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteDT")
	public void deleteDT(PrintWriter out) throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
//			return;
//		} // 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "删除dataType");
		PageData pd = new PageData();
		pd = this.getPageData();
		String typeid = pd.getString("typeid");
		dataTypeService.deleteDataType(typeid);
		dataRelationService.deleteDataRelation(typeid);
		mapConfigService.ReLoadCache();//重新加载缓存
		out.write("success");
		out.close();
	}
	
	/**
	 * ajax查询关系表
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selCount")
	public String selCount(String clssid) throws Exception {
		
		Integer cun=dataRelationService.relationCount(clssid);
		
		if (cun==0) {
			return "0";

		}
		return "1";
	}
	
	
	
	
}
