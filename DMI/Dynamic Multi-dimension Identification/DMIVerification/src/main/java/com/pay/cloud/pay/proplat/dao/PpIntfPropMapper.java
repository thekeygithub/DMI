package com.pay.cloud.pay.proplat.dao;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpIntfProp;

/**
 * 资金平台交互属性Mapper
 */
public interface PpIntfPropMapper {
	
    int deleteByPrimaryKey(String fundId);

    int insert(PpIntfProp record);

    int insertSelective(PpIntfProp record);

    int updateByPrimaryKeySelective(PpIntfProp record);

    int updateByPrimaryKey(PpIntfProp record);
    
    /**
	 * 通过主键获取资金平台交互属性
	 * @param fundId
	 * @return
	 */
    PpIntfProp findPpIntfPropByPk(String fundId);
    
    /**
     * 查询所有资金平台交互属性
     * @return
     */
    List<PpIntfProp> findPpIntfPropList();
}