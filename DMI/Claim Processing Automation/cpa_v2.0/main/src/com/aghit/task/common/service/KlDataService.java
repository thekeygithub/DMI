package com.aghit.task.common.service;

import java.util.List;
import java.util.Map;

import com.aghit.task.common.entity.klbase.KlDef;

/**
 * 知识服务接口
 */
public interface KlDataService {

	/**
	 * 根据诊断类型直接加载所有对应知识
	 * @param ruleType
	 * @return
	 * @throws Exception
	 */
	public List<KlDef> findKlDefByRuleType(int ruleType) throws Exception;

}
