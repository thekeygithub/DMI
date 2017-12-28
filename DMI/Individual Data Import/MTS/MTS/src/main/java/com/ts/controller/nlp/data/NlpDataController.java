package com.ts.controller.nlp.data;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.OTerm;
import com.ts.entity.nlp.NlpData;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.impl.DataOTerm;
import com.ts.service.nlp.data.impl.NlpDataService;
import com.ts.service.nlp.data.impl.TermTypeService;
import com.ts.util.AppUtil;
import com.ts.util.CommonUtils;
import com.ts.util.Const;
import com.ts.util.FileDownload;
import com.ts.util.FileUpload;
import com.ts.util.Jurisdiction;
import com.ts.util.ObjectExcelRead;
import com.ts.util.PageData;
import com.ts.util.PathUtil;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value = "/nlpData")
public class NlpDataController extends BaseController {
	
	String menuUrl = "nlpData/listdatas.do"; //菜单地址(权限用)
	@Resource(name = "DataOTerm")
	private DataOTerm dataOTerm;
	
	@Resource(name = "NlpDataService")
	private NlpDataService nlpDataService;
	@Resource(name = "TermTypeService")
	private TermTypeService termTypeService;
	@Resource(name="DataClassService")
	private DataClassManger dataClassService;
	@Resource(name="MTSAreaService")
	private MTSAreaService mTSAreaService;
	
	private String completed = "1";
	
	/**
	 * 查询非医学术语列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listOTerms")
	public ModelAndView listOTerms(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		String flag = pd.getString("flag"); // 无用术语类型
		if (flag != null && !"".equals(flag)) {
			pd.put("FLAG", flag);
		}else{
			pd.put("FLAG", "1");
		}
		page.setPd(pd);
		List<PageData> oTermList =dataOTerm.findOtermlistPage(page); // 列出无用术语列表

		mv.setViewName("nlp/oterm/oterm_list");
		mv.addObject("oTermList", oTermList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**
	 * 查询标准术语列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listdatas")
	public ModelAndView listdatas(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		String nlptype = pd.getString("NLPTYPE"); // 术语类型
		String area = pd.getString("AREAID"); // 区域
		String classcode = pd.getString("CLASSCODE"); // 聚类
		pd.put("NLPTYPE", nlptype);		
		pd.put("AREAID", area);
		pd.put("CLASSCODE", classcode);
		
		
		page.setPd(pd);
		List<PageData> dataList =nlpDataService.findNlplistPage(page); // 列出术语列表
		List<MtsDataClass>	dataClassList =dataClassService.listAllDataClass();//列出所有聚类
		List<String> termList=termTypeService.findAllTerm();//列出所有术语类型
		MtsArea mts=new MtsArea();
		List<MtsArea> areaList=mTSAreaService.findMtsArea(mts);//列出所有区域

		mv.setViewName("nlp/nlpData/data_list");
		mv.addObject("dataList", dataList);
		mv.addObject("dataClassList", dataClassList);
		mv.addObject("termList", termList);
		mv.addObject("areaList", areaList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 非医学术语导出TXT
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/productDetailBurn_export")  
    @ResponseBody  
    public void productDetailBurn_export( HttpServletRequest request,  
            HttpServletResponse response) throws IOException {  
        //DbContextHolder.setDbType("isap");  
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		List<OTerm> strotm=new ArrayList<OTerm>();
		List<String> strs= new ArrayList<String>();
		try {
			strotm = dataOTerm.findByFlag("1");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		for(int i=0;i<strotm.size();i++){
			String term_name=strotm.get(i).getTERM_NAME();
			strs.add(term_name);
		}
		
   
            //导出txt文件   
            response.setContentType("text/plain");    
            String fileName="videolist";   
              try {   
               fileName = URLEncoder.encode("videolist", "UTF-8");   
              } catch (UnsupportedEncodingException e1) {   
               // TODO Auto-generated catch block   
               e1.printStackTrace();   
              }    
              response.setHeader("Content-Disposition","attachment; filename=" + fileName + ".txt");    
              BufferedOutputStream buff = null;     
              StringBuffer write = new StringBuffer();     
              String enter = "\r\n";     
              ServletOutputStream outSTr = null;   
              try {     
                    outSTr = response.getOutputStream(); // 建立     
                    
                    //String path = CommonUtils.getPropValue("redis.properties", "nlp.paths");
                   // String path = System.getProperty("java.io.tmpdir") + "\\poem.txt";
                   // File file = new File(path);
                    //FileOutputStream fos = new FileOutputStream(file);   
                    //Writer writer = new OutputStreamWriter(fos, "utf-8");
                    
                    buff = new BufferedOutputStream(outSTr);   
                    //把内容写入文件   
                    if(strs.size()>0){   
                     for (int i = 0; i < strs.size(); i++) {   
                      write.append(strs.get(i));                     
                      write.append(enter);     
                     }   
                    }   
                    buff.write(write.toString().getBytes("UTF-8"));     
                    buff.flush();     
                    buff.close();     
                  } catch (Exception e) {     
                   e.printStackTrace();     
                  } finally {     
	                   try {     
	                    buff.close();     
	                    outSTr.close();     
	                   } catch (Exception e) {     
	                    e.printStackTrace();     
	                   }     
                  }                 
 
        
    }  
	
	
	/****
	 * 标准术语导出术语到TXT
	 * @param request
	 * @param response
	 * @throws IOException
	 */

	
	
	@RequestMapping(value = "/nlpData_writeFile")  
    @ResponseBody  
	public void writeFile() throws IOException  {    
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		completed = "0";
		
		List<PageData> strs= new ArrayList<PageData>();		
		String enter = "\r\n";    
		FileWriter fw=null;
		BufferedWriter bw =null;
		try {
			strs = nlpDataService.findByType();
		} catch (Exception e2) {
			e2.printStackTrace();
		} 
		
		try   {    
			String rootPath = CommonUtils.getPropValue("nlp.properties", "nlp.txt");   
			String filePath = null;    
			if (rootPath.endsWith("/")) {       
				filePath = rootPath + "dic.txt"; 
				
			} else {        
				filePath = rootPath + "/dic.txt";    
				}     
			File dir = new File(filePath);    
			 fw = new FileWriter(dir);     
			 bw = new BufferedWriter(fw);   
			
			if(strs.size()>0){   
                for (int i = 0; i < strs.size(); i++) {   
                	bw.write(strs.get(i).getString("NLPNAME")); 
                	bw.write("\t"); 
                	bw.write(strs.get(i).getString("TERM_EN_NAME"));    
                	bw.write(enter);     
                }   
               }   			   
			bw.flush();     
			bw.close(); 
			} catch (Exception ex) {     
                ex.printStackTrace();     
               } finally {     
	                   try {     
	                	   bw.close();     
	                       fw.close();     
	                   } catch (Exception e) {     
	                    e.printStackTrace();     
	                   }    
               }
		completed = "1";
		} 
	
	/**
	 * 获取导出TXT是否完成的标识
	 * @return
	 */
	@RequestMapping(value = "/getComplemt")
	@ResponseBody
	public String getComplemt(){
		return this.completed;
	}
	
	/*
	public String readerFile() throws IOException  {    
		String access_token = "";    
		String filePath = System.getProperty("user.dir")+"/demo.txt";   
		FileInputStream fis = null;   
		try   { 

		   fis = new FileInputStream(filePath);     
		   InputStreamReader reader = new InputStreamReader(fis, "UTF-8");    
		   BufferedReader br = new BufferedReader(reader);    
		   String info = "";    
		   while ((info = br.readLine()) != null) {     
			   access_token = info;    
			   }     
		   br.close();    
		   fis.close();     
		   Logger.getLogger("wxpay").info("读取缓存,缓存地址["+filePath +"]-Access_token["+access_token+"]");    
		   return access_token;   
		   } catch (Exception ex) {     
			   Logger.getLogger("wxpay").info("读取缓存异常");            
			   return null;   
			   }  
		}
		*/
	
	/**标准术语打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("nlp/nlpData/uploadexcel");
		return mv;
	}
	
	/**标准术语下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "nlpDataexcel.xlsx", "nlpDataexcel.xlsx");
	}
	
	/**标准术语从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcelcol(
			@RequestParam(value="excel",required=false) MultipartFile file
			) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		NlpData ndata=new NlpData();
		List<String> errlist=new ArrayList<String>();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;								//文件上传路径
			String fileName =  FileUpload.fileUp(file, filePath, "nlpDataexcel");	//执行上传
			@SuppressWarnings("unchecked")
			List<PageData> listPd = (List)ObjectExcelRead.readExcelWithoutTitle(filePath, fileName, 1, 0, 0);		//执行读EXCEL操作,读出的数据导入List 1:从第2行开始；0:从第A列开始；0:第0个sheet
			/*存入数据库操作======================================*/
			
			/**
			 * 
			 * var0 :术语名称
			 * var1 :术语类型
			 * var2 :区域
			 * var3 :聚类			
			 */
			int bl=0;
			for(int i=0;i<listPd.size();i++){		
				bl=0;	
				if(listPd.get(i).getString("var0")!="" || !"".equals(listPd.get(i).getString("var0"))){
					ndata.setNLPNAME(listPd.get(i).getString("var0"));
					ndata.setNLPTYPE(listPd.get(i).getString("var1"));
					int areaid=(Double.valueOf(listPd.get(i).getString("var2"))).intValue();
				    String areacode=areaid+"";
					ndata.setAREAID(areacode);
					ndata.setCLASSCODE(listPd.get(i).getString("var3"));
					Date d=new Date();	
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
					String dx=df.format(d);
					ndata.setINTDATE(dx);
//					bl=nlpDataService.findByName(listPd.get(i).getString("var0"));
//					if(bl==0){
//						nlpDataService.addNLPData(ndata);
//					}else{
//						errlist.add(listPd.get(i).getString("var0"));
//					}
					
					nlpDataService.addNLPData(ndata);
				}
									
				
				
			}
			
			/*存入数据库操作======================================*/
			if(errlist.size()==0){
				mv.addObject("msg", "success");
			}else{
				mv.addObject("msg", "导入失败个数："+errlist.size()+"条！<br>失败原因：术语已存在！<br>失败术语列表："+errlist.toString());
			}
			
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	
	/**
	 * 批量删除
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteAllU")
	@ResponseBody
	public Object deleteAllU() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除NlpData");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String NLPIDS = pd.getString("NLPIDS");
		if(null != NLPIDS && !"".equals(NLPIDS)){
			String[] ArrayNLPIDS = NLPIDS.split(",");
			int[] ArrayIDS=new int[ArrayNLPIDS.length];			
			
			
			for(int i=0;i<ArrayNLPIDS.length;i++){				
				ArrayIDS[i]=Integer.parseInt(ArrayNLPIDS[i]);
			}
			nlpDataService.deleteAllU(ArrayIDS);	
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	

	@RequestMapping(value = "/updateTermType", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateTermType(@RequestBody String TermType) {	
		try {
			
			JSONObject inOTerm = JSONObject.fromObject(TermType);		
			String  nlpname = null;		
			String  nlptype = null;
			String  areaid = null;
			String  classcode = null;
			String  upcode = null;  //操作标识：1 新增 2 修改 3 删除
			Date d=new Date();	
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
			String dx=df.format(d);
			if (inOTerm.containsKey("NLPNAME")) {				
				nlpname=URLDecoder.decode(inOTerm.getString("NLPNAME"),"UTF-8");				
			}	
			if (inOTerm.containsKey("NLPTYPE")) {				
				nlptype=URLDecoder.decode(inOTerm.getString("NLPTYPE"),"UTF-8");				
			}	
			if (inOTerm.containsKey("AREAID")) {				
				areaid=inOTerm.getString("AREAID");				
			}
			if (inOTerm.containsKey("CLASSCODE")) {				
				classcode=inOTerm.getString("CLASSCODE");				
			}
			if (inOTerm.containsKey("UPCODE")) {				
				upcode=inOTerm.getString("UPCODE");				
			}
			
			if (nlpname != null&& nlptype!=null) {
				
				
				NlpData ndata=new NlpData();
				ndata.setAREAID(areaid);
				ndata.setCLASSCODE(classcode);
				ndata.setNLPNAME(nlpname);
				ndata.setNLPTYPE(nlptype);
				ndata.setINTDATE(dx);
				
				if(upcode=="1"||"1".equals(upcode)){
					
					nlpDataService.addNLPData(ndata);
				}else{
					
				    nlpDataService.deleteNLPData(ndata);
				   
				}
				
				
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
