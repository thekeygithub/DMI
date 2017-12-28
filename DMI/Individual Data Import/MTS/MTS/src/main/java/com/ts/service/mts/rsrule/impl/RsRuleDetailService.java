package com.ts.service.mts.rsrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.mts.RsRuleDetailT;
import com.ts.service.mts.rsrule.RsRuleDetailManger;

/**
 * 标化结果处理实现类
 * @ClassName:RsRuleDetailService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年11月30日下午3:40:26
 */
@Service("RsRuleDetail")
public class RsRuleDetailService implements RsRuleDetailManger {
	

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	
	/**添加匹配规则详细
	 * @param pd
	 * @throws Exception
	 */
	public void addRuleDetail(RsRuleDetailT rrd) throws Exception {
		dao.save("MtsRsRuleDetail.addRuleDetail", rrd);
	}
	
	/**修改匹配规则详细
	 * @param pd
	 * @throws Exception
	 */
	public void editRuleDetail(RsRuleDetailT rrd) throws Exception {
		dao.update("MtsRsRuleDetail.editRuleDetail", rrd);
	}
	
	/**删除匹配规则详细
	 * @param ROLE_ID
	 * @throws Exception
	 */
	public void deleteRuleDetail(String RRD_ID) throws Exception {
		dao.delete("MtsRsRuleDetail.deleteRuleDetail", RRD_ID);
	}
	
	/**
	 * 查询标化结果的规则
	 */
	@SuppressWarnings("unchecked")	
	public List<RsRuleDetailT> findRsRuleList(RsRuleDetailT rsRuleDetailT) throws Exception {
	
		return (List<RsRuleDetailT>) dao.findForList("MtsRsRuleDetail.findRsRuleList", rsRuleDetailT);
	}
}
