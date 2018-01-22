package com.pay.cloud.pay.proplat.dao;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpIntfPara;
import com.pay.cloud.pay.proplat.entity.PpIntfParaKey;

public interface PpIntfParaMapper {

  	/**
  	 * 根据逐渐获取平台属性
  	 * @param key
  	 * @return
  	 */
    PpIntfPara selectByPrimaryKey(PpIntfParaKey key);

    /**
     * 根据资金平台ID获取该平台所有属性
     * @return
     */
    List<PpIntfPara> selectPpIntfParaList();
}