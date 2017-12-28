package com.ts.controller.mts.mtsdict;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataType;
import com.ts.entity.mts.MtsDict;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.matchrule.MtsDictManger;
import com.ts.service.mts.mtsdict.MtsDictManager;
import com.ts.service.system.role.RoleManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;

import net.sf.json.JSONObject;

/**
 * 
 * @类名称: LoadRuleController
 * @类描述: Redis数据装载规则维护
 * @作者:李巍
 * @创建时间:2016年10月8日 下午2:04:26
 */
@Controller
@RequestMapping(value = "/mtsDict")
public class MtsDictController extends BaseController {

	String menuUrl = "mtsDict/listMtsDict.do"; // 菜单地址(权限用)
	@Resource(name = "mtsDictService")
	private MtsDictManager mtsDictService;
	@Resource(name = "roleService")
	private RoleManager roleService;
	@Resource(name = "DictMtsService")
	private MtsDictManger dictMtsService;
	@Resource(name = "DataClassService")
	private DataClassManger dataClassService;
	@Resource(name = "DataTypeService")
	private DataTypeManger dataTypeService;

	// ====================Excel本体数据导入====================
	/**
	 * 打开上传EXCEL页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goUploadExcel")
	public ModelAndView goUploadExcel() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/loadrule/uploadexcel");
		return mv;
	}

	/**
	 * 显示规则列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMtsDict")
	public ModelAndView listMtsDict(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		String dataClassCode = pd.getString("DATA_CLASS_CODE");	//聚类
		if(dataClassCode != null && !"".equals(dataClassCode)){
			pd.put("DATA_CLASS_CODE", dataClassCode);
		} 
		page.setPd(pd);
		List<PageData> listMtsDict = mtsDictService.listMtsDict(page); // 列出用户列表
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		mv.setViewName("mts/mtsdict/mts_dict_list");
		mv.addObject("listMtsDict", listMtsDict);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 
	 * @方法名称: goAddDict
	 * @功能描述: 新增字典
	 * @作者:李巍
	 * @创建时间:2017年3月14日 上午9:10:06
	 * @return
	 * @throws Exception ModelAndView
	 */
	@RequestMapping(value = "/goAddDict")
	public ModelAndView goAddDict() throws Exception {
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
//			return null;
//		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType>	dataTypeList =dataTypeService.listAllDataType();//列出所有标化类型
		mv.setViewName("mts/mtsdict/mts_dict_edit");
		mv.addObject("msg", "saveDict");
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		return mv;
	}

	//
	/**
	 * 保存用户
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveDict")
	public ModelAndView saveDict() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "保存MTSDict");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String mruleid = mtsDictService.maxRule();
		int Lid = Integer.parseInt(mruleid);
		pd.put("DID", String.valueOf(Lid + 1));
		pd.put("OPDATE", new Date());
		// 添加操作
		mtsDictService.saveDict(pd);
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 去修改加载规则页面
	 * 
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditDict")
	public ModelAndView goEditDict() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		// 查询规则实体
		pd = mtsDictService.findDictById(pd);
		List<MtsDataClass> dataClassList = dataClassService.listAllDataClass();// 列出所有聚类
		List<MtsDataType>	dataTypeList =dataTypeService.listClassDataType(pd.getString("DATACLASS"));
		mv.setViewName("mts/mtsdict/mts_dict_edit");
		mv.addObject("msg", "editDict");
		mv.addObject("pd", pd);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("dataTypeList", dataTypeList);
		return mv;
	}

	/**
	 * 修改数据字典
	 */
	@RequestMapping(value = "/editDict")
	public ModelAndView editDict() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "修改DICT");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("OPDATE", new Date());
		mtsDictService.editDict(pd);
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 删除匹配规则
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delMtsDict")
	public void delMtsDict(PrintWriter out) throws Exception {
		// 校验权限
		logBefore(logger, Jurisdiction.getUsername() + "删除MtsDict");
		PageData pd = new PageData();
		pd = this.getPageData();
		String did = pd.getString("did");
		mtsDictService.deleteMtsDict(did);
		out.write("success");
		out.close();
	}
	
	
	
	/**
	 * ajax查询字典表
	 * 
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/selByName")
	public String selByName(String name) throws Exception {
		JSONObject jso = new JSONObject();
		PageData pd = mtsDictService.selByName(name);
		jso = JSONObject.fromObject(pd);
		return jso.toString();
	}

}
