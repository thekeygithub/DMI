package com.dhcc.ts.library.solr.timer;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dhcc.ts.database.DBManager_HSD;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年5月12日 下午5:00:53 
* @类说明：
*/
public class BiDetail implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		doSomething();
	}
	
	private static void doSomething(){
		DBManager_HSD dbm=new DBManager_HSD();
		try{
			String sql="delete from ts_bisj where fun_status=1";
			dbm.executeUpdate(sql);
			sql="insert into ts_bisj(y,id,bi_name,sort_status,fun_status) select y,id,name,rownum,1 fun_status "
               +" from (select round(sum(m.AKC264) / 100000, 2) y, m.AKB020 id, p.hos_name name"
               +" from MV_FEE m"
               +" inner join ts_hosp p"
               +" on p.hos_code = m.akb020"
               +" where m.aae040 >= to_date('2016-01', 'yyyy-mm')"
               +" and m.aae040 < add_months(to_date('2016-12', 'yyyy-mm'), 1)"
               +" group by m.AKB020, p.hos_name"
               +" order by sum(m.AKC264) desc)"
               +" where rownum < 4 ";
			dbm.executeUpdate(sql);
			sql="select id from ts_bisj where fun_status=1 order by sort_status";
			List<Map<String,Object>> list=dbm.executeQueryListHashMap(sql);
			sql="delete from ts_bisj where fun_status=2";
			dbm.executeUpdate(sql);
			if(!list.isEmpty()){
				for(Map<String,Object> hos_code:list){
					sql="insert into ts_bisj(id,y,bi_name,sort_status,fun_status) "
                       +" select  '"+hos_code.get("ID").toString()+"',y,name,rownum,2 fun_status"
                       +" from (select round(sum(AKC264) / 100000, 2) y, name_chn name"
                       +" from MV_FEE"
                       +" where AKB020 = '"+hos_code.get("ID").toString()+"'"
                       +" and aae040 >= to_date('2016-01', 'yyyy-mm')"
                       +" and aae040 < add_months(to_date('2016-12', 'yyyy-mm'), 1)"
                       +" group by name_chn"
                       +" order by y desc)"
                       +" where rownum < 6";
					dbm.executeUpdate(sql);
				}
			}
			sql="delete from ts_bisj where fun_status=3";
			dbm.executeUpdate(sql);
			sql="insert into ts_bisj(y,bi_name,sort_status,fun_status)"
               +" select f,yp,rownum,3"
               +" from (select round(sum(AKC264) / 100000, 2) f, akc516 yp"
               +" from mv_fee"
               +" where AAE040 >= to_date('2016-01', 'yyyy-mm')"
               +" and AAE040 <= add_months(to_date('2016-12', 'yyyy-mm'), 1)"
               +" group by akc516"
               +" order by f desc)"
               +" where rownum < 11";
			dbm.executeUpdate(sql);
			sql="delete from ts_bisj where fun_status=4";
			dbm.executeUpdate(sql);
			sql="insert into ts_bisj(a1,y,a2,bi_name,sort_status,fun_status)"
               +" select f,y,d,name_chn,rownum,fun_status from("
               +" select round(sum(AKC264) / 100000, 2) f,"
               +" round(sum(AKC226) / 100000, 2) y,"
               +" nvl(max(AKA076), '支') d,"
               +" name_chn ,4 fun_status"
               +" from MV_FEE"
               +" where AAE040 >= to_date('2016-01', 'yyyy-mm')"
               +" and AAE040 <= add_months(to_date('2016-12', 'yyyy-mm'), 1)"
               +" group by name_chn"
               +" order by f desc)";
			dbm.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("定时更新BI数据出错");
		}finally{
			dbm.close();
		}
	}

}
