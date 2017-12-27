package com.dhcc.ts.ipdData.sheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.database.DBManager_HSD;
import com.dhcc.ts.ipdData.model.CurrentModel;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年2月27日 下午4:20:51 
* @类说明：结算单
*/
public class SettlementDao {
	private final static Logger logger=Logger.getLogger(SettlementDao.class);
	private SimpleDateFormat sf = new SimpleDateFormat("dd-MM月-yyHH.mm.ss");
	private SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**  
	* @标题: findSheetList  
	* @描述: TODO查询医保结算单列表
	* @作者 EbaoWeixun
	* @param cardno
	* @param bname
	* @param bdate
	* @param edate
	* @param flag
	* @return
	*/  
	public List<CurrentModel> findSheetList(String cardno,String bname,String bdate,String edate,String flag){
		List<CurrentModel> list=null;
    	DBManager_HSD dbm=new DBManager_HSD();	
    	DBManager_HIS his=new DBManager_HIS();
    	try{
    		//门诊
    	    StringBuffer sql=new StringBuffer("SELECT k.akc190||'@'||k.aae072 id,k.AAE040 rdate,case when k.aka130='21' then '住院' else '门诊' end as dtype ");
    	    sql.append(" ,'医保结算单' as mtype,'' unit,case when akb020='000013' then '河南大学淮河医院' else akb020 end as corp,''as detail,case when akb020='000013' then akb020 else '' end as corp_id ");
    	    sql.append(" FROM kc24 k ");
    	    sql.append(" INNER JOIN ac01 a ON a.aac001=k.aac001 ");
    	    sql.append(" WHERE a.aac002='"+cardno+"' AND k.aka135='1'  ");
    	    if(StringUtil.isNullOrEmpty(flag)){
    	    	sql.append(" AND k.aka130 IN ('11','15','21') ");
    	    }else if("1".equals(flag)){
    	    	sql.append(" AND k.aka130 IN ('11','15') ");
    	    }else{
    	    	sql.append(" AND k.aka130 ='21' ");
    	    }
    	    /*if(!StringUtil.isNullOrEmpty(bname)){
    	    	sql.append(" AND k. like '%"+bname+"%'");
    	    }*/
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND k.AAE040>=to_date('"+bdate+"','yyyy-mm-dd hh24:mi:ss') ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND k.AAE040<=to_date('"+edate+"','yyyy-mm-dd hh24:mi:ss')+1  ");
    	    }
    	    list=dbm.getObjectList(CurrentModel.class, sql.toString());
    	    String sql1="";
    	    if(!list.isEmpty()){

    	    	for(int i=0;i<list.size();i++){
    	    		
    	    		if("000013".equals(list.get(i).getCorp_id())){//其他医院不查询诊断信息

        	    		if("门诊".equals(list.get(i).getDtype())){
        	    			sql1="SELECT distinct h.cmzh id,h.ccfzd zd,f.hos_name corp,r.cysmc dcname,h.mts_zd FROM h_reg r ";
        	    			sql1+=" INNER JOIN h_recipe h ON h.cmzh=r.cmzh";
        	    			sql1+=" LEFT JOIN fac_hospital f ON f.hos_id=r.corp_id ";
        	    			sql1+=" WHERE r.csfzh='"+cardno+"' AND to_date(substr(r.dgh,0,10),'yyyy-mm-dd hh24:mi:ss')=to_date('"+list.get(i).getRdate().substring(0, 10)+"','yyyy-mm-dd hh24:mi:ss')";
        	    		}else{
        	    			sql1="SELECT v.czyh id,v.cmzzdmc zd,f.hos_name corp,v.czyys dcname,v.mts_zd FROM h_visit v ";
        	    			sql1+=" LEFT JOIN fac_hospital f ON f.hos_id=v.corp_id ";
        	    			sql1+=" WHERE v.csfzh='"+cardno+"' ";
        	    			sql1+=" AND to_date(substr(v.dcysj,0,10),'yyyy-mm-dd hh24:mi:ss')=to_date('"+list.get(i).getRdate().substring(0, 10)+"','yyyy-mm-dd hh24:mi:ss')";
        	    		}
        	    		Map<String,Object> map=his.executeQueryHashMap(sql1);
        	    		String zd="";
        	    		String mts_zd="";
        	    		
        	    		if(map.isEmpty()){
        	    			list.get(i).setDetail("");
    	    				//System.out.println("结算单模拟数据: "+list.get(i).getId());
    	    			}else{
    	    				if(map.get("ZD")!=null){
            	    			zd=map.get("ZD").toString();
            	    			list.get(i).setDetail(zd);
        	    				
            	    		}else{
            	    			list.get(i).setDetail("");
            	    		}
    	    				if(map.get("MTS_ZD")!=null){
            	    			mts_zd=map.get("MTS_ZD").toString();
            	    			list.get(i).setMts_zd(mts_zd);
        	    				
            	    		}else{
            	    			list.get(i).setMts_zd("");
            	    		}
    	    				list.get(i).setCorp(map.get("CORP").toString());
    	    				list.get(i).setDcname(map.get("DCNAME").toString());
    	    				
    	    			}
    	    		
    	    		}else{
    	    			//System.out.println("不是当前医院: "+list.get(i).getId());
    	    		}
    	    		list.get(i).setDtype("医保结算单");
    	    	}
    	    }
    	    
    	}catch(Exception e){
    		logger.error("查询结算单出错"+e.getMessage());
    		e.printStackTrace();
    	}finally{
    		dbm.close();
    		his.close();
    	}
    	
    	return list;
	}
	/**  
	* @标题: getSettlement  
	* @描述: TODO查询结算单详细信息
	* @作者 EbaoWeixun
	* @param id
	* @return
	*/  
	public Map<String,Object> getSettlement(String cardno,String lsh,String pjh,String id,String corp_id,String flag){
		Map<String,Object> map=new HashMap<String,Object>();
		DBManager_HSD dbm=new DBManager_HSD();
		Map<String,Object> dmap=new HashMap<String,Object>();
		DBManager_HIS hdbm=new DBManager_HIS();
		try{
			String sql1="";
			//查询结算单信息
			StringBuffer sql=new StringBuffer("SELECT k.akc190 lsh,k.akb020 hos_id,k.aka130 zm,to_char(k.aae040,'yyyy-mm-dd hh24:mi:ss') jsrq,k.aae072 djh,k.AKC336 zyts,k.AKC264 ylfz,k.AKC254 zffy,k.AKC255 grzh,k.AKC280 ylyp,k.AAE011 jbr,to_char(k.AAE036,'yyyy-mm-dd hh24:mi:ss') jbrq,k.AKC260 tczf,to_char(a.aac006,'yyyy-mm-dd hh24:mi:ss') csrq ");
			sql.append(" FROM kc24 k");
			sql.append(" INNER JOIN ac01 a ON a.aac001=k.aac001 ");
			sql.append(" WHERE k.aka135='1'  AND k.aka130 IN ('11','15','21') ");
			sql.append(" AND a.aac002='"+cardno+"'");
			//如果是从结算单入口进入通过票据号和流水号查询
			if(!StringUtil.isNullOrEmpty(lsh)&&!StringUtil.isNullOrEmpty(pjh)){
				sql.append(" AND k.akc190='"+lsh+"' AND k.aae072='"+pjh+"' And k.akb020='"+corp_id+"' ");
				map=dbm.executeQueryHashMap(sql.toString());
				sql1="SELECT * FROM(SELECT  v.cxm,v.cxb,v.cbrnl cnl,v.dcsny sj FROM h_visit v WHERE v.csfzh='"+cardno+"' ) WHERE rownum=1 ";
				dmap=hdbm.executeQueryHashMap(sql1);
				if(!dmap.isEmpty()){
					map.putAll(dmap);
				}else{
					sql1="SELECT * FROM(SELECT r.cxm,r.cxb,r.cnl,r.dcsny sj FROM h_reg r WHERE r.csfzh='"+cardno+"') WHERE rownum=1";
					dmap=hdbm.executeQueryHashMap(sql1);
					if(!dmap.isEmpty()){
						map.putAll(dmap);
					}
				}
				
				
				
				
			}else{//门诊住院信息详细页面进入结算单
				
                String cardnoColl="";
                String idcardSql="";
              
                List<Map<String,Object>> idcardList=new ArrayList<Map<String,Object>>();
				if("1".equals(flag)){//门诊
					
					sql1="SELECT r.cxm,r.cxb,r.cnl,r.dcsny sj,r.dgh FROM h_reg r WHERE r.csfzh='"+cardno+"' AND r.cmzh='"+id+"' AND r.corp_id='"+corp_id+"' ";
					dmap=hdbm.executeQueryHashMap(sql1);
					String date=dmap.get("DGH").toString();
					if(!dmap.isEmpty()){
						if(date.length()>25){
							
							Date ndate=sf.parse(date.substring(0, 19).replace(" ", ""));
							date=sff.format(ndate).substring(0,10);
						}else{
							date=date.substring(0,10);
						}
						sql.append(" AND k.aae040=to_date('"+date+"','yyyy-mm-dd hh24:mi:ss')  AND k.aka130 IN ('11','15')");
						
					}
					map=dbm.executeQueryHashMap(sql.toString());
					if(map.isEmpty()){
						idcardSql="select * from(select distinct csfzh  from h_reg where corp_id='"+corp_id+"' ORDER BY dbms_random.value ) where rownum<20";
						idcardList=hdbm.executeQueryListHashMap(idcardSql);
						if(!idcardList.isEmpty()){
							for(Map<String,Object> cmap:idcardList){
								
								cardnoColl+="'"+cmap.get("CSFZH").toString()+"',";
							}
							
						}
						sql=new StringBuffer("SELECT * FROM (SELECT k.akc190 lsh, k.akb020 hos_id,k.aka130 zm,to_char(k.aae040,'yyyy-mm-dd hh24:mi:ss') jsrq,k.aae072 djh,k.AKC336 zyts,k.AKC264 ylfz,k.AKC254 zffy,k.AKC255 grzh,k.AKC280 ylyp,k.AAE011 jbr,to_char(k.AAE036,'yyyy-mm-dd hh24:mi:ss') jbrq,k.AKC260 tczf,to_char(a.aac006,'yyyy-mm-dd hh24:mi:ss') csrq ");
						sql.append(" FROM kc24 k");
						sql.append(" INNER JOIN ac01 a ON a.aac001=k.aac001 ");
						sql.append(" WHERE k.aka135='1' AND k.akb020='000013' AND k.aka130 IN ('11','15') ");
						//sql.append(" AND a.aac002='"+cardno+"'");
						sql.append(" AND k.aae040>=to_date('"+date+"','yyyy-mm-dd hh24:mi:ss') AND a.aac002 IN ("+cardnoColl.substring(0, cardnoColl.length()-1)+") ORDER BY dbms_random.value) WHERE ROWNUM=1 ");
						map=dbm.executeQueryHashMap(sql.toString());	
						//System.out.println("结算单模拟数据"+id);
					}
				}else{//住院
					sql1="SELECT v.cxm,v.cxb,v.cbrnl cnl,v.dcsny sj,v.drysj FROM h_visit v WHERE v.csfzh='"+cardno+"' AND v.czyh='"+id+"' AND v.corp_id='"+corp_id+"'";
					dmap=hdbm.executeQueryHashMap(sql1);
					String date=dmap.get("DRYSJ").toString().substring(0,10);
					if(!dmap.isEmpty()){
					    
					    sql.append(" AND k.aae040=to_date('"+date+"','yyyy-mm-dd hh24:mi:ss')+k.AKC336  AND k.aka130='21' ");
					   
					}
					map=dbm.executeQueryHashMap(sql.toString());
					if(map.isEmpty()){
						idcardSql="select * from(select distinct csfzh from h_visit where corp_id='"+corp_id+"' ORDER BY dbms_random.value) where rownum<20";
						idcardList=hdbm.executeQueryListHashMap(idcardSql);
						if(!idcardList.isEmpty()){
							for(Map<String,Object> cmap:idcardList){
								
								cardnoColl+="'"+cmap.get("CSFZH").toString()+"',";
							}
							
						}
						sql=new StringBuffer("SELECT * FROM (SELECT k.akc190 lsh, k.akb020 hos_id,k.aka130 zm,to_char(k.aae040,'yyyy-mm-dd hh24:mi:ss') jsrq,k.aae072 djh,k.AKC336 zyts,k.AKC264 ylfz,k.AKC254 zffy,k.AKC255 grzh,k.AKC280 ylyp,k.AAE011 jbr,to_char(k.AAE036,'yyyy-mm-dd hh24:mi:ss') jbrq,k.AKC260 tczf,to_char(a.aac006,'yyyy-mm-dd hh24:mi:ss') csrq ");
						sql.append(" FROM kc24 k");
						sql.append(" INNER JOIN ac01 a ON a.aac001=k.aac001 ");
						sql.append(" WHERE k.aka135='1' AND k.akb020='000013' AND k.aka130='21' ");
						//sql.append(" AND a.aac002='"+cardno+"'");
						 sql.append(" AND k.aae040>=to_date('"+date+"','yyyy-mm-dd hh24:mi:ss')+k.AKC336 AND a.aac002 IN ("+cardnoColl.substring(0,cardnoColl.length()-1)+") ORDER BY dbms_random.value) WHERE ROWNUM=1 ");
						map=dbm.executeQueryHashMap(sql.toString());
						//System.out.println("结算单模拟数据"+id);
					} 
				}
				if(!map.isEmpty()){
					map.putAll(dmap);	
				}
				
			}
			
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询医保结算单"+e.getMessage());
		}finally {
			dbm.close();
			hdbm.close();
		}
		return map;
	}
	/**  
	* @标题: findSettlementDetail  
	* @描述: TODO查询结算单明细
	* @作者 EbaoWeixun
	* @param cardno
	* @param hos_id
	* @param lsh
	* @param djh
	* @param flag
	* @return
	*/  
	public Map<String,Object> findSettlementDetail(String cardno,String hos_id,String lsh,String djh,String flag){
		List<Map<String,Object>> list=null;
		Map<String,Object> map=new HashMap<String,Object>();
		DBManager_HSD dbm=new DBManager_HSD();
		DBManager_HIS hdbm=new DBManager_HIS();
		try{
			StringBuffer sql=new StringBuffer("SELECT k.AKC220 cfh,k.akc223 zxmc,k.akc222 zxbm,k.akc225 dj,k.akc226 sl,k.akc227 jn,h1.item_value sfxm,k.akc515 yybm,k.akc516 yymc,h2.item_value bxlb ");
			sql.append(" FROM kc22 k ");
			sql.append(" LEFT JOIN ac01 a ON a.aac001=k.aac001 ");
			sql.append(" LEFT JOIN hsd_dict h1 ON h1.item_name='AKA063' AND h1.item_code=k.aka063 ");
			sql.append(" LEFT JOIN hsd_dict h2 ON h2.item_name='AKA065' AND h2.item_code=k.aka065 ");
			sql.append(" WHERE k.akb020='"+hos_id+"' AND k.akc190='"+lsh+"' AND k.aae072='"+djh+"' ");
			list=dbm.executeQueryListHashMap(sql.toString());
			String sql1="";
			if("1".equals(flag)){//门诊
				sql1="SELECT * FROM(SELECT k.cxm,k.cxb,k.cnl FROM h_reg k WHERE k.csfzh='"+cardno+"') WHERE rownum=1";
			}else{//
				sql1="SELECT * FROM(SELECT k.cxm,k.cxb,k.cbrnl cnl FROM h_visit k WHERE k.csfzh='"+cardno+"') WHERE rownum=1";
			}
			map=hdbm.executeQueryHashMap(sql1);
			map.put("jList",list );
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询结算单详细信息出错"+e.getMessage());
		}finally{
			dbm.close();
			hdbm.close();
		}
		return map;
	}
}
