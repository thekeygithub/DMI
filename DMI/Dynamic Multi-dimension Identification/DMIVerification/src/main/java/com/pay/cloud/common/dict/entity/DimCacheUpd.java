package com.pay.cloud.common.dict.entity;

import java.util.Date;

/**
 * 字典更新状态Model类
 */
public class DimCacheUpd {
	
    private String tabName;

    private Date lastUpdTime;

    private String remark;

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName == null ? null : tabName.trim();
    }

    public Date getLastUpdTime() {
        return lastUpdTime;
    }

    public void setLastUpdTime(Date lastUpdTime) {
        this.lastUpdTime = lastUpdTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}