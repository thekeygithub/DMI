package com.ts.service.mts.rsrule;

import java.util.List;

import com.ts.entity.mts.RsRuleDetailT;

/**
 * 标化结果处理
 * @ClassName:RsRuleDetailManger
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年11月30日下午3:15:27
 */
public interface RsRuleDetailManger {
	
	public void addRuleDetail(RsRuleDetailT rrd) throws Exception ;
	
	public void editRuleDetail(RsRuleDetailT rrd) throws Exception ;
	
	public void deleteRuleDetail(String RRD_ID) throws Exception ;
	
	public List<RsRuleDetailT> findRsRuleList(RsRuleDetailT rsRuleDetailT) throws Exception ;
	
}
