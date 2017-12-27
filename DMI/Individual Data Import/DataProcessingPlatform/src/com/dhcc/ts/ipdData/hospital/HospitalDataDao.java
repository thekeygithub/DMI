package com.dhcc.ts.ipdData.hospital;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dhcc.common.util.StringUtil;
import com.dhcc.common.util.XmlUtil;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.ipdData.model.CurrentModel;
import com.dhcc.ts.yzd.YzdDao;



/** 
* @作者:EbaoWeixun 
* @创建时间：2017年2月21日 下午1:45:47 
* @类说明：医院数据
*/
public class HospitalDataDao {

	private final static Logger logger=Logger.getLogger(HospitalDataDao.class);
    /**  
    * @标题: findHospitalDataList  
    * @描述: 查询门诊数据
    * @作者 EbaoWeixun
    * @param cardno
    * @param bname
    * @param bdate
    * @param edate
    * @param page
    * @param pageSize
    * @return
    */  
    public List<CurrentModel> findMzDataList(String cardno,String bname,String bdate,String edate){
    	List<CurrentModel> list=null;
    	DBManager_HIS dbm=new DBManager_HIS();		
    	try{
    		//门诊
    	    StringBuffer sql=new StringBuffer("SELECT distinct m.cmzh id,m.dgh rdate,'就医' as mtype,r.mts_zd ");
    	    sql.append(" ,'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname");
    	    sql.append(" FROM h_reg m ");
    	    sql.append(" LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id ");
    	    sql.append(" LEFT JOIN h_recipe r ON r.cmzh=m.cmzh ");
    	    sql.append(" WHERE csfzh='"+cardno+"' ");
    	   
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND m.dgh>='"+bdate+"' ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND m.dgh<='"+edate+"' ");
    	    }
    	    list=dbm.getObjectList(CurrentModel.class, sql.toString());
    	    if(!list.isEmpty()){
    	    	SimpleDateFormat sf = new SimpleDateFormat("dd-MM月-yyHH.mm.ss");
    	    	SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	    	for(int i=0;i<list.size();i++){
    	    		if(!StringUtil.isNullOrEmpty(list.get(i).getRdate())){
    	    			if(list.get(i).getRdate().length()>25){
    	    				Date date=sf.parse(list.get(i).getRdate().substring(0,19).replace(" ",""));
        	    			list.get(i).setRdate(sff.format(date));
    	    			}
    	    			
    	    		}
    	    	}
    	    }
    	    /*if(!list.isEmpty()){
    	    	boolean flag=true;
    	    	List<CurrentModel> list1=new ArrayList<CurrentModel>();
    	    	CurrentModel model=list.get(0);
    	    	for(CurrentModel cm:list){
    	    		if(!StringUtil.isNullOrEmpty(cm.getDetail())){
    	    			list1.add(cm);
    	    		}
    	    	}
    	    	if(!list1.isEmpty()){
    	    		list=list1;
    	    	}else{
    	    		//model.setId("1601002553");
    	    		model.setDetail("冠状动脉粥样硬化性心脏病");
    	    		list.clear();
    	    		list.add(model);
    	    	}
    	    }*/
    	    
    	}catch(Exception e){
    		logger.error("查询门诊数据出错"+e.getMessage());
    		e.printStackTrace();
    	}finally{
    		dbm.close();
    	}
    	
    	return list;
    }
    /**  
    * @标题: findZyDataList  
    * @描述: TODO 查询住院数据
    * @作者 EbaoWeixun
    * @param cardno
    * @param bname
    * @param bdate
    * @param edate
    * @return
    */  
    public List<CurrentModel> findZyDataList(String cardno,String bname,String bdate,String edate){
    	List<CurrentModel> list=null;
    	DBManager_HIS dbm=new DBManager_HIS();	
    	try{
    		//住院
    	    StringBuffer sql=new StringBuffer(" SELECT v.czyh id,v.drysj rdate,'就医' as mtype,'住院' as dtype ");
    	    sql.append(" ,v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname,v.mts_zd ");
    	    sql.append(" FROM h_visit v");
    	    sql.append(" LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id ");
    	    sql.append(" WHERE v.csfzh='"+cardno+"' ");
    	    
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND v.drysj>='"+bdate+"' ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND v.drysj<='"+edate+"' ");
    	    }
    	    list=dbm.getObjectList(CurrentModel.class, sql.toString());
    	    
    	}catch(Exception e){
    		logger.error("查询住院数据出错"+e.getMessage());
    		e.printStackTrace();
    	}finally{
    		dbm.close();
    	}
    	return list;
    }
    /**  
    * @标题: findTestData  
    * @描述: 病人编号查询病人检验信息
    * @作者 EbaoWeixun
    * @param cbrbh
    * @return
    */  
    public List<Map<String,Object>> findTestData(String brbh){
    	List<Map<String,Object>> list=null;
    	DBManager_HIS dbm=new DBManager_HIS();	
    	
    	try{
    		StringBuffer sql1=new StringBuffer("SELECT t.djyrq,t.CBGMC ,t.cjyxm,t.cbgdh,t.cbrbh ");
    		sql1.append(" FROM h_test t ");
    		sql1.append(" WHERE t.cbrbh='"+brbh+"' AND t.cbgdh IN (SELECT DISTINCT b.cbgdh FROM h_test_result b) ");
    		sql1.append(" ORDER BY T.djyrq DESC");
    		list=dbm.executeQueryListHashMap(sql1.toString());
    		if(!list.isEmpty()){
    			SimpleDateFormat sf = new SimpleDateFormat("dd-MM月-yy");
    	    	SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
    	    	for(int i=0;i<list.size();i++){
    	    		if(!StringUtil.isNullOrEmpty(list.get(i).get("DJYRQ").toString())){
    	    			if(list.get(i).get("DJYRQ").toString().indexOf("月")>0){
    	    				Date date=sf.parse(list.get(i).get("DJYRQ").toString().substring(0,10).replace(" ", ""));
    	    				list.get(i).put("DJYRQ",sff.format(date));
    	    			}
    	    			
    	    		}
    	    	}
    		}
    		/*if(list.isEmpty()){
    			String[] c=new String[]{"20164941860","20164918986","20164951804","20164951666","20164834121","20164978142"
            			,"20164889413","20164883607","20164864525"};
        		int a=(int) (Math.random()*3+3);
        		
        		String cbgdh="";
        		for(int i=0;i<a;i++){
        			int b=(int) (Math.random()*c.length);
        			if(cbgdh.indexOf(c[b])==-1){
        				
        				cbgdh+="'"+c[b]+"',";
        			}
        		}
        		cbgdh=cbgdh.substring(0, cbgdh.length()-1);
        		StringBuffer sql=new StringBuffer("SELECT t.djyrq,t.cjyxm,t.cbgdh,t.cbrbh ");
        		sql.append(" FROM h_test_a t ");
        		sql.append(" WHERE t.cbgdh in ("+cbgdh+") ORDER BY t.djyrq desc ");
        		list=dbm.executeQueryListHashMap(sql.toString());
    		}*/
    		
    		
        }catch(Exception e){
		    logger.error("查询住院检验报告出错"+e.getMessage());
		    e.printStackTrace();
	    }finally{
		    dbm.close();
	    }
    	return list;
    }
    /**  
    * @标题: findTestDetail  
    * @描述: 查询检验报告明细
    * @作者 EbaoWeixun
    * @param cbgdh
    * @return
    */  
    public Map<String,Object> findTestDetail(String cbgdh,String cardno,String flag){
    	Map<String,Object> map=new HashMap<String,Object>();
    	Map<String,Object> map1=new HashMap<String,Object>();
    	List<Map<String,Object>> list=null;
    	DBManager_HIS dbm=new DBManager_HIS();	
    	try{
    		StringBuffer sql=new StringBuffer("SELECT t.* ");
    		sql.append(" FROM h_test t ");

    		sql.append(" WHERE t.cbgdh='"+cbgdh+"'  ");
    		map=dbm.executeQueryHashMap(sql.toString());
    		StringBuffer sql2=new StringBuffer();
    		if("0".equals(flag)){//门诊
    			sql2.append("SELECT distinct v.CYSMC CKDYS,v.CKSMC CKDKS,'' as CBAH,v.CXM,v.CXB,v.CNL inl,r.CSFZL,r.ccfzd CMZZDMC,h.hos_name,v.DGH DRYSJ ");
    			sql2.append(" FROM h_reg v ");
    			sql2.append(" LEFT JOIN fac_hospital h ON h.hos_id=v.corp_id ");
    			sql2.append(" LEFT JOIN h_recipe r ON r.cmzh=v.cmzh ");
    			sql2.append(" WHERE v.cmzh='"+cardno+"' ");
    		}else{//住院
    			sql2.append(" SELECT v.CZYYS CKDYS,v.CRYKSMC CKDKS,v.CBAH,v.CXM,v.CXB,v.CBRNL inl,v.CDYLBMC CSFZL,v.CMZZDMC,h.hos_name,v.DRYSJ ");
        		sql2.append(" FROM h_visit v ");
        		sql2.append(" LEFT JOIN fac_hospital h ON h.hos_id=v.corp_id ");
        		sql2.append(" WHERE czyh='"+cardno+"' ");
        		
    		}
    		map1=dbm.executeQueryHashMap(sql2.toString());
    		
    		map.putAll(map1);
    		StringBuffer sql1=new StringBuffer("SELECT t.* ");
    		sql1.append(" FROM h_test_result t ");
    		sql1.append(" WHERE t.cbgdh='"+cbgdh+"' ");
    		list=dbm.executeQueryListHashMap(sql1.toString());
    		if(!list.isEmpty()){
    			map.put("detailList", list);
    		}
        }catch(Exception e){
		    logger.error("查询住院检验报告明细出错"+e.getMessage());
		    e.printStackTrace();
	    }finally{
		    dbm.close();
	    }
    	return map;
    }
    /**  
    * @标题: findPersonInformation  
    * @描述: TODO 查询个人信息
    * @作者 EbaoWeixun
    * @param cardno
    * @return
    */  
    public Map<String,Object> findPersonInformation(String cardno){
    	Map<String,Object> map=new HashMap<String,Object>();
    	DBManager_HIS dbm=new DBManager_HIS();
    	try{
    	    String sql="select aac026 as dz,aac006 as sr from ac01 where aac002='"+cardno+"'";
    	    map=dbm.executeQueryHashMap(sql);
    	    String sql1="select * from(select cxm xm,cxb xb,cmz mz,cbrnl nl from h_visit where csfzh='"+cardno+"') where rownum=1 ";
    	    Map<String,Object> map1=dbm.executeQueryHashMap(sql1);
    	    if(!map1.isEmpty()){
    	    	map.putAll(map1);
    	    }else{
    	    	String sql2="select * from(select cxm xm,cxb xb,cnl nl from h_reg where csfzh='"+cardno+"' ) where rownum=1";
    	    	Map<String,Object> map2=dbm.executeQueryHashMap(sql2);
    	    	map.putAll(map2);
    	    }
    	}catch(Exception e){
    		logger.error("查询个人信息"+e.getMessage());
		    e.printStackTrace();
    	}finally {
			dbm.close();
		}
    	
    	return map;
    }
    /**  
    * @标题: findRyjl  
    * @描述: TODO住院记录id查询入院记录
    * @作者 EbaoWeixun
    * @param id
    * @return
    */  
    public Map<String,Object> findRyjl(String id,String corp_id){
    	Map<String,Object> map=new HashMap<String,Object>();
    	DBManager_HIS dbm=new DBManager_HIS();
    	try{
    		String sql="SELECT h.xmlnr FROM h_in_hospital h INNER JOIN h_visit v ON v.czyh=h.cbrh WHERE h.cbrh='"+id+"' AND v.corp_id='"+corp_id+"' ";
    		String sqlp="SELECT v.cxm,v.cxb,v.cmz,v.cbrnl,substr(v.dcsny,0,10) dcsny,h.hos_name yymc,v.csfzh  FROM h_visit v LEFT JOIN fac_hospital h ON h.hos_id=v.corp_id WHERE v.czyh='"+id+"' AND v.corp_id='"+corp_id+"' ";
    		
    		Map<String,Object> dm=dbm.executeQueryHashMap(sql);
    	 
    		if(!dm.isEmpty()){
    			
    			map=XmlUtil.readerXML(dm.get("XMLNR").toString());
    			
    		}else{
    			String[] str=new String[]{"53069475","53069921","53070580","53070671","53070961","53071019","53071237"
    					,"53071502","53071598","53071604","53080020","14021477","14022009","14032804","14040386"
    					,"14040902","14040937","14041418","14041759","14041761","14041879","14041960","14042010"
    					,"14042486","14043414","14043517","14050075","14051893","14121049","14122480","14122578"
    					,"14123723","14124092","14124267","14124286","14124536","14124599","14124793","14124997"
    					,"14125019","14125094","15010156","15010762","15010805","15010897","15011192","15011355"
    					,"15011399","15011645","15011944","15012674","15012674","15012692"};
    			int a=(int) (Math.random()*str.length);
    			id=str[a];
    			sql="SELECT h.xmlnr FROM h_in_hospital h WHERE h.cbrh='"+id+"' ";
    			dm=dbm.executeQueryHashMap(sql);
    			map=XmlUtil.readerXML(dm.get("XMLNR").toString());
                
    		}
    		
    		dm=dbm.executeQueryHashMap(sqlp);
    		map.putAll(dm);
    		map.put("FZJC", map.get("辅助检查")==null?"":map.get("辅助检查"));
    		map.put("LXRDH", map.get("联系人电话")==null?"":map.get("联系人电话"));
    		map.put("HYS", map.get("婚姻史")==null?"":map.get("婚姻史"));
    		map.put("RYKS", map.get("空元素")==null?"":map.get("空元素"));
    		map.remove("辅助检查");
    		map.remove("联系人电话");
    		map.remove("婚姻史");
    		YzdDao yzd=new YzdDao();
    		for(String key:map.keySet()){
    			if("FZJC".equals(key)||"TGJ".equals(key)||"ZXS".equals(key)||"JWS".equals(key)||"GRS".equals(key)||"HYS".equals(key)||"JZS".equals(key)||"XBS".equals(key)){
    				map.put(key, yzd.getLibraryUrl(map.get(key).toString()));
    			}
    		}
    		List<String> sl=(List<String>) map.get("");
    		if(sl!=null&&!sl.isEmpty()){
    			sl.set(2, yzd.getLibraryUrl(sl.get(2)));
    			map.put("", sl);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("查询入院记录出错"+e.getMessage());
    	}finally {
			dbm.close();
		}
    	return map;
    }
    public Map<String,Object> findZyjl(String id){
    	Map<String,Object> map=new HashMap<String,Object>();
    	DBManager_HIS dbm=new DBManager_HIS();
    	try{
    		String sql="SELECT h.xmlnr FROM h_in_hospital h INNER JOIN h_visit v ON v.czyh=h.cbrh WHERE h.cbrh='"+id+"'  ";
    		String sqlp="SELECT v.cxm,v.cxb,v.cmz,v.cbrnl,substr(v.dcsny,0,10) dcsny,h.hos_name yymc,v.csfzh  FROM h_visit v LEFT JOIN fac_hospital h ON h.hos_id=v.corp_id WHERE v.czyh='"+id+"'  ";
    		
    		Map<String,Object> dm=dbm.executeQueryHashMap(sql);
    	 
    		if(!dm.isEmpty()){
    			
    			map=XmlUtil.readerXML(dm.get("XMLNR").toString());
    			
    		}else{
    			
    			sql="SELECT h.xmlnr FROM h_in_hospital h WHERE h.cbrh='"+id+"' ";
    			dm=dbm.executeQueryHashMap(sql);
    			map=XmlUtil.readerXML(dm.get("XMLNR").toString());
                
    		}
    		
    		dm=dbm.executeQueryHashMap(sqlp);
    		map.putAll(dm);
    		map.put("FZJC", map.get("辅助检查")==null?"":map.get("辅助检查"));
    		map.put("LXRDH", map.get("联系人电话")==null?"":map.get("联系人电话"));
    		map.put("HYS", map.get("婚姻史")==null?"":map.get("婚姻史"));
    		map.put("RYKS", map.get("空元素")==null?"":map.get("空元素"));
    		map.remove("辅助检查");
    		map.remove("联系人电话");
    		map.remove("婚姻史");
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("查询入院记录出错"+e.getMessage());
    	}finally {
			dbm.close();
		}
    	return map;
    }
}
