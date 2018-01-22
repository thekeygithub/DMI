package com.pay.cloud.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.pay.cloud.common.cache.dict.*;
import com.pay.cloud.common.dict.entity.DimBankCardBase;
import com.pay.cloud.common.dict.entity.DimDict;
import com.pay.cloud.common.dict.entity.DimSysConf;
import com.pay.cloud.pay.proplat.entity.PpFundPlatform;
import com.pay.cloud.pay.proplat.entity.PpFundType;
import com.pay.cloud.pay.proplat.entity.PpIntfPara;
import com.pay.cloud.pay.proplat.entity.PpIntfProp;
import com.pay.cloud.pay.trade.entity.DimBusiDetType;

/**
 * 缓存工具类
 */
public class CacheUtils {
	
	/**
	 * 获取系统基础配置表配置项取值(confValue)
	 * @param confId
	 * @return
	 */
	public static String getDimSysConfConfValue(String confId) {
		String confValue = "";
		Map<String,DimSysConf> map = DimSysConfCache.getDimSysConfMap();
		if(map != null){
			DimSysConf conf = map.get(confId);
			if(null != conf){
				confValue = conf.getConfValue() != null ? conf.getConfValue():"";
			}
		}
		return confValue;
	}
	
	/**
	 * 获取系统基础字典表显示名称(dispName)
	 * @param dictTypeId
	 * @param dictId
	 * @return
	 */
	public static String getDimDictDictName(String dictTypeId,String dictId){
		String dictName = "";
		Map<String,Map<String,DimDict>> map1 = DimDictCache.getDimDictMap();
		if(null != map1){
			Map<String,DimDict> map2 = map1.get(dictTypeId);
			if(null != map2){
				DimDict dict = map2.get(dictId);
				if(null != dict){
					dictName = dict.getDispName() != null ? dict.getDispName():"";
				}
			}
		}
		return dictName;
	}
	
	/**
	 * 获取系统基础字典表集合数据<dictId,dispName>
	 * @param dictTypeId
	 * @return
	 */
	public static Map<String,String> getDimDictMap(String dictTypeId){
		Map<String,String> map = new LinkedHashMap<String,String>();
		Map<String,Map<String,DimDict>> map1 = DimDictCache.getDimDictMap();
		if(null != map1){
			Map<String,DimDict> map2 = map1.get(dictTypeId);
			if(null != map2){
				for (Map.Entry<String, DimDict> entry : map2.entrySet()) {
					DimDict dict = entry.getValue();
					if(null != dict){
						map.put(dict.getDictId().toString(), dict.getDispName());
					} 
		        } 
			}
		}
		return map;
	}
	
    /**
     * 获取资金平台交互属性
     * @param fundId
     * @return
     */
	public static PpIntfProp getPpIntfProp(String fundId){
		PpIntfProp prop = new PpIntfProp();
		Map<String,PpIntfProp> map = PpIntfPropCache.getPpIntfPropMap();
		if(null != map){
			prop = map.get(fundId);
		}
		return prop;
	}
	
	 /**
     * 获取资金平台交互属性参数
     * @param paraType
     * @return
     */
	public static String getPpIntfPara(String paraType){
		String val = "";
		Map<String,PpIntfPara> map = PpIntfParaCache.getPpIntfParaMap();
		if(null != map){
			PpIntfPara para = map.get(paraType);
			if(null != para){
				val = para.getVal() != null ? para.getVal():"";
			}
		}
		return val;
	}

	/**
	 * 获取银行卡信息
	 * @param bankCardNum 银行卡号
     * @return 卡信息Object
     */
	public static DimBankCardBase getBankCardInfo(String bankCardNum) {
		Map<String, DimBankCardBase> dimBankCardBaseMap = DimBankCardBaseCache.getDimBankCardBaseMap();
		if(null == dimBankCardBaseMap || dimBankCardBaseMap.size() == 0){
			return null;
		}
		String bankCardBinLength = String.valueOf(getDimSysConfConfValue("BANK_CARD_BIN_LENGTH")).trim();
        if(ValidateUtils.isEmpty(bankCardBinLength)){
            return null;
        }
		String[] binLengthArray = bankCardBinLength.split(",");
        String key;
        String binCode;
        DimBankCardBase dimBankCardBase = null;
        for(String binLength : binLengthArray){
            binCode = bankCardNum.substring(0, Integer.parseInt(binLength));
            key = binCode.concat("_").concat(String.valueOf(bankCardNum.length()));
            dimBankCardBase = dimBankCardBaseMap.get(key);
            if(null != dimBankCardBase){
                break;
            }
        }
        return dimBankCardBase;
	}

	/**
	 * 获取资金平台类型
	 * @param ppFundTypeId 资金平台编码
	 * @return 资金平台Object
     */
	public static PpFundType getPpFundType(String ppFundTypeId) {
		Map<String, PpFundType> ppFundTypeMap = PpFundTypeCache.getPpFundTypeMap();
		if(null == ppFundTypeMap || ppFundTypeMap.size() == 0){
			return null;
		}
		return ppFundTypeMap.get(ppFundTypeId);
	}

	/**
	 * 获取资金平台
	 * @param fundId 资金平台ID
	 * @return 资金平台Object
     */
	public static PpFundPlatform getPpFundPlatform(String fundId) {
		Map<String, PpFundPlatform> ppFundPlatformMap = PpFundPlatformCache.getPpFundPlatformMap();
		if(null == ppFundPlatformMap || ppFundPlatformMap.size() == 0){
			return null;
		}
		return ppFundPlatformMap.get(fundId);
	}

	/**
	 * 根据合作商户ID获取资金平台
	 * @param workActId 合作商户ID
	 * @return 资金平台Object
	 */
	public static PpFundPlatform getPpFundPlatformByWorkActId(String workActId) {
		SortedMap<String, PpFundPlatform> sortedMap = getPpFundPlatformMapByWorkActId(workActId);
		if(null == sortedMap || sortedMap.size() == 0){
			return null;
		}
		return sortedMap.get(sortedMap.firstKey());
	}

	/**
	 * 根据合作商户ID获取资金平台Map
	 * @param workActId 合作商户ID
	 * @return 资金平台Map
     */
	public static SortedMap<String, PpFundPlatform> getPpFundPlatformMapByWorkActId(String workActId) {
		Map<String, PpFundPlatform> ppFundPlatformMap = PpFundPlatformCache.getPpFundPlatformMap();
		if(null == ppFundPlatformMap || ppFundPlatformMap.size() == 0){
			return null;
		}
		SortedMap<String, PpFundPlatform> sortedMap = new TreeMap<>();
		for(PpFundPlatform ppFundPlatform : ppFundPlatformMap.values()){
			if(workActId.equals(String.valueOf(ppFundPlatform.getSpActId()).trim())){
				sortedMap.put(ppFundPlatform.getDispOrder().toString(), ppFundPlatform);
			}
		}
		return sortedMap;
	}

	/**
	 * 获取业务类型字典
	 * @param busiDetTypeId 业务子类型ID
	 * @return 业务类型Object
	 */
	public static DimBusiDetType getDimBusiDetType(String busiDetTypeId) {
		Map<String, DimBusiDetType> dimBusiDetTypeMap = DimBusiDetTypeCache.getDimBusiDetTypeMap();
		if(null == dimBusiDetTypeMap || dimBusiDetTypeMap.size() == 0){
			return null;
		}
		return dimBusiDetTypeMap.get(busiDetTypeId);
	}
}
