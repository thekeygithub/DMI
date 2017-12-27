package com.aghit.task.manager.rule;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aghit.task.common.entity.CpaTask;
import com.aghit.task.manager.Manager;

/**
 * 执行存储过程方法
 */
public class ProcedureRuleMgr extends Manager {
	
	private Logger log = Logger.getLogger(ProcedureRuleMgr.class);
	
	private static ProcedureRuleMgr mgr = new ProcedureRuleMgr();

	private ProcedureRuleMgr() {
	}

	public static ProcedureRuleMgr getInstance() {
		return mgr;
	}
	
	
	/**
	 * 调用存储过程
	 * @param start
	 * @param end
	 * @param task
	 * @throws Exception
	 */
	public void updateClmByProcedure(long start,long end, CpaTask task) throws Exception {
	    
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct nvl(p.clm_id,fy.clm_id) clm_id ");
		sql.append("  from mid_inp_clm fy, cpa_handled_patient p ");
		sql.append(" where p.patient_id_number = fy.lgcy_p_code ");
	    sql.append("   and fy.clm_date >= ? ");
		sql.append("   and fy.clm_date <= ? ");
	    sql.append("   and p.seqid >= ? ");
	    sql.append("   and p.seqid <= ? ");
        sql.append(" and fy.data_src_id = ? ");
        
        List<Map<String,Object>> rs = getJdbcService().findForList(sql.toString(), 
        		new Object[]{task.getTaskStart(),task.getTaskEnd(),start,end,task.getDataSrcId()});
        
        if(null == rs || 0 == rs.size()){
        	log.error("外部执行结算单数为0！" + task.getTaskStart() + " " + task.getTaskEnd() + " " + start + " " + end + " " + 
        			task.getDataSrcId() ) ;
        	return ;
        }
        log.info("外部执行结算单数为" +  rs.size());
        //拼接结算单ID
        StringBuffer clmIds = new StringBuffer();
        for (Map<String,Object> m :rs){
        	if (null ==  m.get("clm_id")){
        		continue;
        	}
        	clmIds.append(m.get("clm_id").toString() + ",");
        }
        if (0 < clmIds.length()){
            clmIds.deleteCharAt(clmIds.length() - 1);
        }else{
        	log.error("没有查询到结算单，直接执行结算单数为0！");
            return;
        }
        //调用存储过程
        String[] result = this.getJdbcService().updateProcedure("PR_RULE_LIMITING_FREQUENCY",
        		new String[]{clmIds.toString(),String.valueOf(task.getLogId()),String.valueOf(task.getDataSrcId()),String.valueOf(task.getScenarioId())}, 4);
        if (null == result || 0== result.length ){
        	log.error("外部执行返回结果为空！");
        }else if(4 != result.length){
        	log.error("外部执行返回结果不正确:返回数目错误！"  );
        }else if("0".equals(result[0])){
        	log.info("返回值:"+ result[0] + ";系统错误号:" + result[1] + ";总记录数:" + result[2] + ";错误描述:" + result[3]);
        }else{
        	log.error("返回值:"+ result[0] + ";系统错误号:" + result[1] + ";总记录数:" + result[2] + ";错误描述:" + result[3]);
        }
        
	}
	

}
