package com.dhcc.ts.library.bookService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.util.StringUtil;
import com.dhcc.common.util.SystemConfig;
import com.dhcc.ts.library.solr.SolrQueryGroupField;
import com.dhcc.ts.library.solr.SolrQueryLibrary;
import com.dhcc.ts.library.solr.SolrQueryModel;
import com.dhcc.ts.library.solr.SolrQuerySort;
import com.dhcc.ts.library.translate.TranslateInit;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/** 
* @作者:lwh
* @创建时间：2017年4月24日 上午8:55:00
* @类说明：基于MTS图书情报服务
*/
public class BookServiceAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String searchMenu;				//检索条件1：0：全部、1：医学百科知识、2：教科书、3：中文期刊、4：外文期刊、5：循证医学、6：影像资料
	private String searchText;				//检索条件2搜索信息
	private String ispage;				//是否分页"1"分页、"0"不分页
	private int pageNo;				//第几页
	private int pageSize; //每页几条
	private String keyWord;
	private String sortFields;
    private InputStream inputStream;
    
    
	
	/**  
    * @标题: findBookServiceMTS
    * @描述: 图书情报服务搜索
    * @作者 lwh
    */  
    public void findBookServiceMTS(){
    	try{
			Map<String, Object> libraryMap = this.findLibrary(searchMenu, searchText, keyWord, sortFields,"0",ispage, pageNo, pageSize);
			JSONObject job = new JSONObject().fromObject(libraryMap);
	    	ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
	    	PrintWriter pw = ServletActionContext.getResponse().getWriter();
			pw.print(job);
			pw.flush();
			pw.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    /**  
	* @标题: findLibrary  
	* @描述: TODO查询图情服务数据
	* @作者 EbaoWeixun
	* @param searchMenu 类型
	* @param searchText 搜索框输入
	* @param keyWord    关键词                    格式：字段1,关键词1@字段2,关键词2
	* @param sortFields 排序字段及排序     格式：排序字段,排序
	* @param ispage     是否分页"1"分页、"0"不分页
	* @param pageNo     第几页
	* @param pageSize   每页几条
	* @return 
	* Map<String, Object>
	* status--0：失败，1：成功
	* data--查询的结果集
	* groupData--分组的返回参数
	*/  
	private  Map<String,Object> findLibrary(String searchMenu,String searchText,String keyWord,String sortFields,String returnFlag,String ispage,int pageNo,int pageSize){
		 Map<String,Object> map=new HashMap<String,Object>();
		 Map<String,String> queryValue=new HashMap<String,String>();//搜索框
		 Map<String, Object> mapGroupField=SolrQueryGroupField.getGroup(searchMenu);//分组
		 Map<String, SolrQuerySort> sortMap=new HashMap<String, SolrQuerySort>();//排序
		 Map<String,String> mustHaveValue=new HashMap<String,String>();//关键词
		 //返回字段  0全返回1不返回detailes
		 List<String> returnField=new LinkedList<String>();
		 if("1".equals(returnFlag)){
			returnField=SolrQueryGroupField.returnField; 
		 }
		 SolrQueryLibrary.init();
		 //排序
		 if(!StringUtil.isNullOrEmpty(sortFields)){
			 String[] sfs=sortFields.split("#");
			 for(String sf:sfs){
				 String[] sortField=sf.split(",");
				 sortMap.put(sortField[0],"0".equals(sortField[1])?SolrQuerySort.asc:SolrQuerySort.desc);
			 }
			 
		 }
		 //搜索框输入
		 if(!StringUtil.isNullOrEmpty(searchText)){
			 Map<String, String> tranMap=TranslateInit.getTransResult(searchText);
			 queryValue.put("title_chs", "\"*"+tranMap.get("resultZh")+"*\"");
			 queryValue.put("title_en", "\"*"+tranMap.get("resultEn")+"*\"");
			 queryValue.put("detailes",tranMap.get("resultEn")+"@#&"+tranMap.get("resultZh"));
		 }else{
			 queryValue.put("*", "*");
		 }
		 //类型
		 if(!"0".equals(searchMenu)){
			 mustHaveValue.put("resource_type", searchMenu);
		 }
		 //关键字
		 if(!StringUtil.isNullOrEmpty(keyWord)){
			
			 String[] keyWords=keyWord.split("@");
			 for(String kkk:keyWords){
				 String[] kk=kkk.split(",");
				 if(!StringUtil.isNullOrEmpty(mustHaveValue.get(kk[0]))){
					 mustHaveValue.put(kk[0],mustHaveValue.get(kk[0])+" "+kk[1]);
				 }else{
					 if("author_en".equals(kk[0]) || "source_en".equals(kk[0]) || "descriptor_en".equals(kk[0])){
						 mustHaveValue.put(kk[0],"\""+kk[1]+"\"");
					 }else{
						 mustHaveValue.put(kk[0],kk[1]);
					 }
			     }
			 }
		 }
		
		 map=SolrQueryLibrary.queryLibrary(queryValue, mustHaveValue,returnField, sortMap, mapGroupField, ispage, pageNo, pageSize);
		 System.out.println(map);
		 return map;
	}
	/**  
	* @标题: findLibDetails  
	* @描述: TODO查询图情服务详情
	* @作者 EbaoWeixun
	*/  
	public void findLibDetails(){
		
		SolrQueryModel sm=queryLibById(searchMenu);
		JSONObject json = JSONObject.fromObject(sm);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**  
	* @标题: findLibVideo  
	* @描述: TODO图情视频
	* @作者 EbaoWeixun
	* @return
	*/  
	public String findLibVideo(){
		System.out.println(searchMenu+"---");
        SolrQueryModel sm=queryLibById(searchMenu);
	    //File file = new File("D:\\2073.flv");
       // System.out.println("文件名："+sm.getDetailes());
        try{	
        	if(!StringUtil.isNullOrEmpty(sm.getDetailes())){
        	    File file = new File("/data/myftp/ftper/library/1/"+sm.getDetailes()); 

            	inputStream=new FileInputStream(file);
        	}

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    	return "success";
	}
    /**  
    * @标题: queryLibById  
    * @描述: TODOid查询图情服务信息
    * @作者 EbaoWeixun
    * @param qlbi_id
    * @return
    */  
    private SolrQueryModel queryLibById(String qlbi_id){
    	SolrQueryLibrary.init();
	    Map<String,String> queryValue=new HashMap<String,String>();
	    queryValue.put("qlbi_id",qlbi_id );
	    Map<String,String> mustHaveValue = new HashMap<String ,String>();
		Map<String, SolrQuerySort> sortMap = new HashMap<String ,SolrQuerySort>();
		Map<String, Object> mapGroupField = new HashMap<String, Object>();
		List<String> listreturn = new ArrayList<String>();
		SolrQueryModel sm=new SolrQueryModel();
		try{
		    Map<String,Object> map=SolrQueryLibrary.queryLibrary(queryValue, mustHaveValue, listreturn, sortMap, mapGroupField, "1", 0, 1);
		    if(map.get("data")!=null){
                List<SolrQueryModel> list=(List<SolrQueryModel>) map.get("data");
                if(!list.isEmpty()){
    		        sm=list.get(0);
    	    	}
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		return sm;
    }
    
	public String getSearchMenu() {
		return searchMenu;
	}
	public void setSearchMenu(String searchMenu) {
		this.searchMenu = searchMenu;
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getIspage() {
		return ispage;
	}
	public void setIspage(String ispage) {
		this.ispage = ispage;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getSortFields() {
		return sortFields;
	}
	public void setSortFields(String sortFields) {
		this.sortFields = sortFields;
	}

}
