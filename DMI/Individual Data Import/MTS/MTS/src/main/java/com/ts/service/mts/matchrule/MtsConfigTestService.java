package com.ts.service.mts.matchrule;

import java.util.List;

import com.ts.entity.mts.MtsConfigTest;
import com.ts.util.PageData;

public interface MtsConfigTestService {
	
	/*
	 * 按批次查询
	 */
	public List<MtsConfigTest> findByPT(String pt) throws Exception;
	/*
	 * 查询所有批次
	 */
	public List<PageData> findPT() throws Exception;	
	
	/*
	 * 添加数据
	 */
	public void addConfigTest(MtsConfigTest mct) throws Exception;
	/*
	 * 修改数据
	 */
	public void editConfigTest(MtsConfigTest mct) throws Exception;
	/*
	 * 删除数据
	 */
	public void deleteConfigTest(int[] PT_IDS) throws Exception;
	
	
}
