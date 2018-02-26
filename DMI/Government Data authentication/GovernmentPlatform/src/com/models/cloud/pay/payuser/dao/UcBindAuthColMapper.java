package com.models.cloud.pay.payuser.dao;

import java.util.List;

import com.models.cloud.pay.payuser.entity.UcBindAuthCol;
import com.models.cloud.pay.payuser.entity.UcBindAuthColKey;

public interface UcBindAuthColMapper {
    int deleteByPrimaryKey(UcBindAuthColKey key);

    int insert(UcBindAuthCol record);

    int insertSelective(UcBindAuthCol record);

    UcBindAuthCol selectByPrimaryKey(UcBindAuthColKey key);

    int updateByPrimaryKeySelective(UcBindAuthCol record);

    int updateByPrimaryKey(UcBindAuthCol record);
    
    List<UcBindAuthCol> selectAll();
}