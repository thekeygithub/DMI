package com.pay.cloud.common.cache.dict;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.proplat.entity.PpFundPlatform;
import com.pay.cloud.pay.proplat.service.PpFundPlatformService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金平台
 * Created by yacheng.ji on 2017/2/23.
 */
@Service("ppFundPlatformCache")
public class PpFundPlatformCache extends DictCache {

    private static Logger log = Logger.getLogger(PpFundPlatformCache.class);

    @Resource
    private PpFundPlatformService ppFundPlatformServiceImpl;

    private static Map<String, PpFundPlatform> ppFundPlatformMap;

    private static Date cacheDate;

    /**
     * 构造方法
     */
    private PpFundPlatformCache(){
        tableName = "PP_FUND_PLATFORM";
    }

    public static Map<String, PpFundPlatform> getPpFundPlatformMap() {
        if(null == ppFundPlatformMap || 0 == ppFundPlatformMap.size()){
            PpFundPlatformCache cache = new PpFundPlatformCache();
            cache.initDictCache();
        }
        return ppFundPlatformMap;
    }

    /**
     * 获取缓存更新时间
     */
    public Date getCacheDate(){
        return cacheDate;
    }

    public void initDictCache() {
        try{
            List<PpFundPlatform> lst = ppFundPlatformServiceImpl.findPpFundPlatformList();
            if(null == lst){
                log.info("加载字典表缓存" + tableName +"：表数据为空。");
                return;
            }
            ppFundPlatformMap = new HashMap<>();
            for(PpFundPlatform conf : lst){
                ppFundPlatformMap.put(conf.getFundId(), conf);
            }
            cacheDate = new Date();
        } catch (Exception ex) {
            log.error("加载字典表" + tableName + "缓存出错：" + ex.getMessage());
        } finally {
            log.info("已完成加载字典表缓存 "+ tableName);
        }
    }
}
